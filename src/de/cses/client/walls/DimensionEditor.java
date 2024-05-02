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
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.editor.client.EditorError;
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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.BeforeRemoveEvent;
import com.sencha.gxt.widget.core.client.event.BeforeRemoveEvent.BeforeRemoveHandler;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.BlurEvent;
import com.sencha.gxt.widget.core.client.event.BlurEvent.BlurHandler;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.StartEditEvent;
import com.sencha.gxt.widget.core.client.event.StartEditEvent.StartEditHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
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
import de.cses.client.ui.OSDListener;
import de.cses.client.walls.WallSketchUploader.WallSketchUploadListener;
import de.cses.client.walls.OSDLoaderWallDimension;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CavePart;
import de.cses.shared.CoordinateEntry;
import de.cses.shared.CoordinatesEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.EmptySpotEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.UserEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallSketchEntry;
import de.cses.shared.WallTreeEntry;

/**
 * @author Erik
 *
 */

interface ImageProperties extends PropertyAccess<WallSketchEntry> {
	ModelKeyProvider<WallSketchEntry> wallSketchID();
	LabelProvider<WallSketchEntry> title();
}



public class DimensionEditor implements IsWidget {
	private WallDimensionEntry wde;
	private OSDLoaderWallDimension osdLoader;
	private OSDListener osdListener;
	private DimensionEditorListener del;
	private ComboBox<WallSketchEntry> sketchSelection;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ImageProperties imageProps = GWT.create(ImageProperties.class);
	private ListStore<WallSketchEntry> wallSketchEntryList = new ListStore<WallSketchEntry>(imageProps.wallSketchID());
	private VerticalLayoutContainer mainView;
	private DepictionEntry correspondingDepictionEntry = null;
	private SimpleComboBox<Integer> directionNF;
	private TextField name;
	private NumberField<Integer> register;
	private NumberField<Integer> number;
	private SimpleComboBox<Integer> typeNF;
	private PopupPanel wallSketchUploadPanel;

	public DimensionEditor(WallDimensionEntry wde, DimensionEditorListener del, DepictionEntry de) {
		this.correspondingDepictionEntry =  de;
		this.wde = wde;
		this.del = del;
	}
	private void loadWallSketches(){
		wallSketchEntryList.clear();
		for (WallSketchEntry wse: StaticTables.getInstance().getWallSketchEntry()) {
			wallSketchEntryList.add(wse);
		}
	}
	private void initPanel(){
		loadWallSketches();
		FramedPanel positionTabPanel = new FramedPanel();
		mainView = new VerticalLayoutContainer();
		VerticalLayoutContainer vlcWallViewEditor = new VerticalLayoutContainer();
		HorizontalLayoutContainer hlcWallUpperView = new HorizontalLayoutContainer();
		FramedPanel editNameFP = new FramedPanel();
		name = new TextField();
		name.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> arg0) {
				update();
			}
			
		});

		editNameFP.add(name);
		editNameFP.setHeading("Set Label");
		FramedPanel editRegisterFP = new FramedPanel();
		editRegisterFP.setHeading("Set Register");
		register = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		register.addValidator(new Validator<Integer>() {

			@Override
			public List<EditorError> validate(Editor<Integer> editor, Integer value) {

				List<EditorError> l = new ArrayList<EditorError>();
				for (CoordinateEntry ce: wde.getCoordinates()) {
					if (ce.getRegister() > value) {
						l.add(new DefaultEditorError(editor, "Register to small", value));
					}


				}
				return l;
			}
			
		});
		register.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> arg0) {
				Util.doLogging("onchange triggered");
				update();
			}
			
		});

		editRegisterFP.add(register);	
		FramedPanel editNumberFP = new FramedPanel();
		number = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		number.addValidator(new Validator<Integer>() {

			@Override
			public List<EditorError> validate(Editor<Integer> editor, Integer value) {

				List<EditorError> l = new ArrayList<EditorError>();
				for (CoordinateEntry ce: wde.getCoordinates()) {
					if (ce.getNumber() > value) {
						l.add(new DefaultEditorError(editor, "Number to small", value));
					}


				}
				return l;
			}
			
		});
		number.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				update();
			}
			
		});
		editNumberFP.add(number);
		editNumberFP.setHeading("Set Number");
		hlcWallUpperView.add(editNameFP, new HorizontalLayoutData(.4, 1));
		hlcWallUpperView.add(editRegisterFP, new HorizontalLayoutData(.3, 1));
		hlcWallUpperView.add(editNumberFP, new HorizontalLayoutData(.3, 1));
		HorizontalLayoutContainer hlcWallLowerView = new HorizontalLayoutContainer();
		FramedPanel editDirtectionFP = new FramedPanel();
		editDirtectionFP.setHeading("Choose Direction");

		directionNF = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {

			@Override
			public String getLabel(Integer item) {
				return WallDimensionEntry.DIRECTION_LABEL.get(item);
			}
		});
		directionNF.add(0);
		directionNF.add(1);
		directionNF.setEmptyText("Select Direction");
		directionNF.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				update();
			}
			
		});
		editDirtectionFP.add(directionNF);
		FramedPanel editSketchFP = new FramedPanel();
		editSketchFP.setHeading("Choose Sketch");
		ToolButton addSketchButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addSketchButton.setToolTip(Util.createToolTip("Add New Wall Sketch"));
		addSketchButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				wallSketchUploadPanel = new PopupPanel();
				WallSketchUploader uploader = new WallSketchUploader(new WallSketchUploadListener() {

					@Override
					public void uploadCompleted() {
						dbService.getWallSketches(new AsyncCallback<ArrayList<WallSketchEntry>>() {

							@Override
							public void onFailure(Throwable arg0) {
								Util.doLogging(arg0.getLocalizedMessage());
								
							}

							@Override
							public void onSuccess(ArrayList<WallSketchEntry> wses) {
								for (WallSketchEntry wse: wses) {
									wallSketchEntryList.add(wse);
								}
								StaticTables.getInstance().reloadWallSketches(wses);
							}
							
						});
						wallSketchUploadPanel.hide();
					}

					@Override
					public void uploadCanceled() {
						wallSketchUploadPanel.hide();
					}
				});
				wallSketchUploadPanel.add(uploader);
				wallSketchUploadPanel.setGlassEnabled(true);
				wallSketchUploadPanel.center();
				wallSketchUploadPanel.show();

			}
		});
		editSketchFP.addTool(addSketchButton);
		sketchSelection = new ComboBox<WallSketchEntry>(wallSketchEntryList, imageProps.title(),
				new AbstractSafeHtmlRenderer<WallSketchEntry>() {

					@Override
					public SafeHtml render(WallSketchEntry item) {
						final ImageViewTemplates ivTemplates = GWT.create(ImageViewTemplates.class);
						if (item != null) {
							return ivTemplates.imageLabel(item.getFilename());							
						} else {
							return ivTemplates.imageLabel("null");
						}
					}
				});
		sketchSelection.setEmptyText("Select Sketch");
		sketchSelection.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				update();
			}
			
		});
		editSketchFP.add(sketchSelection);
		
		FramedPanel editTypeFP = new FramedPanel();
		editTypeFP.setHeading("Select Type");
		typeNF = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {

			@Override
			public String getLabel(Integer item) {
				return WallDimensionEntry.TYPE_LABEL.get(item);
			}
		});
		typeNF.add(0);
		typeNF.add(1);
		typeNF.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent arg0) {
				update();
			}
			
		});
		editTypeFP.add(typeNF);		
		hlcWallLowerView.add(editTypeFP, new HorizontalLayoutData(.4, 1));
		hlcWallLowerView.add(editDirtectionFP, new HorizontalLayoutData(.3, 1));
		hlcWallLowerView.add(editSketchFP, new HorizontalLayoutData(.3, 1));
		HorizontalLayoutContainer hlcWallButtomView = new HorizontalLayoutContainer();
		TextButton newPositionButton = new TextButton("Change Position");
		newPositionButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				wde.setRegisters(register.getValue());
				wde.setColumns(number.getValue());
				osdLoader.changePosition();
			}
		});

		hlcWallButtomView.add(newPositionButton, new HorizontalLayoutData(1, 1));
		vlcWallViewEditor.add(hlcWallUpperView, new VerticalLayoutData(1, .4));
		vlcWallViewEditor.add(hlcWallLowerView, new VerticalLayoutData(1, .4));
		vlcWallViewEditor.add(hlcWallButtomView, new VerticalLayoutData(1, .2));
		FramedPanel editDimensionFP = new FramedPanel();
		editDimensionFP.setHeading("Edit Register");
		ToolButton deleteDimension = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		deleteDimension.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Boolean found = false;
				for (CoordinateEntry ce: wde.getCoordinates()){
					if (!ce.isdeleted()) {
						found = true;
					}
				}
				for (EmptySpotEntry ese: wde.getEmptySpots()){
					if (!ese.isdeleted()) {
						found = true;
					}
				}
				if (found) {
					Info.display("Delete aborted!","there are still entries in this Register System! only empty Register Systems are to be deleted.");
				} else {
					wde.delete();
					del.deleteDimension(wde);
				}
			}
		});
		deleteDimension.setToolTip("Delete this Dimension. Works only, if no entries are set!");
		editDimensionFP.addTool(deleteDimension);
		editDimensionFP.add(vlcWallViewEditor);
		mainView.add(editDimensionFP, new VerticalLayoutData(1.0, .35));
		mainView.add(positionTabPanel, new VerticalLayoutData(1.0, .65));
		if (wallSketchEntryList.size() > 0) {
			if (wde.getWallSketch() == null) {
				WallSketchEntry wse = wallSketchEntryList.findModelWithKey("1");
				Util.doLogging("setting wallsketch to default" + wse.getFilename());
				wde.setWallSketch(wse);
				sketchSelection.setValue(wse, true);			
			} else {
				WallSketchEntry wse = wallSketchEntryList.findModelWithKey(Integer.toString(wde.getWallSketch().getWallSketchID()));
				sketchSelection.setValue(wse, true);				
			}			
		}
		if (correspondingDepictionEntry != null) {
			osdLoader = new OSDLoaderWallDimension(wde, correspondingDepictionEntry, true, osdListener, "editor");			
		} else {
			osdLoader = new OSDLoaderWallDimension(wde, true, osdListener, "editor");			
			
		}
		HTMLPanel zoomPanel = new HTMLPanel(SafeHtmlUtils.fromTrustedString("<figure class='paintRepImgPreview' style='height: 98%;width: 98%;text-align: center;'><div id= 'wallDimensionentry"+Integer.toString(wde.getWallDimensionID())+"editor' style='width: 100%; height: 100%;text-align: center;'></div></fugure>"));
		positionTabPanel.setHeading("Layout");
		positionTabPanel.add(zoomPanel);
		name.setValue(wde.getName());
		register.setValue(wde.getRegisters());
		number.setValue(wde.getColumns());
		typeNF.setValue(wde.getType());
		directionNF.setValue(wde.getDirection());
		if (wallSketchEntryList.size() > 0 && wde.getWallSketch() != null) {
			osdListener = new OSDListener() {

				@Override
				public int getDepictionID() {
					// TODO Auto-generated method stub
					return 0;
				}

				@Override
				public boolean isOrnament() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public void setAnnotationsInParent(ArrayList<AnnotationEntry> entryList) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public ArrayList<AnnotationEntry> getAnnotations() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public void addAnnotation(AnnotationEntry ae) {
					// TODO Auto-generated method stub
					
				}
				
			};			
			osdLoader.setosd();
		}


	}
	public void update() {
		if (register.isValid() && number.isValid()) {
			wde.setName(name.getCurrentValue());
			boolean isOutOfRangeRegister = false;
			boolean isOutOfRangeNumber = false;
			for (CoordinateEntry ce: wde.getCoordinates()) {
				if (ce.getRegister() > register.getCurrentValue()) {
					isOutOfRangeRegister = true;
				}
				if (ce.getNumber() > number.getCurrentValue()) {
					isOutOfRangeNumber = true;
				}
			}
			if (isOutOfRangeRegister) {
				Info.display("Register out of range", "unset given connections first!");
				register.setValue(0,true);
				register.setValue(wde.getRegisters(),true);
			} else {
				wde.setRegisters(register.getCurrentValue());			
			}
			if (isOutOfRangeNumber) {
				Info.display("Number out of range", "unset given connections first!");
				number.setValue(0, true);
				number.setValue(wde.getColumns(), true);
			} else {
				wde.setColumns(number.getCurrentValue());		
			}
			wde.setDirection(directionNF.getCurrentValue());
			wde.setType(typeNF.getCurrentValue());
			wde.setWallSketch(sketchSelection.getCurrentValue());
			if (wde.getWallSketch() != null && wallSketchEntryList.size() > 0) {
				osdLoader.setosd();				
			}
			del.saveDimension(wde);			
		}
	}
	@Override
	public Widget asWidget() {
		if (mainView == null) {
			initPanel();
		}
		return mainView;
	}
	
	
}
