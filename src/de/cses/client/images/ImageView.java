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

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.ImageEntry;

/**
 * @author alingnau
 *
 */
public class ImageView extends AbstractView {

	interface ImageViewResources extends ClientBundle {
		@Source("lock-protection.png")
		ImageResource locked();

		@Source("lock-semiprotection.png")
		ImageResource semi();

		@Source("photo.png")
		ImageResource open();
}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<div><center><img src='{imgUri}'></img></center></div>")
		SafeHtml view(SafeUri imgUri);
		
		@XTemplate(source = "ImageViewTemplate.html")
		SafeHtml view(SafeUri imgUri, String title, String shortName, String author, String imgType, String date, boolean isPublic, int imageID, boolean hasLicense, SafeHtml license);
	}
	
	private ImageEntry imgEntry;
	private ImageViewTemplates ivTemplates;
	private ImageViewResources res;
	SafeUri imgUri;
	ImageResultView irv;
	/**
	 * @param text
	 */
	public ImageView(ImageEntry imgEntry, SafeUri uri, ImageResultView irv) {
		super();
		ivTemplates = GWT.create(ImageViewTemplates.class);
		res = GWT.create(ImageViewResources.class);
		this.imgEntry = imgEntry;
		this.imgUri = uri;
		this.irv=irv;

		refreshHTML();
		getStyleElement().getStyle().setProperty("minHeight", "130px");
		setWidth("350px");
		//setSize("350px", "130px");

		new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(imgEntry);
				event.getStatusProxy().update(ivTemplates.view(imgUri));
			}
			
		};
	}

	private void refreshHTML() {
		long now = new Date().getTime();
		setHTML(ivTemplates.view(
				imgUri, 
				imgEntry.getTitle() != null ? imgEntry.getTitle() : "n/a", 
				imgEntry.getShortName() != null ? imgEntry.getShortName() : "n/a", 
				imgEntry.getImageAuthor() != null ? imgEntry.getImageAuthor().getLabel() : "n/a",
				imgEntry.getImageTypeID() > 0 ? StaticTables.getInstance().getImageTypeEntries().get(imgEntry.getImageTypeID()).getName() : "n/a", 
				imgEntry.getDate(),
				(((imgEntry.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_PUBLIC) && (!imgEntry.getIsExpiring()))|| ((imgEntry.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_PUBLIC) && (imgEntry.getIsExpiring())&&(now < imgEntry.getExpiriesAt()))),
				imgEntry.getImageID(), imgEntry.getCC() == null? false: true, imgEntry.getCC() == null ? null : SafeHtmlUtils.fromTrustedString(imgEntry.getCC().getHtml())));
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor(AbstractEntry entry, AbstractView av) {
		return new SingleImageEditor(imgEntry.clone(), av);
	}
	public void setEditor(ImageEntry entry) {
		this.imgEntry=entry;
	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof ImageEntry) {
			imgEntry = (ImageEntry) entry;
		}
		refreshHTML();
	}
	public ImageResultView getImageResultView() {
		return irv;
	}
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return imgEntry;
	}
	public void refreshpic( SafeUri uri ) {
		imgUri= uri;

		refreshHTML();
	}
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getPermalink()
	 */
	@Override
	protected String getPermalink() {
		// TODO Auto-generated method stub
		return null;
	}

}
