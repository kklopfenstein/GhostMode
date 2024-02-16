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
import android.graphics.Canvas;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;

public class StaticSprite extends Sprite {
	
	@SuppressWarnings("unused")
	private static final String TAG = StaticSprite.class.getSimpleName();
	
	public StaticSprite(Bound bound, int bmp, float x, float y, int fps, 
			String type, Resources res) throws GameException {
		super(bound, bmp, x, y, fps, type, res);
	}
	
	public void update(Long gameTime, float targetX, float targetY, int speed, 
			boolean center) throws GameException {
		super.update(gameTime, targetX, targetY, speed, center);
	}
	
	@Override
	public void draw(Canvas canvas, RectBound bound) {
		if(isInBound(bound)) {
			canvas.drawBitmap(bitmap, getX(), getY(), null);
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawBitmap(bitmap, getX(), getY(), null);
	}
}
