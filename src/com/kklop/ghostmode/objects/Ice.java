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
