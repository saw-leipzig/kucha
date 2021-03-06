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
import de.cses.shared.OrnamentEntry;

/**
 * @author alingnau
 *
 */
public class DepictionResultView extends AbstractResultView {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param title
	 */
	public void getPics(String masterImageIDs, int size, String userLogin) {
		dbService.getPicsByImageID(masterImageIDs, size, userLogin, new AsyncCallback<Map<Integer,String>>() {
			
			@Override
			public void onFailure(Throwable caught) {				
				//Info.display("getPics", "got bad response, retry");
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
					if (imgdic.containsKey(((DepictionView)getContainer().getWidget(i)).getDepictionEntry().getMasterImageID())) {
						//Util.doLogging("Got Match! Do refresh");
						((DepictionView)getContainer().getWidget(i)).refreshpic(UriUtils.fromTrustedString(imgdic.get(((DepictionView)getContainer().getWidget(i)).getDepictionEntry().getMasterImageID())));
					}
				}
				
				
			}
					});

	}
	public DepictionResultView(String title) {
		super(title);
		addMoreResults.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbService.searchDepictions((DepictionSearchEntry)getSearchEntry(), new AsyncCallback<ArrayList<DepictionEntry>>() {

					@Override
					public void onSuccess(ArrayList<DepictionEntry> result) {
						Util.doLogging("Größe des Results: "+Integer.toString(result.size()));
						int count=0;
						String imageIDs="";
						searchEntry.setEntriesShowed(searchEntry.getEntriesShowed()+searchEntry.getMaxentries());
						for (DepictionEntry de : result) {
							count++;
							//Util.doLogging("Anzahl der Wallentries bei DepictionID "+Integer.toString(de.getDepictionID())+": "+Integer.toString(de.getWalls().size()));

							addResult(new DepictionView(de,UriUtils.fromTrustedString("icons/load_active.png")));
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
				} else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					int bibID = ((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID();
					searchEntry.getBibIdList().add(bibID);
				} else if (event.getData() instanceof OrnamentEntry) {
					int icoID = ((OrnamentEntry) event.getData()).getIconographyID();
					Util.doLogging(Integer.toString(icoID));
					if (icoID>0) {
						searchEntry.getIconographyIdList().add(icoID);
					}
				} else if (event.getData() instanceof ImageEntry) {
					int imgID = ((ImageEntry) event.getData()).getImageID();
					if (imgID>0) {
						searchEntry.getImageIdList().add(imgID);
					}
				} else {
					return;
				}
				boolean startsearch=(searchEntry.getCaveIdList().size()>0)||(searchEntry.getBibIdList().size()>0)||(searchEntry.getIconographyIdList().size()>0)||(searchEntry.getImageIdList().size()>0);
				Util.doLogging("Search is starting:"+Boolean.toString(startsearch));
				Util.showYesNo("Delete old filters?", "Do you wish to delete old filters?", new SelectHandler() {
					
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
				//dbService.searchDepictions(searchEntry, new AsyncCallback<ArrayList<DepictionEntry>>() {
				//	
				//	@Override
				//	public void onFailure(Throwable caught) { }
				//	
				//	@Override
				//	public void onSuccess(ArrayList<DepictionEntry> result) {
				//		String masterImageIDs = "";
				//		for (DepictionEntry de : result) {
				//			if (masterImageIDs == "") {
				//				masterImageIDs = Integer.toString(de.getMasterImageID());
				//			}
				//			else {
				//				masterImageIDs = masterImageIDs + ","+Integer.toString(de.getMasterImageID());
				//			}
				//		}
				//	dbService.getPicsByImageID(masterImageIDs, 120, UserLogin.getInstance().getSessionID(), new AsyncCallback<Map<Integer,String>>() {
				//	
				//	@Override
				//	public void onFailure(Throwable caught) {				
				//		//Info.display("getPics", "got bad response");
				//		}
				//	
				//	@Override
				//	public void onSuccess(Map<Integer,String> imgdic) {
				//		//Info.display("getPics", "got good response");
				//		for (DepictionEntry de : result) {
				//			addResult(new DepictionView(de,UriUtils.fromTrustedString(imgdic.get(de.getMasterImageID()))));
				//		}
				//	}
				//			});
				//				
				//			
				//	}
				//});
			}
		};
	
	}

}
