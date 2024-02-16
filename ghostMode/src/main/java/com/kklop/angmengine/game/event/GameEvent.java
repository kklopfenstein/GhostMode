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
package com.kklop.angmengine.game.event;

import android.graphics.Canvas;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public interface GameEvent {
	/**
	 * 
	 * @param gameTime
	 */
	public void continueEvent(long gameTime);
	
	/**
	 * 
	 * @return
	 */
	public boolean isActive();
	
	/**
	 * 
	 */
	public void startEvent();
	
	/**
	 * 
	 * @param canvas
	 */
	public void drawEvent(Canvas canvas);
	
	/**
	 * 
	 */
	public void stopEvent();
}
