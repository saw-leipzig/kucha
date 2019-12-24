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
import de.cses.shared.WallTreeEntry;

/**
 * @author Erik
 *
 */
public class PositionEditor {


	private ListStore<PositionEntry> positionEntryLS;
	private ComboBox<PositionEntry> positionComboBox;
	private PositionProperties positionProps;
	private WallTree wallTree;
	private CaveEntry entry;
	int init= 0;
	PopupPanel popup = new PopupPanel();
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface PositionProperties extends PropertyAccess<PositionEntry> {
		ModelKeyProvider<PositionEntry> positionID();
										

		LabelProvider<PositionEntry> name();
	}

	private WallSelector wallselector;

	public PositionEditor(CaveEntry entry) {
		this.entry=entry;
		positionProps = GWT.create(PositionProperties.class);
		
		positionEntryLS = new ListStore<PositionEntry>(positionProps.positionID());
	
		for (PositionEntry ope : StaticTables.getInstance().getPositionEntries().values()) {
			Util.doLogging(ope.getName());
			Util.doLogging(Integer.toString(ope.getPositionID()));
			positionEntryLS.add(ope);
		}

	}

	private FramedPanel createForm() {
		
		wallselector = new WallSelector(new SelectionHandler<WallEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				positionComboBox.clear();
				positionComboBox.disable();
				positionEntryLS.clear();
				//filterPositionbyCaveArea();
				positionComboBox.setEnabled(true);
			}
		});
		wallselector.setCave(entry);

		FramedPanel selectWallFP = new FramedPanel();
		selectWallFP.setHeading("Select Wall");
		ArrayList<WallTreeEntry> wallTreeEntries = new ArrayList<WallTreeEntry>();
		WallTree wallTree = new WallTree(StaticTables.getInstance().getWallTreeEntries().values(), wallTreeEntries, false, true);
		selectWallFP.add(wallTree.wallTree);
	

		positionComboBox = new ComboBox<PositionEntry>(positionEntryLS, positionProps.name(),
				new AbstractSafeHtmlRenderer<PositionEntry>() {

					@Override
					public SafeHtml render(PositionEntry item) {
						final OrnamentPositionViewTemplates pvTemplates = GWT.create(OrnamentPositionViewTemplates.class);
						return pvTemplates.ornamentPosition(item.getName());
					}
				});
		
		
		
		positionComboBox.setTypeAhead(false);
		positionComboBox.setEditable(false);
		positionComboBox.setTriggerAction(TriggerAction.ALL);

		FramedPanel positionFP = new FramedPanel();
		positionFP.setHeading("Select position");
		positionFP.add(positionComboBox);
		//FramedPanel wallFP = new FramedPanel();
		//wallFP.setHeading("Select wall");
		//wallFP.add(positionComboBox);
	
		ValueChangeHandler<PositionEntry> positionSelectionHandler = new ValueChangeHandler<PositionEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PositionEntry> event) {
					

			}

		};
		positionComboBox.addValueChangeHandler(positionSelectionHandler);

		VerticalLayoutContainer vlcWalls = new VerticalLayoutContainer();
		vlcWalls.add(positionFP, new VerticalLayoutData(1.0, .15));
		
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
	
	public void show(CaveEntry entry) {
		//Afruf mit Entry zum Bearbeiten
		init = 1;
//		this.caveEntry = OrnamenticEditor.ornamentCaveRelationEditor.getCaveEntryComboBox().getValue();

		this.entry = entry;
		popup = new PopupPanel();
		
		popup.setWidget(createForm());
			wallselector.setCave(entry);;
			popup.center();
		
	}
}
