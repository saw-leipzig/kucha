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
package de.cses.client.depictions;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.WallLocationEntry;

/**
 * @author alingnau
 *
 */
public class DepictionView extends AbstractView {

	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();

		@Source("lock-protection.png")
		ImageResource locked();

		@Source("photo.png")
		ImageResource open();
}

	interface DepictionViewTemplates extends XTemplates {
		@XTemplate("<div><center><img src='{imgUri}'></img></center></div>")
		SafeHtml view(SafeUri imgUri);
		
		@XTemplate(source = "DepictionViewTemplate.html")
		SafeHtml view(SafeUri imgUri, String officialCaveNumber, String historicCaveName, String shortName, String siteDistrictInformation, String wallLocation, SafeUri lockUri);
	}

	private DepictionEntry depictionEntry;
	private DepictionViewTemplates dvTemplates;
	private Resources resources;

	/**
	 * @param text
	 */
	public DepictionView(DepictionEntry entry) {
		depictionEntry = entry;
		resources = GWT.create(Resources.class);
		dvTemplates = GWT.create(DepictionViewTemplates.class);

		refreshHTML();
		setSize("350px", "130px");

		new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(depictionEntry);
				event.getStatusProxy().update(dvTemplates.view(resources.logo().getSafeUri()));
			}
			
		};
	}
	
	private void refreshHTML() {
		StaticTables st = StaticTables.getInstance();
		CaveEntry ce = depictionEntry.getCave();
		DistrictEntry de = (ce != null && ce.getDistrictID() > 0) ? st.getDistrictEntries().get(ce.getDistrictID()) : null;
		SiteEntry se = (ce != null && ce.getSiteID() > 0) ? st.getSiteEntries().get(ce.getSiteID()) : null;
		String siteDistrictInformation = (se != null ? se.getName() : "") + (de != null ? (se != null ? " / " : "") + de.getName() : "");
		String wallLocation = "";
		String officialCaveNumberStr = (se != null && ce != null) ? se.getShortName() + " " + ce.getOfficialNumber() : "";
		if (depictionEntry.getWallID() > 0) {
			WallLocationEntry wle = st.getWallLocationEntries().get(depictionEntry.getWallID());
			wallLocation = wle.getLabel();
		}
		setHTML(dvTemplates.view(
				UriUtils.fromString("resource?imageID=" + depictionEntry.getMasterImageID() + "&thumb=120" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()), 
				officialCaveNumberStr, 
				ce != null && ce.getHistoricName() != null ? ce.getHistoricName() : "",
				depictionEntry.getShortName() != null ? depictionEntry.getShortName() : "",
				siteDistrictInformation,
				wallLocation,
				depictionEntry.getAccessLevel() <= UserLogin.getInstance().getAccessRights() ? resources.open().getSafeUri() : resources.locked().getSafeUri()
		));
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor() {
		return new DepictionEditor(depictionEntry.clone());
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	@Override
	protected AbstractEntry getEntry() {
		return depictionEntry;
	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof DepictionEntry) {
			depictionEntry = (DepictionEntry) entry;
		}
		refreshHTML();
//		CaveEntry ce = depictionEntry.getCave();
//		setHTML(dvTemplates.view(
//				UriUtils.fromString("resource?imageID=" + depictionEntry.getMasterImageID() + "&thumb=120" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()), 
//				StaticTables.getInstance().getSiteEntries().get(ce.getSiteID()).getShortName() + " " + ce.getOfficialNumber(), 
//				ce != null && ce.getHistoricName() != null ? ce.getHistoricName() : "",
//				depictionEntry.getShortName() != null ? depictionEntry.getShortName() : "",
//				depictionEntry.isOpenAccess() ? resources.open().getSafeUri() : resources.locked().getSafeUri()
//		));
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getPermalink()
	 */
	@Override
	protected String getPermalink() {
		return "/json?paintedRepID=" + depictionEntry.getDepictionID();
	}

}
