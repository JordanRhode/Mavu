package com.mavu;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;

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
     private String accountID;
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_layout);
             
        accountID = getIntent().getStringExtra("accountId");
        
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
					
					//TODO Datepicker
					/*try {
						//theDate = new SimpleDateFormat("MM-dd-yyyy", Locale.ENGLISH).parse(date);
						} 
					catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}*/
					
					if (title.equals("") || desc.equals("") || category.equals("") || address.equals("") || city.equals("") || time.equals(""))
					{
						Toast.makeText(getApplicationContext(),
								 "Please fill in all the values",
								 Toast.LENGTH_SHORT).show();
					}
					else
					{
						//Create post - accountId
						Post newPost = new Post(accountID, title, desc, category, address, city, time, date);
						DataAccess Da = new DataAccess(getApplicationContext(), onResponseListener, newPost);
				        Da.execute("5");
						
						Toast.makeText(getApplicationContext(),
								 "Post successfully saved",
								 Toast.LENGTH_SHORT).show();
						
						this.finish();
					}

					
					

					
				break;

			} 
		 	return true;
		}
	
		protected OnResponseListener onResponseListener = new OnResponseListener() {
			
			public void onFailure(String message) {
				Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
	
			}


			public void onSuccess(Vector<Post> posts) {

			}
			
			public void onSuccess(Account account) {
		
			}

			public void onSuccess(Boolean passed) {
				Toast.makeText(getApplicationContext(), "big booty: ", Toast.LENGTH_LONG).show();
			}
			
			public void onSuccess(String accountID) {
			}
		};
}
