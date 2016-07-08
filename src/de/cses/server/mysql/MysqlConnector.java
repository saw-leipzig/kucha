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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import de.cses.shared.District;
import de.cses.shared.ImageEntry;
import de.cses.shared.PhotographerEntry;

/**
 * This is the central Database connector. 
 * @author alingnau
 *
 */
public class MysqlConnector {

	private static String url = "jdbc:mysql://kucha.informatik.hu-berlin.de/infosys?useUnicode=true&characterEncoding=UTF-8"; //$NON-NLS-1$
	private static String user = Messages.getString("MysqlConnector.db.user");
	private static String password = Messages.getString("MysqlConnector.db.password");

	private static MysqlConnector instance = null;

	/**
	 * We try to avoid that a new instance will be created if there is already one existing.
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

			System.err.println("class loaded"); //$NON-NLS-1$

			try {
				connection = DriverManager.getConnection(MysqlConnector.url, MysqlConnector.user, MysqlConnector.password);
			} catch (SQLException e) {
				e.printStackTrace();
			}

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
	 * This is a test method that can be used for testing purpose only. 
	 * Likely to disappear in later versions!
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
	 * @return
	 */
	public synchronized ArrayList<District> getDistricts() {
		ArrayList<District> Districts = new ArrayList<District>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			String sql = "SELECT * FROM Districts"; //$NON-NLS-1$

			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				District District = new District();
				District.setDistrictID(rs.getInt("DistrictID"));
				District.setName(rs.getString("Name"));
				District.setDescription(rs.getString("Description"));
				Districts.add(District);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return Districts;
	}

	/**
	 * Creates a new image entry in the table 'Images'
	 * @return auto incremented primary key 'ImageID'
	 */
	public synchronized int createNewImageEntry() {
		Connection dbc = getConnection();
		Statement stmt;
		int generatedKey = -1;
		try {
			stmt = dbc.createStatement();

			stmt.execute("INSERT INTO Images (Title,Comment) VALUES ('Image Title','please type your comment here')", Statement.RETURN_GENERATED_KEYS);
			ResultSet keys = stmt.getGeneratedKeys();
			while (keys.next()) { 
				// there should only be 1 key returned here but we need to modify this in case
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
	 * @param sqlUpdate 
	 * @return
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
	 * 
	 * @return ArrayList<String> with result from 'SELECT * FROM Images'
	 */
	public ArrayList<ImageEntry> getImageEntries() {
		ArrayList<ImageEntry> results = new ArrayList<ImageEntry>();
		Connection dbc = getConnection();
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images");
			while (rs.next()) { 
				results.add(new ImageEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getDate(9)));
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
	public ImageEntry getImageEntry(int imageID) {
		Connection dbc = getConnection();
		ImageEntry result = null;
		Statement stmt;
		try {
			stmt = dbc.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM Images WHERE ImageID="+imageID);
			while (rs.next()) { 
				result = new ImageEntry(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getInt(7), rs.getString(8), rs.getDate(9));
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

	public ArrayList<PhotographerEntry> getPhotographerEntries() {
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

}
