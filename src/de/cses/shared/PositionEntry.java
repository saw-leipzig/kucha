package de.cses.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PositionEntry extends AbstractEntry {

	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int RHOMBUS = 0;
	public static final int SQHARE = 1;
	public static final List<String> DIRECTION_LABEL = Arrays.asList("left", "right");
	public static final List<String> TYPE_LABEL = Arrays.asList("rhombus", "square");
	
	private int positionID;
	private String name;
	private int registers = -1;
	private int columns = -1;
	private int type;
	private int direction;
	private ArrayList<CoordinatesEntry> coordinates;
	public PositionEntry(int positionID, String name) {
		this.positionID = positionID;
		this.name = name;
	}
	public PositionEntry(int positionID, String name, Integer type, Integer direction, Integer registers, Integer columns, ArrayList<CoordinatesEntry> coordinates) {
		this.positionID = positionID;
		this.name = name;
		this.type = type;
		this.direction = direction;
		this.registers = registers;
		this.columns = columns;
		this.coordinates = coordinates;
	}
	public int getDirection() {
		return this.direction;
	}
	public void setdirection(int direction) {
		this.direction = direction;
	}
	public int getType() {
		return this.type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getRegisters() {
		return this.registers;
	}
	public void setRegisters(int register) {
		this.registers = register;
	}
	public int getColumns() {
		return this.columns;
	}
	public void setColumns(int columns) {
		this.columns = columns;
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

	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<CoordinatesEntry> getCoodinates() {
		return coordinates;
	}

	public void setCoodinates(ArrayList<CoordinatesEntry> coordinates) {
		this.coordinates = coordinates;
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
