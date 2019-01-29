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

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.shared.AbstractEntry;

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
	private ToolButton searchToolButton;
	private ToolButton saveToolButton;
	private ToolButton plusToolButton;
	private ToolButton resetButton;
	private FlowLayoutContainer resultContainer;
	private MarginData resultLayoutData;
	private String title;
	private ToolButton minTB;
	private ToolButton maxTB;
	
	public AbstractResultView(String title) {
		super();
		this.title = title;
		this.setHeading(title);
		setCollapsible(true);
		setAnimCollapse(true);
		setHeight(MIN_HEIGHT);
    getHeader().setStylePrimaryName("frame-header");
    setStylePrimaryName("frame-body");
    addStyleName("transparent");
		
		searchToolButton = new ToolButton(new IconConfig("startSearchButton", "startSearchButtonOver", "searchActiveButton"));
		searchToolButton.setToolTip(Util.createToolTip("start search"));
		getHeader().addTool(searchToolButton);
		
		this.getAppearance().collapseIcon().setStyle("collapseWindowButton");
		this.getAppearance().collapseIcon().setOverStyle("collapseWindowButtonOver");
		this.getAppearance().expandIcon().setStyle("expandWindowButton");
		this.getAppearance().expandIcon().setOverStyle("expandWindowButtonOver");
		
//		saveToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
//		saveToolButton.setToolTip(Util.createToolTip("save"));
//		saveToolButton.addSelectHandler(new SelectHandler() {
//			
//			@Override
//			public void onSelect(SelectEvent event) {
//			}
//		});
//		getHeader().addTool(saveToolButton);
		
		plusToolButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		plusToolButton.setToolTip(Util.createToolTip("Add New"));
		getHeader().addTool(plusToolButton);
		
		minTB = new ToolButton(new IconConfig("minimizeButton", "minimizeButtonOver"));
		minTB.setToolTip(Util.createToolTip("small window"));
		minTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(MIN_HEIGHT);
				minTB.setVisible(false);
				maxTB.setVisible(true);
			}
		});
		getHeader().addTool(minTB);
		minTB.setVisible(false);

		maxTB= new ToolButton(new IconConfig("maximizeButton", "maximizeButtonOver"));
		maxTB.setToolTip(Util.createToolTip("large window"));
		maxTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setHeight(MAX_HEIGHT);
				maxTB.setVisible(false);
				minTB.setVisible(true);
			}
		});
		getHeader().addTool(maxTB);
		
		resetButton = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetButton.setToolTip(Util.createToolTip("Reset Results"));
		resetButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				reset();
			}
		});
		getHeader().addTool(resetButton);
		
		resultContainer = new FlowLayoutContainer();
		resultContainer.setScrollMode(ScrollMode.AUTOY);
		resultLayoutData = new MarginData(10);
		this.add(resultContainer);
		
	}
	
	/**
	 * 
	 * @param enable
	 */
	public void setSearchEnabled(boolean enable) {
		searchToolButton.setEnabled(enable);
	}

	/**
	 * Adds an element that is not contained in the result view
	 * @param w
	 */
	public void addResult(Widget view) {
		// TODO implementation is quite ugly, should be a better way
		if (view instanceof AbstractView && ((AbstractView)view).getEntry() instanceof AbstractEntry) {
			AbstractEntry droppedEntry = (AbstractEntry)((AbstractView)view).getEntry();
			Iterator<Widget> widgetIterator = resultContainer.iterator();
			while (widgetIterator.hasNext()) {
				Widget w = widgetIterator.next();
				if (w instanceof AbstractView && ((AbstractView)w).getEntry() instanceof AbstractEntry) {
					if (((AbstractEntry)((AbstractView)w).getEntry()).getUniqueID().equals(droppedEntry.getUniqueID())) {
						return;
					}
				}
			}
		}
		if (view instanceof DepictionDataDisplay) {
			resultContainer.add(view, new VerticalLayoutContainer.VerticalLayoutData(1.0, 300.0, new Margins(10)));
		} else {
			resultContainer.add(view, resultLayoutData);
		}
		setHeading(title + " (" + resultContainer.getWidgetCount() + " elements)");
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
		setHeading(title);
	}
	
	public ArrayList<AbstractEntry> getEntriesOnDisplay() {
		ArrayList<AbstractEntry> results = new ArrayList<AbstractEntry>();
		Iterator<Widget> widgetIterator = resultContainer.iterator();
		while (widgetIterator.hasNext()) {
			Widget w = widgetIterator.next();
			if (w instanceof AbstractView && ((AbstractView)w).getEntry() instanceof AbstractEntry) {
				results.add(((AbstractView)w).getEntry());
			}
		}
		return results;
	}
	
//	/**
//	 * Implements the specific Button to create and add a new element
//	 * @return
//	 */
//	public abstract Widget newElementButton();

}
