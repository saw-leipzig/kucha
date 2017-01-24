package de.cses.client.kuchaMapClient;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
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
			 image.setPixelSize(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize(), Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize());
	
			if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()== -1){
				
				RegionenUebersicht regionenUebersicht = new RegionenUebersicht();
				
				regionenUebersicht.setB(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getButton());
				regionenUebersicht.setRegion(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i));
				regionenUebersicht.setID(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getID());
				regionenUebersicht.setRegionname(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getname());
			
				regionenUebersicht.getRegionenInfos();
			}
			else{
				HoehlenUebersicht hoehlenUebersicht = new HoehlenUebersicht();
				
				hoehlenUebersicht.setButton(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getButton());
				hoehlenUebersicht.setHoehle(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i));
				hoehlenUebersicht.setID(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getID());
			
				hoehlenUebersicht.getHoehlenInfos();
			}
		}
		
	}
	
	 public ArrayList<CaveEntry> findRelatedCaves(ArrayList<CaveEntry> hoehlenContainerArrayList){
		
		 final ArrayList<CaveEntry> hoehlenErgebnisse = new ArrayList<CaveEntry>();
			for(int i =0; i < hoehlenContainerArrayList.size(); i++){
				if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
					if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().allCaveIDs.contains(hoehlenContainerArrayList.get(i).getCaveID() )){
						
						hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
					}
				}
				else{for(int j = 0; j< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); j++){
				if(hoehlenContainerArrayList.get(i).getCaveID() == Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(j).getID()){
					hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
				
				}	
				}
				}
				}
		 changeStyle(hoehlenErgebnisse);
		 return hoehlenContainerArrayList;
				
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
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
				for(int i =0; i < hoehlenContainerArrayList.size(); i++){
					if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().allCaveIDs.contains(hoehlenContainerArrayList.get(i).getCaveID() )){
							hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
						}
					}
					
					else{for(int j = 0; j< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); j++){
					if(hoehlenContainerArrayList.get(i).getCaveID() == Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(j).getID()){
						hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
					
					}	
					}
				}
				}
				
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
				
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
					for(int i =0; i < hoehlenContainerArrayList.size(); i++){
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
							if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().allCaveIDs.contains(hoehlenContainerArrayList.get(i).getCaveID() )){
								
								hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
							}
						}
						else{
						for(int j = 0; j< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); j++){
						if(hoehlenContainerArrayList.get(i).getCaveID() == Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(j).getID()){
							hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
							
						}	
						}
					}
					}
				
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
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
							if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().allCaveIDs.contains(hoehlenContainerArrayList.get(i).getCaveID() )){
								hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
							}
						}
						else{
						for(int j = 0; j< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); j++){
						if(hoehlenContainerArrayList.get(i).getCaveID() == Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(j).getID()){
							hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
							
						}	
							}
					}
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
					
				}

				@Override
				public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
					for(int i =0; i < hoehlenContainerArrayList.size(); i++){
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
							if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().allCaveIDs.contains(hoehlenContainerArrayList.get(i).getCaveID() )){
								
								hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));
							}
						}
						else {
						for(int j = 0; j< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); j++){
						if(hoehlenContainerArrayList.get(i).getCaveID() == Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(j).getID()){
							hoehlenErgebnisse.add(hoehlenContainerArrayList.get(i));

						}	
							}
					}
				}
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
		
	if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getID()==-1){
		for(int i = 0; i< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().size(); i++){
			for(int j = 0; j< hoehlenErgebnisse.size(); j++){
				if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID() == hoehlenErgebnisse.get(j).getDistrictID()){
					for(int k = 0; k< Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().size(); k++){
						if(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getID()==Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getRegionen().get(i).getID() ){
							Image image = new Image("http://kucha.informatik.hu-berlin.de/tomcat/images/BUTTONHOEHLEgruen.png");
							Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getButton().add(image);
							image.setPixelSize(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize(), Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize());
							
							RegionenUebersicht regionenUebersicht = new RegionenUebersicht();
							
							regionenUebersicht.setB(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getButton());
							regionenUebersicht.setRegion(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k));
							regionenUebersicht.setID(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getID());
							regionenUebersicht.setRegionname(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(k).getname());
							
						
							regionenUebersicht.getRegionenInfos();
							
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
				Image image = new Image("http://kucha.informatik.hu-berlin.de/tomcat/images/BUTTONHOEHLEgruen.png");
				Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getButton().add(image);
				 image.setPixelSize(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize(), Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenButtonsize());
				
				HoehlenUebersicht hoehlenUebersicht = new HoehlenUebersicht();
				
				hoehlenUebersicht.setButton(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getButton());
				hoehlenUebersicht.setHoehle(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i));
				hoehlenUebersicht.setID(Home.getKuchaMapPrototyp().getDetailansichtVerwaltung().getaktiveRegion().getHoehlenArrayList().get(i).getID());
			
				hoehlenUebersicht.getHoehlenInfos();
				
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
