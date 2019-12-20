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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallOrnamentCaveRelation;

/**
 * @author Erik
 *
 */
public class PositionEditor {


	private ListStore<PositionEntry> PositionEntryLS;
	private ComboBox<PositionEntry> PositionComboBox;
	private PositionProperties positionProps;
	private CaveEntry entry;
	int init= 0;
	PopupPanel popup = new PopupPanel();
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface PositionProperties extends PropertyAccess<PositionEntry> {
		ModelKeyProvider<PositionEntry> PositionID();

		LabelProvider<PositionEntry> name();
	}

	private WallSelector wallselector;

	public PositionEditor(CaveEntry entry) {
		this.entry=entry;
		positionProps = GWT.create(PositionProperties.class);
		
		PositionEntryLS = new ListStore<PositionEntry>(positionProps.PositionID());
	
		for (PositionEntry ope : StaticTables.getInstance().getPositionEntries().values()) {
			PositionEntryLS.add(ope);
		}

	}

	private FramedPanel createForm() {
		
		wallselector = new WallSelector(new SelectionHandler<WallEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				PositionComboBox.clear();
				PositionComboBox.disable();
				PositionEntryLS.clear();
				//filterPositionbyCaveArea();
				PositionComboBox.setEnabled(true);
			}
		});
		wallselector.setCave(entry);

		FramedPanel selectWallFP = new FramedPanel();
		selectWallFP.setHeading("Select Wall");
		selectWallFP.add(wallselector);
	

		PositionComboBox = new ComboBox<PositionEntry>(PositionEntryLS, positionProps.name(),
				new AbstractSafeHtmlRenderer<PositionEntry>() {

					@Override
					public SafeHtml render(PositionEntry item) {
						final OrnamentPositionViewTemplates pvTemplates = GWT.create(OrnamentPositionViewTemplates.class);
						return pvTemplates.ornamentPosition(item.getName());
					}
				});
		
		
		
		ornamentPositionComboBox.setTypeAhead(false);
		ornamentPositionComboBox.setEditable(false);
		ornamentPositionComboBox.setTriggerAction(TriggerAction.ALL);

		FramedPanel ornamentPositionFP = new FramedPanel();
		ornamentPositionFP.setHeading("Select position");
		ornamentPositionFP.add(ornamentPositionComboBox);

		ornamentfunctionComboBox = new ComboBox<OrnamentFunctionEntry>(ornamentFunctionEntryLS, ornamentFunctionProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentFunctionEntry>() {

					@Override
					public SafeHtml render(OrnamentFunctionEntry item) {
						final OrnamentFunctionViewTemplates pvTemplates = GWT.create(OrnamentFunctionViewTemplates.class);
						return pvTemplates.ornamentFunction(item.getName());
					}
				});
		ornamentfunctionComboBox.setTypeAhead(false);
		ornamentfunctionComboBox.setEditable(false);
		ornamentfunctionComboBox.setTriggerAction(TriggerAction.ALL);
		FramedPanel ornamentFunctionFP = new FramedPanel();
		ornamentFunctionFP.setHeading("Select function");
		ornamentFunctionFP.add(ornamentfunctionComboBox);
		
		ValueChangeHandler<OrnamentPositionEntry> positionSelectionHandler = new ValueChangeHandler<OrnamentPositionEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<OrnamentPositionEntry> event) {
				ornamentFunctionEntryLS.clear();
				
				dbService.getFunctionbyPosition(event.getValue(), new AsyncCallback<ArrayList<OrnamentFunctionEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<OrnamentFunctionEntry> result) {
						for (OrnamentFunctionEntry pe : result) {
							ornamentFunctionEntryLS.add(pe);
						}
						if(wallOrnamentCaveRelation != null && init == 1) {
							ornamentfunctionComboBox.setValue(ornamentFunctionEntryLS.findModelWithKey(Integer.toString(wallOrnamentCaveRelation.getOrnamenticFunctionID())));
							init = 0;
						}
						ornamentfunctionComboBox.setEnabled(true);
					}
				});
			}

		};
		ornamentPositionComboBox.addValueChangeHandler(positionSelectionHandler);
		
		notes = new TextArea();
		notes.setAllowBlank(true);
		FramedPanel notesFP = new FramedPanel();
		if (wallOrnamentCaveRelation != null) {
			notes.setText(wallOrnamentCaveRelation.getNotes());
		}
		notesFP.setHeading("Notes");
		notesFP.add(notes);

		VerticalLayoutContainer vlcWalls = new VerticalLayoutContainer();
		vlcWalls.add(ornamentPositionFP, new VerticalLayoutData(1.0, .15));
		vlcWalls.add(ornamentFunctionFP, new VerticalLayoutData(1.0, .15));
		vlcWalls.add(notesFP, new VerticalLayoutData(1.0, .7));
		
		HorizontalLayoutContainer wallRelationHLC = new HorizontalLayoutContainer();
		wallRelationHLC.add(selectWallFP, new HorizontalLayoutData(.5, 1.0));
		wallRelationHLC.add(vlcWalls, new HorizontalLayoutData(.5, 1.0));

		FramedPanel wallrelationFramedPanel = new FramedPanel();
		wallrelationFramedPanel.setHeading("Add Position in Cave");
		wallrelationFramedPanel.setSize("600px", "450px");
		wallrelationFramedPanel.add(wallRelationHLC);
		
		ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		cancelTB.setToolTip(Util.createToolTip("close"));
		cancelTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				popup.hide();
			}
		});

		ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveTB.setToolTip(Util.createToolTip("save"));
		saveTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				save();
				popup.hide();
			}
		});
		wallrelationFramedPanel.addTool(saveTB);
		wallrelationFramedPanel.addTool(cancelTB);


		Util.doLogging("WallOrnamentCaveRelationEditor.createForm() finished");
		return wallrelationFramedPanel;
	}

	protected void save() {
		//Speicherung der Wand und der zugehoerigen Eigenschaften
		WallOrnamentCaveRelation caveWallOrnamentRelation = new WallOrnamentCaveRelation(caveEntry.getCaveID(), wallselector.getSelectedWallEntry());
		Util.doLogging(this.getClass().getName() + " cavewallornamentrelation wurde erstellt");
		if (ornamentfunctionComboBox.getValue() == null) {
			caveWallOrnamentRelation.setOrnamenticFunctionID(18); // 18 = unknown
		} else {
			caveWallOrnamentRelation.setOrnamenticFunctionID(ornamentfunctionComboBox.getValue().getOrnamentFunctionID());
		}
		if (ornamentPositionComboBox.getValue() == null) {
			caveWallOrnamentRelation.setOrnamenticPositionID(19); // 19 = unknwon
		} else {
			caveWallOrnamentRelation.setOrnamenticPositionID(ornamentPositionComboBox.getValue().getOrnamentPositionID());
		}
		caveWallOrnamentRelation.setNotes(notes.getText());
		
		
		if(wallOrnamentCaveRelation != null) {
			//OrnamenticEditor.ornamentCaveRelationEditor.getWallsListStore().remove(wallOrnamentCaveRelation);
		}
		//OrnamenticEditor.ornamentCaveRelationEditor.getWallsListStore().add(caveWallOrnamentRelation);
		//OrnamenticEditor.ornamentCaveRelationEditor.getWallsListView().refresh();
	}

	public CaveEntry getCave() {
		return caveEntry;
	}

	public void setCave(CaveEntry cave) {
		this.caveEntry = cave;
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
	public void filterPositionbyCaveArea() {
		ornamentPositionEntryLS.clear();
		String wallOrCeiling = StaticTables.getInstance().getWallLocationEntries().get(wallselector.getSelectedWallEntry().getWallLocationID()).getLabel();

		if( wallOrCeiling.contains("wall")) {

	
			dbService.getPositionbyWall(wallselector.getSelectedWallEntry(), new AsyncCallback<ArrayList<OrnamentPositionEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(ArrayList<OrnamentPositionEntry> result) {
					for (OrnamentPositionEntry pe : result) {
						ornamentPositionEntryLS.add(pe);
					}
					if(wallOrnamentCaveRelation != null && init == 1) {
						ornamentPositionComboBox.setValue(ornamentPositionEntryLS.findModelWithKey(Integer.toString(wallOrnamentCaveRelation.getOrnamenticPositionID())),true);
					}
				}
			});
		}
		else if( wallOrCeiling.contains("ceiling"))  {
			dbService.getCaveAreas(caveEntry.getCaveID(),new AsyncCallback<ArrayList<CaveAreaEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
					System.err.println("Problem loading CaveGroupEntry");
				}

				@Override
				public void onSuccess(ArrayList<CaveAreaEntry> result) {
					caveEntry.setCaveAreaList(result);
					String cavearealabel = StaticTables.getInstance().getWallLocationEntries().get(wallselector.getSelectedWallEntry().getWallLocationID()).getCaveAreaLabel();//.getLabel();
					Util.doLogging(Integer.toString(wallselector.getSelectedWallEntry().getWallLocationID()));
					for (CaveAreaEntry cae : result){
						Util.doLogging(cae.getCaveAreaLabel()+" - "+cavearealabel);
						if(cae.getCaveAreaLabel().contains(cavearealabel)) {
							CaveAreaEntry cavearea = cae;
							int ceiling1 = cavearea.getCeilingType1() != null ? cavearea.getCeilingType1().getCeilingTypeID() : 0;
							int ceiling2 = cavearea.getCeilingType2() != null ? cavearea.getCeilingType2().getCeilingTypeID() : 0;
							if(ceiling1 == 0 && ceiling2 == 0) {
								ceiling1 = 11;
							}
							dbService.getPositionbyCeiling(ceiling1, ceiling2, new AsyncCallback<ArrayList<OrnamentPositionEntry>>() {
		
								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}
		
								@Override
								public void onSuccess(ArrayList<OrnamentPositionEntry> result) {
									ListStore<OrnamentPositionEntry> test = ornamentPositionEntryLS;
		//							Util.doLogging("Nina: in create form step 4.2 variante 2 wallornamenteditor");
									for (OrnamentPositionEntry pe : result) {
										try {
										ornamentPositionEntryLS.add(pe);
										}
										catch(Exception e) {
											Util.doLogging(e.getMessage());
										}
									}
									if(wallOrnamentCaveRelation != null && init == 1) {
										ornamentPositionComboBox.setValue(ornamentPositionEntryLS.findModelWithKey(Integer.toString(wallOrnamentCaveRelation.getOrnamenticPositionID())),true);
									}
									
					}
						
			});
						
					}
					}
				
				}});
		}
		else if(wallOrCeiling.contains("reveal")){
			dbService.getPositionbyReveal(wallselector.getSelectedWallEntry(), new AsyncCallback<ArrayList<OrnamentPositionEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(ArrayList<OrnamentPositionEntry> result) {
					for (OrnamentPositionEntry pe : result) {
						ornamentPositionEntryLS.add(pe);
					}
					if(wallOrnamentCaveRelation != null && init == 1) {
						ornamentPositionComboBox.setValue(ornamentPositionEntryLS.findModelWithKey(Integer.toString(wallOrnamentCaveRelation.getOrnamenticPositionID())),true);
					}
				}
			});
			
		}
		else {
//			Util.doLogging("keinen Case gefunden!");
		}
		}
	
	
	public void show() {
		//Aufruf mit leeren Feldern f�r die Eingabe
		popup = new PopupPanel();
		//this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		popup.setWidget(createForm());
		popup.center();

	}
	
	public void show(WallOrnamentCaveRelation wallOrnamentCaveRelation) {
		//Afruf mit Entry zum Bearbeiten
		init = 1;
//		this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		this.wallOrnamentCaveRelation = wallOrnamentCaveRelation;
		popup = new PopupPanel();
		
		popup.setWidget(createForm());
			wallselector.selectWall(wallOrnamentCaveRelation.getWall().getWallLocationID());
			filterPositionbyCaveArea();
			popup.center();
		
	}
}
