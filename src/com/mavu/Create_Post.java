package com.mavu;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Vector;

import com.mavu.Account_Maint.DatePickerFragment;
import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class Create_Post extends Activity {
  
   
   int postID;
     String title;
     String desc;
     String category;
     String address;
     String city;
     EditText txtTime;
     EditText txtDate;
     
     Date theDate;
     private String accountID;
     private String storedDate = "";
     private String storedTime = "";
     private Boolean storedTimePm = false;
     private OnResponseListener responder;
  
  public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_post_layout);
             
        accountID = getIntent().getStringExtra("accountId");
        
        
    txtTime = ((EditText) findViewById(R.id.txtTime));  
    txtDate = ((EditText) findViewById(R.id.txtDate));
    
    Time now = new Time();
      now.setToNow();
      
        int year = now.year;
        int month = now.month;
        int day = now.monthDay;
        setNewDate(year, month, day);
        
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
          city = ((EditText) findViewById(R.id.txtCity)).getText().toString(); //TODO: shouldnt need these anymore

          
          if (title.equals("") || desc.equals("") || category.equals("") || address.equals("") || city.equals("") || storedDate.equals("") || storedTime.equals(""))
          {
            Toast.makeText(getApplicationContext(),
                 "Please fill in all the values",
                 Toast.LENGTH_SHORT).show();
          }
          else
          {
            //Create post - accountId
            //Post newPost = new Post(accountID, title, desc, category, address, city, time, date);
            Post newPost = new Post(accountID, title, desc, category, address, city, storedTime, storedDate);
            DataAccess Da = new DataAccess(this, onResponseListener, newPost);
                Da.execute("5");
            
            Toast.makeText(getApplicationContext(),
                 "Post successfully saved",
                 Toast.LENGTH_SHORT).show();
            
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
        Toast.makeText(getApplicationContext(), "Post Created", Toast.LENGTH_LONG).show();
        Intent resultIntent = getIntent();
        //resultIntent.putExtra("newAccountInfo", newAccountInfo);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
      }
      
      public void onSuccess(String accountID) {
      }
    };
    
    
    @SuppressLint("NewApi")
    public void showDatePickerDialog(View v) {
        //Display: 12/12/2000
        
        String date = storedDate; //txtDOB.getText().toString();
        if (!date.equals(""))
        {
          String[] dateVals = date.split("-");
          
          DatePickerFragment.month = Integer.parseInt(dateVals[1]);
          DatePickerFragment.day = Integer.parseInt(dateVals[2]);
          DatePickerFragment.year = Integer.parseInt(dateVals[0]);
          
          if (DatePickerFragment.month < 0)
          {
            DatePickerFragment.month = 0;
          }
    
        }
        
          DialogFragment newFragment = new DatePickerFragment();
          newFragment.setTargetFragment(newFragment, 1);
          newFragment.show(getFragmentManager(), "datePicker");
          
      }
      
      private void setNewDate(int year, int month, int day)
      {
        Boolean failed = false;
        Time now = new Time();
        now.setToNow();
        if (year < now.year)
        {
              failed = true;
        }
        else if(year == now.year)
        {
          if (month < now.month)
          {
            failed = true;
          }
          else if(month == now.month)
          {
            if(day < now.monthDay)
            {
              failed = true;
            }
          }
        }
        
        
        if (failed)
        {
          Toast.makeText(getApplicationContext(),
              "Event is set to a date in the past. Please choose another date",
                      Toast.LENGTH_SHORT).show();
        }
        else
        {
        
        String monthStr = getMonthString(month);
        String tMonth = String.valueOf(month);
        String tDay = String.valueOf(day);
        
        txtDate.setText(monthStr + " " + day + ", " + year);
        
        if (month < 10)
        {
          tMonth = "0" + tMonth;
        }
        if (day < 10)
        {
          tDay = "0" + tDay;
        }
        
        storedDate = year + "-" + tMonth + "-" + tDay;
        }
        
      
      }
      
    @SuppressLint("NewApi")
    public static class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    
      public static int year ;
      public static int month;
      public static int day;
      @Override
      public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
        
    
      final Calendar c = Calendar.getInstance();
      int year = DatePickerFragment.year; // c.get(Calendar.YEAR);
      int month = DatePickerFragment.month; //c.get(Calendar.MONTH);
      int day =   DatePickerFragment.day; // c.get(Calendar.DAY_OF_MONTH);
      
      // Create a new instance of DatePickerDialog and return it
      return new DatePickerDialog(getActivity(), this, year, month, day);
      }
      
      public void onDateSet(DatePicker view, int year, int month, int day) {
      // Do something with the date chosen by the user
        DatePickerFragment.year = year;
        DatePickerFragment.month = month;
        DatePickerFragment.day = day;

        Create_Post callingActivity = (Create_Post) getActivity();
            callingActivity.setNewDate(year, month, day);

        //Intent data = null;
        
        //getTargetFragment().onActivityResult(getTargetRequestCode(), 2, data);  
      }
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
    
    /*---------------------------------------------Time Picker----------------------------------------*/
    @SuppressLint("NewApi")
    public void showTimePickerDialog(View v) {
        //Display: 12/12/2000
        
        String time = storedTime; //txtDOB.getText().toString();
        if (!time.equals(""))
        {
          String[] dateVals = time.split(":");
          String tmpMin = "";
          
          TimePickerFragment.hour = Integer.parseInt(dateVals[0]);
          tmpMin = dateVals[1].split(" ")[0];
          
          TimePickerFragment.minute = Integer.parseInt(tmpMin);
    
        }
        
          DialogFragment newFragment = new TimePickerFragment();
          newFragment.setTargetFragment(newFragment, 2);
          newFragment.show(getFragmentManager(), "timePicker");
          
      }

    @SuppressLint("NewApi")
    public static class TimePickerFragment extends DialogFragment
      implements OnTimeSetListener {
    
      public static int hour ;
      public static int minute;
      @Override
      public Dialog onCreateDialog(Bundle savedInstanceState) {
      // Use the current date as the default date in the picker
        
        int hour = TimePickerFragment.hour;
        int minute = TimePickerFragment.minute;
    
      
      // Create a new instance of DatePickerDialog and return it
      return new TimePickerDialog(getActivity(), this , hour, minute, false );
      }
      

      public void onTimeSet(TimePicker arg0, int hour, int minute) {
        Create_Post callingActivity = (Create_Post) getActivity();
        
        TimePickerFragment.hour = hour;
        TimePickerFragment.minute = minute;
        
            callingActivity.setNewTime(hour, minute);

        
      }
    }
    
      
      private void setNewTime(int hour, int minute)
      {

        String hourStr = String.valueOf(hour);
        String minStr = String.valueOf(minute);
        if (hour > 12)
        {
          hourStr = String.valueOf(hour - 12);
          storedTimePm = true;
        }
        else
        {
          storedTimePm = false;
        }
        
        if (minute < 10)
        {
          minStr = "0" + minStr;
        }
        
        if (storedTimePm)
        {
          minStr += " PM";
        }
        else
        {
          minStr += " AM";
        }
        
        txtTime.setText(hourStr + ":" + minStr);
        storedTime = hour + ":" + minute;
      
      }
}
