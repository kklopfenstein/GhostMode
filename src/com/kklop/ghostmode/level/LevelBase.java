package com.kklop.ghostmode.level;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.util.Log;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.map.gridMap.GridMap;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.R;
import com.kklop.ghostmode.chars.Collectable;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.state.GameState;
import com.kklop.ghostmode.utils.Constants;
import com.kklop.ghostmode.utils.PropertyManager;

public abstract class LevelBase implements Level {

	private static String TAG = "LevelBase";
	protected GridMap gridMap;
	
	public LevelBase(Context context) throws GameException {
		Resources res = context.getResources();
		Bitmap mapBuilder = BitmapFactory.decodeResource(res, R.drawable.mapbuilder2);
		// init grid map
		try {
			gridMap = new GridMap(
					scale(mapBuilder,PropertyManager.getInteger(getClass().getSimpleName() + ".mapWidth")),
					scale(mapBuilder,PropertyManager.getInteger(getClass().getSimpleName() + ".mapHeight")),
					BitmapFactory.decodeResource(res, res.getIdentifier(PropertyManager.getProperty(getClass().getSimpleName() + ".resource"),  "drawable" , context.getPackageName())), 
					4
				);
		} catch(PropertyManagerException e) {
			throw new GameException(e);
		}
	}
	
	public void createLevel(GhostThread ghostThread) 
			throws GameException {
		ghostThread.setGridMap(gridMap);
	}
	
	@Override
	public boolean isWon(GameState state) throws GameException {
		// TODO Auto-generated method stub
		return false;
	}
	
	public ArrayList<Sprite> generatePotions(GhostThread ghostThread) throws GameException {
		
		Resources res = ghostThread.getContext().getResources();
		
		// init potions
		ArrayList<Sprite> potions = new ArrayList<Sprite>();
		int bluePotionCount = PropertyManager.getInteger(Constants.BLUE_POTION_COUNT);
		
		for(int i=0;i<bluePotionCount;i++) {
			Bitmap chest = BitmapFactory.
					decodeResource(res, R.drawable.blue_potion);
			int ranX = ghostThread.ranX(chest);
			int ranY = ghostThread.ranY(chest); 
			Collectable ch = new Collectable(
					ghostThread.getBound(), 
					R.drawable.blue_potion,
					ranX,
					ranY,
					chest.getWidth()/3,
					chest.getHeight(),
					20,
					3,
					60, 
					Constants.CHEST_TYPE,
					true,
					res,
					Collectable.COLLECTABLE_TYPE.BLUE_POTION
				);
			ghostThread.getSprites().add(ch);
			ghostThread.getChests().add(ch);
			potions.add((Collectable) ch);
			try {
				ghostThread.getGrid().addSprite(ch);
			} catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
		}
		
		int redPotionCount = PropertyManager.getInteger(Constants.RED_POTION_COUNT);
		for(int i=0;i<redPotionCount;i++) {
			Bitmap chest = BitmapFactory.
					decodeResource(res, R.drawable.red_potion);
			int ranX = ghostThread.ranX(chest);
			int ranY = ghostThread.ranY(chest); 
			Collectable ch = new Collectable(
					ghostThread.getBound(), 
					R.drawable.red_potion,
					ranX,
					ranY,
					chest.getWidth()/3,
					chest.getHeight(),
					20,
					3,
					60, 
					Constants.CHEST_TYPE,
					true,
					res,
					Collectable.COLLECTABLE_TYPE.RED_POTION
				);
			ghostThread.getSprites().add(ch);
			ghostThread.getChests().add(ch);
			potions.add((Collectable) ch);
			try {
				ghostThread.getGrid().addSprite(ch);
			} catch(GridException g) {
				Log.e(TAG, "GridException in " +
						"constructor of GhostView. " + g.getMessage());
			}
			
		}
		
		return potions;
	}
	
	public int scale(Sprite sp, int val) {
		int defaultHeight = sp.getBitmap().getScaledHeight(DisplayMetrics.DENSITY_DEFAULT);
		return (int) Math.round(val * sp.getHeight()/defaultHeight);
	}
	
	public int scale(Bitmap sp, int val) {
		int defaultHeight = sp.getScaledHeight(DisplayMetrics.DENSITY_DEFAULT);
		return (int) Math.round(val * sp.getHeight()/defaultHeight);
	}

	public GridMap getGridMap() {
		return gridMap;
	}

	public void setGridMap(GridMap gridMap) {
		this.gridMap = gridMap;
	}
	
}
