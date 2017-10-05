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
package de.cses.server;

import java.util.Date;
import java.util.HashMap;

import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public class UserManager {
	
	private static UserManager instance = null;
	private HashMap<String, UserEntry> userMap = new HashMap<String, UserEntry>();

	/**
	 * 
	 */
	private UserManager() {	}

	public static synchronized UserManager getInstance() {
		if (instance  == null) {
			instance = new UserManager();
		}
		return instance;
	}
	
	public void addUser(String username, UserEntry user) {
		userMap.put(username, user);
	}

	public String getSessionID(String username) {
		UserEntry user = userMap.get(username);
		Date now = new Date();
		long diffDays = (now.getTime() - user.getLoginDate()) / (24 * 60 * 60 * 1000);
		if (diffDays > 1) {
			userMap.remove(username);
			return null;
		}
		return user.getSessionID();
	}

}
