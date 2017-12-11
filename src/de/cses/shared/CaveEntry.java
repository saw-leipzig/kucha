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
	private String c14DocumentFilename;
	private String caveLayoutComments;
	private ArrayList<CaveAreaEntry> caveAreaList;
	private ArrayList<WallEntry> wallList;
	private ArrayList<C14AnalysisUrlEntry> c14AnalysisUrlList;

	public CaveEntry() {
		this(0, "", "", "", 0, 0, 0, 0, "", "", "", "", 0, 0, 0, null, null, null);
	}

	public CaveEntry(int caveID, String officialNumber, String historicName, String optionalHistoricName, int caveTypeID, int districtID,
			int regionID, int orientationID, String stateOfPerservation, String findings, String notes, String firstDocumentedBy, int firstDocumentedInYear, int preservationClassificationID,
			int caveGroupID, String optionalCaveSketch, String c14DocumentFilename, String caveLayoutComments) {
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
		this.c14DocumentFilename = c14DocumentFilename;
		this.setCaveLayoutComments(caveLayoutComments);
		caveAreaList = new ArrayList<CaveAreaEntry>();
		wallList = new ArrayList<WallEntry>();
		c14AnalysisUrlList = new ArrayList<C14AnalysisUrlEntry>();
	}
	
	public CaveEntry clone() {
		CaveEntry clonedCE = new CaveEntry(caveID, officialNumber, historicName, optionalHistoricName, caveTypeID, districtID,
				regionID, orientationID, stateOfPerservation, findings, notes, firstDocumentedBy, firstDocumentedInYear, preservationClassificationID,
				caveGroupID, optionalCaveSketch, c14DocumentFilename, caveLayoutComments);
		ArrayList<CaveAreaEntry> clonedCaveAreaList = new ArrayList<CaveAreaEntry>();
		for (CaveAreaEntry cae : caveAreaList) {
			clonedCaveAreaList.add(cae.clone());
		}
		clonedCE.setCaveAreaList(clonedCaveAreaList);
		ArrayList<WallEntry> clonedWallList = new ArrayList<WallEntry>();
		for (WallEntry wa : wallList) {
			clonedWallList.add(wa.clone());
		}
		clonedCE.setWallList(clonedWallList);
		ArrayList<C14AnalysisUrlEntry> clonedC14AnalysisUrlList = new ArrayList<C14AnalysisUrlEntry>();
		for (C14AnalysisUrlEntry c14aue : c14AnalysisUrlList) {
			clonedC14AnalysisUrlList.add(c14aue);
		}
		clonedCE.setC14AnalysisUrlList(clonedC14AnalysisUrlList);
		return clonedCE;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public String getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(String officialNumber) {
		this.officialNumber = officialNumber;
	}

	public String getHistoricName() {
		return historicName;
	}

	public void setHistoricName(String historicName) {
		this.historicName = historicName;
	}

	public String getOptionalHistoricName() {
		return optionalHistoricName;
	}

	public void setOptionalHistoricName(String optionalHistoricName) {
		this.optionalHistoricName = optionalHistoricName;
	}

	public int getCaveTypeID() {
		return caveTypeID;
	}

	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public String getStateOfPerservation() {
		return stateOfPerservation;
	}

	public void setStateOfPerservation(String stateOfPerservation) {
		this.stateOfPerservation = stateOfPerservation;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
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
	}

	public String getFirstDocumentedBy() {
		return firstDocumentedBy;
	}

	public void setFirstDocumentedBy(String firstDocumentedBy) {
		this.firstDocumentedBy = firstDocumentedBy;
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
	}

	public ArrayList<CaveAreaEntry> getCaveAreaList() {
		return caveAreaList;
	}

	public void setCaveAreaList(ArrayList<CaveAreaEntry> caveAreaList) {
		this.caveAreaList = caveAreaList;
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
	}

	public ArrayList<WallEntry> getWallList() {
		return wallList;
	}

	public void setWallList(ArrayList<WallEntry> wallList) {
		this.wallList = wallList;
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
	}

	/**
	 * @return the caveLayoutComments
	 */
	public String getCaveLayoutComments() {
		return caveLayoutComments;
	}

	/**
	 * @param caveLayoutComments the caveLayoutComments to set
	 */
	public void setCaveLayoutComments(String caveLayoutComments) {
		this.caveLayoutComments = caveLayoutComments;
	}

	public ArrayList<C14AnalysisUrlEntry> getC14AnalysisUrlList() {
		return c14AnalysisUrlList;
	}

	public void setC14AnalysisUrlList(ArrayList<C14AnalysisUrlEntry> c14AnalysisUrlList) {
		this.c14AnalysisUrlList = c14AnalysisUrlList;
	}
	
}
