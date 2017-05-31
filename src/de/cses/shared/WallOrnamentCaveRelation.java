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
	int WallOrnamentCaveRelationID;
	int WallID;
	int OrnamentCaveRelationID;
	int position;
	int function;
	String notes;
	
	public WallOrnamentCaveRelation(){
		
	}
	public int getWallOrnamentCaveRelationID() {
		return WallOrnamentCaveRelationID;
	}
	public void setWallOrnamentCaveRelationID(int wallOrnamentCaveRelationID) {
		WallOrnamentCaveRelationID = wallOrnamentCaveRelationID;
	}
	public int getWallID() {
		return WallID;
	}
	public void setWallID(int wallID) {
		WallID = wallID;
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
	
	

}
