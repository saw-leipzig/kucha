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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HTML;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.shared.DepictionEntry;

/**
 * @author alingnau
 *
 */
public class DepictionDataDisplay extends AbstractDataDisplay {
	
	DepictionViewTemplates view = GWT.create(DepictionViewTemplates.class);

	/**
	 * 
	 */
	public DepictionDataDisplay(DepictionEntry e) {
		super();
		add(new HTML(view.display(
				e.getShortName() != null ? e.getShortName() : "", 
				e.getInventoryNumber(), 
				e.getCave(), 
				StaticTables.getInstance().getSiteEntries().get(e.getCave().getSiteID()).getShortName(), 
				StaticTables.getInstance().getExpeditionEntries().get(e.getExpeditionID()), 
				StaticTables.getInstance().getVendorEntries().get(e.getVendorID()), 
				e.getPurchaseDate().toString(), 
				StaticTables.getInstance().getLocationEntries().get(e.getLocationID()), 
				e.getPreservationAttributesList())));
		setSize("300", "300");
	}

}
