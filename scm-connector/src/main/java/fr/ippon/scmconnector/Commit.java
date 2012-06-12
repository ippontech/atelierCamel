package fr.ippon.scmconnector;

import java.io.Serializable;

public class Commit implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String authorEmail;
	private String message;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAuthorEmail() {
		return authorEmail;
	}
	
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
}
