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
package de.cses.client.ui;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;

/**
 * @author alingnau
 *
 */
public abstract class AbstractResultView extends Portlet {
	
	private ToolButton toolButton;
	private FlowLayoutContainer resultContainer;
	private MarginData resultLayoutData;

	public AbstractResultView(String title) {
		super();
		setCollapsible(true);
		setAnimCollapse(true);
		toolButton = new ToolButton(ToolButton.GEAR);
		getHeader().addTool(toolButton);
		resultContainer = new FlowLayoutContainer();
		resultContainer.setScrollMode(ScrollMode.AUTOY);
		resultLayoutData = new MarginData(20);
		if (true) { // check here if the user has permission to edit and add elements
			resultContainer.add(newElementButton());
		}
		this.add(resultContainer);
	}
	
	public void addResult(Widget w) {
		resultContainer.add(w, resultLayoutData);
	}

	/**
	 * 
	 */
	public void reset() {
		resultContainer.clear();
		if (true) { // check here if the user has permission to edit and add elements
			resultContainer.add(newElementButton());
		}
	}
	
	/**
	 * Implements the specific Button to create and add a new element
	 * @return
	 */
	public abstract TextButton newElementButton();

}
