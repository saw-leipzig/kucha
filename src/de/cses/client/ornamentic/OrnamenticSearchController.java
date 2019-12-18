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
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamenticSearchEntry;

/**
 * @author nina
 *
 */
public class OrnamenticSearchController extends AbstractSearchController {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	interface Resources extends ClientBundle {
		@Source("ornamentation.png")
		ImageResource logo();
	}
	Resources resources;
	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public OrnamenticSearchController(String selectorTitle, OrnamenticFilter filter, OrnamenticResultView resultView, ToolButton inactiveTB, ToolButton activeTB) {
		super(selectorTitle, filter, resultView, inactiveTB, activeTB);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		OrnamenticSearchEntry searchEntry = (OrnamenticSearchEntry) getFilter().getSearchEntry();
		resources = GWT.create(Resources.class);
		dbService.searchOrnaments(searchEntry, new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				
				caught.printStackTrace();
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				String masterImageIDs = "";
				int count = 0;
				searchEntry.setEntriesShowed(searchEntry.getMaxentries());
				getResultView().reset();
				if (result.size()==searchEntry.getMaxentries()) {
					getResultView().setSearchbuttonVisible();
				}
				else {
					getResultView().setSearchbuttonHide();
				}
			int x = result.size();
			getResultView().setSearchEnabled(true);
			for (OrnamentEntry oe : result) {
				if ((oe.getImages() != null) && (!oe.getImages().isEmpty())) {
				count++;
				if (masterImageIDs == "") {
					masterImageIDs = Integer.toString(oe.getImages().get(0).getImageID());
				}
				else {
					masterImageIDs = masterImageIDs + ","+Integer.toString(oe.getImages().get(0).getImageID());
				}
				}
				getResultView().addResult(new OrnamenticView(oe,resources.logo().getSafeUri()));
				if (count==20 ){
					getResultView().getPics(masterImageIDs, 80, UserLogin.getInstance().getSessionID()) ;
					masterImageIDs="";
					count=0;
				}

			}
			getResultView().getPics(masterImageIDs, 80, UserLogin.getInstance().getSessionID()) ;
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		DialogBox ornamenticEditorPanel = new DialogBox(false);
		OrnamenticEditor ornamenticEditor = new OrnamenticEditor(null);
		ornamenticEditor.addEditorListener(new EditorListener() {
			
			@Override
			public void closeRequest(AbstractEntry entry) {
				ornamenticEditorPanel.hide();
				if (entry != null) {
					getResultView().addResult(new OrnamenticView((OrnamentEntry)entry,resources.logo().getSafeUri()));
					dbService.getPicsByImageID(Integer.toString(((OrnamentEntry)entry).getImages().get(0).getImageID()), 80, UserLogin.getInstance().getSessionID(), new AsyncCallback<Map<Integer,String>>() {
						
						@Override
						public void onFailure(Throwable caught) {				
							Util.doLogging(caught.getMessage());
						}
						
						@Override
						public void onSuccess(Map<Integer,String> imgdic) {
							getResultView().addResult(new OrnamenticView((OrnamentEntry)entry, UriUtils.fromTrustedString(imgdic.get(((OrnamentEntry)entry).getImages().get(0).getImageID()))));
						}

						});
				}
			}
		});
		ornamenticEditorPanel.add(ornamenticEditor);
		ornamenticEditorPanel.setGlassEnabled(true);
		ornamenticEditorPanel.center();
	}

}
