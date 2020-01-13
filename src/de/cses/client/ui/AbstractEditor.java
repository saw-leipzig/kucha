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

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractEditor implements IsWidget {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ArrayList<EditorListener> listenerList = new ArrayList<EditorListener>();
	
	public void addEditorListener(EditorListener l) {
		listenerList.add(l);
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
			((Widget)el).removeFromParent();
			dbService.deleteAbstractEntry(entry, new AsyncCallback<Boolean>() {			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Boolean result) {
					
				}
		
		});
		}
	}
	
//	protected void updateEntry(AbstractEntry entry) {
//		for (EditorListener el : listenerList) {
//			el.updateEntryRequest(entry);
//		}
//	}
	
}
