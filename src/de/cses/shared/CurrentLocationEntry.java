/*
 * Copyright 2016, 2017
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

public class CurrentLocationEntry extends AbstractEntry {
	
	private int currentLocationID, parentID;
	private String locationName;
	private ArrayList<CurrentLocationEntry> children;
	
	public CurrentLocationEntry() {
		super();
	}

	/**
	 * @param currentLocationID
	 * @param parentID
	 * @param locationName
	 * @param children
	 */
	public CurrentLocationEntry(int currentLocationID, int parentID, String locationName) {
		super();
		this.currentLocationID = currentLocationID;
		this.parentID = parentID;
		this.locationName = locationName;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getCurrentLocationID() {
		return currentLocationID;
	}

	public void setCurrentLocationID(int currentLocationID) {
		this.currentLocationID = currentLocationID;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public ArrayList<CurrentLocationEntry> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<CurrentLocationEntry> children) {
		this.children = children;
	}

}
