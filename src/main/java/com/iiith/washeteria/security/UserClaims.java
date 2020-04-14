package com.iiith.washeteria.security;

public class UserClaims {

	private String userName;
	private long tokenIssueTime;
	private String scope;
	
	
	public UserClaims() {
		
	}
	
	public UserClaims(String userName, long tokenIssueTime, String scope) {
		this.userName = userName;
		this.tokenIssueTime = tokenIssueTime;
		this.scope = scope;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getTokenIssueTime() {
		return tokenIssueTime;
	}

	public void setTokenIssueTime(long tokenIssueTime) {
		this.tokenIssueTime = tokenIssueTime;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	
}
