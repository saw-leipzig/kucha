/*
 * Copyright 2016-2017
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

import java.util.ArrayList;
import java.util.List;

public class OrnamentEntry extends AbstractEntry {
	private int typicalID;
	private String code;
	private ArrayList<Integer> sourceDepictionIDs = new ArrayList<Integer>();
	private String description;
	private String remarks;
	private int iconographyID;
	private boolean isVirtualTour;
	private double virtualTourOrder;
	private IconographyEntry ie;
	//private String annotations; wurde mal geloescht, evtl wird es irgendwann wieder gewollt
	private ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
	private ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList = new ArrayList<AnnotatedBibliographyEntry>();
	private Integer masterImageID = 0;
	private ArrayList<IconographyEntry> relatedIconographyList = new ArrayList<IconographyEntry>();
	private ArrayList<AnnotationEntry> relatedAnnotationList = new ArrayList<AnnotationEntry>();
	private List<ExternalRessourceEntry> relatedExternalRessourcesList = new ArrayList<ExternalRessourceEntry>();

	public OrnamentEntry() {
		typicalID = 0;
	}
	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof OrnamentEntry) {
			return (this.typicalID == ((OrnamentEntry) object).getTypicalID());
		}
		return false;
	}

	public OrnamentEntry(int typicalID, String code) {
		this.typicalID = typicalID;
		this.code = code;
	}

	public OrnamentEntry(int typicalID, String code, String description, String remarks, 
			//String annotations,
			String interpretation,
			String references, int ornamentClassID, ArrayList<ImageEntry> images,
			ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList, String modifiedOn, int iconographyID, int masterImageID,
			ArrayList<IconographyEntry> relatedIconographyList, ArrayList<AnnotationEntry> relatedAnnotationList, int accessLevel, 
			double tourOrder, boolean isVirtualTour, List<ExternalRessourceEntry> relatedExternalRessourcesList, ArrayList<Integer> sourceDepictionIDs) {
		this.typicalID = typicalID;
		this.code = code;
		this.description = description;
		this.remarks = remarks;
		//this.annotations = annotations;
		this.images = images;
		this.relatedBibliographyList = relatedBibliographyList;
		this.setModifiedOn(modifiedOn);
		this.iconographyID=iconographyID;
		this.masterImageID=masterImageID;
		this.relatedIconographyList=relatedIconographyList;
		this.relatedAnnotationList=relatedAnnotationList;
		this.accessLevel=accessLevel;
		this.virtualTourOrder=tourOrder;
		this.isVirtualTour = isVirtualTour;
		this.relatedExternalRessourcesList = relatedExternalRessourcesList;
		this.sourceDepictionIDs = sourceDepictionIDs;
	}

	public OrnamentEntry(int typicalID, String code, String description, String remarks, 
			//String annotations, String modifiedOn, ArrayList<IconographyEntry> relatedIconographyList,
			ArrayList<AnnotationEntry> relatedAnnotationList, int accessLevel, List<ExternalRessourceEntry> relatedExternalRessourcesList, ArrayList<Integer> sourceDepictionID) {
		this.typicalID = typicalID;
		this.code = code;
		this.description = description;
		this.remarks = remarks;
		//this.annotations = annotations;
		this.setModifiedOn(modifiedOn);
		this.relatedIconographyList=relatedIconographyList;
		this.relatedAnnotationList=relatedAnnotationList;
		this.accessLevel=accessLevel;
		this.relatedExternalRessourcesList = relatedExternalRessourcesList;
		this.sourceDepictionIDs = sourceDepictionIDs;
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

	public List<ExternalRessourceEntry> getrelatedExternalRessourcesList() {
		return relatedExternalRessourcesList;
	}
	public void adRelatedExternalRessourcesList(ExternalRessourceEntry ere) {
		this.relatedExternalRessourcesList.add(ere);
	}

	public void setRelatedExternalRessources(List<ExternalRessourceEntry> relatedExternalRessourcesList) {
		this.relatedExternalRessourcesList = relatedExternalRessourcesList;
	}
	
	public void deleteRelatedExternalRessourceEntry(ExternalRessourceEntry ere) {
		List<ExternalRessourceEntry> newRelatedExternalRessourcesList = new ArrayList<ExternalRessourceEntry>();
		for (ExternalRessourceEntry extres : this.relatedExternalRessourcesList) {
			if (extres.compareTo(ere) != 0) {
				newRelatedExternalRessourcesList.add(extres);
			}
		}
		this.relatedExternalRessourcesList = newRelatedExternalRessourcesList;
	}

	public int getTypicalID() {
		return typicalID;
	}

	public void setTypicalID(int typicalID) {
		this.typicalID = typicalID;
	}
	
	public ArrayList<Integer> getSourceDepictionIDs() {
		return sourceDepictionIDs;
	}

	public void setSourceDepictionIDs(ArrayList<Integer> sourceDepictionIDs) {
		this.sourceDepictionIDs = sourceDepictionIDs;
	}
	
	public IconographyEntry getIconographyEntry() {
		return ie;
	}

	public void setVirtualTourOrder(double virtualTourOrder) {
		this.virtualTourOrder = virtualTourOrder;
	}
	public double getVirtualTourOrder() {
		return virtualTourOrder;
	}

	public void setIsVirtualTour(boolean isVirtualTour) {
		this.isVirtualTour = isVirtualTour;
	}
	public boolean getisVirtualTour() {
		return isVirtualTour;
	}

	public void setIconographyEntry(IconographyEntry ie) {
		this.ie = ie;
	}
	public int getIconographyID() {
		return iconographyID;
	}

	public void setIconographyID(int iconographyID) {
		this.iconographyID = iconographyID;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}
	public ArrayList<IconographyEntry> getRelatedIconographyList() {
		return relatedIconographyList;
	}

	public void setRelatedIconographyList(ArrayList<IconographyEntry> relatedIconographyList) {
		this.relatedIconographyList = relatedIconographyList;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public void addRelatedImages(ImageEntry ie) {
		images.add(ie);
	}
	public Integer getMasterImageID() {
		return masterImageID != 0 ? masterImageID : (!images.isEmpty() ? images.get(0).getImageID() : 0);
	}

	public void setMasterImageID(Integer masterImageID) {
		this.masterImageID = masterImageID;
	}

	/*public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the images
	 */
	public ArrayList<ImageEntry> getImages() {
		return images;
	}

	/**
	 * @param images
	 *          the images to set
	 */
	public void setImages(ArrayList<ImageEntry> images) {
		this.images = images;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Typical-" + typicalID;
	}

	public ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyList() {
		return relatedBibliographyList;
	}
	public void setRelatedBibliographyList(ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		this.relatedBibliographyList = relatedBibliographyList;
	}

}
