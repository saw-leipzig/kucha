package de.cses.client.kuchaMapClient;




import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanel;
import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanelContainer;


public class HoehlenUebersicht {

 DatabaseServiceAsync  dbService = GWT.create(DatabaseService.class);
 int iD;
 SimpleContainer button;
 Hoehle hoehle;
 String Hoehlenname;
 MouseOverEvent event;

public HoehlenUebersicht(){
	
}
public void createToolTips(){
	MouseOverHandler handlermouse = new MouseOverHandler(){

		@Override
		public void onMouseOver(MouseOverEvent event2) {
			event = event2;
		     hoehle.getHoehlenUebersichtPopUp().setPopupPositionAndShow(new PopupPanel.PositionCallback() {
		          public void setPosition(int offsetWidth, int offsetHeight) {
		        	
		            int left = event.getRelativeElement().getAbsoluteLeft()+event.getRelativeElement().getOffsetWidth();
		            int top = event.getRelativeElement().getAbsoluteTop()+event.getRelativeElement().getOffsetHeight();
		            hoehle.getHoehlenUebersichtPopUp().setPopupPosition(left, top);
		          }
		        });	
		}	
	};
	
	final MouseOutHandler handlermouseout = new MouseOutHandler(){

		@Override
		public void onMouseOut(MouseOutEvent event) {
			hoehle.getHoehlenUebersichtPopUp().hide();
		}
		
	};
	
	button.getWidget().addHandler(handlermouse,MouseOverEvent.getType());
	button.getWidget().addHandler(handlermouseout,MouseOutEvent.getType());
	
	
	 
	
	      
	    
}

// Should be done on Server side 
public void getHoehlenInfos() {

	dbService.getHoehlenInfosbyID(iD, new AsyncCallback<HoehlenUebersichtPopUpPanelContainer>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
			
		}
		@Override
		public void onSuccess(HoehlenUebersichtPopUpPanelContainer popupContainer) {
			HoehlenUebersichtPopUpPanel popup = new HoehlenUebersichtPopUpPanel();
			popup.setHoehlenID(popupContainer.getHoehlenID());
			popup.setOfficialName(popupContainer.getOfficialName());
			popup.setHistoricName(popupContainer.getHistoricName());
			popup.setHoehlenFotoURLs(popupContainer.getBilderURLs());
			popup.setCaveTypeDescription(popupContainer.getCaveTypeDescription());
			popup.iniHoehlenUebersichtPanel();
			hoehle.setHoehlenUebersichtPopUp(popup);
			createToolTips();
			
		}
	});
	
}
public SimpleContainer getButton() {
	return button;
}
public void setButton(SimpleContainer button) {
	this.button = button;
}
public Hoehle getHoehle() {
	return hoehle;
}
public void setHoehle(Hoehle hoehle) {
	this.hoehle = hoehle;
}
public String getHoehlenname() {
	return Hoehlenname;
}
public void setHoehlenname(String hoehlenname) {
	Hoehlenname = hoehlenname;
}
public MouseOverEvent getEvent() {
	return event;
}
public void setEvent(MouseOverEvent event) {
	this.event = event;
}
public int getID() {
	return iD;
}
public void setID(int iD) {
	this.iD = iD;
}





}