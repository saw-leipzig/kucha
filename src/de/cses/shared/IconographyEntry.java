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
import java.text.Normalizer;

public class IconographyEntry extends AbstractEntry {
	
	private int iconographyID, parentID;
	private String text;
	private String search;
	private ArrayList<IconographyEntry> children;
	
	public IconographyEntry() { }

	public IconographyEntry(int iconographyID, int parentID, String text, String search) {
		super();
		this.iconographyID = iconographyID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
	}

	public int getIconographyID() {
		return iconographyID;
	}

	public void setIconographyID(int iconographyID) {
		this.iconographyID = iconographyID;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	public String getSearch() {
		return search;
	}	

	public void setSearch(String search) {
		this.search = search;
	}

	public ArrayList<IconographyEntry> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<IconographyEntry> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "IconographyEntry-" + iconographyID;
	}
	
	public Boolean equals(IconographyEntry entry) {
		if (iconographyID == entry.getIconographyID()) {
			return true;
		}
		else{
			return false;
		}
	}

}
