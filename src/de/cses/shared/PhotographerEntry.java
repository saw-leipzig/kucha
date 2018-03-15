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

public class PhotographerEntry extends AbstractEntry {
	
	private int photographerID;
	private String name;
	private String institution;

	public PhotographerEntry() {
	}

	/**
	 * @param name
	 * @param insitution
	 */
	public PhotographerEntry(String name, String insitution) {
		this.name = name;
		this.institution = insitution;
	}

	/**
	 * @param photographerID
	 * @param name
	 * @param institution
	 */
	public PhotographerEntry(int photographerID, String name, String institution) {
		this.photographerID = photographerID;
		this.name = name;
		this.institution = institution;
	}

	public int getPhotographerID() {
		return photographerID;
	}

	public void setPhotographerID(int photographerID) {
		this.photographerID = photographerID;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Photographer-" + photographerID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}
	
	public String getLabel() {
		return name!=null ? (name + institution!=null ? " ( " + institution + ")" : "") : institution;
	}

}
