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

/**
 * @author alingnau
 *
 */
public class RegionEntry extends AbstractEntry {
	
	private int regionID;
	private String phoneticName, originalName, englishName;
	private int siteID;

	/**
	 * 
	 */
	public RegionEntry() {
		this(0,"","","",0);
	}

	public RegionEntry(int regionID, String phoneticName, String originalName, String englishName, int siteID) {
		super();
		this.regionID = regionID;
		this.phoneticName = phoneticName;
		this.originalName = originalName;
		this.englishName = englishName;
		this.siteID = siteID;
	}

	public int getRegionID() {
		return regionID;
	}

	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}

	public String getPhoneticName() {
		return phoneticName;
	}

	public void setPhoneticName(String phoneticName) {
		this.phoneticName = phoneticName;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public int getSiteID() {
		return siteID;
	}

	public void setSiteID(int siteID) {
		this.siteID = siteID;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		return "INSERT INTO Regions (PhoneticName, OriginalName, EnglishName, SiteID) VALUES ('" + phoneticName + "', '" + originalName + "', '" + englishName + "', " + siteID + ")";
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUpdateSql()
	 */
	@Override
	public String getUpdateSql() {
		return "UPDATE Regions SET PhoneticName='" + phoneticName + "', OriginalName='" + originalName + "', EnglishName='" + englishName + "', SiteID=" + siteID + "WHERE RegionID=" + regionID;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Region-" + regionID;
	}

}
