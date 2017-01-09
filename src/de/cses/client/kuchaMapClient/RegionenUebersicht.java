package de.cses.client.kuchaMapClient;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.KuchaMapProject.Home;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanel;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanelContainer;


public class RegionenUebersicht {
 
	 Hoehle region;
	 SimpleContainer b;
	 MouseOverEvent event;
	 String regionname;
	 DetailansichtVerwaltung detailansichtVerwaltung;
	 EditierenVerwaltung editierenVerwaltung;
	 int iD;
	
	public RegionenUebersicht(){
		detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		editierenVerwaltung = Home.getKuchaMapPrototyp().getEditierenVerwaltung();
		
	}
	
private  DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

public synchronized void createToolTips(){
	
	MouseOverHandler handlermouse = new MouseOverHandler(){

		@Override
		public void onMouseOver(MouseOverEvent event2) {
			event = event2;
		     region.getRegionUebersichtPopUp().setPopupPositionAndShow(new PopupPanel.PositionCallback() {
		          public void setPosition(int offsetWidth, int offsetHeight) {
		            int left = event.getRelativeElement().getAbsoluteLeft()+event.getRelativeElement().getOffsetWidth();
		            int top = event.getRelativeElement().getAbsoluteTop()+event.getRelativeElement().getOffsetHeight();
		            region.getRegionUebersichtPopUp().setPopupPosition(left, top);
		          }
		        });	
		}	
	};
	ClickHandler clickhandler = new ClickHandler(){
		@Override
		public void onClick(ClickEvent event) {
			detailansichtVerwaltung.setaktiveRegion(detailansichtVerwaltung.getRegionbyID(iD));
			editierenVerwaltung.seteditieren(false);
	    	detailansichtVerwaltung.loadRegion();
		}
	};
	
	final MouseOutHandler handlermouseout = new MouseOutHandler(){

		@Override
		public void onMouseOut(MouseOutEvent event) {
			// TODO Auto-generated method stub
			region.getRegionUebersichtPopUp().hide();
		}
		
	};
	b.getWidget().addHandler(clickhandler, ClickEvent.getType());
	b.getWidget().addHandler(handlermouse, MouseOverEvent.getType());
	b.getWidget().addHandler(handlermouseout, MouseOutEvent.getType());
	
	
	 
	
	      
	    
}
public void getRegionenInfos() {
	
	dbService.getRegionenInfosbyID(iD, new AsyncCallback<RegionenUebersichtPopUpPanelContainer>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
			
			
			
			
		}


		@Override
		public synchronized void onSuccess(RegionenUebersichtPopUpPanelContainer PopupContainer) {
		RegionenUebersichtPopUpPanel popup = new RegionenUebersichtPopUpPanel();
		popup.setName(PopupContainer.getName());
		popup.setDescription(PopupContainer.getDescription());
		popup.setiD(PopupContainer.getID());
		popup.setUrl(PopupContainer.getUrl());
		popup.iniRegionenUebersichtsPanel();
		region.setRegionUebersichtPopUp(popup); 
		createToolTips();
		
			
		}
	});
	
}

public Container getB() {
	return b;
}
public void setB(SimpleContainer b) {
	this.b = b;
}
public MouseOverEvent getEvent() {
	return event;
}
public void setEvent(MouseOverEvent event) {
	this.event = event;
}
public String getRegionname() {
	return regionname;
}
public void setRegionname(String regionname) {
	this.regionname = regionname;
}
public Hoehle getRegion() {
	return region;
}
public void setRegion(Hoehle region) {
	this.region = region;
}
public int getID() {
	return iD;
}
public void setID(int iD) {
	this.iD = iD;
}



}
