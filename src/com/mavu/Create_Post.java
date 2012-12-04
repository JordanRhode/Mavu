package com.mavu;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class Create_Post extends Activity {
	
	 
	 int postID;
     String title;
     String desc;
     String category;
     String address;
     String city;
     String time;
     Date theDate;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_layout);
             
        int accountID = getIntent().getIntExtra("accountId", 0);
        
	}
        
        
    // Create menu item for post 
	public boolean onCreateOptionsMenu(Menu menu) {
		 getMenuInflater().inflate(R.menu.create_post_menu, menu);
		 return true;
		 }
	
	//Saves the post
		public boolean onOptionsItemSelected(MenuItem item) 
		{
			switch (item.getItemId())
			{
				case R.id.save_post:
					
				//Retrieve next open ID from DataAccess class and assign to new Post
		    		//DataAccess da = new DataAccess();
					
		    	//Assign variables to pass
					title = ((EditText) findViewById(R.id.txtTitle)).getText().toString();
					desc = ((EditText) findViewById(R.id.txtDescription)).getText().toString();
					category = ((Spinner) findViewById(R.id.spnCategory)).getSelectedItem().toString();
					address = ((EditText) findViewById(R.id.txtAddress)).getText().toString();
					city = ((EditText) findViewById(R.id.txtCity)).getText().toString();
					time = ((EditText) findViewById(R.id.txtTime)).getText().toString();
					
					
				//Date setting
					String date = ((EditText) findViewById(R.id.txtDate)).getText().toString();
					
					try {
						theDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(date);
						} 
					catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				//Create post
					//Post newPost = new Post(25, title, desc, category, address, city, time, theDate);
					//da.CreatePost(newPost);
					
					Toast.makeText(getApplicationContext(),
							 "Post successfully saved",
							 Toast.LENGTH_SHORT).show();
					
					this.finish();
					
				break;

			} 
		 	return true;
		}
	
	
}
