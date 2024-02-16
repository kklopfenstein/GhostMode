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
package com.kklop.angmengine.game.bitmap.cache;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.DisplayMetrics;

import com.kklop.angmengine.game.exception.GameException;

public class BitmapCache {
	private static BitmapCache instance = null;
	
	private LruCache<String, Bitmap> cache;
	
	private static final String FLIP = "flip";
	
	protected BitmapCache() { }
	
	public static BitmapCache getInstance() {
		if(instance == null) {
			instance = new BitmapCache();
		}
		return instance;
	}
	
	public LruCache<String, Bitmap> getCache() {
		if(cache == null) {
			// Get max available VM memory, exceeding this amount will throw an
		    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
		    // int in its constructor.
		    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

		    // Use 1/8th of the available memory for this memory cache.
		    final int cacheSize = maxMemory / 8;
			cache = new LruCache<String, Bitmap>(cacheSize);
		}
		return cache;
	}
	
	public Bitmap getBitmapFromCache(Resources res, String id) throws GameException {
		return getBitmapFromCache(res, id, true);
	}
	
	public Bitmap getBitmapFromCache(Resources res, String id, boolean exception) throws GameException {
		Bitmap bitmap = getCache().get(id);
		if(bitmap == null) {
			try { 
				bitmap = BitmapFactory.decodeResource(res, Integer.parseInt(id));
				getCache().put(id, bitmap);
		    } catch(NumberFormatException e) { /* eat exception */ }
		}
		
		if(bitmap == null && exception) {
			throw new GameException("Bitmap not found");
		}
		
		return bitmap;
	}
	
	public Bitmap getBitmapFromCache(Resources res, int id) throws GameException {
		return getBitmapFromCache(res, Integer.toString(id), true);
	}
	
	public Bitmap getFlipBitmapFromCache(Resources res, int id) throws GameException {
		return getFlipBitmapFromCache(res, Integer.toString(id));
	}
	
	public Bitmap getFlipBitmapFromCache(Resources res, String id) throws GameException {
		Bitmap flipBitmap = getBitmapFromCache(res, id + FLIP, false);
		if(flipBitmap == null) {
			Bitmap bitmap = getBitmapFromCache(res, id, true);
			if(bitmap != null) {
				flipBitmap = createFlipBitmap(bitmap);
				getCache().put(id + FLIP, flipBitmap);
			}
		}
		return flipBitmap;
	}
	
	private Bitmap createFlipBitmap(Bitmap bitmap) {
		Bitmap flipBitmap = null;
		Matrix m = new Matrix();
		m.preScale(-1, 1);
		flipBitmap = Bitmap.createBitmap(bitmap, 0, 0, 
				bitmap.getWidth(), bitmap.getHeight(), 
				m, false);
		flipBitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);
		return flipBitmap;
	}
}
