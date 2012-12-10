package com.mavu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.LocalAccountsDataSource;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;
import com.mavu.appcode.SelectionParameters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Account_Maint extends Activity {

	private Account currentAccount;
	
	private EditText txtFName;
	private EditText txtLName;
	private EditText txtEmail;
	private EditText txtPassword;
	private EditText txtConfirmPassword;
	private EditText txtDOB;
	private Button btnPickDate;
	private OnResponseListener responder;
	private DataAccess Da;
	private Menu menu;
	private String mode;
	private Resources resources;
	private SharedPreferences.Editor editor;
	private String storedDate = "";

	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_maint_layout);
        
        //Set class preference variables for later use
		resources = this.getResources();
		SharedPreferences preferences =
        	    PreferenceManager.getDefaultSharedPreferences(this);
		editor = preferences.edit();
        
        
        // Create new account object. If an account is passed in then that means this phone has a user already created and it will load that account and prefill the values        
        txtFName = ((EditText)findViewById(R.id.txtFName));   
    	txtLName = ((EditText)findViewById(R.id.txtLName));  	
    	txtEmail = ((EditText)findViewById(R.id.txtEmail));  	
    	txtDOB = ((EditText)findViewById(R.id.txtDOB));
    	txtPassword = ((EditText)findViewById(R.id.txtPassword));
    	txtConfirmPassword = ((EditText)findViewById(R.id.txtConfirmPassword));
    	btnPickDate = ((Button)findViewById(R.id.btnSetDate));
    	
        // Start off with all the fields as read only. If the user hits the edit button on the menu then all fields will become active and they can update and save
    	// "accountInfo" will have real account data from the server if an account exists on this phone
    	currentAccount = new Account();
    	int year = 0;
        int month = 0;
        int day = 0; 
        if (getIntent().getStringArrayExtra("accountInfo") != null)
        {
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
        	
        	String[] datevals = accountInfo[4].split("-");
        	year = Integer.parseInt(datevals[0]);
            month = Integer.parseInt(datevals[1]); // todo -1
            day = Integer.parseInt(datevals[2]);
        	
        	
        	txtFName.setText(currentAccount.getfName());        	
        	txtLName.setText(currentAccount.getlName());
        	txtEmail.setText(currentAccount.getEmail());  	
        	txtDOB.setText(currentAccount.getDob());
        	txtPassword.setText(currentAccount.getPassword());
        	txtConfirmPassword.setText(currentAccount.getPassword());
        	
        	//Set the Title to show the user's name?
    	
        	/*The following fields will never be editable. Just to display likes and dislikes.*/
    		//TODO - implement likes and dislikes
			//currentAccount.setLikes();
			//currentAccount.setDislikes();
        	TextView lblLikes = ((TextView)findViewById(R.id.lblLikes));
        	if (currentAccount.getLikes() > 0){
        		lblLikes.setText(currentAccount.getLikes());
        	}
        	TextView lblDislikes = ((TextView)findViewById(R.id.lblDislikes));
        	if (currentAccount.getDislikes() > 0){
        		lblDislikes.setText(currentAccount.getDislikes());
        	}
        	
        	disableFields();
        }
        else
        {
        	mode = "edit";
            Toast.makeText(getApplicationContext(),
    				"No account was found. You must create a new Account",
                    Toast.LENGTH_SHORT).show();
        }
        
        //Dumb conversion Stuff
        if (month < 0)
        {
        	month = 0;
        }
        setNewDate(year, month, day);
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_maint_menu, menu);
        this.menu = menu;
        
        //0 - edit 1- save
        if (mode.equals("edit"))
        {
        	this.menu.getItem(0).setEnabled(false);
        	this.menu.getItem(0).setVisible(false);
        	this.menu.getItem(1).setEnabled(true);
        	this.menu.getItem(1).setVisible(true);
        	btnPickDate.setVisibility(View.VISIBLE);
        }
        else
        {
        	this.menu.getItem(1).setEnabled(false);
        	this.menu.getItem(1).setVisible(false);
        	this.menu.getItem(0).setEnabled(true);
        	this.menu.getItem(0).setVisible(true);
        	btnPickDate.setVisibility(View.INVISIBLE);
        }
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    		case R.id.EditAccount:
    			enableFields();
    			//0 - edit 1- save
    			this.menu.getItem(0).setEnabled(false);
            	this.menu.getItem(0).setVisible(false);
    			this.menu.getItem(1).setEnabled(true);
            	this.menu.getItem(1).setVisible(true);
            	btnPickDate.setVisibility(View.VISIBLE);
    			
    			break;
    			
    		case R.id.SaveAccount:
    			saveFields();
    			btnPickDate.setVisibility(View.INVISIBLE);
    	
    	}  	
		return true;
    }
    
    private void saveFields()
    {

    	String fName = ((EditText) findViewById(R.id.txtFName)).getText().toString();
    	String lName = ((EditText) findViewById(R.id.txtLName)).getText().toString();
    	String email = ((EditText) findViewById(R.id.txtEmail)).getText().toString();
    	String password = ((EditText) findViewById(R.id.txtPassword)).getText().toString();
    	String confirmpassword = ((EditText) findViewById(R.id.txtConfirmPassword)).getText().toString();
    	String dob = ((EditText) findViewById(R.id.txtDOB)).getText().toString();
    	//Create DA object to use to save values to DB
    	
    	 if (fName.equals("") || lName.equals("") || email.equals("") || password.equals("") || confirmpassword.equals("") || storedDate.equals(""))
    	{
    		Toast.makeText(getApplicationContext(),
					"Not all fields are filled out. Cannot save.",
	                Toast.LENGTH_SHORT).show();
    	}
    	/*else if (!da.EmailIsAvailable(email))
    	{
    		Toast.makeText(getApplicationContext(),
					"This email is already being used on our server. Please enter a new email.",
	                Toast.LENGTH_SHORT).show();
    	}*/
    	else if (!password.equals(confirmpassword))
    	{
    		Toast.makeText(getApplicationContext(),
					"Passwords do not match. Please Try again",
	                Toast.LENGTH_SHORT).show();
    	}
    	else //passed validation
    	{
    		
			this.menu.getItem(1).setEnabled(false);
        	this.menu.getItem(1).setVisible(false);
			this.menu.getItem(0).setEnabled(true);
        	this.menu.getItem(0).setVisible(true);
        	mode = "read";
        	
    		currentAccount.setfName(fName);
    		currentAccount.setlName(lName);
    		currentAccount.setEmail(email);
    		currentAccount.setPassword(confirmpassword);
    		//currentAccount.setDob(dob); 
    		currentAccount.setDob(storedDate);
    		
     		//Need to get new Id if the account didnt exist before this
    		if (currentAccount.getAccountId() == null) //good enough? todo
    		{
    			//Create account
    	        Da = new DataAccess(this, onResponseListener, currentAccount);
    	        Da.execute("2");
    		}
    		else
    		{	//Update Account
    		    Da = new DataAccess(this, onResponseListener, currentAccount);
    	        Da.execute("3");
    		}
    		//disableFields();
    	}
    
    }
    
    @SuppressLint("ShowToast")
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		
		public void onFailure(String message) {
			Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
		}

		public void onSuccess(Account account) {
		}

		public void onSuccess(Vector<Post> posts) {
		}
	
		public void onSuccess(Boolean passed) {
			//Will be called on UpdateAccount(case:3)
			Toast.makeText(getApplicationContext(),
					"Account Update Successful",
	                Toast.LENGTH_SHORT).show();
			//Pass back values to Home and close this activity. Resource: http://stackoverflow.com/questions/1124548/how-to-pass-the-values-from-one-activity-to-previous-activity
			String[] newAccountInfo = new String[]{String.valueOf(currentAccount.getAccountId()),
					currentAccount.getfName(),
					currentAccount.getlName(),
					currentAccount.getEmail(),
					currentAccount.getDob(),
					currentAccount.getPassword(),
					"0",
					"0"};
			
			Intent resultIntent = getIntent();
			resultIntent.putExtra("newAccountInfo", newAccountInfo);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();
		}

		public void onSuccess(String accountID) {
			//Will be called on CreateAccount(case:2)
			currentAccount.setAccountId(accountID);
			Toast.makeText(getApplicationContext(),
					"created account" + accountID, //TODO remove this accountID from toast
	                Toast.LENGTH_SHORT).show();
			
			//Update preferences account Id
			editor.putString("pref_account_id", accountID);
			editor.commit();
			
			//Pass back values to Home and close this activity. Resource: http://stackoverflow.com/questions/1124548/how-to-pass-the-values-from-one-activity-to-previous-activity
			String[] newAccountInfo = new String[]{String.valueOf(currentAccount.getAccountId()),
					currentAccount.getfName(),
					currentAccount.getlName(),
					currentAccount.getEmail(),
					currentAccount.getDob(),
					currentAccount.getPassword(),
					"0",
					"0"};
			
			Intent resultIntent = getIntent();
			resultIntent.putExtra("newAccountInfo", newAccountInfo);
			setResult(Activity.RESULT_OK, resultIntent);
			finish();		
		}
	};
    
    private void enableFields()
    {
    	
	    txtFName.setEnabled(true);
	    txtLName.setEnabled(true);
	    txtEmail.setEnabled(true);
	    txtPassword.setEnabled(true);
	    txtConfirmPassword.setEnabled(true);
	    txtDOB.setEnabled(true);
	    mode = "edit";
    }
    
    private void disableFields()
    {
    	 txtFName.setEnabled(false);
    	 txtLName.setEnabled(false);
    	 txtEmail.setEnabled(false);
    	 txtPassword.setEnabled(false);
    	 txtConfirmPassword.setEnabled(false);
    	 txtDOB.setEnabled(false);

    	mode = "read";
    	

    }
    
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
    	String monthStr = getMonthString(month);
    	String tMonth = String.valueOf(month);
    	String tDay = String.valueOf(day);
    	
    	txtDOB.setText(monthStr + " " + day + ", " + year);
    	
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

			Account_Maint callingActivity = (Account_Maint) getActivity();
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
	
	
}

