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
public class CaveAreaEntry extends AbstractEntry {
	
	public static String ANTECHAMBER = "antechamber";
	public static String MAIN_CHAMBER = "main chamber";
	public static String MAIN_CHAMBER_CORRIDOR = "main chamber corridor";
	public static String REAR_AREA_LEFT_CORRIDOR = "rear area left corridor";
	public static String REAR_AREA_RIGHT_CORRIDOR = "rear area right corridor";
	public static String REAR_AREA = "rear area";

	protected int caveID;
	protected String caveAreaLabel;
	protected double height = 0;
	protected double width= 0;
	protected double depth = 0;
	protected int preservationClassificationID = 0;
	protected int ceilingTypeID1 = 0, ceilingTypeID2 = 0;
	protected int ceilingPreservationClassificationID1 = 0, ceilingPreservationClassificationID2 = 0;
	
	/**
	 * 
	 */
	public CaveAreaEntry() {
	}
	
	/**
	 * @param caveID
	 * @param caveAreaLabel
	 * @param height
	 * @param width
	 * @param depth
	 * @param preservationClassificationID
	 * @param ceilingTypeID1
	 * @param ceilingTypeID2
	 * @param ceilingPreservationClassificationID1
	 * @param ceilingPreservationClassificationID2
	 */
	public CaveAreaEntry(int caveID, String caveAreaLabel, double height, double width, double depth, int preservationClassificationID,
			int ceilingTypeID1, int ceilingTypeID2, int ceilingPreservationClassificationID1, int ceilingPreservationClassificationID2) {
		super();
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
		this.height = height;
		this.width = width;
		this.depth = depth;
		this.preservationClassificationID = preservationClassificationID;
		this.ceilingTypeID1 = ceilingTypeID1;
		this.ceilingTypeID2 = ceilingTypeID2;
		this.ceilingPreservationClassificationID1 = ceilingPreservationClassificationID1;
		this.ceilingPreservationClassificationID2 = ceilingPreservationClassificationID2;
	}



	/**
	 * @param caveID2
	 * @param aNTECHAMBER2
	 */
	public CaveAreaEntry(int caveID, String caveAreaLabel) {
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return "CaveArea-" + caveID + caveAreaLabel;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public String getCaveAreaLabel() {
		return caveAreaLabel;
	}

	public void setCaveAreaLabel(String caveAreaLabel) {
		this.caveAreaLabel = caveAreaLabel;
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

	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}

	public void setPreservationClassificationID(int preservationClassificationID) {
		this.preservationClassificationID = preservationClassificationID;
	}

	public int getCeilingTypeID1() {
		return ceilingTypeID1;
	}

	public void setCeilingTypeID1(int ceilingTypeID1) {
		this.ceilingTypeID1 = ceilingTypeID1;
	}

	public int getCeilingTypeID2() {
		return ceilingTypeID2;
	}

	public void setCeilingTypeID2(int ceilingTypeID2) {
		this.ceilingTypeID2 = ceilingTypeID2;
	}

	public int getCeilingPreservationClassificationID1() {
		return ceilingPreservationClassificationID1;
	}

	public void setCeilingPreservationClassificationID1(int ceilingPreservationClassificationID1) {
		this.ceilingPreservationClassificationID1 = ceilingPreservationClassificationID1;
	}

	public int getCeilingPreservationClassificationID2() {
		return ceilingPreservationClassificationID2;
	}

	public void setCeilingPreservationClassificationID2(int ceilingPreservationClassificationID2) {
		this.ceilingPreservationClassificationID2 = ceilingPreservationClassificationID2;
	}
	
}
