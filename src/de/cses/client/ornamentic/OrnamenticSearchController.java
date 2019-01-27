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
package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.caves.CaveEditor;
import de.cses.client.caves.CaveView;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticSearchController extends AbstractSearchController {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public OrnamenticSearchController(String selectorTitle, OrnamenticFilter filter, OrnamenticResultView resultView) {
		super(selectorTitle, filter, resultView);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		OrnamenticSearchEntry searchEntry = (OrnamenticSearchEntry) getFilter().getSearchEntry();
		
		dbService.searchOrnaments(searchEntry, new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				
				caught.printStackTrace();
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				
				getResultView().reset();
				for (OrnamentEntry de : result) {
					getResultView().addResult(new OrnamenticView(de));
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
		PopupPanel ornamenticEditorPanel = new PopupPanel(false);
		OrnamenticEditor ornamenticEditor = new OrnamenticEditor(null);
		ornamenticEditor.addEditorListener(new EditorListener() {
			
			@Override
			public void closeRequest(AbstractEntry entry) {
				ornamenticEditorPanel.hide();
				if (entry != null) {
					getResultView().addResult(new OrnamenticView((OrnamentEntry)entry));
				}
			}

//			@Override
//			public void updateEntryRequest(AbstractEntry updatedEntry) { }
		});
		ornamenticEditorPanel.add(ornamenticEditor);
		ornamenticEditorPanel.setGlassEnabled(true);
		ornamenticEditorPanel.center();
	}

}
