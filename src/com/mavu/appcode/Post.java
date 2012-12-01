package com.mavu.appcode;

import java.util.Date;

public class Post {
	
	private int accountID;

	private String title;
	private String desc;
	private String category;
	private String city;
	private String time;
	private Date date;
	private String address;
	
	public Post(int accountID, String title, String desc, String category, String address, String city, String time, Date date)
	{
		/*this.accountID = accountID;
		this.postID = postID;
		this.title = title;
		this.desc = desc;
		this.category = category;
		this.city = city;
		this.time = time;
		this.date = date;
		this.address = address;*/
		
		this.setAccountID(accountID);
		this.setTitle(title);
		this.setDesc(desc);
		this.setCategory(category);
		this.setAddress(address);
		this.setCity(city);
		this.setTime(time);
		this.setDate(date);
		
	}

	
//Getters
	

	public int getAccountID()
	{
		return this.accountID;
	}
	public String getTitle()
	{
		return this.title;
	}
	public String getDesc()
	{
		return this.desc;
	}
	public String getCategory()
	{
		return this.category;
	}
	public String getCity()
	{
		return this.city;
	}
	public String getTime()
	{
		return this.time;
	}
	public Date getDate()
	{
		return this.date;
	}
	public String getAddress()
	{
		return this.address;
	}
	
	
//Setters	

	
<<<<<<< HEAD
	public Post(int id, String title, String desc, String category, String address, String city, String time, Date date)
	{
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.category = category;
		this.city = city;
		this.time = time;
		this.date = date;
		this.address = address;

=======
	public void setAccountID(int id) {
		this.accountID = id;
>>>>>>> create_post.java
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	//Really just using this to pass the values from activity to activity, not used for display
	//This is my way of serializing with a comma delimiter :)
	public String toString()
	{
		return title + "," + desc + "," 
			+ category + "," + address + "," 
		    + city + "," + time + "," + date;
	
	}

}
