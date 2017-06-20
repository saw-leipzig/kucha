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

import java.io.File;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;

/**
 * @author alingnau
 *
 */
public class ImageView extends AbstractView {

	interface Resources extends ClientBundle {
		@Source("addimage.png")
		ImageResource plus();
	}

	private ImageEntry imgEntry;
//	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * 
	 */
//	public ImageView() {
//		Resources resources = GWT.create(Resources.class);
//		Image img = new Image(resources.plus());
//		String html = "<div><center><img src='" + img.getUrl()
//				+ "' height = '80px' width = '80px'></img></center><label> New Image </label></br></div>";
//
//		setHTML(html);
//		setPixelSize(110, 110);
//		imgEntry = null;
//		initAddImage();
//	}

	/**
	 * @param text
	 */
	public ImageView(ImageEntry imgEntry) {
		super();
		this.imgEntry = imgEntry;
		 String html = "<div><center><img src='resource?imageID=" + imgEntry.getImageID() + "&thumb=80'"
		 + "' ></img></center><label>" + imgEntry.getTitle() + "</label></br></div>";
		setHTML(html);
		setPixelSize(110, 110);
//		initEditImage();
	}

	/**
	 * 
	 */
//	private void initEditImage() {
//		addClickHandler(new ClickHandler() {
//			PopupPanel imageEditorPanel;
//
//			@Override
//			public void onClick(ClickEvent event) {
//				imageEditorPanel = new PopupPanel(false);
//				SingleImageEditor singleIE = new SingleImageEditor(imgEntry);
//				singleIE.addEditorListener(new EditorListener() {
//
//					@Override
//					public void closeRequest() {
//						imageEditorPanel.hide();
//						String html = "<div><center><img src='resource?imageID=" + imgEntry.getImageID() + "&thumb=80'"
//								+ "' ></img></center><label>" + imgEntry.getTitle() + "</label></br></div>";
//						setHTML(html);
//					}
//				});
//				imageEditorPanel.add(singleIE);
//				imageEditorPanel.setGlassEnabled(true);
//				imageEditorPanel.center();
//				imageEditorPanel.show();
//
//			}
//		});
//	}

	/**
	 * 
	 */
//	private void initAddImage() {
//		addClickHandler(new ClickHandler() {
//			PopupPanel imageUploadPanel;
//			PopupPanel imageEditorPanel;
//
//			@Override
//			public void onClick(ClickEvent event) {
//				imageUploadPanel = new PopupPanel(false);
//				imageEditorPanel = new PopupPanel(false);
//
//				ImageUploader iu = new ImageUploader(new ImageUploadListener() {
//
//					@Override
//					public void uploadCompleted(int newImageID, final String filename) {
//						imageUploadPanel.hide();
//						dbService.getImage(newImageID, new AsyncCallback<ImageEntry>() {
//
//							@Override
//							public void onSuccess(ImageEntry result) {
//								imgEntry = result;
//								imgEntry.setTitle(filename);
//								SingleImageEditor singleIE = new SingleImageEditor(imgEntry);
//								singleIE.addEditorListener(this);
//								imageEditorPanel.add(singleIE);
//								imageEditorPanel.setGlassEnabled(true);
//								imageEditorPanel.center();
//								imageEditorPanel.show();
//							}
//
//							@Override
//							public void onFailure(Throwable caught) {
//								imgEntry = null;
//							}
//						});
//					}
//
//					@Override
//					public void uploadCanceled() {
//						imageUploadPanel.hide();
//					}
//				});
//				imageUploadPanel.add(iu);
//				imageUploadPanel.setGlassEnabled(true);
//				imageUploadPanel.center();
//				imageUploadPanel.show();
//			}
//		});
//
//	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor() {
		return new SingleImageEditor(imgEntry);
	}

	@Override
	public void closeRequest() {
		super.closeRequest();
		String html = "<div><center><img src='resource?imageID=" + imgEntry.getImageID() + "&thumb=80'"
				+ "' ></img></center><label>" + imgEntry.getTitle() + "</label></br></div>";
		setHTML(html);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return imgEntry;
	}

}
