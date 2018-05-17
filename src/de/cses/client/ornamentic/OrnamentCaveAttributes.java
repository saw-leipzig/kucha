package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.IconographySelector;
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.StructureOrganization;
import de.cses.shared.StyleEntry;
import de.cses.shared.WallOrnamentCaveRelation;

public class OrnamentCaveAttributes extends PopupPanel {

	private FramedPanel header;
	private ComboBox<CaveEntry> caveEntryComboBox;
	private ListStore<CaveEntry> caveEntryList;
	private CaveEntryProperties caveEntryProps;
	private ListStore<DistrictEntry> districtEntryList;
	private DistrictEntryProperties districtEntryProps;
	private ListStore<OrnamentEntry> ornamentEntryList;
	private ListStore<OrnamentEntry> ornamentEntryList2;
	private OrnamentEntryProperties ornamentEntryProps;
	private ComboBox<DistrictEntry> districtComboBox;
	private ListStore<WallOrnamentCaveRelation> wallsListStore;
	private ListView<WallOrnamentCaveRelation, String> wallList;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PopupPanel popup = this;
	private OrnamentCaveAttributes ornamentCaveAttributes = this;
	private ListStore<OrnamentEntry> selectedSimilarOrnaments;
	private ListStore<OrnamentEntry> selectedRedlatedOrnaments;
	private WallRelationProperties wallRelationProps;
	private IconographySelector icoSelector;
	private OrnamenticEditor ornamenticEditor;
	private ComboBox<StyleEntry> styleComboBox;
	private TextField caveType = new TextField();
	private OrnamentCaveRelation ornamentCaveRelationEntry;
	private ListStore<OrientationEntry> orientationListStore;
	private ListStore<OrientationEntry> selectedorientationListStore;
	private OrientationProperties orientationProps;
	private StoreFilter<CaveEntry> caveFilter;
	private ListStore<StyleEntry> styleEntryList;
	private StyleProperties styleProps;
	private ContentPanel mainPanel = null;

	public OrnamentCaveAttributes() {
		this(null);
	}
	
	public OrnamentCaveAttributes(OrnamentCaveRelation ornamentCaveRelationEntry) {
		this.ornamentCaveRelationEntry = ornamentCaveRelationEntry;

		caveEntryProps = GWT.create(CaveEntryProperties.class);
		styleProps = GWT.create(StyleProperties.class);
		ornamentEntryProps = GWT.create(OrnamentEntryProperties.class);
		districtEntryProps = GWT.create(DistrictEntryProperties.class);
		wallRelationProps = GWT.create(WallRelationProperties.class);
		orientationProps = GWT.create(OrientationProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveEntryProps.caveID());
		districtEntryList = new ListStore<DistrictEntry>(districtEntryProps.districtID());
		ornamentEntryList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamentEntryList2 = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedSimilarOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedRedlatedOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		orientationListStore = new ListStore<OrientationEntry>(orientationProps.orientationID());
		selectedorientationListStore = new ListStore<OrientationEntry>(orientationProps.orientationID());
		styleEntryList = new ListStore<StyleEntry>(styleProps.styleID());
		wallsListStore = new ListStore<WallOrnamentCaveRelation>(wallRelationProps.wallLocationID());

		for (DistrictEntry pe : StaticTables.getInstance().getDistrictEntries().values()) {
			districtEntryList.add(pe);
		}

		dbService.getOrientations(new AsyncCallback<ArrayList<OrientationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			public void onSuccess(ArrayList<OrientationEntry> result) {
				Util.doLogging(this.getClass().getName() + " groesse orientation " + result.size());
				orientationListStore.clear();
				selectedorientationListStore.clear();
				for (OrientationEntry pe : result) {
					orientationListStore.add(pe);
				}
				if (ornamentCaveRelationEntry != null) {
					for (OrientationEntry oe : ornamentCaveRelationEntry.getOrientations()) {
						orientationListStore.remove(orientationListStore.findModelWithKey(Integer.toString(oe.getOrientationID())));
						selectedorientationListStore.add(oe);
					}
				}
				//				
//				
//				if (ornamentCaveRelationEntry != null) {
//					Util.doLogging(this.getClass().getName() + "groesse orientation in entry" + ornamentCaveRelationEntry.getOrientations().size());
//					for (OrientationEntry pe : result) {
//						int count = 0;
//						for (OrientationEntry oe : ornamentCaveRelationEntry.getOrientations()) {
//							if (pe.getOrientationID() != oe.getOrientationID()) {
//								count++;
//							}
//							if (count == ornamentCaveRelationEntry.getOrientations().size()) {
//								orientationListStore.add(pe);
//							}
//						}
//					}
//					for (OrientationEntry oe : ornamentCaveRelationEntry.getOrientations()) {
//						selectedorientationListStore.add(oe);
//					}
//					if (ornamentCaveRelationEntry.getOrientations().size() == 0) {
//						for (OrientationEntry nu : result) {
//							orientationListStore.add(nu);
//						}
//					}
//				} else {
//					for (OrientationEntry pe : result) {
//						orientationListStore.add(pe);
//					}
//				}

			}
		});

//		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ArrayList<CaveEntry> result) {
//				for (CaveEntry pe : result) {
//					caveEntryList.add(pe);
//				}
//			}
//		});
		
		for (CaveEntry ce : StaticTables.getInstance().getCaveEntries().values()) {
			caveEntryList.add(ce);
		}

		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {

				ornamentEntryList.clear();
				// ornamentEntryList2.clear();
				// selectedSimilarOrnaments.clear();
				selectedRedlatedOrnaments.clear();
					for (OrnamentEntry pe : result) {
						ornamentEntryList.add(pe);
					}
					if (ornamentCaveRelationEntry != null) {
						for (OrnamentEntry oe : ornamentCaveRelationEntry.getRelatedOrnamentsRelations()) {
							ornamentEntryList.remove(ornamentEntryList.findModelWithKey(Integer.toString(oe.getOrnamentID())));
							selectedRedlatedOrnaments.add(oe);
						}
					}
					
//					for (OrnamentEntry pe : result) {
//
//						// int count = 0;
//						// for (OrnamentEntry oe : ornamentCaveRelationEntry.getSimilarOrnamentsRelations()) {
//						// if (pe.getOrnamentID() != oe.getOrnamentID()) {
//						// count++;
//						// }
//						// if (count == ornamentCaveRelationEntry.getSimilarOrnamentsRelations().size()) {
//						// ornamentEntryList2.add(pe);
//						// }
//						// }
//
//						int countrelated = 0;
//						for (OrnamentEntry oe : ornamentCaveRelationEntry.getRelatedOrnamentsRelations()) {
//
//							if (pe.getOrnamentID() != oe.getOrnamentID()) {
//								countrelated++;
//							}
//							if (countrelated == ornamentCaveRelationEntry.getRelatedOrnamentsRelations().size()) {
//								ornamentEntryList.add(pe);
//							}
//						}
//						if (ornamentCaveRelationEntry.getRelatedOrnamentsRelations().size() == 0) {
//							for (OrnamentEntry nu : result) {
//								ornamentEntryList.add(nu);
//							}
//						}
//
//					}

					// for (OrnamentEntry oe : ornamentCaveRelationEntry.getSimilarOrnamentsRelations()) {
					// selectedSimilarOrnaments.add(oe);
					// }


			}
		});
		
		createForm();
	}

	public void createForm() {
		for (StyleEntry pe : StaticTables.getInstance().getStyleEntries().values()) {
			styleEntryList.add(pe);
		}

		TabPanel tabPanel = new TabPanel();
		tabPanel.setWidth(650);
		tabPanel.setHeight(570);
		tabPanel.setTabScroll(true);

		VerticalLayoutContainer caveAttributesVerticalPanel = new VerticalLayoutContainer();

		caveAttributesVerticalPanel.add(tabPanel);

		VerticalLayoutContainer vlcCave = new VerticalLayoutContainer();

		FramedPanel cavesFrame = new FramedPanel();
		tabPanel.add(cavesFrame, "Cave");
		cavesFrame.add(vlcCave);
		cavesFrame.setHeading("Cave");

		districtComboBox = new ComboBox<DistrictEntry>(districtEntryList, districtEntryProps.name(),
				new AbstractSafeHtmlRenderer<DistrictEntry>() {

					@Override
					public SafeHtml render(DistrictEntry item) {
						final DistrictViewTemplates pvTemplates = GWT.create(DistrictViewTemplates.class);
						return pvTemplates.district(item.getName());
					}
				});
		districtComboBox.setTypeAhead(true);
		districtComboBox.setEditable(false);
		districtComboBox.setTriggerAction(TriggerAction.ALL);
		if (ornamentCaveRelationEntry.getDistrict() != null) {
			districtComboBox.setValue(StaticTables.getInstance().getDistrictEntries().get(ornamentCaveRelationEntry.getDistrict().getDistrictID()), false);
		}
		
		header = new FramedPanel();
		header.setHeading("Select District");
		header.add(districtComboBox);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		ValueChangeHandler<DistrictEntry> districtSelectionHandler = new ValueChangeHandler<DistrictEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<DistrictEntry> event) {
				caveType.clear();
				wallsListStore.clear();
				activateCaveFilter();
				caveEntryComboBox.setEnabled(true);
			}

		};

		// hier aenderungen

		districtComboBox.addValueChangeHandler(districtSelectionHandler);

		caveEntryComboBox = new ComboBox<CaveEntry>(caveEntryList, caveEntryProps.officialNumber(), new AbstractSafeHtmlRenderer<CaveEntry>() {

			@Override
			public SafeHtml render(CaveEntry item) {
				final CaveViewTemplates pvTemplates = GWT.create(CaveViewTemplates.class);
				if ((item.getHistoricName() != null) && (item.getHistoricName().length() == 0)) {
					return pvTemplates.caveLabel(item.getOfficialNumber());
				} else {
					return pvTemplates.caveLabel(item.getOfficialNumber(), item.getHistoricName());
				}
			}
		});
		caveEntryComboBox.setEnabled(false);
		caveEntryComboBox.setTypeAhead(true);
		caveEntryComboBox.setEditable(false);
		caveEntryComboBox.setTriggerAction(TriggerAction.ALL);

		if (ornamentCaveRelationEntry != null) {
			if (ornamentCaveRelationEntry.getCaveEntry() != null) {
				caveEntryComboBox.setValue(caveEntryList.findModelWithKey(Integer.toString(ornamentCaveRelationEntry.getCaveEntry().getCaveID())), false);
			}
			int p = ornamentCaveRelationEntry.getCaveEntry().getCaveTypeID();
			caveType.setText(StaticTables.getInstance().getCaveTypeEntries().get(p).getNameEN());

		}

		if (ornamentCaveRelationEntry != null) {
			districtComboBox.setValue(ornamentCaveRelationEntry.getDistrict());
			ValueChangeEvent.fire(districtComboBox, ornamentCaveRelationEntry.getDistrict());
		}

		header = new FramedPanel();

		header.setHeading("Select Cave");
		header.add(caveEntryComboBox);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		caveType.setAllowBlank(false);
		caveType.setEnabled(false);

		header = new FramedPanel();

		header.setHeading("Cave Type");

		header.add(caveType);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		SelectionHandler<CaveEntry> caveSelectionHandler = new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				int p = event.getSelectedItem().getCaveTypeID();
				wallsListStore.clear();
				caveType.setText(StaticTables.getInstance().getCaveTypeEntries().get(p).getNameEN());
				// dbService.getCaveTypebyID(p, new AsyncCallback<CaveTypeEntry>() {
				//
				// @Override
				// public void onFailure(Throwable caught) {
				// caught.printStackTrace();
				// }
				//
				// @Override
				// public void onSuccess(CaveTypeEntry result) {
				// caveType.setText(result.getNameEN());
				// }
				// });
				// style ueber hoehle raussuchen
			}

		};

		styleComboBox = new ComboBox<StyleEntry>(styleEntryList, styleProps.styleName(), new AbstractSafeHtmlRenderer<StyleEntry>() {

			@Override
			public SafeHtml render(StyleEntry item) {
				final StyleViewTemplates pvTemplates = GWT.create(StyleViewTemplates.class);
				return pvTemplates.style(item.getStyleName());
			}
		});

		if (ornamentCaveRelationEntry != null) {
			if (ornamentCaveRelationEntry.getStyle() != null) {
				styleComboBox.setValue(ornamentCaveRelationEntry.getStyle());
			}
		}

		header = new FramedPanel();

		header.setHeading("Style");
		header.add(styleComboBox);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		TextButton addWalls = new TextButton("Add Wall");

		ClickHandler addWallsClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (districtComboBox.getValue() == null) {

					return;
				}
				if (caveEntryComboBox.getValue() == null) {

					return;
				}
				OrnamentWallAttributes attributespopup = new OrnamentWallAttributes(caveEntryComboBox.getValue(), null);
				attributespopup.setOrnamentCaveRelation(ornamentCaveAttributes);
				attributespopup.setModal(true);
				attributespopup.setGlassEnabled(true);
				attributespopup.center();

			}

		};
		addWalls.addHandler(addWallsClickHandler, ClickEvent.getType());

		caveEntryComboBox.addSelectionHandler(caveSelectionHandler);

		header = new FramedPanel();

		HorizontalPanel selectedWallsHorizontalPanel = new HorizontalPanel();
		header.setHeading("Walls");
		header.add(selectedWallsHorizontalPanel, new VerticalLayoutData(1.0, 1.0));
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		if (ornamentCaveRelationEntry != null) {
			Util.doLogging(this.getClass().getName() + " Walls list laenge: " + ornamentCaveRelationEntry.getWalls().size());
			wallsListStore.clear();
			for (WallOrnamentCaveRelation pe : ornamentCaveRelationEntry.getWalls()) {
				wallsListStore.add(pe);
				Util.doLogging(this.getClass().getName() + "added wall");
			}
		}

		// wallList = new ListView<WallOrnamentCaveRelation, String>(wallsListStore, wallRelationProps.name());
		wallList = new ListView<WallOrnamentCaveRelation, String>(wallsListStore, new ValueProvider<WallOrnamentCaveRelation, String>() {

			@Override
			public String getValue(WallOrnamentCaveRelation wocr) {
				return StaticTables.getInstance().getWallLocationEntries().get(wocr.getWall().getWallLocationID()).getCaveAreaLabel() + ", "
						+ StaticTables.getInstance().getOrnamentPositionEntries().get(wocr.getOrnamenticPositionID()).getName() + ", "
						+ StaticTables.getInstance().getOrmanemtFunctionEntries().get(wocr.getOrnamenticFunctionID()).getName();
			}

			@Override
			public void setValue(WallOrnamentCaveRelation object, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getPath() {
				// TODO Auto-generated method stub
				return null;
			}
		});
		wallList.setAllowTextSelection(true);

		selectedWallsHorizontalPanel.add(wallList);

		TextButton edit = new TextButton("edit");
		ClickHandler editWallsClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (!caveEntryComboBox.getValue().equals(null)) {
					OrnamentWallAttributes attributespopup = new OrnamentWallAttributes(caveEntryComboBox.getValue(),
							wallList.getSelectionModel().getSelectedItem());
					attributespopup.setOrnamentCaveRelation(ornamentCaveAttributes);
					attributespopup.setModal(true);
					attributespopup.setGlassEnabled(true);
					attributespopup.center();
				} else {
					Window.alert("Please select an entry!");
				}

			}

		};
		edit.addHandler(editWallsClickHandler, ClickEvent.getType());

		TextButton delete = new TextButton("delete");

		header.addButton(addWalls);
		header.addButton(edit);
		header.addButton(delete);

		ClickHandler deleteClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				wallsListStore.remove(wallList.getSelectionModel().getSelectedItem());

			}
		};
		delete.addHandler(deleteClickHandler, ClickEvent.getType());

		if (ornamentCaveRelationEntry != null) {
			wallsListStore.clear();
			wallsListStore.addAll(ornamentCaveRelationEntry.getWalls());
		}

		VerticalLayoutContainer vlcAttributes = new VerticalLayoutContainer();

		HorizontalLayoutContainer horizontalContainerLayout = new HorizontalLayoutContainer();

		HorizontalLayoutContainer orientationHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrientationEntry, String> orientationView = new ListView<OrientationEntry, String>(orientationListStore, orientationProps.name());
		ListView<OrientationEntry, String> selectedOrientationView = new ListView<OrientationEntry, String>(selectedorientationListStore, orientationProps.name());
		orientationHorizontalPanel.add(orientationView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		orientationHorizontalPanel.add(selectedOrientationView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrientationEntry>(orientationView).setGroup("Orientation");
		new ListViewDragSource<OrientationEntry>(selectedOrientationView).setGroup("Orientation");

		new ListViewDropTarget<OrientationEntry>(selectedOrientationView).setGroup("Orientation");
		new ListViewDropTarget<OrientationEntry>(orientationView).setGroup("Orientation");

		header = new FramedPanel();
		header.setHeading("Select Orientation");
		header.add(orientationHorizontalPanel);

		horizontalContainerLayout.add(header, new HorizontalLayoutData(.5, 1.0));

		TextArea notes = new TextArea();

		header = new FramedPanel();
		if (ornamentCaveRelationEntry != null) {
			notes.setText(ornamentCaveRelationEntry.getNotes());
		}
		header.setHeading("Notes");
		header.add(notes);
		horizontalContainerLayout.add(header, new HorizontalLayoutData(.5, 1.0));

		TextArea colours = new TextArea();
		colours.setAllowBlank(true);
		header = new FramedPanel();

		header.setHeading("Colours");
		if (ornamentCaveRelationEntry != null) {
			colours.setText(ornamentCaveRelationEntry.getColours());
		}
		header.add(colours);
		vlcAttributes.add(horizontalContainerLayout, new VerticalLayoutData(1.0, .3));
		vlcAttributes.add(header, new VerticalLayoutData(0.5, .3));

		colours.setAllowBlank(true);

		FramedPanel attributes = new FramedPanel();
		attributes.setHeading("Attributes");
		attributes.add(vlcAttributes);
		tabPanel.add(attributes, "Attributes");

//		FramedPanel relationToOtherOrnaments = new FramedPanel();
//		relationToOtherOrnaments.setHeading("Relations");
		VerticalLayoutContainer vlcRelationToTherornaments1 = new VerticalLayoutContainer();
		VerticalLayoutContainer vlcRelationToTherornaments2 = new VerticalLayoutContainer();
		
		HorizontalLayoutContainer relationToOtherOrnamentsHLC = new HorizontalLayoutContainer();

//		relationToOtherOrnaments.add(relationToOtherOrnamentsHLC);
		relationToOtherOrnamentsHLC.add(vlcRelationToTherornaments1, new HorizontalLayoutData(.5, 1.0));
		relationToOtherOrnamentsHLC.add(vlcRelationToTherornaments2, new HorizontalLayoutData(.5, 1.0));

		HorizontalLayoutContainer relatedOrnamentsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrnamentEntry, String> ornamentListViewRelated = new ListView<OrnamentEntry, String>(ornamentEntryList,
				ornamentEntryProps.code());
		// ornamentListViewRelated.setPixelSize(150, 150);
		ListView<OrnamentEntry, String> selectedRelatedOrnamentsListView = new ListView<OrnamentEntry, String>(selectedRedlatedOrnaments,
				ornamentEntryProps.code());
		// selectedRelatedOrnamentsListView.setPixelSize(150, 150);
		relatedOrnamentsHorizontalPanel.add(ornamentListViewRelated, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		relatedOrnamentsHorizontalPanel.add(selectedRelatedOrnamentsListView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrnamentEntry>(ornamentListViewRelated).setGroup("relatedOrnament");
		new ListViewDragSource<OrnamentEntry>(selectedRelatedOrnamentsListView).setGroup("relatedOrnament");

		new ListViewDropTarget<OrnamentEntry>(selectedRelatedOrnamentsListView).setGroup("relatedOrnament");
		new ListViewDropTarget<OrnamentEntry>(ornamentListViewRelated).setGroup("relatedOrnament");

		header = new FramedPanel();
		header.setHeading("Select related ornaments");
		header.add(relatedOrnamentsHorizontalPanel);

		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1.0, .4));

		HorizontalLayoutContainer similarOrnamentsHorizontalPanel = new HorizontalLayoutContainer();
		ListView<OrnamentEntry, String> ornamentListViewSimilar = new ListView<OrnamentEntry, String>(ornamentEntryList2,
				ornamentEntryProps.code());
		// ornamentListViewSimilar.setPixelSize(150, 150);
		ListView<OrnamentEntry, String> selectedSimilarOrnamentsListView = new ListView<OrnamentEntry, String>(selectedSimilarOrnaments,
				ornamentEntryProps.code());
		// selectedSimilarOrnamentsListView.setPixelSize(150, 150);
		similarOrnamentsHorizontalPanel.add(ornamentListViewSimilar, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		similarOrnamentsHorizontalPanel.add(selectedSimilarOrnamentsListView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrnamentEntry>(ornamentListViewSimilar).setGroup("similarOrnament");
		new ListViewDragSource<OrnamentEntry>(selectedSimilarOrnamentsListView).setGroup("similarOrnament");

		new ListViewDropTarget<OrnamentEntry>(selectedSimilarOrnamentsListView).setGroup("similarOrnament");
		new ListViewDropTarget<OrnamentEntry>(ornamentListViewSimilar).setGroup("similarOrnament");

		/*
		 * header = new FramedPanel(); header.setHeading("Select similar ornaments"); header.add(similarOrnamentsHorizontalPanel);
		 * vlcRelationToTherornaments2.add(header, new VerticalLayoutData(1.0, .4));
		 */

		icoSelector = new IconographySelector(StaticTables.getInstance().getIconographyForOrnamenticEntries().values());
		Util.doLogging("OrnamentCaveAttributes - IconographySelector initialised");
//		header = new FramedPanel();
//		header.setHeading("Select elements showing similar pattern");
//		header.add(icoSelector);
		if (ornamentCaveRelationEntry != null) {
			if (ornamentCaveRelationEntry.getIconographyElements() == null) {
				Util.doLogging("OrnamentCaveAttributes - ornamentCaveRelationEntry.getIconographyElements() is NULL");
			}
			Util.doLogging("OrnamentCaveAttributes - ornamentCaveRelationEntry.getIconographyElements().size() = " + ornamentCaveRelationEntry.getIconographyElements().size());
			icoSelector.setSelectedIconography(ornamentCaveRelationEntry.getIconographyElements());
		}
		vlcRelationToTherornaments2.add(icoSelector, new VerticalLayoutData(1.0, .4));

		final TextField groupOfOrnaments = new TextField();
		groupOfOrnaments.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Ornamental Group");
		if (ornamentCaveRelationEntry != null) {
			groupOfOrnaments.setText(ornamentCaveRelationEntry.getGroup());
		}
		header.add(groupOfOrnaments);
		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .125));

		/*
		 * final TextField relatedElementsofOtherCultures = new TextField();
		 * 
		 * header = new FramedPanel(); header.setHeading("Describe related elements of other cultures"); if (ornamentCaveRelationEntry != null) {
		 * relatedElementsofOtherCultures.setText(ornamentCaveRelationEntry.getRelatedelementeofOtherCultures()); } header.add(relatedElementsofOtherCultures);
		 * vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .125));
		 */

		final TextArea similarElementsofOtherCultures = new TextArea();

		header = new FramedPanel();
		if (ornamentCaveRelationEntry != null) {
			similarElementsofOtherCultures.setText(ornamentCaveRelationEntry.getSimilarelementsOfOtherCultures());
		}
		header.setHeading("Describe similarities with elements of other cultural areas");
		header.add(similarElementsofOtherCultures);
		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .3));

		tabPanel.add(relationToOtherOrnamentsHLC, "Relations");

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		TextButton save = new TextButton("save");
		TextButton cancel = new TextButton("cancel");

		caveAttributesVerticalPanel.add(buttonsPanel);

		ClickHandler cancelClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();

			}
		};
		cancel.addHandler(cancelClickHandler, ClickEvent.getType());

		ClickHandler saveClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				OrnamentCaveRelation ornamentCaveRelation;

				if (ornamentCaveRelationEntry == null) {

					ornamentCaveRelation = new OrnamentCaveRelation();
				} else {
					ornamentCaveRelation = ornamentCaveRelationEntry;

				}
				if (districtComboBox.getValue() == null) {

					return;
				}
				if (caveEntryComboBox.getValue() == null) {

					return;
				}

//				ornamentCaveRelation.setName("Cave: " + caveEntryComboBox.getValue().getCaveID());
				ornamentCaveRelation.setCaveEntry(caveEntryComboBox.getValue());
				ornamentCaveRelation.setDistrict(districtComboBox.getValue());
				ornamentCaveRelation.setColours(colours.getText());
				ornamentCaveRelation.setGroup(groupOfOrnaments.getText());

				ornamentCaveRelation.setStyle(styleComboBox.getValue());
				ornamentCaveRelation.setNotes(notes.getText());
				List<OrnamentEntry> relatedOrnaments = selectedRedlatedOrnaments.getAll();
				ornamentCaveRelation.getRelatedOrnamentsRelations().clear();
				for (OrnamentEntry ornament : relatedOrnaments) {
					ornamentCaveRelation.getRelatedOrnamentsRelations().add(ornament);
				}
//				List<OrientationEntry> orientationslist = selectedorientationListStore.getAll();
//				for (OrientationEntry orientation : orientationslist) {
//					ornamentCaveRelation.getOrientations().add(orientation);
//				}
				ornamentCaveRelation.setOrientations(new ArrayList<OrientationEntry>(selectedorientationListStore.getAll()));

				ornamentCaveRelation.getIconographyElements().clear();
				for (int i = 0; i < icoSelector.getSelectedIconography().size(); i++) {
					ornamentCaveRelation.getIconographyElements().add(icoSelector.getSelectedIconography().get(i));
				}

				ornamentCaveRelation.getWalls().clear();
				for (int i = 0; i < wallsListStore.size(); i++) {
					ornamentCaveRelation.getWalls().add(wallsListStore.get(i));
				}
				// ornamentCaveRelation.setRelatedelementeofOtherCultures(relatedElementsofOtherCultures.getText());
				ornamentCaveRelation.setSimilarelementsOfOtherCultures(similarElementsofOtherCultures.getText());
				// set walls
				if (ornamentCaveRelationEntry == null) {
					ornamenticEditor.getCaveOrnamentRelationList().add(ornamentCaveRelation);
				}

				popup.hide();

			}
		};
		save.addHandler(saveClickHandler, ClickEvent.getType());

		mainPanel = new ContentPanel();
		mainPanel.addButton(save);
		mainPanel.addButton(cancel);
		mainPanel.setHeading("New Cave Relation");
		mainPanel.add(caveAttributesVerticalPanel);
		
		this.add(mainPanel);
	}

	interface CaveEntryProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();

		LabelProvider<CaveEntry> officialNumber();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{officialNumber}: {historicName}</div>")
		SafeHtml caveLabel(String officialNumber, String historicName);

		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);
	}

	interface OrnamentEntryProperties extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> OrnamentID();

		@Path("code")
		ValueProvider<OrnamentEntry, String> code();
	}

	interface OrnamentOfOtherCulturesEntryProperties extends PropertyAccess<OrnamentOfOtherCulturesEntry> {
		ModelKeyProvider<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesID();

		ValueProvider<OrnamentOfOtherCulturesEntry, String> name();
	}

	interface DistrictEntryProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();

		LabelProvider<DistrictEntry> name();
	}

	interface OrnamentViewTemplates extends XTemplates {
		@XTemplate("<div>{code}</div>")
		SafeHtml ornament(String code);
	}

	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml district(String name);
	}

	interface OrnamentOfOtherCulturesEntryViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentOfOtherCultures(String name);
	}

	interface StructureOrganizationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml structureOrganization(String name);
	}

	interface MainTypologicalClassViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml mainTypologicalClass(String name);
	}

	interface OrnamentCaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentCaveType(String name);
	}

	interface StyleViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml style(String name);
	}

	interface StyleProperties extends PropertyAccess<StyleEntry> {
		ModelKeyProvider<StyleEntry> styleID();

		LabelProvider<StyleEntry> styleName();
	}

	interface StructureOrganizationProperties extends PropertyAccess<StructureOrganization> {
		ModelKeyProvider<StructureOrganization> structureOrganizationID();

		LabelProvider<StructureOrganization> name();
	}

	interface OrnamentCaveTypeProperties extends PropertyAccess<OrnamentCaveType> {
		ModelKeyProvider<OrnamentCaveType> ornamentCaveTypeID();

		LabelProvider<OrnamentCaveType> name();
	}

	public OrnamenticEditor getOrnamentic() {
		return ornamenticEditor;
	}

	public void setOrnamentic(OrnamenticEditor ornamenticEditor) {
		this.ornamenticEditor = ornamenticEditor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
//	@Override
//	public Widget asWidget() {
//		if (mainPanel == null) {
//			createForm();
//		}
//		return mainPanel;
//	}

	interface WallRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<WallOrnamentCaveRelation> wallLocationID();
	}

	interface OrientationProperties extends PropertyAccess<OrientationEntry> {
		ModelKeyProvider<OrientationEntry> orientationID();

		ValueProvider<OrientationEntry, String> name();
	}

	/**
	 * @return the wallsListStore
	 */
	public ListStore<WallOrnamentCaveRelation> getWallsListStore() {
		return wallsListStore;
	}

	/**
	 * @param wallsListStore
	 *          the wallsListStore to set
	 */
	public void setWallsListStore(ListStore<WallOrnamentCaveRelation> wallsListStore) {
		this.wallsListStore = wallsListStore;
	}

	private void activateCaveFilter() {
		caveFilter = new StoreFilter<CaveEntry>() {

			@Override
			public boolean select(Store<CaveEntry> store, CaveEntry parent, CaveEntry item) {
				return (item.getSiteID() == districtComboBox.getCurrentValue().getSiteID());
			}
		};
		caveEntryList.addFilter(caveFilter);
		caveEntryList.setEnableFilters(true);
		caveEntryComboBox.setEnabled(true);
	}

}
