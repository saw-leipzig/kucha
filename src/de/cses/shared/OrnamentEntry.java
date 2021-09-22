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
	private int ornamentID;
	private String code;
	private String description;
	private String remarks;
	private int iconographyID;
	//private String annotations; wurde mal geloescht, evtl wird es irgendwann wieder gewollt
	private String interpretation;
	private String references;
	private int ornamentClass; // hei�t jetzt motif, name wurde nur oberflaechlich angepasst. 
	private int structureOrganizationID; // gibt es nicht mehr, wird evtl. mal wieder gewollt
	private ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
	private ArrayList<OrnamentCaveRelation> cavesRelations = new ArrayList<OrnamentCaveRelation>();
	private ArrayList<OrnamentComponentsEntry> ornamentComponents = new ArrayList<OrnamentComponentsEntry>();
	private ArrayList<InnerSecondaryPatternsEntry> innerSecondaryPatterns = new ArrayList<InnerSecondaryPatternsEntry>();
	private ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList = new ArrayList<AnnotatedBibliographyEntry>();
	private Integer masterImageID = 0;
	private ArrayList<IconographyEntry> relatedIconographyList = new ArrayList<IconographyEntry>();
	private ArrayList<AnnotationEntry> relatedAnnotationList = new ArrayList<AnnotationEntry>();

	public OrnamentEntry() {
		ornamentID = 0;
	}
	@Override
	public boolean equals(Object object) {
		if (object != null && object instanceof OrnamentEntry) {
			return (this.ornamentID == ((OrnamentEntry) object).getOrnamentID());
		}
		return false;
	}

	public OrnamentEntry(int ornamentID, String code) {
		this.ornamentID = ornamentID;
		this.code = code;
	}

	public OrnamentEntry(int ornamentID, String code, String description, String remarks, 
			//String annotations,
			String interpretation,
			String references, int ornamentClassID, ArrayList<ImageEntry> images, ArrayList<OrnamentCaveRelation> cavesRelations,
			ArrayList<OrnamentComponentsEntry> ornamentComponents, ArrayList<InnerSecondaryPatternsEntry> innerSecondaryPatterns,
			ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList, String modifiedOn, int iconographyID, int masterImageID,
			ArrayList<IconographyEntry> relatedIconographyList, ArrayList<AnnotationEntry> relatedAnnotationList) {
		this.ornamentID = ornamentID;
		this.code = code;
		this.description = description;
		this.remarks = remarks;
		//this.annotations = annotations;
		this.interpretation = interpretation;
		this.references = references;
		this.ornamentClass = ornamentClassID;
		this.images = images;
		this.innerSecondaryPatterns = innerSecondaryPatterns;
		this.cavesRelations = cavesRelations;
		this.ornamentComponents = ornamentComponents;
		this.relatedBibliographyList = relatedBibliographyList;
		this.setModifiedOn(modifiedOn);
		this.iconographyID=iconographyID;
		this.masterImageID=masterImageID;
		this.relatedIconographyList=relatedIconographyList;
		this.relatedAnnotationList=relatedAnnotationList;
	}

	public OrnamentEntry(int ornamentID, String code, String description, String remarks, 
			//String annotations, 
			String interpretation,
			String references, int ornamentClassID, String modifiedOn, ArrayList<IconographyEntry> relatedIconographyList,
			ArrayList<AnnotationEntry> relatedAnnotationList) {
		this.ornamentID = ornamentID;
		this.code = code;
		this.description = description;
		this.remarks = remarks;
		//this.annotations = annotations;
		this.interpretation = interpretation;
		this.references = references;
		this.ornamentClass = ornamentClassID;
		this.setModifiedOn(modifiedOn);
		this.relatedIconographyList=relatedIconographyList;
		this.relatedAnnotationList=relatedAnnotationList;
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

	public int getOrnamentID() {
		return ornamentID;
	}

	public void setOrnamentID(int ornamentID) {
		this.ornamentID = ornamentID;
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

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public String getReferences() {
		return references;
	}

	public void setReferences(String references) {
		this.references = references;
	}

	public List<OrnamentCaveRelation> getCavesRelations() {
		return cavesRelations;
	}
	public void addRelatedImages(ImageEntry ie) {
		images.add(ie);
	}
	public void setCavesRelations(ArrayList<OrnamentCaveRelation> list) {
		this.cavesRelations = list;
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
		return "OrnamentEntry" + ornamentID;
	}

	/**
	 * @return the ornamentClass
	 */
	public int getOrnamentClass() {
		return ornamentClass;
	}

	/**
	 * @param ornamentClass
	 *          the ornamentClass to set
	 */
	public void setOrnamentClass(int ornamentClass) {
		this.ornamentClass = ornamentClass;
	}

	/**
	 * @return the structureOrganizationID
	 */
	public int getStructureOrganizationID() {
		return structureOrganizationID;
	}

	/**
	 * @param structureOrganizationID
	 *          the structureOrganizationID to set
	 */
	public void setStructureOrganizationID(int structureOrganizationID) {
		this.structureOrganizationID = structureOrganizationID;
	}

	/**
	 * @return the ornamentComponents
	 */
	public ArrayList<OrnamentComponentsEntry> getOrnamentComponents() {
		return ornamentComponents;
	}

	/**
	 * @param ornamentComponents
	 *          the ornamentComponents to set
	 */
	public void setOrnamentComponents(ArrayList<OrnamentComponentsEntry> ornamentComponents) {
		this.ornamentComponents = ornamentComponents;
	}

	/**
	 * @return the innerSecondaryPatterns
	 */
	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() {
		return innerSecondaryPatterns;
	}

	/**
	 * @param innerSecondaryPatterns
	 *          the innerSecondaryPatterns to set
	 */
	public void setInnerSecondaryPatterns(ArrayList<InnerSecondaryPatternsEntry> innerSecondaryPatterns) {
		this.innerSecondaryPatterns = innerSecondaryPatterns;
	}
	public ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyList() {
		return relatedBibliographyList;
	}
	public void setRelatedBibliographyList(ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		this.relatedBibliographyList = relatedBibliographyList;
	}

}
