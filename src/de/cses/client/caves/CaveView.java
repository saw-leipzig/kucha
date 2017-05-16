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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;

/**
 * @author alingnau
 *
 */
public class CaveView extends TextButton {

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
		
		@XTemplate("<img align=\"center\" width=\"80\" height=\"80\" margin=\"20\" src=\"{imageUri}\"><br><h3>{title}</h3>")
		SafeHtml image(SafeUri imageUri, String title);
	}
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private CaveEntry cEntry;
	private Resources resources;
	protected String caveType;
	private CaveViewTemplates cvTemplate;
	
	
	public CaveView() {
		super("Add New");
		resources = GWT.create(Resources.class);
		this.setIcon(resources.plus());
		cEntry = null;
		init();
	}

	/**
	 * @param text
	 * @param icon
	 */
	public CaveView(CaveEntry entry) {
		super("Cave no. " + entry.getOfficialNumber());
		cEntry = entry;
		resources = GWT.create(Resources.class);
		this.setIcon(resources.logo());
		cvTemplate = GWT.create(CaveViewTemplates.class);
		dbService.getCaveTypebyID(cEntry.getCaveID(), new AsyncCallback<CaveTypeEntry>() {
			
			@Override
			public void onSuccess(CaveTypeEntry result) {
				setToolTip(cvTemplate.tooltip(cEntry.getOfficialNumber(), cEntry.getOfficialName(), cEntry.getHistoricName(), result.getNameEN()));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.setIconAlign(IconAlign.TOP);

		setScale(ButtonScale.LARGE);

		addSelectHandler(new SelectHandler() {

			private PopupPanel caveEditorPanel;

			@Override
			public void onSelect(SelectEvent event) {
				caveEditorPanel = new PopupPanel(false);
				CaveEditor ced = new CaveEditor(cEntry, new CaveEditorListener() {

					@Override
					public void closeRequest() {
						caveEditorPanel.hide();
					}
				});
				caveEditorPanel.add(ced);
//				new Draggable(caveEditorPanel);
				caveEditorPanel.setGlassEnabled(true);
				caveEditorPanel.center();
				caveEditorPanel.show();
			}
		});
	}
	
	

}
