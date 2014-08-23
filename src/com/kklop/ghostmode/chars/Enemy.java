package com.kklop.ghostmode.chars;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;

import com.kklop.angmengine.game.bitmap.cache.BitmapCache;
import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.AnimatedSprite;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.chars.collision.CharacterCollision;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class Enemy extends AnimatedSprite implements CharacterCollision {

	public final String TAG = getClass().getName();
	private float prevX; // the previous x location
	private float prevY; // the previous y location
	
	private boolean knockedOut = false;
	private boolean dead = false;
	
	private long knockoutStart = 0l;
	private long knockedOutLimit = 0l;
	
	private int idleAn;
	private int idleFrames;
	private int attackAn;
	private int attackFrames;
	private int freezeAn;
	private int freezeFrames;
	
	private int health; 
	
	Context context;
	
	public enum ANIMATION_STATE { STOPPED, MOVING };
	private ANIMATION_STATE anState;
	
	/**
	 * Enemy constructor
	 * @param context
	 * @param bitmap
	 * @param x
	 * @param y
	 * @param fps
	 * @param type
	 */
	public Enemy(Context context, Bound bound, int bitmap, float x, float y,
			int width, int height, int fps, int frameCount, int moveFps, 
			String type, boolean loop, int idleAn, int idleFrames, int attackAn, 
			int attackFrames, int freezeAn, int freezeFrames, Resources res) throws GameException {
		super(bound, bitmap, x, y, width, height, fps, frameCount, moveFps, 
				type, loop, res);
		this.context = context;
		this.anState = ANIMATION_STATE.STOPPED;
		this.idleAn = idleAn;
		this.idleFrames = idleFrames;
		this.attackAn = attackAn;
		this.attackFrames = attackFrames;
		this.freezeAn = freezeAn;
		this.freezeFrames = freezeFrames;
		try {
			this.knockedOutLimit = PropertyManager.getEnemyFreezeTime();
			this.health = PropertyManager.getInteger(Constants.ENEMY_HEALTH);
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
	}

	/**
	 * Update and point to target sprite
	 * @param gameTime
	 * @param target
	 * @param speed
	 * @param center
	 */
	public void update(Long gameTime, Sprite target, int speed,
			boolean center) throws GameException {
		
		//  stop being knocked out after a period of time
		if(knockedOut && ((this.knockoutStart + this.knockedOutLimit) < 
				gameTime)) {
			this.knockedOut = false;
			this.knockoutStart = 0l;
		}
		
		if(health <= 0) {
			this.dead = true;
		}
		
		float targetX = -1;
		float targetY = -1;
		if((Math.hypot(Math.abs(getX()-target.getCenterX()), 
				Math.abs(getY()-target.getCenterY())) < 250) &&
				(!knockedOut)) {
			targetX = target.getCenterX();
			targetY = target.getCenterY();
			triggerMovingAnimation();
		} else {
			triggerIdleAnimation();
		}
		update(gameTime, targetX, targetY, speed, center);
	}
	
	
	private void triggerIdleAnimation() throws GameException {
		if(!anState.equals(ANIMATION_STATE.STOPPED)) {
			Resources res = context.getResources();
			int enIdle = 0;
			if(knockedOut) {
				enIdle = freezeAn;
			} else {
				enIdle = idleAn;
			}
			addAnimation(
					enIdle, 
					BitmapCache.getInstance().getBitmapFromCache(res, enIdle).getWidth()/(knockedOut ? freezeFrames : idleFrames), 
					BitmapCache.getInstance().getBitmapFromCache(res, enIdle).getHeight(), 
					5, 
					(knockedOut ? freezeFrames : idleFrames), 
					60, 
					ANIMATION_STATE.STOPPED.toString(), 
					true, 
					true,
					res
				);
			anState = ANIMATION_STATE.STOPPED;
		}
	}
	
	private void triggerMovingAnimation() throws GameException {
		if(!anState.equals(ANIMATION_STATE.MOVING)) {
			Resources res = context.getResources();
			int enMove = attackAn;
			addAnimation(
					enMove, 
					BitmapCache.getInstance().getBitmapFromCache(res, enMove).getWidth()/attackFrames, 
					BitmapCache.getInstance().getBitmapFromCache(res, enMove).getHeight(), 
					5, 
					attackFrames, 
					60, 
					ANIMATION_STATE.STOPPED.toString(), 
					true, 
					true,
					res
				);
			anState = ANIMATION_STATE.MOVING;
		}
	}

	
	@Override
	public void update(Long gameTime, float targetX, float targetY, int speed,
			boolean center) throws GameException {
		// set the previous values
		setPrevX(getX());
		setPrevY(getY());
		// now update
		super.update(gameTime, targetX, targetY, speed, center);
	}
	
	public float getPrevX() {
		return prevX;
	}

	public void setPrevX(float prevX) {
		this.prevX = prevX;
	}

	public float getPrevY() {
		return prevY;
	}

	public void setPrevY(float prevY) {
		this.prevY = prevY;
	}
	
	public boolean getDead() {
		return this.dead;
	}
	
	@Override
	public void handleCollision(GhostThread ghostThread) throws GameException {
		try {
			List<Sprite> collisions = ghostThread.getGrid().getCollisions(
					this);
			if(collisions != null && collisions.size() > 0) {
				for(Sprite s : collisions) {
					if(Constants.ENV_TYPE.equals(s.getType())){
						/* clear target and restore to previous position
						 * we want all other types of sprites to block
						 * the ghost
						 */
						setX(getPrevX());
						setY(getPrevY());
						moveX(System.currentTimeMillis(), targetX, targetY,
								PropertyManager.getEnemySpeed(), true);
						ghostThread.getGrid().updateSprite(this);
						List<Sprite> newCols = ghostThread.getGrid().
								getCollisions(this);
						boolean stillColliding = false;
						for(Sprite newCol : newCols) {
							if(s.equals(newCol)) {
								stillColliding = true;
							}
						}
						if(stillColliding) {
							setX(getPrevX());
							setY(getPrevY());
							moveY(System.currentTimeMillis(), targetX, targetY,
									PropertyManager.getEnemySpeed(), true);
							ghostThread.getGrid().updateSprite(this);
							newCols = ghostThread.getGrid().getCollisions(
									this);
							stillColliding = false;
							for(Sprite newCol : newCols) {
								if(s.equals(newCol)) {
									stillColliding = true;
								}
							}
						}
						if(stillColliding) {
							setX(getPrevX());
							setY(getPrevY());
							targetX = -1;
							targetY = -1;
						}
					} else if(Constants.ICE_TYPE.equals(s.getType())) {
						knockedOut = true;
						knockoutStart = System.currentTimeMillis();
						ghostThread.getGameState().addScoreFromProperty(Constants.ENEMY_HIT_SCORE);
					} else if(Constants.FLAME_TYPE.equals(s.getType())) {
						health = health - PropertyManager.getInteger(Constants.FLAME_DAMAGE);
						ghostThread.getGameState().addScoreFromProperty(Constants.ENEMY_HIT_SCORE);
					}
				}
			}
		} catch(PropertyManagerException e) {
			throw new GameException("PropertyManagerException in method " +
					"handleCollision of Enemy" + e);
		}
		
	}
	
	

}
