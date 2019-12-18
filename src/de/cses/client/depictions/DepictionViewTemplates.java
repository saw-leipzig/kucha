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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.client.ui.TextElement;
import de.cses.shared.AnnotatedBibliographyEntry;

/**
 * @author alingnau
 *
 */
public interface DepictionViewTemplates extends XTemplates {
	
	@XTemplate(source = "DepictionDisplay.html")
	SafeHtml display(String shortName, String inventoryNumber, String cave, String wall, String expedition, String vendor, String purchaseDate, String currentLocation, String stateOfPreservation, SafeUri imageUri, SafeUri fullImageUri,
			SafeUri realCaveSketchUri, double width, double height, String style, String modeOfRepresentation, String description, String generalRemarks, String otherSuggestedIdentifications, 
			ArrayList<TextElement> iconography, ArrayList<TextElement> pictorialElements, ArrayList<TextElement> decorationOrnaments, ArrayList<AnnotatedBibliographyEntry> bib, String user, String timestamp);
	@XTemplate(source = "DepictionDisplay_oben.html")
	SafeHtml displayoben(String shortName, String inventoryNumber, String cave, String wall, String expedition, String vendor, String purchaseDate, String currentLocation, String stateOfPreservation, SafeUri imageUri, SafeUri fullImageUri,
			SafeUri realCaveSketchUri, double width, double height, String style, String modeOfRepresentation, String description, String generalRemarks, String otherSuggestedIdentifications); 
	@XTemplate(source = "DepictionDisplay_unten.html")
	SafeHtml displayunten( ArrayList<AnnotatedBibliographyEntry> bib, String user, String timestamp, String description, String generalRemarks, String otherSuggestedIdentifications);
				}
