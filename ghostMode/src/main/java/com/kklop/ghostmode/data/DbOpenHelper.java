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
