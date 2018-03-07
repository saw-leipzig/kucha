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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.ValueBaseField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.caves.CaveFilter;
import de.cses.client.caves.CaveSearchController;
import de.cses.client.depictions.DepictionEditor.CaveProperties;
import de.cses.client.depictions.DepictionEditor.CaveViewTemplates;
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

	private TextField caveSearch;
	private ComboBox<LocationEntry> locationSelectionCB;
	private ValueBaseField<String> shortNameSearch;
	private CaveProperties caveProps;
	private ListStore<CaveEntry> caveEntryLS;

	private ComboBox<CaveEntry> caveSelectionCoBo;

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
		caveSelectionCoBo = new ComboBox<CaveEntry>(caveEntryLS, new LabelProvider<CaveEntry>() {

			@Override
			public String getLabel(CaveEntry item) {
				StaticTables st = StaticTables.getInstance();
				DistrictEntry de = null;
				SiteEntry se = null;
				de = st.getDistrictEntries().get(item.getDistrictID());
				if (de != null) {
					se = st.getSiteEntries().get(de.getSiteID());
				}
				return (se != null ? se.getName()+": " : (de != null ? de.getName()+": " : "")) + item.getOfficialNumber() 
					+ (item.getHistoricName() != null ? " "+item.getHistoricName() : "");
			}
		}, new AbstractSafeHtmlRenderer<CaveEntry>() {

			@Override
			public SafeHtml render(CaveEntry item) {
				final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
				StaticTables st = StaticTables.getInstance();
				DistrictEntry de = null;
				SiteEntry se = null;
				de = st.getDistrictEntries().get(item.getDistrictID());
				if (de != null) {
					se = st.getSiteEntries().get(de.getSiteID());
				}
				String siteDistrictInformation = (se != null ? se.getName() : "") + (de != null ? (se != null ? " / " : "") + de.getName() : "");
				if ((item.getHistoricName() != null) && (item.getHistoricName().length() > 0)) {
					return cvTemplates.caveLabel(siteDistrictInformation, item.getOfficialNumber(), item.getHistoricName());
				} else {
					return cvTemplates.caveLabel(siteDistrictInformation, item.getOfficialNumber());
				}
			}
		});
		caveSelectionCoBo.setEmptyText("search for cave");
		caveSelectionCoBo.setTypeAhead(false);
		caveSelectionCoBo.setEditable(false);
		caveSelectionCoBo.setTriggerAction(TriggerAction.ALL);
		
		shortNameSearch = new TextField();
		shortNameSearch.setEmptyText("search short name");

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
//		FramedPanel currentLocationFP = new FramedPanel();
//		currentLocationFP.setHeading("Current Location");
		locationSelectionCB = new ComboBox<LocationEntry>(locationEntryLS, locationProps.name(), new AbstractSafeHtmlRenderer<LocationEntry>() {

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
		});
		locationSelectionCB.setEmptyText("search current location");
		locationSelectionCB.setTypeAhead(false);
		locationSelectionCB.setEditable(false);
		locationSelectionCB.setTriggerAction(TriggerAction.ALL);
//		currentLocationFP.add(locationSelectionCB);
		
		HorizontalLayoutContainer caveSearchHLC = new HorizontalLayoutContainer();
		caveSearchHLC.add(caveSearch, new HorizontalLayoutData(1.0, 1.0));

		VerticalPanel vp = new VerticalPanel();
		vp.setSpacing(5);
		vp.add(caveSearchHLC);
		vp.add(shortNameSearch);
		vp.add(locationSelectionCB);
		return vp;
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
		if (locationSelectionCB.getCurrentValue() != null) {
			result.add("CurrentLocationID=" + locationSelectionCB.getCurrentValue().getLocationID());
		}
		if (caveSelectionCoBo.getCurrentValue() != null) {
			result.add("CaveID=" + caveSelectionCoBo.getCurrentValue().getCaveID());
		}
		return result;
	}

}
