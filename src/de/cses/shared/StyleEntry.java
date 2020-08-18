package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class StyleEntry implements IsSerializable {
	
	private int styleID;
	private String styleName;

	public StyleEntry() {
	}

	public StyleEntry(int styleID, String styleName) {
		super();
		this.styleID = styleID;
		this.styleName = styleName;
	}

	public int getStyleID() {
		return styleID;
	}

	public void setStyleID(int styleID) {
		this.styleID = styleID;
	}

	public String getStyleName() {
		return styleName;
	}

	public void setStyleName(String styleName) {
		this.styleName = styleName;
	}

}
