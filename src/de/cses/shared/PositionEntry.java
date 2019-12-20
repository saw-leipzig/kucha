package de.cses.shared;

public class PositionEntry extends AbstractEntry {

	private int ornamentPositionID;
	private String name;

	public PositionEntry(int positionID, String name) {
		this.ornamentPositionID = positionID;
		this.name = name;
	}

	public PositionEntry() {
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
