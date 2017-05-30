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
public class PreservationClassificationEntry implements IsSerializable {
	
	private int preservationClassificationID;
	private String name;
	
	public PreservationClassificationEntry() { }
	
	/**
	 * @param preservationClassificationID
	 * @param name
	 */
	public PreservationClassificationEntry(int classificationID, String text) {
		super();
		this.preservationClassificationID = classificationID;
		this.name = text;
	}

	public int getPreservationClassificationID() {
		return preservationClassificationID;
	}
	
	public void setPreservationClassificationID(int classificationID) {
		this.preservationClassificationID = classificationID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setText(String text) {
		this.name = text;
	}
	
}
