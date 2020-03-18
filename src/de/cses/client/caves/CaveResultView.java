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

import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveSearchEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.SiteEntry;

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
				super.onDragDrop(event);
				CaveSearchEntry searchEntry;
				if (UserLogin.isLoggedIn()) {
					searchEntry = new CaveSearchEntry(UserLogin.getInstance().getSessionID());
				} else {
					searchEntry = new CaveSearchEntry();
				}
				if (event.getData() instanceof DepictionEntry) {
					searchEntry.getCaveIdList().add(((DepictionEntry) event.getData()).getCave().getCaveID());
				}
				else if (event.getData() instanceof OrnamentEntry) {
					searchEntry.geticonographyIDList().add(((OrnamentEntry) event.getData()).getIconographyID());
				}
				else if (event.getData() instanceof ImageEntry) {
					String[] splitted = ((ImageEntry) event.getData()).getShortName().split(" ");
					Util.doLogging(splitted[0]+" - "+splitted[1]);
					searchEntry.getOfficialNumberList().add(splitted[1]);
					for (SiteEntry se : StaticTables.getInstance().getSiteEntries().values()) {
						if (se.getShortName()==splitted[0]) {
							searchEntry.getSiteIdList().add(se.getSiteID());
							break;
						}
					}
				}
				else if (event.getData() instanceof AnnotatedBibliographyEntry) {
					int bibID = ((AnnotatedBibliographyEntry) event.getData()).getAnnotatedBibliographyID();
					searchEntry.getBibIdList().add(bibID);
				}
				else {
					return;
				}
				boolean startsearch=(searchEntry.getCaveIdList().size()>0)||(searchEntry.geticonographyIDList().size()>0)||(searchEntry.getBibIdList().size()>0)||(searchEntry.getOfficialNumberList().size()>0)||(searchEntry.getSiteIdList().size()>0);
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
