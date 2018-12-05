package de.cses.shared;

import java.util.ArrayList;

public class OrnamenticSearchEntry extends AbstractSearchEntry {
	
	private String searchOrnamentCode = "";
	private ArrayList<Integer> searchOrnamenClassIdList = new ArrayList<Integer>();

	public OrnamenticSearchEntry(boolean orSearch) {
		super(orSearch);
	}

	public OrnamenticSearchEntry() { }

	public String getSearchOrnamentCode() {
		return searchOrnamentCode;
	}

	public void setSearchOrnamentCode(String searchOrnamentCode) {
		this.searchOrnamentCode = searchOrnamentCode;
	}

	public ArrayList<Integer> getSearchOrnamenClassIdList() {
		return searchOrnamenClassIdList;
	}

	public void setSearchOrnamenClassIdList(ArrayList<Integer> searchOrnamenClassIdList) {
		this.searchOrnamenClassIdList = searchOrnamenClassIdList;
	}

}
