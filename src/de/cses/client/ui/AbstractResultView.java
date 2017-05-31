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
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * AbstractResultView is the base for the result views shown in MainView. Here the 
 * search results are added using the method {@link #addResult(Widget)}.
 * 
 * @author alingnau
 * @see de.cses.client.MainView
 * 
 */
public abstract class AbstractResultView extends Portlet {
	
	protected static final int MAX_HEIGHT = 700;
	protected static final int MIN_HEIGHT = 300;
	private ToolButton searchToolButton, saveToolButton, plusToolButton;
	private FlowLayoutContainer resultContainer;
	private MarginData resultLayoutData;

	public AbstractResultView(String title) {
		super();
		this.setHeading(title);
		setCollapsible(true);
		setAnimCollapse(true);
		setHeight(MIN_HEIGHT);
		
		searchToolButton = new ToolButton(ToolButton.SEARCH);
		searchToolButton.setToolTip("start search");
		getHeader().addTool(searchToolButton);
		
//		saveToolButton = new ToolButton(ToolButton.SAVE);
//		saveToolButton.setToolTip("save");
//		saveToolButton.addSelectHandler(new SelectHandler() {
//			
//			@Override
//			public void onSelect(SelectEvent event) {
//			}
//		});
//		getHeader().addTool(saveToolButton);
		
		plusToolButton = new ToolButton(ToolButton.PLUS);
		plusToolButton.setToolTip("Add New");
		getHeader().addTool(plusToolButton);
		
		ToolButton toolButton = new ToolButton(ToolButton.MINIMIZE);
		toolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(MIN_HEIGHT);
			}
		});
		getHeader().addTool(toolButton);

		toolButton = new ToolButton(ToolButton.MAXIMIZE);
		toolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(MAX_HEIGHT);
			}
		});
		getHeader().addTool(toolButton);
		
		
		resultContainer = new FlowLayoutContainer();
		resultContainer.setScrollMode(ScrollMode.AUTOY);
		resultLayoutData = new MarginData(20);
		this.add(resultContainer);
	}
	
	public void addResult(Widget w) {
		resultContainer.add(w, resultLayoutData);
	}
	
	/**
	 * adds a handler to the SEARCH ToolButton
	 * @param handler
	 */
	public void addSearchSelectHandler(SelectHandler handler) {
		searchToolButton.addSelectHandler(handler);
	}
	
	/**
	 * adds a handler to the PLUS ToolButton
	 * @param handler
	 */
	public void addPlusSelectHandler(SelectHandler handler) {
		plusToolButton.addSelectHandler(handler);
	}

	/**
	 * 
	 */
	public void reset() {
		resultContainer.clear();
	}
	
//	/**
//	 * Implements the specific Button to create and add a new element
//	 * @return
//	 */
//	public abstract Widget newElementButton();

}
