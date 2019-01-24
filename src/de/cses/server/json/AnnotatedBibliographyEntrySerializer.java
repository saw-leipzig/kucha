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
import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

/**
 * @author alingnau
 *
 */
public class AnnotatedBibliographyEntrySerializer implements JsonSerializer<AnnotatedBibliographyEntry> {

	/**
	 * 
	 */
	public AnnotatedBibliographyEntrySerializer() { }

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(AnnotatedBibliographyEntry abe, Type typeOfSource, JsonSerializationContext context) {
		JsonObject jsonObj = new JsonObject();
		if (!abe.getAuthorList().isEmpty()) {
			jsonObj.addProperty("authors", getAuthors(abe.getAuthorList()));
		}
		if (!abe.getEditorList().isEmpty()) {
			jsonObj.addProperty("editors", getAuthors(abe.getEditorList()));
		}
		jsonObj.addProperty("year", abe.getYearORG());
		jsonObj.addProperty("title", abe.getTitleFull());
		jsonObj.addProperty("type", abe.getPublicationType().getName());
		jsonObj.addProperty("in", abe.getParentTitleORG());
		jsonObj.addProperty("edition", abe.getEditionORG());
		jsonObj.addProperty("number", abe.getNumberORG());
		jsonObj.addProperty("publisher", abe.getPublisher());
		jsonObj.addProperty("pages", abe.getPagesORG());
		return jsonObj;
	}
	
	private String getAuthors(ArrayList<AuthorEntry> authorList) {
		String result = "";
		for (AuthorEntry ae : authorList) {
			result+= result.isEmpty() ? ae.getName() : "; " + ae.getName();
		}
		return result;
	}
	
}
