package de.cses.client;


import java.util.ArrayList;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
import de.cses.shared.NicheEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.PublicationEntry;
import de.cses.shared.RegionContainer;
import de.cses.shared.StyleEntry;
import de.cses.shared.TypologyEntry;
import de.cses.shared.VendorEntry;


/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DatabaseService extends RemoteService {
	HoehlenUebersichtPopUpPanelContainer getHoehlenInfosbyID(int iD) throws IllegalArgumentException;
	String deleteHoehlebyID(int hoehlenID) throws IllegalArgumentException;
	RegionenUebersichtPopUpPanelContainer getRegionenInfosbyID(int iD ) throws IllegalArgumentException;
	ArrayList<RegionContainer> createRegionen() throws IllegalArgumentException;
	ArrayList<HoehlenContainer> createHoehlenbyRegion(int iD) throws IllegalArgumentException;
	String saveRegion(ArrayList<HoehlenContainer> hoehlen, int RegioniD, int buttonSize, int imageID) throws IllegalArgumentException;
	String setRegionFoto(int imageID, int regionID) throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyCella(ArrayList<CellaEntry> cellas)  throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyAntechamber(ArrayList<AntechamberEntry> antechamber)  throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyCaveType(ArrayList<CaveTypeEntry> caveType)  throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyDistrict(ArrayList<DistrictEntry> districts)  throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyDepiction(ArrayList<DepictionEntry> depictions)  throws IllegalArgumentException;
	
	//ArrayList<CaveEntry> getCavesbyNiches(ArrayList<NicheEntry> niches)  throws IllegalArgumentException;
	ArrayList<CaveEntry> getCavesbyOrnaments(ArrayList<OrnamentEntry> ornaments)  throws IllegalArgumentException;
	//ArrayList<CaveEntry> getCavesbyOrnamentsOfOtherCultures(ArrayList<OrnamentOfOtherCulturesEntry> ornamentOfOtherCultures)  throws IllegalArgumentException;
	//ArrayList<CaveEntry> getCavesbyPhotographer(ArrayList<PhotographerEntry> photographers)  throws IllegalArgumentException;
	//ArrayList<CaveEntry> getCavesbyTypology(ArrayList<TypologyEntry> typologys)  throws IllegalArgumentException;

	String dbServer(String name) throws IllegalArgumentException;



	ArrayList<DistrictEntry> getDistricts() throws IllegalArgumentException;



	ArrayList<ImageEntry> getImages() throws IllegalArgumentException;



	ArrayList<ImageEntry> getImages(String where) throws IllegalArgumentException;



	ArrayList<PhotographerEntry> getPhotographer() throws IllegalArgumentException;



	ImageEntry getImage(int imageID) throws IllegalArgumentException;



	ArrayList<CaveEntry> getCaves() throws IllegalArgumentException;



	ArrayList<CaveEntry> getCavesbyDistrictID(int DistrictID) throws IllegalArgumentException;
	
	ArrayList<HoehlenContainer> getCavesbyDistrictIDKucha(int DistrictID) throws IllegalArgumentException;



	ArrayList<OrnamentEntry> getOrnaments() throws IllegalArgumentException;



	ArrayList<OrnamentOfOtherCulturesEntry> getOrnamentsOfOtherCultures() throws IllegalArgumentException;



	ArrayList<DepictionEntry> getDepictions() throws IllegalArgumentException;



	DepictionEntry getDepictionEntry(int depictionID) throws IllegalArgumentException;



	boolean updateEntry(String sqlUpdate);



	boolean deleteEntry(String sqlDelete);



	int insertEntry(String sqlInsert);



	ArrayList<IconographyEntry> getIconography() throws IllegalArgumentException;



	ArrayList<PictorialElementEntry> getPictorialElements() throws IllegalArgumentException;



	boolean saveOrnamentEntry(OrnamentEntry ornamentEntry) throws IllegalArgumentException;



	CaveTypeEntry getCaveTypebyID(int caveTypeID) throws IllegalArgumentException;



	ArrayList<CaveTypeEntry> getCaveTypes() throws IllegalArgumentException;



	ArrayList<VendorEntry> getVendors() throws IllegalArgumentException;



	ArrayList<StyleEntry> getStyles() throws IllegalArgumentException;



	ArrayList<ExpeditionEntry> getExpeditions() throws IllegalArgumentException;



	PublicationEntry getPublicationEntry(int id) throws IllegalArgumentException;



	AuthorEntry getAuthorEntry(int id) throws IllegalArgumentException;



	ImageEntry getMasterImageEntryForDepiction(int depictionID) throws IllegalArgumentException;


	ArrayList<CaveEntry> getRandomCaves() throws IllegalArgumentException;
	
	ArrayList<OrnamentEntry> getRandomOrnaments() throws IllegalArgumentException;

	ArrayList<CaveTypeEntry> getRandomCaveTypes() throws IllegalArgumentException;
	
	ArrayList<DistrictEntry> getRandomDistricts() throws IllegalArgumentException;

}




