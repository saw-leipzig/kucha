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
public class PreservationAttributeEntry extends AbstractEntry {
	
	private int preservationAttributeID = 0;
	private String name;
	
	/**
	 * 
	 */
	public PreservationAttributeEntry() {
		super();
	}


	/**
	 * @param preservationAttributeID
	 * @param name
	 */
	public PreservationAttributeEntry(int preservationAttributeID, String name) {
		super();
		this.preservationAttributeID = preservationAttributeID;
		this.name = name;
	}


	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "PreservationAttribute-" + preservationAttributeID;
	}


	public int getPreservationAttributeID() {
		return preservationAttributeID;
	}


	public void setPreservationAttributeID(int preservationAttributeID) {
		this.preservationAttributeID = preservationAttributeID;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}

}
