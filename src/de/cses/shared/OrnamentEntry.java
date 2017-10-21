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
	private String annotations;
	private String interpretation;
	private String references;
	private String sketch;
	private MainTypologicalClass maintypologycalClass;
	private StructureOrganization structureOrganization;
	private ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
	private ArrayList<OrnamentCaveRelation> cavesRelations = new ArrayList<OrnamentCaveRelation>();

	public OrnamentEntry() {
		ornamentID = 0;
	}

	public OrnamentEntry(int ornamentID, String code) {
		super();
		this.ornamentID = ornamentID;
		this.code = code;
		cavesRelations = new ArrayList<OrnamentCaveRelation>();
	}

	public int getOrnamentID() {
		return ornamentID;
	}

	public void setOrnamentID(int ornamentID) {
		this.ornamentID = ornamentID;
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

	public String getSketch() {
		return sketch;
	}

	public void setSketch(String sketch) {
		this.sketch = sketch;
	}

	public List<OrnamentCaveRelation> getCavesRelations() {
		return cavesRelations;
	}

	public void setCavesRelations(ArrayList<OrnamentCaveRelation> list) {
		this.cavesRelations = list;
	}

	public String getAnnotations() {
		return annotations;
	}

	public void setAnnotations(String annotations) {
		this.annotations = annotations;
	}

	/**
	 * @return the maintypologycalClass
	 */
	public MainTypologicalClass getMaintypologycalClass() {
		return maintypologycalClass;
	}

	/**
	 * @param maintypologycalClass
	 *          the maintypologycalClass to set
	 */
	public void setMaintypologycalClass(MainTypologicalClass maintypologycalClass) {
		this.maintypologycalClass = maintypologycalClass;
	}

	/**
	 * @return the structureOrganization
	 */
	public StructureOrganization getStructureOrganization() {
		return structureOrganization;
	}

	/**
	 * @param structureOrganization
	 *          the structureOrganization to set
	 */
	public void setStructureOrganization(StructureOrganization structureOrganization) {
		this.structureOrganization = structureOrganization;
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

}
