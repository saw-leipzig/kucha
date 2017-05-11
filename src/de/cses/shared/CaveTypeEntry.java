/*
 * Copyright 2016 
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

public class CaveTypeEntry implements IsSerializable{
	private int caveTypeID;
	private String nameEN;
	private String descriptionEN;
	private String sketchName;
	
	public CaveTypeEntry(){
		
	}

	public CaveTypeEntry(int caveTypeID, String nameEN, String descriptionEN, String sketchName) {
		super();
		this.caveTypeID = caveTypeID;
		this.nameEN = nameEN;
		this.descriptionEN = descriptionEN;
		this.setSketchName(sketchName);
	}

	public int getCaveTypeID() {
		return caveTypeID;
	}

	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}

	public String getNameEN() {
		return nameEN;
	}

	public void setNameEN(String nameEN) {
		this.nameEN = nameEN;
	}

	public String getDescriptionEN() {
		return descriptionEN;
	}

	public void setDescriptionEN(String descriptionEN) {
		this.descriptionEN = descriptionEN;
	}

	/**
	 * @return the sketchName
	 */
	public String getSketchName() {
		return sketchName;
	}

	/**
	 * @param sketchName the sketchName to set
	 */
	public void setSketchName(String sketchName) {
		this.sketchName = sketchName;
	}
	

}
