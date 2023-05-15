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
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.AnnotationEntry;
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
import de.cses.shared.ModifiedEntry;
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
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DatabaseService extends RemoteService {

//	String dbServer(String name) throws IllegalArgumentException;

	ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException;

	boolean serializeAllDepictionEntries(String sessionId) throws IllegalArgumentException;
		
	boolean resetPassword(UserEntry currentUser) throws IllegalArgumentException;
	
	boolean resetPasswordFrontEnd(UserEntry currentUser) throws IllegalArgumentException;
	
	Map<Integer,String> getMasterImageFromOrnament(int tnSize,String sessionID) throws IllegalArgumentException;

	boolean saveWallDimension(ArrayList<WallTreeEntry> pe, Integer caveID) throws IllegalArgumentException;
	
	Map<Integer,ArrayList<PositionEntry>> getWallDimension(Integer caveID) throws IllegalArgumentException;

	ArrayList<ImageEntry> getImages() throws IllegalArgumentException;

	AnnotatedBibliographyEntry getAnnotatedBiblographybyID(int bibid) throws IllegalArgumentException;
	
	ArrayList<PublicationTypeEntry> getPublicationTypes() throws IllegalArgumentException;

	ArrayList<ImageEntry> getImages(String where) throws IllegalArgumentException;

	ArrayList<PhotographerEntry> getPhotographer() throws IllegalArgumentException;

	ImageEntry getImage(int imageID) throws IllegalArgumentException;

	ArrayList<CaveEntry> getCaves() throws IllegalArgumentException;

	ArrayList<CaveEntry> getCavesbyDistrictID(int DistrictID) throws IllegalArgumentException;

	ArrayList<OrnamentEntry> getOrnaments() throws IllegalArgumentException;

	OrnamentEntry getOrnamentEntry(int OrnamentID) throws IllegalArgumentException;

	ArrayList<OrnamentOfOtherCulturesEntry> getOrnamentsOfOtherCultures() throws IllegalArgumentException;

//	ArrayList<DepictionEntry> getDepictions() throws IllegalArgumentException;

//	DepictionEntry getDepictionEntry(int depictionID) throws IllegalArgumentException;

	boolean updateEntry(String sqlUpdate) throws IllegalArgumentException;

	boolean isHan(String title) throws IllegalArgumentException;

	boolean deleteEntry(String sqlDelete) throws IllegalArgumentException;

	boolean deleteAbstractEntry(AbstractEntry entry) throws IllegalArgumentException;

	ArrayList<IconographyEntry> getIconography() throws IllegalArgumentException;

	ArrayList<WallTreeEntry> getWallTree() throws IllegalArgumentException;
	
	int saveOrnamentEntry(OrnamentEntry ornamentEntry) throws IllegalArgumentException;

	CaveTypeEntry getCaveTypebyID(int caveTypeID) throws IllegalArgumentException;

	ArrayList<CaveTypeEntry> getCaveTypes() throws IllegalArgumentException;

	ArrayList<WallTreeEntry> getWallTreeEntriesByIconographyID(int IconographyID, String SessionID) throws IllegalArgumentException;
	
	ArrayList<VendorEntry> getVendors() throws IllegalArgumentException;

	ArrayList<StyleEntry> getStyles() throws IllegalArgumentException;
	
	Map<Integer,String> getPics(ArrayList<ImageEntry> imgSources,int tnSize, String sessionID) throws IllegalArgumentException; 

	Map<Integer,String> getPicsByImageID(String imgSourceIds,int tnSize, String sessionID) throws IllegalArgumentException; 

	ArrayList<ExpeditionEntry> getExpeditions() throws IllegalArgumentException;

	PublicationEntry getPublicationEntry(int id) throws IllegalArgumentException;

	AuthorEntry getAuthorEntry(int id) throws IllegalArgumentException;
	
	boolean iconographyIDisUsed(int iconographyID, int OrnamentID) throws IllegalArgumentException;

	boolean isGoodDimension(int caveID, int register, int number) throws IllegalArgumentException;

	@Deprecated
	ImageEntry getMasterImageEntryForDepiction(int depictionID) throws IllegalArgumentException;

	CaveEntry getCaveEntry(int id) throws IllegalArgumentException;

//	AntechamberEntry getAntechamberEntry(int publicationTypeID) throws IllegalArgumentException;
//
//	MainChamberEntry getMainChamberEntry(int publicationTypeID) throws IllegalArgumentException;
//
//	RearAreaEntry getRearAreaEntry(int publicationTypeID) throws IllegalArgumentException;

	ArrayList<RegionEntry> getRegions() throws IllegalArgumentException;

	ArrayList<SiteEntry> getSites() throws IllegalArgumentException;

	SiteEntry getSite(int id)  throws IllegalArgumentException;

	ArrayList<CaveEntry> getCaves(String sqlWhere) throws IllegalArgumentException;

//	ArrayList<DepictionEntry> getDepictions(String sqlWhere) throws IllegalArgumentException;
	
	ArrayList<DepictionEntry> getDepictionsbyWallID(int wallID) throws IllegalArgumentException;
	
	String saveDepiction(int depictionID, int absoluteLeft, int absoluteTop) throws IllegalArgumentException;

//	ArrayList<ImageEntry> getRelatedImages(int depictionID) throws IllegalArgumentException;
	
	//ArrayList<OrientationEntry> getOrientations() throws IllegalArgumentException;

	ArrayList<MainTypologicalClass> getMainTypologicalClasses() throws IllegalArgumentException;
	
	ArrayList<OrnamentCaveType> getOrnamentCaveTypes() throws IllegalArgumentException;
	
	ArrayList<WallEntry> getWalls() throws IllegalArgumentException;

//	WallEntry getWall(int caveID, String locationLabel) throws IllegalArgumentException;

	ArrayList<WallEntry> getWalls(int caveID) throws IllegalArgumentException;

	ArrayList<OrnamentFunctionEntry> getOrnamentFunctions() throws IllegalArgumentException;
	
	ArrayList<OrnamentPositionEntry> getOrnamentPositions() throws IllegalArgumentException;
	
	ArrayList<PositionEntry> getPositions() throws IllegalArgumentException;
	
	ArrayList<StructureOrganization> getStructureOrganizations() throws IllegalArgumentException;
	
	ArrayList<CaveAreaEntry> getCaveAreas(int caveID) throws IllegalArgumentException;
	
	ArrayList<OrnamentEntry> getOrnamentsWHERE(String sqlWhere) throws IllegalArgumentException;
	
	ArrayList<OrientationEntry> getOrientationInformation() throws IllegalArgumentException;
	
	IconographyEntry getIconographyEntry(int iconographyID) throws IllegalArgumentException;
	

	ArrayList<CeilingTypeEntry> getCeilingTypes() throws IllegalArgumentException;

	ArrayList<PreservationClassificationEntry> getPreservationClassifications();


	boolean updateCaveEntry(CaveEntry caveEntry) throws IllegalArgumentException;

	int insertCaveEntry(CaveEntry caveEntry) throws IllegalArgumentException;

	ArrayList<CaveGroupEntry> getCaveGroups() throws IllegalArgumentException;

	UserEntry userLogin(String username, String password);

	boolean updateImageEntry(ImageEntry imgEntry, String sessionID) throws IllegalArgumentException;

	ArrayList<ImageTypeEntry> getImageTypes() throws IllegalArgumentException;

	int insertDepictionEntry(DepictionEntry depictionEntry, ArrayList<IconographyEntry> iconographyList);

	boolean updateDepictionEntry(DepictionEntry correspondingDepictionEntry, ArrayList<IconographyEntry> iconographyList, String sessionID);

	ArrayList<ModeOfRepresentationEntry> getModesOfRepresentation() throws IllegalArgumentException;

	ArrayList<WallLocationEntry> getWallLocations() throws IllegalArgumentException;

	boolean updateAuthorEntry(AuthorEntry currentAuthorEntry) throws IllegalArgumentException;
	
	AnnotatedBibliographyEntry insertAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) throws IllegalArgumentException;
	
	ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography() throws IllegalArgumentException;

	ArrayList<PublisherEntry> getPublishers() throws IllegalArgumentException;

	ArrayList<AuthorEntry> getAuthors() throws IllegalArgumentException;

	int insertPhotographerEntry(PhotographerEntry photographerEntry) throws IllegalArgumentException;

	int insertCaveGroupEntry(CaveGroupEntry cgEntry) throws IllegalArgumentException;

	int insertDistrictEntry(DistrictEntry de) throws IllegalArgumentException;

	int insertRegionEntry(RegionEntry re) throws IllegalArgumentException;

	int insertAuthorEntry(AuthorEntry currentAuthorEntry) throws IllegalArgumentException;

	int insertCeilingTypeEntry(CeilingTypeEntry ctEntry) throws IllegalArgumentException;

	@Deprecated
	ArrayList<CurrentLocationEntry> getCurrentLocations() throws IllegalArgumentException;

	ArrayList<IconographyEntry> getRelatedIconography(int depictionID) throws IllegalArgumentException;

	ArrayList<LocationEntry> getLocations() throws IllegalArgumentException;

	int insertVendorEntry(VendorEntry vEntry) throws IllegalArgumentException;

	int insertLocationEntry(LocationEntry lEntry) throws IllegalArgumentException;

	UserEntry checkSessionID(String sessionID, String username);
	
	ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() throws IllegalArgumentException;
	
	ArrayList<OrnamentComponentsEntry> getOrnamentComponents() throws IllegalArgumentException;

	ArrayList<OrnamentClassEntry> getOrnamentClass() throws IllegalArgumentException;
	
	OrnamentClassEntry addOrnamentClass(OrnamentClassEntry entry)  throws IllegalArgumentException;
	
	String getOSDContext() throws IllegalArgumentException;

	String getContext() throws IllegalArgumentException;

	OrnamentClassEntry renameOrnamentClass(OrnamentClassEntry entry)  throws IllegalArgumentException;
	
	OrnamentComponentsEntry renameOrnamentComponents(OrnamentComponentsEntry entry)  throws IllegalArgumentException;
	
	InnerSecondaryPatternsEntry addInnerSecondaryPatterns(InnerSecondaryPatternsEntry entry)  throws IllegalArgumentException;
	
	OrnamentComponentsEntry addOrnamentComponent(OrnamentComponentsEntry entry)  throws IllegalArgumentException;

	ArrayList<PreservationAttributeEntry> getPreservationAttributes() throws IllegalArgumentException;

	int insertPreservationAttributeEntry(PreservationAttributeEntry paEntry) throws IllegalArgumentException;

	int insertPublisherEntry(PublisherEntry publisherEntry) throws IllegalArgumentException;

	ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography(String sqlWhere) throws IllegalArgumentException;

	AnnotatedBibliographyEntry updateAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry);
	
	ArrayList<OrnamentPositionEntry> getPositionbyWall (WallEntry wall) throws IllegalArgumentException;
	
	ArrayList<OrnamentPositionEntry> getPositionbyReveal(WallEntry wall) throws IllegalArgumentException;
	
	ArrayList<OrnamentFunctionEntry> getFunctionbyPosition (OrnamentPositionEntry wall) throws IllegalArgumentException;

	ArrayList<OrnamentPositionEntry> getPositionbyCeiling (int ceiling1, int ceiling2) throws IllegalArgumentException;

	ArrayList<Integer> getDepictionFromIconography(String sqlWhere) throws IllegalArgumentException;

//	ArrayList<Integer> getRelatedDepictionIDs(String iconographyIDs, int correlationFactor) throws IllegalArgumentException;

	ArrayList<IconographyEntry> getIconography(int rootIndex) throws IllegalArgumentException;

	void doLogging(String usertag, String message);

	boolean updateOrnamentEntry(OrnamentEntry oEntry) throws IllegalArgumentException;

	ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliographyFromAuthors(ArrayList<AuthorEntry> authorList) throws IllegalArgumentException;

	ArrayList<BibKeywordEntry> getBibKeywords() throws IllegalArgumentException;

	int insertBibKeyword(BibKeywordEntry bkEntry) throws IllegalArgumentException;

	Boolean updateBibKeyword(BibKeywordEntry bkEntry) throws IllegalArgumentException;

	boolean deleteAuthorEntry(AuthorEntry selectedEntry) throws IllegalArgumentException;

	boolean updateUserEntry(UserEntry currentUser, String passwordHash, String newPasswordHash) throws IllegalArgumentException;

	boolean setAnnotationResults(AnnotationEntry annoEntry, boolean isOrnament) throws IllegalArgumentException;
	
	ArrayList<AnnotationEntry> getAnnotations(int depictionEntry) throws IllegalArgumentException;

	ArrayList<AnnotationEntry> getProposedAnnotations(ArrayList<ImageEntry> images, int depictionEntry) throws IllegalArgumentException;

	ArrayList<CaveEntry> searchCaves(CaveSearchEntry searchEntry) throws IllegalArgumentException;

	ArrayList<DepictionEntry> searchDepictions(DepictionSearchEntry searchEntry) throws IllegalArgumentException;

	Map<Integer,ArrayList<ImageEntry>> searchImages(ImageSearchEntry searchEntry) throws IllegalArgumentException;

	ArrayList<AnnotatedBibliographyEntry> searchAnnotatedBibliography(AnnotatedBibliographySearchEntry searchEntry) throws IllegalArgumentException;

	ArrayList<OrnamentEntry> searchOrnaments(OrnamenticSearchEntry searchEntry) throws IllegalArgumentException;

	int insertIconographyEntry(IconographyEntry iconographyEntry) throws IllegalArgumentException;

	boolean updateIconographyEntry(IconographyEntry iconographyEntryToEdit) throws IllegalArgumentException;

	boolean updateWallTreeEntry(WallTreeEntry wte) throws IllegalArgumentException;

	ArrayList<UserEntry> getUsers() throws IllegalArgumentException;

	ArrayList<UserEntry> getUsersFrontEnd() throws IllegalArgumentException;

	boolean updateUserEntry(UserEntry userEntry) throws IllegalArgumentException;

	boolean updateUserEntryFrontEnd(UserEntry userEntry) throws IllegalArgumentException;

	int insertUserEntry(UserEntry entry) throws IllegalArgumentException;

	int saveWebPageUser(UserEntry entry) throws IllegalArgumentException;

	boolean saveCollectedEntries(String sessionID, String collectionLabel, Boolean isGroupCollection, ArrayList<AbstractEntry> entryList) throws IllegalArgumentException;

	ArrayList<CollectionEntry> getRelatedCollectionNames(String sessionID) throws IllegalArgumentException;

	ArrayList<AbstractEntry> loadCollectedEntries(CollectionEntry value) throws IllegalArgumentException;

	CollectionEntry delCollectedEntries(CollectionEntry value) throws IllegalArgumentException;

	int addPreservationClassification(PreservationClassificationEntry pcEntry) throws IllegalArgumentException;

	ArrayList<ModifiedEntry> getModifiedAbstractEntry(AbstractEntry Entry) throws IllegalArgumentException;

	ArrayList<ModifiedEntry> getModifiedAnnoEntry(int ID, boolean isOrnament) throws IllegalArgumentException;
}
