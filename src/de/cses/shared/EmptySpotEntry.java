package de.cses.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmptySpotEntry extends AbstractEntry {
	
	private int emptySpotID;
	private String name;
	private int registerPositionX = -1;
	private int registerPositionY = -1;
	
	public EmptySpotEntry(int emptySpotID, int y, int x, String name, boolean deleted) {
		this.emptySpotID = emptySpotID;
		this.registerPositionY = y;
		this.registerPositionX = x;
		this.name = name;
		this.deleted = deleted;
	}
	public int getEmptySpotID() {
		return this.emptySpotID;
	}
	public void setEmptySpotID(int emptySpotID) {
		this.emptySpotID = emptySpotID;
	}
	public int getY() {
		return this.registerPositionY;
	}
	public void setY(int y) {
		this.registerPositionY = y;
	}
	public int getX() {
		return this.registerPositionX;
	}
	public void setX(int x) {
		this.registerPositionX = x;
	}
	public EmptySpotEntry() {
		this(-1, -1, -1, "", false);
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getUniqueID() {
		return "EmptySpotEntry-" + emptySpotID;
	}
    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof EmptySpotEntry){
        	if (this.getEmptySpotID()==((EmptySpotEntry)anObject).getEmptySpotID()) {
        		isEqual=true;
        	}
    	}
        return isEqual;
    }

}
