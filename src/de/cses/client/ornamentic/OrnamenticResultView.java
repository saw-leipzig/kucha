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
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import de.cses.client.depictions.DepictionView;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticResultView extends AbstractResultView{
	//ResultView nach erfolgter Suche der Ornamentik
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public void getPics(String masterImageIDs, int size, String userLogin) {
		dbService.getPicsByImageID(masterImageIDs, size, userLogin, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onFailure(Throwable caught) {				
				Info.display("getPics", "got bad response, retry");
				getPics(masterImageIDs, size, userLogin);
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
					if (((OrnamentEntry)((OrnamenticView)getContainer().getWidget(i)).getEntry()).getImages().size()>0){
						if (imgdic.containsKey(((OrnamentEntry)((OrnamenticView)getContainer().getWidget(i)).getEntry()).getMasterImageID())) {
							//Util.doLogging(imgdic.get(((OrnamentEntry)((OrnamenticView)getContainer().getWidget(i)).getEntry()).getMasterImageID()));
							((OrnamenticView)getContainer().getWidget(i)).refreshpic(UriUtils.fromTrustedString(imgdic.get(((OrnamentEntry)((OrnamenticView)getContainer().getWidget(i)).getEntry()).getMasterImageID())));
						}
					}
				}
				
				
			}
					});

	}
	public OrnamenticResultView(String title) {
		super(title);
		//setHeight(300);
		addMoreResults.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				OrnamenticSearchEntry oes = (OrnamenticSearchEntry)getSearchEntry();
				dbService.searchOrnaments((OrnamenticSearchEntry)getSearchEntry(), new AsyncCallback<ArrayList<OrnamentEntry>>() {

					@Override
					public void onSuccess(ArrayList<OrnamentEntry> result) {
						Util.doLogging("Größe des Results: "+Integer.toString(result.size()));
						int count=0;
						String imageIDs="";
						searchEntry.setEntriesShowed(searchEntry.getEntriesShowed()+searchEntry.getMaxentries());
						for (OrnamentEntry de : result) {
							count++;
							//Util.doLogging("Anzahl der Wallentries bei DepictionID "+Integer.toString(de.getDepictionID())+": "+Integer.toString(de.getWalls().size()));

							addResult(new OrnamenticView(de,UriUtils.fromTrustedString("icons/load_active.png")));
							if (imageIDs == "") {
								imageIDs = Integer.toString(de.getMasterImageID());
							}
							else {
								imageIDs = imageIDs + ","+Integer.toString(de.getMasterImageID());
							}
							if (count==20 ){
								getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
								imageIDs="";
								count=0;
							}
						}
						if (imageIDs !="") {
							getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
						}
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
				OrnamenticSearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new OrnamenticSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new OrnamenticSearchEntry();
				}
				if (event.getData() instanceof CaveEntry) {
					searchEntry.getCaves().add((CaveEntry) event.getData());
				}
				else if (event.getData() instanceof DepictionEntry) {
					searchEntry.setIconographys(((DepictionEntry) event.getData()).getRelatedIconographyList());
				}
				else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					searchEntry.getBibIdList().add(((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID());
				}
				else if (event.getData() instanceof ImageEntry) {
					searchEntry.getImageIDList().add(((ImageEntry) event.getData()).getImageID());
				}
				else {
					return;
				}
				boolean startsearch=(searchEntry.getCaves().size()>0)||(searchEntry.getIconographys().size()>0)||(searchEntry.getBibIdList().size()>0)||(searchEntry.getImageIDList().size()>0);
				Util.showYesNo("Delete old filters?", "Do you whisch to delete old filters?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						initiateSearch(searchEntry,startsearch,true);
					}
				}, new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						initiateSearch(searchEntry,startsearch,false);
					}},
					new KeyDownHandler() {
						public void onKeyDown(KeyDownEvent e) {
							initiateSearch(searchEntry,startsearch,true);
					}
				});
				
//				dbService.searchOrnaments(searchEntry, new AsyncCallback<ArrayList<OrnamentEntry>>() {
//					
//					@Override
//					public void onSuccess(ArrayList<OrnamentEntry> result) {
//						int count =0;
//						String masterImageIDs = "";
//						for (OrnamentEntry oe : result) {
//							if ((oe.getImages() != null) && (!oe.getImages().isEmpty())) {
//							count++;
//							if (masterImageIDs == "") {
//								masterImageIDs = Integer.toString(oe.getMasterImageID());
//							}
//							else {
//								masterImageIDs = masterImageIDs + ","+Integer.toString(oe.getMasterImageID());
//							}
//							}
//							addResult(new OrnamenticView(oe,UriUtils.fromTrustedString("icons/load_active.png")));
//							if (count==20 ){
//								getPics(masterImageIDs, 80, UserLogin.getInstance().getSessionID()) ;
//								masterImageIDs="";
//								count=0;
//							}
//
//						}
//						getPics(masterImageIDs, 80, UserLogin.getInstance().getSessionID()) ;
//					}
//					
//					@Override
//					public void onFailure(Throwable caught) {
//					}
//				});
			}
		};
	}

}
