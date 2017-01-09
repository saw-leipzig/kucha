package de.cses.client.kuchaMapClient;


import com.google.gwt.user.client.ui.Button;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ModalPanel;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.InsertContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanel;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanel;


public class Hoehle{
	private int iD;
	int regionID;
	private String name;
	private int buttonPositionLeft;
	private int buttonPositionTop;
	private SimpleContainer hoehlenButton = new SimpleContainer();
	private HoehlenUebersichtPopUpPanel hoehlenUebersichtPopUp;
	private RegionenUebersichtPopUpPanel regionUebersichtPopUp;
	private Draggable drag = new Draggable(hoehlenButton);
	
public Hoehle(){
	drag.setEnabled(false);
}
public int getButtonPositionLeft(){
	return buttonPositionLeft;
}
public int getButtonPositionTop(){
	return buttonPositionTop;
}

public String getname(){
	return name;
}

public void setname(String name){
	this.name= name;
}
public void setButtonpositionleft(int buttonPositionLeft){
	this.buttonPositionLeft= buttonPositionLeft;
}
public void setButtonpositiontop(int buttonPositionTop){
	this.buttonPositionTop= buttonPositionTop;
}

public SimpleContainer getButton(){
	return hoehlenButton;
}

public void setHoehlenUebersichtPopUp(HoehlenUebersichtPopUpPanel HoehlenUebersichtPopUp){
	this.hoehlenUebersichtPopUp = HoehlenUebersichtPopUp;
}

public HoehlenUebersichtPopUpPanel getHoehlenUebersichtPopUp(){
	return hoehlenUebersichtPopUp;
}
public void setID(int iD){
	this.iD = iD;
}
public int getID(){
	return iD;
}
public int getRegionID() {
	return regionID;
}
public void setRegionID(int regionID) {
	this.regionID = regionID;
}
public RegionenUebersichtPopUpPanel getRegionUebersichtPopUp() {
	return regionUebersichtPopUp;
}
public void setRegionUebersichtPopUp(RegionenUebersichtPopUpPanel regionUebersichtPopUp) {
	this.regionUebersichtPopUp = regionUebersichtPopUp;
}
public Draggable getDrag() {
	return drag;
}
public void setDrag(Draggable drag) {
	this.drag = drag;
}



}

