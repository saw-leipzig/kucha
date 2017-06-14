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
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public class CaveView extends AbstractView {

	interface Resources extends ClientBundle {
		@Source("cave.png")
		ImageResource logo();
		
		@Source("plus.png")
		ImageResource plus();
	}
	
	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div><h3>Cave number {officialNumber}</h3><br>Name: {officialName}<br>Historic name: {historicName}<br>"
				+ "Cavetype: {caveType}</div>")
		SafeHtml tooltip(String officialNumber, String officialName, String historicName, String caveType);
		
		@XTemplate("<div><center><img align=\"center\" width=\"80\" height=\"80\" margin=\"20\" src=\"{imageUri}\"></center><label>{title}</label></div>")
		SafeHtml image(SafeUri imageUri, String title);
	}
	
//	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private CaveEntry cEntry;
	private Resources resources;
	protected String caveType;
//	private CaveViewTemplates cvTemplate;
	
	
	public CaveView() {
		resources = GWT.create(Resources.class);
		Image img = new Image(resources.logo());
		String html = "<div><center><img src='" + img.getUrl() + "' height = '80px' width = '80px'></img></center><label>Add New</label></br></div>";
		setHTML(html);
		cEntry = null;
		setPixelSize(110, 110);
//		init();
	}

	/**
	 * @param text
	 * @param icon
	 */
	public CaveView(CaveEntry entry) {
		resources = GWT.create(Resources.class);
		Image img = new Image(resources.logo());
		String html = "<div><center><img src='" + img.getUrl() + "' height = '80px' width = '80px'></img></center><label>Cave no. " + entry.getOfficialNumber() + "</label></br></div>";
		setHTML(html);
		cEntry = entry;
		setPixelSize(110, 110);
//		init();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	@Override
	protected AbstractEditor getEditor() {
		return new CaveEditor(cEntry);
	}

//	/**
//	 * 
//	 */
//	private void init() {
//		addClickHandler(new ClickHandler() {
//			private PopupPanel caveEditorPanel;
//			
//			@Override
//			public void onClick(ClickEvent event) {
//				caveEditorPanel = new PopupPanel(false);
//				CaveEditor ced = new CaveEditor(cEntry, new CaveEditorListener() {
//
//					@Override
//					public void closeRequest() {
//						caveEditorPanel.hide();
//					}
//				});
//				caveEditorPanel.add(ced);
//				caveEditorPanel.setGlassEnabled(true);
//				caveEditorPanel.center();
//				caveEditorPanel.show();
//			}
//		});
//	}
	
	

}
