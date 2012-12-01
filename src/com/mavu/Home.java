package com.mavu;

import java.util.List;
import java.util.Vector;

import com.mavu.appcode.Post;
import com.mavu.appcode.ViewHolder;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends ListActivity {

	private Vector<Post> posts = new Vector<Post>();
	private LayoutInflater mInflater;

    @SuppressLint({ "NewApi", "NewApi" })
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        
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
        mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        posts = new Vector<Post>();
        Post post1 = new Post(1, "item1", "description1", "food","123 smith", "Stevens Point", "12:00", "12/10/2000");
        Post post2 = new Post(2, "item2", "description2", "business", "222 jones", "Wausau", "12:00", "12/10/2000");
        Post post3 = new Post(3, "item3", "description2", "music", "222 jones", "Wausau", "12:00", "12/10/2000");
        
        posts.add(post1);
        posts.add(post2);
        posts.add(post3);
        //, not sure what 2nd and 3rd parameter should be, maybe they need to be flipped
        //CustomAdapter adapter = new CustomAdapter(this, R.layout.custom_post_layout,R.id.postTitle, posts);
        CustomAdapter adapter = new CustomAdapter(this, android.R.id.list, posts);
        setListAdapter(adapter);        
        getListView().setTextFilterEnabled(true);
        
        
        

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
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

}
