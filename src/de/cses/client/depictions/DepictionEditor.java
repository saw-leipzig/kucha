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
import java.util.List;

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
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
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
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.walls.Walls;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;

public class DepictionEditor implements IsWidget {

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
	private Label iconographyLabel;
	protected PopupPanel iconographySelectionDialog;
	private FlowLayoutContainer caveSketchContainer;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;

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
		@XTemplate("<img align=\"center\" margin=\"10\" src=\"{imageUri}\">")
		SafeHtml image(SafeUri imageUri);
	}

	public DepictionEditor(DepictionEntry entry, DepictionEditorListener deListener) {
		if (entry != null) {
			correspondingDepictionEntry = entry;
		} else {
			correspondingDepictionEntry = new DepictionEntry();
		}
		listener = new ArrayList<DepictionEditorListener>();
		listener.add(deListener);
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

		initPanel();
		loadCaves();
		loadStyles();
		loadVendors();
		loadExpeditions();
	}

	/**
	 * 
	 */
	private void loadExpeditions() {
		dbService.getExpeditions(new AsyncCallback<ArrayList<ExpeditionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<ExpeditionEntry> expedResults) {
				for (ExpeditionEntry exped : expedResults) {
					expedEntryList.add(exped);
				}
				if (correspondingDepictionEntry.getExpeditionID() > 0) {
					expedSelection.setValue(expedEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getExpeditionID())));
				}
			}
		});
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
	private void loadStyles() {
		dbService.getStyles(new AsyncCallback<ArrayList<StyleEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<StyleEntry> styleResults) {
				for (StyleEntry se : styleResults) {
					styleEntryList.add(se);
				}
				if (correspondingDepictionEntry.getStyleID() > 0) {
					styleSelection.setValue(styleEntryList.findModelWithKey(Integer.toString(correspondingDepictionEntry.getStyleID())));
				}
			}
		});
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
					dbService.getCaveTypebyID(ce.getCaveTypeID(), new AsyncCallback<CaveTypeEntry>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(CaveTypeEntry ctEntry) {
							caveSketchContainer.clear();
							caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(UriUtils.fromString("resource?background=" + ctEntry.getSketchName()))));
						}
					});
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
	 * Here the view is initialised. This is only done once at the beginning!
	 */
	private void initPanel() {
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Depiction Editor (ID = " + correspondingDepictionEntry.getDepictionID());

		FramedPanel attributePanel;

		TabPanel tabPanel = new TabPanel();
		tabPanel.setTabScroll(false);
		tabPanel.setSize("75%", "100%");

		HorizontalLayoutContainer hlContainer = new HorizontalLayoutContainer();
		hlContainer.setSize("100%", "100%");
		VerticalLayoutContainer vlContainer = new VerticalLayoutContainer();

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

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Located in Cave");
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
				dbService.getCaveTypebyID(event.getSelectedItem().getCaveTypeID(), new AsyncCallback<CaveTypeEntry>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(CaveTypeEntry ctEntry) {
						caveSketchContainer.clear();
						caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(UriUtils.fromString("resource?background=" + ctEntry.getSketchName()))));
					}
				});
			}
		});
		caveSelection.setToolTip("This field can only be changed until a depiction is allocated to a wall");
		// TODO check if wall id is set, then set caveSelection.editable(false)
		attributePanel.add(caveSelection);
//		attributePanel.setWidth("40%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Belongs to wall");
		attributePanel.add(new Label("Wall selection"));
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));
		
		HorizontalPanel dimPanel = new HorizontalPanel();
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Width");
		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthField.setWidth("60%");
		widthField.setValue(correspondingDepictionEntry.getWidth());
		attributePanel.add(widthField);
		dimPanel.add(attributePanel);
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Height");
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightField.setWidth("60%");
		heightField.setValue(correspondingDepictionEntry.getHeight());
		attributePanel.add(heightField);
		dimPanel.add(attributePanel);
		vlContainer.add(dimPanel, new VerticalLayoutData(1.0, .1));
//		dimPanel.setWidth("100%");

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Acquired by expedition");
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
//		expedSelection.setWidth("100%");
		attributePanel.add(expedSelection);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date of acquisition");
		dateOfAcquisitionField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		attributePanel.add(dateOfAcquisitionField);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Vendor");
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
//		vendorSelection.setWidth("100%");
		attributePanel.add(vendorSelection);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date purchased");
		purchaseDateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		purchaseDateField.setValue(correspondingDepictionEntry.getPurchaseDate());
		attributePanel.add(purchaseDateField);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Current location");
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Background colour");
		backgroundColourField = new TextField();
//		backgroundColourField.setWidth("100%");
		attributePanel.add(backgroundColourField);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Material");
		materialField = new TextField();
//		materialField.setWidth("100%");
		attributePanel.add(materialField);
//		attributePanel.setWidth("100%");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .1));
		hlContainer.add(vlContainer, new HorizontalLayoutData(.4, 1.0));
		
		vlContainer = new VerticalLayoutContainer();
//		vPanel.setWidth("60%");

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Wall");
		TextButton wallEditorButton = new TextButton("Position");
		wallEditor = new Walls(1, false);
		wallEditorButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				wallEditorDialog = new PopupPanel();
//				new Draggable(wallEditorDialog);
				wallEditorDialog.add(wallEditor);
				wallEditor.createNewDepictionOnWall(correspondingDepictionEntry, true, true);
				wallEditor.setPanel(wallEditorDialog);
				wallEditorDialog.setModal(true);
				wallEditorDialog.center();
				wallEditorDialog.show();
			}
		});
		attributePanel.addButton(wallEditorButton);
//		attributePanel.setButtonAlign(BoxLayoutPack.CENTER);

		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		caveSketchContainer = new FlowLayoutContainer();
//	SafeUri imageUri = UriUtils.fromString("resource?background=centralPillarCave.png");
//		caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(imageUri)));
//		caveSketchContainer.setSize("100%", "100%");
		attributePanel.add(caveSketchContainer);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0));
		hlContainer.add(vlContainer, new HorizontalLayoutData(.6, 1.0));
		tabPanel.add(hlContainer, "Basics");
	
		hlContainer = new HorizontalLayoutContainer();
		hlContainer.setSize("100%", "100%");
		
		vlContainer = new VerticalLayoutContainer();
//		vPanel.setSize("35%", "100%");
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Style");
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
//		styleSelection.setWidth(300);
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
		iconographySelector = new IconographySelector(new IconographySelectorListener() {
			
			@Override
			public void iconographySelected(IconographyEntry entry) {
				correspondingDepictionEntry.setIconographyID(entry.getIconographyID());
				iconographyLabel.setText(entry.getText());
				iconographySelectionDialog.hide();
			}
		});
		TextButton selectIconographyButton = new TextButton("select Iconography");
		selectIconographyButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				iconographySelectionDialog = new PopupPanel();
//				new Draggable(iconographySelectionDialog);
				iconographySelectionDialog.add(iconographySelector);
				iconographySelectionDialog.setModal(true);
				iconographySelectionDialog.center();
			}
		});
		attributePanel.addButton(selectIconographyButton);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .25));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Inscriptions");
		inscriptionsField = new TextField();
		inscriptionsField.setText(correspondingDepictionEntry.getInscriptions());
		inscriptionsField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingDepictionEntry.setInscriptions(event.getValue());
			}
		});
//		inscriptionsField.setWidth(130);
		attributePanel.add(inscriptionsField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Dating");
		datingField = new TextField();
		datingField.setWidth(130);
		datingField.setText(correspondingDepictionEntry.getDating());
		attributePanel.add(datingField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

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
//				new Draggable(imageSelectionDialog);
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

		hlContainer.add(vlContainer, new HorizontalLayoutData(.35, 1.0));
		
		vlContainer = new VerticalLayoutContainer();
//		vPanel.setSize("65%", "100%");
		
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
//		generalRemarksArea.setSize("100%", "33%");
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

		hlContainer = new HorizontalLayoutContainer();
		hlContainer.setSize("100%", "100%");
		FramedPanel depictionImagesPanel = new FramedPanel();
		depictionImagesPanel.setHeading("Images");
		depictionImagesPanel.add(lf);
		depictionImagesPanel.setSize("25%", "100%");
		depictionImagesPanel.addButton(addImageButton);
		depictionImagesPanel.addButton(removeImageButton);
		depictionImagesPanel.addButton(setMasterButton);
		
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
		hlContainer.add(attributePanel);
		tabPanel.add(hlContainer, "Pictorial Elements");

//		hPanel.add(vPanel);
//		tabPanel.add(hPanel, "Details");
		
    BorderLayoutData eastData = new BorderLayoutData(200);
    eastData.setMargins(new Margins(0, 5, 5, 5));
    eastData.setCollapsible(true);
    eastData.setCollapseHeaderVisible(true);

    MarginData centerData = new MarginData(0, 5, 5, 0);

    BorderLayoutContainer view = new BorderLayoutContainer();
    view.setBorders(true);
//    depictionImagesPanel.setWidth("25%");
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

	protected void saveDepictionEntry() {
		if (correspondingDepictionEntry.getDepictionID() == 0) {
			dbService.insertEntry(correspondingDepictionEntry.getInsertSql(), new AsyncCallback<Integer>() {
				
				@Override
				public void onSuccess(Integer newDepictionID) {
					correspondingDepictionEntry.setDepictionID(newDepictionID.intValue());
					insertDepictionImageRelations();
					insertDepictionPERelations();
				}
				
				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
			});
		} else {
			dbService.updateEntry(correspondingDepictionEntry.getUpdateSql(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(Boolean updateSucessful) {
				}
			});
			dbService.deleteEntry("DELETE FROM DepictionImageRelation WHERE DepictionID=" + correspondingDepictionEntry.getDepictionID(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(Boolean diRelationResult) {
					insertDepictionImageRelations();
				}
			});
			dbService.deleteEntry("DELETE FROM DepictionPERelation WHERE DepictionID=" + correspondingDepictionEntry.getDepictionID(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(Boolean dpeRelationResult) {
					insertDepictionPERelations();
				}
			});
		}
		
		Iterator<DepictionEditorListener> deIterator = listener.iterator();
		while (deIterator.hasNext()) {
			deIterator.next().depictionSaved(correspondingDepictionEntry);
		}
	}

	/**
	 * @return
	 */
	private boolean insertDepictionPERelations() {
		String insertSqlString = "INSERT INTO DepictionPERelation VALUES ";
		List<PictorialElementEntry> list = peSelector.getSelectedPE();
		if (list.isEmpty()) {
			return false;
		}
		Info.display("List<PictorialElementEntry>", "no. = " + list.size() + " first = " + list.get(0).getText());
		Iterator<PictorialElementEntry> it = list.iterator();
		while (it.hasNext()) {
			PictorialElementEntry entry = it.next();
			Info.display("entry", entry.getText());
			if (list.indexOf(entry) == 0) {
				insertSqlString = insertSqlString.concat("(" + correspondingDepictionEntry.getDepictionID() + ", " + entry.getPictorialElementID() + ")");
			} else {
				insertSqlString = insertSqlString.concat(", (" + correspondingDepictionEntry.getDepictionID() + ", " + entry.getPictorialElementID() + ")");
			}
		}
		dbService.insertEntry(insertSqlString, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer insertSqlStringResult) {
				// TODO Auto-generated method stub
			}
		});
		return true;
	}

	/**
	 * @return
	 */
	private boolean insertDepictionImageRelations() {
		String insertSqlString = "INSERT INTO DepictionImageRelation VALUES ";
		if (imageEntryList.size() == 0) {
			return false;
		}
		for (ImageEntry entry : imageEntryList.getAll()) {
			if (imageEntryList.indexOf(entry) == 0) {
				insertSqlString = insertSqlString.concat("(" + correspondingDepictionEntry.getDepictionID() + ", " + entry.getImageID() + ", " + true + ")");
			} else {
				insertSqlString = insertSqlString.concat(", (" + correspondingDepictionEntry.getDepictionID() + ", " + entry.getImageID() + ", " + false + ")");
			}
		}
		dbService.insertEntry(insertSqlString, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer insertSqlStringResult) {
				// TODO Auto-generated method stub
			}
		});
		return true;
	}

}