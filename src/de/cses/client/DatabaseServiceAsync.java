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
import java.util.Map;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;

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

public interface DatabaseServiceAsync {

	void getDistricts(AsyncCallback<ArrayList<DistrictEntry>> callback) throws IllegalArgumentException;

	void getImage(int imageID, AsyncCallback<ImageEntry> callback) throws IllegalArgumentException;

	void getPublicationTypes(AsyncCallback<ArrayList<PublicationTypeEntry>> callback) throws IllegalArgumentException;

	void getImages(AsyncCallback<ArrayList<ImageEntry>> callback) throws IllegalArgumentException;

	void getImages(String sqlWhere, AsyncCallback<ArrayList<ImageEntry>> callback) throws IllegalArgumentException;

	void getPhotographer(AsyncCallback<ArrayList<PhotographerEntry>> callback) throws IllegalArgumentException;

	void getPics(ArrayList<ImageEntry> imgSources, int tnSize, String sessionID, AsyncCallback <Map<String,String>> callback) throws IllegalArgumentException;

	void getPicsByImageID(String imgSourceIds, int tnSize, String sessionID, AsyncCallback <Map<Integer,String>> callback) throws IllegalArgumentException;

	void getCaves(AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;

	void getCaves(String sqlWhere, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;

	void getCavesbyDistrictID(int DistrictID, AsyncCallback<ArrayList<CaveEntry>> callback) throws IllegalArgumentException;

	void getOrnaments(AsyncCallback<ArrayList<OrnamentEntry>> callback) throws IllegalArgumentException;

	void getOrnamentEntry(int OrnamentID, AsyncCallback<OrnamentEntry> callback) throws IllegalArgumentException;

	void getAnnotatedBiblographybyID(int bibid, AsyncCallback<AnnotatedBibliographyEntry> callback) throws IllegalArgumentException;

	void getOrnamentsOfOtherCultures(AsyncCallback<ArrayList<OrnamentOfOtherCulturesEntry>> callback) throws IllegalArgumentException;

	/**
	 * Executes an SQL update using a pre-defined SQL UPDATE string
	 * 
	 * @param sqlUpdate
	 * @param callback
	 * @throws IllegalArgumentException
	 */
	void updateEntry(String sqlUpdate, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	/**
	 * Executes a SQL delete using a predefined SQL DELETE string
	 * 
	 * @param sqlDelete
	 * @param callback
	 * @throws IllegalArgumentException
	 */
	void deleteEntry(String sqlDelete, AsyncCallback<Boolean> callback) throws IllegalArgumentException;

	void saveOrnamentEntry(OrnamentEntry ornamentEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	/**
	 * Executes a pre-defined SQL INSERT statement and returns the generated (auto-increment) unique key from the table.
	 * 
	 * @param sqlInsert
	 * @param callback
	 * @throws IllegalArgumentException
	 */
	// void insertEntry(String sqlInsert, AsyncCallback<Integer> callback) throws IllegalArgumentException;

//	void getDepictions(AsyncCallback<ArrayList<DepictionEntry>> callback) throws IllegalArgumentException;

	void getIconography(AsyncCallback<ArrayList<IconographyEntry>> callback) throws IllegalArgumentException;


	void getCaveTypebyID(int caveTypeID, AsyncCallback<CaveTypeEntry> callback) throws IllegalArgumentException;

	void getCaveTypes(AsyncCallback<ArrayList<CaveTypeEntry>> callback) throws IllegalArgumentException;

//	void getDepictionEntry(int depictionID, AsyncCallback<DepictionEntry> callback);

	void getVendors(AsyncCallback<ArrayList<VendorEntry>> callback) throws IllegalArgumentException;

	void getStyles(AsyncCallback<ArrayList<StyleEntry>> callback) throws IllegalArgumentException;

	void getExpeditions(AsyncCallback<ArrayList<ExpeditionEntry>> asyncCallback) throws IllegalArgumentException;

	void iconographyIDisUsed(int iconographyID, int OrnamentID, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void getPublicationEntry(int id, AsyncCallback<PublicationEntry> asyncCallback) throws IllegalArgumentException;

	void getAuthorEntry(int id, AsyncCallback<AuthorEntry> asyncCallback) throws IllegalArgumentException;

	@Deprecated
	void getMasterImageEntryForDepiction(int depictionID, AsyncCallback<ImageEntry> asyncCallback) throws IllegalArgumentException;

	void getCaveEntry(int id, AsyncCallback<CaveEntry> asyncCallback) throws IllegalArgumentException;

	// void getAntechamberEntry(int publicationTypeID, AsyncCallback<AntechamberEntry> asyncCallback) throws IllegalArgumentException;
	//
	// void getMainChamberEntry(int publicationTypeID, AsyncCallback<MainChamberEntry> asyncCallback) throws IllegalArgumentException;
	//
	// void getRearAreaEntry(int publicationTypeID, AsyncCallback<RearAreaEntry> asyncCallback) throws IllegalArgumentException;

	void getRegions(AsyncCallback<ArrayList<RegionEntry>> asyncCallback) throws IllegalArgumentException;

	void getSites(AsyncCallback<ArrayList<SiteEntry>> asyncCallback) throws IllegalArgumentException;

	void getSite(int id, AsyncCallback<SiteEntry> asyncCallback) throws IllegalArgumentException;

//	void getDepictions(String sqlWhere, AsyncCallback<ArrayList<DepictionEntry>> asyncCallback) throws IllegalArgumentException;

	void getDepictionsbyWallID(int wallID, AsyncCallback<ArrayList<DepictionEntry>> asyncCallback) throws IllegalArgumentException;

	void saveDepiction(int depictionID, int absoluteLeft, int absoluteTop, AsyncCallback<String> asyncCallback)
			throws IllegalArgumentException;

//	void getRelatedImages(int depictionID, AsyncCallback<ArrayList<ImageEntry>> asyncCallback) throws IllegalArgumentException;

	void getOrientationInformation(AsyncCallback<ArrayList<OrientationEntry>> asyncCallback) throws IllegalArgumentException;

	void getIconographyEntry(int iconographyID, AsyncCallback<IconographyEntry> asyncCallback) throws IllegalArgumentException;

	//void getOrientations(AsyncCallback<ArrayList<OrientationEntry>> asyncCallback) throws IllegalArgumentException;

	void getMainTypologicalClasses(AsyncCallback<ArrayList<MainTypologicalClass>> asyncCallback) throws IllegalArgumentException;

	void getStructureOrganizations(AsyncCallback<ArrayList<StructureOrganization>> asyncCallback) throws IllegalArgumentException;

	void getOrnamentCaveTypes(AsyncCallback<ArrayList<OrnamentCaveType>> asyncCallback) throws IllegalArgumentException;

	void getCaveAreas(int caveID, AsyncCallback<ArrayList<CaveAreaEntry>> asyncCallback) throws IllegalArgumentException;

	void getWalls(AsyncCallback<ArrayList<WallEntry>> asyncCallback) throws IllegalArgumentException;

//	void getWall(int caveID, String locationLabel, AsyncCallback<WallEntry> callback);

	void getOrnamentPositions(AsyncCallback<ArrayList<OrnamentPositionEntry>> asyncCallback) throws IllegalArgumentException;

	void getOrnamentFunctions(AsyncCallback<ArrayList<OrnamentFunctionEntry>> asyncCallback) throws IllegalArgumentException;

	void getOrnamentsWHERE(String sqlWhere, AsyncCallback<ArrayList<OrnamentEntry>> asyncCallback) throws IllegalArgumentException;

	void getCeilingTypes(AsyncCallback<ArrayList<CeilingTypeEntry>> asyncCallback) throws IllegalArgumentException;

	void getPreservationClassifications(AsyncCallback<ArrayList<PreservationClassificationEntry>> asyncCallback)
			throws IllegalArgumentException;

	void updateCaveEntry(CaveEntry caveEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void insertCaveEntry(CaveEntry caveEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void getCaveGroups(AsyncCallback<ArrayList<CaveGroupEntry>> asyncCallback) throws IllegalArgumentException;

	void userLogin(String username, String password, AsyncCallback<UserEntry> asyncCallback);

	void updateImageEntry(ImageEntry imgEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void getImageTypes(AsyncCallback<ArrayList<ImageTypeEntry>> asyncCallback) throws IllegalArgumentException;

	void insertDepictionEntry(DepictionEntry depictionEntry, ArrayList<IconographyEntry> iconographyList, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void updateDepictionEntry(DepictionEntry correspondingDepictionEntry, ArrayList<IconographyEntry> iconographyList, AsyncCallback<Boolean> asyncCallback)
			throws IllegalArgumentException;

	void getModesOfRepresentation(AsyncCallback<ArrayList<ModeOfRepresentationEntry>> asyncCallback) throws IllegalArgumentException;

	void getWalls(int caveID, AsyncCallback<ArrayList<WallEntry>> callback) throws IllegalArgumentException;

	void getWallLocations(AsyncCallback<ArrayList<WallLocationEntry>> asyncCallback) throws IllegalArgumentException;

	/**
	 * @param currentAuthorEntry
	 * @param asyncCallback
	 */
	void updateAuthorEntry(AuthorEntry currentAuthorEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void insertAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry, AsyncCallback<AnnotatedBibliographyEntry> callback);

	void getAuthors(AsyncCallback<ArrayList<AuthorEntry>> callback) throws IllegalArgumentException;// ?

	void getPublishers(AsyncCallback<ArrayList<PublisherEntry>> callback) throws IllegalArgumentException;

	void getAnnotatedBibliography(AsyncCallback<ArrayList<AnnotatedBibliographyEntry>> callback) throws IllegalArgumentException;

	void insertPhotographerEntry(PhotographerEntry photographerEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertCaveGroupEntry(CaveGroupEntry cgEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertDistrictEntry(DistrictEntry de, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertRegionEntry(RegionEntry re, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertAuthorEntry(AuthorEntry currentAuthorEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertCeilingTypeEntry(CeilingTypeEntry ctEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	@Deprecated
	void getCurrentLocations(AsyncCallback<ArrayList<CurrentLocationEntry>> asyncCallback) throws IllegalArgumentException;

	void getRelatedIconography(int depictionID, AsyncCallback<ArrayList<IconographyEntry>> asyncCallback) throws IllegalArgumentException;

	void getLocations(AsyncCallback<ArrayList<LocationEntry>> asyncCallback) throws IllegalArgumentException;

	void insertVendorEntry(VendorEntry vEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void insertLocationEntry(LocationEntry lEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void checkSessionID(String sessionID, String username, AsyncCallback<UserEntry> asyncCallback);

	void getInnerSecondaryPatterns( AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>> callback) throws IllegalArgumentException;

	void getOrnamentComponents( AsyncCallback<ArrayList<OrnamentComponentsEntry>> callback) throws IllegalArgumentException;
	
	void getOrnamentClass( AsyncCallback<ArrayList<OrnamentClassEntry>> callback) throws IllegalArgumentException;

	void addOrnamentComponent (OrnamentComponentsEntry entry, AsyncCallback<OrnamentComponentsEntry> asyncCallback) throws IllegalArgumentException;
	
	void addOrnamentClass (OrnamentClassEntry entry, AsyncCallback<OrnamentClassEntry> asyncCallback) throws IllegalArgumentException;
	
	void renameOrnamentClass (OrnamentClassEntry entry, AsyncCallback<OrnamentClassEntry> asyncCallback) throws IllegalArgumentException;
	
	void renameOrnamentComponents (OrnamentComponentsEntry entry, AsyncCallback<OrnamentComponentsEntry> asyncCallback) throws IllegalArgumentException;
	
	void addInnerSecondaryPatterns (InnerSecondaryPatternsEntry entry, AsyncCallback<InnerSecondaryPatternsEntry> asyncCallback) throws IllegalArgumentException;

	void getPreservationAttributes(AsyncCallback<ArrayList<PreservationAttributeEntry>> asyncCallback) throws IllegalArgumentException;

	void insertPreservationAttributeEntry(PreservationAttributeEntry paEntry, AsyncCallback<Integer> asyncCallback)
			throws IllegalArgumentException;

	void insertPublisherEntry(PublisherEntry publisherEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void getAnnotatedBibliography(String sqlWhere, AsyncCallback<ArrayList<AnnotatedBibliographyEntry>> asyncCallback) throws IllegalArgumentException;

	void updateAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry, AsyncCallback<AnnotatedBibliographyEntry> asyncCallback) throws IllegalArgumentException;
	
	void getPositionbyWall(WallEntry wall, AsyncCallback<ArrayList<OrnamentPositionEntry>> asyncCallback) throws IllegalArgumentException;
	
	void getPositionbyReveal(WallEntry wall, AsyncCallback<ArrayList<OrnamentPositionEntry>> asyncCallback) throws IllegalArgumentException;


	void getFunctionbyPosition(OrnamentPositionEntry position, AsyncCallback<ArrayList<OrnamentFunctionEntry>> asyncCallback) throws IllegalArgumentException;

	void getPositionbyCeiling(int ceiling1, int ceiling2, AsyncCallback<ArrayList<OrnamentPositionEntry>> asyncCallback) throws IllegalArgumentException;

	void getDepictionFromIconography(String sqlWhere, AsyncCallback<ArrayList<Integer>> asyncCallback) throws IllegalArgumentException;

//	void getRelatedDepictionIDs(String iconographyIDs, int correlationFactor, AsyncCallback<ArrayList<Integer>> asyncCallback) throws IllegalArgumentException;

	void getIconography(int rootIndex, AsyncCallback<ArrayList<IconographyEntry>> asyncCallback) throws IllegalArgumentException;

	void doLogging(String usertag, String message, AsyncCallback asyncCallback) throws IllegalArgumentException;

	void updateOrnamentEntry(OrnamentEntry oEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void getAnnotatedBibliographyFromAuthors(ArrayList<AuthorEntry> authorList, AsyncCallback<ArrayList<AnnotatedBibliographyEntry>> asyncCallback) throws IllegalArgumentException;

	void getBibKeywords(AsyncCallback<ArrayList<BibKeywordEntry>> asyncCallback) throws IllegalArgumentException;

	void insertBibKeyword(BibKeywordEntry bkEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void deleteAuthorEntry(AuthorEntry selectedEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void updateUserEntry(UserEntry currentUser, String passwordHash, String newPasswordHash, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void searchCaves(CaveSearchEntry searchEntry, AsyncCallback<ArrayList<CaveEntry>> asyncCallback) throws IllegalArgumentException;

	void searchDepictions(DepictionSearchEntry searchEntry, AsyncCallback<ArrayList<DepictionEntry>> asyncCallback) throws IllegalArgumentException;

	void searchImages(ImageSearchEntry searchEntry, AsyncCallback<ArrayList<ImageEntry>> asyncCallback) throws IllegalArgumentException;

	void searchAnnotatedBibliography(AnnotatedBibliographySearchEntry searchEntry, AsyncCallback<ArrayList<AnnotatedBibliographyEntry>> asyncCallback) throws IllegalArgumentException;

	void searchOrnaments(OrnamenticSearchEntry searchEntry, AsyncCallback<ArrayList<OrnamentEntry>> asyncCallback) throws IllegalArgumentException;

	void insertIconographyEntry(IconographyEntry iconographyEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;
	
	void updateIconographyEntry(IconographyEntry iconographyEntryToEdit, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void getUsers(AsyncCallback<ArrayList<UserEntry>> asyncCallback) throws IllegalArgumentException;

	void updateUserEntry(UserEntry userEntry, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void insertUserEntry(UserEntry entry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

	void saveCollectedEntries(String sessionID, String collectionLabel, Boolean isGroupCollection, ArrayList<AbstractEntry> entryList, AsyncCallback<Boolean> asyncCallback) throws IllegalArgumentException;

	void getRelatedCollectionNames(String sessionID, AsyncCallback<ArrayList<CollectionEntry>> asyncCallback) throws IllegalArgumentException;

	void loadCollectedEntries(CollectionEntry value, AsyncCallback<ArrayList<AbstractEntry>> asyncCallback) throws IllegalArgumentException;

	void addPreservationClassification(PreservationClassificationEntry pcEntry, AsyncCallback<Integer> asyncCallback) throws IllegalArgumentException;

}
