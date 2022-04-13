/*
 * Copyright 2016 - 2018
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

import java.util.Comparator;

import de.cses.client.Util;

/**
 * @author alingnau
 *
 */
public class AuthorEntry extends AbstractEntry implements Comparable<AuthorEntry> {

	private int authorID = 0;
	private String lastname, firstname, institution, alias, altSpelling = "";
	private boolean kuchaVisitor;
	private String affiliation, email, homepage;
	private boolean institutionEnabled;

	/**
	 * The default constructor is used to create a new AuthorEntry. The authorID
	 * is set to 0 to indicate that this entry is not taken from a database and
	 * therefore has to be inserted instead of updated.
	 */
	public AuthorEntry() {	}

	public AuthorEntry(int authorID, String lastname, String firstname, String institution, boolean kuchaVisitor, String affiliation, String email,
			String homepage, String alias, boolean institutionEnabled, String lastChangedOn, String altSpelling) {
		this.authorID = authorID;
		this.lastname = lastname;
		this.setInstitution(institution);
		this.firstname = firstname;
		this.kuchaVisitor = kuchaVisitor;
		this.affiliation = affiliation;
		this.email = email;
		this.homepage = homepage;
		this.alias = alias;
		this.institutionEnabled = institutionEnabled;
		this.setModifiedOn(lastChangedOn);
		this.altSpelling=altSpelling;
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

	public void setAltSpelling(String altSpelling) {
		this.altSpelling = altSpelling;
	}
	public String getAltSpelling() {
		return altSpelling;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
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

	public String getName() {
		String name = institutionEnabled ? institution : lastname + (firstname!=null && !firstname.isEmpty() ? ", " + firstname : "") + (alias != null && !alias.isEmpty() ? " " + alias + "" : ""); 
		// Util.doLogging(name);
		return name;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Author-" + authorID;
	}

	public boolean isKuchaVisitor() {
		return kuchaVisitor;
	}

	public void setKuchaVisitor(boolean kuchaVisitor) {
		this.kuchaVisitor = kuchaVisitor;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public boolean isInstitutionEnabled() {
		return institutionEnabled;
	}

	public void setInstitutionEnabled(boolean institutionEnabled) {
		this.institutionEnabled = institutionEnabled;
	}
	 
    @Override
    public int compareTo(AuthorEntry ae)
    {
    	return getLastname().compareTo(ae.getLastname());
    }

}
