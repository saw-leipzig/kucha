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
	
	public static String ANTECHAMBER_FRONT_WALL = "antechamber front wall";
	public static String ANTECHAMBER_LEFT_WALL = "antechamber left wall";
	public static String ANTECHAMBER_REAR_WALL = "antechamber rear wall";
	public static String ANTECHAMBER_RIGHT_WALL = "antechamber right wall";
	public static String MAIN_CHAMBER_CORRIDOR_INNER_WALL = "main chamber corridor inner wall";
	public static String MAIN_CHAMBER_CORRIDOR_OUTER_WALL = "main chamber corridor outer wall";
	public static String MAIN_CHAMBER_FRONT_WALL = "main chamber front wall";
	public static String MAIN_CHAMBER_LEFT_WALL = "main chamber left wall";
	public static String MAIN_CHAMBER_REAR_WALL = "main chamber rear wall";
	public static String MAIN_CHAMBER_RIGHT_WALL = "main chamber right wall";
	public static String REAR_AREA_LEFT_CORRIDOR_INNER_WALL = "rear area left corridor inner wall";
	public static String REAR_AREA_LEFT_CORRIDOR_OUTER_WALL = "rear area left corridor outer wall";
	public static String REAR_AREA_RIGHT_CORRIDOR_INNER_WALL = "rear area right corridor inner wall";
	public static String REAR_AREA_RIGHT_CORRIDOR_OUTER_WALL = "rear area right corridor outer wall";
	public static String REAR_AREA_INNER_WALL = "rear area inner wall";
	public static String REAR_AREA_LEFT_WALL = "rear area left wall";
	public static String REAR_AREA_OUTER_WALL = "rear area outer wall";
	public static String REAR_AREA_RIGHT_WALL = "rear area right wall";

	private int caveID = 0;
	private String locationLabel;
	private int preservationClassificationID = 0;
	double width = 0, height = 0;
	
	public WallEntry(){
	}
	
	public WallEntry(int caveID, String locationLabel) {
		this.caveID = caveID;
		this.locationLabel = locationLabel;
	}
	
	
	/**
	 * @param caveID
	 * @param wallLocationID
	 * @param locationLabel
	 * @param preservationClassificationID
	 * @param width
	 * @param height
	 */
	public WallEntry(int caveID, String locationLabel, int preservationClassificationID, double width, double height) {
		this.caveID = caveID;
		this.locationLabel = locationLabel;
		this.preservationClassificationID = preservationClassificationID;
		this.width = width;
		this.height = height;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Wall_" + caveID + "_" + locationLabel;
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

	/**
	 * @return the locationLabel
	 */
	public String getLocationLabel() {
		return locationLabel;
	}

	/**
	 * @param locationLabel the locationLabel to set
	 */
	public void setLocationLabel(String locationLabel) {
		this.locationLabel = locationLabel;
	}
	

}
