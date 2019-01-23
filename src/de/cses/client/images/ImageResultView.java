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
package de.cses.client.images;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.depictions.DepictionView;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageEntry;

/**
 * @author alingnau
 *
 */
public class ImageResultView extends AbstractResultView {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param title
	 */
	public ImageResultView(String title) {
		super(title);
		setHeight(300);

		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				DepictionSearchEntry dse;
				if (UserLogin.isLoggedIn()) {
					dse = new DepictionSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					dse = new DepictionSearchEntry();
				}
				if (event.getData() instanceof DepictionEntry) {
					for (ImageEntry ie : ((DepictionEntry) event.getData()).getRelatedImages()) {
						addResult(new ImageView(ie));
					}
					return;
				} else if (event.getData() instanceof CaveEntry) {
					dse.getCaveIdList().add(((CaveEntry) event.getData()).getCaveID());
				} else if (event.getData() instanceof AnnotatedBiblographyEntry) {
					int bibID = ((AnnotatedBiblographyEntry) event.getData()).getAnnotatedBiblographyID();
					dse.getBibIdList().add(bibID);
				}
				dbService.searchDepictions(dse, new AsyncCallback<ArrayList<DepictionEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ArrayList<DepictionEntry> result) {
						for (DepictionEntry de : result) {
							for (ImageEntry ie : de.getRelatedImages()) {
								addResult(new ImageView(ie));
							}
						}
					}
				});

			}
		};
	}

}
