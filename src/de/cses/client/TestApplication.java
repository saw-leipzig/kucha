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
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import de.cses.client.caves.Antechamber;
import de.cses.client.caves.CaveType;
import de.cses.client.caves.Caves;
import de.cses.client.caves.Cella;
import de.cses.client.caves.Niches;
import de.cses.client.images.ImageEditor;
import de.cses.client.images.ImageUploader;
import de.cses.client.ornamentic.Ornamentic;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint {

	static Ornamentic Ornamentic = new Ornamentic();

	private TabLayoutPanel main;

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		main = new TabLayoutPanel(3.0, Unit.EM);
		RootLayoutPanel.get().add(main);

		Ornamentic co = new Ornamentic();
		Caves caves = new Caves();
		Cella cella = new Cella();
		CaveType caveType = new CaveType();
		Niches niches = new Niches();
		Antechamber antechamber = new Antechamber();
		ImageEditor imgEditor = new ImageEditor();
		ImageUploader imageUploader = new ImageUploader(imgEditor);

		// main.add(new TestPanel("Maja"), "Test Panel");
		main.add(co.asWidget(), "Ornamentic Editor");
		main.add(caves.asWidget(), "Cave Editor");
		main.add(cella.asWidget(), "Cella Editor");
		main.add(caveType.asWidget(),"Cave Type Editor");
		main.add(niches.asWidget(), "Niches Editor");
		main.add(antechamber.asWidget(), "Antechamber Editor");

		VerticalLayoutContainer c = new VerticalLayoutContainer();
		c.add(imgEditor, new VerticalLayoutData(1, .5));
		c.add(imageUploader, new VerticalLayoutData(1, .5));
		// Panel auxPanel = new FlowPanel();
		// auxPanel.add(imgEditor.asWidget());
		// auxPanel.add(imageUploader.asWidget());
		main.add(c, "Image Manager");

	}

	public static Ornamentic getCreateOrnamentic() {
		return Ornamentic;
	}

	public static void setCreateOrnamentic(Ornamentic ornamentic) {
		Ornamentic = ornamentic;
	}
}
