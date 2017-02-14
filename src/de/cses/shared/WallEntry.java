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

public class WallEntry {
	
	private int socketBorderID;
	private int corniceBorderID;
	private int wallID;
	
	public int getSocketBorderID() {
		return socketBorderID;
	}
	public void setSocketBorderID(int socketBorderID) {
		this.socketBorderID = socketBorderID;
	}
	public int getCorniceBorderID() {
		return corniceBorderID;
	}
	public void setCorniceBorderID(int corniceBorderID) {
		this.corniceBorderID = corniceBorderID;
	}
	public int getWallID() {
		return wallID;
	}
	public void setWallID(int wallID) {
		this.wallID = wallID;
	}
	

}