package com.mavu;

import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_login, menu);
        return true;
    }
    

@Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
    	switch (item.getItemId())
    	{
    		case R.id.menu_login:
    			String email = ((EditText) findViewById(R.id.txtLoginEmail)).getText().toString();
    			String pass = ((EditText) findViewById(R.id.txtLoginPass)).getText().toString();
    			
    			
    		    DataAccess Da = new DataAccess(this, onResponseListener);
    	        Da.execute("1", email, pass);
    			break;
    			
    		
    			
    		             			  		       
    	} 	
    	return super.onOptionsItemSelected(item);
    	//return true;

    }

	private void setAccount(Account account)
	{
		//Pass back values to Home and close this activity. Resource: http://stackoverflow.com/questions/1124548/how-to-pass-the-values-from-one-activity-to-previous-activity
		String[] newAccountInfo = new String[]{account.getAccountId(),
				account.getfName(),
				account.getlName(),
				account.getEmail(),
				account.getDob(),
				account.getPassword(),
				String.valueOf(account.getLikes()),
				String.valueOf(account.getDislikes())};
		
		Intent resultIntent = getIntent();
		resultIntent.putExtra("newAccountInfo", newAccountInfo);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();	
	}
protected OnResponseListener onResponseListener = new OnResponseListener() {
	
	public void onFailure(String message) {
		Toast.makeText(getApplicationContext(), "Failure, message: " + message, Toast.LENGTH_LONG).show();	
	}

	public void onSuccess(Vector<Post> posts) {
		
	}
	
	public void onSuccess(Account account) {
		setAccount(account);

	}

	public void onSuccess(Boolean passed) {
	}
	
	public void onSuccess(String accountID) {
	}
};

}
