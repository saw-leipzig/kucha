package de.cses.shared;

public class ModifiedEntry extends AbstractEntry {

	private int modifiedEntryID = 0;
	private String entryID, modifiedBy, modifiedOn, annoID, tags;

	public ModifiedEntry() {	}

	public ModifiedEntry(int ModifiedEntryID, String EntryID, String modifiedBy, String modifiedOn) {
		this.modifiedEntryID = ModifiedEntryID;
		this.entryID = EntryID;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
	}
	public ModifiedEntry(int ModifiedEntryID, String EntryID, String modifiedBy, String modifiedOn, String annoID, String tags) {
		this.modifiedEntryID = ModifiedEntryID;
		this.entryID = EntryID;
		this.modifiedBy = modifiedBy;
		this.modifiedOn = modifiedOn;
		this.annoID = annoID;
		this.tags = tags;
	}
	
	public int getModifiedEntryID() {
		return modifiedEntryID;
	}

	public void setAuthorID(int modifiedEntryID) {
		this.modifiedEntryID = modifiedEntryID;
	}

	public String getEntryID() {
		return entryID;
	}

	public void setEntryID(String entryID) {
		this.entryID = entryID;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}
	public String getAnnoID() {
		return annoID;
	}

	public void setAnnoID(String annoID) {
		this.annoID = annoID;
	}
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	@Override
	public String getUniqueID() {
		return "Modified-" + modifiedEntryID;
	}
}
