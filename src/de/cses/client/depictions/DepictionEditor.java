/*
 * Copyright 2016 
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
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.depictions.DepictionEditor2.CaveLayoutViewTemplates;
import de.cses.client.depictions.DepictionEditor2.CaveViewTemplates;
import de.cses.client.depictions.DepictionEditor2.ExpeditionViewTemplates;
import de.cses.client.depictions.DepictionEditor2.ImageViewTemplates;
import de.cses.client.depictions.DepictionEditor2.StyleViewTemplates;
import de.cses.client.depictions.DepictionEditor2.VendorViewTemplates;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.walls.Walls;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;

public class DepictionEditor implements IsWidget, ImageSelectorListener {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField inscriptionsField;
	private TextField datingField;
	private NumberField<Double> widthField;
	private NumberField<Double> heightField;
	private DateField purchaseDateField;
	private DateField dateOfAcquisitionField;
	private TextArea descriptionArea;
	private TextField backgroundColourField;
	private TextField materialField;
	private TextArea generalRemarksArea;
	private TextArea othersSuggestedIdentificationsArea;
	protected IconographySelector iconographySelector;
	protected PictorialElementSelector peSelector;
	protected ImageSelector imageSelector;
	private FramedPanel mainPanel;
	protected PopupPanel imageSelectionDialog;
	private ArrayList<DepictionEditorListener> listener;
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
	protected PopupPanel wallEditorDialog;
	private Walls wallEditor;

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
		@XTemplate("<div>{officialNumber}: {officialName}</div>")
		SafeHtml caveLabel(String officialNumber, String officialName);
		
		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);	
	}

	interface ExpeditionProperties extends PropertyAccess<ExpeditionEntry> {
		ModelKeyProvider<ExpeditionEntry> expeditionID();
		LabelProvider<ExpeditionEntry> name();
	}

	interface ExpeditionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}<br>{startYear} - {endYear}</div>")
		SafeHtml expedLabel(String name, String startYear, String endYear);
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
	}

	/**
	 * creates the view how a thumbnail of an image entry will be shown currently
	 * we are relying on the url of the image until we have user management
	 * implemented and protect images from being viewed from the outside without
	 * permission
	 * 
	 * @author alingnau
	 *
	 */
	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" width=\"150\" height=\"150\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);

		// @XTemplate("<div qtip=\"{slogan}\" qtitle=\"State Slogan\">{name}</div>")
		// SafeHtml state(String slogan, String name);
	}

	interface CaveLayoutViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" width=\"242\" height=\"440\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);
	}

	public DepictionEditor(DepictionEntry entry, DepictionEditorListener deListener) {
		if (entry != null) {
			this.correspondingDepictionEntry = entry;
		} else {
			createCorrespondingDepictionEntry();
		}
		listener = new ArrayList<DepictionEditorListener>();
		listener.add(deListener);
		// depictionEntryList = new
		// ListStore<DepictionEntry>(depictionProps.depictionID());
		// refreshDepictionList();
		iconographySelector = new IconographySelector();
		peSelector = new PictorialElementSelector();
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

		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				for (CaveEntry ce : result) {
					caveEntryList.add(ce);
				}
				Info.display("CaveID", "loaded "+caveEntryList.size() + " caveEditor");
			}
		});

		dbService.getStyles(new AsyncCallback<ArrayList<StyleEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<StyleEntry> result) {
				for (StyleEntry se : result) {
					styleEntryList.add(se);
				}
			}
		});

		dbService.getVendors(new AsyncCallback<ArrayList<VendorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<VendorEntry> result) {
				for (VendorEntry ve : result) {
					vendorEntryList.add(ve);
				}
			}
		});

		dbService.getExpeditions(new AsyncCallback<ArrayList<ExpeditionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<ExpeditionEntry> result) {
				for (ExpeditionEntry exped : result) {
					expedEntryList.add(exped);
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
			public void onSuccess(ArrayList<ImageEntry> result) {
				for (ImageEntry ie : result) {
					imageEntryList.add(ie);
				}
			}
		});
	}

	private void createCorrespondingDepictionEntry() {
		correspondingDepictionEntry = new DepictionEntry(0, 0, "add text here", null, null, "add text here",
				"add text here", "add text here", "add text here", 0, 0, null, 0, null, 0, 0, 0, 0);
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	/**
	 * Here the view is initialised. This is only done once at the beginning!
	 */
	private void initPanel() {
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Depiction Editor");

		FramedPanel attributePanel;

		TabPanel tabPanel = new TabPanel();
		tabPanel.setTabScroll(false);

		HorizontalPanel hPanel = new HorizontalPanel();
		VerticalPanel vPanel = new VerticalPanel();

		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=true");
				return imageViewTemplates.image(imageUri, item.getTitle());
			}
		}));

		imageListView.setSize("340", "290");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("350", "300");

		vendorSelection = new ComboBox<VendorEntry>(vendorEntryList, vendorProps.vendorName(),
				new AbstractSafeHtmlRenderer<VendorEntry>() {

					@Override
					public SafeHtml render(VendorEntry item) {
						final VendorViewTemplates vTemplates = GWT.create(VendorViewTemplates.class);
						return vTemplates.vendorName(item.getVendorName());
					}

				});
		vendorSelection.setEmptyText("Select a Vendor ...");
		vendorSelection.setTypeAhead(false);
		vendorSelection.setEditable(false);
		vendorSelection.setTriggerAction(TriggerAction.ALL);
		vendorSelection.addSelectionHandler(new SelectionHandler<VendorEntry>() {

			@Override
			public void onSelection(SelectionEvent<VendorEntry> event) {
				correspondingDepictionEntry.setVendorID(event.getSelectedItem().getVendorID());
			}
		});

		styleSelection = new ComboBox<StyleEntry>(styleEntryList, styleProps.styleName(),
				new AbstractSafeHtmlRenderer<StyleEntry>() {

					@Override
					public SafeHtml render(StyleEntry item) {
						final StyleViewTemplates svTemplates = GWT.create(StyleViewTemplates.class);
						return svTemplates.styleName(item.getStyleName());
					}
				});
		styleSelection.setEmptyText("Select a Style ...");
		styleSelection.setTypeAhead(false);
		styleSelection.setEditable(false);
		styleSelection.setTriggerAction(TriggerAction.ALL);
		styleSelection.addSelectionHandler(new SelectionHandler<StyleEntry>() {

			@Override
			public void onSelection(SelectionEvent<StyleEntry> event) {
				correspondingDepictionEntry.setStyleID(event.getSelectedItem().getStyleID());
			}
		});

		caveSelection = new ComboBox<CaveEntry>(caveEntryList, caveProps.officialNumber(),
				new AbstractSafeHtmlRenderer<CaveEntry>() {

					@Override
					public SafeHtml render(CaveEntry item) {
						final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
						if ((item.getOfficialName() != null) && (item.getOfficialName().length() == 0)) {
							return cvTemplates.caveLabel(item.getOfficialNumber());
						} else {
							return cvTemplates.caveLabel(item.getOfficialNumber(), item.getOfficialName());
						}
					}
				});
		caveSelection.setEmptyText("Select a Cave ...");
		caveSelection.setTypeAhead(false);
		caveSelection.setEditable(false);
		caveSelection.setTriggerAction(TriggerAction.ALL);
		caveSelection.addSelectionHandler(new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				correspondingDepictionEntry.setCaveID(event.getSelectedItem().getCaveID());
			}
		});

		expedSelection = new ComboBox<ExpeditionEntry>(expedEntryList, expedProps.name(),
				new AbstractSafeHtmlRenderer<ExpeditionEntry>() {

					@Override
					public SafeHtml render(ExpeditionEntry item) {
						final ExpeditionViewTemplates expedTemplates = GWT.create(ExpeditionViewTemplates.class);
						DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy");
						return expedTemplates.expedLabel(item.getName(), dtf.format(item.getStartDate()),
								dtf.format(item.getEndDate()));
					}
				});
		expedSelection.setEmptyText("Select an expedition ...");
		expedSelection.setTypeAhead(false);
		expedSelection.setEditable(false);
		expedSelection.setTriggerAction(TriggerAction.ALL);
		expedSelection.addSelectionHandler(new SelectionHandler<ExpeditionEntry>() {

			@Override
			public void onSelection(SelectionEvent<ExpeditionEntry> event) {
				correspondingDepictionEntry.setExpeditionID(event.getSelectedItem().getExpeditionID());
			}
		});

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Located in Cave");
		caveSelection.select(caveEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getCaveID())));
		caveSelection.setWidth(250);
		caveSelection.setToolTip("This field can only be changed until a depiction is allocated to a wall");
		// TODO check if wall id is set, then set caveSelection.editable(false)
		attributePanel.add(caveSelection);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Belongs to wall");
		attributePanel.add(new Label("Wall selection"));
		vPanel.add(attributePanel);
		
		HorizontalPanel dimPanel = new HorizontalPanel();
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Width");
		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthField.setWidth(50);
		widthField.setValue(correspondingDepictionEntry.getWidth());
		attributePanel.add(widthField);
		dimPanel.add(attributePanel);
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Height");
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightField.setWidth(50);
		heightField.setValue(correspondingDepictionEntry.getHeight());
		attributePanel.add(heightField);
		dimPanel.add(attributePanel);
		vPanel.add(dimPanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Acquired by expedition");
		expedSelection
				.select(expedEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getExpeditionID())));
		expedSelection.setWidth(250);
		attributePanel.add(expedSelection);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date of acquisition");
		dateOfAcquisitionField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		attributePanel.add(dateOfAcquisitionField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Vendor");
		vendorSelection.select(vendorEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getVendorID())));
		vendorSelection.setWidth(250);
		attributePanel.add(vendorSelection);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date purchased");
		purchaseDateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		purchaseDateField.setValue(correspondingDepictionEntry.getPurchaseDate());
		attributePanel.add(purchaseDateField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Current location");
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Background colour");
		backgroundColourField = new TextField();
		backgroundColourField.setWidth(35);
		attributePanel.add(backgroundColourField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Material");
		materialField = new TextField();
		materialField.setWidth(130);
		attributePanel.add(materialField);
		vPanel.add(attributePanel);
		hPanel.add(vPanel);
		
		vPanel = new VerticalPanel();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Wall");
		TextButton wallEditorButton = new TextButton("Position");
		wallEditor = new Walls(1, false);
		wallEditorButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				wallEditorDialog = new PopupPanel();
				new Draggable(wallEditorDialog);
				wallEditorDialog.add(wallEditor);
				wallEditor.createNewDepictionOnWall(correspondingDepictionEntry, true, true);
				wallEditor.setPanel(wallEditorDialog);
				wallEditorDialog.setModal(true);
				wallEditorDialog.center();
				wallEditorDialog.show();
			}
		});
		attributePanel.addButton(wallEditorButton);
		attributePanel.setButtonAlign(BoxLayoutPack.CENTER);

		final CaveLayoutViewTemplates caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);	
		SafeUri imageUri = UriUtils.fromString("resource?background=centralPillarCave.jpeg");
		FlowLayoutContainer imageContainer = new FlowLayoutContainer();
		imageContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(imageUri, "Central Pillar Cave")));
		attributePanel.add(imageContainer);
		hPanel.add(attributePanel);

		tabPanel.add(hPanel, "Basics");
		hPanel = new HorizontalPanel();
		vPanel = new VerticalPanel();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Style");
		styleSelection.select(styleEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getStyleID())));
		styleSelection.setWidth(300);
		attributePanel.add(styleSelection);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Inscriptions");
		inscriptionsField = new TextField();
		inscriptionsField.setWidth(130);
		inscriptionsField.setText(correspondingDepictionEntry.getInscriptions());
		attributePanel.add(inscriptionsField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Dating");
		datingField = new TextField();
		datingField.setWidth(130);
		datingField.setText(correspondingDepictionEntry.getDating());
		attributePanel.add(datingField);
		vPanel.add(attributePanel);

		imageSelector = new ImageSelector(ImageSelector.PHOTO, this);
		TextButton addImageButton = new TextButton("Select Image");
		addImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageSelectionDialog = new PopupPanel();
				new Draggable(imageSelectionDialog);
				imageSelectionDialog.add(imageSelector);
				imageSelectionDialog.setModal(true);
				imageSelectionDialog.center();
				imageSelectionDialog.show();
			}
		});
		TextButton removeImageButton = new TextButton("Remove Image");
		removeImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageEntryList.remove(imageListView.getSelectionModel().getSelectedItem());
			}
		});

		hPanel.add(vPanel);
		vPanel = new VerticalPanel();
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Description");
		descriptionArea = new TextArea();
		descriptionArea.setSize("350", "140");
		attributePanel.add(descriptionArea);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("General remarks");
		generalRemarksArea = new TextArea();
		generalRemarksArea.setSize("350", "140");
		attributePanel.add(generalRemarksArea);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Other suggested identifications");
		othersSuggestedIdentificationsArea = new TextArea();
		othersSuggestedIdentificationsArea.setSize("350", "140");
		attributePanel.add(othersSuggestedIdentificationsArea);
		vPanel.add(attributePanel);

		hPanel.add(vPanel);
		tabPanel.add(hPanel, "Description");
//		vPanel = new VerticalPanel();

		TextButton iconographyExpandButton = new TextButton("expand tree");
		iconographyExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographySelector.expandAll();
			}
		});
		TextButton iconographyCollapseButton = new TextButton("collapse tree");
		iconographyCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographySelector.collapseAll();
			}
		});
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Iconography");
		attributePanel.add(iconographySelector);
		attributePanel.addButton(iconographyExpandButton);
		attributePanel.addButton(iconographyCollapseButton);
		tabPanel.add(attributePanel, "Iconography");
		
		hPanel = new HorizontalPanel();
		
		HorizontalPanel hbp = new HorizontalPanel();
		hbp.add(addImageButton);
		hbp.add(removeImageButton);
		VerticalPanel vp = new VerticalPanel();
		vp.add(lf);
		vp.add(hbp);
		vp.setSize("350", "300");
		FramedPanel depictionImagesPanel = new FramedPanel();
		depictionImagesPanel.setHeading("Images");
		depictionImagesPanel.add(vp);
//		hPanel.add(attributePanel);
		
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
		hPanel.add(attributePanel);
		tabPanel.add(hPanel, "Images");

//		hPanel.add(vPanel);
//		tabPanel.add(hPanel, "Details");
		
    BorderLayoutData eastData = new BorderLayoutData(200);
    eastData.setMargins(new Margins(0, 5, 5, 5));
    eastData.setCollapsible(true);
    eastData.setCollapseHeaderVisible(true);

    MarginData centerData = new MarginData(0, 5, 5, 0);

    BorderLayoutContainer view = new BorderLayoutContainer();
    view.setBorders(true);
    view.setCenterWidget(tabPanel, centerData);
    view.setEastWidget(depictionImagesPanel, eastData);
		
		mainPanel.add(view);
		mainPanel.setSize("900px", "650px");

		TextButton saveButton = new TextButton("Save & Exit");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveDepictionEntry();
			}
		});
		TextButton cancelButton = new TextButton("Cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				cancelDepictionEditor();
			}
		});
		mainPanel.addButton(saveButton);
		mainPanel.addButton(cancelButton);
	}

	protected void cancelDepictionEditor() {
		Iterator<DepictionEditorListener> deIterator = listener.iterator();
		while (deIterator.hasNext()) {
			deIterator.next().depictionSaved(null);
		}
	}

	protected void setFields(DepictionEntry de) {
		// TODO Auto-generated method stub

	}

	// protected void newDepictionEntry() {
	// refreshDepictionList();
	// }
	//
	protected void saveDepictionEntry() {
		updateDepictionEntryFromValues();
		Iterator<DepictionEditorListener> deIterator = listener.iterator();
		while (deIterator.hasNext()) {
			deIterator.next().depictionSaved(null);
		}
	}

	private void updateDepictionEntryFromValues() {

	}

	@Override
	public void imageSelected(int imageID) {
		if (imageID != 0) {
			dbService.getImage(imageID, new AsyncCallback<ImageEntry>() {

				@Override
				public void onSuccess(ImageEntry result) {
					imageEntryList.add(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
		imageSelectionDialog.hide();
	}

}