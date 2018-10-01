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
package de.cses.client.caves;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class CaveView extends AbstractView {

	interface Resources extends ClientBundle {
		@Source("cave.png")
		ImageResource logo();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div><img src='{imgUri}' height='16px' width='16px'> <b style='font-size: 20px'> {sitename} {officialNumber} </b><p style='font-size:9px'> {officialName} <br> {historicName} </p></div>")
		SafeHtml view(SafeUri imgUri, String officialNumber, String officialName, String historicName, String sitename);

		@XTemplate("<div><img src='{imgUri}' height='16px' width='16px'> <b style='font-size: 20px'> {officialNumber} </b><p style='font-size:9px'> {officialName} <br> {historicName} </p></div>")
		SafeHtml view(SafeUri imgUri, String officialNumber, String officialName, String historicName);

		@XTemplate("<div><img src='{imgUri}' height='16px' width='16px' > <b style='font-size: 20px'> {officialNumber} </b></div>")
		SafeHtml view(SafeUri imgUri, String officialNumber);
	}

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();

		LabelProvider<CaveTypeEntry> nameEN();
	}

	private CaveEntry cEntry;
	private Resources resources;
	protected String caveType;
	private CaveViewTemplates cvTemplate;
	private StaticTables stab = StaticTables.getInstance();

	/**
	 * @param text
	 * @param icon
	 */
	public CaveView(CaveEntry entry) {
		super();
		cEntry = entry;
		resources = GWT.create(Resources.class);
		cvTemplate = GWT.create(CaveViewTemplates.class);
		SiteEntry se = stab.getSiteEntries().get(entry.getSiteID());
		if ((se != null) && (se.getSiteID() > 0)) {
			setHTML(cvTemplate.view(resources.logo().getSafeUri(), entry.getOfficialNumber(),
					entry.getHistoricName() != null ? entry.getHistoricName() : "",
					entry.getOptionalHistoricName() != null ? entry.getOptionalHistoricName() : "", se.getShortName()));
		} else {
			setHTML(cvTemplate.view(resources.logo().getSafeUri(), entry.getOfficialNumber(),
					entry.getHistoricName() != null ? entry.getHistoricName() : "",
					entry.getOptionalHistoricName() != null ? entry.getOptionalHistoricName() : ""));
		}
		setSize("150px", "110px");

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(cEntry);
				event.getStatusProxy().update(cvTemplate.view(resources.logo().getSafeUri(), entry.getOfficialNumber()));
			}

		};

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor() {
		return new CaveEditor(cEntry.clone()); // we are cloning the entry and only update it if changes are saved
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return cEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
	 */
//	@Override
//	public void updateEntryRequest(AbstractEntry updatedEntry) {
//	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof CaveEntry) {
			cEntry = (CaveEntry) entry;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractView#getPermalink()
	 */
	@Override
	protected String getPermalink() {
		return "/json?caveID=" + cEntry.getCaveID();
	}

}
