package com.mavu;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;


import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;
import com.mavu.appcode.SelectionParameters;
import com.mavu.appcode.ViewHolder;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends ListActivity {

	private Vector<Post> posts = new Vector<Post>();
	private LayoutInflater mInflater;
	private Account currentAccount;
	private EditText searchOption;
	private Resources resources;
	private SelectionParameters parameters;
	private DataAccess Da;
	private OnResponseListener responder;


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        
        resources = this.getResources();
      //Todo:
		//Maybe do a call to a local db to see if an account is stored..if so then pre fill the values to the logged in person
		// assign currentAccount object
        currentAccount = new Account(); //todo...temp
        
        //Saturday morning....
        // 1.) Context menu for search
        // 2.) Preferences...possibly drop down. Dont waste time if we cant figure it out
        // 3.) Account Form
        
        //todo: for drop down:
        //http://wptrafficanalyzer.in/blog/adding-drop-down-navigation-to-action-bar-in-android/
        
        
        // Todo:
        /* Create background service to retrieve data
         * Get settings information that was set by user to get City values
         * If no cities are set up then toast "Please select a city"
         * Bound Service??? find out which is best
         * 
         * Resources: http://stackoverflow.com/questions/1917773/dynamic-listview-in-android-app
         *			  http://sogacity.com/how-to-make-a-custom-arrayadapter-for-listview/
         */
        
        //Temporarily going to setup our list view with dummy values
        /*
         * mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        	posts = new Vector<Post>();
        	Post post1 = new Post("1", "item1", "description1", "food","123 smith", "Stevens Point", "12:00", new Date(2012, 4, 6));
        	Post post2 = new Post("3", "item2", "description2", "business", "222 jones", "Wausau", "12:00", new Date(2012, 4, 6));
        	Post post3 = new Post("2", "item3", "description2", "music", "222 jones", "Wausau", "12:00", new Date(2012, 4, 6));
        
	        posts.add(post1);
	        posts.add(post2);
	        posts.add(post3);
         */
        parameters = new SelectionParameters(null, null, "Stevens Point", true, true, true, null);
        Da = new DataAccess(responder, parameters);
        Da.execute("6");
        
        //, not sure what 2nd and 3rd parameter should be, maybe they need to be flipped
        //CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_post_layout,R.id.postTitle, posts);
        CustomAdapter adapter = new CustomAdapter(this, android.R.id.list, posts);
        setListAdapter(adapter);        
        getListView().setTextFilterEnabled(true);
        
       

    }
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
			
			public void onSuccess(String message ) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
			
			public void onFailure(String message) {
				Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
			}

			public void onSuccess() {
				// TODO Auto-generated method stub
				
			}
		};


@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	Toast.makeText(getApplicationContext(),
				"clicked",
                Toast.LENGTH_SHORT).show();
    	switch (item.getItemId())
    	{
    		case R.id.accountMenu:
    			Toast.makeText(getApplicationContext(),
    					"clicked account",
    	                Toast.LENGTH_SHORT).show();
    			//Todo:
    			//Maybe do a call to a local db to see if an account is stored..if so then pre fill the values to the logged in person
    			// assign currentAccount object
    			openAccount();
    			break;
    			
    		case R.id.search:
    			Toast.makeText(getApplicationContext(),
    					"clicked search",
    	                Toast.LENGTH_SHORT).show();
    			openSearchContext();
    			break;
    	
    		case R.id.createPost:
    			createPost();
    			break;
    			
    		case R.id.menu_prefs:     
    		           Intent intent = new Intent()
    		           		.setClass(this,
    		                com.mavu.Settings.class);
    		           this.startActivityForResult(intent, 0);
    		           break;
    		       
    	} 	
    	//return super.onOptionsItemSelected(item);
    	return true;

    }


	public void onListItemClick(ListView parent, View v, int position, long id) {
		//Set the selected post
	    CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
		Post selectedPost = adapter.getItem(position);
		
		Toast.makeText(getApplicationContext(),
				selectedPost.toString(),
                Toast.LENGTH_SHORT).show();
		
		//Pass the post to the post view intent
		Intent intent = new Intent();
		intent.setClass(this, Post_View.class);
		intent.putExtra("SelectedPost", selectedPost.toString());
		startActivity(intent);

	}
	
	private class CustomAdapter extends ArrayAdapter<Post> {
		//public CustomAdapter(Context context, int resource, int textViewResourceId, List<Post> objects) {
			//super(context, resource, textViewResourceId, objects);
		public CustomAdapter(Activity a, int textViewResourceId, List<Post> entries) {        
			super(a, textViewResourceId, entries);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;

			//widgets displayed by each item in your list
			TextView title = null;
			TextView description = null;
			TextView date = null;
			ImageView image = null;

			//data from your adapter
			Post post = getItem(position);


			//we want to reuse already constructed row views...
			if(null == convertView){
				convertView = mInflater.inflate(R.layout.custom_post_layout, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			// 
			holder = (ViewHolder) convertView.getTag();
			
			image = holder.getImage();
			image.setImageResource(setImageType(post.getCategory()));
			
			title = holder.getTitle();
			//title.setText(post.getTitle() + "\t(" + post.getTime() + "-" + post.getDate() + ")");
			title.setText(post.getTitle());

			
			date = holder.getDate();
			date.setText("(" + post.getTime() + "-" + post.getDate() + ")");

			description = holder.getDescription();		
			description.setText(post.getDesc());

			return convertView;
		}
		
		private int setImageType(String category)
		{
			int resID = 0;
			if (category.equals("food"))
			{
				resID = R.drawable.food;
			}
			else if (category.equals("business"))
			{
				resID = R.drawable.business;
			}
			else if (category.equals("music"))
			{
				resID = R.drawable.music;
			}
			return resID;
		}

	}

	private void openAccount()
	{
		Intent intent = new Intent();
		intent.setClass(this, Account_Maint.class);
		//intent.setClass(getApplicationContext());
		
		if (currentAccount != null && currentAccount.getAcccountId() > 0)
		{
			String[] accountInfo = new String[]{String.valueOf(currentAccount.getAcccountId()),
												currentAccount.getfName(),
												currentAccount.getlName(),
												currentAccount.getEmail(),
												currentAccount.getDob().toString(),
												currentAccount.getPassword(),
												String.valueOf(currentAccount.getLikes()),
												String.valueOf(currentAccount.getDislikes())};	
		    
	    
			intent.putExtra("accountInfo", accountInfo);
		}
		
		
		startActivity(intent);		    
	}

	
	private void createPost()
	{
		if (currentAccount.getAcccountId() < 1)
		{
			//means no account is selected.
			Toast.makeText(getApplicationContext(),
					"You must have an Account and be logged in order to create a post",
	                Toast.LENGTH_SHORT).show();
		}
		else
		{
			int accountId = currentAccount.getAcccountId();	
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), Create_Post.class);
			intent.putExtra("accountId", accountId);
			startActivity(intent);	
		}
	}

	private void openSearchContext()
	{
		
	}
	
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
    	super.onActivityResult(reqCode, resCode, data);
    	setOptionText();
    }
    
    private void setOptionText()
    {
  
        SharedPreferences preferences =
        	    PreferenceManager.getDefaultSharedPreferences(this);
        
        String filter_city = preferences.getString("city", "n/a");
    	Boolean filter_music_cat = preferences.getBoolean("music_cat", false);
    	Boolean filter_business_cat = preferences.getBoolean("business_cat", false);
    	Boolean filter_food_cat = preferences.getBoolean("food_cat", false);

      	Toast.makeText(getApplicationContext(),
      			filter_city,
                Toast.LENGTH_SHORT).show();
    	
    	
    	Time now = new Time();
    	now.setToNow();
    	
    	@SuppressWarnings("deprecation")
		Date lowDate = new Date(now.year, now.month, now.monthDay);
    	@SuppressWarnings("deprecation")
		Date highDate = new Date(now.year, now.month, now.monthDay);
    	
    	parameters = new SelectionParameters(lowDate, highDate, filter_city, filter_music_cat, filter_business_cat, filter_food_cat, "");
    	parameters = new SelectionParameters(lowDate,highDate,filter_city, filter_music_cat, filter_business_cat, filter_food_cat,"");
    	
    	//Da.GetPosts(10, parameters);
    
//      This is the other way to get to the shared preferences:
//    	SharedPreferences prefs = getSharedPreferences(
//    			"com.androidbook.preferences.sample_preferences", 0);

    	
    }
}

