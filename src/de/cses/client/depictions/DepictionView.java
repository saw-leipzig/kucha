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
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonScale;
import com.sencha.gxt.cell.core.client.ButtonCell.IconAlign;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
//import de.cses.client.images.ImageView.Resources;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;

/**
 * @author alingnau
 *
 */
public class DepictionView extends Button {

	interface Resources extends ClientBundle {
		@Source("buddha.png")
		ImageResource logo();
	}

	private DepictionEntry entry;
	private static final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * 
	 */
	public DepictionView() {
		// super("Add New Depiction");
		Resources resources = GWT.create(Resources.class);
		Image img = new Image(resources.logo());
		String html = "<div><center><img src='" + img.getUrl()
				+ "' height = '128px' width = '128px'></img></center><label> New Depiction </label></br></div>";
		setHTML(html);
		setPixelSize(150, 160);
		entry = null;
		init();
	}

	/**
	 * @param text
	 */
	public DepictionView(DepictionEntry entry) {
		// super("DepictionID: " + entry.getDepictionID());
		this.entry = entry;
		dbService.getMasterImageEntryForDepiction(entry.getDepictionID(), new AsyncCallback<ImageEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				Resources resources = GWT.create(Resources.class);
				Image img = new Image(resources.logo());
				String html = "<div><center><img src='" + img.getUrl()
						+ "' height = '128px' width = '128px'></img></center><label> DepictionID " + entry.getDepictionID()
						+ "</label></br></div>";
				setHTML(html);
			}

			@Override
			public void onSuccess(ImageEntry result) {
				String html = "<div><center><img src='resource?imageID=" + result.getImageID() + "&thumb=true'"
						+ "' height = '128px' width = '128px'></img></center><label> DepictionID " + entry.getDepictionID()
						+ "</label></br></div>";
				setHTML(html);
			}
		});
		setPixelSize(150, 160);
		init();
	}

	/**
	 * 
	 */
	private void init() {
		// this.setIconAlign(IconAlign.TOP);
		//
		// Resources resources = GWT.create(Resources.class);
		// this.setIcon(resources.logo());
		// setScale(ButtonScale.LARGE);

		addClickHandler(new ClickHandler() {

			private PopupPanel depictionEditorPanel;

			@Override
			public void onClick(ClickEvent event) {
				depictionEditorPanel = new PopupPanel(false);
				DepictionEditor de = new DepictionEditor(entry, new DepictionEditorListener() {

					@Override
					public void depictionSaved(DepictionEntry depictionEntry) {
						depictionEditorPanel.hide();
					}
				});
				depictionEditorPanel.add(de);
				// new Draggable(depictionEditorPanel);
				depictionEditorPanel.setGlassEnabled(true);
				depictionEditorPanel.center();
				depictionEditorPanel.show();
			}
		});

		// addSelectHandler(new SelectHandler() {
		//
		// private PopupPanel depictionEditorPanel;
		//
		// @Override
		// public void onSelect(SelectEvent event) {
		// depictionEditorPanel = new PopupPanel(false);
		// DepictionEditor de = new DepictionEditor(entry, new
		// DepictionEditorListener() {
		//
		// @Override
		// public void depictionSaved(DepictionEntry depictionEntry) {
		// depictionEditorPanel.hide();
		// }
		// });
		// depictionEditorPanel.add(de);
		// new Draggable(depictionEditorPanel);
		// depictionEditorPanel.setGlassEnabled(true);
		// depictionEditorPanel.center();
		// depictionEditorPanel.show();
		// }
		// });
	}

}
