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
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyView extends AbstractView {
	
	private AnnotatedBibliographyEntry annotatedBibliographyEntry;
	private AnnotatedBibliographyViewTemplates dvTemplates;

	/**
	 * @param text
	 */
	public AnnotatedBiblographyView(AnnotatedBibliographyEntry annotatedBibliographyEntry) {
		this.annotatedBibliographyEntry = annotatedBibliographyEntry;
		dvTemplates = GWT.create(AnnotatedBibliographyViewTemplates.class);
		setHTML(dvTemplates.view(annotatedBibliographyEntry));
//		setHTML(dvTemplates.extendedView(annotatedBibliographyEntry));
		setSize("95%", "80px");

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(annotatedBibliographyEntry);
				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry));
//				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry.getTitleEN()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	protected AbstractEditor getEditor() {
		return new AnnotatedBibliographyEditor(annotatedBibliographyEntry.clone());
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	protected AbstractEntry getEntry() {
		return annotatedBibliographyEntry;
	}

//	/* (non-Javadoc)
//	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
//	 */
//	@Override
//	public void updateEntryRequest(AbstractEntry updatedEntry) {
//	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof AnnotatedBibliographyEntry) { // refresh view
			annotatedBibliographyEntry = (AnnotatedBibliographyEntry) entry;
			setHTML(dvTemplates.view(annotatedBibliographyEntry));
//			setHTML(dvTemplates.extendedView(annotatedBibliographyEntry));
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
