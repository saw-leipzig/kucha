/*
 * Copyright 2016-2017
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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MinNumberValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.client.walls.WallSelector;
import de.cses.client.walls.Walls;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ModeOfRepresentationEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;

public class DepictionEditor extends AbstractEditor {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextArea inscriptionsTestArea;
	private TextField datingField;
	private NumberField<Double> widthField;
	private NumberField<Double> heightField;
	private DateField purchaseDateField;
	private DateField dateOfAcquisitionField;
	private TextArea descriptionArea;
	private TextField backgroundColourField;
	private TextArea generalRemarksArea;
	private TextArea othersSuggestedIdentificationsArea;
	protected IconographySelector iconographySelector;
	protected PictorialElementSelector peSelector;
	protected ImageSelector imageSelector;
	private FramedPanel mainPanel;
	protected PopupPanel imageSelectionDialog;
	// private ArrayList<DepictionEditorListener> listener;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ListStore<ImageEntry> imageEntryList;
	private ImageProperties imgProperties;
	private DepictionEntry correspondingDepictionEntry;
	private VendorProperties vendorProps;
	private ListStore<VendorEntry> vendorEntryList;
	private ComboBox<VendorEntry> vendorSelection;
	private ComboBox<StyleEntry> styleSelection;
	private StyleProperties styleProps;
	private ListStore<StyleEntry> styleEntryList;
	private CaveProperties caveProps;
	private ListStore<CaveEntry> caveEntryList;
	private ComboBox<CaveEntry> caveSelection;
	private ExpeditionProperties expedProps;
	private ListStore<ExpeditionEntry> expedEntryList;
	private ComboBox<ExpeditionEntry> expedSelection;
	private ComboBox<ModeOfRepresentationEntry> modeOfRepresentationSelection;
	protected PopupPanel wallEditorDialog;
	private Walls wallEditor;
	private Label iconographyLabel;
	protected PopupPanel iconographySelectionDialog;
	private WallSelector wallSelectorPanel;
	private TextArea separateAksarasTextArea;
	private ModesOfRepresentationProperties morProps;
	private ListStore<ModeOfRepresentationEntry> morEntryList;

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
		@XTemplate("<div>{siteName} {officialNumber}: {officialName}</div>")
		SafeHtml caveLabel(String siteName, String officialNumber, String officialName);

		@XTemplate("<div>{siteName} {officialNumber}</div>")
		SafeHtml caveLabel(String siteName, String officialNumber);

		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);
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

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();

		ValueProvider<ImageEntry, String> shortName();
	}

	/**
	 * creates the view how a thumbnail of an image entry will be shown currently we are relying on the url of the image until we have user management implemented
	 * and protect images from being viewed from the outside without permission
	 * 
	 * @author alingnau
	 *
	 */
	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img src=\"{imageUri}\" style=\"width: 230px; height: auto; align-content: center; margin: 5px;\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);
	}

	public DepictionEditor(DepictionEntry entry) {
		if (entry != null) {
			correspondingDepictionEntry = entry;
		} else {
			correspondingDepictionEntry = new DepictionEntry();
		}
		// listener = new ArrayList<DepictionEditorListener>();
		// listener.add(deListener);
		peSelector = new PictorialElementSelector(correspondingDepictionEntry.getDepictionID());
		imgProperties = GWT.create(ImageProperties.class);
		imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
		if (correspondingDepictionEntry.getDepictionID() > 0) {
			loadImages();
		}
		vendorProps = GWT.create(VendorProperties.class);
		vendorEntryList = new ListStore<VendorEntry>(vendorProps.vendorID());
		styleProps = GWT.create(StyleProperties.class);
		styleEntryList = new ListStore<StyleEntry>(styleProps.styleID());
		caveProps = GWT.create(CaveProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveProps.caveID());
		expedProps = GWT.create(ExpeditionProperties.class);
		expedEntryList = new ListStore<ExpeditionEntry>(expedProps.expeditionID());
		morProps = GWT.create(ModesOfRepresentationProperties.class);
		morEntryList = new ListStore<ModeOfRepresentationEntry>(morProps.modeOfRepresentationID());

		initPanel();
		loadCaves();
		loadStyles();
		loadVendors();
		loadExpeditions();
		loadModesOfRepresentation();
	}

	/**
	 * 
	 */
	private void loadExpeditions() {
		for (ExpeditionEntry exped : StaticTables.getInstance().getExpeditionEntries().values()) {
			expedEntryList.add(exped);
		}
		if (correspondingDepictionEntry.getExpeditionID() > 0) {
			expedSelection.setValue(expedEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getExpeditionID())));
		}
	}

	/**
	 * 
	 */
	private void loadVendors() {
		dbService.getVendors(new AsyncCallback<ArrayList<VendorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<VendorEntry> vendorResults) {
				for (VendorEntry ve : vendorResults) {
					vendorEntryList.add(ve);
				}
				if (correspondingDepictionEntry.getVendorID() > 0) {
					vendorSelection.setValue(vendorEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getVendorID())));
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void loadModesOfRepresentation() {
		for (ModeOfRepresentationEntry morEntry : StaticTables.getInstance().getModesOfRepresentationEntries().values()) {
			morEntryList.add(morEntry);
		}
		if (correspondingDepictionEntry.getModeOfRepresentationID() > 0) {
			modeOfRepresentationSelection.setValue(morEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getModeOfRepresentationID())));
		}
	}

	/**
	 * 
	 */
	private void loadStyles() {
		for (StyleEntry se : StaticTables.getInstance().getStyleEntries().values()) {
			styleEntryList.add(se);
		}
		if (correspondingDepictionEntry.getStyleID() > 0) {
			styleSelection.setValue(styleEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getStyleID())));
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
				for (CaveEntry ce : caveResults) {
					caveEntryList.add(ce);
				}
				if (correspondingDepictionEntry.getCaveID() > 0) {
					CaveEntry ce = caveEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getCaveID()));
					caveSelection.setValue(ce);
					wallSelectorPanel.setCave(ce);
				}
			}
		});
	}

	/**
	 * 
	 */
	private void loadImages() {
		dbService.getRelatedImages(correspondingDepictionEntry.getDepictionID(), new AsyncCallback<ArrayList<ImageEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<ImageEntry> imgResults) {
				for (ImageEntry ie : imgResults) {
					imageEntryList.add(ie);
				}
			}
		});
	}

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
	private void initPanel() {
		mainPanel = new FramedPanel();
		// the name is set depending on wheter we have a new or an existing entry
		mainPanel.setHeading("Painted Representation Editor ("
				+ (correspondingDepictionEntry.getDepictionID() > 0 ? "ID = " + correspondingDepictionEntry.getDepictionID() : "NEW") + ")");

		// this panel is used for the different fields in the editor
		FramedPanel attributePanel;

		TabPanel tabPanel = new TabPanel();
		tabPanel.setTabScroll(false);
		// the tab only gets 75% of the width and the added images get the 25% on the right to be shown all the time
		tabPanel.setSize("75%", "100%");

		HorizontalLayoutContainer hlContainer = new HorizontalLayoutContainer();
		hlContainer.setSize("100%", "100%");
		VerticalLayoutContainer vlContainer = new VerticalLayoutContainer();

		// the images related with the depiction entry that will be shown on the right
		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=300" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				return imageViewTemplates.image(imageUri, item.getShortName());
			}
		}));

//		imageListView.setSize("340", "290");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("100%", "100%");

		/**
		 * --------------------- content of first tab (BASICS) starts here --------------------------------
		 */
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Located in Cave");
		caveSelection = new ComboBox<CaveEntry>(caveEntryList, caveProps.officialNumber(), new AbstractSafeHtmlRenderer<CaveEntry>() {

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
				// String site = "test";
				if ((se != null) && (item.getHistoricName() != null) && (item.getHistoricName().length() > 0)) {
					return cvTemplates.caveLabel(se.getName(), item.getOfficialNumber(), item.getHistoricName());
				} else if (se != null) {
					return cvTemplates.caveLabel(se.getName(), item.getOfficialNumber());
				} else {
					return cvTemplates.caveLabel(item.getOfficialNumber());
				}
			}
		});
		caveSelection.setEmptyText("nothing selected");
		caveSelection.setTypeAhead(true);
		caveSelection.setEditable(false);
		caveSelection.setTriggerAction(TriggerAction.ALL);
		caveSelection.addSelectionHandler(new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				correspondingDepictionEntry.setCaveID(event.getSelectedItem().getCaveID());
				wallSelectorPanel.setCave(event.getSelectedItem());
			}
		});
		caveSelection.setToolTip("This field can only be changed until a depiction is allocated to a wall");
		// TODO check if wall publicationTypeID is set, then set caveSelection.editable(false)
		attributePanel.add(caveSelection);
		// attributePanel.setWidth("40%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		// attributePanel = new FramedPanel();
		// attributePanel.setHeading("Belongs to wall");
		// attributePanel.add(new Label("Wall selection"));
		// vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));

		HorizontalPanel dimPanel = new HorizontalPanel();
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Width");
		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthField.setWidth("60%");
		widthField.addValidator(new MinNumberValidator<Double>((double) 0));
		widthField.setValue(correspondingDepictionEntry.getWidth());
		widthField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				correspondingDepictionEntry.setWidth(event.getValue());
			}
		});
		attributePanel.add(widthField);
		dimPanel.add(attributePanel);
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Height");
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightField.setWidth("60%");
		heightField.addValidator(new MinNumberValidator<Double>((double) 0));
		heightField.setValue(correspondingDepictionEntry.getHeight());
		heightField.addValueChangeHandler(new ValueChangeHandler<Double>() {

			@Override
			public void onValueChange(ValueChangeEvent<Double> event) {
				correspondingDepictionEntry.setHeight(event.getValue());
			}
		});
		attributePanel.add(heightField);
		dimPanel.add(attributePanel);
		vlContainer.add(dimPanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Acquired by expedition");
		expedSelection = new ComboBox<ExpeditionEntry>(expedEntryList, expedProps.name(), new AbstractSafeHtmlRenderer<ExpeditionEntry>() {

			@Override
			public SafeHtml render(ExpeditionEntry item) {
				final ExpeditionViewTemplates expedTemplates = GWT.create(ExpeditionViewTemplates.class);
				DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
				return expedTemplates.expedLabel(item.getName(), item.getLeader(), dtf.format(item.getStartDate()), dtf.format(item.getEndDate()));
			}
		});
		expedSelection.setEmptyText("nothing selected");
		expedSelection.addSelectionHandler(new SelectionHandler<ExpeditionEntry>() {

			@Override
			public void onSelection(SelectionEvent<ExpeditionEntry> event) {
				correspondingDepictionEntry.setExpeditionID(event.getSelectedItem().getExpeditionID());
			}
		});
		expedSelection.setHeight("1.0");
		expedSelection.setTypeAhead(false);
		expedSelection.setEditable(false);
		expedSelection.setTriggerAction(TriggerAction.ALL);
		expedSelection.addSelectionHandler(new SelectionHandler<ExpeditionEntry>() {

			@Override
			public void onSelection(SelectionEvent<ExpeditionEntry> event) {
				correspondingDepictionEntry.setExpeditionID(event.getSelectedItem().getExpeditionID());
			}
		});
		attributePanel.add(expedSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Vendor");
		vendorSelection = new ComboBox<VendorEntry>(vendorEntryList, vendorProps.vendorName(), new AbstractSafeHtmlRenderer<VendorEntry>() {

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
				correspondingDepictionEntry.setVendorID(event.getSelectedItem().getVendorID());
			}
		});
		attributePanel.add(vendorSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date purchased");
		purchaseDateField = new DateField(new DateTimePropertyEditor("yyyy"));
		purchaseDateField.setValue(correspondingDepictionEntry.getPurchaseDate());
		purchaseDateField.setEmptyText("nothing selected");
		// TODO add change handler
		attributePanel.add(purchaseDateField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Current location");
		// TODO add currentLocationID selector
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Background colour");
		backgroundColourField = new TextField();
		backgroundColourField.setValue(correspondingDepictionEntry.getBackgroundColour());
		backgroundColourField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setBackgroundColour(event.getValue());
			}
		});
		attributePanel.add(backgroundColourField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Modes of Representation");
		modeOfRepresentationSelection = new ComboBox<ModeOfRepresentationEntry>(morEntryList, morProps.name(), new AbstractSafeHtmlRenderer<ModeOfRepresentationEntry>() {

			@Override
			public SafeHtml render(ModeOfRepresentationEntry morEntry) {
				ModesOfRepresentationViewTemplates morTemplates = GWT.create(ModesOfRepresentationViewTemplates.class);
				return morTemplates.morLabel(morEntry.getName());
			}
			
		});
		modeOfRepresentationSelection.setEmptyText("nothing selected");
		modeOfRepresentationSelection.setTypeAhead(false);
		modeOfRepresentationSelection.setEditable(false);
		modeOfRepresentationSelection.setTriggerAction(TriggerAction.ALL);
		modeOfRepresentationSelection.addSelectionHandler(new SelectionHandler<ModeOfRepresentationEntry>() {

			@Override
			public void onSelection(SelectionEvent<ModeOfRepresentationEntry> event) {
				correspondingDepictionEntry.setModeOfRepresentationID(event.getSelectedItem().getModeOfRepresentationID());
			}
		});
		attributePanel.add(modeOfRepresentationSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0 / 8));

		hlContainer.add(vlContainer, new HorizontalLayoutData(.4, 1.0));

		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Wall");
		TextButton wallEditorButton = new TextButton("set position on wall");
		wallEditor = new Walls(1, false);
		wallEditorButton.addSelectHandler(new SelectHandler() {

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
		attributePanel.addButton(wallEditorButton);

		wallSelectorPanel = new WallSelector(350);
		attributePanel.add(wallSelectorPanel);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0));
		hlContainer.add(vlContainer, new HorizontalLayoutData(.6, 1.0));
		tabPanel.add(hlContainer, "Basics");

		/**
		 * --------------------- content of second tab (Descriptions) starts here --------------------------------
		 */
		hlContainer = new HorizontalLayoutContainer();
		hlContainer.setSize("100%", "100%");

		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Style");
		styleSelection = new ComboBox<StyleEntry>(styleEntryList, styleProps.styleName(), new AbstractSafeHtmlRenderer<StyleEntry>() {

			@Override
			public SafeHtml render(StyleEntry item) {
				final StyleViewTemplates svTemplates = GWT.create(StyleViewTemplates.class);
				return svTemplates.styleName(item.getStyleName());
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
		attributePanel.add(styleSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Iconography");
		iconographyLabel = new Label();
		if (correspondingDepictionEntry.getIconographyID() > 0) {
			dbService.getIconographyEntry(correspondingDepictionEntry.getIconographyID(), new AsyncCallback<IconographyEntry>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(IconographyEntry iconResults) {
					if (iconResults != null) {
						iconographyLabel.setText(iconResults.getText());
					}
				}
			});
		}
		attributePanel.add(iconographyLabel);
		iconographySelector = new IconographySelector(correspondingDepictionEntry.getIconographyID(), new IconographySelectorListener() {

			@Override
			public void iconographySelected(IconographyEntry entry) {
				correspondingDepictionEntry.setIconographyID(entry.getIconographyID());
				iconographyLabel.setText(entry.getText());
				iconographySelectionDialog.hide();
			}

			@Override
			public void cancel() {
				iconographySelectionDialog.hide();
			}
		});
		TextButton selectIconographyButton = new TextButton("select Iconography");
		selectIconographyButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				iconographySelectionDialog = new PopupPanel();
				iconographySelectionDialog.add(iconographySelector);
				iconographySelectionDialog.setModal(true);
				iconographySelectionDialog.center();
			}
		});
		attributePanel.addButton(selectIconographyButton);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .25));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Inscriptions");
		inscriptionsTestArea = new TextArea();
		inscriptionsTestArea.setText(correspondingDepictionEntry.getInscriptions());
		inscriptionsTestArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setInscriptions(event.getValue());
			}
		});
		attributePanel.add(inscriptionsTestArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Separate Akṣaras");
		separateAksarasTextArea = new TextArea();
		separateAksarasTextArea.setText(correspondingDepictionEntry.getSeparateAksaras());
		separateAksarasTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setSeparateAksaras(event.getValue());
			}
		});
		attributePanel.add(separateAksarasTextArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Dating");
		datingField = new TextField();
		// datingField.setWidth(130);
		datingField.setText(correspondingDepictionEntry.getDating());
		datingField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setDating(event.getValue());
			}
		});
		attributePanel.add(datingField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		hlContainer.add(vlContainer, new HorizontalLayoutData(.35, 1.0));

		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Description");
		descriptionArea = new TextArea();
		descriptionArea.setValue(correspondingDepictionEntry.getDescription());
		descriptionArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setDescription(event.getValue());
			}
		});
		attributePanel.add(descriptionArea);

		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .33));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("General remarks");
		generalRemarksArea = new TextArea();
		generalRemarksArea.setValue(correspondingDepictionEntry.getGeneralRemarks());
		generalRemarksArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setGeneralRemarks(event.getValue());
			}
		});
		attributePanel.add(generalRemarksArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .33));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Other suggested identifications");
		othersSuggestedIdentificationsArea = new TextArea();
		othersSuggestedIdentificationsArea.setSize("100%", "33%");
		attributePanel.add(othersSuggestedIdentificationsArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .33));

		hlContainer.add(vlContainer, new HorizontalLayoutData(.65, 1.0));
		tabPanel.add(hlContainer, "Description");

		/**
		 * --------------------- definition of image panel on right side starts here --------------------------------
		 */
		imageSelector = new ImageSelector(ImageSelector.PHOTO, new ImageSelectorListener() {

			@Override
			public void imageSelected(int imageID) {
				if (imageID != 0) {
					dbService.getImage(imageID, new AsyncCallback<ImageEntry>() {

						@Override
						public void onSuccess(ImageEntry ieResults) {
							imageEntryList.add(ieResults);
						}

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}
					});
				}
				imageSelectionDialog.hide();
			}
		});
		TextButton addImageButton = new TextButton("Select Image");
		addImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageSelectionDialog = new PopupPanel();
				imageSelectionDialog.add(imageSelector);
				imageSelectionDialog.setModal(true);
				imageSelectionDialog.center();
			}
		});
		TextButton removeImageButton = new TextButton("Remove Image");
		removeImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageEntryList.remove(imageListView.getSelectionModel().getSelectedItem());
			}
		});
		TextButton setMasterButton = new TextButton("Set Master");
		setMasterButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				ImageEntry entry = imageListView.getSelectionModel().getSelectedItem();
				if (imageEntryList.indexOf(entry) > 0) {
					imageEntryList.remove(entry);
					imageEntryList.add(0, entry);
				}
			}
		});

		FramedPanel depictionImagesPanel = new FramedPanel();
		depictionImagesPanel.setHeading("Images");
		depictionImagesPanel.add(lf);
		// depictionImagesPanel.setSize("25%", "100%");
		depictionImagesPanel.addButton(addImageButton);
		depictionImagesPanel.addButton(removeImageButton);
		depictionImagesPanel.addButton(setMasterButton);

		/**
		 * --------------------- content of third tab (Pictorial Elements) starts here --------------------------------
		 */
		hlContainer = new HorizontalLayoutContainer();
		VerticalLayoutContainer peVLC = new VerticalLayoutContainer();

		TextButton peExpandButton = new TextButton("expand tree");
		peExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				peSelector.expandAll();
			}
		});
		TextButton peCollapseButton = new TextButton("collapse tree");
		peCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				peSelector.collapseAll();
			}
		});
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Pictorial Elements");
		attributePanel.add(peSelector);
		attributePanel.addButton(peExpandButton);
		attributePanel.addButton(peCollapseButton);
		hlContainer.add(attributePanel, new HorizontalLayoutData(1.0, 1.0));
		tabPanel.add(hlContainer, "Pictorial Elements");

		/**
		 * --------------------------- next the editor as a whole will be assembled -------------------
		 */

		// the layout of the right side panel with the images
//		BorderLayoutData eastData = new BorderLayoutData(.25);
//		eastData.setMargins(new Margins(0, 5, 5, 5));
//		eastData.setCollapsible(true);
//		eastData.setCollapseHeaderVisible(true);
//		eastData.setSplit(true);

//		MarginData centerData = new MarginData(0, 5, 5, 0);

//		BorderLayoutContainer view = new BorderLayoutContainer();
		HorizontalLayoutContainer mainHLC = new HorizontalLayoutContainer();
//		view.setBorders(true);
		mainHLC.add(tabPanel, new HorizontalLayoutData(.7, 1.0));
		mainHLC.add(depictionImagesPanel, new HorizontalLayoutData(.3, 1.0));
//		view.setCenterWidget(tabPanel, centerData);
//		view.setEastWidget(depictionImagesPanel, eastData);

		ToolButton saveToolButton = new ToolButton(ToolButton.SAVE);
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveDepictionEntry(false);
			}
		});
		
		ToolButton closeToolButton = new ToolButton(ToolButton.CLOSE);
		closeToolButton.setToolTip("close");
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				 Dialog d = new Dialog();
				 d.setHeading("Exit Warning!");
				 d.setWidget(new HTML("Do you wish to save before exiting?"));
				 d.setBodyStyle("fontWeight:bold;padding:13px;");
				 d.setPixelSize(300, 100);
				 d.setHideOnButtonClick(true);
				 d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
				 d.setModal(true);
				 d.center();
				 d.show();
				 d.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						saveDepictionEntry(true);
					}
				});
				 d.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 closeEditor();
					}
				});
			}
		});		
		
		mainPanel.add(mainHLC);
		mainPanel.setSize("900px", "650px");
		mainPanel.addTool(saveToolButton);
		mainPanel.addTool(closeToolButton);
	}

	// /**
	// * Called when the save button is pressed. Calls <code>DepictionEditorListener.depictionSaved(null)<code>
	// */
	// protected void cancelDepictionEditor() {
	// Iterator<DepictionEditorListener> deIterator = listener.iterator();
	// while (deIterator.hasNext()) {
	// deIterator.next().depictionSaved(null);
	// }
	// }

	/**
	 * Called when the save button is pressed. Calls <code>DepictionEditorListener.depictionSaved(correspondingDepictionEntry)<code>
	 * @param close 
	 */
	protected void saveDepictionEntry(boolean close) {
		ArrayList<ImageEntry> associatedImageEntryList = new ArrayList<ImageEntry>();
		for (int i = 0; i < imageEntryList.size(); ++i) {
			associatedImageEntryList.add(imageEntryList.get(i));
		}
		ArrayList<PictorialElementEntry> selectedPEList = new ArrayList<PictorialElementEntry>();
		for (PictorialElementEntry pe : peSelector.getSelectedPE()) {
			selectedPEList.add(pe);
		}
		if (correspondingDepictionEntry.getDepictionID() == 0) {
			dbService.insertDepictionEntry(correspondingDepictionEntry, associatedImageEntryList, selectedPEList, new AsyncCallback<Integer>() {

				@Override
				public void onSuccess(Integer newDepictionID) {
					correspondingDepictionEntry.setDepictionID(newDepictionID.intValue());
					if (close) {
						closeEditor();
					}
				}

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		} else {
			dbService.updateDepictionEntry(correspondingDepictionEntry, associatedImageEntryList, selectedPEList, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(Boolean updateSucessful) {
					if (close) {
						closeEditor();
					}
				}
			});
		}
	}

}