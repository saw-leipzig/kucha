/*
 * Copyright 2016 
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

import java.sql.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author alingnau
 *
 */
public class AuthorEntry implements IsSerializable {

	private int authorID;
	private String lastname, firstname;
	private Date kuchaVisitDate;
	private String affiliation, email, homepage;

	/**
	 * The default constructor is used to create a new AuthorEntry. The authorID
	 * is set to 0 to indicate that this entry is not taken from a database and
	 * therefore has to be inserted instead of updated.
	 */
	public AuthorEntry() {
		this.authorID = 0;
	}

	public AuthorEntry(int authorID, String lastname, String firstname, Date kuchaVisitDate, String affiliation, String email,
			String homepage) {
		super();
		this.authorID = authorID;
		this.lastname = lastname;
		this.firstname = firstname;
		this.kuchaVisitDate = kuchaVisitDate;
		this.affiliation = affiliation;
		this.email = email;
		this.homepage = homepage;
	}

	public int getAuthorID() {
		return authorID;
	}

	public void setAuthorID(int authorID) {
		this.authorID = authorID;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public Date getKuchaVisitDate() {
		return kuchaVisitDate;
	}

	public void setKuchaVisitDate(Date kuchaVisitDate) {
		this.kuchaVisitDate = kuchaVisitDate;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getInsertSql() {
		return "INSERT INTO Authors (LastName,FirstName,KuchaVisitDate,Affiliation,Email,Homepage) VALUES ('" + lastname
				+ "','" + firstname + "'," + kuchaVisitDate + ",'" + affiliation + "','" + email + "','" + homepage + "')";
	}

	public String getUpdateSql() {
		return "UPDATE Authors SET LastName='" + lastname + "', FirstName='" + firstname + "', KuchaVisitDate=" + kuchaVisitDate
				+ ", Affiliation='" + affiliation + "', Email='" + email + "', Homepage='" + homepage + "' WHERE AuthorID=" + authorID;
	}

}
