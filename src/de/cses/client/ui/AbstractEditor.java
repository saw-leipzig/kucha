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

import com.google.gwt.user.client.ui.IsWidget;

import de.cses.shared.AbstractEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractEditor implements IsWidget {

	private ArrayList<EditorListener> listenerList = new ArrayList<EditorListener>();
	
	public void addEditorListener(EditorListener l) {
		listenerList.add(l);
	}
	
	protected void closeEditor() {
		for (EditorListener el : listenerList) {
			el.closeRequest();
		}
	}
	
	protected void updateEntry(AbstractEntry entry) {
		for (EditorListener el : listenerList) {
			el.updateEntryRequest(entry);
		}
	}

}
