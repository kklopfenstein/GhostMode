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
package com.kklop.ghostmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.WindowManager;

import com.kklop.angmengine.game.event.TouchScreenEvent;
import com.kklop.ghostmode.GhostView.GhostThread;
import com.kklop.ghostmode.utils.Constants;

public class GhostMode extends Activity {

	private static final String TAG = "GhostMode";
	
    /** A handle to the thread that's actually running the animation. */
    private GhostThread mGhostThread;

    /** A handle to the View in which the game is running. */
    private GhostView mGhostView;
    protected boolean music = true;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Log.e(TAG, "Setting content view");
        setContentView(R.layout.activity_ghost_mode);
        Intent intent = getIntent();
        if(intent != null && (Constants.FALSE.equals(intent.getAction()))) {
        	music = false;
        }
        mGhostView = (GhostView) findViewById(R.id.ghost);
        try {
        	mGhostThread= mGhostView.getThread();
        	mGhostThread.setMusicOn(music);
        	mGhostThread.setActivity(this);
        } catch(Exception e) {
        	e.printStackTrace();
        	Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_ghost_mode, menu);
        return true;
    }
    
    @Override 
    public boolean onTouchEvent(MotionEvent event) {
    	mGhostThread.addEvent(new TouchScreenEvent(event));
    	return true;
    }
    
    public void startHighScore(int score) {
    	mGhostThread.setMRun(false);
		Intent intent = new Intent(GhostMode.this, HighScore.class);
		intent.putExtra("score", score);
		GhostMode.this.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
	}
    
    
}
