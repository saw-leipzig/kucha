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

public class ImageEntry extends AbstractEntry {

	private int imageID, photographerID;
	private String copyright, comment, filename, title, shortName, type, date;

	public static final int FILENAME = 2;

	public ImageEntry() {
		imageID = 0;
		photographerID = 0;
	}
	
	/**
	 * 
	 * @param imageID
	 * @param filename
	 * @param title
	 * @param copyright
	 * @param photographerID
	 * @param comment
	 * @param captureDate
	 */
	public ImageEntry(int imageID, String filename, String title, String shortName, String copyright,
			int photographerID, String comment, String type, String date) {
		this.imageID = imageID;
		this.filename = filename;
		this.title = title;
		this.shortName = shortName;
		this.copyright = copyright;
		this.photographerID = photographerID;
		this.comment = comment;
		this.type = type;
		this.date = date;
	}

	public int getImageID() {
		return imageID;
	}

	public void setImageID(int imageID) {
		this.imageID = imageID;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPhotographerID() {
		return photographerID;
	}

	public void setPhotographerID(int photographerID) {
		this.photographerID = photographerID;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @param imageID
	 * @return String with the SQL UPDATE command for this ImageEntry
	 */
	public String getSingleFieldUpdateSql(int id) {
		switch (id) {
		case FILENAME:
			return "UPDATE Images SET Filename='" + filename + "' WHERE ImageID=" + imageID;
		default:
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		return "INSERT INTO Images (Filename, Title, ShortName, Copyright, PhotographerID, Comment, Date, ImageType) VALUES ('" + filename + "', '" + title + "', '" + shortName + "', '" + copyright + "'," 
				+ photographerID + ", '" + comment + "', '" + date + "', '" + type + "')";
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUpdateSql()
	 */
	@Override
	public String getUpdateSql() {
		return "UPDATE Images SET Title='" + title + "', ShortName='" + shortName + "', Copyright='" + copyright + "', PhotographerID=" + photographerID + ", Comment='" + comment 
				+ "', Date='" + date + "', ImageType='" + type + "'  WHERE ImageID=" + imageID;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * @param shortName the shortName to set
	 */
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
}
