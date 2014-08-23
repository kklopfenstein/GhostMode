package com.kklop.ghostmode.chars.collision;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.ghostmode.GhostView;

public interface CharacterCollision {
	/**
	 * Handle collision
	 * @param ghostThread
	 */
	public void handleCollision(GhostView.GhostThread ghostThread)
		throws GameException;
}
