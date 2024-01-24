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
	private ArrayList<PositionEntry> position = new ArrayList<PositionEntry>();
	
	public WallTreeEntry() { }

	public WallTreeEntry(int wallLocationID, int parentID, String text, String search) {
		super();
		this.wallLocationID = wallLocationID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
		this.children = null;
	}
	public WallTreeEntry(int wallLocationID, int parentID, String text, String search, ArrayList<WallDimensionEntry> dimension,  ArrayList<PositionEntry> position) {
		super();
		this.wallLocationID = wallLocationID;
		this.parentID = parentID;
		this.text = text;
		this.search = search;
		this.children = null;
		if (dimension!=null) {
			this.dimension=dimension;			
		}
		if (position!=null) {
			this.position=position;			
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
		String name=text;
		String dimensions ="";
		if (dimension.size() == 0 && position.size() == 0) {
			return text;
		} else {
			for (WallDimensionEntry wde: dimension) {
				String dimensionString = wde.getName().isEmpty()? "Register" :"Register " + wde.getName() + " (" + Integer.toString(wde.getRegisters()) + ", " + Integer.toString(wde.getColumns()) + ")";
				if (dimensions.isEmpty()) {
					dimensions = dimensionString;
				} else {
					dimensions += ", " + dimensionString;
				}
			}
			String posNames ="";
			for (PositionEntry pos : position) {
				if (posNames=="") {
					posNames = pos.getNameWithPosition();
				}
				else {
					posNames = posNames+", "+pos.getNameWithPosition();
				}
			}
			if (!posNames.isEmpty()) {
				name=name+" ("+posNames + ")";
			}			
			if (dimensions.isEmpty()) {
				return name;
			} else {
				return name + " - " + dimensions;	
			}
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
		ArrayList<WallDimensionEntry> updatedDimension = new ArrayList<WallDimensionEntry>();
		boolean found = false;
		for (WallDimensionEntry wde: dimension) {
			if (wde.getWallDimensionID() == de.getWallDimensionID()) {
				updatedDimension.add(de);
				found = true;
			} else {
				updatedDimension.add(wde);
			}
		}
		if (!found) {
			updatedDimension.add(de);
		}
		this.dimension = updatedDimension;
	}
	public void setPositions(ArrayList<PositionEntry> pe) {
		this.position=pe;
	}
	public ArrayList<PositionEntry> getPositions() {
		return this.position;
	}
	public void addPosition(PositionEntry updatedpe) {
		ArrayList<PositionEntry> updatedPosition = new ArrayList<PositionEntry>();
		boolean found = false;
		for (PositionEntry pe: position) {
			if (pe.getPositionID() == pe.getPositionID()) {
				updatedPosition.add(updatedpe);
				found = true;
			} else {
				updatedPosition.add(pe);
			}
		}
		if (found) {
			updatedPosition.add(updatedpe);
		}
		this.position = updatedPosition;
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
        	if (this.wallLocationID==((WallTreeEntry)anObject).getWallLocationID()) {
        			isEqual=true;
        	}
    	}
        return isEqual;
    }

}
