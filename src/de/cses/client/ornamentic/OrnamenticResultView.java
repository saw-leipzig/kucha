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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractResultView;
import de.cses.shared.CaveEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticResultView extends AbstractResultView{
	//ResultView nach erfolgter Suche der Ornamentik
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	
	public OrnamenticResultView(String title) {
		super(title);
		setHeight(300);

		new DropTarget(this) {
			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof CaveEntry) {
					OrnamenticSearchEntry searchEntry = new OrnamenticSearchEntry();
					searchEntry.getCaves().add((CaveEntry) event.getData());
					dbService.searchOrnaments(searchEntry, new AsyncCallback<ArrayList<OrnamentEntry>>() {
						
						@Override
						public void onSuccess(ArrayList<OrnamentEntry> result) {
							for (OrnamentEntry oe : result) {
								addResult(new OrnamenticView(oe));
							}
						}
						
						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
			}
		};
	}

}
