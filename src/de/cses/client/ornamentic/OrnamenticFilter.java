/*
 * Copyright 2017, 2018
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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticFilter  extends AbstractFilter{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField ornamentCodeSearchTF;
	private ListStore<OrnamentClassEntry> ornamentClassEntryList;
	private OrnamentClassProperties ornamentClassProps;
	private ListView<OrnamentClassEntry, OrnamentClassEntry> ornamentClassSelectionLV;
	
	interface OrnamentClassProperties extends PropertyAccess<OrnamentClassEntry> {
		ModelKeyProvider<OrnamentClassEntry> ornamentClassID();
		LabelProvider<OrnamentClassEntry> uniqueID();
		ValueProvider<OrnamentClassEntry, String> name();
	}

	interface OrnamentClassViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentClassLabel(String name);
	}
	
	
	public OrnamenticFilter(String filterName) {
		super(filterName);
		
		ornamentClassProps = GWT.create(OrnamentClassProperties.class);
		ornamentClassEntryList = new ListStore<OrnamentClassEntry>(ornamentClassProps.ornamentClassID());
		loadOrnamentClassEntryList();
	}

	private void loadOrnamentClassEntryList() {
		dbService.getOrnamentClass(new AsyncCallback<ArrayList<OrnamentClassEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentClassEntry> result) {
				ornamentClassEntryList.clear();
				for (OrnamentClassEntry pe : result) {
					ornamentClassEntryList.add(pe);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {

		ornamentCodeSearchTF = new TextField();
		ornamentCodeSearchTF.setEmptyText("search ornament code");
		ornamentCodeSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));

		ornamentClassSelectionLV = new ListView<OrnamentClassEntry, OrnamentClassEntry>(ornamentClassEntryList, new IdentityValueProvider<OrnamentClassEntry>(), 
				new SimpleSafeHtmlCell<OrnamentClassEntry>(new AbstractSafeHtmlRenderer<OrnamentClassEntry>() {
			final OrnamentClassViewTemplates ocvTemplates = GWT.create(OrnamentClassViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentClassEntry entry) {
				return ocvTemplates.ornamentClassLabel(entry.getName());
			}
			
		}));
		ornamentClassSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentClassPanel = new ContentPanel();
		ornamentClassPanel.setHeaderVisible(true);
		ornamentClassPanel.setToolTip(Util.createToolTip("Search for ornament class."));
		ornamentClassPanel.setHeading("Ornament Class");
		ornamentClassPanel.add(ornamentClassSelectionLV);

		// add more here

		VerticalLayoutContainer ornamenticFilterVLC = new VerticalLayoutContainer();
		ornamenticFilterVLC.add(ornamentCodeSearchTF, new VerticalLayoutData(1.0, .15));
		ornamenticFilterVLC.add(ornamentClassPanel, new VerticalLayoutData(1.0, .85));
		ornamenticFilterVLC.setHeight("300px");
		
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
		
		if (!ornamentClassSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentClassEntry oce : ornamentClassSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getSearchOrnamenClassIdList().add(oce.getOrnamentClassID());
			}
		}
		
		// @nina: siehe auch getSearchEntry() z.B. in DepictionFilter
		
		return searchEntry;
	}

}
