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
	private String firstDocumentedBy;
	private int firstDocumentedInYear;
	private AntechamberEntry antechamberEntry;
	private MainChamberEntry mainChamberEntry;
	private RearAreaEntry rearAreaEntry;

	public CaveEntry() {
		this(0, "", "", "", 0, 0, 0, 0, "", "", "", 0, 0, 0);
		antechamberEntry = new AntechamberEntry();
		mainChamberEntry = new MainChamberEntry();
		rearAreaEntry = new RearAreaEntry();
	}

	public CaveEntry(int caveID, String officialNumber, String historicName, String optionalHistoricName, int caveTypeID, int districtID,
			int regionID, int orientationID, String stateOfPerservation, String findings, String firstDocumentedBy, int firstDocumentedInYear, int preservationClassificationID,
			int caveGroupID) {
		super();
		setCaveID(caveID);
		setOfficialNumber(officialNumber);
		setHistoricName(historicName);
		setOptionalHistoricName(optionalHistoricName);
		setCaveTypeID(caveTypeID);
		setDistrictID(districtID);
		setRegionID(regionID);
		setOrientationID(orientationID);
		setStateOfPerservation(stateOfPerservation);
		setFindings(findings);
		setFirstDocumentedBy(firstDocumentedBy);
		setFirstDocumentedInYear(firstDocumentedInYear);
		setPreservationClassificationID(preservationClassificationID);
		setCaveGroupID(caveGroupID);
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
		antechamberEntry.setAntechamberID(caveID);
		mainChamberEntry.setMainChamberID(caveID);
		rearAreaEntry.setRearAreaID(caveID);
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

	public AntechamberEntry getAntechamberEntry() {
		return antechamberEntry;
	}

	public void setAntechamberEntry(AntechamberEntry antechamberEntry) {
		this.antechamberEntry = antechamberEntry;
	}

	public MainChamberEntry getMainChamberEntry() {
		return mainChamberEntry;
	}

	public void setMainChamberEntry(MainChamberEntry mainChamberEntry) {
		this.mainChamberEntry = mainChamberEntry;
	}

	public RearAreaEntry getRearAreaEntry() {
		return rearAreaEntry;
	}

	public void setRearAreaEntry(RearAreaEntry rearAreaEntry) {
		this.rearAreaEntry = rearAreaEntry;
	}

//	public String getUpdateSql() {
//		return "UPDATE Caves SET OfficialNumber='" + officialNumber + "', HistoricName='" + historicName + "', OptionalHistoricName='"
//				+ optionalHistoricName + "', CaveTypeID=" + caveTypeID + ", DistrictID=" + districtID + ", RegionID=" + regionID
//				+ ", OrientationID=" + orientationID + ", StateOfPreservation='" + stateOfPerservation + "', Findings='" + findings
//				+ "', AlterationDate='" + firstDocumentedBy + "', PreservationClassificationID=" + preservationClassificationID + ", CaveGroupID="
//				+ caveGroupID + " WHERE CaveID=" + caveID;
//	}
//
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

}
