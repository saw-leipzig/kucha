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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
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
	public ImageSearchController(String selectorTitle, ImageFilter filter, ImageResultView resultView) {
		super(selectorTitle, filter, resultView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ImageSearchEntry searchEntry = (ImageSearchEntry) getFilter().getSearchEntry();

		dbService.searchImages(searchEntry, new AsyncCallback<ArrayList<ImageEntry>>() {

			@Override
			public void onSuccess(ArrayList<ImageEntry> result) {
				getResultView().reset();
				for (ImageEntry ie : result) {
					getResultView().addResult(new ImageView(ie));
				}
				getResultView().setSearchEnabled(true);
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
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
		final PopupPanel imageUploadPanel = new PopupPanel(false);
		final PopupPanel imageEditorPanel = new PopupPanel(false);

		ImageUploader iu = new ImageUploader(new ImageUploadListener() {

			@Override
			public void uploadCompleted(int newImageID, final String filename) {
				imageUploadPanel.hide();
				dbService.getImage(newImageID, new AsyncCallback<ImageEntry>() {

					@Override
					public void onSuccess(ImageEntry result) {
						ImageEntry imgEntry = result;
						imgEntry.setTitle(filename);
						SingleImageEditor singleIE = new SingleImageEditor(imgEntry);
						singleIE.addEditorListener(new EditorListener() {
							
							@Override
							public void closeRequest(AbstractEntry entry) {
								imageEditorPanel.hide();
								if (entry != null) {
									getResultView().addResult(new ImageView((ImageEntry)entry));
								} else {
									// we should at least save the title of the image!
									dbService.updateImageEntry(imgEntry, new AsyncCallback<Boolean>() {
										
										@Override
										public void onSuccess(Boolean result) { 
											getResultView().addResult(new ImageView(imgEntry));
										}
										
										@Override
										public void onFailure(Throwable caught) {
											Info.display("ERROR", "Image information has NOT been updated!");
										}
									});
								}
							}

						});
						imageEditorPanel.add(singleIE);
						imageEditorPanel.setGlassEnabled(true);
						imageEditorPanel.center();
						imageEditorPanel.show();
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
