/*
 * Copyright 2016-2018
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
import java.util.Comparator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.caves.C14DocumentUploader.C14DocumentUploadListener;
import de.cses.client.caves.CaveSketchUploader.CaveSketchUploadListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.C14AnalysisUrlEntry;
import de.cses.shared.C14DocumentEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CaveSketchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallLocationEntry;

public class CaveEditor extends AbstractEditor {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private FramedPanel mainPanel;
	private CaveEntry correspondingCaveEntry;
	private ComboBox<CaveTypeEntry> caveTypeSelectionCB;
	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryListStore;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryListStore;
	private SiteProperties siteProps;
	private OrientationProperties orientationProps;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;
	private CaveTypeViewTemplates ctvTemplates;
	private CaveTypeViewTemplates ctvt;
	private PreservationClassificationViewTemplates pcvt;

	private TextField officialNumberField;
	private TextField historicalNameField;
	private TextField optionalHistoricNameField;
	private ComboBox<DistrictEntry> districtSelection;
	private ComboBox<RegionEntry> regionSelection;
	private TextArea stateOfPreservationTextArea;
	private ListStore<SiteEntry> siteEntryListStore;
	private StoreFilter<RegionEntry> regionFilter;
	private ListStore<OrientationEntry> orientationEntryList;
	private ComboBox<SiteEntry> siteSelection;
	private StoreFilter<DistrictEntry> districtFilter;
	protected Object siteEntryAccess;
	private TextField firstDocumentedByField;
	private NumberField<Integer> firstDocumentedInYearField;
	private TextArea findingsTextArea;
	private FlowLayoutContainer caveSketchFLC;
	private ComboBox<OrientationEntry> orientationSelection;
	private CeilingTypeProperties ceilingTypeProps;
	private ListStore<CeilingTypeEntry> ceilingTypeEntryList;
	private ComboBox<CeilingTypeEntry> rearAreaCeilingTypeSelector1;
	private ComboBox<CeilingTypeEntry> mainChamberCeilingTypeSelector1;
	private ComboBox<CeilingTypeEntry> antechamberCeilingTypeSelector1;
	private ComboBox<CeilingTypeEntry> corridorCeilingTypeSelector1;
	private ComboBox<CeilingTypeEntry> leftCorridorCeilingTypeSelector1;
	private ComboBox<CeilingTypeEntry> rightCorridorCeilingTypeSelector1;
	private PreservationClassificationProperties preservationClassificationProps;
	private ListStore<PreservationClassificationEntry> preservationClassificationEntryList;
	private ComboBox<PreservationClassificationEntry> rearAreaPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> rearAreaCeilingPreservationSelectorCB1;
	private ComboBox<PreservationClassificationEntry> mainChamberPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> mainChamberCeilingPreservationSelectorCB1;
	private ComboBox<PreservationClassificationEntry> antechamberPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> antechamberCeilingPreservationSelectorCB1;
	private ComboBox<PreservationClassificationEntry> corridorPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> corridorCeilingPreservationSelectorCB1;
	private ComboBox<PreservationClassificationEntry> leftCorridorPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> leftCorridorCeilingPreservationSelectorCB1;
	private ComboBox<PreservationClassificationEntry> rightCorridorPreservationSelectorCB;
	private ComboBox<PreservationClassificationEntry> rightCorridorCeilingPreservationSelectorCB1;
	private ComboBox<CaveGroupEntry> caveGroupSelector;
	private CaveGroupProperties caveGroupProps;
	private ListStore<CaveGroupEntry> caveGroupEntryList;
	private FramedPanel rearAreaCeilingTypeFP;
	private FramedPanel mainChamberCeilingTypeFP;
	private FramedPanel antechamberCeilingTypeFP;
	private FramedPanel corridorCeilingTypeFP;
	private FramedPanel leftCorridorCeilingTypeFP;
	private FramedPanel rightCorridorCeilingTypeFP;
	private FramedPanel rearAreaStateOfPreservationFP;
	private FramedPanel mainChamberStateOfPreservationFP;
	private FramedPanel antechamberStateOfPreservationFP;
	private FramedPanel rearAreaCeilingStateOfPreservationFP;
	private FramedPanel mainChamberCeilingStateOfPreservationFP;
	private ContentPanel antechamberCeilingStateOfPreservationFP;
	private FramedPanel leftCorridorStateOfPreservationFP;
	private FramedPanel leftCorridorCeilingStateOfPreservationFP;
	private FramedPanel rightCorridorStateOfPreservationFP;
	private FramedPanel rightCorridorCeilingStateOfPreservationFP;
	private FramedPanel corridorStateOfPreservationFP;
	private FramedPanel corridorCeilingStateOfPreservationFP;
	private ComboBox<CeilingTypeEntry> rearAreaCeilingTypeSelector2;
	private ComboBox<CeilingTypeEntry> mainChamberCeilingTypeSelector2;
	private ComboBox<CeilingTypeEntry> antechamberCeilingTypeSelector2;
	private ComboBox<CeilingTypeEntry> corridorCeilingTypeSelector2;
	private ComboBox<CeilingTypeEntry> leftCorridorCeilingTypeSelector2;
	private ComboBox<CeilingTypeEntry> rightCorridorCeilingTypeSelector2;
	private TextArea caveLayoutCommentsTextArea;
	private ComboBox<PreservationClassificationEntry> rearAreaCeilingPreservationSelectorCB2;
	private ComboBox<PreservationClassificationEntry> leftCorridorCeilingPreservationSelectorCB2;
	private ComboBox<PreservationClassificationEntry> rightCorridorCeilingPreservationSelectorCB2;
	private ComboBox<PreservationClassificationEntry> mainChamberCeilingPreservationSelectorCB2;
	private ComboBox<PreservationClassificationEntry> corridorCeilingPreservationSelectorCB2;
	private ComboBox<PreservationClassificationEntry> antechamberCeilingPreservationSelectorCB2;
	private TextArea notesTextArea;
	private FramedPanel c14UploadPanel;
	private DocumentLinkTemplate documentLinkTemplate;
	private ListStore<WallEntry> wallEntryLS;
	private ComboBox<WallEntry> wallSelectorCB;
	private WallProperties wallProps;
	private WallViewTemplate wallVT;
	protected WallEntry selectedWallEntry;
	private ComboBox<PreservationClassificationEntry> selectedWallStateOfPreservationCB;
	private FramedPanel wallManagementFP;
	private FramedPanel antechamberFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> antechamberFloorPreservationSelectorCB;
	private FramedPanel mainChamberFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> mainChamberFloorPreservationSelectorCB;
	private FramedPanel corridorFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> corridorFloorPreservationSelectorCB;
	private FramedPanel rearAreaFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> rearAreaFloorPreservationSelectorCB;
	private FramedPanel rearAreaLeftFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> leftCorridorFloorPreservationSelectorCB;
	private FramedPanel rearAreaRightFloorStateOfPreservationFP;
	private ComboBox<PreservationClassificationEntry> rightCorridorFloorPreservationSelectorCB;
	private FramedPanel leftCorridorFloorStateOfPreservationFP;
	private FramedPanel rightCorridorFloorStateOfPreservationFP;
	private FlowLayoutContainer c14AnalysisLinksFLC;
	private FlowLayoutContainer c14DocumentsFLC;
	protected C14DocumentUploader uploader;
	private NumberField<Double> wallWidthNF;
	private NumberField<Double> wallHeightNF;

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();

		LabelProvider<CaveTypeEntry> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}

	interface CeilingTypeProperties extends PropertyAccess<CeilingTypeEntry> {
		ModelKeyProvider<CeilingTypeEntry> ceilingTypeID();

		LabelProvider<CeilingTypeEntry> name();
	}

	interface CeilingTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ceilingTypeLabel(String name);
	}

	// interface CaveAreaProperties extends PropertyAccess<CaveAreaEntry> {
	// ModelKeyProvider<CaveAreaEntry> caveAreaID();
	// LabelProvider<CaveAreaEntry> caveAreaLabel();
	// }

	interface PreservationClassificationProperties extends PropertyAccess<PreservationClassificationEntry> {
		ModelKeyProvider<PreservationClassificationEntry> preservationClassificationID();

		LabelProvider<PreservationClassificationEntry> name();
	}

	interface PreservationClassificationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml preservationClassificationLabel(String name);
	}

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		ValueProvider<DistrictEntry, String> label();
		LabelProvider<DistrictEntry> name();
	}

	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml districtLabel(String name);
	}

	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		ValueProvider<RegionEntry, String> label();
		LabelProvider<RegionEntry> englishName();
	}

	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div>{phoneticName} ({originalName} / {englishName})</div>")
		SafeHtml regionLabel(String phoneticName, String originalName, String englishName);

		@XTemplate("<div>{englishName}</div>")
		SafeHtml regionLabel(String englishName);
	}

	interface SiteProperties extends PropertyAccess<SiteEntry> {
		ModelKeyProvider<SiteEntry> siteID();
		ValueProvider<SiteEntry, String> label();
		LabelProvider<SiteEntry> name();
	}

	interface SiteViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml siteLabel(String name);
	}

	interface OrientationProperties extends PropertyAccess<OrientationEntry> {
		ModelKeyProvider<OrientationEntry> orientationID();

		LabelProvider<OrientationEntry> name();
	}

	interface OrientationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml orientationLabel(String name);
	}

	interface CaveGroupProperties extends PropertyAccess<CaveGroupEntry> {
		ModelKeyProvider<CaveGroupEntry> caveGroupID();

		LabelProvider<CaveGroupEntry> name();
	}

	interface CaveGroupViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveGroupLabel(String name);
	}

	interface CaveLayoutViewTemplates extends XTemplates {
		// @XTemplate("<img align=\"center\" width=\"242\" height=\"440\" margin=\"20\" src=\"{imageUri}\">")
		@XTemplate("<img src=\"{imageUri}\" style=\"width: 280px; height: auto; align-content: center; margin: 5px;\">")
		SafeHtml image(SafeUri imageUri);
	}

	interface DocumentLinkTemplate extends XTemplates {
		@XTemplate("<a target=\"_blank\" href=\"{documentUri}\" rel=\"noopener\">click here to open {documentDescription}</a>")
		SafeHtml documentLink(SafeUri documentUri, String documentDescription);
	}

	interface WallProperties extends PropertyAccess<WallEntry> {
		ModelKeyProvider<WallEntry> wallLocationID();
	}

	interface WallViewTemplate extends XTemplates {
		@XTemplate("<div>{label}</div>")
		SafeHtml wallLabel(String label);
	}

	public CaveEditor(CaveEntry caveEntry) {
		if (caveEntry == null) {
			correspondingCaveEntry = new CaveEntry();
		} else {
			correspondingCaveEntry = caveEntry;
		}
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryListStore = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		ceilingTypeProps = GWT.create(CeilingTypeProperties.class);
		ceilingTypeEntryList = new ListStore<CeilingTypeEntry>(ceilingTypeProps.ceilingTypeID());
		Comparator<CeilingTypeEntry> ceilingTypeEntryComparator = new Comparator<CeilingTypeEntry>() {
			@Override
			public int compare(CeilingTypeEntry ct1, CeilingTypeEntry ct2) {
				return ct1.getName().toLowerCase().compareTo(ct2.getName().toLowerCase());
			}
		};
		ceilingTypeEntryList.addSortInfo(new StoreSortInfo<CeilingTypeEntry>(ceilingTypeEntryComparator, SortDir.ASC));
		preservationClassificationProps = GWT.create(PreservationClassificationProperties.class);
		preservationClassificationEntryList = new ListStore<PreservationClassificationEntry>(
				preservationClassificationProps.preservationClassificationID());
		siteProps = GWT.create(SiteProperties.class);
		siteEntryListStore = new ListStore<SiteEntry>(siteProps.siteID());
		siteEntryListStore.addSortInfo(new StoreSortInfo<SiteEntry>(siteProps.label(), SortDir.ASC));
		orientationProps = GWT.create(OrientationProperties.class);
		orientationEntryList = new ListStore<OrientationEntry>(orientationProps.orientationID());
		caveGroupProps = GWT.create(CaveGroupProperties.class);
		caveGroupEntryList = new ListStore<CaveGroupEntry>(caveGroupProps.caveGroupID());
		regionProps = GWT.create(RegionProperties.class);
		regionEntryListStore = new ListStore<RegionEntry>(regionProps.regionID());
		regionEntryListStore.addSortInfo(new StoreSortInfo<RegionEntry>(regionProps.label(), SortDir.ASC));
		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		districtEntryList.addSortInfo(new StoreSortInfo<DistrictEntry>(districtProps.label(), SortDir.ASC));
		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		documentLinkTemplate = GWT.create(DocumentLinkTemplate.class);
		ctvTemplates = GWT.create(CaveTypeViewTemplates.class);
		ctvt = GWT.create(CaveTypeViewTemplates.class);
		pcvt = GWT.create(PreservationClassificationViewTemplates.class);
		wallProps = GWT.create(WallProperties.class);
		wallVT = GWT.create(WallViewTemplate.class);
		wallEntryLS = new ListStore<WallEntry>(wallProps.wallLocationID());
		Comparator<WallEntry> wallEntryComparator = new Comparator<WallEntry>() {
			@Override
			public int compare(WallEntry we1, WallEntry we2) {
				return StaticTables.getInstance().getWallLocationEntries().get(we1.getWallLocationID()).getLabel()
						.compareTo(StaticTables.getInstance().getWallLocationEntries().get(we2.getWallLocationID()).getLabel());
			}
		};
		wallEntryLS.addSortInfo(new StoreSortInfo<WallEntry>(wallEntryComparator, SortDir.ASC));

		initPanel();
		loadCaveAndCeilingTypes();
		loadSites();
		loadDistricts();
		loadRegions();
		loadOrientation();
		loadCaveGroups();
	}

	private void refreshCaveSketchFLC(String caveTypeSketchName) {
		caveSketchFLC.clear();
		for (CaveSketchEntry cse : correspondingCaveEntry.getCaveSketchList()) {
			caveSketchFLC.add(
					new HTMLPanel(caveLayoutViewTemplates.image(UriUtils.fromString(
							"resource?cavesketch=" + cse.getCaveSketchFilename() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()))),
					new MarginData(5));
		}
		if ((caveTypeSketchName != null) && !caveTypeSketchName.isEmpty()) {
			caveSketchFLC.add(
					new HTMLPanel(caveLayoutViewTemplates.image(UriUtils
							.fromString("resource?background=" + caveTypeSketchName + UserLogin.getInstance().getUsernameSessionIDParameterForUri()))),
					new MarginData(5));
		}
	}

	/**
	 * 
	 * @param c14AnalysisUrlList
	 */
	private void refreshC14AnalysisLinksFLC(ArrayList<C14AnalysisUrlEntry> c14AnalysisUrlList) {
		c14AnalysisLinksFLC.clear();
		for (C14AnalysisUrlEntry c14aue : c14AnalysisUrlList) {
			c14AnalysisLinksFLC
					.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(c14aue.getC14Url()), c14aue.getC14ShortName())));
		}
	}

	/**
	 * @param c14DocumentList
	 */
	private void refreshC14DocumentsFLC(ArrayList<C14DocumentEntry> c14DocumentList) {
		c14DocumentsFLC.clear();
		for (C14DocumentEntry entry : c14DocumentList) {
			c14DocumentsFLC.add(new HTMLPanel(documentLinkTemplate.documentLink(
					UriUtils.fromString(
							"resource?document=" + entry.getC14DocumentName() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
					entry.getC14OriginalDocumentName())));
		}
	}

	/**
	 * 
	 */
	private void loadCaveAndCeilingTypes() {
		for (CaveTypeEntry pe : StaticTables.getInstance().getCaveTypeEntries().values()) {
			caveTypeEntryListStore.add(pe);
		}
		if (correspondingCaveEntry.getCaveTypeID() > 0) {
			CaveTypeEntry correspondingCaveTypeEntry = caveTypeEntryListStore
					.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID()));
			caveTypeSelectionCB.setValue(correspondingCaveTypeEntry);
			refreshCaveSketchFLC(correspondingCaveTypeEntry.getSketchName());
			updateCeilingTypePanel(correspondingCaveEntry.getCaveTypeID());
			updateStateOfPreservationPanel(correspondingCaveEntry.getCaveTypeID());
			updateWallList(correspondingCaveEntry.getCaveTypeID());
		}
		for (CeilingTypeEntry cte : StaticTables.getInstance().getCeilingTypeEntries().values()) {
			ceilingTypeEntryList.add(cte);
		}
		if (correspondingCaveEntry.getCaveTypeID() > 0) {
			rearAreaCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getCeilingType1());
			rearAreaCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getCeilingType2());

			leftCorridorCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getCeilingType1());
			leftCorridorCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getCeilingType2());

			rightCorridorCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getCeilingType1());
			rightCorridorCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getCeilingType2());

			mainChamberCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getCeilingType1());
			mainChamberCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getCeilingType2());

			corridorCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getCeilingType1());
			corridorCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getCeilingType2());

			antechamberCeilingTypeSelector1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getCeilingType1());
			antechamberCeilingTypeSelector2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getCeilingType2());
			rearAreaPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getPreservationClassification());
			rearAreaCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getCeilingPreservationClassification1());
			rearAreaCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getCeilingPreservationClassification2());
			rearAreaFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).getFloorPreservationClassification());

			leftCorridorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getPreservationClassification());
			leftCorridorCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getCeilingPreservationClassification1());
			leftCorridorCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getCeilingPreservationClassification2());
			leftCorridorFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).getFloorPreservationClassification());
		}

		for (PreservationClassificationEntry pce : StaticTables.getInstance().getPreservationClassificationEntries().values()) {
			preservationClassificationEntryList.add(pce);
		}
		rightCorridorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getPreservationClassification());
		rightCorridorCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getCeilingPreservationClassification1());
		rightCorridorCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getCeilingPreservationClassification2());
		rightCorridorFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).getFloorPreservationClassification());

		mainChamberPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getPreservationClassification());
		mainChamberCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getCeilingPreservationClassification1());
		mainChamberCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getCeilingPreservationClassification2());
		mainChamberFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).getFloorPreservationClassification());

		corridorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getPreservationClassification());
		corridorCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getCeilingPreservationClassification1());
		corridorCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getCeilingPreservationClassification2());
		corridorFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).getFloorPreservationClassification());

		antechamberPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getPreservationClassification());
		antechamberCeilingPreservationSelectorCB1.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getCeilingPreservationClassification1());
		antechamberCeilingPreservationSelectorCB2.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getCeilingPreservationClassification2());
		antechamberFloorPreservationSelectorCB.setValue(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).getFloorPreservationClassification());
	}

	/**
	 * 
	 */
	private void loadSites() {
		for (SiteEntry se : StaticTables.getInstance().getSiteEntries().values()) {
			siteEntryListStore.add(se);
		}
		if (correspondingCaveEntry.getSiteID() > 0) {
			siteSelection.setValue(siteEntryListStore.findModelWithKey(Integer.toString(correspondingCaveEntry.getSiteID())));
			activateRegionFilter();
			activateDistrictFilter();
		}
	}

	/**
	 * 
	 */
	private void loadOrientation() {
		orientationEntryList.clear();
		for (OrientationEntry entry : StaticTables.getInstance().getOrientationEntries().values()) {
			orientationEntryList.add(entry);
			if (correspondingCaveEntry.getOrientationID() > 0) {
				orientationSelection.setValue(orientationEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getOrientationID())));
			}
		}
	}

	/**
	 * 
	 */
	private void loadCaveGroups() {
		dbService.getCaveGroups(new AsyncCallback<ArrayList<CaveGroupEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Problem loading CaveGroupEntry");
			}

			@Override
			public void onSuccess(ArrayList<CaveGroupEntry> result) {
				caveGroupEntryList.clear();
				for (CaveGroupEntry entry : result) {
					caveGroupEntryList.add(entry);
					if (correspondingCaveEntry.getCaveGroupID() > 0) {
						caveGroupSelector.setValue(caveGroupEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveGroupID())));
					}
				}
			}
		});
	}

	/* 
	 * 
	 */
	private void loadRegions() {
		for (RegionEntry re : StaticTables.getInstance().getRegionEntries().values()) {
			regionEntryListStore.add(re);
		}
		if (correspondingCaveEntry.getRegionID() > 0) {
			RegionEntry re = regionEntryListStore.findModelWithKey(Integer.toString(correspondingCaveEntry.getRegionID()));
			regionSelection.setValue(re);
//			if (siteSelection.getCurrentValue() == null || siteSelection.getCurrentValue().getSiteID() != re.getSiteID()) {
//				siteSelection.setValue(StaticTables.getInstance().getSiteEntries().get(re.getSiteID()));
//				activateRegionFilter();
//				activateDistrictFilter();
//			}

		}
	}

	/**
	 * 
	 */
	private void loadDistricts() {
		for (DistrictEntry de : StaticTables.getInstance().getDistrictEntries().values()) {
			districtEntryList.add(de);
		}
		if (correspondingCaveEntry.getDistrictID() > 0) {
			final DistrictEntry de = districtEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getDistrictID()));
			districtSelection.setValue(de);
//			if (siteSelection.getCurrentValue() == null || siteSelection.getCurrentValue().getSiteID() != de.getSiteID()) {
//				SiteEntry se = siteEntryListStore.findModelWithKey(Integer.toString(de.getSiteID()));
//				siteSelection.setValue(se);
//				activateRegionFilter();
//				activateDistrictFilter();
//			}
		}
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		// all fields added are encapsulated by a FramedPanel

		// the tab will use the right 70% of the main HorizontalLayoutPanel
		HorizontalLayoutContainer mainHlContainer = new HorizontalLayoutContainer();

		// each column is represented by a VerticalLayoutPanel

		/**
		 * ------------------------- this is the first column on the left side -------------------------
		 */
		VerticalLayoutContainer mainInformationVLC = new VerticalLayoutContainer();

		FramedPanel officialNumberPanel = new FramedPanel();
		officialNumberPanel.setHeading("Official Number");
		officialNumberField = new TextField();
		officialNumberField.addValidator(new MinLengthValidator(1));
		officialNumberField.setEmptyText("mandatory cave number");
		officialNumberField.setAllowBlank(false);
		officialNumberField.setValue(correspondingCaveEntry.getOfficialNumber());
		officialNumberField.addValidator(new MinLengthValidator(1));
		officialNumberField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (officialNumberField.validate()) {
					correspondingCaveEntry.setOfficialNumber(event.getValue());
				}
			}
		});
		CheckBox openAccessCB = new CheckBox();
		openAccessCB.setBoxLabel("open access");
		openAccessCB.setValue(correspondingCaveEntry.isOpenAccess());
		openAccessCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setOpenAccess(event.getValue());
			}
		});
		VerticalLayoutContainer officialNumberVLC = new VerticalLayoutContainer();
		officialNumberVLC.add(officialNumberField, new VerticalLayoutData(1.0, .5));
		officialNumberVLC.add(openAccessCB, new VerticalLayoutData(1.0, .5));
		officialNumberPanel.add(officialNumberVLC);

		FramedPanel historicalNamePanel = new FramedPanel();
		historicalNamePanel.setHeading("Historical Name");
		historicalNameField = new TextField();
		historicalNameField.addValidator(new MaxLengthValidator(64));
		historicalNameField.setEmptyText("historic cave name");
		historicalNameField.setValue(correspondingCaveEntry.getHistoricName());
		historicalNameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (historicalNameField.validate()) {
					correspondingCaveEntry.setHistoricName(event.getValue());
				}
			}
		});
		historicalNamePanel.add(historicalNameField);

		FramedPanel optionalHistoricalNamePanel = new FramedPanel();
		optionalHistoricalNamePanel.setHeading("Optional Historical Name");
		optionalHistoricNameField = new TextField();
		optionalHistoricNameField.addValidator(new MaxLengthValidator(64));
		optionalHistoricNameField.setEmptyText("optional historic name");
		optionalHistoricNameField.setValue(correspondingCaveEntry.getOptionalHistoricName());
		optionalHistoricNameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (optionalHistoricNameField.validate()) {
					correspondingCaveEntry.setOptionalHistoricName(event.getValue());
				}
			}
		});
		optionalHistoricalNamePanel.add(optionalHistoricNameField);

		FramedPanel firstDocumentedByPanel = new FramedPanel();
		firstDocumentedByPanel.setHeading("First documented by");
		firstDocumentedByField = new TextField();
		firstDocumentedByField.setEmptyText("name");
		firstDocumentedByField.setValue(correspondingCaveEntry.getFirstDocumentedBy());
		firstDocumentedByField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setFirstDocumentedBy(event.getValue());
			}
		});
		firstDocumentedByPanel.add(firstDocumentedByField);

		FramedPanel firstDocumentedInYearFP = new FramedPanel();
		firstDocumentedInYearFP.setHeading("First documented in");
		firstDocumentedInYearField = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		firstDocumentedInYearField.setEmptyText("year");
		DateWrapper dw = new DateWrapper(); // we always want to use the current year!
		firstDocumentedInYearField.addValidator(new MaxNumberValidator<Integer>(dw.getFullYear()));
		firstDocumentedInYearField.setAllowNegative(false);
		if (correspondingCaveEntry.getFirstDocumentedInYear() > 0) {
			firstDocumentedInYearField.setValue(correspondingCaveEntry.getFirstDocumentedInYear());
		}
		firstDocumentedInYearField.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				if (firstDocumentedInYearField.validate()) {
					correspondingCaveEntry.setFirstDocumentedInYear(event.getValue());
				}
			}
		});
		firstDocumentedInYearFP.add(firstDocumentedInYearField);

		FramedPanel caveGroupPanel = new FramedPanel();
		caveGroupPanel.setHeading("Cave Group");
		caveGroupSelector = new ComboBox<CaveGroupEntry>(caveGroupEntryList, caveGroupProps.name(),
				new AbstractSafeHtmlRenderer<CaveGroupEntry>() {

					@Override
					public SafeHtml render(CaveGroupEntry item) {
						final CaveGroupViewTemplates cgvTemplates = GWT.create(CaveGroupViewTemplates.class);
						return cgvTemplates.caveGroupLabel(item.getName());
					}
				});
		caveGroupSelector.setTriggerAction(TriggerAction.ALL);
		caveGroupSelector.setEditable(false);
		caveGroupSelector.setTypeAhead(false);
		caveGroupSelector.addSelectionHandler(new SelectionHandler<CaveGroupEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveGroupEntry> event) {
				correspondingCaveEntry.setCaveGroupID(event.getSelectedItem().getCaveGroupID());
			}
		});
		caveGroupPanel.add(caveGroupSelector);
		
		ToolButton resetCaveGroupSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetCaveGroupSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		caveGroupPanel.addTool(resetCaveGroupSelectionTB);
		resetCaveGroupSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				caveGroupSelector.setValue(null, true);
			}
		});
		
		ToolButton newCaveGroupPlusTool = new ToolButton(ToolButton.PLUS);
		newCaveGroupPlusTool.setToolTip(Util.createToolTip("add Cave Group"));
		caveGroupPanel.addTool(newCaveGroupPlusTool);
		newCaveGroupPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addNewCaveGroupDialog = new PopupPanel();
				FramedPanel newCaveGroupFP = new FramedPanel();
				newCaveGroupFP.setHeading("Add Cave Group");
				TextField caveGroupNameField = new TextField();
				caveGroupNameField.addValidator(new MinLengthValidator(2));
				caveGroupNameField.addValidator(new MaxLengthValidator(32));
				caveGroupNameField.setValue("");
				caveGroupNameField.setWidth(200);
				newCaveGroupFP.add(caveGroupNameField);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (caveGroupNameField.validate()) {
							CaveGroupEntry cgEntry = new CaveGroupEntry();
							cgEntry.setName(caveGroupNameField.getCurrentValue());
							dbService.insertCaveGroupEntry(cgEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									loadCaveGroups();
								}
							});
							addNewCaveGroupDialog.hide();
						}
					}
				});
				newCaveGroupFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addNewCaveGroupDialog.hide();
					}
				});
				newCaveGroupFP.addButton(cancelButton);
				addNewCaveGroupDialog.add(newCaveGroupFP);
				addNewCaveGroupDialog.setModal(true);
				addNewCaveGroupDialog.center();
			}
		});

		FramedPanel sitePanel = new FramedPanel();
		sitePanel.setHeading("Site");
		siteSelection = new ComboBox<SiteEntry>(siteEntryListStore, siteProps.name(), new AbstractSafeHtmlRenderer<SiteEntry>() {

			@Override
			public SafeHtml render(SiteEntry item) {
				final SiteViewTemplates svTemplates = GWT.create(SiteViewTemplates.class);
				return svTemplates.siteLabel(item.getName());
			}
		});
		siteSelection.addValidator(new Validator<SiteEntry>() {

			@Override
			public List<EditorError> validate(Editor<SiteEntry> editor, SiteEntry value) {
				List<EditorError> l = new ArrayList<EditorError>();
				if (correspondingCaveEntry.getSiteID() == 0) {
					l.add(new DefaultEditorError(editor, "selecting a site is mandatory", value));
				}
				return l;
			}
		});
		siteSelection.setEmptyText("select site");
		siteSelection.setTypeAhead(false);
		siteSelection.setEditable(false);
		siteSelection.setWidth("100%");
		siteSelection.setTriggerAction(TriggerAction.ALL);
		siteSelection.addSelectionHandler(new SelectionHandler<SiteEntry>() {

			@Override
			public void onSelection(SelectionEvent<SiteEntry> event) {
				correspondingCaveEntry.setSiteID(event.getSelectedItem().getSiteID());
				activateRegionFilter();
				activateDistrictFilter();
			}
		});
		siteSelection.setWidth(250);
		sitePanel.add(siteSelection);

		FramedPanel districtPanel = new FramedPanel();
		districtPanel.setHeading("District");
		districtSelection = new ComboBox<DistrictEntry>(districtEntryList, districtProps.name(), new AbstractSafeHtmlRenderer<DistrictEntry>() {

			@Override
			public SafeHtml render(DistrictEntry item) {
				final DistrictViewTemplates dvTemplates = GWT.create(DistrictViewTemplates.class);
				return dvTemplates.districtLabel(item.getName());
			}
		});
		districtSelection.setEmptyText("select district");
		districtSelection.setTypeAhead(false);
		districtSelection.setEditable(false);
		districtSelection.setTriggerAction(TriggerAction.ALL);
		districtSelection.addSelectionHandler(new SelectionHandler<DistrictEntry>() {

			@Override
			public void onSelection(SelectionEvent<DistrictEntry> event) {
				correspondingCaveEntry.setDistrictID(event.getSelectedItem().getDistrictID());
			}
		});
		districtSelection.setWidth(250);
		if (correspondingCaveEntry.getCaveID() == 0) {
			districtSelection.setEnabled(false);
		}
		districtPanel.add(districtSelection);

		ToolButton resetDistrictSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetDistrictSelectionTB.setToolTip(Util.createToolTip("add District"));
		districtPanel.addTool(resetDistrictSelectionTB);
		resetDistrictSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				districtSelection.setValue(null, true);
			}
		});
		
		ToolButton newDistrictPlusTool = new ToolButton(ToolButton.PLUS);
		newDistrictPlusTool.setToolTip(Util.createToolTip("add District"));
		districtPanel.addTool(newDistrictPlusTool);
		newDistrictPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (siteSelection.getCurrentValue() == null) {
					Util.showWarning("A problem occurred", "Please select Site first!");
					return;
				}
				PopupPanel addNewDistrictDialog = new PopupPanel();
				FramedPanel newDistrictFP = new FramedPanel();
				newDistrictFP.setHeading("Add District in " + siteSelection.getCurrentValue().getName());
				DistrictEntry de = new DistrictEntry();
				de.setSiteID(siteSelection.getCurrentValue().getSiteID());
				VerticalLayoutContainer newDistrictVLC = new VerticalLayoutContainer();
				FramedPanel fp = new FramedPanel();
				fp.setHeading("District Name");
				TextField districtNameField = new TextField();
				districtNameField.addValidator(new MinLengthValidator(2));
				districtNameField.setValue("");
				fp.add(districtNameField);
				newDistrictVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("Descritpion");
				TextArea descriptionField = new TextArea();
				descriptionField.setValue("");
				fp.add(descriptionField);
				newDistrictVLC.add(fp, new VerticalLayoutData(1.0, 2.0 / 3));
				newDistrictFP.add(newDistrictVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (districtNameField.isValid()) {
							de.setName(districtNameField.getValue());
							de.setDescription(descriptionField.getValue().isEmpty() ? "" : descriptionField.getValue());
							dbService.insertDistrictEntry(de, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									de.setDistrictID(result);
									districtEntryList.add(de);
								}
							});
							addNewDistrictDialog.hide();
						}
					}
				});
				newDistrictFP.addButton(saveButton);
				TextButton closeButton = new TextButton("close");
				closeButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addNewDistrictDialog.hide();
					}
				});
				newDistrictFP.addButton(closeButton);
				newDistrictFP.setSize("250", "250");
				addNewDistrictDialog.add(newDistrictFP);
				addNewDistrictDialog.setModal(true);
				addNewDistrictDialog.center();
			}
		});

		FramedPanel regionPanel = new FramedPanel();
		regionPanel.setHeading("Region");
		regionSelection = new ComboBox<RegionEntry>(regionEntryListStore, regionProps.englishName(),
				new AbstractSafeHtmlRenderer<RegionEntry>() {

					@Override
					public SafeHtml render(RegionEntry item) {
						final RegionViewTemplates rvTemplates = GWT.create(RegionViewTemplates.class);
						if (!item.getPhoneticName().isEmpty() && !item.getOriginalName().isEmpty()) {
							return rvTemplates.regionLabel(item.getPhoneticName(), item.getOriginalName(), item.getEnglishName());
						} else {
							return rvTemplates.regionLabel(item.getEnglishName());
						}
					}
				});
		regionSelection.setEmptyText("select region");
		regionSelection.setTypeAhead(false);
		regionSelection.setEditable(false);
		regionSelection.setTriggerAction(TriggerAction.ALL);
		regionSelection.addSelectionHandler(new SelectionHandler<RegionEntry>() {

			@Override
			public void onSelection(SelectionEvent<RegionEntry> event) {
				correspondingCaveEntry.setRegionID(event.getSelectedItem().getRegionID());
			}
		});
		regionSelection.setWidth(250);
		if (correspondingCaveEntry.getCaveID() == 0) {
			regionSelection.setEnabled(false);
		}
		regionPanel.add(regionSelection);

		ToolButton resetRegionSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetRegionSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		regionPanel.addTool(resetRegionSelectionTB);
		resetRegionSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				regionSelection.setValue(null, true);
			}
		});

		ToolButton addRegionPlusTool = new ToolButton(ToolButton.PLUS);
		addRegionPlusTool.setToolTip(Util.createToolTip("add Region"));
		regionPanel.addTool(addRegionPlusTool);
		addRegionPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (siteSelection.getCurrentValue() == null) {
					Util.showWarning("A problem occurred", "Please select Site first!");
					return;
				}
				PopupPanel addNewRegionDialog = new PopupPanel();
				FramedPanel newRegionFP = new FramedPanel();
				newRegionFP.setHeading("Add Region in " + siteSelection.getCurrentValue().getName());
				RegionEntry re = new RegionEntry();
				re.setSiteID(siteSelection.getCurrentValue().getSiteID());
				VerticalLayoutContainer newRegionVLC = new VerticalLayoutContainer();
				FramedPanel fp = new FramedPanel();
				fp.setHeading("Phonetic Name");
				TextField phoneticNameField = new TextField();
				phoneticNameField.setValue("");
				fp.add(phoneticNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("Original Name");
				TextField originalNameField = new TextField();
				originalNameField.setValue("");
				fp.add(originalNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("English Name");
				TextField englishNameField = new TextField();
				englishNameField.setValue("");
				fp.add(englishNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				newRegionFP.add(newRegionVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (englishNameField.getValue().isEmpty()) {
							Util.showWarning("A problem occurred", "Please add at least an english name!");
						} else {
							re.setPhoneticName(phoneticNameField.getValue().isEmpty() ? "" : phoneticNameField.getValue());
							re.setOriginalName(originalNameField.getValue().isEmpty() ? "" : originalNameField.getValue());
							re.setEnglishName(englishNameField.getValue());
							dbService.insertRegionEntry(re, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									re.setRegionID(result);
									regionEntryListStore.add(re);
								}
							});
							addNewRegionDialog.hide();
						}
					}
				});
				newRegionFP.addButton(saveButton);
				TextButton closeButton = new TextButton("close");
				closeButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addNewRegionDialog.hide();
					}
				});
				newRegionFP.addButton(closeButton);
				newRegionFP.setSize("250", "250");
				addNewRegionDialog.add(newRegionFP);
				addNewRegionDialog.setModal(true);
				addNewRegionDialog.center();
			}
		});

		// assembling the left side
		mainInformationVLC.add(officialNumberPanel, new VerticalLayoutData(1.0, .15));
		mainInformationVLC.add(historicalNamePanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(optionalHistoricalNamePanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(firstDocumentedByPanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(firstDocumentedInYearFP, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(caveGroupPanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(sitePanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(districtPanel, new VerticalLayoutData(1.0, .1));
		mainInformationVLC.add(regionPanel, new VerticalLayoutData(1.0, .1));

		// and adding it to the main VLC
		mainHlContainer.add(mainInformationVLC, new HorizontalLayoutData(.3, 1.0));

		/**
		 * ------------------------------ the column with the text fields (state of preservation tab) ----------------------------
		 */

		rearAreaStateOfPreservationFP = new FramedPanel();
		rearAreaStateOfPreservationFP.setHeading("Rear Area");
		rearAreaPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		rearAreaPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton rearAreaStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rearAreaStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rearAreaStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rearAreaPreservationSelectorCB.setValue(null, true);
			}
		});
		rearAreaStateOfPreservationFP.addTool(rearAreaStateOfPreservationResetSelectionTB);
		rearAreaStateOfPreservationFP.add(rearAreaPreservationSelectorCB);

		rearAreaCeilingStateOfPreservationFP = new FramedPanel();
		rearAreaCeilingStateOfPreservationFP.setHeading("Rear Area");
		rearAreaCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		rearAreaCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		rearAreaCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		rearAreaCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer rearAreaCeilingPreservationHLC = new HorizontalLayoutContainer();
		rearAreaCeilingPreservationHLC.add(rearAreaCeilingPreservationSelectorCB1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		rearAreaCeilingPreservationHLC.add(rearAreaCeilingPreservationSelectorCB2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		ToolButton rearAreaCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rearAreaCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rearAreaCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rearAreaCeilingPreservationSelectorCB1.setValue(null, true);
				rearAreaCeilingPreservationSelectorCB2.setValue(null, true);
			}
		});
		rearAreaCeilingStateOfPreservationFP.addTool(rearAreaCeilingStateOfPreservationResetSelectionTB);
		rearAreaCeilingStateOfPreservationFP.add(rearAreaCeilingPreservationHLC);

		leftCorridorStateOfPreservationFP = new FramedPanel();
		leftCorridorStateOfPreservationFP.setHeading("Left Corridor");
		leftCorridorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		leftCorridorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton leftCorridorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		leftCorridorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		leftCorridorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				leftCorridorPreservationSelectorCB.setValue(null, true);
			}
		});
		leftCorridorStateOfPreservationFP.addTool(leftCorridorStateOfPreservationResetSelectionTB);
		leftCorridorStateOfPreservationFP.add(leftCorridorPreservationSelectorCB);

		leftCorridorCeilingStateOfPreservationFP = new FramedPanel();
		leftCorridorCeilingStateOfPreservationFP.setHeading("Left Corridor");
		leftCorridorCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		leftCorridorCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		leftCorridorCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		leftCorridorCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer leftCorridorCeilingPreservationHLC = new HorizontalLayoutContainer();
		leftCorridorCeilingPreservationHLC.add(leftCorridorCeilingPreservationSelectorCB1,
				new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		leftCorridorCeilingPreservationHLC.add(leftCorridorCeilingPreservationSelectorCB2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		ToolButton leftCorridorCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		leftCorridorCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		leftCorridorCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				leftCorridorCeilingPreservationSelectorCB1.setValue(null, true);
				leftCorridorCeilingPreservationSelectorCB2.setValue(null, true);
			}
		});
		leftCorridorCeilingStateOfPreservationFP.addTool(leftCorridorCeilingStateOfPreservationResetSelectionTB);
		leftCorridorCeilingStateOfPreservationFP.add(leftCorridorCeilingPreservationHLC);

		rightCorridorStateOfPreservationFP = new FramedPanel();
		rightCorridorStateOfPreservationFP.setHeading("Right Corridor");
		rightCorridorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		rightCorridorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton rightCorridorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rightCorridorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rightCorridorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rightCorridorPreservationSelectorCB.setValue(null, true);
			}
		});
		rightCorridorStateOfPreservationFP.addTool(rightCorridorStateOfPreservationResetSelectionTB);
		rightCorridorStateOfPreservationFP.add(rightCorridorPreservationSelectorCB);

		rightCorridorCeilingStateOfPreservationFP = new FramedPanel();
		rightCorridorCeilingStateOfPreservationFP.setHeading("Right Corridor");
		rightCorridorCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		rightCorridorCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		rightCorridorCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		rightCorridorCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer rightCorridorCeilingPreservationHLC = new HorizontalLayoutContainer();
		rightCorridorCeilingPreservationHLC.add(rightCorridorCeilingPreservationSelectorCB1,
				new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		rightCorridorCeilingPreservationHLC.add(rightCorridorCeilingPreservationSelectorCB2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		ToolButton rightCorridorCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rightCorridorCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rightCorridorCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rightCorridorCeilingPreservationSelectorCB1.setValue(null, true);
				rightCorridorCeilingPreservationSelectorCB2.setValue(null, true);
			}
		});
		rightCorridorCeilingStateOfPreservationFP.addTool(rightCorridorCeilingStateOfPreservationResetSelectionTB);
		rightCorridorCeilingStateOfPreservationFP.add(rightCorridorCeilingPreservationHLC);

		mainChamberStateOfPreservationFP = new FramedPanel();
		mainChamberStateOfPreservationFP.setHeading("Main Chamber");
		mainChamberPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		mainChamberPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton mainChamberStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		mainChamberStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		mainChamberStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				mainChamberPreservationSelectorCB.setValue(null, true);
			}
		});
		mainChamberStateOfPreservationFP.addTool(mainChamberStateOfPreservationResetSelectionTB);
		mainChamberStateOfPreservationFP.add(mainChamberPreservationSelectorCB);

		mainChamberCeilingStateOfPreservationFP = new FramedPanel();
		mainChamberCeilingStateOfPreservationFP.setHeading("Main Chamber");
		mainChamberCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		mainChamberCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		mainChamberCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		mainChamberCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer mainChamberCeilingPreservationHLC = new HorizontalLayoutContainer();
		mainChamberCeilingPreservationHLC.add(mainChamberCeilingPreservationSelectorCB1,
				new HorizontalLayoutData(0.5, 1.0, new Margins(0, 5, 0, 0)));
		mainChamberCeilingPreservationHLC.add(mainChamberCeilingPreservationSelectorCB2, new HorizontalLayoutData(0.5, 1.0, new Margins(0)));
		ToolButton mainChamberCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		mainChamberCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		mainChamberCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				mainChamberCeilingPreservationSelectorCB1.setValue(null, true);
				mainChamberCeilingPreservationSelectorCB2.setValue(null, true);			
			}
		});
		mainChamberCeilingStateOfPreservationFP.addTool(mainChamberCeilingStateOfPreservationResetSelectionTB);
		mainChamberCeilingStateOfPreservationFP.add(mainChamberCeilingPreservationHLC);

		corridorStateOfPreservationFP = new FramedPanel();
		corridorStateOfPreservationFP.setHeading("Main Corridor");
		corridorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		corridorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton corridorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		corridorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		corridorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				corridorPreservationSelectorCB.setValue(null, true);
			}
		});
		corridorStateOfPreservationFP.addTool(corridorStateOfPreservationResetSelectionTB);
		corridorStateOfPreservationFP.add(corridorPreservationSelectorCB);

		corridorCeilingStateOfPreservationFP = new FramedPanel();
		corridorCeilingStateOfPreservationFP.setHeading("Main Corridor");
		corridorCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		corridorCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		corridorCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		corridorCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer corridorCeilingPreservationHLC = new HorizontalLayoutContainer();
		corridorCeilingPreservationHLC.add(corridorCeilingPreservationSelectorCB1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		corridorCeilingPreservationHLC.add(corridorCeilingPreservationSelectorCB2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		ToolButton corridorCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		corridorCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		corridorCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				corridorCeilingPreservationSelectorCB1.setValue(null, true);
				corridorCeilingPreservationSelectorCB2.setValue(null, true);
			}
		});
		corridorCeilingStateOfPreservationFP.addTool(corridorCeilingStateOfPreservationResetSelectionTB);
		corridorCeilingStateOfPreservationFP.add(corridorCeilingPreservationHLC);

		antechamberStateOfPreservationFP = new FramedPanel();
		antechamberStateOfPreservationFP.setHeading("Antechamber");
		antechamberPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		antechamberPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton antechamberStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		antechamberStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		antechamberStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				antechamberPreservationSelectorCB.setValue(null, true);
			}
		});
		antechamberStateOfPreservationFP.addTool(antechamberStateOfPreservationResetSelectionTB);
		antechamberStateOfPreservationFP.add(antechamberPreservationSelectorCB);

		antechamberCeilingStateOfPreservationFP = new FramedPanel();
		antechamberCeilingStateOfPreservationFP.setHeading("Antechamber");
		antechamberCeilingPreservationSelectorCB1 = createStateOfPreservationSelector("state of preservation");
		antechamberCeilingPreservationSelectorCB1.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setCeilingPreservationClassification1(event.getSelectedItem());
			}
		});
		antechamberCeilingPreservationSelectorCB2 = createStateOfPreservationSelector("optional 2nd type");
		antechamberCeilingPreservationSelectorCB2.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setCeilingPreservationClassification2(event.getSelectedItem());
			}
		});
		HorizontalLayoutContainer antechamberCeilingPreservationHLC = new HorizontalLayoutContainer();
		antechamberCeilingPreservationHLC.add(antechamberCeilingPreservationSelectorCB1,
				new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		antechamberCeilingPreservationHLC.add(antechamberCeilingPreservationSelectorCB2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		ToolButton antechamberCeilingStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		antechamberCeilingStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		antechamberCeilingStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				antechamberCeilingPreservationSelectorCB1.setValue(null, true);
				antechamberCeilingPreservationSelectorCB2.setValue(null, true);
			}
		});
		antechamberCeilingStateOfPreservationFP.addTool(antechamberCeilingStateOfPreservationResetSelectionTB);
		antechamberCeilingStateOfPreservationFP.add(antechamberCeilingPreservationHLC);

		antechamberFloorStateOfPreservationFP = new FramedPanel();
		antechamberFloorStateOfPreservationFP.setHeading("Antechamber");
		antechamberFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		antechamberFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton antechamberFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		antechamberFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		antechamberFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				antechamberFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		antechamberFloorStateOfPreservationFP.addTool(antechamberFloorStateOfPreservationResetSelectionTB);
		antechamberFloorStateOfPreservationFP.add(antechamberFloorPreservationSelectorCB);

		mainChamberFloorStateOfPreservationFP = new FramedPanel();
		mainChamberFloorStateOfPreservationFP.setHeading("Main Chamber");
		mainChamberFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		mainChamberFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton mainChamberFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		mainChamberFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		mainChamberFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				mainChamberFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		mainChamberFloorStateOfPreservationFP.addTool(mainChamberFloorStateOfPreservationResetSelectionTB);
		mainChamberFloorStateOfPreservationFP.add(mainChamberFloorPreservationSelectorCB);

		corridorFloorStateOfPreservationFP = new FramedPanel();
		corridorFloorStateOfPreservationFP.setHeading("Main Corridor");
		corridorFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		corridorFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton corridorFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		corridorFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		corridorFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				corridorFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		corridorFloorStateOfPreservationFP.addTool(corridorFloorStateOfPreservationResetSelectionTB);
		corridorFloorStateOfPreservationFP.add(corridorFloorPreservationSelectorCB);

		rearAreaFloorStateOfPreservationFP = new FramedPanel();
		rearAreaFloorStateOfPreservationFP.setHeading("Rear Area");
		rearAreaFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		rearAreaFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton rearAreaFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rearAreaFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rearAreaFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rearAreaFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		rearAreaFloorStateOfPreservationFP.addTool(rearAreaFloorStateOfPreservationResetSelectionTB);
		rearAreaFloorStateOfPreservationFP.add(rearAreaFloorPreservationSelectorCB);

		leftCorridorFloorStateOfPreservationFP = new FramedPanel();
		leftCorridorFloorStateOfPreservationFP.setHeading("Left Corridor");
		leftCorridorFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		leftCorridorFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton leftCorridorFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		leftCorridorFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		leftCorridorFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				leftCorridorFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		leftCorridorFloorStateOfPreservationFP.addTool(leftCorridorFloorStateOfPreservationResetSelectionTB);
		leftCorridorFloorStateOfPreservationFP.add(leftCorridorFloorPreservationSelectorCB);

		rightCorridorFloorStateOfPreservationFP = new FramedPanel();
		rightCorridorFloorStateOfPreservationFP.setHeading("Right Corridor");
		rightCorridorFloorPreservationSelectorCB = createStateOfPreservationSelector("state of preservation");
		rightCorridorFloorPreservationSelectorCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setFloorPreservationClassification(event.getSelectedItem());
			}
		});
		ToolButton rightCorridorFloorStateOfPreservationResetSelectionTB = new ToolButton(ToolButton.REFRESH);
		rightCorridorFloorStateOfPreservationResetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rightCorridorFloorStateOfPreservationResetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rightCorridorFloorPreservationSelectorCB.setValue(null, true);
			}
		});
		rightCorridorFloorStateOfPreservationFP.addTool(rightCorridorFloorStateOfPreservationResetSelectionTB);
		rightCorridorFloorStateOfPreservationFP.add(rightCorridorFloorPreservationSelectorCB);

		HorizontalLayoutContainer rearAnteHLC = new HorizontalLayoutContainer();
		rearAnteHLC.add(rearAreaStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		rearAnteHLC.add(antechamberStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		HorizontalLayoutContainer leftRightCorridorHLC = new HorizontalLayoutContainer();
		leftRightCorridorHLC.add(leftCorridorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		leftRightCorridorHLC.add(rightCorridorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		HorizontalLayoutContainer corridorMainChamberHLC = new HorizontalLayoutContainer();
		corridorMainChamberHLC.add(corridorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		corridorMainChamberHLC.add(mainChamberStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));

		HorizontalLayoutContainer rearAnteFloorHLC = new HorizontalLayoutContainer();
		rearAnteFloorHLC.add(rearAreaFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		rearAnteFloorHLC.add(antechamberFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		HorizontalLayoutContainer leftRightCorridorFloorHLC = new HorizontalLayoutContainer();
		leftRightCorridorFloorHLC.add(leftCorridorFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		leftRightCorridorFloorHLC.add(rightCorridorFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		HorizontalLayoutContainer corridorMainChamberFloorHLC = new HorizontalLayoutContainer();
		corridorMainChamberFloorHLC.add(corridorFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		corridorMainChamberFloorHLC.add(mainChamberFloorStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));

		FramedPanel furtherCommentsPanel = new FramedPanel();
		furtherCommentsPanel.setHeading("Further Comments");
		stateOfPreservationTextArea = new TextArea();
		stateOfPreservationTextArea.setEmptyText("This field is for remarks on the state of the preservation");
		stateOfPreservationTextArea.setValue(correspondingCaveEntry.getStateOfPerservation());
		stateOfPreservationTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setStateOfPerservation(event.getValue());
			}
		});
		furtherCommentsPanel.add(stateOfPreservationTextArea);

		// Walls
		wallSelectorCB = new ComboBox<WallEntry>(wallEntryLS, new LabelProvider<WallEntry>() {

			@Override
			public String getLabel(WallEntry entry) {
				return StaticTables.getInstance().getWallLocationEntries().get(entry.getWallLocationID()).getLabel();
			}
		}, new AbstractSafeHtmlRenderer<WallEntry>() {

			@Override
			public SafeHtml render(WallEntry entry) {
				return wallVT.wallLabel(StaticTables.getInstance().getWallLocationEntries().get(entry.getWallLocationID()).getLabel());
			}
		});
		wallSelectorCB.setEditable(false);
		wallSelectorCB.setTypeAhead(false);
		wallSelectorCB.setEmptyText("select a wall");
		wallSelectorCB.setTriggerAction(TriggerAction.ALL);
		wallSelectorCB.addSelectionHandler(new SelectionHandler<WallEntry>() {

			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				selectedWallEntry = event.getSelectedItem();
				selectedWallStateOfPreservationCB.setValue(preservationClassificationEntryList.findModelWithKey(Integer.toString(selectedWallEntry.getPreservationClassificationID())));
				wallWidthNF.setValue(selectedWallEntry.getWidth());
				wallHeightNF.setValue(selectedWallEntry.getHeight());
			}
		});
		selectedWallStateOfPreservationCB = createStateOfPreservationSelector("select wall preservation");
		selectedWallStateOfPreservationCB.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				selectedWallEntry.setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		wallWidthNF = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor(NumberFormat.getFormat("#0.00")));
		wallWidthNF.setAllowNegative(false);
		wallWidthNF.setEmptyText("n/a");
		wallWidthNF.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					selectedWallEntry.setWidth(0.0);
				} else if (wallWidthNF.validate()) {
					selectedWallEntry.setWidth(event.getValue());
				} 
			}
		});
		wallHeightNF = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor(NumberFormat.getFormat("#0.00")));
		wallHeightNF.setAllowNegative(false);
		wallHeightNF.setEmptyText("n/a");
		wallHeightNF.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					selectedWallEntry.setHeight(0.0);
				} else if (wallHeightNF.validate()) {
					selectedWallEntry.setHeight(event.getValue());
				} 
			}
		});
		HorizontalLayoutContainer wallMeasureHLC = new HorizontalLayoutContainer();
		wallMeasureHLC.add(new FieldLabel(wallWidthNF, "Width in meter"), new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		wallMeasureHLC.add(new FieldLabel(wallHeightNF, "Height in meter"), new HorizontalLayoutData(.5, 1.0, new Margins(0, 0, 0, 5)));
		
		VerticalLayoutContainer wallManagementVLC = new VerticalLayoutContainer();
		wallManagementVLC.add(wallSelectorCB, new VerticalLayoutData(1.0, .35));
		wallManagementVLC.add(selectedWallStateOfPreservationCB, new VerticalLayoutData(1.0, .35));
		wallManagementVLC.add(wallMeasureHLC, new VerticalLayoutData(1.0, .3));
		wallManagementFP = new FramedPanel();
		wallManagementFP.setHeading("Walls");
		wallManagementFP.add(wallManagementVLC);
		ToolButton showWallTB = new ToolButton(ToolButton.QUESTION);
		showWallTB.setToolTip(Util.createToolTip("show wall"));
		showWallTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				// TODO implementation of show wall
				Util.showWarning("Show Wall", "This feature will be implemented in a future version.");
			}
		});
		wallManagementFP.addTool(showWallTB);
		wallManagementFP.add(wallManagementVLC);
		
		VerticalLayoutContainer floorStateOfPreservationVLC = new VerticalLayoutContainer();
		floorStateOfPreservationVLC.add(rearAnteFloorHLC, new VerticalLayoutData(1.0, 1.0 / 3));
		floorStateOfPreservationVLC.add(leftRightCorridorFloorHLC, new VerticalLayoutData(1.0, 1.0 / 3));
		floorStateOfPreservationVLC.add(corridorMainChamberFloorHLC, new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel floorStateOfPreservationFP = new FramedPanel();
		floorStateOfPreservationFP.setHeading("Floor");
		floorStateOfPreservationFP.add(floorStateOfPreservationVLC);

		VerticalLayoutContainer ceilingStateOfPreservationVLC = new VerticalLayoutContainer();
		ceilingStateOfPreservationVLC.add(rearAreaCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingStateOfPreservationVLC.add(leftCorridorCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingStateOfPreservationVLC.add(rightCorridorCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingStateOfPreservationVLC.add(mainChamberCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingStateOfPreservationVLC.add(corridorCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingStateOfPreservationVLC.add(antechamberCeilingStateOfPreservationFP, new VerticalLayoutData(1.0, 1.0 / 6));

		FramedPanel ceilingStateOfPreservationFP = new FramedPanel();
		ceilingStateOfPreservationFP.setHeading("Ceiling");
		ceilingStateOfPreservationFP.add(ceilingStateOfPreservationVLC);

		VerticalLayoutContainer generalStateOfPreservationVLC = new VerticalLayoutContainer();
		generalStateOfPreservationVLC.add(rearAnteHLC, new VerticalLayoutData(1.0, 1.0 / 3));
		generalStateOfPreservationVLC.add(leftRightCorridorHLC, new VerticalLayoutData(1.0, 1.0 / 3));
		generalStateOfPreservationVLC.add(corridorMainChamberHLC, new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel generalStateOfPreservationFP = new FramedPanel();
		generalStateOfPreservationFP.setHeading("General");
		generalStateOfPreservationFP.add(generalStateOfPreservationVLC);

		VerticalLayoutContainer stateOfPreservationLeftVLC = new VerticalLayoutContainer();
		stateOfPreservationLeftVLC.add(generalStateOfPreservationFP, new VerticalLayoutData(1.0, .5));
		stateOfPreservationLeftVLC.add(floorStateOfPreservationFP, new VerticalLayoutData(1.0, .5));

		HorizontalLayoutContainer stateOfPreservationHLC = new HorizontalLayoutContainer();
		stateOfPreservationHLC.add(stateOfPreservationLeftVLC, new HorizontalLayoutData(.5, 1.0));
		stateOfPreservationHLC.add(ceilingStateOfPreservationFP, new HorizontalLayoutData(.5, 1.0));
		
		HorizontalLayoutContainer commentsAndWallHLC = new HorizontalLayoutContainer();
		commentsAndWallHLC.add(furtherCommentsPanel, new HorizontalLayoutData(.5, 1.0));
		commentsAndWallHLC.add(wallManagementFP, new HorizontalLayoutData(.5, 1.0));

		VerticalLayoutContainer finalStateOfPreservationVLC = new VerticalLayoutContainer();
		finalStateOfPreservationVLC.add(stateOfPreservationHLC, new VerticalLayoutData(1.0, .75));
		finalStateOfPreservationVLC.add(commentsAndWallHLC, new VerticalLayoutData(1.0, .25));

		updateStateOfPreservationPanel(0);

		// we will use this HLC for the tabs

		/**
		 * ------------------------------ the column with the text fields (description tab) ----------------------------
		 */

		HorizontalLayoutContainer descriptionHLC = new HorizontalLayoutContainer();

		FramedPanel findingsFP = new FramedPanel();
		findingsFP.setHeading("Findings");
		findingsTextArea = new TextArea();
		findingsTextArea.setEmptyText("enter findings");
		findingsTextArea.setValue(correspondingCaveEntry.getFindings());
		findingsTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setFindings(event.getValue());
			}
		});
		findingsFP.add(findingsTextArea);

		FramedPanel notesFP = new FramedPanel();
		notesFP.setHeading("Notes");
		notesTextArea = new TextArea();
		notesTextArea.setEmptyText("enter notes");
		notesTextArea.setValue(correspondingCaveEntry.getNotes());
		notesTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setNotes(event.getValue());
			}
		});
		notesFP.add(notesTextArea);

		HorizontalLayoutContainer findingsNotesHLC = new HorizontalLayoutContainer();
		findingsNotesHLC.add(findingsFP, new HorizontalLayoutData(.5, 1.0));
		findingsNotesHLC.add(notesFP, new HorizontalLayoutData(.5, 1.0));
		

		CheckBox sculpturesCB = new CheckBox();
		sculpturesCB.setBoxLabel("contains sculptures");
		sculpturesCB.setValue(correspondingCaveEntry.isHasSculptures());
		sculpturesCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasSculptures(event.getValue());
			}
		});
		
		CheckBox clayFiguresCB = new CheckBox();
		clayFiguresCB.setBoxLabel("contains clay figures");
		clayFiguresCB.setValue(correspondingCaveEntry.isHasClayFigures());
		clayFiguresCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasClayFigures(event.getValue());
			}
		});

		CheckBox immitationOfMountainsCB = new CheckBox();
		immitationOfMountainsCB.setBoxLabel("contains immitations of mountains");
		immitationOfMountainsCB.setValue(correspondingCaveEntry.isHasImmitationOfMountains());
		immitationOfMountainsCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasImmitationOfMountains(event.getValue());
			}
		});

		CheckBox holesForFixationCB = new CheckBox();
		holesForFixationCB.setBoxLabel("has holes for fixation of plastical items");
		holesForFixationCB.setValue(correspondingCaveEntry.isHasHolesForFixationOfPlasticalItems());
		holesForFixationCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasHolesForFixationOfPlasticalItems(event.getValue());
			}
		});

		CheckBox woodenConstructionCB = new CheckBox();
		woodenConstructionCB.setBoxLabel("has wooden contructions");
		woodenConstructionCB.setValue(correspondingCaveEntry.isHasWoodenConstruction());
		woodenConstructionCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasWoodenConstruction(event.getValue());
			}
		});
		
		VerticalLayoutContainer sculpturesVLC = new VerticalLayoutContainer();
		sculpturesVLC.add(sculpturesCB, new VerticalLayoutData(1.0, 1.0 / 3));
		sculpturesVLC.add(clayFiguresCB, new VerticalLayoutData(1.0, 1.0 / 3, new Margins(0, 0, 0, 20)));
		sculpturesVLC.add(immitationOfMountainsCB, new VerticalLayoutData(1.0, 1.0 / 3, new Margins(0, 0, 0, 20)));
		
		VerticalLayoutContainer holesConstructionsVLC = new VerticalLayoutContainer();
		holesConstructionsVLC.add(holesForFixationCB, new VerticalLayoutData(1.0, .5));
		holesConstructionsVLC.add(woodenConstructionCB, new VerticalLayoutData(1.0, .5));
		
		HorizontalLayoutContainer plasticalItemsHLC = new HorizontalLayoutContainer();
		plasticalItemsHLC.add(sculpturesVLC, new HorizontalLayoutData(.5, 1.0));
		plasticalItemsHLC.add(holesConstructionsVLC, new HorizontalLayoutData(.5, 1.0));

		FramedPanel plasticalItemsFP = new FramedPanel();
		plasticalItemsFP.setHeading("Plastical Items");
		plasticalItemsFP.add(plasticalItemsHLC);
		
		FramedPanel c14AnalysisLinkFP = new FramedPanel();
		c14AnalysisLinkFP.setHeading("C14 Analysis (links)");
		c14AnalysisLinksFLC = new FlowLayoutContainer();
		c14AnalysisLinksFLC.setScrollMode(ScrollMode.AUTOY);
		c14AnalysisLinkFP.add(c14AnalysisLinksFLC);
		refreshC14AnalysisLinksFLC(correspondingCaveEntry.getC14AnalysisUrlList());

		ToolButton addC14LinkTB = new ToolButton(ToolButton.PLUS);
		addC14LinkTB.setToolTip(Util.createToolTip("add new C14 link"));
		c14AnalysisLinkFP.addTool(addC14LinkTB);
		addC14LinkTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addNewC14LinkDialog = new PopupPanel();
				FramedPanel newC14LinkFP = new FramedPanel();
				newC14LinkFP.setHeading("Add C14 Link");
				TextField c14AnalysisShortName = new TextField();
				c14AnalysisShortName.setEmptyText("shortname");
				c14AnalysisShortName.addValidator(new MaxLengthValidator(64));
				TextField c14AnalysisUrlTextField = new TextField();
				c14AnalysisUrlTextField.setEmptyText("http/https/ftp");
				c14AnalysisUrlTextField.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "Please enter valid URL"));
				VerticalLayoutContainer c14AnalysisVLC = new VerticalLayoutContainer();
				c14AnalysisVLC.add(c14AnalysisShortName, new VerticalLayoutData(1.0, .5));
				c14AnalysisVLC.add(c14AnalysisUrlTextField, new VerticalLayoutData(1.0, .5));
				newC14LinkFP.add(c14AnalysisVLC);
				ToolButton saveTB = new ToolButton(ToolButton.SAVE);
				saveTB.setToolTip(Util.createToolTip("save"));
				saveTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (c14AnalysisUrlTextField.validate() && c14AnalysisShortName.validate()) {
							C14AnalysisUrlEntry c14aue = new C14AnalysisUrlEntry(c14AnalysisUrlTextField.getValue(), c14AnalysisShortName.getValue());
							correspondingCaveEntry.getC14AnalysisUrlList().add(c14aue);
							refreshC14AnalysisLinksFLC(correspondingCaveEntry.getC14AnalysisUrlList());
							addNewC14LinkDialog.hide();
						}
					}
				});
				ToolButton cancelTB = new ToolButton(ToolButton.CLOSE);
				cancelTB.setToolTip(Util.createToolTip("close"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addNewC14LinkDialog.hide();
					}
				});
				newC14LinkFP.addTool(saveTB);
				newC14LinkFP.addTool(cancelTB);
				addNewC14LinkDialog.add(newC14LinkFP);
				addNewC14LinkDialog.setSize("500px", "100px");
				addNewC14LinkDialog.setModal(true);
				addNewC14LinkDialog.center();
			}
		});

		c14UploadPanel = new FramedPanel();
		c14UploadPanel.setHeading("C14 additional document");
		c14DocumentsFLC = new FlowLayoutContainer();
		c14DocumentsFLC.setScrollMode(ScrollMode.AUTOY);
		c14UploadPanel.add(c14DocumentsFLC);
		refreshC14DocumentsFLC(correspondingCaveEntry.getC14DocumentList());

		ToolButton uploadButton = new ToolButton(ToolButton.PLUS);
		uploadButton.setToolTip(Util.createToolTip("Upload C14 document.", "A new Cave has to be saved first, otherwise upload won't work."));
		uploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (correspondingCaveEntry.getCaveID() == 0) {
					Util.showWarning("Document upload problem",
							"For technical reasons, an optional C14 document\n cannot be uploaded before the cave has been saved.");
					return;
				}
				PopupPanel c14DocUploadPanel = new PopupPanel();
				C14DocumentUploadListener c14dul = new C14DocumentUploadListener() {

					@Override
					public void uploadCompleted(String documentFilename) {
						String origFilename = uploader.getUploadedFilename().substring(12);
						correspondingCaveEntry.getC14DocumentList().add(new C14DocumentEntry(documentFilename, origFilename));
						refreshC14DocumentsFLC(correspondingCaveEntry.getC14DocumentList());
						c14DocUploadPanel.hide();
					}

					@Override
					public void uploadCanceled() {
						c14DocUploadPanel.hide();
					}
				};
				uploader = new C14DocumentUploader(correspondingCaveEntry, c14dul);
				c14DocUploadPanel.add(uploader);
				c14DocUploadPanel.setGlassEnabled(true);
				c14DocUploadPanel.center();
				// c14DocUploadPanel.show();
			}
		});
		c14UploadPanel.addTool(uploadButton);

		VerticalLayoutContainer descriptionsVLC = new VerticalLayoutContainer();
		descriptionsVLC.add(findingsNotesHLC, new VerticalLayoutData(1.0, .5));
		descriptionsVLC.add(plasticalItemsFP, new VerticalLayoutData(1.0, .2));
		descriptionsVLC.add(c14AnalysisLinkFP, new VerticalLayoutData(1.0, .15));
		descriptionsVLC.add(c14UploadPanel, new VerticalLayoutData(1.0, .15));

		descriptionHLC.add(descriptionsVLC, new HorizontalLayoutData(1.0, 1.0));

		/**
		 * ------------------------- the cave type and layout description (cave type tab) -----------------------------
		 */

		HorizontalLayoutContainer caveTypeHLC = new HorizontalLayoutContainer();
		// VerticalLayoutContainer caveTypeVLC = new VerticalLayoutContainer();

		/**
		 * ======== caveTypeSelector
		 */
		FramedPanel caveTypeFP = new FramedPanel();
		caveTypeFP.setHeading("Cave Type");
		caveTypeSelectionCB = new ComboBox<CaveTypeEntry>(caveTypeEntryListStore, caveTypeProps.nameEN(),
				new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

					@Override
					public SafeHtml render(CaveTypeEntry item) {
						return ctvTemplates.caveTypeLabel(item.getNameEN());
					}
				});
		caveTypeSelectionCB.setEmptyText("select cave type");
		caveTypeSelectionCB.setTypeAhead(false);
		caveTypeSelectionCB.setEditable(false);
		caveTypeSelectionCB.setTriggerAction(TriggerAction.ALL);
		caveTypeSelectionCB.addSelectionHandler(new SelectionHandler<CaveTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveTypeEntry> event) {
				correspondingCaveEntry.setCaveTypeID(event.getSelectedItem().getCaveTypeID());
				CaveTypeEntry correspondingCaveTypeEntry = caveTypeEntryListStore
						.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID()));
				refreshCaveSketchFLC(correspondingCaveTypeEntry.getSketchName());
				updateCeilingTypePanel(correspondingCaveEntry.getCaveTypeID());
				updateStateOfPreservationPanel(correspondingCaveEntry.getCaveTypeID());
				updateWallList(correspondingCaveEntry.getCaveTypeID());
			}
		});
		caveTypeFP.add(caveTypeSelectionCB);

		/**
		 * ======== orientationSelection
		 */
		FramedPanel orientationFP = new FramedPanel();
		orientationFP.setHeading("Orientation");
		orientationSelection = new ComboBox<OrientationEntry>(orientationEntryList, orientationProps.name(),
				new AbstractSafeHtmlRenderer<OrientationEntry>() {

					@Override
					public SafeHtml render(OrientationEntry item) {
						final OrientationViewTemplates ovTemplates = GWT.create(OrientationViewTemplates.class);
						return ovTemplates.orientationLabel(item.getName());
					}
				});
		orientationSelection.setTriggerAction(TriggerAction.ALL);
		orientationSelection.setEditable(false);
		orientationSelection.setTypeAhead(false);
		orientationSelection.addSelectionHandler(new SelectionHandler<OrientationEntry>() {

			@Override
			public void onSelection(SelectionEvent<OrientationEntry> event) {
				correspondingCaveEntry.setOrientationID(event.getSelectedItem().getOrientationID());
			}
		});
		orientationFP.add(orientationSelection);

		/**
		 * ------------------------ building the ceiling type selection panels ----------------------------------------------
		 */
		/**
		 * ======== rear area ceiling types selection
		 */
		rearAreaCeilingTypeFP = new FramedPanel();
		rearAreaCeilingTypeFP.setHeading("Rear Area Ceiling Type");
		rearAreaCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		rearAreaCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setCeilingType1(event.getSelectedItem());
			}
		});
		rearAreaCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		rearAreaCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA).setCeilingType2(event.getSelectedItem());
			}
		});
		ToolButton resetRearAreaCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetRearAreaCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rearAreaCeilingTypeFP.addTool(resetRearAreaCeilingTypeSelectionTB);
		resetRearAreaCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rearAreaCeilingTypeSelector1.setValue(null, true);
				rearAreaCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer rearAreaCeilingTypeHLC = new HorizontalLayoutContainer();
		rearAreaCeilingTypeHLC.add(rearAreaCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		rearAreaCeilingTypeHLC.add(rearAreaCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		rearAreaCeilingTypeFP.add(rearAreaCeilingTypeHLC);

		/**
		 * ======== main chamber ceiling types selection
		 */
		mainChamberCeilingTypeFP = new FramedPanel();
		mainChamberCeilingTypeFP.setHeading("Main Chamber Ceiling Type");
		mainChamberCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		mainChamberCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setCeilingType1(event.getSelectedItem());
			}
		});
		mainChamberCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		mainChamberCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER).setCeilingType2(event.getSelectedItem());
			}
		});
		ToolButton resetMainChamberCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetMainChamberCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		mainChamberCeilingTypeFP.addTool(resetMainChamberCeilingTypeSelectionTB);
		resetMainChamberCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				mainChamberCeilingTypeSelector1.setValue(null, true);
				mainChamberCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer mainChamberCeilingTypeHLC = new HorizontalLayoutContainer();
		mainChamberCeilingTypeHLC.add(mainChamberCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		mainChamberCeilingTypeHLC.add(mainChamberCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		mainChamberCeilingTypeFP.add(mainChamberCeilingTypeHLC);

		/**
		 * ======== antechamber ceiling types selection
		 */
		antechamberCeilingTypeFP = new FramedPanel();
		antechamberCeilingTypeFP.setHeading("Antechamber Ceiling Type");
		antechamberCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		antechamberCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setCeilingType1(event.getSelectedItem());
			}
		});
		antechamberCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		antechamberCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER).setCeilingType2(event.getSelectedItem());
			}
		});
		ToolButton resetAntechamberCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetAntechamberCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		antechamberCeilingTypeFP.addTool(resetAntechamberCeilingTypeSelectionTB);
		resetAntechamberCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				antechamberCeilingTypeSelector1.setValue(null, true);
				antechamberCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer antechamberCeilingTypeHLC = new HorizontalLayoutContainer();
		antechamberCeilingTypeHLC.add(antechamberCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		antechamberCeilingTypeHLC.add(antechamberCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		antechamberCeilingTypeFP.add(antechamberCeilingTypeHLC);

		/**
		 * ======== main chamber corridor ceiling types selection
		 */
		corridorCeilingTypeFP = new FramedPanel();
		corridorCeilingTypeFP.setHeading("Corridor Ceiling Type");
		corridorCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		corridorCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setCeilingType1(event.getSelectedItem());
			}
		});
		corridorCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		corridorCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR).setCeilingType2(event.getSelectedItem());
			}
		});
		ToolButton resetCorridorCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetCorridorCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		corridorCeilingTypeFP.addTool(resetCorridorCeilingTypeSelectionTB);
		resetCorridorCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				corridorCeilingTypeSelector1.setValue(null, true);
				corridorCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer corridorCeilingTypeHLC = new HorizontalLayoutContainer();
		corridorCeilingTypeHLC.add(corridorCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		corridorCeilingTypeHLC.add(corridorCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		corridorCeilingTypeFP.add(corridorCeilingTypeHLC);

		/**
		 * ======== left corridor ceiling types selection
		 */
		leftCorridorCeilingTypeFP = new FramedPanel();
		leftCorridorCeilingTypeFP.setHeading("Left Corridor Ceiling Type");
		leftCorridorCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		leftCorridorCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setCeilingType1(event.getSelectedItem());
			}
		});
		leftCorridorCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		leftCorridorCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR).setCeilingType2(event.getSelectedItem());
			}
		});
		ToolButton resetLeftCorridorCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetLeftCorridorCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		leftCorridorCeilingTypeFP.addTool(resetLeftCorridorCeilingTypeSelectionTB);
		resetLeftCorridorCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				leftCorridorCeilingTypeSelector1.setValue(null, true);
				leftCorridorCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer leftCorridorCeilingTypeHLC = new HorizontalLayoutContainer();
		leftCorridorCeilingTypeHLC.add(leftCorridorCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		leftCorridorCeilingTypeHLC.add(leftCorridorCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		leftCorridorCeilingTypeFP.add(leftCorridorCeilingTypeHLC);

		/**
		 * ======== right corridor ceiling types selection
		 */
		rightCorridorCeilingTypeFP = new FramedPanel();
		rightCorridorCeilingTypeFP.setHeading("Right Corridor Ceiling Type");
		rightCorridorCeilingTypeSelector1 = createCeilingTypeSelector("select ceiling type");
		rightCorridorCeilingTypeSelector1.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setCeilingType1(event.getSelectedItem());
			}
		});
		rightCorridorCeilingTypeSelector2 = createCeilingTypeSelector("optional 2nd type");
		rightCorridorCeilingTypeSelector2.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR).setCeilingType2(event.getSelectedItem());
			}

		});
		ToolButton resetRightCorridorCeilingTypeSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetRightCorridorCeilingTypeSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		rightCorridorCeilingTypeFP.addTool(resetRightCorridorCeilingTypeSelectionTB);
		resetRightCorridorCeilingTypeSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				rightCorridorCeilingTypeSelector1.setValue(null, true);
				rightCorridorCeilingTypeSelector2.setValue(null, true);
			}
		});
		HorizontalLayoutContainer rightCorridorCeilingTypeHLC = new HorizontalLayoutContainer();
		rightCorridorCeilingTypeHLC.add(rightCorridorCeilingTypeSelector1, new HorizontalLayoutData(.5, 1.0, new Margins(0, 5, 0, 0)));
		rightCorridorCeilingTypeHLC.add(rightCorridorCeilingTypeSelector2, new HorizontalLayoutData(.5, 1.0, new Margins(0)));
		rightCorridorCeilingTypeFP.add(rightCorridorCeilingTypeHLC);

		/**
		 * ======== cave layout comments
		 */
		FramedPanel caveLayoutCommentsFP = new FramedPanel();
		caveLayoutCommentsFP.setHeading("Comments on Cave Layout");
		
		CheckBox volutedHorseshoeArchCB = new CheckBox();
		volutedHorseshoeArchCB.setBoxLabel("has Voluted Horseshoe Arch");
		volutedHorseshoeArchCB.setValue(correspondingCaveEntry.isHasVolutedHorseShoeArch());
		volutedHorseshoeArchCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				correspondingCaveEntry.setHasVolutedHorseShoeArch(event.getValue());
			}
		});

		caveLayoutCommentsTextArea = new TextArea();
		caveLayoutCommentsTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setCaveLayoutComments(event.getValue());
			}
		});
		caveLayoutCommentsTextArea.setValue(correspondingCaveEntry.getCaveLayoutComments());
		
		VerticalLayoutContainer caveLayoutCommentsVLC = new VerticalLayoutContainer();
		caveLayoutCommentsVLC.add(volutedHorseshoeArchCB, new VerticalLayoutData(1.0, .2));
		caveLayoutCommentsVLC.add(caveLayoutCommentsTextArea, new VerticalLayoutData(1.0, .8));
		
		caveLayoutCommentsFP.add(caveLayoutCommentsVLC);

		/**
		 * here we assemble the whole left column of the Cave Layout tab
		 */
		HorizontalLayoutContainer typeOrientationHLC = new HorizontalLayoutContainer();
		typeOrientationHLC.add(caveTypeFP, new HorizontalLayoutData(.6, 1.0));
		typeOrientationHLC.add(orientationFP, new HorizontalLayoutData(.4, 1.0));

		VerticalLayoutContainer ceilingTyleSelectionsVLC = new VerticalLayoutContainer();
		ceilingTyleSelectionsVLC.add(rearAreaCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingTyleSelectionsVLC.add(leftCorridorCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingTyleSelectionsVLC.add(rightCorridorCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingTyleSelectionsVLC.add(mainChamberCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingTyleSelectionsVLC.add(corridorCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		ceilingTyleSelectionsVLC.add(antechamberCeilingTypeFP, new VerticalLayoutData(1.0, 1.0 / 6));
		
		ToolButton addCeilingTypeTB = new ToolButton(ToolButton.PLUS);
		addCeilingTypeTB.setToolTip(Util.createToolTip("add new ceiling type"));
		addCeilingTypeTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addNewCeilingTypeDialog = new PopupPanel();
				FramedPanel newCeilingTypeFP = new FramedPanel();
				newCeilingTypeFP.setHeading("Add Ceiling Type");
				TextField ceilingTypeNameField = new TextField();
				ceilingTypeNameField.addValidator(new MinLengthValidator(2));
				ceilingTypeNameField.addValidator(new MaxLengthValidator(64));
				ceilingTypeNameField.setEmptyText("enter new ceiling type");
				ceilingTypeNameField.setWidth(200);
				newCeilingTypeFP.add(ceilingTypeNameField);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (ceilingTypeNameField.isValid()) {
							CeilingTypeEntry ctEntry = new CeilingTypeEntry();
							ctEntry.setName(ceilingTypeNameField.getCurrentValue());
							dbService.insertCeilingTypeEntry(ctEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									ctEntry.setCeilingTypeID(result);
									ceilingTypeEntryList.add(ctEntry);
								}
							});
							addNewCeilingTypeDialog.hide();
						}
					}
				});
				newCeilingTypeFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addNewCeilingTypeDialog.hide();
					}
				});
				newCeilingTypeFP.addButton(cancelButton);
				addNewCeilingTypeDialog.add(newCeilingTypeFP);
				addNewCeilingTypeDialog.setModal(true);
				addNewCeilingTypeDialog.center();
			}
		});

		FramedPanel ceilingTypeSelectionsFP = new FramedPanel();
		ceilingTypeSelectionsFP.setHeading("Ceiling Types");
		ceilingTypeSelectionsFP.add(ceilingTyleSelectionsVLC);
		ceilingTypeSelectionsFP.addTool(addCeilingTypeTB);

		VerticalLayoutContainer caveLayoutLeftVLC = new VerticalLayoutContainer();
		caveLayoutLeftVLC.add(typeOrientationHLC, new VerticalLayoutData(1.0, .11));
		caveLayoutLeftVLC.add(ceilingTypeSelectionsFP, new VerticalLayoutData(1.0, .65));
		caveLayoutLeftVLC.add(caveLayoutCommentsFP, new VerticalLayoutData(1.0, .24));

		caveTypeHLC.add(caveLayoutLeftVLC, new HorizontalLayoutData(.5, 1.0));

		updateCeilingTypePanel(0);

		FramedPanel caveSketchFP = new FramedPanel();
		ToolButton addSketchButton = new ToolButton(ToolButton.PLUS);
		addSketchButton.setToolTip(Util.createToolTip("upload cave sketch"));
		addSketchButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (correspondingCaveEntry.getCaveID() == 0) {
					Util.showWarning("Cave sketch upload problem",
							"For technical reasons, an optional cave sketch\n cannot be uploaded before the cave has been saved.");
					return;
				}
				PopupPanel caveSketchUploadPanel = new PopupPanel();
				CaveSketchUploader uploader = new CaveSketchUploader(correspondingCaveEntry.getCaveID(), new CaveSketchUploadListener() {

					@Override
					public void uploadCompleted(CaveSketchEntry csEntry) {
						correspondingCaveEntry.getCaveSketchList().add(csEntry);
						caveSketchUploadPanel.hide();
						refreshCaveSketchFLC(
								caveTypeEntryListStore.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID())).getSketchName());
					}

					@Override
					public void uploadCanceled() {
						caveSketchUploadPanel.hide();
					}
				});
				caveSketchUploadPanel.add(uploader);
				caveSketchUploadPanel.setGlassEnabled(true);
				caveSketchUploadPanel.center();
				caveSketchUploadPanel.show();
			}
		});
		caveSketchFP.addTool(addSketchButton);
		caveSketchFP.setHeading("Cave Sketch");
		caveSketchFLC = new FlowLayoutContainer();
		caveSketchFLC.setScrollMode(ScrollMode.AUTOY);
		caveSketchFP.add(caveSketchFLC);
		
//		VerticalLayoutContainer caveLayoutRightVLC = new VerticalLayoutContainer();
//		caveLayoutRightVLC.add(caveSketchFP, new VerticalLayoutData(1.0, .85));
//		caveLayoutRightVLC.add(wallManagementFP, new VerticalLayoutData(1.0, .15));

		caveTypeHLC.add(caveSketchFP, new HorizontalLayoutData(.5, 1.0));

		/**
		 * --------------------------------- measurement tab ------------------------------------------
		 */

		VerticalLayoutContainer expeditionMeasurementVLC = new VerticalLayoutContainer();
		expeditionMeasurementVLC.add(createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		expeditionMeasurementVLC.add(createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		expeditionMeasurementVLC.add(
				createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		expeditionMeasurementVLC.add(createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		expeditionMeasurementVLC.add(
				createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		expeditionMeasurementVLC.add(
				createCaveAreaExpeditionMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));

		VerticalLayoutContainer modernMeasurementVLC = new VerticalLayoutContainer();
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.ANTECHAMBER)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.MAIN_CHAMBER_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_LEFT_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));
		modernMeasurementVLC.add(createCaveAreaModernMeasurePanel(correspondingCaveEntry.getCaveArea(CaveAreaEntry.REAR_AREA_RIGHT_CORRIDOR)),
				new VerticalLayoutData(1.0, 1.0 / 6));

		PlainTabPanel measurementPTP = new PlainTabPanel();
		measurementPTP.setTabScroll(false);
		measurementPTP.setAnimScroll(false);
		measurementPTP.add(expeditionMeasurementVLC, new TabItemConfig("Expedition Measurement", false));
		measurementPTP.add(modernMeasurementVLC, new TabItemConfig("Modern Measurement", false));


		/**
		 * ------------------------------ now we are assembling the tabs and add them to the main hlc ----------------------------------
		 */
		PlainTabPanel tabPanel = new PlainTabPanel();
		tabPanel.setTabScroll(false);
		tabPanel.setAnimScroll(false);
		tabPanel.add(caveTypeHLC, new TabItemConfig("Cave Layout", false));
		tabPanel.add(measurementPTP, new TabItemConfig("Measurements", false));
		tabPanel.add(finalStateOfPreservationVLC, new TabItemConfig("State of Preservation", false));
		tabPanel.add(descriptionHLC, new TabItemConfig("Descriptions", false));

		mainHlContainer.add(tabPanel, new HorizontalLayoutData(.7, 1.0));

		ToolButton saveToolButton = new ToolButton(ToolButton.SAVE);
		saveToolButton.setToolTip(Util.createToolTip("save"));
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveEntries(false);
			}
		});

		ToolButton closeToolButton = new ToolButton(ToolButton.CLOSE);
		closeToolButton.setToolTip(Util.createToolTip("close"));
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						saveEntries(true);
					}
				}, new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						closeEditor(null);
					}
				});
			}
		});

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Cave Editor (entry last modified on " + correspondingCaveEntry.getModifiedOn() + ")");
		mainPanel.setSize("900px", "650px"); // here we set the size of the panel
		mainPanel.add(mainHlContainer);
		mainPanel.addTool(saveToolButton);
		mainPanel.addTool(closeToolButton);
	}

	/**
	 * 
	 * @param caEntry
	 * @return
	 */
	private FramedPanel createCaveAreaExpeditionMeasurePanel(CaveAreaEntry caEntry) {
		FramedPanel expeditionMeasureFP = new FramedPanel();
		expeditionMeasureFP.setHeading(caEntry.getCaveAreaLabel());
		
		NumberField<Double> expeditionWidthNumberField = createMeasurementNumberField(caEntry.getExpeditionWidth());
		expeditionWidthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setExpeditionWidth(0);
				} else if (expeditionWidthNumberField.validate()) {
					caEntry.setExpeditionWidth(event.getValue());
				} 
			}
		});

		NumberField<Double> expeditionLengthNumberField = createMeasurementNumberField(caEntry.getExpeditionLength());
		expeditionLengthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setExpeditionLength(0);
				} else if (expeditionLengthNumberField.validate()) {
					caEntry.setExpeditionLength(event.getValue());
				}
			}
		});

		NumberField<Double> expeditionHeightWallNumberField = createMeasurementNumberField(caEntry.getExpeditionWallHeight());
		expeditionHeightWallNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setExpeditionWallHeight(0);
				} else if (expeditionHeightWallNumberField.validate()) {
					caEntry.setExpeditionWallHeight(event.getValue());
				}
			}
		});

		NumberField<Double> expeditionHeightTotalNumberField = createMeasurementNumberField(caEntry.getExpeditionTotalHeight());
		expeditionHeightTotalNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setExpeditionTotalHeight(0);
				} else if (expeditionHeightTotalNumberField.validate()) {
					caEntry.setExpeditionTotalHeight(event.getValue());
				}
			}
		});

		FramedPanel expeditionMeasuresWidthFP = new FramedPanel();
		expeditionMeasuresWidthFP.setHeading("Width");
		expeditionMeasuresWidthFP.add(expeditionWidthNumberField);

		FramedPanel expeditionMeasuresLengthFP = new FramedPanel();
		expeditionMeasuresLengthFP.setHeading("Length");
		expeditionMeasuresLengthFP.add(expeditionLengthNumberField);

		FramedPanel expeditionMeasuresHeightWallFP = new FramedPanel();
		expeditionMeasuresHeightWallFP.setHeading("Wall Height");
		expeditionMeasuresHeightWallFP.add(expeditionHeightWallNumberField);

		FramedPanel expeditionMeasuresHeightTotalFP = new FramedPanel();
		expeditionMeasuresHeightTotalFP.setHeading("Total Height");
		expeditionMeasuresHeightTotalFP.add(expeditionHeightTotalNumberField);

		HorizontalLayoutContainer expeditionMeasuresHLC = new HorizontalLayoutContainer();
		expeditionMeasuresHLC.add(expeditionMeasuresWidthFP, new HorizontalLayoutData(.25,  1.0));
		expeditionMeasuresHLC.add(expeditionMeasuresLengthFP, new HorizontalLayoutData(.25,  1.0));
		expeditionMeasuresHLC.add(expeditionMeasuresHeightWallFP, new HorizontalLayoutData(.25,  1.0));
		expeditionMeasuresHLC.add(expeditionMeasuresHeightTotalFP, new HorizontalLayoutData(.25,  1.0));
		
		expeditionMeasureFP.add(expeditionMeasuresHLC);
		
		return expeditionMeasureFP;
	}

	/**
	 * 
	 * @param caEntry
	 * @return
	 */
	private FramedPanel createCaveAreaModernMeasurePanel(CaveAreaEntry caEntry) {
		FramedPanel modernMeasurementFP = new FramedPanel();
		modernMeasurementFP.setHeading(caEntry.getCaveAreaLabel() + " (min-max)");

		NumberField<Double> modernMinWidthNumberField = createMeasurementNumberField(caEntry.getModernMinWidth());
		modernMinWidthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMinWidth(0);
				} else if (modernMinWidthNumberField.validate()) {
					caEntry.setModernMinWidth(event.getValue());
				}
			}
		});

		NumberField<Double> modernMaxWidthNumberField = createMeasurementNumberField(caEntry.getModernMaxWidth());
		modernMaxWidthNumberField.addValidator(new Validator<Double>() {

			@Override
			public List<EditorError> validate(Editor<Double> editor, Double value) {

				List<EditorError> l = new ArrayList<EditorError>();
				if ((modernMinWidthNumberField.getCurrentValue() == null) && (value != null)) {
					l.add(new DefaultEditorError(editor, "put in min value first", value));
				} else if ((value != null) && (!"".equals(value)) && (value <= modernMinWidthNumberField.getValue())) {
					l.add(new DefaultEditorError(editor, "only values > min", value));
				}
				return l;
			}
		});
		modernMaxWidthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMaxWidth(0);
				} else if (modernMaxWidthNumberField.validate()) {
					caEntry.setModernMaxWidth(event.getValue());
				}
			}
		});

		NumberField<Double> modernMinLengthNumberField = createMeasurementNumberField(caEntry.getModernMinLength());
		modernMinLengthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMinLength(0);
				} else if (modernMinLengthNumberField.validate()) {
					caEntry.setModernMinLength(event.getValue());
				}
			}
		});

		NumberField<Double> modernMaxLengthNumberField = createMeasurementNumberField(caEntry.getModernMaxLength());
		modernMaxLengthNumberField.addValidator(new Validator<Double>() {

			@Override
			public List<EditorError> validate(Editor<Double> editor, Double value) {

				List<EditorError> l = new ArrayList<EditorError>();
				if ((modernMinLengthNumberField.getValue() == null) && (value != null)) {
					l.add(new DefaultEditorError(editor, "put in min value first", value));
				} else if (value <= modernMinLengthNumberField.getValue()) {
					l.add(new DefaultEditorError(editor, "only values > min", value));
				}
				return l;
			}
		});
		modernMaxLengthNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMaxLength(0);
				} else if (modernMaxLengthNumberField.validate()) {
					caEntry.setModernMaxLength(event.getValue());
				}
			}
		});

		NumberField<Double> modernMinHeightNumberField = createMeasurementNumberField(caEntry.getModernMinHeight());
		modernMinHeightNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMinHeight(0);
				} else if (modernMinHeightNumberField.validate()) {
					caEntry.setModernMinHeight(event.getValue());
				}
			}
		});

		NumberField<Double> modernMaxHeightNumberField = createMeasurementNumberField(caEntry.getModernMaxHeight());
		modernMaxHeightNumberField.addValidator(new Validator<Double>() {

			@Override
			public List<EditorError> validate(Editor<Double> editor, Double value) {

				List<EditorError> l = new ArrayList<EditorError>();
				if ((modernMinHeightNumberField.getCurrentValue() == null) && (value != null)) {
					l.add(new DefaultEditorError(editor, "put in min value first", value));
				} else if (value <= modernMinHeightNumberField.getValue()) {
					l.add(new DefaultEditorError(editor, "only values > min", value));
				}
				return l;
			}
		});
		modernMaxHeightNumberField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				if (event.getValue() == null) {
					caEntry.setModernMaxHeight(0);
				} else if (modernMaxHeightNumberField.validate()) {
					caEntry.setModernMaxHeight(event.getValue());
				}
			}
		});

		Label dl1 = new Label("–");
		dl1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label dl2 = new Label("–");
		dl2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		Label dl3 = new Label("–");
		dl3.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		HorizontalLayoutContainer modernMeasuresWidthHLC = new HorizontalLayoutContainer();
		modernMeasuresWidthHLC.add(modernMinWidthNumberField, new HorizontalLayoutData(.4, 1.0));
		modernMeasuresWidthHLC.add(dl1, new HorizontalLayoutData(.2, 1.0));
		modernMeasuresWidthHLC.add(modernMaxWidthNumberField, new HorizontalLayoutData(.4, 1.0));

		HorizontalLayoutContainer modernMeasuresLengthHLC = new HorizontalLayoutContainer();
		modernMeasuresLengthHLC.add(modernMinLengthNumberField, new HorizontalLayoutData(.4, 1.0));
		modernMeasuresLengthHLC.add(dl2, new HorizontalLayoutData(.2, 1.0));
		modernMeasuresLengthHLC.add(modernMaxLengthNumberField, new HorizontalLayoutData(.4, 1.0));
		
		HorizontalLayoutContainer modernMeasuredHeightHLC = new HorizontalLayoutContainer();
		modernMeasuredHeightHLC.add(modernMinHeightNumberField, new HorizontalLayoutData(.4, 1.0));
		modernMeasuredHeightHLC.add(dl3, new HorizontalLayoutData(.2, 1.0));
		modernMeasuredHeightHLC.add(modernMaxHeightNumberField, new HorizontalLayoutData(.4, 1.0));
		
		FramedPanel modernMeasuresWidthFP = new FramedPanel();
		modernMeasuresWidthFP.setHeading("Width");
		modernMeasuresWidthFP.add(modernMeasuresWidthHLC);

		FramedPanel modernMeasuresLengthFP = new FramedPanel();
		modernMeasuresLengthFP.setHeading("Length");
		modernMeasuresLengthFP.add(modernMeasuresLengthHLC);

		FramedPanel modernMeasuresHeightFP = new FramedPanel();
		modernMeasuresHeightFP.setHeading("Height");
		modernMeasuresHeightFP.add(modernMeasuredHeightHLC);

		HorizontalLayoutContainer modernMeasuresHLC = new HorizontalLayoutContainer();
		modernMeasuresHLC.add(modernMeasuresWidthFP, new HorizontalLayoutData(1.0 / 3,  1.0));
		modernMeasuresHLC.add(modernMeasuresLengthFP, new HorizontalLayoutData(1.0 / 3,  1.0));
		modernMeasuresHLC.add(modernMeasuresHeightFP, new HorizontalLayoutData(1.0 / 3,  1.0));
		
		modernMeasurementFP.add(modernMeasuresHLC);

		return modernMeasurementFP;
	}

	private NumberField<Double> createMeasurementNumberField(double value) {
		NumberField<Double> measurementNF = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor(NumberFormat.getFormat("#0.00")));
//		measurementNF.setDirection(Direction.RTL);
		measurementNF.setAllowNegative(false);
		measurementNF.setEmptyText("meter");
		if (value > 0) {
			measurementNF.setValue(value);
		}
		return measurementNF;
	}

	private void updateCeilingTypePanel(int caveTypeID) {

		switch (caveTypeID) {
			case 2: // square cave
				rearAreaCeilingTypeSelector1.setEnabled(false);
				rearAreaCeilingTypeSelector2.setEnabled(false);
				leftCorridorCeilingTypeSelector1.setEnabled(false);
				leftCorridorCeilingTypeSelector2.setEnabled(false);
				rightCorridorCeilingTypeSelector1.setEnabled(false);
				rightCorridorCeilingTypeSelector2.setEnabled(false);
				mainChamberCeilingTypeSelector1.setEnabled(true);
				mainChamberCeilingTypeSelector2.setEnabled(true);
				corridorCeilingTypeSelector1.setEnabled(false);
				corridorCeilingTypeSelector2.setEnabled(false);
				antechamberCeilingTypeSelector1.setEnabled(true);
				antechamberCeilingTypeSelector2.setEnabled(true);
				break;

			case 3: // residential cave
				rearAreaCeilingTypeSelector1.setEnabled(true);
				rearAreaCeilingTypeSelector2.setEnabled(true);
				leftCorridorCeilingTypeSelector1.setEnabled(false);
				leftCorridorCeilingTypeSelector2.setEnabled(false);
				rightCorridorCeilingTypeSelector1.setEnabled(false);
				rightCorridorCeilingTypeSelector2.setEnabled(false);
				mainChamberCeilingTypeSelector1.setEnabled(true);
				mainChamberCeilingTypeSelector2.setEnabled(true);
				corridorCeilingTypeSelector1.setEnabled(true);
				corridorCeilingTypeSelector2.setEnabled(true);
				antechamberCeilingTypeSelector1.setEnabled(true);
				antechamberCeilingTypeSelector2.setEnabled(true);
				break;

			case 4: // central-pillar cave
				rearAreaCeilingTypeSelector1.setEnabled(true);
				rearAreaCeilingTypeSelector2.setEnabled(true);
				leftCorridorCeilingTypeSelector1.setEnabled(true);
				leftCorridorCeilingTypeSelector2.setEnabled(true);
				rightCorridorCeilingTypeSelector1.setEnabled(true);
				rightCorridorCeilingTypeSelector2.setEnabled(true);
				mainChamberCeilingTypeSelector1.setEnabled(true);
				mainChamberCeilingTypeSelector2.setEnabled(true);
				corridorCeilingTypeSelector1.setEnabled(true);
				corridorCeilingTypeSelector2.setEnabled(true);
				antechamberCeilingTypeSelector1.setEnabled(true);
				antechamberCeilingTypeSelector2.setEnabled(true);
				break;

			case 1: // unknown
			case 5: // storage cave
			case 7: // other
			case 8: // lecture hall
			case 9: // niche
				rearAreaCeilingTypeSelector1.setEnabled(false);
				rearAreaCeilingTypeSelector2.setEnabled(false);
				leftCorridorCeilingTypeSelector1.setEnabled(false);
				leftCorridorCeilingTypeSelector2.setEnabled(false);
				rightCorridorCeilingTypeSelector1.setEnabled(false);
				rightCorridorCeilingTypeSelector2.setEnabled(false);
				mainChamberCeilingTypeSelector1.setEnabled(true);
				mainChamberCeilingTypeSelector2.setEnabled(true);
				corridorCeilingTypeSelector1.setEnabled(false);
				corridorCeilingTypeSelector2.setEnabled(false);
				antechamberCeilingTypeSelector1.setEnabled(false);
				antechamberCeilingTypeSelector2.setEnabled(false);
				break;

			case 6: // monumental image cave
				rearAreaCeilingTypeSelector1.setEnabled(true);
				rearAreaCeilingTypeSelector2.setEnabled(true);
				leftCorridorCeilingTypeSelector1.setEnabled(true);
				leftCorridorCeilingTypeSelector2.setEnabled(true);
				rightCorridorCeilingTypeSelector1.setEnabled(true);
				rightCorridorCeilingTypeSelector2.setEnabled(true);
				mainChamberCeilingTypeSelector1.setEnabled(true);
				mainChamberCeilingTypeSelector2.setEnabled(true);
				corridorCeilingTypeSelector1.setEnabled(false);
				corridorCeilingTypeSelector2.setEnabled(false);
				antechamberCeilingTypeSelector1.setEnabled(true);
				antechamberCeilingTypeSelector2.setEnabled(true);
				break;

			default:
				rearAreaCeilingTypeSelector1.setEnabled(false);
				rearAreaCeilingTypeSelector2.setEnabled(false);
				leftCorridorCeilingTypeSelector1.setEnabled(false);
				leftCorridorCeilingTypeSelector2.setEnabled(false);
				rightCorridorCeilingTypeSelector1.setEnabled(false);
				rightCorridorCeilingTypeSelector2.setEnabled(false);
				mainChamberCeilingTypeSelector1.setEnabled(false);
				mainChamberCeilingTypeSelector2.setEnabled(false);
				corridorCeilingTypeSelector1.setEnabled(false);
				corridorCeilingTypeSelector2.setEnabled(false);
				antechamberCeilingTypeSelector1.setEnabled(false);
				antechamberCeilingTypeSelector2.setEnabled(false);
				break;
		}
	}

	private void updateStateOfPreservationPanel(int caveTypeID) {

		switch (caveTypeID) {
			case 2: // square cave
				rearAreaPreservationSelectorCB.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(false);
				rearAreaFloorPreservationSelectorCB.setEnabled(false);
				leftCorridorPreservationSelectorCB.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				leftCorridorFloorPreservationSelectorCB.setEnabled(false);
				rightCorridorPreservationSelectorCB.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				rightCorridorFloorPreservationSelectorCB.setEnabled(false);
				mainChamberPreservationSelectorCB.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(true);
				mainChamberFloorPreservationSelectorCB.setEnabled(true);
				corridorPreservationSelectorCB.setEnabled(false);
				corridorCeilingPreservationSelectorCB1.setEnabled(false);
				corridorCeilingPreservationSelectorCB2.setEnabled(false);
				corridorFloorPreservationSelectorCB.setEnabled(false);
				antechamberPreservationSelectorCB.setEnabled(true);
				antechamberCeilingPreservationSelectorCB1.setEnabled(true);
				antechamberCeilingPreservationSelectorCB2.setEnabled(true);
				antechamberFloorPreservationSelectorCB.setEnabled(true);
				break;

			case 3: // residential cave
				rearAreaPreservationSelectorCB.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(true);
				rearAreaFloorPreservationSelectorCB.setEnabled(true);
				leftCorridorPreservationSelectorCB.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				leftCorridorFloorPreservationSelectorCB.setEnabled(false);
				rightCorridorPreservationSelectorCB.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				rightCorridorFloorPreservationSelectorCB.setEnabled(false);
				mainChamberPreservationSelectorCB.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(true);
				mainChamberFloorPreservationSelectorCB.setEnabled(true);
				corridorPreservationSelectorCB.setEnabled(true);
				corridorCeilingPreservationSelectorCB1.setEnabled(true);
				corridorCeilingPreservationSelectorCB2.setEnabled(true);
				corridorFloorPreservationSelectorCB.setEnabled(true);
				antechamberPreservationSelectorCB.setEnabled(true);
				antechamberCeilingPreservationSelectorCB1.setEnabled(true);
				antechamberCeilingPreservationSelectorCB2.setEnabled(true);
				antechamberFloorPreservationSelectorCB.setEnabled(true);
				break;

			case 4: // central-pillar cave
				rearAreaPreservationSelectorCB.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(true);
				rearAreaFloorPreservationSelectorCB.setEnabled(true);
				leftCorridorPreservationSelectorCB.setEnabled(true);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(true);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(true);
				leftCorridorFloorPreservationSelectorCB.setEnabled(true);
				rightCorridorPreservationSelectorCB.setEnabled(true);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(true);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(true);
				rightCorridorFloorPreservationSelectorCB.setEnabled(true);
				mainChamberPreservationSelectorCB.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(true);
				mainChamberFloorPreservationSelectorCB.setEnabled(true);
				corridorPreservationSelectorCB.setEnabled(true);
				corridorCeilingPreservationSelectorCB1.setEnabled(true);
				corridorCeilingPreservationSelectorCB2.setEnabled(true);
				corridorFloorPreservationSelectorCB.setEnabled(true);
				antechamberPreservationSelectorCB.setEnabled(true);
				antechamberCeilingPreservationSelectorCB1.setEnabled(true);
				antechamberCeilingPreservationSelectorCB2.setEnabled(true);
				antechamberFloorPreservationSelectorCB.setEnabled(true);
				break;

			case 6: // monumental image cave
				rearAreaPreservationSelectorCB.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(true);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(true);
				rearAreaFloorPreservationSelectorCB.setEnabled(true);
				leftCorridorPreservationSelectorCB.setEnabled(true);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(true);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(true);
				leftCorridorFloorPreservationSelectorCB.setEnabled(true);
				rightCorridorPreservationSelectorCB.setEnabled(true);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(true);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(true);
				rightCorridorFloorPreservationSelectorCB.setEnabled(true);
				mainChamberPreservationSelectorCB.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(true);
				mainChamberFloorPreservationSelectorCB.setEnabled(true);
				corridorPreservationSelectorCB.setEnabled(false);
				corridorCeilingPreservationSelectorCB1.setEnabled(false);
				corridorCeilingPreservationSelectorCB2.setEnabled(false);
				corridorFloorPreservationSelectorCB.setEnabled(false);
				antechamberPreservationSelectorCB.setEnabled(true);
				antechamberCeilingPreservationSelectorCB1.setEnabled(true);
				antechamberCeilingPreservationSelectorCB2.setEnabled(true);
				antechamberFloorPreservationSelectorCB.setEnabled(true);
				break;

			case 1: // unknown
			case 5: // storage cave
			case 7: // other
			case 8: // lecture hall
			case 9: // niche
				rearAreaPreservationSelectorCB.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(false);
				rearAreaFloorPreservationSelectorCB.setEnabled(false);
				leftCorridorPreservationSelectorCB.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				leftCorridorFloorPreservationSelectorCB.setEnabled(false);
				rightCorridorPreservationSelectorCB.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				rightCorridorFloorPreservationSelectorCB.setEnabled(false);
				mainChamberPreservationSelectorCB.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(true);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(true);
				mainChamberFloorPreservationSelectorCB.setEnabled(false);
				corridorPreservationSelectorCB.setEnabled(false);
				corridorCeilingPreservationSelectorCB1.setEnabled(false);
				corridorCeilingPreservationSelectorCB2.setEnabled(false);
				corridorFloorPreservationSelectorCB.setEnabled(false);
				antechamberPreservationSelectorCB.setEnabled(false);
				antechamberCeilingPreservationSelectorCB1.setEnabled(false);
				antechamberCeilingPreservationSelectorCB2.setEnabled(false);
				antechamberFloorPreservationSelectorCB.setEnabled(false);
				break;

			default:
				rearAreaPreservationSelectorCB.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB1.setEnabled(false);
				rearAreaCeilingPreservationSelectorCB2.setEnabled(false);
				rearAreaFloorPreservationSelectorCB.setEnabled(false);
				leftCorridorPreservationSelectorCB.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				leftCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				leftCorridorFloorPreservationSelectorCB.setEnabled(false);
				rightCorridorPreservationSelectorCB.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB1.setEnabled(false);
				rightCorridorCeilingPreservationSelectorCB2.setEnabled(false);
				rightCorridorFloorPreservationSelectorCB.setEnabled(false);
				mainChamberPreservationSelectorCB.setEnabled(false);
				mainChamberCeilingPreservationSelectorCB1.setEnabled(false);
				mainChamberCeilingPreservationSelectorCB2.setEnabled(false);
				mainChamberFloorPreservationSelectorCB.setEnabled(false);
				corridorPreservationSelectorCB.setEnabled(false);
				corridorCeilingPreservationSelectorCB1.setEnabled(false);
				corridorCeilingPreservationSelectorCB2.setEnabled(false);
				corridorFloorPreservationSelectorCB.setEnabled(false);
				antechamberPreservationSelectorCB.setEnabled(false);
				antechamberCeilingPreservationSelectorCB1.setEnabled(false);
				antechamberCeilingPreservationSelectorCB2.setEnabled(false);
				antechamberFloorPreservationSelectorCB.setEnabled(false);
				break;
		}
	}

	public void updateWallList(int caveTypeID) {
		wallEntryLS.clear();
		switch (caveTypeID) {
			// 'antechamber','main chamber','main chamber corridor','rear area left corridor','rear area right corridor','rear area'
			case 2: // square cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if (((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)) && wle.getLabel().contains("wall")) {
						wallEntryLS.add(correspondingCaveEntry.getWall(wle.getWallLocationID()));
					}
				}
				break;

			case 3: // resitential cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if (((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_CORRIDOR_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL)) && wle.getLabel().contains("wall")) {
						wallEntryLS.add(correspondingCaveEntry.getWall(wle.getWallLocationID()));
					}
				}
				break;

			case 4: // central-pillar cave
			case 6: // monumental image cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if (((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL)) && wle.getLabel().contains("wall")) {
						wallEntryLS.add(correspondingCaveEntry.getWall(wle.getWallLocationID()));
					}
				}
				break;

			default:
				break;
		}
	}

	/**
	 * Will be called when the save button is selected. After saving <code>CaveEditorListener.closeRequest()</code> is called to inform all listener.
	 */
	protected void saveEntries(boolean close) {
		if (siteSelection.validate() && officialNumberField.validate()) {
			
			if (correspondingCaveEntry.getCaveID() > 0) {
				dbService.updateCaveEntry(correspondingCaveEntry, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Util.showWarning("A problem occurred", "The changes could not be saved!");
					}

					@Override
					public void onSuccess(Boolean result) {
//						updateEntry(correspondingCaveEntry);
						if (close) {
							closeEditor(correspondingCaveEntry);
						}
					}
				});

			} else { // its 0 and we need to create a new entry
				dbService.insertCaveEntry(correspondingCaveEntry, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						Util.showWarning("A problem occurred", "The new Cave could not be saved!");
					}

					@Override
					public void onSuccess(Integer result) {
						correspondingCaveEntry.setCaveID(result.intValue());
//						updateEntry(correspondingCaveEntry);
						if (close) {
							closeEditor(correspondingCaveEntry);
						}
					}
				});
				
			}
			
		}
		
	}

	/**
	 * 
	 */
	private void activateRegionFilter() {
		regionFilter = new StoreFilter<RegionEntry>() {

			@Override
			public boolean select(Store<RegionEntry> store, RegionEntry parent, RegionEntry item) {
				return (item.getSiteID() == siteSelection.getCurrentValue().getSiteID());
			}
		};
		regionEntryListStore.addFilter(regionFilter);
		regionEntryListStore.setEnableFilters(true);
		regionSelection.setEnabled(true); // we enable the selector only after a site is selected 
	}

	/**
	 * 
	 */
	private void activateDistrictFilter() {
		districtFilter = new StoreFilter<DistrictEntry>() {

			@Override
			public boolean select(Store<DistrictEntry> store, DistrictEntry parent, DistrictEntry item) {
				return (item.getSiteID() == siteSelection.getCurrentValue().getSiteID());
			}

		};
		districtEntryList.addFilter(districtFilter);
		districtEntryList.setEnableFilters(true);
		districtSelection.setEnabled(true);
	}

	private ComboBox<CeilingTypeEntry> createCeilingTypeSelector(String emptyText) {
		ComboBox<CeilingTypeEntry> ceilingTypeSelector = new ComboBox<CeilingTypeEntry>(ceilingTypeEntryList, ceilingTypeProps.name(),
				new AbstractSafeHtmlRenderer<CeilingTypeEntry>() {

					@Override
					public SafeHtml render(CeilingTypeEntry item) {
						return ctvt.caveTypeLabel(item.getName());
					}
				});
		ceilingTypeSelector.setEmptyText(emptyText);
		ceilingTypeSelector.setTypeAhead(false);
		ceilingTypeSelector.setEditable(false);
		ceilingTypeSelector.setTriggerAction(TriggerAction.ALL);
		return ceilingTypeSelector;
	}

	private ComboBox<PreservationClassificationEntry> createStateOfPreservationSelector(String emptyText) {
		ComboBox<PreservationClassificationEntry> selectorCB = new ComboBox<PreservationClassificationEntry>(
				preservationClassificationEntryList, preservationClassificationProps.name(),
				new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		selectorCB.setEmptyText(emptyText);
		selectorCB.setTypeAhead(false);
		selectorCB.setEditable(false);
		selectorCB.setTriggerAction(TriggerAction.ALL);
		return selectorCB;
	}

}
