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
public class WallSketchEntry extends AbstractEntry {
	private int wallSketchID;
	private String title;
	private String filename;

	/**
	 * 
	 */
	public WallSketchEntry() {	}
	
 	/**
	 * @param caveSketchID
	 * @param caveID
	 * @param imageType
	 */
	public WallSketchEntry(int wallSketchID, String title, String filename) {
		this.wallSketchID = wallSketchID;
		this.title = title;
		this.filename = filename;
	}

	public int getWallSketchID() {
		return wallSketchID;
	}

	public void setWallSketchID(int wallSketchID) {
		this.wallSketchID = wallSketchID;
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
		return "WallSketch-" + wallSketchID;
	}


}
