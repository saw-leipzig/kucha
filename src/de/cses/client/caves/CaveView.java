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
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public class CaveView extends TextButton {

	interface Resources extends ClientBundle {
		@Source("cave.png")
		ImageResource logo();
	}

	private CaveEntry entry;
	
	
	public CaveView() {
		super("Add New Cave");
		entry = null;
		init();
	}

	/**
	 * @param text
	 * @param icon
	 */
	public CaveView(CaveEntry entry) {
		super(entry.getOfficialNumber() + " - " + entry.getOfficialName());
		this.entry = entry;
		init();
	}

	/**
	 * 
	 */
	private void init() {
		this.setIconAlign(IconAlign.TOP);

		Resources resources = GWT.create(Resources.class);
		this.setIcon(resources.logo());
		setScale(ButtonScale.LARGE);

		addSelectHandler(new SelectHandler() {

			private PopupPanel caveEditorPanel;

			@Override
			public void onSelect(SelectEvent event) {
				caveEditorPanel = new PopupPanel(false);
				CaveEditor ced = new CaveEditor(entry, new CaveEditorListener() {

					@Override
					public void closeRequest() {
						caveEditorPanel.hide();
					}
				});
				caveEditorPanel.add(ced);
				new Draggable(caveEditorPanel);
				caveEditorPanel.setGlassEnabled(true);
				caveEditorPanel.center();
				caveEditorPanel.show();
			}
		});
	}
	
	

}
