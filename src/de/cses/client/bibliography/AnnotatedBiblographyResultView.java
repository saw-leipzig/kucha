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

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;

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
					if (e instanceof AnnotatedBibliographyEntry) {
						idStr = "all".equals(idStr) ? Integer.toString(((AnnotatedBibliographyEntry)e).getAnnotatedBibliographyID()) : idStr + "," + ((AnnotatedBibliographyEntry)e).getAnnotatedBibliographyID();
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
				AnnotatedBibliographySearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new AnnotatedBibliographySearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new AnnotatedBibliographySearchEntry();
				}
				if (event.getData() instanceof CaveEntry) {
					for (AnnotatedBibliographyEntry abe : ((CaveEntry) event.getData()).getRelatedBibliographyList()) {
						searchEntry.getBibIdList().add(abe.getAnnotatedBibliographyID());
					}
				}
				else if (event.getData() instanceof DepictionEntry) {
					for (AnnotatedBibliographyEntry abe : ((DepictionEntry) event.getData()).getRelatedBibliographyList()) {
						searchEntry.getBibIdList().add(abe.getAnnotatedBibliographyID());
					}
				}
				else if (event.getData() instanceof OrnamentEntry) {
					for (AnnotatedBibliographyEntry abe : ((OrnamentEntry) event.getData()).getRelatedBibliographyList()) {
						searchEntry.getBibIdList().add(abe.getAnnotatedBibliographyID());
					}
				}
				else if (event.getData() instanceof ImageEntry) {
					return;
				}
				else {
					return;
				}
				boolean startsearch=(searchEntry.getBibIdList().size()>0);
				Util.showYesNo("Delete old filters?", "Do you whisch to delete old filters?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						initiateSearch(searchEntry,startsearch,true);
					}
				}, new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						initiateSearch(searchEntry,startsearch,false);
					}},
					new KeyDownHandler() {
						public void onKeyDown(KeyDownEvent e) {
							initiateSearch(searchEntry,startsearch,true);
					}
				});
			}
		};
	
	}
	
}
