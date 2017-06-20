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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;

/**
 * @author alingnau
 *
 */
public class ImageView extends AbstractView {

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<div><center><img src='{imgUri}'></img></center></div>")
		SafeHtml view(SafeUri imgUri);
		
		@XTemplate("<div><center><img src='{imgUri}'></img></center><label style='font-size:9px>{title}</label></div>")
		SafeHtml view(SafeUri imgUri, String title);
	}
	
	
	private ImageEntry imgEntry;
//	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ImageViewTemplates ivTemplates;

	/**
	 * @param text
	 */
	public ImageView(ImageEntry imgEntry) {
		super();
		ivTemplates = GWT.create(ImageViewTemplates.class);
		this.imgEntry = imgEntry;
		setHTML(ivTemplates.view(UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=80"), imgEntry.getTitle().substring(0, 11)));
		setPixelSize(110, 110);

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(imgEntry);
				event.getStatusProxy().update(ivTemplates.view(UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=80")));
			}
			
		};
	}

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
		setHTML(ivTemplates.view(UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=80"), imgEntry.getTitle().substring(0, 11)));
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return imgEntry;
	}

}
