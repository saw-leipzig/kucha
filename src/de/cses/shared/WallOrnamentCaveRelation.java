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

/**
 * @author nina
 *
 */
public class WallOrnamentCaveRelation extends AbstractEntry {
	int caveID;
	int ornamentCaveRelationID;
	int wallOrnamentCaveRelationID;
	WallEntry wall;
//	int wallLocationID;
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

	/**
	 * @param caveID
	 * @param wallLocationID
	 */
	public WallOrnamentCaveRelation(int caveID, WallEntry wall) {
		this.caveID = caveID;
		this.wall = wall;
	}

	public WallOrnamentCaveRelation() {
	}

	public WallOrnamentCaveRelation(int wallOrnamentCaveRelationID, int caveOrnamentRelationID, int ornamentPositionID, int ornamentFunctionID, String notes, WallEntry wall) {
		this.wallOrnamentCaveRelationID = wallOrnamentCaveRelationID;
		this.ornamentCaveRelationID = caveOrnamentRelationID;
//		this.wallLocationID = wallLocationID;
		this.notes = notes;
		this.ornamenticFunctionID = ornamentFunctionID;
		this.ornamenticPositionID = ornamentPositionID;
		this.wall = wall;

	}

	public WallEntry getWall() {
		return wall;
	}

	public void setWall(WallEntry wall) {
		this.wall = wall;
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

//	public int getWallLocationID() {
//		return wallLocationID;
//	}

	/**
	 * @return the ornamentCaveRelationID
	 */
	public int getOrnamentCaveRelationID() {
		return ornamentCaveRelationID;
	}

	/**
	 * @param ornamentCaveRelationID
	 *          the ornamentCaveRelationID to set
	 */
	public void setOrnamentCaveRelationID(int ornamentCaveRelationID) {
		this.ornamentCaveRelationID = ornamentCaveRelationID;
	}

	/**
	 * @return the wallOrnamentCaveRelationID
	 */
	public int getWallOrnamentCaveRelationID() {
		return wallOrnamentCaveRelationID;
	}

	/**
	 * @param wallOrnamentCaveRelationID
	 *          the wallOrnamentCaveRelationID to set
	 */
	public void setWallOrnamentCaveRelationID(int wallOrnamentCaveRelationID) {
		this.wallOrnamentCaveRelationID = wallOrnamentCaveRelationID;
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

//	public void setWallLocationID(int wallLocationID) {
//		this.wallLocationID = wallLocationID;
//	}

}
