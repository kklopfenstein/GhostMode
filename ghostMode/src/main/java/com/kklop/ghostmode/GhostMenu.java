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
		int itemId = item.getItemId();
		if (itemId == R.id.about) {
			startAbout();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
