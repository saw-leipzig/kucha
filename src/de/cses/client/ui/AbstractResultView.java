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

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.Menu.MenuAppearance;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

/**
 * AbstractResultView is the base for the result views shown in MainView. Here the 
 * search results are added using the method {@link #addResult(Widget)}.
 * 
 * @author alingnau
 * @see de.cses.client.MainView
 * 
 */
public abstract class AbstractResultView extends Portlet {
	
	private ToolButton searchToolButton, saveToolButton;
	private FlowLayoutContainer resultContainer;
	private MarginData resultLayoutData;

	public AbstractResultView(String title) {
		super();
		this.setHeading(title);
		setCollapsible(true);
		setAnimCollapse(true);
		
		searchToolButton = new ToolButton(ToolButton.SEARCH);
		searchToolButton.setToolTip("start search");
		getHeader().addTool(searchToolButton);
		
		saveToolButton = new ToolButton(ToolButton.SAVE);
		saveToolButton.setToolTip("save");
		saveToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
			}
		});
		getHeader().addTool(saveToolButton);
		
		
		ToolButton toolButton = new ToolButton(ToolButton.MINIMIZE);
		toolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(300);
			}
		});
		getHeader().addTool(toolButton);

		toolButton = new ToolButton(ToolButton.MAXIMIZE);
		toolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(700);
			}
		});
		getHeader().addTool(toolButton);
		
		resultContainer = new FlowLayoutContainer();
		resultContainer.setScrollMode(ScrollMode.AUTOY);
		resultLayoutData = new MarginData(20);
		if (true) { // check here if the user has permission to edit and add elements
			resultContainer.add(newElementButton(), resultLayoutData);
		}
		this.add(resultContainer);
	}
	
	public void addResult(Widget w) {
		resultContainer.add(w, resultLayoutData);
	}
	
	public void addSearchSelectHandler(SelectHandler handler) {
		searchToolButton.addSelectHandler(handler);
	}

	/**
	 * 
	 */
	public void reset() {
		resultContainer.clear();
		if (true) { // check here if the user has permission to edit and add elements
			resultContainer.add(newElementButton(), resultLayoutData);
		}
	}
	
	/**
	 * Implements the specific Button to create and add a new element
	 * @return
	 */
	public abstract Widget newElementButton();

}
