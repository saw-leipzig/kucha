package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class NicheEntry implements IsSerializable{
	private int caveID;
	private int height;
	private int width;
	private int depth;
	private boolean frontWall;
	private boolean leftSideWall;
	private boolean rightSideWall;
	private boolean mainWall;
	private boolean ceiling;
	private boolean floor;
	private boolean nichesInCave;
	public int getCaveID() {
		return caveID;
	}
	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
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
		return mainWall;
	}
	public void setMainWall(boolean mainWall) {
		this.mainWall = mainWall;
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
	public boolean isNichesInCave() {
		return nichesInCave;
	}
	public void setNichesInCave(boolean nichesInCave) {
		this.nichesInCave = nichesInCave;
	}

}
