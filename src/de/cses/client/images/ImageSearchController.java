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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.DialogBox;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageSearchEntry;

/**
 * @author alingnau
 *
 */
public class ImageSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public ImageSearchController(String selectorTitle, ImageFilter filter, ImageResultView resultView, ToolButton inactiveTB, ToolButton activeTB) {
		super(selectorTitle, filter, resultView, inactiveTB, activeTB);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ImageSearchEntry searchEntry = (ImageSearchEntry) getFilter().getSearchEntry();
		dbService.searchImages(searchEntry, new AsyncCallback<Map<Integer,ArrayList<ImageEntry>>>() {

			@Override
			public void onSuccess(Map<Integer,ArrayList<ImageEntry>> result) {
				getResultView().reset();
				int count=0;
				String imageIDs="";
				searchEntry.setEntriesShowed(searchEntry.getMaxentries());
				getResultView().setSearchEntry(searchEntry);
				getResultView().setSearchEnabled(true);
				for (Integer key : result.keySet()) {
					if (result.get(key).size()<=searchEntry.getMaxentries()) {
						getResultView().setSearchbuttonVisible();
					}
					else {
						getResultView().setSearchbuttonHide();
					}
					if (searchEntry.getMaxentries()>key) {
						getResultView().setSelectorTitle(" ("+Integer.toString(key)+"/"+Integer.toString(key)+")");
					}
					else{
						getResultView().setSelectorTitle(" ("+Integer.toString(searchEntry.getMaxentries())+"/"+Integer.toString(key)+")");
					}
					for (ImageEntry ie: result.get(key)) {
						count++;
						getResultView().addResult(new ImageView(ie,UriUtils.fromTrustedString("icons/load_active.png"),((ImageResultView)getResultView())));
						if (imageIDs == "") {
							imageIDs = Integer.toString(ie.getImageID());
						}
						else {
							imageIDs = imageIDs + ","+Integer.toString(ie.getImageID());
						}
						if (count==20 ){
							getResultView().getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
							imageIDs="";
							count=0;
						}
					}
				}

				if (imageIDs != "") {
					getResultView().getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());				
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			     try {
			         throw caught;
			       } catch (Exception e) {
			         // this client is not compatible with the server; cleanup and refresh the 
			         // browser
			    	   Throwable cause = e.getCause();
			    	   Util.doLogging(cause.getMessage());

			       } catch (Throwable e) {
			         // last resort -- a very unexpected exception
			    	   Util.doLogging(e.getMessage());
			       }
				
				getResultView().setSearchEnabled(true);
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		final DialogBox imageUploadPanel = new DialogBox(false);
		final DialogBox imageEditorPanel = new DialogBox(false);
		
		ImageUploader iu = new ImageUploader(new ImageUploadListener() {

			@Override
			public void uploadCompleted(int newImageID, final String filename) {
				imageUploadPanel.hide();
				dbService.getImage(newImageID, new AsyncCallback<ImageEntry>() {

					@Override
					public void onSuccess(ImageEntry result) {
						ImageEntry imgEntry = result;
						imgEntry.setTitle(filename);
						EditorListener el = new EditorListener() {
							
							@Override
							public void closeRequest(AbstractEntry entry) {
								imageEditorPanel.hide();
								if (entry != null) {
									if (!imgEntry.isdeleted()) {
										getResultView().addResult(new ImageView((ImageEntry)entry,UriUtils.fromTrustedString("icons/load_active.png"),((ImageResultView)getResultView())));
										String imageIDs = Integer.toString(((ImageEntry)entry).getImageID());
										getResultView().getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
									}
								} else {
									// we should at least save the title of the image!
									dbService.updateImageEntry(imgEntry, new AsyncCallback<Boolean>() {
										
										@Override
										public void onSuccess(Boolean result) { 
											if (!imgEntry.isdeleted()) {
												getResultView().addResult(new ImageView(imgEntry,UriUtils.fromTrustedString("icons/close_icon.png"),((ImageResultView)getResultView())));
											}
										}
										
										@Override
										public void onFailure(Throwable caught) {
											Info.display("ERROR", "Image information has NOT been updated!");
										}
									});
								}
							}
							public Integer getClickNumber() {
								return 0;
							}
							public void addClickNumber() {
							}
							public void setClickNumber(int clicks) {
							}

						};
						SingleImageEditor singleIE = new SingleImageEditor(imgEntry, el);
						//singleIE.addEditorListener(
						imageEditorPanel.add(singleIE);
						imageEditorPanel.setGlassEnabled(true);
						imageEditorPanel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
						imageEditorPanel.setModal(true);
						
						imageEditorPanel.center();
						//imageEditorPanel.show();
						singleIE.setfocus();
					}

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}
				});
			}

			@Override
			public void uploadCanceled() {
				imageUploadPanel.hide();
			}
		});
		imageUploadPanel.add(iu);
		imageUploadPanel.setGlassEnabled(true);
		imageUploadPanel.center();
		imageUploadPanel.show();
	}

}
