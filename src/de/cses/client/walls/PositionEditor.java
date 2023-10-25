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

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.ImageXTemplate;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.CoordinatesEntry;
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

	private ListStore<WallDimensionEntry> wallDimensionEntryLS;
	private ListStore<CoordinatesEntry> coordinatesEntryLS;
	private ListView<WallDimensionEntry, WallDimensionEntry> PositionSelectionLV;
	private ListView<CoordinatesEntry, CoordinatesEntry> coordinatesLV;
	private CoordinatesProperties coordinatesProps;
	private WallDimensionProperties wallDimensionProps;
	private WallTree wallTree;
	private CaveEntry entry;
	private WallViewListener wvl;
	private WallView wv;;
	private List<WallTreeEntry> walls;
	private boolean setSearch;
	private CheckBox exact;
	int init= 0;
	final PositionViewTemplates pvTemplates = GWT.create(PositionViewTemplates.class);
	private FramedPanel selectWallFP = null;
	public PopupPanel popup = new PopupPanel();
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private String heading = "";
	private boolean isWallDimension = false;
	private Grid<PositionEntry> grid = null;
	private PositionDeimensionProperties positionDimensionProps = GWT.create(PositionDeimensionProperties.class);
	private ListStore<WallDimensionEntry> sourceStore;
	private SimpleComboBox<String> positionEntriesCB;
	private GridRowEditing<PositionEntry> editing;
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
	interface PositionDeimensionProperties extends PropertyAccess<PositionEntry> {
		@Path("positionID")
		ModelKeyProvider<PositionEntry> key();

		ValueProvider<PositionEntry, String> name();		
		ValueProvider<PositionEntry, Integer> type();
		ValueProvider<PositionEntry, Integer> direction();
		ValueProvider<PositionEntry, Integer> registers();
		ValueProvider<PositionEntry, Integer> columns();
	}
	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionView(String name);
	}
	public PositionEditor(CaveEntry entry, List<WallTreeEntry> entries, boolean setSearch) {
		this(entry, entries, setSearch, "Add Position in Cave", true, false);
	}

	public PositionEditor(CaveEntry entry, List<WallTreeEntry> entries, boolean setSearch, String heading, boolean isCheckable, boolean isWallDimension) {
		this.heading = heading;
		this.entry=entry;
		this.walls=entries;
		this.setSearch=setSearch;
		this.isWallDimension = isWallDimension;
		this.isCheckable = isCheckable;
		wallDimensionProps = GWT.create(WallDimensionProperties.class);
		
		wallDimensionEntryLS = new ListStore<WallDimensionEntry>(wallDimensionProps.wallDimensionID());
		if (isCheckable) {
			wallTree = new WallTree(StaticTables.getInstance().getWallTreeEntries().values(), walls, false, true, entry);			
		} else {
			wallTree = new WallTree(StaticTables.getInstance().getWallTreeEntries().values(), walls, false, false, entry);						
		}
		wvl = new WallViewListener() {

			@Override
			public void saveWall(WallTreeEntry wte) {
				save(getSelectedWalls());
				
			}
			
		};
		wv = new WallView(wvl);

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
		    public void onBrowserEvent(Context context, Element parent, String value,
		    	      NativeEvent event, ValueUpdater<String> valueUpdater) {
		    	    String eventType = event.getType();
		    	    if (BrowserEvents.MOUSEOVER.equals(eventType) ) {
		    	    	
		    	    }

		    	    
		    	    if (BrowserEvents.MOUSEOUT.equals(eventType)) {

			    	}
		    	    if (BrowserEvents.DBLCLICK.equals(eventType)) {
		    	    	Util.doLogging("Key pressed "+ this.wallTree.wallTree.getStore().findModelWithKey(context.getKey().toString()).getText());
		    	    	wv.setWall(this.wallTree.wallTree.getStore().findModelWithKey(context.getKey().toString()));
			    	 }

		    	    if (BrowserEvents.KEYDOWN.equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
			    	}
		    }
		    private void showPOPUP(Context context,int x,int y) {

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
		WallTreeEntry oldWte = wallTree.wallTree.getStore().findModelWithKey(Integer.toString(newWte.getWallLocationID()));
		ArrayList<WallTreeEntry> children = oldWte.getChildren();
		WallTreeEntry parent = wallTree.wallTree.getStore().getParent(oldWte); 
		//wallTree.wallTree.getStore().remove(oldWte);
		//wallTree.wallTree.getStore().add(parent, newWte);
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
		


		selectWallFP = new FramedPanel();

				//Info.display("Ausgewählt: ",wallTree.wallTree.getSelectionModel().getSelectedItem().getText());

		selectWallFP.add(wallTree.wallSelectorBLC);
	
		//PositionSelectionLV.set;
		
	
		//FramedPanel wallFP = new FramedPanel();
		//wallFP.setHeading("Select wall");
		//wallFP.add(positionComboBox);
	
		ValueChangeHandler<PositionEntry> positionSelectionHandler = new ValueChangeHandler<PositionEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PositionEntry> event) {
					

			}

		};

		


		selectWallFP.setSize("600px", "450px");
		if (setSearch) {
			ToolButton editWall = new ToolButton(new IconConfig("editButton", "editButtonOver"));
			editWall.setToolTip(Util.createToolTip("Edit wall", "Edit selected wall entry."));
			editWall.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					Util.doLogging("editing wall tree");
					if (wallTree.wallTree.getSelectionModel().getSelectedItem() == null) { // we can only add a new entry if there is a parent selected
						Util.doLogging("nothing selected");
						return;
					}
					WallTreeEntry wallTreeEntryToEdit = wallTree.wallTree.getSelectionModel().getSelectedItem();
					PopupPanel addwallTreeEntryDialog = new PopupPanel();
					FramedPanel newwallTreeEntryFP = new FramedPanel();
					TextArea ieTextArea = new TextArea();
					ieTextArea.addValidator(new MinLengthValidator(2));
					ieTextArea.addValidator(new MaxLengthValidator(256));
					ieTextArea.setValue(wallTreeEntryToEdit.getText());
					FramedPanel fpanelText = new FramedPanel();
					fpanelText.setHeading("Name");
					fpanelText.add(ieTextArea);
					TextArea ieTextAreaSearch = new TextArea();
					ieTextAreaSearch.addValidator(new MinLengthValidator(2));
					ieTextAreaSearch.addValidator(new MaxLengthValidator(256));
					ieTextAreaSearch.setValue(wallTreeEntryToEdit.getSearch());
					FramedPanel fpanelSearch = new FramedPanel();
					fpanelSearch.setHeading("Alternative Names");
					fpanelSearch.add(ieTextAreaSearch);
					VerticalLayoutContainer editIconogryphyVLC = new VerticalLayoutContainer();
					editIconogryphyVLC.add(fpanelText, new VerticalLayoutData(1.0, .5));
					editIconogryphyVLC.add(fpanelSearch, new VerticalLayoutData(1.0, .5));

					newwallTreeEntryFP.add(editIconogryphyVLC);
					newwallTreeEntryFP.setHeading("edit text");
					newwallTreeEntryFP.setSize("300px", "250px");
					ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
					saveTB.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							if (ieTextArea.isValid()) {
								wallTreeEntryToEdit.setText(ieTextArea.getValue());
								wallTreeEntryToEdit.setSearch(ieTextAreaSearch.getValue());
								wallTree.wallTreeStore.update(wallTreeEntryToEdit);
								dbService.updateWallTreeEntry(wallTreeEntryToEdit, new AsyncCallback<Boolean>() {

									@Override
									public void onFailure(Throwable caught) {
										caught.printStackTrace();
									}

									@Override
									public void onSuccess(Boolean result) {
										StaticTables.getInstance().reloadWallTree(); // we need to reload the whole tree otherwise this won't work
									}
								});
								addwallTreeEntryDialog.hide();
							}
						}

					});
					newwallTreeEntryFP.addTool(saveTB);
					ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
					cancelTB.addSelectHandler(new SelectHandler() {

						@Override
						public void onSelect(SelectEvent event) {
							addwallTreeEntryDialog.hide();
						}
					});
					newwallTreeEntryFP.addTool(cancelTB);
					addwallTreeEntryDialog.add(newwallTreeEntryFP);
					addwallTreeEntryDialog.setModal(true);
					addwallTreeEntryDialog.center();
			}
			});
			selectWallFP.addTool(editWall);
		}
		ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		if (setSearch) {
			cancelTB.setToolTip(Util.createToolTip("Close selection.", "Currently selected items will be used in the filter."));			
		}
		else {
			cancelTB.setToolTip(Util.createToolTip("close"));			
		}
		if (setSearch) {
			selectWallFP.setHeading("Select walls for search.");
		}
		else {
			selectWallFP.setHeading(heading);
		}
		
		cancelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (setSearch) {
					save(getSelectedWalls());
				}
				popup.hide();
			}
		});
		
		ToolButton editTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
//		PopupPanel editWallGrid = new PopupPanel();
//		FramedPanel positionFP = new FramedPanel();
//		positionFP.setHeading("Set Wall Layout");
//		ColumnConfig<PositionEntry, String> posCol = new ColumnConfig<PositionEntry, String>(positionDimensionProps.name(), 300, "Position");
//		ColumnConfig<PositionEntry, Integer> directionCol = new ColumnConfig<PositionEntry, Integer>(positionDimensionProps.direction(), 300, "Direction");
//		ColumnConfig<PositionEntry, Integer> typeCol = new ColumnConfig<PositionEntry, Integer>(positionDimensionProps.type(), 300, "Type");
//		ColumnConfig<PositionEntry, Integer> regCol = new ColumnConfig<PositionEntry, Integer>(positionDimensionProps.registers(), 300, "Register");
//		ColumnConfig<PositionEntry, Integer> colCol = new ColumnConfig<PositionEntry, Integer>(positionDimensionProps.columns(), 300, "Amount");
//	    List<ColumnConfig<PositionEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<PositionEntry, ?>>();
////	    sourceColumns.add(selectionModel.getColumn());
//	    sourceColumns.add(posCol);
//	    sourceColumns.add(directionCol);
//	    sourceColumns.add(typeCol);
//	    sourceColumns.add(regCol);
//	    sourceColumns.add(colCol);
//	    ColumnModel<PositionEntry> sourceColumnModel = new ColumnModel<PositionEntry>(sourceColumns);
//	    
//	    sourceStore = new ListStore<PositionEntry>(positionDimensionProps.key());
//	    // hier daten laden
//	    grid = new Grid<PositionEntry>(sourceStore, sourceColumnModel);
////	    grid.setSelectionModel(selectionModel);
////	    grid.setColumnReordering(true);
//	    grid.getView().setAutoExpandColumn(posCol);
//	    grid.setBorders(false);
//	    grid.getView().setStripeRows(true);
//	    grid.getView().setColumnLines(true);
//	    grid.getView().setForceFit(true);
//    
//		positionEntriesCB.setEditable(false);
//		positionEntriesCB.setTypeAhead(false);
//		positionEntriesCB.setTriggerAction(TriggerAction.ALL);
//	    editing = new GridRowEditing<PositionEntry>(grid);
//	    editing.addEditor(posCol, positionEntriesCB);
//		directionNF.add(PositionEntry.LEFT);
//		directionNF.add(PositionEntry.RIGHT);
//
//		directionNF.setEditable(false);
//		directionNF.setTypeAhead(false);
//		directionNF.setTriggerAction(TriggerAction.ALL);
//	    directionNF.setAllowBlank(false);
//	    editing.addEditor(directionCol, directionNF);
//		typeNF.add(PositionEntry.RHOMBUS);
//		typeNF.add(PositionEntry.SQHARE);
//
//		typeNF.setEditable(false);
//		typeNF.setTypeAhead(false);
//		typeNF.setTriggerAction(TriggerAction.ALL);
//		typeNF.setAllowBlank(false);
//	    editing.addEditor(typeCol, typeNF);
//	    NumberField<Integer> registerNF = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
//	    registerNF.setAllowBlank(false);
//	    editing.addEditor(regCol, registerNF);
//	    NumberField<Integer> colNF = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
//	    colNF.setAllowBlank(false);
//	    editing.addEditor(colCol, colNF);				    				    
//	    editing.addCancelEditHandler(new CancelEditHandler<PositionEntry>() {
//				
//				@Override
//				public void onCancelEdit(CancelEditEvent<PositionEntry> event) {
//					PositionEntry entry = event.getSource().getEditableGrid().getSelectionModel().getSelectedItem();
//					if (entry!=null) {
//						if (entry.getPositionID() == 0 && (!positionEntriesCB.isValid() || !directionNF.isValid() || !typeNF.isValid() || !registerNF.isValid())) {
//							sourceStore.remove(entry);
//						}
//					}
//				}
//			});
//	    editing.addStartEditHandler(new StartEditHandler<PositionEntry>() {
//
//			@Override
//			public void onStartEdit(StartEditEvent<PositionEntry> event) {
//				// TODO Auto-generated method stub
//				peEdit = event.getSource().getEditableGrid().getSelectionModel().getSelectedItem();
//			}
//	    	
//	    });
//	    editing.addCompleteEditHandler(new CompleteEditHandler<PositionEntry>() {
//				
//				@Override
//				public void onCompleteEdit(CompleteEditEvent<PositionEntry> event) {
//					PositionEntry peNew = event.getSource().getEditableGrid().getSelectionModel().getSelectedItem();
//					if (peNew!=null) {
//						if (peNew.getPositionID() == 0 && (!positionEntriesCB.isValid() || !directionNF.isValid() || !typeNF.isValid() || !registerNF.isValid())) {
//							sourceStore.remove(peNew);
//						} else {
//							dbService.isGoodDimension(entry.getCaveID(), peNew.getRegisters(), peNew.getColumns(), new AsyncCallback<Boolean>() {
//
//								@Override
//								public void onFailure(Throwable caught) {
//									// TODO Auto-generated method stub
//									Util.doLogging(caught.getLocalizedMessage());
//								}
//
//								@Override
//								public void onSuccess(Boolean result) {
//									if (result) {
//										Info.display("New Walldimensions", "good");
//									} else {
//										Info.display("New Walldimensions declined!", "there are Entries, which would be outside the new dimensions.");
//										sourceStore.remove(peNew);
//										sourceStore.add(peEdit);
//									}
//								}
//							});
//						}
//					}
//				}
//			});
//
//
//		positionFP.add(grid);
//		positionFP.setHeight(500);
//		ToolButton cancelpositionTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
//		cancelpositionTB.setToolTip(Util.createToolTip("close"));
//		cancelpositionTB.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				editWallGrid.hide();
//			};
//		});
//		ToolButton savepositionTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
//		savepositionTB.setToolTip(Util.createToolTip("Save Wall Positions"));
//		savepositionTB.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				ArrayList<PositionEntry> positions = new ArrayList<PositionEntry>();
//				Collection<Store<PositionEntry>.Record> records = sourceStore.getModifiedRecords();
//				for (Store<PositionEntry>.Record record : sourceStore.getModifiedRecords()) {
//					String positionName = record.getValue(positionDimensionProps.name());
//					PositionEntry peChosen = null;
//					for (PositionEntry pe : StaticTables.getInstance().getPositionEntries().values()) {
//						if (pe.getName().equals(positionName)) {
//							peChosen = pe;
//							break;
//						}
//					}
//					peChosen.setdirection(record.getValue(positionDimensionProps.direction()));
//					peChosen.setType(record.getValue(positionDimensionProps.type()));
//					peChosen.setRegisters(record.getValue(positionDimensionProps.registers()));
//					peChosen.setColumns(record.getValue(positionDimensionProps.columns()));
//					positions.add(peChosen);
//				};
//				ArrayList<PositionEntry> newPositions = new ArrayList<PositionEntry>();
//				WallTreeEntry wte = wallTree.wallTree.getSelectionModel().getSelectedItem();
//				ArrayList<PositionEntry> oldPositions = entry.getWallPositions().get(wte.getWallLocationID());
//				if (oldPositions != null) {
//					for (PositionEntry pe: oldPositions) {
//						if (!positions.contains(pe)) {
//							newPositions.add(pe);
//						} else {
//							for (PositionEntry peNew: positions) {
//								if (peNew.getPositionID() == pe.getPositionID()) {
//									if ((peNew.getRegisters() < pe.getRegisters())||(peNew.getColumns() < pe.getColumns())) {
//										dbService.isGoodDimension(entry.getCaveID(), peNew.getRegisters(), peNew.getColumns(), new AsyncCallback<Boolean>() {
//
//											@Override
//											public void onFailure(Throwable caught) {
//												// TODO Auto-generated method stub
//												Util.doLogging(caught.getLocalizedMessage());
//											}
//
//											@Override
//											public void onSuccess(Boolean result) {
//												if (result) {
//													Info.display("New Walldimensions", "good");
//												} else {
//													Info.display("New Walldimensions", "bad");
//												}
//											}
//										});
//									}
//								}
//							}
//						}
//					}
//				}
//				newPositions.addAll(positions);
//				wallTree.wallTree.getSelectionModel().getSelectedItem().setPosition(newPositions);
//				wallTree.wallTree.refresh(wallTree.wallTree.getSelectionModel().getSelectedItem());
//				wallTree.wallTree.setAutoExpand(true);
//
//				entry.getWallPositions().put(wte.getWallLocationID(), newPositions);
//				if (!isWallDimension) {
//					ArrayList<WallTreeEntry> wtes = getAllWalls();
//					dbService.saveWallDimension(wtes, entry.getCaveID(), new AsyncCallback<Boolean>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							// TODO Auto-generated method stub
//							Util.doLogging(caught.getLocalizedMessage());
//						}
//
//						@Override
//						public void onSuccess(Boolean result) {
//							if (result) {
//								Info.display("Walls saved", "Success!");
//							} else {
//								Info.display("Walls saved", "Failed!");
//							}
//						}
//					});
//				}
//				editWallGrid.hide(true);
//			};
//		});
//		ToolButton addPositionTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
//		addPositionTB.addSelectHandler(new SelectHandler() {
//			
//			@Override
//			public void onSelect(SelectEvent event) {
//				PositionEntry newUser = new PositionEntry(0,"");
//				sourceStore.add(newUser);
//				editing.startEditing(new GridCell(sourceStore.size()-1, 0));
//			}
//		});
//		ToolButton delPositionTB = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
//		delPositionTB.addSelectHandler(new SelectHandler() {
//			
//			@Override
//			public void onSelect(SelectEvent event) {
//				WallTreeEntry wte = wallTree.wallTree.getSelectionModel().getSelectedItem();
//				ArrayList<PositionEntry> removedEntries = new ArrayList<PositionEntry>(grid.getSelectionModel().getSelectedItems());
//				ArrayList<PositionEntry> newPositions = new ArrayList<PositionEntry>();
//				for (PositionEntry pe: entry.getWallPositions().get(wte.getWallLocationID())){
//					if (!removedEntries.contains(pe)) {
//						newPositions.add(pe);
//					}
//				}
//				entry.getWallPositions().put(wte.getWallLocationID(), newPositions);
//				wallTree.wallTree.getSelectionModel().getSelectedItem().setPosition(newPositions);
//				wallTree.wallTree.refresh(wallTree.wallTree.getSelectionModel().getSelectedItem());
//				wallTree.wallTree.setAutoExpand(true);
//
//				editWallGrid.hide();
//			}
//		});
//		positionFP.addTool(delPositionTB);
//		positionFP.addTool(addPositionTB);
//		positionFP.addTool(savepositionTB);
//		positionFP.addTool(cancelpositionTB);
		if (this.isWallDimension) {
			this.wallTree.wallTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			editTB.setToolTip(Util.createToolTip("Add Wall Dimension"));
			editTB.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					wv.setWall(wallTree.wallTree.getSelectionModel().getSelectedItem());
//					editWallGrid.clear();
//					sourceStore.clear();
//				    WallTreeEntry wte = wallTree.wallTree.getSelectionModel().getSelectedItem();
//				    ArrayList<PositionEntry> positions = null;
//				    if (wte != null) {
//					    positions = entry.getWallPositions().get(wte.getWallLocationID());
//					    if (positions != null) {
//							for (PositionEntry pe: positions) {
//								sourceStore.add(pe);
//							}				    	
//					    }
//						editWallGrid.add(positionFP);
//						editWallGrid.center();
//				    }
				}
			});					
		} else {
			editTB.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					PopupPanel editWallPosition = new PopupPanel();					
					sourceStore.clear();
				    WallTreeEntry wte = wallTree.wallTree.getSelectionModel().getSelectedItem();
				    ArrayList<WallDimensionEntry> dimensions = null;
				    if (wte != null) {
					    dimensions = entry.getWallDimensions().get(wte.getWallLocationID());	    	
				    }
				    if (dimensions != null) {
						for (WallDimensionEntry wde: dimensions) {
							sourceStore.add(wde);
						}				    	
				    }
				    FramedPanel positionCombiFP = new FramedPanel();
					
					positionCombiFP.setHeading("Set position");
					if (wallTree.wallTree.getSelectionModel().getSelectedItem().getDimensions()==null) {
						PositionSelectionLV.getSelectionModel().deselectAll();
					}
					else {
						PositionSelectionLV.getSelectionModel().deselectAll();
//						PositionSelectionLV.getSelectionModel().setSelection(wallTree.wallTree.getSelectionModel().getSelectedItem().getPosition());
						ArrayList<WallDimensionEntry> selected = wallTree.wallTree.getSelectionModel().getSelectedItem().getDimensions();
						for (WallDimensionEntry pe : selected) {
							PositionSelectionLV.getSelectionModel().select(true, pe);
						}
					}
					FramedPanel fpPosition = new FramedPanel();
					fpPosition.setHeading("Position");
					fpPosition.add(PositionSelectionLV);
					FramedPanel fpDimension = new FramedPanel();
					fpDimension.setHeading("Dimension");
					VerticalLayoutContainer dimensionVLC = new VerticalLayoutContainer();
					FramedPanel fpSelectedPosition = new FramedPanel();
					fpSelectedPosition.setHeading("Selected Position");
					exact = new CheckBox();
					exact.setBoxLabel("exact");
					//exact.setValue(bibEntry.isOfficialTitleTranslation());
					editWallPosition.center();
				}
			});			
		}
		ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveTB.setToolTip(Util.createToolTip("save"));
		saveTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Util.doLogging("test");
				save(getSelectedWalls());
				popup.hide();
			}
		});
		if (!setSearch) {
			selectWallFP.addTool(editTB);
			//selectWallFP.addTool(saveTB);
		}
		selectWallFP.addTool(cancelTB);
		Util.doLogging("WallOrnamentCaveRelationEditor.createForm() finished");
		return selectWallFP;
	}
	protected void save(ArrayList<WallTreeEntry> results ) {
		Util.doLogging(this.getClass().getName() + " cavewallornamentrelation wurde erstellt");
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
	//Komplexe Filterung damit beim Eintragen schon keine Fehler gemacht werden koennen sondern 
	// nur Felder ausgewaaehlt werden koennen, die an einer bestimmten Position ueberhaupt moeglich sind
	// Unterscheidung erst zwischen Wall, Ceiling und Reveal
	
	// Die eigentliche Filterung uebernimmt die Datenbank mit den Tabellen OrnamentPosition, OrnamentFunction und den 
	// dazugehoerigen Relationen Tabellen
	
	public void show() {
		Util.doLogging("blubb");
		//Aufruf mit leeren Feldern f�r die Eingabe
		popup = new PopupPanel();
		//this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		popup.setWidget(createForm());
		popup.center();

	}
	public List<WallTreeEntry> getSelectedItems(){
		return wallTree.wallTree.getSelectionModel().getSelectedItems();
	}
	public void selectChildren(boolean Children) {
		if (Children) {
				wallTree.wallTree.setCheckStyle(CheckCascade.CHILDREN);
		
		}
		else {
			wallTree.wallTree.setCheckStyle(CheckCascade.NONE);
		}
	}
	public void show(CaveEntry entry) {
		//Afruf mit Entry zum Bearbeiten
		init = 1;
//		this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		this.entry = entry;
		popup = new PopupPanel();
		
		popup.setWidget(createForm());
			popup.center();
		
	}
}
