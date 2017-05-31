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
package de.cses.client.ornamentic;

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

import de.cses.shared.OrnamentEntry;

/**
 * @author nina
 *
 */
public class OrnamenticView extends TextButton{
	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();
	}

	private OrnamentEntry entry;

	/**
	 * 
	 */
	public OrnamenticView() {
		super("Add New Ornament");
		entry = null;
		init();
	}

	/**
	 * @param text
	 */
	public OrnamenticView(OrnamentEntry entry) {
		super("Ornament: " + entry.getCode());
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

			private PopupPanel ornamentEditorPanel;

			@Override
			public void onSelect(SelectEvent event) {
				ornamentEditorPanel = new PopupPanel(false);
				
				Ornamentic ornamentic = new Ornamentic();
				ornamentic.setPopup(ornamentEditorPanel);
				ornamentEditorPanel.add(ornamentic);
				new Draggable(ornamentEditorPanel);
				ornamentEditorPanel.setGlassEnabled(true);
				ornamentEditorPanel.center();
			}
		});
	}

}
