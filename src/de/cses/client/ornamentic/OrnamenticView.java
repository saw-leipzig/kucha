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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.OrnamentEntry;

/**
 * @author nina
 *
 */
public class OrnamenticView extends AbstractView {
	//Klasse zur Darstellung des Bildes fuer die Ornamentik in der Suche
	


	interface OrnamentationViewTemplates extends XTemplates {
//		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px'> <b style='font-size: 20px'> {officialNumber} </b></center><label style='font-size:9px'> {officialName} <br> {historicName} </label></div>")
//		SafeHtml view(SafeUri imgUri, String officialNumber, String officialName, String historicName);

		@XTemplate("<div><center><img src='{imgUri}' style='max-height: 80px; max-width: 120px; margin: 0 5px 0 0;'></img></center><label style='font-size:9px' >{shortName}</label></div>")
		SafeHtml view(SafeUri imgUri, String shortName);

//		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px' > <b style='font-size: 12px'> {name} </b></center></div>")
//		SafeHtml view(SafeUri imgUri, String name);
	}

	private OrnamentEntry oEntry;
	private OrnamentationViewTemplates ovTemplate;
	private SafeUri imageUri;

	/**
	 * @param text
	 */
	
	public OrnamenticView(OrnamentEntry oe,SafeUri uri) {
		super();
		oEntry = oe;
		imageUri = uri;
		ovTemplate = GWT.create(OrnamentationViewTemplates.class);
		//SafeUri imageUri = null;
		//if ((oEntry.getImages() != null) && (!oEntry.getImages().isEmpty())) {
		//	imageUri = UriUtils.fromString("resource?imageID=" + oEntry.getImages().get(0).getImageID() + "&thumb=80" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		//}
		setHTML(ovTemplate.view(imageUri, "Ornament Code: " + oEntry.getCode()));
		setPixelSize(110, 110);

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(oEntry);
				event.getStatusProxy().update(ovTemplate.view(imageUri, "ID = " + oEntry.getOrnamentID()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor(AbstractEntry entry, AbstractView av) {
		return new OrnamenticEditor((OrnamentEntry)entry, av);
	}
	public void setEditor(OrnamentEntry entry) {
		this.oEntry=entry;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return oEntry;
	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof OrnamentEntry) {
			oEntry = (OrnamentEntry) entry;
		}
	}
	public void refreshpic( SafeUri uri ) {
		imageUri= uri;

		setHTML(ovTemplate.view(imageUri, "Ornament Code: " + oEntry.getCode()));

	}
//	/* (non-Javadoc)
//	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
//	 */
//	@Override
//	public void updateEntryRequest(AbstractEntry updatedEntry) {
//	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getPermalink()
	 */
	@Override
	protected String getPermalink() {
		// TODO Auto-generated method stub
		return null;
	}

}
