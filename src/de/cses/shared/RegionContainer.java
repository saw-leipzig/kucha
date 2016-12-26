package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RegionContainer implements IsSerializable{
	public RegionContainer(){
		
	}
	private int id;
	private String name;
	private String fotoURL;
	private int regionButtonPositionX = 30;
	private int regionButtonPositionY = 100;
	private int hoehlenButtonSize= 20;
	private int imageID;
	
	public int getID() {
		return id;
	}
	public void setID(int iD) {
		id = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFotoURL() {
		return fotoURL;
	}
	public void setFotoURL(String fotoURL) {
		this.fotoURL = fotoURL;
	}
	public int getRegionButtonPositionX() {
		return regionButtonPositionX;
	}
	public void setRegionButtonPositionX(int regionButtonPositionX) {
		this.regionButtonPositionX = regionButtonPositionX;
	}
	public int getRegionButtonPositionY() {
		return regionButtonPositionY;
	}
	public void setRegionButtonPositionY(int regionButtonPositionY) {
		this.regionButtonPositionY = regionButtonPositionY;
	}
	public int getHoehlenButtonSize() {
		return hoehlenButtonSize;
	}
	public void setHoehlenButtonSize(int hoehlenButtonSize) {
		this.hoehlenButtonSize = hoehlenButtonSize;
	}
	public int getImageID() {
		return imageID;
	}
	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	

}
