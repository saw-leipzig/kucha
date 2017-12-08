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
	protected double modernMinWidth = 0;
	protected double modernMaxWidth = 0;
	protected double modernMinLength = 0;
	protected double modernMaxLength = 0;
	protected double modernMinHeight = 0;
	protected double modernMaxHeight = 0;
	protected int preservationClassificationID = 0;
	protected int ceilingTypeID1 = 0, ceilingTypeID2 = 0;
	protected int ceilingPreservationClassificationID1 = 0, ceilingPreservationClassificationID2 = 0;
	protected int floorPreservationClassificationID = 0;
	
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
	public CaveAreaEntry(int caveID, String caveAreaLabel, double expeditionWidth, double expeditionLength, double expeditionHeight, 
			double modernMinWidth, double modernMaxWidth, double modernMinLength, double modernMaxLength, double modernMinHeight, double modernMaxHeight, 
			int preservationClassificationID, int ceilingTypeID1, int ceilingTypeID2, int ceilingPreservationClassificationID1, int ceilingPreservationClassificationID2, 
			int floorPreservationClassificationID) {
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
		this.expeditionWidth = expeditionWidth;
		this.expeditionLength = expeditionLength;
		this.expeditionHeight = expeditionHeight;
		this.modernMinWidth = modernMinWidth;
		this.modernMaxWidth = modernMaxWidth;
		this.modernMinLength = modernMinLength;
		this.modernMaxLength = modernMaxLength;
		this.modernMinHeight = modernMinHeight;
		this.modernMaxHeight = modernMaxHeight;
		this.preservationClassificationID = preservationClassificationID;
		this.ceilingTypeID1 = ceilingTypeID1;
		this.ceilingTypeID2 = ceilingTypeID2;
		this.ceilingPreservationClassificationID1 = ceilingPreservationClassificationID1;
		this.ceilingPreservationClassificationID2 = ceilingPreservationClassificationID2;
		this.floorPreservationClassificationID = floorPreservationClassificationID;
	}

	/**
	 * @param caveID
	 * @param caveAreaLabel
	 */
	public CaveAreaEntry(int caveID, String caveAreaLabel) {
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
	}
	
	public CaveAreaEntry clone() {
		return new CaveAreaEntry(caveID, caveAreaLabel, expeditionWidth, expeditionLength, expeditionHeight, 
				modernMinWidth, modernMaxWidth, modernMinLength, modernMaxLength, modernMinHeight, modernMaxHeight, 
				preservationClassificationID, ceilingTypeID1, ceilingTypeID2, ceilingPreservationClassificationID1, ceilingPreservationClassificationID2, 
				floorPreservationClassificationID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
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

	public double getModernMinWidth() {
		return modernMinWidth;
	}

	public void setModernMinWidth(double modernMinWidth) {
		this.modernMinWidth = modernMinWidth;
	}

	public double getModernMinLength() {
		return modernMinLength;
	}

	public void setModernMinLength(double modernMinLength) {
		this.modernMinLength = modernMinLength;
	}

	public double getModernMinHeight() {
		return modernMinHeight;
	}

	public void setModernMinHeight(double modernMinHeight) {
		this.modernMinHeight = modernMinHeight;
	}

	public double getModernMaxWidth() {
		return modernMaxWidth;
	}

	public void setModernMaxWidth(double modernMaxWidth) {
		this.modernMaxWidth = modernMaxWidth;
	}

	public double getModernMaxLength() {
		return modernMaxLength;
	}

	public void setModernMaxLength(double modernMaxLength) {
		this.modernMaxLength = modernMaxLength;
	}

	public double getModernMaxHeight() {
		return modernMaxHeight;
	}

	public void setModernMaxHeight(double modernMaxHeight) {
		this.modernMaxHeight = modernMaxHeight;
	}

	public int getFloorPreservationClassificationID() {
		return floorPreservationClassificationID;
	}

	public void setFloorPreservationClassificationID(int floorPreservationClassificationID) {
		this.floorPreservationClassificationID = floorPreservationClassificationID;
	}
	
}
