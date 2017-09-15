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

import java.sql.Date;

public class DepictionEntry extends AbstractEntry {

	private int depictionID;
	private int styleID;
	private String inscriptions;
	private String separateAksaras;
	private String dating;
	private String description;
	private String backgroundColour;
	private String material;
	private String generalRemarks;
	private String otherSuggestedIdentifications;
	private double width, height;
	private int expeditionID;
	private Date purchaseDate;
	private int currentLocationID;
	private int vendorID;
	private int storyID;
	private int caveID;
	private int wallID;
	private int iconographyID;
	private int absoluteLeft;
	private int absoluteTop;

	public DepictionEntry() {
		this(0, 0, "", "", "", "", "", "", "", "", 0, 0, 0, null, 0, 0, 0, 0, 0, 0);
	}

	public DepictionEntry(int depictionID, int styleID, String inscriptions, String separateAksaras, String dating, String description, String backgroundColour,
			String material, String generalRemarks, String otherSuggestedIdentifications, double width, double height, int expeditionID,
			Date purchaseDate, int currentLocationID, int vendorID, int storyID, int caveID, int wallID, int iconographyID) {
		super();
		setDepictionID(depictionID);
		setStyleID(styleID);
		setInscriptions(inscriptions);
		setSeparateAksaras(separateAksaras);
		setDating(dating);
		setDescription(description);
		setBackgroundColour(backgroundColour);
		setMaterial(material);
		setGeneralRemarks(generalRemarks);
		setOtherSuggestedIdentifications(otherSuggestedIdentifications);
		setWidth(width);
		setHeight(height);
		setExpeditionID(expeditionID);
		setPurchaseDate(purchaseDate);
		setCurrentLocationID(currentLocationID);
		setVendorID(vendorID);
		setStoryID(storyID);
		setCaveID(caveID);
		setWallID(wallID);
		setIconographyID(iconographyID);
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

	public String getMaterial() {
		return material;
	}

	public void setMaterial(String material) {
		this.material = material;
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

	public int getCurrentLocationID() {
		return currentLocationID;
	}

	public void setCurrentLocationID(int currentLocationID) {
		this.currentLocationID = currentLocationID;
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

	public int getIconographyID() {
		return iconographyID;
	}

	public void setIconographyID(int iconographyID) {
		this.iconographyID = iconographyID;
	}

	public int getWallID() {
		return wallID;
	}

	public void setWallID(int wallID) {
		this.wallID = wallID;
	}

	/* (non-Javadoc)
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
	 * @param separateAksaras the separateAksaras to set
	 */
	public void setSeparateAksaras(String separateAksaras) {
		this.separateAksaras = separateAksaras;
	}

}
