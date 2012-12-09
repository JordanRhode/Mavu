package com.mavu.appcode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class LocalAccountsDataSource {

	private Context context;
  // Database fields
  private SQLiteDatabase database;
  private LocalSQLhelper dbHelper;
  private String[] allColumns = { LocalSQLhelper.COLUMN_ID,
		  LocalSQLhelper.COLUMN_ACCOUNT_FNAME,
		  LocalSQLhelper.COLUMN_ACCOUNT_LNAME,
		  LocalSQLhelper.COLUMN_ACCOUNT_EMAIL,
		  LocalSQLhelper.COLUMN_ACCOUNT_PASS,
		  LocalSQLhelper.COLUMN_ACCOUNT_DOB };

  public LocalAccountsDataSource(Context context) {
    dbHelper = new LocalSQLhelper(context);
    this.context = context;
  }

  public void open() throws SQLException {
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public void createAccount(Account account) {
    ContentValues values = new ContentValues();

    values.put(LocalSQLhelper.COLUMN_ACCOUNT_FNAME, account.getfName());
    values.put(LocalSQLhelper.COLUMN_ACCOUNT_LNAME, account.getlName());
    values.put(LocalSQLhelper.COLUMN_ACCOUNT_EMAIL, account.getEmail());
    values.put(LocalSQLhelper.COLUMN_ACCOUNT_PASS, account.getPassword());
    values.put(LocalSQLhelper.COLUMN_ACCOUNT_DOB, account.getDob());
    
    long insertId = database.insert(LocalSQLhelper.TABLE_ACCOUNTS, null,
        values);
  }

  public void deleteAccount(String email) {
    database.delete(LocalSQLhelper.TABLE_ACCOUNTS, LocalSQLhelper.COLUMN_ACCOUNT_EMAIL
        + " = '" + email +"'", null);
  }
  
  public void updateAccount(Account account) {
	  try
	  {
	    ContentValues values = new ContentValues();

	    values.put(LocalSQLhelper.COLUMN_ACCOUNT_FNAME, account.getfName());
	    values.put(LocalSQLhelper.COLUMN_ACCOUNT_LNAME, account.getlName());
	    values.put(LocalSQLhelper.COLUMN_ACCOUNT_EMAIL, account.getEmail());
	    values.put(LocalSQLhelper.COLUMN_ACCOUNT_PASS, account.getPassword());
	    values.put(LocalSQLhelper.COLUMN_ACCOUNT_DOB, account.getDob());
	  
	    database.update(LocalSQLhelper.TABLE_ACCOUNTS, values, 
	    		LocalSQLhelper.COLUMN_ACCOUNT_EMAIL + " = '" + account.getEmail() +"'", null);
	  }
	  catch (SQLException e) {
		  Toast.makeText(this.context,
					"SQL exception" + e.toString(),
	                Toast.LENGTH_SHORT).show();
	  }

  }

  public Account getAccount(String email) {
	 Account account = new Account();
	 try
	 {
		 Cursor cursor = database.query(LocalSQLhelper.TABLE_ACCOUNTS,
		        allColumns, LocalSQLhelper.COLUMN_ACCOUNT_EMAIL + " = '" + email +"'", null, null, null, null);	


	    cursor.moveToFirst();
	    account = cursorToAccount(cursor);
	    cursor.close();
	 }
	 
	 catch (SQLException e) {
		// TODO: handle exception
			Toast.makeText(this.context,
					"SQL exception" + email + e.toString(),
	                Toast.LENGTH_SHORT).show();
	}

	 
    return account;
  }
  
  public ArrayList<String> getAccountEmails() {
	  ArrayList<String> accounts = new ArrayList<String>();

    Cursor cursor = database.query(LocalSQLhelper.TABLE_ACCOUNTS,
        allColumns, null, null, null, null, null);

    int i = 0;
    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
    	String email = cursor.getString(3);
    	accounts.add(email);
    	i++;
        cursor.moveToNext();
      }
    
      accounts.add("Guest");


    cursor.close();
    return accounts;
  }

  private Account cursorToAccount(Cursor cursor) {

	 Account account = new Account();
	  try
	  {
		 //todo: set acocuntid....shouldnt do once we have real db
		 account.setAccountId(cursor.getString(0));
		 account.setfName(cursor.getString(1));
		 account.setlName(cursor.getString(2));
		 account.setEmail(cursor.getString(3));
		 account.setPassword(cursor.getString(4));
		 account.setDob(cursor.getString(5));
	  }
	  catch (Exception e) {
		  Toast.makeText(this.context,
					"" + e.toString(),
	                Toast.LENGTH_SHORT).show();
	}
	 
    return account;
  }
} 
