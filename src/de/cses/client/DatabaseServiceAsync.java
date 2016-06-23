package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.shared.District;

public interface DatabaseServiceAsync {

	void dbServer(String name, AsyncCallback<String> callback) throws IllegalArgumentException;;
	void  getDistricts(AsyncCallback<ArrayList<District>> callback) throws IllegalArgumentException;;

}
