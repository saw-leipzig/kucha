/*
 * Copyright 2016 -2017
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

import java.sql.Date;

public class DepictionEntry extends AbstractEntry {

	private int depictionID = 0;
	private int styleID = 0;
	private String inscriptions;
	private String separateAksaras;
	private String dating;
	private String description;
	private String backgroundColour;
	private String generalRemarks;
	private String otherSuggestedIdentifications;
	private double width = 0, height = 0;
	private int expeditionID = 0;
	private Date purchaseDate;
	private int locationID = 0;
	private String inventoryNumber;
	private int vendorID = 0;
	private int storyID = 0;
	private int caveID = 0;
	private int wallID = 0;
	private int absoluteLeft = -1;
	private int absoluteTop = -1;
	private int modeOfRepresentationID = 0;
	private String shortName;
	private String positionNotes;

	public DepictionEntry() { }

	/**
	 * @param depictionID
	 * @param styleID
	 * @param inscriptions
	 * @param separateAksaras
	 * @param dating
	 * @param description
	 * @param backgroundColour
	 * @param generalRemarks
	 * @param otherSuggestedIdentifications
	 * @param width
	 * @param height
	 * @param expeditionID
	 * @param purchaseDate
	 * @param currentLocationID
	 * @param inventoryNumber
	 * @param vendorID
	 * @param storyID
	 * @param caveID
	 * @param wallID
	 * @param absoluteLeft
	 * @param absoluteTop
	 * @param modeOfRepresentationID
	 * @param paintedRepresentationShortName
	 */
	public DepictionEntry(int depictionID, int styleID, String inscriptions, String separateAksaras, String dating, String description,
			String backgroundColour, String generalRemarks, String otherSuggestedIdentifications, double width, double height, int expeditionID,
			Date purchaseDate, int locationID, String inventoryNumber, int vendorID, int storyID, int caveID, int wallID, int absoluteLeft,
			int absoluteTop, int modeOfRepresentationID, String shortName, String positionNotes) {
		super();
		this.depictionID = depictionID;
		this.styleID = styleID;
		this.inscriptions = inscriptions;
		this.separateAksaras = separateAksaras;
		this.dating = dating;
		this.description = description;
		this.backgroundColour = backgroundColour;
		this.generalRemarks = generalRemarks;
		this.otherSuggestedIdentifications = otherSuggestedIdentifications;
		this.width = width;
		this.height = height;
		this.expeditionID = expeditionID;
		this.purchaseDate = purchaseDate;
		this.locationID = locationID;
		this.inventoryNumber = inventoryNumber;
		this.vendorID = vendorID;
		this.storyID = storyID;
		this.caveID = caveID;
		this.wallID = wallID;
		this.absoluteLeft = absoluteLeft;
		this.absoluteTop = absoluteTop;
		this.modeOfRepresentationID = modeOfRepresentationID;
		this.shortName = shortName;
		this.positionNotes = positionNotes;
	}

	public DepictionEntry clone() {
		return new DepictionEntry(depictionID, styleID, inscriptions, separateAksaras, dating, description, backgroundColour, generalRemarks,
				otherSuggestedIdentifications, width, height, expeditionID, purchaseDate, locationID, inventoryNumber, vendorID, storyID,
				caveID, wallID, absoluteLeft, absoluteTop, modeOfRepresentationID, shortName, positionNotes);
	}

	public int getDepictionID() {
		return depictionID;
	}

	public void setDepictionID(int depictionID) {
		this.depictionID = depictionID;
	}

	public int getStyleID() {
		return styleID;
	}

	public void setStyleID(int styleID) {
		this.styleID = styleID;
	}

	public String getInscriptions() {
		return inscriptions;
	}

	public void setInscriptions(String inscriptions) {
		this.inscriptions = inscriptions;
	}

	public String getDating() {
		return dating;
	}

	public void setDating(String dating) {
		this.dating = dating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getBackgroundColour() {
		return backgroundColour;
	}

	public void setBackgroundColour(String backgroundColour) {
		this.backgroundColour = backgroundColour;
	}

	public String getGeneralRemarks() {
		return generalRemarks;
	}

	public void setGeneralRemarks(String generalRemarks) {
		this.generalRemarks = generalRemarks;
	}

	public String getOtherSuggestedIdentifications() {
		return otherSuggestedIdentifications;
	}

	public void setOtherSuggestedIdentifications(String otherSuggestedIdentifications) {
		this.otherSuggestedIdentifications = otherSuggestedIdentifications;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public int getExpeditionID() {
		return expeditionID;
	}

	public void setExpeditionID(int expeditionID) {
		this.expeditionID = expeditionID;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public int getLocationID() {
		return locationID;
	}

	public void setLocationID(int locationID) {
		this.locationID = locationID;
	}

	public int getVendorID() {
		return vendorID;
	}

	public void setVendorID(int vendorID) {
		this.vendorID = vendorID;
	}

	public int getStoryID() {
		return storyID;
	}

	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	/**
	 * This method is needed by for the DepictionViewTemplate
	 * 
	 * @see de.cses.client.depictions.DepictionEditor
	 * @return String containing caveID and depictionID
	 */
	public String getName() {
		return "Cave: " + caveID + " Depiction: " + depictionID;
	}

	public int getAbsoluteLeft() {
		return absoluteLeft;
	}

	public void setAbsoluteLeft(int absoluteLeft) {
		this.absoluteLeft = absoluteLeft;
	}

	public int getAbsoluteTop() {
		return absoluteTop;
	}

	public void setAbsoluteTop(int absoluteTop) {
		this.absoluteTop = absoluteTop;
	}

	public int getWallID() {
		return wallID;
	}

	public void setWallID(int wallID) {
		this.wallID = wallID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Depiction-" + depictionID;
	}

	/**
	 * @return the separateAksaras
	 */
	public String getSeparateAksaras() {
		return separateAksaras;
	}

	/**
	 * @param separateAksaras
	 *          the separateAksaras to set
	 */
	public void setSeparateAksaras(String separateAksaras) {
		this.separateAksaras = separateAksaras;
	}

	public int getModeOfRepresentationID() {
		return modeOfRepresentationID;
	}

	public void setModeOfRepresentationID(int modeOfRepresentationID) {
		this.modeOfRepresentationID = modeOfRepresentationID;
	}

	public String getInventoryNumber() {
		return inventoryNumber;
	}

	public void setInventoryNumber(String inventoryNumber) {
		this.inventoryNumber = inventoryNumber;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getPositionNotes() {
		return positionNotes;
	}

	public void setPositionNotes(String positionNotes) {
		this.positionNotes = positionNotes;
	}

}
