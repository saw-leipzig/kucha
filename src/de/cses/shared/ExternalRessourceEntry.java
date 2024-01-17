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
public class ExternalRessourceEntry extends AbstractEntry implements Comparable<ExternalRessourceEntry> {

	private int externalRessourceID = 0;
	private String entity = "";
	private ExternalRessourceTypeEntry source;
	
	public ExternalRessourceEntry() {	}

	public ExternalRessourceEntry(int externalRessourceID, String entity, ExternalRessourceTypeEntry source) {
		this.externalRessourceID =externalRessourceID;
		this.entity = entity;
		this.source = source;
	}
	
	public int getExternalRessourceID() {
		return externalRessourceID;
	}

	public void setExternalRessourceID(int externalRessourceID) {
		this.externalRessourceID = externalRessourceID;
	}

	public String getEntity() {
		return entity;
	}

	public void setEntity(String entity) {
		this.entity = entity;
	}

	public ExternalRessourceTypeEntry getSource() {
		return source;
	}

	public void setSource(ExternalRessourceTypeEntry source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "ExternalRessource-" + externalRessourceID;
	}
	 
    @Override
    public int compareTo(ExternalRessourceEntry rse)
    {
    	return (getSource() + getEntity()).compareTo(rse.getSource() + rse.getEntity())  ;
    }

}
