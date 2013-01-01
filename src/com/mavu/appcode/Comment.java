package com.mavu.appcode;

public class Comment {
	
	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getCommenterID() {
		return commenterID;
	}

	public void setCommenterID(String commenterID) {
		this.commenterID = commenterID;
	}

	public String getPostID() {
		return postID;
	}

	public void setPostID(String postID) {
		this.postID = postID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	private String accountID;
	private String commenterID;
	private String postID;
	private String description;
	private String date;
	
	public Comment()
	{
		
	}
	
	/*TODO: Im thinking the postID is optional? If there is no post id then its for just the account? otherwise it links to the post?
	 * The main reason im thinking we should have comments for posts now is like if a user has a question on the event then the owner can respond on that post? not sure
	 */
	public Comment(String accountID, String commenterID, String description, String date, String postID)
	{	
		this.accountID = accountID;
		this.commenterID = commenterID;
		this.postID = postID;
		this.description = description;
		this.date = date;		
	}

}
