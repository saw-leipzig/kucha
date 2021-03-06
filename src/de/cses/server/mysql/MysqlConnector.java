/*
 * Copyright 2016-2019
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
package de.cses.server.mysql;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.google.gson.Gson;
//import javax.mail.Authenticator;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.server.ServerProperties;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.BibKeywordEntry;
import de.cses.shared.C14AnalysisUrlEntry;
import de.cses.shared.C14DocumentEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.CaveSketchEntry;
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
import de.cses.shared.OrnamentCaveRelation;
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
import de.cses.shared.WallOrnamentCaveRelation;
import de.cses.shared.WallTreeEntry;

/**
 * This is the central Database connector. Here are all methods that we need for standard database operations, including user login and access management.
 * 
 * @author alingnau
 *
 */
public class MysqlConnector implements IsSerializable {

	private String url; // MysqlConnector.db.url
	private String user; // MysqlConnector.db.user
	private String password; // MysqlConnector.db.password
	private boolean dologging = true;
	private boolean dologgingbegin = false;
	private ImageProperties imgProperties;
	private Map<Integer,Integer> rootItems = new HashMap<Integer,Integer>();
	public class KuchaDic{ 
		public ArrayList<DistrictEntry> districts; 
		public ArrayList<SiteEntry> sites; 
		public ArrayList<RegionEntry> region; 
		public ArrayList<CaveTypeEntry> caveType; 
		public ArrayList<CeilingTypeEntry> ceilingType; 
		public ArrayList<PreservationClassificationEntry> preservationClassification; 
		public ArrayList<ImageTypeEntry> imageTypes; 
		public ArrayList<ExpeditionEntry> expeditions; 
		public ArrayList<StyleEntry> style; 
		public ArrayList<IconographyEntry> iconography; 
		public ArrayList<ModeOfRepresentationEntry> modesOfRepresentation; 
		public ArrayList<WallTreeEntry> wallLocation; 
		public ArrayList<LocationEntry> location; 
		public ArrayList<VendorEntry> vendor; 
		public ArrayList<PublicationTypeEntry> publicationType;  
		public ArrayList<OrnamentEntry> ornaments;
		public ArrayList<OrientationEntry> orientation; 
		public ArrayList<PositionEntry> position; 
		public KuchaDic( ArrayList<DistrictEntry> districts, ArrayList<SiteEntry> sites, ArrayList<RegionEntry> region, ArrayList<CaveTypeEntry> caveType, ArrayList<CeilingTypeEntry> ceilingType, ArrayList<PreservationClassificationEntry> preservationClassification, ArrayList<ImageTypeEntry> imageTypes, 
				ArrayList<ExpeditionEntry> expeditions, ArrayList<StyleEntry> style, 
				ArrayList<IconographyEntry> iconography, ArrayList<ModeOfRepresentationEntry> modesOfRepresentation, 
				ArrayList<WallTreeEntry> wallLocation, ArrayList<LocationEntry> location, 
				ArrayList<VendorEntry> vendor,ArrayList<PublicationTypeEntry> publicationType,
				ArrayList<OrnamentEntry> ornaments, 
				ArrayList<OrientationEntry> orientation,
				ArrayList<PositionEntry> position ) {
			this.districts = districts;
			this.sites = sites;
			this.region=region;
			this.caveType=caveType;
			this.ceilingType=ceilingType;
			this.preservationClassification=preservationClassification;
			this.imageTypes=imageTypes;
			this.expeditions=expeditions;
			this.style=style;
			this.iconography=iconography;
			this.modesOfRepresentation=modesOfRepresentation;
			this.wallLocation=wallLocation;
			this.location=location;
			this.vendor=vendor;
			this.publicationType=publicationType;
			this.ornaments=ornaments;
			this.orientation=orientation;
			this.position=position;
		}
		}

	// private int auto_increment_id;
	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();
		LabelProvider<ImageEntry> title();
		ValueProvider<ImageEntry, String> shortName();
	}
	private static MysqlConnector instance = null;
	private ServerProperties serverProperties = ServerProperties.getInstance();

	/**
	 * By calling this method, we avoid that a new instance will be created if there is already one existing. If this method is called without an instance
	 * existing, one will be created.
	 * 
	 * @return
	 */
	public static synchronized MysqlConnector getInstance() {
		if (instance == null) {
			instance = new MysqlConnector();
		}
		return instance;
	}

	private Connection connection;

	private MysqlConnector() {

		user = serverProperties.getProperty("MysqlConnector.db.user");
		password = serverProperties.getProperty("MysqlConnector.db.password");
		url = serverProperties.getProperty("MysqlConnector.db.url");
		try {
			Class.forName("org.mariadb.jdbc.Driver"); //$NON-NLS-1$
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
	}

	private Connection getConnection() {
		try {
			if (connection == null || !connection.isValid(5)) {
				connection = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		return connection;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<DistrictEntry> getDistricts() {
		return getDistricts(null);
	}

	/**
	 * Selects all districts from the table 'Districts' in the database
	 * 
	 * @return
	 */
	private LocationEntry searchLocationByFilename(String filename) {
		int CurrentLocationID = -1;
		if (filename.contains("BritishMuseum")){
			CurrentLocationID=26;
		}
		else if(filename.contains("VandA")){
			CurrentLocationID=12;
		}
		else if(filename.contains("AshmoleanMuseum")){
			CurrentLocationID=11;
		}
		else if(filename.contains("MuseeGuimet")){
			CurrentLocationID=10;
		}
		else if(filename.contains("HoppFerenceMuseum")){
			CurrentLocationID=13;
		}
		else if(filename.contains("IkuoHirayamaSilkRoadMuseum")){
			CurrentLocationID=7;
		}
		else if(filename.contains("TokyoNationalMuseum")){
			CurrentLocationID=9;
		}
		else if(filename.contains("TokyoUniversity")){
			CurrentLocationID=28;
		}
		else if(filename.contains("MWoods")){
			CurrentLocationID=37;
		}
		else if(filename.contains("Eremitage")){
			CurrentLocationID=5;
		}
		else if(filename.contains("NMK")){
			CurrentLocationID=15;
		}
		else if(filename.contains("AcademiaSinica_Taipei")){
			CurrentLocationID=25;
		}
		else if(filename.contains("SeattleAsianArtMuseum")){
			CurrentLocationID=22;
		}
		else if(filename.contains("BostonMFA")){
			CurrentLocationID=20;
		}
		else if(filename.contains("MuseumofArt_Cleveland")){
			CurrentLocationID=17;
		}
		else if(filename.contains("HarvardFoggArtMuseum")){
			CurrentLocationID=34;
		}
		else if(filename.contains("Nelson-AtkinsMuseum")){
			CurrentLocationID=36;
		}
		else if(filename.contains("BrooklynArtMuseum")){
			CurrentLocationID=27;
		}
		else if(filename.contains("MetropolitanMuseum")){
			CurrentLocationID=19;
		}
		else if(filename.contains("MetropolitanMuseumNY")){
			CurrentLocationID=19;
		}
		else if(filename.contains("NYC_Metropolitan")){
			CurrentLocationID=19;
		}
		else if(filename.contains("MetNY")){
			CurrentLocationID=19;
		}
		else if(filename.contains("PennMuseum")){
			CurrentLocationID=21;
		}
		else if(filename.contains("AsianArtMuseumSanFrancisco")){
			CurrentLocationID=16;
		}
		else if(filename.contains("SmithsonianAmericanArtMuseum")){
			CurrentLocationID=23;
		}
		else if(filename.matches(".*III\\d{3}.*")){
			CurrentLocationID=4;
		}
		else if(filename.matches(".*TA\\d{3}.*")){
			CurrentLocationID=4;
		}
		//System.out.println("For filename: "+filename+" found locationID: "+Integer.toString(CurrentLocationID));
		if (CurrentLocationID>0) {
			return getLocation(CurrentLocationID);
		}
		else {
			return null;
		}
		
	}
	private String searchInventoryNumberByFilename(String filename) {
		System.out.println("searchInventoryNumberByFilename for "+filename);
		String inventoryNumber="";
		Pattern pattern;
	    Matcher matcher;
		pattern = Pattern.compile("III\\d{3}\\d?[a-z]?[a-z]?");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.start(), matcher.end());
			inventoryNumber=inventoryNumber.substring(0, 3) + " " + inventoryNumber.substring(3);
			return inventoryNumber;
		}
		pattern = Pattern.compile("IB\\d{3}\\d?[a-z]?[a-z]?");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.start(), matcher.end());
			inventoryNumber=inventoryNumber.substring(0, 2) + " " + inventoryNumber.substring(2);
			return inventoryNumber;
		}
		pattern = Pattern.compile("VD\\d{3}\\d?[a-z]?[a-z]?");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.start(), matcher.end());
			inventoryNumber="ВДсэ-" + inventoryNumber.substring(2);
			return inventoryNumber;
		}
		pattern = Pattern.compile("KU\\d{3}\\d?[a-z]?[a-z]?");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.start(), matcher.end());
			inventoryNumber="КУ-" + inventoryNumber.substring(2);
			return inventoryNumber;
		}
		pattern = Pattern.compile("SmithsonianAmericanArtMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("PennMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("BostonMFA_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("NYC_Metropolitan");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("BrooklynMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("DetroitInstituteOfArts_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("SmithsonianInstitutionMuseumWA_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("AsianArtMuseumSanFrancisco_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("HarvardFoggArtMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("Nelson-AtkinsMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("MuseumOfArt_Cleveland_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("SeattleAsianArtMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("HoppFerenceMuseum_Budapest_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			inventoryNumber=inventoryNumber.substring(0, 3) + " " + inventoryNumber.substring(3);
			return inventoryNumber;
		}
		pattern = Pattern.compile("MuseeGuimet_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			inventoryNumber=inventoryNumber.substring(0, 2) + " " + inventoryNumber.substring(2);
			return inventoryNumber;
		}
		pattern = Pattern.compile("NMK_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("TokyoUniversity_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("TNM_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("Kamakura_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		pattern = Pattern.compile("BritishMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			inventoryNumber= inventoryNumber.substring(0, inventoryNumber.lastIndexOf("-")).replaceAll("-" , ",").concat("."+inventoryNumber.substring(inventoryNumber.lastIndexOf("-")+1));
			return inventoryNumber;
		}
		pattern = Pattern.compile("VandA_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("E", "E.");
			return inventoryNumber;
		}
		pattern = Pattern.compile("AshmoleanMuseum_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end()).replace("-", ".");
			return inventoryNumber;
		}
		pattern = Pattern.compile("Mumu-Mwoods_");
	    matcher = pattern.matcher(filename);
		if(matcher.find()){
			inventoryNumber=filename.substring(matcher.end());
			return inventoryNumber;
		}
		return null;
		
	}
	public ArrayList<DistrictEntry> getDistricts(String sqlWhere) {
		ArrayList<DistrictEntry> result = new ArrayList<DistrictEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDistricts wurde ausgelöst.");;
		}
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(sqlWhere != null ? "SELECT * FROM Districts WHERE " + sqlWhere + " ORDER BY Name Asc"
					: "SELECT * FROM Districts ORDER BY Name Asc");
			while (rs.next()) {
				result.add(new DistrictEntry(rs.getInt("DistrictID"), rs.getString("Name"), rs.getInt("SiteID"), rs.getString("Description"),
						rs.getString("Map"), rs.getString("ArialMap")));
			}
			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDistricts brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public boolean protocollModifiedAbstractEntry(AbstractEntry entry, String changes) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				pstmt = dbc.prepareStatement( "INSERT INTO Modified (EntryID, ModifiedBy, ModifiedOn,Tags) VALUES (?, ?, ?, ?)");
				pstmt.setString(1, entry.getUniqueID());
				pstmt.setString(2, entry.getLastChangedByUser());
				pstmt.setString(3, entry.getModifiedOn());
				pstmt.setString(4, changes);
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}		
	}
	public boolean protocollModifiedAnnoEntry(AnnotationEntry entry) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement( "INSERT INTO Modified (EntryID, ModifiedBy, ModifiedOn,AnnoID,Tags) VALUES (?, ?, ?, ?, ?)");
			pstmt.setString(1, Integer.toString(entry.getDepictionID()));
			pstmt.setString(2, entry.getLastChangedByUser());
			pstmt.setString(3, entry.getModifiedOn());
			pstmt.setString(4, entry.getAnnotoriousID());
			if (entry.getTagsAsString().length()<513) {
				pstmt.setString(5, entry.getTagsAsString());
			}
			else {
				pstmt.setString(5, entry.getTagsAsString().substring(0, 512));
			}
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}		
	}

	public ArrayList<ModifiedEntry> getModifiedAbstractEntry(AbstractEntry entry) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<ModifiedEntry> results = new ArrayList<ModifiedEntry>(); 
		try {
			pstmt = dbc.prepareStatement( "SELECT * FROM Modified WHERE EntryID = ?");
			pstmt.setString(1, entry.getUniqueID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) { 
				ModifiedEntry result = new ModifiedEntry(rs.getInt("id"),rs.getString("EntryID"), rs.getString("ModifiedBy"), rs.getString("ModifiedOn"), rs.getString("AnnoID"), rs.getString("Tags"));
				results.add(result);
			}
			rs.close();
			pstmt.close();
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}		
}
	public ArrayList<ModifiedEntry> getModifiedAnnoEntry(DepictionEntry entry) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<ModifiedEntry> results = new ArrayList<ModifiedEntry>(); 
		try {
			pstmt = dbc.prepareStatement( "SELECT * FROM Modified WHERE EntryID = ?");
			pstmt.setString(1, Integer.toString(entry.getDepictionID()));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) { 
				ModifiedEntry result = new ModifiedEntry(rs.getInt("id"),rs.getString("EntryID"), rs.getString("ModifiedBy"), rs.getString("ModifiedOn"), rs.getString("AnnoID"), rs.getString("Tags"));
				results.add(result);
			}
			rs.close();
			pstmt.close();
			return results;
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}		
}
	
	
	
	public boolean deleteAbstractEntry(AbstractEntry entry) {
		if (entry instanceof DepictionEntry) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				pstmt = dbc.prepareStatement( " UPDATE Depictions SET deleted = 1 WHERE DepictionID = " +((DepictionEntry)entry).getDepictionID()  + ";");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}

		}
		if (entry instanceof AnnotationEntry) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				pstmt = dbc.prepareStatement( "UPDATE Polygon SET deleted = 1 WHERE AnnotoriousID = '" +((AnnotationEntry)entry).getAnnotoriousID()  + "';");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}
			try {
				pstmt = dbc.prepareStatement( "UPDATE Annotations SET deleted = 1 WHERE AnnotoriousID = '" +((AnnotationEntry)entry).getAnnotoriousID()  + "';");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}

		}
		if (entry instanceof AnnotatedBibliographyEntry) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try { 
				System.err.println("UPDATE AnnotatedBibliography SET deleted = 1 WHERE BibID = " +((AnnotatedBibliographyEntry)entry).getAnnotatedBibliographyID());
				pstmt = dbc.prepareStatement( " UPDATE AnnotatedBibliography SET deleted = 1 WHERE BibID = " +((AnnotatedBibliographyEntry)entry).getAnnotatedBibliographyID()  + ";");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}

		}
		else if (entry instanceof ImageEntry) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				System.out.println(" UPDATE Images SET deleted = 1 WHERE ImageID = " +((ImageEntry)entry).getImageID()  + ";");
				pstmt = dbc.prepareStatement( " UPDATE Images SET deleted = 1 WHERE ImageID = " +((ImageEntry)entry).getImageID()  + ";");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}

		}		
		else if (entry instanceof OrnamentEntry) {
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				System.out.println(" UPDATE Ornaments SET deleted = 1 WHERE OrnamentID = " +((OrnamentEntry)entry).getOrnamentID()  + ";");
				pstmt = dbc.prepareStatement( " UPDATE Ornaments SET deleted = 1 WHERE OrnamentID = " +((OrnamentEntry)entry).getOrnamentID()  + ";");
				ResultSet rs = pstmt.executeQuery();
				rs.close();
				pstmt.close();
				return true;
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return false;
			}

		}
		else {
			System.out.println("Problem bei der Erkennung der Klasse des AbstractEntries...");
			return false;
		}
		
	}
	public String getContext() {
		return serverProperties.getProperty("iiif.images");
	}
	public String getOSDContext() {
		return serverProperties.getProperty("iiif.osd");
	}

	public Map<Integer,String> getPics(ArrayList<ImageEntry> imgSources, int tnSize, String sessionID) {
		int accessLevelOfSession = getAccessLevelForSessionID(sessionID);
		ArrayList<Integer> authorizedAccessLevel = new ArrayList<Integer>();
		switch (accessLevelOfSession) {
			case UserEntry.GUEST:
				break;
			case UserEntry.ASSOCIATED:
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
				//System.err.println("acess Level PUBLIC and COPYRIGHT");
				break; 
			case UserEntry.FULL:
			case UserEntry.ADMIN:
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PRIVATE);
				//System.err.println("acess Level PUBLIC, COPYRIGHT and PRIVATE");
				break;
			default:
				authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
				//System.err.println("acess Level PUBLIC");
				break;
		}
		System.out.println(accessLevelOfSession+" - "+authorizedAccessLevel);
		
		String filename = "";
		Map<Integer,String> result = new HashMap<Integer,String>();
		for (ImageEntry imgEntry : imgSources) {
			try {
				if (imgEntry!=null && authorizedAccessLevel.contains(imgEntry.getAccessLevel())) {
					filename = imgEntry.getFilename();
//					inputFile = new File(serverProperties.getProperty("home.images"), filename);
				} else if ((accessLevelOfSession == UserEntry.GUEST) && (imgEntry.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_COPYRIGHT)) {
					// guests should be informed that there is an image
					filename = "accessNotPermitted.png";
				}
				else {
					// guests should be informed that there is an image
					filename = "accessNotPermitted.png";
				}
			URL imageURL = new URL("http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/!" + tnSize + "," + tnSize + "/0/default.jpg");
			InputStream in = imageURL.openStream();
			ByteArrayOutputStream bab = new ByteArrayOutputStream();
			//ByteArrayBuffer bab = new ByteArrayBuffer(0);
		    int eof = 0;
		    byte buffer[] = new byte[4096];
			while ((eof = in.read(buffer)) > 0) {
				bab.write(buffer, 0, eof);
			}
		    in.close();
			String base64 = Base64.getEncoder().encodeToString(bab.toByteArray());
			result.put(imgEntry.getImageID(), "data:image/jpg;base64,"+base64);
			}
			catch (Exception e) {
				//System.out.println("                <><><>"+e.getMessage());
				result.put(imgEntry.getImageID(), "icons/close_icon.png");
			}
			//System.out.println(imgEntry.getFilename()+" umgewandelt.");
		}
			
	
		return result;
		}
	private void serializeAllDepictionEntries(String sessionID) {
		Connection dbc = getConnection();
		DepictionEntry result = null;
		Statement stmt;
		ArrayList<OrientationEntry> orientations = getOrientationInformation();
		ArrayList<CaveGroupEntry> caveGroupes = getCaveGroups();
		ArrayList<ModeOfRepresentationEntry> moRep = getModesOfRepresentations();
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Depictions WHERE deleted=0");
			System.out.println("Start Select");
			int accessLevel=-1;
			accessLevel = getAccessLevelForSessionID(sessionID);
			// we only need to call this once, since we do not expect more than 1 result!
			String filename=serverProperties.getProperty("home.jsons")+"result.json";
			String dicFilename=serverProperties.getProperty("home.jsons")+"dic.json";
			Gson gson = new Gson();
		    try {
		        FileWriter myWriter = new FileWriter(filename);
				while (rs.next()) { 
					//System.out.println("got result");				
					result = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
							rs.getString("SeparateAksaras"), rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"),
							rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getDouble("Width"), rs.getDouble("Height"),
							getExpedition(rs.getInt("ExpeditionID")), rs.getDate("PurchaseDate"), getLocation(rs.getInt("CurrentLocationID")), rs.getString("InventoryNumber"),
							getVendor(rs.getInt("VendorID")), rs.getInt("StoryID"), getCave(rs.getInt("CaveID")), getwallsbyDepictionID(rs.getInt("DepictionID")), rs.getInt("AbsoluteLeft"),
							rs.getInt("AbsoluteTop"), rs.getInt("ModeOfRepresentationID"), rs.getString("ShortName"), rs.getString("PositionNotes"),
							rs.getInt("MasterImageID"), rs.getInt("AccessLevel"), rs.getString("LastChangedByUser"), rs.getString("LastChangedOnDate"),getAnnotations(rs.getInt("DepictionID")));
					result.setRelatedImages(getRelatedImages(result.getDepictionID(), sessionID,accessLevel));
					result.setRelatedBibliographyList(getRelatedBibliographyFromDepiction(result.getDepictionID()));
					result.setRelatedIconographyList(getRelatedIconography(result.getDepictionID()));
					ArrayList<ImageEntry> publicImmages = new ArrayList<ImageEntry>();
//					for (ImageEntry ie : result.getRelatedImages()) {
//						if (ie.getAccessLevel()>1) {
//							publicImmages.add(ie);
//						}
//					}
//					result.setRelatedImages(publicImmages);
					if (result.getStyleID()>0) {
						result.setStyle(getStyles("StyleID = "+Integer.toString(result.getStyleID())).get(0));						
					}
					for (ModeOfRepresentationEntry mr : moRep) {
						if (mr.getModeOfRepresentationID()==result.getModeOfRepresentationID()) {
							result.setModeOfRepresentation(mr);
						}
					};
					if (result.getCave() != null) {
						if (result.getCave().getCaveTypeID()>0) {
							result.getCave().setCaveType(getCaveTypes("CaveTypeID = " +Integer.toString(result.getCave().getCaveTypeID())).get(0));						
						}
						if (result.getCave().getSiteID()>0) {
							result.getCave().setSiteType(getSites("SiteID = " +Integer.toString(result.getCave().getSiteID())).get(0));
						}
						if (result.getCave().getDistrictID()>0) {
							result.getCave().setDistrictEntry(getDistricts("DistrictID = " +Integer.toString(result.getCave().getDistrictID())).get(0));
						}
						if (result.getCave().getRegionID()>0) {
							result.getCave().setRegionEntry(getRegions("RegionID = " +Integer.toString(result.getCave().getRegionID())).get(0));
						}
						for (OrientationEntry oe : orientations) {
							if (oe.getOrientationID()==result.getCave().getOrientationID()) {
								result.getCave().setOrientatioType(oe);
							}
						}
						result.getCave().setPreservationClassificationEntryType(getPreservationClassification(result.getCave().getSiteID()));
						for (CaveGroupEntry cge : caveGroupes) {
							if(cge.getCaveGroupID()==result.getCave().getCaveGroupID()) {
								result.getCave().setcaveGroupType(cge);
							}
						}						
					}

					//Gson gson = new GsonBuilder().create();//.setPrettyPrinting().create();

					String json = gson.toJson(result);
					//System.out.println(filename);
				    myWriter.write(json+String.format("%n"));
				}
				ArrayList<CaveEntry> caves = getCaves();
				for (CaveEntry cave : caves) {
					if (cave.getCaveTypeID()>0) {
						cave.setCaveType(getCaveTypes("CaveTypeID = " +Integer.toString(cave.getCaveTypeID())).get(0));						
					}
					if (cave.getSiteID()>0) {
						cave.setSiteType(getSites("SiteID = " +Integer.toString(cave.getSiteID())).get(0));
					}
					if (cave.getDistrictID()>0) {
						cave.setDistrictEntry(getDistricts("DistrictID = " +Integer.toString(cave.getDistrictID())).get(0));
					}
					if (cave.getRegionID()>0) {
						cave.setRegionEntry(getRegions("RegionID = " +Integer.toString(cave.getRegionID())).get(0));
					}
					for (OrientationEntry oe : orientations) {
						if (oe.getOrientationID()==cave.getOrientationID()) {
							cave.setOrientatioType(oe);
						}
					}
					cave.setPreservationClassificationEntryType(getPreservationClassification(cave.getSiteID()));
					for (CaveGroupEntry cge : caveGroupes) {
						if(cge.getCaveGroupID()==cave.getCaveGroupID()) {
							cave.setcaveGroupType(cge);
						}
					}
					String json = gson.toJson(cave);
					//System.out.println(filename);
				    myWriter.write(json+String.format("%n"));
				
				}
				ArrayList<AnnotatedBibliographyEntry> bibs = getAnnotatedBiblography();
				for (AnnotatedBibliographyEntry bib : bibs) {

					String json = gson.toJson(bib);
					//System.out.println(filename);
				    myWriter.write(json+String.format("%n"));
				
				}
				ArrayList<IconographyEntry> icos = getIconographyEntries("IconographyID > -1");
				for (IconographyEntry ico : icos) {

					String json = gson.toJson(ico);
					//System.out.println(filename);
				    myWriter.write(json+String.format("%n"));
				
				}
				
		        myWriter.close();
		        System.out.println("Starting praparing Kucha-Dictionary");
		        FileWriter myWriter2 = new FileWriter(dicFilename);
		        KuchaDic dic = new KuchaDic( 
		        		getDistricts(), 
		        		getSites(), 
		        		getRegions(), 
		        		getCaveTypes(), 
		        		getCeilingTypes(), 
		        		getPreservationClassifications(), 
		        		getImageTypes(), 
						getExpeditions(), 
						getStyles(),
						getIconography(0), 
						getModesOfRepresentations(), 
						getWallTree(0), 
						getLocations(),
						getVendors(), 
						getPublicationTypes(),
						getOrnaments(), 
						getOrientationInformation(), 
						getPosition());
		        System.out.println("Kucha-Dictionary created, preparing to write to json");
				Gson gson2 = new Gson();
				System.out.println("Gson-object created.");
				try {
					String json2 = gson2.toJson(dic);
					json2 = json2.replace("\"text\"", "\"name\"");
					System.out.println(dicFilename);
					myWriter2.write(json2+String.format("%n"));
			        System.out.println("Kucha-Dictionary is written to json.");
				}
			    catch (Exception e) {
			    	System.out.println(e);
			    }


		        myWriter2.close();
		      } catch (IOException e) {
			        System.out.println("An error occurred.");
			        e.printStackTrace();
			      }

			rs.close();
			stmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}

	}
	public Map<Integer,String> getPicsByImageID(String imgSourceIds, int tnSize, String sessionID) {
		Map<Integer,String> result = new HashMap<Integer,String>();
			
		if (imgSourceIds!="") {
			
			ArrayList<ImageEntry> imgSources = new ArrayList<ImageEntry>();
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				System.out.println("SELECT * FROM Images WHERE ImageID in (" + imgSourceIds + ") and deleted=0;");
				pstmt = dbc.prepareStatement( "SELECT * FROM Images WHERE ImageID in (" + imgSourceIds + ") and deleted=0;");

				
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					ImageEntry image = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
							rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
							rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Height"),rs.getDouble("Width"));
					if (image.getLocation()==null) {
						if (rs.getString("Title")!=null){
							image.setLocation(searchLocationByFilename(image.getTitle()));
						}
					}
					if (image.getInventoryNumber()==null||image.getInventoryNumber()=="") {
						if (rs.getString("Title")!=null) {
							image.setInventoryNumber(searchInventoryNumberByFilename(image.getTitle()));
						}
					}
					imgSources.add(image);
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return null;
			}
			int userAccessLevel = AbstractEntry.ACCESS_LEVEL_PUBLIC;
			int accessLevelOfSession = getAccessLevelForSessionID(sessionID);
			ArrayList<Integer> authorizedAccessLevel = new ArrayList<Integer>();
			switch (accessLevelOfSession) {
				case UserEntry.GUEST:
					break;
				case UserEntry.ASSOCIATED:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					//System.err.println("acess Level PUBLIC and COPYRIGHT");
					break; 
				case UserEntry.FULL:
				case UserEntry.ADMIN:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PRIVATE);
					//System.err.println("acess Level PUBLIC, COPYRIGHT and PRIVATE");
					break;
				default:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					//System.err.println("acess Level PUBLIC");
					break;
			}
			//System.out.println(accessLevelOfSession+" - "+authorizedAccessLevel);
			
			
			String filename = "";
			for (ImageEntry imgEntry : imgSources) {
				try {
					if (imgEntry!=null && authorizedAccessLevel.contains(imgEntry.getAccessLevel())) {
						filename = imgEntry.getFilename();
//						inputFile = new File(serverProperties.getProperty("home.images"), filename);
					} else if ((accessLevelOfSession == UserEntry.GUEST) && (imgEntry.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_COPYRIGHT)) {
						// guests should be informed that there is an image
						filename = "accessNotPermitted.png";
					}
					else {
						// all others shouldn't see anything
						filename = "accessNotPermitted.png";
					}
				URL imageURL = new URL("http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/!" + tnSize + "," + tnSize + "/0/default.jpg");
				InputStream in = imageURL.openStream();
				ByteArrayOutputStream bab = new ByteArrayOutputStream();
				//ByteArrayBuffer bab = new ByteArrayBuffer(0);
			    int eof = 0;
			    byte buffer[] = new byte[4096];
				while ((eof = in.read(buffer)) > 0) {
					bab.write(buffer, 0, eof);
				}
			    in.close();
				String base64 = Base64.getEncoder().encodeToString(bab.toByteArray());
				//System.out.println("------"+Integer.toString(imgEntry.getImageID())+"-----"+base64);
				result.put(imgEntry.getImageID(), "data:image/jpg;base64,"+base64);
				}
				catch (Exception e) {
					//System.out.println("                <><><>"+e.getMessage());
					result.put(imgEntry.getImageID(), "icons/close_icon.png");
				}
			}
		}
		
		return result;
	}	
	
	/**
	 * 
	 * @param publicationTypeID
	 *          DistrictID
	 * @return The corresponding DistrictEntry from the table Districts
	 */
	public DistrictEntry getDistrict(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDistrict wurde ausgelöst.");;
		}
		DistrictEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Districts WHERE DistrictID=" + id + " ORDER BY Name Asc");
			while (rs.next()) {
				result = new DistrictEntry(rs.getInt("DistrictID"), rs.getString("Name"), rs.getInt("SiteID"), rs.getString("Description"),
						rs.getString("Map"), rs.getString("ArialMap"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDistrict brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	private PublicationTypeEntry getPublicationType(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationType wurde ausgelöst.");;
		}
		PublicationTypeEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM PublicationTypes WHERE PublicationTypeID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new PublicationTypeEntry(rs.getInt("PublicationTypeID"), rs.getString("Name"), rs.getBoolean("AccessDateEnabled"),
						rs.getBoolean("AuthorEnabled"), rs.getBoolean("ParentTitleEnabled"), rs.getString("ParentTitleLabel"),
						rs.getBoolean("EditionEnabled"), rs.getBoolean("EditorEnabled"), rs.getBoolean("MonthEnabled"), rs.getBoolean("NumberEnabled"),
						rs.getBoolean("PagesEnabled"), rs.getBoolean("SeriesEnabled"), rs.getBoolean("TitleAddonEnabled"), rs.getString("TitleAddonLabel"),
						rs.getBoolean("UniversityEnabled"), rs.getBoolean("VolumeEnabled"), rs.getBoolean("IssueEnabled"),
						rs.getBoolean("YearEnabled"), rs.getBoolean("ThesisTypeEnabled"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationType brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<PublicationTypeEntry> getPublicationTypes() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationTypes wurde ausgelöst.");;
		}
		ArrayList<PublicationTypeEntry> result = new ArrayList<PublicationTypeEntry>();
		PublicationTypeEntry entry;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PublicationTypes ORDER BY Name");
			while (rs.next()) {
				entry = new PublicationTypeEntry(rs.getInt("PublicationTypeID"), rs.getString("Name"), rs.getBoolean("AccessDateEnabled"),
						rs.getBoolean("AuthorEnabled"), rs.getBoolean("ParentTitleEnabled"), rs.getString("ParentTitleLabel"),
						rs.getBoolean("EditionEnabled"), rs.getBoolean("EditorEnabled"), rs.getBoolean("MonthEnabled"), rs.getBoolean("NumberEnabled"),
						rs.getBoolean("PagesEnabled"), rs.getBoolean("SeriesEnabled"), rs.getBoolean("TitleAddonEnabled"), rs.getString("TitleAddonLabel"),
						rs.getBoolean("UniversityEnabled"), rs.getBoolean("VolumeEnabled"), rs.getBoolean("IssueEnabled"),
						rs.getBoolean("YearEnabled"), rs.getBoolean("ThesisTypeEnabled"));
				result.add(entry);

			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationTypes brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * Creates a new image entry in the table 'Images'
	 * 
	 * @return auto incremented primary key 'ImageID'
	 */
	public synchronized ImageEntry createNewImageEntry() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von createNewImageEntry wurde ausgelöst.");;
		}
		ImageEntry entry = new ImageEntry();
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			System.err.println("Preparing statement.");
			pstmt = dbc.prepareStatement(
					"INSERT INTO Images (Filename, Title, ShortName, Copyright, PhotographerID, Comment, Date, ImageTypeID, AccessLevel, deleted, InventoryNumber, Width, Height) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, "");
			pstmt.setString(2, entry.getTitle());
			pstmt.setString(3, entry.getShortName());
			pstmt.setString(4, entry.getCopyright());
			System.err.println(entry.getImageAuthor());
			if (entry.getImageAuthor() != null) {
				pstmt.setInt(5, entry.getImageAuthor().getPhotographerID());				
			}
			else {
				pstmt.setInt(5, 0);
			}
			pstmt.setString(6, entry.getComment());
			pstmt.setString(7, entry.getDate());
			pstmt.setInt(8, entry.getImageTypeID());
			pstmt.setInt(9, entry.getAccessLevel());
			pstmt.setBoolean(10, entry.isdeleted());
			pstmt.setString(11, entry.getInventoryNumber());
			pstmt.setDouble(12, entry.getWidth());
			pstmt.setDouble(13, entry.getHeight());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				entry.setImageID(keys.getInt(1));
			}
			keys.close();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von createNewImageEntry brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}
	
	/**
	 * 
	 * @param caveID
	 * @return
	 */
	protected ArrayList<C14DocumentEntry> getC14Documents(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getC14Documents wurde ausgelöst.");;
		}
		C14DocumentEntry entry = new C14DocumentEntry();
		ArrayList<C14DocumentEntry> result = new ArrayList<C14DocumentEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM C14Documents WHERE CaveID=?");
			pstmt.setInt(1, caveID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = new C14DocumentEntry(rs.getString("C14DocumentName"), rs.getString("C14OriginalDocumentName"), rs.getInt("CaveID"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getC14Documents brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * 
	 * @param caveID
	 * @param entryList
	 * @return
	 */
	private synchronized boolean writeC14DocumentEntry(int caveID, ArrayList<C14DocumentEntry> entryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeC14DocumentEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement prestat;
		deleteEntry("DELETE FROM C14Documents WHERE CaveID=" + caveID);
		try {
			prestat = dbc.prepareStatement("INSERT INTO C14Documents (C14DocumentName, C14OriginalDocumentName, CaveID) VALUES (?, ?, ?)");
			for (C14DocumentEntry entry : entryList) {
				prestat.setString(1, entry.getC14DocumentName());
				prestat.setString(2, entry.getC14OriginalDocumentName());
				prestat.setInt(3, caveID);
				prestat.executeUpdate();
			}
			prestat.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeC14DocumentEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	/**
	 * Executes a pre-defined SQL INSERT statement and returns the generated (auto-increment) unique key from the table.
	 * 
	 * @return auto incremented primary key
	 */
	// public synchronized int insertEntry(String sqlInsert) {
	// Connection dbc = getConnection();
	// Statement stmt;
	// int generatedKey = -1;
	// try {
	// stmt = dbc.createStatement();
	// stmt.execute(sqlInsert, Statement.RETURN_GENERATED_KEYS);
	// ResultSet keys = stmt.getGeneratedKeys();
	// while (keys.next()) {
	// // there should only be 1 key returned here but we need to modify this
	// // in case
	// // we have requested multiple new entries. works for the moment
	// generatedKey = keys.getInt(1);
	// }
	// keys.close();
	// stmt.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// }
	//
	// return generatedKey;
	// }

	/**
	 * Executes an SQL update using a pre-defined SQL UPDATE string
	 * 
	 * @param sqlUpdate
	 *          The full sql string including the UPDATE statement
	 * @return true if sucessful
	 */
	public synchronized boolean updateEntry(String sqlUpdate) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			stmt.executeUpdate(sqlUpdate);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}

		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}
	
	/**
	 * Executes an SQL update using a pre-defined SQL UPDATE string
	 * 
	 * @param int IconographyID
	 */
	public ArrayList<WallTreeEntry> getWallTreeEntriesByIconographyID(int IconographyID, String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry wurde ausgelöst.");;
		}
		ArrayList<WallTreeEntry> results = new ArrayList<WallTreeEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		
		try {
			ArrayList<Integer> des = new ArrayList<Integer>();
			System.out.println("SELECT DepictionID FROM DepictionIconographyRelation where IconographyID="+Integer.toString(IconographyID) + " UNION SELECT Polygon.DepictionID FROM Annotations inner join Polygon on (Annotations.AnnotoriousID=Polygon.AnnotoriousID) where IconographyID="+Integer.toString(IconographyID));
			pstmt = dbc.prepareStatement("SELECT DepictionID FROM DepictionIconographyRelation where IconographyID="+Integer.toString(IconographyID) + " UNION SELECT Polygon.DepictionID FROM Annotations inner join Polygon on (Annotations.AnnotoriousID=Polygon.AnnotoriousID) where IconographyID="+Integer.toString(IconographyID));
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//System.out.println("DepictionID is here: "+rs.getInt("DepictionID"));
				DepictionEntry de= getDepictionEntry(rs.getInt("DepictionID"),sessionID);
				//System.out.println("Found Depiction"+de.getDepictionID());
				CaveEntry item = de.getCave();
				String site = item.getSiteID() > 0 ? getSites(" SiteID="+item.getSiteID()).get(0).getShortName() : "";
				String district = item.getDistrictID() > 0 ? getDistricts(" DistrictID="+item.getDistrictID()).get(0).getName() : "";
				String region = item.getRegionID() > 0 ? getRegions(" RegionID="+item.getRegionID()).get(0).getEnglishName() : "";
				String name=site  + " " + item.getOfficialNumber() + (!district.isEmpty() ? " / " + district : "") + (!region.isEmpty() ? " / " + region : "");
				WallTreeEntry result = new WallTreeEntry(item.getCaveID()+1000,0,name,name );
				boolean found = false;
				for (WallTreeEntry res : results) {
					if (result.getWallLocationID()==res.getWallLocationID()) {
						found=true;
						break;
					}
				}
				if (!found){ 
					System.out.println("add WallTreeEntry: "+result.getWallLocationID()+" - "+result.getParentID());
					results.add(result);
				}
				ArrayList<WallTreeEntry> resultsWallsByDe = getwallsbyDepictionID(de.getDepictionID());
				System.out.println("Länge der gefundenen Wall-Elemente: "+resultsWallsByDe.size());
				for (WallTreeEntry resWallByDe : resultsWallsByDe) {
					boolean found2 = false;
					resWallByDe.setWallLocationID(Integer.parseInt(Integer.toString(resWallByDe.getWallLocationID())+Integer.toString(item.getCaveID())));
					if ((resWallByDe.getParentID()==0)) {
						resWallByDe.setParentID(result.getWallLocationID());
					}
					else {
						resWallByDe.setParentID(Integer.parseInt(Integer.toString(resWallByDe.getParentID())+Integer.toString(item.getCaveID())));
					}
					Dictionary<Integer,PositionEntry> newPositions = new Hashtable();
					int i = 0;
					for (WallTreeEntry res : results) {
						if (resWallByDe.getWallLocationID()==res.getWallLocationID()) {
							found2=true;
							if (resWallByDe.getPosition()!=null) {
								for (PositionEntry pe : resWallByDe.getPosition()) {
									boolean foundPos = false;
									for (PositionEntry posOld : res.getPosition()) {
										if (pe.getPositionID()==posOld.getPositionID()) {
											foundPos=true;
											break;
										}
										if (!foundPos) {
											newPositions.put(i,pe);
										}
									}
								}
							}
							break;
						}
						i+=1;
					}
					Enumeration<Integer> e = newPositions.keys();
					while (e.hasMoreElements()) {
						int key = e.nextElement();
						results.get(key).addPosition(newPositions.get(key));
					}
				
					if (!found2){ 
						System.out.println("add WallTreeEntry: "+resWallByDe.getWallLocationID()+" - "+resWallByDe.getParentID());
						results.add(resWallByDe);
					}
					else {
						System.out.println("did not add WallTreeEntry: "+resWallByDe.getWallLocationID()+" - "+resWallByDe.getParentID());
					}
						
					
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}

		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public Map<Integer,String> getMasterImageFromOrnament(int tnSize, String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry wurde ausgelöst.");;
		}
		String result = "";
		Map<Integer,String> images =null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		
		try {
			pstmt = dbc.prepareStatement("SELECT MasterImageID FROM Ornaments");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//System.out.println("DepictionID is here: "+rs.getInt("DepictionID"));
				if (result=="") {
					result = rs.getString("MasterImageID");
				}
				else {
					result=result+","+rs.getString("MasterImageID");
				}
				
			}
			
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		images=getPicsByImageID(result, tnSize, sessionID);
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEntry brauchte "+diff + " Millisekunden.");;}}
		return images;
	}

	/**
	 * Executes a SQL delete using a predefined SQL DELETE string
	 * 
	 * @param sqlDelete
	 *          The full sql string including the DELETE statement
	 * @return true if sucessful
	 */
	public boolean deleteEntry(String sqlDelete) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von deleteEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			stmt.execute(sqlDelete);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von deleteEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}
	
	public Map<Integer,ArrayList<ImageEntry>> searchImages(ImageSearchEntry searchEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchImages wurde ausgelöst.");;
		}
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String where = "";
		
		if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
			where = "(Title LIKE ? OR ShortName LIKE ?)";
		}
		if (searchEntry.getCopyrightSearch() != null && !searchEntry.getCopyrightSearch().isEmpty()) {
			where += where.isEmpty() ? "Copyright LIKE ?" : " AND Copyright LIKE ?";
		}
		if (searchEntry.getCommentSearch() != null && !searchEntry.getCommentSearch().isEmpty()) {
			where += where.isEmpty() ? "Comment LIKE ?" : "AND Comment LIKE ?";
		}
		if (searchEntry.getDaysSinceUploadSearch() > 0) {
			where += where.isEmpty() ? "DATEDIFF(NOW(),ModifiedOn)<=" + searchEntry.getDaysSinceUploadSearch() : " AND DATEDIFF(NOW(),ModifiedOn)<=" + searchEntry.getDaysSinceUploadSearch();
		}
		String imageTypeIDs = "";
		for (int imageTypeID : searchEntry.getImageTypeIdList()) {
			imageTypeIDs += imageTypeIDs.isEmpty() ? Integer.toString(imageTypeID) : "," + imageTypeID;
		}
		if (!imageTypeIDs.isEmpty()) {
			where += where.isEmpty() ? "ImageTypeID IN (" + imageTypeIDs + ")" : " AND ImageTypeID IN (" + imageTypeIDs + ")" ;
		}
		String imageIDs = "";
		for (int imageID : searchEntry.getImageIdList()) {
			imageIDs += imageIDs.isEmpty() ? Integer.toString(imageID) : "," + imageID;
		}
		System.err.println(imageIDs);
		if (!imageIDs.isEmpty()) {
			where += where.isEmpty() ? "ImageID IN (" + imageIDs + ")" : " AND ImageID IN (" + imageIDs + ")" ;
		}
		
		String inStatement = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		switch (getAccessLevelForSessionID(searchEntry.getSessionID())) {
			case UserEntry.GUEST:
			case UserEntry.ASSOCIATED:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT;
				break; 
			case UserEntry.FULL:
			case UserEntry.ADMIN:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT + "," + AbstractEntry.ACCESS_LEVEL_PRIVATE;
				break;
		}
		
		where += where.isEmpty() ? "AccessLevel IN (" + inStatement + ")" : " AND AccessLevel IN (" + inStatement + ")";
		
		System.out.println(where.isEmpty() ? "SELECT * FROM Images where deleted=0 ORDER BY Title Asc LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+ ", "+Integer.toString(searchEntry.getMaxentries()) : "SELECT * FROM Images WHERE deleted=0 and " + where + " ORDER BY Title Asc LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+", "+Integer.toString(searchEntry.getMaxentries()));
		int anzahl=0;
		int i=1;
		try {
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT count(ImageID) as Anzahl FROM Images where deleted=0 ORDER BY Title Asc" : "SELECT count(ImageID) as Anzahl FROM Images WHERE deleted=0 and " + where +" ORDER BY Title Asc ");			
			if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++, searchEntry.getTitleSearch().replace("*", "%"));
			}
			if (searchEntry.getCopyrightSearch() != null && !searchEntry.getCopyrightSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getCopyrightSearch().replace("*", "%"));
			}
			if (searchEntry.getCommentSearch() != null && !searchEntry.getCommentSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getCommentSearch().replace("*", "%"));
			}
			
			ResultSet rsAnz = pstmt.executeQuery();
			while (rsAnz.next()) {
				anzahl= rsAnz.getInt("Anzahl");
			}
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM Images where deleted=0 ORDER BY Title Asc LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+ ", "+Integer.toString(searchEntry.getMaxentries()) : "SELECT * FROM Images WHERE deleted=0 and " + where + " ORDER BY Title Asc LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+", "+Integer.toString(searchEntry.getMaxentries()));
			i = 1; // counter to fill ? in where clause
			if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++, searchEntry.getTitleSearch().replace("*", "%"));
			}
			if (searchEntry.getCopyrightSearch() != null && !searchEntry.getCopyrightSearch().isEmpty()) {
				pstmt.setString(i++,searchEntry.getCopyrightSearch().replace("*", "%"));
			}
			if (searchEntry.getCommentSearch() != null && !searchEntry.getCommentSearch().isEmpty()) {
				pstmt.setString(i++,searchEntry.getCommentSearch().replace("*", "%"));
			}
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ImageEntry image = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
						rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Width"),rs.getDouble("Height"));
				if (image.getLocation()==null) {
					if (rs.getString("Title")!=null){
						image.setLocation(searchLocationByFilename(image.getTitle()));
					}
					
				}
				if (image.getInventoryNumber().isEmpty()) {
					if (rs.getString("Title")!=null) {
						image.setInventoryNumber(searchInventoryNumberByFilename(image.getTitle()));
					}
				}

//				if (image.getLocation()!=null) {
//					System.out.println("imagelocationID= "+image.getLocation().getLocationID());					
//				}
//				else {
//					System.out.println("Imaglocation not set. filename= "+image.getTitle());										
//				}
				results.add(image);
			}
			rs.close();
		 	pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
//		if (where.startsWith("AccessLevel") && results.size() > 100) {
//			// when there is  not filter option selected the where clause only deals with AccessLevel
//			// limiting the number of search results to avoid slowing down the system
//			ArrayList<ImageEntry> subResultList = new ArrayList<ImageEntry>();
//			for (ImageEntry ie : results.subList(0, 100)) {
//				subResultList.add(ie);
//			}
//			return subResultList;
//		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchImages brauchte "+diff + " Millisekunden.");;}}
		Map <Integer,ArrayList<ImageEntry>> endRes = new HashMap <Integer,ArrayList<ImageEntry>>();
		endRes.put(anzahl, results);
		System.out.println(endRes);
		return endRes;	
	}

	/**
	 * 
	 * @return ArrayList<String> with result from 'SELECT * FROM Images'
	 */
	public ArrayList<ImageEntry> getImageEntries() {
		return getImageEntries(null);
	}

	public ArrayList<ImageEntry> getImageEntries(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageEntries wurde ausgelöst.");;
		}
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			if (sqlWhere != null) {
				pstmt = dbc.prepareStatement("SELECT * FROM Images WHERE deleted=0 and " + sqlWhere + " ORDER BY Title Asc");
			} else {
				pstmt = dbc.prepareStatement("SELECT * FROM Images WHERE deleted=0 ORDER BY Title Asc");
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ImageEntry image = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
						rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Height"),rs.getDouble("Width"));
				if (image.getLocation()==null) {
					if (rs.getString("Title")!=null){
						image.setLocation(searchLocationByFilename(image.getTitle()));
					}
				}
				if (image.getInventoryNumber()==null||image.getInventoryNumber()=="") {
					if (rs.getString("Title")!=null) {
						image.setInventoryNumber(searchInventoryNumberByFilename(image.getTitle()));
					}
				}

				results.add(image);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	/**
	 * 
	 * @param imageID
	 * @return ImageEntry that matches imageID, or NULL
	 */
	public ImageEntry getImageEntry(int imageID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		ImageEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images WHERE deleted=0 and ImageID=" + imageID);
			if (rs.first()) {
				result = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), 
						rs.getInt("ImageTypeID"), rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Height"),rs.getDouble("Width"));
				if (result.getLocation()==null) {
					if (rs.getString("Title")!=null){
						result.setLocation(searchLocationByFilename(result.getTitle()));
					}
				}
				if (result.getInventoryNumber()==null||result.getInventoryNumber()=="") {
					if (rs.getString("Title")!=null) {
						result.setInventoryNumber(searchInventoryNumberByFilename(result.getTitle()));
					}
				}


			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (result==null) {
			System.out.println("getImageEntry: Kein passendes Image gefunden!");
		}
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<PhotographerEntry> getPhotographerEntries() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPhotographerEntries wurde ausgelöst.");;
		}
		ArrayList<PhotographerEntry> results = new ArrayList<PhotographerEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Photographers");
			while (rs.next()) {
				results.add(new PhotographerEntry(rs.getInt("PhotographerID"), rs.getString("Name"), rs.getString("Institution")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPhotographerEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	/**
	 * 
	 * @return
	 */
	public PhotographerEntry getPhotographerEntry(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPhotographerEntry wurde ausgelöst.");;
		}
		PhotographerEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Photographers WHERE PhotographerID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new PhotographerEntry(rs.getInt("PhotographerID"), rs.getString("Name"), rs.getString("Institution"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPhotographerEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<CaveEntry> searchCaves(CaveSearchEntry searchEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchCaves wurde ausgelöst.");;
		}
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		String caveTypeIdSet = "";
		String siteIdSet = "";
		String districtIdSet = "";
		String regionIdSet = "";
		String caveIdSet = "";
		PreparedStatement pstmt;
		String where = "";
		UserEntry ue = checkSessionID(searchEntry.getSessionID());
		for (int caveTypeID : searchEntry.getCaveTypeIdList()) {
			caveTypeIdSet += caveTypeIdSet.isEmpty() ? Integer.toString(caveTypeID) : "," + caveTypeID;
		}
		if (!caveTypeIdSet.isEmpty()) {
			where = "CaveTypeID IN (" + caveTypeIdSet + ")";
		}
		
		for (int siteID : searchEntry.getSiteIdList()) {
			siteIdSet += siteIdSet.isEmpty() ? Integer.toString(siteID) : "," + siteID;
		}
		if (!siteIdSet.isEmpty()) {
			where += where.isEmpty() ? "Caves.SiteID IN (" + siteIdSet + ")" : " AND Caves.SiteID IN (" + siteIdSet + ")";
		}
		
		for (int districtID : searchEntry.getDistrictIdList()) { 
			districtIdSet += districtIdSet.isEmpty() ? Integer.toString(districtID) : "," + districtID;
		}
		if (!districtIdSet.isEmpty()) {
			where += where.isEmpty() ? "DistrictID IN (" + districtIdSet + ")" : " AND DistrictID IN (" + districtIdSet + ")";
		}

		for (int regionID : searchEntry.getRegionIdList()) {
			regionIdSet += regionIdSet.isEmpty() ? Integer.toString(regionID) : "," + regionID;
		}
		if (!regionIdSet.isEmpty()) {
			where += where.isEmpty() ? "RegionID IN (" + regionIdSet + ")" : " AND RegionID IN (" + regionIdSet + ")";
		}
		
		for (int caveID : searchEntry.getCaveIdList()) {
			caveIdSet += caveIdSet.isEmpty() ? Integer.toString(caveID) : "," + caveID;
		}
		if (!searchEntry.geticonographyIDList().isEmpty()) {
			DepictionSearchEntry dse = new DepictionSearchEntry(searchEntry.getSessionID());
			for (int icoID : searchEntry.geticonographyIDList()) {
					dse.getIconographyIdList().add(icoID);
			}
			ArrayList<DepictionEntry> des = searchDepictions(dse);
			if (!des.isEmpty()) {
				for (DepictionEntry de : des) {
					caveIdSet += caveIdSet.isEmpty() ? Integer.toString(de.getCave().getCaveID()) : "," + de.getCave().getCaveID();
				}
			}
		}
		if (!caveIdSet.isEmpty()) {
			where += where.isEmpty() ? "Caves.CaveID IN (" + caveIdSet + ")" : " AND Caves.CaveID IN (" + caveIdSet + ")";
		}
		if (!searchEntry.getHistoricalName().isEmpty()) {
			where += where.isEmpty() ? "(HistoricName LIKE ? OR OptionalHistoricName LIKE ? OR CONCAT(Sites.ShortName, \" \",Caves.OfficialNumber) LIKE ?)" : " AND (HistoricName LIKE ? OR OptionalHistoricName LIKE ? OR CONCAT(Sites.ShortName, \" \",Caves.OfficialNumber) LIKE ?)";
		}

		if (searchEntry.isDecoratedOnly()) {
			where += where.isEmpty() ? "Caves.CaveID IN (SELECT CaveID FROM Depictions where Depictions.deleted=0 )" : " AND Caves.CaveID IN (SELECT CaveID FROM Depictions where Depictions.deleted=0 )";
		}
		String bibIDs ="";
		if (!searchEntry.getBibIdList().isEmpty()) {
			for (int bibID : searchEntry.getBibIdList()) {
				bibIDs += bibIDs.isEmpty() ? Integer.toString(bibID) : "," + bibID;
			}
		}
		if (!bibIDs.isEmpty()) {
			where += where.isEmpty() ? "BibID IN (" + bibIDs + ")" : " AND BibID IN (" + bibIDs + ")";
		}
		String officialNumbers ="";
		if (!searchEntry.getOfficialNumberList().isEmpty()) {
			for (String officialNumber : searchEntry.getOfficialNumberList()) {
				officialNumbers += officialNumbers.isEmpty() ? officialNumber : "," + officialNumber;
			}
		}
		if (!officialNumbers.isEmpty()) {
			where += where.isEmpty() ? "OfficialNumber IN (" + officialNumbers + ")" : " AND OfficialNumber IN (" + officialNumbers + ")";
		}
	
		/**
		 * We cannot filter the accessLevel because that would create problems e.g. when choosing a cave for a depiction.
		 * What we can do is restricting the visibility of certain fields e.g. comments but this has to be done 
		 * when the UI is build on the client side!
		 */
//		String inStatement = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
//		switch (getAccessLevelForSessionID(searchEntry.getSessionID())) {
//			case UserEntry.GUEST:
//			case UserEntry.ASSOCIATED:
//				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT;
//				break; 
//			case UserEntry.FULL:
//			case UserEntry.ADMIN:
//				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT + "," + AbstractEntry.ACCESS_LEVEL_PRIVATE;
//				break;
//		}
//		where += where.isEmpty() ? "AccessLevel IN (" + inStatement + ")" : " AND AccessLevel IN (" + inStatement + ")";

		System.err.println("searchCaves: where = " + where);
		
		try {
			int i=1; // counter for ? insert
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT Caves.*, CaveBibliographyRelation.BibID FROM Caves left Join CaveBibliographyRelation on (Caves.CaveID = CaveBibliographyRelation.CaveID) inner join Sites on (Caves.SiteID = Sites.SiteID)" : "SELECT Caves.*, CaveBibliographyRelation.BibID FROM Caves left Join CaveBibliographyRelation on (Caves.CaveID = CaveBibliographyRelation.CaveID) inner join Sites on (Caves.SiteID = Sites.SiteID) WHERE " + where);
			if (!searchEntry.getHistoricalName().isEmpty()) {
				pstmt.setString(i++, searchEntry.getHistoricalName().replace("*","%"));
				pstmt.setString(i++, searchEntry.getHistoricalName().replace("*","%"));
				pstmt.setString(i++, searchEntry.getHistoricalName().replace("*","%"));
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CaveEntry ce = new CaveEntry(rs.getInt("CaveID"), rs.getString("OfficialNumber"), rs.getString("HistoricName"),
						rs.getString("OptionalHistoricName"), rs.getInt("CaveTypeID"), rs.getInt("SiteID"), rs.getInt("DistrictID"),
						rs.getInt("RegionID"),rs.getInt("OrientationID"),
						rs.getString("StateOfPreservation"), rs.getString("Findings"),
						rs.getString("Notes"), rs.getString("FirstDocumentedBy"), rs.getInt("FirstDocumentedInYear"),
						rs.getInt("PreservationClassificationID"), rs.getInt("CaveGroupID"), rs.getString("OptionalCaveSketch"),
						rs.getString("CaveLayoutComments"), rs.getBoolean("HasVolutedHorseShoeArch"), rs.getBoolean("HasSculptures"),
						rs.getBoolean("HasClayFigures"), rs.getBoolean("HasImmitationOfMountains"),
						rs.getBoolean("HasHolesForFixationOfPlasticalItems"), rs.getBoolean("HasWoodenConstruction"), rs.getInt("AccessLevel"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				ce.setCaveAreaList(getCaveAreas(ce.getCaveID()));
				ce.setWallList(getWalls(ce.getCaveID()));
				ce.setC14AnalysisUrlList(getC14AnalysisEntries(ce.getCaveID()));
				ce.setC14DocumentList(getC14Documents(ce.getCaveID()));
				ce.setCaveSketchList(getCaveSketchEntriesFromCave(ce.getCaveID()));
				ce.setRelatedBibliographyList(getRelatedBibliographyFromCave(ce.getCaveID()));
				results.add(ce);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchCaves brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<CaveEntry> getCaves() {
		return getCaves(null);
	}

	public ArrayList<CaveEntry> getCaves(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaves wurde ausgelöst. WHERE-Klausel: "+sqlWhere);;
		}
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String caveIDs = "";
		String sqlKlauselCaveAreas = "";
		String sqlKlauselWalls = "";
		String sqlKlauselsqlC14AnalysisUrls = "";
		String sqlKlauselC14DocumentList = "";
		String sqlKlauselCaveSketchList = "";
		String sqlKlauselRelatedBibliographyList = "";
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery((sqlWhere == null) ? "SELECT * FROM Caves" : "SELECT * FROM Caves WHERE " + sqlWhere);
			if (sqlWhere == null) {
				sqlKlauselCaveAreas = "SELECT * FROM CaveAreas;";
				sqlKlauselWalls = "SELECT * FROM Walls;";
				sqlKlauselsqlC14AnalysisUrls = "SELECT * FROM C14AnalysisUrls;";
				sqlKlauselC14DocumentList = "SELECT * FROM C14Documents;";
				sqlKlauselCaveSketchList = "SELECT * FROM CaveSketches;";
				sqlKlauselRelatedBibliographyList="SELECT * FROM CaveBibliographyRelation;";
			}
			else {
				while (rs.next()) {
					if (caveIDs=="") {
						caveIDs=Integer.toString(rs.getInt("CaveID"));
					}
					else {
						caveIDs=caveIDs+ ", "+Integer.toString(rs.getInt("CaveID"));
						
					}
				}
				sqlKlauselCaveAreas = "SELECT * FROM CaveAreas WHERE CaveID in ("+caveIDs+");";
				sqlKlauselWalls = "SELECT * FROM Walls WHERE CaveID in ("+caveIDs+");";
				sqlKlauselsqlC14AnalysisUrls = "SELECT * FROM C14AnalysisUrls WHERE CaveID in ("+caveIDs+");";
				sqlKlauselC14DocumentList = "SELECT * FROM C14Documents WHERE CaveID in ("+caveIDs+");";
				sqlKlauselCaveSketchList = "SELECT * FROM CaveSketches WHERE CaveID in ("+caveIDs+");";
				sqlKlauselRelatedBibliographyList="SELECT * FROM CaveBibliographyRelation WHERE CaveID in ("+caveIDs+");";
			}
			ResultSet rsCaveAreas = stmt.executeQuery(sqlKlauselCaveAreas);
			CaveAreaEntry caEntry;
			ArrayList<CaveAreaEntry> caEntries = new ArrayList<CaveAreaEntry>();
				while (rsCaveAreas.next()) {
					caEntry = new CaveAreaEntry(rsCaveAreas.getInt("CaveID"), rsCaveAreas.getString("CaveAreaLabel"),
							rsCaveAreas.getDouble("ExpeditionMeasuredWidth"), rsCaveAreas.getDouble("ExpeditionMeasuredLength"),
							rsCaveAreas.getDouble("ExpeditionMeasuredWallHeight"), rsCaveAreas.getDouble("ExpeditionMeasuredTotalHeight"),
							rsCaveAreas.getDouble("ModernMeasuredMinWidth"), rsCaveAreas.getDouble("ModernMeasuredMaxWidth"),
							rsCaveAreas.getDouble("ModernMeasuredMinLength"), rsCaveAreas.getDouble("ModernMeasuredMaxLength"),
							rsCaveAreas.getDouble("ModernMeasuredMinHeight"), rsCaveAreas.getDouble("ModernMeasuredMaxHeight"),
							getPreservationClassification(rsCaveAreas.getInt("PreservationClassificationID")), getCeilingType(rsCaveAreas.getInt("CeilingTypeID1")), 
							getCeilingType(rsCaveAreas.getInt("CeilingTypeID2")), getPreservationClassification(rsCaveAreas.getInt("CeilingPreservationClassificationID1")), 
							getPreservationClassification(rsCaveAreas.getInt("CeilingPreservationClassificationID2")), getPreservationClassification(rsCaveAreas.getInt("FloorPreservationClassificationID")));
					caEntries.add(caEntry);
				}
			ResultSet rsKlauselWalls = stmt.executeQuery(sqlKlauselWalls);
			ArrayList<WallEntry> wallEntries = new ArrayList<WallEntry>();
			WallEntry wallE;
			while (rsKlauselWalls.next()) {
				wallE = new WallEntry(rsKlauselWalls.getInt("CaveID"), rsKlauselWalls.getInt("WallLocationID"), rsKlauselWalls.getInt("PreservationClassificationID"),
						rsKlauselWalls.getDouble("Width"), rsKlauselWalls.getDouble("Height"));
				wallEntries.add(wallE);
			}
			ResultSet rsKlauselsqlC14AnalysisUrls = stmt.executeQuery(sqlKlauselsqlC14AnalysisUrls);
			C14AnalysisUrlEntry c14URLE;
			ArrayList<C14AnalysisUrlEntry> c14URLEs = new ArrayList<C14AnalysisUrlEntry>();
			while (rsKlauselsqlC14AnalysisUrls.next()) {
				c14URLE = new C14AnalysisUrlEntry(rsKlauselsqlC14AnalysisUrls.getInt("C14AnalysisUrlID"), rsKlauselsqlC14AnalysisUrls.getString("C14Url"), rsKlauselsqlC14AnalysisUrls.getString("C14ShortName"), rsKlauselsqlC14AnalysisUrls.getInt("CaveID"));
				c14URLEs.add(c14URLE);
			}
			ResultSet rsKlauselC14DocumentList = stmt.executeQuery(sqlKlauselC14DocumentList);
			C14DocumentEntry C14DE = new C14DocumentEntry();
			ArrayList<C14DocumentEntry> C14DEs = new ArrayList<C14DocumentEntry>();
			while (rsKlauselC14DocumentList.next()) {
				C14DE = new C14DocumentEntry(rsKlauselC14DocumentList.getString("C14DocumentName"), rsKlauselC14DocumentList.getString("C14OriginalDocumentName"), rsKlauselC14DocumentList.getInt("CaveID"));
				C14DEs.add(C14DE);
			}
			ResultSet rsKlauselCaveSketchList = stmt.executeQuery(sqlKlauselCaveSketchList);
			ArrayList<CaveSketchEntry> caveSketches = new ArrayList<CaveSketchEntry>();
			CaveSketchEntry caveSketche;
				while (rsKlauselCaveSketchList.next()) {
					caveSketche = new CaveSketchEntry(rsKlauselCaveSketchList.getInt("CaveSketchID"), rsKlauselCaveSketchList.getInt("caveID"), rsKlauselCaveSketchList.getString("ImageType"));
					caveSketches.add(caveSketche);
				}
			ResultSet rsKlauselRelatedBibliographyList = stmt.executeQuery(sqlKlauselRelatedBibliographyList);
			
			Map<Integer, ArrayList<AnnotatedBibliographyEntry>> ABEs = new HashMap<Integer, ArrayList<AnnotatedBibliographyEntry>>();

			while (rsKlauselRelatedBibliographyList.next()) {
				if (ABEs.containsKey(rsKlauselRelatedBibliographyList.getInt("CaveID"))){
					ABEs.get(rsKlauselRelatedBibliographyList.getInt("CaveID")).add(getAnnotatedBiblographybyID(rsKlauselRelatedBibliographyList.getInt("BibID"),rsKlauselRelatedBibliographyList.getString("Page")));
				}
				else {
					ArrayList<AnnotatedBibliographyEntry> BibEntries = new ArrayList<AnnotatedBibliographyEntry>();
					BibEntries.add(getAnnotatedBiblographybyID(rsKlauselRelatedBibliographyList.getInt("BibID"),rsKlauselRelatedBibliographyList.getString("Page")));
					ABEs.put(rsKlauselRelatedBibliographyList.getInt("CaveID"),BibEntries);
				}
							}
			System.out.println(sqlKlauselCaveAreas);
			while (rs.next()) {
				CaveEntry ce = new CaveEntry(rs.getInt("CaveID"), rs.getString("OfficialNumber"), rs.getString("HistoricName"),
						rs.getString("OptionalHistoricName"), rs.getInt("CaveTypeID"), rs.getInt("SiteID"), rs.getInt("DistrictID"),
						rs.getInt("RegionID"),rs.getInt("OrientationID"),
						rs.getString("StateOfPreservation"), rs.getString("Findings"),
						rs.getString("Notes"), rs.getString("FirstDocumentedBy"), rs.getInt("FirstDocumentedInYear"),
						rs.getInt("PreservationClassificationID"), rs.getInt("CaveGroupID"), rs.getString("OptionalCaveSketch"),
						rs.getString("CaveLayoutComments"), rs.getBoolean("HasVolutedHorseShoeArch"), rs.getBoolean("HasSculptures"),
						rs.getBoolean("HasClayFigures"), rs.getBoolean("HasImmitationOfMountains"),
						rs.getBoolean("HasHolesForFixationOfPlasticalItems"), rs.getBoolean("HasWoodenConstruction"), rs.getInt("AccessLevel"), 
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				ArrayList<CaveAreaEntry> caEntriesSelect = new ArrayList<CaveAreaEntry>();
				for (CaveAreaEntry cae : caEntries) {
					if (cae.getCaveID()==ce.getCaveID()) {
						caEntriesSelect.add(cae);
					}
				}
				//ce.setCaveAreaList(getCaveAreas(ce.getCaveID()));
				ce.setCaveAreaList(caEntriesSelect);
				ArrayList<WallEntry> wallEntriesSelect = new ArrayList<WallEntry>();
				for (WallEntry we : wallEntries) {
					if (we.getCaveID()==ce.getCaveID()) {
						wallEntriesSelect.add(we);
					}
				}
//				ce.setWallList(getWalls(ce.getCaveID()));
				ce.setWallList(wallEntriesSelect);
				ArrayList<C14AnalysisUrlEntry> c14URLEsSelect = new ArrayList<C14AnalysisUrlEntry>();
				for (C14AnalysisUrlEntry c14e : c14URLEs) {
					if (c14e.getCaveID()==ce.getCaveID()) {
						c14URLEsSelect.add(c14e);
					}
				}
//				ce.setC14AnalysisUrlList(getC14AnalysisEntries(ce.getCaveID()));
				ce.setC14AnalysisUrlList(c14URLEsSelect);
				ArrayList<C14DocumentEntry> C14DEsSelect = new ArrayList<C14DocumentEntry>();
				for (C14DocumentEntry c14DE : C14DEs) {
					if (c14DE.getCaveID()==ce.getCaveID()) {
						C14DEsSelect.add(c14DE);
					}
				}
//				ce.setC14DocumentList(getC14Documents(ce.getCaveID()));
				ce.setC14DocumentList(C14DEsSelect);
				ArrayList<CaveSketchEntry> caveSketchesSelect = new ArrayList<CaveSketchEntry>();
				for (CaveSketchEntry cse : caveSketches) {
					if (cse.getCaveID()==ce.getCaveID()) {
						caveSketchesSelect.add(cse);
					}
				}
//				ce.setCaveSketchList(getCaveSketchEntriesFromCave(ce.getCaveID()));
				ce.setCaveSketchList(caveSketchesSelect);
				ArrayList<AnnotatedBibliographyEntry> ABEsSelect = new ArrayList<AnnotatedBibliographyEntry>();
				if (ABEs.containsKey(ce.getCaveID())){
					ce.setRelatedBibliographyList(ABEs.get(ce.getCaveID()));
				}
//				ce.setRelatedBibliographyList(getRelatedBibliographyFromCave(ce.getCaveID()));
				
				results.add(ce);
			}
			System.out.println(caveIDs);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaves brauchte "+diff + " Millisekunden. Where-Klausel: "+sqlWhere);;}}
		return results;
	}

	public CaveEntry getCave(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCave wurde ausgelöst.");;
		}
		CaveEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Caves WHERE CaveID=" + id);
			if (rs.first()) {
				result = new CaveEntry(rs.getInt("CaveID"), rs.getString("OfficialNumber"), rs.getString("HistoricName"),
						rs.getString("OptionalHistoricName"), rs.getInt("CaveTypeID"), rs.getInt("SiteID"), rs.getInt("DistrictID"),
						rs.getInt("RegionID"),	rs.getInt("OrientationID"),
						rs.getString("StateOfPreservation"), rs.getString("Findings"),
						rs.getString("Notes"), rs.getString("FirstDocumentedBy"), rs.getInt("FirstDocumentedInYear"),
						rs.getInt("PreservationClassificationID"), rs.getInt("CaveGroupID"), rs.getString("OptionalCaveSketch"),
						rs.getString("CaveLayoutComments"), rs.getBoolean("HasVolutedHorseShoeArch"), rs.getBoolean("HasSculptures"),
						rs.getBoolean("HasClayFigures"), rs.getBoolean("HasImmitationOfMountains"),
						rs.getBoolean("HasHolesForFixationOfPlasticalItems"), rs.getBoolean("HasWoodenConstruction"), rs.getInt("AccessLevel"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				result.setCaveAreaList(getCaveAreas(result.getCaveID()));
				result.setWallList(getWalls(result.getCaveID()));
				result.setC14AnalysisUrlList(getC14AnalysisEntries(result.getCaveID()));
				result.setC14DocumentList(getC14Documents(result.getCaveID()));
				result.setCaveSketchList(getCaveSketchEntriesFromCave(result.getCaveID()));
				result.setRelatedBibliographyList(getRelatedBibliographyFromCave(result.getCaveID()));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCave brauchte "+diff + " Millisekunden. Where-Klausel: ");;}}
		return result;
	}

	public ArrayList<CaveEntry> getCavesbyDistrictID(int districtID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCavesbyDistrictID wurde ausgelöst.");;
		}
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Caves WHERE DistrictID=?");
			pstmt.setInt(1, districtID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				CaveEntry ce = new CaveEntry(rs.getInt("CaveID"), rs.getString("OfficialNumber"), rs.getString("HistoricName"),
						rs.getString("OptionalHistoricName"), rs.getInt("CaveTypeID"), rs.getInt("SiteID"), rs.getInt("DistrictID"),
						rs.getInt("RegionID"), rs.getInt("OrientationID"), rs.getString("StateOfPreservation"), rs.getString("Findings"),
						rs.getString("Notes"), rs.getString("FirstDocumentedBy"), rs.getInt("FirstDocumentedInYear"),
						rs.getInt("PreservationClassificationID"), rs.getInt("CaveGroupID"), rs.getString("OptionalCaveSketch"),
						rs.getString("CaveLayoutComments"), rs.getBoolean("HasVolutedHorseShoeArch"), rs.getBoolean("HasSculptures"),
						rs.getBoolean("HasClayFigures"), rs.getBoolean("HasImmitationOfMountains"),
						rs.getBoolean("HasHolesForFixationOfPlasticalItems"), rs.getBoolean("HasWoodenConstruction"), rs.getInt("AccessLevel"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				ce.setCaveAreaList(getCaveAreas(ce.getCaveID()));
				ce.setWallList(getWalls(ce.getCaveID()));
				ce.setC14AnalysisUrlList(getC14AnalysisEntries(ce.getCaveID()));
				ce.setC14DocumentList(getC14Documents(ce.getCaveID()));
				ce.setCaveSketchList(getCaveSketchEntriesFromCave(ce.getCaveID()));
				ce.setRelatedBibliographyList(getRelatedBibliographyFromCave(ce.getCaveID()));
				results.add(ce);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCavesbyDistrictID brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<OrnamentEntry> getOrnaments() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnaments wurde ausgelöst.");;
		}
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Ornaments where  Ornaments.deleted =0");
			while (rs.next()) {
				//System.out.println("Durchlauf"+Integer.toString(rs.getInt("OrnamentID")));
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						//rs.getString("Annotation"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")), 
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),rs.getInt("IconographyID"),rs.getInt("MasterImageID"), getOrnamentRelatedIconography(rs.getInt("OrnamentID"))));
				// Aufruf der h�heren Hierarchie Ebenen der Ornamentik mittels getCaveRelation
				// Aufruf der Tabellen OrnamentComponentsRelation, OrnamentImageRelation und InnerSecondaryPatternRelation
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnaments brauchte "+diff + " Millisekunden.");;}}


		return results;
	}

	public ArrayList<OrnamentOfOtherCulturesEntry> getOrnametsOfOtherCultures() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnametsOfOtherCultures wurde ausgelöst.");;
		}
		ArrayList<OrnamentOfOtherCulturesEntry> results = new ArrayList<OrnamentOfOtherCulturesEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamentsOfOtherCultures");
			while (rs.next()) {
				results.add(new OrnamentOfOtherCulturesEntry(rs.getInt("OtherOrnamentID"), rs.getString("Description")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging) {
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnametsOfOtherCultures brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public CaveTypeEntry getCaveTypebyID(int caveTypeID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveTypebyID wurde ausgelöst.");;
		}
		CaveTypeEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CaveType WHERE CaveTypeID =" + caveTypeID);
			while (rs.next()) {
				result = new CaveTypeEntry(rs.getInt("CaveTypeID"), rs.getString("NameEN"), rs.getString("DescriptionEN"),
						rs.getString("SketchName"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveTypebyID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<CaveTypeEntry> getCaveTypes() {
		return getCaveTypes(null);
	}

	public ArrayList<CaveTypeEntry> getCaveTypes(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveTypes wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		ArrayList<CaveTypeEntry> results = new ArrayList<CaveTypeEntry>();
		Statement stmt;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					sqlWhere != null ? "SELECT * FROM CaveType WHERE " + sqlWhere + " ORDER BY NameEN" : "SELECT * FROM CaveType ORDER BY NameEN");
			while (rs.next()) {
				CaveTypeEntry caveType = new CaveTypeEntry(rs.getInt("CaveTypeID"), rs.getString("NameEN"), rs.getString("DescriptionEN"),
						rs.getString("SketchName"));
				results.add(caveType);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return results;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveTypes brauchte "+diff + " Millisekunden.");;}}
		return results;

	}

	public int saveOrnamentEntry(OrnamentEntry ornamentEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von saveOrnamentEntry wurde ausgelöst.");;
		}
		int newOrnamentID = 0;
		Connection dbc = getConnection();
		PreparedStatement ornamentStatement;
		// deleteEntry("DELETE FROM Ornaments WHERE OrnamentID=" + ornamentEntry.getCode());
		try {
			ornamentStatement = dbc.prepareStatement("INSERT INTO Ornaments (Code, Description, Remarks, Interpretation, OrnamentReferences, OrnamentClassID, StructureOrganizationID, IconographyID, MasterImageID) VALUES (?, ?, ?, ?, ?, ?, ?,?,?)",
//					ornamentStatement = dbc.prepareStatement("INSERT INTO Ornaments (Code, Description, Remarks, Interpretation, OrnamentReferences, Annotation , OrnamentClassID, StructureOrganizationID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			ornamentStatement.setString(1, ornamentEntry.getCode());
			ornamentStatement.setString(2, ornamentEntry.getDescription());
			ornamentStatement.setString(3, ornamentEntry.getRemarks());
			ornamentStatement.setString(4, ornamentEntry.getInterpretation());
			ornamentStatement.setString(5, ornamentEntry.getReferences());
			//ornamentStatement.setString(6, ornamentEntry.getAnnotations());
			ornamentStatement.setInt(6, ornamentEntry.getOrnamentClass());
			ornamentStatement.setInt(7, ornamentEntry.getStructureOrganizationID());
			ornamentStatement.setInt(8, ornamentEntry.getIconographyID());
			ornamentStatement.setInt(9, ornamentEntry.getMasterImageID());
			ornamentStatement.executeUpdate();
			ResultSet keys = ornamentStatement.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				newOrnamentID = keys.getInt(1);
			}
			keys.close();

			updateInnerSecondaryPatternsRelations(newOrnamentID, ornamentEntry.getInnerSecondaryPatterns());
			updateOrnamentComponentsRelations(newOrnamentID, ornamentEntry.getOrnamentComponents());
			updateOrnamentImageRelations(newOrnamentID, ornamentEntry.getImages());
			updateCaveOrnamentRelation(newOrnamentID, ornamentEntry.getCavesRelations());
			writeOrnamenticBibliographyRelation(newOrnamentID, ornamentEntry.getRelatedBibliographyList());
			deleteEntry("DELETE FROM OrnamentIconographyRelation WHERE DepictionID=" + ornamentEntry.getOrnamentID());
			if (ornamentEntry.getRelatedIconographyList().size() > 0) {
				insertOrnamentIconographyRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getRelatedIconographyList());
			}
			ornamentStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		ornamentEntry.setOrnamentID(newOrnamentID);
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		ornamentEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(ornamentEntry,"");

		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von saveOrnamentEntry brauchte "+diff + " Millisekunden.");;}}
		return newOrnamentID;
	}
	
	/**
	 * @param oEntry
	 * @return
	 */
	public boolean updateOrnamentEntry(OrnamentEntry ornamentEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement ornamentStatement;
		try {
			ornamentStatement = dbc.prepareStatement("UPDATE Ornaments SET Code=?, Description=?, Remarks=?, Interpretation=?, OrnamentReferences=?, OrnamentClassID=?, StructureOrganizationID=?, IconographyID=?, MasterImageID=? WHERE OrnamentID=?");
			ornamentStatement.setString(1, ornamentEntry.getCode());
			ornamentStatement.setString(2, ornamentEntry.getDescription());
			ornamentStatement.setString(3, ornamentEntry.getRemarks());
			ornamentStatement.setString(4, ornamentEntry.getInterpretation());
			ornamentStatement.setString(5, ornamentEntry.getReferences());
			ornamentStatement.setInt(6, ornamentEntry.getOrnamentClass());
			ornamentStatement.setInt(7, ornamentEntry.getStructureOrganizationID());
			ornamentStatement.setInt(8, ornamentEntry.getIconographyID());
			ornamentStatement.setInt(9, ornamentEntry.getMasterImageID());
			ornamentStatement.setInt(10, ornamentEntry.getOrnamentID());
			ornamentStatement.executeUpdate();
			deleteEntry("DELETE FROM OrnamentIconographyRelation WHERE DepictionID=" + ornamentEntry.getOrnamentID());
			if (ornamentEntry.getRelatedIconographyList().size() > 0) {
				insertOrnamentIconographyRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getRelatedIconographyList());
			}
			updateInnerSecondaryPatternsRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getInnerSecondaryPatterns());
			updateOrnamentComponentsRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getOrnamentComponents());
			updateOrnamentImageRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getImages());
			updateCaveOrnamentRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getCavesRelations());
			
			System.err.println("writeOrnamenticBibliographyRelation triggered");
			writeOrnamenticBibliographyRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getRelatedBibliographyList());
			ornamentStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		ornamentEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(ornamentEntry,"");
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	private void updateOrnamentComponentsRelations(int ornamentID, List<OrnamentComponentsEntry> ornamentComponents) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentComponentsRelations wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		deleteEntry("DELETE FROM OrnamentComponentRelation WHERE OrnamentID=" + ornamentID);
		PreparedStatement stmt;
		try {
			for (int i = 0; i < ornamentComponents.size(); i++) {
				stmt = dbc.prepareStatement("INSERT INTO OrnamentComponentRelation (OrnamentID, OrnamentComponentID) VALUES (?,?)");
				stmt.setInt(1, ornamentID);
				stmt.setInt(2, ornamentComponents.get(i).getOrnamentComponentsID());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentComponentsRelations brauchte "+diff + " Millisekunden.");;}}
	}

	private void updateInnerSecondaryPatternsRelations(int ornamentID, List<InnerSecondaryPatternsEntry> innerSecPatterns) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateInnerSecondaryPatternsRelations wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		deleteEntry("DELETE FROM InnerSecondaryPatternRelation WHERE OrnamentID=" + ornamentID);
		PreparedStatement stmt;
		try {
			for (int i = 0; i < innerSecPatterns.size(); i++) {
				stmt = dbc.prepareStatement("INSERT INTO InnerSecondaryPatternRelation (OrnamentID, InnerSecID) VALUES (?,?)");
				stmt.setInt(1, ornamentID);
				stmt.setInt(2, innerSecPatterns.get(i).getInnerSecondaryPatternsID());
				stmt.executeUpdate();
			}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateInnerSecondaryPatternsRelations brauchte "+diff + " Millisekunden.");;}}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
	}

	/**
	 * @param newOrnamentID
	 * @param cavesRelations
	 */
	private void updateCaveOrnamentRelation(int ornamentID, List<OrnamentCaveRelation> cavesRelations) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateCaveOrnamentRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		int newCaveOrnamentRelationID = 0;
		deleteEntry("DELETE FROM CaveOrnamentRelation WHERE OrnamentID=" + ornamentID);
		PreparedStatement ornamentCaveRelationStatement;
		try {
			ornamentCaveRelationStatement = dbc.prepareStatement("INSERT INTO CaveOrnamentRelation "
					+ "(CaveID, OrnamentID, Colours, Notes, GroupOfOrnaments, SimilarElementsOfOtherCultures, StyleID) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			ornamentCaveRelationStatement.setInt(2, ornamentID);
			for (OrnamentCaveRelation ornamentCaveR : cavesRelations) {
				System.out.println(Integer.toString(ornamentCaveR.getCaveEntry().getCaveID()));
				ornamentCaveRelationStatement.setInt(1, ornamentCaveR.getCaveEntry().getCaveID());
				ornamentCaveRelationStatement.setString(3, ornamentCaveR.getColours());
				ornamentCaveRelationStatement.setString(4, ornamentCaveR.getNotes());
				ornamentCaveRelationStatement.setString(5, ornamentCaveR.getGroup());
				// ornamentCaveRelationStatement.setString(6, ornamentCaveR.getRelatedelementeofOtherCultures());
				ornamentCaveRelationStatement.setString(6, ornamentCaveR.getSimilarelementsOfOtherCultures());
				if (ornamentCaveR.getStyle() == null) {
					ornamentCaveRelationStatement.setInt(7, 0);
				} else {
					ornamentCaveRelationStatement.setInt(7, ornamentCaveR.getStyle().getStyleID());
				}
				ornamentCaveRelationStatement.executeUpdate();
				ResultSet keys = ornamentCaveRelationStatement.getGeneratedKeys();
				if (keys.next()) { // there should only be 1 key returned here
					newCaveOrnamentRelationID = keys.getInt(1);
				}
				keys.close();
				

				/*deleteEntry("DELETE FROM OrnamentOrientationRelation WHERE OrnamentCaveRelationID=" + newCaveOrnamentRelationID);
				PreparedStatement ornamentOrientationRelationStatement = dbc.prepareStatement("INSERT INTO OrnamentOrientationRelation (OrnamentCaveRelationID, OrientationID) VALUES (?, ?)");
				ornamentOrientationRelationStatement.setInt(1, newCaveOrnamentRelationID);
				for (OrientationEntry orientationEntry : ornamentCaveR.getOrientations()) {
					ornamentOrientationRelationStatement.setInt(2, orientationEntry.getOrientationID());
					ornamentOrientationRelationStatement.executeUpdate();
				}
				*/
				
				deleteEntry("DELETE FROM OrnamentCaveIconographyRelation WHERE OrnamentCaveRelationID=" + newCaveOrnamentRelationID);
				PreparedStatement ornamentCavePictorialRelationStatement = dbc.prepareStatement("INSERT INTO OrnamentCaveIconographyRelation (OrnamentCaveRelationID, IconographyID) VALUES (?, ?)");
				ornamentCavePictorialRelationStatement.setInt(1, newCaveOrnamentRelationID);
				for (IconographyEntry peEntry : ornamentCaveR.getIconographyElements()) {
					ornamentCavePictorialRelationStatement.setInt(2, peEntry.getIconographyID());
					ornamentCavePictorialRelationStatement.executeUpdate();
				}

				deleteEntry("DELETE FROM RelatedOrnamentsRelation WHERE OrnamentCaveRelationID=" + newCaveOrnamentRelationID);
				PreparedStatement relatedOrnamentsRelationStatement = dbc.prepareStatement("INSERT INTO RelatedOrnamentsRelation (OrnamentID, OrnamentCaveRelationID) VALUES (?, ?)");
				relatedOrnamentsRelationStatement.setInt(2, newCaveOrnamentRelationID);
				for (OrnamentEntry ornamentEntry : ornamentCaveR.getRelatedOrnamentsRelations()) {
					relatedOrnamentsRelationStatement.setInt(1, ornamentEntry.getOrnamentID());
					relatedOrnamentsRelationStatement.executeUpdate();
				}
				deleteEntry("DELETE FROM OrnamentCaveWallRelation WHERE OrnamentCaveRelationID=" + newCaveOrnamentRelationID);
				PreparedStatement wallCaveOrnamentRelationStatement = dbc.prepareStatement("INSERT INTO OrnamentCaveWallRelation (WallLocationID, PositionID, FunctionID, Notes, OrnamentCaveRelationID, CaveID) VALUES (?,?,?,?,?,?)");
				for (WallOrnamentCaveRelation we : ornamentCaveR.getWalls()) {
					if (we.getWall()!=null) {
					System.out.println(Integer.toString(we.getWall().getWallLocationID()));
					wallCaveOrnamentRelationStatement.setInt(1, we.getWall().getWallLocationID());
					wallCaveOrnamentRelationStatement.setInt(2, we.getOrnamenticPositionID());
					wallCaveOrnamentRelationStatement.setInt(3, we.getOrnamenticFunctionID());
					wallCaveOrnamentRelationStatement.setString(4, we.getNotes());
					wallCaveOrnamentRelationStatement.setInt(5, newCaveOrnamentRelationID);
					wallCaveOrnamentRelationStatement.setInt(6, we.getWall().getCaveID());
					wallCaveOrnamentRelationStatement.executeUpdate();
					}
				}
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateCaveOrnamentRelation brauchte "+diff + " Millisekunden.");;}}

	}

	private void updateOrnamentImageRelations(int ornamentID, ArrayList<ImageEntry> imgEntryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentImageRelations wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement oirStatement;
		try {
			// 1st we delete all relations
			deleteEntry("DELETE FROM OrnamentImageRelation WHERE OrnamentID=" + ornamentID);
			// now we add the existing ones
			oirStatement = dbc.prepareStatement("INSERT INTO OrnamentImageRelation (OrnamentID, ImageID) VALUES(?, ?)");
			oirStatement.setInt(1, ornamentID);
			for (ImageEntry entry : imgEntryList) {
				oirStatement.setInt(2, entry.getImageID());
				oirStatement.executeUpdate();
			}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateOrnamentImageRelations brauchte "+diff + " Millisekunden.");;}}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
	}
	
	/**
	 * This method allows selecting only used IconoghtaphyEntry elements
	 * @return List of IconographyEntry that are used in relation with pictorial elements
	 */
	protected Integer findIconographyRoot(Integer IcoID) {
		//System.out.println("FindIconographyRoot started for: " +Integer.toString(IcoID));
		IconographyEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE IconographyID=" + IcoID + " ORDER BY Text Asc");
			if (rs.first()) {
				result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (result!=null) {
			if (result.getParentID()==0) {
				return result.getIconographyID();
			}
			else {
				return findIconographyRoot(result.getParentID());
			}
		}
		else {
			return -1;
		}
	}
	public ArrayList<IconographyEntry> getIconographyEntriesUsedInDepictions() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Iconography WHERE IconographyID IN (SELECT DepictionIconographyRelation.IconographyID FROM DepictionIconographyRelation inner join Depictions on (DepictionIconographyRelation.DepictionID = Depictions.DepictionID) where Depictions.deleted=0) ORDER BY Text Asc");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntriesUsedInDepictions brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public IconographyEntry getIconographyEntry(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntry wurde ausgelöst.");;
		}
		IconographyEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE IconographyID=" + id + " ORDER BY Text Asc");
			if (rs.first()) {
				result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<IconographyEntry> getIconographyEntries(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE " + sqlWhere + " ORDER BY Text Asc");
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
				
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<IconographyEntry> getIconography(int rootIndex) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconography wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> root = getIconographyEntries(rootIndex);

		for (IconographyEntry item : root) {
			processIconographyTree(item);
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconography brauchte "+diff + " Millisekunden.");;}}
		return root;
	}

	protected void processIconographyTree(IconographyEntry parent) {
		ArrayList<IconographyEntry> children = getIconographyEntries(parent.getIconographyID());
		if (children != null) {
			parent.setChildren(children);
			for (IconographyEntry child : children) {
				processIconographyTree(child);
			}
		}
	}

	protected ArrayList<IconographyEntry> getIconographyEntries(int parentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String where = (parentID == 0) ? "IS NULL" : "= " + parentID;
//		System.out.println("SELECT * FROM Iconography WHERE ParentID " + where + " ORDER BY Text Asc");
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE ParentID " + where + " ORDER BY IconographyID Asc");
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}
  	public ArrayList<WallTreeEntry> getWallTree(int rootIndex) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconography wurde ausgelöst.");;
		}
		ArrayList<WallTreeEntry> root = getWallTreeEntries(rootIndex);

		for (WallTreeEntry item : root) {
			processWallEntryTree(item);
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconography brauchte "+diff + " Millisekunden.");;}}
		return root;
	}
	protected void processWallEntryTree(WallTreeEntry parent) {
		ArrayList<WallTreeEntry> children = getWallTreeEntries(parent.getWallLocationID());
		if (children != null) {
			parent.setChildren(children);
			for (WallTreeEntry child : children) {
				processWallEntryTree(child);
			}
		}
	}
	protected ArrayList<WallTreeEntry> getWallTreeEntries(int parentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries wurde ausgelöst.");;
		}
		ArrayList<WallTreeEntry> results = new ArrayList<WallTreeEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String where = (parentID == 0) ? "IS NULL" : "= " + parentID;
//		System.out.println("SELECT * FROM Iconography WHERE ParentID " + where + " ORDER BY Text Asc");
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM WallLocationsTree WHERE ParentID " + where + " ORDER BY Text Asc");
			while (rs.next()) {
				results.add(new WallTreeEntry(rs.getInt("WallLocationID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}
public boolean isHan(String s) {
	    for (int i = 0; i < s.length(); ) {
	        int codepoint = s.codePointAt(i);
	        i += Character.charCount(codepoint);
	        if ((codepoint>19967)&&(codepoint<40960)) {
	        	return true;
	        }
	    }
	    return false;
	}

	/**
	 * insert new IconogrpahyEntry in tree structure
	 * @param iconographyEntry
	 * @return
	 */
	public int insertIconographyEntry(IconographyEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertIconographyEntry wurde ausgelöst.");;
		}
		if (entry.getIconographyID() != 0 || entry.getParentID() == 0 || entry.getText().isEmpty()) { // otherwise this is not a new entry!
			return 0;
		}
		int newID = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("INSERT INTO Iconography (ParentID, Text, search) VALUES (?, ?,?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, entry.getParentID());
			System.out.println("Erg:");
			System.out.println(entry.getSearch()==null ? "<>" : entry.getSearch());
			System.out.println("---");
			pstmt.setString(2, entry.getText()==null ? "" : entry.getText());
			pstmt.setString(3, entry.getSearch()==null ? "" : entry.getSearch());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				newID = keys.getInt(1);
			}
			keys.close();
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertIconographyEntry brauchte "+diff + " Millisekunden.");;}}
		return newID;		
	}

	/**
	 * 
	 * @param iconographyEntryToEdit
	 * @return
	 */
	public boolean updateIconographyEntry(IconographyEntry iconographyEntryToEdit) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateIconographyEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("UPDATE Iconography SET ParentID=?, Text=?, search=? WHERE IconographyID =?");
			if (iconographyEntryToEdit.getParentID()==0) {
				pstmt.setNull(1, java.sql.Types.INTEGER);}
			else {
				pstmt.setInt(1, iconographyEntryToEdit.getParentID());
			}
			pstmt.setString(2, iconographyEntryToEdit.getText());
			pstmt.setString(3, iconographyEntryToEdit.getSearch());
			pstmt.setInt(4, iconographyEntryToEdit.getIconographyID());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateIconographyEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}
	public boolean iconographyIDisUsed(int iconographyID, int OrnamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von iconographyIDisUsed wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			System.out.println(Integer.toString(iconographyID)+" - "+Integer.toString(OrnamentID));
			pstmt = dbc.prepareStatement("SELECT IconographyID FROM Ornaments WHERE  Ornaments.deleted =0 and IconographyID=? and OrnamentID<>?");
			pstmt.setInt(1, iconographyID);
			pstmt.setInt(2, OrnamentID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()){
				System.out.println("found"+rs.toString());
				return true;
			}
			else{
				System.out.println("not found");
				return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			System.out.println("error");
			if (dologging){
		long end = System.currentTimeMillis();
			long diff = (end-start);
		if (diff>100){
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von iconographyIDisUsed brauchte "+diff + " Millisekunden.");;}}
			return false;
		}
	}

	@Deprecated
	public ArrayList<CurrentLocationEntry> getCurrentLocations() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCurrentLocations wurde ausgelöst.");;
		}
		ArrayList<CurrentLocationEntry> root = getCurrentLocationEntries(0);

		for (CurrentLocationEntry item : root) {
			processCurrentLocationTree(item);
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCurrentLocations brauchte "+diff + " Millisekunden.");;}}
		return root;
	}

	@Deprecated
	protected void processCurrentLocationTree(CurrentLocationEntry parent) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von processCurrentLocationTree wurde ausgelöst.");;
		}
		ArrayList<CurrentLocationEntry> children = getCurrentLocationEntries(parent.getCurrentLocationID());
		if (children != null) {
			parent.setChildren(children);
			for (CurrentLocationEntry child : children) {
				processCurrentLocationTree(child);
			}
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von processCurrentLocationTree brauchte "+diff + " Millisekunden.");;}}
	}

	@Deprecated
	protected ArrayList<CurrentLocationEntry> getCurrentLocationEntries(int parentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCurrentLocationEntries wurde ausgelöst.");;
		}
		ArrayList<CurrentLocationEntry> results = new ArrayList<CurrentLocationEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM CurrentLocations WHERE ParentID = ?");
			pstmt.setInt(1, parentID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(new CurrentLocationEntry(rs.getInt("CurrentLocationID"), rs.getInt("ParentID"), rs.getString("LocationName")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCurrentLocationEntries brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<VendorEntry> getVendors() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getVendors wurde ausgelöst.");;
		}
		ArrayList<VendorEntry> results = new ArrayList<VendorEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Vendors");
			while (rs.next()) {
				results.add(new VendorEntry(rs.getInt("VendorID"), rs.getString("VendorName")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getVendors brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public VendorEntry getVendor(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getVendor wurde ausgelöst.");;
		}
		VendorEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Vendors WHERE VendorID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new VendorEntry(rs.getInt("VendorID"), rs.getString("VendorName"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getVendor brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<StyleEntry> getStyles() {
		return getStyles(null);
	}

	public ArrayList<StyleEntry> getStyles(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStyles wurde ausgelöst.");;
		}
		ArrayList<StyleEntry> results = new ArrayList<StyleEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(sqlWhere != null ? "SELECT * FROM Styles WHERE " + sqlWhere + " ORDER BY StyleName Asc"
					: "SELECT * FROM Styles ORDER BY StyleName Asc");
			while (rs.next()) {
				results.add(new StyleEntry(rs.getInt("StyleID"), rs.getString("StyleName")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStyles brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public StyleEntry getStylebyID(int styleID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStylebyID wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		Statement stmt;
		StyleEntry result = null;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Styles WHERE StyleID = " + styleID);
			while (rs.next()) {
				result = new StyleEntry(rs.getInt("StyleID"), rs.getString("StyleName"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStylebyID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<ExpeditionEntry> getExpeditions() {
		return getExpeditions(null);
	}

	public ArrayList<ExpeditionEntry> getExpeditions(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getExpeditions wurde ausgelöst.");;
		}
		ArrayList<ExpeditionEntry> results = new ArrayList<ExpeditionEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(sqlWhere != null ? "SELECT * FROM Expeditions WHERE " + sqlWhere + " ORDER BY Name Asc"
					: "SELECT * FROM Expeditions ORDER BY Name Asc");
			while (rs.next()) {
				results.add(new ExpeditionEntry(rs.getInt("ExpeditionID"), rs.getString("Name"), rs.getString("Leader"), rs.getDate("StartDate"),
						rs.getDate("EndDate")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getExpeditions brauchte "+diff + " Millisekunden.");;}}
		return results;
	}
	
	public ExpeditionEntry getExpedition(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getExpedition wurde ausgelöst.");;
		}
		ExpeditionEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Expeditions WHERE ExpeditionID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new ExpeditionEntry(rs.getInt("ExpeditionID"), rs.getString("Name"), rs.getString("Leader"), rs.getDate("StartDate"), rs.getDate("EndDate"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getExpedition brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<PublicationEntry> getPublications() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublications wurde ausgelöst.");;
		}
		ArrayList<PublicationEntry> results = new ArrayList<PublicationEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publications");
			while (rs.next()) {
				results.add(new PublicationEntry(rs.getInt("PublicationID"), rs.getString("Editors"), rs.getString("Type"), rs.getString("DOI"),
						rs.getString("Pages"), rs.getDate("Year"), rs.getInt("PublisherID"), rs.getString("Title.English"),
						rs.getString("Title.Phonetic"), rs.getString("Title.Original"), rs.getString("Abstract")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublications brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public PublicationEntry getPublicationEntry(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationEntry wurde ausgelöst.");;
		}
		PublicationEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publications WHERE PublicationID=" + id);
			while (rs.next()) {
				result = new PublicationEntry(rs.getInt("PublicationID"), rs.getString("Editors"), rs.getString("Type"), rs.getString("DOI"),
						rs.getString("Pages"), rs.getDate("Year"), rs.getInt("PublisherID"), rs.getString("Title.English"),
						rs.getString("Title.Phonetic"), rs.getString("Title.Original"), rs.getString("Abstract"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublicationEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param publicationTypeID
	 * @return
	 */
	public AuthorEntry getAuthorEntry(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthorEntry wurde ausgelöst.");;
		}
		AuthorEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Authors WHERE AuthorID=" + id);
			if (rs.first()) {
				result = new AuthorEntry(rs.getInt("AuthorID"), rs.getString("Lastname"), rs.getString("Firstname"), rs.getString("Institution"),
						rs.getBoolean("KuchaVisitor"), rs.getString("Affiliation"), rs.getString("Email"), rs.getString("Homepage"), rs.getString("Alias"), 
						rs.getBoolean("InstitutionEnabled"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), rs.getString("altSpelling"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthorEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	@Deprecated
	public int getRelatedMasterImageID(int depictionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedMasterImageID wurde ausgelöst.");;
		}
		int result = 0;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM DepictionImageRelation WHERE DepictionID=" + depictionID + " ORDER BY ImageID");
			if (rs.first()) {
				result = rs.getInt("ImageID");
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedMasterImageID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * Gets the images related to the depictionID
	 * 
	 * @param depictionID
	 * @return
	 */
	private ArrayList<ImageEntry> getRelatedImages(int depictionID, String sessionID, int accessLevel) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedImages wurde ausgelöst.");;
		}
//		System.out.println("                -->  "+depictionID+" - "+sessionID+" - "+accessLevel);;
		accessLevel=-1;
		String inStatement = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		if (accessLevel==-1) {
			accessLevel = getAccessLevelForSessionID(sessionID);
		}
		switch (accessLevel) {
			case UserEntry.GUEST:
			case UserEntry.ASSOCIATED:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT;
				break; 
			case UserEntry.FULL:
			case UserEntry.ADMIN:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT + "," + AbstractEntry.ACCESS_LEVEL_PRIVATE;
				break;
		}
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();

		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Images WHERE deleted=0 and ImageID IN (SELECT ImageID FROM DepictionImageRelation WHERE DepictionID=?) AND AccessLevel IN (" + inStatement + ")");
			//System.out.println("SELECT * FROM Images WHERE ImageID IN (SELECT ImageID FROM DepictionImageRelation WHERE DepictionID="+depictionID+") AND AccessLevel IN (" + inStatement + ")");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				//System.out.println("ImageID = "+Integer.toString(depictionID)+" ImageID = "+Integer.toString(rs.getInt("ImageID")));
				ImageEntry image = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
						rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Height"),rs.getDouble("Width"));
				if (image.getLocation()==null) {
					if (rs.getString("Title")!=null){
						image.setLocation(searchLocationByFilename(image.getTitle()));
					}
				}
				if (image.getInventoryNumber()==null||image.getInventoryNumber()=="") {
					if (rs.getString("Title")!=null) {
						image.setInventoryNumber(searchInventoryNumberByFilename(image.getTitle()));
					}
				}

				results.add(image);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedImages brauchte "+diff + " Millisekunden.");;}}
//		System.out.println("Größe von relatedimages: "+results.size());
		return results;
	}

	public ArrayList<DepictionEntry> getAllDepictionsbyWall(int wallID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAllDepictionsbyWall wurde ausgelöst.");;
		}
		ArrayList<DepictionEntry> depictions = new ArrayList<DepictionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DepictionID, AbsoluteLeft, AbsoluteTop FROM Depictions WHERE WallID = " + wallID +" and deleted=0");
			while (rs.next()) {
				DepictionEntry depiction = new DepictionEntry();
				depiction.setDepictionID(rs.getInt("DepictionID"));
				depiction.setAbsoluteLeft(rs.getInt("AbsoluteLeft"));
				depiction.setAbsoluteTop(rs.getInt("AbsoluteTop"));

				depictions.add(depiction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAllDepictionsbyWall brauchte "+diff + " Millisekunden.");;}}
		return depictions;
	}

	public String saveDepiction(int depictionID, int AbsoluteLeft, int AbsoluteTop) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von saveDepiction wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("UPDATE Depictions SET AbsoluteLeft=?, AbsoluteTop=? WHERE DepictionID =?");
			pstmt.setInt(1, AbsoluteLeft);
			pstmt.setInt(2, AbsoluteTop);
			pstmt.setInt(3, depictionID);
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return "failed to save depiction";
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von saveDepiction brauchte "+diff + " Millisekunden.");;}}
		return "saved";
	}

	/**
	 * Updates the information of the entry given as parameter in the SQL database.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean updateImageEntry(ImageEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateImageEntry wurde ausgelöst.");;
		}
		System.out.println("Starting UpdateImageEntry");
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			if (entry.getImageAuthor() != null) {
				System.out.println("UPDATE Images SET Filename="+entry.getFilename()+", Title="+entry.getTitle()+", ShortName="+entry.getShortName()+", Copyright="+entry.getCopyright()+", PhotographerID="+Integer.toString(entry.getImageAuthor().getPhotographerID())+", Comment="+entry.getComment()+", Date="+entry.getDate()+", ImageTypeID="+Integer.toString(entry.getImageTypeID())+", AccessLevel="+Integer.toString(entry.getAccessLevel())+", deleted="+entry.isdeleted()+"  WHERE ImageID="+entry.getImageID());
			}
				else {
					System.out.println("UPDATE Images SET Filename="+entry.getFilename()+", Title="+entry.getTitle()+", ShortName="+entry.getShortName()+", Copyright="+entry.getCopyright()+", PhotographerID=0, Comment="+entry.getComment()+", Date="+entry.getDate()+", ImageTypeID="+Integer.toString(entry.getImageTypeID())+", AccessLevel="+Integer.toString(entry.getAccessLevel())+", deleted="+entry.isdeleted()+"  WHERE ImageID="+entry.getImageID());
				}
			pstmt = dbc.prepareStatement(
					"UPDATE Images SET Filename=?, Title=?, ShortName=?, Copyright=?, PhotographerID=?, Comment=?, Date=?, ImageTypeID=?, AccessLevel=?, location=?, deleted=?, InventoryNumber=?, Width=?, Height=? WHERE ImageID=?");
			pstmt.setString(1, entry.getFilename());
			pstmt.setString(2, entry.getTitle());
			pstmt.setString(3, entry.getShortName());
			pstmt.setString(4, entry.getCopyright());
			if (entry.getImageAuthor() != null) {
				pstmt.setInt(5, entry.getImageAuthor().getPhotographerID());				
			}
			else {
				pstmt.setInt(5, 0);
			}
			pstmt.setString(6, entry.getComment());
			pstmt.setString(7, entry.getDate());
			pstmt.setInt(8, entry.getImageTypeID());
			pstmt.setInt(9, entry.getAccessLevel());
			if (entry.getLocation() != null) {
				pstmt.setInt(10, entry.getLocation().getLocationID());				
			}
			else {
				pstmt.setInt(10, 0);
			}
			pstmt.setBoolean(11, entry.isdeleted());
			pstmt.setString(12, entry.getInventoryNumber());
			pstmt.setDouble(13, entry.getWidth());
			pstmt.setDouble(14, entry.getHeight());
			pstmt.setInt(15, entry.getImageID());
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		entry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(entry,"");
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateImageEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	/**
	 * 
	 * @return A list of all Regions as RegionEntry objects
	 */
	public ArrayList<RegionEntry> getRegions() {
		return getRegions(null);
	}

	/**
	 * 
	 * @param sqlWhere
	 * @return A list of all Regions mathing the where clause as RegionEntry objects
	 */
	public ArrayList<RegionEntry> getRegions(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRegions wurde ausgelöst.");;
		}
		ArrayList<RegionEntry> result = new ArrayList<RegionEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					sqlWhere != null ? "SELECT * FROM Regions WHERE " + sqlWhere + " ORDER BY EnglishName Asc, PhoneticName Asc, OriginalName Asc"
							: "SELECT * FROM Regions ORDER BY EnglishName Asc, PhoneticName Asc, OriginalName Asc");

			while (rs.next()) {
				result.add(new RegionEntry(rs.getInt("RegionID"), rs.getString("PhoneticName"), rs.getString("OriginalName"),
						rs.getString("EnglishName"), rs.getInt("SiteID")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRegions brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<SiteEntry> getSites() {
		return getSites(null);
	}

	/**
	 * 
	 * @param sqlWhere
	 * @return
	 */
	public ArrayList<SiteEntry> getSites(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSites wurde ausgelöst.");;
		}
		ArrayList<SiteEntry> result = new ArrayList<SiteEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt
					.executeQuery(sqlWhere != null ? "SELECT * FROM Sites WHERE " + sqlWhere + " ORDER BY Name Asc, AlternativeName Asc"
							: "SELECT * FROM Sites ORDER BY Name Asc, AlternativeName Asc");
			while (rs.next()) {
				result.add(new SiteEntry(rs.getInt("SiteID"), rs.getString("Name"), rs.getString("AlternativeName"), rs.getString("ShortName")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSites brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param publicationTypeID
	 * @return
	 */
	public SiteEntry getSite(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSite wurde ausgelöst.");;
		}
		SiteEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Sites WHERE SiteID=" + id);
			if (rs.first()) {
				result = new SiteEntry(rs.getInt("SiteID"), rs.getString("Name"), rs.getString("AlternativeName"), rs.getString("ShortName"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSite brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/*public ArrayList<OrientationEntry> getOrientations() {
		OrientationEntry result = null;
		ArrayList<OrientationEntry> orientations = new ArrayList<OrientationEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamenticOrientation");
			while (rs.next()) {
				result = new OrientationEntry(rs.getInt("OrnamenticOrientationID"), rs.getString("Name"));
				orientations.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		return orientations;
	}

	/**
	 * @return
	 */
	public ArrayList<OrientationEntry> getOrientationInformation() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrientationInformation wurde ausgelöst.");;
		}
		ArrayList<OrientationEntry> result = new ArrayList<OrientationEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrientationInformation");
			while (rs.next()) {
				result.add(new OrientationEntry(rs.getInt("OrientationID"), rs.getString("NameEN")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrientationInformation brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<MainTypologicalClass> getMainTypologicalClass() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getMainTypologicalClass wurde ausgelöst.");;
		}
		MainTypologicalClass result = null;
		ArrayList<MainTypologicalClass> maintypologicalclasses = new ArrayList<MainTypologicalClass>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MainTypologicalClass");
			while (rs.next()) {
				result = new MainTypologicalClass(rs.getInt("MainTypologicalClassID"), rs.getString("Name"));
				maintypologicalclasses.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getMainTypologicalClass brauchte "+diff + " Millisekunden.");;}}
		return maintypologicalclasses;
	}

	public MainTypologicalClass getMainTypologicalClassbyID(int maintypoID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getMainTypologicalClassbyID wurde ausgelöst.");;
		}
		MainTypologicalClass result = null;

		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM MainTypologicalClass WHERE MainTypologicalClassID = " + maintypoID);
			while (rs.next()) {
				result = new MainTypologicalClass(rs.getInt("MainTypologicalClassID"), rs.getString("Name"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getMainTypologicalClassbyID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<WallEntry> getWalls() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWalls wurde ausgelöst.");;
		}
		WallEntry result = null;
		ArrayList<WallEntry> walls = new ArrayList<WallEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Walls");
			while (rs.next()) {
				result = new WallEntry(rs.getInt("CaveID"), rs.getInt("WallLocationID"), rs.getInt("PreservationClassificationID"),
						rs.getDouble("Width"), rs.getDouble("Height"));
				walls.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWalls brauchte "+diff + " Millisekunden.");;}}
		return walls;
	}

	public WallEntry getWall(int caveID, int wallLocationID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWall wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		WallEntry result = null;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Walls WHERE CaveID=? AND WallLocationID=?");
			pstmt.setInt(1, caveID);
			pstmt.setInt(2, wallLocationID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new WallEntry(rs.getInt("CaveID"), rs.getInt("WallLocationID"), rs.getInt("PreservationClassificationID"),
						rs.getDouble("Width"), rs.getDouble("Height"));
			}
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWall brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<AnnotatedBibliographyEntry> searchAnnotatedBibliography(AnnotatedBibliographySearchEntry searchEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchAnnotatedBibliography wurde ausgelöst.");;
		}
		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String where = " deleted=0 ";
		if ((searchEntry.getAuthorSearch() != null) && !searchEntry.getAuthorSearch().isEmpty()) {
			String authorTerm = "";
			String editorTerm = "";
			for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
				authorTerm += authorTerm.isEmpty() 
						? "SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?) OR (altSpelling LIKE ?)))"
						: " INTERSECT SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?) OR (altSpelling LIKE ?)))";
				editorTerm += editorTerm.isEmpty() 
						? "SELECT BibID FROM EditorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?) OR (altSpelling LIKE ?)))"
						: " INTERSECT SELECT BibID FROM EditorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?) OR (altSpelling LIKE ?)))";
			}
			where = where + " and BibID IN (" + authorTerm + ") OR BibID IN (" + editorTerm + ")";
		}
		
		if (searchEntry.getPublisherSearch() != null && !searchEntry.getPublisherSearch().isEmpty()) {
			where += where.isEmpty() ? "Publisher LIKE ?" : "AND Publisher LIKE ?";
		}

		if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
			where += (where.isEmpty() ? "" : " AND ") + 
					"((TitleORG LIKE ?) OR (TitleEN LIKE ?) OR (TitleTR LIKE ?) OR (ParentTitleORG LIKE ?) OR (ParentTitleEN LIKE ?) OR (ParentTitleTR LIKE ?) OR "
					+ "(TitleAddonORG LIKE ?) OR (TitleAddonEN LIKE ?) OR (TitleAddonTR LIKE ?) OR (SubtitleORG LIKE ?) OR (SubtitleEN LIKE ?) OR (SubtitleTR LIKE ?))";
		}
		
		if (searchEntry.getYearSearch() > 0) {
			where += where.isEmpty() ? "YearORG LIKE ?" : " AND YearORG LIKE ?";
		}
		String bibIDs = "";
		for (Integer bibID : searchEntry.getBibIdList()) {
			bibIDs += bibIDs.isEmpty() ? Integer.toString(bibID) : "," + bibID;
		}
		if (!bibIDs.isEmpty()) {
			where += where.isEmpty() ? "BibID IN ("+bibIDs+") " : " AND BibID IN ("+bibIDs+") ";
		}

		/**
		 * We cannot filter the accessLevel because that would create problems e.g. when choosing a cave for a depiction.
		 * What we can do is restricting the visibility of certain fields e.g. comments but this has to be done 
		 * when the UI is build on the client side!
		 */
		String inStatement = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		switch (getAccessLevelForSessionID(searchEntry.getSessionID())) {
			case UserEntry.GUEST:
			case UserEntry.ASSOCIATED:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT;
				break; 
			case UserEntry.FULL:
			case UserEntry.ADMIN:
				inStatement += "," + AbstractEntry.ACCESS_LEVEL_COPYRIGHT + "," + AbstractEntry.ACCESS_LEVEL_PRIVATE;
				break;
		}
		where += where.isEmpty() ? "AccessLevel IN (" + inStatement + ")" : " AND AccessLevel IN (" + inStatement + ")";
		System.err.println(where.isEmpty() ? "SELECT * FROM AnnotatedBibliography" : "SELECT * FROM AnnotatedBibliography WHERE " + where);
		try {
			int i = 1;
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM AnnotatedBibliography" : "SELECT * FROM AnnotatedBibliography WHERE " + where);
			if ((searchEntry.getAuthorSearch() != null) && !searchEntry.getAuthorSearch().isEmpty()) {
				for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
					pstmt.setString(i++,name.replace("*", "%"));
					pstmt.setString(i++,name.replace("*", "%"));
					pstmt.setString(i++,name.replace("*", "%"));
					pstmt.setString(i++,name.replace("*", "%"));
					pstmt.setString(i++,name.replace("*", "%"));
				}
				for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
					pstmt.setString(i++, name.replace("*", "%"));
					pstmt.setString(i++, name.replace("*", "%"));
					pstmt.setString(i++, name.replace("*", "%"));
					pstmt.setString(i++, name.replace("*", "%"));
					pstmt.setString(i++, name.replace("*", "%"));
				}
			}
			if (searchEntry.getPublisherSearch() != null && !searchEntry.getPublisherSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getPublisherSearch().replace("*", "%"));
			}
			if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
				pstmt.setString(i++, searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
				pstmt.setString(i++,searchEntry.getTitleSearch().replace("*", "%"));
			}
			if (searchEntry.getYearSearch() > 0) {
				pstmt.setString(i++,Integer.toString(searchEntry.getYearSearch()).replace("*", "%"));
			}
			System.out.println(where.isEmpty() ? "SELECT * FROM AnnotatedBibliography" : "SELECT * FROM AnnotatedBibliography WHERE " + where);
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = new AnnotatedBibliographyEntry(rs.getInt("BibID"), getPublicationType(rs.getInt("PublicationTypeID")),
						rs.getString("TitleEN"), rs.getString("TitleORG"), rs.getString("TitleTR"), rs.getString("ParentTitleEN"),
						rs.getString("ParentTitleORG"), rs.getString("ParentTitleTR"), rs.getString("SubtitleEN"), rs.getString("SubtitleORG"),
						rs.getString("SubtitleTR"), rs.getString("UniversityEN"), rs.getString("UniversityORG"), rs.getString("UniversityTR"),
						rs.getString("NumberEN"), rs.getString("NumberORG"), rs.getString("NumberTR"), rs.getString("AccessDateEN"),
						rs.getString("AccessDateORG"), rs.getString("AccessDateTR"), rs.getString("TitleAddonEN"), rs.getString("TitleAddonORG"),
						rs.getString("TitleAddonTR"), rs.getString("Publisher"), rs.getString("SeriesEN"), rs.getString("SeriesORG"),
						rs.getString("SeriesTR"), rs.getString("EditionEN"), rs.getString("EditionORG"), rs.getString("EditionTR"),
						rs.getString("VolumeEN"), rs.getString("VolumeORG"), rs.getString("VolumeTR"), rs.getString("IssueEN"),
						rs.getString("IssueORG"), rs.getString("IssueTR"), rs.getInt("YearEN"), rs.getString("YearORG"), rs.getString("YearTR"),
						rs.getString("MonthEN"), rs.getString("MonthORG"), rs.getString("MonthTR"), rs.getString("PagesEN"), rs.getString("PagesORG"),
						rs.getString("PagesTR"), rs.getString("Comments"), rs.getString("Notes"), rs.getString("URL"), rs.getString("URI"),
						rs.getBoolean("Unpublished"), rs.getInt("FirstEditionBibID"), rs.getInt("AccessLevel"), rs.getString("AbstractText"),
						rs.getString("ThesisType"), rs.getString("EditorType"), rs.getBoolean("OfficialTitleTranslation"), rs.getString("BibTexKey"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), isHan(rs.getString("TitleORG")),null, rs.getBoolean("Annotation"));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				if ((entry.getBibtexKey().isEmpty())&(entry.getAuthorList().size()>0)) {
					if (!entry.getAuthorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getAuthorList().get(0), entry.getYearORG()));
					} else if (!entry.getEditorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getEditorList().get(0), entry.getYearORG()));
					}
					System.err.println("started Update updateAnnotatedBiblographyEntry due to updating BibtexKey");
					updateAnnotatedBiblographyEntry(entry);
				}
				entry.setAnnotation(bibHasAnnotation(entry.getUniqueID()));
				entry.setArticle(bibHasArticle(entry.getUniqueID()));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		} 
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchAnnotatedBibliography brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	public boolean bibHasAnnotation(String filename) {
		File inputFile = new File(serverProperties.getProperty("home.documents"), filename + "-annotation.pdf");
		if (inputFile.exists()) {
			//System.out.println(inputFile.getAbsolutePath()+" exists.");
		return true;}
		else {
			//System.out.println(inputFile.getAbsolutePath()+" doesn't exists.");
			return false;
		}
	}
	public boolean bibHasArticle(String filename) {
		File inputFile = new File(serverProperties.getProperty("home.documents"), filename + "-paper.pdf");
		if (inputFile.exists()) {
		return true;}
		else {
			return false;
		}
	}
	/**
	 * @param sqlWhere
	 * @return sorted list based on implementation of {@link #Comparable} in {@link #AnnotatedBibliographyEntry}
	 */
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBibliography wurde ausgelöst.");;
		}

		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					(sqlWhere == null) ? "SELECT * FROM AnnotatedBibliography Where deleted = 0" : "SELECT * FROM AnnotatedBibliography WHERE deleted = 0 and " + sqlWhere);
			while (rs.next()) {
				entry = new AnnotatedBibliographyEntry(rs.getInt("BibID"), getPublicationType(rs.getInt("PublicationTypeID")),
						rs.getString("TitleEN"), rs.getString("TitleORG"), rs.getString("TitleTR"), rs.getString("ParentTitleEN"),
						rs.getString("ParentTitleORG"), rs.getString("ParentTitleTR"), rs.getString("SubtitleEN"), rs.getString("SubtitleORG"),
						rs.getString("SubtitleTR"), rs.getString("UniversityEN"), rs.getString("UniversityORG"), rs.getString("UniversityTR"),
						rs.getString("NumberEN"), rs.getString("NumberORG"), rs.getString("NumberTR"), rs.getString("AccessDateEN"),
						rs.getString("AccessDateORG"), rs.getString("AccessDateTR"), rs.getString("TitleAddonEN"), rs.getString("TitleAddonORG"),
						rs.getString("TitleAddonTR"), rs.getString("Publisher"), rs.getString("SeriesEN"), rs.getString("SeriesORG"),
						rs.getString("SeriesTR"), rs.getString("EditionEN"), rs.getString("EditionORG"), rs.getString("EditionTR"),
						rs.getString("VolumeEN"), rs.getString("VolumeORG"), rs.getString("VolumeTR"), rs.getString("IssueEN"),
						rs.getString("IssueORG"), rs.getString("IssueTR"), rs.getInt("YearEN"), rs.getString("YearORG"), rs.getString("YearTR"),
						rs.getString("MonthEN"), rs.getString("MonthORG"), rs.getString("MonthTR"), rs.getString("PagesEN"), rs.getString("PagesORG"),
						rs.getString("PagesTR"), rs.getString("Comments"), rs.getString("Notes"), rs.getString("URL"), rs.getString("URI"),
						rs.getBoolean("Unpublished"), rs.getInt("FirstEditionBibID"), rs.getInt("AccessLevel"), rs.getString("AbstractText"),
						rs.getString("ThesisType"), rs.getString("EditorType"), rs.getBoolean("OfficialTitleTranslation"), rs.getString("BibTexKey"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), isHan(rs.getString("TitleORG")),null, rs.getBoolean("Annotation"));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				entry.setAnnotation(bibHasAnnotation(entry.getUniqueID()));
				entry.setArticle(bibHasArticle(entry.getUniqueID()));
				if ((entry.getBibtexKey().isEmpty())&(entry.getAuthorList().size()>0)) {
					if (!entry.getAuthorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getAuthorList().get(0), entry.getYearORG()));
					} else if (!entry.getEditorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getEditorList().get(0), entry.getYearORG()));
					}
					System.err.println("started Update updateAnnotatedBiblographyEntry due to updating BibtexKey");
					updateAnnotatedBiblographyEntry(entry);
				}
				result.add(entry);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		} 
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBibliography brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param authorList
	 * @return sorted list based on implementation of {@link #Comparable} in {@link #AnnotatedBibliographyEntry}
	 */
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliographyFromAuthors(ArrayList<AuthorEntry> authorList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBibliographyFromAuthors wurde ausgelöst.");;
		}
		AnnotatedBibliographyEntry entry;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		if (authorList.isEmpty()) {
			return result;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String authorIDs = "";
		for (AuthorEntry ae : authorList) {
			authorIDs += (authorIDs.length() > 0) ? "," + ae.getAuthorID() : ae.getAuthorID();
		}
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM AnnotatedBibliography WHERE deleted = 0  and AnnotatedBibliography.BibID IN (SELECT DISTINCT BibID FROM AuthorBibliographyRelation WHERE AuthorBibliographyRelation.AuthorID IN (" + authorIDs + "))"
				);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = new AnnotatedBibliographyEntry(rs.getInt("BibID"), getPublicationType(rs.getInt("PublicationTypeID")),
						rs.getString("TitleEN"), rs.getString("TitleORG"), rs.getString("TitleTR"), rs.getString("ParentTitleEN"),
						rs.getString("ParentTitleORG"), rs.getString("ParentTitleTR"), rs.getString("SubtitleEN"), rs.getString("SubtitleORG"),
						rs.getString("SubtitleTR"), rs.getString("UniversityEN"), rs.getString("UniversityORG"), rs.getString("UniversityTR"),
						rs.getString("NumberEN"), rs.getString("NumberORG"), rs.getString("NumberTR"), rs.getString("AccessDateEN"),
						rs.getString("AccessDateORG"), rs.getString("AccessDateTR"), rs.getString("TitleAddonEN"), rs.getString("TitleAddonORG"),
						rs.getString("TitleAddonTR"), rs.getString("Publisher"), rs.getString("SeriesEN"), rs.getString("SeriesORG"),
						rs.getString("SeriesTR"), rs.getString("EditionEN"), rs.getString("EditionORG"), rs.getString("EditionTR"),
						rs.getString("VolumeEN"), rs.getString("VolumeORG"), rs.getString("VolumeTR"), rs.getString("IssueEN"),
						rs.getString("IssueORG"), rs.getString("IssueTR"), rs.getInt("YearEN"), rs.getString("YearORG"), rs.getString("YearTR"),
						rs.getString("MonthEN"), rs.getString("MonthORG"), rs.getString("MonthTR"), rs.getString("PagesEN"), rs.getString("PagesORG"),
						rs.getString("PagesTR"), rs.getString("Comments"), rs.getString("Notes"), rs.getString("URL"), rs.getString("URI"),
						rs.getBoolean("Unpublished"), rs.getInt("FirstEditionBibID"), rs.getInt("AccessLevel"), rs.getString("AbstractText"),
						rs.getString("ThesisType"), rs.getString("EditorType"), rs.getBoolean("OfficialTitleTranslation"), rs.getString("BibTexKey"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), isHan(rs.getString("TitleORG")), null, rs.getBoolean("Annotation"));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				entry.setAnnotation(bibHasAnnotation(entry.getUniqueID()));
				entry.setArticle(bibHasArticle(entry.getUniqueID()));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBibliographyFromAuthors brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	private ArrayList<AnnotatedBibliographyEntry> getDepictionBibRelation(int depictionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDepictionBibRelation wurde ausgelöst.");;
		}
		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM DepictionBibliographyRelation WHERE DepictionID=?");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = getAnnotatedBiblographybyID(rs.getInt("BibID"), rs.getString("Page"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDepictionBibRelation brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBiblography() {
		return getAnnotatedBibliography(null);
	}

	/**
	 * @param annotatedBiblographyID
	 * @return
	 */
	private ArrayList<AuthorEntry> getEditorBibRelation(int annotatedBiblographyID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getEditorBibRelation wurde ausgelöst.");;
		}
		AuthorEntry entry = null;
		ArrayList<AuthorEntry> result = new ArrayList<AuthorEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM EditorBibliographyRelation WHERE BibID=? ORDER BY EditorSequence Asc");
			pstmt.setInt(1, annotatedBiblographyID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = getAuthorEntry(rs.getInt("AuthorID"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getEditorBibRelation brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param annotatedBiblographyID
	 * @return
	 */
	private ArrayList<AuthorEntry> getAuthorBibRelation(int annotatedBiblographyID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthorBibRelation wurde ausgelöst.");;
		}
		AuthorEntry entry = null;
		ArrayList<AuthorEntry> result = new ArrayList<AuthorEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM AuthorBibliographyRelation WHERE BibID=? ORDER BY AuthorSequence Asc");
			pstmt.setInt(1, annotatedBiblographyID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = getAuthorEntry(rs.getInt("AuthorID"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthorBibRelation brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	
	public AnnotatedBibliographyEntry getAnnotatedBiblographybyID(int bibID, String page) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBiblographybyID wurde ausgelöst.");;
		}
		AnnotatedBibliographyEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AnnotatedBibliography where deleted = 0 and BibID=" + bibID);
			if (rs.first()) {
				result = new AnnotatedBibliographyEntry(rs.getInt("BibID"), getPublicationType(rs.getInt("PublicationTypeID")),
						rs.getString("TitleEN"), rs.getString("TitleORG"), rs.getString("TitleTR"), rs.getString("ParentTitleEN"),
						rs.getString("ParentTitleORG"), rs.getString("ParentTitleTR"), rs.getString("SubtitleEN"), rs.getString("SubtitleORG"),
						rs.getString("SubtitleTR"), rs.getString("UniversityEN"), rs.getString("UniversityORG"), rs.getString("UniversityTR"),
						rs.getString("NumberEN"), rs.getString("NumberORG"), rs.getString("NumberTR"), rs.getString("AccessDateEN"),
						rs.getString("AccessDateORG"), rs.getString("AccessDateTR"), rs.getString("TitleAddonEN"), rs.getString("TitleAddonORG"),
						rs.getString("TitleAddonTR"), rs.getString("Publisher"), rs.getString("SeriesEN"), rs.getString("SeriesORG"),
						rs.getString("SeriesTR"), rs.getString("EditionEN"), rs.getString("EditionORG"), rs.getString("EditionTR"),
						rs.getString("VolumeEN"), rs.getString("VolumeORG"), rs.getString("VolumeTR"), rs.getString("IssueEN"),
						rs.getString("IssueORG"), rs.getString("IssueTR"), rs.getInt("YearEN"), rs.getString("YearORG"), rs.getString("YearTR"),
						rs.getString("MonthEN"), rs.getString("MonthORG"), rs.getString("MonthTR"), rs.getString("PagesEN"), rs.getString("PagesORG"),
						rs.getString("PagesTR"), rs.getString("Comments"), rs.getString("Notes"), rs.getString("URL"), rs.getString("URI"),
						rs.getBoolean("Unpublished"), rs.getInt("FirstEditionBibID"), rs.getInt("AccessLevel"), rs.getString("AbstractText"),
						rs.getString("ThesisType"), rs.getString("EditorType"), rs.getBoolean("OfficialTitleTranslation"), rs.getString("BibTexKey"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), isHan(rs.getString("TitleORG")), page, rs.getBoolean("Annotation"));
				result.setAuthorList(getAuthorBibRelation(result.getAnnotatedBibliographyID()));
				result.setEditorList(getEditorBibRelation(result.getAnnotatedBibliographyID()));
				result.setKeywordList(getRelatedBibKeywords(result.getAnnotatedBibliographyID()));
				result.setAnnotation(bibHasAnnotation(result.getUniqueID()));
				result.setArticle(bibHasArticle(result.getUniqueID()));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAnnotatedBiblographybyID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<StructureOrganization> getStructureOrganizations() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStructureOrganizations wurde ausgelöst.");;
		}
		StructureOrganization result = null;
		ArrayList<StructureOrganization> structureOrganizations = new ArrayList<StructureOrganization>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM StructureOrganization");
			while (rs.next()) {
				result = new StructureOrganization(rs.getInt("StructureOrganizationID"), rs.getString("Name"));
				structureOrganizations.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getStructureOrganizations brauchte "+diff + " Millisekunden.");;}}
		return structureOrganizations;
	}

	public ArrayList<OrnamentPositionEntry> getOrnamentPosition() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentPosition wurde ausgelöst.");;
		}
		OrnamentPositionEntry result = null;
		ArrayList<OrnamentPositionEntry> positions = new ArrayList<OrnamentPositionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamenticPosition");
			while (rs.next()) {
				result = new OrnamentPositionEntry(rs.getInt("OrnamenticPositionID"), rs.getString("Name"));
				positions.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentPosition brauchte "+diff + " Millisekunden.");;}}
		return positions;
	}
	public ArrayList<PositionEntry> getPosition() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentPosition wurde ausgelöst.");;
		}
		PositionEntry result = null;
		ArrayList<PositionEntry> positions = new ArrayList<PositionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Position");
			while (rs.next()) {
				result = new PositionEntry(rs.getInt("PositionID"), rs.getString("Name"));
				positions.add(result);
				//System.out.print(result.getPositionID()+" - "+result.getName());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPosition brauchte "+diff + " Millisekunden.");;}}
		return positions;
	}
	public ArrayList<OrnamentFunctionEntry> getOrnamentFunction() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentFunction wurde ausgelöst.");;
		}
		OrnamentFunctionEntry result = null;
		ArrayList<OrnamentFunctionEntry> functions = new ArrayList<OrnamentFunctionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamenticFunction");
			while (rs.next()) {
				result = new OrnamentFunctionEntry(rs.getInt("OrnamenticFunctionID"), rs.getString("Name"));
				functions.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentFunction brauchte "+diff + " Millisekunden.");;}}
		return functions;
	}

	public ArrayList<OrnamentCaveType> getOrnamentCaveTypes() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentCaveTypes wurde ausgelöst.");;
		}
		OrnamentCaveType result = null;
		ArrayList<OrnamentCaveType> cavetypes = new ArrayList<OrnamentCaveType>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamentCaveType");
			while (rs.next()) {
				result = new OrnamentCaveType(rs.getInt("OrnamentCaveTypeID"), rs.getString("Name"));
				cavetypes.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentCaveTypes brauchte "+diff + " Millisekunden.");;}}
		return cavetypes;
	}

	/**
	 * This method unfolds the searchEntry and creates the query incl. sophisticated WHERE clause
	 * @param searchEntry
	 * @return
	 */
	public ArrayList<OrnamentEntry> searchOrnaments(OrnamenticSearchEntry searchEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchOrnaments wurde ausgelöst.");;
		}
		System.out.println("Search Ornaments wurde gestartet.");
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<OrnamentEntry> resultList = new ArrayList<OrnamentEntry>();
		String where = "";
		if (searchEntry.getCode() != null && !searchEntry.getCode().isEmpty()) {
			where = "Code LIKE '"+searchEntry.getCode().replace("*", "%")+"'";
		}
		where += where.isEmpty() ? "Ornaments.deleted=0" : " AND Ornaments.deleted=0";
		String caveIDs="";
		DepictionSearchEntry depictionSearchEntry=new DepictionSearchEntry(searchEntry.getSessionID());
		for (CaveEntry cave : searchEntry.getCaves()) {
			System.out.println("Got Cave Number: "+Integer.toString(cave.getCaveID()));
			caveIDs += caveIDs.isEmpty() ? Integer.toString(cave.getCaveID()) : "," + Integer.toString(cave.getCaveID());
		}
		
		if (!caveIDs.isEmpty()) {
			where += where.isEmpty() ? "CaveID IN (" + caveIDs + ")" : " AND CaveID IN (" + caveIDs + ")";
		}		
		String iconographyIDs = "";
		for (IconographyEntry iconography : searchEntry.getIconography()) {
			iconographyIDs += iconographyIDs.isEmpty() ? Integer.toString(iconography.getIconographyID()) : "," + iconography.getIconographyID();
		}
		if (!iconographyIDs.isEmpty()) {
			where += where.isEmpty() 
					? " Ornaments.IconographyID IN ("+ iconographyIDs + ")"
					: " AND Ornaments.IconographyID IN ("+ iconographyIDs + ")";
		}
		String wallIDs = "";
		for (WallTreeEntry wall : searchEntry.getWalls()) {
			wallIDs += wallIDs.isEmpty() ? Integer.toString(wall.getWallLocationID()) : "," + wall.getWallLocationID();
		}
		if (!wallIDs.isEmpty()) {
			where += where.isEmpty() 
					? " DepictionWallsRelation.WallID IN ("+ wallIDs + ")"
					: " AND DepictionWallsRelation.WallID IN ("+ wallIDs + ")";
		}
		String districtsIDs = "";
		for (DistrictEntry district : searchEntry.getDistricts()) {
			districtsIDs += districtsIDs.isEmpty() ? Integer.toString(district.getDistrictID()) : "," + district.getDistrictID();
		}
		if (!districtsIDs.isEmpty()) {
			where += where.isEmpty() 
					? " Depictionsall.CaveID IN (Select CaveID from Caves where DistrictID in ("+districtsIDs+"))"
					: " AND Depictionsall.CaveID IN (Select CaveID from Caves where DistrictID in ("+districtsIDs+"))";
		}
		String imgIDs = "";
		for (int imgID : searchEntry.getImageIDList()) {
			imgIDs += imgIDs.isEmpty() ? Integer.toString(imgID) : "," + imgID;
		}
		if (!imgIDs.isEmpty()) {
			where += where.isEmpty() 
					? "Ornaments.OrnamentID IN (SELECT OrnamentID FROM OrnamentImageRelation WHERE ImageID IN (" + imgIDs + ")) "
					: " AND Ornaments.OrnamentID IN (SELECT OrnamentID FROM OrnamentImageRelation WHERE ImageID IN (" + imgIDs + ")) " ;
		}
		String bibIDs = "";
		for (int bibID : searchEntry.getBibIdList()) {
			bibIDs += bibIDs.isEmpty() ? Integer.toString(bibID) : "," + bibID;
		}
		if (!bibIDs.isEmpty()) {
			where += where.isEmpty() 
					? "Ornaments.OrnamentID IN (SELECT OrnamentID FROM OrnamentBibliographyRelation WHERE BibID IN (" + bibIDs + ")) "
					: " AND Ornaments.OrnamentID IN (SELECT OrnamentID FROM OrnamentBibliographyRelation WHERE BibID IN (" + bibIDs + ")) " ;
		}
		
//		for (int bibID : searchEntry.getBibIdList()) {
//			bibIDs += bibIDs.isEmpty() ? Integer.toString(bibID) : "," + bibID;
// 		}
//		if (!bibIDs.isEmpty()) {
//			where += where.isEmpty() 
//					? "DepictionID IN (SELECT DISTINCT DepictionID FROM DepictionBibliographyRelation WHERE BibID IN (" + bibIDs + "))" 
//					: " AND DepictionID IN (SELECT DISTINCT DepictionID FROM DepictionBibliographyRelation WHERE BibID IN (" + bibIDs + "))";
//		}
		String positions="";
		if (searchEntry.getPosition()!=null) {
			for (PositionEntry pe : searchEntry.getPosition()) {
				positions += positions.isEmpty() ? Integer.toString(pe.getPositionID()) : "," + pe.getPositionID();
			}
			if (!positions.isEmpty()) {
				where += where.isEmpty() 
						? " Position.PositionID IN ("+ positions + ")"
						: " AND Position.PositionID IN ("+ positions + ")";
			}
		}
		String components="";
		if (searchEntry.getComponents()!=null) {
			for (OrnamentComponentsEntry oce : searchEntry.getComponents()) {
				components += components.isEmpty() ? Integer.toString(oce.getOrnamentComponentsID()) : "," + oce.getOrnamentComponentsID();
			}
			if (!components.isEmpty()) {
				where += where.isEmpty() 
						? " OrnamentComponentRelation.OrnamentComponentID IN ("+ components + ")"
						: " AND OrnamentComponentRelation.OrnamentComponentID IN ("+ components + ")";
			}
		}
		if (searchEntry.getOrnamentClass()!=null) {
				where += where.isEmpty() 
						? " Ornaments.OrnamentClassID= "+searchEntry.getOrnamentClass().getOrnamentClassID()
						: " AND Ornaments.OrnamentClassID= "+searchEntry.getOrnamentClass().getOrnamentClassID();
			}
		if (!searchEntry.getDescription().isEmpty()) {
			where += where.isEmpty() 
					? " Ornaments.Description like '"+searchEntry.getDescription().replace("*", "%")+"'"
					: " AND Ornaments.Description like '"+searchEntry.getDescription().replace("*", "%")+"'";
			
		}
		if (!searchEntry.getRemarks().isEmpty()) {
			where += where.isEmpty() 
					? " Ornaments.Remarks like '"+searchEntry.getRemarks().replace("*", "%")+"'"
					: " AND Ornaments.Remarks like '"+searchEntry.getRemarks().replace("*", "%")+"'";
			
		}
		/**
		 * We cannot filter the accessLevel because that would create problems e.g. when choosing a cave for a depiction.
		 * What we can do is restricting the visibility of certain fields e.g. comments but this has to be done 
		 * when the UI is build on the client side!
		 */
		String inStatement  = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		System.err.println("Accesslevel ist: "+getAccessLevelForSessionID(searchEntry.getSessionID()));
		if (getAccessLevelForSessionID(searchEntry.getSessionID()) <= UserEntry.GUEST) {
			where += where.isEmpty() ? "AccessLevel IN (" + inStatement + ")" : " AND AccessLevel IN (" + inStatement + ")";
		}
		String sqlStatement=where.isEmpty() ? "SELECT DISTINCT Ornaments.* FROM Ornaments left join DepictionIconographyRelation on (Ornaments.IconographyID=DepictionIconographyRelation.IconographyID) left join (Select * from Depictions where Depictions.deleted=0) as Depictionsall on (DepictionIconographyRelation.DepictionID=Depictionsall.DepictionID) left join DepictionWallsRelation on (Depictionsall.DepictionID=DepictionWallsRelation.DepictionID) left join WallLocationsTree on (WallLocationsTree.WallLocationID = DepictionWallsRelation.WallID) \r\n" + 
				"left join WallPositionsRelation on (WallLocationsTree.WallLocationID=WallPositionsRelation.WallID and Depictionsall.DepictionID=WallPositionsRelation.DepictionID) left join Position on (WallPositionsRelation.PositionID=Position.PositionID) left join OrnamentComponentRelation on (OrnamentComponentRelation.OrnamentID = Ornaments.OrnamentID) ORDER BY Ornaments.Code LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+ ", "+Integer.toString(searchEntry.getMaxentries()): "SELECT DISTINCT Ornaments.* FROM Ornaments left join DepictionIconographyRelation on (Ornaments.IconographyID=DepictionIconographyRelation.IconographyID) left join (Select * from Depictions where Depictions.deleted=0) as Depictionsall on (DepictionIconographyRelation.DepictionID=Depictionsall.DepictionID) left join DepictionWallsRelation on (Depictionsall.DepictionID=DepictionWallsRelation.DepictionID) left join WallLocationsTree on (WallLocationsTree.WallLocationID = DepictionWallsRelation.WallID) \r\n" + 
				"left join WallPositionsRelation on (WallLocationsTree.WallLocationID=WallPositionsRelation.WallID and Depictionsall.DepictionID=WallPositionsRelation.DepictionID) left join Position on (WallPositionsRelation.PositionID=Position.PositionID) left join OrnamentComponentRelation on (OrnamentComponentRelation.OrnamentID = Ornaments.OrnamentID) WHERE " + where+" ORDER BY Ornaments.Code LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+", "+Integer.toString(searchEntry.getMaxentries());
		System.err.println(sqlStatement);

		try {
			pstmt = dbc.prepareStatement(sqlStatement);


			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				boolean found = false;
				for (OrnamentEntry oe : resultList) {
					if (oe.getOrnamentID()==rs.getInt("OrnamentID")){
						found=true;
						break;
					}
				}
				OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
						rs.getString("Remarks"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),rs.getInt("IconographyID"),rs.getInt("MasterImageID"), getOrnamentRelatedIconography(rs.getInt("OrnamentID")));
				resultList.add(entry);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchOrnaments brauchte "+diff + " Millisekunden.");;}}

		return resultList;
	}

	public ArrayList<OrnamentEntry> getOrnamentsWhere(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentsWhere wurde ausgelöst.");;
		}
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery((sqlWhere == null) ? "SELECT * FROM Ornaments WHERE Ornaments.deleted =0" : "SELECT * FROM Ornaments WHERE Ornaments.deleted =0 and  " + sqlWhere+" order by Code");
			//System.out.println((sqlWhere == null) ? "SELECT * FROM Ornaments WHERE Ornaments.deleted =0" : "SELECT * FROM Ornaments WHERE Ornaments.deleted =0 and  " + sqlWhere+" order by Code");
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						//rs.getString("Annotation"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),rs.getInt("IconographyID"),rs.getInt("MasterImageID"), getOrnamentRelatedIconography(rs.getInt("OrnamentID"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentsWhere brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public OrnamentEntry getOrnamentEntry(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentEntry wurde ausgelöst.");;
		}
		OrnamentEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		if (id == 0) {
			return result;
		}
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Ornaments WHERE Ornaments.deleted =0 and  OrnamentID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),rs.getInt("IconographyID"),rs.getInt("MasterImageID"), getOrnamentRelatedIconography(rs.getInt("OrnamentID")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<CeilingTypeEntry> getCeilingTypes() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCeilingTypes wurde ausgelöst.");;
		}
		ArrayList<CeilingTypeEntry> result = new ArrayList<CeilingTypeEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CeilingTypes ORDER BY Name Asc");
			while (rs.next()) {
				result.add(new CeilingTypeEntry(rs.getInt("CeilingTypeID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCeilingTypes brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public CeilingTypeEntry getCeilingType(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCeilingType wurde ausgelöst.");;
		}
		if (id == 0) {
			return null;
		}
		CeilingTypeEntry result = new CeilingTypeEntry();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CeilingTypes WHERE CeilingTypeID=" + id);
			if (rs.first()) {
				result = new CeilingTypeEntry(rs.getInt("CeilingTypeID"), rs.getString("Name"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCeilingType brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<PreservationClassificationEntry> getPreservationClassifications() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPreservationClassifications() wurde ausgelöst.");;
		}
		ArrayList<PreservationClassificationEntry> result = new ArrayList<PreservationClassificationEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PreservationClassifications");
			while (rs.next()) {
				result.add(new PreservationClassificationEntry(rs.getInt("PreservationClassificationID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPreservationClassifications brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	private PreservationClassificationEntry getPreservationClassification(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPreservationClassification wurde ausgelöst. ID= "+id);;}
		if (id == 0) {
			return null;
		}
		PreservationClassificationEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PreservationClassifications WHERE PreservationClassificationID="+id);
			while (rs.next()) {
				result = new PreservationClassificationEntry(rs.getInt("PreservationClassificationID"), rs.getString("Name"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPreservationClassification brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * TODO: only save existing areas depending on cave type!
	 * 
	 * @param caveEntry
	 * @return
	 */
	public synchronized boolean updateCaveEntry(CaveEntry caveEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateCaveEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"UPDATE Caves SET OfficialNumber=?, HistoricName=?, OptionalHistoricName=?, CaveTypeID=?, SiteID=?, DistrictID=?, "
							+ "RegionID=?, OrientationID=?, StateOfPreservation=?, Findings=?, Notes=?, FirstDocumentedBy=?, FirstDocumentedInYear=?, PreservationClassificationID=?, "
							+ "CaveGroupID=?, OptionalCaveSketch=?, CaveLayoutComments=?, HasVolutedHorseShoeArch=?, HasSculptures=?, HasClayFigures=?, HasImmitationOfMountains=?, "
							+ "HasHolesForFixationOfPlasticalItems=?, HasWoodenConstruction=?, AccessLevel=? WHERE CaveID=?");
			pstmt.setString(1, caveEntry.getOfficialNumber());
			pstmt.setString(2, caveEntry.getHistoricName());
			pstmt.setString(3, caveEntry.getOptionalHistoricName());
			pstmt.setInt(4, caveEntry.getCaveTypeID());
			pstmt.setInt(5, caveEntry.getSiteID());
			pstmt.setInt(6, caveEntry.getDistrictID());
			pstmt.setInt(7, caveEntry.getRegionID());
			pstmt.setInt(8, caveEntry.getOrientationID());
			pstmt.setString(9, caveEntry.getStateOfPerservation());
			pstmt.setString(10, caveEntry.getFindings());
			pstmt.setString(11, caveEntry.getNotes());
			pstmt.setString(12, caveEntry.getFirstDocumentedBy());
			pstmt.setInt(13, caveEntry.getFirstDocumentedInYear());
			pstmt.setInt(14, caveEntry.getPreservationClassificationID());
			pstmt.setInt(15, caveEntry.getCaveGroupID());
			pstmt.setString(16, caveEntry.getOptionalCaveSketch());
			pstmt.setString(17, caveEntry.getCaveLayoutComments());
			pstmt.setBoolean(18, caveEntry.isHasVolutedHorseShoeArch());
			pstmt.setBoolean(19, caveEntry.isHasSculptures());
			pstmt.setBoolean(20, caveEntry.isHasClayFigures());
			pstmt.setBoolean(21, caveEntry.isHasImmitationOfMountains());
			pstmt.setBoolean(22, caveEntry.isHasHolesForFixationOfPlasticalItems());
			pstmt.setBoolean(23, caveEntry.isHasWoodenConstruction());
			pstmt.setInt(24, caveEntry.getAccessLevel());
			pstmt.setInt(25, caveEntry.getCaveID());
			pstmt.executeUpdate();
			pstmt.close();
//			for (CaveAreaEntry caEntry : caveEntry.getCaveAreaList()) {
//				writeCaveArea(caEntry);
//			}
			for (WallEntry wEntry : caveEntry.getWallList()) {
				writeWall(wEntry);
			}
			writeC14AnalysisUrlEntry(caveEntry.getCaveID(), caveEntry.getC14AnalysisUrlList());
			writeC14DocumentEntry(caveEntry.getCaveID(), caveEntry.getC14DocumentList());
			writeCaveBibliographyRelation(caveEntry.getCaveID(), caveEntry.getRelatedBibliographyList());
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		caveEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(caveEntry,"");
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateCaveEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	/**
	 * @param caveEntry
	 * @return
	 */
	public synchronized int insertCaveEntry(CaveEntry caveEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveEntry wurde ausgelöst.");;
		}
		int newCaveID = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"INSERT INTO Caves (OfficialNumber, HistoricName, OptionalHistoricName, CaveTypeID, SiteID, DistrictID, RegionID, OrientationID, StateOfPreservation, "
							+ "Findings, Notes, FirstDocumentedBy, FirstDocumentedInYear, PreservationClassificationID, CaveGroupID, OptionalCaveSketch, CaveLayoutComments, HasVolutedHorseShoeArch, "
							+ "HasSculptures, HasClayFigures, HasImmitationOfMountains, HasHolesForFixationOfPlasticalItems, HasWoodenConstruction, AccessLevel) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, caveEntry.getOfficialNumber());
			pstmt.setString(2, caveEntry.getHistoricName());
			pstmt.setString(3, caveEntry.getOptionalHistoricName());
			pstmt.setInt(4, caveEntry.getCaveTypeID());
			pstmt.setInt(5, caveEntry.getSiteID());
			pstmt.setInt(6, caveEntry.getDistrictID());
			pstmt.setInt(7, caveEntry.getRegionID());
			pstmt.setInt(8, caveEntry.getOrientationID());
			pstmt.setString(9, caveEntry.getStateOfPerservation());
			pstmt.setString(10, caveEntry.getFindings());
			pstmt.setString(11, caveEntry.getNotes());
			pstmt.setString(12, caveEntry.getFirstDocumentedBy());
			pstmt.setInt(13, caveEntry.getFirstDocumentedInYear());
			pstmt.setInt(14, caveEntry.getPreservationClassificationID());
			pstmt.setInt(15, caveEntry.getCaveGroupID());
			pstmt.setString(16, caveEntry.getOptionalCaveSketch());
			pstmt.setString(17, caveEntry.getCaveLayoutComments());
			pstmt.setBoolean(18, caveEntry.isHasVolutedHorseShoeArch());
			pstmt.setBoolean(19, caveEntry.isHasSculptures());
			pstmt.setBoolean(20, caveEntry.isHasClayFigures());
			pstmt.setBoolean(21, caveEntry.isHasImmitationOfMountains());
			pstmt.setBoolean(22, caveEntry.isHasHolesForFixationOfPlasticalItems());
			pstmt.setBoolean(23, caveEntry.isHasWoodenConstruction());
			pstmt.setInt(24, caveEntry.getAccessLevel());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				newCaveID = keys.getInt(1);
			}

			keys.close();
			pstmt.close();

			if (newCaveID > 0) {
				for (CaveAreaEntry caEntry : caveEntry.getCaveAreaList()) {
					caEntry.setCaveID(newCaveID);
					writeCaveArea(caEntry);
				}
				for (WallEntry wEntry : caveEntry.getWallList()) {
					wEntry.setCaveID(newCaveID);
					writeWall(wEntry);
				}
				writeC14AnalysisUrlEntry(newCaveID, caveEntry.getC14AnalysisUrlList());
				writeC14DocumentEntry(newCaveID, caveEntry.getC14DocumentList());
				writeCaveBibliographyRelation(caveEntry.getCaveID(), caveEntry.getRelatedBibliographyList());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		caveEntry.setCaveID(newCaveID);
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		caveEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(caveEntry,"");
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveEntry brauchte "+diff + " Millisekunden.");;}}
		return newCaveID;
	}

	/**
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized int insertCaveSketchEntry(CaveSketchEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveSketchEntry wurde ausgelöst.");;
		}
		int newCaveSketchID = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"INSERT INTO CaveSketches (CaveID, ImageType) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, entry.getCaveID());
			pstmt.setString(2, entry.getImageType());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				newCaveSketchID = keys.getInt(1);
			}

			keys.close();
			pstmt.close();

		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveSketchEntry brauchte "+diff + " Millisekunden.");;}}
		return newCaveSketchID;
	}
	
	private ArrayList<CaveSketchEntry> getCaveSketchEntriesFromCave(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveSketchEntriesFromCave wurde ausgelöst.");;
		}
		ArrayList<CaveSketchEntry> results = new ArrayList<CaveSketchEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CaveSketches WHERE CaveID=" + caveID);
			CaveSketchEntry cse;
			while (rs.next()) {
				cse = new CaveSketchEntry(rs.getInt("CaveSketchID"), caveID, rs.getString("ImageType"));
				results.add(cse);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveSketchEntriesFromCave brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	/**
	 * @return
	 */
	public ArrayList<CaveGroupEntry> getCaveGroups() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveGroups wurde ausgelöst.");;
		}
		ArrayList<CaveGroupEntry> result = new ArrayList<CaveGroupEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CaveGroups");
			while (rs.next()) {
				result.add(new CaveGroupEntry(rs.getInt("CaveGroupID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveGroups brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public synchronized UserEntry userLogin(String username, String password) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von userLogin wurde ausgelöst.");;
		}
		String newSessionID = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		UserEntry result = null;
		
		try {
			if (username.contains("@")) {
				pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE Email=? AND Password=?");
			} else {
				pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE Username=? AND Password=?");
			}
			pstmt.setString(1, username);
			pstmt.setString(2, password);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				System.err.println("user logged in sucessfully");
				newSessionID = UUID.randomUUID().toString();
				updateSessionIDforUser(username, newSessionID);
				result = new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), newSessionID, 
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			} else {
				System.err.println("wrong password for user " + username + ": hash = " + password);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von userLogin brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public UserEntry getUser(String username) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getUser wurde ausgelöst.");;
		}
		System.out.println("getUser called for username = " + username);
		if (username == null || username.isEmpty()) {
			return null;
		}
		UserEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE Username = ?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), rs.getString("SessionID"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			} else {
				System.err.println("no user " + username + " existing");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getUser brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	private UserEntry getUser(int userID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getUser wurde ausgelöst.");;
		}
		if (userID == 0) {
			return null;
		}
		UserEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE UserID = ?");
			pstmt.setInt(1, userID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), rs.getString("SessionID"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			} else {
				System.err.println("no user " + userID + " existing");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getUser brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public String getSessionIDfromUser(String username) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSessionIDfromUser wurde ausgelöst.");;
		}
		String sessionID = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT SessionID FROM Users WHERE Username=?");
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				sessionID = rs.getString("SessionID");
			} else {
				System.err.println("no user " + username + " existing");
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getSessionIDfromUser brauchte "+diff + " Millisekunden.");;}}
		return sessionID;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public int getAccessLevelForSessionID(String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAccessLevelForSessionID wurde ausgelöst.");;
		}
		int accessRights = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		//System.err.println("getAccessLevelForSessionID(" + sessionID + ")");
		try {
			pstmt = dbc.prepareStatement("SELECT AccessLevel FROM Users WHERE SessionID=?");
			pstmt.setString(1, sessionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				accessRights = rs.getInt("AccessLevel");
				//System.err.println("accessLevel=" + accessRights);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			return 0;
		}

		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAccessLevelForSessionID brauchte "+diff + " Millisekunden.");;}}
		return accessRights;
	}

	/**
	 * 
	 * @param username
	 * @param sessionID
	 */
	public void updateSessionIDforUser(String username, String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateSessionIDforUser wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			if (username.contains("@")) {
				pstmt = dbc.prepareStatement("UPDATE Users SET SessionID=? WHERE Email=?");
			} else {
				pstmt = dbc.prepareStatement("UPDATE Users SET SessionID=? WHERE Username=?");
			}
			pstmt.setString(1, sessionID);
			pstmt.setString(2, username);
//			pstmt.executeUpdate();
			if (pstmt.executeUpdate() > 0) // temporary check 
				System.err.println("updated sessionID for user " + username + " to " + sessionID);
			pstmt.close();
		} catch (SQLException e) {
			System.err.println(e.getLocalizedMessage());
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateSessionIDforUser brauchte "+diff + " Millisekunden.");;}}

	}
	
	public UserEntry checkSessionID(String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von checkSessionID wurde ausgelöst.");;
		}
		if (sessionID == null) {
			return null;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		UserEntry result = null;
		
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE SessionID=?");
			pstmt.setString(1, sessionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), rs.getString("SessionID"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von checkSessionID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
 
	/**
	 * @param sessionID
	 * @param username
	 * @return UserEntry if user & sessionID combination exists, null else
	 */
	public UserEntry checkSessionID(String sessionID, String username) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von checkSessionID wurde ausgelöst.");;
		}
		if ((sessionID == null) || (username == null)) {
			return null;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		UserEntry result = null;
		
		System.err.println("sessionID = " + sessionID);
		System.err.println("username = " + username);
		
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Users WHERE SessionID=? AND Username=?");
			pstmt.setString(1, sessionID);
			pstmt.setString(2, username);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), rs.getString("SessionID"),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von checkSessionID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<ImageTypeEntry> getImageTypes() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageTypes wurde ausgelöst.");;
		}
		ArrayList<ImageTypeEntry> result = new ArrayList<ImageTypeEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ImageTypes");
			while (rs.next()) {
				result.add(new ImageTypeEntry(rs.getInt("ImageTypeID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImageTypes brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param iconographyLists
	 * @param list
	 * @param depictionEntry
	 * @return
	 */
	public synchronized int insertDepictionEntry(DepictionEntry de, ArrayList<IconographyEntry> iconographyLists) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionEntry wurde ausgelöst.");;
		}
		int newDepictionID = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		de.setModifiedOn(df.format(date));
		try {
			pstmt = dbc.prepareStatement(
					"INSERT INTO Depictions (StyleID, Inscriptions, SeparateAksaras, Dating, Description, BackgroundColour, GeneralRemarks, "
							+ "OtherSuggestedIdentifications, Width, Height, ExpeditionID, PurchaseDate, CurrentLocationID, InventoryNumber, VendorID, "
							+ "StoryID, CaveID, WallID, AbsoluteLeft, AbsoluteTop, ModeOfRepresentationID, ShortName, PositionNotes, MasterImageID, AccessLevel, LastChangedByUser, LastChangedOnDate) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, de.getStyleID());
			pstmt.setString(2, de.getInscriptions());
			pstmt.setString(3, de.getSeparateAksaras());
			pstmt.setString(4, de.getDating());
			pstmt.setString(5, de.getDescription());
			pstmt.setString(6, de.getBackgroundColour());
			pstmt.setString(7, de.getGeneralRemarks());
			pstmt.setString(8, de.getOtherSuggestedIdentifications());
			pstmt.setDouble(9, de.getHeight());
			pstmt.setDouble(10, de.getWidth());
			pstmt.setInt(11, de.getExpedition() != null ? de.getExpedition().getExpeditionID() : 0);
			pstmt.setDate(12, de.getPurchaseDate());
			pstmt.setInt(13, de.getLocation() != null ? de.getLocation().getLocationID() : 0);
			pstmt.setString(14, de.getInventoryNumber());
			pstmt.setInt(15, de.getVendor() != null ? de.getVendor().getVendorID() : 0);
			pstmt.setInt(16, de.getStoryID());
			pstmt.setInt(17, de.getCave() != null ? de.getCave().getCaveID() : 0);
			pstmt.setInt(18, de.getWallID());
			pstmt.setInt(19, de.getAbsoluteLeft());
			pstmt.setInt(20, de.getAbsoluteTop());
			pstmt.setInt(21, de.getModeOfRepresentationID());
			pstmt.setString(22, de.getShortName());
			pstmt.setString(23, de.getPositionNotes());
			pstmt.setInt(24, de.getMasterImageID());
			pstmt.setInt(25, de.getAccessLevel());
			pstmt.setString(26, de.getLastChangedByUser());
			pstmt.setString(27, de.getModifiedOn());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				newDepictionID = keys.getInt(1);
				de.setDepictionID(newDepictionID);
			}
			keys.close();
			pstmt.close();
			protocollModifiedAbstractEntry(de,"");
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
		deleteEntry("DELETE FROM DepictionImageRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getRelatedImages().size() > 0) {
			insertDepictionImageRelation(de.getDepictionID(), de.getRelatedImages());
		}
		deleteEntry("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (iconographyLists.size() > 0) {
			insertDepictionIconographyRelation(de.getDepictionID(), iconographyLists);
		}
		deleteEntry("DELETE FROM DepictionBibliographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getRelatedBibliographyList().size() > 0) {
			insertDepictionBibliographyRelation(de.getDepictionID(), de.getRelatedBibliographyList());
		}
		deleteEntry("DELETE FROM DepictionWallsRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM WallPositionsRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getWalls().size() > 0) {
			insertDepictionWallsRelation(de.getDepictionID(), de.getWalls());
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionEntry brauchte "+diff + " Millisekunden.");;}}
		return newDepictionID;
	}

	/**
	 * @param correspondingDepictionEntry
	 * @param imgEntryList
	 * @param iconographyList
	 * @return <code>true</code> when operation is successful
	 */
	public synchronized boolean updateDepictionEntry(DepictionEntry de, ArrayList<IconographyEntry> iconographyList, String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateDepictionEntry wurde ausgelöst.");;
		}
		System.err.println("==> updateDepictionEntry called");
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		de.setModifiedOn(df.format(date));
//		DepictionEntry oldDe = getDepictionEntry(de.getDepictionID(), sessionID);
		String changes="";
//		changes= de.getchanges(oldDe);

//		System.err.println("Tracked Changes are: "+changes);
		try {
			System.err.println("===> updateDepictionEntry ID = " + de.getDepictionID());
			pstmt = dbc.prepareStatement(
					"UPDATE Depictions SET StyleID=?, Inscriptions=?, SeparateAksaras=?, Dating=?, Description=?, BackgroundColour=?, GeneralRemarks=?, "
							+ "OtherSuggestedIdentifications=?, Width=?, Height=?, ExpeditionID=?, PurchaseDate=?, CurrentLocationID=?, InventoryNumber=?, VendorID=?, "
							+ "StoryID=?, CaveID=?, WallID=?, AbsoluteLeft=?, AbsoluteTop=?, ModeOfRepresentationID=?, ShortName=?, PositionNotes=?, MasterImageID=?, AccessLevel=?, LastChangedByUser=?, "
							+ "LastChangedOnDate=?, deleted=? WHERE DepictionID=?");
			pstmt.setInt(1, de.getStyleID());
			pstmt.setString(2, de.getInscriptions());
			pstmt.setString(3, de.getSeparateAksaras());
			pstmt.setString(4, de.getDating());
			pstmt.setString(5, de.getDescription());
			pstmt.setString(6, de.getBackgroundColour());
			pstmt.setString(7, de.getGeneralRemarks());
			pstmt.setString(8, de.getOtherSuggestedIdentifications());
			pstmt.setDouble(9, de.getHeight());
			pstmt.setDouble(10, de.getWidth());
			pstmt.setInt(11, de.getExpedition() != null ? de.getExpedition().getExpeditionID() : 0);
			pstmt.setDate(12, de.getPurchaseDate());
			pstmt.setInt(13, de.getLocation() != null ? de.getLocation().getLocationID() : 0);
			pstmt.setString(14, de.getInventoryNumber());
			pstmt.setInt(15, de.getVendor() != null ? de.getVendor().getVendorID() : 0);
			pstmt.setInt(16, de.getStoryID());
			pstmt.setInt(17, de.getCave() != null ? de.getCave().getCaveID() : 0);
			pstmt.setInt(18, de.getWallID());
			pstmt.setInt(19, de.getAbsoluteLeft());
			pstmt.setInt(20, de.getAbsoluteTop());
			pstmt.setInt(21, de.getModeOfRepresentationID());
			pstmt.setString(22, de.getShortName());
			pstmt.setString(23, de.getPositionNotes());
			pstmt.setInt(24, de.getMasterImageID());
			pstmt.setInt(25, de.getAccessLevel());
			pstmt.setString(26, de.getLastChangedByUser());
			pstmt.setString(27, de.getModifiedOn());
			pstmt.setBoolean(28, de.isdeleted());
			pstmt.setInt(29, de.getDepictionID());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException ex) {
			System.err.println(ex.getLocalizedMessage());
			return false;
		}
//		System.err.println("DELETE FROM DepictionImageRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM DepictionImageRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getRelatedImages().size() > 0) {
			insertDepictionImageRelation(de.getDepictionID(), de.getRelatedImages());
		}
		deleteEntry("DELETE FROM DepictionWallsRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM WallPositionsRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getWalls().size() > 0) {
			insertDepictionWallsRelation(de.getDepictionID(), de.getWalls());
		}//		System.err.println("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
//		deleteEntry("DELETE FROM WallPositionsRelation WHERE DepictionID=" + de.getDepictionID());
//		for ( WallTreeEntry wall : de.getWalls()) {
//			if (wall.getPosition().size() > 0) {
//				insertDepictionWallsRelation(de.getDepictionID(), wall.getWallLocationID(), wall.getPosition());
//			}//		System.err.println("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
//		}
		deleteEntry("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (iconographyList.size() > 0) {
			insertDepictionIconographyRelation(de.getDepictionID(), iconographyList);
		}
//		System.err.println("DELETE FROM DepictionBibliographyRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM DepictionBibliographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getRelatedBibliographyList().size() > 0) {
			insertDepictionBibliographyRelation(de.getDepictionID(), de.getRelatedBibliographyList());
		}
		protocollModifiedAbstractEntry(de, changes);
		serializeAllDepictionEntries(sessionID);
		System.err.println("==> updateDepictionEntry finished");
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateDepictionEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	private synchronized void insertDepictionImageRelation(int depictionID, ArrayList<ImageEntry> imgEntryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		// System.err.println("==> updateDepictionImageRelation called");
		try {
			pstmt = dbc.prepareStatement("INSERT INTO DepictionImageRelation VALUES (?, ?)");
			for (ImageEntry entry : imgEntryList) {
				pstmt.setInt(1, depictionID);
				pstmt.setInt(2, entry.getImageID());
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation brauchte "+diff + " Millisekunden.");;}}
	}
	private synchronized void insertWallPositionsRelation(int depictionID, int wallID, ArrayList<PositionEntry> positions) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		System.err.println("==> WallPositionsRelation called for DepictionID "+Integer.toString(depictionID)+" WallID "+Integer.toString(wallID)+" Size of Positions: "+Integer.toString(positions.size()));
		try {
			pstmt = dbc.prepareStatement("INSERT INTO WallPositionsRelation VALUES (?, ?,?)");
			for (PositionEntry entry : positions) {
				System.out.println("Setze Position für DepictionID: "+depictionID+" WallID:"+wallID+ " Position: "+entry.getName());
				pstmt.setInt(1, depictionID);
				pstmt.setInt(2, wallID);
				pstmt.setInt(3, entry.getPositionID());
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation brauchte "+diff + " Millisekunden.");;}}
	}
	private synchronized void insertDepictionWallsRelation(int depictionID, List<WallTreeEntry> wallsEntryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		System.err.println("==> insertDepictionWallsRelation called");
		try {
			pstmt = dbc.prepareStatement("INSERT INTO DepictionWallsRelation VALUES (?, ?)");
			for (WallTreeEntry entry : wallsEntryList) {
				pstmt.setInt(1, depictionID);
				pstmt.setInt(2, entry.getWallLocationID());
				pstmt.executeUpdate();
				if (entry.getPosition() !=null) {
					insertWallPositionsRelation(depictionID, entry.getWallLocationID(), entry.getPosition());
				}


			}
			pstmt.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionImageRelation brauchte "+diff + " Millisekunden.");;}}
	}
	private synchronized void insertDepictionIconographyRelation(int depictionID, ArrayList<IconographyEntry> iconographyList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionIconographyRelation wurde ausgelöst.");;
		}
		System.err.println("InsertIconography started");
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		try {
			relationStatement = dbc.prepareStatement("INSERT INTO DepictionIconographyRelation VALUES (?, ?)");
			for (IconographyEntry entry : iconographyList) {
				relationStatement.setInt(1, depictionID);
				relationStatement.setInt(2, entry.getIconographyID());
				relationStatement.executeUpdate();
			}
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionIconographyRelation brauchte "+diff + " Millisekunden.");;}}

	}
	private synchronized void insertOrnamentIconographyRelation(int ornamentID, ArrayList<IconographyEntry> iconographyList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertOrnamentIconographyRelation wurde ausgelöst.");;
		}
		System.err.println("InsertIconography started");
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		try {
			relationStatement = dbc.prepareStatement("INSERT INTO OrnamentIconographyRelation VALUES (?, ?)");
			for (IconographyEntry entry : iconographyList) {
				relationStatement.setInt(1, ornamentID);
				relationStatement.setInt(2, entry.getIconographyID());
				relationStatement.executeUpdate();
			}
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertOrnamentIconographyRelation brauchte "+diff + " Millisekunden.");;}}

	}

	/**
	 * @param depictionID
	 * @param relatedBibliographyList
	 */
	private synchronized void insertDepictionBibliographyRelation(int depictionID,
			ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionBibliographyRelation wurde ausgelöst.");;
		}

		try {
			relationStatement = dbc.prepareStatement("INSERT INTO DepictionBibliographyRelation VALUES (?, ?, ?)");
			for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
				relationStatement.setInt(1, depictionID);
				relationStatement.setInt(2, entry.getAnnotatedBibliographyID()); 
				relationStatement.setString(3, entry.getQuotedPages());
				relationStatement.executeUpdate();
			}
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDepictionBibliographyRelation brauchte "+diff + " Millisekunden.");;}}
	}

	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromDepiction(int depictionID) {
		System.out.println("triggered getRelatedBibliography");
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromDepiction wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM DepictionBibliographyRelation WHERE DepictionID=?");
			relationStatement.setInt(1, depictionID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				System.out.println("found RelatedBibliography");
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID"), rs.getString("Page")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromDepiction brauchte "+diff + " Millisekunden.");;}}
		System.out.println("size of RelatedBibliography:"+Integer.toString(result.size()));
		return result;
	}

	/**
	 * @param depictionID
	 * @param relatedBibliographyList
	 */
	private synchronized void writeCaveBibliographyRelation(int caveID, ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeCaveBibliographyRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		if (caveID > 0 && relatedBibliographyList.size() > 0) {
			deleteEntry("DELETE FROM CaveBibliographyRelation WHERE CaveID=" + caveID);
			try {
				relationStatement = dbc.prepareStatement("INSERT INTO CaveBibliographyRelation VALUES (?, ?, ?)");
				relationStatement.setInt(1, caveID);
				for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
					relationStatement.setInt(2, entry.getAnnotatedBibliographyID());
					relationStatement.setString(3, entry.getQuotedPages());
					relationStatement.executeUpdate();
				}
				relationStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			}
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeCaveBibliographyRelation brauchte "+diff + " Millisekunden.");;}}
	}
	
	private synchronized void writeOrnamenticBibliographyRelation(int OrnamentID, ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeOrnamenticBibliographyRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		if (OrnamentID > 0) {
			deleteEntry("DELETE FROM OrnamentBibliographyRelation WHERE OrnamentID=" + OrnamentID);
			if (relatedBibliographyList.size() > 0) {
				try {
					relationStatement = dbc.prepareStatement("INSERT INTO OrnamentBibliographyRelation VALUES (?, ?, ?)");
					relationStatement.setInt(1, OrnamentID);
					for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
						System.err.println("INSERT INTO OrnamentBibliographyRelation started ");
						relationStatement.setInt(2, entry.getAnnotatedBibliographyID());
						relationStatement.setString(3, entry.getQuotedPages());
						relationStatement.executeUpdate();
					}
					relationStatement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				}
			}
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeOrnamenticBibliographyRelation brauchte "+diff + " Millisekunden.");;}}
	}

	/**
	 * 
	 * @param caveID
	 * @return
	 */
	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromCave(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromCave wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM CaveBibliographyRelation WHERE CaveID=?");
			relationStatement.setInt(1, caveID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID"),rs.getString("Page")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromCave brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromOrnamen(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromOrnamen wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM OrnamentBibliographyRelation WHERE OrnamentID=?");
			relationStatement.setInt(1, ornamentID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID"), rs.getString("Page")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibliographyFromOrnamen brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<ModeOfRepresentationEntry> getModesOfRepresentations() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getModesOfRepresentations wurde ausgelöst.");;
		}
		ArrayList<ModeOfRepresentationEntry> result = new ArrayList<ModeOfRepresentationEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM ModesOfRepresentation");
			while (rs.next()) {
				result.add(new ModeOfRepresentationEntry(rs.getInt("ModeOfRepresentationID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getModesOfRepresentations brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<CaveAreaEntry> getCaveAreas(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveAreas wurde ausgelöst.");;
		}
		CaveAreaEntry caEntry;
		Connection dbc = getConnection();
		PreparedStatement caveAreaStatement;
		ArrayList<CaveAreaEntry> result = new ArrayList<CaveAreaEntry>();
		ResultSet caveAreaRS;
		try {
			caveAreaStatement = dbc.prepareStatement("SELECT * FROM CaveAreas WHERE CaveID=?");
			caveAreaStatement.setInt(1, caveID);
			caveAreaRS = caveAreaStatement.executeQuery();
			while (caveAreaRS.next()) {
				caEntry = new CaveAreaEntry(caveAreaRS.getInt("CaveID"), caveAreaRS.getString("CaveAreaLabel"),
						caveAreaRS.getDouble("ExpeditionMeasuredWidth"), caveAreaRS.getDouble("ExpeditionMeasuredLength"),
						caveAreaRS.getDouble("ExpeditionMeasuredWallHeight"), caveAreaRS.getDouble("ExpeditionMeasuredTotalHeight"),
						caveAreaRS.getDouble("ModernMeasuredMinWidth"), caveAreaRS.getDouble("ModernMeasuredMaxWidth"),
						caveAreaRS.getDouble("ModernMeasuredMinLength"), caveAreaRS.getDouble("ModernMeasuredMaxLength"),
						caveAreaRS.getDouble("ModernMeasuredMinHeight"), caveAreaRS.getDouble("ModernMeasuredMaxHeight"),
						getPreservationClassification(caveAreaRS.getInt("PreservationClassificationID")), getCeilingType(caveAreaRS.getInt("CeilingTypeID1")), 
						getCeilingType(caveAreaRS.getInt("CeilingTypeID2")), getPreservationClassification(caveAreaRS.getInt("CeilingPreservationClassificationID1")), 
						getPreservationClassification(caveAreaRS.getInt("CeilingPreservationClassificationID2")), getPreservationClassification(caveAreaRS.getInt("FloorPreservationClassificationID")));
				result.add(caEntry);
			}
			caveAreaStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveAreas brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	protected synchronized boolean writeCaveArea(CaveAreaEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeCaveArea wurde ausgelöst.");;
		}
		int rowCount = 0;
		Connection dbc = getConnection();
		PreparedStatement caveAreaStatement;
		try {
			caveAreaStatement = dbc.prepareStatement(
					"INSERT INTO CaveAreas (CaveID, CaveAreaLabel, ExpeditionMeasuredWidth, ExpeditionMeasuredLength, ExpeditionMeasuredWallHeight, ExpeditionMeasuredTotalHeight, ModernMeasuredMinWidth, ModernMeasuredMaxWidth, "
							+ "ModernMeasuredMinLength, ModernMeasuredMaxLength, ModernMeasuredMinHeight, ModernMeasuredMaxHeight, "
							+ "PreservationClassificationID, CeilingTypeID1, CeilingTypeID2, CeilingPreservationClassificationID1, CeilingPreservationClassificationID2, FloorPreservationClassificationID) "
							+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE "
							+ "ExpeditionMeasuredWidth=?, ExpeditionMeasuredLength=?, ExpeditionMeasuredWallHeight=?, ExpeditionMeasuredTotalHeight=?, ModernMeasuredMinWidth=?, ModernMeasuredMaxWidth=?, ModernMeasuredMinLength=?, ModernMeasuredMaxLength=?, "
							+ "ModernMeasuredMinHeight=?, ModernMeasuredMaxHeight=?, PreservationClassificationID=?, CeilingTypeID1=?, CeilingTypeID2=?, "
							+ "CeilingPreservationClassificationID1=?, CeilingPreservationClassificationID2=?, FloorPreservationClassificationID=?");
			caveAreaStatement.setInt(1, entry.getCaveID());
			caveAreaStatement.setString(2, entry.getCaveAreaLabel());
			caveAreaStatement.setDouble(3, entry.getExpeditionWidth());
			caveAreaStatement.setDouble(4, entry.getExpeditionLength());
			caveAreaStatement.setDouble(5, entry.getExpeditionWallHeight());
			caveAreaStatement.setDouble(6, entry.getExpeditionTotalHeight());
			caveAreaStatement.setDouble(7, entry.getModernMinWidth());
			caveAreaStatement.setDouble(8, entry.getModernMaxWidth());
			caveAreaStatement.setDouble(9, entry.getModernMinLength());
			caveAreaStatement.setDouble(10, entry.getModernMaxLength());
			caveAreaStatement.setDouble(11, entry.getModernMinHeight());
			caveAreaStatement.setDouble(12, entry.getModernMaxHeight());
			caveAreaStatement.setInt(13, entry.getPreservationClassification() != null ? entry.getPreservationClassification().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(14, entry.getCeilingType1() != null ? entry.getCeilingType1().getCeilingTypeID() : 0);
			caveAreaStatement.setInt(15, entry.getCeilingType2() != null ? entry.getCeilingType2().getCeilingTypeID() : 0);
			caveAreaStatement.setInt(16, entry.getCeilingPreservationClassification1() != null ? entry.getCeilingPreservationClassification1().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(17, entry.getCeilingPreservationClassification2() != null ? entry.getCeilingPreservationClassification2().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(18, entry.getFloorPreservationClassification() != null ? entry.getFloorPreservationClassification().getPreservationClassificationID() : 0);
			caveAreaStatement.setDouble(19, entry.getExpeditionWidth());
			caveAreaStatement.setDouble(20, entry.getExpeditionLength());
			caveAreaStatement.setDouble(21, entry.getExpeditionWallHeight());
			caveAreaStatement.setDouble(22, entry.getExpeditionTotalHeight());
			caveAreaStatement.setDouble(23, entry.getModernMinWidth());
			caveAreaStatement.setDouble(24, entry.getModernMaxWidth());
			caveAreaStatement.setDouble(25, entry.getModernMinLength());
			caveAreaStatement.setDouble(26, entry.getModernMaxLength());
			caveAreaStatement.setDouble(27, entry.getModernMinHeight());
			caveAreaStatement.setDouble(28, entry.getModernMaxHeight());
			caveAreaStatement.setInt(29, entry.getPreservationClassification() != null ? entry.getPreservationClassification().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(30, entry.getCeilingType1() != null ? entry.getCeilingType1().getCeilingTypeID() : 0);
			caveAreaStatement.setInt(31, entry.getCeilingType2() != null ? entry.getCeilingType2().getCeilingTypeID() : 0);
			caveAreaStatement.setInt(32, entry.getCeilingPreservationClassification1() != null ? entry.getCeilingPreservationClassification1().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(33, entry.getCeilingPreservationClassification2() != null ? entry.getCeilingPreservationClassification2().getPreservationClassificationID() : 0);
			caveAreaStatement.setInt(34, entry.getFloorPreservationClassification() != null ? entry.getFloorPreservationClassification().getPreservationClassificationID() : 0);
			rowCount = caveAreaStatement.executeUpdate();
			caveAreaStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeCaveArea brauchte "+diff + " Millisekunden.");;}}
		return (rowCount > 0);
	}

	protected synchronized boolean writeWall(WallEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeWall wurde ausgelöst.");;
		}
		int rowCount = 0;
		Connection dbc = getConnection();
		PreparedStatement wallStatement;
		try {
			wallStatement = dbc.prepareStatement("INSERT INTO Walls (CaveID, WallLocationID, PreservationClassificationID, Width, Height) "
					+ "VALUES (?, ?, ?, ?, ?) " + "ON DUPLICATE KEY UPDATE " + "PreservationClassificationID=?, Width=?, Height=?",
					Statement.RETURN_GENERATED_KEYS);
			wallStatement.setInt(1, entry.getCaveID());
			wallStatement.setInt(2, entry.getWallLocationID());
			wallStatement.setInt(3, entry.getPreservationClassificationID());
			wallStatement.setDouble(4, entry.getWidth());
			wallStatement.setDouble(5, entry.getHeight());
			wallStatement.setInt(6, entry.getPreservationClassificationID());
			wallStatement.setDouble(7, entry.getWidth());
			wallStatement.setDouble(8, entry.getHeight());
			rowCount = wallStatement.executeUpdate();

			wallStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeWall brauchte "+diff + " Millisekunden.");;}}
		return (rowCount > 0);
	}

	private synchronized boolean writeC14AnalysisUrlEntry(int caveID, ArrayList<C14AnalysisUrlEntry> entryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeC14AnalysisUrlEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement c14UrlStatement;
		deleteEntry("DELETE FROM C14AnalysisUrls WHERE CaveID=" + caveID);
		try {
			c14UrlStatement = dbc.prepareStatement("INSERT INTO C14AnalysisUrls (C14Url, C14ShortName, CaveID) VALUES (?, ?, ?)");
			for (C14AnalysisUrlEntry entry : entryList) {
				c14UrlStatement.setString(1, entry.getC14Url());
				c14UrlStatement.setString(2, entry.getC14ShortName());
				c14UrlStatement.setInt(3, caveID);
				c14UrlStatement.executeUpdate();
			}
			c14UrlStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace(System.err);
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von writeC14AnalysisUrlEntry brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	/**
	 * @param caveID
	 * @return
	 */
	public ArrayList<WallEntry> getWalls(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWalls wurde ausgelöst.");;
		}
		WallEntry entry;
		Connection dbc = getConnection();
		PreparedStatement wallStatement;
		ArrayList<WallEntry> result = new ArrayList<WallEntry>();
		ResultSet wallRS;
		try {
			wallStatement = dbc.prepareStatement("SELECT * FROM Walls WHERE CaveID=?");
			wallStatement.setInt(1, caveID);
			wallRS = wallStatement.executeQuery();
			while (wallRS.next()) {
				entry = new WallEntry(wallRS.getInt("CaveID"), wallRS.getInt("WallLocationID"), wallRS.getInt("PreservationClassificationID"),
						wallRS.getDouble("Width"), wallRS.getDouble("Height"));
				result.add(entry);
			}
			wallStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWalls brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param caveID
	 * @return
	 */
	private ArrayList<C14AnalysisUrlEntry> getC14AnalysisEntries(int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getC14AnalysisEntries wurde ausgelöst.");;
		}
		C14AnalysisUrlEntry entry;
		Connection dbc = getConnection();
		PreparedStatement c14AnalysisStatement;
		ArrayList<C14AnalysisUrlEntry> result = new ArrayList<C14AnalysisUrlEntry>();
		ResultSet c14RS;
		try {
			c14AnalysisStatement = dbc.prepareStatement("SELECT * FROM C14AnalysisUrls WHERE CaveID=?");
			c14AnalysisStatement.setInt(1, caveID);
			c14RS = c14AnalysisStatement.executeQuery();
			while (c14RS.next()) {
				entry = new C14AnalysisUrlEntry(c14RS.getInt("C14AnalysisUrlID"), c14RS.getString("C14Url"), c14RS.getString("C14ShortName"), c14RS.getInt("CaveID"));
				result.add(entry);
			}
			c14AnalysisStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getC14AnalysisEntries brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	
	/**
	 * @return
	 */
	public ArrayList<AnnotationEntry> getAnnotations(int depictionID) {
		long start = System.currentTimeMillis();
		//System.err.println("Entering getAnnotations for DepictionID: "+Integer.toString(depictionID));
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		ArrayList<AnnotationEntry> results = new ArrayList<AnnotationEntry>();
		Statement stmt;
		Connection dbc2 = getConnection();
		Statement stmt2 = null;
		try {
			stmt = dbc.createStatement();
			String sqlText = "SELECT PolygoneID,AnnotoriousID,ST_AsGeoJSON(Polygon.Polygon) as \"Polygon\",Images.ImageID,Images.Filename,DepictionID,Polygon.deleted FROM Polygon left join Images on( Polygon.ImageID=Images.ImageID) WHERE Polygon.DepictionID="+depictionID+" and Polygon.deleted=0 and Polygon.Polygon is not null";
			//System.err.println(sqlText);
			ResultSet rs = stmt.executeQuery(sqlText);
			while (rs.next()) {
				AnnotationEntry newAnno = new AnnotationEntry(rs.getInt("DepictionID"), rs.getString("AnnotoriousID"), null,rs.getString("Polygon"), rs.getString("Filename"), false,false);
				//System.err.println("Found Annotation for Depiction "+depictionID+ " - "+newAnno.getAnnotoriousID());
				ArrayList<IconographyEntry> icoResults = new ArrayList<IconographyEntry>();
				try {
					stmt2 = dbc2.createStatement();
					ResultSet rs2 = stmt2.executeQuery(
							"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM Annotations WHERE deleted=0 and AnnotoriousID='"
									+ newAnno.getAnnotoriousID() + "')");
					while (rs2.next()) {
						//System.err.println("Entering Iteration 2");
						IconographyEntry icoRes = new IconographyEntry(rs2.getInt("IconographyID"), rs2.getInt("ParentID"), rs2.getString("Text")==null ? "" : rs2.getString("Text"), rs2.getString("search")==null ? "" : rs2.getString("search"));
						if (rootItems.containsKey(icoRes.getIconographyID())){
							icoRes.setRoot(rootItems.get(icoRes.getIconographyID()));
						}
						else {
							rootItems.put(icoRes.getIconographyID(), findIconographyRoot(icoRes.getIconographyID()));
							icoRes.setRoot(rootItems.get(icoRes.getIconographyID()));
						}
						icoResults.add(icoRes);
						//System.err.println("IconographyID for AnnoID "+newAnno.getAnnotoriousID()+": "+Integer.toString(rs2.getInt("IconographyID")));
					}
					rs2.close();
					stmt2.close();
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				}
				newAnno.setTags(icoResults);
				
				results.add(newAnno);
				//System.err.println("Polygon2SVG:"+newAnno.getPolygone());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDepictionEntry brauchte "+diff + " Millisekunden.");;}}
		return results;
	}
	
	/**
	 * @return
	 */
	public boolean setAnnotationResults(AnnotationEntry annoEntry) {
		if (!annoEntry.getDelete()) {
			if (annoEntry.getUpdate()) {
				deleteAbstractEntry(annoEntry);
			}
			Connection dbc = getConnection();
			PreparedStatement annoStatement;
			try {
				for (IconographyEntry tag : annoEntry.getTags()) {
					System.err.println("Annotation (for Depiction: "+Integer.toString(annoEntry.getDepictionID())+") recieved: Tag: "+tag.getText()+", Polygon: "+annoEntry.getPolygone()+", Image: "+annoEntry.getImage()+", delete: "+Boolean.toString(annoEntry.getDelete())+", update: "+Boolean.toString(annoEntry.getUpdate()));
					annoStatement = dbc.prepareStatement(
							"INSERT INTO Annotations ( AnnotoriousID, iconographyID) VALUES ( ?, ?)",
							Statement.RETURN_GENERATED_KEYS);
					annoStatement.setString(1, annoEntry.getAnnotoriousID());
					annoStatement.setInt(2, tag.getIconographyID());
					annoStatement.executeUpdate();
					ResultSet keys = annoStatement.getGeneratedKeys();
					int annoID = -1;
					if (keys.next()) {
						annoID = keys.getInt(1);
					}
					keys.close();
				}
				PreparedStatement polygoneStatement;
				String parts[]=annoEntry.getImage().split("\\.",-1);
	            String part1=parts[0];
				String poly=annoEntry.getPolygone();
//				if (annoEntry.getPolygone().indexOf(" ")>annoEntry.getPolygone().indexOf(",")) {
//					poly=poly.replace(" ", "|");
//					poly=poly.replace(",", " ");
//					poly=poly.replace("|", ",");					
//				}
				System.err.println("INSERT INTO Polygon (DepictionID, AnnotoriousID, Polygon, ImageID) VALUES ("+Integer.toString(annoEntry.getDepictionID())+", "+annoEntry.getAnnotoriousID()+", PolygonFromText(\""+poly+"\"),"+part1+")");
				polygoneStatement = dbc.prepareStatement("INSERT INTO Polygon (DepictionID, AnnotoriousID, Polygon, ImageID) VALUES (?, ?, PolygonFromText(?),?)");
				polygoneStatement.setInt(1, annoEntry.getDepictionID());
				polygoneStatement.setString(2, annoEntry.getAnnotoriousID());
//				if (annoEntry.getPolygone().indexOf("\"></polygon></svg>")>0) {
//					polygoneStatement.setString(3, "POLYGON(("+poly.substring(22,annoEntry.getPolygone().indexOf("\"></polygon></svg>"))+"))");					
//				}
//				else {
//					polygoneStatement.setString(3, "POLYGON(("+poly+"))");
//				}
				polygoneStatement.setString(3, poly);
				polygoneStatement.setInt(4, Integer.parseInt(part1));
				polygoneStatement.executeUpdate();
				polygoneStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			}
			
			

		}
		else {
			deleteAbstractEntry(annoEntry);
		}
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		annoEntry.setModifiedOn(df.format(date));
		protocollModifiedAnnoEntry(annoEntry);
		System.out.println("Leaving setAnnotation");
		return true;
		
	}
	public ArrayList<WallLocationEntry> getWallLocations() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWallLocations wurde ausgelöst.");;
		}
		WallLocationEntry entry;
		Connection dbc = getConnection();
		PreparedStatement wallLocationStatement;
		ArrayList<WallLocationEntry> result = new ArrayList<WallLocationEntry>();
		ResultSet wallLocationRS;
		try {
			wallLocationStatement = dbc.prepareStatement("SELECT * FROM WallLocations ORDER BY Label Asc");
			wallLocationRS = wallLocationStatement.executeQuery();
			while (wallLocationRS.next()) {
				entry = new WallLocationEntry(wallLocationRS.getInt("WallLocationID"), wallLocationRS.getString("Label"),
						wallLocationRS.getString("CaveAreaLabel"));
				result.add(entry);
			}
			wallLocationStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWallLocations brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param currentAuthorEntry
	 * @return
	 */
	public boolean updateAuthorEntry(AuthorEntry authorEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateAuthorEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement authorStatement;
		int rowCount = 0;
		try {
			authorStatement = dbc.prepareStatement(
					"UPDATE Authors SET LastName=?, FirstName=?, Institution=?, KuchaVisitor=?, Affiliation=?, Email=?, Homepage=?, Alias=?, InstitutionEnabled=?, altSpelling=? WHERE AuthorID=?");
			authorStatement.setString(1, authorEntry.getLastname());
			authorStatement.setString(2, authorEntry.getFirstname());
			authorStatement.setString(3, authorEntry.getInstitution());
			authorStatement.setBoolean(4, authorEntry.isKuchaVisitor());
			authorStatement.setString(5, authorEntry.getAffiliation());
			authorStatement.setString(6, authorEntry.getEmail());
			authorStatement.setString(7, authorEntry.getHomepage());
			authorStatement.setString(8, authorEntry.getAlias());
			authorStatement.setBoolean(9, authorEntry.isInstitutionEnabled());
			authorStatement.setString(10, authorEntry.getAltSpelling());
			authorStatement.setInt(11, authorEntry.getAuthorID());
			rowCount = authorStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateAuthorEntry brauchte "+diff + " Millisekunden.");;}}
		return rowCount > 0;
	}

	/**
	 * @param currentAuthorEntry
	 * @return
	 */
	public int insertAuthorEntry(AuthorEntry authorEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertAuthorEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement authorStatement;
		int authorID = 0;
		try {
			authorStatement = dbc.prepareStatement(
					"INSERT INTO Authors (LastName, FirstName, Institution, KuchaVisitor, Affiliation, Email, Homepage, Alias, InstitutionEnabled, altSpelling) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			authorStatement.setString(1, authorEntry.getLastname());
			authorStatement.setString(2, authorEntry.getFirstname());
			authorStatement.setString(3, authorEntry.getInstitution());
			authorStatement.setBoolean(4, authorEntry.isKuchaVisitor());
			authorStatement.setString(5, authorEntry.getAffiliation());
			authorStatement.setString(6, authorEntry.getEmail());
			authorStatement.setString(7, authorEntry.getHomepage());
			authorStatement.setString(8, authorEntry.getAlias());
			authorStatement.setBoolean(9, authorEntry.isInstitutionEnabled());
			authorStatement.setString(10, authorEntry.getAltSpelling());
			authorStatement.executeUpdate();
			ResultSet keys = authorStatement.getGeneratedKeys();
			if (keys.next()) {
				authorID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertAuthorEntry brauchte "+diff + " Millisekunden.");;}}
		return authorID;
	}

	/**
	 * @return
	 */
	private String createBibtexKey(AuthorEntry entry, String year) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von createBibtexKey wurde ausgelöst.");;
		}
		String result = (entry.isInstitutionEnabled() ? entry.getInstitution().replace(" ", "") : entry.getLastname()) + year;
		List<String> appendix = Arrays.asList("","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
		int count = 0;
		while (!getAnnotatedBibliography("BibTexKey='"+result+appendix.get(count)+"'").isEmpty()) {
			++count;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von createBibtexKey brauchte "+diff + " Millisekunden.");;}}
		return result+appendix.get(count);
	}

	/**
	 * @param bibEntry
	 * @return
	 */
	public AnnotatedBibliographyEntry insertAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) {
		long start = System.currentTimeMillis();
		System.err.println("insertAnnotatedBiblographyEntry triggered");
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertAnnotatedBiblographyEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int newBibID = 0;
		if (bibEntry.getBibtexKey().isEmpty() || !getAnnotatedBibliography("BibTexKey='"+bibEntry.getBibtexKey()+"'").isEmpty()) {
			if (bibEntry.getAuthorList().get(0) != null) {
				bibEntry.setBibtexKey(createBibtexKey(bibEntry.getAuthorList().get(0), bibEntry.getYearORG()));
			} else if (bibEntry.getEditorList().get(0) != null) {
				bibEntry.setBibtexKey(createBibtexKey(bibEntry.getAuthorList().get(0), bibEntry.getYearORG()));
			}
		}
		System.err.println("insertAnnotatedBiblographyEntry - saving");
		try {
			pstmt = dbc.prepareStatement("INSERT INTO AnnotatedBibliography (PublicationTypeID, " 
					+ "AccessDateEN, AccessDateORG, AccessDateTR, "
					+ "ParentTitleEN, ParentTitleORG, ParentTitleTR, " 
					+ "Comments, " 
					+ "EditionEN, EditionORG, EditionTR, " 
					+ "FirstEditionBibID, "
					+ "MonthEN, MonthORG, MonthTR, " 
					+ "Notes, " 
					+ "NumberEN, NumberORG, NumberTR, " 
					+ "PagesEN, PagesORG, PagesTR, " 
					+ "Publisher, "
					+ "SeriesEN, SeriesORG, SeriesTR, " 
					+ "TitleAddonEN, TitleAddonORG, TitleAddonTR, " 
					+ "TitleEN, TitleORG, TitleTR, "
					+ "SubtitleEN, SubtitleORG, SubtitleTR, "
					+ "UniversityEN, UniversityORG, UniversityTR, " 
					+ "URI, URL, " 
					+ "VolumeEN, VolumeORG, VolumeTR, "
					+ "IssueEN, IssueORG, IssueTR, " 
					+ "YearEN, YearORG, YearTR, " 
					+ "Unpublished, AccessLevel, AbstractText, ThesisType, EditorType, OfficialTitleTranslation, BibTexKey,Annotation) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, bibEntry.getPublicationType().getPublicationTypeID());
			pstmt.setString(2, bibEntry.getAccessdateEN());
			pstmt.setString(3, bibEntry.getAccessdateORG());
			pstmt.setString(4, bibEntry.getAccessdateTR());
			pstmt.setString(5, bibEntry.getParentTitleEN());
			pstmt.setString(6, bibEntry.getParentTitleORG());
			pstmt.setString(7, bibEntry.getParentTitleTR());
			pstmt.setString(8, bibEntry.getComments());
			pstmt.setString(9, bibEntry.getEditionEN());
			pstmt.setString(10, bibEntry.getEditionORG());
			pstmt.setString(11, bibEntry.getEditionTR());
			pstmt.setInt(12, bibEntry.getFirstEditionBibID());
			pstmt.setString(13, bibEntry.getMonthEN());
			pstmt.setString(14, bibEntry.getMonthORG());
			pstmt.setString(15, bibEntry.getMonthTR());
			pstmt.setString(16, bibEntry.getNotes());
			pstmt.setString(17, bibEntry.getNumberEN());
			pstmt.setString(18, bibEntry.getNumberORG());
			pstmt.setString(19, bibEntry.getNumberTR());
			pstmt.setString(20, bibEntry.getPagesEN());
			pstmt.setString(21, bibEntry.getPagesORG());
			pstmt.setString(22, bibEntry.getPagesTR());
			pstmt.setString(23, bibEntry.getPublisher());
			pstmt.setString(24, bibEntry.getSeriesEN());
			pstmt.setString(25, bibEntry.getSeriesORG());
			pstmt.setString(26, bibEntry.getSeriesTR());
			pstmt.setString(27, bibEntry.getTitleaddonEN());
			pstmt.setString(28, bibEntry.getTitleaddonORG());
			pstmt.setString(29, bibEntry.getTitleaddonTR());
			pstmt.setString(30, bibEntry.getTitleEN());
			pstmt.setString(31, bibEntry.getTitleORG());
			pstmt.setString(32, bibEntry.getTitleTR());
			pstmt.setString(33, bibEntry.getSubtitleEN());
			pstmt.setString(34, bibEntry.getSubtitleORG());
			pstmt.setString(35, bibEntry.getSubtitleTR());
			pstmt.setString(36, bibEntry.getUniversityEN());
			pstmt.setString(37, bibEntry.getUniversityORG());
			pstmt.setString(38, bibEntry.getUniversityTR());
			pstmt.setString(39, bibEntry.getUri());
			pstmt.setString(40, bibEntry.getUrl());
			pstmt.setString(41, bibEntry.getVolumeEN());
			pstmt.setString(42, bibEntry.getVolumeORG());
			pstmt.setString(43, bibEntry.getVolumeTR());
			pstmt.setString(44, bibEntry.getIssueEN());
			pstmt.setString(45, bibEntry.getIssueORG());
			pstmt.setString(46, bibEntry.getIssueTR());
			pstmt.setInt(47, bibEntry.getYearEN());
			pstmt.setString(48, bibEntry.getYearORG());
			pstmt.setString(49, bibEntry.getYearTR());
			pstmt.setBoolean(50, bibEntry.isUnpublished());
			pstmt.setInt(51, bibEntry.getAccessLevel());
			pstmt.setString(52, bibEntry.getAbstractText());
			pstmt.setString(53, bibEntry.getThesisType());
			pstmt.setString(54, bibEntry.getEditorType());
			pstmt.setBoolean(55, bibEntry.isOfficialTitleTranslation());
			pstmt.setString(56, bibEntry.getBibtexKey());
			pstmt.setBoolean(57, bibEntry.getAnnotation());
			pstmt.executeUpdate();

			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) {
				newBibID = keys.getInt(1);
				bibEntry.setAnnotatedBibliographyID(newBibID);
			}
			keys.close();

			if (newBibID > 0) {
				updateAuthorBibRelation(newBibID, bibEntry.getAuthorList());
				updateEditorBibRelation(newBibID, bibEntry.getEditorList());
				updateBibKeywordRelation(newBibID, bibEntry.getKeywordList());
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		bibEntry.setAnnotatedBibliographyID(newBibID);
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		bibEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(bibEntry,"");

		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertAnnotatedBiblographyEntry brauchte "+diff + " Millisekunden.");;}}
		return bibEntry;
	}

	private void updateAuthorBibRelation(int bibID, ArrayList<AuthorEntry> authorList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateAuthorBibRelation wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int sequence = 1;
		deleteEntry("DELETE FROM AuthorBibliographyRelation WHERE BibID=" + bibID); // in case there are already relations
		try {
			pstmt = dbc.prepareStatement("INSERT INTO AuthorBibliographyRelation (AuthorID, BibID, AuthorSequence) VALUES (?, ?, ?)");
			for (AuthorEntry entry : authorList) {
				pstmt.setInt(1, entry.getAuthorID());
				pstmt.setInt(2, bibID);
				pstmt.setInt(3, sequence++);
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateAuthorBibRelation brauchte "+diff + " Millisekunden.");;}}
	}

	private void updateEditorBibRelation(int bibID, ArrayList<AuthorEntry> editorList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEditorBibRelation wurde ausgelöst.");;
		}
		if (editorList == null)
			return;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int sequence = 1;
		deleteEntry("DELETE FROM EditorBibliographyRelation WHERE BibID=" + bibID); // in case there are already relations
		try {
			pstmt = dbc.prepareStatement("INSERT INTO EditorBibliographyRelation (AuthorID, BibID, EditorSequence) VALUES (?, ?, ?)");
			for (AuthorEntry entry : editorList) {
				pstmt.setInt(1, entry.getAuthorID());
				pstmt.setInt(2, bibID);
				pstmt.setInt(3, sequence++);
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateEditorBibRelation brauchte "+diff + " Millisekunden.");;}}
	}

	/**
	 * @return
	 */
	public ArrayList<PublisherEntry> getPublishers() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublishers wurde ausgelöst.");;
		}
		ArrayList<PublisherEntry> result = new ArrayList<PublisherEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publishers");
			while (rs.next()) {
				result.add(new PublisherEntry(rs.getInt("PublisherID"), rs.getString("Name"), rs.getString("Location")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublishers brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public PublisherEntry getPublisher(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublisher wurde ausgelöst.");;
		}
		PublisherEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publishers WHERE PublisherID=" + id);
			if (rs.first()) {
				result = new PublisherEntry(rs.getInt("PublisherID"), rs.getString("Name"), rs.getString("Location"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPublisher brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<AuthorEntry> getAuthors() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthors wurde ausgelöst.");;
		}
		ArrayList<AuthorEntry> result = new ArrayList<AuthorEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Authors");
			while (rs.next()) {
				result.add(new AuthorEntry(rs.getInt("AuthorID"), rs.getString("Lastname"), rs.getString("Firstname"), rs.getString("Institution"),
						rs.getBoolean("KuchaVisitor"), rs.getString("Affiliation"), rs.getString("Email"), rs.getString("Homepage"), rs.getString("Alias"), 
						rs.getBoolean("InstitutionEnabled"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")), rs.getString("altSpelling")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getAuthors brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param photographerEntry
	 * @return
	 */
	public int insertPhotographerEntry(PhotographerEntry photographerEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertPhotographerEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement peStatement;
		int photographerID = 0;
		try {
			peStatement = dbc.prepareStatement("INSERT INTO Photographers (Name, Institution) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			peStatement.setString(1, photographerEntry.getName());
			peStatement.setString(2, photographerEntry.getInstitution());
			peStatement.executeUpdate();
			ResultSet keys = peStatement.getGeneratedKeys();
			if (keys.next()) {
				photographerID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertPhotographerEntry brauchte "+diff + " Millisekunden.");;}}
		return photographerID;
	}

	/**
	 * @param cgEntry
	 * @return
	 */
	public int insertCaveGroupEntry(CaveGroupEntry cgEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveGroupEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement cgStatement;
		int caveGroupID = 0;
		try {
			cgStatement = dbc.prepareStatement("INSERT INTO CaveGroups (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			cgStatement.setString(1, cgEntry.getName());
			cgStatement.executeUpdate();
			ResultSet keys = cgStatement.getGeneratedKeys();
			if (keys.next()) {
				caveGroupID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCaveGroupEntry brauchte "+diff + " Millisekunden.");;}}
		return caveGroupID;
	}

	/**
	 * @param de
	 * @return
	 */
	public int insertDistrictEntry(DistrictEntry de) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDistrictEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement deStatement;
		int districtID = 0;
		try {
			deStatement = dbc.prepareStatement("INSERT INTO Districts (Name, SiteID, Description, Map, ArialMap) VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			deStatement.setString(1, de.getName());
			deStatement.setInt(2, de.getSiteID());
			deStatement.setString(3, de.getDescription());
			deStatement.setString(4, de.getMap());
			deStatement.setString(5, de.getArialMap());
			deStatement.executeUpdate();
			ResultSet keys = deStatement.getGeneratedKeys();
			if (keys.next()) {
				districtID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertDistrictEntry brauchte "+diff + " Millisekunden.");;}}
		return districtID;
	}

	/**
	 * @param re
	 * @return
	 */
	public int insertRegionEntry(RegionEntry re) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertRegionEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement regionStatement;
		int regionID = 0;
		try {
			regionStatement = dbc.prepareStatement("INSERT INTO Regions (PhoneticName, OriginalName, EnglishName, SiteID) VALUES (?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			regionStatement.setString(1, re.getPhoneticName());
			regionStatement.setString(2, re.getOriginalName());
			regionStatement.setString(3, re.getEnglishName());
			regionStatement.setInt(4, re.getSiteID());
			regionStatement.executeUpdate();
			ResultSet keys = regionStatement.getGeneratedKeys();
			if (keys.next()) {
				regionID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertRegionEntry brauchte "+diff + " Millisekunden.");;}}
		return regionID;
	}

	/**
	 * @param ctEntry
	 * @return
	 */
	public int insertCeilingTypeEntry(CeilingTypeEntry ctEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCeilingTypeEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement ceilingTypeStatement;
		int ceilingTypeID = 0;
		try {
			ceilingTypeStatement = dbc.prepareStatement("INSERT INTO CeilingTypes (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			ceilingTypeStatement.setString(1, ctEntry.getName());
			ceilingTypeStatement.executeUpdate();
			ResultSet keys = ceilingTypeStatement.getGeneratedKeys();
			if (keys.next()) {
				ceilingTypeID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertCeilingTypeEntry brauchte "+diff + " Millisekunden.");;}}
		return ceilingTypeID;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	public ArrayList<IconographyEntry> getRelatedIconography(int depictionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedIconography wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			
			pstmt = dbc.prepareStatement(
					"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM DepictionIconographyRelation WHERE DepictionID=?)");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedIconography brauchte "+diff + " Millisekunden.");;}}
		return results;
	}
	/**
	 * @param depictionID
	 * @return
	 */
	public ArrayList<IconographyEntry> getOrnamentRelatedIconography(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedIconography wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			
			pstmt = dbc.prepareStatement(
					"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM OrnamentIconographyRelation WHERE OrnamentID=?)");
			pstmt.setInt(1, ornamentID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedIconography brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	/**
	 * @return
	 */
	public ArrayList<LocationEntry> getLocations() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getLocations wurde ausgelöst.");;
		}
		ArrayList<LocationEntry> results = new ArrayList<LocationEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Locations ORDER BY Name Asc, Country Asc");
			while (rs.next()) {
				results.add(new LocationEntry(rs.getInt("LocationID"), rs.getString("Name"), rs.getString("Town"), rs.getString("Region"),
						rs.getString("Country"), rs.getString("URL")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getLocations brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	/**
	 * @return
	 */
	public LocationEntry getLocation(int id) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getLocation wurde ausgelöst.");;
		}
		if (id>-1) {
		
			LocationEntry result = null;
			Connection dbc = getConnection();
			PreparedStatement pstmt;
			try {
				pstmt = dbc.prepareStatement("SELECT * FROM Locations WHERE LocationID=?");
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				if (rs.first()) {
					result = new LocationEntry(rs.getInt("LocationID"), rs.getString("Name"), rs.getString("Town"), rs.getString("Region"), rs.getString("Country"), rs.getString("URL"));
				}
				rs.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
				System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
				return null;
			}
			if (dologging){
			long end = System.currentTimeMillis();
			long diff = (end-start);
			if (diff>100){
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getLocation brauchte "+diff + " Millisekunden.");;}}
			return result;
		}
		else {
			return null;
		}
	}

	/**
	 * @param vEntry
	 * @return
	 */
	public int inserVendorEntry(VendorEntry vEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von inserVendorEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement cgStatement;
		int vendorID = 0;
		try {
			cgStatement = dbc.prepareStatement("INSERT INTO Vendors (VendorName) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			cgStatement.setString(1, vEntry.getVendorName());
			cgStatement.executeUpdate();
			ResultSet keys = cgStatement.getGeneratedKeys();
			if (keys.next()) {
				vendorID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von inserVendorEntry brauchte "+diff + " Millisekunden.");;}}
		return vendorID;
	}

	/**
	 * @param lEntry
	 * @return
	 */
	public int insertLocationEntry(LocationEntry lEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertLocationEntry wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pStatement;
		int locationID = 0;
		try {
			pStatement = dbc.prepareStatement("INSERT INTO Locations (Name, Town, Region, Country, URL) VALUES (?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, lEntry.getName());
			pStatement.setString(2, lEntry.getTown());
			pStatement.setString(3, lEntry.getRegion());
			pStatement.setString(4, lEntry.getCounty());
			pStatement.setString(5, lEntry.getUrl());
			pStatement.executeUpdate();
			ResultSet keys = pStatement.getGeneratedKeys();
			if (keys.next()) {
				locationID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertLocationEntry brauchte "+diff + " Millisekunden.");;}}
		return locationID;
	}

	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getInnerSecondaryPatterns wurde ausgelöst.");;
		}
		ArrayList<InnerSecondaryPatternsEntry> result = new ArrayList<InnerSecondaryPatternsEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM InnerSecondaryPattern");
			while (rs.next()) {
				result.add(new InnerSecondaryPatternsEntry(rs.getInt("InnerSecID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getInnerSecondaryPatterns brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<OrnamentClassEntry> getOrnamentClass() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentClass wurde ausgelöst.");;
		}
		ArrayList<OrnamentClassEntry> result = new ArrayList<OrnamentClassEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamentClass");
			while (rs.next()) {
				result.add(new OrnamentClassEntry(rs.getInt("OrnamentClassID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName() +" brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<OrnamentComponentsEntry> getOrnamentComponents() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentComponentsEntry> result = new ArrayList<OrnamentComponentsEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamentComponent");
			while (rs.next()) {
				result.add(new OrnamentComponentsEntry(rs.getInt("OrnamentComponentID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentComponents brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public OrnamentComponentsEntry addOrnamentComponents(OrnamentComponentsEntry ornamentComponent) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		OrnamentComponentsEntry entry = null;
		PreparedStatement stmt;
		try {
			stmt = dbc.prepareStatement("INSERT INTO OrnamentComponent (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, ornamentComponent.getName());
			stmt.executeUpdate();

			int ID = 0;
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				ID = keys.getInt(1);
			}
			entry = new OrnamentComponentsEntry(ID, ornamentComponent.getName());
			keys.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return entry;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von addOrnamentComponents brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}

	public OrnamentClassEntry addOrnamentClass(OrnamentClassEntry ornamentClass) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		OrnamentClassEntry entry = null;

		PreparedStatement stmt;
		try {
			stmt = dbc.prepareStatement("INSERT INTO OrnamentClass (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, ornamentClass.getName());
			stmt.executeUpdate();
			int ID = 0;
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				ID = keys.getInt(1);
			}
			entry = new OrnamentClassEntry(ID, ornamentClass.getName());
			keys.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return entry;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von addOrnamentClass brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}

	public OrnamentClassEntry renameOrnamentClass(OrnamentClassEntry ornamentClass) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		OrnamentClassEntry entry = null;

		PreparedStatement stmt;
		try {
			stmt = dbc.prepareStatement("UPDATE OrnamentClass SET Name=? WHERE OrnamentClassID=?");
			stmt.setString(1, ornamentClass.getName());
			stmt.setInt(2, ornamentClass.getOrnamentClassID());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return entry;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von renameOrnamentClass brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}
	
	/**
	 * 
	 * @param ornamentComponents
	 * @return
	 */
	public OrnamentComponentsEntry renameOrnamentComponents(OrnamentComponentsEntry ornamentComponents) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		OrnamentComponentsEntry entry = null;

		PreparedStatement stmt;
		try {
			stmt = dbc.prepareStatement("UPDATE OrnamentComponents SET Name=? WHERE VALUES OrnamentComponentsID=?");
			stmt.setString(1, ornamentComponents.getName());
			stmt.setInt(2, ornamentComponents.getOrnamentComponentsID());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return entry;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von renameOrnamentComponents brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}

	/**
	 * 
	 * @param innerSecPattern
	 * @return
	 */
	public InnerSecondaryPatternsEntry addInnerSecondaryPatterns(InnerSecondaryPatternsEntry innerSecPattern) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		InnerSecondaryPatternsEntry entry;

		PreparedStatement stmt;
		try {

			stmt = dbc.prepareStatement("INSERT INTO InnerSecondaryPattern (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, innerSecPattern.getName());
			stmt.executeUpdate();
			int ID = 0;
			ResultSet keys = stmt.getGeneratedKeys();
			if (keys.next()) {
				ID = keys.getInt(1);
			}
			entry = new InnerSecondaryPatternsEntry(ID, innerSecPattern.getName());
			keys.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von addInnerSecondaryPatterns brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}

	/**
	 * @return
	 */
	public ArrayList<PreservationAttributeEntry> getPreservationAttributes() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<PreservationAttributeEntry> result = new ArrayList<PreservationAttributeEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM PreservationAttributes");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new PreservationAttributeEntry(rs.getInt("PreservationAttributeID"), rs.getString("Name")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPreservationAttributes brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param paEntry
	 * @return
	 */
	public int insertPreservationAttributeEntry(PreservationAttributeEntry paEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pStatement;
		int preservationAttributeID = 0;
		try {
			pStatement = dbc.prepareStatement("INSERT INTO PreservationAttributes (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, paEntry.getName());
			pStatement.executeUpdate();
			ResultSet keys = pStatement.getGeneratedKeys();
			if (keys.next()) {
				preservationAttributeID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertPreservationAttributeEntry brauchte "+diff + " Millisekunden.");;}}
		return preservationAttributeID;
	}

	/**
	 * @param publisherEntry
	 * @return
	 */
	public int insertPublisherEntry(PublisherEntry publisherEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pStatement;
		int newPublisherID = 0;
		try {
			pStatement = dbc.prepareStatement("INSERT INTO Publishers (Name, Location) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, publisherEntry.getName());
			pStatement.setString(2, publisherEntry.getLocation());
			pStatement.executeUpdate();
			ResultSet keys = pStatement.getGeneratedKeys();
			if (keys.next()) {
				newPublisherID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertPublisherEntry brauchte "+diff + " Millisekunden.");;}}
		return newPublisherID;
	}

	public ArrayList<OrnamentPositionEntry> getPositionbyWall(WallEntry wall) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentPositionEntry> result = new ArrayList<OrnamentPositionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM OrnamenticPosition JOIN OrnamentPositionWallRelation ON OrnamenticPosition.OrnamenticPositionID = OrnamentPositionWallRelation.OrnamentPositionID WHERE WallPositionID = 1");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new OrnamentPositionEntry(rs.getInt("OrnamenticPositionID"), rs.getString("Name")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPositionbyWall brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	
	public ArrayList<OrnamentPositionEntry> getPositionbyReveal(WallEntry wall) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentPositionEntry> result = new ArrayList<OrnamentPositionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM (OrnamenticPosition JOIN OrnamentPositionRevealsRelation ON OrnamenticPosition.OrnamenticPositionID = OrnamentPositionRevealsRelation.OrnamentPositionID) WHERE RevealsID = 1");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new OrnamentPositionEntry(rs.getInt("OrnamenticPositionID"), rs.getString("Name")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPositionbyReveal brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<OrnamentPositionEntry> getPositionbyCeilingTypes(int ceilingID1, int ceilingID2) {

		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentPositionEntry> result = new ArrayList<OrnamentPositionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM OrnamenticPosition JOIN OrnamentPositionCeilingRelation ON OrnamenticPosition.OrnamenticPositionID = OrnamentPositionCeilingRelation.OrnamentPositionID WHERE CeilingID = "
							+ ceilingID1 + " OR CeilingID =" + ceilingID2);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new OrnamentPositionEntry(rs.getInt("OrnamenticPositionID"), rs.getString("Name")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getPositionbyCeilingTypes brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<OrnamentFunctionEntry> getFunctionbyPosition(OrnamentPositionEntry position) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentFunctionEntry> result = new ArrayList<OrnamentFunctionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM OrnamenticFunction JOIN OrnamentPositionOrnamentFunctionRelation ON OrnamenticFunction.OrnamenticFunctionID = OrnamentPositionOrnamentFunctionRelation.OrnamentFunctionID WHERE OrnamentPositionID = "
							+ position.getOrnamentPositionID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new OrnamentFunctionEntry(rs.getInt("OrnamenticFunctionID"), rs.getString("Name")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getFunctionbyPosition brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param bibEntry
	 * @return
	 */
	public AnnotatedBibliographyEntry updateAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		System.err.println("updateAnnotatedBiblographyEntry - saving");
		if (bibEntry.getBibtexKey().isEmpty() || !getAnnotatedBibliography("BibTexKey='"+bibEntry.getBibtexKey()+"' AND BibID!="+bibEntry.getAnnotatedBibliographyID()).isEmpty()) {
			if (!bibEntry.getAuthorList().isEmpty()) {
				bibEntry.setBibtexKey(createBibtexKey(bibEntry.getAuthorList().get(0), bibEntry.getYearORG()));
			} else if (!bibEntry.getEditorList().isEmpty()) {
				bibEntry.setBibtexKey(createBibtexKey(bibEntry.getEditorList().get(0), bibEntry.getYearORG()));
			}
		}
		System.err.println("updateAnnotatedBiblographyEntry - bibtexKey = " + bibEntry.getBibtexKey());
		try {
			pstmt = dbc.prepareStatement("UPDATE AnnotatedBibliography SET PublicationTypeID=?, "
					+ "AccessDateEN=?, AccessDateORG=?, AccessDateTR=?, " 
					+ "ParentTitleEN=?, ParentTitleORG=?, ParentTitleTR=?, " 
					+ "Comments=?, "
					+ "EditionEN=?, EditionORG=?, EditionTR=?, " 
					+ "FirstEditionBibID=?, " 
					+ "MonthEN=?, MonthORG=?, MonthTR=?, " + "Notes=?, "
					+ "NumberEN=?, NumberORG=?, NumberTR=?, " 
					+ "PagesEN=?, PagesORG=?, PagesTR=?, " 
					+ "Publisher=?, "
					+ "SeriesEN=?, SeriesORG=?, SeriesTR=?, " 
					+ "TitleAddonEN=?, TitleAddonORG=?, TitleAddonTR=?, "
					+ "TitleEN=?, TitleORG=?, TitleTR=?, " 
					+ "SubtitleEN=?, SubtitleORG=?, SubtitleTR=?, " 
					+ "UniversityEN=?, UniversityORG=?, UniversityTR=?, " 
					+ "URI=?, URL=?, "
					+ "VolumeEN=?, VolumeORG=?, VolumeTR=?, " 
					+ "IssueEN=?, IssueORG=?, IssueTR=?, " 
					+ "YearEN=?, YearORG=?, YearTR=?, "
					+ "Unpublished=?, AccessLevel=?, AbstractText=?, ThesisType=?, EditorType=?, OfficialTitleTranslation=?, BibTexKey=?, Annotation=? WHERE BibID=?");
			pstmt.setInt(1, bibEntry.getPublicationType().getPublicationTypeID());
			pstmt.setString(2, bibEntry.getAccessdateEN());
			pstmt.setString(3, bibEntry.getAccessdateORG());
			pstmt.setString(4, bibEntry.getAccessdateTR());
			pstmt.setString(5, bibEntry.getParentTitleEN());
			pstmt.setString(6, bibEntry.getParentTitleORG());
			pstmt.setString(7, bibEntry.getParentTitleTR());
			pstmt.setString(8, bibEntry.getComments());
			pstmt.setString(9, bibEntry.getEditionEN());
			pstmt.setString(10, bibEntry.getEditionORG());
			pstmt.setString(11, bibEntry.getEditionTR());
			pstmt.setInt(12, bibEntry.getFirstEditionBibID());
			pstmt.setString(13, bibEntry.getMonthEN());
			pstmt.setString(14, bibEntry.getMonthORG());
			pstmt.setString(15, bibEntry.getMonthTR());
			pstmt.setString(16, bibEntry.getNotes());
			pstmt.setString(17, bibEntry.getNumberEN());
			pstmt.setString(18, bibEntry.getNumberORG());
			pstmt.setString(19, bibEntry.getNumberTR());
			pstmt.setString(20, bibEntry.getPagesEN());
			pstmt.setString(21, bibEntry.getPagesORG());
			pstmt.setString(22, bibEntry.getPagesTR());
			pstmt.setString(23, bibEntry.getPublisher());
			pstmt.setString(24, bibEntry.getSeriesEN());
			pstmt.setString(25, bibEntry.getSeriesORG());
			pstmt.setString(26, bibEntry.getSeriesTR());
			pstmt.setString(27, bibEntry.getTitleaddonEN());
			pstmt.setString(28, bibEntry.getTitleaddonORG());
			pstmt.setString(29, bibEntry.getTitleaddonTR());
			pstmt.setString(30, bibEntry.getTitleEN());
			pstmt.setString(31, bibEntry.getTitleORG());
			pstmt.setString(32, bibEntry.getTitleTR());
			pstmt.setString(33, bibEntry.getSubtitleEN());
			pstmt.setString(34, bibEntry.getSubtitleORG());
			pstmt.setString(35, bibEntry.getSubtitleTR());
			pstmt.setString(36, bibEntry.getUniversityEN());
			pstmt.setString(37, bibEntry.getUniversityORG());
			pstmt.setString(38, bibEntry.getUniversityTR());
			pstmt.setString(39, bibEntry.getUri());
			pstmt.setString(40, bibEntry.getUrl());
			pstmt.setString(41, bibEntry.getVolumeEN());
			pstmt.setString(42, bibEntry.getVolumeORG());
			pstmt.setString(43, bibEntry.getVolumeTR());
			pstmt.setString(44, bibEntry.getIssueEN());
			pstmt.setString(45, bibEntry.getIssueORG());
			pstmt.setString(46, bibEntry.getIssueTR());
			pstmt.setInt(47, bibEntry.getYearEN());
			pstmt.setString(48, bibEntry.getYearORG());
			pstmt.setString(49, bibEntry.getYearTR());
			pstmt.setBoolean(50, bibEntry.isUnpublished());
			pstmt.setInt(51, bibEntry.getAccessLevel());
			pstmt.setString(52, bibEntry.getAbstractText());
			pstmt.setString(53, bibEntry.getThesisType());
			pstmt.setString(54, bibEntry.getEditorType());
			pstmt.setBoolean(55, bibEntry.isOfficialTitleTranslation());
			pstmt.setString(56, bibEntry.getBibtexKey());
			pstmt.setBoolean(57, bibEntry.getAnnotation());
			pstmt.setInt(58, bibEntry.getAnnotatedBibliographyID());
			pstmt.executeUpdate();

			updateAuthorBibRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getAuthorList());
			updateEditorBibRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getEditorList());
			updateBibKeywordRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getKeywordList());
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		bibEntry.setModifiedOn(df.format(date));
		protocollModifiedAbstractEntry(bibEntry,"");
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateAnnotatedBiblographyEntry brauchte "+diff + " Millisekunden.");;}}
		return bibEntry;
	}

	/**
	 * @param sqlWhere
	 * @return
	 */
	public ArrayList<Integer> getDepictionFromIconography(String sqlWhere) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<Integer> result = new ArrayList<Integer>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		System.err.println("SELECT * FROM DepictionIconographyRelation WHERE " + sqlWhere);
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM DepictionIconographyRelation WHERE " + sqlWhere);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new Integer(rs.getInt("DepictionID")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDepictionFromIconography brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<ImageEntry> getImagesbyOrnamentID(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM Images JOIN OrnamentImageRelation ON OrnamentImageRelation.ImageID = Images.ImageID WHERE Images.deleted=0 and OrnamentID ="
							+ ornamentID + " ORDER BY Title Asc");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				ImageEntry image = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
						rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")),getLocation(rs.getInt("location")),rs.getString("InventoryNumber"),rs.getDouble("Height"),rs.getDouble("Width"));
				if (image.getLocation()==null) {
					if (rs.getString("Title")!=null){
						image.setLocation(searchLocationByFilename(image.getTitle()));
					}
				}
				if (image.getInventoryNumber()==null||image.getInventoryNumber()=="") {
					if (rs.getString("Title")!=null) {
						image.setInventoryNumber(searchInventoryNumberByFilename(image.getTitle()));
					}
				}

				results.add(image);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDistricts brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecPatternsbyOrnamentID(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<InnerSecondaryPatternsEntry> result = new ArrayList<InnerSecondaryPatternsEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM InnerSecondaryPattern JOIN InnerSecondaryPatternRelation ON InnerSecondaryPatternRelation.InnerSecID = InnerSecondaryPattern.InnerSecID WHERE OrnamentID ="
							+ ornamentID);
			while (rs.next()) {
				result.add(new InnerSecondaryPatternsEntry(rs.getInt("InnerSecID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getImagesbyOrnamentID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	private ArrayList<OrnamentCaveRelation> getCaveRelationbyOrnamentID(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		// zweite Hierarchie Ebene der Ornamentik
		Connection dbc = getConnection();
		ArrayList<OrnamentCaveRelation> result = new ArrayList<OrnamentCaveRelation>();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs;

			rs = stmt.executeQuery("SELECT * FROM CaveOrnamentRelation WHERE OrnamentID = " + ornamentID);
			while (rs.next()) {
				result.add(new OrnamentCaveRelation(rs.getInt("CaveOrnamentRelationID"), getStylebyID(rs.getInt("StyleID")),
						rs.getInt("OrnamentID"), getDistrict(getCave(rs.getInt("CaveID")).getDistrictID()), getCave(rs.getInt("CaveID")),
						rs.getString("Colours"), rs.getString("Notes"), rs.getString("GroupOfOrnaments"),
						rs.getString("SimilarElementsofOtherCultures"), getIconographyElementsbyOrnamentCaveID(rs.getInt("CaveOrnamentRelationID")),
						getRelatedOrnamentsbyOrnamentCaveID(rs.getInt("CaveOrnamentRelationID")),
						getWallsbyOrnamentCaveID(rs.getInt("CaveOrnamentRelationID"))));
					//	,getOrientationsbyOrnamentCaveID(rs.getInt("CaveOrnamentRelationID"))));
				
				//Aufruf der 3. Hierarchie Ebene und der Tabelle OrnamentCaveWallRelation
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}

		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getCaveRelationbyOrnamentID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public ArrayList<OrnamentComponentsEntry> getOrnamentComponentsbyOrnamentID(int ornamentID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}

		ArrayList<OrnamentComponentsEntry> result = new ArrayList<OrnamentComponentsEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM OrnamentComponent JOIN OrnamentComponentRelation ON OrnamentComponent.OrnamentComponentID = OrnamentComponentRelation.OrnamentComponentID WHERE OrnamentID = "
							+ ornamentID);
			while (rs.next()) {
				result.add(new OrnamentComponentsEntry(rs.getInt("OrnamentComponentID"), rs.getString("Name")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getOrnamentComponentsbyOrnamentID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/*public ArrayList<OrientationEntry> getOrientationsbyOrnamentCaveID(int ornamentCaveID) {
		OrientationEntry result = null;
		ArrayList<OrientationEntry> orientations = new ArrayList<OrientationEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM OrnamenticOrientation JOIN OrnamentOrientationRelation ON OrnamenticOrientation.OrnamenticOrientationID = OrnamentOrientationRelation.OrientationID WHERE OrnamentCaveRelationID="
							+ ornamentCaveID);
			while (rs.next()) {
				result = new OrientationEntry(rs.getInt("OrnamenticOrientationID"), rs.getString("Name"));
				orientations.add(result);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		return orientations;
	}
*/
	
	private ArrayList<IconographyEntry> getIconographyElementsbyOrnamentCaveID(int ornamentCaveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM OrnamentCaveIconographyRelation WHERE OrnamentCaveRelationID="
							+ ornamentCaveID + ")");
			while (rs.next()) {
				IconographyEntry result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"), rs.getString("search"));
				if (rootItems.containsKey(result.getIconographyID())){
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				else {
					rootItems.put(result.getIconographyID(), findIconographyRoot(result.getIconographyID()));
					result.setRoot(rootItems.get(result.getIconographyID()));
				}
				results.add(result);
				System.err.println("getIconographyElementsbyOrnamentCaveID: IconographyID=" + rs.getInt("IconographyID"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getIconographyElementsbyOrnamentCaveID brauchte "+diff + " Millisekunden.");;}}
		return results;
	}

	public ArrayList<OrnamentEntry> getRelatedOrnamentsbyOrnamentCaveID(int ornamentCaveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM Ornaments JOIN RelatedOrnamentsRelation ON Ornaments.OrnamentID = RelatedOrnamentsRelation.OrnamentID WHERE Ornaments.deleted =0 and OrnamentCaveRelationID = "
							+ ornamentCaveID);
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedOrnamentsbyOrnamentCaveID brauchte "+diff + " Millisekunden.");;}}
		return results;

	}

	public ArrayList<WallOrnamentCaveRelation> getWallsbyOrnamentCaveID(int ornamentCaveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<WallOrnamentCaveRelation> results = new ArrayList<WallOrnamentCaveRelation>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM OrnamentCaveWallRelation WHERE OrnamentCaveRelationID = " + ornamentCaveID);
			while (rs.next()) {
				results.add(new WallOrnamentCaveRelation(rs.getInt("OrnamentCaveWallRelationID"), rs.getInt("OrnamentCaveRelationID"),
						rs.getInt("PositionID"), rs.getInt("FunctionID"), rs.getString("Notes"),
						getWall(rs.getInt("CaveID"), rs.getInt("WallLocationID"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWallsbyOrnamentCaveID brauchte "+diff + " Millisekunden.");;}}
		return results;

	}

	/**
	 * @deprecated please use {@link #getWall(int, int)} instead!
	 * @param wallLocationID
	 * @param caveID
	 * @return
	 */
	private WallEntry getWallbyWallLocationANDCaveID(int wallLocationID, int caveID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		WallEntry result = null;

		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Walls WHERE WallLocationID =" + wallLocationID + " AND CaveID = " + caveID);
			while (rs.next()) {
				result = new WallEntry(rs.getInt("CaveID"), rs.getInt("WallLocationID"), rs.getInt("PreservationClassificationID"),
						rs.getDouble("Width"), rs.getDouble("Height"));

			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getWallbyWallLocationANDCaveID brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	/**
	 * @param clientName
	 * @param message
	 */
	public void doLogging(String clientName, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("MMM dd,yyyy HH:mm");    
		Date resultdate = new Date(System.currentTimeMillis());
		System.err.println(">>> " + clientName + " ("+sdf.format(resultdate)+")" + ": " + message);
	}

	/**
	 * @return
	 */
	public ArrayList<BibKeywordEntry> getBibKeywords() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<BibKeywordEntry> result = new ArrayList<BibKeywordEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM BibKeywords");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new BibKeywordEntry(rs.getInt("BibKeywordID"), rs.getString("Keyword")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getBibKeywords brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	
	/**
	 * loads bibKeywords related to the annotated bibliography ID
	 * @param bibID
	 * @return
	 */
	private ArrayList<BibKeywordEntry> getRelatedBibKeywords(int bibID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<BibKeywordEntry> result = new ArrayList<BibKeywordEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM BibKeywords WHERE BibKeywordID IN (SELECT DISTINCT BibKeywordID FROM BibKeywordBibliographyRelation WHERE BibID=?)");
			pstmt.setInt(1, bibID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new BibKeywordEntry(rs.getInt("BibKeywordID"), rs.getString("Keyword")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedBibKeywords brauchte "+diff + " Millisekunden.");;}}
		return result;
	}
	
	private void updateBibKeywordRelation(int bibID, ArrayList<BibKeywordEntry> keywordList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		if (keywordList == null)
			return;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		deleteEntry("DELETE FROM BibKeywordBibliographyRelation WHERE BibID=" + bibID); // in case there are already relations
		try {
			pstmt = dbc.prepareStatement("INSERT INTO BibKeywordBibliographyRelation (BibID, BibKeywordID) VALUES (?, ?)");
			pstmt.setInt(1, bibID);
			for (BibKeywordEntry bke : keywordList) {
				pstmt.setInt(2, bke.getBibKeywordID());
				pstmt.executeUpdate();
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateBibKeywordRelation brauchte "+diff + " Millisekunden.");;}}
	}

	/**
	 * @param bkEntry
	 * @return
	 */
	public int insertBibKeyword(BibKeywordEntry bkEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement cgStatement;
		int bibKeywordID = 0;
		try {
			cgStatement = dbc.prepareStatement("INSERT INTO BibKeywords (Keyword) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			cgStatement.setString(1, bkEntry.getBibKeyword());
			cgStatement.executeUpdate();
			ResultSet keys = cgStatement.getGeneratedKeys();
			if (keys.first()) {
				bibKeywordID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertBibKeyword brauchte "+diff + " Millisekunden.");;}}
		return bibKeywordID;
	}

	/**
	 * @param selectedEntry
	 * @return
	 */
	public boolean deleteAuthorEntry(AuthorEntry selectedEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM AuthorBibliographyRelation WHERE AuthorID=?");
			pstmt.setInt(1, selectedEntry.getAuthorID());
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				return false;
			}
			pstmt = dbc.prepareStatement("SELECT * FROM EditorBibliographyRelation WHERE AuthorID=?");
			pstmt.setInt(1, selectedEntry.getAuthorID());
			rs = pstmt.executeQuery();
			if (rs.first()) {
				return false;
			}
			if (deleteEntry("DELETE FROM Authors WHERE AuthorID=" + selectedEntry.getAuthorID())) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von deleteAuthorEntry brauchte "+diff + " Millisekunden.");;}}
		return false;
	}

	/**
	 * @param currentUser
	 * @param newPasswordHash 
	 * @return
	 */
	public boolean updateUserEntry(UserEntry currentUser, String passwordHash, String newPasswordHash) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int rowsChangedCount;
		
		try {
			if (newPasswordHash != null && !newPasswordHash.isEmpty()) {
				pstmt = dbc.prepareStatement("UPDATE Users SET Email=?, Affiliation=?, Password=? WHERE UserID=? AND Password=?");
				pstmt.setString(1, currentUser.getEmail());
				pstmt.setString(2, currentUser.getAffiliation());
				pstmt.setString(3, newPasswordHash);
				pstmt.setInt(4, currentUser.getUserID());
				pstmt.setString(5, passwordHash);
			} else {
				pstmt = dbc.prepareStatement("UPDATE Users SET Email=?, Affiliation=? WHERE UserID=? AND Password=?");
				pstmt.setString(1, currentUser.getEmail());
				pstmt.setString(2, currentUser.getAffiliation());
				pstmt.setInt(3, currentUser.getUserID());
				pstmt.setString(4, passwordHash);
			}	
			rowsChangedCount = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateUserEntry brauchte "+diff + " Millisekunden.");;}}
		return rowsChangedCount > 0;
	}
    private String shuffleString(String string) {
        List<String> letters = Arrays.asList(string.split(""));
        Collections.shuffle(letters);
        return letters.stream().collect(Collectors.joining());
    }
    private String generate_password(int length) {
    	SecureRandom random = new SecureRandom();
    	String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";
        String OTHER_CHAR = "!@#$%&*()_+-=[]?";
        String PASSWORD_ALLOW_BASE = CHAR_LOWER + CHAR_UPPER + NUMBER + OTHER_CHAR;
        String PASSWORD_ALLOW_BASE_SHUFFLE = shuffleString(PASSWORD_ALLOW_BASE);
        String PASSWORD_ALLOW = PASSWORD_ALLOW_BASE_SHUFFLE;
        if (length < 1) throw new IllegalArgumentException();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {

            int rndCharAt = random.nextInt(PASSWORD_ALLOW.length());
            char rndChar = PASSWORD_ALLOW.charAt(rndCharAt);

            // debug
            //System.out.format("%d\t:\t%c%n", rndCharAt, rndChar);

            sb.append(rndChar);

        }

        return sb.toString();

    }
	public boolean resetPassword(UserEntry currentUser) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int rowsChangedCount;
		
		try {
			String pwd = generate_password(9);
//			pstmt.setString(2, "ff4ea6b28f247ccdd4a03321dc2bd1a");
			
			pstmt = dbc.prepareStatement("UPDATE Users SET Password=? WHERE UserID=?");
			pstmt.setString(1, cryptWithMD5(pwd));
			pstmt.setInt(2, currentUser.getUserID());
			rowsChangedCount = pstmt.executeUpdate();
			sendMail("radisch@saw-leipzig.de",currentUser.getEmail(),currentUser.getLastname()+", "+currentUser.getFirstname(),"Your Password for kucha.saw-leipzig.de was reseted.","Dear "+currentUser.getFirstname()+ " "+currentUser.getLastname()+",\n the Admin has reseted the password of your account in kucha.saw-leipzig.de for you.\nYour username is:"+currentUser.getUsername()+"\nYour password is:  \""+pwd+"\"\n");
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateUserEntry brauchte "+diff + " Millisekunden.");;}}
		return rowsChangedCount > 0;
	}

	private void sendMail(String from, String to, String toName, String subject, String message) {
        String output=null;
        Properties props = System.getProperties();
	    String smtpHostServer = "zimbra.saw-leipzig.de";
	    props.put("mail.smtp.host", smtpHostServer);
		props.put("mail.smtp.port", "465"); //TLS Port
		props.put("mail.smtp.auth", "true"); //enable authentication
		props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
		props.put("mail.smtp.socketFactory.port", "465"); //SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		
                //create Authenticator object to pass in Session.getInstance argument
		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(serverProperties.getProperty("mail.user"), serverProperties.getProperty("mail.pw"));
			}
		};
       try {
           Session session = Session.getDefaultInstance(props, auth);
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from, "Kucha-Admin"));
            msg.addRecipient(Message.RecipientType.TO,
                             new InternetAddress(to, toName));
            msg.setSubject(subject);
            msg.setText(message);
            msg.setReplyTo(new InternetAddress[]{new InternetAddress("radisch@saw-leipzig.de")});
            Transport.send(msg);

        } catch (Exception e) {
            System.err.println(e.toString());                
       }   
    }
	private static String cryptWithMD5(String pass) {
		if (pass == null || pass.isEmpty()) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public int insertUserEntry(UserEntry userEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		System.out.println(userEntry.getUserID());
		if (userEntry == null || userEntry.getUserID() > 0) {
			return 0;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int newUserID = 0;
		
		try {
			pstmt = dbc.prepareStatement("INSERT INTO Users (Username, Password, Firstname, Lastname, Email, Affiliation, AccessLevel) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userEntry.getUsername());
			String pwd = generate_password(9);
//			pstmt.setString(2, "ff4ea6b28f247ccdd4a03321dc2bd1a");
			pstmt.setString(2, cryptWithMD5(pwd));
			pstmt.setString(3, userEntry.getFirstname());
			pstmt.setString(4, userEntry.getLastname());
			pstmt.setString(5, userEntry.getEmail());
			pstmt.setString(6, userEntry.getAffiliation());
			pstmt.setInt(7, userEntry.getAccessLevel());
			if (url.contains("infotest")) {
				sendMail("radisch@saw-leipzig.de",userEntry.getEmail(),userEntry.getLastname()+", "+userEntry.getFirstname(),"An account has been created for you at kuchatest.saw-leipzig.de","Dear "+userEntry.getFirstname()+ " "+userEntry.getLastname()+",\n the Admin has created an Account in kuchatest.saw-leipzig.de for you.\nYour username is:"+userEntry.getUsername()+"\nYour password is:  \""+pwd+"\"\n");
			}
			else {
				sendMail("radisch@saw-leipzig.de",userEntry.getEmail(),userEntry.getLastname()+", "+userEntry.getFirstname(),"An account has been created for you at kucha.saw-leipzig.de","Dear "+userEntry.getFirstname()+ " "+userEntry.getLastname()+",\n the Admin has created an Account in kucha.saw-leipzig.de for you.\nYour username is:"+userEntry.getUsername()+"\nYour password is:  \""+pwd+"\"\n");

			}
			
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.first()) {
				newUserID = keys.getInt(1);
			}
			keys.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return 0;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von insertUserEntry brauchte "+diff + " Millisekunden.");;}}
		return newUserID;
	}

	/**
	 * 
	 * @param userEntry
	 * @return
	 */
	public boolean updateUserEntry(UserEntry userEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		if (userEntry == null) {
			return false;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int rowsChangedCount;
		
		try {
			pstmt = dbc.prepareStatement("UPDATE Users SET Username=?, Firstname=?, Lastname=?, Email=?, Affiliation=?, AccessLevel=? WHERE UserID=?");
			pstmt.setString(1, userEntry.getUsername());
			pstmt.setString(2, userEntry.getFirstname());
			pstmt.setString(3, userEntry.getLastname());
			pstmt.setString(4, userEntry.getEmail());
			pstmt.setString(5, userEntry.getAffiliation());
			pstmt.setInt(6, userEntry.getAccessLevel());
			pstmt.setInt(7, userEntry.getUserID());
			rowsChangedCount = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von updateUserEntry brauchte "+diff + " Millisekunden.");;}}
		return rowsChangedCount > 0;
	}

	/**
	 * 
	 * @param searchEntry
	 * @return
	 * @throws IOException 
	 */
	public ArrayList<DepictionEntry> searchDepictions(DepictionSearchEntry searchEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<DepictionEntry> results = new ArrayList<DepictionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String where = "";
		
		if (searchEntry.getShortName() != null && !searchEntry.getShortName().isEmpty()) {
			where = "ShortName LIKE ?";
		}
		where += where.isEmpty() ? "deleted=0" : " AND deleted=0";
		String caveIDs="";
		for (int caveID : searchEntry.getCaveIdList()) {
			caveIDs += caveIDs.isEmpty() ? Integer.toString(caveID) : "," + caveID;
		}
		if (!caveIDs.isEmpty()) {
			where += where.isEmpty() ? "CaveID IN (" + caveIDs + ")" : " AND CaveID IN (" + caveIDs + ")";
		}
		
		String locationIDs = "";
		for (int locationID : searchEntry.getLocationIdList()) {
			locationIDs += locationIDs.isEmpty() ? Integer.toString(locationID) : "," + locationID;
		}
		if (!locationIDs.isEmpty()) {
			where += where.isEmpty() ? "CurrentLocationID IN (" + locationIDs + ")" : " AND CurrentLocationID IN (" + locationIDs + ")";
		}
		
		String iconographyIDs = "";
		for (int iconographyID : searchEntry.getIconographyIdList()) {
			iconographyIDs += iconographyIDs.isEmpty() ? Integer.toString(iconographyID) : "," + iconographyID;
		}
		if (!iconographyIDs.isEmpty()) {
			where += where.isEmpty() 
					? "DepictionID IN (SELECT DepictionID FROM DepictionIconographyRelation WHERE IconographyID IN (" + iconographyIDs + ") GROUP BY DepictionID HAVING (COUNT(DepictionID) >= " 
						+ searchEntry.getCorrelationFactor() + ")) or DepictionID in (SELECT Polygon.DepictionID FROM Annotations inner join Polygon on (Polygon.AnnotoriousID=Annotations.AnnotoriousID) WHERE Annotations.IconographyID IN ("+iconographyIDs+") and Polygon.deleted=0 and Polygon.Polygon is not null and Annotations.deleted=0  GROUP BY DepictionID HAVING (COUNT(DepictionID) >= "+searchEntry.getCorrelationFactor()+"))"
					: " AND DepictionID IN (SELECT DepictionID FROM DepictionIconographyRelation WHERE IconographyID IN (" + iconographyIDs + ") GROUP BY DepictionID HAVING (COUNT(DepictionID) >= " 
						+ searchEntry.getCorrelationFactor() + ")) or DepictionID in (SELECT Polygon.DepictionID FROM Annotations inner join Polygon on (Polygon.AnnotoriousID=Annotations.AnnotoriousID) WHERE Annotations.IconographyID IN ("+iconographyIDs+") and Polygon.deleted=0 and Polygon.Polygon is not null and Annotations.deleted=0  GROUP BY DepictionID HAVING (COUNT(DepictionID) >= "+searchEntry.getCorrelationFactor()+"))";
		}
		String wallTreeIDs = "";
		for (int wallTreeID : searchEntry.getWallIDList()) {
			wallTreeIDs += wallTreeIDs.isEmpty() ? Integer.toString(wallTreeID) : "," + wallTreeID;
		}
		if (!wallTreeIDs.isEmpty()) {
			where += where.isEmpty() 
					? "DepictionID IN (SELECT DepictionID FROM DepictionWallsRelation WHERE WallID IN (" + wallTreeIDs + ") GROUP BY DepictionID HAVING (COUNT(DepictionID) >= " 
						+ searchEntry.getCorrelationFactorWT() + "))"
					: " AND DepictionID IN (SELECT DepictionID FROM DepictionWallsRelation WHERE WallID IN (" + wallTreeIDs + ") GROUP BY DepictionID HAVING (COUNT(DepictionID) >= " 
						+ searchEntry.getCorrelationFactorWT() + "))";
		}
		String positionIDs = "";
		for (int positionID : searchEntry.getPositionIDList()) {
			positionIDs += positionIDs.isEmpty() ? Integer.toString(positionID) : "," + positionID;
		}
		if (!positionIDs.isEmpty()) {
			where += where.isEmpty() 
					? "DepictionID IN (SELECT DepictionID FROM WallPositionsRelation WHERE PositionID IN (" + positionIDs + ")) "
					: " AND DepictionID IN (SELECT DepictionID FROM WallPositionsRelation WHERE PositionID IN (" + positionIDs + ")) ";
		}
		String imgIDs = "";
		for (int imgID : searchEntry.getImageIdList()) {
			imgIDs += imgIDs.isEmpty() ? Integer.toString(imgID) : "," + imgID;
		}
		if (!imgIDs.isEmpty()) {
			where += where.isEmpty() 
					? "DepictionID IN (SELECT DepictionID FROM DepictionImageRelation WHERE ImageID IN (" + imgIDs + ")) "
					: " AND DepictionID IN (SELECT DepictionID FROM DepictionImageRelation WHERE ImageID IN (" + imgIDs + ")) " ;
		}
		
		String bibIDs = "";
		for (int bibID : searchEntry.getBibIdList()) {
			bibIDs += bibIDs.isEmpty() ? Integer.toString(bibID) : "," + bibID;
 		}
		if (!bibIDs.isEmpty()) {
			where += where.isEmpty() 
					? "DepictionID IN (SELECT DISTINCT DepictionID FROM DepictionBibliographyRelation WHERE BibID IN (" + bibIDs + "))" 
					: " AND DepictionID IN (SELECT DISTINCT DepictionID FROM DepictionBibliographyRelation WHERE BibID IN (" + bibIDs + "))";
		}

		/**
		 * We cannot filter the accessLevel because that would create problems e.g. when choosing a cave for a depiction.
		 * What we can do is restricting the visibility of certain fields e.g. comments but this has to be done 
		 * when the UI is build on the client side!
		 */
		String inStatement  = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		
		if (getAccessLevelForSessionID(searchEntry.getSessionID()) <= UserEntry.GUEST) {
			where += where.isEmpty() ? "AccessLevel IN (" + inStatement + ")" : " AND AccessLevel IN (" + inStatement + ")";
		}
				
		System.err.println(where.isEmpty() ? "SELECT * FROM Depictions" : "SELECT * FROM Depictions WHERE " + where);

		try {
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM Depictions LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+ ", "+Integer.toString(searchEntry.getMaxentries()): "SELECT * FROM Depictions WHERE " + where+" LIMIT "+Integer.toString(searchEntry.getEntriesShowed())+", "+Integer.toString(searchEntry.getMaxentries()));

			if (!searchEntry.getShortName().isEmpty()) {
				pstmt.setString(1,searchEntry.getShortName().replace("*", "%"));
			}
			
			
			ResultSet rs = pstmt.executeQuery();
			int accessLevel = -1;
			accessLevel = getAccessLevelForSessionID(searchEntry.getSessionID());
			while (rs.next()) {
				DepictionEntry de = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("SeparateAksaras"), rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getDouble("Width"), rs.getDouble("Height"),
						getExpedition(rs.getInt("ExpeditionID")), rs.getDate("PurchaseDate"), getLocation(rs.getInt("CurrentLocationID")), rs.getString("InventoryNumber"),
						getVendor(rs.getInt("VendorID")), rs.getInt("StoryID"), getCave(rs.getInt("CaveID")), getwallsbyDepictionID(rs.getInt("DepictionID")), rs.getInt("AbsoluteLeft"),
						rs.getInt("AbsoluteTop"), rs.getInt("ModeOfRepresentationID"), rs.getString("ShortName"), rs.getString("PositionNotes"),
						rs.getInt("MasterImageID"), rs.getInt("AccessLevel"), rs.getString("LastChangedByUser"), rs.getString("LastChangedOnDate"),getAnnotations(rs.getInt("DepictionID")));
				de.setRelatedImages(getRelatedImages(de.getDepictionID(), searchEntry.getSessionID(),accessLevel));
				de.setRelatedBibliographyList(getRelatedBibliographyFromDepiction(de.getDepictionID()));
				de.setRelatedIconographyList(getRelatedIconography(de.getDepictionID()));
				results.add(de);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von searchDepictions brauchte "+diff + " Millisekunden. where-Klausel: "+where);;}}
		return results;
	}
  	private ArrayList<WallTreeEntry> getwallsbyDepictionID (Integer depictionID){
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<WallTreeEntry> results = new ArrayList<WallTreeEntry>();
		int zaehler1 =0;
		int zaehler2 =0;
		try {
			zaehler2=0;
			pstmt = dbc.prepareStatement("SELECT * FROM DepictionWallsRelation left join WallLocationsTree on (WallLocationsTree.WallLocationID = DepictionWallsRelation.WallID) WHERE DepictionID ="+Integer.toString(depictionID) );
			ResultSet rs = pstmt.executeQuery();
			zaehler1+=1;
			while (rs.next()) {
				ArrayList<PositionEntry> positions = getPositionsbyWallrelation(depictionID, rs.getInt("WallID"));
				if (positions!=null) {
					for (PositionEntry pe : positions){
					}
				}
				WallTreeEntry wte = new WallTreeEntry(rs.getInt("WallID"),rs.getInt("ParentID"),rs.getString("Text"),rs.getString("search"),positions );
				results.add(wte);
				zaehler2+=1;
				}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		return results;
	}	
	private ArrayList<PositionEntry> getPositionsbyWallrelation (int depictionID, int wallID){
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<PositionEntry> results = new ArrayList<PositionEntry>();
		try {
			pstmt = dbc.prepareStatement("SELECT DepictionID, WallID, Position.PositionID, Name FROM WallPositionsRelation inner join Position on (WallPositionsRelation.PositionID=Position.PositionID) WHERE DepictionID = "+depictionID+" and WallID = "+wallID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				if (rs.getInt("PositionID")!=-1){
					results.add(new PositionEntry(rs.getInt("PositionID"),rs.getString("Name")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		//System.out.println("Größe des Resultats von DepictionWallsRealtion "+Integer.toString(results.size())+" für "+Integer.toString(depictionID));
		return results;
	}
  public boolean deleteEntry(AbstractEntry entry) {
		if (entry instanceof DepictionEntry) {
			System.out.println("gut!");
		}
		return true;
	}

	public ArrayList<UserEntry> getUsers() {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		ArrayList<UserEntry> result = new ArrayList<UserEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Users ORDER BY Username Asc");
			while (rs.next()) {
				result.add(new UserEntry(rs.getInt("UserID"), rs.getString("Username"), rs.getString("Firstname"), rs.getString("Lastname"),
						rs.getString("Email"), rs.getString("Affiliation"), rs.getInt("AccessLevel"), rs.getString("SessionID"), 
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getUsers brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public boolean saveCollectedEntries(String sessionID, String collectionLabel, boolean isGroupCollection, ArrayList<AbstractEntry> entryList) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		UserEntry ue = this.checkSessionID(sessionID);
		if (ue == null) {
			return false;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int collectionID=0;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Collections WHERE deleted=0 and UserID=? AND CollectionLabel=?");
			pstmt.setInt(1, ue.getUserID());
			pstmt.setString(2, collectionLabel);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				collectionID = rs.getInt("CollectionID");
				rs.close();
				pstmt.close();
			} else {
				rs.close();
				pstmt.close();
				pstmt = dbc.prepareStatement("INSERT INTO Collections (UserID, CollectionLabel, GroupCollection) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
				pstmt.setInt(1, ue.getUserID());
				pstmt.setString(2, collectionLabel);
				pstmt.setBoolean(3, isGroupCollection);
				pstmt.executeQuery();
				ResultSet keys = pstmt.getGeneratedKeys();
				if (keys.first()) {
					collectionID = keys.getInt(1);
				}
				keys.close();
				pstmt.close();
			}
			if (collectionID > 0) {
				deleteEntry("DELETE FROM CollectionEntryRelation WHERE CollectionID=" + collectionID);
				PreparedStatement insertStatement = dbc.prepareStatement("INSERT INTO CollectionEntryRelation (CollectionID, EntryID, EntryClass) VALUES (?,?,?)");
				insertStatement.setInt(1, collectionID);
				for (AbstractEntry entry : entryList) {
					if (entry instanceof CaveEntry) {
						insertStatement.setInt(2, ((CaveEntry)entry).getCaveID());
						insertStatement.setString(3, "CaveEntry");
					} else if (entry instanceof DepictionEntry) {
						insertStatement.setInt(2, ((DepictionEntry)entry).getDepictionID());
						insertStatement.setString(3, "DepictionEntry");
					} else if (entry instanceof AnnotatedBibliographyEntry) {
						insertStatement.setInt(2, ((AnnotatedBibliographyEntry)entry).getAnnotatedBibliographyID());
						insertStatement.setString(3, "AnnotatedBibliographyEntry");
					} else if (entry instanceof OrnamentEntry) {
						insertStatement.setInt(2, ((OrnamentEntry)entry).getOrnamentID());
						insertStatement.setString(3, "OrnamentEntry");
					} else if (entry instanceof ImageEntry) {
						insertStatement.setInt(2, ((ImageEntry)entry).getImageID());
						insertStatement.setString(3, "ImageEntry");
					}
					insertStatement.executeQuery();
				}
				insertStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return false;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von saveCollectedEntries brauchte "+diff + " Millisekunden.");;}}
		return true;
	}

	public ArrayList<CollectionEntry> getRelatedCollectionNames(String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<CollectionEntry> resultList = new ArrayList<CollectionEntry>();
		UserEntry ue = checkSessionID(sessionID);
		if (ue == null) {
			return resultList;
		}
		try {
			pstmt = dbc.prepareStatement(""
					+ "");
			pstmt.setInt(1, ue.getUserID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				resultList.add(new CollectionEntry(rs.getInt("CollectionID"), getUser(rs.getInt("UserID")), rs.getString("CollectionLabel"), rs.getBoolean("GroupCollection")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getRelatedCollectionNames brauchte "+diff + " Millisekunden.");;}}
		return resultList;
	}
	
	public CollectionEntry delCollectedEntries(CollectionEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<AbstractEntry> resultList = new ArrayList<AbstractEntry>();
		if (entry == null) {
			return null;
		}
		try {
			pstmt = dbc.prepareStatement("UPDATE Collections SET deleted = 1 WHERE (CollectionID = ?);");
			pstmt.setInt(1, entry.getCollectionID());
			ResultSet rs = pstmt.executeQuery();
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
			return null;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von delCollectedEntries brauchte "+diff + " Millisekunden.");;}}
		return entry;
	}
	public ArrayList<AbstractEntry> loadCollectedEntries(CollectionEntry entry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<AbstractEntry> resultList = new ArrayList<AbstractEntry>();
		if (entry == null) {
			return resultList;
		}
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM CollectionEntryRelation WHERE CollectionID=?");
			pstmt.setInt(1, entry.getCollectionID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				int entryID = rs.getInt("EntryID");
				String entryClass = rs.getString("EntryClass");
				switch (entryClass) {
					case "AnnotatedBibliographyEntry":
						resultList.add(getAnnotatedBiblographybyID(entryID, ""));
						break;
					case "CaveEntry":
						resultList.add(getCave(entryID));
						break;
					case "DepictionEntry":
						resultList.add(getDepictionEntry(entryID, entry.getUser().getSessionID()));
						break;
					case "OrnamentEntry":
						resultList.add(getOrnamentEntry(entryID));
						break;
					case "ImageEntry":
						resultList.add(getImageEntry(entryID));
						break;
					default:
						break;
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von loadCollectedEntries brauchte "+diff + " Millisekunden.");;}}
		return resultList;
	}

	private DepictionEntry getDepictionEntry(int depictionID, String sessionID) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		DepictionEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Depictions WHERE DepictionID=" + depictionID +" and deleted=0");
			System.out.println("Start Select");
			int accessLevel=-1;
			accessLevel = getAccessLevelForSessionID(sessionID);
			// we only need to call this once, since we do not expect more than 1 result!
			if (rs.next()) { 
				System.out.println("got result");				
				result = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("SeparateAksaras"), rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getDouble("Width"), rs.getDouble("Height"),
						getExpedition(rs.getInt("ExpeditionID")), rs.getDate("PurchaseDate"), getLocation(rs.getInt("CurrentLocationID")), rs.getString("InventoryNumber"),
						getVendor(rs.getInt("VendorID")), rs.getInt("StoryID"), getCave(rs.getInt("CaveID")), getwallsbyDepictionID(rs.getInt("DepictionID")), rs.getInt("AbsoluteLeft"),
						rs.getInt("AbsoluteTop"), rs.getInt("ModeOfRepresentationID"), rs.getString("ShortName"), rs.getString("PositionNotes"),
						rs.getInt("MasterImageID"), rs.getInt("AccessLevel"), rs.getString("LastChangedByUser"), rs.getString("LastChangedOnDate"),getAnnotations(rs.getInt("DepictionID")));
				result.setRelatedImages(getRelatedImages(result.getDepictionID(), sessionID,accessLevel));
				result.setRelatedBibliographyList(getRelatedBibliographyFromDepiction(result.getDepictionID()));
				result.setRelatedIconographyList(getRelatedIconography(result.getDepictionID()));
				System.out.println("Depiction found: "+result.getDepictionID());
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von getDepictionEntry brauchte "+diff + " Millisekunden.");;}}
		return result;
	}

	public int addPreservationClassification(PreservationClassificationEntry pcEntry) {
		long start = System.currentTimeMillis();
		if (dologgingbegin){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde ausgelöst.");;
		}
		Connection dbc = getConnection();
		PreparedStatement pStatement;
		int preservationAttributeID = 0;
		try {
			pStatement = dbc.prepareStatement("INSERT INTO PreservationClassifications (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			pStatement.setString(1, pcEntry.getName());
			pStatement.executeUpdate();
			ResultSet keys = pStatement.getGeneratedKeys();
			if (keys.next()) {
				preservationAttributeID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von "+ new Throwable().getStackTrace()[0].getMethodName()+" wurde abgebrochen:."+e.toString());;
		}
		if (dologging){
		long end = System.currentTimeMillis();
		long diff = (end-start);
		if (diff>100){
		System.out.println("                -->  "+System.currentTimeMillis()+"  SQL-Statement von addPreservationClassification brauchte "+diff + " Millisekunden.");;}}
		return preservationAttributeID;
	}
	
}