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
package de.cses.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DualListField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class LocationFilter extends AbstractFilter {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private SiteProperties siteProps;
	private ListStore<SiteEntry> siteEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private DualListField<SiteEntry, String> siteSelection;
	private DualListField<DistrictEntry, String> districtSelection;
	private DualListField<RegionEntry, String> regionSelection;
	private ListStore<SiteEntry> selectedSitesList;
	private ListStore<DistrictEntry> selectedDistrictsList;
	private ListStore<RegionEntry> selectedRegionsList;

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}
	
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml districtLabel(String name);
	}
	
	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		LabelProvider<RegionEntry> uniqueID();
		ValueProvider<RegionEntry, String> englishName();
	}
	
	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div>{phoneticName}<br>{originalName}<br>{englishName}</div>")
		SafeHtml regionLabel(String phoneticName, String originalName, String englishName);
		
		@XTemplate("<div>{englishName}</div>")
		SafeHtml regionLabel(String englishName);
	}
	
	interface SiteProperties extends PropertyAccess<SiteEntry> {
		ModelKeyProvider<SiteEntry> siteID();
		LabelProvider<SiteEntry> uniqueID();
		ValueProvider<SiteEntry, String> name();
		
	}
	
	interface SiteViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml siteLabel(String name);
	}

	/**
	 * 
	 */
	public LocationFilter(String filterName) {
		super(filterName);
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		selectedSitesList = new ListStore<SiteEntry>(siteProps.siteID());

		regionProps = GWT.create(RegionProperties.class);
		regionEntryList = new ListStore<RegionEntry>(regionProps.regionID());
		selectedRegionsList = new ListStore<RegionEntry>(regionProps.regionID());

		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		selectedDistrictsList  = new ListStore<DistrictEntry>(districtProps.districtID());

		loadSites();		
		loadDistricts();
		loadRegions();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();

		siteSelection = new DualListField<SiteEntry, String>(siteEntryList, selectedSitesList, siteProps.name(), new TextCell());
		siteSelection.setEnableDnd(true);
		siteSelection.getDownButton().removeFromParent();
		siteSelection.getUpButton().removeFromParent();
		siteSelection.setMode(DualListField.Mode.INSERT);
		ContentPanel sitePanel = new ContentPanel();
		sitePanel.setHeaderVisible(true);
		sitePanel.setHeading("Sites");
		sitePanel.add(siteSelection);
		
		districtSelection = new DualListField<DistrictEntry, String>(districtEntryList, selectedDistrictsList, districtProps.name(), new TextCell());
		districtSelection.setEnableDnd(true);
		districtSelection.getDownButton().removeFromParent();
		districtSelection.getUpButton().removeFromParent();
		districtSelection.setMode(DualListField.Mode.INSERT);
		ContentPanel districtPanel = new ContentPanel();
		districtPanel.setHeaderVisible(true);
		districtPanel.setHeading("Districts");
		districtPanel.add(districtSelection);
		
		regionSelection = new DualListField<RegionEntry, String>(regionEntryList, selectedRegionsList, regionProps.englishName(), new TextCell());
		regionSelection.setEnableDnd(true);
		regionSelection.getDownButton().removeFromParent();
		regionSelection.getUpButton().removeFromParent();
		regionSelection.setMode(DualListField.Mode.INSERT);
		ContentPanel regionPanel = new ContentPanel();
		regionPanel.setHeaderVisible(true);
		regionPanel.setHeading("Regions");
		regionPanel.add(regionSelection);
		
    vlc.add(sitePanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 5, 0)));
    vlc.add(districtPanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 5, 0)));
    vlc.add(regionPanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 0, 0)));

    vlc.setHeight("360px");
		return vlc;
	}
	
	/**
	 * 
	 */
	private void loadSites() {
		dbService.getSites(new AsyncCallback<ArrayList<SiteEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<SiteEntry> result) {
				siteEntryList.clear();
				for (SiteEntry se : result) {
					siteEntryList.add(se);
				}
			}
		});
	}

	/* 
	 * 
	 */
	private void loadRegions() {
		dbService.getRegions(new AsyncCallback<ArrayList<RegionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<RegionEntry> result) {
				regionEntryList.clear();
				for (RegionEntry re : result) {
					regionEntryList.add(re);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void loadDistricts() {
		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				districtEntryList.clear();
				for (DistrictEntry de : result) {
					districtEntryList.add(de);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		ArrayList<String> result = new ArrayList<String>();
		String districtQuery = "";
		String regionQuery = "";
		List<DistrictEntry> nonSelectedDistricts = districtEntryList.getAll();
		List<RegionEntry> nonSelectedRegions = regionEntryList.getAll();
		
		if (selectedDistrictsList.size() > 0) {
			for (DistrictEntry de : selectedDistrictsList.getAll()) {
				if (districtQuery.isEmpty()) {
					districtQuery = "" + de.getDistrictID();
				} else {
					districtQuery = districtQuery.concat(", " + de.getDistrictID());
				}
			}
		}

		if (selectedRegionsList.size() > 0) {
			for (RegionEntry re : selectedRegionsList.getAll()) {
				if (regionQuery.isEmpty()) {
					regionQuery = "" + re.getRegionID();
				} else {
					regionQuery = regionQuery.concat(", " + re.getRegionID());
				}
			}
		}
		
		if (selectedSitesList.size() > 0) {
			for (SiteEntry se : selectedSitesList.getAll()) {

				for (DistrictEntry nsde : nonSelectedDistricts) {
					if (se.getSiteID() == nsde.getSiteID()) {
						if (districtQuery.isEmpty()) {
							districtQuery = "" + nsde.getDistrictID();
						} else {
							districtQuery = districtQuery.concat(", " + nsde.getDistrictID());
						}
					}
				}
				
				for (RegionEntry nsre : nonSelectedRegions) {
					if (se.getSiteID() == nsre.getSiteID()) {
						if (regionQuery.isEmpty()) {
							regionQuery = "" + nsre.getRegionID();
						} else {
							regionQuery = regionQuery.concat(", " + nsre.getRegionID());
						}
					}
				}
			}
		}

		if (!districtQuery.isEmpty()) {
			result.add("(DistrictID IN (" + districtQuery + "))");
		}
		if (!regionQuery.isEmpty()) {
			result.add("(RegionID IN (" + regionQuery + "))");
		}
		return result;
	}
	

}
