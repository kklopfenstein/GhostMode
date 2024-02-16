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
package com.kklop.angmengine.game.sprite.comparator;

import android.util.Log;

import com.kklop.angmengine.game.sprite.Sprite;

import java.util.Comparator;

public class SpriteComparator implements Comparator<Sprite> {

	public static final String TAG = SpriteComparator.class.getSimpleName();
	
	@Override
	public int compare(Sprite arg0, Sprite arg1) {
		int result = Float.valueOf(arg0.getCompY()).
				compareTo(Float.valueOf(arg1.getCompY()));
		if(result != 1) {
			Log.d(TAG, "Comparing " + arg0.getCompY() + " to " 
					+ arg1.getCompY() +  " = " + result);
		}
		return result;
		
	}

}
