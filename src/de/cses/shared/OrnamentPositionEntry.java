package de.cses.shared;

public class OrnamentPositionEntry extends AbstractEntry {

	private int ornamentPositionID;
	private String name;

	public OrnamentPositionEntry(int positionID, String name) {
		this.ornamentPositionID = positionID;
		this.name = name;
	}

	public OrnamentPositionEntry() {
		this(0, "");
	}

	public int getOrnamentPositionID() {
		return ornamentPositionID;
	}

	public void setOrnamentPositionID(int ornamentPositionID) {
		this.ornamentPositionID = ornamentPositionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamentPositionEntry" + ornamentPositionID;
	}

}
