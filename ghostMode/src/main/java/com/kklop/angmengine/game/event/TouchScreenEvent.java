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
import android.view.MotionEvent;

public class TouchScreenEvent implements GameEvent {
	MotionEvent event;
	
	public TouchScreenEvent(MotionEvent event) {
		this.event = event;
	}

	public MotionEvent getEvent() {
		return event;
	}

	public void setEvent(MotionEvent event) {
		this.event = event;
	}

	@Override
	public void continueEvent(long gameTime) {
		
	}

	@Override
	public boolean isActive() {
		return false;
	}

	@Override
	public void startEvent() {
		
	}

	@Override
	public void drawEvent(Canvas canvas) {
		
	}

	@Override
	public void stopEvent() {
		
	}
	
}
