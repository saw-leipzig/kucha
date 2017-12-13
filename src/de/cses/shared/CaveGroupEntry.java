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
public class CaveGroupEntry extends AbstractEntry {
	
	private int caveGroupID;
	private String name;

	/**
	 * 
	 */
	public CaveGroupEntry() {
		this(0, "");
	}
	
	/**
	 * @param caveGroupID
	 * @param name
	 */
	public CaveGroupEntry(int caveGroupID, String name) {
		super();
		this.caveGroupID = caveGroupID;
		this.name = name;
	}

	public int getCaveGroupID() {
		return caveGroupID;
	}

	public void setCaveGroupID(int caveGroupID) {
		this.caveGroupID = caveGroupID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "CaveGroup-" + caveGroupID;
	}
	
	

}
