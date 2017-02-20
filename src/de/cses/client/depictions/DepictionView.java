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
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.shared.DepictionEntry;

/**
 * @author alingnau
 *
 */
public class DepictionView extends TextButton {
	
	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();
	}

	private DepictionEntry entry;

	/**
	 * 
	 */
	public DepictionView() {
		super("Add New Depiction");
		entry = null;
		init();
	}

	/**
	 * @param text
	 */
	public DepictionView(DepictionEntry entry) {
		super("DepictionID: " + entry.getDepictionID());
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

			private PopupPanel depictionEditorPanel;

			@Override
			public void onSelect(SelectEvent event) {
				depictionEditorPanel = new PopupPanel(false);
				DepictionEditor de = new DepictionEditor(entry, new DepictionEditorListener() {

					@Override
					public void depictionSaved(DepictionEntry depictionEntry) {
						depictionEditorPanel.hide();
					}
				});
				depictionEditorPanel.add(de);
				new Draggable(depictionEditorPanel);
				depictionEditorPanel.setGlassEnabled(true);
				depictionEditorPanel.center();
				depictionEditorPanel.show();
			}
		});
	}

}
