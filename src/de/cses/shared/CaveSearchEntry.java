package de.cses.shared;

import java.util.ArrayList;

public class CaveSearchEntry extends AbstractSearchEntry {

	private String historicalName = "";
	private boolean decoratedOnly = false;
	private ArrayList<Integer> caveTypeIdList = new ArrayList<Integer>();
	private ArrayList<Integer> siteIdList = new ArrayList<Integer>();
	private ArrayList<Integer> districtIdList = new ArrayList<Integer>();
	private ArrayList<Integer> regionIdList = new ArrayList<Integer>();

	public CaveSearchEntry(boolean orSearch) {
		super(orSearch);
		// TODO Auto-generated constructor stub
	}

	public CaveSearchEntry() {
		// TODO Auto-generated constructor stub
	}

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

}
