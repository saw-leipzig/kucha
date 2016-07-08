package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import de.cses.shared.District;
import de.cses.shared.ImageEntry;

public interface DatabaseServiceAsync {

	void dbServer(String name, AsyncCallback<String> callback) throws IllegalArgumentException;
	void getDistricts(AsyncCallback<ArrayList<District>> callback) throws IllegalArgumentException;
	void getImages(AsyncCallback<ArrayList<ImageEntry>> callback) throws IllegalArgumentException;
	
}
