package com.kklop.ghostmode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import com.kklop.ghostmode.utils.Constants;

/**
 * Main menu for game
 * @author Kevin Klopfenstein
 *
 */
public class GhostMenu extends Activity {
	
	/**
	 * Use menu layout
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ghostmenu);
	}
	
	public void startGame(View view) {
		Intent intent = new Intent(GhostMenu.this, GhostMode.class);
		CheckBox musicOn = (CheckBox) findViewById(R.id.musicOn);
		if(musicOn.isChecked()) {
			intent.setAction(Constants.TRUE);
		} else {
			intent.setAction(Constants.FALSE);
		}
		GhostMenu.this.startActivity(intent);
	}
	
	public void startHighScore(View view) {
		Intent intent = new Intent(GhostMenu.this, HighScore.class);
		GhostMenu.this.startActivity(intent);
	}
	
	public void startAbout() {
		Intent intent = new Intent(GhostMenu.this, About.class);
		GhostMenu.this.startActivity(intent);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.activity_ghost_mode, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.about:
	            startAbout();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
