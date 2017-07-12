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
package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.walls.WallSelector;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.OrnamentFunction;
import de.cses.shared.OrnamentPosition;
import de.cses.shared.WallEntry;
import de.cses.shared.WallOrnamentCaveRelation;

/**
 * @author nina
 *
 */
public class OrnamentWallAttributes extends PopupPanel {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	
	private CaveEntry cave;
	private PopupPanel popup;
	private FramedPanel header;
	private OrnamentCaveAttributes ornamentCaveRelation;
	
	
	private OrnamentPositionProperties ornamentPositionProps;
	
	private OrnamentFunctionProperties ornamentFunctionProps;
	
	private WallProperties wallProps;
	
	
	private ListStore<OrnamentPosition> ornamentPosition;
	private ComboBox<OrnamentPosition> ornamentPositionComboBox;
	
	private ListStore<OrnamentFunction> ornamentfunction;
	private ComboBox<OrnamentFunction> ornamentfunctionComboBox;
	
	private ListStore<WallEntry> walls;
	private ComboBox<WallEntry> wallsComboBox;
	private  WallOrnamentCaveRelation wallOrnamentCaveRelation;

	
	public OrnamentWallAttributes(CaveEntry cave){
		super(false);
		initOrnamentWallAttributes(cave);
	}
	public OrnamentWallAttributes(CaveEntry cave, WallOrnamentCaveRelation wallOrnamentCaveRelation){
		super(false);
		this.wallOrnamentCaveRelation = wallOrnamentCaveRelation;
		initOrnamentWallAttributes(cave);
	}
	
	public void initOrnamentWallAttributes(CaveEntry cave){
	
		popup = this;
		this.cave = cave;
		ornamentPositionProps = GWT.create(OrnamentPositionProperties.class);
	
		ornamentFunctionProps = GWT.create(OrnamentFunctionProperties.class);
	
		wallProps = GWT.create(WallProperties.class);
		ornamentPosition = new ListStore<OrnamentPosition>(ornamentPositionProps.ornamentPositionID());
		ornamentfunction = new ListStore<OrnamentFunction>(ornamentFunctionProps.ornamentFunctionID());
		walls = new ListStore<WallEntry>(wallProps.wallID());
		
	
		
		dbService.getWalls(new AsyncCallback<ArrayList<WallEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<WallEntry> result) {
				walls.clear();
				for (WallEntry pe : result) {
					walls.add(pe);
				}
			}
		});
		
		dbService.getOrnamentPositions(new AsyncCallback<ArrayList<OrnamentPosition>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentPosition> result) {
				ornamentPosition.clear();
				for (OrnamentPosition pe : result) {
					ornamentPosition.add(pe);
				}
			}
		});
		
		dbService.getOrnamentFunctions(new AsyncCallback<ArrayList<OrnamentFunction>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentFunction> result) {
				ornamentfunction.clear();
				for (OrnamentFunction pe : result) {
					ornamentfunction.add(pe);
				}
			}
		});
		
		setWidget(createForm());
	}
	
	
	public Widget createForm(){
		VerticalPanel wallrelationMainVerticalPanel = new VerticalPanel();
		VerticalLayoutContainer vlcWalls = new VerticalLayoutContainer();
		wallrelationMainVerticalPanel.add(vlcWalls);
		
		
		FramedPanel wallrelationFramedPanel = new FramedPanel();
		wallrelationFramedPanel.setHeading("Select Walls");
		wallrelationFramedPanel.add(wallrelationMainVerticalPanel);
		
		WallSelector wallselector = new WallSelector();
		
		header = new FramedPanel();
		header.setHeading("Select Wall");
		header.add(wallselector);
		vlcWalls.add(header, new VerticalLayoutData(1.0, .125));
	
		wallselector.setCave(cave);
	  
		ornamentPositionComboBox = new ComboBox<OrnamentPosition>(ornamentPosition, ornamentPositionProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentPosition>() {

					@Override
					public SafeHtml render(OrnamentPosition item) {
						final OrnamentPositionViewTemplates pvTemplates = GWT.create(OrnamentPositionViewTemplates.class);
						return pvTemplates.ornamentPosition(item.getName());
					}
				});
		
	  
		header = new FramedPanel();
		header.setWidth(300);
		ornamentPositionComboBox.setTriggerAction(TriggerAction.ALL);
		if( wallOrnamentCaveRelation != null){
			ornamentPositionComboBox.setValue(wallOrnamentCaveRelation.getPosition());
		}
		header.setHeading("Select ornament position");
		header.add(ornamentPositionComboBox);
		vlcWalls.add(header);
	  
		ornamentfunctionComboBox = new ComboBox<OrnamentFunction>(ornamentfunction, ornamentFunctionProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentFunction>() {

					@Override
					public SafeHtml render(OrnamentFunction item) {
						final OrnamentFunctionViewTemplates pvTemplates = GWT.create(OrnamentFunctionViewTemplates.class);
						return pvTemplates.ornamentFunction(item.getName());
					}
				});
		
		header = new FramedPanel();
		header.setWidth(300);
		if( wallOrnamentCaveRelation != null){
			ornamentfunctionComboBox.setValue(wallOrnamentCaveRelation.getFunction());
		}
		header.setHeading("Select the ornament function");
		ornamentfunctionComboBox.setTriggerAction(TriggerAction.ALL);
		header.add(ornamentfunctionComboBox);
		vlcWalls.add(header, new VerticalLayoutData(1.0, .125));
	  
	  
	  
		
	  final TextField notes = new TextField();
	  notes.setAllowBlank(true);
	  
		header = new FramedPanel();
		header.setWidth(300);
		if( wallOrnamentCaveRelation != null){
			notes.setText(wallOrnamentCaveRelation.getNotes());
		}
		header.setHeading("Notes");
		header.add(notes);
		vlcWalls.add(header, new VerticalLayoutData(1.0, .125));
	  
	
	  
	  TextButton save = new TextButton("save");
	  
	  TextButton cancel = new TextButton("cancel");

	wallrelationFramedPanel.addButton(save);
	wallrelationFramedPanel.addButton(cancel);
	
ClickHandler saveHandler = new ClickHandler(){

	@Override
	public void onClick(ClickEvent event) {
		
		WallOrnamentCaveRelation relation = new WallOrnamentCaveRelation();
		relation.setFunction(ornamentfunctionComboBox.getValue());
		relation.setPosition(ornamentPositionComboBox.getValue());
		relation.setNotes(notes.getText());
		relation.setWallID(wallsComboBox.getValue().getWallID());
		ornamentCaveRelation.getWallsListStore().add(relation);
		popup.hide();
	}
	
};
save.addHandler(saveHandler, ClickEvent.getType());
	  
	  ClickHandler cancelClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
			popup.hide();		
			}
	  };
	  cancel.addHandler(cancelClickHandler, ClickEvent.getType());
	 
		return wallrelationFramedPanel;
		
	}
	
	
	public CaveEntry getCave() {
		return cave;
	}


	public void setCave(CaveEntry cave) {
		this.cave = cave;
	}
	




	public OrnamentCaveAttributes getOrnamentCaveRelation() {
		return ornamentCaveRelation;
	}


	public void setOrnamentCaveRelation(OrnamentCaveAttributes ornamentCaveRelation) {
		this.ornamentCaveRelation = ornamentCaveRelation;
	}







	interface CavePartProperties extends PropertyAccess<CavePart> {
		ModelKeyProvider<CavePart> cavePartID();

		LabelProvider<CavePart> name();
	}
	
	interface OrnamentPositionProperties extends PropertyAccess<OrnamentPosition> {
		ModelKeyProvider<OrnamentPosition> ornamentPositionID();

		LabelProvider<OrnamentPosition> name();
	}
	
	interface OrnamentFunctionProperties extends PropertyAccess<OrnamentFunction> {
		ModelKeyProvider<OrnamentFunction> ornamentFunctionID();

		LabelProvider<OrnamentFunction> name();
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
	
}
