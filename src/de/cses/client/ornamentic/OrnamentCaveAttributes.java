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
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.StructureOrganization;
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
	private PictorialElementSelectorObjects selector;
	private Ornamentic ornamentic;
	private ListStore<OrientationEntry> orientation;
	private ListStore<OrientationEntry> selectedorientation;
	private TextField style = new TextField();
	private TextField caveType = new TextField();
	private OrnamentCaveRelation ornamentCaveRelationEntry;

	private OrientationProperties orientationProps;

	public OrnamentCaveAttributes(OrnamentCaveRelation ornamentCaveRelationEntry) {
		this.ornamentCaveRelationEntry = ornamentCaveRelationEntry;
		init();
	}

	public OrnamentCaveAttributes() {
		init();
	}

	public void init() {
		caveEntryProps = GWT.create(CaveEntryProperties.class);
		ornamentEntryProps = GWT.create(OrnamentEntryProperties.class);
		districtEntryProps = GWT.create(DistrictEntryProperties.class);
		wallRelationProps = GWT.create(WallRelationProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveEntryProps.caveID());
		districtEntryList = new ListStore<DistrictEntry>(districtEntryProps.districtID());
		ornamentEntryList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamentEntryList2 = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedSimilarOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedRedlatedOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());

		orientationProps = GWT.create(OrientationProperties.class);
		wallsListStore = new ListStore<WallOrnamentCaveRelation>(wallRelationProps.wallLocationID());

		selectedorientation = new ListStore<OrientationEntry>(orientationProps.orientationID());
		orientation = new ListStore<OrientationEntry>(orientationProps.orientationID());

		// if (ornamentCaveRelationEntry != null) {
		// wallsListStore.addAll(ornamentCaveRelationEntry.getWalls());
		// }
		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				ornamentEntryList.clear();
				ornamentEntryList2.clear();
				selectedSimilarOrnaments.clear();
				selectedRedlatedOrnaments.clear();
				if (ornamentCaveRelationEntry != null) {
					for (OrnamentEntry pe : result) {

						int count = 0;
						for (OrnamentEntry oe : ornamentCaveRelationEntry.getSimilarOrnamentsRelations()) {
							if (pe.getOrnamentID() != oe.getOrnamentID()) {
								count++;
							}
							if (count == ornamentCaveRelationEntry.getSimilarOrnamentsRelations().size()) {
								ornamentEntryList2.add(pe);
							}
						}

						int countrelated = 0;
						for (OrnamentEntry oe : ornamentCaveRelationEntry.getRelatedOrnamentsRelations()) {
							if (pe.getOrnamentID() != oe.getOrnamentID()) {
								countrelated++;
							}
							if (countrelated == ornamentCaveRelationEntry.getRelatedOrnamentsRelations().size()) {
								ornamentEntryList.add(pe);
							}
						}

					}
					for (OrnamentEntry oe : ornamentCaveRelationEntry.getSimilarOrnamentsRelations()) {
						selectedSimilarOrnaments.add(oe);
					}
					for (OrnamentEntry oe : ornamentCaveRelationEntry.getRelatedOrnamentsRelations()) {
						selectedRedlatedOrnaments.add(oe);
					}

				} else {
					for (OrnamentEntry pe : result) {
						ornamentEntryList2.add(pe);
						ornamentEntryList.add(pe);
					}
				}

			}
		});

		// dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {
		//
		// @Override
		// public void onFailure(Throwable caught) {
		// caught.printStackTrace();
		// }
		//
		// @Override
		// public void onSuccess(ArrayList<DistrictEntry> result) {
		// districtEntryList.clear();
		for (DistrictEntry pe : StaticTables.getInstance().getDistrictEntries().values()) {
			districtEntryList.add(pe);
		}
		if (ornamentCaveRelationEntry != null) {

			districtComboBox.setValue(ornamentCaveRelationEntry.getDistrict());
			ValueChangeEvent.fire(districtComboBox, ornamentCaveRelationEntry.getDistrict());

		}
		// }
		// });

		dbService.getOrientations(new AsyncCallback<ArrayList<OrientationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrientationEntry> result) {
				orientation.clear();
				selectedorientation.clear();
				if (ornamentCaveRelationEntry != null) {
					for (OrientationEntry pe : result) {

						int count = 0;
						for (OrientationEntry oe : ornamentCaveRelationEntry.getOrientations()) {
							if (pe.getOrientationID() != oe.getOrientationID()) {
								count++;
							}
							if (count == ornamentCaveRelationEntry.getOrientations().size()) {
								orientation.add(pe);
							}
						}
					}
					for (OrientationEntry oe : ornamentCaveRelationEntry.getOrientations()) {
						selectedorientation.add(oe);
					}
				} else {
					for (OrientationEntry pe : result) {
						orientation.add(pe);
					}
				}

			}
		});

		setWidget(createForm());

	}

	public Widget createForm() {
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

		header = new FramedPanel();

		header.setHeading("Select District");
		header.add(districtComboBox);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		ValueChangeHandler<DistrictEntry> districtSelectionHandler = new ValueChangeHandler<DistrictEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<DistrictEntry> event) {
				int p = event.getValue().getDistrictID();
				caveEntryList.clear();
				caveType.clear();
				caveEntryComboBox.reset();
				wallsListStore.clear();
				style.clear();

				dbService.getCavesbyDistrictID(p, new AsyncCallback<ArrayList<CaveEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<CaveEntry> result) {
						for (CaveEntry pe : result) {
							caveEntryList.add(pe);
						}
						if (ornamentCaveRelationEntry != null) {
							caveEntryComboBox.setValue(ornamentCaveRelationEntry.getCave());
						}
					}
				});

			}

		};

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

		caveEntryComboBox.setTypeAhead(true);
		caveEntryComboBox.setEditable(false);
		caveEntryComboBox.setTriggerAction(TriggerAction.ALL);

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

		style.setEnabled(false);
		header = new FramedPanel();

		header.setHeading("Style in which it is used");
		header.add(style);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		TextButton addWalls = new TextButton("Select Walls");

		ClickHandler addWallsClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (districtComboBox.getValue() == null) {
					Window.alert("Please select the District first");
					return;
				}
				if (caveEntryComboBox.getValue() == null) {
					Window.alert("Please select the Cave first");
					return;
				}
				OrnamentWallAttributes attributespopup = new OrnamentWallAttributes(caveEntryComboBox.getValue());
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
		header.add(selectedWallsHorizontalPanel);
		vlcCave.add(header, new VerticalLayoutData(0.5, .125));

		wallList = new ListView<WallOrnamentCaveRelation, String>(wallsListStore, wallRelationProps.name());
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

		VerticalLayoutContainer vlcAttributes = new VerticalLayoutContainer();

		HorizontalLayoutContainer orientationHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrientationEntry, String> orientationView = new ListView<OrientationEntry, String>(orientation, orientationProps.nameEN());
		ListView<OrientationEntry, String> selectedOrientationView = new ListView<OrientationEntry, String>(selectedorientation,
				orientationProps.nameEN());
		orientationHorizontalPanel.add(orientationView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		orientationHorizontalPanel.add(selectedOrientationView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrientationEntry>(orientationView).setGroup("Orientation");
		new ListViewDragSource<OrientationEntry>(selectedOrientationView).setGroup("Orientation");

		new ListViewDropTarget<OrientationEntry>(selectedOrientationView).setGroup("Orientation");
		new ListViewDropTarget<OrientationEntry>(orientationView).setGroup("Orientation");

		header = new FramedPanel();
		header.setHeading("Select Orientation");
		if (ornamentCaveRelationEntry != null) {
			for (int i = 0; i < ornamentCaveRelationEntry.getOrientations().size(); i++) {
				selectedorientation.add(ornamentCaveRelationEntry.getOrientations().get(i));

			}
		}
		header.add(orientationHorizontalPanel);

		vlcAttributes.add(header, new VerticalLayoutData(0.5, .4));

		final TextField colours = new TextField();
		colours.setAllowBlank(true);
		header = new FramedPanel();

		header.setHeading("Colours");
		if (ornamentCaveRelationEntry != null) {
			colours.setText(ornamentCaveRelationEntry.getColours());
		}
		header.add(colours);
		vlcAttributes.add(header, new VerticalLayoutData(0.5, .125));

		final TextField notes = new TextField();
		colours.setAllowBlank(true);
		header = new FramedPanel();

		if (ornamentCaveRelationEntry != null) {
			notes.setText(ornamentCaveRelationEntry.getNotes());
		}
		header.setHeading("Notes");
		header.add(notes);
		vlcAttributes.add(header, new VerticalLayoutData(0.5, .125));

		FramedPanel attributes = new FramedPanel();
		attributes.setHeading("Attributes");
		attributes.add(vlcAttributes);
		tabPanel.add(attributes, "Attributes");

		FramedPanel relationToOtherOrnaments = new FramedPanel();
		relationToOtherOrnaments.setHeading("Similar or related elements or ornaments");
		VerticalLayoutContainer vlcRelationToTherornaments1 = new VerticalLayoutContainer();
		VerticalLayoutContainer vlcRelationToTherornaments2 = new VerticalLayoutContainer();
		HorizontalLayoutContainer backgroundHorizontalPanel = new HorizontalLayoutContainer();

		relationToOtherOrnaments.add(backgroundHorizontalPanel);
		backgroundHorizontalPanel.add(vlcRelationToTherornaments1, new HorizontalLayoutData(.5, 1.0));
		backgroundHorizontalPanel.add(vlcRelationToTherornaments2, new HorizontalLayoutData(.5, 1.0));

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

		header = new FramedPanel();
		header.setHeading("Select similar ornaments");
		header.add(similarOrnamentsHorizontalPanel);
		vlcRelationToTherornaments2.add(header, new VerticalLayoutData(1.0, .4));

		selector = new PictorialElementSelectorObjects();
		header = new FramedPanel();
		header.setHeading("Select similar elements");
		if (ornamentCaveRelationEntry != null) {
			selector.setOrnamentCaveRelationEntry(ornamentCaveRelationEntry);
		}
		header.add(selector.asWidget());
		vlcRelationToTherornaments2.add(header, new VerticalLayoutData(1, .4));

		final TextField groupOfOrnaments = new TextField();
		groupOfOrnaments.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Group of Ornaments");
		if (ornamentCaveRelationEntry != null) {
			groupOfOrnaments.setText(ornamentCaveRelationEntry.getGroup());
		}
		header.add(groupOfOrnaments);
		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .125));

		final TextField relatedElementsofOtherCultures = new TextField();

		header = new FramedPanel();
		header.setHeading("Describe related elements of other cultures");
		if (ornamentCaveRelationEntry != null) {
			relatedElementsofOtherCultures.setText(ornamentCaveRelationEntry.getRelatedelementeofOtherCultures());
		}
		header.add(relatedElementsofOtherCultures);
		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .125));

		final TextField similarElementsofOtherCultures = new TextField();

		header = new FramedPanel();
		if (ornamentCaveRelationEntry != null) {
			similarElementsofOtherCultures.setText(ornamentCaveRelationEntry.getSimilarelementsOfOtherCultures());
		}
		header.setHeading("Describe similar elements of other cultures");
		header.add(similarElementsofOtherCultures);
		vlcRelationToTherornaments1.add(header, new VerticalLayoutData(1, .125));

		tabPanel.add(relationToOtherOrnaments, "Relations");

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		TextButton save = new TextButton("save");
		FramedPanel panel = new FramedPanel();
		TextButton cancel = new TextButton("cancel");
		panel.addButton(save);
		panel.addButton(cancel);
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
					Window.alert("Please select a District");
					return;
				}
				if (caveEntryComboBox.getValue() == null) {
					Window.alert("Please select a Cave");
					return;
				}
				ornamentCaveRelation.setName("Cave: " + caveEntryComboBox.getValue().getCaveID());
				ornamentCaveRelation.setCave(caveEntryComboBox.getValue());
				ornamentCaveRelation.setDistrict(districtComboBox.getValue());
				ornamentCaveRelation.setColours(colours.getText());
				ornamentCaveRelation.setGroup(groupOfOrnaments.getText());

				ornamentCaveRelation.getOrientations().clear();
				for (int i = 0; i < selectedorientation.size(); i++) {
					ornamentCaveRelation.getOrientations().add(selectedorientation.get(i));
				}
				ornamentCaveRelation.setNotes(notes.getText());
				List<OrnamentEntry> relatedOrnaments = selectedRedlatedOrnaments.getAll();
				ornamentCaveRelation.getRelatedOrnamentsRelations().clear();
				for (OrnamentEntry ornament : relatedOrnaments) {
					ornamentCaveRelation.getRelatedOrnamentsRelations().add(ornament);
				}
				ornamentCaveRelation.getSimilarOrnamentsRelations().clear();
				List<OrnamentEntry> similarOrnaments = selectedSimilarOrnaments.getAll();
				for (OrnamentEntry ornament : similarOrnaments) {
					ornamentCaveRelation.getSimilarOrnamentsRelations().add(ornament);
				}

				ornamentCaveRelation.getPictorialElements().clear();
				for (int i = 0; i < selector.getSelectedPE().size(); i++) {
					ornamentCaveRelation.getPictorialElements().add(selector.getSelectedPE().get(i));
				}

				ornamentCaveRelation.getWalls().clear();
				for (int i = 0; i < wallsListStore.size(); i++) {
					ornamentCaveRelation.getWalls().add(wallsListStore.get(i));
				}
				ornamentCaveRelation.setRelatedelementeofOtherCultures(relatedElementsofOtherCultures.getText());
				ornamentCaveRelation.setSimilarelementsOfOtherCultures(similarElementsofOtherCultures.getText());
				// set walls
				if (ornamentCaveRelationEntry == null) {
					ornamentic.getCaveOrnamentRelationList().add(ornamentCaveRelation);
				}

				popup.hide();

			}
		};
		save.addHandler(saveClickHandler, ClickEvent.getType());

		panel.setHeading("New Cave Relation");
		panel.add(caveAttributesVerticalPanel);
		return panel;

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

	interface OrientationViewTemplates extends XTemplates {
		@XTemplate("<div>{nameEN}</div>")
		SafeHtml orientation(String nameEN);
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

	interface OrientationProperties extends PropertyAccess<OrientationEntry> {
		ModelKeyProvider<OrientationEntry> orientationID();

		ValueProvider<OrientationEntry, String> nameEN();
	}

	interface MainTypologicalClassProperties extends PropertyAccess<MainTypologicalClass> {
		ModelKeyProvider<MainTypologicalClass> mainTypologicalClassID();

		LabelProvider<MainTypologicalClass> name();
	}

	interface StructureOrganizationProperties extends PropertyAccess<StructureOrganization> {
		ModelKeyProvider<StructureOrganization> structureOrganizationID();

		LabelProvider<StructureOrganization> name();
	}

	interface OrnamentCaveTypeProperties extends PropertyAccess<OrnamentCaveType> {
		ModelKeyProvider<OrnamentCaveType> ornamentCaveTypeID();

		LabelProvider<OrnamentCaveType> name();
	}

	public Ornamentic getOrnamentic() {
		return ornamentic;
	}

	public void setOrnamentic(Ornamentic ornamentic) {
		this.ornamentic = ornamentic;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {

		return createForm();
	}

	interface WallRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<WallOrnamentCaveRelation> wallLocationID();

		ValueProvider<WallOrnamentCaveRelation, String> name();
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

}
