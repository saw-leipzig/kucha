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
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
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

	/*
	 * (non-Javadoc)
	 * 
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
		for (String sql :  sqlWhereClauses) {
			if (sqlWhere == null) {
				sqlWhere = sql;
			} else {
				sqlWhere = sqlWhere.concat(" AND " + sql);
			}
		}
		com.google.gwt.user.client.Window.alert("search for images WHERE " + sqlWhere);
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
							public void closeRequest() {
								imageEditorPanel.hide();
								getResultView().addResult(new ImageView(imgEntry));
							}

							@Override
							public void updateEntryRequest(AbstractEntry updatedEntry) {
								// nothing needs to be done here
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
