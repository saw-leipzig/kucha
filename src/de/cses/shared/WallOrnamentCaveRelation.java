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
	int wallOrnamentCaveRelationID;
	int wallLocationID;
	int OrnamentCaveRelationID;
	OrnamentPositionEntry position;
	OrnamentFunctionEntry function;
	String notes;

	public WallOrnamentCaveRelation() {

	}

	public int getWallOrnamentCaveRelationID() {
		return wallOrnamentCaveRelationID;
	}

	public void setWallOrnamentCaveRelationID(int wallOrnamentCaveRelationID) {
		this.wallOrnamentCaveRelationID = wallOrnamentCaveRelationID;
	}

	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int wallID) {
		this.wallLocationID = wallID;
	}

	public int getOrnamentCaveRelationID() {
		return OrnamentCaveRelationID;
	}

	public void setOrnamentCaveRelationID(int ornamentCaveRelationID) {
		OrnamentCaveRelationID = ornamentCaveRelationID;
	}

	/**
	 * @return the position
	 */
	public OrnamentPositionEntry getPosition() {
		return position;
	}

	/**
	 * @param position
	 *          the position to set
	 */
	public void setPosition(OrnamentPositionEntry position) {
		this.position = position;
	}

	/**
	 * @return the function
	 */
	public OrnamentFunctionEntry getFunction() {
		return function;
	}

	/**
	 * @param function
	 *          the function to set
	 */
	public void setFunction(OrnamentFunctionEntry function) {
		this.function = function;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
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
