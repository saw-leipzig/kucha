/*
 * Copyright 2017 - 2018
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

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.ui.AbstractResultView;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.DepictionEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyResultView extends AbstractResultView {

	/**
	 * @param title
	 */
	public AnnotatedBiblographyResultView(String title) {
		super(title);
		
		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof DepictionEntry) {
					for (AnnotatedBiblographyEntry bibEntry : ((DepictionEntry) event.getData()).getRelatedBibliographyList()) {
						addResult(new AnnotatedBiblographyView(bibEntry));
					}
				}
			}
		};
	
	}

}
