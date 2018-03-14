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
public class InnerSecondaryPatternsEntry extends AbstractEntry{
	int innerSecondaryPatternsID;
	String name;
	/**
	 * @return the innerSecondaryPatternsID
	 */
	public int getInnerSecondaryPatternsID() {
		return innerSecondaryPatternsID;
	}
	public InnerSecondaryPatternsEntry() {
		
	}
	
	public InnerSecondaryPatternsEntry(int innerSecondaryPatternsID, String name) {
		this.innerSecondaryPatternsID = innerSecondaryPatternsID;
		this.name = name;
		
	}
	/**
	 * @param innerSecondaryPatternsId the innerSecondaryPatternsId to set
	 */
	public void setInnerSecondaryPatternsID(int innerSecondaryPatternsID) {
		this.innerSecondaryPatternsID = innerSecondaryPatternsID;
	}
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
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
