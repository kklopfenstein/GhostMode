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
