package com.mavu.appcode;

import java.util.Date;

public class Account {

	
	
	public int getAcccountId() {
		return acccountId;
	}

	public void setAcccountId(int acccountId) {
		this.acccountId = acccountId;
	}

	public String getfName() {
		return fName;
	}

	public void setfName(String fName) {
		this.fName = fName;
	}

	public String getlName() {
		return lName;
	}

	public void setlName(String lName) {
		this.lName = lName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public int getDislikes() {
		return dislikes;
	}

	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}

	private int acccountId;
	private String fName;
	private String lName;
	private String email;
	private Date dob;
	private String password;
	private int likes;
	private int dislikes;
	
	public Account()
	{

		
	}
}
