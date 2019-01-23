/*
 * Copyright 2017 - 2019
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

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Cookies;
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
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.comparator.DistrictEntryComparator;
import de.cses.shared.comparator.RegionEntryComparator;

/**
 * @author alingnau
 * 
 * The CaveFilter is shown on the left when Caves are activated for search.
 *
 */
public class CaveFilter extends AbstractFilter {

	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryList;
	private ListView<CaveTypeEntry, CaveTypeEntry> caveTypeSelectionLV;
	private SiteProperties siteProps;
	private ListStore<SiteEntry> siteEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private ListView<DistrictEntry, DistrictEntry> districtSelectionLV;
	private ListView<RegionEntry, RegionEntry> regionSelectionLV;
	private ListView<SiteEntry, SiteEntry> siteSelectionLV;
	private TextField searchNameTF;
	private CheckBox decoratedOnlyCB;

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}
	
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{siteName}<br>{districtName}</div>")
		SafeHtml districtLabel(String districtName, String siteName);
	}
	
	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		LabelProvider<RegionEntry> uniqueID();
		ValueProvider<RegionEntry, String> englishName();
	}
	
	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{siteName}<br>{englishName}<br>{phoneticName} - {originalName}</div>")
		SafeHtml regionLabel(String englishName, String phoneticName, String originalName, String siteName);
		
		@XTemplate("<div style=\"border: 1px solid grey;\">{siteName}<br>{englishName}<br>{phoneticName}</div>")
		SafeHtml regionLabel(String englishName, String phoneticName, String siteName);
		
		@XTemplate("<div style=\"border: 1px solid grey;\">{siteName}<br>{englishName}</div>")
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
		LabelProvider<CaveTypeEntry> uniqueID();
		ValueProvider<CaveTypeEntry, String> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}

	/**
	 * 
	 * @param filterName will be displayed as name of the frame on the left
	 */
	public CaveFilter(String filterName) {
		super(filterName);
		
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());

		regionProps = GWT.create(RegionProperties.class);
		regionEntryList = new ListStore<RegionEntry>(regionProps.regionID());
		regionEntryList.addSortInfo(new StoreSortInfo<>(new RegionEntryComparator(), SortDir.ASC));

		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		districtEntryList.addSortInfo(new StoreSortInfo<>(new DistrictEntryComparator(), SortDir.ASC));
		
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
		
		searchNameTF = new TextField();
		searchNameTF.setEmptyText("search historical names");
		
		decoratedOnlyCB = new CheckBox();
		decoratedOnlyCB.setBoxLabel("decorated caves only");
		decoratedOnlyCB.setValue(false);
		
		caveTypeSelectionLV = new ListView<CaveTypeEntry, CaveTypeEntry>(caveTypeEntryList, new IdentityValueProvider<CaveTypeEntry>(), new SimpleSafeHtmlCell<CaveTypeEntry>(new AbstractSafeHtmlRenderer<CaveTypeEntry>() {
			final CaveTypeViewTemplates ctvTemplates = GWT.create(CaveTypeViewTemplates.class);

			@Override
			public SafeHtml render(CaveTypeEntry entry) {
				return ctvTemplates.caveTypeLabel(entry.getNameEN());
			}
			
		}));
		caveTypeSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel caveTypePanel = new ContentPanel();
		caveTypePanel.setHeaderVisible(true);
		caveTypePanel.setHeading("Cave Types");
		caveTypePanel.add(caveTypeSelectionLV);
		
		ToolButton caveTypeSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		caveTypeSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		caveTypeSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				caveTypeSelectionLV.getSelectionModel().deselectAll();
			}
		});
		caveTypePanel.addTool(caveTypeSelectionResetTB);
		
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
		
		ToolButton siteSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		siteSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		siteSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				siteSelectionLV.getSelectionModel().deselectAll();
			}
		});
		sitePanel.addTool(siteSelectionResetTB);
		
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

		ToolButton districtSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		districtSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		districtSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				districtSelectionLV.getSelectionModel().deselectAll();
			}
		});
		districtPanel.addTool(districtSelectionResetTB);

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

		ToolButton regionSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		regionSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		regionSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				regionSelectionLV.getSelectionModel().deselectAll();
			}
		});
		regionPanel.addTool(regionSelectionResetTB);

		/**
		 * create the view
		 */
		
		AccordionLayoutContainer locationALC = new AccordionLayoutContainer();
    locationALC.setExpandMode(ExpandMode.SINGLE_FILL);
    locationALC.add(caveTypePanel);
    locationALC.add(sitePanel);
    locationALC.add(districtPanel);
    locationALC.add(regionPanel);
    locationALC.setActiveWidget(caveTypePanel);
    
    VerticalLayoutContainer dfVLC = new VerticalLayoutContainer();
    dfVLC.add(searchNameTF, new VerticalLayoutData(1.0, .5));
    dfVLC.add(decoratedOnlyCB, new VerticalLayoutData(1.0, .5));
		
    BorderLayoutContainer depictionFilterBLC = new BorderLayoutContainer();
    depictionFilterBLC.setNorthWidget(dfVLC, new BorderLayoutData(40));
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
		CaveSearchEntry result;
		if (UserLogin.isLoggedIn()) {
			result = new CaveSearchEntry(UserLogin.getInstance().getSessionID());
		} else {
			result = new CaveSearchEntry();
		}
		
		if (searchNameTF.getValue() != null && !searchNameTF.getValue().isEmpty()) {
			result.setHistoricalName(searchNameTF.getValue());
		}
		
		result.setDecoratedOnly(decoratedOnlyCB.getValue());
		
		if (!caveTypeSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (CaveTypeEntry cte : caveTypeSelectionLV.getSelectionModel().getSelectedItems()) {
				result.getCaveTypeIdList().add(cte.getCaveTypeID());
			}
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
