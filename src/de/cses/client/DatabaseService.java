package de.cses.client;

import java.util.ArrayList;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import de.cses.shared.District;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("db")
public interface DatabaseService extends RemoteService {
	String dbServer(String name) throws IllegalArgumentException;
	ArrayList<District> getDistricts() throws IllegalArgumentException;
	
}
