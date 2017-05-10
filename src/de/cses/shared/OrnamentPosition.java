package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrnamentPosition implements IsSerializable{
	
	private int ornamentPositionID;
	String name;
	
	public OrnamentPosition(int positionID, String name){
		this.ornamentPositionID = positionID;
		this.name = name;
	}
	public OrnamentPosition(){
		
	}
	public int getOrnamentPositionID() {
		return ornamentPositionID;
	}
	public void setOrnamentPositionID(int ornamentPositionID) {
		this.ornamentPositionID = ornamentPositionID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
