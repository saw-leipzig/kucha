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

import java.util.ArrayList;

public class CaveEntry extends AbstractEntry {
	private int caveID;
	private String officialNumber; 
	private String historicName;
	private String optionalHistoricName;
	private int caveTypeID;
	private int districtID;
	private int regionID;
	private int orientationID;
	private int preservationClassificationID;
	private int caveGroupID;
	private String stateOfPerservation;
	private String findings;
	private String notes;
	private String firstDocumentedBy;
	private int firstDocumentedInYear;
	private String optionalCaveSketch;
	private String c14url;
	private String c14DocumentFilename;
	private ArrayList<CaveAreaEntry> caveAreaList;
	private ArrayList<WallEntry> wallList;

	public CaveEntry() {
		this(0, "", "", "", 0, 0, 0, 0, "", "", "", "", 0, 0, 0, null, null, null);
	}

	public CaveEntry(int caveID, String officialNumber, String historicName, String optionalHistoricName, int caveTypeID, int districtID,
			int regionID, int orientationID, String stateOfPerservation, String findings, String notes, String firstDocumentedBy, int firstDocumentedInYear, int preservationClassificationID,
			int caveGroupID, String optionalCaveSketch, String c14url, String c14DocumentFilename) {
		this.caveID = caveID;
		this.officialNumber = officialNumber;
		this.historicName = historicName;
		this.optionalHistoricName = optionalHistoricName;
		this.caveTypeID = caveTypeID;
		this.districtID = districtID;
		this.regionID = regionID;
		this.orientationID = orientationID;
		this.stateOfPerservation = stateOfPerservation;
		this.findings = findings;
		this.setNotes(notes);
		this.firstDocumentedBy = firstDocumentedBy;
		this.firstDocumentedInYear = firstDocumentedInYear;
		this.preservationClassificationID = preservationClassificationID;
		this.caveGroupID = caveGroupID;
		this.optionalCaveSketch = optionalCaveSketch;
		this.c14url = c14url;
		this.c14DocumentFilename = c14DocumentFilename;
		caveAreaList = new ArrayList<CaveAreaEntry>();
		wallList = new ArrayList<WallEntry>();
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
		modified = true;
	}

	public String getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(String officialNumber) {
		this.officialNumber = officialNumber;
		modified = true;
	}

	public String getHistoricName() {
		return historicName;
	}

	public void setHistoricName(String historicName) {
		this.historicName = historicName;
		modified = true;
	}

	public String getOptionalHistoricName() {
		return optionalHistoricName;
	}

	public void setOptionalHistoricName(String optionalHistoricName) {
		this.optionalHistoricName = optionalHistoricName;
		modified = true;
	}

	public int getCaveTypeID() {
		return caveTypeID;
	}

	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
		modified = true;
	}

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
		modified = true;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
		modified = true;
	}

	public String getStateOfPerservation() {
		return stateOfPerservation;
	}

	public void setStateOfPerservation(String stateOfPerservation) {
		this.stateOfPerservation = stateOfPerservation;
		modified = true;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
		modified = true;
	}

	/**
	 * @return the orientationID
	 */
	public int getOrientationID() {
		return orientationID;
	}

	/**
	 * @param orientationID
	 *          the orientationID to set
	 */
	public void setOrientationID(int orientationID) {
		this.orientationID = orientationID;
		modified = true;
	}

	/**
	 * @return the preservationClassificationID
	 */
	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}

	/**
	 * @param preservationClassificationID
	 *          the preservationClassificationID to set
	 */
	public void setPreservationClassificationID(int preservationClassificationID) {
		this.preservationClassificationID = preservationClassificationID;
		modified = true;
	}

	/**
	 * @return the caveGroupID
	 */
	public int getCaveGroupID() {
		return caveGroupID;
	}

	/**
	 * @param caveGroupID
	 *          the caveGroupID to set
	 */
	public void setCaveGroupID(int caveGroupID) {
		this.caveGroupID = caveGroupID;
		modified = true;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Cave-" + caveID;
	}

	public int getFirstDocumentedInYear() {
		return firstDocumentedInYear;
	}

	public void setFirstDocumentedInYear(int firstDocumentedInYear) {
		this.firstDocumentedInYear = firstDocumentedInYear;
		modified = true;
	}

	public String getFirstDocumentedBy() {
		return firstDocumentedBy;
	}

	public void setFirstDocumentedBy(String firstDocumentedBy) {
		this.firstDocumentedBy = firstDocumentedBy;
		modified = true;
	}

	public CaveAreaEntry getCaveArea(String label) {
		for (CaveAreaEntry ca : caveAreaList) {
			if (label == ca.getCaveAreaLabel()) {
				return ca;
			}
		}
		CaveAreaEntry newEntry = new CaveAreaEntry(caveID, label);
		caveAreaList.add(newEntry);
		return newEntry;
	}
	
	public void addCaveArea(CaveAreaEntry entry) {
		caveAreaList.remove(getCaveArea(entry.caveAreaLabel));
		caveAreaList.add(entry);
		modified = true;
	}

	public ArrayList<CaveAreaEntry> getCaveAreaList() {
		return caveAreaList;
	}

	public void setCaveAreaList(ArrayList<CaveAreaEntry> caveAreaList) {
		this.caveAreaList = caveAreaList;
		modified = true;
	}
	
	public WallEntry getWall(int wallLocationID) {
		for (WallEntry we : wallList) {
			if (wallLocationID == we.getWallLocationID()) {
				return we;
			}
		}
		WallEntry newEntry = new WallEntry(caveID, wallLocationID);
		wallList.add(newEntry);
		return newEntry;
	}
	
	public void addWall(WallEntry entry) {
		wallList.remove(getWall(entry.getWallLocationID()));
		wallList.add(entry);
		modified = true;
	}

	public ArrayList<WallEntry> getWallList() {
		return wallList;
	}

	public void setWallList(ArrayList<WallEntry> wallList) {
		this.wallList = wallList;
		modified = true;
	}

	/**
	 * @return the optionalCaveSketch
	 */
	public String getOptionalCaveSketch() {
		return optionalCaveSketch;
	}

	/**
	 * @param optionalCaveSketch the optionalCaveSketch to set
	 */
	public void setOptionalCaveSketch(String optionalCaveSketch) {
		this.optionalCaveSketch = optionalCaveSketch;
		modified = true;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
		modified = true;
	}

	/**
	 * @return the c14url
	 */
	public String getC14url() {
		return c14url;
	}

	/**
	 * @param c14url the c14url to set
	 */
	public void setC14url(String c14url) {
		this.c14url = c14url;
		modified = true;
	}

	/**
	 * @return the c14DocumentFileName
	 */
	public String getC14DocumentFilename() {
		return c14DocumentFilename;
	}

	/**
	 * @param c14DocumentFileName the c14DocumentFileName to set
	 */
	public void setC14DocumentFilename(String c14DocumentFilename) {
		this.c14DocumentFilename = c14DocumentFilename;
		modified = true;
	}
	
}
