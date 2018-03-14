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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author alingnau
 *
 */
public class OrientationEntry implements IsSerializable {
	
	private int orientationID;
	private String name;

	/**
	 * 
	 */
	public OrientationEntry() {
	}

	public OrientationEntry(int orientatiationID, String name) {
		super();
		this.orientationID = orientatiationID;
		this.name = name;
	}

	public int getOrientationID() {
		return orientationID;
	}

	public void setOrientationID(int orientatiationID) {
		this.orientationID = orientatiationID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
