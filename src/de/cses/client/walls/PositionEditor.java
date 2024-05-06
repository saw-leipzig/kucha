/*
 * Copyright 2017 
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
package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.ImageXTemplate;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.CoordinateEntry;
import de.cses.shared.CoordinatesEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.UserEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallTreeEntry;

/**
 * @author Erik
 *
 */
public class PositionEditor {

	private WallTree wallTree;
	private CaveEntry entry;
	private DepictionEntry correspondingDepictionEntry = null;
	private WallViewListener wvl;
	private WallView wv;;
	private List<WallTreeEntry> walls;
	int init = 0;
	final PositionViewTemplates pvTemplates = GWT.create(PositionViewTemplates.class);
	private FramedPanel selectWallFP = null;
	public PopupPanel popup = new PopupPanel();
	private String heading = "";
	private boolean isWallDimension = false;
	private boolean isCheckable = true;
	PositionEntry peEdit = null;
	NumberField<Integer> register = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
	NumberField<Integer> number = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
	SimpleComboBox<Integer> directionNF = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {

		@Override
		public String getLabel(Integer item) {
			return WallDimensionEntry.DIRECTION_LABEL.get(item);
		}
	});
	SimpleComboBox<Integer> typeNF = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {

		@Override
		public String getLabel(Integer item) {
			return WallDimensionEntry.TYPE_LABEL.get(item);
		}
	});

	interface WallDimensionProperties extends PropertyAccess<WallDimensionEntry> {
		ModelKeyProvider<WallDimensionEntry> wallDimensionID();

		LabelProvider<PositionEntry> name();
	}

	interface CoordinatesProperties extends PropertyAccess<CoordinatesEntry> {
		ModelKeyProvider<CoordinatesEntry> coordinatesID();

		LabelProvider<CoordinatesEntry> name();
	}

	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionView(String name);
	}

	public PositionEditor(DepictionEntry de) {
		this(de.getCave(), de.getWalls(), "Add Position in Cave", true, false);
		this.correspondingDepictionEntry = de;
	}

	public PositionEditor(CaveEntry entry, List<WallTreeEntry> entries, String heading, boolean isCheckable,
			boolean isWallDimension) {
		this.heading = heading;
		this.entry = entry;
		this.walls = entries;
		this.isWallDimension = isWallDimension;
		this.isCheckable = isCheckable;
		Collection<WallTreeEntry> wtes = StaticTables.getInstance().getWallTreeEntries().values();
		if (entry != null) {			
			for (WallEntry wte : entry.getWallList()) {
				if (entry != null) {
					wte.setDimensions(entry.getDimensionsByWall(wte.getWallLocationID()));
				}
			}
		}
		if (isCheckable) {
			wallTree = new WallTree(wtes, walls, false, true, entry);
		} else {
			wallTree = new WallTree(wtes, walls, false, false, entry);
		}

	}

	public ArrayList<WallTreeEntry> getSelectedWalls() {
		if (this.isCheckable) {
			return new ArrayList<WallTreeEntry>(wallTree.wallTree.getCheckedSelection());
		} else {
			ArrayList<WallTreeEntry> res = new ArrayList<WallTreeEntry>(wallTree.wallTree.getStore().getAll());
			return res;
		}

	}

	public ArrayList<WallTreeEntry> getAllWalls() {
		return new ArrayList<WallTreeEntry>(wallTree.wallTree.getStore().getAll());
	}

	public void enablePoistionVisualization() {
		class CustomImageCell extends AbstractCell<String> {
			private ImageXTemplate imageTemplate = GWT.create(ImageXTemplate.class);
			private WallTree wallTree;

			public CustomImageCell(String... consumedEvents) {
				super(consumedEvents);
			}

			public CustomImageCell(Set<String> consumedEvents, WallTree wallTree) {
				super(consumedEvents);
				this.wallTree = wallTree;
			}

			@Override
			public void render(Context context, String ie, SafeHtmlBuilder sb) {

				sb.append(SafeHtmlUtils.fromTrustedString(ie));

			}

			@Override
			public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
					ValueUpdater<String> valueUpdater) {
				String eventType = event.getType();
				if (BrowserEvents.MOUSEOVER.equals(eventType)) {

				}

				if (BrowserEvents.MOUSEOUT.equals(eventType)) {

				}
				if (BrowserEvents.DBLCLICK.equals(eventType)) {
					wv.setWall(this.wallTree.wallTree.getStore().findModelWithKey(context.getKey().toString()));
				}

				if (BrowserEvents.KEYDOWN.equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
				}
			}

			private void showPOPUP(Context context, int x, int y) {

			}

			private void hidePOPUP() {
			}

		}
		Set<String> events = new HashSet<String>();
		events.add(BrowserEvents.MOUSEOVER);
		events.add(BrowserEvents.MOUSEOUT);
		events.add(BrowserEvents.DBLCLICK);
		Cell<String> cCell = new CustomImageCell(events, this.wallTree);
		wallTree.wallTree.setCell(cCell);
	}

	public WallTree getWallTree() {
		return this.wallTree;
	}

	public void setWall(WallTreeEntry newWte) {
		WallTreeEntry oldWte = wallTree.wallTree.getStore()
				.findModelWithKey(Integer.toString(newWte.getWallLocationID()));
		ArrayList<WallTreeEntry> children = oldWte.getChildren();
		WallTreeEntry parent = wallTree.wallTree.getStore().getParent(oldWte);
		// wallTree.wallTree.getStore().remove(oldWte);
		// wallTree.wallTree.getStore().add(parent, newWte);
		wallTree.wallTree.getStore().update(newWte);
	}

	public WallTreeEntry getWallByID(Integer ID) {
		return wallTree.wallTree.getStore().findModelWithKey(Integer.toString(ID));
	}

	public FramedPanel getPE() {
		if (selectWallFP == null) {
			return createForm();
		} else {
			return selectWallFP;
		}
	}

	private FramedPanel createForm() {

		wvl = new WallViewListener() {

			@Override
			public void saveWall(WallTreeEntry wte) {
				if (correspondingDepictionEntry != null) {
					Boolean found = false;
					if (wte.getDimensions() != null) {
						for (WallDimensionEntry wde : wte.getDimensions()) {
							if (wde.getCoordinates() != null) {
								for (CoordinateEntry ce : wde.getCoordinates()) {
									if (ce.getDepictionID() == correspondingDepictionEntry.getDepictionID()) {
										wallTree.wallTree.getStore()
												.findModelWithKey(Integer.toString(wte.getWallLocationID()))
												.setDimensions(wte.getDimensions());
										wallTree.wallTree.getStore()
												.findModelWithKey(Integer.toString(wte.getWallLocationID()))
												.setPositions(wte.getPositions());
										wallTree.wallTree.setChecked(wallTree.wallTree.getStore().findModelWithKey(
												Integer.toString(wte.getWallLocationID())), CheckState.CHECKED);
									}
								}
							}
						}
					}
				}

				save(getSelectedWalls());

			}

		};
		wv = new WallView(wvl, correspondingDepictionEntry);
		selectWallFP = new FramedPanel();

		// Info.display("Ausgewählt:
		// ",wallTree.wallTree.getSelectionModel().getSelectedItem().getText());

		selectWallFP.add(wallTree.wallSelectorBLC);

		// PositionSelectionLV.set;

		// FramedPanel wallFP = new FramedPanel();
		// wallFP.setHeading("Select wall");
		// wallFP.add(positionComboBox);

		ValueChangeHandler<PositionEntry> positionSelectionHandler = new ValueChangeHandler<PositionEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PositionEntry> event) {

			}

		};

		selectWallFP.setSize("600px", "450px");
		ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		cancelTB.setToolTip(Util.createToolTip("close"));
		selectWallFP.setHeading(heading);
		cancelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				save(getSelectedWalls());
				popup.hide();
			}
		});

		ToolButton editTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		if (this.isWallDimension) {
			this.wallTree.wallTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			editTB.setToolTip(Util.createToolTip("Add Wall Register"));
			editTB.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					if (wallTree.wallTree.getSelectionModel().getSelectedItem().getChildren() != null && wallTree.wallTree.getSelectionModel().getSelectedItem().getChildren().size() == 0) {
						wv.setWall(wallTree.wallTree.getSelectionModel().getSelectedItem());
					} else {
						Info.display("Invalid Wall", "Only walls can be edited. Register Systems cannot be added to Parent-Objects");
					}

				}
			});
		} else {
			editTB.setToolTip(Util.createToolTip("Add a position to a Wall"));
			editTB.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					if (wallTree.wallTree.getSelectionModel().getSelectedItem().getChildren() != null && wallTree.wallTree.getSelectionModel().getSelectedItem().getChildren().size() == 0) {
						wv.setWall(wallTree.wallTree.getSelectionModel().getSelectedItem());
					} else {
						Info.display("Invalid Wall", "Only walls can be edited. Register Systems cannot be added to Parent-Objects");
					}
				}
			});
		}			
		ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveTB.setToolTip(Util.createToolTip("save"));
		saveTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				save(getSelectedWalls());
				popup.hide();
			}
		});
		selectWallFP.addTool(editTB);
		selectWallFP.addTool(cancelTB);
		return selectWallFP;
	}

	protected void save(ArrayList<WallTreeEntry> results) {
		for (WallTreeEntry wte : results) {
			this.wallTree.wallTree.getStore().findModelWithKey(Integer.toString(wte.getWallLocationID()))
					.setDimensions(wte.getDimensions());
			this.wallTree.wallTree.getStore().findModelWithKey(Integer.toString(wte.getWallLocationID()))
					.setPositions(wte.getPositions());
		}
	}

	public CaveEntry getCave() {
		return entry;
	}

	public void setCave(CaveEntry cave) {
		this.entry = cave;
	}

	interface CavePartProperties extends PropertyAccess<CavePart> {
		ModelKeyProvider<CavePart> cavePartID();

		LabelProvider<CavePart> name();
	}

	interface OrnamentPositionProperties extends PropertyAccess<OrnamentPositionEntry> {
		ModelKeyProvider<OrnamentPositionEntry> ornamentPositionID();

		LabelProvider<OrnamentPositionEntry> name();
	}

	interface OrnamentFunctionProperties extends PropertyAccess<OrnamentFunctionEntry> {
		ModelKeyProvider<OrnamentFunctionEntry> ornamentFunctionID();

		LabelProvider<OrnamentFunctionEntry> name();
	}

	interface WallProperties extends PropertyAccess<WallEntry> {
		ModelKeyProvider<WallEntry> wallID();

		LabelProvider<WallEntry> wallIDLabel();
	}

	interface OrnamentCavePartViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentCavePart(String name);
	}

	interface OrnamentPositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentPosition(String name);
	}

	interface OrnamentFunctionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentFunction(String name);
	}

	interface WallsViewTemplates extends XTemplates {
		@XTemplate("<div>{wallID}</div>")
		SafeHtml walls(int wallID);
	}

	public void show() {
		// Aufruf mit leeren Feldern f�r die Eingabe
		popup = new PopupPanel();
		// this.caveEntry =
		// OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();
		if (selectWallFP == null) {
			createForm();
		}
		popup.setWidget(selectWallFP);
		popup.center();

	}

	public List<WallTreeEntry> getSelectedItems() {
		return wallTree.wallTree.getSelectionModel().getSelectedItems();
	}

	public void selectChildren(boolean Children) {
		if (Children) {
			wallTree.wallTree.setCheckStyle(CheckCascade.CHILDREN);

		} else {
			wallTree.wallTree.setCheckStyle(CheckCascade.NONE);
		}
	}

	public void show(CaveEntry entry) {
		// Afruf mit Entry zum Bearbeiten
		init = 1;
//		this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		this.entry = entry;
		popup = new PopupPanel();

		popup.setWidget(createForm());
		popup.center();

	}
}
