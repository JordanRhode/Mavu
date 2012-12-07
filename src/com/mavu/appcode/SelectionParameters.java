package com.mavu.appcode;

import java.util.Date;

public class SelectionParameters {

	public String getLowDate() {
		return lowDate;
	}

	public void setLowDate(String lowDate) {
		this.lowDate = lowDate;
	}

	public String getHighDate() {
		return highDate;
	}

	public void setHighDate(String highDate) {
		this.highDate = highDate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	
	public Boolean getBusiness_category() {
		return business_category;
	}

	public void setBusiness_category(Boolean business_category) {
		this.business_category = business_category;
	}

	public Boolean getFood_category() {
		return food_category;
	}

	public void setFood_category(Boolean food_category) {
		this.food_category = food_category;
	}

	public Boolean getMusic_category() {
		return music_category;
	}

	public void setMusic_category(Boolean music_category) {
		this.music_category = music_category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	public String getAccountId() {
		return accountName;
	}

	public void setAccountId(String accountName) { //Up to you how we should do this.. check first and last for anything like?
		this.accountName = accountName;
	}

	private String lowDate;// --- if not specified then set to like 1900's 
	private String highDate; //--- if not specified then set to like 9999's
	private String city;
	private Boolean business_category;
	private Boolean food_category;
	private Boolean music_category;
	private String title;
	private String accountName;
	

	@SuppressWarnings("deprecation")
	public SelectionParameters(String lowDate2, String highDate2, String city, Boolean music, Boolean business, Boolean food, String title)
	{
		this.lowDate = lowDate2;
		this.highDate = highDate2;
		this.business_category = business;
		this.food_category = food;
		this.music_category = music;
		
		this.city = city;
		this.title = title;
		
		if (this.lowDate == null)
		{
			this.lowDate = "0001-01-01";
		}
		if (this.highDate == null)
		{
			this.highDate = "9999-01-01";
		}
		
		
		
	}
	

}
