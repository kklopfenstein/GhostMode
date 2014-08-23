package com.kklop.ghostmode.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;

import com.kklop.ghostmode.R;
import com.kklop.ghostmode.exception.PropertyManagerException;

/**
 * Basic property manager for storing game
 * information that needs to be configurable
 * @author Kevin Klopfenstein
 *
 */
public class PropertyManager {
	
	private static Properties properties;
	private static boolean init = false;
	
	// property keys
	public static final String GHOST_COLLISION_LENGTH = 
			"ghost.collision.length";
	
	private PropertyManager() { }
	
	/**
	 * Initialize the property manager with context
	 * @param context
	 * @throws PropertyManagerException
	 */
	public static void init(Context context) throws PropertyManagerException { 
		try {
			Resources resources = context.getResources();
		    InputStream rawResource = resources.openRawResource(R.raw.ghost);
		    properties = new Properties();
		    properties.load(rawResource);
		    System.out.println("The properties are now loaded");
		    System.out.println("properties: " + properties);
		} catch (NotFoundException e) {
		    System.err.println("Did not find raw resource: "+e);
		    throw new PropertyManagerException(e);
		} catch (IOException e) {
		    System.err.println("Failed to open microlog property file");
		    throw new PropertyManagerException(e);
		}
		init = true;
	}
	
	
	public static String getProperty(String name) 
			throws PropertyManagerException {
		if(init) {
			return properties.getProperty(name);
		} else {
			throw new PropertyManagerException("Property manager not " +
					"initialized.");
		}
	}
	
	public static int getGhostCollisionLength() throws PropertyManagerException {
		String value = getProperty(GHOST_COLLISION_LENGTH);
		return Integer.parseInt(value);
	}
	
	public static int getEnemies() throws PropertyManagerException {
		String value = getProperty(Constants.ENEMIES);
		return Integer.parseInt(value);
	}
	
	public static int getMapSize() throws PropertyManagerException {
		String value = getProperty(Constants.MAP_SIZE);
		return Integer.parseInt(value);
	}
	
	public static int getNumberTrees() throws PropertyManagerException {
		String value = getProperty(Constants.TREES);
		return Integer.parseInt(value);
	}
	
	public static int getNumberGraves() throws PropertyManagerException {
		String value = getProperty(Constants.GRAVES);
		return Integer.parseInt(value);
	}
	
	public static int getNumberGraves2() throws PropertyManagerException {
		String value = getProperty(Constants.GRAVES2);
		return Integer.parseInt(value);
	}
	
	public static int getNumberChests() throws PropertyManagerException {
		String value = getProperty(Constants.CHESTS);
		return Integer.parseInt(value);
	}
	
	public static int getChanceDrop() throws PropertyManagerException {
		String value = getProperty(Constants.CHANCE_DROP);
		return Integer.parseInt(value);
	}
	
	public static int getEnemySpeed() throws PropertyManagerException {
		String value = getProperty(Constants.ENEMY_SPEED);
		return Integer.parseInt(value);
	}
	
	public static int getGhostSpeed() throws PropertyManagerException {
		String value = getProperty(Constants.GHOST_SPEED);
		return Integer.parseInt(value);
	}
	
	public static boolean canEnemiesClip() throws PropertyManagerException {
		String value = getProperty(Constants.CLIP_ENEMY);
		if(Constants.TRUE.equals(value)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static String getGameMode() throws PropertyManagerException {
		String value = getProperty(Constants.GAME_MODE);
		return value;
	}
	
	public static int getEnemyFreezeTime() throws PropertyManagerException {
		String value = getProperty(Constants.ENEMY_FREEZE);
		return Integer.parseInt(value);
	}
	
	public static int getTimeLeftMultiplyer() throws PropertyManagerException {
		String value = getProperty(Constants.TIME_LEFT_MULTIPLYER);
		return Integer.parseInt(value);
	}
	
	public static int getEnemyHitScore() throws PropertyManagerException {
		String value = getProperty(Constants.ENEMY_HIT_SCORE);
		return Integer.parseInt(value);
	}
	
	public static int getPickUpChestScore() throws PropertyManagerException {
		String value = getProperty(Constants.PICK_UP_COIN_SCORE);
		return Integer.parseInt(value);
	}
	
	public static int getInteger(String prop) throws PropertyManagerException {
		String value = getProperty(prop);
		return Integer.parseInt(value);
	}
	
	public static boolean getBoolean(String prop) throws PropertyManagerException {
		String value = getProperty(prop);
		return Boolean.parseBoolean(value);
	}
	
}
