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

public class WallTreeEntry extends AbstractEntry {
	
	private int wallLocationID, parentID;
	private String text;
	private String search;
	private ArrayList<WallTreeEntry> children;
	
	public WallTreeEntry() { }

	public WallTreeEntry(int wallLocationID, int parentID, String text, String search) {
		super();
		this.wallLocationID = wallLocationID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
	}

	public int getIconographyID() {
		return wallLocationID;
	}

	public void setIconographyID(int iconographyID) {
		this.wallLocationID = iconographyID;
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

	public ArrayList<WallTreeEntry> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<WallTreeEntry> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "IconographyEntry-" + wallLocationID;
	}
	
	public Boolean equals(WallTreeEntry entry) {
		if (wallLocationID == entry.getIconographyID()) {
			return true;
		}
		else{
			return false;
		}
	}

}
