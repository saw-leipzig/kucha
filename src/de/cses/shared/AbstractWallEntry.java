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

public class AbstractWallEntry extends AbstractEntry {
	
	public static String ANTECHAMBER_FRONT_WALL = "antechamber front wall";
	public static String ANTECHAMBER_LEFT_WALL = "antechamber left wall";
	public static String ANTECHAMBER_REAR_WALL = "antechamber rear wall";
	public static String ANTECHAMBER_RIGHT_WALL = "antechamber right wall";
	public static String MAIN_CHAMBER_CORRIDOR_INNER_WALL = "main chamber corridor inner wall";
	public static String MAIN_CHAMBER_CORRIDOR_OUTER_WALL = "main chamber corridor outer wall";
	public static String MAIN_CHAMBER_FRONT_WALL = "main chamber front wall";
	public static String MAIN_CHAMBER_LEFT_WALL = "main chamber left wall";
	public static String MAIN_CHAMBER_REAR_WALL = "main chamber rear wall";
	public static String MAIN_CHAMBER_RIGHT_WALL = "main chamber right wall";
	public static String REAR_AREA_LEFT_CORRIDOR_INNER_WALL = "rear area left corridor inner wall";
	public static String REAR_AREA_LEFT_CORRIDOR_OUTER_WALL = "rear area left corridor outer wall";
	public static String REAR_AREA_RIGHT_CORRIDOR_INNER_WALL = "rear area right corridor inner wall";
	public static String REAR_AREA_RIGHT_CORRIDOR_OUTER_WALL = "rear area right corridor outer wall";
	public static String REAR_AREA_INNER_WALL = "rear area inner wall";
	public static String REAR_AREA_LEFT_WALL = "rear area left wall";
	public static String REAR_AREA_OUTER_WALL = "rear area outer wall";
	public static String REAR_AREA_RIGHT_WALL = "rear area right wall";
	public static final ArrayList<Integer> ANTECHAMBER_LABEL = new ArrayList<Integer>(Arrays.asList(101,104));
	public static final ArrayList<Integer>  MAIN_CHAMBER_LABEL = new ArrayList<Integer>(Arrays.asList(102,106));
	public static final ArrayList<Integer>  MAIN_CHAMBER_CORRIDOR_LABEL= new ArrayList<Integer>(Arrays.asList( 105));
	public static final ArrayList<Integer>  REAR_AREA_LABEL = new ArrayList<Integer>(Arrays.asList( 103,110,113));
	public static final ArrayList<Integer>  REAR_AREA_LEFT_CORRIDOR_LABEL= new ArrayList<Integer>(Arrays.asList(107,108));
	public static final ArrayList<Integer>  REAR_AREA_RIGHT_CORRIDOR_LABEL = new ArrayList<Integer>(Arrays.asList( 111,112));
	

	protected int wallLocationID;
	protected ArrayList<WallDimensionEntry> wallDimensions = new ArrayList<WallDimensionEntry>();
	
	public AbstractWallEntry() { }

	public AbstractWallEntry(int wallLocationID) {
		super();
		this.wallLocationID = wallLocationID;
	}
	public AbstractWallEntry(int wallLocationID, ArrayList<WallDimensionEntry> wallDimensions) {
		super();
		this.wallLocationID = wallLocationID;
		if (wallDimensions!=null) {
			this.wallDimensions=wallDimensions;			
		}
	}
	public AbstractWallEntry clone() {
		return new AbstractWallEntry(wallLocationID, wallDimensions);
	}
	public int getWallLocationID() {
		return wallLocationID;
	}

	public void setWallLocationID(int iconographyID) {
		this.wallLocationID = iconographyID;
	}

	public void setDimensions(ArrayList<WallDimensionEntry> de) {
		this.wallDimensions=de;
	}
	public ArrayList<WallDimensionEntry> getDimensions() {
		return this.wallDimensions;
	}
	public void addDimension(WallDimensionEntry de) {
		ArrayList<WallDimensionEntry> updatedDimension = new ArrayList<WallDimensionEntry>();
		boolean found = false;
		for (WallDimensionEntry wde: wallDimensions) {
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
		this.wallDimensions = updatedDimension;
	}
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "AbstractWallTreeEntry-" + wallLocationID;
	}
    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof AbstractWallEntry){
        	if (this.wallLocationID==((AbstractWallEntry)anObject).getWallLocationID()) {
        			isEqual=true;
        	}
    	}
        return isEqual;
    }

}
