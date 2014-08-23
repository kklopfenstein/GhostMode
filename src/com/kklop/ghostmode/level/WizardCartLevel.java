package com.kklop.ghostmode.level;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.hitbox.HitBox;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.R;
import com.kklop.ghostmode.chars.Collectable;
import com.kklop.ghostmode.chars.Enemy;
import com.kklop.ghostmode.chars.EnemyFactory;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

/**
 * 
 * @author hal9000
 *
 */
public class WizardCartLevel extends LevelBase  implements Level {

	public WizardCartLevel(Context context) throws GameException {
		super(context);
	}

	private static String TAG = "Level3";
	public void createLevel(GhostThread ghostThread) 
			throws GameException {
		super.createLevel(ghostThread);
		try {
			Resources res = ghostThread.getContext().getResources();
			
			// init trees
			int numberTrees = 2;
			for(int i=0;i<numberTrees;i++) {
				Bitmap tree = BitmapFactory.
						decodeResource(res, R.drawable.desert_cactus);
				int ranX = ghostThread.ranX(tree);
				int ranY = ghostThread.ranY(tree);
				StaticSprite sp1 = new StaticSprite(
						ghostThread.getBound(), 
						R.drawable.desert_cactus, 
						ranX, 
						ranY, 
						Constants.FPS, 
						Constants.ENV_TYPE,
						res
					);
				sp1.addHitbox(new HitBox(scale(sp1,10), (sp1.getHeight()-((int)Math.round(sp1.getHeight() * .19))), 
						sp1.getWidth()-((int)Math.round(sp1.getWidth()*.01)), sp1.getHeight()-((int)Math.round(.06*sp1.getWidth()))));
				ghostThread.getSprites().add(sp1);
				ghostThread.getGrid().addSprite(sp1);
				//ghostThread.getTrees().add(sp1);
			}
	
//			// init tubleweed
//			int numberGraves = PropertyManager.getNumberGraves();
//			for(int i=0;i<numberGraves;i++) {
//				/* we can share the same bitmap between
//				 * all the sprites because it won't
//				 * need to be manipulated
//				 */
//				Bitmap grave = BitmapFactory.
//						decodeResource(res, R.drawable.tumbleweed);
//				int ranX = ghostThread.ranX(grave);
//				int ranY = ghostThread.ranY(grave);
//				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
//						grave,
//						ranX,ranY,60, Constants.ENV_TYPE);
//				// set hitbox to bottom of gravestone
//				sp2.addHitbox(new HitBox(0, (grave.getHeight()-20), 
//						grave.getWidth(), grave.getHeight()-5));
//				ghostThread.getSprites().add(sp2);
//				ghostThread.getGrid().addSprite(sp2);
//			}
//			
			// init graves2
//			int numberGraves2 = PropertyManager.getNumberGraves2();
//			for(int i=0;i<numberGraves2;i++) {
//				/* we can share the same bitmap between
//				 * all the sprites because it won't
//				 * need to be manipulated
//				 */
//				Bitmap grave = BitmapFactory.
//						decodeResource(res, R.drawable.grave2);
//				int ranX = ghostThread.ranX(grave);
//				int ranY = ghostThread.ranY(grave);
//				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
//						grave,
//						ranX,ranY,60, Constants.ENV_TYPE);
//				// set hitbox to bottom of gravestone
//				sp2.addHitbox(new HitBox(0, (grave.getHeight()-20), 
//						grave.getWidth(), grave.getHeight()-5));
//				ghostThread.getSprites().add(sp2);
//				ghostThread.getGrid().addSprite(sp2);
//			}
//			
			
			// init chests
			ArrayList<Sprite> coins = new ArrayList<Sprite>();
			int numberChests = PropertyManager.getNumberChests();
			for(int i=0;i<numberChests;i++) {
				Bitmap chest = BitmapFactory.
						decodeResource(res, R.drawable.coin);
				int ranX = ghostThread.ranX(chest);
				int ranY = ghostThread.ranY(chest);
				Collectable ch = new Collectable(
						ghostThread.getBound(), 
						R.drawable.coin,
						ranX,
						ranY,
						chest.getWidth()/6,
						chest.getHeight(),
						20,
						6,
						60, 
						Constants.CHEST_TYPE,
						true,
						res,
						Collectable.COLLECTABLE_TYPE.COIN
					);
				ghostThread.getSprites().add(ch);
				ghostThread.getChests().add(ch);
				coins.add(ch);
				try {
					ghostThread.getGrid().addSprite(ch);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
				
			}
			
			ArrayList<Sprite> potions = this.generatePotions(ghostThread);
			
			EnemyFactory factory = new EnemyFactory();
			factory.assembleEnemy(R.drawable.knight, 4, R.drawable.knight_freeze, 1, R.drawable.knight_idle, 1);
			factory.assembleEnemy(R.drawable.pumpkin_attack, 2, R.drawable.pumpkin_freeze, 2, R.drawable.pumpkin_attack, 2);
			factory.assembleEnemy(R.drawable.enemy2_an, 4, R.drawable.enemy2__freeze_an, 4, R.drawable.enemy2_idle_an, 4);
			
			// test code to init 25 enemies
			int numberEnemies = PropertyManager.getEnemies();
			for(int i=0; i<numberEnemies; i++) {
				Enemy enemy = factory.getRandomEnemy(ghostThread);
				ghostThread.getSprites().add(enemy);
				ghostThread.getEnemies().add(enemy);
				try {
					ghostThread.getGrid().addSprite(enemy);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
			}
			
			
			StaticSprite fence = new StaticSprite(ghostThread.getBound(), 
					R.drawable.wizard_pix,
					550,500,60, Constants.ENV_TYPE, res);
			StaticSprite cart = new StaticSprite(ghostThread.getBound(), 
					R.drawable.cart,
					590,500,60, Constants.ENV_TYPE, res);
//			StaticSprite fence2 = new StaticSprite(ghostThread.getBound(), 
//					BitmapFactory.decodeResource(res, R.drawable.fence_horiz2),
//					300+fence.getWidth(),300,60, Constants.ENV_TYPE);
			ghostThread.getSprites().add(fence);
			ghostThread.getSprites().add(cart);
			try {
				ghostThread.getGrid().addSprite(fence);
				ghostThread.getGrid().addSprite(cart);
			} catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
			// welcome message event
			ghostThread.getEvents().add(new GhostGameEvent(Constants.FPS, 
					GhostGameEvent.EVENT_TYPE.
					WELCOME));
			
			SoundHelper.getInstance().setMusic(
					ghostThread.getContext().getAssets().openFd("ghostmusic.mp3"));
			LevelUtil.replaceIfCollision(ghostThread, coins);
			LevelUtil.replaceIfCollision(ghostThread, potions, false);
		} catch(PropertyManagerException e) {
			throw new GameException("PropertyManagerException exception" +
					" in method createLevel " + e);
		} catch(IOException e) {
			throw new GameException(e);
		}
	}

	@Override
	public int getMapSize() throws GameException {
		return gridMap.getWdith();
	}

	@Override
	public String getLevelName() throws GameException {
		return Constants.WIZARDS_CART_LEVEL;
	}
}
