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
package de.cses.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ModifiedEntry;

/**
 * @author alingnau
 *
 */



public abstract class AbstractEditor implements IsWidget {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	protected ToolButton modifiedToolButton;
	protected ToolButton nextToolButton;
	protected ToolButton prevToolButton;
	protected ModifiedProperties modifiedProps = GWT.create(ModifiedProperties.class);
	private Grid<ModifiedEntry> grid = null;
	protected ListStore<ModifiedEntry> sourceStore;
	private ArrayList<EditorListener> listenerList = new ArrayList<EditorListener>();
	protected void doslide( int where) {
		AbstractView el = (AbstractView)listenerList.get(0);
		closeEditor(el.getEntry());
		AbstractView nextChild = (AbstractView)(((FlowLayoutContainer)el.getParent()).getWidget(((FlowLayoutContainer)el.getParent()).getWidgetIndex(el)+where));;
		nextChild.showEditor(nextChild.getEntry());

	}
	protected abstract void loadModifiedEntries() ;

	protected void createNextPrevButtons() {
		modifiedToolButton = new ToolButton(new IconConfig("foldButton", "foldButtonOver"));
		modifiedToolButton.setToolTip(Util.createToolTip("show modification history"));
		modifiedToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
			ColumnConfig<ModifiedEntry, String> modifiedByCol = new ColumnConfig<ModifiedEntry, String>(modifiedProps.modifiedBy(), 300, "Modified By");
			ColumnConfig<ModifiedEntry, String> modifiedOColn = new ColumnConfig<ModifiedEntry, String>(modifiedProps.modifiedOn(), 300, "Modified On");
//			ColumnConfig<ModifiedEntry, String> changes = new ColumnConfig<ModifiedEntry, String>(modifiedProps.tags(), 300, "Changed Values");
			
//				yearColumn.setHideable(false);
//				yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
			
		    List<ColumnConfig<ModifiedEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<ModifiedEntry, ?>>();
//		    sourceColumns.add(selectionModel.getColumn());
		    sourceColumns.add(modifiedByCol);
		    sourceColumns.add(modifiedOColn);
//		    sourceColumns.add(changes);

		    ColumnModel<ModifiedEntry> sourceColumnModel = new ColumnModel<ModifiedEntry>(sourceColumns);
		    
		    sourceStore = new ListStore<ModifiedEntry>(modifiedProps.key());

		    loadModifiedEntries();

		    grid = new Grid<ModifiedEntry>(sourceStore, sourceColumnModel);
//			    grid.setSelectionModel(selectionModel);
//			    grid.setColumnReordering(true);
		    grid.setBorders(false);
		    grid.getView().setStripeRows(true);
		    grid.getView().setColumnLines(true);
		    grid.getView().setForceFit(true);

			PopupPanel modifiedPopUp = new PopupPanel();
			FramedPanel modifiedFP = new FramedPanel();
			modifiedFP.setHeading("Modification Protocoll");
			modifiedFP.setHeight(500);
			modifiedFP.add(grid);
			modifiedPopUp.add(modifiedFP);
			ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
			closeToolButton.setToolTip(Util.createToolTip("close"));
			closeToolButton.addSelectHandler(new SelectHandler() {
				@Override
				public void onSelect(SelectEvent event) {
					modifiedPopUp.hide();
				}
			});
			modifiedFP.addTool(closeToolButton);
			modifiedPopUp.setModal(true);
			modifiedPopUp.center();

			}
		});

		nextToolButton = new ToolButton(new IconConfig("leftButton", "leftButtonOver"));
		nextToolButton.setToolTip(Util.createToolTip("next entry"));
		nextToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
//				Util.doLogging("Start caling next item.");
					AbstractView el = (AbstractView)listenerList.get(0);
					Util.doLogging(Integer.toString(el.getClickNumber()));
					if (el.getClickNumber()>0) {
						
						Util.showYesNo("Possible unsaved Changes!", "You may have changed values of this Entry. Do you whish to save them before openening another Entry?", new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								save(false,1);

							}
						}, new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								doslide(1);
							}
					
							
						}
						, new KeyDownHandler() {

							@Override
							public void onKeyDown(KeyDownEvent e) {
								save(false,1);
							}}
					
							
						);

					}
					else {
						doslide(1);
					}
				}
		});

		prevToolButton = new ToolButton(new IconConfig("rightButton", "rightButtonOver"));
		prevToolButton.setToolTip(Util.createToolTip("previous entry"));	
		prevToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
//				Util.doLogging("Start caling next item.");
				AbstractView el = (AbstractView)listenerList.get(0);
					if (el.getClickNumber()>0) {
						Util.showYesNo("Possible unsaved Changes!", "You may have changed values of this Entry. Do you whish to save them before openening another Entry?", new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								save(false,-1);
							}
						}, new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
								doslide(-1);
							}
					
							
						}
						, new KeyDownHandler() {

							@Override
							public void onKeyDown(KeyDownEvent e) {
								save(false,-1);
								
							}}
					
							
						);

					}
					else {
						doslide(-1);
					}
				}
		});

	}

	
	public void addEditorListener(EditorListener l) {
		listenerList.add(l);
	}
	public void setfocus() {
	}
	protected void save(boolean close, int slide) {
	}
	public ArrayList<EditorListener> getListenerList() {
		return listenerList;
	}	
	public void setlistenerList(ArrayList<EditorListener> l) {
		listenerList =l;
	}	
	protected void closeEditor(AbstractEntry entry) {
		for (EditorListener el : listenerList) {
			el.closeRequest(entry);
		}
	}
	protected void deleteEntry(AbstractEntry entry) {
		for (EditorListener el : listenerList) {
			dbService.deleteAbstractEntry(entry, new AsyncCallback<Boolean>() {			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean result) {
					Info.display("Delete", "Finished with result:"+Boolean.toString(result));
				}
		
		});
		try {
			((Widget)el).removeFromParent();
		}
		catch (Exception e){
			Util.doLogging("There was a problem deleting the Entry: "+e.getMessage());
		}

		}
	}
	
//	protected void updateEntry(AbstractEntry entry) {
//		for (EditorListener el : listenerList) {
//			el.updateEntryRequest(entry);
//		}
//	}
	
}
