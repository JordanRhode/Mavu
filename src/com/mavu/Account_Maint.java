package com.mavu;

import java.util.ArrayList;
import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.LocalAccountsDataSource;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;
import com.mavu.appcode.SelectionParameters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
	private OnResponseListener responder;
	private DataAccess Da;
	private Menu menu;
	private String mode;
	private Resources resources;
	
	private LocalAccountsDataSource datasource;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_maint_layout);
       /*       
        final Button button = (Button) findViewById(R.id.btnViewPosts);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(getApplicationContext(),
        				"You clicked the button!t",
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        
        // Create new account object. If an account is passed in then that means this phone has a user already created and it will load that account
        // and prefill the values
        
        txtFName = ((EditText)findViewById(R.id.txtFName));   
    	txtLName = ((EditText)findViewById(R.id.txtLName));  	
    	txtEmail = ((EditText)findViewById(R.id.txtEmail));  	
    	txtDOB = ((EditText)findViewById(R.id.txtDOB));
    	txtPassword = ((EditText)findViewById(R.id.txtPassword));
    	txtConfirmPassword = ((EditText)findViewById(R.id.txtConfirmPassword));
    	
        // Start off with all the fields as read only. If the user hits the edit button on the menu then all fields will become active and they can update and save
    	currentAccount = new Account();
        if (getIntent().getStringArrayExtra("accountInfo") != null)
        {
        	//TODO get account info from server with accountID, need to send accountID from shared val
        	Da = new DataAccess(this, onResponseListener);
 	        Da.execute("4");
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
        	
        	
        	txtFName.setText(currentAccount.getfName());        	
        	txtLName.setText(currentAccount.getlName());
        	txtEmail.setText(currentAccount.getEmail());  	
        	txtDOB.setText(currentAccount.getDob());
        	txtPassword.setText(currentAccount.getPassword());
        	txtConfirmPassword.setText(currentAccount.getPassword());
        	
        	//Set the Title to show the user's name?
    	
        	/*The following fields will never be editable. Just to display likes and dislikes.*/
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
        }
        else
        {
        	this.menu.getItem(1).setEnabled(false);
        	this.menu.getItem(1).setVisible(false);
        	this.menu.getItem(0).setEnabled(true);
        	this.menu.getItem(0).setVisible(true);
        }
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    		case R.id.EditAccount:
    			Toast.makeText(getApplicationContext(),
    					"clicked edit account",
    	                Toast.LENGTH_SHORT).show();
    			enableFields();
    			//0 - edit 1- save
    			this.menu.getItem(0).setEnabled(false);
            	this.menu.getItem(0).setVisible(false);
    			this.menu.getItem(1).setEnabled(true);
            	this.menu.getItem(1).setVisible(true);

    			
    			break;
    			
    		case R.id.SaveAccount:
    			Toast.makeText(getApplicationContext(),
    					"clicked save account",
    	                Toast.LENGTH_SHORT).show();
    			

    			saveFields();
    	
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
    	
    	 if (fName.equals("") || lName.equals("") || email.equals("") || password.equals("") || confirmpassword.equals("") || dob.equals(""))
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
    		currentAccount.setDob(dob);
    		
     		//Need to get new Id if the account didnt exist before this
    		if (currentAccount.getAccountId() == null) //good enough? todo
    		{
    			//TODO create account
    	        Da = new DataAccess(this, onResponseListener, currentAccount);
    	        Da.execute("2");
    		}
    		else
    		{

            	
    			//TODO Update account
    			datasource = new LocalAccountsDataSource(this);
    			datasource.open();
    			datasource.updateAccount(currentAccount);
    			
    		    Da = new DataAccess(this, onResponseListener, currentAccount);
    	        Da.execute("3");
    			
    			Toast.makeText(getApplicationContext(),
    					"updated account",
    	                Toast.LENGTH_SHORT).show();
    		}
    		disableFields();
       	 	
        	//finish();
    	}
    
    }
    
    @SuppressLint("ShowToast")
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		
		public void onFailure(String message) {
			Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();
		}

		public void onSuccess(Account account) {
			
			//currentAccount.setAcccountId(da.GetNextAccountId());
			//datasource = new LocalAccountsDataSource(this);
			//datasource.open();
			//datasource.createAccount(currentAccount);
		}

		public void onSuccess(Vector<Post> posts) {
			
		}

		public void onSuccess(String accountID) {
			currentAccount.setAccountId(accountID);
			Toast.makeText(getApplicationContext(),
					"created account",
	                Toast.LENGTH_SHORT).show();
			datasource = new LocalAccountsDataSource(getApplicationContext());
			datasource.open();
			datasource.createAccount(currentAccount);
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
}

