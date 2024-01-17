package de.cses.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PositionEntry extends AbstractEntry {

	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int RHOMBUS = 0;
	public static final int SQHARE = 1;
	public static final int EXACT = 0;
	public static final int VAGUE = 1;
	public static final List<String> DIRECTION_LABEL = Arrays.asList("left", "right");
	public static final List<String> TYPE_LABEL = Arrays.asList("rhombus", "square");
	
	private int positionID;
	private String name;
	
	public PositionEntry(int positionID, String name) {
		this.positionID = positionID;
		this.name = name;
	}
	public PositionEntry() {
		this(-1, "");
	}

	public int getPositionID() {
		return positionID;
	}

	public void setpositionID(int ornamentPositionID) {
		this.positionID = ornamentPositionID;
	}

	public String getName() {
		return name;
	}
	
	public String getNameWithPosition() {
			return name;		
	}

	public void setName(String name) {
		this.name = name;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamentPositionEntry" + positionID;
	}
    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof PositionEntry){
        	if (this.getPositionID()==((PositionEntry)anObject).getPositionID()) {
        		isEqual=true;
        	}
    	}
        return isEqual;
    }

}