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
public class Cave3DModelEntry extends AbstractEntry {
	private int cave3DModelID;
	private String title;
	private String filename;
	private int caveTypeID;

	/**
	 * 
	 */
	public Cave3DModelEntry() {	}
	
 	/**
	 * @param caveSketchID
	 * @param caveID
	 * @param imageType
	 */
	public Cave3DModelEntry(int cave3DModelID, String title, String filename, int caveTypeID, boolean deleted) {
		this.cave3DModelID = cave3DModelID;
		this.title = title;
		this.filename = filename;
		this.caveTypeID = caveTypeID;
		this.deleted = deleted;
	}

	public int getCave3DModelID() {
		return cave3DModelID;
	}

	public void setCave3DID(int cave3DModelID) {
		this.cave3DModelID = cave3DModelID;
	}

	public int getCaveTypeID() {
		return caveTypeID;
	}

	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String imageType) {
		this.title = imageType;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String imageType) {
		this.filename = filename;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "WallSketch-" + cave3DModelID;
	}


}
