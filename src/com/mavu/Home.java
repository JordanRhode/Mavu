package com.mavu;

import java.util.List;
import java.util.Vector;



import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.LocalAccountsDataSource;
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
  private SharedPreferences preferences;
  private Resources resources;
  private SharedPreferences.Editor editor;
  private SelectionParameters parameters;
  private DataAccess Da;
  private Menu menu;
  private String tmpAccountId;
  
  private EditText txtSearch;
  //Preferences variables
    private String filter_city;
  private Boolean filter_music_cat;
  private Boolean filter_business_cat;
  private Boolean filter_food_cat;

  
  private Boolean updatedPrefs = false;

  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        
        resources = this.getResources();
    preferences =
              PreferenceManager.getDefaultSharedPreferences(this);
    editor = preferences.edit();
        
        //Gets filter preferences and checks if there is an account saved. If so, then a call to the server is made to get the current account
        retrievePreferences();

      //Get the search bar and then hide it. It is only displayed on search click
      txtSearch = ((EditText)findViewById(R.id.searchText));
      
      
      //Setup customlayout for posts listview
        mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        
        parameters = new SelectionParameters(null, null, filter_city, filter_music_cat, filter_business_cat, filter_food_cat, "");   
    Da = new DataAccess(Home.this, onResponseListener, parameters);
        Da.execute("6");
       

    }
  
  //Gets filter preferences and checks if there is an account saved. If so, then a call to the server is made to get the current account
  protected void retrievePreferences()
  {
        //Try reading saved preferences...see if an account exists.
        // If so then set the current account id, call the server and return the current account
        
        //Get filters saved in preferences
        filter_city = preferences.getString("city", "n/a");
      filter_music_cat = preferences.getBoolean("music_cat", true);
      filter_business_cat = preferences.getBoolean("business_cat", true);
      filter_food_cat = preferences.getBoolean("food_cat", true);
      
      //Default city to Stevens Point if its blank
      if (filter_city.equals("n/a"))
      {
        filter_city = "Stevens Point";
      }
      
        
        //Read the preferences to see if there is any saved account id
        tmpAccountId = preferences.getString("pref_account_id", "no"); 
        
        if (tmpAccountId.equals("no"))
        {
          currentAccount = null;
        }
        else
        {
          //If the value is not 'no' then it has been set so we can call the server, pass in 'tmpAccountId'
          
          Da = new DataAccess(Home.this, onResponseListener, tmpAccountId);
            Da.execute("4");    
        }
  }
  
  @Override
  protected void onResume()
  {
    //TODO set current account equal to what it was from account_maint
    if (txtSearch != null)
    {
      txtSearch.setVisibility(View.GONE);
    }
    super.onResume();

  }
  
  protected OnResponseListener onResponseListener = new OnResponseListener() {
        
      public void onFailure(String message) {
        Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
        updateCreatePostEnabledStatus(false);
      }


      public void onSuccess(Vector<Post> posts) {
        setPost(posts);
      }
      
      public void onSuccess(Account account) {
        setAccount(account);
        updateCreatePostEnabledStatus(true);
        
      }

      public void onSuccess(Boolean passed) {
      }
      
      public void onSuccess(String accountID) {
      }
    };

    private void setPost(Vector<Post> post1) {
    this.posts = post1;
    mInflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
    CustomAdapter adapter = new CustomAdapter(this, android.R.id.list, posts);
        setListAdapter(adapter);        
        getListView().setTextFilterEnabled(true);
        adapter.notifyDataSetChanged();
  }
  
  private void setAccount(Account account) {
    this.currentAccount = account;
    this.currentAccount.setAccountId(tmpAccountId);
    Toast.makeText(getApplicationContext(), "Signed in: " + account.getEmail(), Toast.LENGTH_LONG).show();
  }

@Override
  public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        this.menu = menu;
        if (currentAccount != null && currentAccount.getEmail() != null && !currentAccount.getEmail().equals(""))
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
                   this.startActivityForResult(intent, 2);
                   break;
                   
        case R.id.menu_logout:
          //Update preferences account Id
          editor.putString("pref_account_id", "no");
          editor.commit();
          updateCreatePostEnabledStatus(false);
          Toast.makeText(getApplicationContext(),
              "Successfully logged out",
                      Toast.LENGTH_SHORT).show();
          
          currentAccount = null;
          break;

        case R.id.menu_login:
          Intent intent2 = new Intent();
          intent2.setClass(this, Login.class);
          startActivityForResult(intent2, 3);
          break;
          
        case R.id.menu_my_posts:
          Intent intent3 = new Intent(); /*TODO*/
          intent3.setClass(this, MyPosts.class);
          
          if (currentAccount != null && currentAccount.getEmail() != null && !currentAccount.getEmail().equals(""))
          {
            String[] accountInfo = new String[]{String.valueOf(currentAccount.getAccountId()),
                              currentAccount.getfName(),
                              currentAccount.getlName(),
                              currentAccount.getEmail(),
                              currentAccount.getDob(),
                              currentAccount.getPassword(),
                              String.valueOf(currentAccount.getLikes()),
                              String.valueOf(currentAccount.getDislikes())};  
              
            
            intent3.putExtra("accountInfo", accountInfo);
          }
          startActivityForResult(intent3, 4);
          break;
                                       
      }   
      return super.onOptionsItemSelected(item);
      //return true;

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
      txtSearch.setVisibility(View.VISIBLE);
      txtSearch.setFocusable(true);
      txtSearch.requestFocus();
      
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

    final String filterBy2 = filterBy;
    txtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
          public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            
              if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_GO ||
                actionId == EditorInfo.IME_ACTION_NEXT) {
                performPostsSearch(filterBy2);
                  return true;
              }
              return false;
          }});
  
  }
    
  private void performPostsSearch(String filterBy)
  {
    
    if (filterBy.equals("byAccount"))
    {
      //Remove any previous search filter
      parameters.setLowDate("");
      parameters.setHighDate("");
      parameters.setTitle("");
      
      //Set new search filter
      parameters.setAccountLastName(txtSearch.getText().toString());
    }
    else if (filterBy.equals("byTitle"))
    {
      //Remove any previous search filter
      parameters.setLowDate("");
      parameters.setHighDate("");
      parameters.setAccountLastName(""); 
      
      //Set new search filter
      parameters.setTitle(txtSearch.getText().toString());
    }
    else if (filterBy.equals("byDate"))
    {
      //Remove any previous search filter
      parameters.setTitle("");
      parameters.setAccountLastName(""); 
      
      //Set new search filter
      parameters.setHighDate(txtSearch.getText().toString()); //TODO: Put date time picker??
      parameters.setLowDate(txtSearch.getText().toString());
    }
    
    Toast.makeText(getApplicationContext(),
        "Performing Search",
                Toast.LENGTH_SHORT).show();
  
    txtSearch.setVisibility(View.GONE);
    InputMethodManager imm = (InputMethodManager)getSystemService(
            getApplicationContext().INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(txtSearch.getWindowToken(), 0);
    
     Da = new DataAccess(Home.this, onResponseListener, parameters);
       Da.execute("6");
  }

  public void onListItemClick(ListView parent, View v, int position, long id) {
    //Set the selected post
      CustomAdapter adapter = (CustomAdapter) parent.getAdapter();
    Post selectedPost = adapter.getItem(position);
    

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
      /*String[] dateVals = post.getDate().split("-");
      String monthStr = getMonthString(Integer.parseInt(dateVals[1]));
      String dateStr = monthStr + " " + dateVals[2] + ", " + dateVals[0];
  
      date.setText(dateStr + " -");*/
      date.setText(MavuDateFormatter.format(post.getDate()) + " -");

      description = holder.getDescription();    
      description.setText(post.getDesc());

      return convertView;
    }
    
    /* TODO: now imlemented in static class: MavuDateFormatter so that we have consistency through other activities
     * private String getMonthString(int i)
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
    }*/
    
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

  private void openAccount()
  {
    Intent intent = new Intent();
    intent.setClass(this, Account_Maint.class);

    if (currentAccount != null && currentAccount.getEmail() != null && !currentAccount.getEmail().equals(""))
    {
      String[] accountInfo = new String[]{String.valueOf(currentAccount.getAccountId()),
                        currentAccount.getfName(),
                        currentAccount.getlName(),
                        currentAccount.getEmail(),
                        currentAccount.getDob(),
                        currentAccount.getPassword(),
                        String.valueOf(currentAccount.getLikes()),
                        String.valueOf(currentAccount.getDislikes())};  
        
      
      intent.putExtra("accountInfo", accountInfo);
    }
    
    
    startActivityForResult(intent, 1);        
  }

  
  private void createPost()
  {
    if (currentAccount != null && currentAccount.getAccountId().equals(null) || currentAccount.getAccountId().equals(""))
    {
      //means no account is selected.
      Toast.makeText(getApplicationContext(),
          "You must have an Account and be logged in order to create a post",
                  Toast.LENGTH_SHORT).show();
    }
    else
    {

      String accountId = currentAccount.getAccountId(); 
      Intent intent = new Intent();
      intent.setClass(getApplicationContext(), Create_Post.class);
      intent.putExtra("accountId", accountId);
      startActivityForResult(intent, 4);  
    }
  }

  
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data)
    {
      switch (reqCode)
      {

        case 1: //Open Account/Return Account
          if (resCode == RESULT_OK)
          {
            String[] newAccountInfo = data.getStringArrayExtra("newAccountInfo");
            currentAccount = new Account();
                currentAccount.setAccountId(newAccountInfo[0]);
                currentAccount.setfName(newAccountInfo[1]);
                currentAccount.setlName(newAccountInfo[2]);
                currentAccount.setEmail(newAccountInfo[3]);
                currentAccount.setDob(newAccountInfo[4]);
                currentAccount.setPassword(newAccountInfo[5]);
                currentAccount.setLikes(Integer.parseInt(newAccountInfo[6]));
                currentAccount.setDislikes(Integer.parseInt(newAccountInfo[7]));
                
                updateCreatePostEnabledStatus(true);
          }

          break;
        case 2: //Set preferences
          setOptionText();
          
            //TODO call data access for new set of posts with correct filters
          
          if (updatedPrefs == true)
          {
            //Only get new list if the prefs change
                Da = new DataAccess(this, onResponseListener, parameters);
                Da.execute("6");
          }
              
          break;
        case 3:
          if (resCode == RESULT_OK)
          {
            String[] newAccountInfo = data.getStringArrayExtra("newAccountInfo");
            currentAccount = new Account();
                currentAccount.setAccountId(newAccountInfo[0]);
                currentAccount.setfName(newAccountInfo[1]);
                currentAccount.setlName(newAccountInfo[2]);
                currentAccount.setEmail(newAccountInfo[3]);
                currentAccount.setDob(newAccountInfo[4]);
                currentAccount.setPassword(newAccountInfo[5]);
                currentAccount.setLikes(Integer.parseInt(newAccountInfo[6]));
                currentAccount.setDislikes(Integer.parseInt(newAccountInfo[7]));
                
              preferences =
                        PreferenceManager.getDefaultSharedPreferences(this);
              editor = preferences.edit();
                editor.putString("pref_account_id", currentAccount.getAccountId());
            editor.commit();
                updateCreatePostEnabledStatus(true);
          }
          break;
        case 4: //Create Post
          if (resCode == RESULT_OK)
          {
            //TODO: add post to list
            Da = new DataAccess(this, onResponseListener, parameters);
                Da.execute("6");
          }
      }
      
      
      super.onActivityResult(reqCode, resCode, data);

    }
    
    private void setOptionText()
    {
  
        SharedPreferences preferences =
              PreferenceManager.getDefaultSharedPreferences(this);
        
        filter_city = preferences.getString("city", "n/a");
      filter_music_cat = preferences.getBoolean("music_cat", false);
      filter_business_cat = preferences.getBoolean("business_cat", false);
      filter_food_cat = preferences.getBoolean("food_cat", false);
      
      Time now = new Time();
      now.setToNow();
      /*
      @SuppressWarnings("deprecation")
    Date lowDate = new Date(now.year, now.month, now.monthDay);
      @SuppressWarnings("deprecation")
    Date highDate = new Date(now.year, now.month, now.monthDay);
      */
      
      /*Integer lDate = now.year + now.month + now.monthDay;
      String lowDate = lDate.toString();
      Integer hDate = now.year + now.month + now.monthDay;
      String highDate = hDate.toString();
      */
      
      String lowDate = now.year + "-" + now.month + "-" + now.monthDay;
      String HighDate = now.year + "-" + now.month + "-" + now.monthDay;
      
      if (filter_city != parameters.getCity() || filter_music_cat != parameters.getMusic_category() || filter_business_cat != parameters.getBusiness_category() || filter_food_cat != parameters.getFood_category() )
      {
        updatedPrefs = true;
      }
      else 
      {
        updatedPrefs = false;
      }
      
      parameters = new SelectionParameters(null,null,filter_city, filter_music_cat, filter_business_cat, filter_food_cat,"");
      

      
    }
    
    private void updateCreatePostEnabledStatus(boolean status)
    {
      // 2 - Create post. If we update the menu layout then we need to change this
      // 4 - View MY posts
      // 5 - Logout
      // 6 - login

      menu.getItem(2).setEnabled(status);
      menu.getItem(2).setVisible(status);

      menu.getItem(4).setEnabled(status);
      menu.getItem(4).setVisible(status);
      menu.getItem(5).setEnabled(status);
      menu.getItem(5).setVisible(status);
      menu.getItem(6).setEnabled(!status);
      menu.getItem(6).setVisible(!status);
      

    
    }
     
}

