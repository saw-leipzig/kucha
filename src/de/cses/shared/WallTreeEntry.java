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
import java.util.Arrays;

public class WallTreeEntry extends AbstractEntry {
	
	public static final ArrayList<Integer> ANTECHAMBER_LABEL = new ArrayList<Integer>(Arrays.asList(101,104));
	public static final ArrayList<Integer>  MAIN_CHAMBER_LABEL = new ArrayList<Integer>(Arrays.asList(102,106));
	public static final ArrayList<Integer>  MAIN_CHAMBER_CORRIDOR_LABEL= new ArrayList<Integer>(Arrays.asList( 105));
	public static final ArrayList<Integer>  REAR_AREA_LABEL = new ArrayList<Integer>(Arrays.asList( 103,110,113));
	public static final ArrayList<Integer>  REAR_AREA_LEFT_CORRIDOR_LABEL= new ArrayList<Integer>(Arrays.asList(107,108));
	public static final ArrayList<Integer>  REAR_AREA_RIGHT_CORRIDOR_LABEL = new ArrayList<Integer>(Arrays.asList( 111,112));
	

	private int wallLocationID, parentID;
	private String text;
	private String search;
	private ArrayList<WallTreeEntry> children = new ArrayList<WallTreeEntry>();
	private ArrayList<WallDimensionEntry> dimension = new ArrayList<WallDimensionEntry>();
	
	public WallTreeEntry() { }

	public WallTreeEntry(int wallLocationID, int parentID, String text, String search) {
		super();
		this.wallLocationID = wallLocationID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
		this.children = null;
	}
	public WallTreeEntry(int wallLocationID, int parentID, String text, String search, ArrayList<WallDimensionEntry> dimension) {
		super();
		this.wallLocationID = wallLocationID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
		this.children = null;
		if (dimension!=null) {
			this.dimension=dimension;			
		}
	}
	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int iconographyID) {
		this.wallLocationID = iconographyID;
	}

	public int getParentID() {
		return parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}
	public String getWallName() {
		return text;
	}

	public String getText() {
		String name="";
		String dimensions ="";
		if (dimension.size() == 0) {
			return text;
		} else {
			for (WallDimensionEntry wde: dimension) {
				String dimensionString = "Register(" + Integer.toString(wde.getRegisters()) + ", " + Integer.toString(wde.getColumns()) + ")";
				if (dimensions.isEmpty()) {
					dimensions = dimensionString;
				} else {
					dimensions += ", " + dimensionString;
				}
			}
			return text + " - " + dimensions;
		}

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
	public void setDimensions(ArrayList<WallDimensionEntry> de) {
		this.dimension=de;
	}
	public ArrayList<WallDimensionEntry> getDimensions() {
		return this.dimension;
	}
	public void addDimension(WallDimensionEntry de) {
		this.dimension.add(de);
	}
	public void setChildren(ArrayList<WallTreeEntry> children) {
		this.children = children;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "WallTreeEntry-" + wallLocationID;
	}
    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof WallTreeEntry){
    		ArrayList<PositionEntry> compPos = new ArrayList<PositionEntry>();
        	if (this.wallLocationID==((WallTreeEntry)anObject).getWallLocationID()) {
        		if (compPos.size()==0) {
        			isEqual=true;
        		}
        	}
    	}
        return isEqual;
    }

}
