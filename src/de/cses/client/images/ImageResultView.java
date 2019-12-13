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
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.depictions.DepictionView;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
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
		//setHeight(300);

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
					String imageIDs="";
					int count=0;
					for (ImageEntry ie : ((DepictionEntry) event.getData()).getRelatedImages()) {
						addResult(new ImageView(ie,UriUtils.fromTrustedString("icons/load_active.png")));
						if (imageIDs == "") {
							imageIDs = Integer.toString(ie.getImageID());
						}
						else {
							imageIDs = imageIDs + ","+Integer.toString(ie.getImageID());
						}
						if (count==20 ){
							getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
							imageIDs="";
							count=0;
						}
						getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
					}
					return;
				} else if (event.getData() instanceof CaveEntry) {
					dse.getCaveIdList().add(((CaveEntry) event.getData()).getCaveID());
				} else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					int bibID = ((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID();
					dse.getBibIdList().add(bibID);
				}
				dbService.searchDepictions(dse, new AsyncCallback<ArrayList<DepictionEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(ArrayList<DepictionEntry> result) {
						String imageIDs = "";
						int count = 0;
						for (DepictionEntry de : result) {
							for (ImageEntry ie : de.getRelatedImages()) {
								count++;
								addResult(new ImageView(ie,UriUtils.fromTrustedString("icons/load_active.png")));
								if (imageIDs == "") {
									imageIDs = Integer.toString(ie.getImageID());
								}
								else {
									imageIDs = imageIDs + ","+Integer.toString(ie.getImageID());
								}
								if (count==20 ){
									getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
									imageIDs="";
									count=0;
								}
								
							
							}
							getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
							
						}
					}
				});

			}
		};
	}
	@Override
	public void getPics(String masterImageIDs , int res, String sessionID) {
		dbService.getPicsByImageID(masterImageIDs, 120, UserLogin.getInstance().getSessionID(), new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onFailure(Throwable caught) {				
				Info.display("getPics", "got bad response");
			}
			
			@Override
			public void onSuccess(Map<Integer,String> imgdic) {
				//Info.display("getPics", "got good response");
				//for (DepictionEntry de : result) {
				//	
				//}
				//Util.doLogging("Anzahl der Widgets: "+Integer.toString(getResultView().getContainer().getWidgetCount()));
				for (int i = 0; i < getContainer().getWidgetCount(); i++) {
					//Util.doLogging("Überprüfe Eintrag: "+Integer.toString(((DepictionView)getResultView().getContainer().getWidget(i)).getDepictionEntry().getDepictionID()));
					if (imgdic.containsKey(((ImageEntry)((ImageView)getContainer().getWidget(i)).getEntry()).getImageID())) {
						//Util.doLogging("Got Match! Do refresh");
						((ImageView)getContainer().getWidget(i)).refreshpic(UriUtils.fromTrustedString(imgdic.get(((ImageEntry)((ImageView)getContainer().getWidget(i)).getEntry()).getImageID())));
					}
				}
				
				
			}
					});
	}

}
