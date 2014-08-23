package com.kklop.ghostmode.event;

import android.graphics.Canvas;
import android.util.Log;

import com.kklop.angmengine.game.event.GameEvent;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

/**
 * The game event class will store information about an
 * event ongoing in the game for a particular amount of
 * frames.
 * @author Kevin Klopfenstein
 *
 */
public class GhostGameEvent implements GameEvent {
	
	public final String TAG = getClass().getName();
	
	private int frameLength; // length of entire event in frames
	private int currentFrame = 0; // current frame of even we're on
	private boolean active = false;
	protected long evFrameTicker;	// the time of the last frame update
	protected int evFramePeriod;	// milliseconds between each frame (1000/fps)
	
	public enum EVENT_TYPE { GHOST_COLLISION, GHOST_INVUL, DROP_CHEST, 
		GAME_WON, WELCOME, ICE_POWER, FLAME_POWER };
	
	public EVENT_TYPE type;
	
	/**
	 * 
	 * @param frameLength
	 * @param type
	 */
	public GhostGameEvent(int frameLength, int fps, EVENT_TYPE type) 
			throws PropertyManagerException { 
		this.frameLength = frameLength;
		evFramePeriod = 1000 / fps;
		evFrameTicker = 0l;
		active = true;
		this.type = type;
	}
	
	public GhostGameEvent(int fps, EVENT_TYPE type) 
			throws PropertyManagerException {
		String flStr = PropertyManager.getProperty(type.toString() 
				+ Constants.FRAME_LENGTH);
		this.frameLength = Integer.parseInt(flStr);
		evFramePeriod = 1000 / fps;
		evFrameTicker = 0l;
		active = true;
		this.type = type;
	}
		
	public void continueEvent(long gameTime) {
		if(active && (gameTime > (evFrameTicker + evFramePeriod))) {
			Log.i(TAG, "Continuing event " + getType().toString());
			continueEventActionRun(gameTime);
			evFrameTicker = gameTime;
			currentFrame++;
			if(currentFrame > frameLength) {
				active = false;
			}
		}
	}
	
	protected void continueEventActionRun(long gameTime) {
		Log.d(TAG, "continueEventActionRun not implemented for " +
				"GhostGameEvent");
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void startEvent() {
		active = true;
	}
	
	public EVENT_TYPE getType() {
		return type;
	}

	@Override
	public void drawEvent(Canvas arg0) {
		Log.d(TAG, "drawEvent not implemented for GhostGameEvent");
	}

	@Override
	public void stopEvent() {
		Log.d(TAG, "stopEvent not implemented for GhostGameEvent");
	}
}
