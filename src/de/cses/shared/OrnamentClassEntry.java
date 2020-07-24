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
 * @author Nina
 *
 */
public class OrnamentClassEntry extends AbstractEntry{
	
	private int ornamentClassID;
	private String name;
	/**
	 * @return the ornamentClassId
	 */
	public int getOrnamentClassID() {
		return ornamentClassID;
	}
	public OrnamentClassEntry() {
		
	}
	public OrnamentClassEntry(int ornamentClassID, String name) {
		this.ornamentClassID = ornamentClassID;
		this.name = name;
		
	}
	/**
	 * @param ornamentClassId the ornamentClassId to set
	 */
	public void setOrnamentClassID(int ornamentClassId) {
		this.ornamentClassID = ornamentClassId;
	}
	/**
	 * @return the name
	 */
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamentClass_" + ornamentClassID;
	}

	
	

}
