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
package de.cses.client.bibliography;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.depictions.DepictionView;
import de.cses.client.ui.AbstractResultView;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyResultView extends AbstractResultView{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param title
	 */
	public AnnotatedBiblographyResultView(String title) {
		super(title);
		
		DropTarget target = new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof CaveEntry) {
					int caveID = ((CaveEntry) event.getData()).getCaveID();
					dbService.getAnnotatedBiblography("CaveID="+caveID, new AsyncCallback<ArrayList<AnnotatedBiblographyEntry>>() {

						@Override
						public void onFailure(Throwable caught) { }

						public void onSuccess(ArrayList<AnnotatedBiblographyEntry> result) {
							for (AnnotatedBiblographyEntry de : result) {
								addResult(new AnnotatedBiblographyView(de));
							}
						}
					});
				}
			}
		};
	
	}

}
