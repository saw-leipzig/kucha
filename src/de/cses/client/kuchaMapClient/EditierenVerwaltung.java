package de.cses.client.kuchaMapClient;


import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.DoubleClickEvent;
import com.google.gwt.event.dom.client.DoubleClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;

import de.cses.client.KuchaMapProject.Home;



public class EditierenVerwaltung{

	private boolean editieren = false;
	private EnvironmentVerwaltung environmentVerwaltung;
	private DetailansichtVerwaltung detailansichtVerwaltung;
	
	
	public EditierenVerwaltung(){
		
	}
	public void iniEditierenVerwaltung(){
		environmentVerwaltung = Home.getKuchaMapPrototyp().getEnvironmentVerwaltung();
		detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		
		
	}
	public boolean geteditieren(){
		return editieren;
	}
	public void seteditieren(boolean editieren){
		this.editieren= editieren;
	}
	public void switchEDundAN(){
		if(editieren){
			environmentVerwaltung.enableEditierenEnvironment();
			environmentVerwaltung.getEnvironment().getHoehlenButtonSlider().setValue(detailansichtVerwaltung.getaktiveRegion().getHoehlenButtonsize());
			
			
		}
		else{
			environmentVerwaltung.disableEditierenEnvironment();
		}
		changeClickHandler();
		
	}
	
	public void changeClickHandler(){
		if(editieren){
			if(detailansichtVerwaltung.getaktiveRegion().getID()!=-1){
				
				DoubleClickHandler doublehandler = new DoubleClickHandler() {
				    @Override
				    public void onDoubleClick(DoubleClickEvent event) { 
				        detailansichtVerwaltung.ShowHoehleErstellenPanel(event.getX(), event.getY());
				        
				    }    
				};
				HandlerRegistration handler = detailansichtVerwaltung.getDetailansicht().getHoehlenFotoIMG().addHandler(doublehandler, DoubleClickEvent.getType());
				detailansichtVerwaltung.getHandlerArrayList().add(handler);
				
				for(int i = 0; i<detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().size(); i++){
		
					
					CustomClickHandler hoehleLoeschenPanelHandler = new CustomClickHandler();
					hoehleLoeschenPanelHandler.setButton(detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i).getButton());
					hoehleLoeschenPanelHandler.setHoehlenID(detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i).getID());
					detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
					HandlerRegistration handlerRegistration = detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i).getButton().getWidget().addHandler(hoehleLoeschenPanelHandler, ClickEvent.getType());
					detailansichtVerwaltung.getHandlerArrayList().add(handlerRegistration);
				
					
					
				
				}
				
			}
				detailansichtVerwaltung.makeDetailansichtDraggable();
			
			
		}
		else{

				detailansichtVerwaltung.makeDetailansichtNoDraggable();
				for(int i = 0; i < detailansichtVerwaltung.getHandlerArrayList().size(); i++){
					detailansichtVerwaltung.getHandlerArrayList().get(i).removeHandler();
				}
			
			
			
		}
	}

}
