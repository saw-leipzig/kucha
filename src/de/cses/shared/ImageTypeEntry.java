/*
 * Copyright 2017 
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
 * @author alingnau
 *
 */
public class ImageTypeEntry extends AbstractEntry {
	
	private int imageTypeID;
	private String name;

	/**
	 * 
	 */
	public ImageTypeEntry() {
		this(0,"");
	}
	
	/**
	 * @param imageTypeID
	 * @param name
	 */
	public ImageTypeEntry(int imageTypeID, String name) {
		super();
		this.imageTypeID = imageTypeID;
		this.name = name;
	}



	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getInsertSql()
	 */
	@Override
	public String getInsertSql() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUpdateSql()
	 */
	@Override
	public String getUpdateSql() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#uniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "ImageType-" + imageTypeID;
	}

	/**
	 * @return the imageTypeID
	 */
	public int getImageTypeID() {
		return imageTypeID;
	}

	/**
	 * @param imageTypeID the imageTypeID to set
	 */
	public void setImageTypeID(int imageTypeID) {
		this.imageTypeID = imageTypeID;
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

}
