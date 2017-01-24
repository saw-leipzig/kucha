/*
 * Copyright 2016 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.client.kuchaMapClient;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.KuchaMapProject.Home;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.OrnamentEntry;

/**
 * @author nina
 *
 */
public class SucheSminulation {
	private static  DatabaseServiceAsync dbService = GWT.create(DatabaseService.class); 
	static ArrayList<OrnamentEntry> ornamentArrayList = new ArrayList<OrnamentEntry>();
	static ArrayList<CaveTypeEntry> caveTypeArrayList = new ArrayList<CaveTypeEntry>();
	static ArrayList<DistrictEntry> districtArrayList = new ArrayList<DistrictEntry>();
	static ArrayList<CaveEntry> caveArrayList = new ArrayList<CaveEntry>();
	static Suche suche = Home.getKuchaMapPrototyp().getSuche();
	
public static void sucheSimulation(String eingabe){
	ornamentArrayList = new ArrayList<OrnamentEntry>();
	caveTypeArrayList = new ArrayList<CaveTypeEntry>();
	districtArrayList = new ArrayList<DistrictEntry>();
	caveArrayList = new ArrayList<CaveEntry>();
	
	// get some example caves
	dbService.getRandomOrnaments( new AsyncCallback<ArrayList<OrnamentEntry>>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
		}

		@Override
		public synchronized void onSuccess(ArrayList<OrnamentEntry> randomOrnaments) {
			for(int i= 0; i < randomOrnaments.size(); i++){
		ornamentArrayList.add(randomOrnaments.get(i));
			}
			suche.setOrnamentArrayList(ornamentArrayList);
			
			suche.findRelatedCavesbyOrnament(ornamentArrayList);
		}
			
		}
	);	
	
	// get some example ornaments
	dbService.getRandomCaves( new AsyncCallback<ArrayList<CaveEntry>>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
		}

		@Override
		public synchronized void onSuccess(ArrayList<CaveEntry> hoehlenContainerArrayList) {
		for(int i =0; i < hoehlenContainerArrayList.size(); i++){
		caveArrayList.add(hoehlenContainerArrayList.get(i));
		}
		suche.setCaveArrayList(caveArrayList);
		suche.findRelatedCaves(caveArrayList);
		}
	});
	
	// get some example cave types
	dbService.getRandomDistricts( new AsyncCallback<ArrayList<DistrictEntry>>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
		}

		@Override
		public synchronized void onSuccess(ArrayList<DistrictEntry> hoehlenContainerArrayList) {
		for(int i =0; i < hoehlenContainerArrayList.size(); i++){
		districtArrayList.add(hoehlenContainerArrayList.get(i));
		}
		suche.setDistrictArrayList(districtArrayList);
		suche.findRelatedCavesbyDistrict(districtArrayList);
		}
	});
	
	// get some example districts
	dbService.getRandomCaveTypes( new AsyncCallback<ArrayList<CaveTypeEntry>>(){
		public void onFailure(Throwable caught) {
			// Show the RPC error message to the user
		}

		@Override
		public synchronized void onSuccess(ArrayList<CaveTypeEntry> hoehlenContainerArrayList) {
		for(int i =0; i < hoehlenContainerArrayList.size(); i++){
		caveTypeArrayList.add(hoehlenContainerArrayList.get(i));
		}
		suche.setCaveTypeArrayList(caveTypeArrayList);
		suche.findRelatedCavesbyCaveType(caveTypeArrayList);
		}
	});
	
}
}
