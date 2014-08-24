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
package com.kklop.ghostmode.sound;

import java.util.ArrayList;

import android.content.res.AssetFileDescriptor;

public class SoundHelper {
	private static SoundHelper instance = null;
	
	private static ArrayList<SoundService> sounds = new ArrayList<SoundService>();
	private static SoundService music = null;
	
	protected SoundHelper() {
		
	}
	public static SoundHelper getInstance() {
		if(instance == null) {
			instance = new SoundHelper();
		}
		return instance;
	}
	
	/**
	 * Play a sound file.
	 * BUG: Sometimes java.lang.RuntimeException: Couldn't load music, uh oh!
	 * thrown from constructor so surrounding with try,catch
	 * @param asset
	 */
	public void play(AssetFileDescriptor asset) {
		try {
			SoundService service = new SoundService(asset);
			service.play();
			sounds.add(service);
		} catch (Exception e) { }
	}
	
	public void cleanupStopped() {
		ArrayList<SoundService> removal = new ArrayList<SoundService>();
		for(SoundService service : sounds) {
			if(!service.isPlaying()) {
				removal.add(service);
			}
		}
		for(SoundService r : removal) {
			sounds.remove(r);
		}
	}
	
	public void stopAll() {
		ArrayList<SoundService> removal = new ArrayList<SoundService>();
		for(SoundService service : sounds) {
			service.stop();
			removal.add(service);
		}
		for(SoundService r : removal) {
			sounds.remove(r);
		}
		stopMusic();
	}
	
	public void setMusic(AssetFileDescriptor asset) {
		music = new SoundService(asset);
	}
	
	public void playMusicIfStopped() {
		if(music != null && !music.isPlaying()) {
			music.play();
		}
	}
	
	public void stopMusic() {
		if(music != null && music.isPlaying()) {
			music.stop();
		}
	}
	
	
}
