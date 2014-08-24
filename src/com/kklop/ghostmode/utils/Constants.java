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
package com.kklop.ghostmode.utils;

public class Constants {
	// sprite types
	public static final String BACKGROUND_TYPE = "background";
	public static final String ENEMY_TYPE = "enemy";
	public static final String HERO_TYPE = "hero";
	public static final String ENV_TYPE = "env";
	public static final String CHEST_TYPE = "chest";
	public static final String HUD_TYPE = "hud";
	public static final String ICE_TYPE = "ice";
	public static final String FLAME_TYPE = "flame";
	
	// property constants
	public static final String FRAME_LENGTH = ".frameLength";
	public static final String ENEMIES = "game.enemies";
	public static final String TREES = "game.trees";
	public static final String GRAVES = "game.graves";
	public static final String GRAVES2 = "game.graves2";
	public static final String CHESTS = "game.chests";
	public static final String MAP_SIZE = "game.map.size";
	public static final String TIMER = "game.timer";
	public static final String CHANCE_DROP = "game.chance.drop";
	public static final String GHOST_SPEED = "game.ghost.speed";
	public static final String ENEMY_SPEED = "game.enemy.speed";
	public static final String CLIP_ENEMY = "game.enemy.clip";
	public static final String GAME_MODE = "game.mode";
	public static final String ENEMY_FREEZE = "game.enemy.freeze";
	public static final String ENEMY_HIT_SCORE = "game.score.enemyHit";
	public static final String TIME_LEFT_MULTIPLYER = "game.score.timeLeftMult";
	public static final String PICK_UP_COIN_SCORE = "game.score.pickUpCoinScore";
	public static final String COINS_TO_WIN = "game.score.coinsToWin";
	public static final String RANDOMIZE_LEVELS = "game.randomizeLevels";
	public static final String ENEMY_HEALTH = "game.enemy.health";
	public static final String FLAME_DAMAGE = "game.flame.damage";
	public static final String BLUE_POTION_COUNT = "game.potion.blue.count";
	public static final String RED_POTION_COUNT = "game.potion.red.count";
	// end property constants
	
	// true and false property file values
	public static final String TRUE = "true";
	public static final String FALSE = "false";
	
	// game modes
	public static final String PROD = "prod";
	public static final String DEBUG = "debug";
	
	public static final int FPS = 60;
	
	// button names
	public static final String PAUSE_BTN = "pause";
	public static final String FREEZE_BTN = "freeze";
	public static final String FLAME_BTN = "flame";
	public static final String HELP_BTN = "help";
	public static final String OK_BTN = "ok";
	
	// level names
	public static final String LEVEL1 = "The Graveyard";
	public static final String DESERT_LEVEL = "Desert";
	public static final String WIZARDS_CART_LEVEL = "Wizard's Cart";
	public static final String CASTLE_LEVEL = "Olaf's Village";
}
