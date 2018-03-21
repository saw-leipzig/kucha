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
public abstract class AbstractEntry implements IsSerializable {
	
	protected boolean openAccess = false;
	
	/**
	 * @param openAccess
	 */
	public AbstractEntry(boolean openAccess) {
		super();
		this.openAccess = openAccess;
	}

	public AbstractEntry() { }

	/**
	 * This method will deliver a unique identifier to make the entry information 
	 * distinguishable throughout the graphic UI
	 * @return a unique ID for the specific entry data
	 */
	abstract public String getUniqueID();
	
	public boolean isOpenAccess() {
		return openAccess;
	}
	
	public void setOpenAccess(boolean openAccess) {
		this.openAccess = openAccess;
	}

}
