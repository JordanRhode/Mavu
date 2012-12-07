package com.mavu.appcode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LocalSQLhelper extends SQLiteOpenHelper {

  public static final String TABLE_ACCOUNTS = "accounts";
  public static final String COLUMN_ID = "_id";
  public static final String COLUMN_ACCOUNT_FNAME = "fname"; 
  public static final String COLUMN_ACCOUNT_LNAME = "lname";  
  public static final String COLUMN_ACCOUNT_EMAIL = "email"; 
  public static final String COLUMN_ACCOUNT_PASS = "password";
  public static final String COLUMN_ACCOUNT_DOB = "dob";

  private static final String DATABASE_NAME = "accounts.db";
  private static final int DATABASE_VERSION = 1;

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
      + TABLE_ACCOUNTS + "(" + COLUMN_ID
      + " integer primary key autoincrement, " 
      + COLUMN_ACCOUNT_FNAME + " text not null, "
      + COLUMN_ACCOUNT_LNAME + " text not null, "
      + COLUMN_ACCOUNT_EMAIL + " text not null, "
      + COLUMN_ACCOUNT_PASS + " text not null, "
      + COLUMN_ACCOUNT_DOB + " text);";

  public LocalSQLhelper(Context context) {
	  super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
  
  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(LocalSQLhelper.class.getName(),
        "Upgrading database from version " + oldVersion + " to "
            + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
    onCreate(db);
  }

} 