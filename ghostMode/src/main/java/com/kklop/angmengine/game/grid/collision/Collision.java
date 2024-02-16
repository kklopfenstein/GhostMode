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
package com.kklop.angmengine.game.grid.collision;

import com.kklop.angmengine.game.sprite.Sprite;

public class Collision {
	Sprite sp1;
	Sprite sp2;
	
	public Collision(Sprite sp1, Sprite sp2) {
		this.sp1 = sp1;
		this.sp2 = sp2;
	}

	public Sprite getSp1() {
		return sp1;
	}

	public void setSp1(Sprite sp1) {
		this.sp1 = sp1;
	}

	public Sprite getSp2() {
		return sp2;
	}

	public void setSp2(Sprite sp2) {
		this.sp2 = sp2;
	}
	
	
}
