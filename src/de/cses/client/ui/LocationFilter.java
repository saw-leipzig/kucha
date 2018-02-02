/*
 * Copyright 2017 - 2018
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
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldAppearance;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.selection.StoreSelectionModel;

import de.cses.client.StaticTables;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class LocationFilter extends AbstractFilter {

	private SiteProperties siteProps;
	private ListStore<SiteEntry> siteEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private DualListField<SiteEntry, String> siteSelection;
	private DualListField<RegionEntry, String> regionSelection;
	private ListStore<SiteEntry> selectedSitesList;
	private ListStore<DistrictEntry> selectedDistrictsList;
	private ListStore<RegionEntry> selectedRegionsList;
	private ListView<DistrictEntry, DistrictEntry> districtSelectionLV;
	private ListView<RegionEntry, RegionEntry> regionSelectionLV;

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}
	
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{districtName}<br>{siteName}</div><hr>")
		SafeHtml districtLabel(String districtName, String siteName);
	}
	
	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		LabelProvider<RegionEntry> uniqueID();
		ValueProvider<RegionEntry, String> englishName();
	}
	
	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div>{englishName}<br>{phoneticName} - {originalName}<br>{siteName}</div><hr>")
		SafeHtml regionLabel(String englishName, String phoneticName, String originalName, String siteName);
		
		@XTemplate("<div>{englishName}<br>{phoneticName}<br>{siteName}</div><hr>")
		SafeHtml regionLabel(String englishName, String phoneticName, String siteName);
		
		@XTemplate("<div>{englishName}<br>{siteName}</div><hr>")
		SafeHtml regionLabel(String englishName, String siteName);
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
		
		districtSelectionLV = new ListView<DistrictEntry, DistrictEntry>(districtEntryList, new IdentityValueProvider<DistrictEntry>(), new SimpleSafeHtmlCell<DistrictEntry>(new AbstractSafeHtmlRenderer<DistrictEntry>() {
			final DistrictViewTemplates dvTemplates = GWT.create(DistrictViewTemplates.class);

			@Override
			public SafeHtml render(DistrictEntry entry) {
				SiteEntry se = siteEntryList.findModelWithKey(Integer.toString(entry.getSiteID()));
				return dvTemplates.districtLabel(entry.getName(), se.getName());
			}}));
		districtSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
//		DualListField<DistrictEntry, String> districtSelection = new DualListField<DistrictEntry, String>(districtEntryList, selectedDistrictsList, districtProps.name(), new TextCell());
//		districtSelection.setEnableDnd(true);
//		districtSelection.getDownButton().removeFromParent();
//		districtSelection.getUpButton().removeFromParent();
//		districtSelection.setMode(DualListField.Mode.INSERT);
		
		ContentPanel districtPanel = new ContentPanel();
		districtPanel.setHeaderVisible(true);
		districtPanel.setHeading("Districts");
		districtPanel.add(districtSelectionLV);
		
		regionSelectionLV = new ListView<RegionEntry, RegionEntry>(regionEntryList, new IdentityValueProvider<RegionEntry>(), new SimpleSafeHtmlCell<RegionEntry>(new AbstractSafeHtmlRenderer<RegionEntry>() {
			final RegionViewTemplates rvTemplates = GWT.create(RegionViewTemplates.class);

			@Override
			public SafeHtml render(RegionEntry entry) {
				SiteEntry se = siteEntryList.findModelWithKey(Integer.toString(entry.getSiteID()));
				if ((entry.getPhoneticName() != null) && (entry.getPhoneticName().length() > 0)) {
					if ((entry.getOriginalName() != null) && (entry.getOriginalName().length() > 0)) {
						return rvTemplates.regionLabel(entry.getEnglishName(), entry.getPhoneticName(), entry.getOriginalName(), se.getName());
					} else {
						return rvTemplates.regionLabel(entry.getEnglishName(), entry.getPhoneticName(), se.getName());
					}
				} else {
					return rvTemplates.regionLabel(entry.getEnglishName(), se.getName());
				}
			}
		}));
		regionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
//		regionSelection = new DualListField<RegionEntry, String>(regionEntryList, selectedRegionsList, regionProps.englishName(), new TextCell());
//		regionSelection.setEnableDnd(true);
//		regionSelection.getDownButton().removeFromParent();
//		regionSelection.getUpButton().removeFromParent();
//		regionSelection.setMode(DualListField.Mode.INSERT);
		ContentPanel regionPanel = new ContentPanel();
		regionPanel.setHeaderVisible(true);
		regionPanel.setHeading("Regions");
		regionPanel.add(regionSelectionLV);
		
    vlc.add(sitePanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 5, 0)));
    vlc.add(districtPanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 5, 0)));
    vlc.add(regionPanel, new VerticalLayoutData(1.0, .33, new Margins(0, 0, 0, 0)));

    vlc.setHeight("450px");
		return vlc;
	}
	
	/**
	 * 
	 */
	private void loadSites() {
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		selectedSitesList = new ListStore<SiteEntry>(siteProps.siteID());

		for (SiteEntry se : StaticTables.getInstance().getSiteEntries().values()) {
			siteEntryList.add(se);
		}
	}

	/* 
	 * 
	 */
	private void loadRegions() {
		for (RegionEntry re : StaticTables.getInstance().getRegionEntries().values()) {
			regionEntryList.add(re);
		}
	}

	/**
	 * 
	 */
	private void loadDistricts() {
		for (DistrictEntry de : StaticTables.getInstance().getDistrictEntries().values()) {
			districtEntryList.add(de);
		}
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
		
		for (DistrictEntry de : districtSelectionLV.getSelectionModel().getSelectedItems()) {
			if (districtQuery.isEmpty()) {
				districtQuery = "" + de.getDistrictID();
			} else {
				districtQuery = districtQuery.concat(", " + de.getDistrictID());
			}
		}
		
//		if (selectedDistrictsList.size() > 0) {
//			for (DistrictEntry de : selectedDistrictsList.getAll()) {
//				if (districtQuery.isEmpty()) {
//					districtQuery = "" + de.getDistrictID();
//				} else {
//					districtQuery = districtQuery.concat(", " + de.getDistrictID());
//				}
//			}
//		}

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

		
		if (!districtQuery.isEmpty() && !regionQuery.isEmpty()) {
			result.add("(DistrictID IN (" + districtQuery + ") OR RegionID IN (" + regionQuery + "))");
		} else if (!districtQuery.isEmpty()) {
			result.add("(DistrictID IN (" + districtQuery + "))");
		} else if (!regionQuery.isEmpty()) {
			result.add("(RegionID IN (" + regionQuery + "))");
		}
		return result;
	}
	

}
