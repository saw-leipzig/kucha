package de.cses.client.KuchaMapProject;

import java.util.ArrayList;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.kuchaMapClient.DetailansichtVerwaltung;
import de.cses.client.kuchaMapClient.Environment;
import de.cses.client.kuchaMapClient.Hoehle;
import de.cses.client.kuchaMapClient.Region;
import de.cses.shared.HoehlenContainer;
import de.cses.shared.RegionContainer;



public class KuchaDatabase {

	private  DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	DetailansichtVerwaltung detailansichtVerwaltung;
	Environment environment;
	boolean firsttime;
	int aufgerufen = 0;
	public KuchaDatabase(){
		Window.alert("kucha database erzeugt");
		detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		environment = Home.getKuchaMapPrototyp().getEnvironment();
		
	}
	
	public void loadRegionen(boolean firsttimebool){
		
		Window.alert("in regionen laden teil 1");
		aufgerufen++;
		Window.alert("aufgerufen anzahl: "+ aufgerufen);
		this.firsttime= firsttimebool;
		
		 dbService.createRegionen( new AsyncCallback<ArrayList<RegionContainer>>(){

			@Override
			public void onFailure(Throwable caught) {
			Window.alert("regionen laden failed");
				System.err.println("failed");
			}

			@Override
			public void onSuccess(ArrayList<RegionContainer> regionenContainer) {
				Window.alert("regionen laden fertig");
				ArrayList<Region> regionen = new ArrayList<Region>();
				for (int i = 0; i< regionenContainer.size(); i++){
					Region region = new Region();
					region.setImageID(regionenContainer.get(i).getImageID());
					region.setName(regionenContainer.get(i).getName());
					region.setFoto(regionenContainer.get(i).getFotoURL());
					region.setID(regionenContainer.get(i).getID());
					region.setHoehlenButtonsize(regionenContainer.get(i).getHoehlenButtonSize());
					region.setRegionButtonPositionX(regionenContainer.get(i).getRegionButtonPositionX());
					region.setRegionButtonPositionY(regionenContainer.get(i).getRegionButtonPositionY());
					regionen.add(region);
		
				}
				detailansichtVerwaltung.setRegionen(regionen);
				Window.alert("in regionen laden teil 2");
				if(firsttime){
					Window.alert("vor dem starten");
					detailansichtVerwaltung.setaktiveRegion(detailansichtVerwaltung.getRegionbyID(-1));
					Window.alert("die erste geladene region ist die "+detailansichtVerwaltung.getaktiveRegion().getName());
				Window.alert("gestartet");
				Home.getKuchaMapPrototyp().createPanels();
				
				
				}
				else{
					detailansichtVerwaltung.setaktiveRegion(detailansichtVerwaltung.getRegionbyID(detailansichtVerwaltung.getaktiveRegion().getID()));
					
				}
		getHoehlenbyRegionID(detailansichtVerwaltung.getaktiveRegion().getID());
		Window.alert("ganzen region laden mist fertig");
		detailansichtVerwaltung.getFotoAbsolutePanel().clear();
		Window.alert(detailansichtVerwaltung.getaktiveRegion().getFoto());
		detailansichtVerwaltung.getDetailansicht().getHoehlenFotoIMG().setUrl(detailansichtVerwaltung.getaktiveRegion().getFoto());
		detailansichtVerwaltung.getFotoAbsolutePanel().add(detailansichtVerwaltung.getDetailansicht().getHoehlenFotoIMG());
		detailansichtVerwaltung.getEditierenVerwaltung().switchEDundAN();
			}
	});
	
}
	public synchronized void getHoehlenbyRegionID(int ID){
		 dbService.createHoehlenbyRegion( ID, new AsyncCallback<ArrayList<HoehlenContainer>>(){

			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(ArrayList<HoehlenContainer> hoehlenContainer) {
				// TODO Auto-generated method stub
				ArrayList<Hoehle> Hoehlen = new ArrayList<Hoehle>();
				for (int i = 0; i< hoehlenContainer.size(); i++){
					Hoehle hoehle = new Hoehle();
					hoehle.setID(hoehlenContainer.get(i).getID());
					hoehle.setButtonpositionleft(hoehlenContainer.get(i).getButtonPositionLeft());
					hoehle.setButtonpositiontop(hoehlenContainer.get(i).getButtonPositionTop());
					hoehle.setRegionID(hoehlenContainer.get(i).getRegionID());
					Hoehlen.add(hoehle);
		
				}
				detailansichtVerwaltung.getaktiveRegion().setHoehlenArrayList(Hoehlen);
				for(int i = 0; i< detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().size();i++){
					if(i == 0 && detailansichtVerwaltung.getaktiveRegion().getID()== -1){
						i++;
						
					}
					detailansichtVerwaltung.createHoehlenButton(i);	
				}
			}
		
	});
	
}
	public void save(ArrayList<HoehlenContainer> hoehlen, int regionID, int buttonSize, int imageID){
		dbService.saveRegion( hoehlen , regionID, buttonSize,imageID, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(String result) {
				Window.alert("speichern successful");
				detailansichtVerwaltung.loadRegion();
				
			}
		
	});
}
	public void deleteHoehlebyID(int HoehlenID){
		dbService.deleteHoehlebyID(HoehlenID, new AsyncCallback<String>(){

			@Override
			public void onFailure(Throwable caught) {
				
				
			}

			@Override
			public void onSuccess(String result) {
				detailansichtVerwaltung.loadRegion();
				environment.getLoeschenListe().clear();
				
			}
		
	});
}
	
}


