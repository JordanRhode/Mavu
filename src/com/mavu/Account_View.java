package com.mavu;

import java.util.Vector;

import com.mavu.appcode.Account;
import com.mavu.appcode.DataAccess;
import com.mavu.appcode.Post;
import com.mavu.appcode.DataAccess.OnResponseListener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Account_View extends Activity {
	
	private Account account;
	private DataAccess Da;
	private String accountId;
	private TextView lblLikes;
	private TextView lblDislikes;
	

	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_view_layout);
        
        accountId = getIntent().getStringExtra("accountId");
        
    	Da = new DataAccess(this, onResponseListener, accountId);
        Da.execute("4"); 
	}
	
	private void setAccount(Account account) {
		this.account = account;
		this.account.setAccountId(this.accountId);
		
		EditText txtFName = ((EditText) findViewById(R.id.txtFName));
		txtFName.setText(account.getfName());
		
		EditText txtLName = ((EditText) findViewById(R.id.txtLName));
		txtLName.setText(account.getlName());
		
		EditText txtEmail = ((EditText) findViewById(R.id.txtEmail));
		txtEmail.setText(account.getEmail());
		
		EditText txtDOB = ((EditText) findViewById(R.id.txtDOB));
		txtDOB.setText(account.getDob());
		
		lblLikes = ((TextView) findViewById(R.id.lblLikes));
		//lblLikes.setText(account.getLikes());
		lblDislikes = ((TextView) findViewById(R.id.lblDislikes));
		//lblDislikes.setText(account.getDislikes());
	}
	protected OnResponseListener onResponseListener = new OnResponseListener() {
		
		public void onFailure(String message) {
			Toast.makeText(getApplicationContext(), "Failure, unalbe to find account: " + message, Toast.LENGTH_LONG).show();
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
