package de.cses.shared;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;

public class WallDimensionEntry extends AbstractEntry {

	
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	public static final int RHOMBUS = 0;
	public static final int SQHARE = 1;
	public static final int EXACT = 0;
	public static final int VAGUE = 1;
	public static final List<String> DIRECTION_LABEL = Arrays.asList("left", "right");
	public static final List<String> TYPE_LABEL = Arrays.asList("rhombus", "square");
	
	private int wallDimensionID;
	private String name;
	private int registers = 0;
	private int columns = 0;
	private int type;
	private int direction;
	private float x,y,w,h = 0;
	private WallSketchEntry wse;
	private String wallPosition;
	private ArrayList<CoordinateEntry> coordinates = new ArrayList<CoordinateEntry>();
	private ArrayList<EmptySpotEntry> emptySpots = new ArrayList<EmptySpotEntry>();
	public String log = "";
	
	public WallDimensionEntry(int wallDimensionID, String name) {
		this.wallDimensionID = wallDimensionID;
		this.name = name;
	}
	public WallDimensionEntry(int wallDimensionID, String name, WallSketchEntry wse, String wallPosition, Integer type, Integer direction, Integer registers, Integer columns, Integer x, Integer y, Integer w, Integer h, ArrayList<CoordinateEntry> coordinates, ArrayList<EmptySpotEntry> emptySpots) {
		this.wallDimensionID = wallDimensionID;
		this.name = name;
		this.type = type;
		this.direction = direction;
		this.registers = registers;
		this.columns = columns;
		this.wse = wse;
		this.wallPosition = wallPosition;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.coordinates = coordinates;
		this.emptySpots = emptySpots;
	}
	public int getDirection() {
		return this.direction;
	}
	public void setDirection(int direction) {
		this.direction = direction;
	}
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getW() {
		return w;
	}

	public void setW(float w) {
		this.w = w;
	}

	public float getH() {
		return h;
	}

	public void setH(float h) {
		this.h = h;
	}


	public ArrayList<CoordinateEntry> getCoordinates() {
		return this.coordinates;
	}
	public void setCoordinates(ArrayList<CoordinateEntry> coordinates) {
		this.coordinates = coordinates;
	}
	public ArrayList<EmptySpotEntry> getEmptySpots() {
		return this.emptySpots;
	}
	public void setEmptySports(ArrayList<EmptySpotEntry> emptySpots) {
		this.emptySpots = emptySpots;
	}
	public void addPosition(CoordinateEntry newCoordinate) {
		this.coordinates.add(newCoordinate);
	}
	public void addEmptySpot(EmptySpotEntry emptySpot) {
		this.emptySpots.add(emptySpot);
	}
	public void removePostion(CoordinateEntry removedCoordinate) {
		ArrayList<CoordinateEntry> updatedCoordinates = new ArrayList<CoordinateEntry>();
		for (CoordinateEntry pe: this.coordinates) {
			if ((removedCoordinate.getExact() == pe.getExact()) && (removedCoordinate.getNumber() == pe.getNumber()) && (removedCoordinate.getRegister() == pe.getRegister()) && (removedCoordinate.getPositionID() == pe.getPositionID())) {
				;
			} else {
				updatedCoordinates.add(pe);
			}
		}
		this.coordinates = updatedCoordinates;
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
	public boolean hasCorrespondingCoordinateLostEntry(int reg, int number) {
		Boolean res = false;
		for (CoordinateEntry pe: this.coordinates) {
			if ((pe.getRegister() == reg) && (pe.getNumber() == number) && (pe.getDepictionID() == -1)) {
				res = true;
				break;
			}
		}
		return res;
	}
	
	public boolean hasHinderingCoordinateEntryForEmptySpot(int reg, int number) {
	    this.log = "";
		Boolean res = false;
		int emptySpots = 0;
		for (EmptySpotEntry ese: this.emptySpots) {
			if (ese.getY() == reg && !ese.isdeleted()) {
				emptySpots += 1;
			}
		}
		GWT.debugger();
		int minusOneForEvenRowRombus = 0;
		if (type == 0 && (reg % 2) == 0) { 
			minusOneForEvenRowRombus = 1;
		}
		for (CoordinateEntry pe: this.coordinates) {
			log = log + "\n" + "hindering: pe.reg = " + Integer.toString(pe.getRegister()) + " - reg: " + Integer.toString(reg) + " / pe.getNumber " + Integer.toString(pe.getNumber()) + " >= " + Integer.toString(registers) + " - " + Integer.toString(emptySpots) + " - " + Integer.toString(minusOneForEvenRowRombus) + " / " + Boolean.toString(!pe.isdeleted());
			if ((pe.getRegister() == reg) && (pe.getNumber() >= this.columns-emptySpots-minusOneForEvenRowRombus) && (!pe.isdeleted())) {
				res = true;
				break;
			}
		}
		return res;
	}
	
	
	public boolean hasCorrespondingCoordinateEntry(int reg, int number) {
		Boolean res = false;
		for (CoordinateEntry pe: this.coordinates) {
			if ((pe.getRegister() == reg) && (pe.getNumber() == number) && (pe.getDepictionID() > 0)) {
				res = true;
				break;
			}
		}
		return res;
	}
	public void setColumns(int columns) {
		this.columns = columns;
	}
	public WallDimensionEntry() {
		this(-1, "");
	}

	public int getWallDimensionID() {
		return wallDimensionID;
	}

	public void setWallDimensionID(int wallDimensionID) {
		this.wallDimensionID = wallDimensionID;
	}

	public String getName() {
		return name;
	}
	

	public void setName(String name) {
		this.name = name;
	}

	public WallSketchEntry getWallSketch() {
		return wse;
	}
	

	public void setWallSketch(WallSketchEntry wse) {
		this.wse = wse;
	}

	public String getWallPosition() {
		return wallPosition;
	}
	

	public void setwallPosition(String wallPosition) {
		this.wallPosition = wallPosition;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamentPositionEntry" + wallDimensionID;
	}
    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof WallDimensionEntry){
        	if (this.getWallDimensionID()==((WallDimensionEntry)anObject).getWallDimensionID()) {
        		isEqual=true;
        	}
    	}
        return isEqual;
    }

}
