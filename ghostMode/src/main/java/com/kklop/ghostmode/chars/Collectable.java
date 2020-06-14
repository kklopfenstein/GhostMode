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
