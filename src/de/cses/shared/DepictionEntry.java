/*
 * Copyright 2016 - 2018
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
import java.util.ArrayList;

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
	private ExpeditionEntry expedition;
	private Date purchaseDate;
	private LocationEntry location;
	private String inventoryNumber;
	private VendorEntry vendor;
	private int storyID = 0;
	private CaveEntry cave;
	private int wallID = 0;
	private int absoluteLeft = -1;
	private int absoluteTop = -1;
	private int modeOfRepresentationID = 0;
	private String shortName;
	private String positionNotes;
	private int masterImageID;
	private ArrayList<ImageEntry> relatedImages = new ArrayList<ImageEntry>();
	private ArrayList<PreservationAttributeEntry> preservationAttributesList = new ArrayList<PreservationAttributeEntry>();
	private ArrayList<AnnotatedBiblographyEntry> relatedBibliographyList = new ArrayList<AnnotatedBiblographyEntry>();
	private ArrayList<IconographyEntry> relatedIconographyList = new ArrayList<IconographyEntry>();

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
			String backgroundColour, String generalRemarks, String otherSuggestedIdentifications, double width, double height, ExpeditionEntry expedition,
			Date purchaseDate, LocationEntry location, String inventoryNumber, VendorEntry vendor, int storyID, CaveEntry cave, int wallID, int absoluteLeft,
			int absoluteTop, int modeOfRepresentationID, String shortName, String positionNotes, int masterImageID, boolean openAccess, String lastChangedByUser, String lastChangedOnDate) {
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
		this.expedition = expedition;
		this.purchaseDate = purchaseDate;
		this.location = location;
		this.inventoryNumber = inventoryNumber;
		this.vendor = vendor;
		this.storyID = storyID;
		this.cave = cave;
		this.wallID = wallID;
		this.absoluteLeft = absoluteLeft;
		this.absoluteTop = absoluteTop;
		this.modeOfRepresentationID = modeOfRepresentationID;
		this.shortName = shortName;
		this.positionNotes = positionNotes;
		this.masterImageID = masterImageID;
		this.setOpenAccess(openAccess);
		this.setLastChangedByUser(lastChangedByUser);
		this.setLastChangedOnDate(lastChangedOnDate);
	}

	public DepictionEntry clone() {
		DepictionEntry clonedDepictionEntry = new DepictionEntry(depictionID, styleID, inscriptions, separateAksaras, dating, description, backgroundColour, generalRemarks,
				otherSuggestedIdentifications, width, height, expedition, purchaseDate, location, inventoryNumber, vendor, storyID,
				cave, wallID, absoluteLeft, absoluteTop, modeOfRepresentationID, shortName, positionNotes, masterImageID, openAccess, lastChangedByUser, lastChangedOnDate);
		ArrayList<ImageEntry> clonedRelatedImages = new ArrayList<ImageEntry>();
		for (ImageEntry ie : this.relatedImages) {
			clonedRelatedImages.add(ie);
		}
		clonedDepictionEntry.setRelatedImages(clonedRelatedImages);
		ArrayList<PreservationAttributeEntry> clonedPreservationAttributesList = new ArrayList<PreservationAttributeEntry>();
		for (PreservationAttributeEntry pae : this.preservationAttributesList) {
			clonedPreservationAttributesList.add(pae);
		}
		clonedDepictionEntry.setPreservationAttributesList(clonedPreservationAttributesList);
		ArrayList<AnnotatedBiblographyEntry> clonedRelatedBibliographyList = new ArrayList<AnnotatedBiblographyEntry>();
		for (AnnotatedBiblographyEntry abe : this.relatedBibliographyList) {
			clonedRelatedBibliographyList.add(abe);
		}
		clonedDepictionEntry.setRelatedBibliographyList(clonedRelatedBibliographyList);
		ArrayList<IconographyEntry> clonedRelatedIconographyList = new ArrayList<IconographyEntry>();
		for (IconographyEntry ie : this.relatedIconographyList) {
			clonedRelatedIconographyList.add(ie);
		}
		clonedDepictionEntry.setRelatedIconographyList(clonedRelatedIconographyList);
		return clonedDepictionEntry;
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

	public ExpeditionEntry getExpedition() {
		return expedition;
	}

	public void setExpedition(ExpeditionEntry expedition) {
		this.expedition = expedition;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public LocationEntry getLocation() {
		return location;
	}

	public void setLocation(LocationEntry location) {
		this.location = location;
	}

	public VendorEntry getVendor() {
		return vendor;
	}

	public void setVendor(VendorEntry vendor) {
		this.vendor = vendor;
	}

	public int getStoryID() {
		return storyID;
	}

	public void setStoryID(int storyID) {
		this.storyID = storyID;
	}

	public CaveEntry getCave() {
		return cave;
	}

	public void setCave(CaveEntry cave) {
		this.cave = cave;
	}

	/**
	 * This method is needed by for the DepictionViewTemplate
	 * 
	 * @see de.cses.client.depictions.DepictionEditor
	 * @return String containing caveID and depictionID
	 */
	@Deprecated
	public String getName() {
		return "Cave: " + cave.getOfficialNumber() + " Depiction: " + depictionID;
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

	public ArrayList<PreservationAttributeEntry> getPreservationAttributesList() {
		return preservationAttributesList;
	}

	public void setPreservationAttributesList(ArrayList<PreservationAttributeEntry> preservationAttributesList) {
		this.preservationAttributesList = preservationAttributesList;
	}

	public ArrayList<ImageEntry> getRelatedImages() {
		return relatedImages;
	}

	public void setRelatedImages(ArrayList<ImageEntry> relatedImages) {
		this.relatedImages = relatedImages;
	}

	public int getMasterImageID() {
		return masterImageID != 0 ? masterImageID : (!relatedImages.isEmpty() ? relatedImages.get(0).getImageID() : 0);
	}

	public void setMasterImageID(int masterImageID) {
		this.masterImageID = masterImageID;
	}

	public ArrayList<AnnotatedBiblographyEntry> getRelatedBibliographyList() {
		return relatedBibliographyList;
	}

	public void setRelatedBibliographyList(ArrayList<AnnotatedBiblographyEntry> relatedBibliographyList) {
		this.relatedBibliographyList = relatedBibliographyList;
	}

	public ArrayList<IconographyEntry> getRelatedIconographyList() {
		return relatedIconographyList;
	}

	public void setRelatedIconographyList(ArrayList<IconographyEntry> relatedIconographyList) {
		this.relatedIconographyList = relatedIconographyList;
	}

}
