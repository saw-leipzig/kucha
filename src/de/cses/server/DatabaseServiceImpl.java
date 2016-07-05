/*
 * Copyright 2016 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU Affero General Public License version 3 (GNU AGPLv3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GNU AGPLv3 for more details.
 * 
 * You should have received a copy of the GNU AGPLv3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/agpl-3.0.txt>.
 */
package de.cses.server;

import java.util.ArrayList;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.cses.client.DatabaseService;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.District;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class DatabaseServiceImpl extends RemoteServiceServlet implements DatabaseService {

	public String dbServer(String name) throws IllegalArgumentException {
		
		MysqlConnector connector = MysqlConnector.getInstance();
		System.err.println("looking for " + name + " in database");
		String result = name + " is " + connector.getTestTable().get(name) + " years old!";
		return result;		
	}

	public ArrayList<District> getDistricts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<District> Regionen = connector.getDistricts();;
		return Regionen;
	}


}
