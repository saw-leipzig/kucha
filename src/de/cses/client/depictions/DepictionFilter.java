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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
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
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class DepictionFilter extends AbstractFilter {

	interface LocationProperties extends PropertyAccess<LocationEntry> {
		ModelKeyProvider<LocationEntry> locationID();
		LabelProvider<LocationEntry> name();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{officialNumber}: {officialName}<br>{siteDistrictInformation}</div>")
		SafeHtml caveLabel(String siteDistrictInformation, String officialNumber, String officialName);

		@XTemplate("<div style=\"border: 1px solid grey;\">{officialNumber}<br>{siteDistrictInformation}</div>")
		SafeHtml caveLabel(String siteDistrictInformation, String officialNumber);
	}

	interface LocationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}<br>{town}, {country}</div>")
		SafeHtml caveLabel(String name, String town, String country);

		@XTemplate("<div>{name}<br>{country}</div>")
		SafeHtml caveLabel(String name, String country);

		@XTemplate("<div>{name}</div>")
		SafeHtml caveLabel(String name);
	}

	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> officialNumber();
	}
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private TextField shortNameSearch;
	private CaveProperties caveProps;
	private ListStore<CaveEntry> caveEntryLS;
	private ListView<CaveEntry, CaveEntry> caveSelectionLV;
	private ListView<LocationEntry, LocationEntry> locationSelectionLV;

	/**
	 * @param filterName
	 */
	public DepictionFilter(String filterName) {
		super(filterName);
		caveProps = GWT.create(CaveProperties.class);
		caveEntryLS = new ListStore<CaveEntry>(caveProps.caveID());
		loadCaves();
	}

	private void loadCaves() {
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> caveResults) {
				for (CaveEntry ce : caveResults) {
					caveEntryLS.add(ce);
				}
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {

		caveSelectionLV = new ListView<CaveEntry, CaveEntry>(caveEntryLS, new IdentityValueProvider<CaveEntry>(), new SimpleSafeHtmlCell<CaveEntry>(new AbstractSafeHtmlRenderer<CaveEntry>() {
			
			@Override
			public SafeHtml render(CaveEntry entry) {
				final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
				StaticTables st = StaticTables.getInstance();
				DistrictEntry de = null;
				SiteEntry se = null;
				de = st.getDistrictEntries().get(entry.getDistrictID());
				if (de != null) {
					se = st.getSiteEntries().get(de.getSiteID());
				}
				String siteDistrictInformation = (se != null ? se.getName() : "") + (de != null ? (se != null ? " / " : "") + de.getName() : "");
				if ((entry.getHistoricName() != null) && (entry.getHistoricName().length() > 0)) {
					return cvTemplates.caveLabel(siteDistrictInformation, entry.getOfficialNumber(), entry.getHistoricName());
				} else {
					return cvTemplates.caveLabel(siteDistrictInformation, entry.getOfficialNumber());
				}
			}
		}));
		caveSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel cavePanel = new ContentPanel();
		cavePanel.setHeaderVisible(true);
		cavePanel.setHeading("Cave search");
		cavePanel.add(caveSelectionLV);
		
		shortNameSearch = new TextField();
		shortNameSearch.setEmptyText("search short name");

		ContentPanel shortNamePanel = new ContentPanel();
		shortNamePanel.setHeaderVisible(true);
		shortNamePanel.setHeading("Shortname search");
		shortNamePanel.add(shortNameSearch);		

		LocationProperties locationProps = GWT.create(LocationProperties.class);
		ListStore<LocationEntry> 		locationEntryLS = new ListStore<LocationEntry>(locationProps.locationID());		locationProps = GWT.create(LocationProperties.class);
		for (LocationEntry locEntry : StaticTables.getInstance().getLocationEntries().values()) {
			locationEntryLS.add(locEntry);
		}
		locationEntryLS.addSortInfo(new StoreSortInfo<LocationEntry>(new ValueProvider<LocationEntry, String>(){

			@Override
			public String getValue(LocationEntry object) {
				return object.getName();
			}

			@Override
			public void setValue(LocationEntry object, String value) {}

			@Override
			public String getPath() {
				return "name";
			}}, SortDir.ASC));
		
		locationSelectionLV = new ListView<LocationEntry, LocationEntry>(locationEntryLS, new IdentityValueProvider<LocationEntry>(), new SimpleSafeHtmlCell<LocationEntry>(new AbstractSafeHtmlRenderer<LocationEntry>() {
			
			@Override
			public SafeHtml render(LocationEntry item) {
				final LocationViewTemplates lvTemplates = GWT.create(LocationViewTemplates.class);
				if ((item.getCounty() != null) && (!item.getCounty().isEmpty())) {
					if ((item.getTown() != null) && (!item.getTown().isEmpty())) {
						return lvTemplates.caveLabel(item.getName(), item.getRegion()!=null && !item.getRegion().isEmpty() ? item.getTown()+" ("+item.getRegion()+")" : item.getTown(), item.getCounty());
					} else if ((item.getRegion() != null) && (!item.getRegion().isEmpty())) {
						return lvTemplates.caveLabel(item.getName(), item.getTown()!=null && !item.getTown().isEmpty() ? item.getTown()+" ("+item.getRegion()+")" : item.getRegion(), item.getCounty());
					} else {
						return lvTemplates.caveLabel(item.getName(), item.getCounty());
					}
				} else {
					return lvTemplates.caveLabel(item.getName());
				}
			}
		}));
		locationSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel currentLocationPanel = new ContentPanel();
		currentLocationPanel.setHeaderVisible(true);
		currentLocationPanel.setHeading("Location search");
		currentLocationPanel.add(locationSelectionLV);
		
		AccordionLayoutContainer depictionFilterALC = new AccordionLayoutContainer();
    depictionFilterALC.setExpandMode(ExpandMode.SINGLE_FILL);
    depictionFilterALC.add(cavePanel);
    depictionFilterALC.add(currentLocationPanel);
    depictionFilterALC.setActiveWidget(cavePanel);
    depictionFilterALC.setHeight("400px");

		HorizontalLayoutContainer depictionFilterHLC = new HorizontalLayoutContainer();
		depictionFilterHLC.add(shortNamePanel, new HorizontalLayoutData(1.0, .2));
		depictionFilterHLC.add(depictionFilterALC,  new HorizontalLayoutData(1.0, .8));

    return depictionFilterHLC;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		ArrayList<String> result = new ArrayList<String>();
		if ((shortNameSearch.getValue() != null) && (shortNameSearch.getValue().length() > 0)) {
			result.add("ShortName LIKE '%" + shortNameSearch.getValue() + "%'");
		}

		String locationQuery = null;
		for (LocationEntry le : locationSelectionLV.getSelectionModel().getSelectedItems()) {
			if (locationQuery == null) {
				locationQuery = Integer.toString(le.getLocationID());
			} else {
				locationQuery = locationQuery.concat(", " + le.getLocationID());
			}
		}
		if (locationQuery != null) {
			result.add("CurrentLocationID IN (" + locationQuery + ")");
		}		

		String caveQuery = null;
		for (CaveEntry ce : caveSelectionLV.getSelectionModel().getSelectedItems()) {
			if (caveQuery == null) {
				caveQuery = Integer.toString(ce.getCaveID());
			} else {
				caveQuery = caveQuery.concat(", " + ce.getCaveID());
			}
		}
		if (caveQuery != null) {
			result.add("CaveID IN (" + caveQuery + ")");
		}
		return result;
	}

}
