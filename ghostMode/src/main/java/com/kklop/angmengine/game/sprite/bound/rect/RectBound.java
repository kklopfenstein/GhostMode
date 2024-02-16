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
package com.kklop.angmengine.game.sprite.bound.rect;

import android.graphics.PointF;

import com.kklop.angmengine.game.sprite.bound.Bound;

public class RectBound extends Bound {

	PointF left;
	PointF right;
	
	public RectBound(PointF left, PointF right) {
		this.left = left;
		this.right = right;
	}
	
	@Override
	public Float inBoundX(Float x, int spriteWidth) {
		Float result = x;
		if((x + spriteWidth) >= right.x) {
			result = right.x - spriteWidth - 1;
		} else if (x <= left.x){
			result = left.x;
		}
		return result;
	}

	@Override
	public Float inBoundY(Float y, int spriteHeight) {
		Float result = y;
		if((y + spriteHeight) >= right.y) {
			result = right.y - spriteHeight - 1;
		} else if (y <= left.y){
			result = left.y;
		}
		return result;
	}

	@Override
	public PointF getBoundPoint() {
		return right;
	}

	public PointF getLeft() {
		return left;
	}

	public PointF getRight() {
		return right;
	}

}
