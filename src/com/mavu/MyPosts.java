package com.mavu;

import java.util.List;
import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.MavuDateFormatter;
import com.mavu.appcode.Post;
import com.mavu.appcode.SelectionParameters;
import com.mavu.appcode.ViewHolder;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class MyPosts extends ListActivity {

  /*Called either from Home or Account_Maint*/
  private LayoutInflater mInflater;
  private DataAccess Da;
  private Account currentAccount;
  private Vector<Post> posts;
  
  private Post selectedPost;
  
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.home_layout);
        
    //Get Account that was passed in (Can only get here if an account is logged in)
        if (getIntent().getStringArrayExtra("accountInfo") != null)
        {
          currentAccount = new Account();
          //Fill in all the values of the currentAccount
          String[] accountInfo = getIntent().getStringArrayExtra("accountInfo");
          currentAccount.setAccountId(accountInfo[0]);
          currentAccount.setfName(accountInfo[1]);
          currentAccount.setlName(accountInfo[2]);
          currentAccount.setEmail(accountInfo[3]);
          currentAccount.setDob(accountInfo[4]);
          currentAccount.setPassword(accountInfo[5]);
          currentAccount.setLikes(Integer.parseInt(accountInfo[6]));
          currentAccount.setDislikes(Integer.parseInt(accountInfo[7]));
          
          
          /*TODO: create DA functionality for this*/
          SelectionParameters parameters = new SelectionParameters(null, null, "", true, true, true, "");   
        Da = new DataAccess(this, onResponseListener, parameters);
            Da.execute("6");
            //Da = new DataAccess(this, onResponseListener, currentAccount);
           // Da.execute("8");
        }
        else
        {
          currentAccount = null;
        }
    
      //Setup customlayout for posts listview
        mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        
        

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.my_posts, menu);
    return true;
  }
  

  
  protected OnResponseListener onResponseListener = new OnResponseListener() {
    
    public void onFailure(String message) {
      Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
    }

    public void onSuccess(Vector<Post> posts) {
      setPosts(posts);
    
    }
    
    public void onSuccess(Boolean passed) {
    }
    
    public void onSuccess(String accountID) {
    }

    @Override
    public void onSuccess(Account account) {
      // TODO Auto-generated method stub
      
    }
  };

  private void setPosts(Vector<Post> post1) {
    this.posts = post1;
    mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    CustomAdapter adapter = new CustomAdapter(this, android.R.id.list, posts);
      setListAdapter(adapter);  
      
      getListView().setTextFilterEnabled(true);
      adapter.notifyDataSetChanged();
      
      final ListView lv = getListView(); 

     // Then you can create a listener like so: 
     lv.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener ()
     { 
             @Override 
             public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) { 
                   onLongListItemClick(lv,v,pos,id); 
             return false; 
         }
             
 

     }); 
  
      
  }
  
  public void onListItemClick(ListView parent, View v, int position, long id) {
  /*TODO: pass value to post view that allows them to update the info? because they are the owner? just a bool
   *TODO: add code to handle bool passed in post view to allow editing
   */
  
    //Set the selected post
      CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
    selectedPost = adapter.getItem(position);
    
    //Pass the post to the post view intent
    String[] postInfo = new String[]
        {
          selectedPost.getAccountID(),
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
    intent.putExtra("EditMode", true);
    startActivity(intent);
  
  }
  
  
  public void onLongListItemClick(ListView parent, View v, int position, long id)
  {
    
    //Set the selected post
      CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
    selectedPost = adapter.getItem(position);
    
    //Open context menu
    registerForContextMenu(this.findViewById(android.R.id.list)); //TODO:unsure
    ((View)findViewById(android.R.id.list)).showContextMenu();
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

      date.setText(MavuDateFormatter.format(post.getDate()) + " -");


      description = holder.getDescription();    
      description.setText(post.getDesc());

      return convertView;
    }

    private int setImageType(String category)
    {
      int resID = 0;
      if (category.equals("Food"))
      {
        resID = R.drawable.food;
      }
      else if (category.equals("Business"))
      {
        resID = R.drawable.business;
      }
      else if (category.equals("Music"))
      {
        resID = R.drawable.music;
      }
      return resID;
    }
  }
  
/*TODO: implement*/ 
  public void onCreateContextMenu(ContextMenu menu, View v,
                                  ContextMenuInfo menuInfo) {
      //super.onCreateContextMenu(menu, v, menuInfo);
      getMenuInflater().inflate(R.menu.post_context_menu, menu);
  }
  
  /*TODO: implement*/
  public boolean onContextItemSelected(MenuItem item) {
      
      switch (item.getItemId()) {
          case R.id.viewPost:
            openPost(false);
            
              return true;
          case R.id.editPost:
            openPost(true);
              return true;
          case R.id.deletePost:
            Toast.makeText(getApplicationContext(), "delete..todo", Toast.LENGTH_LONG).show();
            return true;
          default:
              return super.onContextItemSelected(item);
      }
  }
  
  private void openPost(boolean mode)
  {
    //Set the selected post
      
    //Pass the post to the post view intent
    String[] postInfo = new String[]
        {
          selectedPost.getAccountID(),
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
    intent.putExtra("EditMode", mode);
    startActivity(intent);
  }
  
}



