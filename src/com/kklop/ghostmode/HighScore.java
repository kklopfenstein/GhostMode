package com.kklop.ghostmode;

import java.util.ArrayList;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kklop.ghostmode.data.HighScoreDbHelper;
import com.kklop.ghostmode.data.Score;

@SuppressLint("DefaultLocale")
public class HighScore extends FragmentActivity implements HighScoreDialog.HighScoreDialogListener {

	ArrayList<String> list;
	
	ArrayAdapter<String> adapter;
	Integer score = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.highscore);
	    Bundle extras = this.getIntent().getExtras();
	    
	    if(extras != null) {
	    	score = (Integer) extras.get("score");
	    	extras.remove("score");
	    	//Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.score_info).concat(Integer.toString(score)), Toast.LENGTH_SHORT);
	    	//toast.show();
	    	if(HighScoreDbHelper.isHighScore(getApplicationContext(), score)) {
	    		openDialog();
	    	}
	    }
	    final ListView listview = (ListView) findViewById(R.id.listview);
	    list = new ArrayList<String>();
	    addTitle();
	    updateHighScores();
	    
	    adapter = new ArrayAdapter<String>(this, 
	    		android.R.layout.simple_list_item_1, list);
	    listview.setAdapter(adapter);
	    
	    
	}
	
	public void openDialog() {
		HighScoreDialog dialog = new HighScoreDialog();
		dialog.show(getSupportFragmentManager(), "highScoreDialog");
	}

	@Override
	public void onDialogPositiveClick(DialogFragment dialog) {
		String initials = (((HighScoreDialog) dialog).initials.getText().toString());
		if(initials != null && initials.length() > 3) {
			initials = initials.substring(0, 3);
		}
		initials = initials.toUpperCase(Locale.ENGLISH);
		if(HighScoreDbHelper.isHighScore(getApplicationContext(), score)) {
			HighScoreDbHelper.insertHighScore(getApplicationContext(), null, initials, score);
			updateHighScores();
		}
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onDialogNegativeClick(DialogFragment dialog) {
		
	}
	
	private void updateHighScores() {
		list.clear();
		addTitle();
		ArrayList<Score> scores = HighScoreDbHelper.getHighScores(getApplicationContext(), null);
		ArrayList<String> scoresView = new ArrayList<String>();
		for(Score score : scores) {
			StringBuilder s = new StringBuilder();
			s.append(score.getName());
			s.append(" ");
			s.append(score.getScore());
			scoresView.add(s.toString());
		}
		list.addAll(scoresView);
	}
	
	private void addTitle() {
		list.add(getString(R.string.high_scores));
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		toMenu();
	}
	
	public void toMenu() {
		Intent intent = new Intent(HighScore.this, GhostMenu.class);
		HighScore.this.startActivity(intent);
	}
}
