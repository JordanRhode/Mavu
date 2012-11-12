package com.mavu.appcode;

import java.util.Date;

public class Post {
	
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
	public String getDate()
	{
		return this.date;
	}
	public String getAddress()
	{
		return this.address;
	}
	
	private int id;
	private String title;
	private String desc;
	private String category;
	private String city;
	private String time;
	private String date;
	private String address;
	
	
	public Post(int id, String title, String desc, String category, String address, String city, String time, String date)
	{
		this.id = id;
		this.title = title;
		this.desc = desc;
		this.category = category;
		this.city = city;
		this.time = time;
		this.date = date;
		this.address = address;
	}
	
	//Really just using this to pass the values from activity to activity, not used for display
	//This is my way of serializing with a comma delimiter :)
	public String toString()
	{
		return String.valueOf(id) + "," + title + "," + desc + "," 
								  + category + "," + address + "," 
								  + city + "," + time + "," + date;
	
	}

}
