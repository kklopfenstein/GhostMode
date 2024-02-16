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
import android.graphics.Rect;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class SpriteAnimation {

	protected int bmp;    // bitmap
	protected Rect sourceRect;	// the rectangle to be drawn from bitmap
	protected int frameNr;		// number of frames in animation
	protected int currentFrame;	// the current frame
	protected long anFrameTicker;	// the time of the last frame update
	protected int anFramePeriod;	// milliseconds btw each frame (1000/fps)
	protected int spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	protected int spriteHeight;	// the height of the sprite
	protected boolean loop; // should the animation loop
	protected Resources res; // android resources for loading bmps

	public SpriteAnimation(int bmp, Rect sourceRect, int frameNr,
			int currentFrame, long anFrameTicker, int anFramePeriod,
			int spriteWidth, int spriteHeight, boolean loop, Resources res) {
		super();
		this.bmp = bmp;
		this.sourceRect = sourceRect;
		this.frameNr = frameNr;
		this.currentFrame = currentFrame;
		this.anFrameTicker = anFrameTicker;
		this.anFramePeriod = anFramePeriod;
		this.spriteWidth = spriteWidth;
		this.spriteHeight = spriteHeight;
		this.loop = loop;
		this.res = res;
	}
	
}
