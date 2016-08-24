package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrnamentOfOtherCulturesEntry implements IsSerializable{
	private int ornamentOfOtherCulturesID;
	private String name;
	
	public OrnamentOfOtherCulturesEntry(){
		
	}
	
	public OrnamentOfOtherCulturesEntry(int ornamentOfOtherCulturesID, String name){
		super();
		this.name=name;
		this.ornamentOfOtherCulturesID= ornamentOfOtherCulturesID;
	}
	
	public int getOrnamentOfOtherCulturesID() {
		return ornamentOfOtherCulturesID;
	}
	public void setIOrnamentOfOtherCulturesID(int ornamentOfOtherCulturesID) {
		this.ornamentOfOtherCulturesID = ornamentOfOtherCulturesID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
