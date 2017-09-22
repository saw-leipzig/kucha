/*
 * Copyright 2017 
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

import java.util.ArrayList;

/**
 * @author alingnau
 *
 */
public class CaveAreaEntry extends AbstractEntry {
	
	public static class Ceiling {
		public static String ANTECHAMBER = "antechamber";
		public static String MAIN_CHAMBER = "main chamber";
		public static String MAIN_CHAMBER_CORRIDOR = "main chamber corridor";
		public static String REAR_AREA_LEFT_CORRIDOR = "rear area left corridor";
		public static String REAR_AREA_RIGHT_CORRIDOR = "rear area right corridor";
		public static String REAR_CHAMBER = "rear chamber";
		public static String REAR_CORRIDOR = "rear corridor";
		
		protected int ceilingTypeID;
		protected int preservationClassificationID;
		
		/**
		 * @param ceilingTypeID
		 * @param preservationClassificationID
		 */
		public Ceiling(int ceilingTypeID, int preservationClassificationID) {
			this.ceilingTypeID = ceilingTypeID;
			this.preservationClassificationID = preservationClassificationID;
		}

		public int getCeilingTypeID() {
			return ceilingTypeID;
		}

		public int getPreservationClassificationID() {
			return preservationClassificationID;
		}
		
	}

	protected int caveAreaID;
	protected int caveID;
	protected String caveAreaLabel;
	protected double height, width, depth;
	protected int preservationClassificationID;
	protected ArrayList<Ceiling> ceilings;

	/**
	 * 
	 */
	public CaveAreaEntry() {
		this(0, 0, "", 0, 0, 0, 0);
	}

	/**
	 * @param caveID
	 * @param caveAreaLabel
	 * @param height
	 * @param width
	 * @param depth
	 * @param preservationClassificationID
	 */
	public CaveAreaEntry(int caveAreaID, int caveID, String caveAreaLabel, double height, double width, double depth, int preservationClassificationID) {
		this.caveAreaID = caveAreaID;
		this.caveID = caveID;
		this.caveAreaLabel = caveAreaLabel;
		this.height = height;
		this.width = width;
		this.depth = depth;
		this.preservationClassificationID = preservationClassificationID;
		ceilings = new ArrayList<Ceiling>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return "CaveArea-" + caveID + caveAreaLabel;
	}

	public int getCaveAreaID() {
		return caveAreaID;
	}

	public void setCaveAreaID(int caveAreaID) {
		this.caveAreaID = caveAreaID;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public String getCaveAreaLabel() {
		return caveAreaLabel;
	}

	/**
	 * We test if the label is valid for the database
	 * @param caveAreaLabel
	 * @return true if the label is valid, otherwise false
	 */
	public void setCaveAreaLabel(String caveAreaLabel) {
		this.caveAreaLabel = caveAreaLabel;
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

	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}

	public void setPreservationClassificationID(int preservationClassificationID) {
		this.preservationClassificationID = preservationClassificationID;
	}

	/**
	 * @return the ceilings
	 */
	public ArrayList<Ceiling> getCeilings() {
		return ceilings;
	}

	/**
	 * @param ceilings the ceilings to set
	 */
	public void setCeilings(ArrayList<Ceiling> ceilings) {
		this.ceilings = ceilings;
	}
	
	public void addCeiling(Ceiling ceiling) {
		ceilings.add(ceiling);
	}
	
	

}
