package de.cses.shared;

public class PositionEntry extends AbstractEntry {

	private int positionID;
	private String name;

	public PositionEntry(int positionID, String name) {
		this.positionID = positionID;
		this.name = name;
	}

	public PositionEntry() {
		this(-1, "");
	}

	public int getPositionID() {
		return positionID;
	}

	public void setpositionID(int ornamentPositionID) {
		this.positionID = ornamentPositionID;
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
		return "OrnamentPositionEntry" + positionID;
	}

}
