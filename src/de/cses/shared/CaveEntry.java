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


	private int districtID;
	private String officialName;
	private String officialNumber;
	private String historicalName;
	private int caveTypeID;
	private String stateOfPerservation;
	private String orientationAccordingToSkyDirection;
	private String pedestals;
	private String findings;
	private String alterationDate;
	
	

	public CaveEntry() {
	}

	public CaveEntry(int caveID,int districtID, String officialName, String officialNumber, String historicalName, int caveTypeID, String stateOfPerservation, String orientationAccordingToTheSkyDirection, String pedestals, String findings) {
		super();
		this.districtID= districtID;
		this.caveID = caveID;
		this.officialName = officialName;
		this.officialNumber= officialNumber;
		this.historicalName = historicalName;
		this.caveTypeID = caveTypeID;
		this.stateOfPerservation = stateOfPerservation;
		this.orientationAccordingToSkyDirection = orientationAccordingToTheSkyDirection;
		this.pedestals = pedestals;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public String getName() {
		return officialName;
	}

	public void setName(String name) {
		this.officialName = name;
	}

	public String getOfficialName() {
		return officialName;
	}

	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}

	public String getOfficialNumber() {
		return officialNumber;
	}

	public void setOfficialNumber(String officialNumber) {

		this.officialNumber = officialNumber;
	}

	public String getHistoricalName() {
		return historicalName;
	}

	public void setHistoricalName(String historicalName) {
		this.historicalName = historicalName;
	}

	public int getCaveType() {
		return caveTypeID;
	}

	public void setCaveType(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}

	public String getStateOfPerservation() {
		return stateOfPerservation;
	}

	public void setStateOfPerservation(String stateOfPerservation) {
		this.stateOfPerservation = stateOfPerservation;
	}

	public String getOrientationAccordingToSkyDirection() {
		return orientationAccordingToSkyDirection;
	}

	public void setOrientationAccordingToSkyDirection(String orientationAccordingToSkyDirection) {
		this.orientationAccordingToSkyDirection = orientationAccordingToSkyDirection;
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

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}
	
	
	


}
