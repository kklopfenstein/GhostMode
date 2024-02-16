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
package com.kklop.angmengine.game.grid;

import android.graphics.PointF;
import android.util.SparseArray;

import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.sprite.Sprite;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Sprite grid to help track object collisions.
 * The limitation of this is that we can't reliably track objects
 * that are larger than the grid size.
 * @author Kevin Klopfenstein
 *
 */
public class Grid {
	
	List<Sprite> sprites;
	/* sprite array */
	private HashMap<Integer,Sprite> grid[];
	
	private int width;
	private int height;
	private int size;
	
	/* class level counter to assign unique id
	 * to a grid member
	 */
	private int gridId = 0;
	
	private SparseArray<HashSet<Integer>> posMap;
	
	/**
	 * Initialize grid based on width, height and size in
	 * units of pixels.
	 * @param widthPx
	 * @param heightPx
	 * @param sizePx
	 * @throws GridException
	 */
	@SuppressWarnings("unchecked")
	public Grid(int widthPx, int heightPx, int sizePx) throws GridException {
		if(widthPx%sizePx != 0 || heightPx%sizePx != 0) {
			throw new GridException("Grid size invalid.");
		}
		
		this.width = widthPx/sizePx;
		this.height = heightPx/sizePx;
		
		this.grid = new HashMap[width*height];
		this.size = sizePx;
		
		this.posMap = new SparseArray<HashSet<Integer>>();
		
		this.sprites = new ArrayList<Sprite>();
	}
	
	/**
	 * Add new sprite to grid
	 * @param sprite
	 * @throws GridException
	 */
	public void addSprite(Sprite sprite) throws GridException {
		sprite.setGridId(this.gridId);
		this.sprites.add(sprite);
		add(sprite);
		this.gridId++;
	}
	
	/**
	 * Update entire grid
	 * @throws GridException
	 */
	public void update() throws GridException {
		for(Sprite sprite : this.sprites) {
			if(Sprite.SPRITE_STATE.MOVING.equals(sprite.getState())
					|| Sprite.SPRITE_STATE.TRACK.equals(sprite.getState())) {
				updateSprite(sprite);
			}
		}
	}
	
	/**
	 * Update sprite position on grid
	 * @param sprite
	 * @throws GridException
	 */
	public void updateSprite(Sprite sprite) throws GridException {
		// remove sprite from grid
		remove(sprite);
		// re-insert into grid at new position
		add(sprite);
	}
	
	/**
	 * Returns grid position in array.
	 * @param x
	 * @param y
	 * @return
	 * @throws GridException
	 */
	private int getGridNumForXY(PointF p) throws GridException {
		Double gridY = Math.floor((float) p.y/this.size);
		Double gridX = Math.floor((float) p.x/this.size);
		
		return ((this.width * gridY.intValue()) + gridX.intValue());
	}
	
	/**
	 * Return a list of cells where
	 * collisions are possible using hit boxes.
	 * @param sprite
	 * @return
	 * @throws GridException
	 */
	public List<Sprite> getCollisions(Sprite sprite) throws GridException {
		return getCollisions(sprite, true);
	}
	
	/**
	 * Return a list of cells where
	 * collisions are possible.
	 * @return
	 */
	public List<Sprite> getCollisions(Sprite sprite, boolean useHitBoxes) throws GridException {
		List<Sprite> collisions = new ArrayList<Sprite>();
		if(sprite.getGridId() == null) {
			throw new GridException("Sprite has not been added to the grid.");
		}
		
		HashSet<Integer> cells = this.posMap.get(sprite.getGridId());
		for(Integer i : cells) {
			//for(int i=0;i<this.grid.length;i++) {
				collisions.addAll(checkColsAtCell(i, sprite, useHitBoxes));
			//}
		}
		return collisions;
	}
	
	public HashSet<Integer> getCollisionCells(Sprite sprite) throws GridException {
		return this.posMap.get(sprite.getGridId());
	}
	
	/**
	 * Check sprite collisions at single cell
	 * @param pos
	 * @param sprite
	 */
	private List<Sprite> checkColsAtCell(int pos, Sprite sprite, boolean useHitBoxes) {
		List<Sprite> collisions = new ArrayList<Sprite>();
		HashMap<Integer, Sprite> cell = this.grid[pos];
		if(cell != null && cell.size() > 0) {
			Collection<Sprite> possibleSprites = cell.values();
			for(Sprite s : possibleSprites) {
				if(!s.equals(sprite) 
						&& sprite.collided(s, useHitBoxes)) {
					collisions.add(s);
				}
			}
		}
		
		return collisions;
	}
	
	/**
	 * Add to grid and also add to position map
	 * @param pos
	 * @param sprite
	 */
	private void addToCell(int pos, Sprite sprite) {
		HashMap<Integer, Sprite> gridCell = this.grid[pos];
		if(gridCell == null) {
			gridCell = new HashMap<Integer, Sprite>();
			this.grid[pos] = gridCell;
		}
		this.grid[pos].put(sprite.getGridId(), sprite);
		HashSet<Integer> s = this.posMap.get(sprite.getGridId());
		if(s == null) {
			s = new HashSet<Integer>();
		}
		s.add(pos);
		this.posMap.put(sprite.getGridId(), s);
	}
	
	/**
	 * Put sprite into grid and position map
	 * @param sprite
	 * @param gridNumber
	 * @throws GridException
	 */
	private void add(Sprite sprite) throws GridException {
		/* Put sprite in the buckets for each of it's four corners.
		 * Some or all of these could be the same bucket. 
		 */
		addToCell(getGridNumForXY(sprite.getTopLeftCrnr()), sprite);
		addToCell(getGridNumForXY(sprite.getTopRightCrnr()), sprite);
		addToCell(getGridNumForXY(sprite.getBotLeftCrnr()), sprite);
		addToCell(getGridNumForXY(sprite.getBotRightCrnr()), sprite);
	}
	
	/**
	 * Remove sprite from grid and position map
	 * @param sprite
	 */
	private void remove(Sprite sprite) {
		HashSet<Integer> pos = posMap.get(sprite.getGridId());
		if(pos != null && pos.size() > 0) {
			for(Integer p : pos) {
				this.grid[p].remove(sprite.getGridId());
			}
		}
		posMap.remove(sprite.getGridId());
	}
	
	/**
	 * Remove sprite from grid
	 * @param sprite
	 */
	public void removeFromGrid(Sprite sprite) {
		// note: this is not very efficient, will have to loop over entire list
		sprites.remove(sprite);
		remove(sprite);
	}
	
}
