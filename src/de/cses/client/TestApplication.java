/*
 * Copyright 2016 
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
package de.cses.client;

import javax.swing.RootPaneContainer;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;

import de.cses.client.images.ImageEditor;
import de.cses.client.images.ImageUploader;
import de.cses.client.images.PhotographerEditor;
import de.cses.client.ornamentic.CreateOrnamentic;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint {

	static CreateOrnamentic CreateOrnamentic = new CreateOrnamentic();

	private TabLayoutPanel main;

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		/* apparently, the viewport is important since it guarantees that the content of all 
		 * tabs will be updated in the background and look nice and clean all the time
		 */
		main = new TabLayoutPanel(3.0, Unit.EM);
    Viewport v = new Viewport();
    v.add(main);
		RootPanel.get().add(v); // use RootPanel, not RootLayoutPanel here!

		CreateOrnamentic co = new CreateOrnamentic();
		ImageEditor imgEditor = new ImageEditor();
		ImageUploader imageUploader = new ImageUploader(imgEditor);
		PhotographerEditor pEditor = new PhotographerEditor();

		main.add(co.asWidget(), "Ornamentic Editor");

		// we are using FlowLayoutContainer 
		FlowLayoutContainer flowLC = new FlowLayoutContainer();
		flowLC.setScrollMode(ScrollMode.ALWAYS);
    MarginData layoutData = new MarginData(new Margins(0, 5, 0, 0));
    flowLC.add(imgEditor, layoutData);
    flowLC.add(imageUploader, layoutData);

		main.add(flowLC, "Image Manager");
		main.add(pEditor, "Photographer Editor");
	}

	public static CreateOrnamentic getCreateOrnamentic() {
		return CreateOrnamentic;
	}

	public static void setCreateOrnamentic(CreateOrnamentic createOrnamentic) {
		CreateOrnamentic = createOrnamentic;
	}
}
