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
package com.kklop.ghostmode.chars;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.util.Log;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.AnimatedSprite;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.ghostmode.GhostView;
import com.kklop.ghostmode.chars.collision.CharacterCollision;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.level.LevelUtil;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class Ghost extends AnimatedSprite implements CharacterCollision {

	public final String TAG = getClass().getName();
	private float prevX; // the previous x location
	private float prevY; // the previous y location
	
	public Ghost(Bound bound, int bitmap, float x, float y, int width,
			int height, int fps, int frameCount, int moveFps, String type, 
			boolean loop, Resources res) throws GameException {
		super(bound, bitmap, x, y, width, height, fps, frameCount, moveFps, 
				type, loop, res);
	}
	
	@Override
	public void update(Long gameTime, float targetX, float targetY, int speed,
			boolean center) throws GameException {
		// set the previous values
		setPrevX(getX());
		setPrevY(getY());
		// now update
		super.update(gameTime, targetX, targetY, speed, center);
	}

	public float getPrevX() {
		return prevX;
	}

	public void setPrevX(float prevX) {
		this.prevX = prevX;
	}

	public float getPrevY() {
		return prevY;
	}

	public void setPrevY(float prevY) {
		this.prevY = prevY;
	}

	public void handleCollision(GhostView.GhostThread ghostThread) 
			throws GameException{
		try {
			List<Sprite> collisions = 
					ghostThread.getGrid().getCollisions(
							ghostThread.getmGhost());
			if(collisions != null && collisions.size() > 0) {
				for(Sprite s : collisions) {
					Log.d(TAG, "Collision with " + s.getType());
					/* if the ghost is not currently being hit and the 
					 * collision is an enemy */
					if(Constants.ENEMY_TYPE.equals(s.getType()) && 
							(!ghostThread.getGameState().isBeingHit())) {
						SoundHelper.getInstance().play(
								ghostThread.getContext().getAssets().openFd(
										"cut_grunt2.wav"));
						ghostThread.getEvents().add(new GhostGameEvent(
								Constants.FPS,
								GhostGameEvent.EVENT_TYPE.
								GHOST_COLLISION));
						ghostThread.getGameState().increaseHits();
						// one in 20 chance to drop chest
						// it's so high because hits happen so frequently
						int dropChest = 0 + (int)(Math.random() * 
								((PropertyManager.getChanceDrop() - 0) + 1));
						boolean droppedChest = false;
						if(dropChest == PropertyManager.getChanceDrop()/2) {
							Collectable chest = null;
							for(Collectable c : ghostThread.getChests()) {
								if(c.isCollected()) {
									chest = c;
									break;
								}
							}
							if(chest != null) {
								chest.setCollected(false);
								ghostThread.getGameState().decreaseCollects();
								chest.setX(ghostThread.ranX(chest.getBitmap()));
								chest.setY(ghostThread.ranY(chest.getBitmap()));
								// to update in collision detection
								ghostThread.getGrid().updateSprite(chest);
								// make sure the coin isn't overlapping with any sprites
								ArrayList<Sprite> coin = new ArrayList<Sprite>();
								coin.add(chest);
								LevelUtil.replaceIfCollision(ghostThread, coin);
								droppedChest = true;
							}
						}
						// add event if dropped chest to
						// display on UI
						if(droppedChest) {
							// play sound
							SoundHelper.getInstance().play(
									ghostThread.getContext().getAssets().
										openFd("death.wav"));
							ghostThread.getEvents().add(new GhostGameEvent(
									Constants.FPS,
									GhostGameEvent.EVENT_TYPE.
									DROP_CHEST)
								);
						}
					} else if(Constants.CHEST_TYPE.equals(s.getType())) {
						Collectable collectable = (Collectable) s;
						if(!collectable.isCollected() && 
								Collectable.COLLECTABLE_TYPE.COIN.equals(collectable.getcType())) {
							ghostThread.getGameState().increaseCollects();
							ghostThread.getGameState().addScoreFromProperty(Constants.PICK_UP_COIN_SCORE);
							collectable.setCollected(true);
							SoundHelper.getInstance().play(
									ghostThread.getContext().getAssets().
										openFd("collect.wav"));
						} else if(!collectable.isCollected() && 
								Collectable.COLLECTABLE_TYPE.BLUE_POTION.equals(collectable.getcType())) {
							ghostThread.getGameState().increaseBluePotions();
							collectable.setCollected(true);
							SoundHelper.getInstance().play(
									ghostThread.getContext().getAssets().
										openFd("collect.wav"));
						} else if(!collectable.isCollected() && 
								Collectable.COLLECTABLE_TYPE.RED_POTION.equals(collectable.getcType())) {
							ghostThread.getGameState().increaseRedPotions();
							collectable.setCollected(true);
							SoundHelper.getInstance().play(
									ghostThread.getContext().getAssets().
										openFd("collect.wav"));
						}
					} else if(Constants.ENV_TYPE.equals(s.getType())){
						/* clear target and restore to previous position
						 * we want all other types of sprites to block
						 * the ghost
						 */
						setX(getPrevX());
						setY(getPrevY());
						moveX(System.currentTimeMillis(), targetX, targetY,
								PropertyManager.getGhostSpeed(), true);
						ghostThread.getGrid().updateSprite(this);
						List<Sprite> newCols = ghostThread.getGrid().
								getCollisions(this);
						boolean stillColliding = false;
						for(Sprite newCol : newCols) {
							if(s.equals(newCol)) {
								stillColliding = true;
							}
						}
						if(stillColliding) {
							setX(getPrevX());
							setY(getPrevY());
							moveY(System.currentTimeMillis(), targetX, targetY,
									PropertyManager.getGhostSpeed(), true);
							ghostThread.getGrid().updateSprite(this);
							newCols = ghostThread.getGrid().getCollisions(
									this);
							stillColliding = false;
							for(Sprite newCol : newCols) {
								if(s.equals(newCol)) {
									stillColliding = true;
								}
							}
						}
						if(stillColliding) {
							setX(getPrevX());
							setY(getPrevY());
							targetX = -1;
							targetY = -1;
						}
					}
				}
			}
		} catch(Exception e) {
			throw new GameException("Game exception in method " +
					"handleCollision of Ghost " + e);
		}
	}
	
}
