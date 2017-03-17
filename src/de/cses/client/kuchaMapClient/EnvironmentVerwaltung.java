package de.cses.client.kuchaMapClient;


import java.util.ArrayList;


import com.google.gwt.event.shared.HandlerRegistration;

import de.cses.client.KuchaMapProject.Home;


public class EnvironmentVerwaltung  {

	private  ArrayList<HandlerRegistration> handlerArrayList = new ArrayList<HandlerRegistration>();
	private Environment environment;
	
	public EnvironmentVerwaltung(){
		
	}
	
	public void iniEnvironmentVerwaltung(){
		environment= Home.getKuchaMapPrototyp().getEnvironment();
	}
	public Environment getEnvironment(){
		return environment;
	}
	public void enableEditierenEnvironment(){
		environment.getENBottomHeaderHorizontalPanel().setVisible(true);
		environment.getSuchenButton().setEnabled(false);
		environment.getFotoUpload().setVisible(true);
		environment.getSuchenBox().setEnabled(false);
		environment.getHoehlenButtonSlider().setVisible(true);
		environment.getResetSuche().setEnabled(false);
		
	}
	public void disableEditierenEnvironment(){
		environment.getENBottomHeaderHorizontalPanel().setVisible(false);
		environment.getSuchenButton().setEnabled(true);
		environment.getFotoUpload().setVisible(false);
		environment.getSuchenBox().setEnabled(true);
		environment.getHoehlenButtonSlider().setVisible(false);
		environment.getResetSuche().setEnabled(true);
		
	}
	public ArrayList<HandlerRegistration> getHandlerArrayList() {
		return handlerArrayList;
	}
	public void setHandlerArrayList(ArrayList<HandlerRegistration> handlerArrayList) {
		this.handlerArrayList = handlerArrayList;
	}
	
}
