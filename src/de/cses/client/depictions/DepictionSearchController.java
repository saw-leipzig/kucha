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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;

/**
 * @author alingnau
 *
 */
public class DepictionSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public DepictionSearchController(String selectorTitle, DepictionFilter filter, DepictionResultView resultView) {
		super(selectorTitle, filter, resultView);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		DepictionSearchEntry searchEntry = (DepictionSearchEntry) getFilter().getSearchEntry();

		dbService.searchDepictions(searchEntry, new AsyncCallback<ArrayList<DepictionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onSuccess(ArrayList<DepictionEntry> result) {
				getResultView().reset();
				for (DepictionEntry de : result) {
					Util.doLogging("adding to view DepictionID = " + de.getDepictionID());
					getResultView().addResult(new DepictionView(de));
					Util.doLogging("done");
				}
				getResultView().setSearchEnabled(true);
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		final PopupPanel depictionEditorPanel = new PopupPanel(false);
		DepictionEditor de = new DepictionEditor(null);
		de.addEditorListener(new EditorListener() {
			
			@Override
			public void closeRequest(AbstractEntry entry) {
				depictionEditorPanel.hide();
				if (entry != null) {
					getResultView().addResult(new DepictionView((DepictionEntry)entry));
				}
			}

//			@Override
//			public void updateEntryRequest(AbstractEntry updatedEntry) { }
		});
		depictionEditorPanel.add(de);
		depictionEditorPanel.setGlassEnabled(true);
		depictionEditorPanel.center();
		depictionEditorPanel.show();
	}

}
