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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

/**
 * @author alingnau
 *
 */
public class ServerProperties extends Properties {

	private static ServerProperties instance = null;

	/**
	 * 
	 */
	public ServerProperties() {
		File f = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
		String propertyFileName = System.getProperty("user.dir") + "/lib/" + f.getParentFile().getParentFile().getName() + ".xml";
		System.out.println(propertyFileName);
		try {
			loadFromXML(new FileInputStream(propertyFileName));
		} catch (InvalidPropertiesFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't find file: " + propertyFileName);
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * By calling this method, we avoid that a new instance will be created if
	 * there is already one existing. If this method is called without an instance
	 * existing, one will be created.
	 * 
	 * @return
	 */
	public static synchronized ServerProperties getInstance() {
		if (instance  == null) {
			instance = new ServerProperties();
		}
		return instance;
	}
}
