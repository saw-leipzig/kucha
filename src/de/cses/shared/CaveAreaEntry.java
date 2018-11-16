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
	protected double expeditionWidth = 0;
	protected double expeditionLength = 0;
	protected double expeditionWallHeight = 0;
	protected double expeditionTotalHeight = 0;
	protected double modernMinWidth = 0;
	protected double modernMaxWidth = 0;
	protected double modernMinLength = 0;
	protected double modernMaxLength = 0;
	protected double modernMinHeight = 0;
	protected double modernMaxHeight = 0;
	protected PreservationClassificationEntry preservationClassification;
	protected CeilingTypeEntry ceilingType1;
	protected CeilingTypeEntry ceilingType2;
	protected PreservationClassificationEntry ceilingPreservationClassification1;
	protected PreservationClassificationEntry ceilingPreservationClassification2;
	protected PreservationClassificationEntry floorPreservationClassification;
	
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
	public CaveAreaEntry(int caveID, String caveAreaLabel, double expeditionWidth, double expeditionLength, double expeditionWallHeight, double expeditionTotalHeight,
			double modernMinWidth, double modernMaxWidth, double modernMinLength, double modernMaxLength, double modernMinHeight, double modernMaxHeight, 
			PreservationClassificationEntry preservationClassification, CeilingTypeEntry ceilingType1, CeilingTypeEntry ceilingType2, PreservationClassificationEntry ceilingPreservationClassification1, PreservationClassificationEntry ceilingPreservationClassification2, 
			PreservationClassificationEntry floorPreservationClassification) {
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
		this.expeditionWidth = expeditionWidth;
		this.expeditionLength = expeditionLength;
		this.expeditionWallHeight = expeditionWallHeight;
		this.expeditionTotalHeight = expeditionTotalHeight;
		this.modernMinWidth = modernMinWidth;
		this.modernMaxWidth = modernMaxWidth;
		this.modernMinLength = modernMinLength;
		this.modernMaxLength = modernMaxLength;
		this.modernMinHeight = modernMinHeight;
		this.modernMaxHeight = modernMaxHeight;
		this.preservationClassification = preservationClassification;
		this.ceilingType1 = ceilingType1;
		this.ceilingType2 = ceilingType2;
		this.ceilingPreservationClassification1 = ceilingPreservationClassification1;
		this.ceilingPreservationClassification2 = ceilingPreservationClassification2;
		this.floorPreservationClassification = floorPreservationClassification;
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
		return new CaveAreaEntry(caveID, caveAreaLabel, expeditionWidth, expeditionLength, expeditionWallHeight, expeditionTotalHeight, 
				modernMinWidth, modernMaxWidth, modernMinLength, modernMaxLength, modernMinHeight, modernMaxHeight, 
				preservationClassification, ceilingType1, ceilingType2, ceilingPreservationClassification1, ceilingPreservationClassification2, 
				floorPreservationClassification);
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

	public double getExpeditionWallHeight() {
		return expeditionWallHeight;
	}

	public void setExpeditionWallHeight(double expeditionWallHeight) {
		this.expeditionWallHeight = expeditionWallHeight;
	}

	public double getExpeditionTotalHeight() {
		return expeditionTotalHeight;
	}

	public void setExpeditionTotalHeight(double expeditionTotalHeight) {
		this.expeditionTotalHeight = expeditionTotalHeight;
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

	public PreservationClassificationEntry getPreservationClassification() {
		return preservationClassification;
	}

	public void setPreservationClassification(PreservationClassificationEntry preservationClassification) {
		this.preservationClassification = preservationClassification;
	}

	public CeilingTypeEntry getCeilingType1() {
		return ceilingType1;
	}

	public void setCeilingType1(CeilingTypeEntry ceilingType1) {
		this.ceilingType1 = ceilingType1;
	}

	public CeilingTypeEntry getCeilingType2() {
		return ceilingType2;
	}

	public void setCeilingType2(CeilingTypeEntry ceilingType2) {
		this.ceilingType2 = ceilingType2;
	}

	public PreservationClassificationEntry getCeilingPreservationClassification1() {
		return ceilingPreservationClassification1;
	}

	public void setCeilingPreservationClassification1(PreservationClassificationEntry ceilingPreservationClassification1) {
		this.ceilingPreservationClassification1 = ceilingPreservationClassification1;
	}

	public PreservationClassificationEntry getCeilingPreservationClassification2() {
		return ceilingPreservationClassification2;
	}

	public void setCeilingPreservationClassification2(PreservationClassificationEntry ceilingPreservationClassification2) {
		this.ceilingPreservationClassification2 = ceilingPreservationClassification2;
	}

	public PreservationClassificationEntry getFloorPreservationClassification() {
		return floorPreservationClassification;
	}

	public void setFloorPreservationClassification(PreservationClassificationEntry floorPreservationClassification) {
		this.floorPreservationClassification = floorPreservationClassification;
	}
	
}
