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
package de.cses.client.ornamentic;

import java.text.ParseException;
import java.util.ArrayList;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
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
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.IconographySelector;
import de.cses.client.ornamentic.OrnamenticEditor.OrnamentClassViewTemplates;
import de.cses.client.ornamentic.WallOrnamentCaveRelationEditor.OrnamentPositionViewTemplates;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamenticSearchEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.comparator.CaveEntryComparator;

/**
 * @author nina
 *
 */
public class OrnamenticFilter extends AbstractFilter {

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

	// Klasse zum erstellen aller Filter auf der Client Seite
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField ornamentCodeSearchTF;
	private TextField ornamentDeskriptionSearchTF;
	private TextField ornamentRemarksSearchTF;
	private TextField ornamentInterpretationSearchTF;
	private TextField ornamentReferencesSearchTF;
	private TextField ornamentOrnamentalGroupSearchTF;
	private TextField ornamentSimilaritiesSearchTF;
	private ListStore<IconographyEntry> selectedIconographyLS;
	private IconographySelector icoSelector;
	private ListView<IconographyEntry, IconographyEntry> icoSelectionLV;
	private IconographyProperties icoProps;
//	private IntegerSpinnerField iconographySpinnerField;
	private PopupPanel extendedFilterDialog = null;

	private ComboBox<OrnamentClassEntry> ornamentClassComboBox;
	private OrnamentClassProperties ornamentClassProps;
	private ListStore<OrnamentClassEntry> ornamentClassEntryList;

	private ListStore<OrnamentComponentsEntry> ornamentComponentsEntryList;
	private OrnamentComponentsProperties ornamentComponentsProps;
	private ListView<OrnamentComponentsEntry, OrnamentComponentsEntry> ornamentComponentsSelectionLV;

	private ListStore<CaveEntry> cavesEntryList;
	private CaveEntryProperties cavesProps;
	private ListView<CaveEntry, CaveEntry> cavesSelectionLV;

	private ListStore<DistrictEntry> districtsEntryList;
	private DistrictsProperties districtsProps;
	private ListView<DistrictEntry, DistrictEntry> districtsSelectionLV;


//	private ListStore<InnerSecondaryPatternsEntry> innerSecondaryPatternsEntryList;
//	private InnerSecondaryPatternsProperties innerSecondaryPatternsProps;
//	private ListView<InnerSecondaryPatternsEntry, InnerSecondaryPatternsEntry> innerSecondaryPatternsSelectionLV;

//	private ListStore<OrnamentEntry> relatedOrnamentsEntryList;
//	private RelatedOrnamentsProperties relatedOrnamentsProps;
//	private ListView<OrnamentEntry, OrnamentEntry> relatedOrnamentsSelectionLV;

	private ListStore<PositionEntry> positionEntryList;
	private PositionProperties positionProps;
	private ListView<PositionEntry, PositionEntry> positionSelectionLV;

	//private ListStore<OrnamentFunctionEntry> functionEntryList;
	//private FunctionProperties functionProps;
	//private ListView<OrnamentFunctionEntry, OrnamentFunctionEntry> functionSelectionLV;
	private SiteProperties siteProps;
	private ListStore<SiteEntry> siteEntryList;
	private ArrayList<Integer> imgIDs = new ArrayList<Integer>();
	private ArrayList<Integer> bibIDs = new ArrayList<Integer>();

	interface OrnamentClassProperties extends PropertyAccess<OrnamentClassEntry> {
		ModelKeyProvider<OrnamentClassEntry> ornamentClassID();

		LabelProvider<OrnamentClassEntry> name();
	}

	interface OrnamentComponentsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<OrnamentComponentsEntry> ornamentComponentsID();

		LabelProvider<OrnamentComponentsEntry> uniqueID();

		ValueProvider<OrnamentComponentsEntry, String> name();
	}

	interface InnerSecondaryPatternsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<InnerSecondaryPatternsEntry> innerSecondaryPatternsID();

		LabelProvider<InnerSecondaryPatternsEntry> uniqueID();

		ValueProvider<InnerSecondaryPatternsEntry, String> name();
	}

	interface CaveEntryProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> officialNumber();
	}

	interface DistrictsProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}

	interface RelatedOrnamentsProperties extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> ornamentID();
		LabelProvider<OrnamentEntry> uniqueID();
		ValueProvider<OrnamentEntry, String> code();
	}

	interface PositionProperties extends PropertyAccess<PositionEntry> {
		ModelKeyProvider<PositionEntry> PositionID();
		LabelProvider<PositionEntry> uniqueID();
		ValueProvider<PositionEntry, String> name();
	}

	interface FunctionProperties extends PropertyAccess<OrnamentFunctionEntry> {
		ModelKeyProvider<OrnamentFunctionEntry> ornamentFunctionID();
		LabelProvider<OrnamentFunctionEntry> uniqueID();
		ValueProvider<OrnamentFunctionEntry, String> name();
	}

	interface IconographyProperties extends PropertyAccess<IconographyEntry> {
		ModelKeyProvider<IconographyEntry> iconographyID();
		LabelProvider<IconographyEntry> uniqueID();
		ValueProvider<IconographyEntry, String> name();
	}

	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> officialNumber();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {districtRegion}<br><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion, ArrayList<NameElement> name);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {districtRegion}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String districtRegion);
	}

	interface OrnamentComponentsViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentComponentsLabel(String name);
	}

	interface InnerSecondaryPatternsViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml innerSecondaryPatternsLabel(String name);
	}

	// interface CaveViewTemplates extends XTemplates {
	// @XTemplate("<div>{officialNumber}: {historicName}</div>")
	// SafeHtml caveLabel(String officialNumber, String historicName);
	//
	// @XTemplate("<div>{officialNumber}</div>")
	// SafeHtml caveLabel(String officialNumber);
	// }
	//

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();

		LabelProvider<DistrictEntry> uniqueID();

		ValueProvider<DistrictEntry, String> name();
	}

	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{siteName}<br>{districtName}</div>")
		SafeHtml districtLabel(String districtName, String siteName);
	}

	// interface DistrictsViewTemplates extends XTemplates {
	// @XTemplate("<div>{name}</div>")
	// SafeHtml districtsLabel(String name);
	// }

	interface RelatedOrnamentsViewTemplates extends XTemplates {
		@XTemplate("<div>{code}</div>")
		SafeHtml relatedOrnamentsLabel(String code);
	}
	interface IconographyTreeViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\"><tpl for='name'> {element}<wbr> </tpl></div>")
		SafeHtml iconographyLabel(ArrayList<NameElement> name);
	}

	interface IconographyViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml iconographyLabel(String name);
	}

	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionLabel(String name);
	}

	interface FunctionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml functionLabel(String name);
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

	public OrnamenticFilter(String filterName) {
		super(filterName);

		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		ornamentComponentsEntryList = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());
		loadOrnamentComponentsEntryList();

//		innerSecondaryPatternsProps = GWT.create(InnerSecondaryPatternsProperties.class);
//		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(innerSecondaryPatternsProps.innerSecondaryPatternsID());
//		loadInnerSecondaryPatternsEntryList();

		cavesProps = GWT.create(CaveEntryProperties.class);
		cavesEntryList = new ListStore<CaveEntry>(cavesProps.caveID());
		loadCaveEntryList();

		districtsProps = GWT.create(DistrictsProperties.class);
		districtsEntryList = new ListStore<DistrictEntry>(districtsProps.districtID());
		loadDistrictEntryList();

		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		loadSites();

//		relatedOrnamentsProps = GWT.create(RelatedOrnamentsProperties.class);
//		relatedOrnamentsEntryList = new ListStore<OrnamentEntry>(relatedOrnamentsProps.ornamentID());
//		loadOrnamentEntryList();

//		functionProps = GWT.create(FunctionProperties.class);
//		functionEntryList = new ListStore<OrnamentFunctionEntry>(functionProps.ornamentFunctionID());
//		loadFunctionEntryList();

		positionProps = GWT.create(PositionProperties.class);
		positionEntryList = new ListStore<PositionEntry>(positionProps.PositionID());
		loadPositionEntryList();

		ornamentClassProps = GWT.create(OrnamentClassProperties.class);
		ornamentClassEntryList = new ListStore<OrnamentClassEntry>(ornamentClassProps.ornamentClassID());
		loadOrnamentClassEntryList();
		icoSelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values());
		icoSelector.enable();
		icoSelector.selectNoParents(true);
		icoProps = GWT.create(IconographyProperties.class);
		selectedIconographyLS = new ListStore<>(icoProps.iconographyID());

	}
	@Override
	public void clear() {
		ornamentCodeSearchTF.clear();
		ornamentDeskriptionSearchTF.clear();
		ornamentRemarksSearchTF.clear();;
		ornamentInterpretationSearchTF.clear();;
		ornamentReferencesSearchTF.clear();;
		ornamentOrnamentalGroupSearchTF.clear();;
		ornamentSimilaritiesSearchTF.clear();;
		icoSelectionLV.getSelectionModel().deselectAll();;
		ornamentClassComboBox.clear();;
		ornamentComponentsSelectionLV.getSelectionModel().deselectAll();;
		cavesEntryList.clear();;
		cavesSelectionLV.getSelectionModel().deselectAll();;
		districtsSelectionLV.getSelectionModel().deselectAll();;
		positionSelectionLV.getSelectionModel().deselectAll();;
		imgIDs.clear();
		bibIDs.clear();

	}

	// Laden aller Daten aus der Datenbank
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

	private void loadOrnamentComponentsEntryList() {
		dbService.getOrnamentComponents(new AsyncCallback<ArrayList<OrnamentComponentsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentComponentsEntry> result) {
				ornamentComponentsEntryList.clear();
				for (OrnamentComponentsEntry pe : result) {
					ornamentComponentsEntryList.add(pe);
				}
			}
		});
	}

	private void loadCaveEntryList() {
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				cavesEntryList.clear();
				for (CaveEntry pe : result) {
					cavesEntryList.add(pe);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void loadSites() {
		for (SiteEntry se : StaticTables.getInstance().getSiteEntries().values()) {
			siteEntryList.add(se);
		}
	}

	private void loadDistrictEntryList() {
		for (DistrictEntry de : StaticTables.getInstance().getDistrictEntries().values()) {
			districtsEntryList.add(de);
		}
		// dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ArrayList<DistrictEntry> result) {
		// districtsEntryList.clear();
		// for (DistrictEntry pe : result) {
		// districtsEntryList.add(pe);
		// }
		// }
		// });
	}

//	private void loadOrnamentEntryList() {
//		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ArrayList<OrnamentEntry> result) {
//				relatedOrnamentsEntryList.clear();
//				for (OrnamentEntry pe : result) {
//					relatedOrnamentsEntryList.add(pe);
//				}
//			}
//		});
//	}

//	private void loadFunctionEntryList() {
//		dbService.getOrnamentFunctions(new AsyncCallback<ArrayList<OrnamentFunctionEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ArrayList<OrnamentFunctionEntry> result) {
//				functionEntryList.clear();
//				for (OrnamentFunctionEntry pe : result) {
//					functionEntryList.add(pe);
//				}
//			}
//		});
//	}

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
	public void setSearchEntry(AbstractSearchEntry searchEntry, boolean reset) {
		// Versenden der Eintraege an den Server nach erfolgter Suche
		if (reset) {
			clear();
		}
		if (((OrnamenticSearchEntry)searchEntry).getCaves()!=null) {
			if (((OrnamenticSearchEntry)searchEntry).getCaves().isEmpty()) {
				for (CaveEntry ce : ((OrnamenticSearchEntry)searchEntry).getCaves()) {
					cavesSelectionLV.getSelectionModel().select(true, ce);
				}
			}
		}
		if (!((OrnamenticSearchEntry)searchEntry).getCode().isEmpty()) {
			ornamentCodeSearchTF.setValue(((OrnamenticSearchEntry)searchEntry).getCode(), true);
		}
		if (!((OrnamenticSearchEntry)searchEntry).getDescription().isEmpty()) {
			ornamentDeskriptionSearchTF.setValue(((OrnamenticSearchEntry)searchEntry).getDescription(), true);
		}
		if (!((OrnamenticSearchEntry)searchEntry).getInterpretation().isEmpty()) {
			ornamentInterpretationSearchTF.setValue(((OrnamenticSearchEntry)searchEntry).getInterpretation(), true);
		}
		if ((((OrnamenticSearchEntry)searchEntry).getComponents()!=null)&&(!((OrnamenticSearchEntry)searchEntry).getComponents().isEmpty())) {
			for (OrnamentComponentsEntry oce :((OrnamenticSearchEntry)searchEntry).getComponents()) {
				ornamentComponentsSelectionLV.getSelectionModel().select(oce, true);
			}
		}
		if ((((OrnamenticSearchEntry)searchEntry).getImageIDList()!=null)&&(!((OrnamenticSearchEntry)searchEntry).getImageIDList().isEmpty())) {
			for (Integer imgID :((OrnamenticSearchEntry)searchEntry).getImageIDList()) {
				imgIDs.add(imgID);
			}
		}

		if ((((OrnamenticSearchEntry)searchEntry).getBibIdList()!=null)&&(((OrnamenticSearchEntry)searchEntry).getImageIDList().isEmpty())) {
			for (Integer bibID :((OrnamenticSearchEntry)searchEntry).getBibIdList()) {
				bibIDs.add(bibID);
			}
		}

		if ((((OrnamenticSearchEntry)searchEntry).getPosition()!=null)&&(((OrnamenticSearchEntry)searchEntry).getPosition().isEmpty())) {
			for (PositionEntry pe :((OrnamenticSearchEntry)searchEntry).getPosition()) {
				positionSelectionLV.getSelectionModel().select(pe, true);
			}
		}

		if ((((OrnamenticSearchEntry)searchEntry).getOrnamentClass()!=null)) {
			ornamentClassComboBox.setValue(((OrnamenticSearchEntry)searchEntry).getOrnamentClass());
		}

		if ((((OrnamenticSearchEntry)searchEntry).getIconography()!=null)&&(((OrnamenticSearchEntry)searchEntry).getIconography().isEmpty())) {
			for (IconographyEntry ie :((OrnamenticSearchEntry)searchEntry).getIconography()) {
				selectedIconographyLS.add(ie);
			}
		}

	}

//	private void loadInnerSecondaryPatternsEntryList() {
//		dbService.getInnerSecondaryPatterns(new AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ArrayList<InnerSecondaryPatternsEntry> result) {
//				innerSecondaryPatternsEntryList.clear();
//				for (InnerSecondaryPatternsEntry pe : result) {
//					innerSecondaryPatternsEntryList.add(pe);
//				}
//			}
//		});
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {

		// Erstellen der Felder und ListViews auf der Client Seite

		ornamentCodeSearchTF = new TextField();
		ornamentCodeSearchTF.setEmptyText("search ornament code");
		ornamentCodeSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		ornamentCodeSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());

		ornamentInterpretationSearchTF = new TextField();
		ornamentInterpretationSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentInterpretationSearchTF.setEmptyText("search ornament interpretation");
		ornamentInterpretationSearchTF.setToolTip(
				Util.createToolTip("search ornament interpretation", "Search for this character sequence in the ornament interpretation field."));

		ornamentDeskriptionSearchTF = new TextField();
		ornamentDeskriptionSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentDeskriptionSearchTF.setEmptyText("search ornament deskription");
		ornamentDeskriptionSearchTF.setToolTip(
				Util.createToolTip("search ornament deskription", "Search for this character sequence in the ornament deskription field."));

		ornamentOrnamentalGroupSearchTF = new TextField();
		ornamentOrnamentalGroupSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentOrnamentalGroupSearchTF.setEmptyText("search ornamental unit");
		ornamentOrnamentalGroupSearchTF
				.setToolTip(Util.createToolTip("search ornament unit", "Search for this character sequence in the ornament unit field."));

		ornamentReferencesSearchTF = new TextField();
		ornamentReferencesSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentReferencesSearchTF.setEmptyText("search ornament references");
		ornamentReferencesSearchTF
				.setToolTip(Util.createToolTip("search ornament references", "Search for this character sequence in the ornament code field."));

		ornamentRemarksSearchTF = new TextField();
		ornamentRemarksSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentRemarksSearchTF.setEmptyText("search ornament remarks");
		ornamentRemarksSearchTF
				.setToolTip(Util.createToolTip("search ornament remarks", "Search for this character sequence in the ornament remarks field."));

		ornamentSimilaritiesSearchTF = new TextField();
		ornamentSimilaritiesSearchTF.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentSimilaritiesSearchTF.setEmptyText("search similar ornaments");
		ornamentSimilaritiesSearchTF.setToolTip(Util.createToolTip("search similar ornaments or elements of other cultural areas",
				"Search for this character sequence in the similar ornaments field."));

		// Components
		ornamentComponentsSelectionLV = new ListView<OrnamentComponentsEntry, OrnamentComponentsEntry>(ornamentComponentsEntryList,
				new IdentityValueProvider<OrnamentComponentsEntry>(),
				new SimpleSafeHtmlCell<OrnamentComponentsEntry>(new AbstractSafeHtmlRenderer<OrnamentComponentsEntry>() {
					final OrnamentComponentsViewTemplates ocvTemplates = GWT.create(OrnamentComponentsViewTemplates.class);

					@Override
					public SafeHtml render(OrnamentComponentsEntry entry) {
						return ocvTemplates.ornamentComponentsLabel(entry.getName());
					}

				}));
		ornamentComponentsSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);

		ContentPanel ornamentComponentsPanel = new ContentPanel();
		ornamentComponentsPanel.setHeaderVisible(true);
		ornamentComponentsPanel
				.setToolTip(Util.createToolTip("Search for ornament components.", "Select one or more elements to search for Ornamentations."));
		ornamentComponentsPanel.setHeading("Ornament Components");
		ornamentComponentsPanel.add(ornamentComponentsSelectionLV);
		ornamentComponentsPanel.getHeader().setStylePrimaryName("frame-header");

		ToolButton resetOrnamentComponentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentComponentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentComponentsPanelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				ornamentComponentsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentComponentsPanel.addTool(resetOrnamentComponentsPanelTB);

		// caves
		// cavesSelectionLV = new ListView<CaveEntry, CaveEntry>(cavesEntryList, new IdentityValueProvider<CaveEntry>(),
		// new SimpleSafeHtmlCell<CaveEntry>(new AbstractSafeHtmlRenderer<CaveEntry>() {
		// final CaveViewTemplates ocvTemplates = GWT.create(CaveViewTemplates.class);
		//
		// @Override
		// public SafeHtml render(CaveEntry entry) {
		// return ocvTemplates.caveLabel(Integer.toString(entry.getCaveID()));
		// }
		//
		// }));

		/**
		 * assemble caveSelection
		 */
		cavesSelectionLV = new ListView<CaveEntry, CaveEntry>(cavesEntryList, new IdentityValueProvider<CaveEntry>(),
				new SimpleSafeHtmlCell<CaveEntry>(new AbstractSafeHtmlRenderer<CaveEntry>() {

					@Override
					public SafeHtml render(CaveEntry entry) {
						final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
						StaticTables st = StaticTables.getInstance();
						DistrictEntry de = st.getDistrictEntries().get(entry.getDistrictID());
						SiteEntry se = st.getSiteEntries().get(entry.getSiteID());
						RegionEntry re = st.getRegionEntries().get(entry.getRegionID());
						String districtRegionInformation = (de != null) ? de.getName() + (re != null ? " / " + re.getOriginalName() : "")
								: (re != null ? re.getOriginalName() : "");
						if ((entry.getHistoricName() != null) && (entry.getHistoricName().length() > 0)) {
							ArrayList<NameElement> historicNameList = new ArrayList<NameElement>();
							for (String s : entry.getHistoricName().split(" ")) {
								historicNameList.add(new NameElement(s));
							}
							return cvTemplates.caveLabel(se != null ? se.getShortName() : "", entry.getOfficialNumber(), districtRegionInformation,
									historicNameList);
						} else {
							return cvTemplates.caveLabel(se != null ? se.getShortName() : "", entry.getOfficialNumber(), districtRegionInformation);
						}
					}
				}));
		cavesSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		cavesSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());
		cavesSelectionLV.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<CaveEntry>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<CaveEntry> event) {
				cavesEntryList.applySort(false);
			}
		});
		cavesEntryList.addSortInfo(new StoreSortInfo<CaveEntry>(new CaveEntryComparator(), SortDir.ASC));

		ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);

		ContentPanel ornamentCavesPanel = new ContentPanel();
		ornamentCavesPanel.setHeaderVisible(true);
		ornamentCavesPanel.setToolTip(
				Util.createToolTip("Search for ornaments in specific caves.", "Select one or more elements to search for Ornamentations."));
		ornamentCavesPanel.setHeading("Caves");
		ornamentCavesPanel.add(cavesSelectionLV);
		ornamentCavesPanel.getHeader().setStylePrimaryName("frame-header");
		ornamentCavesPanel.addDomHandler(getShortkey(), KeyPressEvent.getType());

		ToolButton resetOrnamentCavesPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentCavesPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentCavesPanelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				cavesSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentCavesPanel.addTool(resetOrnamentCavesPanelTB);

		// Districts

		// districtsSelectionLV = new ListView<DistrictEntry, DistrictEntry>(districtsEntryList, new IdentityValueProvider<DistrictEntry>(),
		// new SimpleSafeHtmlCell<DistrictEntry>(new AbstractSafeHtmlRenderer<DistrictEntry>() {
		// final DistrictsViewTemplates ocvTemplates = GWT.create(DistrictsViewTemplates.class);
		//
		// @Override
		// public SafeHtml render(DistrictEntry entry) {
		// return ocvTemplates.districtsLabel(entry.getName());
		// }
		//
		// }));

		districtsSelectionLV = new ListView<DistrictEntry, DistrictEntry>(districtsEntryList, new IdentityValueProvider<DistrictEntry>(),
				new SimpleSafeHtmlCell<DistrictEntry>(new AbstractSafeHtmlRenderer<DistrictEntry>() {
					final DistrictViewTemplates dvTemplates = GWT.create(DistrictViewTemplates.class);

					@Override
					public SafeHtml render(DistrictEntry entry) {
						SiteEntry se = siteEntryList.findModelWithKey(Integer.toString(entry.getSiteID()));
						return dvTemplates.districtLabel(entry.getName(), se.getName());
					}
				}));
		districtsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		districtsSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

		cavesSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);

		ContentPanel ornamentdistrictsPanel = new ContentPanel();
		ornamentdistrictsPanel.setHeaderVisible(true);
		ornamentdistrictsPanel
				.setToolTip(Util.createToolTip("Search for districts.", "Select one or more elements to search for Ornamentations."));
		ornamentdistrictsPanel.setHeading("Districts");
		ornamentdistrictsPanel.add(districtsSelectionLV);
		ornamentdistrictsPanel.getHeader().setStylePrimaryName("frame-header");

		ToolButton resetDistrictsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetDistrictsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetDistrictsPanelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				districtsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentdistrictsPanel.addTool(resetDistrictsPanelTB);

		// seconpary patterns

//		innerSecondaryPatternsSelectionLV = new ListView<InnerSecondaryPatternsEntry, InnerSecondaryPatternsEntry>(
//				innerSecondaryPatternsEntryList, new IdentityValueProvider<InnerSecondaryPatternsEntry>(),
//				new SimpleSafeHtmlCell<InnerSecondaryPatternsEntry>(new AbstractSafeHtmlRenderer<InnerSecondaryPatternsEntry>() {
//					final InnerSecondaryPatternsViewTemplates ocvTemplates = GWT.create(InnerSecondaryPatternsViewTemplates.class);
//
//					@Override
//					public SafeHtml render(InnerSecondaryPatternsEntry entry) {
//						return ocvTemplates.innerSecondaryPatternsLabel(entry.getName());
//					}
//
//				}));
//		innerSecondaryPatternsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
//		innerSecondaryPatternsSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

//		ContentPanel innerSecPanel = new ContentPanel();
//		innerSecPanel.setHeaderVisible(true);
//		innerSecPanel.setToolTip(
//				Util.createToolTip("Search for inner secondary patterns.", "Select one or more elements to search for Ornamentations."));
//		innerSecPanel.setHeading("InnerSecondary Pattern");
//		innerSecPanel.add(innerSecondaryPatternsSelectionLV);
//		innerSecPanel.getHeader().setStylePrimaryName("frame-header");
//
//		ToolButton resetInnerSecPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
//		resetInnerSecPanelTB.setToolTip(Util.createToolTip("Reset selection"));
//		resetInnerSecPanelTB.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				innerSecondaryPatternsSelectionLV.getSelectionModel().deselectAll();
//			}
//		});
//		innerSecPanel.addTool(resetInnerSecPanelTB);

		// related ornaments
//		relatedOrnamentsSelectionLV = new ListView<OrnamentEntry, OrnamentEntry>(relatedOrnamentsEntryList,
//				new IdentityValueProvider<OrnamentEntry>(), new SimpleSafeHtmlCell<OrnamentEntry>(new AbstractSafeHtmlRenderer<OrnamentEntry>() {
//					final OrnamentViewTemplates ocvTemplates = GWT.create(OrnamentViewTemplates.class);
//
//					@Override
//					public SafeHtml render(OrnamentEntry entry) {
//						return ocvTemplates.ornament(entry.getCode());
//					}
//
//				}));
//		relatedOrnamentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
//		relatedOrnamentsSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

//		ContentPanel relatedornamentPanel = new ContentPanel();
//		relatedornamentPanel.setHeaderVisible(true);
//		relatedornamentPanel
//				.setToolTip(Util.createToolTip("Search for related ornaments.", "Select one or more elements to search for Ornamentations."));
//		relatedornamentPanel.setHeading("Related Ornaments");
//		relatedornamentPanel.add(relatedOrnamentsSelectionLV);
//		relatedornamentPanel.getHeader().setStylePrimaryName("frame-header");
//
//		ToolButton resetrelatedOrnamentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
//		resetrelatedOrnamentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
//		resetrelatedOrnamentsPanelTB.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				relatedOrnamentsSelectionLV.getSelectionModel().deselectAll();
//			}
//		});
//		relatedornamentPanel.addTool(resetrelatedOrnamentsPanelTB);

		// position

		positionSelectionLV = new ListView<PositionEntry, PositionEntry>(positionEntryList,
				new IdentityValueProvider<PositionEntry>(),
				new SimpleSafeHtmlCell<PositionEntry>(new AbstractSafeHtmlRenderer<PositionEntry>() {
					final OrnamentPositionViewTemplates ocvTemplates = GWT.create(OrnamentPositionViewTemplates.class);

					@Override
					public SafeHtml render(PositionEntry entry) {
						return ocvTemplates.ornamentPosition(entry.getName());
					}

				}));
		positionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		positionSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

		ContentPanel ornamentpositionPanel = new ContentPanel();
		ornamentpositionPanel.setHeaderVisible(true);
		ornamentpositionPanel
				.setToolTip(Util.createToolTip("Search for positions.", "Select one or more elements to search for Ornamentations."));
		ornamentpositionPanel.setHeading("Position");
		ornamentpositionPanel.add(positionSelectionLV);
		ornamentpositionPanel.getHeader().setStylePrimaryName("frame-header");

		ToolButton resetOrnamentPositionPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentPositionPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentPositionPanelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				positionSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentpositionPanel.addTool(resetOrnamentPositionPanelTB);

		// function

//		functionSelectionLV = new ListView<OrnamentFunctionEntry, OrnamentFunctionEntry>(functionEntryList,
//				new IdentityValueProvider<OrnamentFunctionEntry>(),
//				new SimpleSafeHtmlCell<OrnamentFunctionEntry>(new AbstractSafeHtmlRenderer<OrnamentFunctionEntry>() {
//					final OrnamentFunctionViewTemplates ocvTemplates = GWT.create(OrnamentFunctionViewTemplates.class);
//
//					@Override
//					public SafeHtml render(OrnamentFunctionEntry entry) {
//						return ocvTemplates.ornamentFunction(entry.getName());
//					}
//
//				}));
//		functionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
//		functionSelectionLV.addDomHandler(getShortkey(), KeyPressEvent.getType());

//		ContentPanel ornamentFunctionPanel = new ContentPanel();
//		ornamentFunctionPanel.setHeaderVisible(true);
//		ornamentFunctionPanel
//				.setToolTip(Util.createToolTip("Search for ornament functions.", "Select one or more elements to search for Ornamentations."));
//		ornamentFunctionPanel.setHeading("Ornament Function");
//		ornamentFunctionPanel.add(functionSelectionLV);
//		ornamentFunctionPanel.getHeader().setStylePrimaryName("frame-header");
//
//		ToolButton resetOrnamentFunctionPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
//		resetOrnamentFunctionPanelTB.setToolTip(Util.createToolTip("Reset selection"));
//		resetOrnamentFunctionPanelTB.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				functionSelectionLV.getSelectionModel().deselectAll();
//			}
//		});
//		ornamentFunctionPanel.addTool(resetOrnamentFunctionPanelTB);

		// iconographys? muesste noch bearbeitet werden, soll die Iconography hinzugefuegt werden. Hier bloss ein Platzhalter mit Copy-Paste
		/*
		 * ornamentComponentsSelectionLV = new ListView<OrnamentComponentsEntry, OrnamentComponentsEntry>(ornamentComponentsEntryList, new
		 * IdentityValueProvider<OrnamentComponentsEntry>(), new SimpleSafeHtmlCell<OrnamentComponentsEntry>(new AbstractSafeHtmlRenderer<OrnamentComponentsEntry>()
		 * { final OrnamentComponentsViewTemplates ocvTemplates = GWT.create(OrnamentComponentsViewTemplates.class);
		 * 
		 * @Override public SafeHtml render(OrnamentComponentsEntry entry) { return ocvTemplates.ornamentComponentsLabel(entry.getName()); }
		 * 
		 * })); ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		 * 
		 * ContentPanel ornamentComponentsPanel = new ContentPanel(); ornamentComponentsPanel.setHeaderVisible(true);
		 * ornamentComponentsPanel.setToolTip(Util.createToolTip("Search for ornament components.", "Select one or more elements to search for Ornamentations."));
		 * ornamentComponentsPanel.setHeading("Ornament Components"); ornamentComponentsPanel.add(ornamentComponentsSelectionLV);
		 * 
		 * ToolButton resetOrnamentComponentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		 * resetOrnamentComponentsPanelTB.setToolTip(Util.createToolTip("Reset selection")); resetOrnamentComponentsPanelTB.addSelectHandler(new SelectHandler() {
		 * 
		 * @Override public void onSelect(SelectEvent event) { ornamentComponentsSelectionLV.getSelectionModel().deselectAll(); } });
		 * ornamentComponentsPanel.addTool(resetOrnamentComponentsPanelTB);
		 * 
		 */
		// ornamentClass/Motif
		ornamentClassComboBox = new ComboBox<OrnamentClassEntry>(ornamentClassEntryList, ornamentClassProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentClassEntry>() {

					@Override
					public SafeHtml render(OrnamentClassEntry item) {
						final OrnamentClassViewTemplates pvTemplates = GWT.create(OrnamentClassViewTemplates.class);
						return pvTemplates.ornamentClass(item.getName());
					}
				});
		ornamentClassComboBox.setTypeAhead(true);
		ornamentClassComboBox.setEditable(false);
		ornamentClassComboBox.setTriggerAction(TriggerAction.ALL);
		ornamentClassComboBox.setEmptyText("select motif");
		ornamentClassComboBox.addDomHandler(getShortkey(), KeyPressEvent.getType());
		
		ContentPanel headerOrnamentClass = new ContentPanel();
		headerOrnamentClass.setHeading("Motif");
		headerOrnamentClass.add(ornamentClassComboBox);
		headerOrnamentClass.getHeader().setStylePrimaryName("frame-header");
		ToolButton resetOrnamentClassComboBoxTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentClassComboBoxTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				ornamentClassComboBox.clear();
			}
		});
		headerOrnamentClass.addTool(resetOrnamentClassComboBoxTB);

//		ContentPanel ornamentCodePanel = new ContentPanel();
//		ornamentCodePanel.setHeaderVisible(true);
//		ornamentCodePanel.setToolTip(Util.createToolTip("Search for ornament codes."));
//		ornamentCodePanel.setHeading("Ornament Code");
//		ornamentCodePanel.add(ornamentCodeSearchTF);
//		ornamentCodePanel.getHeader().setStylePrimaryName("frame-header");
//		accordion.add(ornamentCodePanel);

		VerticalPanel ornamenticFilterVLC = new VerticalPanel();
		ornamenticFilterVLC.setHeight("100px");
		ContentPanel ornamentDeskriptionPanel = new ContentPanel();
		ornamentDeskriptionPanel.setHeaderVisible(true);
		ornamentDeskriptionPanel.setToolTip(Util.createToolTip("Search for ornament description."));
		ornamentDeskriptionPanel.setHeading("Description");
		ornamentDeskriptionPanel.add(ornamentDeskriptionSearchTF);
		ornamentDeskriptionPanel.getHeader().setStylePrimaryName("frame-header");
		ornamenticFilterVLC.add(ornamentDeskriptionPanel);
		ornamenticFilterVLC.addDomHandler(getShortkey(), KeyPressEvent.getType());

//		ContentPanel ornamentInterpretationPanel = new ContentPanel();
//		ornamentInterpretationPanel.setHeaderVisible(true);
//		ornamentInterpretationPanel.setToolTip(Util.createToolTip("Search for ornament interpretation."));
//		ornamentInterpretationPanel.setHeading("Ornament Interpretation");
//		ornamentInterpretationPanel.add(ornamentInterpretationSearchTF);
//		ornamentInterpretationPanel.getHeader().setStylePrimaryName("frame-header");
//		ornamenticFilterVLC.add(ornamentInterpretationPanel);

//		ContentPanel ornamentGroupPanel = new ContentPanel();
//		ornamentGroupPanel.setHeaderVisible(true);
//		ornamentGroupPanel.setToolTip(Util.createToolTip("Search for ornament unit."));
//		ornamentGroupPanel.setHeading("Ornament Unit");
//		ornamentGroupPanel.add(ornamentOrnamentalGroupSearchTF);
//		ornamentGroupPanel.getHeader().setStylePrimaryName("frame-header");
//		ornamenticFilterVLC.add(ornamentGroupPanel);

//		ContentPanel referencesPanel = new ContentPanel();
//		referencesPanel.setHeaderVisible(true);
//		referencesPanel.setToolTip(Util.createToolTip("Search for ornament references."));
//		referencesPanel.setHeading("Ornament References");
//		referencesPanel.add(ornamentReferencesSearchTF);
//		referencesPanel.getHeader().setStylePrimaryName("frame-header");
//		ornamenticFilterVLC.add(referencesPanel);

		ContentPanel remarksPanel = new ContentPanel();
		remarksPanel.setHeaderVisible(true);
		remarksPanel.setToolTip(Util.createToolTip("Search for ornament remarks."));
		remarksPanel.setHeading("Generell Remarks");
		remarksPanel.getHeader().setStylePrimaryName("frame-header");
		remarksPanel.add(ornamentRemarksSearchTF);
		ornamenticFilterVLC.add(remarksPanel);

//		ContentPanel similatitiesPanel = new ContentPanel();
//		similatitiesPanel.setHeaderVisible(true);
//		similatitiesPanel.setToolTip(Util.createToolTip("Search for similar ornaments or elements of other cultures."));
//		similatitiesPanel.setHeading("Similarities");
//		similatitiesPanel.add(ornamentSimilaritiesSearchTF);
//		ornamenticFilterVLC.add(similatitiesPanel);

		ContentPanel textSearch = new ContentPanel();
		textSearch.setHeading("Text Search");
		textSearch.add(ornamenticFilterVLC);
		textSearch.getHeader().setStylePrimaryName("frame-header");
		textSearch.addDomHandler(getShortkey(), KeyPressEvent.getType());

		AccordionLayoutContainer accordion = new AccordionLayoutContainer();
		accordion.setExpandMode(ExpandMode.SINGLE_FILL);

//		accordion.add(headerOrnamentClass);
		accordion.add(ornamentCavesPanel);
//		accordion.add(ornamentFunctionPanel);
		accordion.add(ornamentpositionPanel);
		accordion.add(ornamentdistrictsPanel);
//		accordion.add(innerSecPanel);
//		accordion.add(relatedornamentPanel);
		accordion.add(ornamentComponentsPanel);
		accordion.add(textSearch);

		VerticalLayoutContainer codeMotifVLC = new VerticalLayoutContainer();
		codeMotifVLC.addDomHandler(getShortkey(), KeyPressEvent.getType());
		codeMotifVLC.add(ornamentCodeSearchTF, new VerticalLayoutData(1.0, 25));
		codeMotifVLC.add(headerOrnamentClass, new VerticalLayoutData(1.0, 45));
		
		// accordion.setActiveWidget(ornamentCavesPanel);

		// iconography? accordion.add(iconographyPanel);
		BorderLayoutContainer ornamentFilterBLC = new BorderLayoutContainer();
		ornamentFilterBLC.addDomHandler(getShortkey(), KeyPressEvent.getType());
		ornamentFilterBLC.setNorthWidget(codeMotifVLC, new BorderLayoutData(70));
		ornamentFilterBLC.setCenterWidget(accordion, new MarginData(5, 0, 0, 0));
		ornamentFilterBLC.setHeight(600);
		icoSelectionLV = new ListView<IconographyEntry, IconographyEntry>(selectedIconographyLS, new IdentityValueProvider<IconographyEntry>(), new SimpleSafeHtmlCell<IconographyEntry>(new AbstractSafeHtmlRenderer<IconographyEntry>() {
			@Override
			public SafeHtml render(IconographyEntry item) {
				IconographyTreeViewTemplates icTemplates = GWT.create(IconographyTreeViewTemplates.class);
				ArrayList<NameElement> name = new ArrayList<NameElement>();
				for (String s : item.getText().split(" ")) {
					name.add(new NameElement(s));
				}
				return icTemplates.iconographyLabel(name);
			}
		}));
		
//		iconographySpinnerField = new IntegerSpinnerField();
//		iconographySpinnerField.setMinValue(1);
//		iconographySpinnerField.setIncrement(1);
//		iconographySpinnerField.setEnabled(false);
//		iconographySpinnerField.setEditable(false);
//		iconographySpinnerField.addKeyPressHandler(getShortkey());
		
//		FieldLabel iconographyFieldLabel = new FieldLabel(iconographySpinnerField, "Matching elements");
//		iconographyFieldLabel.setLabelWidth(120);
		
		BorderLayoutContainer iconographyBLC = new BorderLayoutContainer();
//		iconographyBLC.setSouthWidget(iconographyFieldLabel, new BorderLayoutData(25));
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
		ornamentFilterBLC.setSouthWidget(iconographyPanel);
		return ornamentFilterBLC;
	}

	/**
	 * In this method we assemble the search entry from the fields in the filter. Here we create the search entry that will be send to the server.
	 */
	@Override
	public AbstractSearchEntry getSearchEntry() {

		// Versenden der Eintraege an den Server nach erfolgter Suche
		OrnamenticSearchEntry searchEntry = new OrnamenticSearchEntry();
		if (UserLogin.isLoggedIn()) {
			searchEntry = new OrnamenticSearchEntry(UserLogin.getInstance().getSessionID());
		} else {
			searchEntry = new OrnamenticSearchEntry();
		}
	
		if (ornamentCodeSearchTF.getValue() != null && !ornamentCodeSearchTF.getValue().isEmpty()) {
			searchEntry.setCode(ornamentCodeSearchTF.getValue());
		}
		if (ornamentDeskriptionSearchTF.getValue() != null && !ornamentDeskriptionSearchTF.getValue().isEmpty()) {
			searchEntry.setDescription(ornamentDeskriptionSearchTF.getValue());
		}
		if (ornamentInterpretationSearchTF.getValue() != null && !ornamentInterpretationSearchTF.getValue().isEmpty()) {
			searchEntry.setInterpretation(ornamentInterpretationSearchTF.getValue());
		}
		if (ornamentOrnamentalGroupSearchTF.getValue() != null && !ornamentOrnamentalGroupSearchTF.getValue().isEmpty()) {
			searchEntry.setGroup(ornamentOrnamentalGroupSearchTF.getValue());
		}
		if (ornamentReferencesSearchTF.getValue() != null && !ornamentReferencesSearchTF.getValue().isEmpty()) {
			searchEntry.setReferences(ornamentReferencesSearchTF.getValue());
		}
		if (ornamentRemarksSearchTF.getValue() != null && !ornamentRemarksSearchTF.getValue().isEmpty()) {
			searchEntry.setRemarks(ornamentRemarksSearchTF.getValue());
		}
		if (ornamentSimilaritiesSearchTF.getValue() != null && !ornamentSimilaritiesSearchTF.getValue().isEmpty()) {
			searchEntry.setSimilaritys(ornamentSimilaritiesSearchTF.getValue());
		}

		for (OrnamentComponentsEntry oce : ornamentComponentsSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getComponents().add(oce);
		}

		for (Integer imgID : imgIDs) {
			searchEntry.getImageIDList().add(imgID);
		}

		for (Integer bibID : bibIDs) {
			searchEntry.getBibIdList().add(bibID);
		}

//		for (InnerSecondaryPatternsEntry oce : innerSecondaryPatternsSelectionLV.getSelectionModel().getSelectedItems()) {
//			searchEntry.getSecondarypatterns().add(oce);
//		}

		for (DistrictEntry oce : districtsSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getDistricts().add(oce);
		}

//		for (OrnamentEntry oce : relatedOrnamentsSelectionLV.getSelectionModel().getSelectedItems()) {
//			searchEntry.getRelatedOrnaments().add(oce);
//		}

		for (PositionEntry oce : positionSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getPosition().add(oce);
		}

//		for (OrnamentFunctionEntry oce : functionSelectionLV.getSelectionModel().getSelectedItems()) {
//			searchEntry.getFunction().add(oce);
//		}
		
		for (CaveEntry ce : cavesSelectionLV.getSelectionModel().getSelectedItems()) {
			searchEntry.getCaves().add(ce);
		}
		
		try {
			if (!(ornamentClassComboBox.getValueOrThrow() == null)) {
				searchEntry.setOrnamentClass(ornamentClassComboBox.getValue());
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (IconographyEntry ie : selectedIconographyLS.getAll()) {
			searchEntry.getIconography().add(ie);
		}
		


		return searchEntry;
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

}
