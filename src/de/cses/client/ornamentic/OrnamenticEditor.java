package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.bibliography.BibliographySelector;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;

public class OrnamenticEditor extends AbstractEditor implements ImageSelectorListener {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	FramedPanel header;
	private FramedPanel backgroundPanel;
	private VBoxLayoutContainer widget;
	FramedPanel cavesContentPanel;
	private OrnamentCaveRelationProperties ornamentCaveRelationProps;
	private ListStore<OrnamentCaveRelation> caveOrnamentRelationList;
	private ListView<OrnamentCaveRelation, String> cavesList;
	protected PopupPanel imageSelectionDialog;
	protected ImageSelector imageSelector;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ListStore<ImageEntry> imageEntryList;
	private ListStore<OrnamentClassEntry> ornamentClassEntryList;
	private ImageProperties imgProperties;
	private OrnamentEntry ornamentEntry = null;;
	private ComboBox<OrnamentClassEntry> ornamentClassComboBox;
	private OrnamentClassProperties ornamentClassProps;
	private InnerSecondaryPatternsProperties innerSecondaryPatternsProps;
	private ListStore<InnerSecondaryPatternsEntry> innerSecondaryPatternsEntryList;
	private ListStore<InnerSecondaryPatternsEntry> selectedinnerSecondaryPatternsEntryList;
	private ListStore<OrnamentComponentsEntry> ornamentComponents;
	private ListStore<OrnamentComponentsEntry> selectedOrnamentComponents;
	private OrnamentComponentsProperties ornamentComponentsProps;
	private TextField ornamentCodeTextField;
	private BibliographySelector bibSelector;
	private TextArea discription;
	private TextArea remarks;
	private TextArea interpretation;
	private TextArea references;
	public static OrnamentCaveRelationEditor ornamentCaveRelationEditor;
	public static WallOrnamentCaveRelationEditor wallOrnamentCaveRelationEditor;
	public static OrnamenticEditor ornamenticEditor;

	//Hauptklasse zum Erstellen von Ornamenten. Gegliedert in 3 Hierarchie Ebenen: 
	// 1. Eigenschaften die bei dem Ornament immer vorhanden sind
	// 2. Eigenschaften die von der H�hle abh�ngen in dem sich das Ornament befindet
	// 3. Eigenschaften, die von der Wand abh�ngen,an der sich das Ornament in einer bestimmten H�hle befindet
	
	// F�r die Suche: OrnamentSearchEntry
	// Die interne Repr�sentation f�r die Kommunikation mit dem Server: OrnamentEntry
	
	// Beim Erstellen des Client seitigen Aufbaus werden 2 F�lle unterschieden: Es wird ein OrnamentEntry geladen und
	// es werden die leeren Felder geladen um ein neues Ornament anzulegen
	
	// Die Speicherung in einen OrnamentEntry erfolgt erst beim Klicken des Save Buttons. Vorher werden keine Daten zwischengespeichert.
	
	// Viele der Eigenschaften der Ornamenten wurden inzwischen umbenannt (siehe OrnamentEntry f�r Kommentare zu den Eigenschafen)
	
	// 


	public OrnamenticEditor(OrnamentEntry ornamentEntry) {
		this.ornamentEntry = ornamentEntry;
	}
	@Override
	public Widget asWidget() {
		if (backgroundPanel == null) {

			createForm();
		}
		return backgroundPanel;
	}
	public Widget createForm() {
		// Aufbau der Listen welche geladen werden m�ssen aus der Datenbank
		Util.doLogging("Create form von ornamenticeditor gestartet");
		ornamentCaveRelationEditor = new OrnamentCaveRelationEditor();
		wallOrnamentCaveRelationEditor = new WallOrnamentCaveRelationEditor();
		ornamenticEditor = this;
		HorizontalLayoutContainer horizontBackground = new HorizontalLayoutContainer();
		VerticalLayoutContainer verticalgeneral2Background = new VerticalLayoutContainer();

		VerticalLayoutContainer verticalgeneral3Background = new VerticalLayoutContainer();

		imgProperties = GWT.create(ImageProperties.class);
		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		ornamentClassProps = GWT.create(OrnamentClassProperties.class);
		innerSecondaryPatternsProps = GWT.create(InnerSecondaryPatternsProperties.class);
		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
		ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);
		ornamentClassEntryList = new ListStore<OrnamentClassEntry>(ornamentClassProps.ornamentClassID());
		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(
				innerSecondaryPatternsProps.innerSecondaryPatternsID());
		selectedinnerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(
				innerSecondaryPatternsProps.innerSecondaryPatternsID());

		selectedOrnamentComponents = new ListStore<OrnamentComponentsEntry>(
				ornamentComponentsProps.ornamentComponentsID());
		ornamentComponents = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());

		// laden der Daten aus der Datenbank
		dbService.getOrnamentComponents(new AsyncCallback<ArrayList<OrnamentComponentsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			public void onSuccess(ArrayList<OrnamentComponentsEntry> result) {

				ornamentComponents.clear();
				selectedOrnamentComponents.clear();
				if (ornamentEntry != null) {
					for (OrnamentComponentsEntry pe : result) {
						int count = 0;
						for (OrnamentComponentsEntry oe : ornamentEntry.getOrnamentComponents()) {
							if (pe.getOrnamentComponentsID() != oe.getOrnamentComponentsID()) {
								count++;
							}
							if (count == ornamentEntry.getOrnamentComponents().size()) {
								ornamentComponents.add(pe);
							}
						}
					}
					for (OrnamentComponentsEntry oe : ornamentEntry.getOrnamentComponents()) {
						selectedOrnamentComponents.add(oe);
					}
					if (ornamentEntry.getOrnamentComponents().size() == 0) {
						for (OrnamentComponentsEntry nu : result) {
							ornamentComponents.add(nu);
						}
					}
				} else {
					for (OrnamentComponentsEntry pe : result) {
						ornamentComponents.add(pe);
					}
				}

			}
		});

		dbService.getInnerSecondaryPatterns(new AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<InnerSecondaryPatternsEntry> result) {

				innerSecondaryPatternsEntryList.clear();
				selectedinnerSecondaryPatternsEntryList.clear();
				if (ornamentEntry != null) {
					for (InnerSecondaryPatternsEntry pe : result) {
						int count = 0;
						for (InnerSecondaryPatternsEntry oe : ornamentEntry.getInnerSecondaryPatterns()) {
							if (pe.getInnerSecondaryPatternsID() != oe.getInnerSecondaryPatternsID()) {
								count++;
							}
							if (count == ornamentEntry.getInnerSecondaryPatterns().size()) {
								innerSecondaryPatternsEntryList.add(pe);
							}
						}
					}
					for (InnerSecondaryPatternsEntry oe : ornamentEntry.getInnerSecondaryPatterns()) {
						selectedinnerSecondaryPatternsEntryList.add(oe);
					}
					if (ornamentEntry.getInnerSecondaryPatterns().size() == 0) {
						for (InnerSecondaryPatternsEntry nu : result) {
							innerSecondaryPatternsEntryList.add(nu);
						}
					}
				} else {
					for (InnerSecondaryPatternsEntry pe : result) {
						innerSecondaryPatternsEntryList.add(pe);
					}
				}

			}
		});

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
				if (ornamentEntry != null) {
					ornamentClassComboBox.setValue(ornamentClassEntryList
							.findModelWithKey(Integer.toString(ornamentEntry.getOrnamentClass())));
				}
			}
		});

		// Aufbau der Felder auf der Client Seite
		TabPanel tabpanel = new TabPanel();
		tabpanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		//tabpanel.setWidth(620);
		//tabpanel.setHeight(600);

		VerticalLayoutContainer panel = new VerticalLayoutContainer();
		VerticalLayoutContainer panel2 = new VerticalLayoutContainer();

		ornamentCodeTextField = new TextField();
		ornamentCodeTextField.setAllowBlank(false);
		header = new FramedPanel();
		header.setHeading("Ornament Code");
		header.add(ornamentCodeTextField);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			ornamentCodeTextField.setValue(ornamentEntry.getCode());
		}
		ornamentClassComboBox = new ComboBox<OrnamentClassEntry>(ornamentClassEntryList, ornamentClassProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentClassEntry>() {

					@Override
					public SafeHtml render(OrnamentClassEntry item) {
						final OrnamentClassViewTemplates pvTemplates = GWT.create(OrnamentClassViewTemplates.class);
						return pvTemplates.ornamentClass(item.getName());
					}
				});

		header = new FramedPanel();
		header.setHeading("Motif");
		ornamentClassComboBox.setTriggerAction(TriggerAction.ALL);
		header.add(ornamentClassComboBox);
		panel.add(header, new VerticalLayoutData(1.0, .125));

		ToolButton addOrnamentClassButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addOrnamentClassButton.setToolTip(Util.createToolTip("Add Ornament Motif"));

		FramedPanel ornamentClassFramedPanel = new FramedPanel();
		ornamentClassFramedPanel.setHeading("New Ornament Motif");

		ToolButton saveOrnamentClass = new ToolButton(new IconConfig("safeButton", "safeButtonOver"));
		saveOrnamentClass.setToolTip(Util.createToolTip("save"));
		ornamentClassFramedPanel.add(saveOrnamentClass);

		ToolButton cancelOrnamentClass = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		cancelOrnamentClass.setToolTip(Util.createToolTip("cancel"));

		ornamentClassFramedPanel.addTool(cancelOrnamentClass);
		ornamentClassFramedPanel.addTool(saveOrnamentClass);

		HorizontalLayoutContainer newOrnamentClassLayoutPanel = new HorizontalLayoutContainer();
		TextField newOrnamentClassTextField = new TextField();
		newOrnamentClassLayoutPanel.add(newOrnamentClassTextField);
		ornamentClassFramedPanel.add(newOrnamentClassLayoutPanel);
		addOrnamentClassButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newOrnamentClassPopup = new PopupPanel();
				newOrnamentClassPopup.add(ornamentClassFramedPanel);
				ornamentClassFramedPanel.setSize("150", "80");
				newOrnamentClassPopup.center();
				cancelOrnamentClass.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newOrnamentClassPopup.hide();
					}
				});
				saveOrnamentClass.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						OrnamentClassEntry entry = new OrnamentClassEntry();
						entry.setName(newOrnamentClassTextField.getText());
						dbService.addOrnamentClass(entry, new AsyncCallback<OrnamentClassEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(OrnamentClassEntry result) {
								Util.doLogging(this.getClass().getName() + " added " + result.getName());
								ornamentClassEntryList.add(result);
								newOrnamentClassPopup.hide();
							}
						});
						newOrnamentClassPopup.hide();
					}
				});

			}
		});
		header.addTool(addOrnamentClassButton);

		ToolButton renameOrnamentClassButton = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		renameOrnamentClassButton
				.setToolTip(Util.createToolTip("Rename Ornament Motif", "Select item and click here."));

		FramedPanel renameornamentClassFramedPanel = new FramedPanel();
		renameornamentClassFramedPanel.setHeading("New Ornament Motif");

		ToolButton saveRenameOrnamentClass = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveRenameOrnamentClass.setToolTip(Util.createToolTip("save"));
		renameornamentClassFramedPanel.add(saveRenameOrnamentClass);

		ToolButton cancelRenameOrnamentClass = new ToolButton(new IconConfig("cancelButton", "cancelButtonOver"));
		cancelRenameOrnamentClass.setToolTip(Util.createToolTip("cancel"));

		renameornamentClassFramedPanel.addTool(cancelRenameOrnamentClass);
		renameornamentClassFramedPanel.addTool(saveRenameOrnamentClass);

		HorizontalLayoutContainer renameOrnamentClassLayoutPanel = new HorizontalLayoutContainer();
		TextField renameOrnamentClassTextField = new TextField();
		renameOrnamentClassLayoutPanel.add(renameOrnamentClassTextField);
		renameornamentClassFramedPanel.add(renameOrnamentClassLayoutPanel);

		renameOrnamentClassButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (ornamentClassComboBox.getValue() == null) {
					Window.alert("Please select an item to rename");
				} else {
					PopupPanel renameOrnamentClassPopup = new PopupPanel();
					renameOrnamentClassPopup.add(renameornamentClassFramedPanel);
					renameornamentClassFramedPanel.setSize("150", "80");
					renameOrnamentClassPopup.center();
					renameOrnamentClassTextField.setText(ornamentClassComboBox.getValue().getName());
					cancelRenameOrnamentClass.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							renameOrnamentClassPopup.hide();
						}
					});
					saveRenameOrnamentClass.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {

							OrnamentClassEntry entry = ornamentClassComboBox.getValue();
							entry.setName(renameOrnamentClassTextField.getText());
							dbService.renameOrnamentClass(entry, new AsyncCallback<OrnamentClassEntry>() {

								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(OrnamentClassEntry result) {
									Util.doLogging(this.getClass().getName() + " renamed " + result.getName());
									renameOrnamentClassPopup.hide();
								}
							});
							renameOrnamentClassPopup.hide();
						}
					});

				}
			}
		});

		header.addTool(renameOrnamentClassButton);

		header = new FramedPanel();
		header.setHeading("Description");
		discription = new TextArea();
		panel.add(header, new VerticalLayoutData(1.0, .3));
		header.add(discription);
		discription.setAllowBlank(true);
		if (ornamentEntry != null) {
			discription.setValue(ornamentEntry.getDescription());
		}

		remarks = new TextArea();
		remarks.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("General Remarks");
		header.add(remarks);
		panel.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			remarks.setValue(ornamentEntry.getRemarks());
		}

		interpretation = new TextArea();
		interpretation.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Interpretation");
		header.add(interpretation);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			interpretation.setValue(ornamentEntry.getInterpretation());
		}

		references = new TextArea();
		references.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("References");
		header.add(references);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			references.setText(ornamentEntry.getReferences());
		}

		ToolButton addCaveTool = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addCaveTool.setToolTip(Util.createToolTip("Add Cave"));

		VerticalPanel cavesPanel = new VerticalPanel();
		header = new FramedPanel();
		header.setHeading("Cave");

		header.add(cavesPanel);
		panel2.add(header, new VerticalLayoutData(1.0, 1.0));

		ClickHandler addCaveClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ornamentCaveRelationEditor.show();
			}
		};

		addCaveTool.addHandler(addCaveClickHandler, ClickEvent.getType());

		cavesContentPanel = new FramedPanel();

		cavesPanel.add(cavesContentPanel);
		caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(
				ornamentCaveRelationProps.ornamentCaveRelationID());

		cavesList = new ListView<OrnamentCaveRelation, String>(caveOrnamentRelationList,
				ornamentCaveRelationProps.name());
		cavesList.setAllowTextSelection(true);

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getCavesRelations().size(); i++) {
				caveOrnamentRelationList.add(ornamentEntry.getCavesRelations().get(i));
			}
		}

		cavesContentPanel.setHeading("Added caves");
		cavesContentPanel.add(cavesList);

		ToolButton edit = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		edit.setToolTip(Util.createToolTip("Edit Cave"));

		ToolButton delete = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		delete.setToolTip(Util.createToolTip("Delete Cave.", "Select cave, then click here."));

		cavesContentPanel.addTool(addCaveTool);
		cavesContentPanel.addTool(edit);
		cavesContentPanel.addTool(delete);

		ClickHandler deleteClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				caveOrnamentRelationList.remove(cavesList.getSelectionModel().getSelectedItem());

			}
		};
		delete.addHandler(deleteClickHandler, ClickEvent.getType());

		ClickHandler editClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ornamentCaveRelationEditor.show(cavesList.getSelectionModel().getSelectedItem());
			}
		};
		edit.addHandler(editClickHandler, ClickEvent.getType());

		ClickHandler saveClickHandler = new ClickHandler() {
			// Sammeln der Informationen aus den Feldern und speicherung des OrnamentEntrys

			@Override
			public void onClick(ClickEvent event) {
				save();

			} // end
		};


		ClickHandler cancelHandler = new ClickHandler() {
			// R�ckfrage �ber schlie�en ohne zu speichern

			@Override
			public void onClick(ClickEvent event) {
				
				Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						save();
						closeEditor(null);
					}
				}, new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						closeEditor(null);
					}
			
					
				}
				, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
						save();
						closeEditor(null);
					}}
			
					
				});
				
//				
//				PopupPanel security = new PopupPanel();
//				ContentPanel securityContent = new ContentPanel();
//				VerticalLayoutContainer verticalPanel= new VerticalLayoutContainer();
//				securityContent.add(verticalPanel);
//				TextButton yesTB = new TextButton("yes");
//				TextButton noTB = new TextButton("no");
//				ButtonBar buttons = new ButtonBar();
//				buttons.add(yesTB);
//				buttons.add(noTB);
//
//				HTML text = new HTML("Really exit without saving? All unsaved data will be lost.");
//				verticalPanel.add(text);
//				verticalPanel.add(buttons);
//				security.setWidget(securityContent);
//				security.center();

//				ClickHandler yesHandler = new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						security.hide();
//						closeEditor(null);
//
//					}
//				};
//				yesTB.addHandler(yesHandler, ClickEvent.getType());

//				ClickHandler noHandler = new ClickHandler() {
//
//					@Override
//					public void onClick(ClickEvent event) {
//						security.hide();
//					}
//				};
//				noTB.addHandler(noHandler, ClickEvent.getType());

			}

		};

		horizontBackground.add(panel, new HorizontalLayoutData(.5, 1.0));
		horizontBackground.add(panel2, new HorizontalLayoutData(.5, 1.0));
		ScrollPanel scrframedpanelornamentic = new ScrollPanel();
		horizontBackground.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));

		scrframedpanelornamentic.add(horizontBackground);

		tabpanel.add(scrframedpanelornamentic, "1. General");

		ScrollPanel scrgeneral2FramedPanel = new ScrollPanel();
		verticalgeneral2Background.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		
		scrgeneral2FramedPanel.add(verticalgeneral2Background);
		tabpanel.add(scrgeneral2FramedPanel, "2. General");

		ScrollPanel scrgeneral3FramedPanel = new ScrollPanel();
		verticalgeneral3Background.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		scrgeneral3FramedPanel.add(verticalgeneral3Background);
		tabpanel.add(scrgeneral3FramedPanel, "3. General");

		HorizontalLayoutContainer ornamentComponentsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrnamentComponentsEntry, String> ornamentComponentView = new ListView<OrnamentComponentsEntry, String>(
				ornamentComponents, ornamentComponentsProps.name());
		ListView<OrnamentComponentsEntry, String> selectedOrnamentComponentView = new ListView<OrnamentComponentsEntry, String>(
				selectedOrnamentComponents, ornamentComponentsProps.name());
		ornamentComponentsHorizontalPanel.add(ornamentComponentView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		ornamentComponentsHorizontalPanel.add(selectedOrnamentComponentView,
				new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");
		new ListViewDragSource<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");

		new ListViewDropTarget<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");
		new ListViewDropTarget<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");

		header = new FramedPanel();
		header.setHeading("Select Ornament Components");
		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getOrnamentComponents().size(); i++) {
				selectedOrnamentComponents.add(ornamentEntry.getOrnamentComponents().get(i));

			}
		}
		header.add(ornamentComponentsHorizontalPanel);

		verticalgeneral3Background.add(header, new VerticalLayoutData(1.0, .3));

		ToolButton addComponentButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addComponentButton.setToolTip(Util.createToolTip("New Component"));
		header.addTool(addComponentButton);

		addComponentButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newComponentPopup = new PopupPanel();
				FramedPanel componentFramedPanel = new FramedPanel();
				componentFramedPanel.setHeading("New Component");

				ToolButton saveComponent = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveComponent.setToolTip(Util.createToolTip("save"));
				componentFramedPanel.addTool(saveComponent);

				ToolButton cancelComponent = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelComponent.setToolTip(Util.createToolTip("cancel"));

				componentFramedPanel.addTool(cancelComponent);

				HorizontalLayoutContainer newComponentLayoutPanel = new HorizontalLayoutContainer();
				TextField newComponentTextField = new TextField();
				newComponentLayoutPanel.add(newComponentTextField);
				componentFramedPanel.add(newComponentLayoutPanel);

				newComponentPopup.add(componentFramedPanel);
				newComponentPopup.setSize("150px", "80px");
				newComponentPopup.center();

				cancelComponent.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newComponentPopup.hide();
					}
				});
				saveComponent.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						OrnamentComponentsEntry entry = new OrnamentComponentsEntry();
						entry.setName(newComponentTextField.getText());

						dbService.addOrnamentComponent(entry, new AsyncCallback<OrnamentComponentsEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(OrnamentComponentsEntry result) {
								ornamentComponents.add(entry);
								Util.doLogging(this.getClass().getName() + " saving sucessful");
								newComponentPopup.hide();
							}
						});
					}
				});
			}
		});

		ToolButton renameComponentButton = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		renameComponentButton
				.setToolTip(Util.createToolTip("Rename Component", "Select entry and click here to edit."));

		header.addTool(renameComponentButton);
		renameComponentButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (ornamentComponentView.getSelectionModel().getSelectedItem() == null) {
					Window.alert("Please select an item to rename");

				} else {
					PopupPanel renameComponentPopup = new PopupPanel();
					FramedPanel renamecomponentFramedPanel = new FramedPanel();
					renamecomponentFramedPanel.setHeading("Rename Component");

					ToolButton saveRenameComponent = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
					saveRenameComponent.setToolTip(Util.createToolTip("save"));
					renamecomponentFramedPanel.addTool(saveRenameComponent);

					ToolButton cancelRenameComponent = new ToolButton(
							new IconConfig("closeButton", "closeButtonOver"));
					cancelRenameComponent.setToolTip(Util.createToolTip("cancel"));

					renamecomponentFramedPanel.addTool(cancelRenameComponent);

					HorizontalLayoutContainer renameComponentLayoutPanel = new HorizontalLayoutContainer();
					TextField renameComponentTextField = new TextField();
					renameComponentLayoutPanel.add(renameComponentTextField);
					renamecomponentFramedPanel.add(renameComponentLayoutPanel);

					renameComponentPopup.add(renamecomponentFramedPanel);
					renameComponentPopup.setSize("150px", "80px");
					renameComponentPopup.center();

					renameComponentTextField
							.setText(ornamentComponentView.getSelectionModel().getSelectedItem().getName());

					cancelRenameComponent.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							renameComponentPopup.hide();
						}
					});
					saveRenameComponent.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							OrnamentComponentsEntry entry = new OrnamentComponentsEntry();
							entry.setName(renameComponentTextField.getText());

							dbService.renameOrnamentComponents(entry, new AsyncCallback<OrnamentComponentsEntry>() {

								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(OrnamentComponentsEntry result) {
									Util.doLogging(this.getClass().getName() + " renaming sucessful");
									renameComponentPopup.hide();
								}
							});
						}
					});
				}
			}
		});

		HorizontalLayoutContainer innerSecondaryPatternsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<InnerSecondaryPatternsEntry, String> innerSecondaryPatternsView = new ListView<InnerSecondaryPatternsEntry, String>(
				innerSecondaryPatternsEntryList, innerSecondaryPatternsProps.name());
		ListView<InnerSecondaryPatternsEntry, String> selectedinnerSecondaryPatternsView = new ListView<InnerSecondaryPatternsEntry, String>(
				selectedinnerSecondaryPatternsEntryList, innerSecondaryPatternsProps.name());
		innerSecondaryPatternsHorizontalPanel.add(innerSecondaryPatternsView,
				new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		innerSecondaryPatternsHorizontalPanel.add(selectedinnerSecondaryPatternsView,
				new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<InnerSecondaryPatternsEntry>(innerSecondaryPatternsView).setGroup("innersec");
		new ListViewDragSource<InnerSecondaryPatternsEntry>(selectedinnerSecondaryPatternsView).setGroup("innersec");

		new ListViewDropTarget<InnerSecondaryPatternsEntry>(selectedinnerSecondaryPatternsView).setGroup("innersec");
		new ListViewDropTarget<InnerSecondaryPatternsEntry>(innerSecondaryPatternsView).setGroup("innersec");

		header = new FramedPanel();
		header.setHeading("Select inner Secondary Patterns");
		ToolButton addInnerSecondaryPatternsButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addInnerSecondaryPatternsButton.setToolTip(Util.createToolTip("Add New Inner Secondary Pattern"));

		addInnerSecondaryPatternsButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newInnerSecondaryPatternPopup = new PopupPanel();
				FramedPanel innersecFramedPanel = new FramedPanel();
				innersecFramedPanel.setHeading("New Inner Secondary Pattern");

				ToolButton saveInnerSec = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveInnerSec.setToolTip(Util.createToolTip("save"));
				innersecFramedPanel.add(saveInnerSec);

				ToolButton cancelInnerSec = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelInnerSec.setToolTip(Util.createToolTip("cancel"));

				innersecFramedPanel.addTool(cancelInnerSec);
				innersecFramedPanel.addTool(saveInnerSec);

				HorizontalLayoutContainer newinnersecLayoutPanel = new HorizontalLayoutContainer();
				TextField newinnersecTextField = new TextField();
				newinnersecLayoutPanel.add(newinnersecTextField);
				innersecFramedPanel.add(newinnersecLayoutPanel);
				newInnerSecondaryPatternPopup.add(innersecFramedPanel);
				innersecFramedPanel.setSize("150", "80");
				newInnerSecondaryPatternPopup.center();
				cancelInnerSec.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newInnerSecondaryPatternPopup.hide();
					}
				});
				saveInnerSec.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						InnerSecondaryPatternsEntry entry = new InnerSecondaryPatternsEntry();
						entry.setName(newinnersecTextField.getText());
						dbService.addInnerSecondaryPatterns(entry, new AsyncCallback<InnerSecondaryPatternsEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(InnerSecondaryPatternsEntry result) {
								Util.doLogging(this.getClass().getName() + "saving sucessful");
								innerSecondaryPatternsEntryList.add(result);
								newInnerSecondaryPatternPopup.hide();
							}
						});
						newInnerSecondaryPatternPopup.hide();
					}
				});
			}
		});

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getInnerSecondaryPatterns().size(); i++) {
				selectedinnerSecondaryPatternsEntryList.add(ornamentEntry.getInnerSecondaryPatterns().get(i));

				
			}
		}
		header.add(innerSecondaryPatternsHorizontalPanel);
		header.addTool(addInnerSecondaryPatternsButton);

		verticalgeneral3Background.add(header, new VerticalLayoutData(1.0, .3));

		FramedPanel imagesFramedPanel = new FramedPanel();
		imagesFramedPanel.setHeading("Images");

		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getImages().size(); i++) {
				imageEntryList.add(ornamentEntry.getImages().get(i));
			}
		}

		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=150"
						+ UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				return imageViewTemplates.image(imageUri, item.getTitle());
			}
		}));

		imageListView.setSize("500", "290");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("500", "300");

		imageSelector = new ImageSelector(this);
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

		VerticalPanel imagesVerticalPanel = new VerticalPanel();
		imagesVerticalPanel.add(lf);
		HorizontalPanel hbp = new HorizontalPanel();
		hbp.add(addImageButton);
		hbp.add(removeImageButton);
		imagesVerticalPanel.add(hbp);
		imagesFramedPanel.add(imagesVerticalPanel);
		imagesFramedPanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		
		ScrollPanel scrimagesFramedPanel = new ScrollPanel();
		scrimagesFramedPanel.add(imagesFramedPanel);
		tabpanel.add(scrimagesFramedPanel, "Images");
		tabpanel.setTabScroll(true);

		ToolButton closeButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeButton.setToolTip(Util.createToolTip("close"));
		ToolButton saveButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveButton.setToolTip(Util.createToolTip("save"));
		closeButton.addHandler(cancelHandler, ClickEvent.getType());
		saveButton.addHandler(saveClickHandler, ClickEvent.getType());
		
		if (ornamentEntry!=null) {
			bibSelector = new BibliographySelector(ornamentEntry.getRelatedBibliographyList());
		} else {
			bibSelector = new BibliographySelector(new ArrayList<AnnotatedBibliographyEntry>());
		}
		
		tabpanel.add(bibSelector, "Related Bibliography");

		backgroundPanel = new FramedPanel();
		
		backgroundPanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		backgroundPanel.add(tabpanel);
		backgroundPanel.setHeading("Ornamentation Editor");
		backgroundPanel.addTool(saveButton);
		backgroundPanel.addTool(closeButton);
		backgroundPanel.addDomHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent e) {
	        	  if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
						save();
						closeEditor(null);		        }
		    }			
		}, KeyDownEvent.getType());
		new Resizable(backgroundPanel);
		new Draggable(backgroundPanel);
		return backgroundPanel;

	}

	protected void save() {
		if (!ornamentCodeTextField.validate()) {
			Util.showWarning("Missing information", "Please insert Ornamentation Code!");
			return;
		}

		if (ornamentEntry == null) {
			ornamentEntry = new OrnamentEntry();
		}

		ArrayList<OrnamentCaveRelation> corList = new ArrayList<OrnamentCaveRelation>();
		for (int i = 0; i < caveOrnamentRelationList.size(); i++) {
			corList.add(caveOrnamentRelationList.get(i));
		}
		ornamentEntry.setCavesRelations(corList);

		ArrayList<ImageEntry> ieList = new ArrayList<ImageEntry>();
		for (int i = 0; i < imageEntryList.size(); i++) {
			ieList.add(imageEntryList.get(i));
		}
		ornamentEntry.setImages(ieList);

		ornamentEntry.setCode(ornamentCodeTextField.getText());
		ornamentEntry.setDescription(discription.getText());
		ornamentEntry.setRemarks(remarks.getText());
		ornamentEntry.setInterpretation(interpretation.getText());
		ornamentEntry.setReferences(references.getText());
		if (ornamentClassComboBox.getValue() == null) {
			ornamentEntry.setOrnamentClass(0);
		} else {
			Util.doLogging("ID gesetzt");
			ornamentEntry.setOrnamentClass(ornamentClassComboBox.getValue().getOrnamentClassID());
		}

		ArrayList<InnerSecondaryPatternsEntry> ispeList = new ArrayList<InnerSecondaryPatternsEntry>();
		for (int i = 0; i < selectedinnerSecondaryPatternsEntryList.size(); i++) {
			ispeList.add(selectedinnerSecondaryPatternsEntryList.get(i));
		}
		ornamentEntry.setInnerSecondaryPatterns(ispeList);

		ArrayList<OrnamentComponentsEntry> oceList = new ArrayList<OrnamentComponentsEntry>();
		for (int i = 0; i < selectedOrnamentComponents.size(); i++) {
			oceList.add(selectedOrnamentComponents.get(i));
		}
		ornamentEntry.setOrnamentComponents(oceList);
		ornamentEntry.setRelatedBibliographyList(bibSelector.getSelectedEntries());

		// send ornament to server
		if (ornamentEntry.getOrnamentID() == 0) {
			dbService.saveOrnamentEntry(ornamentEntry, new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					Util.showWarning("Saving failed", caught.getMessage());
				}

				@Override
				public void onSuccess(Integer result) {
					if (result > 0) {
						Util.doLogging(this.getClass().getName() + " saving sucessful");
						ornamentEntry.setOrnamentID(result);
						// updateEntry(ornamentEntry);
						closeEditor(ornamentEntry);
					} else {
						Util.showWarning("Saving failed", "ornamentID == 0");
					}
				}
			});
		} else {
			dbService.updateOrnamentEntry(ornamentEntry, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Util.showWarning("Update failed", caught.getMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					closeEditor(ornamentEntry);
				}
			});
		}
	}

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();
	}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);

	}

	interface OrnamentClassViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentClass(String name);
	}

	interface OrnamentClassProperties extends PropertyAccess<OrnamentClassEntry> {
		ModelKeyProvider<OrnamentClassEntry> ornamentClassID();

		LabelProvider<OrnamentClassEntry> name();
	}

	interface OrnamentComponentsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<OrnamentComponentsEntry> ornamentComponentsID();

		ValueProvider<OrnamentComponentsEntry, String> name();
	}

	interface InnerSecondaryPatternsProperties extends PropertyAccess<InnerSecondaryPatternsEntry> {
		ModelKeyProvider<InnerSecondaryPatternsEntry> innerSecondaryPatternsID();

		ValueProvider<InnerSecondaryPatternsEntry, String> name();
	}

	interface OrnamentCaveRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<OrnamentCaveRelation> ornamentCaveRelationID();

		ValueProvider<OrnamentCaveRelation, String> name();
	}

	public ListStore<OrnamentCaveRelation> getCaveOrnamentRelationList() {
		return caveOrnamentRelationList;
	}

	public void setCaveOrnamentRelationList(ListStore<OrnamentCaveRelation> caveOrnamentRelationList) {
		this.caveOrnamentRelationList = caveOrnamentRelationList;
	}

	public ListView<OrnamentCaveRelation, String> getCavesList() {
		return cavesList;
	}

	public void setCavesList(ListView<OrnamentCaveRelation, String> cavesList) {
		this.cavesList = cavesList;
	}

	@Override
	public void imageSelected(ArrayList<ImageEntry> entryList) {
		for (ImageEntry ie : entryList) {
			imageEntryList.add(ie);
		}
		imageSelectionDialog.hide();
	}

}
