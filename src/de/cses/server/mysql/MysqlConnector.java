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
package de.cses.server.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import de.cses.client.kuchaMapPopupPanels.HoehlenUebersichtPopUpPanelContainer;
import de.cses.client.kuchaMapPopupPanels.RegionenUebersichtPopUpPanelContainer;
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
 * This is the central Database connector. Here are all method located that we
 * need for standard database operations, including use login and access
 * management.
 * 
 * @author alingnau
 *
 */
public class MysqlConnector {

	private static String url = "jdbc:mysql://kucha.informatik.hu-berlin.de/infosys?useUnicode=true&characterEncoding=UTF-8"; //$NON-NLS-1$
	private static String user = Messages.getString("MysqlConnector.db.user");
	private static String password = Messages.getString("MysqlConnector.db.password");
	private int auto_increment_id;

	private static MysqlConnector instance = null;

	/**
	 * By calling this method, we avoid that a new instance will be created if
	 * there is already one existing. If this method is called without an instance
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

		try {
			Class.forName("org.mariadb.jdbc.Driver"); //$NON-NLS-1$
			connection = DriverManager.getConnection(MysqlConnector.url, MysqlConnector.user, MysqlConnector.password);
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() {
		try {
			if (connection == null || !connection.isValid(5)) {
				connection = DriverManager.getConnection(MysqlConnector.url, MysqlConnector.user, MysqlConnector.password);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return connection;
	}

	/**
	 * This is a test method that can be used for testing purpose only. Likely to
	 * disappear in later versions!
	 * 
	 * @return
	 */
	public synchronized Hashtable<String, String> getTestTable() {

		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();

			String sql = "SELECT * FROM test"; //$NON-NLS-1$

			ResultSet rs = stmt.executeQuery(sql);

			Hashtable<String, String> results = new Hashtable<String, String>();

			String name, age;
			while (rs.next()) {
				name = rs.getString("Name"); //$NON-NLS-1$
				age = rs.getString("Age"); //$NON-NLS-1$
				results.put(name, age);
				System.err.println("(" + name + "," + age + ") read from database"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}

			rs.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
			return results;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Selects all districts from the table 'Districts' in the database
	 * 
	 * @return
	 */
	public synchronized ArrayList<DistrictEntry> getDistricts() {
		ArrayList<DistrictEntry> DistrictEntries = new ArrayList<DistrictEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			String sql = "SELECT * FROM Districts"; //$NON-NLS-1$

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				DistrictEntry DistrictEntry = new DistrictEntry();
				DistrictEntry.setDistrictID(rs.getInt("DistrictID"));
				DistrictEntry.setName(rs.getString("Name"));
				DistrictEntry.setDescription(rs.getString("Description"));
				DistrictEntries.add(DistrictEntry);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return DistrictEntries;
	}

	/**
	 * Creates a new image entry in the table 'Images'
	 * 
	 * @return auto incremented primary key 'ImageID'
	 */
	public synchronized int createNewImageEntry() {
		Connection dbc = getConnection();
		Statement stmt;
		int generatedKey = -1;
		try {
			stmt = dbc.createStatement();
			stmt.execute("INSERT INTO Images (Title,Comment,ImageType) VALUES ('New Image','please type your comment here','photo')",
					Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = stmt.getGeneratedKeys();
			while (keys.next()) {
				// there should only be 1 key returned here but we need to modify this
				// in case
				// we have requested multiple new entries. works for the moment
				generatedKey = keys.getInt(1);
				System.err.println("result key " + generatedKey);
			}
			keys.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generatedKey;
	}

	/**
	 * Executes a pre-defined SQL INSERT statement and returns the generated
	 * (auto-increment) unique key from the table.
	 * 
	 * @return auto incremented primary key
	 */
	public synchronized int insertEntry(String sqlInsert) {
		Connection dbc = getConnection();
		Statement stmt;
		int generatedKey = -1;
		try {
			stmt = dbc.createStatement();
			stmt.execute(sqlInsert, Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = stmt.getGeneratedKeys();
			while (keys.next()) {
				// there should only be 1 key returned here but we need to modify this
				// in case
				// we have requested multiple new entries. works for the moment
				generatedKey = keys.getInt(1);
				System.err.println("result key " + generatedKey);
			}
			keys.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return generatedKey;
	}

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
			dbc.close();
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
	public synchronized boolean deleteEntry(String sqlDelete) {
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			stmt.execute(sqlDelete);
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public synchronized ArrayList<DepictionEntry> getDepictions() {
		ArrayList<DepictionEntry> results = new ArrayList<DepictionEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Depictions");
			while (rs.next()) {
				results.add(new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"), rs.getString("Material"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getInt("Dimension.width"),
						rs.getInt("Dimension.height"), rs.getDate("DateOfAcquisition"), rs.getInt("ExpeditionID"), rs.getDate("PurchaseDate"),
						rs.getInt("CurrentLocationID"), rs.getInt("VendorID"), rs.getInt("StoryID"), rs.getInt("CaveID")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public DepictionEntry getDepictionEntry(int depictionID) {
		Connection dbc = getConnection();
		DepictionEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Depictions WHERE DepictionID=" + depictionID);
			if (rs.next()) { // we only need to call this once, since we do not expect
												// more than 1 result!
				result = new DepictionEntry(rs.getInt("DepictionID"), rs.getInt("StyleID"), rs.getString("Inscriptions"),
						rs.getString("Dating"), rs.getString("Description"), rs.getString("BackgroundColour"), rs.getString("Material"),
						rs.getString("GeneralRemarks"), rs.getString("OtherSuggestedIdentifications"), rs.getInt("Dimension.width"),
						rs.getInt("Dimension.height"), rs.getDate("DateOfAcquisition"), rs.getInt("ExpeditionID"), rs.getDate("PurchaseDate"),
						rs.getInt("CurrentLocationID"), rs.getInt("VendorID"), rs.getInt("StoryID"), rs.getInt("CaveID"));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * @return ArrayList<String> with result from 'SELECT * FROM Images'
	 */
	public synchronized ArrayList<ImageEntry> getImageEntries() {
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images");
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6),
						rs.getDate(7), rs.getString(8)));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<ImageEntry> getImageEntries(String sqlWhere) {
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images WHERE " + sqlWhere);
			while (rs.next()) {
				results.add(new ImageEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6),
						rs.getDate(7), rs.getString(8)));
			}
			rs.close();
			stmt.close();
			dbc.close();
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
	public synchronized ImageEntry getImageEntry(int imageID) {
		Connection dbc = getConnection();
		ImageEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images WHERE ImageID=" + imageID);
			while (rs.next()) {
				result = new ImageEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5), rs.getString(6),
						rs.getDate(7), rs.getString(8));
			}
			rs.close();
			stmt.close();
			dbc.close();
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
	public synchronized ArrayList<PhotographerEntry> getPhotographerEntries() {
		ArrayList<PhotographerEntry> results = new ArrayList<PhotographerEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Photographers");
			while (rs.next()) {
				results.add(new PhotographerEntry(rs.getInt(1), rs.getString(2)));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	// public synchronized boolean updateImage(String data) {
	// Connection dbc = getConnection();
	// Statement stmt;
	// try {
	// stmt = dbc.createStatement();
	// int result= stmt.executeUpdate(data);
	// stmt.close();
	// dbc.close();
	// } catch (SQLException e) {
	// e.printStackTrace();
	// return false;
	// }
	// return true;
	// }

	public synchronized ArrayList<CaveEntry> getCaves() {
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Caves");
			while (rs.next()) {
				results.add(new CaveEntry(rs.getInt("CaveID"), rs.getInt("DistrictID"), rs.getString("OfficialName"),
						rs.getString("OfficialNumber"), rs.getString("HistoricName"), rs.getInt("CaveTypeID"),
						rs.getString("StateOfPreservation"), rs.getString("Orientation"), rs.getString("Pedestals"),
						rs.getString("Findings")));

			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<CaveEntry> getCavesbyDistrictID(int DistrictID) {
		ArrayList<CaveEntry> results = new ArrayList<CaveEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Caves WHERE DistrictID =" + DistrictID);
			while (rs.next()) {
				results.add(new CaveEntry(rs.getInt("CaveID"), rs.getInt("DistrictID"), rs.getString("OfficialName"),
						rs.getString("OfficialNumber"), rs.getString("HistoricName"), rs.getInt("CaveTypeID"),
						rs.getString("StateOfPreservation"), rs.getString("Orientation"), rs.getString("Pedestals"),
						rs.getString("Findings")));

			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<OrnamentEntry> getOrnaments() {
		ArrayList<OrnamentEntry> results = new ArrayList<OrnamentEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Ornaments");
			while (rs.next()) {
				results.add(new OrnamentEntry(rs.getInt("OrnamentID"), rs.getString("Code")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<OrnamentOfOtherCulturesEntry> getOrnametsOfOtherCultures() {
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
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized CaveTypeEntry getCaveTypebyID(int caveTypeID) {
		Connection dbc = getConnection();
		CaveTypeEntry resultCaveType = new CaveTypeEntry();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CaveType WHERE CaveTypeID =" + caveTypeID);
			while (rs.next()) {
				resultCaveType.setEnDescription(rs.getString("DescriptionEN"));
				resultCaveType.setEnShortname(rs.getString("NameEN"));

			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return resultCaveType;
	}

	public synchronized ArrayList<CaveTypeEntry> getCaveTypes() {
		System.err.println("IN METHODE DRIN");
		Connection dbc = getConnection();
		ArrayList<CaveTypeEntry> results = new ArrayList<CaveTypeEntry>();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM CaveType");
			while (rs.next()) {
				System.err.println("cave type gefunden");
				CaveTypeEntry caveType = new CaveTypeEntry(rs.getInt("CaveTypeID"), rs.getString("NameEN"),
						rs.getString("DescriptionEN"));
				results.add(caveType);
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			System.err.println("error in db zugriff");
			e.printStackTrace();
			return results;
		}
		return results;

	}

	public synchronized boolean saveOrnamentEntry(OrnamentEntry ornamentEntry) {
		System.err.println("reached saving function");

		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt
					.executeQuery("INSERT INTO Ornaments (Code, Description, Remarks, Interpretation, Sketch, OrnamentReferences) VALUES ("
							+ ornamentEntry.getCode() + "," + ornamentEntry.getDescription() + "," + ornamentEntry.getRemarks() + ","
							+ ornamentEntry.getInterpretation() + "," + ornamentEntry.getSketch() + "," + ornamentEntry.getReferences() + ")");
			rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
			while (rs.next()) {
				auto_increment_id = rs.getInt(1);
			}
			System.err.println("inserted into ornaments got autoid");
			for (int i = 0; i < ornamentEntry.getCavesRelations().size(); i++) {
				stmt = dbc.createStatement();
				for (int j = 0; j < ornamentEntry.getCavesRelations().get(i).getRelatedOrnamentsRelationID().size(); j++) {
					rs = stmt.executeQuery("INSERT INTO RelatedOrnamentsRelation (OrnamentID1, OrnamentID2, CaveID) VALUES ("
							+ auto_increment_id + "," + ornamentEntry.getCavesRelations().get(i).getRelatedOrnamentsRelationID().get(j) + ","
							+ ornamentEntry.getCavesRelations().get(i).getCaveID() + ")");
				}
				System.err.println("inserted related ornaments");
				for (int j = 0; j < ornamentEntry.getCavesRelations().get(i).getSimilarOrnamentsRelationID().size(); j++) {
					rs = stmt.executeQuery("INSERT INTO SimilarOrnamentsRelation (OrnamentID1, OrnamentID2, CaveID) VALUES ("
							+ auto_increment_id + "," + ornamentEntry.getCavesRelations().get(i).getSimilarOrnamentsRelationID().get(j) + ","
							+ ornamentEntry.getCavesRelations().get(i).getCaveID() + ")");
				}
				System.err.println("inserted similar ornaments");
				for (int j = 0; j < ornamentEntry.getCavesRelations().get(i).getOtherCulturalOrnamentsRelationID().size(); j++) {
					rs = stmt.executeQuery(
							"INSERT INTO OrnamentsOfOtherCulturesRelation (OrnamentID, OrnamentOfOtherCulturesID, CaveID) VALUES ("
									+ auto_increment_id + ","
									+ ornamentEntry.getCavesRelations().get(i).getOtherCulturalOrnamentsRelationID().get(j) + ","
									+ ornamentEntry.getCavesRelations().get(i).getCaveID() + ")");
				}
				System.err.println("inserted ornaments of other cultures");
				rs = stmt.executeQuery(
						"INSERT INTO CaveOrnamentRelation (CaveID, OrnamentID, Style, Orientation, Structure, MainTypologicalClass, Colours, Position, Function, CavePart, Notes,GroupOfOrnaments) VALUES ("
								+ ornamentEntry.getCavesRelations().get(i).getCaveID() + "," + auto_increment_id + ","
								+ ornamentEntry.getCavesRelations().get(i).getStyle() + ","
								+ ornamentEntry.getCavesRelations().get(i).getOrientation() + ","
								+ ornamentEntry.getCavesRelations().get(i).getStructure() + ","
								+ ornamentEntry.getCavesRelations().get(i).getMainTopologycalClass() + ","
								+ ornamentEntry.getCavesRelations().get(i).getColours() + ","
								+ ornamentEntry.getCavesRelations().get(i).getPosition() + ","
								+ ornamentEntry.getCavesRelations().get(i).getFunction() + ","
								+ ornamentEntry.getCavesRelations().get(i).getCavepart() + ","
								+ ornamentEntry.getCavesRelations().get(i).getNotes() + "," + ornamentEntry.getCavesRelations().get(i).getGroup()
								+ ")");
				System.err.println("inserted cave relation");
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public synchronized ArrayList<IconographyEntry> getIconography() {
		ArrayList<IconographyEntry> root = getIconographyEntries(0);

		for (IconographyEntry item : root) {
			processIconographyTree(item);
		}
		return root;
	}

	private synchronized void processIconographyTree(IconographyEntry parent) {
		ArrayList<IconographyEntry> children = getIconographyEntries(parent.getIconographyID());
		if (children != null) {
			parent.setChildren(children);
			for (IconographyEntry child : children) {
				processIconographyTree(child);
			}
		}
	}

	protected synchronized ArrayList<IconographyEntry> getIconographyEntries(int parentID) {
		ArrayList<IconographyEntry> results = new ArrayList<IconographyEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String where = (parentID == 0) ? "IS NULL" : "= " + parentID;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Iconography WHERE ParentID " + where);
			while (rs.next()) {
				results.add(new IconographyEntry(rs.getInt("IconographyID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<PictorialElementEntry> getPictorialElements() {
		ArrayList<PictorialElementEntry> root = getPictorialElementEntries(0);

		for (PictorialElementEntry item : root) {
			processPictorialElementsTree(item);
		}
		return root;
	}

	private synchronized void processPictorialElementsTree(PictorialElementEntry parent) {
		ArrayList<PictorialElementEntry> children = getPictorialElementEntries(parent.getPictorialElementID());
		if (children != null) {
			parent.setChildren(children);
			for (PictorialElementEntry child : children) {
				processPictorialElementsTree(child);
			}
		}
	}

	protected synchronized ArrayList<PictorialElementEntry> getPictorialElementEntries(int parentID) {
		ArrayList<PictorialElementEntry> results = new ArrayList<PictorialElementEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		String where = (parentID == 0) ? "IS NULL" : "= " + parentID;

		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM PictorialElements WHERE ParentID " + where);
			while (rs.next()) {
				results.add(new PictorialElementEntry(rs.getInt("PictorialElementID"), rs.getInt("ParentID"), rs.getString("Text")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<VendorEntry> getVendors() {
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
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<StyleEntry> getStyles() {
		ArrayList<StyleEntry> results = new ArrayList<StyleEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Styles");
			while (rs.next()) {
				results.add(new StyleEntry(rs.getInt("StyleID"), rs.getString("StyleName")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<ExpeditionEntry> getExpeditions() {
		ArrayList<ExpeditionEntry> results = new ArrayList<ExpeditionEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Expeditions");
			while (rs.next()) {
				results.add(new ExpeditionEntry(rs.getInt("ExpeditionID"), rs.getString("Name"), rs.getString("Leader"),
						rs.getDate("StartDate"), rs.getDate("EndDate")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized ArrayList<PublicationEntry> getPublications() {
		ArrayList<PublicationEntry> results = new ArrayList<PublicationEntry>();
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publications");
			while (rs.next()) {
				results.add(
						new PublicationEntry(rs.getInt("PublicationID"), rs.getString("Editors"), rs.getString("Type"), rs.getString("DOI"),
								rs.getString("Pages"), rs.getDate("Year"), rs.getInt("PublisherID"), rs.getString("Title.English"),
								rs.getString("Title.Phonetic"), rs.getString("Title.Original"), rs.getString("Abstract")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}

	public synchronized PublicationEntry getPublicationEntry(int id) {
		PublicationEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Publications WHERE PublicationID=" + id);
			while (rs.next()) {
				result = new PublicationEntry(rs.getInt("PublicationID"), rs.getString("Editors"), rs.getString("Type"),
						rs.getString("DOI"), rs.getString("Pages"), rs.getDate("Year"), rs.getInt("PublisherID"),
						rs.getString("Title.English"), rs.getString("Title.Phonetic"), rs.getString("Title.Original"),
						rs.getString("Abstract"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * @param id
	 * @return
	 */
	public synchronized AuthorEntry getAuthorEntry(int id) {
		AuthorEntry result = null;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Authors WHERE AuthorID=" + id);
			while (rs.next()) {
				result = new AuthorEntry(rs.getInt("AuthorID"), rs.getString("Lastname"), rs.getString("Firstname"),
						rs.getDate("KuchaVisitDate"), rs.getString("Affiliation"), rs.getString("Email"), rs.getString("Homepage"));
			}
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
	public synchronized int getRelatedMasterImageID(int depictionID) {
		int result = 0;
		Connection dbc = getConnection();

		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM DepictionImageRelation WHERE DepictionID=" + depictionID);
			while (rs.next() && (result == 0)) {
				if (rs.getBoolean("IsMaster")) {
					result = rs.getInt("ImageID");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return result;
	}
	public  synchronized RegionenUebersichtPopUpPanelContainer getRegionenInfosbyID(int ID ){
		Connection dbc = getConnection();
		
		System.out.println("reached");
		Statement stmt;
	
			try {
			stmt = dbc.createStatement();
			String sql = "SELECT * FROM Districts WHERE DistrictID = "+ "'"+ID+"'"; //$NON-NLS-1$
			ResultSet rs = stmt.executeQuery(sql);
			RegionenUebersichtPopUpPanelContainer popup = new RegionenUebersichtPopUpPanelContainer();
			while(rs.next()){
				popup.setID(rs.getInt("DistrictID")); 
				popup.setName(rs.getString("Name"));
				popup.setDescription(rs.getString("Description"));
				
			}
			int imageID=0;
			sql = "SELECT * FROM KuchaMapDistricts WHERE ID = "+ "'"+ID+"'";
			rs = stmt.executeQuery(sql);
				while(rs.next()){
					imageID = rs.getInt("ImageID");
				}
			sql = "SELECT * FROM Images WHERE ImageID = "+ "'"+imageID+"'";
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				popup.setUrl("http://kucha.informatik.hu-berlin.de/tomcat/images/tn" + rs.getString("Filename"));
			}
			
			
			
			rs.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
			return popup;

			}
				catch (SQLException e) {
			
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		

}
	public synchronized HoehlenUebersichtPopUpPanelContainer getHoehleInfosbyID(int ID) {

		Connection dbc = getConnection();
		
		System.out.println("reached");
		Statement stmt;
		int caveTypeID = 0;
		ArrayList<Integer> depictionID = new ArrayList<Integer>();
		ArrayList<Integer> imageIDArrayList = new ArrayList<Integer>();
		try {
			stmt = dbc.createStatement();

			String sql = "SELECT * FROM Caves WHERE CaveID = "+ "'"+ID+"'"; //$NON-NLS-1$

			ResultSet rs = stmt.executeQuery(sql);
			HoehlenUebersichtPopUpPanelContainer popup = new HoehlenUebersichtPopUpPanelContainer();

			while (rs.next()) {
				popup.setOfficialName(rs.getString("OfficialName"));; //$NON-NLS-1$
				popup.setHoehlenID(rs.getInt("CaveID"));
				popup.setHistoricName(rs.getString("HistoricName"));
				caveTypeID = rs.getInt("CaveTypeID");
			}

			sql = "SELECT * FROM CaveType WHERE CaveTypeID = "+ "'"+caveTypeID+"'"; //$NON-NLS-1$

			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				popup.setCaveTypeDescription(rs.getString("DescriptionEN"));
				System.err.println("cavetype description funktioniert ");
			}
			sql = "SELECT * FROM Depictions WHERE CaveID = "+ "'"+ID+"'"; //$NON-NLS-1$
			System.err.println("sql query wurde auf get depiction geändert ");
			rs = stmt.executeQuery(sql);
			while (rs.next()) {
				depictionID.add(rs.getInt("DepictionID")); 
				System.err.println("depiction id wurde gefunden " + depictionID);
			}
			for(int k = 0; k < depictionID.size(); k++){
			sql = "SELECT * FROM DepictionImageRelation WHERE DepictionID = "+ "'"+depictionID.get(k)+"'"; //$NON-NLS-1$
            int i = 0;
            System.err.println("sql wurde gändert auf depictionimageRelation ");
			rs = stmt.executeQuery(sql);
			while (rs.next() && i<10) {
				imageIDArrayList.add(rs.getInt("ImageID")); 
				i++;
				System.err.println("imageid wurde gefunden");
			}
			}
			System.err.println(imageIDArrayList.size() + " ist imageidarraysize");
			
			for(int p = 0; p < imageIDArrayList.size(); p ++){
				sql = "SELECT * FROM Images WHERE ImageID = "+ "'"+imageIDArrayList.get(p)+"'"; //$NON-NLS-1$
				rs = stmt.executeQuery(sql);
				while (rs.next()) {
					System.err.println("ein bild eingetragen");
					String filename = rs.getString("Filename");
					popup.getBilderURLs().add("http://kucha.informatik.hu-berlin.de/tomcat/images/tn" +filename);
				}
			}

			rs.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
			return popup;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
	public synchronized String saveRegion(ArrayList<HoehlenContainer> Hoehlen, int RegionID, int ButtonSize, int imageID){
		Connection dbc = getConnection();
		String sql = "REPLACE INTO KuchaMapCaves(ID,ButtonPosLeft, ButtonPosTop, RegionID) VALUES (?,?,?,?)";
		PreparedStatement pstmt;
		
	try {
		setRegionFoto(imageID, RegionID);
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
		
	if (RegionID==-1){
		sql = "UPDATE KuchaMapDistricts SET ButtonPosLeft = ? ,ButtonPosTop = ? WHERE ID = ? ";
	}
		for (int i=0; i< Hoehlen.size(); i++){
			System.err.println(Hoehlen.size() + "Runde: " + i);
			
			
			try {
				if(RegionID == -1){
					pstmt = dbc.prepareStatement(sql);
					System.err.println("startseite case");
				    pstmt.setInt(1, Hoehlen.get(i).getButtonPositionLeft());
				    System.err.println("set buttonpos left");
				    pstmt.setInt(2, Hoehlen.get(i).getButtonPositionTop());
				    System.err.println("set button pos top");
				    pstmt.setInt(3, Hoehlen.get(i).getID());
				    System.err.println("set hoehlen id");
				    pstmt.executeUpdate();
				    System.err.println("executet");
				}
				else{
				pstmt = dbc.prepareStatement(sql);
				System.err.println(pstmt);
				System.err.println("prepared in not NUll case");
			    pstmt.setInt(1, Hoehlen.get(i).getID());
			    System.err.println("settet int ID");
			    pstmt.setInt(2, Hoehlen.get(i).getButtonPositionLeft());
			    System.err.println("settet int buttonpositionleft");
			    pstmt.setInt(3, Hoehlen.get(i).getButtonPositionTop());
			    System.err.println("settet int buttonpositiontop");
			    pstmt.setInt(4, Hoehlen.get(i).getRegionID());
			    System.err.println("settet int regionID");
			    pstmt.executeUpdate();
				}
			} catch (SQLException e) {
				System.err.println("Hoehlen speichern failed");
				e.printStackTrace();
			}
		}
			sql = "UPDATE KuchaMapDistricts SET ButtonSize = "+ "'"+ButtonSize+"'" + "WHERE ID = "+  "'"+RegionID+"'";
			try {
				System.err.println("buttonsize setzen ");
				pstmt = dbc.prepareStatement(sql);
			    pstmt.executeUpdate();
			    ResultSet rs = pstmt.getGeneratedKeys();
			    System.err.println("buttonsize setzen kram erledigt ");
			    int idreturn = -1;
			    if (rs.next()) {
			      idreturn = rs.getInt(1);
			    }
			    if(idreturn == 1){
				    rs.close();
				    pstmt.close();
			    	return "saving failed";
			    }
			    else{
				    rs.close();
				    pstmt.close();
			    	return "saved";
			    }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return "saved";
		
	}
	public synchronized String getImageURLbyImageID(int imageID) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		String sql = "SELECT * FROM Images WHERE ImageID = "+ imageID; 
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			return rs.getString("Filename");
		}
		return null;
	}
	public  synchronized ArrayList<RegionContainer> createRegionen(){
		System.err.println("in createregionenmethode drin");
		Connection dbc = getConnection();
		Statement stmt;
		System.err.println("initialized everything");
		ArrayList<RegionContainer> Regionen = new ArrayList<RegionContainer>();
		System.err.println("created arraylist");
			try {
			stmt = dbc.createStatement();
			System.err.println("created statement");
			String sql = "SELECT * FROM KuchaMapDistricts"; //$NON-NLS-1$
			ResultSet rs = stmt.executeQuery(sql);
			System.err.println("executed query");
			while(rs.next()){
				RegionContainer region = new RegionContainer();
				region.setID(rs.getInt("ID"));
				System.err.println(rs.getInt("ID"));
				region.setName(rs.getString("Name"));
				region.setImageID(rs.getInt("ImageID"));
				region.setHoehlenButtonSize(rs.getInt("ButtonSize"));
				region.setRegionButtonPositionX(rs.getInt("ButtonPosLeft"));
				region.setRegionButtonPositionX(rs.getInt("ButtonPosTop"));
				Regionen.add(region);
				
			}
		for(int i = 0; i< Regionen.size(); i++){
			sql = "SELECT * FROM Images WHERE ImageID ="+ Regionen.get(i).getImageID(); 
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				Regionen.get(i).setFotoURL("http://kucha.informatik.hu-berlin.de/tomcat/images/"+ rs.getString("Filename"));
			}
		}

			
			rs.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished"); //$NON-NLS-1$
			
			return Regionen;
			}
				catch (SQLException e) {
			System.err.println("fuuuuuuuuuuuuu");
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			return Regionen;
			
	}
	public synchronized ArrayList<HoehlenContainer> getHoehlenbyRegionID(int ID){
		Connection dbc = getConnection();
		Statement stmt;
		ArrayList<HoehlenContainer> Hoehlen = new ArrayList<HoehlenContainer>();
		System.err.println(ID);

		
		try {
			stmt = dbc.createStatement();
			System.err.println("created statement");

			String sql = "SELECT * FROM KuchaMapCaves WHERE RegionID =" + "'"+ID+"'"; //$NON-NLS-1$
			if(ID == -1){
				sql = "SELECT * FROM KuchaMapDistricts";
			}
			ResultSet rs = stmt.executeQuery(sql);
			System.err.println("executed query");
		
			while(rs.next()){
			System.err.println("creating HoehlenContainer and filling list");
				HoehlenContainer Hoehle = new HoehlenContainer();
				System.err.println("created HoehlenContainer");
				Hoehle.setID(rs.getInt("ID"));		
				System.err.println("got the ID");
				Hoehle.setButtonPositionLeft(rs.getInt("ButtonPosLeft"));
				System.err.println("got the button pos left");
				Hoehle.setButtonPositionTop(rs.getInt("ButtonPosTop"));
				System.err.println("got buttonpos right");
				if(ID !=-1){
				Hoehle.setRegionID(rs.getInt("RegionID"));
				System.err.println("got regionID");
				}
				
				Hoehlen.add(Hoehle);
				System.err.println("set everything");
				
				
			}
			
			rs.close();
			stmt.close();
			dbc.close();
			System.err.println("Database request finished");
			return Hoehlen;
		
		
	}
		catch(SQLException e){
			return null;
		}
	}
	public synchronized ArrayList<HoehlenContainer> getCavesbyDistrictIDKucha(int DistrictID) {
		ArrayList<HoehlenContainer> results = new ArrayList<HoehlenContainer>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Caves WHERE DistrictID ="+DistrictID);
			while (rs.next()) { 
				results.add(new HoehlenContainer(rs.getInt("DistrictID"),rs.getInt("CaveID"),rs.getString("OfficialName")));
			}
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return results;
	}
	public synchronized String deleteHoehlebyID(int hoehlenID) {
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("DELETE FROM KuchaMapCaves WHERE ID ="+hoehlenID);
			rs.close();
			stmt.close();
			dbc.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return "failed";
		}
		return "deleted";
	}
	public synchronized String setRegionFoto(int imageID, int regionID) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		String sql = "UPDATE KuchaMapDistricts SET ImageID = "+ imageID +" "+ "WHERE ID = "+regionID; 
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			return "saved";
		}
		return "saved";
	}
	public synchronized ArrayList<CaveEntry> getCavesbyAntechamber(ArrayList<AntechamberEntry> antechambers) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =0; i< antechambers.size(); i++){
		String sql = "SELECT * FROM Caves WHERE CaveID = "+ antechambers.get(i).getcaveID(); 
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			CaveEntry newCave = new CaveEntry();
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	public synchronized ArrayList<CaveEntry> getCavesbyCaveType(ArrayList<CaveTypeEntry> caveTypes) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =0; i< caveTypes.size(); i++){
		String sql = "SELECT * FROM Caves WHERE CaveTypeID = "+ caveTypes.get(i).getCaveTypeID(); 
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("cavetype hoehle gefunden");
			CaveEntry newCave = new CaveEntry();
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	public synchronized ArrayList<CaveEntry> getCavesbyCella(ArrayList<CellaEntry> cellas) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =0; i< cellas.size(); i++){
		String sql = "SELECT * FROM Caves WHERE CaveID = "+ cellas.get(i).getCaveID(); 
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			
			CaveEntry newCave = new CaveEntry();
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	public synchronized ArrayList<CaveEntry> getCavesbyDistrict(ArrayList<DistrictEntry> districts) throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =0; i< districts.size(); i++){
		String sql = "SELECT * FROM Caves WHERE DistrictID = "+ districts.get(i).getDistrictID();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("district hoehle gefunden");
			CaveEntry newCave = new CaveEntry();
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	
	public synchronized ArrayList<CaveEntry> getCavesbyDepiction(ArrayList<DepictionEntry> depictions)throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		
		return null;
	}
	
	public synchronized ArrayList<CaveEntry> getCavesbyOrnament(ArrayList<OrnamentEntry> ornaments)throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =0; i< ornaments.size(); i++){
		String sql = "SELECT * FROM CaveOrnamentRelation JOIN Caves ON CaveOrnamentRelation.CaveID = Caves.CaveID WHERE OrnamentID = "+ ornaments.get(i).getOrnamentID();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("ornament hoehle gefunden");
			CaveEntry newCave = new CaveEntry();
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	
	public synchronized ArrayList<OrnamentEntry> getRandomOrnaments()throws SQLException{
		System.err.println("ger random elements gestartet");
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<OrnamentEntry> ornaments = new ArrayList<OrnamentEntry>();
		for(int i =3; i< 6; i++){
		String sql = "SELECT * FROM Ornaments WHERE OrnamentID = "+ i;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("ornament gefunden");
			OrnamentEntry newOrnament = new OrnamentEntry();
			newOrnament.setCode(rs.getString("Code"));
			newOrnament.setOrnamentID(rs.getInt("OrnamentID"));
			newOrnament.setDescription(rs.getString("Description"));
			newOrnament.setRemarks(rs.getString("Remarks"));
			newOrnament.setReferences("OrnamentReferences");
			ornaments.add(newOrnament);
		}
		}
		return ornaments;
	}

	
	public synchronized ArrayList<CaveEntry> getRandomCaves()throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		System.err.println("in get random caves");
		ArrayList<CaveEntry> caves = new ArrayList<CaveEntry>();
		for(int i =4; i< 9; i++){
		String sql = "SELECT * FROM Caves WHERE CaveID = "+ i;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			CaveEntry newCave = new CaveEntry();
			System.err.println("random cave gefunden");
			newCave.setCaveID(rs.getInt("CaveID"));
			newCave.setDistrictID(rs.getInt("DistrictID"));
			newCave.setAlterationDate(rs.getString("AlterationDate"));
			newCave.setHistoricalName(rs.getString("HistoricName"));
			newCave.setName(rs.getString("OfficialName"));
			newCave.setOfficialNumber(rs.getString("OfficialNumber"));
			caves.add(newCave);
		}
		}
		return caves;
	}
	
	
	public synchronized ArrayList<CaveTypeEntry> getRandomCaveTypes()throws SQLException{
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		System.err.println("in random cavetype methode");
		ArrayList<CaveTypeEntry> caveTypes = new ArrayList<CaveTypeEntry>();
		for(int i =1; i< 2; i++){
		String sql = "SELECT * FROM CaveType WHERE CaveTypeID = "+ i;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("cave type gefunden");
			CaveTypeEntry newCaveType = new CaveTypeEntry();
			newCaveType.setCaveTypeID(rs.getInt("CaveTypeID"));
			newCaveType.setEnDescription(rs.getString("DescriptionEN"));
			newCaveType.setEnShortname(rs.getString("NameEN"));
			caveTypes.add(newCaveType);
		}
		}
		return caveTypes;
	}
	
	public synchronized ArrayList<DistrictEntry> getRandomDistricts() throws SQLException{
		System.err.println("get random district gestartet");
		Connection dbc = getConnection();
		Statement stmt = dbc.createStatement();
		ArrayList<DistrictEntry> districts = new ArrayList<DistrictEntry>();
		for(int i =1; i< 2; i++){
		String sql = "SELECT * FROM Districts WHERE DistrictID = "+ i;
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()){
			System.err.println("district gefunden");
			DistrictEntry newDistrict= new DistrictEntry();
			newDistrict.setDistrictID(rs.getInt("DistrictID"));
			newDistrict.setName(rs.getString("Name"));
			newDistrict.setDescription(rs.getString("Description"));
			districts.add(newDistrict);
		}
		}
		return districts;
	}
	
}
