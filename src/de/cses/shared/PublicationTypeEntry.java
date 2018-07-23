/*
 * Copyright 2017 - 2018
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.shared;

/**
 * @author Nina
 *
 */
public class PublicationTypeEntry extends AbstractEntry {
	

	int publicationTypeID = 0;
	String name;
	boolean accessDateEnabled = false;
	boolean authorEnabled = false;
	boolean parentTitleEnabled = false;
	String parentTitleLabel;
	boolean editionEnabled = false;
	boolean editorEnabled = false;
	boolean monthEnabled = false;
	boolean numberEnabled = false;
	boolean pagesEnabled = false;
	boolean seriesEnabled = false;
	boolean titleAddonEnabled = false;
	String titleAddonLabel;
	boolean universityEnabled = false;
	boolean volumeEnabled = false;
	boolean issueEnabled = false;
	boolean yearEnabled = false;
	boolean thesisTypeEnabled = false;
	
	/**
	 * @return the publicationTypeID
	 */
	
	public PublicationTypeEntry() {	}

	/**
	 * @param publicationTypeID
	 * @param name
	 * @param accessDateEnabled
	 * @param authorEnabled
	 * @param parentTitleEnabled
	 * @param parentTitleLabel
	 * @param editionEnabled
	 * @param editorEnabled
	 * @param monthEnabled
	 * @param numberEnabled
	 * @param pagesEnabled
	 * @param seriesEnabled
	 * @param titleAddonEnabled
	 * @param universityEnabled
	 * @param volumeEnabled
	 * @param issueEnabled
	 * @param yearEnabled
	 * @param thesisTypeEnabled
	 */
	public PublicationTypeEntry(int publicationTypeID, String name, boolean accessDateEnabled, boolean authorEnabled,
			boolean parentTitleEnabled, String parentTitleLabel, boolean editionEnabled, boolean editorEnabled, boolean monthEnabled,
			boolean numberEnabled, boolean pagesEnabled, boolean seriesEnabled, boolean titleAddonEnabled, String titleAddonLabel, boolean universityEnabled,
			boolean volumeEnabled, boolean issueEnabled, boolean yearEnabled, boolean thesisTypeEnabled) {
		this.publicationTypeID = publicationTypeID;
		this.name = name;
		this.accessDateEnabled = accessDateEnabled;
		this.authorEnabled = authorEnabled;
		this.parentTitleEnabled = parentTitleEnabled;
		this.parentTitleLabel = parentTitleLabel;
		this.editionEnabled = editionEnabled;
		this.editorEnabled = editorEnabled;
		this.monthEnabled = monthEnabled;
		this.numberEnabled = numberEnabled;
		this.pagesEnabled = pagesEnabled;
		this.seriesEnabled = seriesEnabled;
		this.titleAddonEnabled = titleAddonEnabled;
		this.titleAddonLabel = titleAddonLabel;
		this.universityEnabled = universityEnabled;
		this.volumeEnabled = volumeEnabled;
		this.issueEnabled = issueEnabled;
		this.yearEnabled = yearEnabled;
		this.thesisTypeEnabled = thesisTypeEnabled;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * @return the publicationTypeID
	 */
	public int getPublicationTypeID() {
		return publicationTypeID;
	}
	/**
	 * @param publicationTypeID the publicationTypeID to set
	 */
	public void setPublicationTypeID(int publicationTypeID) {
		this.publicationTypeID = publicationTypeID;
	}

	public boolean isAccessDateEnabled() {
		return accessDateEnabled;
	}

	public void setAccessDateEnabled(boolean accessDateEnabled) {
		this.accessDateEnabled = accessDateEnabled;
	}

	public boolean isEditionEnabled() {
		return editionEnabled;
	}

	public void setEditionEnabled(boolean editionEnabled) {
		this.editionEnabled = editionEnabled;
	}

	public boolean isMonthEnabled() {
		return monthEnabled;
	}

	public void setMonthEnabled(boolean monthEnabled) {
		this.monthEnabled = monthEnabled;
	}

	public boolean isNumberEnabled() {
		return numberEnabled;
	}

	public void setNumberEnabled(boolean numberEnabled) {
		this.numberEnabled = numberEnabled;
	}

	public boolean isPagesEnabled() {
		return pagesEnabled;
	}

	public void setPagesEnabled(boolean pagesEnabled) {
		this.pagesEnabled = pagesEnabled;
	}

	public boolean isSeriesEnabled() {
		return seriesEnabled;
	}

	public void setSeriesEnabled(boolean seriesEnabled) {
		this.seriesEnabled = seriesEnabled;
	}

	public boolean isTitleAddonEnabled() {
		return titleAddonEnabled;
	}

	public void setTitleAddonEnabled(boolean titleAddonEnabled) {
		this.titleAddonEnabled = titleAddonEnabled;
	}

	public boolean isUniversityEnabled() {
		return universityEnabled;
	}

	public void setUniversityEnabled(boolean universityEnabled) {
		this.universityEnabled = universityEnabled;
	}

	public boolean isVolumeEnabled() {
		return volumeEnabled;
	}

	public void setVolumeEnabled(boolean volumeEnabled) {
		this.volumeEnabled = volumeEnabled;
	}

	public boolean isYearEnabled() {
		return yearEnabled;
	}

	public void setYearEnabled(boolean yearEnabled) {
		this.yearEnabled = yearEnabled;
	}

	public boolean isAuthorEnabled() {
		return authorEnabled;
	}

	public void setAuthorEnabled(boolean authorEnabled) {
		this.authorEnabled = authorEnabled;
	}

	public boolean isEditorEnabled() {
		return editorEnabled;
	}

	public void setEditorEnabled(boolean editorEnabled) {
		this.editorEnabled = editorEnabled;
	}

	public boolean isIssueEnabled() {
		return issueEnabled;
	}

	public void setIssueEnabled(boolean issueEnabled) {
		this.issueEnabled = issueEnabled;
	}

	public boolean isParentTitleEnabled() {
		return parentTitleEnabled;
	}

	public void setParentTitleEnabled(boolean parentTitleEnabled) {
		this.parentTitleEnabled = parentTitleEnabled;
	}

	public String getParentTitleLabel() {
		return parentTitleLabel;
	}

	public void setParentTitleLabel(String parentTitleLabel) {
		this.parentTitleLabel = parentTitleLabel;
	}

	public boolean isThesisTypeEnabled() {
		return thesisTypeEnabled;
	}

	public void setThesisTypeEnabled(boolean thesisTypeEnabled) {
		this.thesisTypeEnabled = thesisTypeEnabled;
	}

	public String getTitleAddonLabel() {
		return titleAddonLabel;
	}

	public void setTitleAddonLabel(String titleAddonLabel) {
		this.titleAddonLabel = titleAddonLabel;
	}
	
}
