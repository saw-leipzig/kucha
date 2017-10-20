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

import de.cses.client.StaticTables;

/**
 * @author nina
 *
 */
public class WallOrnamentCaveRelation extends AbstractEntry {
	int caveID;
	int wallLocationID;
	int ornamenticPositionID;
	int ornamenticFunctionID;
	String notes;

	/**
	 * @param caveID
	 * @param wallLocationID
	 * @param ornamenticPositionID
	 * @param ornamenticFunctionID
	 * @param notes
	 */
	public WallOrnamentCaveRelation(int caveID, int wallLocationID, int ornamenticPositionID, int ornamenticFunctionID, String notes) {
		super();
		this.caveID = caveID;
		this.wallLocationID = wallLocationID;
		this.ornamenticPositionID = ornamenticPositionID;
		this.ornamenticFunctionID = ornamenticFunctionID;
		this.notes = notes;
	}

	/**
	 * @param caveID
	 * @param wallLocationID
	 */
	public WallOrnamentCaveRelation(int caveID, int wallLocationID) {
		this.caveID = caveID;
		this.wallLocationID = wallLocationID;
	}

	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int wallID) {
		this.wallLocationID = wallID;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public int getOrnamenticPositionID() {
		return ornamenticPositionID;
	}

	public void setOrnamenticPositionID(int ornamenticPositionID) {
		this.ornamenticPositionID = ornamenticPositionID;
	}

	public int getOrnamenticFunctionID() {
		return ornamenticFunctionID;
	}

	public void setOrnamenticFunctionID(int ornamenticFunctionID) {
		this.ornamenticFunctionID = ornamenticFunctionID;
	}
	
	public String getName() {
		return StaticTables.getInstance().getWallLocationEntries().get(wallLocationID).getCaveAreaLabel() + ", "
				+ StaticTables.getInstance().getOrnamentPositionEntries().get(ornamenticPositionID).getName() + ", "
				+ StaticTables.getInstance().getOrmanemtFunctionEntries().get(ornamenticFunctionID).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

}
