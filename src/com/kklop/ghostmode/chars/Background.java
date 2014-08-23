package com.kklop.ghostmode.chars;

import android.content.res.Resources;

import com.kklop.angmengine.game.sprite.MapSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;

public class Background extends MapSprite {

	private int size = 3000;
	public Background(Bound bound, int bitmap, int x, int y, int fps,
			int screenHeight, int screenWidth, String type, int size, Resources res) throws Exception {
		super(bound, bitmap, x, y, fps, screenHeight, screenWidth, type, res);
		this.size = size;//PropertyManager.getMapSize();
	}

	@Override
	public int getHeight() {
		return size;
	}

	@Override
	public int getWidth() {
		return size;
	}

	@Override
	public float getMaxX() {
		return size;
	}

	@Override
	public float getMaxY() {
		return size;
	}

	@Override
	public float getX() {
		return 0;
	}

	@Override
	public float getY() {
		return 0;
	}
	
	

}
