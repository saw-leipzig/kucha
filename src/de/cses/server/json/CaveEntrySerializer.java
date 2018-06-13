/*
 * Copyright 2018 
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
package de.cses.server.json;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public class CaveEntrySerializer implements JsonSerializer<CaveEntry> {

	/**
	 * 
	 */
	public CaveEntrySerializer() { }

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(CaveEntry ce, Type typeOfSource, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		jsonObj.addProperty("caveID", ce.getCaveID());
		jsonObj.addProperty("officialNumber", ce.getOfficialNumber());
		jsonObj.addProperty("historicalName", ce.getHistoricName());
		jsonObj.addProperty("optionalHistoricalName", ce.getOptionalHistoricName());
		jsonObj.addProperty("caveTypeID", ce.getCaveTypeID());
		jsonObj.addProperty("siteID", ce.getSiteID());
		jsonObj.addProperty("districtID", ce.getDistrictID());
		jsonObj.addProperty("regionID", ce.getRegionID());
		jsonObj.addProperty("optionalCaveSketch", ce.getOptionalCaveSketch());
		return jsonObj;
	}
	
}
