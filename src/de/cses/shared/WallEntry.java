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

public class WallEntry extends AbstractEntry {

	private int wallID;
	private int caveID;
	private int wallLocationID;
	private int preservationClassificationID;
	double width, height;
	
	public WallEntry(){
		this(0, 0, 0, 0, 0.0, 0.0);
	}
	
	public WallEntry(int wallID, int caveID, int wallLocationID, int preservationClassificationID, double width, double height) {
		setWallID(wallID);
		setCaveID(caveID);
		setWallLocationID(wallLocationID);
		setPreservationClassificationID(preservationClassificationID);
		setWidth(width);
		setHeight(height);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Wall_" + wallID;
	}

	public int getWallID() {
		return wallID;
	}

	public void setWallID(int wallID) {
		this.wallID = wallID;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int wallLocationID) {
		this.wallLocationID = wallLocationID;
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
	

}
