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
import java.util.HashMap;
import java.util.List;

public class DepictionEntry extends AbstractEntry {

	private int depictionID = 0;
	private int styleID = 0;
	private StyleEntry style = null;
	private String inscriptions="";
	private String separateAksaras="";
	private String dating="";
	private String description="";
	private String backgroundColour="";
	private String generalRemarks="";
	private String otherSuggestedIdentifications="";
	private double width = 0, height = 0;
	private ExpeditionEntry expedition;
	private Date purchaseDate;
	private LocationEntry location;
	private String inventoryNumber;
	private VendorEntry vendor;
	private int storyID = 0;
	private CaveEntry cave;
	private int wallID = 0;
	private List<WallTreeEntry> wallIDs = new ArrayList<WallTreeEntry>();
	private int absoluteLeft = -1;
	private int absoluteTop = -1;
	private int modeOfRepresentationID = 0;
	private ModeOfRepresentationEntry modeOfRepresentation = null;
	private String shortName;
	private String positionNotes;
	private int masterImageID;
	private ArrayList<ImageEntry> relatedImages = new ArrayList<ImageEntry>();
	private ArrayList<PreservationAttributeEntry> preservationAttributesList = new ArrayList<PreservationAttributeEntry>();
	private ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList = new ArrayList<AnnotatedBibliographyEntry>();
	private ArrayList<IconographyEntry> relatedIconographyList = new ArrayList<IconographyEntry>();
	private ArrayList<AnnotationEntry> relatedAnnotationList = new ArrayList<AnnotationEntry>();
	private HashMap<Integer, Integer> imageSortInfo = null;

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
			Date purchaseDate, LocationEntry location, String inventoryNumber, VendorEntry vendor, int storyID, CaveEntry cave,List<WallTreeEntry> wallIDs, int absoluteLeft,
			int absoluteTop, int modeOfRepresentationID, String shortName, String positionNotes, int masterImageID, int accessLevel, String lastChangedByUser, 
			String lastChangedOnDate, ArrayList<AnnotationEntry> relatedAnnotationList, HashMap<Integer, Integer> imageSortInfo) {
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
		this.wallIDs = wallIDs;
		if (this.wallIDs.size()>0) {
			this.wallID= wallIDs.get(0).getWallLocationID();
		}
		else {
			this.wallID=0;
		}
		this.absoluteLeft = absoluteLeft;
		this.absoluteTop = absoluteTop;
		this.modeOfRepresentationID = modeOfRepresentationID;
		this.shortName = shortName;
		this.positionNotes = positionNotes;
		this.masterImageID = masterImageID;
		this.setAccessLevel(accessLevel);
		this.setLastChangedByUser(lastChangedByUser);
		this.setModifiedOn(lastChangedOnDate);
		this.relatedAnnotationList=relatedAnnotationList;
		this.imageSortInfo= imageSortInfo;
	}

	public DepictionEntry clone() {
		DepictionEntry clonedDepictionEntry = new DepictionEntry(depictionID, styleID, inscriptions, separateAksaras, dating, description, backgroundColour, generalRemarks,
				otherSuggestedIdentifications, width, height, expedition, purchaseDate, location, inventoryNumber, vendor, storyID,
				cave, wallIDs, absoluteLeft, absoluteTop, modeOfRepresentationID, shortName, positionNotes, masterImageID, accessLevel, lastChangedByUser, modifiedOn, relatedAnnotationList, imageSortInfo);
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
		ArrayList<AnnotatedBibliographyEntry> clonedRelatedBibliographyList = new ArrayList<AnnotatedBibliographyEntry>();
		for (AnnotatedBibliographyEntry abe : this.relatedBibliographyList) {
			clonedRelatedBibliographyList.add(abe);
		}
		clonedDepictionEntry.setRelatedBibliographyList(clonedRelatedBibliographyList);
		ArrayList<IconographyEntry> clonedRelatedIconographyList = new ArrayList<IconographyEntry>();
		for (IconographyEntry ie : this.relatedIconographyList) {
			clonedRelatedIconographyList.add(ie);
		}
		clonedDepictionEntry.setRelatedIconographyList(clonedRelatedIconographyList);
		ArrayList<AnnotationEntry> clonedRelatedAnnotationList = new ArrayList<AnnotationEntry>();
		for (AnnotationEntry ie : this.relatedAnnotationList) {
			clonedRelatedAnnotationList.add(ie);
		}
		clonedDepictionEntry.setRelatedAnnotationList(clonedRelatedAnnotationList);
		return clonedDepictionEntry;
	}
	public void setStyle(StyleEntry se) {
		this.style=se;
	}
	
	public void setModeOfRepresentation(ModeOfRepresentationEntry mre) {
		this.modeOfRepresentation=mre;
	}
	
	public int getDepictionID() {
		return depictionID;
	}

	public void setDepictionID(int depictionID) {
		this.depictionID = depictionID;
	}

	public HashMap<Integer, Integer> getImageSortInfo() {
		return imageSortInfo;
	}

	public void setImageSortInfo(HashMap<Integer, Integer> imageSortInfo) {
		this.imageSortInfo = imageSortInfo;
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
	public List<WallTreeEntry> getWalls() {
		//Util.doLogging("Size of related Images: "+relatedImages.size());
		return wallIDs;
	}
	public void setWalls(List<WallTreeEntry> walls) {
		wallIDs= walls;
	}
	public void addRelatedImages(ImageEntry ie) {
		relatedImages.add(ie);
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

	public ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyList() {
		return relatedBibliographyList;
	}

	public void setRelatedBibliographyList(ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		this.relatedBibliographyList = relatedBibliographyList;
	}

	public ArrayList<IconographyEntry> getRelatedIconographyList() {
		return relatedIconographyList;
	}

	public void setRelatedIconographyList(ArrayList<IconographyEntry> relatedIconographyList) {
		this.relatedIconographyList = relatedIconographyList;
	}
	public ArrayList<AnnotationEntry> getRelatedAnnotationList() {
		return relatedAnnotationList;
	}
	public void addAnnotation(AnnotationEntry ae) {
		this.relatedAnnotationList.add(ae);
	}

	public void setRelatedAnnotationList(ArrayList<AnnotationEntry> relatedAnnotationList) {
		this.relatedAnnotationList = relatedAnnotationList;
	}
	public String compareStrings(String oldString, String newString, String name) {
		String changedValues="";
		if ((newString!=null)&(!newString.isEmpty())) {
			if((oldString!=null)&(!oldString.isEmpty())) {
				if (!newString.equals(oldString)) {
					changedValues+="Changed "+name+" from "+oldString+" to "+newString+". ";
				}
			}
			else {
				changedValues+="removed "+name+": "+newString;
			}
		}
		else {
			if (oldString==null) {
				changedValues+="added "+name+": "+oldString;
			}
		}
		return changedValues;

	}
	public String getchanges(DepictionEntry oldDe) {
		String changedValues = "";
//		try {
			if (depictionID!=oldDe.getDepictionID()) {
				changedValues+="Changed depictionID from "+Integer.toString(oldDe.getDepictionID())+" to "+Integer.toString(depictionID)+". ";
			}
			if (styleID!=oldDe.getStyleID()) {
				changedValues+="Changed styleID from "+Integer.toString(oldDe.getStyleID())+" to "+Integer.toString(styleID)+". ";
			}
			if (storyID!=oldDe.getStoryID()) {
				changedValues+="Changed storyID from "+Integer.toString(oldDe.getStoryID())+" to "+Integer.toString(storyID)+". ";
			}
			if (wallID!=oldDe.getWallID()) {
				changedValues+="Changed wallID from "+Integer.toString(oldDe.getWallID())+" to "+Integer.toString(wallID)+". ";
			}
			if (absoluteLeft!=oldDe.getAbsoluteLeft()) {
				changedValues+="Changed absoluteLeft from "+Integer.toString(oldDe.getAbsoluteLeft())+" to "+Integer.toString(absoluteLeft)+". ";
			}
			if (modeOfRepresentationID!=oldDe.getModeOfRepresentationID()) {
				changedValues+="Changed modeOfRepresentationID from "+Integer.toString(oldDe.getModeOfRepresentationID())+" to "+Integer.toString(modeOfRepresentationID)+". ";
			}
			if (masterImageID!=oldDe.getMasterImageID()) {
				changedValues+="Changed masterImageID from "+Integer.toString(oldDe.getMasterImageID())+" to "+Integer.toString(masterImageID)+". ";
			}
			if (width!=oldDe.getWidth()) {
				changedValues+="Changed width from "+Double.toString(oldDe.getWidth())+" to "+Double.toString(width)+". ";
			}
			if (height!=oldDe.getHeight()) {
				changedValues+="Changed height from "+Double.toString(oldDe.getHeight())+" to "+Double.toString(height)+". ";
			}
			changedValues+=compareStrings(oldDe.getInscriptions(),inscriptions, "inscriptions");
			changedValues+=compareStrings(oldDe.getSeparateAksaras(),separateAksaras,"separateAksaras");
			changedValues+=compareStrings(oldDe.getDating(),dating,"dating");
			changedValues+=compareStrings(oldDe.getDescription(),description,"description");
			changedValues+=compareStrings(oldDe.getBackgroundColour(),backgroundColour,"backgroundColour");
			changedValues+=compareStrings(oldDe.getGeneralRemarks(),generalRemarks,"generalRemarks");
			changedValues+=compareStrings(oldDe.getOtherSuggestedIdentifications(),otherSuggestedIdentifications,"otherSuggestedIdentifications");
			changedValues+=compareStrings(oldDe.getInventoryNumber(),inventoryNumber,"inventoryNumber");
			changedValues+=compareStrings(oldDe.getShortName(),shortName,"shortName");
			changedValues+=compareStrings(oldDe.getPositionNotes(),positionNotes,"positionNotes");
			if (purchaseDate!=oldDe.getPurchaseDate()) {
				changedValues+="Changed purchaseDate from "+oldDe.getPurchaseDate()+" to "+purchaseDate+". ";
			}
			if (location.getLocationID()!=oldDe.getLocation().getLocationID()) {
				changedValues+="Changed location from "+oldDe.getLocation().getName()+" to "+location.getName()+". ";
			}
			if (vendor!=null) {
				if(oldDe.getVendor()!=null) {
					if (vendor.getVendorID()!=oldDe.getVendor().getVendorID()) {
						changedValues+="Changed vendor from "+oldDe.getVendor().getVendorName()+" to "+vendor.getVendorName()+". ";
					}
				}
				else {
					changedValues+="added vendor: "+vendor.getVendorName();
				}
			}
			else {
				if (oldDe.getVendor()!=null) {
					changedValues+="removed vendor: "+oldDe.getVendor().getVendorName();
				}
			}
			if (cave!=null) {
				if(oldDe.getCave()!=null) {
					if (cave.getCaveID()!=oldDe.getCave().getCaveID()) {
						changedValues+="Changed vendor from "+oldDe.getCave().getOfficialNumber()+" to "+cave.getOfficialNumber()+". ";
					}
				}
				else {
					changedValues+="added vendor: "+cave.getOfficialNumber();
				}
			}
			else {
				if (oldDe.getVendor()!=null) {
					changedValues+="removed vendor: "+oldDe.getCave().getOfficialNumber();
				}
			}
			if (expedition!=null) {
				if(oldDe.getVendor()!=null) {
					if (expedition.getExpeditionID()!=oldDe.getExpedition().getExpeditionID()) {
						changedValues+="Changed vendor from "+oldDe.getExpedition().getName()+" to "+expedition.getName()+". ";
					}
				}
				else {
					changedValues+="added vendor: "+expedition.getName();
				}
			}
			else {
				if (oldDe.getVendor()!=null) {
					changedValues+="removed expedition: "+oldDe.getExpedition().getName();
				}
			}
	
			List<WallTreeEntry> newWalls = new ArrayList<WallTreeEntry>(wallIDs);
			List<WallTreeEntry> oldWalls = new ArrayList<WallTreeEntry>(oldDe.getWalls());
			System.err.println("newWalls before: "+Integer.toString(newWalls.size()));
			System.err.println("oldWalls before: "+Integer.toString(oldWalls.size()));
			newWalls.removeAll(oldDe.getWalls());
			oldWalls.removeAll(wallIDs);
			System.err.println("newWalls after: "+Integer.toString(newWalls.size()));
			System.err.println("oldWalls after: "+Integer.toString(oldWalls.size()));
			for (WallTreeEntry wte : newWalls) {
				changedValues+="Added wall: "+wte.getText()+". ";
			}
			for (WallTreeEntry wte : oldWalls) {
				changedValues+="Removed wall: "+wte.getText()+". ";
			}
			List<ImageEntry> newImages = new ArrayList<ImageEntry>(relatedImages);
			List<ImageEntry> oldImages = new ArrayList<ImageEntry>(oldDe.getRelatedImages());
			newImages.removeAll(oldDe.getRelatedImages());
			oldImages.removeAll(relatedImages);
			for (ImageEntry wte : newImages) {
				changedValues+="Added image: "+wte.getTitle()+". ";
			}
			for (ImageEntry wte : oldImages) {
				changedValues+="Removed image: "+wte.getTitle()+". ";
			}
			List<PreservationAttributeEntry> newPAEs = new ArrayList<PreservationAttributeEntry>(preservationAttributesList);
			List<PreservationAttributeEntry> oldPAEs = new ArrayList<PreservationAttributeEntry>(oldDe.getPreservationAttributesList());
			newPAEs.removeAll(oldDe.getRelatedImages());
			oldPAEs.removeAll(relatedImages);
			for (PreservationAttributeEntry wte : newPAEs) {
				changedValues+="Added PreservationAttributeEntry: "+wte.getName()+". ";
			}
			for (PreservationAttributeEntry wte : oldPAEs) {
				changedValues+="Removed PreservationAttributeEntry: "+wte.getName()+". ";
			}
			List<AnnotatedBibliographyEntry> newABEs = new ArrayList<AnnotatedBibliographyEntry>(relatedBibliographyList);
			List<AnnotatedBibliographyEntry> oldABEs = new ArrayList<AnnotatedBibliographyEntry>(oldDe.getRelatedBibliographyList());
			newABEs.removeAll(oldDe.getRelatedBibliographyList());
			oldABEs.removeAll(relatedBibliographyList);
			for (AnnotatedBibliographyEntry wte : newABEs) {
				changedValues+="Added AnnotatedBibliographyEntry: "+wte.getLabel()+". ";
			}
			for (AnnotatedBibliographyEntry wte : oldABEs) {
				changedValues+="Removed AnnotatedBibliographyEntry: "+wte.getLabel()+". ";
			}
			List<IconographyEntry> newRILs = new ArrayList<IconographyEntry>(relatedIconographyList);
			List<IconographyEntry> oldRILs = new ArrayList<IconographyEntry>(oldDe.getRelatedIconographyList());
			newRILs.removeAll(oldDe.getRelatedBibliographyList());
			oldRILs.removeAll(relatedBibliographyList);
			for (IconographyEntry wte : newRILs) {
				changedValues+="Added IconographyEntry: "+wte.getText()+". ";
			}
			for (IconographyEntry wte : oldRILs) {
				changedValues+="Removed IconographyEntry: "+wte.getText()+". ";
			}
//		}
//		catch (Exception e) {
//			return changedValues+ "protocolling abborted due to error: "+ e.getMessage();
//		}
		System.err.println("returning: "+changedValues);
		return changedValues;
	}
}
