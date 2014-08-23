package com.kklop.ghostmode.objects;

import android.content.res.Resources;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;

public class Ice extends StaticSprite {

	private long angle;
	
	public Ice(Bound bound, int bitmap, float x, float y, int fps, 
			String type, Resources res) throws GameException {
		super(bound, bitmap, x, y, fps, type, res);
	}

	@Override
	protected void move(Long gameTime, float targetX, float targetY,
			float speed, boolean center, MOVEMENT_AXIS axis, boolean override) {
		super.move(gameTime, targetX, targetY, speed, center, axis, override);
	}

	public long getAngle() {
		return angle;
	}

	public void setAngle(long angle) {
		this.angle = angle;
	}

}
