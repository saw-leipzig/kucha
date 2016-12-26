package de.cses.client.kuchaMapPopupPanels;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RegionenUebersichtPopUpPanelContainer implements IsSerializable{
	String name="";
	String url = "";
	String description;
	int iD ;
	int imageID;
	
	public RegionenUebersichtPopUpPanelContainer(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getID() {
		return iD;
	}

	public void setID(int iD) {
		this.iD = iD;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	
	
	

}
