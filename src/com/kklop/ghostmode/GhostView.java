package com.kklop.ghostmode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.TextView;

import com.kklop.angmengine.game.event.GameEvent;
import com.kklop.angmengine.game.event.TouchScreenEvent;
import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.Grid;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.map.gridMap.GridMap;
import com.kklop.angmengine.game.sprite.AnimatedSprite;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;
import com.kklop.angmengine.game.sprite.comparator.SpriteComparator;
import com.kklop.angmengine.game.sprite.hitbox.HitBox;
import com.kklop.ghostmode.chars.Background;
import com.kklop.ghostmode.chars.Collectable;
import com.kklop.ghostmode.chars.Enemy;
import com.kklop.ghostmode.chars.Ghost;
import com.kklop.ghostmode.event.FlameEvent;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.event.IceEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.hud.Hud;
import com.kklop.ghostmode.level.CastleLevel;
import com.kklop.ghostmode.level.DesertLevel;
import com.kklop.ghostmode.level.Level;
import com.kklop.ghostmode.level.Level1;
import com.kklop.ghostmode.level.WizardCartLevel;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.sound.SoundService;
import com.kklop.ghostmode.state.GameState;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class GhostView extends SurfaceView implements SurfaceHolder.Callback {
	@SuppressWarnings("unused")
	private final String TAG = getClass().getName();
	public class GhostThread extends Thread {

		private Activity activity;
		
		/**
         * State-tracking constants.
         */
        public static final int STATE_START = -1;
        public static final int STATE_PLAY = 0;
        public static final int STATE_LOSE = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_RUNNING = 3;
        public static final int STATE_WON = 4;
        SoundService soundService;
        
        // music on or off
        private boolean musicOn = false;
        
        public static final int CHANCE_DROP = 3;
        
		
		private static final String TAG = "GhostThread";
		
		/** Indicate whether the surface has been created and is ready to draw */
		private boolean mRun = true;
		
		/** Handle to the surface manager object we interact with */
	    private SurfaceHolder mSurfaceHolder;
	    
	    /** The state of the game. One of READY, RUNNING, PAUSE, LOSE, or WIN */
	    @SuppressWarnings("unused")
		private int mMode;
	    
	    // start, play, running, lose are the states we use
        public int mState;
	    
	    /*
         * Member (state) fields
         */
        /** The drawable to use as the background of the animation canvas */
        private Background mBackgroundImage;
        
        //private Bitmap light;
        
        /** main character bitmap **/
        private Ghost mGhost;
        
        private static final int GHOST_WIDTH = 25;
        private static final int GHOST_HEIGHT = 60;
        
        private static final int GHOST_SPEED = 5;
        
        private int dHeight; // screen height 
        private int dWidth; // screen width
        private Bound bound; // map bounds in point form
        private int dpi;

        // movement targets for main character
        // as positions of everything
        // else is derived based on this
        private float targetX;
        private float targetY;
        
        private int level = 0;
        
        GameState state;
        Hud hud;
        
        /**
         * Current height of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        @SuppressWarnings("unused")
		private int mCanvasHeight = 1;

        /**
         * Current width of the surface/canvas.
         *
         * @see #setSurfaceSize
         */
        @SuppressWarnings("unused")
		private int mCanvasWidth = 1;
        
        /*test sprites*/
        ArrayList<Sprite> sprites;
        ArrayList<Enemy> enemies;
        ArrayList<Collectable> chests;
        ArrayList<AnimatedSprite> trees;
        ArrayList<Sprite> weapons;
        /*end test sprites*/
        
        // game events
        ArrayList<GhostGameEvent> events;
        
        /* event queue */
        protected ConcurrentLinkedQueue<GameEvent> mEventQueue = 
        		new ConcurrentLinkedQueue<GameEvent>();
        
        // game grid
        Grid grid;
        
        // context
        Context context;
        
        // level collection
        List<Level> levels;
        
        private long eventTimeout = 10;
        private long lastEvent = 0l;
        
        private GridMap gridMap;
        
        /**
		 * Get random x on bitmap
		 * @return
		 */
		public int ranX(Bitmap b) {
			return 0 + (int)(Math.random() * 
					((mBackgroundImage.getWidth()-(2*b.getWidth())-5 - 0) + 1));
		}
		
		/**
		 * Get random y on bitmap
		 * @param b
		 * @return
		 */
		public int ranY(Bitmap b) {
			return mGhost.getHeight()+10 + (int)(Math.random() * 
					((mBackgroundImage.getHeight()-b.getHeight()-5 - 
							(mGhost.getHeight()+10)) + 1));
		}
		
		@SuppressWarnings("deprecation")
		@SuppressLint("NewApi")
		public GhostThread(SurfaceHolder surfaceHolder, Context context,
				Handler handler) throws Exception {
			PropertyManager.init(context);
			
			WindowManager wm = (WindowManager) context.
					getSystemService(Context.WINDOW_SERVICE);
			Display display = wm.getDefaultDisplay();
			
			if(android.os.Build.VERSION.SDK_INT >= 13) {
				Point dSize = new Point();
				display.getSize(dSize);
				this.dHeight = dSize.y;
				this.dWidth = dSize.x;
			} else {
				this.dHeight = display.getHeight();
				this.dWidth = display.getWidth();
			}
			
			levels = new ArrayList<Level>();
			levels.add(new Level1(context));
			levels.add(new DesertLevel(context));
			levels.add(new CastleLevel(context));
			levels.add(new WizardCartLevel(context));
			
			if(PropertyManager.getBoolean(Constants.RANDOMIZE_LEVELS)) {
				long seed = System.nanoTime();
				Collections.shuffle(levels, new Random(seed));
			}
			
			mSurfaceHolder = surfaceHolder;
			
			this.context = context;
			resetGame();
		}
		
		public void resetGame() throws Exception {
			Resources res = context.getResources();
			
			Log.i(TAG, "Decoding background object");
			mBackgroundImage = new Background(bound, R.drawable.map_xlg, 0, 0, 60
					, dHeight, dWidth, Constants.BACKGROUND_TYPE, 
					levels.get(level).getMapSize(), res);
			
			// set bound to background image
			bound = new RectBound(new PointF(0,0), 
					new PointF(mBackgroundImage.getWidth(), 
							mBackgroundImage.getHeight()));
			
			//light = BitmapFactory.decodeResource(res, R.drawable.light);
			
			int gPosX = (mBackgroundImage.getWidth()/2 - (GHOST_WIDTH/2));
			int gPosY = (mBackgroundImage.getHeight()/2 - (GHOST_HEIGHT/2));
			
			Bitmap ghostBit = 
					BitmapFactory.decodeResource(res, R.drawable.ghostp);
			mGhost = new Ghost(
						bound,				// map bounds 
						R.drawable.ghostp, 			// bitmap
						gPosX, 				// x position
						gPosY, 				// y position
						ghostBit.getWidth()/5, 		// width of one frame
						ghostBit.getHeight(),  // height of one frame
						12, 				// animation fps
						5, 					// number of animation frames
						60,					// fps sprite can move by
						Constants.HERO_TYPE, // type of sprite
						true,
						res
					);
			mGhost.addHitbox(new HitBox(0, mGhost.getHeight()-((int) Math.round(((int) Math.round(mGhost.getHeight()*0.25))*0.25)), 
					mGhost.getWidth(), mGhost.getHeight()));
			
			// TEST CODE
			targetX = -1;
			targetY = -1;
			// END TEST CODE
			
			sprites = new ArrayList<Sprite>();
			enemies = new ArrayList<Enemy>();
			chests = new ArrayList<Collectable>();
			trees = new ArrayList<AnimatedSprite>();
			weapons = new ArrayList<Sprite>();
			
			sprites.add(mGhost);
			
			// initialize the grid
			try {
				this.grid = new Grid(mBackgroundImage.getWidth(),
						mBackgroundImage.getHeight(), 100);
				this.grid.addSprite(mGhost);
			}
			catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
			// init game events
			events = new ArrayList<GhostGameEvent>();
			
			// init level
			levels.get(level).createLevel(this);
			
			// init game state
			if(state != null) {
				state.resetState(chests.size());
			} else {
				state = new GameState(chests.size(), this.context);
			}
			hud = new Hud(this.dWidth, this.dHeight, this.context, this.bound);
			
			mState = STATE_START;
		}
		
		@Override
		public void run() {
			try {
				while (mRun) {
					if (mState != STATE_PAUSE) {
						if(state.timeOver()) {
							resetGame();
						}
						Canvas c = null;
						try {
							c = mSurfaceHolder.lockCanvas(null);
							synchronized (mSurfaceHolder) {
								if(c != null) {
									handleEvent(c);
									updatePhysics();
									doDraw(c);
								}
							}
						} finally {
							// do this in a finally so that if an exception is thrown
		                    // during the above, we don't leave the Surface in an
		                    // inconsistent state
							if (c != null) {
								mSurfaceHolder.unlockCanvasAndPost(c);
							}
						}
						if(mState == STATE_LOSE) {
							sleep(5000);
							//resetGame();
							state.calculateEndOfLevelScore();
							highScores(state.getScore());
						} else if(mState == STATE_WON) {
							sleep(5000);
							nextLevel();
							resetGame();
						}
					} else {
						SoundHelper.getInstance().stopAll();
						handleEvent(null);
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
				Log.e(TAG, "An exception occurred in run method of " +
						"GhostThread " + e.getMessage());
			}
		}
		
		private void nextLevel() throws GameException {
			state.calculateEndOfLevelScore();
			if(level != (levels.size()-1)) {
				level++;
			} else {
				highScores(state.getScore());
			}
		}
		
		private void highScores(int score) {
			mRun = false;
			((GhostMode) getActivity()).startHighScore(score);
		}
		
		private void handleEvent(Canvas c) throws GameException {
			TouchScreenEvent event = (TouchScreenEvent) 
					this.mEventQueue.poll();
			long currentTime = System.currentTimeMillis();
			if(event != null && ((lastEvent + eventTimeout) < currentTime)) {
				lastEvent = System.currentTimeMillis();
				//this.targetX = event.getEvent().getRawX()+(mGhost.getX()-(dWidth/2));
				//this.targetY = event.getEvent().getRawY()+(mGhost.getY()-(dHeight/2));
				
				// compare against the raw values for hud
				if(Constants.PAUSE_BTN.equals(hud.isButtonPress(
						event.getEvent().getX(), 
						event.getEvent().getY())) && !getGameState().isHelp()) {
					try {
						SoundHelper.getInstance().play(
								getContext().getAssets().openFd(
										"key.mp3"));
					} catch (IOException e) {
						e.printStackTrace();
						throw new GameException(e);
					}
					// pause the game
					if(this.mState == STATE_PAUSE) {
						this.mState = STATE_PLAY;
						this.getGameState().unPause();
						// clear event queue so as not to unpause immediately
						mEventQueue.clear();
						this.eventTimeout = 500;
					} else {
						this.mState = STATE_PAUSE;
						this.getGameState().pause();
						// clear event queue so as not to unpause immediately
						mEventQueue.clear();
						this.eventTimeout = 500;
					}
				} else if (Constants.HELP_BTN.equals(hud.isButtonPress(
						event.getEvent().getX(), 
						event.getEvent().getY())) || 
						Constants.OK_BTN.equals(hud.isButtonPress(
								event.getEvent().getX(), 
								event.getEvent().getY()))) {
					try {
						SoundHelper.getInstance().play(
								getContext().getAssets().openFd(
										"key.mp3"));
					} catch (IOException e) {
						e.printStackTrace();
						throw new GameException(e);
					}
					// pause the game
					if(this.mState == STATE_PAUSE) {
						this.mState = STATE_PLAY;
						this.getGameState().setHelp(false);
						// clear event queue so as not to unpause immediately
						mEventQueue.clear();
						this.eventTimeout = 500;
					} else {
						this.mState = STATE_PAUSE;
						this.getGameState().setHelp(true);
						// clear event queue so as not to unpause immediately
						mEventQueue.clear();
						this.eventTimeout = 500;
					}
				} else if(Constants.FREEZE_BTN.equals(hud.isButtonPress(
						event.getEvent().getX(), 
						event.getEvent().getY()))) {
					try {
						events.add(new IceEvent(
								Constants.FPS,
								GhostGameEvent.EVENT_TYPE.ICE_POWER,
								mGhost,
								bound,
								sprites,
								context,
								grid));
						// play sound
						try {
							SoundHelper.getInstance().play(
									getContext().getAssets().openFd(
											"door.mp3"));
						} catch (IOException e) {
							e.printStackTrace();
							throw new GameException(e);
						}
						mEventQueue.clear();
						this.eventTimeout = 500;
						this.getGameState().decreaseBluePotions();
					} catch (PropertyManagerException e) {
						e.printStackTrace();
						throw new GameException(e);
					}
				} else if(Constants.FLAME_BTN.equals(hud.isButtonPress(
						event.getEvent().getX(), 
						event.getEvent().getY()))) {
					try {
						events.add(new FlameEvent(
								Constants.FPS,
								GhostGameEvent.EVENT_TYPE.FLAME_POWER,
								mGhost,
								bound,
								sprites,
								context,
								grid));
						// play sound
						try {
							SoundHelper.getInstance().play(
									getContext().getAssets().openFd(
											"door.mp3"));
						} catch (IOException e) {
							e.printStackTrace();
							throw new GameException(e);
						}
						mEventQueue.clear();
						this.eventTimeout = 500;
						this.getGameState().decreaseRedPotions();
					} catch (PropertyManagerException e) {
						e.printStackTrace();
						throw new GameException(e);
					}
				} else if(c != null) {
					PointF transPoint = this.translateMove(
							c, 
							event.getEvent().getX(), 
							event.getEvent().getY(), 
							mBackgroundImage.getWidth(), 
							mBackgroundImage.getHeight(), 
							mGhost.getX(), mGhost.getY()
						);
					targetX = transPoint.x;
					targetY = transPoint.y;
					this.eventTimeout = 10;
				}
				//Log.i(TAG, "Set target to (" + targetX + ", " + targetY + ")");
			}
		}
		
		private void updatePhysics() throws Exception {
			/*mBackgroundImage.update(System.currentTimeMillis(),
					targetX, targetY, GHOST_SPEED);*/
			mGhost.update(System.currentTimeMillis(), targetX, targetY,
					GHOST_SPEED, true);
			// update all the enemies and point to ghost
			List<Sprite> deadEnemies = new ArrayList<Sprite>();
			for(Enemy sp : enemies) {
				if(!sp.getDead()) {
					sp.update(System.currentTimeMillis(), mGhost, 
							GHOST_SPEED-2, true);
				} else {
					deadEnemies.add(sp);
				}
			}
			
			for(Sprite sp : deadEnemies) {
				this.grid.removeFromGrid(sp);
				this.sprites.remove(sp);
				this.enemies.remove(sp);
			}
			// update the trees
			for(AnimatedSprite tree : trees) {
				tree.update(System.currentTimeMillis(), -1, -1, 0, false);
			}
			// update chests
			for(Collectable chest : chests) {
				chest.update(System.currentTimeMillis(), -1, -1, 0, false);
			}
			for(Sprite updateSprite : weapons) {
				updateSprite.update(System.currentTimeMillis(), targetX, targetY, GHOST_SPEED, false);
			}
			this.grid.update();
			state.update(System.currentTimeMillis(), events);
			mGhost.handleCollision(this);
			// handle ghost collisions with environment
			if(!PropertyManager.canEnemiesClip()) {
				for(Enemy sp : enemies) {
					sp.handleCollision(this);
				}
			}
			
			// is music off or on?
			updateMusic();
			
			SoundHelper.getInstance().cleanupStopped();
			
			// have we won or lost?
			if(state.isWon()) {
				mState = STATE_WON;
				SoundHelper.getInstance().play(
						context.getAssets().openFd("ghost_1.wav"));
			} else if(state.timeOver()) {
				mState = STATE_LOSE;
				SoundHelper.getInstance().play(
						context.getAssets().openFd("1yell6.wav"));
			}
		}
		
		public void updateMusic() {
			if(musicOn == false) {
				SoundHelper.getInstance().stopMusic();
			} else if(musicOn == true) {
				SoundHelper.getInstance().playMusicIfStopped();
			}
		}
		
		/* Callback invoked when the surface dimensions change. */
        public void setSurfaceSize(int width, int height) {
            // synchronized to make sure these all change atomically
            synchronized (mSurfaceHolder) {
                mCanvasWidth = width;
                mCanvasHeight = height;

                // don't forget to resize the background image
                /*mBackgroundImage = Bitmap.createScaledBitmap(
                        mBackgroundImage, width, height, true);*/
            }
        }
		
		private void doDraw(Canvas canvas) throws GameException {
			//Log.i(TAG,"Drawing background image");
			//canvas.drawBitmap(mBackgroundImage, 0, 0, null);
			/*canvas.translate(-mGhost.getX()+(this.dWidth/2), 
					-mGhost.getY()+(this.dHeight/2));*/
			// draw hud before translating canvas
			Paint paint = new Paint(); 
			if(state.isBeingHit()) {
				paint.setColor(Color.RED);
			} else {
				paint.setColor(Color.GRAY);
			}
			paint.setStyle(Style.FILL); 
			canvas.drawPaint(paint); 
			
			RectBound bound = aimCamera(
					canvas, 
					mGhost.getCenterX(), 
					mGhost.getCenterY(),
					mBackgroundImage.getWidth(),
					mBackgroundImage.getHeight()
				);
			//mBackgroundImage.draw(canvas);
			//drawDebugGrid(canvas);
			gridMap.draw(canvas, bound);
			
			// draw main char
			//Log.i(TAG, "Ghost at " + ghostx + "," + ghosty);
			//canvas.drawBitmap(mGhost, ghostx, ghosty, null);
			//mGhost.draw(canvas);
			// sort by y position
			Collections.sort(sprites, new SpriteComparator());
			// draw events
			for(GhostGameEvent ev : events) {
				ev.drawEvent(canvas);
			}
			// now draw sprites
			for(int i=0;i<sprites.size();i++) {
				sprites.get(i).draw(canvas, bound);
			}
			// not drawing light anymore
			//canvas.drawBitmap(light, 55, 55, null);
			
			hud.draw(canvas, state, levels.get(level));
		}
		
		public void drawDebugGrid(Canvas canvas) {
			int size = 100;
			int gridWidth = this.mBackgroundImage.getWidth()/size;
			int gridHeight = this.mBackgroundImage.getHeight()/size;
			int gridSize = gridWidth * gridHeight;
			HashSet<Integer> cells = null;
			try {
				cells = grid.getCollisionCells(mGhost);
			} catch (GridException e) {
				e.printStackTrace();
			}
			for(int i=0; i<gridSize;i++) {
				int x = i%gridWidth * size;
				int y = Double.valueOf(Math.floor(i/gridWidth)).intValue() * size;
				Paint paint = new Paint();
				paint.setColor(Color.BLACK);
		        paint.setStrokeWidth(3);
		        canvas.drawRect(x, y, x+size, y+size, paint);
		        paint.setStrokeWidth(0);
		        if(cells.contains(i)) {
		        	paint.setColor(Color.YELLOW);
		        } else {
		        	paint.setColor(Color.CYAN);
		        }
		        canvas.drawRect(x+3, y+3, x+size-3, y+size-3, paint );
			}
		}
		
		/**
		 * Translate the canvas (center the camera)
		 * @param canvas
		 * @param x center point
		 * @param y center point
		 * @param mWidth map width
		 * @param mHeight map height
		 */
		private RectBound aimCamera(Canvas canvas, float x, float y,
				int mWidth, int mHeight) {
			float translateX = 0;
			float translateY = 0;
			// only do this if the background is bigger than the canvas
			if(canvas.getWidth() < mWidth && canvas.getHeight() < mHeight) {
				float xTrans = 0;
				float yTrans = 0;
				
				if(x < (dWidth/2)) {
					xTrans = 0;
				}
				else if (x > (mWidth-(dWidth/2))) {
					xTrans = -(mWidth-dWidth);
				}
				else {
					xTrans = -x+(dWidth/2);
				}
				
				if(y < (dHeight/2)) {
					yTrans = 0;
				}
				else if (y > (mHeight-(dHeight/2))) {
					yTrans = -(mHeight-dHeight);
				}
				else {
					yTrans = -y+(dHeight/2);
				}
				
				canvas.translate(xTrans, yTrans);
				translateX = xTrans;
				translateY = yTrans;
			} else {
				canvas.translate((canvas.getWidth()/2)-(mWidth/2),
						(canvas.getHeight()/2)-(mHeight/2));
				translateX = (canvas.getWidth()/2)-(mWidth/2);
				translateY = (canvas.getHeight()/2)-(mHeight/2);
			}
			RectBound bound = new RectBound(new PointF(Math.abs(translateX), Math.abs(translateY)),
					new PointF(Math.abs(translateX) + this.dWidth, 
							Math.abs(translateY) + this.dHeight));
			return bound;
		}
		
		/**
		 * translate a touch event based
		 * on the center position
		 * @param x touch point
		 * @param y touch point
		 * @param mWidth map width
		 * @param mHeight map height
		 * @param gX center point
		 * @param gY center point
		 * @return
		 */
		private PointF translateMove(Canvas canvas, float x, float y,
				int mWidth, int mHeight,
				float cX, float cY) {
			PointF point = new PointF();
			if(canvas.getWidth() < mWidth && canvas.getHeight() < mHeight) {
				float pX = 0;
				float pY = 0;
				//this.targetX = event.getEvent().getRawX()+(mGhost.getX()-(dWidth/2));
				//this.targetY = event.getEvent().getRawY()+(mGhost.getY()-(dHeight/2));
				if(cX < (dWidth/2)) {
					pX = x;
				}
				else if (cX > (mWidth-(dWidth/2))) {
					pX = x + (mWidth-dWidth);
				}
				else {
					//xTrans = -x+(dWidth/2);
					pX = x + (cX - (dWidth/2));
				}
				
				if(cY < (dHeight/2)) {
					pY = y;
				}
				else if (cY > (mHeight-(dHeight/2))) {
					pY = y + (mHeight-dHeight);
				}
				else {
					pY = y + (cY -(dHeight/2));
				}
				
				point.set(pX,pY);
			} else {
				point.set((x-((canvas.getWidth()/2)-(mWidth/2))),
						(y-((canvas.getHeight()/2)-(mHeight/2))));
			}
			if(point.x > (mBackgroundImage.getMaxX()-mGhost.getWidth())) {
				point.x = mBackgroundImage.getMaxX()-mGhost.getWidth();
			} else if(point.x < mBackgroundImage.getX()) {
				point.x = mBackgroundImage.getX();
			} 
			if(point.y < mBackgroundImage.getY()) {
				point.y = mBackgroundImage.getY();
			} else  if(point.y > (mBackgroundImage.getMaxY()-mGhost.getHeight())) {
				point.y = mBackgroundImage.getMaxY()-mGhost.getHeight();
			}
			return point;
		}
		
		/*public void renderMap(Canvas canvas, GhostSprite ghost) {
			// get new center of the screen based on where
			// the ghost moved
			int xCenter = ghost.getX() + (this.GHOST_WIDTH/2);
			int yCenter = ghost.getY() + (this.GHOST_HEIGHT/2);
			
			// add the difference to the bg
			// draw point
			int xDif = this.prGX - xCenter;
			int yDif = this.prGY - yCenter;
			
			this.bgX += xDif;
			this.bgY += yDif;
			
			canvas.drawBitmap(mBackgroundImage, bgX, bgY, null);
			
			this.prGX = xCenter;
			this.prGY = yCenter;
		}*/
		
		public void setMRun(boolean mRun) {
			this.mRun = mRun;
		}
		
		public void addEvent(GameEvent event) {
			this.mEventQueue.add(event);
		}
		
		public synchronized void pause() {
			mState = STATE_PAUSE;
			state.pauseTimer();
			SoundHelper.getInstance().stopAll();
		}
		
		public synchronized void unPause() {
			mState = STATE_PLAY;
		}

		public GameState getGameState() {
			return state;
		}

		public void setGameState(GameState state) {
			this.state = state;
		}

		public Context getContext() {
			return context;
		}

		public void setContext(Context context) {
			this.context = context;
		}

		public Grid getGrid() {
			return grid;
		}

		public void setGrid(Grid grid) {
			this.grid = grid;
		}

		public ArrayList<GhostGameEvent> getEvents() {
			return events;
		}

		public void setEvents(ArrayList<GhostGameEvent> events) {
			this.events = events;
		}

		public ArrayList<Collectable> getChests() {
			return chests;
		}

		public void setChests(ArrayList<Collectable> chests) {
			this.chests = chests;
		}

		public Ghost getmGhost() {
			return mGhost;
		}

		public void setmGhost(Ghost mGhost) {
			this.mGhost = mGhost;
		}

		public Bound getBound() {
			return bound;
		}

		public void setBound(Bound bound) {
			this.bound = bound;
		}

		public ArrayList<Sprite> getSprites() {
			return sprites;
		}

		public void setSprites(ArrayList<Sprite> sprites) {
			this.sprites = sprites;
		}

		public ArrayList<Enemy> getEnemies() {
			return enemies;
		}

		public void setEnemies(ArrayList<Enemy> enemies) {
			this.enemies = enemies;
		}

		public SoundService getSoundService() {
			return soundService;
		}

		public void setSoundService(SoundService soundService) {
			this.soundService = soundService;
		}

		public ArrayList<AnimatedSprite> getTrees() {
			return trees;
		}

		public void setTrees(ArrayList<AnimatedSprite> trees) {
			this.trees = trees;
		}

		public boolean isMusicOn() {
			return musicOn;
		}

		public void setMusicOn(boolean musicOn) {
			this.musicOn = musicOn;
		}

		public GridMap getGridMap() {
			return gridMap;
		}

		public void setGridMap(GridMap gridMap) {
			this.gridMap = gridMap;
		}

		public ArrayList<Sprite> getWeapons() {
			return weapons;
		}

		public void setWeapons(ArrayList<Sprite> updatableSprites) {
			this.weapons = updatableSprites;
		}

		public Activity getActivity() {
			return activity;
		}

		public void setActivity(Activity activity) {
			this.activity = activity;
		}

		public int getDpi() {
			return dpi;
		}

		public void setDpi(int dpi) {
			this.dpi = dpi;
		}
		
	}
	
	/** The thread that actually draws the animation */
    private GhostThread thread;
    
    /** Pointer to the text view to display "Paused.." etc. */
    private TextView mStatusText;
    
	public GhostView(Context context, AttributeSet attrs) throws Exception {
		super(context, attrs);

		SurfaceHolder holder = getHolder();
		holder.addCallback(this);
		
		thread = new GhostThread(holder, context, new Handler() {
			@Override
			public void handleMessage(Message m) {
				mStatusText.setVisibility(m.getData().getInt("viz"));
				mStatusText.setText(m.getData().getString("text"));
			}
		});
	}

	/**
     * Fetches the animation thread corresponding to this GhostModeView.
     *
     * @return the animation thread
     */
    public GhostThread getThread() {
        return thread;
    }
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(thread.mState == GhostThread.STATE_START) {
			thread.start();
		} else {
			thread.unPause();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		//thread.interrupt();
		
	}
	
	/**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus) {
            if (thread != null)
                thread.pause();
        } else if(thread.mState != -1){
        	thread.unPause();
        }
    }
}
