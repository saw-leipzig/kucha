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

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author nina
 *
 */
public class GameEntry implements IsSerializable {
	
	private int gameID;
	private String name;
	private String description;
	private String imageID;
	private ArrayList<AnnotationEntry> annotations;
	private Boolean is18;
	private Integer difficulty;
	
	public GameEntry(int gameID, String name, String imageID, ArrayList<AnnotationEntry> annotations, String description, Boolean is18, Integer difficulty){
			this.gameID = gameID;
			this.name = name;
			this.imageID = imageID;
			this.annotations = annotations;
			this.description = description;
			this.is18 = is18;
			this.difficulty = difficulty;
	}
}
