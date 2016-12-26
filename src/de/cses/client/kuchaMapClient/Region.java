package de.cses.client.kuchaMapClient;


import java.util.ArrayList;

import com.google.gwt.user.client.ui.Button;

import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanel;


public class Region{



	private int iD;
	private int imageID;
	private String name;
	private String fotoURL = "https://image.jimcdn.com/app/cms/image/transf/dimension=210X210:mode=crop:format=jpg/path/s82859c0cac0b72f4/image/i09c22320254db495/version/1321268250/image.jpg";
	private   ArrayList<Hoehle> hoehlenArrayList = new ArrayList<>();
	private int regionButtonPositionX = 30;
	private int regionButtonPositionY = 100;
	private Button regionButton = new Button();
	private int hoehlenButtonSize= 20;
	private RegionenUebersichtPopUpPanel regionUebersichtPopUp;
	
	public Region(){
		
	}

	public void setFoto(String URL){
		this.fotoURL = URL;
	}
	public String getFoto(){
		return fotoURL;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Hoehle> getHoehlenArrayList() {
		return hoehlenArrayList;
	}
	public void setHoehlenArrayList(ArrayList<Hoehle> hoehlenArrayList) {
		this.hoehlenArrayList = hoehlenArrayList;
	}
	public Hoehle getHoehlebyID(int ID){
		for (int i = 0; i < hoehlenArrayList.size(); i++){
		if(hoehlenArrayList.get(i).getID()==(ID)){
			return hoehlenArrayList.get(i);
		}
		
		}
		return null;
	}
	public int getHoehlenButtonsize(){
		return hoehlenButtonSize;
	}
	public void setHoehlenButtonsize(int HoehlenButtonSize){
		this.hoehlenButtonSize=HoehlenButtonSize;
	}
	public int getRegionButtonPositionX(){
		return regionButtonPositionX;
	}
	public int getRegionButtonPositionY(){
		return regionButtonPositionY;
	}
	public void setRegionButtonPositionX(int RegionButtonPositionX){
		this.regionButtonPositionX = RegionButtonPositionX;
	}
	public void setRegionButtonPositionY(int regionButtonPositionY){
		this.regionButtonPositionY = regionButtonPositionY;
	}
	public void setRegionUebersichtPopUp(RegionenUebersichtPopUpPanel regionUebersichtPopUp){
		this.regionUebersichtPopUp= regionUebersichtPopUp;
	}
	public RegionenUebersichtPopUpPanel getRegionUebersichtPopUp(){
		return regionUebersichtPopUp;
	}
	public void setRegionButton(Button regionButton){
		this.regionButton= regionButton;
		
	}
	public Button getRegionButton(){
		return regionButton;
	}
	public int getID(){
		return iD;
	}
	public void setID(int ID){
		this.iD= ID;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}
	
}
