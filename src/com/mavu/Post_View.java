package com.mavu;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.mavu.appcode.Post;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Post_View extends Activity {
	
	private Post post;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view_layout);
        
        String[] postInfo = getIntent().getStringArrayExtra("postInfo");
        post = new Post(postInfo[0], 
        		postInfo[1], postInfo[2], postInfo[3], postInfo[4], postInfo[5], postInfo[6], new Date(postInfo[7]));
        
    	//Assign variables to pass
		 EditText title = ((EditText) findViewById(R.id.txtTitle));
		 title.setText(post.getTitle());
		 EditText desc = ((EditText) findViewById(R.id.txtDescription));
		 desc.setText(post.getDesc());
		 EditText category =  ((EditText) findViewById(R.id.txtCategory));
		 category.setText(post.getCategory());
		 
		 
		 EditText address = ((EditText) findViewById(R.id.txtAddress));
		 address.setText(post.getAddress());
		 EditText city = ((EditText) findViewById(R.id.txtCity));
		 city.setText(post.getCity());
		 EditText time = ((EditText) findViewById(R.id.txtTime));
		 time.setText(post.getTime());
		 
			
			
		//Date setting
		 EditText date = ((EditText) findViewById(R.id.txtDate));
		 date.setText(post.getDate().toString());

        
        //Todo: We will have to serialize the object i guess to pass it...Rhode? You had fun with this right?
        //selectedPost = new Post(getIntent().getStringExtra("SelectedPost"));
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_info_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    		case R.id.view_owner:
    			Toast.makeText(getApplicationContext(),
    					"clicked account",
    	                Toast.LENGTH_SHORT).show();
    			//Todo:
    			//Maybe do a call to a local db to see if an account is stored..if so then pre fill the values to the logged in person
    			// assign currentAccount object
    			openAccount();
    			break;
    		       
    	} 	
    	//return super.onOptionsItemSelected(item);
    	return true;

    }
    
    protected void openAccount()
    {
    	int accountId = Integer.parseInt(post.getAccountID());
    	Intent intent = new Intent();
		intent.setClass(this, Account_View.class);
		intent.putExtra("accountId", accountId);
		startActivity(intent);		
    	
    }
	

}
