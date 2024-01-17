/*
 * Copyright 2018 
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
public class BibKeywordEntry extends AbstractEntry {
	
	private int bibKeywordID = 0;
	private String bibKeyword;

	/**
	 * default constructor
	 */
	public BibKeywordEntry() { }

	/**
	 * @param bibKeywordID
	 * @param bibKeyword
	 */
	public BibKeywordEntry(int bibKeywordID, String bibKeyword) {
		this.bibKeywordID = bibKeywordID;
		this.bibKeyword = bibKeyword;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getBibKeywordID() {
		return bibKeywordID;
	}

	public void setBibKeywordID(int bibKeywordID) {
		this.bibKeywordID = bibKeywordID;
	}

	public String getBibKeyword() {
		return bibKeyword;
	}

	public String getBibKeywordLower() {
		return bibKeyword.toLowerCase();
	}

	public void setBibKeyword(String bibKeyword) {
		this.bibKeyword = bibKeyword;
	}

}
