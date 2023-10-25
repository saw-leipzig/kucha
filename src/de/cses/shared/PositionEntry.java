package de.cses.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PositionEntry extends AbstractEntry {
	
	private int positionID;
	private String name;
	private int register = -1;
	private int number = -1;
	private boolean exact;
	private int depictionID;
	
	public PositionEntry(int positionID, String name) {
		this.positionID = positionID;
		this.name = name;
	}
	public PositionEntry(int positionID, int depictionID, String name, Integer register, Integer number, boolean exact) {
		this.positionID = positionID;
		this.depictionID = depictionID;
		this.name = name;
		this.register = register;
		this.number = number;
	}
	public void setExact(boolean exact) {
		this.exact = exact;
	}
	public boolean getExact() {
		return this.exact;
	}
	public int getDepictionID() {
		return this.depictionID;
	}
	public void setDepictionID(int depictionID) {
		this.depictionID = depictionID;
	}
	public int getRegister() {
		return this.register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getNumber() {
		return this.number;
	}
	public void setNumber(int number) {
		this.number = number;
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
	public String getPosition() {
		String position = "Reg. " + Integer.toString(getRegister()) + ", No. " + Integer.toString(getNumber());
		return position;
	}
//	public String getNameWithPosition() {
//		String position = " (";
//		if (coordinates != null) {
//			Boolean first = true;
//			for (CoordinatesEntry ce: coordinates) {
//				if (first) {
//					position = position + "Reg. " + Integer.toString(ce.getRegister()) + ", No. " + Integer.toString(ce.getNumber());
//					first = false;
//				} else {
//					position = position + "; Reg. " + Integer.toString(ce.getRegister()) + ", No. " + Integer.toString(ce.getNumber());
//				}
//			}
//			position = position + ")";
//			return name+position;
//		} else {
//			return name;
//		}
//		
//	}

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
