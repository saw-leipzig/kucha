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
package de.cses.server;

import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sencha.gxt.data.shared.TreeStore;

import de.cses.client.DatabaseService;
import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanelContainer;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanelContainer;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CellaEntry;
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

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

	public String dbServer(String name) throws IllegalArgumentException {
		
		MysqlConnector connector = MysqlConnector.getInstance();
		System.err.println("looking for " + name + " in database");
		String result = name + " is " + connector.getTestTable().get(name) + " years old!";
		return result;		
	}

	public ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<DistrictEntry> districts = connector.getDistricts();;
		return districts;
	}
	public ImageEntry getImage(int imageID) throws IllegalArgumentException{
		MysqlConnector connector = MysqlConnector.getInstance();
		ImageEntry image = connector.getImageEntry(imageID);
		return image;
	}

	public ArrayList<ImageEntry> getImages() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getImageEntries();
	}
	
	public ArrayList<ImageEntry> getImages(String sqlWhere) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getImageEntries(sqlWhere);
	}
	
	public ArrayList<PhotographerEntry> getPhotographer() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPhotographerEntries();
	}

	/**
	 * A universal SQL UPDATE call for the Kucha database
	 * The String needs to contain the full sql command, including the UPDATE statement at the beginning!
	 */
	public boolean updateEntry(String sqlUpdate) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateEntry(sqlUpdate);
	}

	/**
	 * A universal SQL DELETE call for the Kucha database
	 * The String needs to contain the full sql command, including the DELETE statement at the beginning!
	 */
	public boolean deleteEntry(String sqlDelete) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.deleteEntry(sqlDelete);
	}
	
	/**
	 * A universal SQL INSERT call for the Kucha database
	 * The String needs to contain the full sql command, including the INSERT statement at the beginning!
	 * @param sqlInsert
	 * @return
	 */
	public int insertEntry(String sqlInsert) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertEntry(sqlInsert);
	}

	@Override
	public ArrayList<CaveEntry> getCaves() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaves();
	}



	@Override
	public ArrayList<OrnamentEntry> getOrnaments() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnaments();
	}

	@Override
	public ArrayList<OrnamentOfOtherCulturesEntry> getOrnamentsOfOtherCultures() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnametsOfOtherCultures();
	}

	@Override
	public boolean saveOrnamentEntry(OrnamentEntry ornamentEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.saveOrnamentEntry(ornamentEntry);
	}
	@Override
	public CaveTypeEntry getCaveTypebyID(int caveTypeID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveTypebyID(caveTypeID);
	}

	@Override
	public ArrayList<CaveTypeEntry> getCaveTypes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveTypes();
	}

	@Override
	public ArrayList<DepictionEntry> getDepictions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getDepictions();
	}

	@Override
	public DepictionEntry getDepictionEntry(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getDepictionEntry(depictionID);
//		this.getThreadLocalRequest().getAttribute("user");
	}

	@Override
	public ArrayList<IconographyEntry> getIconography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getIconography();
	}

	@Override
	public ArrayList<PictorialElementEntry> getPictorialElements() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPictorialElements();
	}

	@Override
	public ArrayList<VendorEntry> getVendors() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getVendors();
	}

	@Override
	public ArrayList<StyleEntry> getStyles() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getStyles();
	}

	@Override
	public ArrayList<ExpeditionEntry> getExpeditions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getExpeditions();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getPublicationEntry(int)
	 */
	@Override
	public PublicationEntry getPublicationEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPublicationEntry(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getAuthorEntry(int)
	 */
	@Override
	public AuthorEntry getAuthorEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAuthorEntry(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getMasterImageEntryForDepiction(int)
	 */
	@Override
	public ImageEntry getMasterImageEntryForDepiction(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		int relatedImageID = connector.getRelatedMasterImageID(depictionID);
		return connector.getImageEntry(relatedImageID);
	}
	
	public HoehlenUebersichtPopUpPanelContainer getHoehlenInfosbyID(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		HoehlenUebersichtPopUpPanelContainer result = connector.getHoehleInfosbyID(id);
		return result;		
	}
	
	public RegionenUebersichtPopUpPanelContainer getRegionenInfosbyID(int ID) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		RegionenUebersichtPopUpPanelContainer result = connector.getRegionenInfosbyID(ID);;
		return result;		
	}
	
	public ArrayList<HoehlenContainer> getCavesbyDistrictIDKucha(int DistrictID) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		return connector.getCavesbyDistrictIDKucha(DistrictID);
	}

	
	public ArrayList<RegionContainer> createRegionen() throws IllegalArgumentException {
		System.err.println("in server side imple");
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<RegionContainer> Regionen = connector.createRegionen();;
		return Regionen;
	}


	public ArrayList<HoehlenContainer> createHoehlenbyRegion(int iD ) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<HoehlenContainer> Hoehlen = connector.getHoehlenbyRegionID(iD);
		return Hoehlen;
	}

	
	public String saveRegion(ArrayList<HoehlenContainer> hoehlen, int regionID, int buttonSize, int imageID) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		String saved = connector.saveRegion(hoehlen, regionID, buttonSize, imageID);
		return saved;
	}
	public String deleteHoehlebyID( int hoehlenID) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		String saved = connector.deleteHoehlebyID(hoehlenID);
		return saved;
	}

	public String setRegionFoto(int imageID, int regionID) throws IllegalArgumentException{
		MysqlConnector connector =MysqlConnector.getInstance();
		String saved="";
		try {
			saved = connector.setRegionFoto(imageID, regionID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return saved;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyCella(ArrayList<CellaEntry> cellas) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		try {
			caves = connector.getCavesbyCella(cellas);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caves;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyAntechamber(ArrayList<AntechamberEntry> antechamber)
			throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		try {
			caves = connector.getCavesbyAntechamber(antechamber);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caves;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyCaveType(ArrayList<CaveTypeEntry> caveType) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();;
		try {
			caves = connector.getCavesbyCaveType(caveType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caves;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyDistrict(ArrayList<DistrictEntry> districts) throws IllegalArgumentException {
		MysqlConnector connector =MysqlConnector.getInstance();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		try {
			caves = connector.getCavesbyDistrict(districts);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return caves;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyDepiction(ArrayList<DepictionEntry> depictions){
	MysqlConnector connector =MysqlConnector.getInstance();
	ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
	try {
		caves = connector.getCavesbyDepiction(depictions);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return caves;
	}

	@Override
	public ArrayList<CaveEntry> getCavesbyOrnaments(ArrayList<OrnamentEntry> ornaments){
	MysqlConnector connector =MysqlConnector.getInstance();
	ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();;
	try {
		caves = connector.getCavesbyOrnament(ornaments);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return caves;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCavesbyDistrictID(int)
	 */
	@Override
	public ArrayList<CaveEntry> getCavesbyDistrictID(int DistrictID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCavesbyDistrictID(DistrictID);
	}

}