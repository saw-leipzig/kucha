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
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallTreeEntry;

/**
 * @author Erik
 *
 */
public class PositionEditor {


	private ListStore<PositionEntry> positionEntryLS;
	private ListView<PositionEntry, PositionEntry> PositionSelectionLV;
	private PositionProperties positionProps;
	private WallTree wallTree;
	private CaveEntry entry;
	private List<WallTreeEntry> walls;
	private boolean setSearch;
	int init= 0;
	final PositionViewTemplates pvTemplates = GWT.create(PositionViewTemplates.class);

	public PopupPanel popup = new PopupPanel();
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface PositionProperties extends PropertyAccess<PositionEntry> {
		ModelKeyProvider<PositionEntry> positionID();
										

		LabelProvider<PositionEntry> name();
	}
	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionView(String name);
	}

	public PositionEditor(CaveEntry entry, List<WallTreeEntry> entries, boolean setSearch) {
		this.entry=entry;
		this.walls=entries;
		this.setSearch=setSearch;
		positionProps = GWT.create(PositionProperties.class);
		
		positionEntryLS = new ListStore<PositionEntry>(positionProps.positionID());
	
		for (PositionEntry ope : StaticTables.getInstance().getPositionEntries().values()) {
			positionEntryLS.add(ope);
		}
		wallTree = new WallTree(StaticTables.getInstance().getWallTreeEntries().values(), walls, false, true, entry);

	}
	public List<WallTreeEntry> getSelectedWalls() {
		return wallTree.wallTree.getCheckedSelection();	
		
	}
	

	private FramedPanel createForm() {
		


		FramedPanel selectWallFP = new FramedPanel();
		selectWallFP.setHeading("Select Wall");

				//Info.display("Ausgewählt: ",wallTree.wallTree.getSelectionModel().getSelectedItem().getText());

		selectWallFP.add(wallTree.wallSelectorBLC);
	
		PositionSelectionLV = new ListView<PositionEntry, PositionEntry>(positionEntryLS,
				new IdentityValueProvider<PositionEntry>(),
				new SimpleSafeHtmlCell<PositionEntry>(new AbstractSafeHtmlRenderer<PositionEntry>() {

					@Override
					public SafeHtml render(PositionEntry entry) {
						return pvTemplates.positionView(entry.getName());
					}

				}));
		PositionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
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
			selectWallFP.setHeading("Add Position in Cave");
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
		editTB.setToolTip(Util.createToolTip("Add Position to Wall"));
		editTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel editWallPosition = new PopupPanel();
				FramedPanel positionFP = new FramedPanel();
				positionFP.setHeading("Select position");
				PositionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
				if (wallTree.wallTree.getSelectionModel().getSelectedItem().getPosition()==null) {
					PositionSelectionLV.getSelectionModel().deselectAll();
				}
				else {
					PositionSelectionLV.getSelectionModel().deselectAll();
//					PositionSelectionLV.getSelectionModel().setSelection(wallTree.wallTree.getSelectionModel().getSelectedItem().getPosition());
					for (PositionEntry pe : wallTree.wallTree.getSelectionModel().getSelectedItem().getPosition()) {
						PositionSelectionLV.getSelectionModel().select(true, pe);
					}
					
				}
				positionFP.add(PositionSelectionLV);
				editWallPosition.add(positionFP);
				ToolButton cancelpositionTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelpositionTB.setToolTip(Util.createToolTip("close"));
				cancelpositionTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						editWallPosition.hide();
					};
				});
				ToolButton savepositionTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				savepositionTB.setToolTip(Util.createToolTip("save Position for"+wallTree.wallTree.getSelectionModel().getSelectedItem().getText()));
				savepositionTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						ArrayList<PositionEntry> positions = new ArrayList<PositionEntry>();
						for (PositionEntry pe : PositionSelectionLV.getSelectionModel().getSelectedItems()) {
							positions.add(pe);
						};
						
						wallTree.wallTree.getSelectionModel().getSelectedItem().setPosition(positions);
				
						Info.display("Test: ", wallTree.wallTree.getSelectionModel().getSelectedItem().getText());
						wallTree.wallTree.refresh(wallTree.wallTree.getSelectionModel().getSelectedItem());
						wallTree.wallTree.setAutoExpand(true);
						editWallPosition.hide();
					};
				});
				positionFP.addTool(savepositionTB);
				positionFP.addTool(cancelpositionTB);
				editWallPosition.center();
			}
		});

		ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveTB.setToolTip(Util.createToolTip("save"));
		saveTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				save(getSelectedWalls());
				popup.hide();
			}
		});
		if (!setSearch) {
			selectWallFP.addTool(editTB);
			selectWallFP.addTool(saveTB);			
		}
		selectWallFP.addTool(cancelTB);


		Util.doLogging("WallOrnamentCaveRelationEditor.createForm() finished");
		return selectWallFP;
	}

	protected void save(List<WallTreeEntry> results ) {
		//Speicherung der Wand und der zugehoerigen Eigenschaften
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
