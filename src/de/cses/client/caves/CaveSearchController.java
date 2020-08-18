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
package de.cses.client.caves;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.comparator.CaveEntryComparator;
//import javafx.scene.control.Button;

/**
 * @author alingnau
 *
 */
public class CaveSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param caveResultView
	 */
	public CaveSearchController(String selectorTitle, CaveFilter filter, CaveResultView caveResultView, ToolButton inactiveTB, ToolButton activeTB) {
		super(selectorTitle, filter, caveResultView, inactiveTB, activeTB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		Util.doLogging("calling CaveSearchController.invokeSearch()"); 
		CaveSearchEntry searchEntry = (CaveSearchEntry) getFilter().getSearchEntry();
		
		dbService.searchCaves(searchEntry, new AsyncCallback<ArrayList<CaveEntry>>() {
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				Util.doLogging("Cave search sucessful");
				result.sort(new CaveEntryComparator());
				getResultView().reset();
				for (CaveEntry ce : result) {
					getResultView().addResult(new CaveView(ce));	
				}
				getResultView().setSearchEnabled(true);
			}

		});
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		DialogBox caveEditorPanel = new DialogBox();
		EditorListener el = new EditorListener() {

			@Override
			public void closeRequest(AbstractEntry entry) {
				caveEditorPanel.hide();
				if (entry != null) {
					getResultView().addResult(new CaveView((CaveEntry)entry));
				}
			}

//			@Override
//			public void updateEntryRequest(AbstractEntry updatedEntry) { }
		};
		CaveEditor ced = new CaveEditor(null, el);
		ScrollPanel scrpanel = new ScrollPanel();
//		scrpanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
//		scrpanel.add(ced);
		caveEditorPanel.add(ced);
		caveEditorPanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		caveEditorPanel.setModal(true);
		caveEditorPanel.setGlassEnabled(true);
		
	}

}
