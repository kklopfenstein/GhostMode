package com.kklop.ghostmode.event;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.angmengine.game.grid.Grid;
import com.kklop.angmengine.game.grid.exception.GridException;
import com.kklop.angmengine.game.sprite.Sprite;
import com.kklop.angmengine.game.sprite.bound.Bound;
import com.kklop.ghostmode.R;
import com.kklop.ghostmode.chars.Ghost;
import com.kklop.ghostmode.exception.PropertyManagerException;
import com.kklop.ghostmode.objects.Flame;
import com.kklop.ghostmode.utils.Constants;

/**
 * 
 * @author Kevin Klopfenstein
 *
 */
public class FlameEvent extends GhostGameEvent {

	@SuppressWarnings("unused")
	private String TAG = getClass().getName();
	
	/* holds all the ice sprites */
	private ArrayList<Flame> flames;
	private Ghost ghost;
	private ArrayList<Sprite> sprites;
	private Context context;
	private Bound bound;
	private Grid grid;
	
	// number of ice particles
	private static final int NUMBER_FLAME = 6;
	
	// speed of ice particles
	private static final int PARTICLE_SPEED = 2;
	
	/**
	 * 
	 * @param fps
	 * @param type
	 * @param ghost
	 * @param bound
	 * @throws PropertyManagerException
	 */
	public FlameEvent(int fps, EVENT_TYPE type, Ghost ghost, Bound bound,
			ArrayList<Sprite> sprites, Context context, Grid grid) 
			throws PropertyManagerException, GameException {
		super(fps, type);
		this.bound = bound;
		this.flames = new ArrayList<Flame>();
		this.ghost = ghost;
		this.sprites = sprites;
		this.context = context;
		this.grid = grid;
		
		try {
			initIce();
		} catch(GridException e) {
			throw new GameException(e);
		}
	}
	
	/**
	 * Init the flames around the ghost and set the travel points
	 * making sure that the travel points are not
	 * outside the boundaries of our little world
	 * @throws GridException 
	 */
	private void initIce() throws GameException {
		Resources res = this.context.getResources();
		for(int i = 0; i < NUMBER_FLAME; i++) {
			Flame particle = new Flame(
						bound,
						R.drawable.flames,
						ghost.getX(),
						ghost.getY(),
						Constants.FPS,
						Constants.FLAME_TYPE,
						res
					);
			flames.add(particle);
			sprites.add(particle);
			grid.addSprite(particle);
		}
	}

	@Override
	public void continueEventActionRun(long gameTime) {
		double angle = 0;
		for(Flame i : flames) {
			i.moveTowardAngle(angle, gameTime, PARTICLE_SPEED, false,
					Sprite.MOVEMENT_AXIS.BOTH);
			// we want each flame particle to move at different angles
			angle += 30;
		}
	}

	@Override
	public void stopEvent() {
		// clean up the sprites so we quit drawing and processing them
		for(Flame i : flames) {
			sprites.remove(i);
			grid.removeFromGrid(i);
			i = null;
		}
	}


	
}
