/*
 * Copyright 2017-2018 
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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractView extends Button implements EditorListener {

	private static PopupPanel editorPanel;
	
	/**
	 * This is the general constructor that amongst other tasks initializes the PopupPanel for the editor
	 */
	public AbstractView() {
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (UserLogin.getInstance().getAccessRights() >= UserEntry.FULL) { // guests are not allowed to edit
					showEditor(getEntry());
				}
			}
		});
	}

	public void showEditor(AbstractEntry entry) {
		AbstractEditor editor = getEditor(entry);
		editor.addEditorListener(this);
		loadEditor(editor);
	}
	public static void loadEditor(AbstractEditor editor) {
		
		editorPanel = new PopupPanel(false);
		editorPanel.add(editor);
		editorPanel.setModal(true);
		editorPanel.setGlassEnabled(true);
		editorPanel.center();
		editor.setfocus();
		}
	private void viewDataSet(String url) { // 
		Window.open(url,"_blank",null);
	}
	
	abstract protected AbstractEditor getEditor(AbstractEntry entry);
	
	abstract protected AbstractEntry getEntry();
	
	abstract protected String getPermalink(); // this will be the URI for the server request...
	 
	@Override
	public void closeRequest(AbstractEntry entry) {
		editorPanel.hide();
		getElement().getStyle().setBorderColor("#FFA500");
		getElement().getStyle().setBorderWidth(3.0, Unit.PX);
	}

	
}
