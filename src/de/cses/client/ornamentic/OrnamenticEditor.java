package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
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
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.fx.client.Draggable.DraggableAppearance;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeAddEvent;
import com.sencha.gxt.widget.core.client.event.BeforeAddEvent.BeforeAddHandler;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.EnableEvent;
import com.sencha.gxt.widget.core.client.event.EnableEvent.EnableHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.event.ShowEvent.ShowHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.bibliography.BibliographySelector;
import de.cses.client.depictions.DepictionView;
import de.cses.client.depictions.IconographySelector;
import de.cses.client.depictions.IconographySelectorListener;
import de.cses.client.depictions.ImageViewTemplates;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.OSDListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.ui.TextElement;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.ExternalRessourceEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.ModifiedEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;

public class OrnamenticEditor extends AbstractEditor implements ImageSelectorListener {
	protected IconographySelector iconographySelector;
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
	private DialogBox showTreeEdit = new DialogBox();
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ListStore<ImageEntry> imageEntryLS;
	private ListStore<OrnamentClassEntry> ornamentClassEntryList;
	private ImageProperties imgProperties;
	private OrnamentEntry ornamentEntry = null;;
	private Map<Integer,String> imgdic;
	private ComboBox<OrnamentClassEntry> ornamentClassComboBox;
	private OrnamentClassProperties ornamentClassProps;
	private InnerSecondaryPatternsProperties innerSecondaryPatternsProps;
	private ListStore<InnerSecondaryPatternsEntry> innerSecondaryPatternsEntryList;
	private ListStore<InnerSecondaryPatternsEntry> selectedinnerSecondaryPatternsEntryList;
	private ListStore<OrnamentComponentsEntry> ornamentComponents;
	private ListStore<OrnamentComponentsEntry> selectedOrnamentComponents;
	private OrnamentComponentsProperties ornamentComponentsProps;
	private TextField ornamentCodeTextField;
	private NumberField tourOrderField;
	private BibliographySelector bibSelector;
	private TextArea discription;
	private TextArea remarks;
	private TextArea interpretation;
	private TextArea references;
	private FramedPanel ftree;
	private FramedPanel ftreeedit;
	private OrnamenticIconographyTree ornamentTrees;
	private ToolButton saveButton;
	private ArrayList<IconographyEntry> iconographyRelationList;
	private OSDLoader osdLoader;
	private boolean annotationsLoaded = true;
	private OSDListener osdListener;
	private IconographySelectorListener icoSelectorListener;
	private CheckBox isVirtualTour;
	private FramedPanel tourOrder;
	private FramedPanel isVirtualTourFP;
	private FramedPanel accessRightsCBFP;
	private SimpleComboBox<String> accessRightsCB;
	private HorizontalLayoutContainer horizonttourData;
	
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

	public OrnamenticEditor(OrnamentEntry ornamentEntry, EditorListener av) {
		this.addEditorListener(av);
		this.ornamentEntry = ornamentEntry;

	}
	@Override
	public Widget asWidget() {
		if (backgroundPanel == null) {

			createForm();
		}
		return backgroundPanel;
	}

	private void reloadPics() {
		imageListView.getStore().clear();
		osdLoader.destroyAllViewers();
		loadImages();
		setosd();
	}
	private void setosd() {
		dbService.getOSDContext(new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(String result) {
				if (ornamentEntry != null) {
					osdLoader.startLoadingTiles(result);					
				}
			}
		});
	}

	@Override
	protected void loadModifiedEntries() {
		sourceStore.clear();
	    dbService.getModifiedAbstractEntry((AbstractEntry)ornamentEntry, new AsyncCallback<ArrayList<ModifiedEntry>>() {
			
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
	public void loadiconogrpahy(ArrayList<IconographyEntry> iconographyRelationList) {
		this.iconographyRelationList = iconographyRelationList;
		iconographySelector.setSelectedIconography(iconographyRelationList);
		iconographySelector.IconographyTreeEnabled(true);
		getListenerList().get(0).setClickNumber(0);
}
	// this method should be added to osdLader
	private ArrayList<AnnotationEntry> findAllIcos(IconographyEntry selectedIE, ArrayList<AnnotationEntry> collectedEntries) {
		for (AnnotationEntry ae : ornamentEntry.getRelatedAnnotationList()) {
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
						Util.doLogging("found entry for: "+selectedIE.getText());
						collectedEntries.add(ae);									
					}
				}
			}
		}
		if (selectedIE.getChildren() != null) {
			for (IconographyEntry child : selectedIE.getChildren()) {
				collectedEntries = findAllIcos(child, collectedEntries);
			}
		}
		return collectedEntries;
	}

	private void highlightIcoEntry(IconographyEntry selectedIE, boolean deselect, List<IconographyEntry>clickedIcos) {
		Util.doLogging("Started highlighting for Iconography: "+selectedIE.getText());
		ArrayList<AnnotationEntry> newAnnos = new ArrayList<AnnotationEntry>();
		newAnnos = findAllIcos(selectedIE, newAnnos);	
		
		if (newAnnos.size()>0) {
			if (annotationsLoaded) {
				osdLoader.removeOrAddAnnotations(newAnnos,!deselect);
			}				
		}
		for (AnnotationEntry aeSelected : newAnnos) {
			if (deselect) {
				osdLoader.deHighlightAnnotation(aeSelected.getAnnotoriousID());
			}
			else {
				osdLoader.highlightAnnotation(aeSelected.getAnnotoriousID());
			}
			
		}
	}

	public Widget createForm() {
		if (ornamentEntry == null) {
			Util.doLogging("do create new OE");
			ornamentEntry = new OrnamentEntry();
		}
		ornamentTrees= new 	OrnamenticIconographyTree(ornamentEntry);
		ornamentTrees.setDialogboxnotcalled(false);
		// Aufbau der Listen welche geladen werden m�ssen aus der Datenbank
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
		//imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
		ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);
		ornamentClassEntryList = new ListStore<OrnamentClassEntry>(ornamentClassProps.ornamentClassID());
		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(
				innerSecondaryPatternsProps.innerSecondaryPatternsID());
		selectedinnerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(
				innerSecondaryPatternsProps.innerSecondaryPatternsID());

		selectedOrnamentComponents = new ListStore<OrnamentComponentsEntry>(
				ornamentComponentsProps.ornamentComponentsID());
		ornamentComponents = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());
		icoSelectorListener = new IconographySelectorListener() {

			@Override
			public void icoHighlighter(int icoID) {
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
						getListenerList().get(0), true, ornamentEntry.getRelatedAnnotationList(),
						icoSelectorListener);
			}
			public void reloadOSD() {
				reloadPics();
			}

			@Override
			public void reduceTree() {
				// TODO Auto-generated method stub
				
			}
		};
		
		//iconographySelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values());
		//loadiconogrpahy(ornamentEntry.getRelatedIconographyList());
		ArrayList<AnnotationEntry> annos;
		if (ornamentEntry!=null) {
			annos = ornamentEntry.getRelatedAnnotationList();			
		} else {
			annos = new ArrayList<AnnotationEntry>();
		}
		iconographySelector = new IconographySelector(StaticTables.getInstance().getIconographyEntries().values(),
				getListenerList().get(0), true, annos,
				icoSelectorListener);
		if (ornamentEntry!=null) {
			loadiconogrpahy(ornamentEntry.getRelatedIconographyList());
		} else {
			loadiconogrpahy(new ArrayList<IconographyEntry>());
		}

		osdListener = new OSDListener() {

			@Override
			public void setAnnotationsInParent(ArrayList<AnnotationEntry> relatedAnnotationList) {
				ornamentEntry.setRelatedAnnotationList(relatedAnnotationList);
				iconographySelector.setRelatedAnnotationList(relatedAnnotationList);
				
			};

			@Override
			public int getDepictionID() {
				return ornamentEntry.getTypicalID();
			}
			@Override
			public boolean isOrnament() {
				return true;
			}

			@Override
			public ArrayList<AnnotationEntry> getAnnotations() {
				// TODO Auto-generated method stub
				if (ornamentEntry!=null) {
					return ornamentEntry.getRelatedAnnotationList();			
				} else {
					return new ArrayList<AnnotationEntry>();
				}
			}

			@Override
			public void addAnnotation(AnnotationEntry ae) {
				ornamentEntry.addAnnotation(ae);
				for (EditorListener el : getListenerList()) {
					if (el instanceof OrnamenticView) {
						((OrnamenticView) el).setOrnamentEntry(ornamentEntry);
					}
				}

				
			};
			

		};
		osdLoader = new OSDLoader(ornamentEntry.getImages(), true,
				iconographySelector.getIconographyStore(),
				osdListener);
		/**
		 * ---------------------- content of fourth tab (Bibliography Selector)
		 * ---------------------
		 */

		ftree = new FramedPanel();
		ftreeedit = new FramedPanel();
		ftreeedit.add(ornamentTrees.getIcoTree());
		ftreeedit.setSize("400","400");
		ToolButton cancelTreeEdit = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		cancelTreeEdit.setToolTip(Util.createToolTip("close"));
		cancelTreeEdit.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				showTreeEdit.hide();
				ornamentTrees.refreshTrees();
			}
		});
		ftreeedit.addTool(cancelTreeEdit);
		showTreeEdit.add(ftreeedit);
//		ftree.add(selectedIconographyTree);
		ToolButton changetree = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		changetree.setToolTip(Util.createToolTip("Change Treeentry.", "Select another Treeentry."));
		changetree.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				getListenerList().get(0).addClickNumber();
				showTreeEdit.setSize("400", "400");
				showTreeEdit.center();
			}
		});
		ftree.setHeading("Typical Entry");
		ftree.addTool(changetree);
		ftree.add(ornamentTrees.getSelectedIcoTree());
		// laden der Daten aus der Datenbank
		Util.doLogging("Create form von ornamenticeditor gestartet");
		if (ornamentEntry!=null) {
			if (ornamentEntry.getTypicalID() > 0){
				dbService.getOrnamentEntry(ornamentEntry.getTypicalID(), new AsyncCallback<OrnamentEntry>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					public void onSuccess(OrnamentEntry result) {
						ornamentEntry=result;
						ornamentCodeTextField.setValue(ornamentEntry.getCode());
						discription.setValue(ornamentEntry.getDescription());
						remarks.setValue(ornamentEntry.getRemarks());
						ornamentTrees.setTreeStore();
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
									}
								} else {
									for (OrnamentComponentsEntry pe : result) {
										ornamentComponents.add(pe);
									}
								}

							}
						});
					}
					});				
			}

		}


		// Aufbau der Felder auf der Client Seite
		TabPanel tabpanel = new TabPanel();
		tabpanel.setSize( Integer.toString(Window.getClientWidth()/100*95),Integer.toString(Window.getClientHeight()/100*95));
		//tabpanel.setWidth(620);
		//tabpanel.setHeight(600);

		VerticalLayoutContainer panel = new VerticalLayoutContainer();
		VerticalLayoutContainer panel2 = new VerticalLayoutContainer();
		accessRightsCB = new SimpleComboBox<String>(new LabelProvider<String>() {

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
		accessRightsCB.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				Util.doLogging("changing value: "+event.getValue());
				ornamentEntry.setAccessLevel(accessRightsCB.getSelectedIndex());
				if (event.getValue().equals(AbstractEntry.ACCESS_LEVEL_LABEL.get(2))) {
					isVirtualTour.setEnabled(true);
					tourOrderField.setEnabled(false);
				} else {
					isVirtualTour.setEnabled(false);
					tourOrderField.setEnabled(false);
					ornamentEntry.setIsVirtualTour(false);
				}
			}
		});
		isVirtualTour = new CheckBox();
		isVirtualTour.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
					tourOrder.setEnabled(event.getValue());
					
			}
			
		});
		accessRightsCBFP = new FramedPanel();
		accessRightsCBFP.setHeading("Access Level");
		accessRightsCBFP.add(accessRightsCB);
		isVirtualTourFP = new FramedPanel();
		isVirtualTourFP.setHeading("Show in Virtual Tour");
		isVirtualTourFP.add(isVirtualTour);
		tourOrderField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		tourOrderField.setValue(ornamentEntry.getVirtualTourOrder());
		tourOrder = new FramedPanel();
		tourOrder.setHeading("Virtual Tour Order");
		tourOrder.add(tourOrderField);
		horizonttourData = new HorizontalLayoutContainer();
		horizonttourData.add(accessRightsCBFP, new HorizontalLayoutData(.4, 1.0));
		horizonttourData.add(isVirtualTourFP, new HorizontalLayoutData(.3, 1.0));
		horizonttourData.add(tourOrder, new HorizontalLayoutData(.3, 1.0));
		
		panel.add(horizonttourData, new VerticalLayoutData(1.0, .125));

		ornamentCodeTextField = new TextField();
		ornamentCodeTextField.setAllowBlank(false);
		header = new FramedPanel();
		header.setHeading("Typical Code / Short Name");
		header.add(ornamentCodeTextField);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		accessRightsCB.setValue(AbstractEntry.ACCESS_LEVEL_LABEL.get(ornamentEntry.getAccessLevel()), true);
		if (accessRightsCB.getValue().equals(AbstractEntry.ACCESS_LEVEL_LABEL.get(2))) {
			isVirtualTour.setValue(ornamentEntry.getisVirtualTour(), true);
		}

		ornamentClassComboBox = new ComboBox<OrnamentClassEntry>(ornamentClassEntryList, ornamentClassProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentClassEntry>() {

					@Override
					public SafeHtml render(OrnamentClassEntry item) {
						final OrnamentClassViewTemplates pvTemplates = GWT.create(OrnamentClassViewTemplates.class);
						return pvTemplates.ornamentClass(item.getName());
					}
				});
		panel.add(ftree, new VerticalLayoutData(1.0, 0.275));
		panel.add(horizonttourData, new VerticalLayoutData(1.0, .125));
		header = new FramedPanel();
		header.setHeading("Motif");
		ornamentClassComboBox.setTriggerAction(TriggerAction.ALL);
		header.add(ornamentClassComboBox);
		//header.add(iconographySelector);
		//panel.add(header, new VerticalLayoutData(1.0, .125));
		ToolButton addOrnamentClassButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addOrnamentClassButton.setToolTip(Util.createToolTip("Add Ornament Motif"));

		FramedPanel ornamentClassFramedPanel = new FramedPanel();
		ornamentClassFramedPanel.setHeading("New Ornament Motif");

		ToolButton saveOrnamentClass = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveOrnamentClass.setToolTip(Util.createToolTip("save"));
		ornamentClassFramedPanel.add(saveOrnamentClass);
		Util.doLogging("Create form von ornamenticeditor gestartet");

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
								//Util.doLogging(this.getClass().getName() + " added " + result.getName());
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
							renameOrnamentClassPopup.hide();
						}
					});

				}
			}
		});

		header.addTool(renameOrnamentClassButton);

		header = new FramedPanel();
		header.setHeading("Type Description");
		discription = new TextArea();
		panel.add(header, new VerticalLayoutData(1.0, .25));
		Util.doLogging("Create form von ornamenticeditor gestartet");

		header.add(discription);
		discription.setAllowTextSelection(true);
		discription.setAllowBlank(true);

		remarks = new TextArea();
		remarks.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("General Remarks");
		header.add(remarks);
		panel.add(header, new VerticalLayoutData(1.0, .25));
		interpretation = new TextArea();
		interpretation.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Interpretation");
		header.add(interpretation);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		references = new TextArea();
		references.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("References");
		header.add(references);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));

		ToolButton addCaveTool = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addCaveTool.setToolTip(Util.createToolTip("Add Cave"));


		ClickHandler addCaveClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ornamentCaveRelationEditor.show();
			}
		};

		addCaveTool.addHandler(addCaveClickHandler, ClickEvent.getType());

		cavesContentPanel = new FramedPanel();
		FramedPanel cavesContentPanel2 = new FramedPanel();

		//cavesPanel.add(cavesContentPanel);
		cavesContentPanel2.setHeight(Integer.toString(Window.getClientHeight()/100*95));
		caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(
				ornamentCaveRelationProps.ornamentCaveRelationID());

		cavesList = new ListView<OrnamentCaveRelation, String>(caveOrnamentRelationList,
				ornamentCaveRelationProps.name());
		cavesList.setAllowTextSelection(true);


		cavesContentPanel.setHeading("Added caves");
		cavesContentPanel.add(cavesList);
		cavesContentPanel2.setHeading("Typical detected in Caves:");
		cavesContentPanel2.add(ornamentTrees.getWalls().wallTree);
		cavesContentPanel2.setHeight(Integer.toString(Window.getClientHeight()/100*95));
		panel2.add(cavesContentPanel2, new VerticalLayoutData(1.0, 1.0));
		Util.doLogging("Create form von ornamenticeditor gestartet");
		
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
				Util.doLogging("Saving started by save button!");
				saveButton.disable();
				save(false,0);

			} // end
		};


		ClickHandler cancelHandler = new ClickHandler() {
			// R�ckfrage �ber schlie�en ohne zu speichern

			@Override
			public void onClick(ClickEvent event) {
				if (saveButton.isEnabled()) {
					Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {
	
						@Override
						public void onSelect(SelectEvent event) {
							Util.doLogging("saving started by exit-saving.");
							save(false,0);
							bibSelector.clearPages();
							closeEditor(ornamentEntry);
						}
					}, new SelectHandler() {
	
						@Override
						public void onSelect(SelectEvent event) {
							bibSelector.clearPages();
							closeEditor(ornamentEntry);
						}
				
						
					}
					, new KeyDownHandler() {
	
						@Override
						public void onKeyDown(KeyDownEvent e) {
							if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
							Util.doLogging("saving started by hotkey-Enter exit.");
							save(false,0);
							closeEditor(ornamentEntry);
						}}
				
						
					});
				}				
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
		horizontBackground.setSize( Integer.toString(Window.getClientWidth()/100*95),Integer.toString(Window.getClientHeight()/100*95));

		scrframedpanelornamentic.add(horizontBackground);

		tabpanel.add(scrframedpanelornamentic, "Basics");

		ScrollPanel scrgeneral2FramedPanel = new ScrollPanel();
		verticalgeneral2Background.setSize( Integer.toString(Window.getClientWidth()/100*95),Integer.toString(Window.getClientHeight()/100*95));
		
		scrgeneral2FramedPanel.add(verticalgeneral2Background);
		//tabpanel.add(scrgeneral2FramedPanel, "2. General");

		ScrollPanel scrgeneral3FramedPanel = new ScrollPanel();
		verticalgeneral3Background.setSize( Integer.toString(Window.getClientWidth()/100*95),Integer.toString(Window.getClientHeight()/100*95));
		scrgeneral3FramedPanel.add(verticalgeneral3Background);
		//tabpanel.add(scrgeneral3FramedPanel, "Components");

		HorizontalLayoutContainer ornamentComponentsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrnamentComponentsEntry, String> ornamentComponentView = new ListView<OrnamentComponentsEntry, String>(
				ornamentComponents, ornamentComponentsProps.name());
		ListView<OrnamentComponentsEntry, String> selectedOrnamentComponentView = new ListView<OrnamentComponentsEntry, String>(
				selectedOrnamentComponents, ornamentComponentsProps.name());
		//ornamentComponentsHorizontalPanel.add(ornamentComponentView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		//ornamentComponentsHorizontalPanel.add(selectedOrnamentComponentView,
		//		new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		ornamentComponentsHorizontalPanel.add(iconographySelector);
		new ListViewDragSource<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");
		new ListViewDragSource<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");

		new ListViewDropTarget<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");
		new ListViewDropTarget<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");

		header = new FramedPanel();
		header.setHeading("Select Ornament Components");

		header.add(ornamentComponentsHorizontalPanel);

		verticalgeneral3Background.add(iconographySelector, new VerticalLayoutData(1.0, 1));

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
		Util.doLogging("Create form von ornamenticeditor gestartet");
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
							OrnamentComponentsEntry entry = ornamentComponentView.getSelectionModel().getSelectedItem();
							entry.setName(renameComponentTextField.getValue());

							dbService.renameOrnamentComponents(entry, new AsyncCallback<OrnamentComponentsEntry>() {

								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(OrnamentComponentsEntry result) {
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
								innerSecondaryPatternsEntryList.add(result);
								newInnerSecondaryPatternPopup.hide();
							}
						});
						newInnerSecondaryPatternPopup.hide();
					}
				});
			}
		});
		header.add(innerSecondaryPatternsHorizontalPanel);
		header.addTool(addInnerSecondaryPatternsButton);

		//verticalgeneral3Background.add(header, new VerticalLayoutData(1.0, .3));

		
		
		
		
		
		
		
		/**
		 * --------------------- definition of image panel on right side starts here --------------------------------
		 */
		imageEntryLS = new ListStore<ImageEntry>(imgProperties.imageID());
		imgdic = new HashMap<Integer,String>();
		if (ornamentEntry != null) {
			getPics(ornamentEntry.getImages(), 600, UserLogin.getInstance().getSessionID());
		}

		imageSelector = new ImageSelector(new ImageSelectorListener() {

			@Override
			public void imageSelected(ArrayList<ImageEntry> imgEntryList) {
				Util.doLogging("Image Selected");
				if (ornamentEntry == null) {
					ornamentEntry = new OrnamentEntry();
				}
				if (imgEntryList!=null) {
					for (ImageEntry imgEntry : imgEntryList) {
						Util.doLogging("Image added: "+Integer.toString(ornamentEntry.getTypicalID()));
						ornamentEntry.addRelatedImages(imgEntry); // TODO check if double adding possible and avoid!
					}
					Util.doLogging("getPics");
					getPics(imgEntryList, 600, UserLogin.getInstance().getSessionID());
					Util.doLogging("loadImages");
					loadImages();
					//Util.doLogging("setosd");
					//setosd();
					
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
		setMasterTB.setToolTip(Util.createToolTip("Set master image.", "The master image will be displayed on top of this list and used for previews in the system (e.g. thumbnails)."));
		setMasterTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				getListenerList().get(0).addClickNumber();
				ImageEntry entry = imageListView.getSelectionModel().getSelectedItem();
				ornamentEntry.setMasterImageID(entry.getImageID());
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
				osdLoader.removeOrAddAnnotations(ornamentEntry.getRelatedAnnotationList(),
						annotationsLoaded);
				annotationsLoaded = !annotationsLoaded;
			}
		});
		showAnnotationTB
				.setToolTip(Util.createToolTip("Show or Hide Annotations.", "This will show or hide all Annotations."));
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
		    dbService.getModifiedAnnoEntry(ornamentEntry.getTypicalID(), true, new AsyncCallback<ArrayList<ModifiedEntry>>() {
				
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


		ToolButton zoomTB = new ToolButton(new IconConfig("expandButton", "expandButtonOver"));
		zoomTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				ImageEntry ie = imageListView.getSelectionModel().getSelectedItem();
				if (ie != null) {
					com.google.gwt.user.client.Window.open("/resource?imageID=" + ie.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri(),"_blank",null);
				}
			}
		});
		zoomTB.setToolTip(Util.createToolTip("View selected image in full size.", "This will open a new browser tab."));

		FramedPanel imagesFramedPanel = new FramedPanel();
		imagesFramedPanel.setHeading("Images");
		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryLS, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri;
				//Util.doLogging( item.getFilename()+" / "+Integer.toString(imgdic.size()));
				//SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=300" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				imageUri = UriUtils.fromString("icons/load_active.png");	
				if (imgdic.containsKey(item.getImageID())){
					
					imageUri = UriUtils.fromTrustedString(imgdic.get(item.getImageID()));
					
				}
				
				ArrayList<TextElement> titleList = new ArrayList<TextElement>();
				for (String s : item.getTitle().split("_")) {
					titleList.add(new TextElement(s));
				}
				String imageAuthor = item.getImageAuthor() != null ? "Author: " + item.getImageAuthor().getLabel() : "";
				String copyrightStr = (item.getCopyright() != null && item.getCopyright().length() > 0) ? "\u00A9 " + item.getCopyright() : ""; 
				
				SafeHtml sb;
				if (item.getImageID() == ornamentEntry.getMasterImageID()) {
					sb = imageViewTemplates.masterImage(item.getFilename(), item.getShortName(), titleList,
							item.getFilename().substring(item.getFilename().lastIndexOf(".") + 1).toUpperCase(),
							imageAuthor, copyrightStr, UriUtils.fromString("resource?imageID=" + item.getImageID()
									+ UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
				} else if (item.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_PUBLIC) {
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
				if (Window.getClientWidth() * 0.8 > 300) {
					s = SafeHtmlUtils.fromTrustedString("<figure class='paintRepImgPreview' style='height: 98%;width:98%;text-ali<fgn: center;'><div id= '" + item.getFilename().split(";")[0]
							+ "' style='overflow:hidden;width: 100%; height: "
							+ Integer.toString((int) (Window.getClientHeight() * - 60))
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
		}));


		ListField<ImageEntry, ImageEntry> imageViewLF = new ListField<ImageEntry, ImageEntry>(imageListView);
		loadImages();
		setosd();

		imagesFramedPanel.add(imageViewLF);
//		depictionImagesPanel.addTool(infoTB);
		imagesFramedPanel.addTool(zoomTB);
		imagesFramedPanel.addTool(addImageTB);
		imagesFramedPanel.addTool(removeImageTB);
		imagesFramedPanel.addTool(setMasterTB);
		imagesFramedPanel.addTool(showAnnotationTB);
		imagesFramedPanel.addTool(modifiedToolButtonImage);

		
		
		
		
		
		
		
		

//			imageListView.setSize("500", "290");
//		
//		
//		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
//		lf.setSize("500", "300");
//
//		imageSelector = new ImageSelector(this);
//		TextButton addImageButton = new TextButton("Select Image");
//		addImageButton.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				imageSelectionDialog = new PopupPanel();
//				imageSelectionDialog.add(imageSelector);
//				imageSelectionDialog.setModal(true);
//				imageSelectionDialog.center();
//			}
//		});
//		TextButton removeImageButton = new TextButton("Remove Image");
//		removeImageButton.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				imageEntryLS.remove(imageListView.getSelectionModel().getSelectedItem());
//			}
//		});
//
//		VerticalPanel imagesVerticalPanel = new VerticalPanel();
//		imagesVerticalPanel.add(lf);
//		HorizontalPanel hbp = new HorizontalPanel();
//		hbp.add(addImageButton);
//		hbp.add(removeImageButton);
//		imagesVerticalPanel.add(hbp);
//		imagesFramedPanel.add(imagesVerticalPanel);
//		imagesFramedPanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		
		ScrollPanel scrimagesFramedPanel = new ScrollPanel();
		HorizontalLayoutContainer componentsImages = new HorizontalLayoutContainer();
		componentsImages.add(scrgeneral3FramedPanel, new HorizontalLayoutData(.5, 1.0));
		scrimagesFramedPanel.add(imagesFramedPanel);
		componentsImages.add(imagesFramedPanel, new HorizontalLayoutData(.5, 1.0));
		componentsImages.addShowHandler(new ShowHandler() {

			@Override
			public void onShow(ShowEvent event) {
				scrimagesFramedPanel.setPixelSize(-1, (int) (Window.getClientHeight()));
			}


			
		});
		tabpanel.add(componentsImages, "Images");
		tabpanel.setTabScroll(true);
		tabpanel.addSelectionHandler(new  SelectionHandler<Widget>() {

			@Override
			public void onSelection(SelectionEvent event) {
				if (event.getSelectedItem() == componentsImages) {
					String height = Integer.toString(Window.getClientHeight()/100*95)+"px";
					reloadPics();				
					scrimagesFramedPanel.setPixelSize(-1, (int) (Window.getClientHeight()));
				}
			}

		});
		
		ToolButton closeButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeButton.setToolTip(Util.createToolTip("close"));
		saveButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveButton.setToolTip(Util.createToolTip("save"));
		closeButton.addHandler(cancelHandler, ClickEvent.getType());
		saveButton.addHandler(saveClickHandler, ClickEvent.getType());
		ToolButton deleteToolButton = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
		deleteToolButton.setToolTip(Util.createToolTip("delete"));
		deleteToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				de.cses.client.Util.showYesNo("Delete Warning!", "Proceeding will remove this Entry from the Database, are you sure?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						deleteEntry(ornamentEntry);
						closeEditor(null);
					}
				}, new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 
					}
				}, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						
					}}
			
					
			
			  );
			}
		});
		EditorListener el = getListenerList().get(0);
		if (ornamentEntry!=null) {
			bibSelector = new BibliographySelector(ornamentEntry.getRelatedBibliographyList(),el);
		} else {
			bibSelector = new BibliographySelector(new ArrayList<AnnotatedBibliographyEntry>(),el);
		}
		
		tabpanel.add(bibSelector, "Related Bibliography");
		ExternalRessourceSelectorListener ersl = new ExternalRessourceSelectorListener() {

			@Override
			public void addExternalRessource(ExternalRessourceEntry ere) {
				ornamentEntry.getrelatedExternalRessourcesList().add(ere);
			}

			@Override
			public List<ExternalRessourceEntry> getExternalRessourceList() {
				return ornamentEntry.getrelatedExternalRessourcesList();
			}

			@Override
			public void changeExternalRessource(ExternalRessourceEntry changedEre) {
				List<ExternalRessourceEntry> newExtResList = new ArrayList<ExternalRessourceEntry>();
				for (ExternalRessourceEntry ere: ornamentEntry.getrelatedExternalRessourcesList()) {
					if (changedEre.getExternalRessourceID() == ere.getExternalRessourceID()) {
						newExtResList.add(changedEre);
					} else {
						newExtResList.add(ere);
					}
				}
				ornamentEntry.setRelatedExternalRessources(newExtResList);
				
			}
			
		};
		ExternalRessourceSelector resSelector = new ExternalRessourceSelector(ornamentEntry.getrelatedExternalRessourcesList(), ersl);
		tabpanel.add(resSelector, "Related External Ressources");
		backgroundPanel = new FramedPanel();
		
		backgroundPanel.setSize( Integer.toString(Window.getClientWidth()/100*95),Integer.toString(Window.getClientHeight()/100*95));
		backgroundPanel.add(tabpanel);
		if (ornamentEntry!=null) {
			backgroundPanel.setHeading("Typical Editor (entry number: "+ornamentEntry.getTypicalID()+")");
		}
		else {
			backgroundPanel.setHeading("Typical Editor (new entry)");
		}
		createNextPrevButtons();
		backgroundPanel.addDomHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent e) {
				if (saveButton.isEnabled()) {
					if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
						de.cses.client.Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?",
						new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								saveButton.disable();
								Util.doLogging("Typical Save triggert by Key-Combination");
								save(true, 0);
								closeEditor(null);
							}
						}, new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								bibSelector.clearPages();
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
				}
			}
		}, KeyDownEvent.getType());

		backgroundPanel.addTool(modifiedToolButton);
		backgroundPanel.addTool(prevToolButton);
		backgroundPanel.addTool(nextToolButton);		
		backgroundPanel.addTool(deleteToolButton);
		backgroundPanel.addTool(saveButton);
		backgroundPanel.addTool(closeButton);
//		backgroundPanel.addDomHandler(new KeyDownHandler() {
//		    @Override
//		    public void onKeyDown(KeyDownEvent e) {
//	        	  if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
//						save(false,0);
//						closeEditor(null);		        }
//		    }			
//		}, KeyDownEvent.getType());
		new Resizable(backgroundPanel);
		DragSource d = new DragSource(backgroundPanel);
		DraggableAppearance dragAp = GWT.<DraggableAppearance> create(DraggableAppearance.class);
		dragAp.addUnselectableStyle(tabpanel.getElement());
		Draggable drag = new Draggable(backgroundPanel,backgroundPanel.getHeader(),  dragAp);
		//Draggable drag2 = new Draggable(tabpanel, dragAp);
		return backgroundPanel;

	}
	private SelectionHandler<Widget> SelectionHandler() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	protected void save(boolean close,int slide) {
		for (EditorListener el :getListenerList()) {
			if (el instanceof OrnamenticView) {
				((OrnamenticView)el).setEditor(ornamentEntry);
			}
		}

		if (!ornamentCodeTextField.validate()) {
			Util.showWarning("Missing information", "Please insert Ornamentation Code!");
			saveButton.enable();
			return;
		}

		if (ornamentEntry == null) {
			ornamentEntry = new OrnamentEntry();
		}
		ornamentEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());
		ArrayList<OrnamentCaveRelation> corList = new ArrayList<OrnamentCaveRelation>();
		for (int i = 0; i < caveOrnamentRelationList.size(); i++) {
			corList.add(caveOrnamentRelationList.get(i));
		}
		if (ornamentTrees.getSelectedie()!=null) {
			ornamentEntry.setIconographyID(ornamentTrees.getSelectedie().getIconographyID());			
		}

		ArrayList<ImageEntry> ieList = new ArrayList<ImageEntry>();
		for (int i = 0; i < imageEntryLS.size(); i++) {
			ieList.add(imageEntryLS.get(i));
		}
		ornamentEntry.setImages(ieList);
		ornamentEntry.setVirtualTourOrder((double) this.tourOrderField.getValue());
		ornamentEntry.setIsVirtualTour(this.isVirtualTour.getValue());
		ornamentEntry.setCode(ornamentCodeTextField.getText());
		ornamentEntry.setDescription(discription.getText());
		ornamentEntry.setRemarks(remarks.getText());
		ornamentEntry.setRelatedIconographyList(iconographySelector.getSelectedIconography());;;

		ArrayList<InnerSecondaryPatternsEntry> ispeList = new ArrayList<InnerSecondaryPatternsEntry>();
		for (int i = 0; i < selectedinnerSecondaryPatternsEntryList.size(); i++) {
			ispeList.add(selectedinnerSecondaryPatternsEntryList.get(i));
		}

		ArrayList<OrnamentComponentsEntry> oceList = new ArrayList<OrnamentComponentsEntry>();
		for (int i = 0; i < selectedOrnamentComponents.size(); i++) {
			oceList.add(selectedOrnamentComponents.get(i));
		}
		ornamentEntry.setRelatedBibliographyList(bibSelector.getSelectedEntries());

		// send ornament to server
		if (ornamentEntry.getTypicalID() == 0) {
			dbService.saveOrnamentEntry(ornamentEntry, new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					saveButton.enable();
					Util.showWarning("Saving failed", caught.getMessage());
				}

				@Override
				public void onSuccess(Integer result) {
					saveButton.enable();
					if (result > 0) {
						Util.doLogging(this.getClass().getName() + " saving sucessful");
						ornamentEntry.setOrnamentID(result);
						// updateEntry(ornamentEntry);
						if (close) {
							closeEditor(ornamentEntry);
						}
						if (slide!=0) {
							doslide(slide);
						}

					} else {
						Util.showWarning("Saving failed", "ornamentID == 0");
					}
				}
			});
		} else {
			dbService.updateOrnamentEntry(ornamentEntry, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					saveButton.enable();
					Util.doLogging(("Update failed"+ caught.getMessage()));
					Info.display("Update failed", caught.getMessage());
				}

				@Override
				public void onSuccess(Boolean result) {
					saveButton.enable();
					if (close) {
						closeEditor(ornamentEntry);
					}
				}
			});
		}
	}

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();
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
			imageEntryLS.add(ie);
		}
		imageSelectionDialog.hide();
	}
	private void getPics(ArrayList<ImageEntry> ies, int size, String login) {
		dbService.getPics(ies, size, login, new AsyncCallback<Map<Integer,String>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				//Info.display("getPics", "got bad response");
			}

			@Override
			public void onSuccess(Map<Integer,String> result) {
				//.display("getPics", "got good response");
				//for (Map.Entry<String,String> entry : result.entrySet())  
		        //    Util.doLogging("Key = " + entry.getKey() + 
		        //                     ", Value = " + entry.getValue());
				for (Integer key : result.keySet()) {
				Util.doLogging("Got response.");
				try {
					imgdic.put(key, result.get(key));
					
				}
				catch (Exception e){
					Util.doLogging("Could not load image "+key+" Reason: "+e.getMessage());
				}
				}
				//imageListView.refresh();
				loadImages();
				setosd();
			
			}
		});
	}

	private void loadImages() {
		imageEntryLS.clear();
		if (ornamentEntry != null) {
			for (ImageEntry ie : ornamentEntry.getImages()) {

				//Util.doLogging("adding "+Integer.toString((ie.getImageID()))+"to imageEntryLS");
				imageEntryLS.add(ie);
			}
		}
	}

}
