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
package com.kklop.angmengine.game.map.gridMap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;

import com.kklop.angmengine.game.map.Map;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.bound.rect.RectBound;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class GridMap implements Map {

	private final Bitmap mapBuilder;
	private final int mapSizeWidth;
	private final int mapSizeHeight;
	private final int gridCellSize;
	private ArrayList<Rect> sourceRects;
	private HashMap<Integer, Rect> gridMapRects;

	/**
	 * 
	 * @param mapSizeWidth
	 * @param mapSizeHeight
	 * @param mapBuilder
	 * @param gridCellSize
	 */
	public GridMap(final int mapSizeWidth, final int mapSizeHeight, 
			final Bitmap mapBuilder, final int gridCellSize) {
		this.mapSizeWidth = mapSizeWidth;
		this.mapSizeHeight = mapSizeHeight;
		this.mapBuilder = mapBuilder;
		this.gridCellSize = mapBuilder.getWidth()/gridCellSize;
		buildSourceRects();
		buildRandomMap();
	}
	
	private void buildSourceRects() {
		int builderWidth = 1;
		int builderHeight = 1;
		
		if(this.mapBuilder.getWidth() > gridCellSize) {
			builderWidth = this.mapBuilder.getWidth() / gridCellSize;
		}
		
		if(this.mapBuilder.getHeight() > gridCellSize) {
			builderHeight = this.mapBuilder.getHeight() / gridCellSize;
		}
		
		sourceRects = new ArrayList<Rect>();
		for(int i = 0; i < builderWidth; i++) {
			for(int j = 0; j < builderHeight; j++) {
				Rect rect = new Rect(
						i * gridCellSize,
						j * gridCellSize,
						(i * gridCellSize) + gridCellSize,
						(j * gridCellSize) + gridCellSize
					);
				sourceRects.add(rect);
			}
		}
	}
	
	private void buildRandomMap() {
		gridMapRects = new HashMap<Integer, Rect>();
		for(int i = 1; i <= mapSizeWidth; i++) {
			for(int j = 1; j <= mapSizeHeight; j++) {
				int gridPos = i * j;
				gridMapRects.put(gridPos, getRandomCell());
			}
		}
	}
	
	public Rect getRandomCell() {
		int randomCell = 0 + (int)(Math.random() * 
				(((sourceRects.size()-1) - 0) + 1));
		return sourceRects.get(randomCell);
	}
	
	@Override
	public void draw(Canvas canvas, RectBound bound) {
		int gridBoundXMin = 
				Float.valueOf(bound.getLeft().x).intValue() / gridCellSize;
		int gridBoundXMax =
				Float.valueOf(bound.getRight().x).intValue() / gridCellSize + 1;
		int gridBoundYMin = 
				Float.valueOf(bound.getLeft().y).intValue() / gridCellSize;
		int gridBoundYMax =
				Float.valueOf(bound.getRight().y).intValue() /gridCellSize + 1;
		for(int j = gridBoundXMin; j < gridBoundXMax; j++) {
			for(int i = gridBoundYMin; i < gridBoundYMax; i++) {
				float x = j*gridCellSize;
				float y = i*gridCellSize;
				if(Sprite.isCollided(x, y, x+gridCellSize, y+gridCellSize, 
						bound.getLeft().x, bound.getLeft().y, 
						bound.getRight().x, bound.getRight().y)) {
					RectF destRect = new RectF(
							x, 
							y, 
							x + gridCellSize, 
							y + gridCellSize
						);
					int gridPos = (i+1) * (j+1);
					canvas.drawBitmap(mapBuilder, gridMapRects.get(gridPos), 
							destRect, null);
				}
			}
		}
	}
	
	public int getWdith() {
		return mapSizeWidth * gridCellSize;
	}
	
	public int getHeight() {
		return mapSizeWidth * gridCellSize;
	}

}
