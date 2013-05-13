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
import android.widget.TextView;
import android.widget.Toast;

public class Post_View extends Activity {
  
  private Post post;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_view_layout);
        
        /*TODO: Add functionality to handle view mode and edit mode*/
        String[] postInfo = getIntent().getStringArrayExtra("postInfo");
        post = new Post(postInfo[0], 
            postInfo[1], postInfo[2], postInfo[3], postInfo[4], postInfo[5], postInfo[6], postInfo[7]);
        
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
     
     
     String dateVals[] = post.getDate().split("-");
     
     int month = Integer.parseInt(dateVals[1]);
     String monthStr = getMonthString(month);
      
      
    //Date setting
     EditText date = ((EditText) findViewById(R.id.txtDate));
     date.setText(monthStr + " " + dateVals[2] + ", " + dateVals[0]);

        
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
          openAccount();
          break;
               
      }   
      //return super.onOptionsItemSelected(item);
      return true;

    }
    
    protected void openAccount()
    {
      String accountId = post.getAccountID();
      Intent intent = new Intent();
    intent.setClass(this, Account_View.class);
    intent.putExtra("accountId", accountId);
    startActivity(intent);    
      
    }
    
  private String getMonthString(int i)

  {
    String month = "";
        
    switch (i)
    {
      case 0: month = "January"; break;
      case 1: month = "February"; break;
      case 2: month = "March"; break;
      case 3: month = "April"; break;
      case 4: month = "May"; break;
      case 5: month = "June"; break;
      case 6: month = "July"; break;
      case 7: month = "August"; break;
      case 8: month = "September"; break;
      case 9: month = "October"; break;
      case 10: month = "November"; break;
      case 11: month = "December"; break;         
    }
    return month;
  }
  

}
