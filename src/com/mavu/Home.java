package com.mavu;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.json.JSONObject;


import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.LocalAccountsDataSource;
import com.mavu.appcode.Post;
import com.mavu.appcode.SelectionParameters;
import com.mavu.appcode.ViewHolder;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.R.menu;
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
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends ListActivity {

	private Vector<Post> posts;
	private LayoutInflater mInflater;
	private Account currentAccount;
	private EditText searchOption;
	private Resources resources;
	private SelectionParameters parameters;
	private DataAccess Da;
	private OnResponseListener responder;
	private Menu menu;
	
	
	private EditText txtSearch;
	//Preferences variables
    private String filter_city;
 	private Boolean filter_music_cat;
 	private Boolean filter_business_cat;
 	private Boolean filter_food_cat;
	private String accountEmail;
	
	private LocalAccountsDataSource datasource;


	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        
        resources = this.getResources();
        
        //Try reading saved preferences...see if an account exists.
        // If so then set the current account
        SharedPreferences preferences =
        	    PreferenceManager.getDefaultSharedPreferences(this);
    	accountEmail = preferences.getString(
    	        resources.getString(R.string.preferred_Account),
    	        resources.getString(R.string.sort_option_default_value));
    	
        filter_city = preferences.getString("city", "n/a");
    	filter_music_cat = preferences.getBoolean("music_cat", false);
    	filter_business_cat = preferences.getBoolean("business_cat", false);
    	filter_food_cat = preferences.getBoolean("food_cat", false);

    	if (!accountEmail.equals("") && !accountEmail.equals("Guest"))
    	{
	    	datasource = new LocalAccountsDataSource(this);
	    	datasource.open();
	    	currentAccount = datasource.getAccount(accountEmail);
	    	
	    	//todo: when we have it set up, call the actual get account from the server.
    	}
    	else
    	{
    		currentAccount = null; //todo...temp
    	}

        mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        parameters = new SelectionParameters(null, null, "Stevens Point", filter_music_cat, filter_business_cat, filter_food_cat, "");
        
        Da = new DataAccess(onResponseListener, parameters);
        Da.execute("6");
       

    }
	
	protected OnResponseListener onResponseListener = new OnResponseListener() {
			
			public void onSuccess(String message ) {
				Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
			}
			
			public void onFailure(String message) {
				Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
			}


			public void onSuccess(Vector<Post> posts) {
				setPost(posts);
				
			}
		};
	private void setPost(Vector<Post> post1) {
		this.posts = post1;
		CustomAdapter adapter = new CustomAdapter(this, android.R.id.list, posts);
        setListAdapter(adapter);        
        getListView().setTextFilterEnabled(true);
	}

@Override
	public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        this.menu = menu;
        if (currentAccount != null && !currentAccount.getEmail().equals(""))
        {
        	updateCreatePostEnabledStatus(true);
        }
        else
        {
        	updateCreatePostEnabledStatus(false);
        }
        return true;
    }

@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    		case R.id.accountMenu:

    			//Todo:
    			//Maybe do a call to a local db to see if an account is stored..if so then pre fill the values to the logged in person
    			// assign currentAccount object
    			openAccount();
    			break;
    			
    		case R.id.search:
    			registerForContextMenu(findViewById(R.id.search));
    			((View)findViewById(R.id.search)).showContextMenu();

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

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
	                                ContextMenuInfo menuInfo) {
	    super.onCreateContextMenu(menu, v, menuInfo);
	    getMenuInflater().inflate(R.menu.search_context_menu, menu);


}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
	    AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    
	    //Show the search text box
		txtSearch = ((EditText)findViewById(R.id.searchText));
    	txtSearch.setVisibility(View.VISIBLE);
    	txtSearch.setFocusable(true);
    	//txtSearch.setHeight(10); //todo
    	
	    switch (item.getItemId()) {
	        case R.id.search_byAccount:
	        	setSearchText("byAccount");
	            return true;
	        case R.id.search_byTitle:
	        	setSearchText("byTitle");
	            return true;
	        case R.id.search_byDate:
	        	setSearchText("byDate");
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void setSearchText(String filterBy)
	{
		txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        	
            if (actionId == EditorInfo.IME_ACTION_DONE ||
            	actionId == EditorInfo.IME_ACTION_GO ||
            	actionId == EditorInfo.IME_ACTION_NEXT) {
            	performPostsSearch();
                return true;
            }
            return false;
        }});

		
		if (filterBy.equals("byAccount"))
		{
			parameters.setAccountId(txtSearch.getText().toString());
		}
		else if (filterBy.equals("byTitle"))
		{
			parameters.setTitle(txtSearch.getText().toString());
		}
		else if (filterBy.equals("byDate"))
		{
			parameters.setHighDate(txtSearch.getText().toString());
			parameters.setLowDate(txtSearch.getText().toString());
			//Todo: how to format search date? Allow them to enter month name? or in gay format?
		}
  
	}
		
	private void performPostsSearch()
	{
		Toast.makeText(getApplicationContext(),
				"Performing Search",
                Toast.LENGTH_SHORT).show();
	
		txtSearch.setVisibility(View.GONE);
		InputMethodManager imm = (InputMethodManager)getSystemService(
			      getApplicationContext().INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
		
		 Da = new DataAccess(onResponseListener, parameters);
	     Da.execute("6");
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		//Set the selected post
	    CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
		Post selectedPost = adapter.getItem(position);
		
		int accountId = 0;
		accountId = currentAccount.getAcccountId();
		
		//Pass the post to the post view intent
		String[] postInfo = new String[]
				{
					String.valueOf(accountId),
					selectedPost.getTitle(),
					selectedPost.getDesc(),
					selectedPost.getCategory(),
					selectedPost.getAddress(),
					selectedPost.getCity(),
					selectedPost.getTime(),
					selectedPost.getDate().toString()
				};
		Intent intent = new Intent();
		intent.setClass(this, Post_View.class);
		intent.putExtra("postInfo", postInfo);
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
			TextView time = null;
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

			time = holder.getTime();
			time.setText(post.getTime());
			
			date = holder.getDate();
			String[] dateVals = post.getDate().split("-");
			String monthStr = getMonthString(Integer.parseInt(dateVals[1]));
			String dateStr = monthStr + " " + dateVals[2] + ", " + dateVals[0];
	
			date.setText(dateStr + " -");

			description = holder.getDescription();		
			description.setText(post.getDesc());

			return convertView;
		}
		
		private String getMonthString(int i)
		{
			String month = "";
					
			switch (i)
			{
				case 1: month = "January"; break;
				case 2: month = "February"; break;
				case 3: month = "March"; break;
				case 4: month = "April"; break;
				case 5: month = "May"; break;
				case 6: month = "June"; break;
				case 7: month = "July"; break;
				case 8: month = "August"; break;
				case 9: month = "September"; break;
				case 10: month = "October"; break;
				case 11: month = "November"; break;
				case 12: month = "December"; break;					
			}
			return month;
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
		
		if (currentAccount != null && !currentAccount.getEmail().equals(""))
		{
			String[] accountInfo = new String[]{String.valueOf(currentAccount.getAcccountId()),
												currentAccount.getfName(),
												currentAccount.getlName(),
												currentAccount.getEmail(),
												currentAccount.getDob(),
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

	
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
    	super.onActivityResult(reqCode, resCode, data);
    	setOptionText();
    	
        /*Da = new DataAccess(onResponseListener, parameters);
        Da.execute("6");
        */
    }
    
    private void setOptionText()
    {
  
        SharedPreferences preferences =
        	    PreferenceManager.getDefaultSharedPreferences(this);
        
        filter_city = preferences.getString("city", "n/a");
    	filter_music_cat = preferences.getBoolean("music_cat", false);
    	filter_business_cat = preferences.getBoolean("business_cat", false);
    	filter_food_cat = preferences.getBoolean("food_cat", false);

    	accountEmail = preferences.getString(
    	        resources.getString(R.string.preferred_Account),
    	        resources.getString(R.string.sort_option_default_value));

    	
    	if (!accountEmail.equals("") && !accountEmail.equals("Guest"))
    	{
	    	datasource = new LocalAccountsDataSource(this);
	    	datasource.open();
	    	currentAccount = datasource.getAccount(accountEmail);
	    	updateCreatePostEnabledStatus(true);
	    	//todo: when we have it set up, call the actual get account from the server.
    	}
    	else
    	{
    		//Disable the Createpost button
    		currentAccount = null;
    		updateCreatePostEnabledStatus(false);
    	}

    	
    	
    	Time now = new Time();
    	now.setToNow();
    	/*
    	@SuppressWarnings("deprecation")
		Date lowDate = new Date(now.year, now.month, now.monthDay);
    	@SuppressWarnings("deprecation")
		Date highDate = new Date(now.year, now.month, now.monthDay);
    	*/
    	
    	Integer lDate = now.year + now.month + now.monthDay;
    	String lowDate = lDate.toString();
    	Integer hDate = now.year + now.month + now.monthDay;
    	String highDate = hDate.toString();
    	
    	parameters = new SelectionParameters(lowDate,highDate,filter_city, filter_music_cat, filter_business_cat, filter_food_cat,"");
    	

    	
    }
    
    private void updateCreatePostEnabledStatus(boolean status)
    {
    	// 2 - Create post. If we update the menu layout then we need to change this
    	menu.getItem(2).setEnabled(status);
    	menu.getItem(2).setVisible(status);
    
    }
    
    //REgister back key so taht when the user presses back I can check if the
    // seach text box is still displayed, if it is then i will disable that instead of destroying
    
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) < 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            onBackPressed();
        }

        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed() {
        Intent setIntent = new Intent(Intent.ACTION_MAIN);
        setIntent.addCategory(Intent.CATEGORY_HOME);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent); 

        return;
    }*/   
}

