/*
 * Copyright 2017 - 2019
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.comparator.CaveEntryComparator;

/**
 * @author alingnau
 *
 */
public class CaveResultView extends AbstractResultView {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * 
	 */
	public CaveResultView(String title) {
		super(title);
//		setHeight(300);

		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				CaveSearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new CaveSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new CaveSearchEntry();
				}
				if (event.getData() instanceof OrnamentEntry) {
					for (OrnamentCaveRelation ocr : ((OrnamentEntry)event.getData()).getCavesRelations()) {
						searchEntry.getCaveIdList().add(ocr.getCaveEntry().getCaveID());
					}
					dbService.searchCaves(searchEntry, new AsyncCallback<ArrayList<CaveEntry>>() {

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(ArrayList<CaveEntry> result) {
							result.sort(new CaveEntryComparator());
							for (CaveEntry ce : result) {
								addResult(new CaveView(ce));
							}
						}
					});
				}
			}
		};
	}

}
