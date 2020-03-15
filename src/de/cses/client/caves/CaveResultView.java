/*
 * Copyright 2017 - 2019
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
package de.cses.client.caves;

import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;

import de.cses.client.ui.AbstractResultView;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentEntry;

/**
 * @author alingnau
 *
 */
public class CaveResultView extends AbstractResultView {
	
	/**
	 * 
	 */
	public CaveResultView(String title) {
		super(title);
//		setHeight(300);

		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
//				super.onDragDrop(event);
//				if (event.getData() instanceof OrnamentEntry) {
//					for (OrnamentCaveRelation ocr : ((OrnamentEntry)event.getData()).getCavesRelations()) {
//						addResult(new CaveView(ocr.getCaveEntry()));
//					}
//				}
				
			}
		};
	}

}
