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
public class MainChamberEntry extends AbstractEntry {

	private int mainChamberID;
	private int ceilingTypeID;
	private int frontWallID;
	private int leftWallID; 
	private int rightWallID; 
	private int rearWallID;
	private int preservationClassificationID;
	private int ceilingPreservationClassificationID;
	private double height, width, depth;
	private CorridorEntry corridorEntry;

	/**
	 * 
	 */
	public MainChamberEntry() {
		this(0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0, 0);
		corridorEntry = new CorridorEntry();
	}

	public MainChamberEntry(int mainChamberID, int ceilingTypeID, int frontWallID, int leftWallID, int rightWallID, int rearWallID,
			double height, double width, double depth, int preservationClassificationID, int ceilingPreservationClassificationID) {
		super();
		setMainChamberID(mainChamberID);
		this.setCeilingTypeID(ceilingTypeID);
		setFrontWallID(frontWallID);
		setLeftWallID(leftWallID);
		setRightWallID(rightWallID);
		setRearWallID(rearWallID);
		setHeight(height);
		setWidth(width);
		setDepth(depth);
		setPreservationClassificationID(preservationClassificationID);
		setCeilingPreservationClassificationID(ceilingPreservationClassificationID);
	}

	public int getMainChamberID() {
		return mainChamberID;
	}

	public void setMainChamberID(int mainChamberID) {
		this.mainChamberID = mainChamberID;
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

	public int getFrontWallID() {
		return frontWallID;
	}

	public void setFrontWallID(int frontWallID) {
		this.frontWallID = frontWallID;
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

	public int getRearWallID() {
		return rearWallID;
	}

	public void setRearWallID(int rearWallID) {
		this.rearWallID = rearWallID;
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

	public void setDepth(double depth) {
		this.depth = depth;
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

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "MainChamber-" + mainChamberID;
	}

	/**
	 * @return the corridor
	 */
	public CorridorEntry getCorridorEntry() {
		return corridorEntry;
	}

	/**
	 * @param corridor the corridor to set
	 */
	public void setCorridorEntry(CorridorEntry corridorEntry) {
		this.corridorEntry = corridorEntry;
	}

	public int getCeilingPreservationClassificationID() {
		return ceilingPreservationClassificationID;
	}

	public void setCeilingPreservationClassificationID(int ceilingPreservationClassificationID) {
		this.ceilingPreservationClassificationID = ceilingPreservationClassificationID;
	}

}
