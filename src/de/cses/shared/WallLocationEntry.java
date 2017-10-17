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
	
	public static final String ANTECHAMBER_LABEL = "antechamber";
	public static final String MAIN_CHAMBER_LABEL = "main chamber";
	public static final String MAIN_CHAMBER_CORRIDOR_LABEL = "main chamber corridor";
	public static final String REAR_AREA_LABEL = "rear area";
	public static final String REAR_AREA_LEFT_CORRIDOR_LABEL = "rear area left corridor";
	public static final String REAR_AREA_RIGHT_CORRIDOR_LABEL = "rear area right corridor";
	
	private int wallLocationID;
	private String label;
	private String caveAreaLabel; // see CaveAreaEntry

	/**
	 * @param wallLocationID
	 * @param label
	 */
	public WallLocationEntry(int wallLocationID, String label, String caveAreaLabel) {
		this.wallLocationID = wallLocationID;
		this.label = label;
		this.caveAreaLabel = caveAreaLabel;
	}

	/**
	 * 
	 */
	public WallLocationEntry() {
		wallLocationID=0;
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

	/**
	 * @return the caveAreaLabel
	 */
	public String getCaveAreaLabel() {
		return caveAreaLabel;
	}

	/**
	 * @param caveAreaLabel the caveAreaLabel to set
	 */
	public void setCaveAreaLabel(String caveAreaLabel) {
		this.caveAreaLabel = caveAreaLabel;
	}

}
