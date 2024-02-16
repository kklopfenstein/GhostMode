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
package com.kklop.angmengine.game.sprite.hitbox;

import com.kklop.angmengine.game.sprite.Sprite;

/**
 * Represents a hitbox for a sprite
 * @author Kevin Klopfenstein
 *
 */
public class HitBox {
	private float x;
	private float y;
	private float xMax;
	private float yMax;
	
	public HitBox(float x, float y, float xMax, float yMax) {
		super();
		this.x = x;
		this.y = y;
		this.xMax = xMax;
		this.yMax = yMax;
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
	public float getxMax() {
		return xMax;
	}
	public void setxMax(float xMax) {
		this.xMax = xMax;
	}
	public float getyMax() {
		return yMax;
	}
	public void setyMax(float yMax) {
		this.yMax = yMax;
	}
	
	// getters based on sprite position
	public float getX(Sprite s) {
		return this.x + s.getX();
	}
	public float getY(Sprite s) {
		return this.y + s.getY();
	}
	public float getxMax(Sprite s) {
		return xMax + s.getX();
	}
	public float getyMax(Sprite s) {
		return yMax + s.getY();
	}
	
}
