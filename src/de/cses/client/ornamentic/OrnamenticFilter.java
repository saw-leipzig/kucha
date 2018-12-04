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
package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticFilter  extends AbstractFilter{
	private TextField ornamentCodeSearchTF;

	public OrnamenticFilter(String filterName) {
		super(filterName);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {

		ornamentCodeSearchTF = new TextField();
		ornamentCodeSearchTF.setEmptyText("search ornament code");
		ornamentCodeSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));

		// Example - add the rest here

		VerticalLayoutContainer ornamenticFilterVLC = new VerticalLayoutContainer();
		ornamenticFilterVLC.add(ornamentCodeSearchTF, new VerticalLayoutData(1.0, .25));
		ornamenticFilterVLC.setHeight("120px");
		return ornamenticFilterVLC;
	}

	/**
	 * In this method we assemble the search entry from the fields in the filter. Here we create the search entry 
	 * that will be send to the server.
	 */
	@Override
	public AbstractSearchEntry getSearchEntry() {
		OrnamenticSearchEntry searchEntry = new OrnamenticSearchEntry();
		
		if (ornamentCodeSearchTF.getValue() != null && !ornamentCodeSearchTF.getValue().isEmpty()) {
			searchEntry.setSearchOrnamentCode(ornamentCodeSearchTF.getValue());
		}
		
		// @nina: siehe auch getSearchEntry() z.B. in DepictionFilter
		
		return searchEntry;
	}

}
