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
package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
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
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class CaveFilter extends AbstractFilter {

	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryList;
	private ComboBox<CaveTypeEntry> caveTypeSelection;
	private SiteProperties siteProps;
	private ListStore<SiteEntry> siteEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private ListView<DistrictEntry, DistrictEntry> districtSelectionLV;
	private ListView<RegionEntry, RegionEntry> regionSelectionLV;
	private ListView<SiteEntry, SiteEntry> siteSelectionLV;

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}
	
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{districtName}<br>{siteName}</div>")
		SafeHtml districtLabel(String districtName, String siteName);
	}
	
	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		LabelProvider<RegionEntry> uniqueID();
		ValueProvider<RegionEntry, String> englishName();
	}
	
	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{englishName}<br>{phoneticName} - {originalName}<br>{siteName}</div>")
		SafeHtml regionLabel(String englishName, String phoneticName, String originalName, String siteName);
		
		@XTemplate("<div style=\"border: 1px solid grey;\">{englishName}<br>{phoneticName}<br>{siteName}</div>")
		SafeHtml regionLabel(String englishName, String phoneticName, String siteName);
		
		@XTemplate("<div style=\"border: 1px solid grey;\">{englishName}<br>{siteName}</div>")
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

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();
		LabelProvider<CaveTypeEntry> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}

	/**
	 * 
	 */
	public CaveFilter(String filterName) {
		super(filterName);
		
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());

		regionProps = GWT.create(RegionProperties.class);
		regionEntryList = new ListStore<RegionEntry>(regionProps.regionID());

		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		
		loadCaveTypes();
		loadSites();		
		loadDistricts();
		loadRegions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {
		caveTypeSelection = new ComboBox<CaveTypeEntry>(caveTypeEntryList, caveTypeProps.nameEN(),
				new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

					@Override
					public SafeHtml render(CaveTypeEntry item) {
						final CaveTypeViewTemplates ctvTemplates = GWT.create(CaveTypeViewTemplates.class);
						return ctvTemplates.caveTypeLabel(item.getNameEN());
					}
				});
		caveTypeSelection.setEmptyText("select cave type");
		caveTypeSelection.setTypeAhead(false);
		caveTypeSelection.setEditable(false);
		caveTypeSelection.setTriggerAction(TriggerAction.ALL);
//		caveTypeSelection.addSelectionHandler(new SelectionHandler<CaveTypeEntry>() {
//
//			@Override
//			public void onSelection(SelectionEvent<CaveTypeEntry> event) {
//				
//			}
//		});
//		caveTypeSelection.setWidth(180);

		
		siteSelectionLV = new ListView<SiteEntry, SiteEntry>(siteEntryList, new IdentityValueProvider<SiteEntry>(), new SimpleSafeHtmlCell<SiteEntry>(new AbstractSafeHtmlRenderer<SiteEntry>() {
			final SiteViewTemplates svTemplates = GWT.create(SiteViewTemplates.class);
			
			@Override
			public SafeHtml render(SiteEntry entry) {
				return svTemplates.siteLabel(entry.getName());
			}
		}));
		siteSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel sitePanel = new ContentPanel();
		sitePanel.setHeaderVisible(true);
		sitePanel.setHeading("Sites");
		sitePanel.add(siteSelectionLV);
		
		districtSelectionLV = new ListView<DistrictEntry, DistrictEntry>(districtEntryList, new IdentityValueProvider<DistrictEntry>(), new SimpleSafeHtmlCell<DistrictEntry>(new AbstractSafeHtmlRenderer<DistrictEntry>() {
			final DistrictViewTemplates dvTemplates = GWT.create(DistrictViewTemplates.class);

			@Override
			public SafeHtml render(DistrictEntry entry) {
				SiteEntry se = siteEntryList.findModelWithKey(Integer.toString(entry.getSiteID()));
				return dvTemplates.districtLabel(entry.getName(), se.getName());
			}}));
		districtSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
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
		
		ContentPanel regionPanel = new ContentPanel();
		regionPanel.setHeaderVisible(true);
		regionPanel.setHeading("Regions");
		regionPanel.add(regionSelectionLV);
		
		/**
		 * create the view
		 */
		
		AccordionLayoutContainer locationALC = new AccordionLayoutContainer();
    locationALC.setExpandMode(ExpandMode.SINGLE_FILL);
    locationALC.add(sitePanel);
    locationALC.add(districtPanel);
    locationALC.add(regionPanel);
    locationALC.setActiveWidget(sitePanel);
		
    BorderLayoutContainer depictionFilterBLC = new BorderLayoutContainer();
    depictionFilterBLC.setNorthWidget(caveTypeSelection, new BorderLayoutData(30));
    depictionFilterBLC.setCenterWidget(locationALC, new MarginData(5, 0, 0, 0));
    depictionFilterBLC.setHeight(450);

    return depictionFilterBLC;
	}

	/**
	 * 
	 */
	private void loadCaveTypes() {
		for (CaveTypeEntry pe : StaticTables.getInstance().getCaveTypeEntries().values()) {
			caveTypeEntryList.add(pe);
		}
	}

	/**
	 * 
	 */
	private void loadSites() {
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

	@Override
	public CaveSearchEntry getSearchEntry() {
		CaveSearchEntry result = new CaveSearchEntry();
		
		if (caveTypeSelection.getValue() != null) {
			result.getCaveTypeIdList().add(caveTypeSelection.getCurrentValue().getCaveTypeID());
		}
		
		if (!siteSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (SiteEntry se : siteSelectionLV.getSelectionModel().getSelectedItems()) {
				result.getSiteIdList().add(se.getSiteID());
			}
		}

		if (!districtSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (DistrictEntry de : districtSelectionLV.getSelectionModel().getSelectedItems()) {
				result.getDistrictIdList().add(de.getDistrictID());
			}
		}

		if (!regionSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (RegionEntry re : regionSelectionLV.getSelectionModel().getSelectedItems()) {
				result.getRegionIdList().add(re.getRegionID());
			}
		}

		return result;
	}

}
