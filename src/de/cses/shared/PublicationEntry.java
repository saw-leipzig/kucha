/*
 * Copyright 2016 
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

import java.sql.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author alingnau
 *
 */
public class PublicationEntry implements IsSerializable {

	/**
	 * SELECT `PublicationID`, `Editors`, `Type`, `DOI`, `Pages`, `Year`,
	 * `PublisherID`, `Titel.English`, `Titel.Phonetic`, `Titel.Original`,
	 * `Abstract`, `DescriptionOfPicsTables` FROM `Publications`
	 */

	private int publicationID;
	private String editors;
	private String type;
	private String doi;
	private String pages;
	private Date year;
	private int publisherID;
	private String titleEN;
	private String titlePhonetic;
	private String titleOriginal;
	private String paperAbstract;
	
	// not sure yet whether the annotated documents should be stored in a relation table of if there should only be one PDF uploaded

	/**
	 * 
	 */
	public PublicationEntry() {
		// TODO Auto-generated constructor stub
	}

	public PublicationEntry(int publicationID, String editors, String type, String doi, String pages, Date year, int publisherID,
			String titleEN, String titlePhonetic, String titleOriginal, String paperAbstract) {
		super();
		this.publicationID = publicationID;
		this.editors = editors;
		this.type = type;
		this.doi = doi;
		this.pages = pages;
		this.year = year;
		this.publisherID = publisherID;
		this.titleEN = titleEN;
		this.titlePhonetic = titlePhonetic;
		this.titleOriginal = titleOriginal;
		this.paperAbstract = paperAbstract;
	}

	public int getPublicationID() {
		return publicationID;
	}

	public void setPublicationID(int publicationID) {
		this.publicationID = publicationID;
	}

	public String getEditors() {
		return editors;
	}

	public void setEditors(String editors) {
		this.editors = editors;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDoi() {
		return doi;
	}

	public void setDoi(String doi) {
		this.doi = doi;
	}

	public String getPages() {
		return pages;
	}

	public void setPages(String pages) {
		this.pages = pages;
	}

	public Date getYear() {
		return year;
	}

	public void setYear(Date year) {
		this.year = year;
	}

	public int getPublisherID() {
		return publisherID;
	}

	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}

	public String getTitleEN() {
		return titleEN;
	}

	public void setTitleEN(String titleEN) {
		this.titleEN = titleEN;
	}

	public String getTitlePhonetic() {
		return titlePhonetic;
	}

	public void setTitlePhonetic(String titlePhonetic) {
		this.titlePhonetic = titlePhonetic;
	}

	public String getTitleOriginal() {
		return titleOriginal;
	}

	public void setTitleOriginal(String titleOriginal) {
		this.titleOriginal = titleOriginal;
	}

	public String getPaperAbstract() {
		return paperAbstract;
	}

	public void setPaperAbstract(String paperAbstract) {
		this.paperAbstract = paperAbstract;
	}


}
