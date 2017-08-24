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

public class DistrictEntry extends AbstractEntry {
	private int districtID;
	private String name;
	private int siteID;
	private String description, map, arialMap;

	public DistrictEntry() {
		this(0,"",0,"","","");
	}

	public DistrictEntry(int districtID, String name, int siteID, String description, String map, String arialMap) {
		super();
		this.districtID = districtID;
		this.name = name;
		this.siteID = siteID;
		this.description = description;
		this.map = map;
		this.arialMap = arialMap;
	}

	public int getDistrictID() {
		return districtID;
	}

	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	public String getArialMap() {
		return arialMap;
	}

	public void setArialMap(String arialMap) {
		this.arialMap = arialMap;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		return "INSERT INTO Districts (Name, SiteID, Description, Map, ArialMap) VALUES ('" + name + "', " + siteID + ", '" + description + "', '" + map + "', '" + arialMap + "')";
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUpdateSql()
	 */
	@Override
	public String getUpdateSql() {
		return "UPDATE Districts SET Name='" + name + "', SiteID=" + siteID + ", Description='" + description + "', Map='" + map + "', ArialMap='" + arialMap + "' WHERE DistrictID=" + districtID;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "District-" + districtID;
	}

}