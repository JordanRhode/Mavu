package com.mavu.appcode;

import java.util.Date;

public class SelectionParameters {

	public Date getLowDate() {
		return lowDate;
	}

	public void setLowDate(Date lowDate) {
		this.lowDate = lowDate;
	}

	public Date getHighDate() {
		return highDate;
	}

	public void setHighDate(Date highDate) {
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

	
	private Date lowDate;// --- if not specified then set to like 1900's 
	private Date highDate; //--- if not specified then set to like 9999's
	private String city;
	private Boolean business_category;
	private Boolean food_category;
	private Boolean music_category;
	private String title;
	

	@SuppressWarnings("deprecation")
	public SelectionParameters(Date lowDate2, Date highDate2, String city, Boolean music, Boolean business, Boolean food, String title)
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
			this.lowDate = new Date(0000, 1, 1);
		}
		if (this.highDate == null)
		{
			this.highDate = new Date(9999, 12, 30);	
		}
		
		
		
	}
	

}
