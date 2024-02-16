/*******************************************************************************
 * Copyright 2012-2014 Kevin Klopfenstein.
 *
 * This file is part of AnGmEngine.
 *
 * AnGmEngine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AnGmEngine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AnGmEngine.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.kklop.angmengine.game.sprite;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.util.Log;

import com.kklop.angmengine.game.bitmap.cache.BitmapCache;
import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;
import com.kklop.angmengine.game.sprite.hitbox.HitBox;

import java.util.ArrayList;

public abstract class Sprite {
	
	public static final String TAG = Sprite.class.getSimpleName();
	
	protected Bitmap bitmap;
	protected Bitmap normalBitmap;
	protected Bitmap flipBitmap;
	
	String type; // holds the type of the sprite
	
	protected float x;
	protected float y;
	
	protected long frameTicker;	// the time of the last frame update
	protected int framePeriod;	// milliseconds between each frame (1000/fps)

	protected Bound bound; // bound of object on map
	
	public enum SPRITE_STATE { MOVING, STOPPED, TRACK }
	public enum SPRITE_DIRECTION { EAST, WEST }
	protected SPRITE_STATE state;
	protected SPRITE_DIRECTION direction;
	public enum MOVEMENT_AXIS { X, Y, BOTH }
	
	protected double startAngle;
	protected float targetX;
	protected float targetY;
	
	protected ArrayList<HitBox> hitBoxes;
	
	/* used by grid for identification
	 * should not be manually set */
	protected Integer gridId = null;
	
	public void draw(Canvas canvas, RectBound bound) {
		Log.i(TAG, "Draw not implemented for Sprite");
	}
	
	public void draw(Canvas canvas) {
		Log.i(TAG, "Draw not implemented for Sprite");
	}
	
	public Sprite(Bound bound, int bmp, float x, float y, int fps, 
			String type, Resources res) throws GameException {
		this.bitmap = BitmapCache.getInstance().getBitmapFromCache(res, bmp);
		this.normalBitmap = bitmap;
		this.x = x;
		this.y = y;
		this.bound = bound;
		if(bound != null) {
			x = (bound.inBoundX(getX(), getWidth()));
			y = (bound.inBoundY(getY(), getHeight()));
		}
		this.framePeriod = 1000/fps;
		this.frameTicker = 0l;
		// probably should get set by constructor I guess
		targetX = 0;
		targetY = 0;
		state = SPRITE_STATE.STOPPED;
		this.type = type;
		// create the flipped bitmap
		this.flipBitmap = BitmapCache.getInstance().getFlipBitmapFromCache(res, bmp);
	}
	
	public void update(Long gameTime, float targetX, 
			float targetY, int speed, boolean center) throws GameException {
		this.move(gameTime, targetX, targetY, speed, center, 
				MOVEMENT_AXIS.BOTH, false);
	}
	
	public void moveX(Long gameTime, float targetX, 
			float targetY, int speed, boolean center) {
		this.move(gameTime, targetX, targetY, speed, center, 
				MOVEMENT_AXIS.X, true);
	}
	
	public void moveY(Long gameTime, float targetX, 
			float targetY, int speed, boolean center) {
		this.move(gameTime, targetX, targetY, speed, center, 
				MOVEMENT_AXIS.Y, true);
	}
	
	/**
	 * Move the object to certain point over time
	 * at a speed.
	 * @param gameTime
	 * @param targetX
	 * @param targetY
	 * @param speed
	 * @param center
	 */
	protected void move(Long gameTime, float targetX, float targetY, float speed, 
			boolean center, MOVEMENT_AXIS axis, boolean override) {
		
		if(targetX != -1 && targetY != -1) {
			
			if(center) {
				targetX -= getWidth()/2;
				targetY -= getHeight()/2;
			}
			
			double delta_x = (double) (this.x-targetX);
			double delta_y = (double) (this.y-targetY);
			
			if(!(Math.abs(delta_x) < speed && Math.abs(delta_y) < speed)) {
				
				// flip the bitmap if it's moving in one direction or the other
				if(delta_x < 0) {
					direction = SPRITE_DIRECTION.EAST;
					bitmap = normalBitmap;
				} else {
					direction = SPRITE_DIRECTION.WEST;
					bitmap = flipBitmap;
				}
			
				double angle = Math.atan2(delta_y, delta_x);
				if(targetX != this.targetX && targetY != this.targetY) {
					// target has changed so store the original angle
					this.startAngle = angle;
					this.targetX = targetX;
					this.targetY = targetY;
				}
				
				moveTowardAngle(angle, gameTime, speed, override, axis);
			}
			else {
				setX(targetX);
				setY(targetY);
			}
		} else {
			state = SPRITE_STATE.STOPPED;
		}
	}
	
	/**
	 * Move toward a specific angle
	 * @param angle
	 * @param gameTime
	 * @param speed
	 * @param override
	 * @param axis
	 */
	public void moveTowardAngle(double angle, long gameTime, 
			float speed, boolean override, MOVEMENT_AXIS axis) {
		
		state = SPRITE_STATE.MOVING;
		
		/* this means that the motion should increment
		 * every cetain number of frames, not every
		 * time the method is called. This is because
		 * this method could be called faster or
		 * slower than the requires FPS.
		 * 
		 * This check can be overridden, but be careful!
		 */
		if ((gameTime > frameTicker + framePeriod) || override) {
			frameTicker = gameTime;
			float difX = speed*(float)Math.cos(angle);
			float difY = speed*(float)Math.sin(angle);
			
			// if axis is specified, move only on that axis
			if(MOVEMENT_AXIS.X.equals(axis)) {
				x += -difX;
			} else if(MOVEMENT_AXIS.Y.equals(axis)){
				y += -difY;
			} else {
				x += -difX;
				y += -difY;
			}
			
			/* don't move past bounds. if we are
			 * passed the bound, then move axis to
			 * the edge of the bound
			 */
			if(bound != null) {
				x = (bound.inBoundX(getX(), getWidth()));
				y = (bound.inBoundY(getY(), getHeight()));
			}
		}
	}
	
	/**
	 * Detect if two sprites are colliding using hitboxes
	 * @param sprite
	 * @return
	 */
	public boolean collided(Sprite sprite) {
		return collided(sprite, true);
	}
	
	/**
	 * Detect if two sprites are colliding using hitboxes
	 * if applicable. Otherwise use raw x,y values.
	 * This method can be O(n^2) if there are too many hitboxes
	 * so it would be best to limit them.
	 * 
	 * Hitbox usage can be disabled
	 * 
	 * @param sprite
	 * @return
	 */
	public boolean collided(Sprite sprite, boolean useHitBoxes) {
		if(this.hitBoxes != null && this.hitBoxes.size() > 0 && useHitBoxes) {
			for(HitBox box : hitBoxes) {
				if(isBoxCollidedWithSprite(box, sprite)) {
					return true;
				}
			}
			return false;
		} else if(sprite.getHitBoxes() != null && 
				sprite.getHitBoxes().size() > 0 && useHitBoxes) {
			/* this sprite doesn't have hitboxes
			 * but the comparing sprite does.
			 * Create a hit box with this sprites
			 * raw data.
			 */
			return isBoxCollidedWithSprite(new HitBox(0, 0, getWidth(), 
					getHeight()), sprite);
		} else {
			/* no hitboxes for either sprite, so
			 * only compare the raw vals
			 */
			return rawCollided(sprite);
		}
	}
	
	private boolean isBoxCollidedWithSprite(HitBox box, Sprite sprite) {
		if((sprite.getHitBoxes() == null || 
				sprite.getHitBoxes().size() == 0)) {
			/* comparing sprite has no hitboxes
			 * so compare this sprites hitboxes against
			 * the comparing sprites raw values
			 */
			if(isCollided(box.getX(this), box.getY(this),
					box.getxMax(this), box.getyMax(this), 
					sprite.getX(), sprite.getY(), sprite.getMaxX(), 
					sprite.getMaxY())) {
				return true;
			}
		} else {
			/* comparing sprite has hitboxes, so
			 * compare current hitbox against
			 * every one of the sprites hitboxes
			 */
			for(HitBox sBox : sprite.getHitBoxes()) {
				if(isCollided(box.getX(this), box.getY(this),
						box.getxMax(this), box.getyMax(this), 
						sBox.getX(sprite), sBox.getY(sprite), 
						sBox.getxMax(sprite), sBox.getyMax(sprite))) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Check if single point is colliding with sprite
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean pointCollision(float x, float y) {
		return isCollided(getX(), getY(), getMaxX(), getMaxY(), x, y, x, y);
	}
	
	/**
	 * Collision based on bitmap boundaries
	 * @return
	 */
	private boolean rawCollided(Sprite sprite) {
		return isCollided(getX(), getY(), getMaxX(), getMaxY(), sprite.getX(), 
				sprite.getY(), sprite.getMaxX(), sprite.getMaxY());
	}
	
	public static boolean isCollided(float x, float y, float maxX, float maxY, 
			float x2, float y2, float maxX2, float maxY2) {
		if(maxX < x2) return false;
		if(x > maxX2) return false;
		if(maxY < y2) return false;
		if(y > maxY2) return false;
		return true;
	}
	
	/**
	 * Is sprite collided with bound
	 * @param b
	 * @return
	 */
	public boolean isInBound(RectBound b) {
		return isCollided(
				getX(), 
				getY(), 
				getMaxX(), 
				getMaxY(), 
				b.getLeft().x, 
				b.getLeft().y, 
				b.getRight().x, 
				b.getRight().y
			);
	}
	
	/**
	 * Add hitbox to sprite
	 * @param box
	 * @throws GameException
	 */
	public void addHitbox(HitBox box) throws GameException {
		if(hitBoxes == null) {
			hitBoxes = new ArrayList<HitBox>();
		}
		hitBoxes.add(box);
	}
	

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	public int getWidth() {
		int result = 0;
		if(bitmap != null) {
			result = bitmap.getWidth();
		}
		return result;
	}
	
	public int getHeight() {
		int result = 0;
		if(bitmap != null) {
			result = bitmap.getHeight();
		}
		return result;
	}
	
	public PointF getTopLeftCrnr() {
		return new PointF(getX(), getY());
	}
	
	public PointF getTopRightCrnr() {
		PointF p = null;
		if(bitmap != null) {
			p = new PointF(getX(), getY() + bitmap.getWidth());
		}
		return p;
	}
	
	public PointF getBotLeftCrnr() {
		PointF p = null;
		if(bitmap != null) {
			p = new PointF(getX(), getY()+ bitmap.getHeight());
		}
		return p;
	}
	
	public PointF getBotRightCrnr() {
		PointF p = null;
		if(bitmap != null) {
			p = new PointF(getX() + bitmap.getWidth(), 
					getY() + bitmap.getHeight());
		}
		return p;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public float getMaxX() {
		return x + bitmap.getWidth();
	}
	
	public float getMaxY() {
		return y + bitmap.getHeight();
	}
	
	public float getCompY() {
		return y + bitmap.getHeight();
	}

	public Integer getGridId() {
		return gridId;
	}

	public void setGridId(Integer gridId) {
		this.gridId = gridId;
	}

	public SPRITE_STATE getState() {
		return state;
	}

	public void setState(SPRITE_STATE state) {
		this.state = state;
	}
	
	public float getCenterX() {
		return x + bitmap.getWidth()/2;
	}
	
	public float getCenterY() {
		return y + bitmap.getHeight()/2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<HitBox> getHitBoxes() {
		return hitBoxes;
	}
}
