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
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;

/**
 * @author alingnau
 *
 */
public class DepictionSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public DepictionSearchController(String selectorTitle, DepictionFilter filter, DepictionResultView resultView, ToolButton inactiveTB, ToolButton activeTB) {
		super(selectorTitle, filter, resultView, inactiveTB, activeTB);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	

	@Override
	public void invokeSearch() {
		DepictionSearchEntry searchEntry = (DepictionSearchEntry) getFilter().getSearchEntry();

		dbService.searchDepictions(searchEntry, new AsyncCallback<ArrayList<DepictionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onSuccess(ArrayList<DepictionEntry> result) {
				String masterImageIDs = "";
				//Info.display("Result", "Größe = "+Integer.toString(result.size()));
				int count = 0;
				searchEntry.setEntriesShowed(searchEntry.getMaxentries());
				getResultView().reset();
				getResultView().setSearchEntry(searchEntry);
				if (result.size()==searchEntry.getMaxentries()) {
					getResultView().setSearchbuttonVisible();
				}
				else {
					getResultView().setSearchbuttonHide();
				}
				getResultView().setSearchEnabled(true);
				int x = result.size();
				for (DepictionEntry de : result){
					count++;
					getResultView().addResult(new DepictionView(de,UriUtils.fromTrustedString("icons/load_active.png")));
					if (masterImageIDs == "") {
						masterImageIDs = Integer.toString(de.getMasterImageID());
					}
					else {
						masterImageIDs = masterImageIDs + ","+Integer.toString(de.getMasterImageID());
					}
//					Util.doLogging("Lade Depiction: "+de.getShortName());
					if (count==20 ){
						getResultView().getPics(masterImageIDs, 120, UserLogin.getInstance().getSessionID()) ;
						masterImageIDs="";
						count=0;
					}
					
				}
				if (masterImageIDs != "") {
					getResultView().getPics(masterImageIDs, 120, UserLogin.getInstance().getSessionID()) ;
				}
				getResultView().setSearchEnabled(true);
				
			}
		});
	}
	
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		final PopupPanel depictionEditorPanel = new PopupPanel(false);
		DepictionEditor de = new DepictionEditor(null);
		de.addEditorListener(new EditorListener() {
			
			@Override
			public void closeRequest(AbstractEntry entry) {
				depictionEditorPanel.hide();
				if (entry != null) {	
					dbService.getPicsByImageID(Integer.toString(((DepictionEntry)entry).getMasterImageID()), 120, UserLogin.getInstance().getSessionID(), new AsyncCallback<Map<Integer,String>>() {
				
						@Override
						public void onFailure(Throwable caught) {				
							Info.display("getPics", "got bad response");
						}
						
						@Override
						public void onSuccess(Map<Integer,String> imgdic) {
							Info.display("getPics", "got good response");
							
							getResultView().addResult(new DepictionView((DepictionEntry)entry, UriUtils.fromTrustedString(imgdic.get(((DepictionEntry)entry).getMasterImageID()))));
						}
					});
				}
			}

//			@Override
//			public void updateEntryRequest(AbstractEntry updatedEntry) { }
		});
		depictionEditorPanel.add(de);
		depictionEditorPanel.setGlassEnabled(true);
		depictionEditorPanel.center();
		depictionEditorPanel.show();
	}

}
