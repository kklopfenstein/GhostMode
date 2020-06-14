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

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.Grid;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.angmengine.game.sprite.hitbox.HitBox;
import com.kklop.ghostmode.GhostView.GhostThread;

public class LevelUtil {
	
	public enum DIRECTION { NORTH, SOUTH, EAST, WEST };
	
	public static ArrayList<StaticSprite> drawLine(Bound bound, int bitmap, float x, float y, int fps, 
			String type, DIRECTION dir, int num, Resources res) throws GameException {
		ArrayList<StaticSprite> line = new ArrayList<StaticSprite>();
		
		float xC = x;
		float yC = y;
		
		for(int i=0;i<num;i++) {
			StaticSprite sprite = new StaticSprite(bound, bitmap, xC, yC, fps, type, res);
			line.add(sprite);
			switch(dir) {
				case NORTH:
					yC = yC + sprite.getHeight();
					break;
				case SOUTH:
					yC = yC - sprite.getHeight();
					break;
				case EAST:
					xC = xC + sprite.getWidth();
					break;
				case WEST:
					xC = xC - sprite.getWidth();
					break;
			}
		}
		
		return line;
	}
	
	public static void replaceIfCollision(GhostThread thread, ArrayList<Sprite> sprites) throws GameException {
		replaceIfCollision(thread, sprites, true);
	}
	
	public static void replaceIfCollision(GhostThread thread, ArrayList<Sprite> sprites, boolean useHitBoxes) throws GameException {
		try {
			Grid grid = thread.getGrid();
			for(Sprite sprite : sprites) {
				List<Sprite> collisions = grid.getCollisions(sprite, useHitBoxes);
				if(collisions != null && collisions.size() > 0) {
					replaceUntilFree(sprite, grid, thread, useHitBoxes);
				}
			}
		} catch(GridException g) {
			throw new GameException(g);
		}
	}
	
	public static void replaceUntilFree(Sprite sprite, Grid grid, GhostThread thread, boolean useHitBoxes) throws GridException {
		int x = thread.ranX(sprite.getBitmap());
		int y = thread.ranY(sprite.getBitmap());
		sprite.setX(x);
		sprite.setY(y);
		grid.updateSprite(sprite);
		List<Sprite> collisions = grid.getCollisions(sprite, useHitBoxes);
		if(collisions != null && collisions.size() > 0) {
			replaceUntilFree(sprite, grid, thread, useHitBoxes);
		}
	}
	
	/**
	 * Creates a hitbox scaled to the dpi of the bitmap.
	 * Originally I hardcoded hitboxes based on the height/width
	 * of the assets themselves. Now I need this method to scale
	 * my original hitbox definitions.
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param sp
	 * @return
	 */
	public HitBox createScaledHitbox(int x1, int y1, int x2, int y2, Sprite sp) {
		int frameCount = sp.getBitmap().getWidth() / sp.getWidth();
		int defaultWidth = sp.getBitmap().getScaledHeight(DisplayMetrics.DENSITY_DEFAULT) / frameCount;
		int defaultHeight = sp.getBitmap().getScaledHeight(DisplayMetrics.DENSITY_DEFAULT);
		
		int widthFactor = (int) Math.round(defaultWidth/sp.getWidth());
		int heightFactor = (int) Math.round(defaultHeight/sp.getHeight());
		
		return new HitBox(x1 * widthFactor, y1 * heightFactor, x2 * widthFactor, y2 * heightFactor);
	}
	
}
