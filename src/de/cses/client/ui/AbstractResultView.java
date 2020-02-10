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

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.bibliography.AnnotatedBiblographyResultView;
import de.cses.client.bibliography.AnnotatedBiblographyView;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AbstractSearchEntry;
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
	protected AbstractSearchEntry searchEntry;
	protected TextButton addMoreResults;
	private VerticalLayoutContainer vlc;
	
	public AbstractResultView(String title) {
		super();
		this.title = title;
		this.setHeading(title);
		setCollapsible(true);
		setAnimCollapse(true);
		setHeight(MIN_HEIGHT);
		getHeader().setStylePrimaryName("frame-header");
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
		vlc = new VerticalLayoutContainer();
		CenterLayoutContainer clc = new CenterLayoutContainer();
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		addMoreResults = new TextButton("Add more Results");
		Util.doLogging(Boolean.toString(addMoreResults.isDeferHeight()));

	    addMoreResults.setStyleName("addResult");
		//clc.add(addMoreResults);
		//addMoreResults.set
		//addMoreResults.setLayoutData("addResult");
		//hlc.add(addMoreResults,new HorizontalLayoutData(1000,1));
		ScrollPanel scrpanel1 = new ScrollPanel();
		scrpanel1.add(vlc);
		this.add(scrpanel1);
		resultContainer = new FlowLayoutContainer();
		resultContainer.setScrollMode(ScrollMode.AUTOY);
		resultLayoutData = new MarginData(10);
		if (this instanceof AnnotatedBiblographyResultView) {
			vlc.addResizeHandler(new ResizeHandler() {
				@Override
				public void onResize(ResizeEvent event) {
					doResize();
				}
			});
			
		 }
		vlc.add(resultContainer);
		vlc.add(addMoreResults,new VerticalLayoutData(1,-1));
		addMoreResults.setWidth("100%");
		addMoreResults.hide();
		
		
	}
	/**
	 * 
	 * @param enable
	 */
	public void setSearchEntry(AbstractSearchEntry se) {
		this.searchEntry = se;

	}	
	public AbstractSearchEntry getSearchEntry() {
		return searchEntry;
	}
	public void getPics(String masterImageIDs , int res, String sessionID) {

	}
	public FlowLayoutContainer getContainer() {
		return resultContainer;
	}
	public void setSearchbuttonVisible() {
		addMoreResults.show();
	}
	public void setSearchbuttonHide() {
		addMoreResults.hide();
	}
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
		}
		else if (view instanceof AnnotatedBiblographyView) {
			resultContainer.add(view, new VerticalLayoutContainer.VerticalLayoutData(-1, -1, new Margins(10)));
		}
		else {
			resultContainer.add(view, resultLayoutData);
		}
		

	}
	
	/**
	 * adds a handler to the SEARCH ToolButton
	 * @param handler
	 */
	public void addSearchSelectHandler(SelectHandler handler) {
		searchToolButton.addSelectHandler(handler);
	}
	public void doResize() {
		for (int i=0; i<resultContainer.getWidgetCount(); i++){
			TextMetrics t = TextMetrics.get();
			t.bind(((AnnotatedBiblographyView)resultContainer.getWidget(i)).getElement());
			t.setFixedWidth(((AnnotatedBiblographyView)resultContainer.getWidget(i)).getElement().getClientWidth());
			//Util.doLogging(Integer.toString(((int)(t.getHeight(((AnnotatedBiblographyView)resultContainer.getWidget(i)).getHTML())*0.8)))+"px");
			((AnnotatedBiblographyView)resultContainer.getWidget(i)).setHeight(Integer.toString(((int)(t.getHeight(((AnnotatedBiblographyView)resultContainer.getWidget(i)).getHTML())*0.85)))+"px");
		}
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
