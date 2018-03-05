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
 * @author Nina
 *
 */
public class PublisherEntry extends AbstractEntry {
	
	private int publisherID = 0;
	private String name;
	private String location;
	
	/**
	 * 
	 */
	public PublisherEntry() {	}
	
	/**
	 * @param publisherID
	 * @param name
	 * @param location
	 */
	public PublisherEntry(int publisherID, String name, String location) {
		super();
		this.setPublisherID(publisherID);
		this.name = name;
		this.location = location;
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
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}
	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	/**
	 * @return the publisherID
	 */
	public int getPublisherID() {
		return publisherID;
	}
	/**
	 * @param publisherID the publisherID to set
	 */
	public void setPublisherID(int publisherID) {
		this.publisherID = publisherID;
	}
	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Publisher-" + publisherID;
	}
	
	public String getLabel() {
		return name + " (" + location + ")";
	}
	
}
