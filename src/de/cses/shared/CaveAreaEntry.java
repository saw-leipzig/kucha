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
	protected double expeditionWidth= 0;
	protected double expeditionLength = 0;
	protected double expeditionHeight = 0;
	protected double modernWidth= 0;
	protected double modernLength = 0;
	protected double modernHeight = 0;
	protected int preservationClassificationID = 0;
	protected int ceilingTypeID1 = 0, ceilingTypeID2 = 0;
	protected int ceilingPreservationClassificationID1 = 0, ceilingPreservationClassificationID2 = 0;
	
	/**
	 * 
	 */
	public CaveAreaEntry() {
	}

	/**
	 * 
	 * @param caveID
	 * @param caveAreaLabel
	 * @param expeditionWidth
	 * @param expeditionLength
	 * @param expeditionHeight
	 */
	public CaveAreaEntry(int caveID, String caveAreaLabel, double expeditionWidth, double expeditionLength, double expeditionHeight, double modernWidth, double modernLength, double modernHeight, int preservationClassificationID,
			int ceilingTypeID1, int ceilingTypeID2, int ceilingPreservationClassificationID1, int ceilingPreservationClassificationID2) {
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
		this.expeditionWidth = expeditionWidth;
		this.expeditionLength = expeditionLength;
		this.expeditionHeight = expeditionHeight;
		this.modernWidth = modernWidth;
		this.modernLength = modernLength;
		this.modernHeight = modernHeight;
		this.preservationClassificationID = preservationClassificationID;
		this.ceilingTypeID1 = ceilingTypeID1;
		this.ceilingTypeID2 = ceilingTypeID2;
		this.ceilingPreservationClassificationID1 = ceilingPreservationClassificationID1;
		this.ceilingPreservationClassificationID2 = ceilingPreservationClassificationID2;
	}

	/**
	 * @param caveID
	 * @param caveAreaLabel
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

	public double getExpeditionWidth() {
		return expeditionWidth;
	}

	public void setExpeditionWidth(double expeditionWidth) {
		this.expeditionWidth = expeditionWidth;
	}

	public double getExpeditionLength() {
		return expeditionLength;
	}

	public void setExpeditionLength(double expeditionLength) {
		this.expeditionLength = expeditionLength;
	}

	public double getExpeditionHeight() {
		return expeditionHeight;
	}

	public void setExpeditionHeight(double expeditionHeight) {
		this.expeditionHeight = expeditionHeight;
	}

	public double getModernWidth() {
		return modernWidth;
	}

	public void setModernWidth(double modernWidth) {
		this.modernWidth = modernWidth;
	}

	public double getModernLength() {
		return modernLength;
	}

	public void setModernLength(double modernLength) {
		this.modernLength = modernLength;
	}

	public double getModernHeight() {
		return modernHeight;
	}

	public void setModernHeight(double modernHeight) {
		this.modernHeight = modernHeight;
	}
	
}
