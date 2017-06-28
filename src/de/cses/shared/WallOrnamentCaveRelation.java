/*
 * Copyright 2017 
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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nina
 *
 */
public class WallOrnamentCaveRelation implements IsSerializable{
	int wallOrnamentCaveRelationID;
	int wallID;
	String name;
	int OrnamentCaveRelationID;
	int position;
	int function;
	String notes;
	
	public WallOrnamentCaveRelation(){
		
	}
	public int getWallOrnamentCaveRelationID() {
		return wallOrnamentCaveRelationID;
	}
	public void setWallOrnamentCaveRelationID(int wallOrnamentCaveRelationID) {
		this.wallOrnamentCaveRelationID = wallOrnamentCaveRelationID;
	}
	public int getWallID() {
		return wallID;
	}
	public void setWallID(int wallID) {
		this.wallID = wallID;
	}
	public int getOrnamentCaveRelationID() {
		return OrnamentCaveRelationID;
	}
	public void setOrnamentCaveRelationID(int ornamentCaveRelationID) {
		OrnamentCaveRelationID = ornamentCaveRelationID;
	}
	public int getPosition() {
		return position;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public int getFunction() {
		return function;
	}
	public void setFunction(int function) {
		this.function = function;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		String name = "Wallnumber: " + Integer.toString(wallID);
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	
	

}
