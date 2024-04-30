/*
 * Copyright 2016-2017
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

import java.util.ArrayList;

public class WallEntry extends AbstractWallEntry {
	
	private int caveID = 0;
	private int preservationClassificationID = 0;
	private double width = 0, height = 0;
	
	public WallEntry() { }
	
	/**
	 * 
	 * @param accessLevel
	 * @param caveID
	 * @param wallLocationID
	 * @param preservationClassificationID
	 * @param width
	 * @param height
	 */
//	public WallEntry(int accessLevel, int caveID, int wallLocationID, int preservationClassificationID, double width, double height) {
//		super(accessLevel);
//		this.caveID = caveID;
//		this.wallLocationID = wallLocationID;
//		this.preservationClassificationID = preservationClassificationID;
//		this.width = width;
//		this.height = height;
//	}
//

	/**
	 * @param caveID
	 * @param wallLocationID
	 * @param locationLabel
	 * @param preservationClassificationID
	 * @param width
	 * @param height
	 */
	public WallEntry(int caveID, int wallLocationID, int preservationClassificationID, double width, double height, ArrayList<WallDimensionEntry> wallDimensions) {
		super(wallLocationID, wallDimensions);
		this.caveID = caveID;
		this.preservationClassificationID = preservationClassificationID;
		this.width = width;
		this.height = height;
	}
	
	public WallEntry clone() {
		return new WallEntry(caveID, wallLocationID, preservationClassificationID, width, height, wallDimensions);
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Wall_" + caveID + "_" + wallLocationID;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	/**
	 * @return the preservationClassificationID
	 */
	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}

	/**
	 * @param preservationClassificationID the preservationClassificationID to set
	 */
	public void setPreservationClassificationID(int preservationClassificationID) {
		this.preservationClassificationID = preservationClassificationID;
	}

	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int wallLocationID) {
		this.wallLocationID = wallLocationID;
	}
}
