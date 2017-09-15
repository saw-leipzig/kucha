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
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ProgressBar;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
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
		Viewport v = new Viewport();
		CenterLayoutContainer c = new CenterLayoutContainer();
		ProgressBar bar = new ProgressBar();
		bar.setSize("250px", "50px");
		c.add(bar);
		
		v.add(c);
		StaticTables.createInstance(new StaticTables.ListsLoadedListener() {
			
			@Override
			public void listsLoaded(double progressCounter) {
				bar.updateProgress(progressCounter, Math.round(progressCounter * 100) + "% loaded");
				if (progressCounter == 1.0) {
					MainView main = new MainView();
					v.remove(c);
					v.add(main);
					v.forceLayout();
				}
			}
		});
		RootPanel.get().add(v, 0, 0); // use RootPanel, not RootLayoutPanel here!
	}

}