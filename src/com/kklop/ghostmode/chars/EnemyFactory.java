package com.kklop.ghostmode.chars;

import java.util.ArrayList;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kklop.angmengine.game.exception.GameException;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.utils.Constants;

public class EnemyFactory {
	private ArrayList<EnemyContainer> enemyTypes = new ArrayList<EnemyContainer>();
	
	public EnemyFactory() {
		
	}
	
	public Enemy getRandomEnemy(GhostThread ghostThread) throws GameException {
		int ran = 0 + (int)(Math.random() * ((enemyTypes.size()-1 - 0) + 1));
		EnemyContainer ec = enemyTypes.get(ran);
		Resources res = ghostThread.getContext().getResources();
		Bitmap en = BitmapFactory.
				decodeResource(res, ec.getId());
		int ranX = ghostThread.ranX(en);
		int ranY = ghostThread.ranY(en);
		Enemy enemy = new Enemy(
				ghostThread.getContext(),
				ghostThread.getBound(), 
				ec.getId(),
				ranX,
				ranY,
				en.getWidth()/ec.getFrames(),
				en.getHeight(),
				4,
				ec.getFrames(),
				60, 
				Constants.ENEMY_TYPE,
				true,
				ec.getIdleId(),
				ec.getIdleFrames(),
				ec.getId(),
				ec.getFrames(),
				ec.getFreezeId(),
				ec.getFreezeFrames(),
				res
			);
		return enemy;
	}
	
	public void assembleEnemy(int id, int frames, int freezeId, int freezeFrames, int idleId, int idleFrames) {
		enemyTypes.add(new EnemyContainer(id, frames, freezeId, freezeFrames, idleId, idleFrames));
	}
	
	private class EnemyContainer {
		private int id;
		private int frames;
		private int freezeId;
		private int freezeFrames;
		private int idleId;
		private int idleFrames;
		public EnemyContainer(int id, int frames, int freezeId,
				int freezeFrames, int idleId, int idleFrames) {
			super();
			this.id = id;
			this.frames = frames;
			this.freezeId = freezeId;
			this.freezeFrames = freezeFrames;
			this.idleId = idleId;
			this.idleFrames = idleFrames;
		}
		public int getId() {
			return id;
		}
		public int getFrames() {
			return frames;
		}
		public int getFreezeId() {
			return freezeId;
		}
		public int getFreezeFrames() {
			return freezeFrames;
		}
		public int getIdleId() {
			return idleId;
		}
		public int getIdleFrames() {
			return idleFrames;
		}
	}
	
}
