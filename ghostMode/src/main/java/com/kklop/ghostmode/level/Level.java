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
package com.kklop.ghostmode.level;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.state.GameState;

public interface Level {
	public void createLevel(GhostThread ghostThread) 
			throws GameException;
	public int getMapSize()
			throws GameException;
	public String getLevelName()
			throws GameException;
	public boolean isWon(GameState state)
			throws GameException;
}
