package com.mavu.appcode;

import java.sql.Date;

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
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
	private String category;
	private String title;
	

	@SuppressWarnings("deprecation")
	public SelectionParameters(Date lowDate, Date highDate, String city, String category, String title)
	{
		this.lowDate = lowDate;
		this.highDate = highDate;
		this.category = category;
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
