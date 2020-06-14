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
import com.kklop.ghostmode.chars.EnemyFactory;
import com.kklop.ghostmode.event.GhostGameEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.sound.SoundHelper;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

/**
 * 
 * @author hal9000
 *
 */
public class CastleLevel extends LevelBase  implements Level {

	public CastleLevel(Context context) throws GameException {
		super(context);
	}

	private static String TAG = "Level4";
	
	public void createLevel(GhostThread ghostThread) 
			throws GameException {
		super.createLevel(ghostThread);
		try {
			Resources res = ghostThread.getContext().getResources();
			
			// init trees
			ArrayList<Sprite> trees = new ArrayList<Sprite>();
			int numberTrees = 150;//PropertyManager.getNumberTrees();
			Bitmap tree = BitmapFactory.
					decodeResource(res, R.drawable.tree_an);
			for(int i=0;i<numberTrees;i++) {
				
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
				sp1.addHitbox(new HitBox(10, (sp1.getHeight()-20), 
						sp1.getWidth()-10, sp1.getHeight()-5));
				ghostThread.getSprites().add(sp1);
				ghostThread.getGrid().addSprite(sp1);
				ghostThread.getTrees().add(sp1);
				trees.add(sp1);
			}
			
			
	
//			// init tubleweed
//			int numberGraves = PropertyManager.getNumberGraves();
//			for(int i=0;i<numberGraves;i++) {
//				/* we can share the same bitmap between
//				 * all the sprites because it won't
//				 * need to be manipulated
//				 */
//				Bitmap grave = BitmapFactory.
//						decodeResource(res, R.drawable.tumbleweed);
//				int ranX = ghostThread.ranX(grave);
//				int ranY = ghostThread.ranY(grave);
//				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
//						grave,
//						ranX,ranY,60, Constants.ENV_TYPE);
//				// set hitbox to bottom of gravestone
//				sp2.addHitbox(new HitBox(0, (grave.getHeight()-20), 
//						grave.getWidth(), grave.getHeight()-5));
//				ghostThread.getSprites().add(sp2);
//				ghostThread.getGrid().addSprite(sp2);
//			}
//			
			// init graves2
//			int numberGraves2 = PropertyManager.getNumberGraves2();
//			for(int i=0;i<numberGraves2;i++) {
//				/* we can share the same bitmap between
//				 * all the sprites because it won't
//				 * need to be manipulated
//				 */
//				Bitmap grave = BitmapFactory.
//						decodeResource(res, R.drawable.grave2);
//				int ranX = ghostThread.ranX(grave);
//				int ranY = ghostThread.ranY(grave);
//				StaticSprite sp2 = new StaticSprite(ghostThread.getBound(), 
//						grave,
//						ranX,ranY,60, Constants.ENV_TYPE);
//				// set hitbox to bottom of gravestone
//				sp2.addHitbox(new HitBox(0, (grave.getHeight()-20), 
//						grave.getWidth(), grave.getHeight()-5));
//				ghostThread.getSprites().add(sp2);
//				ghostThread.getGrid().addSprite(sp2);
//			}
//			
			
			// init chests
			ArrayList<Sprite> coins = new ArrayList<Sprite>();
			int numberChests = PropertyManager.getNumberChests();
			Bitmap chest = BitmapFactory.
					decodeResource(res, R.drawable.coin);
			for(int i=0;i<numberChests;i++) {
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
				coins.add((Sprite) ch);
				try {
					ghostThread.getGrid().addSprite(ch);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
				
			}
			
			ArrayList<Sprite> potions = this.generatePotions(ghostThread);
			
			EnemyFactory factory = new EnemyFactory();
			factory.assembleEnemy(R.drawable.knight, 4, R.drawable.knight_freeze, 1, R.drawable.knight_idle, 1);
			factory.assembleEnemy(R.drawable.pumpkin_attack, 2, R.drawable.pumpkin_freeze, 2, R.drawable.pumpkin_attack, 2);
			//factory.assembleEnemy(R.drawable.enemy2_an, 4, R.drawable.enemy2__freeze_an, 4, R.drawable.enemy2_idle_an, 4);
			
			// test code to init 25 enemies
			ArrayList<Sprite> enemies = new ArrayList<Sprite>();
			int numberEnemies = PropertyManager.getEnemies();
			for(int i=0; i<numberEnemies; i++) {
				Enemy enemy = factory.getRandomEnemy(ghostThread);
				ghostThread.getSprites().add(enemy);
				ghostThread.getEnemies().add(enemy);
				enemies.add(enemy);
				try {
					ghostThread.getGrid().addSprite(enemy);
				} catch(GridException g) {
					Log.e(TAG, "GridException in " +
							"constructor of GhostView. " + g.getMessage());
				}
			}
			
			ArrayList<StaticSprite> singleSprites = new ArrayList<StaticSprite>();
			
			singleSprites.add(new StaticSprite(ghostThread.getBound(), 
					R.drawable.cottage,
					650,650,60, Constants.ENV_TYPE, res));
			
			Bitmap horizWall = BitmapFactory.
					decodeResource(res, R.drawable.stone_wall_horiz);
			Bitmap vertWall = BitmapFactory.
					decodeResource(res, R.drawable.stone_wall_vert);
			
			ArrayList<ArrayList<StaticSprite>> lines = new ArrayList<ArrayList<StaticSprite>>();
			lines.add(LevelUtil.drawLine(ghostThread.getBound(), 
					R.drawable.stone_wall_horiz,
					550,500,60, Constants.ENV_TYPE, LevelUtil.DIRECTION.EAST, 5, res));
			lines.add(LevelUtil.drawLine(ghostThread.getBound(), 
					R.drawable.stone_wall_vert,
					550,500,60, Constants.ENV_TYPE, LevelUtil.DIRECTION.NORTH, 10, res));
			lines.add(LevelUtil.drawLine(ghostThread.getBound(), 
					R.drawable.stone_wall_vert,
					550+(horizWall.getWidth()*5),500,60, Constants.ENV_TYPE, LevelUtil.DIRECTION.NORTH, 10, res));
			lines.add(LevelUtil.drawLine(ghostThread.getBound(), 
					R.drawable.stone_wall_horiz,
					550,500+vertWall.getHeight()*10,60, Constants.ENV_TYPE, LevelUtil.DIRECTION.EAST, 2, res));
			lines.add(LevelUtil.drawLine(ghostThread.getBound(), 
					R.drawable.stone_wall_horiz,
					550+(horizWall.getWidth()*3),500+vertWall.getHeight()*10,60, Constants.ENV_TYPE, LevelUtil.DIRECTION.EAST, 2, res));
//			StaticSprite stoneWall = new StaticSprite(ghostThread.getBound(), 
//					BitmapFactory.decodeResource(res, R.drawable.stone_wall_horiz),
//					550,500,60, Constants.ENV_TYPE);
//			StaticSprite cart = new StaticSprite(ghostThread.getBound(), 
//					BitmapFactory.decodeResource(res, R.drawable.cart),
//					590,500,60, Constants.ENV_TYPE);
//			StaticSprite fence2 = new StaticSprite(ghostThread.getBound(), 
//					BitmapFactory.decodeResource(res, R.drawable.fence_horiz2),
//					300+fence.getWidth(),300,60, Constants.ENV_TYPE);
			for(ArrayList<StaticSprite> line : lines) {
				for(StaticSprite s : line) {
					ghostThread.getSprites().add(s);
				}
			}
			for(StaticSprite s : singleSprites) {
				ghostThread.getSprites().add(s);
			}
			//ghostThread.getSprites().add(cart);
			try {
				for(ArrayList<StaticSprite> line : lines) {
					for(StaticSprite s : line) {
						ghostThread.getGrid().addSprite(s);
					}
				}
				for(StaticSprite s : singleSprites) {
					ghostThread.getGrid().addSprite(s);
				}
				//ghostThread.getGrid().addSprite(cart);
			} catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
			// welcome message event
			ghostThread.getEvents().add(new GhostGameEvent(Constants.FPS, 
					GhostGameEvent.EVENT_TYPE.
					WELCOME));
			
			SoundHelper.getInstance().setMusic(
					ghostThread.getContext().getAssets().openFd("ghostmusic.mp3"));
			
			LevelUtil.replaceIfCollision(ghostThread, trees);
			LevelUtil.replaceIfCollision(ghostThread, enemies);
			LevelUtil.replaceIfCollision(ghostThread, coins, false);
			LevelUtil.replaceIfCollision(ghostThread, potions, false);
		} catch(PropertyManagerException e) {
			throw new GameException("PropertyManagerException exception" +
					" in method createLevel " + e);
		} catch(IOException e) {
			throw new GameException(e);
		}
	}

	@Override
	public int getMapSize() throws GameException {
		return gridMap.getWdith();
	}

	@Override
	public String getLevelName() throws GameException {
		return Constants.CASTLE_LEVEL;
	}
}
