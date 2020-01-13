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

import java.util.Arrays;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author alingnau
 *
 */
public abstract class AbstractEntry implements IsSerializable {
	
	public static final int ACCESS_LEVEL_PRIVATE = 0;
	public static final int ACCESS_LEVEL_COPYRIGHT = 1;
	public static final int ACCESS_LEVEL_PUBLIC = 2;
	public static final List<String> ACCESS_LEVEL_LABEL = Arrays.asList("private", "copyright", "public");
	
	protected int accessLevel = ACCESS_LEVEL_PRIVATE;
	protected String lastChangedByUser = "";
	protected String modifiedOn = "";
	protected boolean deleted = false;

	/**
	 * 
	 * @param accessLevel
	 */
	public AbstractEntry(int accessLevel) {
		super();
		this.accessLevel = accessLevel;
	}

	public AbstractEntry() { }

	/**
	 * This method will deliver a unique identifier to make the entry information 
	 * distinguishable throughout the graphic UI
	 * @return a unique ID for the specific entry data
	 */
	abstract public String getUniqueID();
	
		public String getLastChangedByUser() {
		return lastChangedByUser;
	}

	public void setLastChangedByUser(String lastChangedByUser) {
		this.lastChangedByUser = lastChangedByUser;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

	public int getAccessLevel() {
		return accessLevel;
	}
	public boolean isdeleted() {
		return deleted;
	}

	public void setAccessLevel(int accessLevel) {
		this.accessLevel = accessLevel;
	}

}
