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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.DepictionEntry;

/**
 * @author alingnau
 *
 */
public class DepictionDataDisplay extends AbstractDataDisplay {
	

	/**
	 * 
	 */
	public DepictionDataDisplay(DepictionEntry e) {
		super();
		String cave = "";
		DepictionViewTemplates view = GWT.create(DepictionViewTemplates.class);
		if (e.getCave() != null) {
			if (e.getCave().getSiteID() > 0) {
				cave += StaticTables.getInstance().getSiteEntries().get(e.getCave().getSiteID()).getShortName() + ": ";
			}
			cave += e.getCave().getOfficialNumber() + ((e.getCave().getHistoricName() != null && e.getCave().getHistoricName().length() > 0) ? e.getCave().getHistoricName() : ""); 
		}
		String expedition = e.getExpeditionID() > 0 ? StaticTables.getInstance().getExpeditionEntries().get(e.getExpeditionID()).getName() : "";
		String vendor = e.getVendorID() > 0 ? StaticTables.getInstance().getVendorEntries().get(e.getVendorID()).getVendorName() : "";
		String location = e.getLocationID() > 0 ? StaticTables.getInstance().getLocationEntries().get(e.getLocationID()).getName() : "";
		String date = e.getPurchaseDate() != null ? e.getPurchaseDate().toString() : "";
		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + e.getMasterImageID() + "&thumb=80" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		add(new HTML(view.display(
				e.getShortName() != null ? e.getShortName() : "", 
				e.getInventoryNumber(), 
				cave,
				expedition, 
				vendor, 
				date, 
				location, 
				e.getPreservationAttributesList(), 
				imageUri)));
//		setSize("100%", "300");
	}

}
