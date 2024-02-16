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
package com.kklop.angmengine.game;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.Grid;
import com.kklop.angmengine.game.sprite.Sprite;

import java.util.ArrayList;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class Game {
	ArrayList<Sprite> sprites;
	Grid grid;
	
	/**
	 * Initialize game with grid
	 * @param width
	 * @param height
	 * @param size
	 * @throws GameException
	 */
	public Game(int width, int height, int size) throws GameException {
		grid = new Grid(width, height, size);
	}
}
