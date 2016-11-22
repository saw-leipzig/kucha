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

import com.google.gwt.user.client.rpc.IsSerializable;

public class CaveTypeEntry implements IsSerializable{
	private int caveTypeID;
	private String enShortname;
	private String enDescription;
	
	public CaveTypeEntry(){
		
	}
	
	public CaveTypeEntry(int caveTypeID, String enShortname, String enDescription){
		this.caveTypeID= caveTypeID;
		this.enShortname = enShortname;
		this.enDescription = enDescription;
		
	}
	
	public int getCaveTypeID() {
		return caveTypeID;
	}
	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}
	public String getEnShortname() {
		return enShortname;
	}
	public void setEnShortname(String enShortname) {
		this.enShortname = enShortname;
	}
	public String getEnDescription() {
		return enDescription;
	}
	public void setEnDescription(String enDescription) {
		this.enDescription = enDescription;
	}
	
	

}
