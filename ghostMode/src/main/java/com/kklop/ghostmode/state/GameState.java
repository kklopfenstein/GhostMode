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
package com.kklop.ghostmode.state;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class GameState {
	@SuppressWarnings("unused")
	private final String TAG = this.getClass().getName();
	
	// playing sounds
	Context context;
	
	private int hits = 0;
	private int collects = 0;
	private int totalChests = 0;
	private int bluePotions = 0;
	private int redPotions = 0;
	
	// state variables
	private boolean beingHit = false;
	private boolean dropChest = false;
	@SuppressWarnings("unused")
	private boolean gameWon = false;
	private boolean welcome = false;
	private boolean paused = false;
	private boolean help = false;
	private boolean dontUpdateTimer = false;
	
	// timer
	private long lastTime = 0l;
	private long timer = 0l;
	private long timeOut = 0l;
	
	// used for timer sounds
	int timeSound = 0;
	
	// score
	int score = 0;
	int levelScore = 0;
	
	public GameState(int totalChests, Context context) throws GameException { 
		this.totalChests = totalChests;
		this.timeSound = 10;
		this.context = context;
		try {
			timeOut = Long.decode(PropertyManager.getProperty(
					Constants.TIMER));
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
	}
	
	public void resetState(int totalChests) {
		this.hits = 0;
		this.collects = 0;
		this.totalChests = totalChests;
		this.beingHit = false;
		this.dropChest = false;
		this.gameWon = false;
		this.welcome = false;
		this.lastTime = 0l;
		this.timer = 0l;
		this.timeSound = 10;
		this.levelScore = 0;
		this.bluePotions = 0;
		this.redPotions = 0;
	}

	public void calculateEndOfLevelScore() throws GameException {
		try {
			int timeLeftMultiplyer = PropertyManager.getTimeLeftMultiplyer();
			this.score = this.score + this.levelScore + ((Long.valueOf(getTimer()).intValue()+1) * timeLeftMultiplyer);
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
	}
	
	public void addToScore(int score) {
		this.levelScore = this.levelScore + score; 
	}
	
	public void addScoreFromProperty(String propertyName) throws GameException {
		try {
			int value = PropertyManager.getInteger(propertyName);
			this.levelScore = this.levelScore + value;
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
	}
	
	/**
	 * Reset states
	 */
	private void resetStates() {
		this.beingHit = false;
		this.dropChest = false;
		this.welcome = false;
	}
	
	/**
	 * Update the game state based on events in progress
	 * @param events
	 */
	public void update(long sysTime, ArrayList<GhostGameEvent> events) throws GameException {
		if(!dontUpdateTimer) {
			updateTimer();
		} else {
			lastTime = System.currentTimeMillis();
			if(!paused && !help) {
				dontUpdateTimer = false;
			}
		}
		// reset all the states
		resetStates();
		// trigger invulnerability event after being hit
		boolean triggerInvul = false;
		ArrayList<GhostGameEvent> removalList = new ArrayList<GhostGameEvent>();
		for(GhostGameEvent event : events) {
			event.continueEvent(sysTime);
			// if it's active then set state appropriately
			if(event.isActive()) {
				switch (event.getType()) {
					case GHOST_COLLISION:
						beingHit = true;
						break;
					case GHOST_INVUL:
						// ghost can't be hit while invul
						beingHit = false;
						break;
					case DROP_CHEST:
						dropChest = true;
						break;
					case GAME_WON:
						gameWon = true;
						break;
					case WELCOME:
						welcome = true;
						break;
					default:
						break;
				}
			} else {
				removalList.add(event);
			}
		}
		for(GhostGameEvent event : removalList) {
			if(!event.isActive()) {
				switch (event.getType()) {
					case GHOST_COLLISION:
						triggerInvul = true;
						break;
					default:
						break;
				}
				event.stopEvent();
				events.remove(event);
			}
		}
		if(triggerInvul) {
			try {
				events.add(new GhostGameEvent(Constants.FPS, GhostGameEvent.EVENT_TYPE.GHOST_INVUL));
			} catch(PropertyManagerException e) {
				throw new GameException(e);
			}
		}
	}
	
	private void updateTimer() {
		long delta = 0l;
		if(lastTime != 0l) {
			delta = System.currentTimeMillis() - lastTime;
			timer += delta;
		}
		
		// play sounds for timer
		int second = Double.valueOf(Math.floor((timeOut-timer)/1000)).intValue();
		if(timeSound == second) {
			playTimeoutSound();
			timeSound--;
		}
		
		lastTime = System.currentTimeMillis();
	}
	
	public void playTimeoutSound() {
		try {
			SoundHelper.getInstance().play(
					context.getAssets().openFd("timeout.wav"));
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean timeOver() {
		if(timer > timeOut) {
			return true;
		}
		return false;
	}
	
	public void pauseTimer() {
		lastTime = 0l;
	}
	
	public boolean isWon() throws PropertyManagerException {
		return this.getCollects() == PropertyManager.getInteger(Constants.COINS_TO_WIN);
	}
	
	public boolean isBeingHit() {
		return beingHit;
	}

	public int getHits() {
		return hits;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}
	
	public void increaseHits() {
		hits++;
	}

	public int getCollects() {
		return collects;
	}

	public void setCollects(int collects) {
		this.collects = collects;
	}
	
	public void increaseCollects() {
		collects++;
	}
	
	public void decreaseCollects() {
		collects = collects - 1;
	}

	public int getTotalChests() {
		return totalChests;
	}

	public void setTotalChests(int totalChests) {
		this.totalChests = totalChests;
	}

	public boolean isDropChest() {
		return dropChest;
	}

	public long getTimer() {
		return timeOut - timer;
	}

	public boolean isWelcome() {
		return welcome;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getLevelScore() {
		return levelScore;
	}

	public void setLevelScore(int levelScore) {
		this.levelScore = levelScore;
	}

	public int getBluePotions() {
		return bluePotions;
	}

	public void setBluePotions(int bluePotions) {
		this.bluePotions = bluePotions;
	}

	public int getRedPotions() {
		return redPotions;
	}

	public void setRedPotions(int redPotions) {
		this.redPotions = redPotions;
	}
	
	public void increaseRedPotions() {
		this.redPotions = this.redPotions + 1;
	}
	
	public void decreaseRedPotions() {
		this.redPotions = this.redPotions - 1;
	}
	
	public void increaseBluePotions() {
		this.bluePotions = this.bluePotions + 1;
	}
	
	public void decreaseBluePotions() {
		this.bluePotions = this.bluePotions - 1;
	}
	
	public void pause() {
		this.dontUpdateTimer = true;
		this.paused = true;
	}
	
	public void unPause() {
		this.paused = false;
	}

	public boolean isPaused() {
		return paused;
	}

	public boolean isHelp() {
		return help;
	}

	public void setHelp(boolean help) {
		if(help == true) {
			this.dontUpdateTimer = true;
		}
		this.help = help;
	}
	
	
}
