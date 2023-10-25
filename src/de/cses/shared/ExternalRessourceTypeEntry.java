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
public class ExternalRessourceTypeEntry extends AbstractEntry implements Comparable<ExternalRessourceTypeEntry> {

	private int externalRessourceTypeID = 0;
	
	private String description, source = "";

	/**
	 * The default constructor is used to create a new AuthorEntry. The authorID
	 * is set to 0 to indicate that this entry is not taken from a database and
	 * therefore has to be inserted instead of updated.
	 */
	public ExternalRessourceTypeEntry() {	}

	public ExternalRessourceTypeEntry(int externalRessourceTypeID, String description, String source) {
		this.externalRessourceTypeID =externalRessourceTypeID;
		this.description = description;
		this.source = source;
	}
	
	public int getExternalRessourceTypeID() {
		return externalRessourceTypeID;
	}

	public void setExternalRessourceTypeID(int externalRessourceTypeID) {
		this.externalRessourceTypeID = externalRessourceTypeID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "ExternalRessource-" + externalRessourceTypeID;
	}
	 
    @Override
    public int compareTo(ExternalRessourceTypeEntry rse)
    {
    	return getSource().compareTo(rse.getSource());
    }

}
