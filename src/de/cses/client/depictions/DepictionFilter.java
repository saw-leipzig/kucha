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
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
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
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.user.UserLogin;
import de.cses.client.walls.PositionEditor;
import de.cses.client.walls.WallTree;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.OrnamenticSearchEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.WallTreeEntry;
import de.cses.shared.comparator.CaveEntryComparator;

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

	public interface IconographyProperties extends PropertyAccess<IconographyEntry> {
		ModelKeyProvider<IconographyEntry> iconographyID();
		LabelProvider<IconographyEntry> text();
	}

	public interface WallTreeProperties extends PropertyAccess<WallTreeEntry> {
		ModelKeyProvider<WallTreeEntry> wallLocationID();
		LabelProvider<WallTreeEntry> text();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {districtRegion}<br><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion, ArrayList<NameElement> name);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {districtRegion}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion);
	}

	interface LocationViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{name}</div>")
		SafeHtml locationLabel(String name);

		@XTemplate("<div style=\"border: 1px solid grey;\">{town}<br>{name}</div>")
		SafeHtml locationLabel(String town, String name);
	}
	
	interface PositionProperties extends PropertyAccess<PositionEntry> {
		ModelKeyProvider<PositionEntry> PositionID();
		LabelProvider<PositionEntry> uniqueID();
		ValueProvider<PositionEntry, String> name();
	}
	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionLabel(String name);
	}


	
	interface IconographyViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\"><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml iconographyLabel(ArrayList<NameElement> name);
	}
	interface WallTreeViewTemplates extends XTemplates {
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
	private ListView<CaveEntry, CaveEntry> caveSelectionLV;
	private ListView<LocationEntry, LocationEntry> locationSelectionLV;
	private ListStore<IconographyEntry> selectedIconographyLS;
	private IconographySelector icoSelector;
	private ListView<IconographyEntry, IconographyEntry> icoSelectionLV;
	private IntegerSpinnerField iconographySpinnerField;
	private WallTreeProperties wallProps;
	private ListStore<WallTreeEntry> selectedWallsLS;
	private ListView<WallTreeEntry, WallTreeEntry> wallSelectionLV;
	private IntegerSpinnerField wallSpinnerField;
	private PopupPanel extendedFilterDialog = null;
	private ArrayList<Integer> imgIDs = new ArrayList<Integer>();
	private ArrayList<Integer> bibIDs = new ArrayList<Integer>();
	private PositionEditor pe;
	
	private ListStore<PositionEntry> positionEntryList;
	private PositionProperties positionProps;
	private ListView<PositionEntry, PositionEntry> positionSelectionLV;

	@Override
	public void setSearchEntry(AbstractSearchEntry searchEntry, boolean reset) {
		if (reset) {
			clear();			
		}
		if (((DepictionSearchEntry)searchEntry).getShortName()!= null && !((DepictionSearchEntry)searchEntry).getShortName().isEmpty()) {
			shortNameSearchTF.setValue(((DepictionSearchEntry)searchEntry).getShortName());
		}
		
		for (int cID : ((DepictionSearchEntry)searchEntry).getCaveIdList()) {
			caveSelectionLV.getSelectionModel().select(caveSelectionLV.getStore().findModelWithKey(Integer.toString(cID)),true);
		}
		for (int bibID : ((DepictionSearchEntry)searchEntry).getBibIdList()) {
			bibIDs.add(bibID);
		}
		if (((DepictionSearchEntry)searchEntry).getImageIdList().size()>0) {
			for (int imgID : ((DepictionSearchEntry)searchEntry).getImageIdList()) {
				imgIDs.add(imgID);
			}
		}
		
		for (int lID : ((DepictionSearchEntry)searchEntry).getLocationIdList()) {
			locationSelectionLV.getSelectionModel().select(locationSelectionLV.getStore().findModelWithKey(Integer.toString(lID)), true);
		}
		if (((DepictionSearchEntry)searchEntry).getCorrelationFactor()>= 0 ) {
			iconographySpinnerField.setValue(((DepictionSearchEntry)searchEntry).getCorrelationFactor());
		}
		selectedIconographyLS.addAll(icoSelector.setSelectionByIDList(((DepictionSearchEntry)searchEntry).getIconographyIdList()));
//		icoSelector.setSelectedIconography(de.getRelatedIconographyList());
		if ((((DepictionSearchEntry)searchEntry).getIconographyIdList() != null) && (((DepictionSearchEntry)searchEntry).getIconographyIdList().size()>0)) {
			iconographySpinnerField.setEnabled(true);
			iconographySpinnerField.setValue(selectedIconographyLS.size());
			iconographySpinnerField.setMaxValue(selectedIconographyLS.size());
		} else {
			iconographySpinnerField.setEnabled(false);
		}
//		Util.doLogging("Hier");
//		for (PositionEntry pe :((OrnamenticSearchEntry)searchEntry).getPosition()) {
//			Util.doLogging("Hier loop");
//			positionSelectionLV.getSelectionModel().select(true, pe);
//		}


		
	}
	private void loadPositionEntryList() {
		dbService.getPositions(new AsyncCallback<ArrayList<PositionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PositionEntry> result) {
				positionEntryList.clear();
				for (PositionEntry pe : result) {
					positionEntryList.add(pe);
				}
			}
		});
	}

	@Override
	public void clear() {
		shortNameSearchTF.reset();
		caveSelectionLV.getSelectionModel().deselectAll();
		locationSelectionLV.getSelectionModel().deselectAll();
		selectedIconographyLS.clear();
		iconographySpinnerField.setValue(0);
		imgIDs.clear();
		bibIDs.clear();
		positionSelectionLV.getSelectionModel().deselectAll();;
		wallSelectionLV.getSelectionModel().deselectAll();;

	}
	/**
	 * @param filterName
	 */
	public DepictionFilter(String filterName) {
		super(filterName);
		positionProps = GWT.create(PositionProperties.class);
		positionEntryList = new ListStore<PositionEntry>(positionProps.PositionID());
		loadPositionEntryList();

		caveProps = GWT.create(CaveProperties.class);
		caveEntryLS = new ListStore<CaveEntry>(caveProps.caveID());
		icoSelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values());
		wallProps = GWT.create(WallTreeProperties.class);
		selectedWallsLS = new ListStore<>(wallProps.wallLocationID());
		Map<Integer, WallTreeEntry> entries = StaticTables.getInstance().getWallTreeEntries();
		ArrayList<WallTreeEntry> wallEntries = new ArrayList<WallTreeEntry>();
		//for (WallTreeEntry wte :  entries.values()){
		//	wallEntries.add(wte);
		//}
		icoSelector.enable();
		icoSelector.selectNoParents(true);
		icoProps = GWT.create(IconographyProperties.class);
		selectedIconographyLS = new ListStore<>(icoProps.iconographyID());
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
		caveSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());
		caveSelectionLV.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<CaveEntry>() {
			
			@Override
			public void onSelectionChanged(SelectionChangedEvent<CaveEntry> event) {
				caveEntryLS.applySort(false);
			}
		});
		caveEntryLS.addSortInfo(new StoreSortInfo<CaveEntry>(new CaveEntryComparator(), SortDir.ASC));
		
		StoreFilterField<CaveEntry> filterField = new StoreFilterField<CaveEntry>() {

			@Override
			protected boolean doSelect(Store<CaveEntry> store, CaveEntry parent, CaveEntry item, String filter) {
				StaticTables st = StaticTables.getInstance();
				String searchString = "";
				if (item.getSiteID() > 0) {
					SiteEntry se = st.getSiteEntries().get(item.getSiteID());
					searchString += se.getName() + " " + se.getShortName() + " ";
				}
				if (item.getDistrictID() > 0) {
					DistrictEntry de = st.getDistrictEntries().get(item.getDistrictID());
					searchString += de.getName() + " ";
				}
				if (item.getRegionID() > 0) {
					RegionEntry re = st.getRegionEntries().get(item.getRegionID());
					searchString += re.getEnglishName() + " " + re.getOriginalName() + " ";
				}
				searchString += item.getHistoricName() + " " + item.getOfficialNumber();
				return searchString.toLowerCase().contains(filter.toLowerCase());
			}
		};
		// TODO why is filter killing selection?
//		filterField.bind(caveEntryLS);
		
//		BorderLayoutData south = new BorderLayoutData();
//		south.setMargins(new Margins(5, 3, 3, 3));
//		BorderLayoutContainer caveBLC = new BorderLayoutContainer();
//		caveBLC.setSouthWidget(filterField, south);
//		caveBLC.setCenterWidget(caveSelectionLV, new MarginData(2));
		
		ContentPanel cavePanel = new ContentPanel();
		cavePanel.setHeaderVisible(true);
		cavePanel.setHeading("Cave search");
		cavePanel.add(caveSelectionLV);
		cavePanel.getHeader().setStylePrimaryName("frame-header");
		
		ToolButton caveSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		caveSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		caveSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				caveSelectionLV.getSelectionModel().deselectAll();
			}
		});
		cavePanel.addTool(caveSelectionResetTB);
		
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
		iconographySpinnerField.addKeyPressHandler(getShortkey());
		
		FieldLabel iconographyFieldLabel = new FieldLabel(iconographySpinnerField, "Matching elements");
		iconographyFieldLabel.setLabelWidth(120);
		
		BorderLayoutContainer iconographyBLC = new BorderLayoutContainer();
		iconographyBLC.setSouthWidget(iconographyFieldLabel, new BorderLayoutData(25));
		iconographyBLC.setCenterWidget(icoSelectionLV, new MarginData(2));
		
		ContentPanel iconographyPanel = new ContentPanel();
		iconographyPanel.setHeaderVisible(true);
		iconographyPanel.setHeading("Iconography search");
		iconographyPanel.add(iconographyBLC);
		iconographyPanel.getHeader().setStylePrimaryName("frame-header");

		new DropTarget(iconographyPanel) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof DepictionEntry) {
					DepictionEntry de = (DepictionEntry) event.getData();
					selectedIconographyLS.clear();
					selectedIconographyLS.addAll(de.getRelatedIconographyList());
//					icoSelector.setSelectedIconography(de.getRelatedIconographyList());
					if ((de.getRelatedIconographyList() != null) && (selectedIconographyLS.size() > 0)) {
						iconographySpinnerField.setEnabled(true);
						iconographySpinnerField.setValue(selectedIconographyLS.size());
						iconographySpinnerField.setMaxValue(selectedIconographyLS.size());
					} else {
						iconographySpinnerField.setEnabled(false);
					}
				}
			}
			
		};
		
		ToolButton selectorTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		selectorTB.setToolTip(Util.createToolTip("Open Iconography & Pictorial Element selection"));
		selectorTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				showIconographySelection();
			}
		});
		iconographyPanel.addTool(selectorTB);

		ToolButton iconographySelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		iconographySelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		iconographySelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				selectedIconographyLS.clear();
				icoSelector.resetSelection();
			}
		});
		iconographyPanel.addTool(iconographySelectionResetTB);

		//WallTree
		wallSelectionLV = new ListView<WallTreeEntry, WallTreeEntry>(selectedWallsLS, new IdentityValueProvider<WallTreeEntry>(), new SimpleSafeHtmlCell<WallTreeEntry>(new AbstractSafeHtmlRenderer<WallTreeEntry>() {

			@Override
			public SafeHtml render(WallTreeEntry item) {
				WallTreeViewTemplates wtTemplates = GWT.create(WallTreeViewTemplates.class);
				ArrayList<NameElement> name = new ArrayList<NameElement>();
				for (String s : item.getText().split(" ")) {
					name.add(new NameElement(s));
				}
				return wtTemplates.iconographyLabel(name);
			}
		}));
		
		wallSpinnerField = new IntegerSpinnerField();
		wallSpinnerField.setMinValue(1);
		wallSpinnerField.setIncrement(1);
		wallSpinnerField.setEnabled(false);
		wallSpinnerField.setEditable(false);
		wallSpinnerField.addKeyPressHandler(getShortkey());
		
		FieldLabel wallFieldLabel = new FieldLabel(wallSpinnerField, "Matching elements");
		wallFieldLabel.setLabelWidth(120);
		
		BorderLayoutContainer wallBLC = new BorderLayoutContainer();
		wallBLC.setSouthWidget(wallFieldLabel, new BorderLayoutData(25));
		wallBLC.setCenterWidget(wallSelectionLV, new MarginData(2));
		
		ContentPanel wallPanel = new ContentPanel();
		wallPanel.setHeaderVisible(true);
		wallPanel.setHeading("Wall search");
		wallPanel.add(wallBLC);
		wallPanel.getHeader().setStylePrimaryName("frame-header");

		new DropTarget(wallPanel) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof DepictionEntry) {
					DepictionEntry de = (DepictionEntry) event.getData();
					selectedWallsLS.clear();
					selectedWallsLS.addAll(de.getWalls());
//					icoSelector.setSelectedIconography(de.getRelatedIconographyList());
					if ((de.getWalls() != null) && (selectedWallsLS.size() > 0)) {
						wallSpinnerField.setEnabled(true);
						wallSpinnerField.setMaxValue(selectedWallsLS.size());
					} else {
						wallSpinnerField.setEnabled(false);
					}
				}
			}
			
		};
		
		ToolButton wallSelectorTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		wallSelectorTB.setToolTip(Util.createToolTip("Open Wall Element selection"));
		wallSelectorTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				showWallSelection();
			}
		});
		wallPanel.addTool(wallSelectorTB);

		ToolButton wallSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		wallSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		wallSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				selectedWallsLS.clear();
			}
		});
		wallPanel.addTool(wallSelectionResetTB);
		// position

		positionSelectionLV = new ListView<PositionEntry, PositionEntry>(positionEntryList,
				new IdentityValueProvider<PositionEntry>(),
				new SimpleSafeHtmlCell<PositionEntry>(new AbstractSafeHtmlRenderer<PositionEntry>() {
					final PositionViewTemplates ocvTemplates = GWT.create(PositionViewTemplates.class);

					@Override
					public SafeHtml render(PositionEntry entry) {
						return ocvTemplates.positionLabel(entry.getName());
					}

				}));
		positionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		positionSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

		ContentPanel positionPanel = new ContentPanel();
		positionPanel.setHeaderVisible(true);
		positionPanel
				.setToolTip(Util.createToolTip("Search for positions.", "Select one or more elements to search for Ornamentations."));
		positionPanel.setHeading("Position");
		positionPanel.add(positionSelectionLV);
		positionPanel.getHeader().setStylePrimaryName("frame-header");

		ToolButton resetOrnamentPositionPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentPositionPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentPositionPanelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				positionSelectionLV.getSelectionModel().deselectAll();
			}
		});
		positionPanel.addTool(resetOrnamentPositionPanelTB);

		/**
		 * assemble shortNameSearchTF
		 */
		
		shortNameSearchTF = new TextField();
		shortNameSearchTF.setEmptyText("search short name");
		shortNameSearchTF.addKeyPressHandler(getShortkey());

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
//				String label;
//				if ((item.getCounty() != null) && (!item.getCounty().isEmpty())) {
//					if ((item.getTown() != null) && (!item.getTown().isEmpty())) {
//						label = item.getName()+", " + (item.getRegion()!=null && !item.getRegion().isEmpty() ? item.getTown()+", " + item.getRegion() : item.getTown()) + ", " + item.getCounty();
//					} else if ((item.getRegion() != null) && (!item.getRegion().isEmpty())) {
//						label = item.getName() +", "+ (item.getTown()!=null && !item.getTown().isEmpty() ? item.getTown()+", "+ item.getRegion() : item.getRegion()) + ", " + item.getCounty();
//					} else {
//						label = item.getName() + ", " + item.getCounty();
//					}
//				} else {
//					label = item.getName();
//				}
				if (item.getTown() != null && !item.getTown().isEmpty()) {
					return lvTemplates.locationLabel(item.getTown(), item.getName());
				} else {
					return lvTemplates.locationLabel(item.getName());
				}
			}
		}));
		locationSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		locationSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());
		
		ContentPanel currentLocationPanel = new ContentPanel();
		currentLocationPanel.setHeaderVisible(true);
		currentLocationPanel.setHeading("Location search");
		currentLocationPanel.add(locationSelectionLV);
		currentLocationPanel.getHeader().setStylePrimaryName("frame-header");
		
		ToolButton locationSelectionResetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		locationSelectionResetTB.setToolTip(Util.createToolTip("Reset selection"));
		locationSelectionResetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				locationSelectionLV.getSelectionModel().deselectAll();
			}
		});
		currentLocationPanel.addTool(locationSelectionResetTB);
		
		/**
		 * create the view
		 */
		
		AccordionLayoutContainer depictionFilterALC = new AccordionLayoutContainer();
    depictionFilterALC.setExpandMode(ExpandMode.SINGLE_FILL);
    depictionFilterALC.add(cavePanel);
    depictionFilterALC.add(currentLocationPanel);
    depictionFilterALC.add(iconographyPanel);
    depictionFilterALC.add(wallPanel);
    depictionFilterALC.add(positionPanel);

//    depictionFilterALC.setActiveWidget(cavePanel);

    BorderLayoutContainer depictionFilterBLC = new BorderLayoutContainer();
    depictionFilterBLC.setNorthWidget(shortNameSearchTF, new BorderLayoutData(20));
    depictionFilterBLC.setCenterWidget(depictionFilterALC, new MarginData(5, 0, 0, 0));
    depictionFilterBLC.setHeight(450);
    return depictionFilterBLC;
	}

	private void showIconographySelection() {
		if (extendedFilterDialog == null) {
			extendedFilterDialog = new PopupPanel();
			ToolButton closeTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
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
					invokeSearch();
				}
			});
			icoSelector.addTool(closeTB);
			extendedFilterDialog.add(icoSelector);
			extendedFilterDialog.setSize("750", "500");
			extendedFilterDialog.setModal(true);
		}
		extendedFilterDialog.center();
		ArrayList<IconographyEntry> list = new ArrayList<IconographyEntry>();
		list.addAll(selectedIconographyLS.getAll());
		icoSelector.setSelectedIconography(list);
	}
	private void showWallSelection() {
//		if (extendedWallFilterDialog == null) {
//			extendedWallFilterDialog = new PopupPanel();
//			ToolButton closeWallTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
//			closeWallTB.setToolTip(Util.createToolTip("Close selection.", "Currently selected items will be used in the filter."));
//			closeWallTB.addSelectHandler(new SelectHandler() {
//				
//				@Override
//				public void onSelect(SelectEvent event) {
//					selectedWallsLS.clear();
//					selectedWallsLS.addAll(wallTree.wallTree.getSelectionModel().getSelectedItems());
//					if ((wallTree.wallTree.getSelectionModel().getSelectedItems() != null) && (selectedWallsLS.size() > 0)) {
//						wallSpinnerField.setEnabled(true);
//						wallSpinnerField.setMaxValue(selectedWallsLS.size());
//					} else {
//						wallSpinnerField.setEnabled(false);
//					}
//					extendedWallFilterDialog.hide();
//					invokeSearch();
//				}
//			});
//			FramedPanel wallEditPanel = new FramedPanel();
//			wallEditPanel.addTool(closeWallTB);
//			Util.doLogging("Größe von wallTree: "+Integer.toString(wallTree.wallTree.getStore().getAllItemsCount()));
//			wallTree.wallTree.setVisible(true);
//			wallEditPanel.add(wallTree.wallSelectorBLC);
//			extendedWallFilterDialog.add(wallEditPanel);
//			extendedWallFilterDialog.setSize("750", "500");
//			extendedWallFilterDialog.setModal(true);
//		}
		pe = new PositionEditor(null, selectedWallsLS.getAll(), true) {
			@Override
			protected void save(List<WallTreeEntry> results ) {
				selectedWallsLS.clear();
				selectedWallsLS.addAll(getSelectedWalls());
				if ((pe.getSelectedItems() != null) && (selectedWallsLS.size() > 0)) {
					wallSpinnerField.setEnabled(true);
					wallSpinnerField.setValue(1);
					wallSpinnerField.setMaxValue(selectedWallsLS.size());
				} else {
					wallSpinnerField.setEnabled(false);
					wallSpinnerField.setValue(1);
				}

				invokeSearch();
				}
			};
		pe.selectChildren(true);
		pe.show();
//		extendedWallFilterDialog.center();
	}

	@Override
	public AbstractSearchEntry getSearchEntry() {
		DepictionSearchEntry searchEntry;
		if (UserLogin.isLoggedIn()) {
			searchEntry = new DepictionSearchEntry(UserLogin.getInstance().getSessionID());
		} else {
			searchEntry = new DepictionSearchEntry();
		}
		
		if (shortNameSearchTF.getValue() != null && !shortNameSearchTF.getValue().isEmpty()) {
			searchEntry.setShortName(shortNameSearchTF.getValue());
		}
		
		for (CaveEntry ce : caveSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getCaveIdList().add(ce.getCaveID());
		}
		if (imgIDs.size()>0) {
			for (int img : imgIDs) {
				searchEntry.getImageIdList().add(img);
			}	
		}
		if (bibIDs.size()>0) {
			for (int img : bibIDs) {
				searchEntry.getBibIdList().add(img);
			}	
		}
		
		for (LocationEntry le : locationSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getLocationIdList().add(le.getLocationID());
		}
		
		for (IconographyEntry ie : selectedIconographyLS.getAll()) {
			searchEntry.getIconographyIdList().add(ie.getIconographyID());
		}
		
		searchEntry.setCorrelationFactor(iconographySpinnerField.isEnabled() ? iconographySpinnerField.getValue() : 0);
		for (WallTreeEntry wte : selectedWallsLS.getAll()) {
			searchEntry.getWallIDList().add(wte.getWallLocationID());
		}
		for (PositionEntry oce : positionSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getPositionIDList().add(oce.getPositionID());
		}

		Util.doLogging(Boolean.toString(wallSpinnerField.isEnabled()));
		searchEntry.setCorrelationFactorWT(wallSpinnerField.isEnabled() ? wallSpinnerField.getValue() : 0);
		
		return searchEntry;
	}


}
