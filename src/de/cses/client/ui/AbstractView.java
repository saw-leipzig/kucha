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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.fx.client.Draggable;

import de.cses.shared.AbstractEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractView extends Button implements EditorListener {

	private PopupPanel editorPanel;
	
	/**
	 * This is the general constructor that amongst other tasks initializes the PopupPanel for the editor
	 */
	public AbstractView() {
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				showEditor();
			}
		});
	}

	private void showEditor() {
		AbstractEditor editor = getEditor();
		editor.addEditorListener(this);
		editorPanel = new PopupPanel(false);
		editorPanel.add(editor);
		editorPanel.setGlassEnabled(true);
		editorPanel.center();
		editorPanel.show();
	}

	abstract protected AbstractEditor getEditor();
	
	abstract protected AbstractEntry getEntry();
	
	@Override
	public void closeRequest() {
		editorPanel.hide();
	}

	
}
