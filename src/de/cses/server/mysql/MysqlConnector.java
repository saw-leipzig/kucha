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
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.eclipse.jetty.jndi.java.javaNameParser;

import de.cses.server.ServerProperties;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
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

/**
 * This is the central Database connector. Here are all methods that we need for standard database operations, including user login and access management.
 * 
 * @author alingnau
 *
 */
public class MysqlConnector {

	private String url; // MysqlConnector.db.url
	private String user; // MysqlConnector.db.user
	private String password; // MysqlConnector.db.password

	// private int auto_increment_id;

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
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		try {
			if (connection == null || !connection.isValid(5)) {
				connection = DriverManager.getConnection(url, user, password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	public ArrayList<DistrictEntry> getDistricts(String sqlWhere) {
		ArrayList<DistrictEntry> result = new ArrayList<DistrictEntry>();
		Connection dbc = getConnection();
		Statement stmt;
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
			return null;
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
		}
		return result;
	}

	private PublicationTypeEntry getPublicationType(int id) {
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
		}
		return result;
	}

	public ArrayList<PublicationTypeEntry> getPublicationTypes() {
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
		}
		return result;
	}

	/**
	 * Creates a new image entry in the table 'Images'
	 * 
	 * @return auto incremented primary key 'ImageID'
	 */
	public synchronized ImageEntry createNewImageEntry() {
		ImageEntry entry = new ImageEntry();
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = dbc.prepareStatement(
					"INSERT INTO Images (Filename, Title, ShortName, Copyright, PhotographerID, Comment, Date, ImageTypeID, AccessLevel) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, "");
			pstmt.setString(2, entry.getTitle());
			pstmt.setString(3, entry.getShortName());
			pstmt.setString(4, entry.getCopyright());
			pstmt.setInt(5, entry.getImageAuthor() != null ? entry.getImageAuthor().getPhotographerID() : 0);
			pstmt.setString(6, entry.getComment());
			pstmt.setString(7, entry.getDate());
			pstmt.setInt(8, entry.getImageTypeID());
			pstmt.setInt(9, entry.getAccessLevel());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) { // there should only be 1 key returned here
				entry.setImageID(keys.getInt(1));
			}
			keys.close();
			pstmt.close();
		} catch (SQLException e) {
			return null;
		}
		return entry;
	}
	
	/**
	 * 
	 * @param caveID
	 * @return
	 */
	protected ArrayList<C14DocumentEntry> getC14Documents(int caveID) {
		C14DocumentEntry entry = new C14DocumentEntry();
		ArrayList<C14DocumentEntry> result = new ArrayList<C14DocumentEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM C14Documents WHERE CaveID=?");
			pstmt.setInt(1, caveID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = new C14DocumentEntry(rs.getString("C14DocumentName"), rs.getString("C14OriginalDocumentName"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			return null;
		}
		return result;
	}

	/**
	 * 
	 * @param caveID
	 * @param entryList
	 * @return
	 */
	private synchronized boolean writeC14DocumentEntry(int caveID, ArrayList<C14DocumentEntry> entryList) {
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
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			stmt.executeUpdate(sqlUpdate);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Executes a SQL delete using a predefined SQL DELETE string
	 * 
	 * @param sqlDelete
	 *          The full sql string including the DELETE statement
	 * @return true if sucessful
	 */
	public boolean deleteEntry(String sqlDelete) {
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			stmt.execute(sqlDelete);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public ArrayList<ImageEntry> searchImages(ImageSearchEntry searchEntry) {
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
		
		System.out.println("SELECT * FROM Images" + (!where.isEmpty() ? " WHERE " + where : "") + " ORDER BY Title Asc");
		
		try {
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM Images ORDER BY Title Asc" : "SELECT * FROM Images WHERE " + where + " ORDER BY Title Asc");
			int i = 1; // counter to fill ? in where clause
			if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
			}
			if (searchEntry.getCopyrightSearch() != null && !searchEntry.getCopyrightSearch().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getCopyrightSearch() + "%");
			}
			if (searchEntry.getCommentSearch() != null && !searchEntry.getCommentSearch().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getCommentSearch() + "%");
			}
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), rs.getInt("ImageTypeID"),
						rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (where.startsWith("AccessLevel") && results.size() > 100) {
			// when there is  not filter option selected the where clause only deals with AccessLevel
			// limiting the number of search results to avoid slowing down the system
			ArrayList<ImageEntry> subResultList = new ArrayList<ImageEntry>();
			for (ImageEntry ie : results.subList(0, 100)) {
				subResultList.add(ie);
			}
			return subResultList;
		}
		return results;	
	}

	/**
	 * 
	 * @return ArrayList<String> with result from 'SELECT * FROM Images'
	 */
	public ArrayList<ImageEntry> getImageEntries() {
		return getImageEntries(null);
	}

	public ArrayList<ImageEntry> getImageEntries(String sqlWhere) {
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			if (sqlWhere != null) {
				pstmt = dbc.prepareStatement("SELECT * FROM Images WHERE " + sqlWhere + " ORDER BY Title Asc");
			} else {
				pstmt = dbc.prepareStatement("SELECT * FROM Images ORDER BY Title Asc");
			}
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), 
						rs.getInt("ImageTypeID"), rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	/**
	 * 
	 * @param imageID
	 * @return ImageEntry that matches imageID, or NULL
	 */
	public ImageEntry getImageEntry(int imageID) {
		Connection dbc = getConnection();
		ImageEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images WHERE ImageID=" + imageID);
			if (rs.first()) {
				result = new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), 
						rs.getInt("ImageTypeID"), rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<PhotographerEntry> getPhotographerEntries() {
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
			return null;
		}
		return results;
	}

	/**
	 * 
	 * @return
	 */
	public PhotographerEntry getPhotographerEntry(int id) {
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
			return null;
		}
		return result;
	}

	public ArrayList<CaveEntry> searchCaves(CaveSearchEntry searchEntry) {
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
			where += where.isEmpty() ? "SiteID IN (" + siteIdSet + ")" : " AND SiteID IN (" + siteIdSet + ")";
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
		if (!caveIdSet.isEmpty()) {
			where += where.isEmpty() ? "CaveID IN (" + caveIdSet + ")" : " AND CaveID IN (" + caveIdSet + ")";
		}
		
		if (!searchEntry.getHistoricalName().isEmpty()) {
			where += where.isEmpty() ? "(HistoricName LIKE ? OR OptionalHistoricName LIKE ?)" : " AND (HistoricName LIKE ? OR OptionalHistoricName LIKE ?)";
		}

		if (searchEntry.isDecoratedOnly()) {
			where += where.isEmpty() ? "CaveID IN (SELECT CaveID FROM Depictions)" : " AND CaveID IN (SELECT CaveID FROM Depictions)";
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
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM Caves" : "SELECT * FROM Caves WHERE " + where);
			if (!searchEntry.getHistoricalName().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getHistoricalName() + "%");
				pstmt.setString(i++, "%" + searchEntry.getHistoricalName() + "%");
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
			return null;
		}
		return results;
	}

	public ArrayList<CaveEntry> getCaves() {
		return getCaves(null);
	}

	public ArrayList<CaveEntry> getCaves(String sqlWhere) {
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery((sqlWhere == null) ? "SELECT * FROM Caves" : "SELECT * FROM Caves WHERE " + sqlWhere);
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
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public CaveEntry getCave(int id) {
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
			return null;
		}
		return result;
	}

	public ArrayList<CaveEntry> getCavesbyDistrictID(int districtID) {
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
			return null;
		}
		return results;
	}

	public ArrayList<OrnamentEntry> getOrnaments() {
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Ornaments");
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						//rs.getString("Annotation"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")), 
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
				// Aufruf der h�heren Hierarchie Ebenen der Ornamentik mittels getCaveRelation
				// Aufruf der Tabellen OrnamentComponentsRelation, OrnamentImageRelation und InnerSecondaryPatternRelation
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ArrayList<OrnamentOfOtherCulturesEntry> getOrnametsOfOtherCultures() {
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
			return null;
		}
		return results;
	}

	public CaveTypeEntry getCaveTypebyID(int caveTypeID) {
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
		}
		return result;
	}

	public ArrayList<CaveTypeEntry> getCaveTypes() {
		return getCaveTypes(null);
	}

	public ArrayList<CaveTypeEntry> getCaveTypes(String sqlWhere) {
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
			return results;
		}
		return results;

	}

	public int saveOrnamentEntry(OrnamentEntry ornamentEntry) {
		int newOrnamentID = 0;
		Connection dbc = getConnection();
		PreparedStatement ornamentStatement;
		// deleteEntry("DELETE FROM Ornaments WHERE OrnamentID=" + ornamentEntry.getCode());
		try {
			ornamentStatement = dbc.prepareStatement("INSERT INTO Ornaments (Code, Description, Remarks, Interpretation, OrnamentReferences, OrnamentClassID, StructureOrganizationID) VALUES (?, ?, ?, ?, ?, ?, ?)",
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
			ornamentStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return newOrnamentID;
	}
	
	/**
	 * @param oEntry
	 * @return
	 */
	public boolean updateOrnamentEntry(OrnamentEntry ornamentEntry) {
		Connection dbc = getConnection();
		PreparedStatement ornamentStatement;
		try {
			ornamentStatement = dbc.prepareStatement("UPDATE Ornaments SET Code=?, Description=?, Remarks=?, Interpretation=?, OrnamentReferences=?, OrnamentClassID=?, StructureOrganizationID=? WHERE OrnamentID=?");
			ornamentStatement.setString(1, ornamentEntry.getCode());
			ornamentStatement.setString(2, ornamentEntry.getDescription());
			ornamentStatement.setString(3, ornamentEntry.getRemarks());
			ornamentStatement.setString(4, ornamentEntry.getInterpretation());
			ornamentStatement.setString(5, ornamentEntry.getReferences());
			ornamentStatement.setInt(6, ornamentEntry.getOrnamentClass());
			ornamentStatement.setInt(7, ornamentEntry.getStructureOrganizationID());
			ornamentStatement.setInt(8, ornamentEntry.getOrnamentID());
			ornamentStatement.executeUpdate();

			updateInnerSecondaryPatternsRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getInnerSecondaryPatterns());
			updateOrnamentComponentsRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getOrnamentComponents());
			updateOrnamentImageRelations(ornamentEntry.getOrnamentID(), ornamentEntry.getImages());
			updateCaveOrnamentRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getCavesRelations());
			writeOrnamenticBibliographyRelation(ornamentEntry.getOrnamentID(), ornamentEntry.getRelatedBibliographyList());
			ornamentStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void updateOrnamentComponentsRelations(int ornamentID, List<OrnamentComponentsEntry> ornamentComponents) {
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
		}
	}

	private void updateInnerSecondaryPatternsRelations(int ornamentID, List<InnerSecondaryPatternsEntry> innerSecPatterns) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param newOrnamentID
	 * @param cavesRelations
	 */
	private void updateCaveOrnamentRelation(int ornamentID, List<OrnamentCaveRelation> cavesRelations) {
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
					wallCaveOrnamentRelationStatement.setInt(1, we.getWall().getWallLocationID());
					wallCaveOrnamentRelationStatement.setInt(2, we.getOrnamenticPositionID());
					wallCaveOrnamentRelationStatement.setInt(3, we.getOrnamenticFunctionID());
					wallCaveOrnamentRelationStatement.setString(4, we.getNotes());
					wallCaveOrnamentRelationStatement.setInt(5, newCaveOrnamentRelationID);
					wallCaveOrnamentRelationStatement.setInt(6, we.getWall().getCaveID());
					wallCaveOrnamentRelationStatement.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void updateOrnamentImageRelations(int ornamentID, ArrayList<ImageEntry> imgEntryList) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method allows selecting only used IconoghtaphyEntry elements
	 * @return List of IconographyEntry that are used in relation with pictorial elements
	 */
	public ArrayList<IconographyEntry> getIconographyEntriesUsedInDepictions() {
		ArrayList<IconographyEntry> result = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM DepictionIconographyRelation) ORDER BY Text Asc");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public IconographyEntry getIconographyEntry(int id) {
		IconographyEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE IconographyID=" + id + " ORDER BY Text Asc");
			if (rs.first()) {
				result = new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<IconographyEntry> getIconographyEntries(String sqlWhere) {
		ArrayList<IconographyEntry> result = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE " + sqlWhere + " ORDER BY Text Asc");
			while (rs.next()) {
				result.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<IconographyEntry> getIconography(int rootIndex) {
		ArrayList<IconographyEntry> root = getIconographyEntries(rootIndex);

		for (IconographyEntry item : root) {
			processIconographyTree(item);
		}
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
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String where = (parentID == 0) ? "IS NULL" : "= " + parentID;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE ParentID " + where + " ORDER BY Text Asc");
			while (rs.next()) {
				results.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	/**
	 * insert new IconogrpahyEntry in tree structure
	 * @param iconographyEntry
	 * @return
	 */
	public int insertIconographyEntry(IconographyEntry entry) {
		if (entry.getIconographyID() != 0 || entry.getParentID() == 0 || entry.getText().isEmpty()) { // otherwise this is not a new entry!
			return 0;
		}
		int newID = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("INSERT INTO Iconography (ParentID, Text) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, entry.getParentID());
			pstmt.setString(2, entry.getText());
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
		return newID;		
	}

	/**
	 * 
	 * @param iconographyEntryToEdit
	 * @return
	 */
	public boolean updateIconographyEntry(IconographyEntry iconographyEntryToEdit) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("UPDATE Iconography SET ParentID=?, Text=? WHERE IconographyID =?");
			pstmt.setInt(1, iconographyEntryToEdit.getParentID());
			pstmt.setString(2, iconographyEntryToEdit.getText());
			pstmt.setInt(3, iconographyEntryToEdit.getIconographyID());
			pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Deprecated
	public ArrayList<CurrentLocationEntry> getCurrentLocations() {
		ArrayList<CurrentLocationEntry> root = getCurrentLocationEntries(0);

		for (CurrentLocationEntry item : root) {
			processCurrentLocationTree(item);
		}
		return root;
	}

	@Deprecated
	protected void processCurrentLocationTree(CurrentLocationEntry parent) {
		ArrayList<CurrentLocationEntry> children = getCurrentLocationEntries(parent.getCurrentLocationID());
		if (children != null) {
			parent.setChildren(children);
			for (CurrentLocationEntry child : children) {
				processCurrentLocationTree(child);
			}
		}
	}

	@Deprecated
	protected ArrayList<CurrentLocationEntry> getCurrentLocationEntries(int parentID) {
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
			return null;
		}
		return results;
	}

	public ArrayList<VendorEntry> getVendors() {
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
			return null;
		}
		return results;
	}

	public VendorEntry getVendor(int id) {
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
			return null;
		}
		return result;
	}

	public ArrayList<StyleEntry> getStyles() {
		return getStyles(null);
	}

	public ArrayList<StyleEntry> getStyles(String sqlWhere) {
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
			return null;
		}
		return results;
	}

	public StyleEntry getStylebyID(int styleID) {
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
			return null;
		}
		return result;
	}

	public ArrayList<ExpeditionEntry> getExpeditions() {
		return getExpeditions(null);
	}

	public ArrayList<ExpeditionEntry> getExpeditions(String sqlWhere) {
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
			return null;
		}
		return results;
	}
	
	public ExpeditionEntry getExpedition(int id) {
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
			return null;
		}
		return result;
	}

	public ArrayList<PublicationEntry> getPublications() {
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
			return null;
		}
		return results;
	}

	public PublicationEntry getPublicationEntry(int id) {
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
			return null;
		}
		return result;
	}

	/**
	 * @param publicationTypeID
	 * @return
	 */
	public AuthorEntry getAuthorEntry(int id) {
		AuthorEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Authors WHERE AuthorID=" + id);
			if (rs.first()) {
				result = new AuthorEntry(rs.getInt("AuthorID"), rs.getString("Lastname"), rs.getString("Firstname"), rs.getString("Institution"),
						rs.getBoolean("KuchaVisitor"), rs.getString("Affiliation"), rs.getString("Email"), rs.getString("Homepage"), rs.getString("Alias"), 
						rs.getBoolean("InstitutionEnabled"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	@Deprecated
	public int getRelatedMasterImageID(int depictionID) {
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
			return 0;
		}
		return result;
	}

	/**
	 * Gets the images related to the depictionID
	 * 
	 * @param depictionID
	 * @return
	 */
	private ArrayList<ImageEntry> getRelatedImages(int depictionID, String sessionID) {
		String inStatement = Integer.toString(AbstractEntry.ACCESS_LEVEL_PUBLIC); // public is always permitted
		switch (getAccessLevelForSessionID(sessionID)) {
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
			pstmt = dbc.prepareStatement("SELECT * FROM Images WHERE ImageID IN (SELECT ImageID FROM DepictionImageRelation WHERE DepictionID=?) AND AccessLevel IN (" + inStatement + ")");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), 
						rs.getInt("ImageTypeID"), rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ArrayList<DepictionEntry> getAllDepictionsbyWall(int wallID) {
		ArrayList<DepictionEntry> depictions = new ArrayList<DepictionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT DepictionID, AbsoluteLeft, AbsoluteTop FROM Depictions WHERE WallID = " + wallID);
			while (rs.next()) {
				DepictionEntry depiction = new DepictionEntry();
				depiction.setDepictionID(rs.getInt("DepictionID"));
				depiction.setAbsoluteLeft(rs.getInt("AbsoluteLeft"));
				depiction.setAbsoluteTop(rs.getInt("AbsoluteTop"));

				depictions.add(depiction);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return depictions;
	}

	public String saveDepiction(int depictionID, int AbsoluteLeft, int AbsoluteTop) {
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
			return "failed to save depiction";
		}
		return "saved";
	}

	/**
	 * Updates the information of the entry given as parameter in the SQL database.
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized boolean updateImageEntry(ImageEntry entry) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"UPDATE Images SET Filename=?, Title=?, ShortName=?, Copyright=?, PhotographerID=?, Comment=?, Date=?, ImageTypeID=?, AccessLevel=? WHERE ImageID=?");
			pstmt.setString(1, entry.getFilename());
			pstmt.setString(2, entry.getTitle());
			pstmt.setString(3, entry.getShortName());
			pstmt.setString(4, entry.getCopyright());
			pstmt.setInt(5, entry.getImageAuthor() != null ? entry.getImageAuthor().getPhotographerID() : 0);
			pstmt.setString(6, entry.getComment());
			pstmt.setString(7, entry.getDate());
			pstmt.setInt(8, entry.getImageTypeID());
			pstmt.setInt(9, entry.getAccessLevel());
			pstmt.setInt(10, entry.getImageID());
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			return false;
		}
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
		ArrayList<RegionEntry> result = new ArrayList<RegionEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					sqlWhere != null ? "SELECT * FROM Regions WHERE " + sqlWhere + "ORDER BY EnglishName Asc, PhoneticName Asc, OriginalName Asc"
							: "SELECT * FROM Regions ORDER BY EnglishName Asc, PhoneticName Asc, OriginalName Asc");
			while (rs.next()) {
				result.add(new RegionEntry(rs.getInt("RegionID"), rs.getString("PhoneticName"), rs.getString("OriginalName"),
						rs.getString("EnglishName"), rs.getInt("SiteID")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		}
		return result;
	}

	/**
	 * @param publicationTypeID
	 * @return
	 */
	public SiteEntry getSite(int id) {
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
		}
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
		}
		return orientations;
	}

	/**
	 * @return
	 */
	public ArrayList<OrientationEntry> getOrientationInformation() {
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
		}
		return result;
	}

	public ArrayList<MainTypologicalClass> getMainTypologicalClass() {
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
		}
		return maintypologicalclasses;
	}

	public MainTypologicalClass getMainTypologicalClassbyID(int maintypoID) {
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
		}
		return result;
	}

	public ArrayList<WallEntry> getWalls() {
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
		}
		return walls;
	}

	public WallEntry getWall(int caveID, int wallLocationID) {
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
		return result;
	}

	public ArrayList<AnnotatedBibliographyEntry> searchAnnotatedBibliography(AnnotatedBibliographySearchEntry searchEntry) {
		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String where = "";
		
		if ((searchEntry.getAuthorSearch() != null) && !searchEntry.getAuthorSearch().isEmpty()) {
			String authorTerm = "";
			String editorTerm = "";
			for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
				authorTerm += authorTerm.isEmpty() 
						? "SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?)))"
						: " INTERSECT SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?)))";
				editorTerm += editorTerm.isEmpty() 
						? "SELECT BibID FROM EditorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?)))"
						: " INTERSECT SELECT BibID FROM EditorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE (FirstName LIKE ?) OR (LastName LIKE ?) OR (Institution LIKE ?) OR (Alias LIKE ?)))";
			}
			where = "BibID IN (" + authorTerm + ") OR BibID IN (" + editorTerm + ")";
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
		
		try {
			int i = 1;
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM AnnotatedBibliography" : "SELECT * FROM AnnotatedBibliography WHERE " + where);
			if ((searchEntry.getAuthorSearch() != null) && !searchEntry.getAuthorSearch().isEmpty()) {
				for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
				}
				for (String name : searchEntry.getAuthorSearch().split("\\s+")) {
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
					pstmt.setString(i++, "%" + name + "%");
				}
			}
			if (searchEntry.getPublisherSearch() != null && !searchEntry.getPublisherSearch().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getPublisherSearch() + "%");
			}
			if (searchEntry.getTitleSearch() != null && !searchEntry.getTitleSearch().isEmpty()) {
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
				pstmt.setString(i++, "%" + searchEntry.getTitleSearch() + "%");
			}
			if (searchEntry.getYearSearch() > 0) {
				pstmt.setString(i++, "%" + searchEntry.getYearSearch() + "%");
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
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				if (entry.getBibtexKey().isEmpty()) {
					if (!entry.getAuthorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getAuthorList().get(0), entry.getYearORG()));
					} else if (!entry.getEditorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getEditorList().get(0), entry.getYearORG()));
					}
					updateAnnotatedBiblographyEntry(entry);
				}
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		return result;
	}

	/**
	 * @param sqlWhere
	 * @return sorted list based on implementation of {@link #Comparable} in {@link #AnnotatedBibliographyEntry}
	 */
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliography(String sqlWhere) {

		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					(sqlWhere == null) ? "SELECT * FROM AnnotatedBibliography" : "SELECT * FROM AnnotatedBibliography WHERE " + sqlWhere);
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
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				if (entry.getBibtexKey().isEmpty()) {
					if (!entry.getAuthorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getAuthorList().get(0), entry.getYearORG()));
					} else if (!entry.getEditorList().isEmpty()) {
						entry.setBibtexKey(createBibtexKey(entry.getEditorList().get(0), entry.getYearORG()));
					}
					updateAnnotatedBiblographyEntry(entry);
				}
				result.add(entry);
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		return result;
	}

	/**
	 * @param authorList
	 * @return sorted list based on implementation of {@link #Comparable} in {@link #AnnotatedBibliographyEntry}
	 */
	public ArrayList<AnnotatedBibliographyEntry> getAnnotatedBibliographyFromAuthors(ArrayList<AuthorEntry> authorList) {
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
					"SELECT * FROM AnnotatedBibliography WHERE AnnotatedBibliography.BibID IN (SELECT DISTINCT BibID FROM AuthorBibliographyRelation WHERE AuthorBibliographyRelation.AuthorID IN (" + authorIDs + "))"
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
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				entry.setAuthorList(getAuthorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setEditorList(getEditorBibRelation(entry.getAnnotatedBibliographyID()));
				entry.setKeywordList(getRelatedBibKeywords(entry.getAnnotatedBibliographyID()));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		result.sort(null); // because AnnotatedBibliographyEntry implements Comparable
		return result;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	private ArrayList<AnnotatedBibliographyEntry> getDepictionBibRelation(int depictionID) {
		AnnotatedBibliographyEntry entry = null;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM DepictionBibliographyRelation WHERE DepictionID=?");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				entry = getAnnotatedBiblographybyID(rs.getInt("BibID"));
				result.add(entry);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		}
		return result;
	}

	/**
	 * @param annotatedBiblographyID
	 * @return
	 */
	private ArrayList<AuthorEntry> getAuthorBibRelation(int annotatedBiblographyID) {
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
		}
		return result;
	}
	
	public AnnotatedBibliographyEntry getAnnotatedBiblographybyID(int bibID) {
		AnnotatedBibliographyEntry result = null;
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM AnnotatedBibliography WHERE BibID=" + bibID);
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
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
				result.setAuthorList(getAuthorBibRelation(result.getAnnotatedBibliographyID()));
				result.setEditorList(getEditorBibRelation(result.getAnnotatedBibliographyID()));
				result.setKeywordList(getRelatedBibKeywords(result.getAnnotatedBibliographyID()));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public ArrayList<StructureOrganization> getStructureOrganizations() {
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
		}
		return structureOrganizations;
	}

	public ArrayList<OrnamentPositionEntry> getOrnamentPosition() {
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
		}
		return positions;
	}

	public ArrayList<OrnamentFunctionEntry> getOrnamentFunction() {
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
		}
		return functions;
	}

	public ArrayList<OrnamentCaveType> getOrnamentCaveTypes() {
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
		}
		return cavetypes;
	}

	/**
	 * This method unfolds the searchEntry and creates the query incl. sophisticated WHERE clause
	 * @param searchEntry
	 * @return
	 */
	public ArrayList<OrnamentEntry> searchOrnaments(OrnamenticSearchEntry search) {
		// Suche ueber einzelne SQL Querys, speichern in ArrayList "listen", anschliessend Schnittmenge bilden
		Connection dbc = getConnection();
		Statement stmt;
		ArrayList<OrnamentEntry> resultList = new ArrayList<OrnamentEntry>();
//		ArrayList<ArrayList<OrnamentEntry>> listen = new ArrayList<ArrayList<OrnamentEntry>>();

		// if no filter has been set, we just select all
		if (search != null) {
			String mysqlquerry = "SELECT * FROM Ornaments";
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {
					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					resultList.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("general search no. of elements: " + resultList.size());
//			return result;
//			listen.add(result);
		}
		
		// Ab hier suche nach den einzelnen Kriterien, bauen von einzelnen Listen und später eine Teilmengenbildung der Listen.
		if (!search.getCaves().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
//			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) WHERE CaveID IN (";
//			for (int i = 0; search.getCaves().size() > i; i++) {
//				mysqlquerry = mysqlquerry + Integer.toString(search.getCaves().get(i).getCaveID());
//				if (search.getCaves().size() > i + 1) {
//					mysqlquerry = mysqlquerry + " , ";
//				}
//			}
//			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) = " + search.getCaves().size();
			String inCaveStatement = "";
			for (CaveEntry ce : search.getCaves()) {
				inCaveStatement += inCaveStatement.isEmpty() ? Integer.toString(ce.getCaveID()) : "," + ce.getCaveID();
			}
			String query = "SELECT * FROM Ornaments WHERE OrnamentID IN (SELECT OrnamentID FROM CaveOrnamentRelation WHERE CaveID IN (" + inCaveStatement + "))";
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(query);
				while (rs.next()) {
					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("cave search no. of elements: " + result.size());
			resultList.retainAll(result); // we only keep the results 
		}
		
		if (search.getGroup() != null && !search.getGroup().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) WHERE GroupofOrnaments LIKE "
					+ search.getGroup();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("group search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getCode() != null) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments LIKE OrnamentCode = " + search.getCode();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("code search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getComponents().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments JOIN OrnamentComponentRelation ON Ornaments.OrnamentID = OrnamentComponentRelation.OrnamentID WHERE OrnamentComponentID IN (";
			for (int i = 0; search.getComponents().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getComponents().get(i).getOrnamentComponentsID());
				if (search.getComponents().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getComponents().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("component search no. of elements: " + result.size());
			resultList.retainAll(result);
		}
		
		if (search.getDescription() != null && !search.getDescription().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments LIKE OrnamentCode = " + search.getCode();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("decription search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getDistricts().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) JOIN Caves ON Caves.CaveID = CaveOrnamentRelation.CaveID WHERE Caves.DistrictID IN (";
			for (int i = 0; search.getDistricts().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getDistricts().get(i).getDistrictID());
				if (search.getDistricts().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getDistricts().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {
					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("district search no. of elements: " + result.size());
			resultList.retainAll(result);
		}
		
		if (search.getFunction().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) JOIN OrnamentCaveWallRelation ON OrnamentCaveWallRelation.OrnamentCaveRelationID = CaveOrnamentRelation.CaveOrnamentRelationID WHERE FunctionID IN (";
			for (int i = 0; search.getFunction().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getFunction().get(i).getOrnamentFunctionID());
				if (search.getFunction().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getFunction().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("function search no. of elements: " + result.size());
			resultList.retainAll(result);
		}
		
		if (search.getIconographys().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) JOIN OrnamentCaveIconographyRelation ON OrnamentCaveIconographyRelation.OrnamentCaveRelationID = CaveOrnamentRelation.CaveOrnamentRelationID WHERE IconographyID IN (";
			for (int i = 0; search.getIconographys().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getIconographys().get(i).getIconographyID());
				if (search.getIconographys().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getIconographys().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("iconography search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getInterpretation() != null && !search.getInterpretation().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments LIKE Interpretation = " + search.getInterpretation();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("interpretation search no. of elements: " + result.size());
			resultList.retainAll(result);
		}
		
		if (search.getPosition().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation ON Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) JOIN OrnamentCaveWallRelation ON OrnamentCaveWallRelation.OrnamentCaveRelationID = CaveOrnamentRelation.CaveOrnamentRelationID WHERE PositionID IN (";
			for (int i = 0; search.getPosition().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getPosition().get(i).getOrnamentPositionID());
				if (search.getPosition().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getPosition().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {
					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("position search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getOrnamentClass() != null) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments WHERE OrnamentClassID = " + search.getOrnamentClass().getOrnamentClassID();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("ornament class search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getReferences() != null && !search.getReferences().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments WHERE References LIKE " + search.getReferences();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("references search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getRelatedOrnaments().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM (Ornaments JOIN CaveOrnamentRelation on Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID) JOIN RelatedOrnamentsRelation "
					+ "ON CaveOrnamentRelation.CaveOrnamentRelationID = RelatedOrnamentsRelation.CaveOrnamentRelationID WHERE RelatedOrnamentsRelation.OrnamentID IN (";
			for (int i = 0; search.getRelatedOrnaments().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getRelatedOrnaments().get(i).getOrnamentID());
				if (search.getCaves().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getRelatedOrnaments().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {
					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("related ornaments search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getRemarks() != null && !search.getRemarks().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments WHERE Remarks LIKE " + search.getRemarks();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("remarks search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getSecondarypatterns().size() > 0) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments JOIN InnerSecondaryPatternRelation ON Ornaments.OrnamentID = InnerSecondaryPatternRelation.OrnamentID WHERE InnerSecID IN (";
			for (int i = 0; search.getSecondarypatterns().size() > i; i++) {
				mysqlquerry = mysqlquerry + Integer.toString(search.getSecondarypatterns().get(i).getInnerSecondaryPatternsID());
				if (search.getSecondarypatterns().size() > i + 1) {
					mysqlquerry = mysqlquerry + " , ";
				}
			}
			mysqlquerry = mysqlquerry + ") GROUP BY Ornaments.OrnamentID HAVING COUNT(*) =" + search.getSecondarypatterns().size();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("secondary patterns search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getSimilaritys() != null && !search.getSimilaritys().isEmpty()) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments JOIN CaveOrnamentRelation on Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID WHERE SimilarElementsOfOtherCultures LIKE "
					+ search.getSimilaritys();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("similarities search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

		if (search.getStyle() != null) {
			ArrayList<OrnamentEntry> result = new ArrayList<OrnamentEntry>();
			String mysqlquerry = "SELECT * FROM Ornaments JOIN CaveOrnamentsRelation on Ornaments.OrnamentID = CaveOrnamentRelation.OrnamentID WHERE StyleID ="
					+ search.getStyle().getStyleID();
			try {
				stmt = dbc.createStatement();
				ResultSet rs = stmt.executeQuery(mysqlquerry);
				while (rs.next()) {

					OrnamentEntry entry = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"),
							rs.getString("Remarks"),
							// rs.getString("Annotation"),
							rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
							getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
							getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
							getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
							new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
					result.add(entry);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.err.println("styles search no. of elements: " + result.size());
			resultList.retainAll(result);
		}

//		ArrayList<OrnamentEntry> intersection = listen.get(0);
//		for (ArrayList<OrnamentEntry> col : listen.subList(1, listen.size()-1)) {
//			intersection.retainAll(col);
//			// setzen der ersten Liste auf die Teilmenge aller Listen
//		}
//
//		if (listen.size() == 0) {
//			// sollte nicht auftreten, falls Listen leer
//			ArrayList<OrnamentEntry> emptyList = new ArrayList<OrnamentEntry>();
//			return emptyList;
//		} else {
//			// rueckgabe der Teilmengenliste
//			return (ArrayList<OrnamentEntry>) intersection;
//		}
		return resultList;
	}

	public ArrayList<OrnamentEntry> getOrnamentsWhere(String sqlWhere) {
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery((sqlWhere == null) ? "SELECT * FROM Ornaments" : "SELECT * FROM Ornaments WHERE " + sqlWhere);
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						//rs.getString("Annotation"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public OrnamentEntry getOrnamentEntry(int id) {
		OrnamentEntry result = null;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		if (id == 0) {
			return result;
		}
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Ornaments WHERE OrnamentID=?");
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				result = new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code"), rs.getString("Description"), rs.getString("Remarks"),
						rs.getString("Interpretation"), rs.getString("OrnamentReferences"), rs.getInt("OrnamentClassID"),
						getImagesbyOrnamentID(rs.getInt("OrnamentID")), getCaveRelationbyOrnamentID(rs.getInt("OrnamentID")),
						getOrnamentComponentsbyOrnamentID(rs.getInt("OrnamentID")), getInnerSecPatternsbyOrnamentID(rs.getInt("OrnamentID")),
						getRelatedBibliographyFromOrnamen(rs.getInt("OrnamentID")),
						new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<CeilingTypeEntry> getCeilingTypes() {
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
			return null;
		}
		return result;
	}

	/**
	 * @return
	 */
	public CeilingTypeEntry getCeilingType(int id) {
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
			return null;
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<PreservationClassificationEntry> getPreservationClassifications() {
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
			return null;
		}
		return result;
	}

	/**
	 * @return
	 */
	private PreservationClassificationEntry getPreservationClassification(int id) {
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
			return null;
		}
		return result;
	}

	/**
	 * TODO: only save existing areas depending on cave type!
	 * 
	 * @param caveEntry
	 * @return
	 */
	public synchronized boolean updateCaveEntry(CaveEntry caveEntry) {
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
			for (CaveAreaEntry caEntry : caveEntry.getCaveAreaList()) {
				writeCaveArea(caEntry);
			}
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
		return true;
	}

	/**
	 * @param caveEntry
	 * @return
	 */
	public synchronized int insertCaveEntry(CaveEntry caveEntry) {
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
		return newCaveID;
	}

	/**
	 * 
	 * @param entry
	 * @return
	 */
	public synchronized int insertCaveSketchEntry(CaveSketchEntry entry) {
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
		return newCaveSketchID;
	}
	
	private ArrayList<CaveSketchEntry> getCaveSketchEntriesFromCave(int caveID) {
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
			return null;
		}
		return results;
	}

	/**
	 * @return
	 */
	public ArrayList<CaveGroupEntry> getCaveGroups() {
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
			return null;
		}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public synchronized UserEntry userLogin(String username, String password) {
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
			return null;
		}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	public UserEntry getUser(String username) {
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
			return null;
		}
		return result;
	}

	/**
	 * @param username
	 * @param password
	 * @return
	 */
	private UserEntry getUser(int userID) {
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
			return null;
		}
		return result;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public String getSessionIDfromUser(String username) {
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
			return null;
		}
		return sessionID;
	}

	/**
	 * 
	 * @param username
	 * @return
	 */
	public int getAccessLevelForSessionID(String sessionID) {
		int accessRights = 0;
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		System.err.println("getAccessLevelForSessionID(" + sessionID + ")");
		try {
			pstmt = dbc.prepareStatement("SELECT AccessLevel FROM Users WHERE SessionID=?");
			pstmt.setString(1, sessionID);
			ResultSet rs = pstmt.executeQuery();
			if (rs.first()) {
				accessRights = rs.getInt("AccessLevel");
				System.err.println("accessLevel=" + accessRights);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace(System.err);
			return 0;
		}

		return accessRights;
	}

	/**
	 * 
	 * @param username
	 * @param sessionID
	 */
	public void updateSessionIDforUser(String username, String sessionID) {
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
	}
	
	public UserEntry checkSessionID(String sessionID) {
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
		}
		return result;
	}
 
	/**
	 * @param sessionID
	 * @param username
	 * @return UserEntry if user & sessionID combination exists, null else
	 */
	public UserEntry checkSessionID(String sessionID, String username) {
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
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<ImageTypeEntry> getImageTypes() {
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
			return null;
		}
		return result;
	}

	/**
	 * @param iconographyLists
	 * @param list
	 * @param depictionEntry
	 * @return
	 */
	public synchronized int insertDepictionEntry(DepictionEntry de, ArrayList<IconographyEntry> iconographyLists) {
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
		} catch (SQLException ex) {
			ex.printStackTrace();
			return 0;
		}
		if (newDepictionID > 0) {
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
		}
		return newDepictionID;
	}

	/**
	 * @param correspondingDepictionEntry
	 * @param imgEntryList
	 * @param iconographyList
	 * @return <code>true</code> when operation is successful
	 */
	public synchronized boolean updateDepictionEntry(DepictionEntry de, ArrayList<IconographyEntry> iconographyList) {
		System.err.println("==> updateDepictionEntry called");
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		Date date = new Date(System.currentTimeMillis());
		DateFormat df = DateFormat.getDateTimeInstance();
		de.setModifiedOn(df.format(date));
		try {
			System.err.println("===> updateDepictionEntry ID = " + de.getDepictionID());
			pstmt = dbc.prepareStatement(
					"UPDATE Depictions SET StyleID=?, Inscriptions=?, SeparateAksaras=?, Dating=?, Description=?, BackgroundColour=?, GeneralRemarks=?, "
							+ "OtherSuggestedIdentifications=?, Width=?, Height=?, ExpeditionID=?, PurchaseDate=?, CurrentLocationID=?, InventoryNumber=?, VendorID=?, "
							+ "StoryID=?, CaveID=?, WallID=?, AbsoluteLeft=?, AbsoluteTop=?, ModeOfRepresentationID=?, ShortName=?, PositionNotes=?, MasterImageID=?, AccessLevel=?, LastChangedByUser=?, "
							+ "LastChangedOnDate=? WHERE DepictionID=?");
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
			pstmt.setInt(28, de.getDepictionID());
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
//		System.err.println("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM DepictionIconographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (iconographyList.size() > 0) {
			insertDepictionIconographyRelation(de.getDepictionID(), iconographyList);
		}
//		System.err.println("DELETE FROM DepictionBibliographyRelation WHERE DepictionID=" + de.getDepictionID());
		deleteEntry("DELETE FROM DepictionBibliographyRelation WHERE DepictionID=" + de.getDepictionID());
		if (de.getRelatedBibliographyList().size() > 0) {
			insertDepictionBibliographyRelation(de.getDepictionID(), de.getRelatedBibliographyList());
		}
		System.err.println("==> updateDepictionEntry finished");
		return true;
	}

	private synchronized void insertDepictionImageRelation(int depictionID, ArrayList<ImageEntry> imgEntryList) {
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
	}

	private synchronized void insertDepictionIconographyRelation(int depictionID, ArrayList<IconographyEntry> iconographyList) {
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
		}
	}

	/**
	 * @param depictionID
	 * @param relatedBibliographyList
	 */
	private synchronized void insertDepictionBibliographyRelation(int depictionID,
			ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		try {
			relationStatement = dbc.prepareStatement("INSERT INTO DepictionBibliographyRelation VALUES (?, ?)");
			for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
				relationStatement.setInt(1, depictionID);
				relationStatement.setInt(2, entry.getAnnotatedBibliographyID());
				relationStatement.executeUpdate();
			}
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromDepiction(int depictionID) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM DepictionBibliographyRelation WHERE DepictionID=?");
			relationStatement.setInt(1, depictionID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param depictionID
	 * @param relatedBibliographyList
	 */
	private synchronized void writeCaveBibliographyRelation(int caveID, ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		if (caveID > 0 && relatedBibliographyList.size() > 0) {
			deleteEntry("DELETE FROM CaveBibliographyRelation WHERE CaveID=" + caveID);
			try {
				relationStatement = dbc.prepareStatement("INSERT INTO CaveBibliographyRelation VALUES (?, ?)");
				relationStatement.setInt(1, caveID);
				for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
					relationStatement.setInt(2, entry.getAnnotatedBibliographyID());
					relationStatement.executeUpdate();
				}
				relationStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private synchronized void writeOrnamenticBibliographyRelation(int OrnamentID, ArrayList<AnnotatedBibliographyEntry> relatedBibliographyList) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;

		if (OrnamentID > 0 && relatedBibliographyList.size() > 0) {
			deleteEntry("DELETE FROM OrnamentBibliographyRelation WHERE OrnamentID=" + OrnamentID);
			try {
				relationStatement = dbc.prepareStatement("INSERT INTO OrnamentBibliographyRelation VALUES (?, ?)");
				relationStatement.setInt(1, OrnamentID);
				for (AnnotatedBibliographyEntry entry : relatedBibliographyList) {
					relationStatement.setInt(2, entry.getAnnotatedBibliographyID());
					relationStatement.executeUpdate();
				}
				relationStatement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param caveID
	 * @return
	 */
	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromCave(int caveID) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM CaveBibliographyRelation WHERE CaveID=?");
			relationStatement.setInt(1, caveID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	private ArrayList<AnnotatedBibliographyEntry> getRelatedBibliographyFromOrnamen(int ornamentID) {
		Connection dbc = getConnection();
		PreparedStatement relationStatement;
		ArrayList<AnnotatedBibliographyEntry> result = new ArrayList<AnnotatedBibliographyEntry>();

		try {
			relationStatement = dbc.prepareStatement("SELECT * FROM OrnamentBibliographyRelation WHERE OrnamentID=?");
			relationStatement.setInt(1, ornamentID);
			ResultSet rs = relationStatement.executeQuery();
			while (rs.next()) {
				result.add(getAnnotatedBiblographybyID(rs.getInt("BibID")));
			}
			rs.close();
			relationStatement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<ModeOfRepresentationEntry> getModesOfRepresentations() {
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
		}
		return result;
	}

	public ArrayList<CaveAreaEntry> getCaveAreas(int caveID) {
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
		return result;
	}

	protected synchronized boolean writeCaveArea(CaveAreaEntry entry) {
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
		return (rowCount > 0);
	}

	protected synchronized boolean writeWall(WallEntry entry) {
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
		return (rowCount > 0);
	}

	private synchronized boolean writeC14AnalysisUrlEntry(int caveID, ArrayList<C14AnalysisUrlEntry> entryList) {
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
		return true;
	}

	/**
	 * @param caveID
	 * @return
	 */
	public ArrayList<WallEntry> getWalls(int caveID) {
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
		return result;
	}

	/**
	 * @param caveID
	 * @return
	 */
	private ArrayList<C14AnalysisUrlEntry> getC14AnalysisEntries(int caveID) {
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
				entry = new C14AnalysisUrlEntry(c14RS.getInt("C14AnalysisUrlID"), c14RS.getString("C14Url"), c14RS.getString("C14ShortName"));
				result.add(entry);
			}
			c14AnalysisStatement.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<WallLocationEntry> getWallLocations() {
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
		return result;
	}

	/**
	 * @param currentAuthorEntry
	 * @return
	 */
	public boolean updateAuthorEntry(AuthorEntry authorEntry) {
		Connection dbc = getConnection();
		PreparedStatement authorStatement;
		int rowCount = 0;
		try {
			authorStatement = dbc.prepareStatement(
					"UPDATE Authors SET LastName=?, FirstName=?, Institution=?, KuchaVisitor=?, Affiliation=?, Email=?, Homepage=?, Alias=?, InstitutionEnabled=? WHERE AuthorID=?");
			authorStatement.setString(1, authorEntry.getLastname());
			authorStatement.setString(2, authorEntry.getFirstname());
			authorStatement.setString(3, authorEntry.getInstitution());
			authorStatement.setBoolean(4, authorEntry.isKuchaVisitor());
			authorStatement.setString(5, authorEntry.getAffiliation());
			authorStatement.setString(6, authorEntry.getEmail());
			authorStatement.setString(7, authorEntry.getHomepage());
			authorStatement.setString(8, authorEntry.getAlias());
			authorStatement.setBoolean(9, authorEntry.isInstitutionEnabled());
			authorStatement.setInt(10, authorEntry.getAuthorID());
			rowCount = authorStatement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rowCount > 0;
	}

	/**
	 * @param currentAuthorEntry
	 * @return
	 */
	public int insertAuthorEntry(AuthorEntry authorEntry) {
		Connection dbc = getConnection();
		PreparedStatement authorStatement;
		int authorID = 0;
		try {
			authorStatement = dbc.prepareStatement(
					"INSERT INTO Authors (LastName, FirstName, Institution, KuchaVisitor, Affiliation, Email, Homepage, Alias, InstitutionEnabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
			authorStatement.executeUpdate();
			ResultSet keys = authorStatement.getGeneratedKeys();
			if (keys.next()) {
				authorID = keys.getInt(1);
			}
			keys.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return authorID;
	}

	/**
	 * @return
	 */
	private String createBibtexKey(AuthorEntry entry, String year) {
		String result = (entry.isInstitutionEnabled() ? entry.getInstitution().replace(" ", "") : entry.getLastname()) + year;
		List<String> appendix = Arrays.asList("","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z");
		int count = 0;
		while (!getAnnotatedBibliography("BibTexKey='"+result+appendix.get(count)+"'").isEmpty()) {
			++count;
		}
		return result+appendix.get(count);
	}

	/**
	 * @param bibEntry
	 * @return
	 */
	public AnnotatedBibliographyEntry insertAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) {
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
					+ "Unpublished, AccessLevel, AbstractText, ThesisType, EditorType, OfficialTitleTranslation, BibTexKey) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
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
			pstmt.executeUpdate();

			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.next()) {
				newBibID = keys.getInt(1);
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
		return bibEntry;
	}

	private void updateAuthorBibRelation(int bibID, ArrayList<AuthorEntry> authorList) {
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
		}
	}

	private void updateEditorBibRelation(int bibID, ArrayList<AuthorEntry> editorList) {
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
		}
	}

	/**
	 * @return
	 */
	public ArrayList<PublisherEntry> getPublishers() {
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
		}
		return result;
	}

	/**
	 * @return
	 */
	public PublisherEntry getPublisher(int id) {
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
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<AuthorEntry> getAuthors() {
		ArrayList<AuthorEntry> result = new ArrayList<AuthorEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Authors");
			while (rs.next()) {
				result.add(new AuthorEntry(rs.getInt("AuthorID"), rs.getString("Lastname"), rs.getString("Firstname"), rs.getString("Institution"),
						rs.getBoolean("KuchaVisitor"), rs.getString("Affiliation"), rs.getString("Email"), rs.getString("Homepage"), rs.getString("Alias"), 
						rs.getBoolean("InstitutionEnabled"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param photographerEntry
	 * @return
	 */
	public int insertPhotographerEntry(PhotographerEntry photographerEntry) {
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
		}
		return photographerID;
	}

	/**
	 * @param cgEntry
	 * @return
	 */
	public int insertCaveGroupEntry(CaveGroupEntry cgEntry) {
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
		}
		return caveGroupID;
	}

	/**
	 * @param de
	 * @return
	 */
	public int insertDistrictEntry(DistrictEntry de) {
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
		}
		return districtID;
	}

	/**
	 * @param re
	 * @return
	 */
	public int insertRegionEntry(RegionEntry re) {
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
		}
		return regionID;
	}

	/**
	 * @param ctEntry
	 * @return
	 */
	public int insertCeilingTypeEntry(CeilingTypeEntry ctEntry) {
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
		}
		return ceilingTypeID;
	}

	/**
	 * @param depictionID
	 * @return
	 */
	public ArrayList<IconographyEntry> getRelatedIconography(int depictionID) {
		ArrayList<IconographyEntry> result = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM DepictionIconographyRelation WHERE DepictionID=?)");
			pstmt.setInt(1, depictionID);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				result.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @return
	 */
	public ArrayList<LocationEntry> getLocations() {
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
			return null;
		}
		return results;
	}

	/**
	 * @return
	 */
	public LocationEntry getLocation(int id) {
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
			return null;
		}
		return result;
	}

	/**
	 * @param vEntry
	 * @return
	 */
	public int inserVendorEntry(VendorEntry vEntry) {
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
		}
		return vendorID;
	}

	/**
	 * @param lEntry
	 * @return
	 */
	public int insertLocationEntry(LocationEntry lEntry) {
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
		}
		return locationID;
	}

	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecondaryPatterns() {
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
		}
		return result;
	}

	public ArrayList<OrnamentClassEntry> getOrnamentClass() {
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
		}
		return result;
	}

	public ArrayList<OrnamentComponentsEntry> getOrnamentComponents() {
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
		}
		return result;
	}

	public OrnamentComponentsEntry addOrnamentComponents(OrnamentComponentsEntry ornamentComponent) {
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
			return entry;
		}
		return entry;
	}

	public OrnamentClassEntry addOrnamentClass(OrnamentClassEntry ornamentClass) {
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
			return entry;
		}
		return entry;
	}

	public OrnamentClassEntry renameOrnamentClass(OrnamentClassEntry ornamentClass) {
		Connection dbc = getConnection();
		OrnamentClassEntry entry = null;

		PreparedStatement stmt;
		try {
			stmt = dbc.prepareStatement("UPDATE OrnamentClass SET Name=? WHERE VALUES OrnamentClassID=?");
			stmt.setString(1, ornamentClass.getName());
			stmt.setInt(2, ornamentClass.getOrnamentClassID());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return entry;
		}
		return entry;
	}
	
	/**
	 * 
	 * @param ornamentComponents
	 * @return
	 */
	public OrnamentComponentsEntry renameOrnamentComponents(OrnamentComponentsEntry ornamentComponents) {
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
			return entry;
		}
		return entry;
	}

	/**
	 * 
	 * @param innerSecPattern
	 * @return
	 */
	public InnerSecondaryPatternsEntry addInnerSecondaryPatterns(InnerSecondaryPatternsEntry innerSecPattern) {
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
			return null;
		}
		return entry;
	}

	/**
	 * @return
	 */
	public ArrayList<PreservationAttributeEntry> getPreservationAttributes() {
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
		}
		return result;
	}

	/**
	 * @param paEntry
	 * @return
	 */
	public int insertPreservationAttributeEntry(PreservationAttributeEntry paEntry) {
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
		}
		return preservationAttributeID;
	}

	/**
	 * @param publisherEntry
	 * @return
	 */
	public int insertPublisherEntry(PublisherEntry publisherEntry) {
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
		}
		return newPublisherID;
	}

	public ArrayList<OrnamentPositionEntry> getPositionbyWall(WallEntry wall) {
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
		}
		return result;
	}
	
	public ArrayList<OrnamentPositionEntry> getPositionbyReveal(WallEntry wall) {
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
		}
		return result;
	}

	public ArrayList<OrnamentPositionEntry> getPositionbyCeilingTypes(int ceilingID1, int ceilingID2) {

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
		}
		return result;
	}

	public ArrayList<OrnamentFunctionEntry> getFunctionbyPosition(OrnamentPositionEntry position) {
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
		}
		return result;
	}

	/**
	 * @param bibEntry
	 * @return
	 */
	public AnnotatedBibliographyEntry updateAnnotatedBiblographyEntry(AnnotatedBibliographyEntry bibEntry) {
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
					+ "Unpublished=?, AccessLevel=?, AbstractText=?, ThesisType=?, EditorType=?, OfficialTitleTranslation=?, BibTexKey=? WHERE BibID=?");
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
			pstmt.setInt(57, bibEntry.getAnnotatedBibliographyID());
			pstmt.executeUpdate();

			updateAuthorBibRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getAuthorList());
			updateEditorBibRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getEditorList());
			updateBibKeywordRelation(bibEntry.getAnnotatedBibliographyID(), bibEntry.getKeywordList());
		} catch (SQLException ex) {
			ex.printStackTrace();
			return null;
		}
		return bibEntry;
	}

	/**
	 * @param sqlWhere
	 * @return
	 */
	public ArrayList<Integer> getDepictionFromIconography(String sqlWhere) {
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
		}
		return result;
	}

	public ArrayList<ImageEntry> getImagesbyOrnamentID(int ornamentID) {
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = dbc.prepareStatement(
					"SELECT * FROM Images JOIN OrnamentImageRelation ON OrnamentImageRelation.ImageID = Images.ImageID WHERE OrnamentID ="
							+ ornamentID + " ORDER BY Title Asc");
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt("ImageID"), rs.getString("Filename"), rs.getString("Title"), rs.getString("ShortName"),
						rs.getString("Copyright"), getPhotographerEntry(rs.getInt("PhotographerID")), rs.getString("Comment"), rs.getString("Date"), 
						rs.getInt("ImageTypeID"), rs.getInt("AccessLevel"), new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(rs.getTimestamp("ModifiedOn"))));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ArrayList<InnerSecondaryPatternsEntry> getInnerSecPatternsbyOrnamentID(int ornamentID) {
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
		}
		return result;
	}

	private ArrayList<OrnamentCaveRelation> getCaveRelationbyOrnamentID(int ornamentID) {
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
		}

		return result;
	}

	public ArrayList<OrnamentComponentsEntry> getOrnamentComponentsbyOrnamentID(int ornamentID) {

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
		}
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
		}
		return orientations;
	}
*/
	
	private ArrayList<IconographyEntry> getIconographyElementsbyOrnamentCaveID(int ornamentCaveID) {
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM Iconography WHERE IconographyID IN (SELECT IconographyID FROM OrnamentCaveIconographyRelation WHERE OrnamentCaveRelationID="
							+ ornamentCaveID + ")");
			while (rs.next()) {
				results.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
				System.err.println("getIconographyElementsbyOrnamentCaveID: IconographyID=" + rs.getInt("IconographyID"));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ArrayList<OrnamentEntry> getRelatedOrnamentsbyOrnamentCaveID(int ornamentCaveID) {
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM Ornaments JOIN RelatedOrnamentsRelation ON Ornaments.OrnamentID = RelatedOrnamentsRelation.OrnamentID WHERE OrnamentCaveRelationID = "
							+ ornamentCaveID);
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code")));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;

	}

	public ArrayList<WallOrnamentCaveRelation> getWallsbyOrnamentCaveID(int ornamentCaveID) {
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
			return null;
		}
		return results;

	}

	/**
	 * @deprecated please use {@link #getWall(int, int)} instead!
	 * @param wallLocationID
	 * @param caveID
	 * @return
	 */
	private WallEntry getWallbyWallLocationANDCaveID(int wallLocationID, int caveID) {
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
		}
		return result;
	}

	/**
	 * @param clientName
	 * @param message
	 */
	public void doLogging(String clientName, String message) {
		System.err.println(">>> " + clientName + ": " + message);
	}

	/**
	 * @return
	 */
	public ArrayList<BibKeywordEntry> getBibKeywords() {
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
		}
		return result;
	}
	
	/**
	 * loads bibKeywords related to the annotated bibliography ID
	 * @param bibID
	 * @return
	 */
	private ArrayList<BibKeywordEntry> getRelatedBibKeywords(int bibID) {
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
		}
		return result;
	}
	
	private void updateBibKeywordRelation(int bibID, ArrayList<BibKeywordEntry> keywordList) {
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
		}
	}

	/**
	 * @param bkEntry
	 * @return
	 */
	public int insertBibKeyword(BibKeywordEntry bkEntry) {
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
		}
		return bibKeywordID;
	}

	/**
	 * @param selectedEntry
	 * @return
	 */
	public boolean deleteAuthorEntry(AuthorEntry selectedEntry) {
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
		}
		return false;
	}

	/**
	 * @param currentUser
	 * @param newPasswordHash 
	 * @return
	 */
	public boolean updateUserEntry(UserEntry currentUser, String passwordHash, String newPasswordHash) {
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
			return false;
		}
		return rowsChangedCount > 0;
	}


	public int insertUserEntry(UserEntry userEntry) {
		if (userEntry == null || userEntry.getUserID() > 0) {
			return 0;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int newUserID = 0;
		
		try {
			pstmt = dbc.prepareStatement("INSERT INTO Users (Username, Password, Firstname, Lastname, Email, Affiliation, AccessLevel) VALUES (?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userEntry.getUsername());
			pstmt.setString(2, "ff4ea6b28f247ccdd4a03321dc2bd1a");
			pstmt.setString(3, userEntry.getFirstname());
			pstmt.setString(4, userEntry.getLastname());
			pstmt.setString(5, userEntry.getEmail());
			pstmt.setString(6, userEntry.getAffiliation());
			pstmt.setInt(7, userEntry.getAccessLevel());
			pstmt.executeUpdate();
			ResultSet keys = pstmt.getGeneratedKeys();
			if (keys.first()) {
				newUserID = keys.getInt(1);
			}
			keys.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return newUserID;
	}

	/**
	 * 
	 * @param userEntry
	 * @return
	 */
	public boolean updateUserEntry(UserEntry userEntry) {
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
			return false;
		}
		return rowsChangedCount > 0;
	}

	/**
	 * 
	 * @param searchEntry
	 * @return
	 */
	public ArrayList<DepictionEntry> searchDepictions(DepictionSearchEntry searchEntry) {
		ArrayList<DepictionEntry> results = new ArrayList<DepictionEntry>();
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		String where = "";
		
		if (searchEntry.getShortName() != null && !searchEntry.getShortName().isEmpty()) {
			where = "ShortName LIKE ?";
		}

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
						+ searchEntry.getCorrelationFactor() + "))"
					: " AND DepictionID IN (SELECT DepictionID FROM DepictionIconographyRelation WHERE IconographyID IN (" + iconographyIDs + ") GROUP BY DepictionID HAVING (COUNT(DepictionID) >= " 
						+ searchEntry.getCorrelationFactor() + "))";
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
				
		System.err.println(where.isEmpty() ? "SELECT * FROM Depictions" : "hier?SELECT * FROM Depictions WHERE " + where);

		try {
			pstmt = dbc.prepareStatement(where.isEmpty() ? "SELECT * FROM Depictions" : "SELECT * FROM Depictions WHERE " + where);
			if (!searchEntry.getShortName().isEmpty()) {
				pstmt.setString(1, "%" + searchEntry.getShortName() + "%");
			}
			

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				DepictionEntry de = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("SeparateAksaras"), rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getDouble("Width"), rs.getDouble("Height"),
						getExpedition(rs.getInt("ExpeditionID")), rs.getDate("PurchaseDate"), getLocation(rs.getInt("CurrentLocationID")), rs.getString("InventoryNumber"),
						getVendor(rs.getInt("VendorID")), rs.getInt("StoryID"), getCave(rs.getInt("CaveID")), rs.getInt("WallID"), rs.getInt("AbsoluteLeft"),
						rs.getInt("AbsoluteTop"), rs.getInt("ModeOfRepresentationID"), rs.getString("ShortName"), rs.getString("PositionNotes"),
						rs.getInt("MasterImageID"), rs.getInt("AccessLevel"), rs.getString("LastChangedByUser"), rs.getString("LastChangedOnDate"));
				de.setRelatedImages(getRelatedImages(de.getDepictionID(), searchEntry.getSessionID()));
				de.setRelatedBibliographyList(getRelatedBibliographyFromDepiction(de.getDepictionID()));
				de.setRelatedIconographyList(getRelatedIconography(de.getDepictionID()));
				results.add(de);
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public ArrayList<UserEntry> getUsers() {
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
		}
		return result;
	}

	public boolean saveCollectedEntries(String sessionID, String collectionLabel, boolean isGroupCollection, ArrayList<AbstractEntry> entryList) {
		UserEntry ue = this.checkSessionID(sessionID);
		if (ue == null) {
			return false;
		}
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		int collectionID=0;

		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Collections WHERE UserID=? AND CollectionLabel=?");
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
					}
					insertStatement.executeQuery();
				}
				insertStatement.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public ArrayList<CollectionEntry> getRelatedCollectionNames(String sessionID) {
		Connection dbc = getConnection();
		PreparedStatement pstmt;
		ArrayList<CollectionEntry> resultList = new ArrayList<CollectionEntry>();
		UserEntry ue = checkSessionID(sessionID);
		if (ue == null) {
			return resultList;
		}
		try {
			pstmt = dbc.prepareStatement("SELECT * FROM Collections WHERE UserID=? OR GroupCollection=TRUE");
			pstmt.setInt(1, ue.getUserID());
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				resultList.add(new CollectionEntry(rs.getInt("CollectionID"), getUser(rs.getInt("UserID")), rs.getString("CollectionLabel"), rs.getBoolean("GroupCollection")));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	public ArrayList<AbstractEntry> loadCollectedEntries(CollectionEntry entry) {
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
						resultList.add(getAnnotatedBiblographybyID(entryID));
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
					default:
						break;
				}
			}
			rs.close();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultList;
	}

	private DepictionEntry getDepictionEntry(int depictionID, String sessionID) {
		Connection dbc = getConnection();
		DepictionEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Depictions WHERE DepictionID=" + depictionID);
			// we only need to call this once, since we do not expect more than 1 result!
			if (rs.next()) { 
				result = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("SeparateAksaras"), rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getDouble("Width"), rs.getDouble("Height"),
						getExpedition(rs.getInt("ExpeditionID")), rs.getDate("PurchaseDate"), getLocation(rs.getInt("CurrentLocationID")), rs.getString("InventoryNumber"),
						getVendor(rs.getInt("VendorID")), rs.getInt("StoryID"), getCave(rs.getInt("CaveID")), rs.getInt("WallID"), rs.getInt("AbsoluteLeft"),
						rs.getInt("AbsoluteTop"), rs.getInt("ModeOfRepresentationID"), rs.getString("ShortName"), rs.getString("PositionNotes"),
						rs.getInt("MasterImageID"), rs.getInt("AccessLevel"), rs.getString("LastChangedByUser"), rs.getString("LastChangedOnDate"));
				result.setRelatedImages(getRelatedImages(result.getDepictionID(), sessionID));
				result.setRelatedBibliographyList(getRelatedBibliographyFromDepiction(result.getDepictionID()));
				result.setRelatedIconographyList(getRelatedIconography(result.getDepictionID()));
			}
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public int addPreservationClassification(PreservationClassificationEntry pcEntry) {
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
		}
		return preservationAttributeID;
	}
	
}
