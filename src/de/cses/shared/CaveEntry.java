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

import com.google.gwt.user.client.rpc.IsSerializable;

public class CaveEntry implements IsSerializable {
	private int caveID;
	private String officialNumber, officialName, historicName;
	private int caveTypeID, districtID, regionID, orientationID; 
	private String stateOfPerservation;
	private String pedestals;
	private String findings;
	private String alterationDate;
	private AntechamberEntry antechamberEntry = null;
	private MainChamberEntry mainChamberEntry = null;
	private RearAreaEntry rearAreaEntry = null;	

	public CaveEntry() {
	}

	public CaveEntry(int caveID, String officialNumber, String officialName, String historicalName, int caveTypeID, int districtID,
			int regionID, int orientationID, String stateOfPerservation, String pedestals, String findings, String alterationDate) {
		super();
		this.caveID = caveID;
		this.officialNumber = officialNumber;
		this.officialName = officialName;
		this.historicName = historicalName;
		this.caveTypeID = caveTypeID;
		this.districtID = districtID;
		this.setRegionID(regionID);
		this.setOrientationID(orientationID);
		this.stateOfPerservation = stateOfPerservation;
		this.pedestals = pedestals;
		this.findings = findings;
		this.alterationDate = alterationDate;
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

	public String getOfficialName() {
		return officialName;
	}

	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}

	public String getHistoricName() {
		return historicName;
	}

	public void setHistoricName(String historicalName) {
		this.historicName = historicalName;
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

	public String getPedestals() {
		return pedestals;
	}

	public void setPedestals(String pedestals) {
		this.pedestals = pedestals;
	}

	public String getFindings() {
		return findings;
	}

	public void setFindings(String findings) {
		this.findings = findings;
	}

	public String getAlterationDate() {
		return alterationDate;
	}

	public void setAlterationDate(String alterationDate) {
		this.alterationDate = alterationDate;
	}

	/**
	 * @return the antechamberEntry
	 */
	public AntechamberEntry getAntechamberEntry() {
		return antechamberEntry;
	}

	/**
	 * @param antechamberEntry the antechamberEntry to set
	 */
	public void setAntechamberEntry(AntechamberEntry antechamberEntry) {
		this.antechamberEntry = antechamberEntry;
	}

	/**
	 * @return the mainChamberEntry
	 */
	public MainChamberEntry getMainChamberEntry() {
		return mainChamberEntry;
	}

	/**
	 * @param mainChamberEntry the mainChamberEntry to set
	 */
	public void setMainChamberEntry(MainChamberEntry mainChamberEntry) {
		this.mainChamberEntry = mainChamberEntry;
	}

	/**
	 * @return the rearAreaEntry
	 */
	public RearAreaEntry getRearAreaEntry() {
		return rearAreaEntry;
	}

	/**
	 * @param rearAreaEntry the rearAreaEntry to set
	 */
	public void setRearAreaEntry(RearAreaEntry rearAreaEntry) {
		this.rearAreaEntry = rearAreaEntry;
	}

	/*
	 * ATTENTION: Orientation is currently not inlcuded!!
	 */
	public String getInsertSql() {		
		return "INSERT INTO Caves (OfficialNumber,OfficialName,HistoricName,CaveTypeID,DistrictID,RegionID, OrientationID,StateOfPreservation,Pedestals,Findings,AlterationDate) VALUES "
				+ "('" + officialNumber + "','" + officialName + "','" + historicName + "'," + caveTypeID + "," + districtID + "," + regionID + "," + orientationID + ",'" + stateOfPerservation
				+ "','" + pedestals + "','" + findings + "','" + alterationDate + "')";
	}

	public String getUpdateSql() {
		return "UPDATE Caves SET OfficialNumber='" + officialNumber + "', OfficialName='" + officialName + "', HistoricName='" + historicName
				+ "', CaveTypeID=" + caveTypeID + ", DistrictID=" + districtID + ", RegionID=" + regionID + ", OrientationID=" + orientationID + ", StateOfPreservation='" + stateOfPerservation
				+ "', Pedestals='" + pedestals + "', Findings='" + findings + "', AlterationDate='" + alterationDate + "' "
				+ "WHERE CaveID=" + caveID;
	}

	/**
	 * @return the orientationID
	 */
	public int getOrientationID() {
		return orientationID;
	}

	/**
	 * @param orientationID the orientationID to set
	 */
	public void setOrientationID(int orientationID) {
		this.orientationID = orientationID;
	}


}
