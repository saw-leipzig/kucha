package de.cses.shared;

public class CollectionEntry extends AbstractEntry {
	
	private int collectionID = 0;
	private UserEntry user;
	private String collectionName = "";
	private boolean isGroupCollection;

	public CollectionEntry() { }

	/**
	 * @param accessLevel
	 * @param collectionID
	 * @param userID
	 * @param collectionName
	 * @param isGroupCollection
	 */
	public CollectionEntry(int collectionID, UserEntry user, String collectionLabel, boolean isGroupCollection) {
		this.collectionID = collectionID;
		this.user = user;
		this.collectionName = collectionLabel;
		this.isGroupCollection = isGroupCollection;
	}


	@Override
	public String getUniqueID() {
		return "Collection-" + collectionID;
	}

	public int getCollectionID() {
		return collectionID;
	}

	public void setCollectionID(int collectionID) {
		this.collectionID = collectionID;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public boolean isGroupCollection() {
		return isGroupCollection;
	}

	public void setGroupCollection(boolean isGroupCollection) {
		this.isGroupCollection = isGroupCollection;
	}

	public UserEntry getUser() {
		return user;
	}

	public void setUser(UserEntry user) {
		this.user = user;
	}
	
}
