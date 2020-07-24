package de.cses.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.Cookies;

import de.cses.client.user.UserLogin;

public class CaveSearchEntry extends AbstractSearchEntry {

	private String historicalName = "";
	private boolean decoratedOnly = false;
	private ArrayList<Integer> caveTypeIdList = new ArrayList<Integer>();
	private ArrayList<Integer> siteIdList = new ArrayList<Integer>();
	private ArrayList<Integer> districtIdList = new ArrayList<Integer>();
	private ArrayList<Integer> regionIdList = new ArrayList<Integer>();
	private ArrayList<Integer> caveIdList = new ArrayList<Integer>(); // we need this to search in certain caves only
	private ArrayList<Integer> iconographyIDList = new ArrayList<Integer>(); // we need this to search for the caves conected to a certain OrnamentEntry
	private ArrayList<String> officialNumberIDList = new ArrayList<String>(); 
	

	public CaveSearchEntry(boolean orSearch, String sessionID) {
		super(orSearch, sessionID);
	}

	public CaveSearchEntry(String sessionID) {
		super(sessionID);
	}

	public CaveSearchEntry() { }

	public ArrayList<Integer> getCaveTypeIdList() {
		return caveTypeIdList;
	}

	public void setCaveTypeIdList(ArrayList<Integer> caveTypeIdList) {
		this.caveTypeIdList = caveTypeIdList;
	}

	public ArrayList<Integer> getSiteIdList() {
		return siteIdList;
	}

	public void setSiteIdList(ArrayList<Integer> siteIdList) {
		this.siteIdList = siteIdList;
	}

	public ArrayList<Integer> getDistrictIdList() {
		return districtIdList;
	}

	public void setDistrictIdList(ArrayList<Integer> districtIdList) {
		this.districtIdList = districtIdList;
	}

	public ArrayList<Integer> getRegionIdList() {
		return regionIdList;
	}

	public void setRegionIdList(ArrayList<Integer> regionIdList) {
		this.regionIdList = regionIdList;
	}

	public String getHistoricalName() {
		return historicalName;
	}

	public void setHistoricalName(String historicalName) {
		this.historicalName = historicalName;
	}

	public boolean isDecoratedOnly() {
		return decoratedOnly;
	}

	public void setDecoratedOnly(boolean decoratedOnly) {
		this.decoratedOnly = decoratedOnly;
	}

	public ArrayList<Integer> getCaveIdList() {
		return caveIdList;
	}

	public void setCaveIdList(ArrayList<Integer> caveIdList) {
		this.caveIdList = caveIdList;
	}
	public ArrayList<Integer> geticonographyIDList() {
		return iconographyIDList;
	}

	public void setOrnamentIDList(ArrayList<Integer> iconographyID) {
		this.iconographyIDList = iconographyID;
	}
	public ArrayList<String> getOfficialNumberList() {
		return officialNumberIDList;
	}
	public void setOfficialNumberList(ArrayList<String> officialNumberIDList) {
		this.officialNumberIDList = officialNumberIDList;
	}



}
