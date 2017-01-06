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
package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanelContainer;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanelContainer;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CellaEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.HoehlenContainer;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.PublicationEntry;
import de.cses.shared.RegionContainer;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;

public interface DatabaseServiceAsync {

	void dbServer(String name, AsyncCallback<String> callback) throws IllegalArgumentException;

	void getDistricts(AsyncCallback<ArrayList<DistrictEntry>> callback) throws IllegalArgumentException;

	void getImage(int imageID, AsyncCallback<ImageEntry> callback) throws IllegalArgumentException;

	void getImages(AsyncCallback<ArrayList<ImageEntry>> callback) throws IllegalArgumentException;

	void getImages(String where, AsyncCallback<ArrayList<ImageEntry>> callback) throws IllegalArgumentException;

	void getPhotographer(AsyncCallback<ArrayList<PhotographerEntry>> callback) throws IllegalArgumentException;

	void getCaves(AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;

	void getCavesbyDistrictID(int DistrictID, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	
	void getCavesbyDistrictIDKucha(int DistrictID, AsyncCallback<ArrayList<HoehlenContainer>> callback) throws IllegalArgumentException;

	void getOrnaments(AsyncCallback<ArrayList<OrnamentEntry>> callback) throws IllegalArgumentException;

	void getOrnamentsOfOtherCultures(AsyncCallback<ArrayList<OrnamentOfOtherCulturesEntry>> callback)
			throws IllegalArgumentException;

	void updateEntry(String sqlUpdate, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void deleteEntry(String sqlDelete, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void saveOrnamentEntry(OrnamentEntry ornamentEntry, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void insertEntry(String sqlInsert, AsyncCallback<Integer> callback) throws IllegalArgumentException;

	void getDepictions(AsyncCallback<ArrayList<DepictionEntry>> callback) throws IllegalArgumentException;

	void getIconography(AsyncCallback<ArrayList<IconographyEntry>> callback) throws IllegalArgumentException;

	void getPictorialElements(AsyncCallback<ArrayList<PictorialElementEntry>> asyncCallback) throws IllegalArgumentException;

	void getCaveTypebyID(int caveTypeID, AsyncCallback<CaveTypeEntry> callback) throws IllegalArgumentException;

	void getCaveTypes(AsyncCallback<ArrayList<CaveTypeEntry>> callback) throws IllegalArgumentException;

	void getDepictionEntry(int depictionID, AsyncCallback<DepictionEntry> callback);

	void getVendors(AsyncCallback<ArrayList<VendorEntry>> callback) throws IllegalArgumentException;

	void getStyles(AsyncCallback<ArrayList<StyleEntry>> callback) throws IllegalArgumentException;

	void getExpeditions(AsyncCallback<ArrayList<ExpeditionEntry>> asyncCallback) throws IllegalArgumentException;

	void getPublicationEntry(int id, AsyncCallback<PublicationEntry> asyncCallback) throws IllegalArgumentException;

	void getAuthorEntry(int id, AsyncCallback<AuthorEntry> asyncCallback) throws IllegalArgumentException;
	
	void getMasterImageEntryForDepiction(int depictionID, AsyncCallback<ImageEntry> asyncCallback) throws IllegalArgumentException;
	
	void getHoehlenInfosbyID(int iD, AsyncCallback<HoehlenUebersichtPopUpPanelContainer> callback) throws IllegalArgumentException;
	void deleteHoehlebyID(int iD, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getRegionenInfosbyID(int iD, AsyncCallback<RegionenUebersichtPopUpPanelContainer> callback) throws IllegalArgumentException;
	void createRegionen( AsyncCallback<ArrayList<RegionContainer>> callback) throws IllegalArgumentException;
	void createHoehlenbyRegion (int iD,  AsyncCallback<ArrayList<HoehlenContainer>> callback) throws IllegalArgumentException;
	void saveRegion (ArrayList<HoehlenContainer> hoehlen, int regionID, int buttonSize,int imageID,  AsyncCallback<String> callback) throws IllegalArgumentException;
	void setRegionFoto(int imageID, int regionID, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getCavesbyAntechamber(ArrayList<AntechamberEntry> antechambers, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	void getCavesbyCaveType(ArrayList<CaveTypeEntry> caveTypes, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	void getCavesbyCella(ArrayList<CellaEntry> cellas, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	void getCavesbyDistrict(ArrayList<DistrictEntry> districts, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	void getCavesbyDepiction(ArrayList<DepictionEntry> depictions, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	void getCavesbyOrnaments(ArrayList<OrnamentEntry> ornaments, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	
	void getRandomOrnaments(AsyncCallback<ArrayList<OrnamentEntry>> callback) throws IllegalArgumentException;
	
	void getRandomCaves(AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;
	
	void getRandomCaveTypes(AsyncCallback<ArrayList<CaveTypeEntry>> callback) throws IllegalArgumentException;
	
	void getRandomDistricts(AsyncCallback<ArrayList<DistrictEntry>> callback) throws IllegalArgumentException;
}
