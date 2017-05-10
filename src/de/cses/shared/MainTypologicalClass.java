package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class MainTypologicalClass implements IsSerializable{
	
	private int mainTypologicalClassID;
	private String name;
	
	/**
	 * @param int1
	 * @param string
	 */
	public MainTypologicalClass(){
		
	}
	public MainTypologicalClass(int mainTypologicalClassID, String name) {
		this.mainTypologicalClassID = mainTypologicalClassID;
		this.name = name;
	}
	public int getMainTypologicalClassID() {
		return mainTypologicalClassID;
	}
	public void setMainTypologicalClassID(int mainTypologicalClassID) {
		this.mainTypologicalClassID = mainTypologicalClassID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
