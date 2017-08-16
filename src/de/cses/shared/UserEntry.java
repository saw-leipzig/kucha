/*
 * Copyright 2017 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.shared;

/**
 * @author alingnau
 *
 */
public class UserEntry extends AbstractEntry {

	public static final int ASSOCIATED = 1;
	public static final int FULL = 2;
	
	private int userID;
	private String username;
	private String firstname;
	private String lastname;
	private String email;
	private String affiliation;
	private int accessrights;

	/**
	 * 
	 */
	public UserEntry() {
		this(0, "", "", "", "", "", ASSOCIATED);
	}

	/**
	 * @param userID
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param affiliation
	 * @param accessrights
	 */
	public UserEntry(int userID, String username, String firstname, String lastname, String email, String affiliation, int accessrights) {
		super();
		this.userID = userID;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.email = email;
		this.affiliation = affiliation;
		this.accessrights = accessrights;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		return "INSERT INTO Users (Username, Firstname, Lastname, Email, Affiliation, Accessrights) VALUES ('" + username + "', '" + firstname + "', '" + lastname
				+ "', '" + email + "', '" + affiliation + "', " + accessrights + ")";
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUpdateSql()
	 */
	@Override
	public String getUpdateSql() {
		return "UPDATE Users SET Username='" + username + "', Firstname='" + firstname + "', Lastname='" + lastname + "', Email='" + email + "', Affiliation='" + affiliation
				+ "', Accessrights=" + accessrights + " WHERE UserID=" + userID;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public int getAccessrights() {
		return accessrights;
	}

	public void setAccessrights(int accessrights) {
		this.accessrights = accessrights;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String uniqueID() {
		return "User-" + userID;
	}
	
	

}
