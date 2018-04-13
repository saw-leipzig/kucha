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
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CavePart;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.CurrentLocationEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
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
import de.cses.shared.PhotographerEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.PublicationEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StructureOrganization;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallLocationEntry;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

	public ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<DistrictEntry> districts = connector.getDistricts();
		return districts;
	}
	public ArrayList<AnnotatedBiblographyEntry> getAnnotatedBiblography() throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<AnnotatedBiblographyEntry> biblography = connector.getAnnotatedBiblography();
		return biblography;
	}
	
	public AnnotatedBiblographyEntry getAnnotatedBiblographybyID(int id) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		AnnotatedBiblographyEntry biblographyEntry = connector.getAnnotatedBiblographybyID(id);
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
	
	public ArrayList<OrientationEntry> getOrientations() throws IllegalArgumentException {
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
		return connector.getPictorialElements(2005); // start with the 5th element subtree
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

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#userLogin(java.lang.String, java.lang.String)
	 */
	@Override
	public String userLogin(String username, String password) throws IllegalArgumentException {
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

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#getWall(int, int)
	 */
	@Override
	public WallEntry getWall(int caveID, String locationLabel) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getWall(caveID, locationLabel);
	}

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
	public int insertAnnotatedBiblographyEntry(AnnotatedBiblographyEntry bibEntry) throws IllegalArgumentException {
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
	
	public ArrayList<AnnotatedBiblographyEntry> getAnnotatedBibliography() throws IllegalArgumentException {
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
	public String checkSessionID(String sessionID) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.checkSessionID(sessionID);
	}
	/* (non-Javadoc)
<<<<<<< HEAD
	 * @see de.cses.client.DatabaseService#getInnerSecondaryPatterns()
	 */
	@Override
	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() throws IllegalArgumentException {
		// TODO Auto-generated method stub
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
	public ArrayList<AnnotatedBiblographyEntry> getAnnotatedBibliography(String sqlWhere) throws IllegalArgumentException {
		MysqlConnector connector = MysqlConnector.getInstance();
		return connector.getAnnotatedBibliography(sqlWhere);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.DatabaseService#updateAnnotatedBiblographyEntry(de.cses.shared.AnnotatedBiblographyEntry)
	 */
	@Override
	public boolean updateAnnotatedBiblographyEntry(AnnotatedBiblographyEntry bibEntry) throws IllegalArgumentException {
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
}
