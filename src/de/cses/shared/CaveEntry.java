package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CaveEntry implements IsSerializable {
	private int caveID;
	private String name;

	public CaveEntry() {
	}

	public CaveEntry(int caveID, String name) {
		super();
		this.caveID = caveID;
		this.name = name;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	


}
