package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrnamentFunction implements IsSerializable{
	
	private int ornamentFunctionID;
	
	private String name;
	public OrnamentFunction(){
		
	}
	
	public OrnamentFunction(int ornamentFunctionID, String name){
		this.ornamentFunctionID = ornamentFunctionID;
		this.name= name;
	}

	public int getOrnamentFunctionID() {
		return ornamentFunctionID;
	}

	public void setOrnamentFunctionID(int ornamentFunctionID) {
		this.ornamentFunctionID = ornamentFunctionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

}
