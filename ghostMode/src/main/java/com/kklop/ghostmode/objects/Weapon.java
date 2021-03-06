/*******************************************************************************
 * Copyright 2012-2014 Kevin Klopfenstein.
 *
 * This file is part of GhostMode.
 *
 * GhostMode is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * GhostMode is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with GhostMode.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package com.kklop.ghostmode.objects;

import android.content.res.Resources;
import android.graphics.Canvas;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;

/**
 * 
 * @author hal9000
 *
 */
public class Weapon extends StaticSprite {

	private int angle;
	private static final int ROTATION = 15;
	
	protected long anFrameTicker;	// the time of the last frame update
	protected int anFramePeriod;	// milliseconds between each frame (1000/fps)
	
	public Weapon(Bound bound, int bitmap, float x, float y, int fps,
			String type, int anFps, Resources res) throws GameException {
		super(bound, bitmap, x, y, fps, type, res);
		angle = 30;
		anFrameTicker = 0l;
		anFrameTicker = 1000/anFps;
		// disabled flipping during animation
		this.flipBitmap = this.bitmap;
	}
	
	

	@Override
	public void update(Long gameTime, float targetX, float targetY, int speed,
			boolean center) throws GameException {
		super.update(gameTime, targetX, targetY, speed, center);
		if (gameTime > anFrameTicker + anFramePeriod) {
			anFrameTicker = gameTime;
			angle = angle + ROTATION;
			if(angle > 360) {
				angle = 0;
			}
		}
	}



	@Override
	public void draw(Canvas canvas, RectBound bound) {
		if(isInBound(bound)) {
			canvas.save();
			canvas.rotate(angle, getX(), getY());
			super.draw(canvas, bound);
			canvas.restore();
		}
	}

}
