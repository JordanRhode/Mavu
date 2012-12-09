package com.mavu.appcode;

import java.util.Date;

public class Account {

	
	
	public String getAccountId() {
		return acccountId;
	}

	public void setAccountId(String acccountId) {
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
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

	private String acccountId;
	private String fName;
	private String lName;
	private String email;
	private String dob;
	private String password;
	private int likes;
	private int dislikes;
	
	public Account()
	{

		
	}
}
