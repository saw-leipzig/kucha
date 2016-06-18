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

	@Override
	public ArrayList<District> getDistricts() throws IllegalArgumentException {
		// TODO Auto-generated method stub
		MysqlConnector connector = MysqlConnector.getInstance();
		ArrayList<District> Regionen = connector.getDistricts();;
		return Regionen;
	}


}
