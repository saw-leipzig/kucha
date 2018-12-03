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
package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer;
import com.sencha.gxt.widget.core.client.container.AccordionLayoutContainer.ExpandMode;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.IntegerSpinnerField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class DepictionFilter extends AbstractFilter {

	class NameElement {
		private String element;
		
		public NameElement(String element) {
			super();
			this.element = element;
		}

		public String getElement() {
			return element;
		}
	}

	interface LocationProperties extends PropertyAccess<LocationEntry> {
		ModelKeyProvider<LocationEntry> locationID();
		LabelProvider<LocationEntry> name();
	}
	
	interface IconographyProperties extends PropertyAccess<IconographyEntry> {
		ModelKeyProvider<IconographyEntry> iconographyID();
		LabelProvider<IconographyEntry> text();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName}: {officialNumber}<br> {districtRegion}<br><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion, ArrayList<NameElement> name);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName}: {officialNumber}<br> {districtRegion}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion);
	}

	interface LocationViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\"><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml locationLabel(ArrayList<NameElement> name);
	}
	
	interface IconographyViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\"><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml iconographyLabel(ArrayList<NameElement> name);
	}

	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> officialNumber();
	}
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private TextField shortNameSearchTF;
	private CaveProperties caveProps;
	private IconographyProperties icoProps;
	private ListStore<CaveEntry> caveEntryLS;
	private ListStore<IconographyEntry> selectedIconographyLS;
	private ListView<CaveEntry, CaveEntry> caveSelectionLV;
	private ListView<LocationEntry, LocationEntry> locationSelectionLV;
	private IconographySelector icoSelector;
	private ArrayList<String> sqlWhereClause;
	private ListView<IconographyEntry, IconographyEntry> icoSelectionLV;

	private IntegerSpinnerField iconographySpinnerField;

	private PopupPanel extendedFilterDialog = null;

	/**
	 * @param filterName
	 */
	public DepictionFilter(String filterName) {
		super(filterName);
		caveProps = GWT.create(CaveProperties.class);
		caveEntryLS = new ListStore<CaveEntry>(caveProps.caveID());
		icoSelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values());
		icoProps = GWT.create(IconographyProperties.class);
		selectedIconographyLS = new ListStore<>(icoProps.iconographyID());
		loadCaves();
	}

	private void loadCaves() {
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			private String getComparisonLabel(CaveEntry ce) {
				StaticTables stab = StaticTables.getInstance();
				String shortName = stab.getSiteEntries().get(ce.getSiteID()).getShortName();
				int len = 0;
				while ((len < ce.getOfficialNumber().length()) && isInteger(ce.getOfficialNumber().substring(0, len+1))) {
					++len;
				}
				switch (len) {
					case 1:
						return shortName + "  " + ce.getOfficialNumber();
					case 2:
						return shortName + " " + ce.getOfficialNumber();
					default:
						return shortName + ce.getOfficialNumber();
				}
			}
			
			private boolean isInteger(String str) {
				if (str == null) {
					return false;
				}
				int length = str.length();
				if (length == 0) {
					return false;
				}
				int i = 0;
				if (str.charAt(0) == '-') {
					if (length == 1) {
						return false;
					}
					i = 1;
				}
				for (; i < length; i++) {
					char c = str.charAt(i);
					if (c < '0' || c > '9') {
						return false;
					}
				}
				return true;
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> caveResults) {
				caveResults.sort(new Comparator<CaveEntry>() {

					@Override
					public int compare(CaveEntry ce1, CaveEntry ce2) {
						return getComparisonLabel(ce1).compareTo(getComparisonLabel(ce2));
					}
				});
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

		/**
		 * assemble caveSelection
		 */
		caveSelectionLV = new ListView<CaveEntry, CaveEntry>(caveEntryLS, new IdentityValueProvider<CaveEntry>(), new SimpleSafeHtmlCell<CaveEntry>(new AbstractSafeHtmlRenderer<CaveEntry>() {
			
			@Override
			public SafeHtml render(CaveEntry entry) {
				final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
				StaticTables st = StaticTables.getInstance();
				DistrictEntry de = st.getDistrictEntries().get(entry.getDistrictID());
				SiteEntry se = st.getSiteEntries().get(entry.getSiteID());
				RegionEntry re = st.getRegionEntries().get(entry.getRegionID());
				String districtRegionInformation = (de != null) ? de.getName() + (re != null ? " / " + re.getOriginalName() : "") : (re != null ? re.getOriginalName() : "");
				if ((entry.getHistoricName() != null) && (entry.getHistoricName().length() > 0)) {
					ArrayList<NameElement> historicNameList = new ArrayList<NameElement>();
					for (String s : entry.getHistoricName().split(" ")) {
						historicNameList.add(new NameElement(s));
					}
					return cvTemplates.caveLabel(se != null ? se.getShortName() : "", entry.getOfficialNumber(), districtRegionInformation, historicNameList);
				} else {
					return cvTemplates.caveLabel(se != null ? se.getShortName() : "", entry.getOfficialNumber(), districtRegionInformation);
				}
			}
		}));
		caveSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel cavePanel = new ContentPanel();
		cavePanel.setHeaderVisible(true);
		cavePanel.setHeading("Cave search");
		cavePanel.add(caveSelectionLV);
		
		icoSelectionLV = new ListView<IconographyEntry, IconographyEntry>(selectedIconographyLS, new IdentityValueProvider<IconographyEntry>(), new SimpleSafeHtmlCell<IconographyEntry>(new AbstractSafeHtmlRenderer<IconographyEntry>() {

			@Override
			public SafeHtml render(IconographyEntry item) {
				IconographyViewTemplates icTemplates = GWT.create(IconographyViewTemplates.class);
				ArrayList<NameElement> name = new ArrayList<NameElement>();
				for (String s : item.getText().split(" ")) {
					name.add(new NameElement(s));
				}
				return icTemplates.iconographyLabel(name);
			}
		}));
		
		iconographySpinnerField = new IntegerSpinnerField();
		iconographySpinnerField.setMinValue(1);
		iconographySpinnerField.setIncrement(1);
		iconographySpinnerField.setEnabled(false);
		iconographySpinnerField.setEditable(false);
		FieldLabel iconographyFieldLabel = new FieldLabel(iconographySpinnerField, "Matching elements");
		iconographyFieldLabel.setLabelWidth(120);
		
		BorderLayoutContainer iconographyBLC = new BorderLayoutContainer();
		iconographyBLC.setSouthWidget(iconographyFieldLabel, new BorderLayoutData(25));
		iconographyBLC.setCenterWidget(icoSelectionLV, new MarginData(2));
		
		ContentPanel iconographyPanel = new ContentPanel();
		iconographyPanel.setHeaderVisible(true);
		iconographyPanel.setHeading("Iconography & Pictorial Element search");
		iconographyPanel.add(iconographyBLC);
		ToolButton selectorTB = new ToolButton(ToolButton.GEAR);
		selectorTB.setToolTip(Util.createToolTip("Open Iconography & Pictorial Element selection"));
		selectorTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				showIconographySelection();
			}
		});
		iconographyPanel.addTool(selectorTB);
		
		/**
		 * assemble shortNameSearchTF
		 */
		
		shortNameSearchTF = new TextField();
		shortNameSearchTF.setEmptyText("search short name");

		/**
		 * assemble current location selection
		 */
		LocationProperties locationProps = GWT.create(LocationProperties.class);
		ListStore<LocationEntry> 		locationEntryLS = new ListStore<LocationEntry>(locationProps.locationID());		locationProps = GWT.create(LocationProperties.class);
		for (LocationEntry locEntry : StaticTables.getInstance().getLocationEntries().values()) {
			locationEntryLS.add(locEntry);
		}
		
		locationSelectionLV = new ListView<LocationEntry, LocationEntry>(locationEntryLS, new IdentityValueProvider<LocationEntry>(), new SimpleSafeHtmlCell<LocationEntry>(new AbstractSafeHtmlRenderer<LocationEntry>() {
			
			@Override
			public SafeHtml render(LocationEntry item) {
				final LocationViewTemplates lvTemplates = GWT.create(LocationViewTemplates.class);
				String label;
				if ((item.getCounty() != null) && (!item.getCounty().isEmpty())) {
					if ((item.getTown() != null) && (!item.getTown().isEmpty())) {
						label = item.getName()+", " + (item.getRegion()!=null && !item.getRegion().isEmpty() ? item.getTown()+", " + item.getRegion() : item.getTown()) + ", " + item.getCounty();
					} else if ((item.getRegion() != null) && (!item.getRegion().isEmpty())) {
						label = item.getName() +", "+ (item.getTown()!=null && !item.getTown().isEmpty() ? item.getTown()+", "+ item.getRegion() : item.getRegion()) + ", " + item.getCounty();
					} else {
						label = item.getName() + ", " + item.getCounty();
					}
				} else {
					label = item.getName();
				}
				ArrayList<NameElement> labelList = new ArrayList<NameElement>();
				for (String s : label.split(" ")) {
					labelList.add(new NameElement(s));
				}
				return lvTemplates.locationLabel(labelList);
			}
		}));
		locationSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel currentLocationPanel = new ContentPanel();
		currentLocationPanel.setHeaderVisible(true);
		currentLocationPanel.setHeading("Location search");
		currentLocationPanel.add(locationSelectionLV);
		
		/**
		 * create the view
		 */
		
		AccordionLayoutContainer depictionFilterALC = new AccordionLayoutContainer();
    depictionFilterALC.setExpandMode(ExpandMode.SINGLE_FILL);
    depictionFilterALC.add(cavePanel);
    depictionFilterALC.add(currentLocationPanel);
    depictionFilterALC.add(iconographyPanel);
    depictionFilterALC.setActiveWidget(cavePanel);

    BorderLayoutContainer depictionFilterBLC = new BorderLayoutContainer();
    depictionFilterBLC.setNorthWidget(shortNameSearchTF, new BorderLayoutData(20));
    depictionFilterBLC.setCenterWidget(depictionFilterALC, new MarginData(5, 0, 0, 0));
    depictionFilterBLC.setHeight(450);

    return depictionFilterBLC;
	}

	private void showIconographySelection() {
		if (extendedFilterDialog == null) {
			extendedFilterDialog = new PopupPanel();
			ToolButton closeTB = new ToolButton(ToolButton.CLOSE);
			closeTB.setToolTip(Util.createToolTip("Close selection.", "Currently selected items will be used in the filter."));
			closeTB.addSelectHandler(new SelectHandler() {
				
				@Override
				public void onSelect(SelectEvent event) {
					selectedIconographyLS.clear();
					selectedIconographyLS.addAll(icoSelector.getSelectedIconography());
					if ((icoSelector.getSelectedIconography() != null) && (selectedIconographyLS.size() > 0)) {
						iconographySpinnerField.setEnabled(true);
						iconographySpinnerField.setValue(selectedIconographyLS.size());
						iconographySpinnerField.setMaxValue(selectedIconographyLS.size());
					} else {
						iconographySpinnerField.setEnabled(false);
					}
					extendedFilterDialog.hide();
				}
			});
			icoSelector.addTool(closeTB);
			extendedFilterDialog.add(icoSelector);
			extendedFilterDialog.setSize("750", "500");
			extendedFilterDialog.setModal(true);
		}
		extendedFilterDialog.center();
	}

	@Override
	public AbstractSearchEntry getSearchEntry() {
		DepictionSearchEntry searchEntry = new DepictionSearchEntry();
		
		if (!shortNameSearchTF.getValue().isEmpty()) {
			searchEntry.setShortName(shortNameSearchTF.getValue());
		}
		
		for (CaveEntry ce : caveSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getCaveIdList().add(ce.getCaveID());
		}
		
		for (LocationEntry le : locationSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getLocationIdList().add(le.getLocationID());
		}
		
		for (IconographyEntry ie : selectedIconographyLS.getAll()) {
			searchEntry.getIconographyIdList().add(ie.getIconographyID());
		}
		
		searchEntry.setCorrelationFactor(iconographySpinnerField.isEnabled() ? iconographySpinnerField.getValue() : 0);
		
		return searchEntry;
	}

}
