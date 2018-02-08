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
import de.cses.shared.AbstractEntry;
import de.cses.shared.OrnamentEntry;

/**
 * @author nina
 *
 */
public class OrnamenticView extends AbstractView {
	
	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();
	}

	interface OrnamentationViewTemplates extends XTemplates {
//		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px'> <b style='font-size: 20px'> {officialNumber} </b></center><label style='font-size:9px'> {officialName} <br> {historicName} </label></div>")
//		SafeHtml view(SafeUri imgUri, String officialNumber, String officialName, String historicName);

		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px' > <b style='font-size: 20px'> {name} </b></center></div>")
		SafeHtml view(SafeUri imgUri, String name);
	}

	private OrnamentEntry entry;
	private OrnamentationViewTemplates ovTemplate;
	private Resources resources;

	/**
	 * @param text
	 */
	public OrnamenticView(OrnamentEntry entry) {
		super();
		this.entry = entry;
		resources = GWT.create(Resources.class);
		ovTemplate = GWT.create(OrnamentationViewTemplates.class);
		setHTML(ovTemplate.view(resources.logo().getSafeUri(), "ID = " + entry.getOrnamentID()));
		setPixelSize(110, 110);

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(entry);
				event.getStatusProxy().update(ovTemplate.view(resources.logo().getSafeUri(), "ID = " + entry.getOrnamentID()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor() {
		return new OrnamenticEditor(entry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return entry;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
	 */
	@Override
	public void updateEntryRequest(AbstractEntry updatedEntry) {
		if (updatedEntry instanceof OrnamentEntry) {
			entry = (OrnamentEntry) updatedEntry;
		}
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
