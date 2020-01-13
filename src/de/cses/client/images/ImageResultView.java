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
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageSearchEntry;

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
		addMoreResults.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				//Info.display("Addmore ausgelöst.",Integer.toString(searchEntry.getEntriesShowed()));
				dbService.searchImages((ImageSearchEntry)getSearchEntry(), new AsyncCallback<ArrayList<ImageEntry>>() {

					@Override
					public void onSuccess(ArrayList<ImageEntry> result) {
						//Info.display("Größe des Results: ",Integer.toString(result.size()));
						int count=0;
						String imageIDs="";
						searchEntry.setEntriesShowed(searchEntry.getEntriesShowed()+searchEntry.getMaxentries());
						for (ImageEntry ie : result) {
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
						if (result.size()==searchEntry.getMaxentries()) {
							setSearchbuttonVisible();
						}
						else {
							setSearchbuttonHide();
						}
						setSearchEnabled(true);
						
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						setSearchEnabled(true);
					}
				});
			}
		});
		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				clear();
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
					}
					if (imageIDs != "") {
						getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());				
					}
					return;
				} else if (event.getData() instanceof CaveEntry) {
					dse.getCaveIdList().add(((CaveEntry) event.getData()).getCaveID());
				} else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					int bibID = ((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID();
					dse.getBibIdList().add(bibID);
				}
				searchEntry=dse;
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
								if (result.size()==searchEntry.getMaxentries()) {
									setSearchbuttonVisible();
								}
								else {
									setSearchbuttonHide();
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
				//Info.display("getPics", "got bad response, retry");
				getPics(masterImageIDs , res, sessionID);
			}
			
			@Override
			public void onSuccess(Map<Integer,String> imgdic) {
				int count = 0;
				int sizeres =imgdic.size();
				//Info.display("getPics", "got good response");
				//for (DepictionEntry de : result) {
				//	
				//}
				//Util.doLogging("Anzahl der Widgets: "+Integer.toString(getResultView().getContainer().getWidgetCount()));
				for (int i = getContainer().getWidgetCount()-1; i > -1; i--) {
					if (imgdic.containsKey(((ImageEntry)((ImageView)getContainer().getWidget(i)).getEntry()).getImageID())) {
						//Util.doLogging("Got Match! Do refresh");
						count++;
						((ImageView)getContainer().getWidget(i)).refreshpic(UriUtils.fromTrustedString(imgdic.get(((ImageEntry)((ImageView)getContainer().getWidget(i)).getEntry()).getImageID())));
						
					}
					if (count==sizeres) {
						i=-1;
					}
				}
				
				
			}
					});
	}

}
