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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


/**
 * @author alingnau
 *
 */
public class AnnotationEntry extends AbstractEntry {
	private int depictionID = 0;
	private String annotoriousID = "";
	private ArrayList<IconographyEntry> tags;
	private String geoJson, w3c, image = "";
	private boolean delete;
	private boolean update;
	private double creationTime;
	private double modificationTime;
	private boolean isProposed;

	/**
	 * The default constructor is used to create a new AuthorEntry. The authorID
	 * is set to 0 to indicate that this entry is not taken from a database and
	 * therefore has to be inserted instead of updated.
	 */
	public AnnotationEntry() {	}

	public AnnotationEntry(int DepictionID, String annotoriousID, ArrayList<IconographyEntry> tags, String geoJson, String image, boolean delete, boolean update, double creationTime, double modificationTime, boolean isProposed) {
		this.depictionID = DepictionID;
		this.annotoriousID = annotoriousID;
		this.tags=tags;
		this.geoJson=geoJson;
		this.image=image;
		this.delete=delete;
		this.update=update;
		this.creationTime=creationTime;
		this.modificationTime=modificationTime;
		this.isProposed=isProposed;
	}

	public boolean getIsProposed() {
		return isProposed;
	}

	public void setTimeSpan(boolean isProposed) {
		this.isProposed = isProposed;
	}
	public double getModificationTime() {
		return modificationTime;
	}

	public void setModificationTime(double modificationTime) {
		this.modificationTime = modificationTime;
	}
	public double getCreationTime() {
		return creationTime;
	}

	public void setCreatonTime(double creationTime) {
		this.creationTime = creationTime;
	}
	public int getDepictionID() {
		return depictionID;
	}

	public void setDepictionID(int depictionID) {
		this.depictionID = depictionID;
	}
	public ArrayList<IconographyEntry> getTags() {
		return tags;
	}
	public String getTagsAsString() {
		String stringTags = "";
		for (IconographyEntry ie : tags)
		{
			stringTags += ie.getText() + "; ";
		}
		return stringTags;
	}
	public void setTags(ArrayList<IconographyEntry> IconographyEntry) {
		this.tags = IconographyEntry;
	}
	public void addTagEntry(IconographyEntry tag) {
		this.tags.add(tag);
	}
	
	public String getAnnotoriousID() {
		return annotoriousID;
	}

	public void setAnnotoriousID(String annotoriousID) {
		this.annotoriousID = annotoriousID;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getGeoJson() {
		return geoJson;
	}

	public void setW3c(String w3c) {
		this.w3c = w3c;
	}
	public String getW3c() {
		return w3c;
	}

	public void setGeoJson(String geoJson) {
		this.geoJson = geoJson;
	}

	public Boolean getDelete() {
		return delete;
	}

	public void setUpdate(Boolean update) {
		this.update = update;
	}
	public Boolean getUpdate() {
		return update;
	}
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Annotation-" + annotoriousID;
	}
	public Boolean equals(AnnotationEntry entry) {
		if (annotoriousID == entry.getAnnotoriousID()) {
			return true;
		}
		else{
			return false;
		}
	}


}
