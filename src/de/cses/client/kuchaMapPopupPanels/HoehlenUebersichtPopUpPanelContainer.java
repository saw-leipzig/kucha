package de.cses.client.kuchaMapPopupPanels;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class HoehlenUebersichtPopUpPanelContainer implements IsSerializable{
	String officialName="";
	ArrayList<String> bilderURLs = new ArrayList<String>();
	int hoehlenID ;
	String caveTypeDescription;
	String historicName;
	
	public HoehlenUebersichtPopUpPanelContainer(){
	
	}
	public String getOfficialName() {
		return officialName;
	}
	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}

	public int getHoehlenID() {
		return hoehlenID;
	}
	public void setHoehlenID(int hoehlenID) {
		this.hoehlenID = hoehlenID;
	}
	public ArrayList<String> getBilderURLs() {
		return bilderURLs;
	}
	public void setBilderURLs(ArrayList<String> bilderURLs) {
		this.bilderURLs = bilderURLs;
	}
	public String getCaveTypeDescription() {
		return caveTypeDescription;
	}
	public void setCaveTypeDescription(String caveTypeDescription) {
		this.caveTypeDescription = caveTypeDescription;
	}
	public String getHistoricName() {
		return historicName;
	}
	public void setHistoricName(String historicName) {
		this.historicName = historicName;
	}
	
	

}
