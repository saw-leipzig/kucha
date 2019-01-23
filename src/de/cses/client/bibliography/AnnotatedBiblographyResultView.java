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

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.images.ImageView;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageEntry;

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
		
		ToolButton bibTexExportTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		bibTexExportTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				String idStr = "all";
				for (AbstractEntry e : getEntriesOnDisplay()) {
					if (e instanceof AnnotatedBiblographyEntry) {
						idStr = "all".equals(idStr) ? Integer.toString(((AnnotatedBiblographyEntry)e).getAnnotatedBiblographyID()) : idStr + "," + ((AnnotatedBiblographyEntry)e).getAnnotatedBiblographyID();
					}
				}
				com.google.gwt.user.client.Window.open("/bibtex?bibID=" + idStr + UserLogin.getInstance().getUsernameSessionIDParameterForUri(),"_blank",null);
			}

		});
		bibTexExportTB.setToolTip(Util.createToolTip("Export search result in BibTeX format", "Empty window = export all"));
		getHeader().addTool(bibTexExportTB);

		new DropTarget(this) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof DepictionEntry) {
					for (AnnotatedBiblographyEntry bibEntry : ((DepictionEntry) event.getData()).getRelatedBibliographyList()) {
						addResult(new AnnotatedBiblographyView(bibEntry));
					}
				} else if (event.getData() instanceof CaveEntry) {
					for (AnnotatedBiblographyEntry bibEntry : ((CaveEntry) event.getData()).getRelatedBibliographyList()) {
						addResult(new AnnotatedBiblographyView(bibEntry));
					}
				}
			}
		};
	
	}
	
}
