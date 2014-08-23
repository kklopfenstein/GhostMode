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
