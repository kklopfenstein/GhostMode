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

import com.kklop.angmengine.game.sprite.MapSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;

public class Background extends MapSprite {

	private int size = 3000;
	public Background(Bound bound, int bitmap, int x, int y, int fps,
			int screenHeight, int screenWidth, String type, int size, Resources res) throws Exception {
		super(bound, bitmap, x, y, fps, screenHeight, screenWidth, type, res);
		this.size = size;//PropertyManager.getMapSize();
	}

	@Override
	public int getHeight() {
		return size;
	}

	@Override
	public int getWidth() {
		return size;
	}

	@Override
	public float getMaxX() {
		return size;
	}

	@Override
	public float getMaxY() {
		return size;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}
	
	

}
