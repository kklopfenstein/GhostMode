package com.kklop.ghostmode.chars;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.AnimatedSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;

public class Collectable extends AnimatedSprite {

	public enum COLLECTABLE_TYPE { COIN, RED_POTION, BLUE_POTION };
	private COLLECTABLE_TYPE cType;
	
	public Collectable(Bound bound, int bitmap, float x, float y, int width,
			int height, int fps, int frameCount, int moveFps, String type,
			boolean loop, Resources res, COLLECTABLE_TYPE cType) throws GameException {
		super(bound, bitmap, x, y, width, height, fps, frameCount, moveFps, 
				type, loop, res);
		this.cType = cType; 
	}

	private boolean collected = false;
	
	@Override
	public void draw(Canvas canvas, RectBound bound) {
		if(!collected) {
			super.draw(canvas, bound);
		}
	}

	public boolean isCollected() {
		return collected;
	}

	public void setCollected(boolean collected) {
		this.collected = collected;
	}

	public COLLECTABLE_TYPE getcType() {
		return cType;
	}

}
