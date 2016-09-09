package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CaveTypeEntry implements IsSerializable{
	private int caveTypeID;
	private String enShortname;
	private String enDescription;
	
	public CaveTypeEntry(){
		
	}
	public int getCaveTypeID() {
		return caveTypeID;
	}
	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}
	public String getEnShortname() {
		return enShortname;
	}
	public void setEnShortname(String enShortname) {
		this.enShortname = enShortname;
	}
	public String getEnDescription() {
		return enDescription;
	}
	public void setEnDescription(String enDescription) {
		this.enDescription = enDescription;
	}
	
	

}
