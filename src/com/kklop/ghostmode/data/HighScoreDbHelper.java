package com.kklop.ghostmode.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

public class HighScoreDbHelper {
	
	private final static String TAG = "HighScoreDbHelper";
	
	private static final int version = 1;
	private static final String db = "HighScore";
	
	public static ArrayList<Score> getHighScores(Context context, CursorFactory factory) {
		ArrayList<Score> scores = new ArrayList<Score>();
		String[] result_columns = new String[] { DbOpenHelper.KEY_ID, DbOpenHelper.USER_NICK, DbOpenHelper.SCORE };
		String order = DbOpenHelper.SCORE + " desc";
		DbOpenHelper helper = new DbOpenHelper(context, db, factory, version);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(DbOpenHelper.DATABASE_TABLE, result_columns, null, null, null, null, order);
		
		int count = 0;
		while(cursor.moveToNext() && count != 10) {
			count++;
			int id = cursor.getInt(0);
			String name = cursor.getString(1);
			int score = cursor.getInt(2);
			scores.add(new Score(id, score, name));
		}
		
		return scores;
	}
	
	public static Boolean isHighScore(Context context, int score) {
		ArrayList<Score> scores = getHighScores(context, null);
		if(scores != null && scores.size() > 0) {
			for(Score s : scores) {
				if(s.getScore() < score) {
					return true;
				}
			}
		} else {
			return true;
		}
		return false;
	}
	
	public static void insertHighScore(Context context, CursorFactory factory, String name, int score) {
		ContentValues newValues = new ContentValues();
		
		newValues.put(DbOpenHelper.USER_NICK, name);
		newValues.put(DbOpenHelper.SCORE, Integer.valueOf(score).toString());
		
		DbOpenHelper helper = new DbOpenHelper(context, db, factory, version);
		
		SQLiteDatabase db = helper.getWritableDatabase();
		long inserted = db.insert(DbOpenHelper.DATABASE_TABLE, null, newValues);
		if(inserted == 0) {
			Log.e(TAG, "Error inserting high score");
		}
	}
}
