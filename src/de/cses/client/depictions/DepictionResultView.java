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
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;

/**
 * @author alingnau
 *
 */
public class DepictionResultView extends AbstractResultView {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param title
	 */
	public DepictionResultView(String title) {
		super(title);
		
		DropTarget target = new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				DepictionSearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new DepictionSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new DepictionSearchEntry();
				}
				if (event.getData() instanceof CaveEntry) {
					int caveID = ((CaveEntry) event.getData()).getCaveID();
					searchEntry.getCaveIdList().add(caveID);
				} else if (event.getData() instanceof AnnotatedBiblographyEntry) {
					int bibID = ((AnnotatedBiblographyEntry) event.getData()).getAnnotatedBiblographyID();
					searchEntry.getBibIdList().add(bibID);
				} else {
					return;
				}
				dbService.searchDepictions(searchEntry, new AsyncCallback<ArrayList<DepictionEntry>>() {
					
					@Override
					public void onFailure(Throwable caught) { }
					
					@Override
					public void onSuccess(ArrayList<DepictionEntry> result) {
						for (DepictionEntry de : result) {
							addResult(new DepictionView(de));
						}
					}
				});
			}
		};
	
	}

}
