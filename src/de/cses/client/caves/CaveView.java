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

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;

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
		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px'> <b style='font-size: 20px'> {officialNumber} </b><p style='font-size:9px; word-wrap:break-word'> {officialName} <br> {historicName} </p></center></div>")
		SafeHtml view(SafeUri imgUri, String officialNumber, String officialName, String historicName);

		@XTemplate("<div><center><img src='{imgUri}' height='16px' width='16px' > <b style='font-size: 20px'> {officialNumber} </b></center></div>")
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

	/**
	 * @param text
	 * @param icon
	 */
	public CaveView(CaveEntry entry) {
		super();
		cEntry = entry;
		resources = GWT.create(Resources.class);
		cvTemplate = GWT.create(CaveViewTemplates.class);
		setHTML(cvTemplate.view(
				resources.logo().getSafeUri(), entry.getOfficialNumber().substring(0, 11), entry.getOfficialName().substring(0, 11), entry.getHistoricName().substring(0, 11)
			));
		setPixelSize(110, 110);
		
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
		return new CaveEditor(cEntry);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return cEntry;
	}

}
