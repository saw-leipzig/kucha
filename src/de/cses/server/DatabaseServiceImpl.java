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

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.cses.client.DatabaseService;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.RearAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CavePart;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.MainChamberEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentFunction;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.OrnamentOrientation;
import de.cses.shared.OrnamentPosition;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.PublicationEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StructureOrganization;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallEntry;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

	public ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<DistrictEntry> districts = connector.getDistricts();
		;
		return districts;
	}

	public ImageEntry getImage(int imageID) throws IllegalArgumentException {
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
	 * A universal SQL UPDATE call for the Kucha database The String needs to
	 * contain the full sql command, including the UPDATE statement at the
	 * beginning!
	 */
	public boolean updateEntry(String sqlUpdate) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateEntry(sqlUpdate);
	}

	/**
	 * A universal SQL DELETE call for the Kucha database The String needs to
	 * contain the full sql command, including the DELETE statement at the
	 * beginning!
	 */
	public boolean deleteEntry(String sqlDelete) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.deleteEntry(sqlDelete);
	}

	/**
	 * A universal SQL INSERT call for the Kucha database The String needs to
	 * contain the full sql command, including the INSERT statement at the
	 * beginning!
	 * 
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
	public ArrayList<CaveEntry> getCavesbyDistrictID(int DistrictID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCavesbyDistrictID(DistrictID);
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
		// this.getThreadLocalRequest().getAttribute("user");
	}

	@Override
	public ArrayList<IconographyEntry> getIconography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getIconography();
	}

	@Override
	public ArrayList<PictorialElementEntry> getPictorialElements() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPictorialElements(0); // start with the absolute root elements
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.DatabaseService#getPublicationEntry(int)
	 */
	@Override
	public PublicationEntry getPublicationEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPublicationEntry(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.DatabaseService#getAuthorEntry(int)
	 */
	@Override
	public AuthorEntry getAuthorEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAuthorEntry(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.DatabaseService#getMasterImageEntryForDepiction(int)
	 */
	@Override
	public ImageEntry getMasterImageEntryForDepiction(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		int relatedImageID = connector.getRelatedMasterImageID(depictionID);
		return connector.getImageEntry(relatedImageID);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.DatabaseService#getCaveEntry(int)
	 */
	@Override
	public CaveEntry getCaveEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCave(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.DatabaseService#getAntechamberEntry(int)
	 */
	@Override
	public AntechamberEntry getAntechamberEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAntechamberEntry(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getMainChamberEntry(int)
	 */
	@Override
	public MainChamberEntry getMainChamberEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getMainChamber(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRearAreaEntry(int)
	 */
	@Override
	public RearAreaEntry getRearAreaEntry(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRearArea(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRegions()
	 */
	@Override
	public ArrayList<RegionEntry> getRegions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRegions();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getSites()
	 */
	@Override
	public ArrayList<SiteEntry> getSites() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getSites();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getSite(int)
	 */
	@Override
	public SiteEntry getSite(int id) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getSite(id);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCaves(java.lang.String)
	 */
	@Override
	public ArrayList<CaveEntry> getCaves(String sqlWhere) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaves(sqlWhere);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getDepictions(java.lang.String)
	 */
	@Override
	public ArrayList<DepictionEntry> getDepictions(String sqlWhere) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getDepictions(sqlWhere);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getDepictionsbyWallID(int)
	 */
	@Override
	public ArrayList<DepictionEntry> getDepictionsbyWallID(int wallID) {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<DepictionEntry> depictions = connector.getAllDepictionsbyWall(wallID);
		return depictions;
	}
	public String saveDepiction(int depictionID, int absoluteLeft, int absoluteTop){
		MysqlConnector connector = MysqlConnector.getInstance();
		String saved = connector.saveDepiction(depictionID, absoluteLeft, absoluteTop);
		return saved;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRelatedImages(int)
	 */
	@Override
	public ArrayList<ImageEntry> getRelatedImages(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRelatedImages(depictionID);
	}
	
	public ArrayList<OrnamentOrientation> getOrientations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrientations();
	}
	public ArrayList<MainTypologicalClass> getMainTypologicalClasses() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getMainTypologicalClass();
	}
	
	public ArrayList<StructureOrganization> getStructureOrganizations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getStructureOrganizations();
	}
	
	public ArrayList<CavePart> getCaveParts() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveParts();
	}
	
	public ArrayList<OrnamentFunction> getOrnamentFunctions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentFunction();
	}
	
	public ArrayList<OrnamentPosition> getOrnamentPositions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentPosition();
	}

	public ArrayList<OrnamentCaveType> getOrnamentCaveTypes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentCaveTypes();
	}
	
	public ArrayList<WallEntry> getWalls() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getWalls();
	}
	public ArrayList<OrnamentEntry> getOrnamentsWHERE(String sqlWhere) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentsWHERE(sqlWhere);
	}
	public ArrayList<PictorialElementEntry> getPictorialElementsObjects() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPictorialElements(5); // start with the 5th element subtree
	}


	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getOrientationInformation()
	 */
	@Override
	public ArrayList<OrientationEntry> getOrientationInformation() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrientationInformation();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getIconographyEntry(int)
	 */
	@Override
	public IconographyEntry getIconographyEntry(int iconographyID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getIconographyEntry(iconographyID);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRelatedPictorialElements(int)
	 */
	@Override
	public ArrayList<PictorialElementEntry> getRelatedPE(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRelatedPE(depictionID);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCeilingTypes()
	 */
	@Override
	public ArrayList<CeilingTypeEntry> getCeilingTypes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCeilingTypes();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getPreservationClassifications()
	 */
	@Override
	public ArrayList<PreservationClassificationEntry> getPreservationClassifications() {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPreservationClassifications();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateCaveEntry(de.cses.shared.CaveEntry)
	 */
	@Override
	public boolean updateCaveEntry(CaveEntry caveEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateCaveEntry(caveEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertCaveEntry(de.cses.shared.CaveEntry)
	 */
	@Override
	public int insertCaveEntry(CaveEntry caveEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertCaveEntry(caveEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCaveGroups()
	 */
	@Override
	public ArrayList<CaveGroupEntry> getCaveGroups() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveGroups();
	}

}
