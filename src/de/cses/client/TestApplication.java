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

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import de.cses.client.images.ImageEditor;
import de.cses.client.images.ImageUploader;
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

		main = new TabLayoutPanel(3.0, Unit.EM);
		RootPanel.get().add(main);

		CreateOrnamentic co = new CreateOrnamentic();
		ImageUploader imageUploader = new ImageUploader(ImageUploader.DEPICTIONS);
		ImageEditor imgEditor = new ImageEditor();

		main.setHeight(Window.getClientHeight() + "px");
		// main.add(new TestPanel("Maja"), "Test Panel");
		main.add(co.asWidget(), "Ornamentic Editor");

		VerticalLayoutContainer c = new VerticalLayoutContainer();
		c.add(imgEditor, new VerticalLayoutData(1, .5));
		c.add(imageUploader, new VerticalLayoutData(1, .5));
		// Panel auxPanel = new FlowPanel();
		// auxPanel.add(imgEditor.asWidget());
		// auxPanel.add(imageUploader.asWidget());
		main.add(c, "Image Manager");

	}

	public static CreateOrnamentic getCreateOrnamentic() {
		return CreateOrnamentic;
	}

	public static void setCreateOrnamentic(CreateOrnamentic createOrnamentic) {
		CreateOrnamentic = createOrnamentic;
	}
}
