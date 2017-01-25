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
package de.cses.client;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * @author alingnau
 *
 */
public class MainView implements IsWidget {

	private VerticalLayoutContainer view;
	private ToolBar selectorBar;
	private ToggleButton siteButton;

	/**
	 * 
	 */
	public MainView() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		
		selectorBar = new ToolBar();
		siteButton = new ToggleButton();
		siteButton.setText("Site");
		selectorBar.add(siteButton);
		
    view = new VerticalLayoutContainer();
    view.add(selectorBar, new VerticalLayoutData(1, 0.25, new Margins(10)));
//    view.add(, new VerticalLayoutData(1, 0.5, new Margins(0, 10, 0, 10)));
//    view.add(, new VerticalLayoutData(1, 0.25, new Margins(10)));

    return view;
	}

}
