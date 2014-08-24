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

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.kklop.angmengine.game.bitmap.cache.BitmapCache;
import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.sprite.AnimatedSprite;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.hitbox.HitBox;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.R;
import com.kklop.ghostmode.chars.Collectable;
import com.kklop.ghostmode.chars.Enemy;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class Level1 extends LevelBase implements Level {
	
	public Level1(Context context) throws GameException {
		super(context);
	}

	private static String TAG = "Level1";
	
	public void createLevel(GhostThread ghostThread) 
			throws GameException {
		super.createLevel(ghostThread);
		try {
			Resources res = ghostThread.getContext().getResources();
			
			// init trees
			int numberTrees = PropertyManager.getNumberTrees();
			for(int i=0;i<numberTrees;i++) {
				Bitmap tree = BitmapCache.getInstance().getBitmapFromCache(res, R.drawable.tree_an);
				int ranX = ghostThread.ranX(tree);
				int ranY = ghostThread.ranY(tree);
				AnimatedSprite sp1 = new AnimatedSprite(
							ghostThread.getBound(), 
							R.drawable.tree_an,
							ranX,
							ranY,
							tree.getWidth()/6,
							tree.getHeight(),
							6,
							6,
							60, 
							Constants.ENV_TYPE,
							true,
							res
						);
				sp1.addHitbox(new HitBox(scale(sp1,10), (sp1.getHeight()-scale(sp1,20)), 
						sp1.getWidth()-scale(sp1,10), sp1.getHeight()-scale(sp1,5)));
				ghostThread.getSprites().add(sp1);
				ghostThread.getGrid().addSprite(sp1);
				ghostThread.getTrees().add(sp1);
			}
			
			// init trees
			int numberTorches = 15;
			for(int i=0;i<numberTorches;i++) {
				Bitmap tree = BitmapFactory.
						decodeResource(res, R.drawable.torch);
				int ranX = ghostThread.ranX(tree);
				int ranY = ghostThread.ranY(tree);
				AnimatedSprite sp1 = new AnimatedSprite(
							ghostThread.getBound(), 
							R.drawable.torch,
							ranX,
							ranY,
							tree.getWidth()/4,
							tree.getHeight(),
							6,
							4,
							60, 
							Constants.ENV_TYPE,
							true,
							res
						);
				sp1.addHitbox(new HitBox(scale(sp1,10), (sp1.getHeight()-scale(sp1,20)), 
						sp1.getWidth()-scale(sp1,10), sp1.getHeight()-scale(sp1,5)));
				ghostThread.getSprites().add(sp1);
				ghostThread.getGrid().addSprite(sp1);
				ghostThread.getTrees().add(sp1);
			}
	
			// init graves
			int numberGraves = PropertyManager.getNumberGraves();
			for(int i=0;i<numberGraves;i++) {
				/* we can share the same bitmap between
				 * all the sprites because it won't
				 * need to be manipulated
				 */
				Bitmap grave = BitmapFactory.
						decodeResource(res, R.drawable.grave);
				int ranX = ghostThread.ranX(grave);
				int ranY = ghostThread.ranY(grave);
				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
						R.drawable.grave,
						ranX,ranY,60, Constants.ENV_TYPE, res);
				// set hitbox to bottom of gravestone
				sp2.addHitbox(new HitBox(0, (grave.getHeight()-scale(sp2,20)), 
						grave.getWidth(), grave.getHeight()-scale(sp2,5)));
				ghostThread.getSprites().add(sp2);
				ghostThread.getGrid().addSprite(sp2);
			}
			
			// init graves2
			int numberGraves2 = PropertyManager.getNumberGraves2();
			for(int i=0;i<numberGraves2;i++) {
				/* we can share the same bitmap between
				 * all the sprites because it won't
				 * need to be manipulated
				 */
				Bitmap grave = BitmapFactory.
						decodeResource(res, R.drawable.grave2);
				int ranX = ghostThread.ranX(grave);
				int ranY = ghostThread.ranY(grave);
				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
						R.drawable.grave2,
						ranX,ranY,60, Constants.ENV_TYPE, res);
				// set hitbox to bottom of gravestone
				sp2.addHitbox(new HitBox(0, (grave.getHeight()-scale(sp2,20)), 
						grave.getWidth(), grave.getHeight()-scale(sp2,5)));
				ghostThread.getSprites().add(sp2);
				ghostThread.getGrid().addSprite(sp2);
			}
			
			
			// init chests
			ArrayList<Sprite> coins = new ArrayList<Sprite>();
			int numberChests = PropertyManager.getNumberChests();
			for(int i=0;i<numberChests;i++) {
				Bitmap chest = BitmapFactory.
						decodeResource(res, R.drawable.coin);
				int ranX = ghostThread.ranX(chest);
				int ranY = ghostThread.ranY(chest);
				Collectable ch = new Collectable(
						ghostThread.getBound(), 
						R.drawable.coin,
						ranX,
						ranY,
						chest.getWidth()/6,
						chest.getHeight(),
						20,
						6,
						60, 
						Constants.CHEST_TYPE,
						true,
						res,
						Collectable.COLLECTABLE_TYPE.COIN
					);
				ghostThread.getSprites().add(ch);
				ghostThread.getChests().add(ch);
				coins.add((Collectable) ch);
				try {
					ghostThread.getGrid().addSprite(ch);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
				
			}

			ArrayList<Sprite> potions = this.generatePotions(ghostThread);
			
			// test code to init 25 enemies
			int numberEnemies = PropertyManager.getEnemies();
			for(int i=0; i<numberEnemies; i++) {
				Bitmap en = BitmapFactory.
						decodeResource(res, R.drawable.enemy2_idle_an);
				int ranX = ghostThread.ranX(en);
				int ranY = ghostThread.ranY(en);
				Enemy enemy = new Enemy(
						ghostThread.getContext(),
						ghostThread.getBound(), 
						R.drawable.enemy2_idle_an,
						ranX,
						ranY,
						en.getWidth()/4,
						en.getHeight(),
						5,
						4,
						60, 
						Constants.ENEMY_TYPE,
						true,
						R.drawable.enemy2_idle_an,
						4,
						R.drawable.enemy2_an,
						4,
						R.drawable.enemy2__freeze_an,
						4,
						res
					);
				ghostThread.getSprites().add(enemy);
				ghostThread.getEnemies().add(enemy);
				try {
					ghostThread.getGrid().addSprite(enemy);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
			}
			
			
			StaticSprite fence = new StaticSprite(ghostThread.getBound(), 
					R.drawable.fence_horiz2,
					300,300,60, Constants.ENV_TYPE, res);
			StaticSprite fence2 = new StaticSprite(ghostThread.getBound(), 
					R.drawable.fence_horiz2,
					300+fence.getWidth(),300,60, Constants.ENV_TYPE, res);
			ghostThread.getSprites().add(fence);
			ghostThread.getSprites().add(fence2);
			try {
				ghostThread.getGrid().addSprite(fence);
				ghostThread.getGrid().addSprite(fence2);
			} catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
//			Weapon weapon = new Weapon(ghostThread.getBound(), BitmapFactory.decodeResource(res, R.drawable.sword), 1500, 1500, 60, Constants.ENV_TYPE, 30);
//			ghostThread.getSprites().add(weapon);
//			ghostThread.getWeapons().add(weapon);
			
			SoundHelper.getInstance().setMusic(
						ghostThread.getContext().getAssets().openFd("ghostmusic.mp3"));
			
			// welcome message event
			ghostThread.getEvents().add(new GhostGameEvent(Constants.FPS, 
					GhostGameEvent.EVENT_TYPE.
					WELCOME));
			
			LevelUtil.replaceIfCollision(ghostThread, coins, false);
			LevelUtil.replaceIfCollision(ghostThread, potions, false);
		} catch(PropertyManagerException e) {
			throw new GameException("PropertyManagerException exception" +
					" in method createLevel " + e);
		} catch(IOException e) {
			throw new GameException("IOException exception" +
					" in method createLevel " + e);
		}
	}

	@Override
	public int getMapSize() throws GameException {
		return gridMap.getWdith();
	}

	@Override
	public String getLevelName() throws GameException {
		return Constants.LEVEL1;
	}

}
