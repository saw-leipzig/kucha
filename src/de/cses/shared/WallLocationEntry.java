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
public class WallLocationEntry extends AbstractEntry {
	
	private int wallLocationID;
	private String label;

	/**
	 * @param wallLocationID
	 * @param label
	 */
	public WallLocationEntry(int wallLocationID, String label) {
		setWallLocationID(wallLocationID);
		setLabel(label);
	}

	/**
	 * 
	 */
	public WallLocationEntry() {
		this(0, "");
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int wallLocationID) {
		this.wallLocationID = wallLocationID;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
