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
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.Util;
import de.cses.client.user.UserLogin;
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
				if (UserLogin.isLoggedIn()) {
					showEditor();
//				} else if (getEntry().isOpenAccess()) {
//					viewDataSet(getPermalink());
				} else {
					Util.showWarning("Permission denied", "Sorry, you either don't have the permission\n to edit or you are currently not logged in!");
				}
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
	}
	
	private void viewDataSet(String url) { // 
		Window.open(url,"_blank",null);
	}
	
	abstract protected AbstractEditor getEditor();
	
	abstract protected AbstractEntry getEntry();
	
	abstract protected String getPermalink(); // this will be the URI for the server request...
	 
	@Override
	public void closeRequest() {
		editorPanel.hide();
		getElement().getStyle().setBorderColor("#FFA500");
		getElement().getStyle().setBorderWidth(3.0, Unit.PX);
	}

	
}
