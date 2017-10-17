/*
 * Copyright 2016 
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
package de.cses.client.bibliography;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.PublicationEntry;

/**
 * @author alingnau
 *
 */
public class PublicationEditor implements IsWidget {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private FramedPanel mainPanel;
	private PublicationEntry pEntry;

	/**
	 * 
	 */
	public PublicationEditor(int publicationID) {
		loadPublicationEntry(publicationID);
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initUI();
		}
		return mainPanel;
	}

	/**
	 * 
	 * @param publicationTypeID
	 */
	private void loadPublicationEntry(int id) {
		dbService.getPublicationEntry(id, new AsyncCallback<PublicationEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				pEntry = null;
			}

			@Override
			public void onSuccess(PublicationEntry result) {
				pEntry = result;
			}
		});
	}

	/**
	 * 
	 */
	private void initUI() {
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Publication Editor");
		
		
		
	}

}
