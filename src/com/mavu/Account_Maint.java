package com.mavu;

import java.util.Date;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Account_Maint extends Activity {

	private Account currentAccount;
	
	private EditText txtFName;
	private EditText txtLName;
	private EditText txtEmail;
	private EditText txtPassword;
	private EditText txtConfirmPassword;
	private OnResponseListener responder;
	private DataAccess Da;
	private Menu menu;
	private String mode;
	
	@SuppressWarnings("deprecation")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_maint_layout);
              
        final Button button = (Button) findViewById(R.id.btnViewPosts);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Toast.makeText(getApplicationContext(),
        				"You clicked the button!t",
                        Toast.LENGTH_SHORT).show();
            }
        });

        
        // Create new account object. If an account is passed in then that means this phone has a user already created and it will load that account
        // and prefill the values
        
        // Start off with all the fields as read only. If the user hits the edit button on the menu then all fields will become active and they can update and save
    	currentAccount = new Account();
        if (getIntent().getStringArrayExtra("accountInfo") != null)
        {
        	//Fill in all the values of the currentAccount
        	String[] accountInfo = getIntent().getStringArrayExtra("accountInfo");
        	currentAccount.setAcccountId(Integer.parseInt(accountInfo[0]));
        	currentAccount.setfName(accountInfo[1]);
        	currentAccount.setlName(accountInfo[2]);
        	currentAccount.setEmail(accountInfo[3]);
        	currentAccount.setDob((java.sql.Date) new Date(accountInfo[4]));
        	currentAccount.setPassword(accountInfo[5]);
        	currentAccount.setLikes(Integer.parseInt(accountInfo[6]));
        	currentAccount.setDislikes(Integer.parseInt(accountInfo[7]));
        	
        	
            txtFName = ((EditText)findViewById(R.id.txtFName));
        	txtFName.setText(currentAccount.getfName());
        	
        	txtLName = ((EditText)findViewById(R.id.txtLName));
        	txtLName.setText(currentAccount.getlName());
        	
        	txtEmail = ((EditText)findViewById(R.id.txtEmail));
        	txtEmail.setText(currentAccount.getEmail());
        	
        	EditText txtDOB = ((EditText)findViewById(R.id.txtDate));
        	txtDOB.setText(currentAccount.getDob().toString());
        	
        	txtPassword = ((EditText)findViewById(R.id.txtPassword));
        	txtPassword.setText(currentAccount.getPassword());
        	
        	txtConfirmPassword = ((EditText)findViewById(R.id.txtConfirmPassword));
        	txtConfirmPassword.setText(currentAccount.getPassword());
        	
        	//Set the Title to show the user's name?
    	
        	/*The following fields will never be editable. Just to display likes and dislikes.*/
        	TextView lblLikes = ((TextView)findViewById(R.id.lblLikes));
        	lblLikes.setText(currentAccount.getLikes());
        	TextView lblDislikes = ((TextView)findViewById(R.id.lblDislikes));
        	lblDislikes.setText(currentAccount.getDislikes());
        	
        	disableFields();
        }
        else
        {
        	mode = "edit";
        }

        
        
        Toast.makeText(getApplicationContext(),
				"No account was found. You must create a new Account",
                Toast.LENGTH_SHORT).show();
	}
	
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.account_maint_menu, menu);
        this.menu = menu;
        
        if (mode.equals("Edit"))
        {
        	this.menu.removeItem(R.id.SaveAccount);
        }
        else
        {
        	this.menu.removeItem(R.id.EditAccount);
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
    		
    		
    		currentAccount.setfName(fName);
    		currentAccount.setlName(lName);
    		currentAccount.setEmail(email);
    		currentAccount.setPassword(confirmpassword);
    		//todo currentAccount.setDob(new Date(dob));
    		
     		//Need to get new Id if the account didnt exist before this
    		if (currentAccount.getAcccountId() < 1) //good enough? todo
    		{
    			//currentAccount.setAcccountId(da.GetNextAccountId());
    	        Da = new DataAccess(responder, currentAccount);
    	        Da.execute("2");
    		}
    		else
    		{
    			//Update account
    		    Da = new DataAccess(responder, currentAccount);
    	        Da.execute("3");
    		}
    		disableFields();    		
    	}
    
    }
    
    private void enableFields()
    {
    	
	    txtFName.setEnabled(true);
	    txtLName.setEnabled(true);
	    txtEmail.setEnabled(true);
	    txtPassword.setEnabled(true);
	    txtConfirmPassword.setEnabled(true);
	    mode = "edit";
    }
    
    private void disableFields()
    {
    	 txtFName.setEnabled(false);
    	 txtLName.setEnabled(false);
    	 txtEmail.setEnabled(false);
    	 txtPassword.setEnabled(false);
    	 txtConfirmPassword.setEnabled(false);
    	mode = "read";
    }
}

