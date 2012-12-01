package com.mavu.appcode;

import java.util.Date;
import java.util.Vector;


import android.content.Context;
import android.os.AsyncTask;

public class DataAccess extends AsyncTask<String, Integer, Boolean> {
	

	private OnResponseListener responder;
	
	public DataAccess(OnResponseListener responder){
		this.responder = responder;
	}
	
	public DataAccess(Context context, OnResponseListener responder){
		this.responder = responder;
	}
	
	@Override
	protected Boolean doInBackground(String... params) {
		
		switch (Integer.parseInt(params[0])) {
		case 1:
			//TODO
			//write account to db
			break;
		case 2:
			//TODO update account
			break;
		case 3:
			//TODO get account data
			break;
		case 4:
			//TODO Get posts from database
			break;
		case 5:
			//TODO create post
			break;
		case 6:
			//TODO get individual post based on post id
			break;
		default:
			break;
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean result){
//		if(this.progressDialog.isShowing()) {
//			this.progressDialog.dismiss();
//		}
		if(result)
			responder.onSuccess();
		else {
			responder.onFailure("Fail");
		}
	}
	
	public interface OnResponseListener {
		public void onSuccess();
		public void onFailure(String message);
	}
	
	
//	public void AddAccount(Account account)
//	{
//		//todo
//		//write account to db
//	}
//	public void UpdateAccount(Account account)
//	{
//		//use account id to find account record in db
//		//write account to db
//	}
//	
//	public Account GetAccount(int accountId)
//	{
//		//Make call to db..use asynch task or whatever... end result just needs to give back the account
//		return new Account();
//	}
//	
//	public Vector<Post> GetPosts(int amount, SelectionParameters parameters)
//	{	
//		/*selectionParameters parameters;
//		
//		String querystring;
//		queryString = "Select * From TB_Post WHERE "
//					+ "date >= parameters.DateLow AND"
//					+ "date <= parameters.DateHigh "
//	    if (parameters.city != "")
//	    {
//	    	queryString += "AND city = " parameters.city;
//	    }
//		if (parameters.category != "")
//		{
//			queryString += "AND category = " parameters.category
//		}	
//		if (parameters.title != "")
//		{
//			queryString += "AND title = " parameters.title
//		}	*/
//		
//		//Make call to db..use asynch task or whatever... end result just needs to give vector of posts...we need json poo parser
//		return new Vector<Post>();
//	}
//	
//	public void CreatePost(Post post)
//	{
//		//Write post to db..
//	}
//	
//	public Post GetPost(int postId)
//	{
//		//Make call to db..use asynch task or whatever... end result just needs to give back the account
//		Post post = new Post(1, "test", "test", "food", "111 smith str", "Ste po", "8pm", new Date(2009, 1,2)); //whatever process you use..
//		
//		return post;
//	}

	

}
