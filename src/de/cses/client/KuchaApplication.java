/*
 * Copyright 2016-2017 
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
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.widget.core.client.container.Viewport;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class KuchaApplication implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		/*
		 * apparently, the viewport is important since it guarantees that the
		 * content of all tabs will be updated in the background and look nice and
		 * clean all the time
		 */
		MainView main = new MainView();
		StaticTables st = StaticTables.getInstance();
		Viewport v = new Viewport();
		v.add(main);
		RootPanel.get().add(v); // use RootPanel, not RootLayoutPanel here!
	}

}