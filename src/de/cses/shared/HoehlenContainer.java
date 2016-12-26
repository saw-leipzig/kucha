package de.cses.shared;



import com.google.gwt.user.client.rpc.IsSerializable;

public class HoehlenContainer implements IsSerializable{
	/**
	 * 
	 */
	

	public HoehlenContainer(){
		
	}
	int regionID;
	private int caveID;
	private String name;
	private int buttonPositionLeft;
	private int buttonPositionTop;
	
	public HoehlenContainer(int regionID, int caveID, String name){
		this.regionID=regionID;
		this.caveID = caveID;
		this.name= name;
	}
	
	public int getID() {
		return caveID;
	}
	public void setID(int iD) {
		caveID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getButtonPositionLeft() {
		return buttonPositionLeft;
	}
	public void setButtonPositionLeft(int buttonPositionLeft) {
		this.buttonPositionLeft = buttonPositionLeft;
	}
	public int getButtonPositionTop() {
		return buttonPositionTop;
	}
	public void setButtonPositionTop(int buttonPositionTop) {
		this.buttonPositionTop = buttonPositionTop;
	}
	public int getRegionID() {
		return regionID;
	}
	public void setRegionID(int regionID) {
		this.regionID = regionID;
	}
	

}
