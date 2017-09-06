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

/**
 * @author alingnau
 *
 */
public class RearAreaEntry extends AbstractEntry {

	private int rearAreaID;
	private int ceilingTypeID;
	private int innerWallID;
	private int leftWallID; 
	private int rightWallID; 
	private int outerWallID; 
	private int preservationClassificationID;
	private int ceilingPreservationClassificationID;
	private boolean isBackChamber;
	private double height, width, depth;
	private CorridorEntry leftCorridorEntry;
	private CorridorEntry rightCorridorEntry;

	/**
	 * 
	 */
	public RearAreaEntry() {
		this(0, 0, 0, 0, 0, 0, false, 0.0, 0.0, 0.0, 0, 0);
		leftCorridorEntry = new CorridorEntry();
		rightCorridorEntry = new CorridorEntry();
	}

	public RearAreaEntry(int rearAreaID, int ceiligTypeID, int innerWallID, int leftWallID, int rightWallID, int outerWallID,
			boolean isBackChamber, double height, double width, double depth, int preservationClassificationID, int ceilingPreservationClassificationID) {
		super();
		setRearAreaID(rearAreaID);
		setCeilingTypeID(ceiligTypeID);
		setInnerWallID(innerWallID);
		setLeftWallID(leftWallID);
		setRightWallID(rightWallID);
		setOuterWallID(outerWallID);
		setBackChamber(isBackChamber);
		setHeight(height);
		setWidth(width);
		setDepth(depth);
		setPreservationClassificationID(preservationClassificationID);
		setCeilingPreservationClassificationID(ceilingPreservationClassificationID);
	}

	public int getRearAreaID() {
		return rearAreaID;
	}

	public void setRearAreaID(int rearAreaID) {
		this.rearAreaID = rearAreaID;
	}

	/**
	 * @return the ceilingTypeID
	 */
	public int getCeilingTypeID() {
		return ceilingTypeID;
	}

	/**
	 * @param ceilingTypeID
	 *          the ceilingTypeID to set
	 */
	public void setCeilingTypeID(int ceilingTypeID) {
		this.ceilingTypeID = ceilingTypeID;
	}

	public int getInnerWallID() {
		return innerWallID;
	}

	public void setInnerWallID(int innerWallID) {
		this.innerWallID = innerWallID;
	}

	public int getLeftWallID() {
		return leftWallID;
	}

	public void setLeftWallID(int leftWallID) {
		this.leftWallID = leftWallID;
	}

	public int getRightWallID() {
		return rightWallID;
	}

	public void setRightWallID(int rightWallID) {
		this.rightWallID = rightWallID;
	}

	public int getOuterWallID() {
		return outerWallID;
	}

	public void setOuterWallID(int outerWallID) {
		this.outerWallID = outerWallID;
	}

	public boolean isBackChamber() {
		return isBackChamber;
	}

	public void setBackChamber(boolean isBackChamber) {
		this.isBackChamber = isBackChamber;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getDepth() {
		return depth;
	}

	/**
	 * @return the preservationClassificationID
	 */
	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}

	/**
	 * @param preservationClassificationID
	 *          the preservationClassificationID to set
	 */
	public void setPreservationClassificationID(int preservationID) {
		this.preservationClassificationID = preservationID;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "RearArea-" + rearAreaID;
	}

	public CorridorEntry getLeftCorridorEntry() {
		return leftCorridorEntry;
	}

	public void setLeftCorridorEntry(CorridorEntry leftCorridorEntry) {
		this.leftCorridorEntry = leftCorridorEntry;
	}

	public CorridorEntry getRightCorridorEntry() {
		return rightCorridorEntry;
	}

	public void setRightCorridorEntry(CorridorEntry rightCorridorEntry) {
		this.rightCorridorEntry = rightCorridorEntry;
	}

	/**
	 * @return the ceilingPreservationClassificationID
	 */
	public int getCeilingPreservationClassificationID() {
		return ceilingPreservationClassificationID;
	}

	/**
	 * @param ceilingPreservationClassificationID the ceilingPreservationClassificationID to set
	 */
	public void setCeilingPreservationClassificationID(int ceilingPreservationClassificationID) {
		this.ceilingPreservationClassificationID = ceilingPreservationClassificationID;
	}

}
