package com.mavu.appcode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DataAccess extends AsyncTask<String, Integer, Boolean> {
	
	private android.app.ProgressDialog progressDialog;
	private OnResponseListener responder;
	private SelectionParameters parameters;
	private Post postVals;
	private Account account;
	private Vector<Post> posts;
	
	private int currentAction;
	
	public DataAccess(android.app.ProgressDialog progressDialog,OnResponseListener responder){
		this.responder = responder;
		this.progressDialog = progressDialog;
	}
	
	
	public DataAccess(Context context, OnResponseListener responder, String progressMessage, SelectionParameters parameters){
		this.responder = responder;
		this.parameters = parameters;
		this.progressDialog = new ProgressDialog(context, progressMessage);
	}
	
	public DataAccess(Context context, OnResponseListener responder, String progressMessage, Post postVals){
		this.responder = responder;
		this.postVals = postVals;
		this.progressDialog = new ProgressDialog(context, progressMessage);
	}
	
	public DataAccess(Context context, OnResponseListener responder, String progressMessage, Account account){
		this.responder = responder;
		this.account = account;
		this.progressDialog = new ProgressDialog(context, progressMessage);
	}
	
	@Override
	protected void onPreExecute(){
		progressDialog.show();
	}
	
	
	@Override
	protected Boolean doInBackground(String... params) {
		/*
		 * case 1: Login
		 * case 2: Create Account
		 * case 3: Update Account
		 * case 4: Get Account (By account Id)
		 * case 5: Create Post
		 * case 6: Get Posts
		 * case 7: See if account username is taken
		 * case 8: Get Account (By email)
		 */
		List<NameValuePair> nameValuePair;
		InputStream is = null;
		String json = "";
		HttpClient httpClient;
		HttpPost httpPost;

		JSONObject jObj;
		
		currentAction = Integer.parseInt(params[0]);
		switch (Integer.parseInt(params[0])) {
		case 1:
			//TODO Login
			//email sent as 2nd parameter value and password sent as 3rd
			nameValuePair = new ArrayList<NameValuePair>(3);
			nameValuePair.add(new BasicNameValuePair("action", "Login"));
			nameValuePair.add(new BasicNameValuePair("email", params[1]));
			nameValuePair.add(new BasicNameValuePair("password", params[2]));
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			}	catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			/*
			try {
				JSONArray jArray = new JSONArray(json);
				//JSONObject jObj = jArray.getJSONObject(0);
				//TODO put values in a shared preferences object
			}	catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}*/
			return true;
		case 2:
			//TODO Create Account
			nameValuePair = new ArrayList<NameValuePair>(6);
			nameValuePair.add(new BasicNameValuePair("action", "Create Account"));
			nameValuePair.add(new BasicNameValuePair("fname", account.getfName()));
			nameValuePair.add(new BasicNameValuePair("lname", account.getlName()));
			nameValuePair.add(new BasicNameValuePair("email", account.getEmail()));
			nameValuePair.add(new BasicNameValuePair("password", account.getPassword()));
			nameValuePair.add(new BasicNameValuePair("dob", account.getDob().toString()));
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case 3:
			//TODO update account
			nameValuePair = new ArrayList<NameValuePair>(7);
			nameValuePair.add(new BasicNameValuePair("action", "Update Account"));
			nameValuePair.add(new BasicNameValuePair("account_id", String.valueOf(account.getAcccountId())));
			nameValuePair.add(new BasicNameValuePair("fname", account.getfName()));
			nameValuePair.add(new BasicNameValuePair("lname", account.getlName()));
			nameValuePair.add(new BasicNameValuePair("email", account.getEmail()));
			nameValuePair.add(new BasicNameValuePair("password", account.getPassword()));
			nameValuePair.add(new BasicNameValuePair("dob", account.getDob().toString()));
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case 4:
			//TODO get account
			nameValuePair = new ArrayList<NameValuePair>(2);
			nameValuePair.add(new BasicNameValuePair("action", "Get Account"));
			nameValuePair.add(new BasicNameValuePair("account_id", params[1]));
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
			}	catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			
			try {
				JSONArray jArray = new JSONArray(json);
				jObj = jArray.getJSONObject(0);
				//TODO make vector list of json data vals
			}	catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			return true;
		case 5:
			//TODO create post
			nameValuePair = new ArrayList<NameValuePair>(10);
			nameValuePair.add(new BasicNameValuePair("action", "Create Post"));
			nameValuePair.add(new BasicNameValuePair("title", postVals.getTitle()));
			nameValuePair.add(new BasicNameValuePair("account_id", postVals.getAccountID()));
			nameValuePair.add(new BasicNameValuePair("description", postVals.getDesc()));
			nameValuePair.add(new BasicNameValuePair("category", postVals.getCategory()));
			nameValuePair.add(new BasicNameValuePair("city", postVals.getCity()));
			nameValuePair.add(new BasicNameValuePair("time", postVals.getTime()));
			nameValuePair.add(new BasicNameValuePair("date", postVals.getDate().toString()));
			nameValuePair.add(new BasicNameValuePair("address", postVals.getAddress()));
			//TODO do we need zipcode?
			//nameValuePair.add(new BasicNameValuePair("zipcode", postVals.getZip()));

			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		case 6:
			//Get Posts
			nameValuePair = new ArrayList<NameValuePair>();
			nameValuePair.add(new BasicNameValuePair("action", "Get Posts"));
			nameValuePair.add(new BasicNameValuePair("lowDate", parameters.getLowDate().toString()));
			nameValuePair.add(new BasicNameValuePair("highDate", parameters.getHighDate().toString()));
			
			if (!parameters.getCity().equals(""))
			{
				nameValuePair.add(new BasicNameValuePair("city", parameters.getCity().toString()));
			}
			if (parameters.getMusic_category())
			{
				nameValuePair.add(new BasicNameValuePair("music", parameters.getMusic_category().toString()));
			}
			if (parameters.getFood_category())
			{
				nameValuePair.add(new BasicNameValuePair("food", parameters.getFood_category().toString()));
			}
			if (parameters.getBusiness_category())
			{
				nameValuePair.add(new BasicNameValuePair("business", parameters.getBusiness_category().toString()));
			}
			if (!parameters.getTitle().equals(""))
			{
				nameValuePair.add(new BasicNameValuePair("title", parameters.getTitle().toString()));
			}
			
			httpClient = new DefaultHttpClient();
			httpPost = new HttpPost("http://mavu.jordanrhode.com/user_actions.php");	
			
			
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
				HttpResponse response = httpClient.execute(httpPost);
				HttpEntity entity = response.getEntity();
				is = entity.getContent();
			}	catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}	catch (ClientProtocolException e) {
				e.printStackTrace();
			}	catch (IOException e) {
				e.printStackTrace();
			}
			
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				is.close();
				json = sb.toString();
				//Log.i("json data", json);
			}	catch (Exception e) {
				Log.e("Buffer Error", "Error converting result " + e.toString());
			}
			posts = new Vector<Post>();
			try {
				
				JSONArray jArray = new JSONArray(json);
				Log.i("array leng", String.valueOf(jArray.length()));
				for(int i=0; i<jArray.length(); i++)
				{
					jObj = jArray.getJSONObject(i);
					Post post = new Post(jObj.get("post_id").toString(), jObj.getString("title"), jObj.getString("description"), jObj.getString("category"),jObj.getString("address"), jObj.getString("city"), jObj.getString("time"), jObj.get("date").toString());
		        	posts.add(post);
				}
				
			}	catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
			return true;
		case 7:
			//TODO see if username is taken
			break;

		default:
			return false;
		}
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result){
		if(this.progressDialog.isShowing()) {
			this.progressDialog.dismiss();
		}
		if(result)
		{
			switch (currentAction)
			{
				case 2: //Create Account - we want to return the account so that we can get the newly assigned account Id
					responder.onSuccess(account);
					break;
				case 4: //Get Account
					responder.onSuccess(account);
					break;
				case 6: 
					responder.onSuccess(posts);
					break;			
			}
			
		}
		else {
			responder.onFailure("Fail");
		}
	}
	
	public interface OnResponseListener {
		public void onSuccess(Vector<Post> posts);
		public void onSuccess(Account account);
		public void onFailure(String message);
	}
	
	public class ProgressDialog extends android.app.ProgressDialog {
		public ProgressDialog(Context context, String progressMessage){
			super(context);
			setCancelable(false);
			setMessage(progressMessage);
		}
	}
}
