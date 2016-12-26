package de.cses.client.kuchaMapClient;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
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
	
	
	public ArrayList<CaveEntry> search (){
		// übertragung der id von Cave Entry in Hoehlen(Container)? oder nur vergleich der IDs
		return null;
	}
	
	 public ArrayList<CaveEntry> findRelatedCaves(ArrayList<CaveEntry> hoehlen){
			for(int i =0; i < hoehlen.size(); i++){
				finaleHoehlen.add(hoehlen.get(i));
			}
		 
		 return null;
		 
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
	 /*
	 public ArrayList<CaveEntry> findRelatedCavesbyDistrict(ArrayList<DistrictEntry> districts){
			dbService.getCavesbyDistrict(districts, new AsyncCallback<ArrayList<CaveEntry>>(){
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
			dbService.getCavesbyOrnaments(ornaments, new AsyncCallback<ArrayList<CaveEntry>>(){
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
	 
	 public void findRelatedCavesbyDepiction(ArrayList<DepictionEntry> depictions){
		 dbService.getCavesbyDepiction(depictions, new AsyncCallback<ArrayList<CaveEntry>>(){
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
	 
	 
	 public void findRelatedCavesbyCaveType(ArrayList<CaveTypeEntry> caveTypes){
		 dbService.getCavesbyCaveType(caveTypes, new AsyncCallback<ArrayList<CaveEntry>>(){
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
	 
	

}
