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

import de.cses.shared.PreservationAttributeEntry;

/**
 * @author alingnau
 *
 */
public interface DepictionViewTemplates extends XTemplates {
	
	@XTemplate(source = "DepictionDisplay.html")
	SafeHtml display(String shortName, String inventoryNumber, String cave, String expedition, String vendor, String purchaseDate, String currentLocation, ArrayList<PreservationAttributeEntry> stateOfPreservation, SafeUri imageUri);

}
