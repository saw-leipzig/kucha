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
import java.util.Arrays;

public class WallEntry extends AbstractEntry {
	
	protected transient ArrayList<String> locations = new ArrayList<String>(
			Arrays.asList("antechamber front wall", "antechamber left wall", "antechamber rear wall", "antechamber right wall",
					"main chamber corridor inner wall", "main chamber corridor outer wall", "main chamber front wall", "main chamber left wall",
					"main chamber rear wall", "main chamber right wall", "rear area left corridor inner wall", "rear area left corridor outer wall",
					"rear area right corridor inner wall", "rear area right corridor outer wall", "rear chamber inner wall", "rear chamber left wall",
					"rear chamber outer wall", "rear chamber right wall"));

	private int caveID;
	private String locationLabel;
	private int preservationClassificationID;
	double width, height;
	
	public WallEntry(){
		this(0, "", 0, 0.0, 0.0);
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
		super();
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
	public boolean setLocationLabel(String locationLabel) {
		if (locations.contains(locationLabel)) {
			this.locationLabel = locationLabel;
			return true;
		} else {
			return false;
		}
	}
	

}
