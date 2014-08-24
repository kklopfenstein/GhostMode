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
package com.kklop.ghostmode.hud;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.ghostmode.R;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.level.Level;
import com.kklop.ghostmode.state.GameState;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public class Hud {
	
	private int hudWidth;
	private int hudHeight;
	private Context context;
	
	private static final int PAD_FROM_BOT = 10;
	private static final int PAD_HORIZ = 20;
	
	private static final int PAD_FROM_TOP = 10;
	private static final int PAD_TOP_HORIZ = 20;
	
	private String gameMode;
	private ArrayList<HudElement> hudElements;
	private ArrayList<HudElement> upperHudElements;
	
	// hud elements
	private HudElement flameBtn;
	private HudElement freezeBtn;
	private HudElement pauseBtn;
	private HudElement helpBtn;
	private HudElement okBtn;
	
	public Hud(int hudWidth, int hudHeight, Context context, Bound bound) 
			throws GameException {
		this.hudWidth = hudWidth;
		this.hudHeight = hudHeight;
		this.context = context;
		
		try {
			this.gameMode = PropertyManager.getGameMode();
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
		
		// initialize the hud buttons
		initHudElements(bound);
	}
	
	private void initHudElements(Bound bound) throws GameException {
		this.hudElements = new ArrayList<HudElement>();
		this.upperHudElements = new ArrayList<HudElement>();
		Resources res = context.getResources();
		
		// pause button
		this.pauseBtn = new HudElement(
				bound,
				R.drawable.pause,
				0,
				0,
				60,
				Constants.HUD_TYPE,
				Constants.PAUSE_BTN,
				res
			);
		upperHudElements.add(pauseBtn);
		
		this.helpBtn = new HudElement(
				bound,
				R.drawable.help,
				0,
				0,
				60,
				Constants.HUD_TYPE,
				Constants.HELP_BTN,
				res
			);
		upperHudElements.add(helpBtn);
		
		initHudUpperBarPos();
		
		// freeze button
		this.freezeBtn = new HudElement(
				bound,
				R.drawable.feezebtn,
				0,
				0,
				60,
				Constants.HUD_TYPE,
				Constants.FREEZE_BTN,
				res
			);
		
		hudElements.add(freezeBtn);
		
		this.flameBtn = new HudElement(
				bound,
				R.drawable.flame,
				0,
				0,
				60,
				Constants.HUD_TYPE,
				Constants.FLAME_BTN,
				res
			);
		
		hudElements.add(flameBtn);
		
		
		// now init all the positions of bottom hud bar
		initHudBtmBarPos();
		
		this.okBtn = new HudElement(
				bound,
				R.drawable.ok,
				0,
				0,
				60,
				Constants.HUD_TYPE,
				Constants.OK_BTN,
				res
			);
		
	}
	
	private void initHudBtmBarPos() {
		float x = PAD_FROM_BOT;
		float y = hudHeight - PAD_FROM_BOT;
		
		for(HudElement hudElement : hudElements) {
			hudElement.setX(x);
			hudElement.setY(y-hudElement.getHeight());
			x += hudElement.getWidth() + PAD_HORIZ;
		}
	}
	
	private void initHudUpperBarPos() {
		float x = hudWidth - PAD_FROM_TOP;
		float y = PAD_FROM_TOP;
		
		for(HudElement hudElement : upperHudElements) {
			hudElement.setX(x - hudElement.getWidth());
			hudElement.setY(y);
			x -= (hudElement.getWidth() + PAD_TOP_HORIZ);
		}
	}
	/**
	 * 
	 * @param canvas
	 * @param state
	 */
	public void draw(Canvas canvas, GameState state, Level level) throws GameException {
		// reset any translation
		canvas.setMatrix(null);
		Paint paint = new Paint(); 
		paint.setColor(Color.WHITE); 
		paint.setStyle(Style.FILL); 
		paint.setTextSize(30);
		paint.setTypeface(Typeface.MONOSPACE);
		if(Constants.DEBUG.equals(gameMode)) {
			canvas.drawText("Collects: " + state.getCollects() +
					" There are " + (state.getTotalChests()-state.getCollects()) + 
					" chests left!!" +
					" Timer: " + 
					Double.valueOf(Math.floor(state.getTimer()/1000)).intValue(), 
					10, 25, paint);
		} else {
			canvas.drawText("Coins: " + state.getCollects() +
					//" " + (state.getTotalChests()-state.getCollects()) + 
					" Time: " +
					Double.valueOf(Math.floor(state.getTimer()/1000)).
						intValue() + " Score: " + state.getLevelScore(), 
					10, 25, paint);
			String text = Integer.valueOf(state.getBluePotions()).toString();
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			if(state.getBluePotions() > 0) {
				canvas.drawText(text, freezeBtn.getX()+freezeBtn.getWidth()/2-(bounds.width()/2), freezeBtn.getY()-(bounds.height()), paint);
			}
			text = Integer.valueOf(state.getRedPotions()).toString();
			bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			if(state.getRedPotions() > 0) {
				canvas.drawText(text, flameBtn.getX()+flameBtn.getWidth()/2-(bounds.width()/2), flameBtn.getY()-(bounds.height()), paint);
			}
		}
		if(state.getCollects() >= state.getTotalChests()) {
			paint.setTextSize(45);
			String text = "YOU WIN!";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
		} else if(state.isDropChest()) {
			String text = "DROPPED COIN!";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
		}
		if(state.timeOver()) {
			paint.setTextSize(45);
			String text = "YOU LOSE!";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
		}
		if(state.isWelcome()) {
			paint.setTextSize(45);
			String text = "GHOST MODE";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
			paint.setTextSize(30);
			text = "Collect " + PropertyManager.getInteger(Constants.COINS_TO_WIN) + " coins!";
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2)+bounds.height(), paint);
			paint.setTextSize(40);
			text = level.getLevelName();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2)+(2*bounds.height()), paint);
		}
		if(state.getRedPotions() > 0) {
			this.flameBtn.enable();
		} else {
			this.flameBtn.disable();
		}
		
		if(state.getBluePotions() > 0) {
			this.freezeBtn.enable();
		} else {
			this.freezeBtn.disable();
		}
		
		if(state.isPaused()) {
			paint.setTextSize(45);
			String text = "PAUSED";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
		}
		
		if(state.isHelp()) {
			paint.setColor(Color.BLACK);
			int buttonWidth = flameBtn.getWidth();
			canvas.drawRect(buttonWidth, buttonWidth, hudWidth - buttonWidth, hudHeight - buttonWidth, paint);
			paint.setColor(Color.WHITE);
			paint.setTextSize(45);
			String text = "HELP";
			Rect bounds = new Rect();
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2), paint);
			paint.setTextSize(20);
			text = "Collect coins to win!";
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2)+bounds.height(), paint);
			paint.setTextSize(20);
			text = "Potions will give you powers to help ward off enemies.";
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2)+(2*bounds.height()), paint);
			paint.setTextSize(20);
			text = "Tap anywhere on the screen to move your ghost.";
			paint.getTextBounds(text, 0, text.length(), bounds);
			canvas.drawText(text, hudWidth/2-(bounds.width()/2), hudHeight/2-(bounds.height()/2)+(3*bounds.height()), paint);
			this.okBtn.setX((hudWidth/2) - (okBtn.getWidth()/2));
			this.okBtn.setY((hudHeight - buttonWidth - okBtn.getHeight()));
			this.okBtn.enable();
		} else {
			this.okBtn.disable();
		}
		
		// draw btm bar buttons
		drawButtons(canvas);
	}
	
	/**
	 * 
	 * @param canvas
	 */
	private void drawButtons(Canvas canvas) {
		for(HudElement hudElement : hudElements) {
			if(hudElement.isEnabled()) {
				hudElement.draw(canvas);
			}
		}
		
		for(HudElement hudElement : upperHudElements) {
			if(hudElement.isEnabled()) {
				hudElement.draw(canvas);
			}
		}
		
		if(okBtn.isEnabled()) {
			okBtn.draw(canvas);
		}
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public String isButtonPress(final float x, final float y) {
		String result = null;
		for(HudElement hudElement : hudElements) {
			if(hudElement.pointCollision(x, y) && hudElement.isEnabled()) {
				result = hudElement.getBtnName();
				break;
			}
		}
		for(HudElement hudElement : upperHudElements) {
			if(hudElement.pointCollision(x, y) && hudElement.isEnabled()) {
				result = hudElement.getBtnName();
				break;
			}
		}
		
		if(okBtn.pointCollision(x, y) && okBtn.isEnabled()) {
			result = okBtn.getBtnName();
		}
		return result;
	}

	public HudElement getFlameBtn() {
		return flameBtn;
	}

	public HudElement getFreezeBtn() {
		return freezeBtn;
	}

	public HudElement getPauseBtn() {
		return pauseBtn;
	}

	public HudElement getHelpBtn() {
		return helpBtn;
	}
	
}
