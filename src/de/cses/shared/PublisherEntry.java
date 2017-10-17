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
public class PublisherEntry {
	
	private int publisherID;
	private String name;
	private String adress;
	
	
	/**
	 * @param publisherID
	 * @param name
	 * @param adress
	 */
	public PublisherEntry(int publisherID, String name, String adress) {
		super();
		this.setPublisherID(publisherID);
		this.name = name;
		this.adress = adress;
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
	 * @return the adress
	 */
	public String getAdress() {
		return adress;
	}
	/**
	 * @param adress the adress to set
	 */
	public void setAdress(String adress) {
		this.adress = adress;
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

	
}
