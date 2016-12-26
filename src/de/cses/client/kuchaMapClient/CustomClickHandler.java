package de.cses.client.kuchaMapClient;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;

import de.cses.client.kuchaMapPopupPanels.HoehleLoeschenPopupPanel;


public class CustomClickHandler implements ClickHandler{

	Button button;
	int hoehlenID;
	DetailansichtVerwaltung detailansichtVerwaltung;
	HoehleLoeschenPopupPanel hoehleLoeschenPopupPanel;
	
	public CustomClickHandler(){
		
	}
	
	
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}
	public int getHoehlenID() {
		return hoehlenID;
	}
	public void setHoehlenID(int hoehlenID) {
		this.hoehlenID = hoehlenID;
	}

	@Override
	public void onClick(ClickEvent event) {
		
    	hoehleLoeschenPopupPanel  = new HoehleLoeschenPopupPanel();
    	hoehleLoeschenPopupPanel.setID(hoehlenID);
    	hoehleLoeschenPopupPanel.iniHoehleLoeschenPopupPanel();
        hoehleLoeschenPopupPanel.setPopupPositionAndShow(new PositionCallback(){

			@Override
			public void setPosition(int offsetWidth, int offsetHeight) {
			
				 int leftloeschen=button.getAbsoluteLeft()+button.getOffsetWidth();
	             int toploeschen = button.getAbsoluteTop()+button.getOffsetWidth();
	            hoehleLoeschenPopupPanel.setPopupPosition(leftloeschen, toploeschen);
			}
        	
        });
    
		
	}
	

}
