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
package de.cses.client.bibliography;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBiblographyEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyView  extends AbstractView {
	
	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();
	}

	interface AnnotatedBiblographyViewTemplates extends XTemplates {
		@XTemplate("<div><center><img src='{imgUri}'></img></center></div>")
		SafeHtml view(SafeUri imgUri);
		
		@XTemplate("<div><center><img src='{imgUri}'></img></center><label style='font-size:9px' > AnnotatedBiblographyID {id} </label></br></div>")
		SafeHtml view(SafeUri imgUri, int id);
	}

	private AnnotatedBiblographyEntry annotatedBiblographyEntry;
	private AnnotatedBiblographyViewTemplates dvTemplates;
	private Resources resources;

	/**
	 * @param text
	 */
	public AnnotatedBiblographyView(AnnotatedBiblographyEntry entry) {
		// super("DepictionID: " + depictionEntry.getDepictionID());
		annotatedBiblographyEntry = entry;
		resources = GWT.create(Resources.class);
		dvTemplates = GWT.create(AnnotatedBiblographyViewTemplates.class);

		setHTML(dvTemplates.view(resources.logo().getSafeUri()));

		setPixelSize(110, 110);

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(annotatedBiblographyEntry);
				event.getStatusProxy().update(dvTemplates.view(resources.logo().getSafeUri()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	protected AbstractEditor getEditor() {
		return new AnnotatedBiblographyEditor(annotatedBiblographyEntry.clone());
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	protected AbstractEntry getEntry() {
		return annotatedBiblographyEntry;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
	 */
	@Override
	public void updateEntryRequest(AbstractEntry updatedEntry) {
		if (updatedEntry instanceof AnnotatedBiblographyEntry) {
			annotatedBiblographyEntry = (AnnotatedBiblographyEntry) updatedEntry;
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
