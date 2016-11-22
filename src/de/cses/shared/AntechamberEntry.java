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

	private int caveID;
	private int dimensionHeight;
	private int dimensionWidth;
	private int dimensionDepth;
	private boolean frontWall;
	private boolean leftSideWall;
	private boolean rightSideWall;
	private boolean MainWall;
	private boolean niches;
	private boolean ceiling;
	private boolean floor;
	
	public AntechamberEntry(){
		
	}
	
	public int getcaveID() {
		return caveID;
	}
	public void setcaveID(int caveID) {
		this.caveID = caveID;
	}
	public int getDimensionHeight() {
		return dimensionHeight;
	}
	public void setDimensionHeight(int dimensionHeight) {
		this.dimensionHeight = dimensionHeight;
	}
	public int getDimensionWidth() {
		return dimensionWidth;
	}
	public void setDimensionWidth(int dimensionWidth) {
		this.dimensionWidth = dimensionWidth;
	}
	public int getDimensionDepth() {
		return dimensionDepth;
	}
	public void setDimensionDepth(int dimensionDepth) {
		this.dimensionDepth = dimensionDepth;
	}
	public boolean isFrontWall() {
		return frontWall;
	}
	public void setFrontWall(boolean frontWall) {
		this.frontWall = frontWall;
	}
	public boolean isLeftSideWall() {
		return leftSideWall;
	}
	public void setLeftSideWall(boolean leftSideWall) {
		this.leftSideWall = leftSideWall;
	}
	public boolean isRightSideWall() {
		return rightSideWall;
	}
	public void setRightSideWall(boolean rightSideWall) {
		this.rightSideWall = rightSideWall;
	}
	public boolean isMainWall() {
		return MainWall;
	}
	public void setMainWall(boolean mainWall) {
		MainWall = mainWall;
	}
	public boolean isNiches() {
		return niches;
	}
	public void setNiches(boolean niches) {
		this.niches = niches;
	}
	public boolean isCeiling() {
		return ceiling;
	}
	public void setCeiling(boolean ceiling) {
		this.ceiling = ceiling;
	}
	public boolean isFloor() {
		return floor;
	}
	public void setFloor(boolean floor) {
		this.floor = floor;
	}
	
}
