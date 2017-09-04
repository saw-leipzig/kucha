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
 * @author alingnau
 *
 */
public class CorridorEntry extends AbstractEntry {
	
	private int corridorID;
	private int outerWallID;
	private int innerWallID;
	private int ceilingTypeID;
	private int preservationClassificationID;

	/**
	 * 
	 */
	public CorridorEntry() {
		this(0,0,0,0,0);
	}

	/**
	 * @param corridorID
	 * @param outerWallID
	 * @param innerWallID
	 * @param ceilingTypeID
	 * @param preservationClassificationID
	 */
	public CorridorEntry(int corridorID, int outerWallID, int innerWallID, int ceilingTypeID, int preservationClassificationID) {
		super();
		setCorridorID(corridorID);
		setOuterWallID(outerWallID);
		setInnerWallID(innerWallID);
		setCeilingTypeID(ceilingTypeID);
		setPreservationClassificationID(preservationClassificationID);
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Corridor-" + corridorID;
	}

	public int getCorridorID() {
		return corridorID;
	}

	public void setCorridorID(int corridorID) {
		this.corridorID = corridorID;
	}

	public int getOuterWallID() {
		return outerWallID;
	}

	public void setOuterWallID(int outerWallID) {
		this.outerWallID = outerWallID;
	}

	public int getInnerWallID() {
		return innerWallID;
	}

	public void setInnerWallID(int innerWallID) {
		this.innerWallID = innerWallID;
	}

	public int getCeilingTypeID() {
		return ceilingTypeID;
	}

	public void setCeilingTypeID(int ceilingTypeID) {
		this.ceilingTypeID = ceilingTypeID;
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

}
