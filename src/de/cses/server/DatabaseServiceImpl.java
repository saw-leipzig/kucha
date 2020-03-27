/*
 * Copyright 2016 - 2019
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
import java.util.Map;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.cses.client.DatabaseService;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.BibKeywordEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.CollectionEntry;
import de.cses.shared.CurrentLocationEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageSearchEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.ModeOfRepresentationEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.OrnamenticSearchEntry;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.PublicationEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StructureOrganization;
import de.cses.shared.StyleEntry;
import de.cses.shared.UserEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallLocationEntry;
import de.cses.shared.WallTreeEntry;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {
	@Override
	public boolean iconographyIDisUsed(int iconographyID, int OrnamentID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.iconographyIDisUsed(iconographyID, OrnamentID);
	}
	public boolean resetPassword(UserEntry currentUser) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.resetPassword(currentUser);
	}
	
	public ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<DistrictEntry> districts = connector.getDistricts();
		return districts;
	}
	public ArrayList<WallTreeEntry> getWallTreeEntriesByIconographyID(int IconographyID, String SessionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<WallTreeEntry> districts = connector.getWallTreeEntriesByIconographyID(IconographyID, SessionID);
		return districts;
	}
	
	public Map<Integer,String> getMasterImageFromOrnament(int tnSize,String sessionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		Map<Integer,String> result = connector.getMasterImageFromOrnament(tnSize,sessionID);
		return result;
	}

	public Map<Integer,String> getPics(ArrayList<ImageEntry> imgSources, int tnSize, String sessionID) {
		MysqlConnector connector = MysqlConnector.getInstance();
		Map<Integer,String>  districts = connector.getPics(imgSources, tnSize, sessionID);
		return districts;
	}
	public boolean isHan(String title) {
		MysqlConnector connector = MysqlConnector.getInstance();
		boolean  hasHan = connector.isHan(title);
		return hasHan;
	}
	public Map<Integer,String> getPicsByImageID(String imgSourceIds, int tnSize, String sessionID) {
		MysqlConnector connector = MysqlConnector.getInstance();
		Map<Integer,String>  districts = connector.getPicsByImageID(imgSourceIds, tnSize, sessionID);
		return districts;
	}
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBiblography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<AnnotatedBibliographyEntry> biblography = connector.getAnnotatedBiblography();
		return biblography;
	}
	
	public AnnotatedBibliographyEntry getAnnotatedBiblographybyID(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		AnnotatedBibliographyEntry biblographyEntry = connector.getAnnotatedBiblographybyID(id, null);
		return biblographyEntry;
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

	public ArrayList<PublicationTypeEntry> getPublicationTypes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPublicationTypes();
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

//	/**
//	 * A universal SQL INSERT call for the Kucha database The String needs to
//	 * contain the full sql command, including the INSERT statement at the
//	 * beginning!
//	 * 
//	 * @param sqlInsert
//	 * @return
//	 */
//	public int insertEntry(String sqlInsert) {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.insertEntry(sqlInsert);
//	}

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
	public int saveOrnamentEntry(OrnamentEntry ornamentEntry) throws IllegalArgumentException {
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

//	@Override
//	public ArrayList<DepictionEntry> getDepictions() throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getDepictions();
//	}

//	@Override
//	public DepictionEntry getDepictionEntry(int depictionID) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getDepictionEntry(depictionID);
//		// this.getThreadLocalRequest().getAttribute("user");
//	}

	@Override
	public ArrayList<IconographyEntry> getIconography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getIconography(0); // root index =0 so we load them all
	}

	@Override
	public ArrayList<WallTreeEntry> getWallTree() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getWallTree(0); // root index =0 so we load them all
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
	@Deprecated
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

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see de.cses.client.DatabaseService#getAntechamberEntry(int)
//	 */
//	@Override
//	public AntechamberEntry getAntechamberEntry(int publicationTypeID) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getAntechamberEntry(publicationTypeID);
//	}
//
//	/* (non-Javadoc)
//	 * @see de.cses.client.DatabaseService#getMainChamberEntry(int)
//	 */
//	@Override
//	public MainChamberEntry getMainChamberEntry(int publicationTypeID) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getMainChamber(publicationTypeID);
//	}
//
//	/* (non-Javadoc)
//	 * @see de.cses.client.DatabaseService#getRearAreaEntry(int)
//	 */
//	@Override
//	public RearAreaEntry getRearAreaEntry(int publicationTypeID) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getRearArea(publicationTypeID);
//	}

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
//	@Override
//	public ArrayList<DepictionEntry> getDepictions(String sqlWhere) {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getDepictions(sqlWhere);
//	}

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
//	public ArrayList<ImageEntry> getRelatedImages(int depictionID) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getRelatedImages(depictionID);
//	}
	
	/*public ArrayList<OrientationEntry> getOrientations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrientations();
	}
	*/
	public ArrayList<MainTypologicalClass> getMainTypologicalClasses() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getMainTypologicalClass();
	}
	
	public ArrayList<StructureOrganization> getStructureOrganizations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getStructureOrganizations();
	}
	
	public ArrayList<CaveAreaEntry> getCaveAreas(int caveID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveAreas(caveID);
	}
	
	public ArrayList<OrnamentFunctionEntry> getOrnamentFunctions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentFunction();
	}
	
	public ArrayList<OrnamentPositionEntry> getOrnamentPositions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentPosition();
	}

	public ArrayList<PositionEntry> getPositions() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPosition();
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
		return connector.getOrnamentsWhere(sqlWhere);
	}
	public OrnamentEntry getOrnamentEntry(int OrnamentId) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentEntry(OrnamentId);
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
	@Override
	public boolean deleteAbstractEntry(AbstractEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.deleteAbstractEntry(entry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCaveGroups()
	 */
	@Override
	public ArrayList<CaveGroupEntry> getCaveGroups() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCaveGroups();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#userLogin(java.lang.String, java.lang.String)
	 */
	@Override
	public UserEntry userLogin(String username, String password) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.userLogin(username, password);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateImageEntry(de.cses.shared.ImageEntry)
	 */
	@Override
	public boolean updateImageEntry(ImageEntry imgEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateImageEntry(imgEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getImageTypes()
	 */
	@Override
	public ArrayList<ImageTypeEntry> getImageTypes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getImageTypes();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertDepictionEntry(de.cses.shared.DepictionEntry, java.util.List)
	 */
	@Override
	public int insertDepictionEntry(DepictionEntry depictionEntry, ArrayList<IconographyEntry> iconographyLists) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertDepictionEntry(depictionEntry, iconographyLists);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateDepictionEntry(de.cses.shared.DepictionEntry, java.util.List, java.util.List)
	 */
	@Override
	public boolean updateDepictionEntry(DepictionEntry correspondingDepictionEntry, ArrayList<IconographyEntry> iconographyList) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateDepictionEntry(correspondingDepictionEntry, iconographyList);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getModesOfRepresentation()
	 */
	@Override
	public ArrayList<ModeOfRepresentationEntry> getModesOfRepresentation() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getModesOfRepresentations();
	}

//	/* (non-Javadoc)
//	 * @see de.cses.client.DatabaseService#getWall(int, int)
//	 */
//	@Override
//	public WallEntry getWall(int caveID, String locationLabel) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		return connector.getWall(caveID, locationLabel);
//	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getWalls(int)
	 */
	@Override
	public ArrayList<WallEntry> getWalls(int caveID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getWalls(caveID);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getWallLocations()
	 */
	@Override
	public ArrayList<WallLocationEntry> getWallLocations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getWallLocations();
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateAuthorEntry(de.cses.shared.AuthorEntry)
	 */
	@Override
	public boolean updateAuthorEntry(AuthorEntry currentAuthorEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateAuthorEntry(currentAuthorEntry);
	}
	
	@Override
	public int insertAuthorEntry(AuthorEntry currentAuthorEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertAuthorEntry(currentAuthorEntry);
	}

	@Override
	public AnnotatedBibliographyEntry insertAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertAnnotatedBiblographyEntry(bibEntry);
	}
	
	public ArrayList<PublisherEntry> getPublishers() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPublishers();
	}

	public ArrayList<AuthorEntry> getAuthors() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAuthors();
	}
	
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAnnotatedBiblography();
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#writePhotographerEntry(de.cses.shared.PhotographerEntry)
	 */
	@Override
	public int insertPhotographerEntry(PhotographerEntry photographerEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertPhotographerEntry(photographerEntry);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertCaveGroupEntry(de.cses.shared.CaveGroupEntry)
	 */
	@Override
	public int insertCaveGroupEntry(CaveGroupEntry cgEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertCaveGroupEntry(cgEntry);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertDistrictEntry(de.cses.shared.DistrictEntry)
	 */
	@Override
	public int insertDistrictEntry(DistrictEntry de) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertDistrictEntry(de);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertRegionEntry(de.cses.shared.RegionEntry)
	 */
	@Override
	public int insertRegionEntry(RegionEntry re) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertRegionEntry(re);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertCeilingTypeEntry(de.cses.shared.CeilingTypeEntry)
	 */
	@Override
	public int insertCeilingTypeEntry(CeilingTypeEntry ctEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertCeilingTypeEntry(ctEntry);
	}
	
	@Deprecated
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getCurrentLocations()
	 */
	@Override
	public ArrayList<CurrentLocationEntry> getCurrentLocations() {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getCurrentLocations();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRelatedIconography(int)
	 */
	@Override
	public ArrayList<IconographyEntry> getRelatedIconography(int depictionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRelatedIconography(depictionID);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getLocations()
	 */
	@Override
	public ArrayList<LocationEntry> getLocations() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getLocations();
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertVendorEntry(de.cses.shared.VendorEntry)
	 */
	@Override
	public int insertVendorEntry(VendorEntry vEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.inserVendorEntry(vEntry);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertLocationEntry(de.cses.shared.LocationEntry)
	 */
	@Override
	public int insertLocationEntry(LocationEntry lEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertLocationEntry(lEntry);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#checkSessionID(java.lang.String)
	 */
	@Override
	public UserEntry checkSessionID(String sessionID, String username) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.checkSessionID(sessionID, username);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getInnerSecondaryPatterns()
	 */
	@Override
	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getInnerSecondaryPatterns();
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getOrnamentComponents()
	 */
	@Override
	public ArrayList<OrnamentComponentsEntry> getOrnamentComponents() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentComponents();
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getOrnamentClass()
	 */
	@Override
	public ArrayList<OrnamentClassEntry> getOrnamentClass() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getOrnamentClass();
	}
	
	public OrnamentClassEntry addOrnamentClass(OrnamentClassEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.addOrnamentClass(entry);
	}
	
	public InnerSecondaryPatternsEntry addInnerSecondaryPatterns (InnerSecondaryPatternsEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.addInnerSecondaryPatterns(entry);
	}
	
	public OrnamentComponentsEntry addOrnamentComponent (OrnamentComponentsEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.addOrnamentComponents(entry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getPreservationAttributes()
	 */
	@Override
	public ArrayList<PreservationAttributeEntry> getPreservationAttributes() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPreservationAttributes();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertPreservationAttributeEntry(de.cses.shared.PreservationAttributeEntry)
	 */
	@Override
	public int insertPreservationAttributeEntry(PreservationAttributeEntry paEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertPreservationAttributeEntry(paEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertPublisherEntry(de.cses.shared.PublisherEntry)
	 */
	@Override
	public int insertPublisherEntry(PublisherEntry publisherEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertPublisherEntry(publisherEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getAnnotatedBibliography(java.lang.String)
	 */
	@Override
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography(String sqlWhere) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAnnotatedBibliography(sqlWhere);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateAnnotatedBiblographyEntry(de.cses.shared.AnnotatedBibliographyEntry)
	 */
	@Override
	public AnnotatedBibliographyEntry updateAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateAnnotatedBiblographyEntry(bibEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getIconography(java.lang.String)
	 */
	@Override
	public ArrayList<Integer> getDepictionFromIconography(String sqlWhere) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getDepictionFromIconography(sqlWhere);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getPositionbyWall(de.cses.shared.WallEntry)
	 */
	@Override
	public ArrayList<OrnamentPositionEntry> getPositionbyWall(WallEntry wall) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPositionbyWall(wall);
	}
	public ArrayList<OrnamentPositionEntry> getPositionbyReveal(WallEntry wall) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPositionbyReveal(wall);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getFunctionbyPosition(de.cses.shared.OrnamentPositionEntry)
	 */
	@Override
	public ArrayList<OrnamentFunctionEntry> getFunctionbyPosition(OrnamentPositionEntry position) throws IllegalArgumentException {
	MysqlConnector connector = MysqlConnector.getInstance();
	return connector.getFunctionbyPosition(position);
	}

	public ArrayList<OrnamentPositionEntry> getPositionbyCeiling(int ceiling1, int ceiling2) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getPositionbyCeilingTypes(ceiling1, ceiling2);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getRelatedDepictions(java.lang.String)
	 */
//	@Override
//	public ArrayList<Integer> getRelatedDepictionIDs(String iconographyIDs, int correlationFactor) throws IllegalArgumentException {
//		MysqlConnector connector = MysqlConnector.getInstance();
//		ArrayList<Integer> resultList = new ArrayList<Integer>();
//		for (DepictionEntry de : connector.getRelatedDepictions(iconographyIDs, correlationFactor)) {
//			resultList.add(de.getDepictionID());
//		}
//		return resultList;
//	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getIconography(int)
	 */

	@Override
	public ArrayList<IconographyEntry> getIconography(int rootIndex) {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getIconography(rootIndex);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#doLogging(java.lang.String, java.lang.String)
	 */
	@Override
	public void doLogging(String usertag, String message) {
		MysqlConnector connector = MysqlConnector.getInstance();
		connector.doLogging(usertag, message);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateOrnamentEntry(de.cses.shared.OrnamentEntry)
	 */
	@Override
	public boolean updateOrnamentEntry(OrnamentEntry oEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateOrnamentEntry(oEntry);
	}
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getAnnotatedBibliographyFromAuthors(java.util.ArrayList)
	 */
	@Override
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliographyFromAuthors(ArrayList<AuthorEntry> authorList) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAnnotatedBibliographyFromAuthors(authorList);
	}
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getBibKeywords()
	 */
	@Override
	public ArrayList<BibKeywordEntry> getBibKeywords() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getBibKeywords();
	}
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#insertBibKeyword(de.cses.shared.BibKeywordEntry)
	 */
	@Override
	public int insertBibKeyword(BibKeywordEntry bkEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertBibKeyword(bkEntry);
	}
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#deleteAuthorEntry(de.cses.shared.AuthorEntry)
	 */
	@Override
	public boolean deleteAuthorEntry(AuthorEntry selectedEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.deleteAuthorEntry(selectedEntry);
	}
  
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#renameOrnamentClass(de.cses.shared.OrnamentClassEntry)
	 */
	@Override
	public OrnamentClassEntry renameOrnamentClass(OrnamentClassEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.renameOrnamentClass(entry);
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#renameOrnamentComponents(de.cses.shared.OrnamentComponentsEntry)
	 */
	@Override
	public OrnamentComponentsEntry renameOrnamentComponents(OrnamentComponentsEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.renameOrnamentComponents(entry);
	}

		/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateUserEntry(de.cses.shared.UserEntry)
	 */
	@Override
	public boolean updateUserEntry(UserEntry currentUser, String passwordHash, String newPasswordHash) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateUserEntry(currentUser, passwordHash, newPasswordHash);
	}
	
	@Override
	public ArrayList<CaveEntry> searchCaves(CaveSearchEntry searchEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.searchCaves(searchEntry);
	}
	
	@Override
	public ArrayList<DepictionEntry> searchDepictions(DepictionSearchEntry searchEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.searchDepictions(searchEntry);
	}
	
	@Override
	public Map<Integer,ArrayList<ImageEntry>> searchImages(ImageSearchEntry searchEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.searchImages(searchEntry);
	}
	
	@Override
	public ArrayList<AnnotatedBibliographyEntry> searchAnnotatedBibliography(AnnotatedBibliographySearchEntry searchEntry)
			throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.searchAnnotatedBibliography(searchEntry);
	}
	
	@Override
	public ArrayList<OrnamentEntry> searchOrnaments(OrnamenticSearchEntry searchEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.searchOrnaments(searchEntry);
	}
	
	@Override
	public int insertIconographyEntry(IconographyEntry iconographyEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertIconographyEntry(iconographyEntry);
	}

	@Override
	public boolean updateIconographyEntry(IconographyEntry iconographyEntryToEdit) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateIconographyEntry(iconographyEntryToEdit);
	}

	@Override
	public ArrayList<UserEntry> getUsers() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getUsers();
	}
	
	@Override
	public boolean updateUserEntry(UserEntry userEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.updateUserEntry(userEntry);
	}
	
	@Override
	public int insertUserEntry(UserEntry entry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.insertUserEntry(entry);
	}
	
	@Override
	public boolean saveCollectedEntries(String sessionID, String collectionLabel, Boolean isGroupCollection, ArrayList<AbstractEntry> entryList) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.saveCollectedEntries(sessionID, collectionLabel, isGroupCollection, entryList);
	}
	
	@Override
	public ArrayList<CollectionEntry> getRelatedCollectionNames(String sessionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getRelatedCollectionNames(sessionID);
	}
	@Override
	public ArrayList<AbstractEntry> loadCollectedEntries(CollectionEntry value) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.loadCollectedEntries(value);
	}
	@Override
	public int addPreservationClassification(PreservationClassificationEntry pcEntry) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.addPreservationClassification(pcEntry);
	}
}
