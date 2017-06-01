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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.shared.ImageEntry;

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
	public ImageSearchController(String selectorTitle, AbstractResultView resultView) {
		super(selectorTitle, resultView);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ArrayList<String> sqlWhereClauses = new ArrayList<String>();
		for (AbstractFilter filter : getRelatedFilter()) {
			if (filter != null) {
				sqlWhereClauses.addAll(filter.getSqlWhereClause());
			}
		}
		String sqlWhere = null;
		for (int i=0; i<sqlWhereClauses.size(); ++i) {
			if (i == 0) {
				sqlWhere = sqlWhereClauses.get(i);
			} else {
				sqlWhere = sqlWhere + " AND " + sqlWhereClauses.get(i);
			}
		}
		System.err.println("search for images WHERE " + sqlWhere);
		dbService.getImages(sqlWhere, new AsyncCallback<ArrayList<ImageEntry>>() {
			
			@Override
			public void onSuccess(ArrayList<ImageEntry> result) {
				getResultView().reset();
				for (ImageEntry ie : result) {
					getResultView().addResult(new ImageView(ie));
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		PopupPanel imageUploadPanel = new PopupPanel(false);
		PopupPanel imageEditorPanel = new PopupPanel(false);

		ImageUploader iu = new ImageUploader(new ImageUploadListener() {

			@Override
			public void uploadCompleted(int newImageID) {
				imageUploadPanel.hide();
				dbService.getImage(newImageID, new AsyncCallback<ImageEntry>() {

					@Override
					public void onSuccess(ImageEntry result) {
						SingleImageEditor singleIE = new SingleImageEditor(result, new ImageEditorListener() {

							@Override
							public void closeImageEditor() {
								imageEditorPanel.hide();
							}

							@Override
							public void cancelImageEditor() {
								imageEditorPanel.hide();
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
		imageUploadPanel.show();	}

}