/*
 * Copyright 2018 
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
public class CaveSketchEntry extends AbstractEntry {
	private int caveSketchID = 0;
	private int caveID = 0;
	private String imageType;

	/**
	 * 
	 */
	public CaveSketchEntry() {	}
	
 	/**
	 * @param caveSketchID
	 * @param caveID
	 * @param imageType
	 */
	public CaveSketchEntry(int caveSketchID, int caveID, String imageType) {
		this.caveSketchID = caveSketchID;
		this.caveID = caveID;
		this.imageType = imageType;
	}

	public int getCaveSketchID() {
		return caveSketchID;
	}

	public void setCaveSketchID(int caveSketchID) {
		this.caveSketchID = caveSketchID;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "CaveSketch-" + caveSketchID;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

}
