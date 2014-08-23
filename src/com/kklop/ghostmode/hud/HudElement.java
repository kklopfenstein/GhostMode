package com.kklop.ghostmode.hud;

import android.content.res.Resources;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.StaticSprite;
import com.kklop.angmengine.game.sprite.bound.Bound;

public class HudElement extends StaticSprite {

	private String btnName;
	private boolean enabled = true;
	
	public HudElement(Bound bound, int bitmap, int x, int y, int fps,
			String type, String btnName, Resources res) throws GameException {
		super(bound, bitmap, x, y, fps, type, res);
		this.btnName = btnName;
		enabled = true;
	}

	public String getBtnName() {
		return btnName;
	}

	public void setBtnName(String btnName) {
		this.btnName = btnName;
	}
	
	public void enable() {
		enabled = true;
	}
	
	public void disable() {
		enabled = false;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

}
