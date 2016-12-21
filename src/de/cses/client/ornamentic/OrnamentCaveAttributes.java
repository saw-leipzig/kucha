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
package de.cses.client.ornamentic;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;

public class OrnamentCaveAttributes extends PopupPanel {

	private ComboBox<CaveEntry> caveEntryComboBox;
	private ListStore<CaveEntry> caveEntryList;
	private CaveEntryProperties caveEntryProps;
	private ListStore<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesEntryList;
	private OrnamentOfOtherCulturesEntryProperties ornamentOfOtherCulturesEntryProps;
	private OrnamentOfOtherCulturesProperties ornamentOfOtherCulturesEntryPropierties;
	private ListStore<DistrictEntry> districtEntryList;
	private DistrictEntryProperties districtEntryProps;
	private ListStore<OrnamentEntry> ornamentEntryList;
	private OrnamentEntryProperties ornamentEntryProps;
	private OrnamentEntryProps ornamenEntryProperties;
	private ComboBox<OrnamentEntry> relationToOtherOrnamentsComboBox;
	private ComboBox<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesComboBox;
	private ComboBox<OrnamentEntry> similarOtherOrnamentsComboBox;
	private ComboBox<DistrictEntry> districtComboBox;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PopupPanel popup = this;
	private ArrayList<Integer> relatedOrnaments = new ArrayList<Integer>();
	private ArrayList<Integer> similarOrnaments = new ArrayList<Integer>();
	private ArrayList<Integer> otherCulturalOrnaments = new ArrayList<Integer>();
	private Ornamentic ornamentic;
	private ListStore<OrnamentEntry> choosenOrnamentEntrysimilarList;
	private ListStore<OrnamentEntry> choosenOrnamentEntryrelatedList;
	private ListStore<OrnamentOfOtherCulturesEntry> choosenOrnamentOfOtherCulturesEntryList;

	public OrnamentCaveAttributes() {
		super(false);
		caveEntryProps = GWT.create(CaveEntryProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveEntryProps.caveID());

		districtEntryProps = GWT.create(DistrictEntryProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtEntryProps.districtID());

		ornamentEntryProps = GWT.create(OrnamentEntryProperties.class);
		ornamentEntryList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamenEntryProperties = GWT.create(OrnamentEntryProps.class);

		ornamentOfOtherCulturesEntryPropierties = GWT.create(OrnamentOfOtherCulturesProperties.class);

		ornamentOfOtherCulturesEntryProps = GWT.create(OrnamentOfOtherCulturesEntryProperties.class);
		ornamentOfOtherCulturesEntryList = new ListStore<OrnamentOfOtherCulturesEntry>(
				ornamentOfOtherCulturesEntryProps.ornamentOfOtherCulturesID());

		choosenOrnamentEntrysimilarList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		choosenOrnamentOfOtherCulturesEntryList = new ListStore<OrnamentOfOtherCulturesEntry>(
				ornamentOfOtherCulturesEntryProps.ornamentOfOtherCulturesID());
		choosenOrnamentEntryrelatedList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());

		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				ornamentEntryList.clear();
				for (OrnamentEntry pe : result) {
					ornamentEntryList.add(pe);
				}
			}
		});

		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				districtEntryList.clear();
				for (DistrictEntry pe : result) {
					districtEntryList.add(pe);
				}
			}
		});

		dbService.getOrnamentsOfOtherCultures(new AsyncCallback<ArrayList<OrnamentOfOtherCulturesEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentOfOtherCulturesEntry> result) {
				ornamentOfOtherCulturesEntryList.clear();
				for (OrnamentOfOtherCulturesEntry pe : result) {
					ornamentOfOtherCulturesEntryList.add(pe);
				}
			}
		});

		setWidget(createForm());

	}

	public Widget createForm() {
		HorizontalPanel panelhorizontal = new HorizontalPanel();

		VerticalPanel ornamentCaveAttributesPanel = new VerticalPanel();
		VerticalPanel ornamentCaveAttributesPanelGrids = new VerticalPanel();
		VerticalPanel ornamentCaveAttributesPanel2 = new VerticalPanel();

		panelhorizontal.add(ornamentCaveAttributesPanel);
		panelhorizontal.add(ornamentCaveAttributesPanel2);
		panelhorizontal.add(ornamentCaveAttributesPanelGrids);

		VBoxLayoutContainer vlcCave = new VBoxLayoutContainer();

		FramedPanel cavesFrame = new FramedPanel();
		ornamentCaveAttributesPanel.add(cavesFrame);
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

		vlcCave.add(new FieldLabel(districtComboBox, "Select District"));

		SelectionHandler<DistrictEntry> districtSelectionHandler = new SelectionHandler<DistrictEntry>() {

			@Override
			public void onSelection(SelectionEvent<DistrictEntry> event) {
				int p = event.getSelectedItem().getDistrictID();
				caveEntryList.clear();

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
					}
				});

			}

		};

		districtComboBox.addSelectionHandler(districtSelectionHandler);

		caveEntryComboBox = new ComboBox<CaveEntry>(caveEntryList, caveEntryProps.officialNumber(),
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
		caveEntryComboBox.setEmptyText("select cave");
		caveEntryComboBox.setTypeAhead(false);
		caveEntryComboBox.setEditable(false);
		caveEntryComboBox.setTriggerAction(TriggerAction.ALL);

		vlcCave.add(new FieldLabel(caveEntryComboBox, "Select Cave"));
		final TextField caveType = new TextField();
		caveType.setAllowBlank(false);
		caveType.setEnabled(false);
		vlcCave.add(new FieldLabel(caveType, "Cave type"));

		SelectionHandler<CaveEntry> caveSelectionHandler = new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				int p = event.getSelectedItem().getCaveTypeID();
				dbService.getCaveTypebyID(p, new AsyncCallback<CaveTypeEntry>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(CaveTypeEntry result) {
						caveType.setText(result.getNameEN());
					}
				});

			}

		};

		caveEntryComboBox.addSelectionHandler(caveSelectionHandler);

		VBoxLayoutContainer vlcAttributes = new VBoxLayoutContainer();

		final TextField style = new TextField();
		style.setAllowBlank(true);
		vlcAttributes.add(new FieldLabel(style, "Style in which it is used"));

		final TextField group = new TextField();
		group.setAllowBlank(true);
		vlcAttributes.add(new FieldLabel(group, "Group of ornaments"));

		final TextField orientation = new TextField();
		orientation.setAllowBlank(true);
		vlcAttributes.add(new FieldLabel(orientation, "Orientation"));

		final TextField colours = new TextField();
		colours.setAllowBlank(true);
		vlcAttributes.add(new FieldLabel(colours, "Colours used"));

		FramedPanel attributes = new FramedPanel();
		attributes.setHeading("Attributes");
		attributes.add(vlcAttributes);
		ornamentCaveAttributesPanel.add(attributes);

		FramedPanel occupiedPosition = new FramedPanel();
		occupiedPosition.setHeading("Occupied Position");
		VBoxLayoutContainer vlcOccupiedPosition = new VBoxLayoutContainer();
		occupiedPosition.add(vlcOccupiedPosition);
		ornamentCaveAttributesPanel.add(occupiedPosition);

		final TextField position = new TextField();
		position.setAllowBlank(true);
		vlcOccupiedPosition.add(new FieldLabel(position, "Position"));

		final TextField function = new TextField();
		function.setAllowBlank(true);
		vlcOccupiedPosition.add(new FieldLabel(function, "Function"));

		final TextField cavePart = new TextField();
		cavePart.setAllowBlank(true);
		vlcOccupiedPosition.add(new FieldLabel(cavePart, "Cave Part"));

		final TextField notes = new TextField();
		notes.setAllowBlank(true);
		vlcOccupiedPosition.add(new FieldLabel(notes, "Notes"));

		FramedPanel relationToOtherOrnaments = new FramedPanel();
		relationToOtherOrnaments.setHeading("Similar or related ornaments");
		VBoxLayoutContainer vlcRelationToTherornaments = new VBoxLayoutContainer();

		relationToOtherOrnaments.add(vlcRelationToTherornaments);

		TextField groupOfOrnaments = new TextField();
		groupOfOrnaments.setAllowBlank(true);
		vlcRelationToTherornaments.add(new FieldLabel(groupOfOrnaments, "Group of Ornaments"));

		relationToOtherOrnamentsComboBox = new ComboBox<OrnamentEntry>(ornamentEntryList, ornamentEntryProps.code(),
				new AbstractSafeHtmlRenderer<OrnamentEntry>() {

					@Override
					public SafeHtml render(OrnamentEntry item) {
						final OrnamentViewTemplates pvTemplates = GWT.create(OrnamentViewTemplates.class);
						return pvTemplates.ornament(item.getCode());
					}
				});
		vlcRelationToTherornaments.add(new FieldLabel(relationToOtherOrnamentsComboBox, "Select related ornaments"));

		TextButton addRelatedOrnamentButton = new TextButton("Add Ornament");
		vlcRelationToTherornaments.add(addRelatedOrnamentButton);

		ColumnConfig<OrnamentOfOtherCulturesEntry, String> nameCol = new ColumnConfig<OrnamentOfOtherCulturesEntry, String>(
				ornamentOfOtherCulturesEntryPropierties.name(), 200, "Ornament Name");
		List<ColumnConfig<OrnamentOfOtherCulturesEntry, ?>> columns = new ArrayList<ColumnConfig<OrnamentOfOtherCulturesEntry, ?>>();
		columns.add(nameCol);
		ColumnModel<OrnamentOfOtherCulturesEntry> cm = new ColumnModel<OrnamentOfOtherCulturesEntry>(columns);
		Grid<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesGrid = new Grid<OrnamentOfOtherCulturesEntry>(
				choosenOrnamentOfOtherCulturesEntryList, cm);
		FramedPanel ornamentsOfOtherChoosenFrame = new FramedPanel();
		ornamentsOfOtherChoosenFrame.setHeading("Choosen Ornaments of other Cultures");
		ornamentsOfOtherChoosenFrame.add(ornamentOfOtherCulturesGrid);
		ornamentCaveAttributesPanelGrids.add(ornamentsOfOtherChoosenFrame);

		ColumnConfig<OrnamentEntry, String> nameColsimilarOrnament = new ColumnConfig<OrnamentEntry, String>(
				ornamenEntryProperties.code(), 200, "Ornament Name");
		List<ColumnConfig<OrnamentEntry, ?>> columnsSimilar = new ArrayList<ColumnConfig<OrnamentEntry, ?>>();
		columnsSimilar.add(nameColsimilarOrnament);
		ColumnModel<OrnamentEntry> cmSimilar = new ColumnModel<OrnamentEntry>(columnsSimilar);
		Grid<OrnamentEntry> ornamentGridsimilar = new Grid<OrnamentEntry>(choosenOrnamentEntrysimilarList, cmSimilar);
		FramedPanel ornamentsSimilarFrame = new FramedPanel();
		ornamentsSimilarFrame.setHeading("Choosen similar ornaments");
		ornamentsSimilarFrame.add(ornamentGridsimilar);
		ornamentCaveAttributesPanelGrids.add(ornamentsSimilarFrame);

		ColumnConfig<OrnamentEntry, String> nameColrelatedOrnament = new ColumnConfig<OrnamentEntry, String>(
				ornamenEntryProperties.code(), 200, "Ornament Name");
		List<ColumnConfig<OrnamentEntry, ?>> columnsrelated = new ArrayList<ColumnConfig<OrnamentEntry, ?>>();
		columnsrelated.add(nameColrelatedOrnament);
		ColumnModel<OrnamentEntry> cmRelated = new ColumnModel<OrnamentEntry>(columnsrelated);
		Grid<OrnamentEntry> ornamentGridrelated = new Grid<OrnamentEntry>(choosenOrnamentEntryrelatedList, cmRelated);
		FramedPanel ornamentsRelatedFrame = new FramedPanel();
		ornamentsRelatedFrame.setHeading("Choosen related ornaments");
		ornamentsRelatedFrame.add(ornamentGridrelated);
		ornamentCaveAttributesPanelGrids.add(ornamentsRelatedFrame);

		ClickHandler addRelatedOrnamentButtonClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				try {
					relatedOrnaments.add(relationToOtherOrnamentsComboBox.getValueOrThrow().getOrnamentID());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					choosenOrnamentEntryrelatedList.add(relationToOtherOrnamentsComboBox.getValueOrThrow());
				} catch (ParseException e) {
					e.printStackTrace();
					Window.alert("error in resolving element");
				}

			}
		};
		addRelatedOrnamentButton.addHandler(addRelatedOrnamentButtonClickHandler, ClickEvent.getType());

		similarOtherOrnamentsComboBox = new ComboBox<OrnamentEntry>(ornamentEntryList, ornamentEntryProps.code(),
				new AbstractSafeHtmlRenderer<OrnamentEntry>() {

					@Override
					public SafeHtml render(OrnamentEntry item) {
						final OrnamentViewTemplates pvTemplates = GWT.create(OrnamentViewTemplates.class);
						return pvTemplates.ornament(item.getCode());
					}
				});
		vlcRelationToTherornaments.add(new FieldLabel(similarOtherOrnamentsComboBox, "Select similar ornaments"));

		TextButton addSimilarOrnamentButton = new TextButton("Add Ornament");
		vlcRelationToTherornaments.add(addSimilarOrnamentButton);

		ClickHandler similarOtherOrnamentsButtonClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				similarOtherOrnamentsComboBox.setAllowTextSelection(true);
				OrnamentEntry ornament2;
				try {
					ornament2 = similarOtherOrnamentsComboBox.getValueOrThrow();
					choosenOrnamentEntrysimilarList.add(ornament2);
					similarOrnaments.add(ornament2.getOrnamentID());
				} catch (ParseException e) {
					Window.alert("error on resolving element");
					e.printStackTrace();
				}

			}
		};
		addSimilarOrnamentButton.addHandler(similarOtherOrnamentsButtonClickHandler, ClickEvent.getType());

		ornamentOfOtherCulturesComboBox = new ComboBox<OrnamentOfOtherCulturesEntry>(ornamentOfOtherCulturesEntryList,
				ornamentOfOtherCulturesEntryProps.name(), new AbstractSafeHtmlRenderer<OrnamentOfOtherCulturesEntry>() {

					@Override
					public SafeHtml render(OrnamentOfOtherCulturesEntry item) {
						final OrnamentOfOtherCulturesEntryViewTemplates pvTemplates = GWT
								.create(OrnamentOfOtherCulturesEntryViewTemplates.class);
						return pvTemplates.ornamentOfOtherCultures(item.getName());
					}
				});
		vlcRelationToTherornaments.add(new FieldLabel(ornamentOfOtherCulturesComboBox, "Select similar ornaments of other cultures"));

		TextButton addOrnamentOfOtherCulturesButton = new TextButton("Add Ornament");
		vlcRelationToTherornaments.add(addOrnamentOfOtherCulturesButton);

		ClickHandler addOrnamentOfOtherCulturesButtonClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				try {
					otherCulturalOrnaments.add(ornamentOfOtherCulturesComboBox.getValueOrThrow().getOrnamentOfOtherCulturesID());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					choosenOrnamentOfOtherCulturesEntryList.add(ornamentOfOtherCulturesComboBox.getValueOrThrow());
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Window.alert("error on resolving element");
				}

			}
		};
		addOrnamentOfOtherCulturesButton.addHandler(addOrnamentOfOtherCulturesButtonClickHandler, ClickEvent.getType());

		ornamentCaveAttributesPanel2.add(relationToOtherOrnaments);

		HorizontalPanel buttonsPanel = new HorizontalPanel();

		TextButton save = new TextButton("save");

		TextButton cancel = new TextButton("cancel");
		buttonsPanel.add(save);
		buttonsPanel.add(cancel);
		ornamentCaveAttributesPanel.add(buttonsPanel);

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
				OrnamentCaveRelation ornamentCaveRelation = new OrnamentCaveRelation();
				ornamentCaveRelation.setCaveID(caveEntryComboBox.getValue().getCaveID());
				ornamentCaveRelation.setName("Cave "+caveEntryComboBox.getValue().getOfficialNumber());
				ornamentCaveRelation.setCavepart(cavePart.getText());
				ornamentCaveRelation.setFunction(function.getText());
				ornamentCaveRelation.setColours(colours.getText());
				ornamentCaveRelation.setGroup(Integer.parseInt(group.getText()));
				ornamentCaveRelation.setOrientation(orientation.getText());
				ornamentCaveRelation.setPosition(position.getText());
				ornamentCaveRelation.setNotes(notes.getText());
				ornamentCaveRelation.setStyle(Integer.parseInt(style.getText()));
				ornamentCaveRelation.setOtherCulturalOrnamentsRelationID(otherCulturalOrnaments);
				ornamentCaveRelation.setRelatedOrnamentsRelationID(relatedOrnaments);
				ornamentCaveRelation.setSimilarOrnamentsRelationID(similarOrnaments);
				ornamentic.getCaveOrnamentRelationList().add(ornamentCaveRelation);
				// doenst work, listview still needs more space

				popup.hide();
			}
		};
		save.addHandler(saveClickHandler, ClickEvent.getType());

		FramedPanel panel = new FramedPanel();
		panel.setHeading("New Cave Relation");
		panel.add(panelhorizontal);

		return panel;

	}

	interface CaveEntryProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();

		LabelProvider<CaveEntry> officialNumber();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{officialNumber}<br>{officialName}</div>")
		SafeHtml caveLabel(String officialNumber, String officialName);

		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);
	}

	interface OrnamentEntryProperties extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> OrnamentID();

		LabelProvider<OrnamentEntry> code();
	}

	interface OrnamentOfOtherCulturesEntryProperties extends PropertyAccess<OrnamentOfOtherCulturesEntry> {
		ModelKeyProvider<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesID();

		LabelProvider<OrnamentOfOtherCulturesEntry> name();
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

	public ComboBox<OrnamentEntry> getRelationToOtherOrnamentsComboBox() {
		return relationToOtherOrnamentsComboBox;
	}

	public void setRelationToOtherOrnamentsComboBox(ComboBox<OrnamentEntry> relationToOtherOrnamentsComboBox) {
		this.relationToOtherOrnamentsComboBox = relationToOtherOrnamentsComboBox;
	}

	public ComboBox<OrnamentEntry> getSimilarOtherOrnamentsComboBox() {
		return similarOtherOrnamentsComboBox;
	}

	public void setSimilarOtherOrnamentsComboBox(ComboBox<OrnamentEntry> similarOtherOrnamentsComboBox) {
		this.similarOtherOrnamentsComboBox = similarOtherOrnamentsComboBox;
	}

	public Ornamentic getOrnamentic() {
		return ornamentic;
	}

	public void setOrnamentic(Ornamentic ornamentic) {
		this.ornamentic = ornamentic;
	}

	interface OrnamentOfOtherCulturesProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesID();

		ValueProvider<OrnamentOfOtherCulturesEntry, String> name();
	}

	interface OrnamentEntryProps extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> OrnamentID();

		ValueProvider<OrnamentEntry, String> code();
	}

}
