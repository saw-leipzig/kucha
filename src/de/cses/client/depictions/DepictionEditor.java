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
package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.core.java.util.Collections;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent.DndDragStartHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DndDropEvent.DndDropHandler;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.fx.client.Draggable.DraggableAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent;
import com.sencha.gxt.widget.core.client.event.ResizeEndEvent.ResizeEndHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.bibliography.BibliographySelector;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.OSDListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.ui.TextElement;
import de.cses.client.user.UserLogin;
import de.cses.client.walls.PositionEditor;
import de.cses.client.walls.WallSelector;
import de.cses.client.walls.WallTree;
import de.cses.client.walls.Walls;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.ModeOfRepresentationEntry;
import de.cses.shared.ModifiedEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallTreeEntry;
import de.cses.shared.comparator.CaveEntryComparator;

public class DepictionEditor extends AbstractEditor {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private TextArea inscriptionsTestArea;
	private TextField datingField;
	private NumberField<Double> widthNF;
	private NumberField<Double> heightNF;
	private DateField purchaseDateField;
	private DateField dateOfAcquisitionField;
	private TextArea descriptionArea;
	private TextField backgroundColourField;
	private TextArea generalRemarksArea;
	protected IconographySelector iconographySelector;
	protected ImageSelector imageSelector;
	private FramedPanel mainPanel;
	protected PopupPanel imageSelectionDialog;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ListStore<ImageEntry> imageEntryLS;
	private ImageProperties imgProperties;
	private DepictionEntry correspondingDepictionEntry;
	private VendorProperties vendorProps;
	private ListStore<VendorEntry> vendorEntryLS;
	private ComboBox<VendorEntry> vendorSelection;
	private ComboBox<StyleEntry> styleSelection;
	private StyleProperties styleProps;
	private ListStore<StyleEntry> styleEntryLS;
	private CaveProperties caveProps;
	private Map<Integer, String> imgdic;
	private ListStore<CaveEntry> caveEntryLS;
	private ComboBox<CaveEntry> caveSelectionCB;
	private ExpeditionProperties expedProps;
	private ListStore<ExpeditionEntry> expedEntryLS;
	private ComboBox<ExpeditionEntry> expedSelectionCB;
	private ComboBox<ModeOfRepresentationEntry> modeOfRepresentationSelectionCB;
	protected PopupPanel wallEditorDialog;
	private Walls wallEditor;
	protected PopupPanel iconographySelectionDialog;
	private WallSelector wallSelectorPanel;
	private TextArea separateAksarasTextArea;
	private ModesOfRepresentationProperties morProps;
	private ListStore<ModeOfRepresentationEntry> modeOfRepresentationLS;
	private ComboBox<LocationEntry> locationSelectionCB;
	private LocationProperties locationProps;
	private ListStore<LocationEntry> locationEntryLS;
	private PreservationAttributeProperties presAttributeProps;
	private ListStore<PreservationAttributeEntry> preservationAttributesLS, selectedPreservationAttributesLS;
	private TextField shortNameTF;
	private BibliographySelector bibliographySelector;
	private StoreFilterField<ImageEntry> filterField;
	private OSDLoader osdLoader;
	private boolean annotationsLoaded = true;
	private double imageWindowRelation;
	private OSDListener osdListener;
	private WallTree wallTree;
	private boolean saveSuccess;
	private IconographySelectorListener icoSelectorListener;
	private ArrayList<IconographyEntry> iconographyRelationList;
	private ToolButton saveToolButton;
	private FramedPanel depictionImagesPanel;
	private TabPanel tabPanel;
	private HorizontalLayoutData tabLayout;
	private HorizontalLayoutData imageLayout;
	private Resizable imgResize;
	private HorizontalLayoutContainer mainHLC;


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
	public static Integer maxUsingIteration(Map<Integer, Integer> map) {
		Map.Entry<Integer, Integer> maxEntry = null;
	    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
	        if (maxEntry == null || entry.getValue()
	            .compareTo(maxEntry.getValue()) > 0) {
	            maxEntry = entry;
	        }
	    }
	    return maxEntry.getValue();
	}

	public void loadiconogrpahy(ArrayList<IconographyEntry> iconographyRelationList) {
				this.iconographyRelationList = iconographyRelationList;
				iconographySelector.setSelectedIconography(iconographyRelationList);
				iconographySelector.IconographyTreeEnabled(true);
				getListenerList().get(0).setClickNumber(0);
	}

	interface DepictionProperties extends PropertyAccess<DepictionEntry> {
		ModelKeyProvider<DepictionEntry> depictionID();

		LabelProvider<DepictionEntry> name();
	}

	interface DepictionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml depiction(String name);
	}

	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();

		LabelProvider<CaveEntry> officialNumber();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {district}<br> {line1}<br> {line2}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String district, String line1, String line2);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {district}<br> {line}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String district, String line);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName} {officialNumber}<br> {district}</div>")
		SafeHtml caveLabel(String shortName, String officialNumber, String district);

		@XTemplate("<div style=\"border: 1px solid grey;\">{shortName}</div>")
		SafeHtml caveLabel(String shortName);
	}

	interface LocationProperties extends PropertyAccess<LocationEntry> {
		ModelKeyProvider<LocationEntry> locationID();

		LabelProvider<LocationEntry> name();
	}

	interface LocationViewTemplates extends XTemplates {
		@XTemplate("<div>{country}, {town}<br>{name}</div>")
		SafeHtml caveLabel(String name, String town, String country);

		@XTemplate("<div>{country}<br>{name}</div>")
		SafeHtml caveLabel(String name, String country);

		@XTemplate("<div>{name}</div>")
		SafeHtml caveLabel(String name);
	}

	interface ExpeditionProperties extends PropertyAccess<ExpeditionEntry> {
		ModelKeyProvider<ExpeditionEntry> expeditionID();

		LabelProvider<ExpeditionEntry> name();
	}

	interface ExpeditionViewTemplates extends XTemplates {
		@XTemplate("<div>{expedName}<br>Leader: {leaderName}<br>{startYear} - {endYear}</div>")
		SafeHtml expedLabel(String expedName, String leaderName, String startYear, String endYear);
	}

	interface ModesOfRepresentationProperties extends PropertyAccess<ModeOfRepresentationEntry> {
		ModelKeyProvider<ModeOfRepresentationEntry> modeOfRepresentationID();

		LabelProvider<ModeOfRepresentationEntry> name();
	}

	interface ModesOfRepresentationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml morLabel(String name);
	}

	interface VendorProperties extends PropertyAccess<VendorEntry> {
		ModelKeyProvider<VendorEntry> vendorID();

		LabelProvider<VendorEntry> vendorName();
	}

	interface VendorViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml vendorName(String name);
	}

	interface StyleProperties extends PropertyAccess<StyleEntry> {
		ModelKeyProvider<StyleEntry> styleID();

		LabelProvider<StyleEntry> styleName();
	}

	interface StyleViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml styleName(String name);
	}

	interface PreservationAttributeProperties extends PropertyAccess<PreservationAttributeEntry> {
		ModelKeyProvider<PreservationAttributeEntry> preservationAttributeID();

		LabelProvider<PreservationAttributeEntry> uniqueID();

		ValueProvider<PreservationAttributeEntry, String> name();
	}

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();

		ValueProvider<ImageEntry, String> shortName();
	}

	public DepictionEditor(DepictionEntry entry, EditorListener av) {
		this.addEditorListener(av);
		if (entry != null) {
			correspondingDepictionEntry = entry;
		} else {
			correspondingDepictionEntry = new DepictionEntry();
		}
		this.correspondingDepictionEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());
		// Util.doLogging("Große von
		// ImageList:"+Integer.toString(correspondingDepictionEntry.getRelatedImages().size()));
		for (WallTreeEntry wte : correspondingDepictionEntry.getWalls()) {
			Util.doLogging(wte.getText() + " - " + wte.getWallLocationID());
		}
		imgProperties = GWT.create(ImageProperties.class);
		imageEntryLS = new ListStore<ImageEntry>(imgProperties.imageID());

		vendorProps = GWT.create(VendorProperties.class);
		vendorEntryLS = new ListStore<VendorEntry>(vendorProps.vendorID());

		styleProps = GWT.create(StyleProperties.class);
		styleEntryLS = new ListStore<StyleEntry>(styleProps.styleID());

		caveProps = GWT.create(CaveProperties.class);
		caveEntryLS = new ListStore<CaveEntry>(caveProps.caveID());
		caveEntryLS.addSortInfo(new StoreSortInfo<CaveEntry>(new CaveEntryComparator(), SortDir.ASC));
		locationProps = GWT.create(LocationProperties.class);
		locationEntryLS = new ListStore<LocationEntry>(locationProps.locationID());
		StoreFilter<LocationEntry> filter = new StoreFilter<LocationEntry>() {
			@Override
			public boolean select(Store<LocationEntry> store, LocationEntry parent, LocationEntry item) {

				boolean canView = false;

				if ((item.getCounty() != null)) {
					if (item.getCounty().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
						canView = true;
					}
				}
				;
				if ((item.getTown() != null)) {
					if (item.getTown().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
						canView = true;
					}
				}
				;
				if ((item.getName() != null)) {
					if (item.getName().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
						canView = true;
					}
				}
				;
				// if (canView) {
				// Util.doLogging("Found: "+item.getName()+", gesucht wurde:
				// "+locationSelectionCB.getText());
				// };
				return canView;
			}
		};
		locationEntryLS.addFilter(filter);

		expedProps = GWT.create(ExpeditionProperties.class);
		expedEntryLS = new ListStore<ExpeditionEntry>(expedProps.expeditionID());

		morProps = GWT.create(ModesOfRepresentationProperties.class);
		modeOfRepresentationLS = new ListStore<ModeOfRepresentationEntry>(morProps.modeOfRepresentationID());

		presAttributeProps = GWT.create(PreservationAttributeProperties.class);
		preservationAttributesLS = new ListStore<PreservationAttributeEntry>(
				presAttributeProps.preservationAttributeID());

		selectedPreservationAttributesLS = new ListStore<PreservationAttributeEntry>(
				presAttributeProps.preservationAttributeID());
		initPanel();
		loadCaves();
		loadLocations();
		loadStyles();
		loadVendors();
		loadExpeditions();
		loadModesOfRepresentation();
		loadPreservationAttributes();
	}
	private ArrayList<AnnotationEntry> findAllIcos(IconographyEntry selectedIE, ArrayList<AnnotationEntry> collectedEntries) {
		for (AnnotationEntry ae : correspondingDepictionEntry.getRelatedAnnotationList()) {
			for (IconographyEntry ie : ae.getTags()) {
				if (ie.getIconographyID() == selectedIE.getIconographyID()) {
					boolean found = false;
					for (AnnotationEntry collectedAe : collectedEntries) {
						if (collectedAe.getAnnotoriousID() == ae.getAnnotoriousID()) {
							found = true;
							break;
						}
					}
					if (!found) {
						collectedEntries.add(ae);									
					}
				}
			}
		}
		if (selectedIE.getChildren() != null) {
			// Util.doLogging("selectedIE has children");
			for (IconographyEntry child : selectedIE.getChildren()) {
				collectedEntries = findAllIcos(child, collectedEntries);
			}
		}
		return collectedEntries;
	}
	// this method should be added to osdLader
	private void highlightIcoEntry(IconographyEntry selectedIE, boolean deselect, List<IconographyEntry>clickedIcos) {
		//Util.doLogging("triggered highlightIcoEntry");
		//Util.doLogging("Started highlighting for Iconography: "+selectedIE.getText());
		ArrayList<AnnotationEntry> newAnnos = new ArrayList<AnnotationEntry>();
		newAnnos = findAllIcos(selectedIE, newAnnos);
		// Util.doLogging("found "+Integer.toString(newAnnos.size())+" annos");
		if (newAnnos.size()>0) {
			if (annotationsLoaded) {
				osdLoader.removeOrAddAnnotations(newAnnos,!deselect);
				osdLoader.setHasContourAllign(false);

			}				
		}
//		for (AnnotationEntry aeSelected : newAnnos) {
//			if (deselect) {
//				osdLoader.deHighlightAnnotation(aeSelected.getAnnotoriousID());
//			}
//			else {
//				osdLoader.highlightAnnotation(aeSelected.getAnnotoriousID());
//			}
//			
//		}

	}

	private void loadExpeditions() {
		for (ExpeditionEntry exped : StaticTables.getInstance().getExpeditionEntries().values()) {
			expedEntryLS.add(exped);
		}
		if (correspondingDepictionEntry.getExpedition() != null) {
			expedSelectionCB.setValue(correspondingDepictionEntry.getExpedition());
		}
	}

	/**
	 * 
	 */
	private void loadVendors() {
		for (VendorEntry ve : StaticTables.getInstance().getVendorEntries().values()) {
			vendorEntryLS.add(ve);
		}
		if (correspondingDepictionEntry.getVendor() != null) {
			vendorSelection.setValue(correspondingDepictionEntry.getVendor());
		}
		vendorEntryLS.addSortInfo(new StoreSortInfo<VendorEntry>(new ValueProvider<VendorEntry, String>() {

			@Override
			public String getValue(VendorEntry object) {
				return object.getVendorName();
			}

			@Override
			public void setValue(VendorEntry object, String value) {
			}

			@Override
			public String getPath() {
				return "vendorName";
			}
		}, SortDir.ASC));
	}

	/**
	 * 
	 */
	private void loadModesOfRepresentation() {
		for (ModeOfRepresentationEntry morEntry : StaticTables.getInstance().getModesOfRepresentationEntries()
				.values()) {
			modeOfRepresentationLS.add(morEntry);
		}
		if (correspondingDepictionEntry.getModeOfRepresentationID() > 0) {
			modeOfRepresentationSelectionCB.setValue(modeOfRepresentationLS
					.findModelWithKey(Integer.toString(correspondingDepictionEntry.getModeOfRepresentationID())));
		}
	}

	/**
	 * 
	 */
	private void loadStyles() {
		for (StyleEntry se : StaticTables.getInstance().getStyleEntries().values()) {
			styleEntryLS.add(se);
		}
		if (correspondingDepictionEntry.getStyleID() > 0) {
			styleSelection.setValue(
					styleEntryLS.findModelWithKey(Integer.toString(correspondingDepictionEntry.getStyleID())));
		}
	}

	/**
	 * 
	 */
	private void loadCaves() {
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> caveResults) {
				CaveEntry unknownCave = new CaveEntry();
				unknownCave.setCaveID(-1);
				caveEntryLS.add(unknownCave);
				for (CaveEntry ce : caveResults) {
					caveEntryLS.add(ce);
				}
				
				if (correspondingDepictionEntry.getCave() != null) {
					CaveEntry ce = correspondingDepictionEntry.getCave();
//					CaveEntry ce = caveEntryLS.findModelWithKey(Integer.toString(correspondingDepictionEntry.getCaveID()));
					caveSelectionCB.setValue(ce);
					wallSelectorPanel.setCave(ce);
					// wallSelectorPanel.selectWall(correspondingDepictionEntry.getWallID());
//					Util.doLogging("Ausgewählte Walls:");
//					for (WallTreeEntry wte : correspondingDepictionEntry.getWalls()) {
//						Util.doLogging("  "+wte.getText());
//						if (wte.getPosition()!=null) {
//							for (PositionEntry pe : wte.getPosition()) {
//								Util.doLogging("    - "+pe.getName());
//							}									
//						}
//					}

					// wallTree.setWall(correspondingDepictionEntry.getWalls());
//					for (WallEntry we : ce.getWallList()) {
//						Util.doLogging(Integer.toString(we.getWallLocationID()));
//					}
				}
			}
		});
	}

	private void loadLocations() {
		for (LocationEntry locEntry : StaticTables.getInstance().getLocationEntries().values()) {
			locationEntryLS.add(locEntry);
		}
		locationEntryLS.addSortInfo(new StoreSortInfo<LocationEntry>(new ValueProvider<LocationEntry, String>() {

			@Override
			public String getValue(LocationEntry object) {
				return object.getCounty() + " " + object.getTown() + " " + object.getName();
			}

			@Override
			public void setValue(LocationEntry object, String value) {
			}

			@Override
			public String getPath() {
				return "name";
			}
		}, SortDir.ASC));

		if (correspondingDepictionEntry.getLocation() != null) {
			locationSelectionCB.setValue(correspondingDepictionEntry.getLocation());
		}
	}

	private void loadPreservationAttributes() {
		dbService.getPreservationAttributes(new AsyncCallback<ArrayList<PreservationAttributeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PreservationAttributeEntry> result) {
				preservationAttributesLS.clear();
				for (PreservationAttributeEntry pae : result) {
					preservationAttributesLS.add(pae);
				}
				preservationAttributesLS.addSortInfo(
						new StoreSortInfo<PreservationAttributeEntry>(presAttributeProps.name(), SortDir.ASC));
			}
		});
	}

	/**
	 * 
	 */
	private void loadImages() {
		for (ImageEntry ie : correspondingDepictionEntry.getRelatedImages()) {
			if (imageEntryLS.findModelWithKey(Integer.toString(ie.getImageID())) == null) {
				imageEntryLS.add(ie);
			}

		}
	}

	/**
	 * 
	 */

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	/**
	 * Here the view is created. This is only done once at the beginning!
	 */
	private void getPics(ArrayList<ImageEntry> ies, int size, String login) {
		dbService.getPics(ies, size, login, new AsyncCallback<Map<Integer, String>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				// Info.display("getPics", "got bad response");
			}

			@Override
			public void onSuccess(Map<Integer, String> result) {
				// .display("getPics", "got good response");
				// for (Map.Entry<String,String> entry : result.entrySet())
				// Util.doLogging("Key = " + entry.getKey() +
				// ", Value = " + entry.getValue());
				for (Integer key : result.keySet()) {
					try {
						if (!imgdic.containsKey(key)) {
							imgdic.put(key, result.get(key));
						}

					} catch (Exception e) {
						Util.doLogging("Could not load image " + key + " Reason: " + e.getMessage());
					}
				}
				// imageListView.refresh();
				// loadImages();

			}
		});
	}

	public class WidgetGridCell extends AbstractCell<Widget> {

		Widget widget;

		public WidgetGridCell(Widget widget) {
			this.widget = widget;
		}

		@Override
		public void render(Context paramContext, Widget param, SafeHtmlBuilder pb) {

			// add text to the button, etc...
			pb.append(SafeHtmlUtils.fromTrustedString(widget.toString()));
		}
	}

	private void initPanel() {
		System.out.println("Length bibliographyEntries of Depiction " + Integer.toString(correspondingDepictionEntry.getDepictionID()) + "at load: " + Integer.toString(correspondingDepictionEntry.getRelatedBibliographyList().size()));
		// the images related with the depiction entry that will be shown on the right
		imgdic = new HashMap<Integer, String>();
		getPics(correspondingDepictionEntry.getRelatedImages(), 300, UserLogin.getInstance().getSessionID());
		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryLS, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});
		SimpleSafeHtmlCell<ImageEntry> imageCell = new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);
			public SafeHtml render(ImageEntry item) {
				ArrayList<TextElement> titleList = new ArrayList<TextElement>();
				for (String s : item.getTitle().split("_")) {
					titleList.add(new TextElement(s));
				}
				String imageAuthor = item.getImageAuthor() != null ? "Author: " + item.getImageAuthor().getLabel() : "";
				String copyrightStr = (item.getCopyright() != null && item.getCopyright().length() > 0)
						? "\u00A9 " + item.getCopyright()
						: "";
				SafeHtml sb;
				long now = new Date().getTime();  
				if (item.getImageID() == correspondingDepictionEntry.getMasterImageID()) {
					sb = imageViewTemplates.masterImage(item.getFilename(), item.getShortName(), titleList,
							item.getFilename().substring(item.getFilename().lastIndexOf(".") + 1).toUpperCase(),
							imageAuthor, copyrightStr, UriUtils.fromString("resource?imageID=" + item.getImageID()
									+ UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
				} else if (((item.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_PUBLIC) && (!item.getIsExpiring()))|| ((item.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_PUBLIC) && (item.getIsExpiring())&&(now < item.getExpiriesAt()))) {
					sb = imageViewTemplates.publicImage(item.getFilename(), item.getShortName(), titleList,
							item.getFilename().substring(item.getFilename().lastIndexOf(".") + 1).toUpperCase(),
							imageAuthor, copyrightStr, UriUtils.fromString("resource?imageID=" + item.getImageID()
									+ UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
				} else {
					sb = imageViewTemplates.nonPublicImage(item.getFilename(), item.getShortName(), titleList,
							item.getFilename().substring(item.getFilename().lastIndexOf(".") + 1).toUpperCase(),
							imageAuthor, copyrightStr, UriUtils.fromString("resource?imageID=" + item.getImageID()
									+ UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
				}
				SafeHtml s = null;
				// Util.doLogging("ImageListView:
				// "+Double.toString(Window.getClientWidth()*0.8*imageWindowRelation));
				if (depictionImagesPanel.isVisible()) {
					imageWindowRelation = ((double) depictionImagesPanel.getOffsetWidth(true)
							/ (double) mainPanel.getOffsetWidth(true));
					
				}
				if (Window.getClientWidth() * 0.8 * imageWindowRelation > 300) {
					s = SafeHtmlUtils.fromTrustedString("<figure class='paintRepImgPreview' style='height: "
							+ Integer.toString((int) (Window.getClientHeight() * (imageWindowRelation)))
							+ "px;width:98%;text-align: center;'><div id= '" + item.getFilename().split(";")[0]
							+ "' style='overflow:hidden;width: 100%; height: "
							+ Integer.toString((int) (Window.getClientHeight() * (imageWindowRelation))-65)
							+ "px;text-align: center;' ></div>");
				} else {
					s = SafeHtmlUtils.fromTrustedString(
							"<figure class='paintRepImgPreview' style='width: 340px;height:290px;text-align: center;'><div id= '"
									+ item.getFilename().split(";")[0]
									+ "' Style='width: 340px;height:290px;text-align: center;' ></div>");
				}
				SafeHtmlBuilder sblast = new SafeHtmlBuilder();
				sblast.append(s);
				sblast.append(sb);

				return sblast.toSafeHtml();
			}
		});
		imageListView.setCell(imageCell);
		filterField = new StoreFilterField<ImageEntry>() {

			@Override
			protected boolean doSelect(Store<ImageEntry> store, ImageEntry parent, ImageEntry item, String filter) {
				ListStore<ImageEntry> ImageStore = (ListStore<ImageEntry>) store;
//				Util.doLogging(item.getTitle()+" - "+filter+" = "+Boolean.toString(item.getFilename().contains(filter)));
				if (item.getTitle().toLowerCase().contains(filter.toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		filterField.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				Util.doLogging("onValueChange triggered");
				reloadPics();
				
			}
			
		});
		filterField.setEmptyText("enter a search term");
		filterField.bind(imageEntryLS);
		imageEntryLS.addStoreFilterHandler(new StoreFilterHandler<ImageEntry>() {

			@Override
			public void onFilter(StoreFilterEvent<ImageEntry> event) {
				Util.doLogging("StoreFilterHandler triggered");
				reloadPics();
				
			}
			
		});
		// imageListView.setSize("340", "290");
//		Util.doLogging("Size of ImageListView: "+Integer.toString(imageEntryLS.size()));
		ListField<ImageEntry, ImageEntry> imageViewLF = new ListField<ImageEntry, ImageEntry>(imageListView);
		loadImages();
		if (correspondingDepictionEntry.getImageSortInfo() == null) {
			Util.doLogging("setting simple sort modus");
			Comparator<? super ImageEntry> defaultItemComparator = new Comparator<ImageEntry>() {
				@Override
				public int compare(ImageEntry o1, ImageEntry o2) {
					String one = "";
					String two = "";
					if (correspondingDepictionEntry.getMasterImageID()==o1.getImageID()) {
						one="aaaaaaaaaaaaaaaaaa"+o1.getTitle();
					}
					else {
						one=o1.getTitle();
					}
					if (correspondingDepictionEntry.getMasterImageID()==o2.getImageID()) {
						two="aaaaaaaaaaaaaaaaaa"+o2.getTitle();
					}
					else {
						two=o2.getTitle();
					}
					return one.compareTo(two);
				}
			};
			imageEntryLS.addSortInfo(new StoreSortInfo(defaultItemComparator,SortDir.DESC));
			imageEntryLS.applySort(true);
			HashMap<Integer, Integer> sortInfo = new HashMap<Integer, Integer>();
			Integer sortPos = 0;
			for (ImageEntry ie: imageEntryLS.getAll()) {
				sortInfo.put(ie.getImageID(), sortPos);
				sortPos += 1;
			}
			correspondingDepictionEntry.setImageSortInfo(sortInfo);
			
		}
		Comparator<? super ImageEntry> customItemComparator = new Comparator<ImageEntry>() {
			@Override
			public int compare(ImageEntry o1, ImageEntry o2) {
				if (correspondingDepictionEntry.getImageSortInfo().get(o1.getImageID()) == null) {
					correspondingDepictionEntry.getImageSortInfo().put(o1.getImageID(), maxUsingIteration(correspondingDepictionEntry.getImageSortInfo())+1); 
				}
				if (correspondingDepictionEntry.getImageSortInfo().get(o2.getImageID()) == null) {
					correspondingDepictionEntry.getImageSortInfo().put(o2.getImageID(), maxUsingIteration(correspondingDepictionEntry.getImageSortInfo())+1); 
				}
				return correspondingDepictionEntry.getImageSortInfo().get(o1.getImageID()).compareTo(correspondingDepictionEntry.getImageSortInfo().get(o2.getImageID()));
			}
		};
		imageEntryLS.addSortInfo(new StoreSortInfo<ImageEntry>(customItemComparator,SortDir.ASC));
		imageEntryLS.applySort(true);

		//imageViewLF.setSize("300px", "1.0");

		/**
		 * --------------------- content of first tab (BASICS) starts here
		 * --------------------------------
		 */

		FramedPanel shortNameFP = new FramedPanel();
		shortNameFP.setHeading("Short Name");
		shortNameTF = new TextField();
		shortNameTF.setEmptyText("optional short name");
		shortNameTF.setValue(correspondingDepictionEntry.getShortName());
		shortNameTF.addValidator(new MaxLengthValidator(128));
		shortNameTF.addValidator(new Validator<String>() {

			@Override
			public List<EditorError> validate(Editor<String> editor, String value) {
				List<EditorError> l = new ArrayList<EditorError>();
				if ((caveSelectionCB.getCurrentValue() == null)
						&& ((shortNameTF.getCurrentValue() == null) || (shortNameTF.getCurrentValue().isEmpty()))) {
					l.add(new DefaultEditorError(editor, "please select either Cave or enter short name", value));
				}
				return l;
			}
		});
		shortNameTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setShortName(event.getValue());
			}
		});

		SimpleComboBox<String> accessRightsCB = new SimpleComboBox<String>(new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		});
		accessRightsCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(0));
		accessRightsCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(1));
		accessRightsCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(2));
		accessRightsCB.setEditable(false);
		accessRightsCB.setTypeAhead(false);
		accessRightsCB.setTriggerAction(TriggerAction.ALL);
		accessRightsCB.setToolTip(Util.createToolTip(
				"The acccess rights for the painted representation will influence which fields are visible.",
				"There are no restrictions at the moment but this might be implemented in the future."));
		accessRightsCB.setValue(AbstractEntry.ACCESS_LEVEL_LABEL.get(correspondingDepictionEntry.getAccessLevel()));
		accessRightsCB.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setAccessLevel(accessRightsCB.getSelectedIndex());
			}
		});
		VerticalLayoutContainer shortNameVLC = new VerticalLayoutContainer();
		shortNameVLC.add(shortNameTF, new VerticalLayoutData(1.0, .5));
		shortNameVLC.add(new FieldLabel(accessRightsCB, "Access Level"), new VerticalLayoutData(1.0, .5));
		shortNameFP.add(shortNameVLC);

		FramedPanel caveSelectionFP = new FramedPanel();
		caveSelectionFP.setHeading("Located in Cave");
		caveSelectionCB = new ComboBox<CaveEntry>(caveEntryLS, new LabelProvider<CaveEntry>() {

			@Override
			public String getLabel(CaveEntry item) {
				if (item.getCaveID() == -1) {
					return "unknown";
				}
				StaticTables st = StaticTables.getInstance();
				String site = item.getSiteID() > 0 ? st.getSiteEntries().get(item.getSiteID()).getShortName() : "";
				String district = item.getDistrictID() > 0 ? st.getDistrictEntries().get(item.getDistrictID()).getName()
						: "";
				String region = item.getRegionID() > 0 ? st.getRegionEntries().get(item.getRegionID()).getEnglishName()
						: "";
				return site + " " + item.getOfficialNumber() + (!district.isEmpty() ? " / " + district : "")
						+ (!region.isEmpty() ? " / " + region : "");

			}
		}, new AbstractSafeHtmlRenderer<CaveEntry>() {

			@Override
			public SafeHtml render(CaveEntry item) {
				final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
				if (item.getCaveID() == -1) {
					return cvTemplates.caveLabel("unknown");
				}
				StaticTables st = StaticTables.getInstance();
				String site = item.getSiteID() > 0 ? st.getSiteEntries().get(item.getSiteID()).getShortName() : "";
				String district = item.getDistrictID() > 0 ? st.getDistrictEntries().get(item.getDistrictID()).getName()
						: "";
				String region = item.getRegionID() > 0 ? st.getRegionEntries().get(item.getRegionID()).getEnglishName()
						: "";
				if (!region.isEmpty() && (item.getHistoricName() != null) && (item.getHistoricName().isEmpty())) {
					return cvTemplates.caveLabel(site, item.getOfficialNumber(), district, region,
							item.getHistoricName());
				} else if (!region.isEmpty()) {
					return cvTemplates.caveLabel(site, item.getOfficialNumber(), district, region);
				} else if ((item.getHistoricName() != null) && (item.getHistoricName().isEmpty())) {
					return cvTemplates.caveLabel(site, item.getOfficialNumber(), district, item.getHistoricName());
				} else {
					return cvTemplates.caveLabel(site, item.getOfficialNumber(), district);
				}
			}
		});
		caveSelectionCB.setTypeAhead(true);
		caveSelectionCB.setEmptyText("nothing selected");
		caveSelectionCB.setTypeAhead(true);
		caveSelectionCB.setEditable(true);
		caveSelectionCB.setTriggerAction(TriggerAction.ALL);
		caveSelectionCB.addSelectionHandler(new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				correspondingDepictionEntry.setCave(event.getSelectedItem());
				wallSelectorPanel.setCave(event.getSelectedItem());	
				shortNameTF.validate();
			}
		});
		caveSelectionCB.setToolTip("This field can only be changed until a depiction is allocated to a wall");
		// TODO check if wallTypeID is set, then set caveSelectionCB.editable(false)
		ToolButton resetCaveSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetCaveSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetCaveSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				caveSelectionCB.setValue(null, true);
			}
		});
		caveSelectionFP.addTool(resetCaveSelectionTB);
		caveSelectionFP.add(caveSelectionCB);

		FramedPanel acquiredByExpeditionFP = new FramedPanel();
		acquiredByExpeditionFP.setHeading("Acquired by expedition");
		expedSelectionCB = new ComboBox<ExpeditionEntry>(expedEntryLS, expedProps.name(),
				new AbstractSafeHtmlRenderer<ExpeditionEntry>() {

					@Override
					public SafeHtml render(ExpeditionEntry item) {
						final ExpeditionViewTemplates expedTemplates = GWT.create(ExpeditionViewTemplates.class);
						DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
						return expedTemplates.expedLabel(item.getName(), item.getLeader(),
								dtf.format(item.getStartDate()), dtf.format(item.getEndDate()));
					}
				});
		expedSelectionCB.setEmptyText("nothing selected");
		expedSelectionCB.addSelectionHandler(new SelectionHandler<ExpeditionEntry>() {

			@Override
			public void onSelection(SelectionEvent<ExpeditionEntry> event) {
				correspondingDepictionEntry.setExpedition(event.getSelectedItem());
			}
		});
		expedSelectionCB.setTypeAhead(false);
		expedSelectionCB.setEditable(false);
		expedSelectionCB.setTriggerAction(TriggerAction.ALL);
		expedSelectionCB.addSelectionHandler(new SelectionHandler<ExpeditionEntry>() {

			@Override
			public void onSelection(SelectionEvent<ExpeditionEntry> event) {
				correspondingDepictionEntry.setExpedition(event.getSelectedItem());
			}
		});
		ToolButton expedSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		expedSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		expedSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				expedSelectionCB.setValue(null, true);
			}
		});
		acquiredByExpeditionFP.addTool(expedSelectionTB);
		acquiredByExpeditionFP.add(expedSelectionCB);

		FramedPanel vendorFP = new FramedPanel();
		vendorFP.setHeading("Vendor");
		vendorSelection = new ComboBox<VendorEntry>(vendorEntryLS, vendorProps.vendorName(),
				new AbstractSafeHtmlRenderer<VendorEntry>() {

					@Override
					public SafeHtml render(VendorEntry item) {
						final VendorViewTemplates vTemplates = GWT.create(VendorViewTemplates.class);
						return vTemplates.vendorName(item.getVendorName());
					}

				});

		vendorSelection.setEmptyText("nothing selected");
		vendorSelection.setTypeAhead(false);
		vendorSelection.setEditable(false);
		vendorSelection.setTriggerAction(TriggerAction.ALL);
		vendorSelection.addSelectionHandler(new SelectionHandler<VendorEntry>() {

			@Override
			public void onSelection(SelectionEvent<VendorEntry> event) {
				correspondingDepictionEntry.setVendor(event.getSelectedItem());
			}
		});
		ToolButton resetVendorSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetVendorSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetVendorSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				vendorSelection.setValue(null, true);
			}
		});
		// adding new vendors is necessary
		ToolButton newVendorPlusTool = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		newVendorPlusTool.setToolTip(Util.createToolTip("add Vendor"));
		newVendorPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addVendorDialog = new PopupPanel();
				FramedPanel newVendorFP = new FramedPanel();
				newVendorFP.setHeading("Add Vendor");
				TextField vendorNameField = new TextField();
				vendorNameField.addValidator(new MinLengthValidator(2));
				vendorNameField.addValidator(new MaxLengthValidator(32));
				vendorNameField.setValue("");
				vendorNameField.setWidth(200);
				newVendorFP.add(vendorNameField);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (vendorNameField.isValid()) {
							VendorEntry vEntry = new VendorEntry();
							vEntry.setVendorName(vendorNameField.getCurrentValue());
							dbService.insertVendorEntry(vEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									vEntry.setVendorID(result);
									vendorEntryLS.add(vEntry);
								}
							});
							addVendorDialog.hide();
						}
					}
				});
				newVendorFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addVendorDialog.hide();
					}
				});
				newVendorFP.addButton(cancelButton);
				addVendorDialog.add(newVendorFP);
				addVendorDialog.setModal(true);
				addVendorDialog.center();
			}
		});
		vendorFP.addTool(newVendorPlusTool);
		vendorFP.addTool(resetVendorSelectionTB);
		vendorFP.add(vendorSelection);

		FramedPanel datePurchasedFP = new FramedPanel();
		datePurchasedFP.setHeading("Date purchased");
		purchaseDateField = new DateField(new DateTimePropertyEditor("yyyy"));
		purchaseDateField.setValue(correspondingDepictionEntry.getPurchaseDate());
		purchaseDateField.setEmptyText("nothing selected");
		// TODO add change handler
		datePurchasedFP.add(purchaseDateField);

		FramedPanel currentLocationFP = new FramedPanel();
		currentLocationFP.setHeading("Current Location");
		locationSelectionCB = new ComboBox<LocationEntry>(locationEntryLS, locationProps.name(),
				new AbstractSafeHtmlRenderer<LocationEntry>() {

					@Override
					public SafeHtml render(LocationEntry item) {
						final LocationViewTemplates lvTemplates = GWT.create(LocationViewTemplates.class);
						if ((item.getCounty() != null) && (!item.getCounty().isEmpty())) {
							if ((item.getTown() != null) && (!item.getTown().isEmpty())) {
								return lvTemplates.caveLabel(item.getName(),
										item.getRegion() != null && !item.getRegion().isEmpty()
												? item.getTown() + " (" + item.getRegion() + ")"
												: item.getTown(),
										item.getCounty());
							} else if ((item.getRegion() != null) && (!item.getRegion().isEmpty())) {
								return lvTemplates.caveLabel(item.getName(),
										item.getTown() != null && !item.getTown().isEmpty()
												? item.getTown() + " (" + item.getRegion() + ")"
												: item.getRegion(),
										item.getCounty());
							} else {
								return lvTemplates.caveLabel(item.getName(), item.getCounty());
							}
						} else {
							return lvTemplates.caveLabel(item.getName());
						}
					}
				});
		locationSelectionCB.setEmptyText("select current location");
		locationSelectionCB.setTypeAhead(false);
		locationSelectionCB.setEditable(true);

		locationSelectionCB.setTriggerAction(TriggerAction.ALL);
		locationSelectionCB.addValueChangeHandler(new ValueChangeHandler<LocationEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<LocationEntry> event) {
				correspondingDepictionEntry.setLocation(event.getValue());
			}
		});
		ToolButton resetLocationSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetLocationSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetLocationSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				locationSelectionCB.setValue(null, true);
			}
		});
		// adding new locations
		ToolButton newLocationPlusTool = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		newLocationPlusTool.setToolTip(Util.createToolTip("add Location"));
		newLocationPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addLocationDialog = new PopupPanel();
				FramedPanel newLocationFP = new FramedPanel();
				newLocationFP.setHeading("Add Location");
				VerticalLayoutContainer locationVLC = new VerticalLayoutContainer();
				TextField locationNameField = new TextField();
				locationNameField.addValidator(new MinLengthValidator(2));
				locationNameField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationNameField, "Name"));
				TextField locationTownField = new TextField();
				locationTownField.addValidator(new MinLengthValidator(2));
				locationTownField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationTownField, "Town"));
				TextField locationRegionField = new TextField();
				locationRegionField.addValidator(new MinLengthValidator(2));
				locationRegionField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationRegionField, "Region"));
				TextField locationCountryField = new TextField();
				locationCountryField.addValidator(new MinLengthValidator(2));
				locationCountryField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationCountryField, "Country"));
				TextField locationUrlField = new TextField();
				locationUrlField.addValidator(new MinLengthValidator(2));
				locationUrlField.addValidator(new MaxLengthValidator(256));
				locationVLC.add(new FieldLabel(locationUrlField, "URL"));
				newLocationFP.add(locationVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (locationNameField.isValid()) {
							LocationEntry lEntry = new LocationEntry();
							lEntry.setName(locationNameField.getCurrentValue());
							lEntry.setTown(locationTownField.getCurrentValue());
							lEntry.setRegion(locationRegionField.getCurrentValue());
							lEntry.setCounty(locationCountryField.getCurrentValue());
							lEntry.setUrl(locationUrlField.getCurrentValue());
							dbService.insertLocationEntry(lEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									lEntry.setLocationID(result);
									locationEntryLS.add(lEntry);
								}
							});
							addLocationDialog.hide();
						}
					}
				});
				newLocationFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addLocationDialog.hide();
					}
				});
				newLocationFP.addButton(cancelButton);
				addLocationDialog.add(newLocationFP);
				addLocationDialog.setModal(true);
				addLocationDialog.center();
			}
		});
		currentLocationFP.addTool(newLocationPlusTool);
		currentLocationFP.addTool(resetLocationSelectionTB);
		currentLocationFP.add(locationSelectionCB);

		FramedPanel inventoryNumberFP = new FramedPanel();
		inventoryNumberFP.setHeading("Inventory Number");
		TextField inventoryNumberTF = new TextField();
		inventoryNumberTF.addValidator(new MaxLengthValidator(128));
		inventoryNumberTF.setValue(correspondingDepictionEntry.getInventoryNumber());
		inventoryNumberTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setInventoryNumber(event.getValue());
			}
		});
		inventoryNumberFP.add(inventoryNumberTF);

		FramedPanel stateOfPreservationFP = new FramedPanel();
		stateOfPreservationFP.setHeading("State of Preservation");
		ToolButton addPreservationAttributeTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addPreservationAttributeTB.setToolTip(Util.createToolTip("Add Preservation Attribute"));
		stateOfPreservationFP.addTool(addPreservationAttributeTB);
		addPreservationAttributeTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addPreservationAttributeDialog = new PopupPanel();
				FramedPanel newPreservationAttributeFP = new FramedPanel();
				newPreservationAttributeFP.setHeading("Add Preservation Attribute");
				TextField preservationAttributeNameField = new TextField();
				preservationAttributeNameField.addValidator(new MinLengthValidator(2));
				preservationAttributeNameField.addValidator(new MaxLengthValidator(32));
				preservationAttributeNameField.setValue("");
				preservationAttributeNameField.setWidth(200);
				newPreservationAttributeFP.add(preservationAttributeNameField);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (preservationAttributeNameField.isValid()) {
							PreservationAttributeEntry paEntry = new PreservationAttributeEntry();
							paEntry.setName(preservationAttributeNameField.getCurrentValue());
							dbService.insertPreservationAttributeEntry(paEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									newPreservationAttributeFP.hide();
								}

								@Override
								public void onSuccess(Integer result) {
									paEntry.setPreservationAttributeID(result);
									preservationAttributesLS.add(paEntry);
									addPreservationAttributeDialog.hide();
								}
							});
						}
					}
				});
				newPreservationAttributeFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addPreservationAttributeDialog.hide();
					}
				});
				newPreservationAttributeFP.addButton(cancelButton);
				addPreservationAttributeDialog.add(newPreservationAttributeFP);
				addPreservationAttributeDialog.setModal(true);
				addPreservationAttributeDialog.center();
			}
		});
		ListView<PreservationAttributeEntry, String> preservationAttributesListView = new ListView<PreservationAttributeEntry, String>(
				preservationAttributesLS, presAttributeProps.name());
		preservationAttributesListView.setToolTip("to select attributes drag right");
		ListView<PreservationAttributeEntry, String> selectedPreservationAttributesListView = new ListView<PreservationAttributeEntry, String>(
				selectedPreservationAttributesLS, presAttributeProps.name());
		selectedPreservationAttributesListView.setToolTip("to deselect attributes drag left");

		new ListViewDragSource<PreservationAttributeEntry>(preservationAttributesListView).setGroup("paGroup");
		new ListViewDragSource<PreservationAttributeEntry>(selectedPreservationAttributesListView).setGroup("paGroup");

		new ListViewDropTarget<PreservationAttributeEntry>(preservationAttributesListView).setGroup("paGroup");
		new ListViewDropTarget<PreservationAttributeEntry>(selectedPreservationAttributesListView).setGroup("paGroup");

		BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
		borderLayoutContainer.setWestWidget(preservationAttributesListView, new BorderLayoutData(0.5));
		borderLayoutContainer.setCenterWidget(selectedPreservationAttributesListView, new BorderLayoutData(0.5));

		stateOfPreservationFP.add(borderLayoutContainer);

		VerticalLayoutContainer basicsLeftVLC = new VerticalLayoutContainer();

		basicsLeftVLC.add(shortNameFP, new VerticalLayoutData(1.0, .15));
		basicsLeftVLC.add(caveSelectionFP, new VerticalLayoutData(1.0, .11));
		basicsLeftVLC.add(acquiredByExpeditionFP, new VerticalLayoutData(1.0, .11));
		basicsLeftVLC.add(vendorFP, new VerticalLayoutData(1.0, .11));
		basicsLeftVLC.add(datePurchasedFP, new VerticalLayoutData(1.0, .11));
		basicsLeftVLC.add(currentLocationFP, new VerticalLayoutData(1.0, .11));
		basicsLeftVLC.add(inventoryNumberFP, new VerticalLayoutData(1.0, .10));
//		basicsLeftVLC.add(positionNoteFP, new VerticalLayoutData(1.0, .15));
		basicsLeftVLC.add(stateOfPreservationFP, new VerticalLayoutData(1.0, .2));

		FramedPanel wallSelectorFP = new FramedPanel();
		wallSelectorFP.setHeading("Wall");
		ToolButton wallEditorTB = new ToolButton(ToolButton.PIN);
		wallEditorTB.setEnabled(false);
		wallEditorTB.setToolTip(Util.createToolTip("set position on wall"));

//		TextButton wallEditorButton = new TextButton("set position on wall");
		wallEditor = new Walls(1, true);
		wallEditorTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				wallEditorDialog = new PopupPanel();
				wallEditorDialog.add(wallEditor);
				wallEditor.createNewDepictionOnWall(correspondingDepictionEntry, true, true);
				wallEditor.setPanel(wallEditorDialog);
				wallEditorDialog.setModal(true);
				wallEditorDialog.center();
			}
		});

		wallTree = new WallTree(StaticTables.getInstance().getWallTreeEntries().values(),
				correspondingDepictionEntry.getWalls(), true, false, null);// correspondingDepictionEntry.getCave());

		FramedPanel wallTreeFP = new FramedPanel();
		// wallTree.setWall(correspondingDepictionEntry.getWalls());
		wallTreeFP.add(wallTree.wallTree);
		ToolButton newPositionPlusTool = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		newPositionPlusTool.setToolTip(Util.createToolTip("edit wall Position"));
		newPositionPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PositionEditor pe = new PositionEditor(correspondingDepictionEntry.getCave(),
						correspondingDepictionEntry.getWalls(), false) {
					@Override
					protected void save(ArrayList<WallTreeEntry> results) {
						getListenerList().get(0).addClickNumber();
						correspondingDepictionEntry.setWalls(getSelectedWalls());
						wallTree.setWall(getSelectedWalls());

					}
				};
				Util.doLogging("Wall Position showing");
				pe.show();
				// PopupPanel positionEditorPopUp = new PopupPanel();
				// positionEditorPopUp.add(pe);
				// positionEditorPopUp.setModal(true);
				// positionEditorPopUp.center();

			}
		});
		wallTreeFP.addTool(newPositionPlusTool);
		/**
		 * the wall visualisation will be implemented at a later time
		 */
		// wallSelectorFP.addTool(wallEditorTB);
		wallSelectorPanel = new WallSelector(new SelectionHandler<WallEntry>() {

			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				correspondingDepictionEntry.setWallID(event.getSelectedItem().getWallLocationID());
			}
		}, false);
		wallSelectorFP.add(wallSelectorPanel);
		// wallSelectorFP.add(wallTreeFP);
		// wallSelectorFP.add(wallEditorTB);
		FramedPanel positionNoteFP = new FramedPanel();
		positionNoteFP.setHeading("Position Notes");
		TextArea positionNotesTA = new TextArea();
		positionNotesTA.setValue(correspondingDepictionEntry.getPositionNotes());
		positionNotesTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setPositionNotes(event.getValue());
			}
		});
		positionNoteFP.add(positionNotesTA);

		VerticalLayoutContainer basicsRightVLC = new VerticalLayoutContainer();
		basicsRightVLC.add(wallSelectorFP, new VerticalLayoutData(1.0, .65));
		basicsRightVLC.add(wallTreeFP, new VerticalLayoutData(1.0, .25));
		basicsRightVLC.add(positionNoteFP, new VerticalLayoutData(1.0, .10));

		HorizontalLayoutContainer basicsTabHLC = new HorizontalLayoutContainer();
		basicsTabHLC.add(basicsLeftVLC, new HorizontalLayoutData(.4, 1.0));
		basicsTabHLC.add(basicsRightVLC, new HorizontalLayoutData(.6, 1.0));

		/**
		 * --------------------- content of second tab (Descriptions) starts here
		 * --------------------------------
		 */

		HorizontalLayoutContainer dimensionHLC = new HorizontalLayoutContainer();

		FramedPanel heightFP = new FramedPanel();
		heightFP.setHeading("Height");
		heightNF = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightNF.addValidator(new MinNumberValidator<Double>((double) 0));
		heightNF.setValue(correspondingDepictionEntry.getHeight());
		heightNF.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				correspondingDepictionEntry.setHeight(event.getValue());
			}
		});
		heightFP.add(heightNF);
		dimensionHLC.add(heightFP, new HorizontalLayoutData(.5, 1.0));

		FramedPanel widthFP = new FramedPanel();
		widthFP.setHeading("Width");
		widthNF = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthNF.addValidator(new MinNumberValidator<Double>((double) 0));
		widthNF.setValue(correspondingDepictionEntry.getWidth());
		widthNF.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				correspondingDepictionEntry.setWidth(event.getValue());
			}
		});
		widthFP.add(widthNF);
		dimensionHLC.add(widthFP, new HorizontalLayoutData(.5, 1.0));

		FramedPanel styleFP = new FramedPanel();
		styleFP.setHeading("Style");
		styleSelection = new ComboBox<StyleEntry>(styleEntryLS, styleProps.styleName(),
				new AbstractSafeHtmlRenderer<StyleEntry>() {

					@Override
					public SafeHtml render(StyleEntry item) {
						final StyleViewTemplates svTemplates = GWT.create(StyleViewTemplates.class);
						return svTemplates.styleName(item.getStyleName());
					}
				});
		styleSelection.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				getListenerList().get(0).addClickNumber();
			}
		});
		styleSelection.setEmptyText("nothing selected");
		styleSelection.setTypeAhead(false);
		styleSelection.setEditable(false);
		styleSelection.setTriggerAction(TriggerAction.ALL);
		styleSelection.addSelectionHandler(new SelectionHandler<StyleEntry>() {

			@Override
			public void onSelection(SelectionEvent<StyleEntry> event) {
				correspondingDepictionEntry.setStyleID(event.getSelectedItem().getStyleID());
			}
		});
		ToolButton resetStyleSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetStyleSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetStyleSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				styleSelection.setValue(null, true);
			}
		});
		styleFP.addTool(resetStyleSelectionTB);
		styleFP.add(styleSelection);

		FramedPanel modesOfRepresentationFP = new FramedPanel();
		modesOfRepresentationFP.setHeading("Modes of Representation");
		modeOfRepresentationSelectionCB = new ComboBox<ModeOfRepresentationEntry>(modeOfRepresentationLS,
				morProps.name(), new AbstractSafeHtmlRenderer<ModeOfRepresentationEntry>() {

					@Override
					public SafeHtml render(ModeOfRepresentationEntry morEntry) {
						ModesOfRepresentationViewTemplates morTemplates = GWT
								.create(ModesOfRepresentationViewTemplates.class);
						return morTemplates.morLabel(morEntry.getName());
					}

				});
		modeOfRepresentationSelectionCB.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				getListenerList().get(0).addClickNumber();
			}
		});
		modeOfRepresentationSelectionCB.setEmptyText("nothing selected");
		modeOfRepresentationSelectionCB.setTypeAhead(false);
		modeOfRepresentationSelectionCB.setEditable(false);
		modeOfRepresentationSelectionCB.setTriggerAction(TriggerAction.ALL);
		modeOfRepresentationSelectionCB.addSelectionHandler(new SelectionHandler<ModeOfRepresentationEntry>() {

			@Override
			public void onSelection(SelectionEvent<ModeOfRepresentationEntry> event) {
				correspondingDepictionEntry
						.setModeOfRepresentationID(event.getSelectedItem().getModeOfRepresentationID());
			}
		});
		ToolButton resetModeOfRepresentationSelectionTB = new ToolButton(
				new IconConfig("resetButton", "resetButtonOver"));
		resetModeOfRepresentationSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetModeOfRepresentationSelectionTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				modeOfRepresentationSelectionCB.setValue(null, true);
			}
		});
		modesOfRepresentationFP.addTool(resetModeOfRepresentationSelectionTB);
		modesOfRepresentationFP.add(modeOfRepresentationSelectionCB);

		FramedPanel backgroundColourFP = new FramedPanel();
		backgroundColourFP.setHeading("Background colour");
		backgroundColourField = new TextField();
		backgroundColourField.setValue(correspondingDepictionEntry.getBackgroundColour());
		backgroundColourField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setBackgroundColour(event.getValue());
			}
		});
		backgroundColourFP.add(backgroundColourField);

		FramedPanel inscriptionsFP = new FramedPanel();
		inscriptionsFP.setHeading("Inscriptions");
		inscriptionsTestArea = new TextArea();
		inscriptionsTestArea.setText(correspondingDepictionEntry.getInscriptions());
		inscriptionsTestArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setInscriptions(event.getValue());
			}
		});
		inscriptionsFP.add(inscriptionsTestArea);

		FramedPanel separateAksarasFP = new FramedPanel();
		separateAksarasFP.setHeading("Separate Akṣaras");
		separateAksarasTextArea = new TextArea();
		separateAksarasTextArea.setText(correspondingDepictionEntry.getSeparateAksaras());
		separateAksarasTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setSeparateAksaras(event.getValue());
			}
		});
		separateAksarasFP.add(separateAksarasTextArea);

		FramedPanel datingFP = new FramedPanel();
		datingFP.setHeading("Dating");
		datingField = new TextField();
		// datingField.setWidth(130);
		datingField.setText(correspondingDepictionEntry.getDating());
		datingField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setDating(event.getValue());
			}
		});
		datingFP.add(datingField);

		VerticalLayoutContainer descriptionLeftVLC = new VerticalLayoutContainer();
		descriptionLeftVLC.add(dimensionHLC, new VerticalLayoutData(1.0, .1));
		descriptionLeftVLC.add(styleFP, new VerticalLayoutData(1.0, .11));
		descriptionLeftVLC.add(modesOfRepresentationFP, new VerticalLayoutData(1.0, .11));
		descriptionLeftVLC.add(backgroundColourFP, new VerticalLayoutData(1.0, .1));
		descriptionLeftVLC.add(inscriptionsFP, new VerticalLayoutData(1.0, .24));
		descriptionLeftVLC.add(separateAksarasFP, new VerticalLayoutData(1.0, .24));
		descriptionLeftVLC.add(datingFP, new VerticalLayoutData(1.0, .1));

		FramedPanel descriptionFP = new FramedPanel();
		descriptionFP.setHeading("Description");
		descriptionArea = new TextArea();
		descriptionArea.setValue(correspondingDepictionEntry.getDescription());
		descriptionArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setDescription(event.getValue());
			}
		});
		descriptionFP.add(descriptionArea);

		FramedPanel generalRemarksFP = new FramedPanel();
		generalRemarksFP.setHeading("General remarks");
		generalRemarksArea = new TextArea();
		generalRemarksArea.setValue(correspondingDepictionEntry.getGeneralRemarks());
		generalRemarksArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setGeneralRemarks(event.getValue());
			}
		});
		generalRemarksFP.add(generalRemarksArea);

		FramedPanel otherSuggestedIdentificationsFP = new FramedPanel();
		otherSuggestedIdentificationsFP.setHeading("Other suggested identifications");
		TextArea otherSuggestedIdentificationsTA = new TextArea();
		otherSuggestedIdentificationsTA.setValue(correspondingDepictionEntry.getOtherSuggestedIdentifications());
		otherSuggestedIdentificationsTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setOtherSuggestedIdentifications(event.getValue());
			}
		});
		otherSuggestedIdentificationsFP.add(otherSuggestedIdentificationsTA);

		VerticalLayoutContainer descriptionRightVLC = new VerticalLayoutContainer();
		descriptionRightVLC.add(descriptionFP, new VerticalLayoutData(1.0, 1.0 / 3));
		descriptionRightVLC.add(generalRemarksFP, new VerticalLayoutData(1.0, 1.0 / 3));
		descriptionRightVLC.add(otherSuggestedIdentificationsFP, new VerticalLayoutData(1.0, 1.0 / 3));

		HorizontalLayoutContainer descriptionTabHLC = new HorizontalLayoutContainer();
		descriptionTabHLC.add(descriptionLeftVLC, new HorizontalLayoutData(.35, 1.0));
		descriptionTabHLC.add(descriptionRightVLC, new HorizontalLayoutData(.65, 1.0));

		/**
		 * --------------------- definition of image panel on right side starts here
		 * --------------------------------
		 */
		imageSelector = new ImageSelector(new ImageSelectorListener() {

			@Override
			public void imageSelected(ArrayList<ImageEntry> imgEntryList) {
				if (imgEntryList != null) {
					for (ImageEntry imgEntry : imgEntryList) {
						if (!correspondingDepictionEntry.getRelatedImages().contains(imgEntry)) {
							correspondingDepictionEntry.addRelatedImages(imgEntry); // TODO check if double adding
																					// possible and avoid!
						}

					}
					getPics(imgEntryList, 300, UserLogin.getInstance().getSessionID());
					loadImages();
					setosd();
				}

				imageSelectionDialog.hide();
			}
		});

		ToolButton addImageTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addImageTB.setToolTip(Util.createToolTip("add image"));
		addImageTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				getListenerList().get(0).addClickNumber();
				imageSelectionDialog = new PopupPanel();
				imageSelectionDialog.add(imageSelector);
				imageSelector.resetSelection();
				imageSelectionDialog.setModal(true);
				imageSelectionDialog.center();
			}
		});

		ToolButton removeImageTB = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
		removeImageTB.setToolTip(Util.createToolTip("remove image"));
		removeImageTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				getListenerList().get(0).addClickNumber();
				imageEntryLS.remove(imageListView.getSelectionModel().getSelectedItem());
			}
		});

		ToolButton setMasterTB = new ToolButton(new IconConfig("favouriteButton", "favouriteButtonOver"));
		setMasterTB.setToolTip(Util.createToolTip("Set master image.",
				"The master image will be displayed on top of this list and used for previews in the system (e.g. thumbnails)."));
		setMasterTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				getListenerList().get(0).addClickNumber();
				ImageEntry entry = imageListView.getSelectionModel().getSelectedItem();
				correspondingDepictionEntry.setMasterImageID(entry.getImageID());
				reloadPics();
			}
		});

//		ToolButton infoTB = new ToolButton(ToolButton.QUESTION);
//		infoTB.addSelectHandler(new SelectHandler() {
//			
//			@Override
//			public void onSelect(SelectEvent event) {
//				PopupPanel dialog = new PopupPanel();
//				FramedPanel infoDialogFP = new FramedPanel();
//				infoDialogFP.setHeading("Colour schema");
//				VerticalPanel infoVP = new VerticalPanel();
//				infoVP.add(new HTML("<div><label style='font-size: 12px; color: #0073e6;'>Master Image</label></div>"));
//				infoVP.add(new HTML("<div><label style='font-size: 12px; color: #99ff66;'>Open Access Image</label></div>"));
//				infoVP.add(new HTML("<div><label style='font-size: 12px; color: #ff1a1a;'>Non Open Access Image</label></div>"));
//				infoDialogFP.add(infoVP);
//				TextButton okButton = new TextButton("OK");
//				okButton.addSelectHandler(new SelectHandler() {
//					
//					@Override
//					public void onSelect(SelectEvent event) {
//						dialog.hide();
//					}
//				});
//				infoDialogFP.addButton(okButton);
//				dialog.add(infoDialogFP);
//				dialog.setModal(true);
//				dialog.setGlassEnabled(true);
//				dialog.center();
//			}
//		});

		ToolButton showAnnotationTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		showAnnotationTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				osdLoader.removeOrAddAnnotations(correspondingDepictionEntry.getRelatedAnnotationList(),
						annotationsLoaded);
				annotationsLoaded = !annotationsLoaded;
				osdLoader.setHasContourAllign(false);
			}
		});
		showAnnotationTB.setToolTip(Util.createToolTip("Show or Hide Annotations.", "This will show or hide all Annotations."));
		ToolButton showproposedAnnotationTB = new ToolButton(new IconConfig("doubleRightButton", "doubleRightButtonOver"));
		showproposedAnnotationTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				ArrayList<AnnotationEntry> proposedAnnotations = new ArrayList<AnnotationEntry>();
				dbService.getProposedAnnotations(correspondingDepictionEntry.getRelatedImages(), correspondingDepictionEntry.getDepictionID(), new AsyncCallback<ArrayList<AnnotationEntry>>() {
					
					@Override
					public void onSuccess(ArrayList<AnnotationEntry> result) {
						if (result != null){
							for (AnnotationEntry entry : result) {
								proposedAnnotations.add(entry);
							}
							osdLoader.removeOrAddAnnotations(proposedAnnotations,
									annotationsLoaded);
							osdLoader.setHasContourAllign(true);
						}
						annotationsLoaded = !annotationsLoaded;
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});

			}
		});
		showproposedAnnotationTB.setToolTip(Util.createToolTip("Show or Hide Proposed Annotations.", "This will show or hide all artificially generated Annotations."));
		ToolButton modifiedToolButtonImage = new ToolButton(new IconConfig("foldButton", "foldButtonOver"));
		modifiedToolButtonImage.setToolTip(Util.createToolTip("show modification history"));
		modifiedToolButtonImage.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
			ColumnConfig<ModifiedEntry, String> modifiedByCol = new ColumnConfig<ModifiedEntry, String>(modifiedProps.modifiedBy(), 300, "Modified By");
			ColumnConfig<ModifiedEntry, String> modifiedOColn = new ColumnConfig<ModifiedEntry, String>(modifiedProps.modifiedOn(), 300, "Midified On");
			ColumnConfig<ModifiedEntry, String> annoIDColn = new ColumnConfig<ModifiedEntry, String>(modifiedProps.annoID(), 300, "Annotation ID");
			ColumnConfig<ModifiedEntry, String> tagsColn = new ColumnConfig<ModifiedEntry, String>(modifiedProps.tags(), 300, "Tags");
			
//				yearColumn.setHideable(false);
//				yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
			
		    List<ColumnConfig<ModifiedEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<ModifiedEntry, ?>>();
//		    sourceColumns.add(selectionModel.getColumn());
		    sourceColumns.add(modifiedByCol);
		    sourceColumns.add(modifiedOColn);
		    sourceColumns.add(annoIDColn);
		    sourceColumns.add(tagsColn);

		    ColumnModel<ModifiedEntry> sourceColumnModel = new ColumnModel<ModifiedEntry>(sourceColumns);
		    
		    sourceStore = new ListStore<ModifiedEntry>(modifiedProps.key());

			sourceStore.clear();
		    dbService.getModifiedAnnoEntry(correspondingDepictionEntry.getDepictionID(), false, new AsyncCallback<ArrayList<ModifiedEntry>>() {
				
					@Override
					public void onSuccess(ArrayList<ModifiedEntry> result) {
						for (ModifiedEntry entry : result) {
							sourceStore.add(entry);
						}
					}
					
					@Override
					public void onFailure(Throwable caught) {
					}
				});


		    Grid<ModifiedEntry> grid = new Grid<ModifiedEntry>(sourceStore, sourceColumnModel);
//			    grid.setSelectionModel(selectionModel);
//			    grid.setColumnReordering(true);
		    grid.setBorders(false);
		    grid.getView().setStripeRows(true);
		    grid.getView().setColumnLines(true);
		    grid.getView().setForceFit(true);

			PopupPanel modifiedPopUp = new PopupPanel();
			FramedPanel modifiedFP = new FramedPanel();
			modifiedFP.setHeading("Modification Protocoll");
			modifiedFP.setHeight(500);
			modifiedFP.add(grid);
			modifiedPopUp.add(modifiedFP);
			ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
			closeToolButton.setToolTip(Util.createToolTip("close"));
			closeToolButton.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					modifiedPopUp.hide();
				}
			});
			modifiedFP.addTool(closeToolButton);
			modifiedPopUp.setModal(true);
			modifiedPopUp.center();

			}
		});
		showAnnotationTB.setToolTip(Util.createToolTip("Show or Hide Annotations.", "This will show or hide all Annotations."));
		ToolButton sortUpToolButtonImage = new ToolButton(new IconConfig("collapseWindowButton", "collapseWindowButtonOver"));
		sortUpToolButtonImage.setToolTip(Util.createToolTip("SortSelected Picture Up"));
		sortUpToolButtonImage.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				ImageEntry entry = imageListView.getSelectionModel().getSelectedItem();
				Integer sortNumber = correspondingDepictionEntry.getImageSortInfo().get(entry.getImageID());
				if (sortNumber > 0) {
					for (Integer key: correspondingDepictionEntry.getImageSortInfo().keySet()) {
						if (correspondingDepictionEntry.getImageSortInfo().get(key) == sortNumber-1) {
							correspondingDepictionEntry.getImageSortInfo().replace(key, sortNumber);
							break;
						}
					}
					correspondingDepictionEntry.getImageSortInfo().replace(entry.getImageID(), sortNumber-1);
					imageEntryLS.applySort(false);
					//					reloadPics();					
//					imageListView.getSelectionModel().select(entry, false);
				} else {
					Info.display("Sorting Up Failed!", "Element is already on highest position.");
				}
			}
		});
		ToolButton sortDownToolButtonImage = new ToolButton(new IconConfig("expandWindowButton", "expandWindowButtonOver"));
		sortDownToolButtonImage.setToolTip(Util.createToolTip("SortSelected Picture Down"));
		sortDownToolButtonImage.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				ImageEntry entry = imageListView.getSelectionModel().getSelectedItem();
				Integer sortNumber = correspondingDepictionEntry.getImageSortInfo().get(entry.getImageID());
				if (sortNumber < correspondingDepictionEntry.getImageSortInfo().size()) {
					for (Integer key: correspondingDepictionEntry.getImageSortInfo().keySet()) {
						if (correspondingDepictionEntry.getImageSortInfo().get(key) == sortNumber+1) {
							correspondingDepictionEntry.getImageSortInfo().replace(key, sortNumber);
							break;
						}
					}
					correspondingDepictionEntry.getImageSortInfo().replace(entry.getImageID(), sortNumber+1);
					imageEntryLS.applySort(false);
					reloadPics();
					imageListView.getSelectionModel().select(entry, false);
					
				} else {
					Info.display("Sorting Up Failed!", "Element is already on lowest position.");
				}
			}
		});

		depictionImagesPanel = new FramedPanel();
		depictionImagesPanel.setHeading("Images");
		VerticalLayoutContainer filtercontainer = new VerticalLayoutContainer();
		filtercontainer.add(filterField, new VerticalLayoutData(1.0, .05));
		filtercontainer.add(imageViewLF, new VerticalLayoutData(1.0, .95));
		depictionImagesPanel.addTool(sortUpToolButtonImage);
		depictionImagesPanel.addTool(sortDownToolButtonImage);
		depictionImagesPanel.add(filtercontainer);
//		depictionImagesPanel.addTool(infoTB);
		depictionImagesPanel.addTool(modifiedToolButtonImage);
		depictionImagesPanel.addTool(addImageTB);
		depictionImagesPanel.addTool(removeImageTB);
		depictionImagesPanel.addTool(setMasterTB);
		depictionImagesPanel.addTool(showAnnotationTB);
		depictionImagesPanel.addTool(showproposedAnnotationTB);

		/**
		 * ---------------------- content of third tab (Iconography & Pictorial
		 * Elements) starts here ---------------------
		 */
		Collection<IconographyEntry> elements = StaticTables.getInstance().getIconographyEntries().values();
		ArrayList<EditorListener> el = getListenerList();

		icoSelectorListener = new IconographySelectorListener() {

			@Override
			public void icoHighlighter(int icoID) {
				//Util.doLogging("triggered icoHighlighter");
				List<IconographyEntry> selectedIcos = iconographySelector.getCLickedItems();
				IconographyEntry selectedIE = iconographySelector.getIconographyStore()
						.findModelWithKey(Integer.toString(icoID));
				highlightIcoEntry(selectedIE, false,selectedIcos);
			};

			@Override
			public void icoDeHighlighter(int icoID) {
				List<IconographyEntry> selectedIcos = iconographySelector.getCLickedItems();
				IconographyEntry selectedIE = iconographySelector.getIconographyStore()
				.findModelWithKey(Integer.toString(icoID));
				if (annotationsLoaded) {
					osdLoader.removeAllAnnotations();					
				}
				else {
					highlightIcoEntry(selectedIE, true,selectedIcos);										
				}
				for (IconographyEntry clickedIE : iconographySelector.getCLickedItems()) {
					highlightIcoEntry(clickedIE, false,selectedIcos);					
				}

			}
			public void reloadIconography(IconographyEntry iconographyEntry) {
				iconographySelector = null;
				iconographySelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values(),
						getListenerList().get(0), true, correspondingDepictionEntry.getRelatedAnnotationList(),
						icoSelectorListener);
			}
			public void reloadOSD() {
				reloadPics();
			}

			@Override
			public void reduceTree() {
				imageWindowRelation= 0.8;
				mainHLC.clear();
				Object test = depictionImagesPanel.getLayoutData();
				mainHLC.add(tabPanel, new HorizontalLayoutData(0.2, 1));
				mainHLC.add(depictionImagesPanel, new HorizontalLayoutData(0.8, 1));
				mainHLC.forceLayout();

				reloadPics();			
			}
			
		};
		iconographySelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values(),
				getListenerList().get(0), true, correspondingDepictionEntry.getRelatedAnnotationList(),
				icoSelectorListener);
		long start = System.currentTimeMillis();
		Util.doLogging("Starte getIconogrpahy for Depictionentry: "
				+ Integer.toString(correspondingDepictionEntry.getDepictionID()));
		loadiconogrpahy(correspondingDepictionEntry.getRelatedIconographyList());
		osdListener = new OSDListener() {

			@Override
			public void setAnnotationsInParent(ArrayList<AnnotationEntry> relatedAnnotationList) {
				correspondingDepictionEntry.setRelatedAnnotationList(relatedAnnotationList);
				iconographySelector.setRelatedAnnotationList(relatedAnnotationList);
				
			};

			@Override
			public int getDepictionID() {
				return correspondingDepictionEntry.getDepictionID();
			}
			@Override
			public boolean isOrnament() {
				return false;
			}

			@Override
			public ArrayList<AnnotationEntry> getAnnotations() {
				// TODO Auto-generated method stub
				return correspondingDepictionEntry.getRelatedAnnotationList();
			}

			@Override
			public void addAnnotation(AnnotationEntry ae) {
				correspondingDepictionEntry.addAnnotation(ae);
				for (EditorListener el : getListenerList()) {
					if (el instanceof DepictionView) {
						((DepictionView) el).setDepictionEntry(correspondingDepictionEntry);
					}
				}

				
			};
			

		};
		osdLoader = new OSDLoader(correspondingDepictionEntry.getRelatedImages(), true,
				iconographySelector.getIconographyStore(),
				osdListener);
		/**
		 * ---------------------- content of fourth tab (Bibliography Selector)
		 * ---------------------
		 */
		bibliographySelector = new BibliographySelector(correspondingDepictionEntry.getRelatedBibliographyList(),
				getListenerList().get(0));
//		if (correspondingDepictionEntry.getRelatedBibliographyList().size() > 0) {
//			bibliographySelector.setSelectedEntries(correspondingDepictionEntry.getRelatedBibliographyList());
//		}

		/**
		 * --------------------------- next the editor as a whole will be assembled
		 * -------------------
		 */
		tabPanel = new TabPanel();
		tabPanel.setTabScroll(false);
		ScrollPanel scrpanel1 = new ScrollPanel();
		ScrollPanel scrpanel2 = new ScrollPanel();
		ScrollPanel scrpanel4 = new ScrollPanel();

		scrpanel1.add(basicsTabHLC);
		scrpanel2.add(descriptionTabHLC);
		scrpanel4.add(bibliographySelector);
		tabPanel.add(scrpanel1, "Basics");
		tabPanel.add(scrpanel2, "Description");
		tabPanel.add(iconographySelector, "Iconography & Pictorial Elements");
		tabPanel.add(scrpanel4, "Bibliography Selector");
		// Resizable tabResize = new Resizable(tabPanel);
		imgResize = new Resizable(depictionImagesPanel);
		imgResize.addResizeEndHandler(new ResizeEndHandler() {
			public void onResizeEnd(ResizeEndEvent event) {
				imageWindowRelation = (double) depictionImagesPanel.getOffsetWidth(true)
						/ (double) mainPanel.getOffsetWidth(true);
				reloadPics();
//				Util.doLogging(
//						"Width depictionImagesPanel: " + Integer.toString(depictionImagesPanel.getOffsetWidth()));
//				Util.doLogging("Width mainPanel: " + Integer.toString(mainPanel.getOffsetWidth()));
//
			}
		});

		depictionImagesPanel.addResizeHandler(new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {
				// TODO Auto-generated method stub

				tabPanel.setWidth((int) (((double) mainPanel.getOffsetWidth()) - event.getWidth() - 10));
				imageWindowRelation = (double) depictionImagesPanel.getOffsetWidth(true)
						/ (double) mainPanel.getOffsetWidth(true);

			}

		});
		
		mainHLC = new HorizontalLayoutContainer();
		imageWindowRelation = 0.35;
		tabLayout = new HorizontalLayoutData(1 - imageWindowRelation, 1.0);
		imageLayout =  new HorizontalLayoutData(imageWindowRelation, 1.0);
		mainHLC.add(tabPanel, tabLayout);
		mainHLC.add(depictionImagesPanel, imageLayout);
		
		saveToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveToolButton.setToolTip(Util.createToolTip("save"));
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveToolButton.disable();
				Util.doLogging("Depiction Save triggert by Savie-Button");
				save(false, 0);
			}
		});
		ToolButton deleteToolButton = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
		deleteToolButton.setToolTip(Util.createToolTip("delete"));
		deleteToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				de.cses.client.Util.showYesNo("Delete Warning!",
						"Proceeding will remove this Entry from the Database, are you sure?", new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								iconographySelector.imgPopHide();
								deleteEntry(correspondingDepictionEntry);
								closeEditor(null);
							}
						}, new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								iconographySelector.imgPopHide();

							}
						}, new KeyDownHandler() {

							@Override
							public void onKeyDown(KeyDownEvent e) {
								iconographySelector.imgPopHide();

							}
						}

				);
			}
		});

		ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeToolButton.setToolTip(Util.createToolTip("close editor"));
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (saveToolButton.isEnabled()) {
					de.cses.client.Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?",
							new SelectHandler() {
	
								@Override
								public void onSelect(SelectEvent event) {
									iconographySelector.imgPopHide();
									Util.doLogging("Depiction Save triggert by Close-Button");
									save(true, 0);
									closeEditor(null);
								}
							}, new SelectHandler() {
	
								@Override
								public void onSelect(SelectEvent event) {
									iconographySelector.imgPopHide();
									bibliographySelector.clearPages();
									closeEditor(null);
								}
							}, new KeyDownHandler() {
	
								@Override
								public void onKeyDown(KeyDownEvent e) {
									iconographySelector.imgPopHide();
									if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
										closeEditor(null);
									}
								}
	
							});
				} else {
					Info.display("Warning", "Wait for Saving-Process to finish");
				}
			}
		});

		mainPanel = new FramedPanel();
		mainPanel.addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent e) {
				if (saveToolButton.isEnabled()) {
					if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
						de.cses.client.Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?",
						new SelectHandler() {
	
							@Override
							public void onSelect(SelectEvent event) {
								saveToolButton.disable();
								Util.doLogging("Depiction Save triggert by Key-Combination");
								save(true, 0);
								closeEditor(null);
							}
						}, new SelectHandler() {
	
							@Override
							public void onSelect(SelectEvent event) {
								bibliographySelector.clearPages();
								closeEditor(null);
							}
						}, new KeyDownHandler() {
	
							@Override
							public void onKeyDown(KeyDownEvent e) {
								if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
									closeEditor(null);
								}
							}
	
						});
					}
					
				} else {
					Info.display("Hotkey-Save canceled", "Saving already in process");
				}
			}
		}, KeyDownEvent.getType());
		mainPanel.setHeading("Painted Representation Editor (entry " + Integer.toString(correspondingDepictionEntry.getDepictionID())+")");

		mainPanel.add(mainHLC);
		mainPanel.setSize(Integer.toString(Window.getClientWidth() / 100 * 95),
				Integer.toString(Window.getClientHeight() / 100 * 95));
		createNextPrevButtons();
		mainPanel.addTool(modifiedToolButton);
		mainPanel.addTool(prevToolButton);
		mainPanel.addTool(nextToolButton);
		mainPanel.addTool(deleteToolButton);
		mainPanel.addTool(saveToolButton);
		mainPanel.addTool(closeToolButton);
		mainPanel.addBeforeShowHandler(new BeforeShowHandler() {

			@Override
			public void onBeforeShow(BeforeShowEvent event) {
				Util.doLogging("showing");
				bibliographySelector.setwidth(tabPanel.getOffsetWidth(),tabPanel.getOffsetHeight());
				
			}
			
		});
		Resizable rs = new Resizable(mainPanel);
		rs.addResizeEndHandler(new ResizeEndHandler() {
			public void onResizeEnd(ResizeEndEvent event) {
				bibliographySelector.setwidth(tabPanel.getOffsetWidth(),tabPanel.getOffsetHeight());

			}
		});
		bibliographySelector.setwidth(tabPanel.getOffsetWidth(),tabPanel.getOffsetHeight());
		new Draggable(mainPanel, mainPanel.getHeader(), GWT.<DraggableAppearance>create(DraggableAppearance.class));

	}

	private void setosd() {
		dbService.getOSDContext(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {
				osdLoader.startLoadingTiles(result);
			}
		});
	}
	@Override
	protected void loadModifiedEntries() {
		sourceStore.clear();
	    dbService.getModifiedAbstractEntry((AbstractEntry)correspondingDepictionEntry, new AsyncCallback<ArrayList<ModifiedEntry>>() {
			
				@Override
				public void onSuccess(ArrayList<ModifiedEntry> result) {
					for (ModifiedEntry entry : result) {
						sourceStore.add(entry);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
				}
			});
	 
	}

	public void setfocus() {
		Util.doLogging("focusing" + Integer.toString(mainPanel.getOffsetHeight()));
		bibliographySelector.setwidth(tabPanel.getOffsetWidth(),mainPanel.getOffsetHeight());
		setosd();

	}

	/**
	 * Called when the save button is pressed. Calls
	 * <code>DepictionEditorListener.depictionSaved(correspondingDepictionEntry)<code>
	 * 
	 * @param close
	 */
	protected void save(boolean close, int slide) {
		System.out.println("depictionSave triggered");
//		ArrayList<IconographyEntry> icolist = new ArrayList<IconographyEntry>();
//		icolist.add(iconographySelector.getIconographyStroe().findModelWithKey("2152"));
////		icolist.add(iconographySelector.getIconographyStroe().findModelWithKey("2247"));
//		String poly = "POLYGON((1403.597412109375,3109.902099609375 1407.431884765625,3305.460205078125 1806.21728515625,3282.453369140625 1994.106689453125,3267.115478515625 2289.361328125,3274.784423828125 2841.52587890625,3267.115478515625 2891.3740234375,3267.115478515625 2876.0361328125,2883.66796875 2899.04296875,2787.80615234375 2952.7255859375,2730.2890625 2964.22900390625,2638.261474609375 2918.21533203125,2557.737548828125 2879.87060546875,2534.730712890625 2837.69140625,2576.909912109375 2745.663818359375,2596.082275390625 2695.815673828125,2592.247802734375 2688.146728515625,2622.923583984375 2649.802001953125,2703.447509765625 2553.940185546875,2726.45458984375 2442.740234375,2722.6201171875 2389.0576171875,2703.447509765625 2343.0439453125,2688.109619140625 2362.21630859375,2741.79248046875 2419.7333984375,2776.302734375 2442.740234375,2803.14404296875 2454.24365234375,2810.81298828125 2454.24365234375,2860.6611328125 2400.56103515625,2914.34375 2366.05078125,2906.6748046875 2331.54052734375,2902.84033203125 2285.52685546875,2875.9990234375 2266.3544921875,2837.654296875 2228.009765625,2810.81298828125 2174.326904296875,2776.302734375 2135.982177734375,2753.2958984375 2112.975341796875,2749.46142578125 2093.802978515625,2772.46826171875 2059.292724609375,2783.9716796875 1928.9205322265625,2818.48193359375 1917.4171142578125,2902.84033203125 1833.0587158203125,2918.17822265625 1744.86572265625,2837.654296875 1741.03125,2795.47509765625 1741.03125,2757.13037109375 1744.86572265625,2703.447509765625 1537.8040771484375,2630.592529296875 1541.6385498046875,2672.771728515625 1545.4730224609375,2699.613037109375 1537.8040771484375,2730.2890625 1507.128173828125,2745.626953125 1422.769775390625,2783.9716796875 1384.425048828125,2806.978515625 1349.9146728515625,2818.48193359375 1349.9146728515625,2833.81982421875 1399.762939453125,2868.330078125 1403.597412109375,3109.902099609375))";
//		poly=poly.replace(" ", "|");
//		poly=poly.replace(",", " ");
//		poly=poly.replace("|", ",");					
//
//		AnnotationEntry annoEntryDB = new AnnotationEntry(correspondingDepictionEntry.getDepictionID(), "restoredAnnotation9",icolist , poly, "21", false, false);
//		dbService.setAnnotationResults(annoEntryDB, new AsyncCallback<Boolean>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				Util.doLogging(caught.getLocalizedMessage());
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(Boolean result) {
//				Util.doLogging("Annotation Saved: "+Boolean.toString(result));
//			}
//		});			

		if (!shortNameTF.validate()) {
			saveToolButton.enable();			
			return;
		}
		ArrayList<ImageEntry> relatedImageEntryList = new ArrayList<ImageEntry>();
		System.out.println("Length bibliographyEntries of Depiction " + Integer.toString(correspondingDepictionEntry.getDepictionID()) + "after save triggered, but before adding new updated entries: " + Integer.toString(correspondingDepictionEntry.getRelatedBibliographyList().size()));
		for (int i = 0; i < imageEntryLS.size(); ++i) {
			relatedImageEntryList.add(imageEntryLS.get(i));
		}
//		List<WallTreeEntry> test = new ArrayList<WallTreeEntry>(correspondingDepictionEntry.getWalls());
//		List<WallTreeEntry> test2 = new ArrayList<WallTreeEntry>(correspondingDepictionEntry.getWalls());
//		test.removeAll(test2);
		correspondingDepictionEntry.setRelatedIconographyList(iconographySelector.getSelectedIconography());
		correspondingDepictionEntry.setRelatedImages(relatedImageEntryList);
		correspondingDepictionEntry.setRelatedBibliographyList(bibliographySelector.getSelectedEntries());
		correspondingDepictionEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());
		correspondingDepictionEntry.setCave(caveSelectionCB.getValue());
		System.out.println("Length bibliographyEntries of Depiction " + Integer.toString(correspondingDepictionEntry.getDepictionID()) + "after save triggered, after adding new updated entries: " + Integer.toString(correspondingDepictionEntry.getRelatedBibliographyList().size()));
			
		Util.doLogging("correspondingDepictionEntry.getDepictionI.getD() = "
				+ Integer.toString(correspondingDepictionEntry.getDepictionID()));

		if (correspondingDepictionEntry.getDepictionID() == 0) {

			dbService.insertDepictionEntry(correspondingDepictionEntry, iconographySelector.getSelectedIconography(),
					new AsyncCallback<Integer>() {

						@Override
						public void onSuccess(Integer newDepictionID) {
							saveToolButton.enable();
							Util.doLogging("Saving Depiction successfull!");
							Util.doLogging("correspondingDepictionEntry.getDepictionID() = "
									+ Integer.toString(correspondingDepictionEntry.getDepictionID()));
							correspondingDepictionEntry.setDepictionID(newDepictionID.intValue());
							Util.doLogging("correspondingDepictionEntry.getDepictionID() = "
									+ Integer.toString(correspondingDepictionEntry.getDepictionID()));
							for (EditorListener el : getListenerList()) {
								if (el instanceof DepictionView) {
									((DepictionView) el).setDepictionEntry(correspondingDepictionEntry);
								}
							}
							saveSuccess = true;
							doretry(close);
//					updateEntry(correspondingDepictionEntry);
							if (close) {
								closeEditor(correspondingDepictionEntry);
								bibliographySelector.clearPages();
							}
							if (slide != 0) {
								doslide(slide);
							}

						}

						@Override
						public void onFailure(Throwable caught) {
							Util.doLogging("Saving Depiction failed!");
							de.cses.client.Util.doLogging(caught.getLocalizedMessage());
							doretry(close);
						}
					});
		} else {
			Util.doLogging("Starting UpdateDepictionEntry");
			dbService.updateDepictionEntry(correspondingDepictionEntry,
					 iconographySelector.getSelectedIconography(), UserLogin.getInstance().getSessionID(), new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Util.doLogging("Saving Depiction failed!");
							de.cses.client.Util.doLogging(caught.getLocalizedMessage());
							doretry(close);
						}

						@Override
						public void onSuccess(Boolean updateSucessful) {
							saveToolButton.enable();
							Util.doLogging("Updating Depiction successfull!");
//					updateEntry(correspondingDepictionEntry);
							if (updateSucessful) {
								saveSuccess = updateSucessful;
								doretry(close);
								for (EditorListener el : getListenerList()) {
									if (el instanceof DepictionView) {
										((DepictionView) el).setDepictionEntry(correspondingDepictionEntry);
									}
								}
								if (close) {
									closeEditor(correspondingDepictionEntry);
									bibliographySelector.clearPages();
								}
							}
							if (slide != 0) {
								doslide(slide);
							}

						}
					});
		}

	}

	private void doretry(boolean close) {
		if (saveSuccess) {
			Util.doLogging("Depiction saved sucessfully!");
		} else {
			Util.showYesNo("Saving Process finished with errors!", "Do you want to retray saving?",
					new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							Util.doLogging("Depiction Save triggert by Dialog Saving Retry");
							save(close, 0);
						}
					}, new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							saveToolButton.enable();
							if (close) {
								closeEditor(correspondingDepictionEntry);
								bibliographySelector.clearPages();
							}

						}
					}, new KeyDownHandler() {

						@Override
						public void onKeyDown(KeyDownEvent e) {

						}
					});
		}
	}
	private void reloadPics() {
		imageListView.getStore().clear();
		osdLoader.destroyAllViewers();
		loadImages();
		setosd();
	}
}

