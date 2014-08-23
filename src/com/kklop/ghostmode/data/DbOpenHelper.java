package com.kklop.ghostmode.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbOpenHelper extends SQLiteOpenHelper {

	private static final String TAG = "DbOpenHelper";
	public static final String KEY_ID = "_id";
	public static final String USER_NICK = "USER_NICK";
	public static final String SCORE = "SCORE";
	
	//private static final String DATABASE_NAME = "ghostMode.db";
	public static final String DATABASE_TABLE = "HighScore";
	//private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table " +
			DATABASE_TABLE + " ( " + KEY_ID + " integer primary key autoincrement, " +
			USER_NICK + " text not null, " + SCORE + " integer);";
	
	public DbOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Log the version upgrade
		Log.w(TAG, "Upgrading from version " + oldVersion + " to " + newVersion + ", "
				+ "which will destroy the old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		onCreate(db);
	}
	
}
