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

public class AntechamberEntry implements IsSerializable{

	private int antechamberID, frontWallID, leftWallID, rightWallID, rearWallID;
	private double height, width, depth;
	
	public AntechamberEntry() {
	}

	public AntechamberEntry(int antechamberID, int frontWallID, int leftWallID, int rightWallID, int rearWallID, double height,
			double width, double depth) {
		super();
		this.antechamberID = antechamberID;
		this.frontWallID = frontWallID;
		this.leftWallID = leftWallID;
		this.rightWallID = rightWallID;
		this.rearWallID = rearWallID;
		this.height = height;
		this.width = width;
		this.depth = depth;
	}

	public int getAntechamberID() {
		return antechamberID;
	}

	public void setAntechamberID(int antechamberID) {
		this.antechamberID = antechamberID;
	}

	public int getFrontWallID() {
		return frontWallID;
	}

	public void setFrontWallID(int frontWallID) {
		this.frontWallID = frontWallID;
	}

	public int getLeftWallID() {
		return leftWallID;
	}

	public void setLeftWallID(int leftWallID) {
		this.leftWallID = leftWallID;
	}

	public int getRightWallID() {
		return rightWallID;
	}

	public void setRightWallID(int rightWallID) {
		this.rightWallID = rightWallID;
	}

	public int getRearWallID() {
		return rearWallID;
	}

	public void setRearWallID(int rearWallID) {
		this.rearWallID = rearWallID;
	}

	public double getHeight() {
		return height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		return width;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public double getDepth() {
		return depth;
	}

	public void setDepth(double depth) {
		this.depth = depth;
	}
	

	
}
