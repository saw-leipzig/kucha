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

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrnamentEntry implements IsSerializable{
	private int ornamentID;
	private String code;
	private String description;
	private String remarks;
	private String annotations;
	private String interpretation;
	private String references;
	private String sketch;
	private int maintypologycalClass;
	private int structureOrganization;
	private ArrayList<Integer> imageIDs = new ArrayList<Integer>();
	private ArrayList<OrnamentCaveRelation> cavesRelations = new ArrayList<OrnamentCaveRelation>();

	
	public OrnamentEntry(){
		
	}
	
	public OrnamentEntry(int ornamentID, String code){
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

	public int getMaintypologycalClass() {
		return maintypologycalClass;
	}

	public void setMaintypologycalClass(int maintypologycalClass) {
		this.maintypologycalClass = maintypologycalClass;
	}

	public int getStructureOrganization() {
		return structureOrganization;
	}

	public void setStructureOrganization(int structureOrganization) {
		this.structureOrganization = structureOrganization;
	}

	public ArrayList<Integer> getImageIDs() {
		return imageIDs;
	}

	public void setImageIDs(ArrayList<Integer> imageIDs) {
		this.imageIDs = imageIDs;
	}
	
	
	
	

	
	
	
	
	

}
