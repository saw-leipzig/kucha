package de.cses.client.kuchaMapClient;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.KuchaMapProject.Home;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanel;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CellaEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.NicheEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.TypologyEntry;

public class Suche {
	
	ArrayList<CellaEntry> cellaArrayList = new ArrayList<CellaEntry>();
	ArrayList<OrnamentEntry> ornamentArrayList = new ArrayList<OrnamentEntry>();
	ArrayList<NicheEntry> nicheArrayList = new ArrayList<NicheEntry>();
	ArrayList<AntechamberEntry> antechamberArrayList = new ArrayList<AntechamberEntry>();
	ArrayList<CaveEntry> caveArrayList = new ArrayList<CaveEntry>();
	ArrayList<CaveTypeEntry> caveTypeArrayList = new ArrayList<CaveTypeEntry>();
	ArrayList<DistrictEntry> districtArrayList = new ArrayList<DistrictEntry>();
	ArrayList<OrnamentOfOtherCulturesEntry> otherCulturesArrayList = new ArrayList<OrnamentOfOtherCulturesEntry>();
	ArrayList<PhotographerEntry> photographerArrayList = new ArrayList<PhotographerEntry>();
	ArrayList<TypologyEntry> typologyArrayList = new ArrayList<TypologyEntry>();
	ArrayList<DepictionEntry> depictionArrayList = new ArrayList<DepictionEntry>();
	private  DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	ArrayList<CaveEntry> finaleHoehlen = new ArrayList<CaveEntry>();
	boolean searching = false;
	
	
	public void search (){
		searching = true;
		for(int i = 0; i < Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size();i++){
			Image image = new Image("http://kucha.informatik.hu-berlin.de/tomcat/images/BUTTONHOEHLEpngblau.png");
			Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getButton().add(image);
			Window.alert("change einen style 2");
		}
		
	}
	
	 public ArrayList<CaveEntry> findRelatedCaves(ArrayList<CaveEntry> hoehlen){
		 Window.alert("in find related caves");
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
			for(int i =0; i < hoehlen.size(); i++){
				hoehlenErgebnisse.add(hoehlen.get(i));
			}
		 changeStyle(hoehlenErgebnisse);
		 return hoehlen;
		 
	 }
	 /*
	 public ArrayList<CaveEntry> findRelatedCavesbyCella(ArrayList<CellaEntry> cellas){
			dbService.getCavesbyCella(cellas, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
		 
		 return null;
		 
	 }*/
	 /*
	 public void findRelatedCavesbyAntechamber(ArrayList<AntechamberEntry> antechamber){
			dbService.getCavesbyAntechamber(antechamber, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
	 }
	 */
	 
	 public ArrayList<CaveEntry> findRelatedCavesbyDistrict(ArrayList<DistrictEntry> districts){
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
			dbService.getCavesbyDistrict(districts, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Window.alert("fail find related cavesbydistrict");
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
					Window.alert("caves by disctrict einen geadded");
					hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
				}
				Window.alert("success find related cavesbydistrict");
				changeStyle(hoehlenErgebnisse);
					
				}
			});
		 
		 return null;
		 
		 
	 }

	/* public ArrayList<CaveEntry> findRelatedCavesbyNiche(ArrayList<NicheEntry> niches){
		 
			dbService.getCavesbyNiches(niches, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
		 return null;
		 
	 }
	 */
	 public void findRelatedCavesbyOrnament(ArrayList<OrnamentEntry> ornaments){
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
			dbService.getCavesbyOrnaments(ornaments, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Window.alert("fail find related cavesby ornament");
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
					Window.alert("caves by ornament einen geadded");
					hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
				}
				Window.alert("success find related cavesbyornament");
					changeStyle(hoehlenErgebnisse);
				}
			});	 
	 }
	 
	 public void findRelatedCavesbyDepiction(ArrayList<DepictionEntry> depictions){
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
		 dbService.getCavesbyDepiction(depictions, new AsyncCallback<ArrayList<CaveEntry>>(){
			 
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
				Window.alert("caves by depiction einen geadded");
				}
				changeStyle(hoehlenErgebnisse);
				
				}
			});	
	 }
	 
	 
	 public void findRelatedCavesbyCaveType(ArrayList<CaveTypeEntry> caveTypes){
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
		 dbService.getCavesbyCaveType(caveTypes, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
					Window.alert("fail find related cavesby cave type");
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
				Window.alert("caves by cave type einen geadded");
				}
				Window.alert("success find related cavesby cave type");
				changeStyle(hoehlenErgebnisse);
				}
			});	
	 }
	 
	 /*
	 public ArrayList<CaveEntry> findRelatedCavesbyOtherCultures(ArrayList<OrnamentOfOtherCulturesEntry> ornamentsOfOtherCultues){
			dbService.getCavesbyOrnamentsOfOtherCultures(ornamentsOfOtherCultues, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
		 
		 return null;
		 
	 }
	 public ArrayList<CaveEntry> findRelatedCavesbyPhotographer(ArrayList<PhotographerEntry> photographers){
			dbService.getCavesbyPhotographer(photographers, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
		 
		 return null;
		 
	 }
	 public ArrayList<CaveEntry> findRelatedCavesbyTypology(ArrayList<TypologyEntry> typologys){
			dbService.getCavesbyTypology(typologys, new AsyncCallback<ArrayList<CaveEntry>>(){
				public void onFailure(Throwable caught) {
					// Show the RPC error message to the user
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				finaleHoehlen.add(hoehlenContainerArrayList.get(i));
				}
					
				}
			});
		 
		 return null;
		 
	 }
	 */

	public ArrayList<CellaEntry> getCellaArrayList() {
		return cellaArrayList;
	}

	public void setCellaArrayList(ArrayList<CellaEntry> cellaArrayList) {
		this.cellaArrayList = cellaArrayList;
	}

	public ArrayList<NicheEntry> getNicheArrayList() {
		return nicheArrayList;
	}

	public void setNicheArrayList(ArrayList<NicheEntry> nicheArrayList) {
		this.nicheArrayList = nicheArrayList;
	}

	public ArrayList<AntechamberEntry> getAntechamberArrayList() {
		return antechamberArrayList;
	}

	public void setAntechamberArrayList(ArrayList<AntechamberEntry> antechamberArrayList) {
		this.antechamberArrayList = antechamberArrayList;
	}

	public ArrayList<CaveEntry> getCaveArrayList() {
		return caveArrayList;
	}

	public void setCaveArrayList(ArrayList<CaveEntry> caveArrayList) {
		this.caveArrayList = caveArrayList;
	}

	public ArrayList<CaveTypeEntry> getCaveTypeArrayList() {
		return caveTypeArrayList;
	}

	public void setCaveTypeArrayList(ArrayList<CaveTypeEntry> caveTypeArrayList) {
		this.caveTypeArrayList = caveTypeArrayList;
	}

	public ArrayList<DistrictEntry> getDistrictArrayList() {
		return districtArrayList;
	}

	public void setDistrictArrayList(ArrayList<DistrictEntry> districtArrayList) {
		this.districtArrayList = districtArrayList;
	}

	public ArrayList<OrnamentOfOtherCulturesEntry> getOtherCulturesArrayList() {
		return otherCulturesArrayList;
	}

	public void setOtherCulturesArrayList(ArrayList<OrnamentOfOtherCulturesEntry> otherCulturesArrayList) {
		this.otherCulturesArrayList = otherCulturesArrayList;
	}

	public ArrayList<PhotographerEntry> getPhotographerArrayList() {
		return photographerArrayList;
	}

	public void setPhotographerArrayList(ArrayList<PhotographerEntry> photographerArrayList) {
		this.photographerArrayList = photographerArrayList;
	}

	public ArrayList<TypologyEntry> getTypologyArrayList() {
		return typologyArrayList;
	}

	public void setTypologyArrayList(ArrayList<TypologyEntry> typologyArrayList) {
		this.typologyArrayList = typologyArrayList;
	}

	public ArrayList<CaveEntry> getFinaleHoehlen() {
		return finaleHoehlen;
	}

	public void setFinaleHoehlen(ArrayList<CaveEntry> finaleHoehlen) {
		this.finaleHoehlen = finaleHoehlen;
	}

	public ArrayList<OrnamentEntry> getOrnamentArrayList() {
		return ornamentArrayList;
	}

	public void setOrnamentArrayList(ArrayList<OrnamentEntry> ornamentArrayList) {
		this.ornamentArrayList = ornamentArrayList;
	}
	
	
	
	
	public void changeStyle(ArrayList<CaveEntry> hoehlenErgebnisse){
		Window.alert("in change style");
		
	if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
		Window.alert("die aktive region ist -1");
		Window.alert(Integer.toString(hoehlenErgebnisse.size()));
		for(int i = 0; i< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().size(); i++){
			Window.alert("change einen style 1 kurz davor pos1");
			for(int j = 0; j< hoehlenErgebnisse.size(); j++){
				Window.alert("change einen style 1 kurz davor pos2");
				Window.alert("Regionen ID: "+Integer.toString(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID()) + "HoehlenRegionID: "+hoehlenErgebnisse.get(j).getDistrictID());
				if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID() == hoehlenErgebnisse.get(j).getDistrictID()){
					Window.alert("change einen style 1 kurz davor pos3");
					Window.alert("Regionen ID: "+Integer.toString(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID()) + "HoehlenRegionID: "+hoehlenErgebnisse.get(j).getDistrictID());
					Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Wegzeichen_gr%C3%BCner_Punkt.svg/599px-Wegzeichen_gr%C3%BCner_Punkt.svg.png");
					for(int k = 0; k< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); k++){
						Window.alert("change einen style 1 kurz davor pos4");
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getID()==Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID() ){
				
							Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getButton().add(image);
							

							Window.alert("change einen style 1");
						}
					}
			
				}
			
			}
			
		}
	}
	else{
	for(int i = 0; i< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size();i++){
		for(int u = 0; u < hoehlenErgebnisse.size(); u++){
			if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getID()== hoehlenErgebnisse.get(u).getCaveID()){
				Image image = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/7/79/Wegzeichen_gr%C3%BCner_Punkt.svg/599px-Wegzeichen_gr%C3%BCner_Punkt.svg.png");
				Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(u).getButton().add(image);
				
				HoehlenUebersicht hoehlenUebersicht = new HoehlenUebersicht();
				
				hoehlenUebersicht.setButton(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(u).getButton());
				hoehlenUebersicht.setHoehle(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(u));
				hoehlenUebersicht.setID(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(u).getID());
			
				hoehlenUebersicht.getHoehlenInfos();
				
			Window.alert("style in fall 2 changed");
			}
			
		}
	}
	}
	}

	public boolean isSearching() {
		return searching;
	}

	public void setSearching(boolean searching) {
		this.searching = searching;
	}
	
	 
	

}
