package com.mavu.appcode;

import java.util.Date;
import java.util.Vector;

public class DataAccess {
	
	public int GetNextPostId()
	{
		//Make call to db..use asynch task or whatever... return next id in post table
		return 0;
	}
	
	public void AddAccount(Account account)
	{
		//todo
		//write account to db
	}
	public void UpdateAccount(Account account)
	{
		//use account id to find account record in db
		//write account to db
	}
	
	public Account GetAccount(int accountId)
	{
		//Make call to db..use asynch task or whatever... end result just needs to give back the account
		return new Account();
	}
	
	public Vector<Post> GetPosts(int amount, SelectionParameters parameters)
	{	
		/*selectionParameters parameters;
		
		String querystring;
		queryString = "Select * From TB_Post WHERE "
					+ "date >= parameters.DateLow AND"
					+ "date <= parameters.DateHigh "
	    if (parameters.city != "")
	    {
	    	queryString += "AND city = " parameters.city;
	    }
		if (parameters.category != "")
		{
			queryString += "AND category = " parameters.category
		}	
		if (parameters.title != "")
		{
			queryString += "AND title = " parameters.title
		}	*/
		
		//Make call to db..use asynch task or whatever... end result just needs to give vector of posts...we need json poo parser
		return new Vector<Post>();
	}
	
	public void CreatePost(Post post)
	{
		//Write post to db..
	}
	
	public Post GetPost(int postId)
	{
		//Make call to db..use asynch task or whatever... end result just needs to give back the account
		//Post post = new Post(1, "test", "test", "food", "111 smith str", "Ste po", "8pm", new Date(2009, 1,2)); //whatever process you use..
		Post post = new Post(1, "test", "test", "food", "111 smith str", "Ste po", "8pm", "2011");
		return post;
	}
	

}
