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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author alingnau
 *
 */
public class CeilingTypeEntry extends AbstractEntry {
	
	private int ceilingTypeID;
	private String name;

	/**
	 * 
	 */
	public CeilingTypeEntry() {	}

	public CeilingTypeEntry(int ceilingTypeID, String name) {
		super();
		this.ceilingTypeID = ceilingTypeID;
		this.name = name;
	}

	public int getCeilingTypeID() {
		return ceilingTypeID;
	}

	public void setCeilingTypeID(int ceilingTypeID) {
		this.ceilingTypeID = ceilingTypeID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "CeilingType_" + ceilingTypeID;
	}	
	
}
