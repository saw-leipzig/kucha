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
public class C14DocumentEntry extends AbstractEntry {
	
	private String c14DocumentName;
	private String c14OriginalDocumentName;
	private int caveID;

	/**
	 * 
	 * @param c14DocumentName
	 * @param c14OriginalDocumentName
	 */
	public C14DocumentEntry(String c14DocumentName, String c14OriginalDocumentName, int caveID) {
		this.c14DocumentName = c14DocumentName;
		this.c14OriginalDocumentName = c14OriginalDocumentName;
		this.caveID = caveID;
	}

	/**
	 * 
	 */
	public C14DocumentEntry() {
		super();
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "C14Document-" + c14DocumentName;
	}

	public String getC14DocumentName() {
		return c14DocumentName;
	}
	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}
	public void setC14DocumentName(String c14DocumentName) {
		this.c14DocumentName = c14DocumentName;
	}

	public String getC14OriginalDocumentName() {
		return c14OriginalDocumentName;
	}

	public void setC14OriginalDocumentName(String c14OriginalDocumentName) {
		this.c14OriginalDocumentName = c14OriginalDocumentName;
	}

}
