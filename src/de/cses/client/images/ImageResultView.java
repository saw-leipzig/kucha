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
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageSearchEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class ImageResultView extends AbstractResultView {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	ImageResultView irv;

	/**
	 * @param title
	 */

	public ImageResultView(String title) {
		super(title);
		//setHeight(300);
		irv=this;
		addMoreResults.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				//Info.display("Addmore ausgelöst.",Integer.toString(searchEntry.getEntriesShowed()));
				dbService.searchImages((ImageSearchEntry)getSearchEntry(), new AsyncCallback<Map<Integer, ArrayList<ImageEntry>>>() {

					@Override
					public void onSuccess(Map<Integer,ArrayList<ImageEntry>> result) {
						//Info.display("Größe des Results: ",Integer.toString(result.size()));
						int count=0;
						String imageIDs="";
						searchEntry.setEntriesShowed(searchEntry.getEntriesShowed()+searchEntry.getMaxentries());
						for (Integer key : result.keySet()) {
							if (searchEntry.getMaxentries()>key) {
								setSelectorTitle(" ("+Integer.toString(key)+"/"+Integer.toString(key)+")");
							}
							else{
								setSelectorTitle(" ("+Integer.toString(searchEntry.getMaxentries())+"/"+Integer.toString(key)+")");
							}
							for (ImageEntry ie: result.get(key)) {
								
								count++;
								addResult(new ImageView(ie,UriUtils.fromTrustedString("icons/load_active.png"), irv));
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
							if (result.get(key).size()==searchEntry.getMaxentries()) {
								setSearchbuttonVisible();
							}
							else {
								setSearchbuttonHide();
							}

						}
						getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());				
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
				ImageSearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new ImageSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new ImageSearchEntry();
				}
				String shortName="";
				if (event.getData() instanceof CaveEntry) {
					for (SiteEntry se : StaticTables.getInstance().getSiteEntries().values()) {
						if (se.getSiteID()==((CaveEntry) event.getData()).getSiteID()) {
							shortName=se.getShortName();
							break;
						}

					}
				searchEntry.setTitleSearch(shortName+" "+((CaveEntry) event.getData()).getOfficialNumber());
				}
				else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					int bibID = ((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID();
					searchEntry.getBibIdList().add(bibID);
				} else if (event.getData() instanceof DepictionEntry) {
					for (ImageEntry img : ((DepictionEntry)event.getData()).getRelatedImages()) {
						searchEntry.getImageIdList().add(img.getImageID());						
					}
				} else if (event.getData() instanceof OrnamentEntry) {
					ArrayList<ImageEntry> imgIDs = ((OrnamentEntry) event.getData()).getImages();
					if (imgIDs!=null) {
						if (imgIDs.size()>0) {
							for (ImageEntry img: imgIDs) {
								searchEntry.getImageIdList().add(img.getImageID());
							}	
						}
					}
				} else {
					return;
				}
				boolean startsearch=(searchEntry.getCaveIdList().size()>0)||(searchEntry.getBibIdList().size()>0)||(searchEntry.getImageIdList().size()>0||(searchEntry.getTitleSearch()!=""));
				Util.doLogging(Boolean.toString(startsearch));
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

//				}
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
