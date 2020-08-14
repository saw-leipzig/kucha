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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.AbstractEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractEditor implements IsWidget {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	protected ToolButton nextToolButton;
	protected ToolButton prevToolButton;
	private ArrayList<EditorListener> listenerList = new ArrayList<EditorListener>();
	
	protected void createNextPrevButtons() {
		nextToolButton = new ToolButton(new IconConfig("leftButton", "leftButtonOver"));
		nextToolButton.setToolTip(Util.createToolTip("next entry"));
		nextToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
//				Util.doLogging("Start caling next item.");
					AbstractView el = (AbstractView)listenerList.get(0);
					closeEditor(el.getEntry());
					AbstractView nextChild = (AbstractView)(((FlowLayoutContainer)el.getParent()).getWidget(((FlowLayoutContainer)el.getParent()).getWidgetIndex(el)+1));;
					nextChild.showEditor(nextChild.getEntry());
				}
		});
		prevToolButton = new ToolButton(new IconConfig("rightButton", "rightButtonOver"));
		prevToolButton.setToolTip(Util.createToolTip("previous entry"));	
		prevToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
//				Util.doLogging("Start caling next item.");
					AbstractView el = (AbstractView)listenerList.get(0);
					closeEditor(el.getEntry());
					AbstractView nextChild = (AbstractView)(((FlowLayoutContainer)el.getParent()).getWidget(((FlowLayoutContainer)el.getParent()).getWidgetIndex(el)-1));;
					nextChild.showEditor(nextChild.getEntry());
				}
		});

	}

	
	public void addEditorListener(EditorListener l) {
		listenerList.add(l);
	}
	public void setfocus() {
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
					//Info.display("Delete", "Finished with result:"+Boolean.toString(result));
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
