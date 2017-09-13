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

import com.google.gwt.user.client.rpc.IsSerializable;

public class PictorialElementEntry extends AbstractEntry {
	
	private int pictorialElementID, parentID;
	String text;
	ArrayList<PictorialElementEntry> children;
	
	public PictorialElementEntry() {
		super();
	}

	public PictorialElementEntry(int pictorialElementID, int parentID, String text) {
		super();
		this.pictorialElementID = pictorialElementID;
		this.parentID = parentID;
		this.text = text;
	}

	public int getPictorialElementID() {
		return pictorialElementID;
	}

	public void setPictorialElementID(int iconographyID) {
		this.pictorialElementID = iconographyID;
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

	public ArrayList<PictorialElementEntry> getChildren() {
		return children;
	}

	public void setChildren(ArrayList<PictorialElementEntry> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "PictorialElement-" + pictorialElementID;
	}

}
