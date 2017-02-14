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

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.caves.CaveFilter;
import de.cses.client.caves.CaveSelector;
import de.cses.client.ui.LocationFilter;
import de.cses.client.ui.LocationSelector;

/**
 * @author alingnau
 *
 */
public class MainView implements IsWidget {

	private BorderLayoutContainer view = null;
	private ToggleButton depictionButton;
	private LocationSelector locationSelector;
	private CaveSelector caveSelector;
	private ContentPanel filterPanel;
	private VerticalLayoutContainer filterView;
	private VerticalLayoutContainer resultView;
	private VerticalLayoutData filterLayoutData;
	private TextButton searchButton;
	private TextField searchText;
	private VerticalLayoutData resultLayoutData;
	private Portlet caveResultView;

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
		if (view == null) {
			initView();
		}
    return view;
	}
	
	private void initView() {
    final boolean borders = true;
    final int margins = 5;
    
    VerticalPanel northPanel = new VerticalPanel();

    Header headline = new Header();
    headline.setHTML("<h1>Welcome to the Kucha Information System</h1>");
    headline.setHeight("2em");
    northPanel.add(headline);
    
    HorizontalLayoutContainer hLayoutContainer = new HorizontalLayoutContainer();
    HorizontalLayoutData hLayoutData = new HorizontalLayoutData(-1, 60, new Margins(margins, 0, margins, margins));
    
    filterView = new VerticalLayoutContainer();
    filterLayoutData = new VerticalLayoutData(-1, -1, new Margins(margins, 0, 0, 0));
    filterView.setScrollMode(ScrollMode.AUTOY);

    locationSelector = new LocationSelector("Location", new LocationFilter("Location Filter"));
    locationSelector.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					filterView.add(locationSelector.getFilter(), filterLayoutData);
				} else {
					locationSelector.getFilter().asWidget().removeFromParent();
				}
			}
		});
		hLayoutContainer.add(locationSelector, hLayoutData);
		
		caveResultView = new Portlet();
		caveResultView.setHeading("Caves");
		caveResultView.setCollapsible(true);
		caveResultView.setAnimCollapse(true);
		caveResultView.getHeader().addTool(new ToolButton(ToolButton.GEAR));

		caveSelector = new CaveSelector("Caves", new CaveFilter("Cave Filter"));
		caveSelector.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue().booleanValue()) {
					filterView.add(caveSelector.getFilter(), filterLayoutData);
					resultView.add(caveResultView, resultLayoutData);
				} else {
					caveSelector.getFilter().asWidget().removeFromParent();
	      	caveResultView.removeFromParent();
				}
			}
		});
		hLayoutContainer.add(caveSelector, hLayoutData);
		
		depictionButton = new ToggleButton();
		depictionButton.setText("Depictions");
		depictionButton.setSize("50px", "50px");
		hLayoutContainer.add(depictionButton, hLayoutData);
		
		searchButton = new TextButton("search");
		searchButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				Info.display("Search", "A search has been invoked");
			}
		});
		searchText = new TextField();
		searchText.setEmptyText("search for ...");
		searchText.setWidth(180);
		FramedPanel freeTextSearch = new FramedPanel();
		freeTextSearch.setHeading("Search Text");
		freeTextSearch.add(searchText);
//		freeTextSearch.addButton(searchButton);
		freeTextSearch.setButtonAlign(BoxLayoutPack.CENTER);
		filterView.add(freeTextSearch, filterLayoutData);
		
    ContentPanel centerPanel = new ContentPanel();
    centerPanel.setHeading("Results");
    centerPanel.setResize(false);
    resultView = new VerticalLayoutContainer();
    resultLayoutData = new VerticalLayoutData(-1, -1, new Margins(margins, 0, margins, 0));
    resultView.setScrollMode(ScrollMode.AUTOY);
    centerPanel.add(resultView);

    ContentPanel north = new ContentPanel();
    north.setHeading("What are you looking for?");
    north.add(hLayoutContainer);
    north.setHeight("80px");
    northPanel.add(north);
    
    filterPanel = new ContentPanel();
    filterPanel.setHeading("Filter");
    filterPanel.add(filterView);
    filterPanel.addButton(searchButton);
    filterPanel.setButtonAlign(BoxLayoutPack.CENTER);

    BorderLayoutData northData = new BorderLayoutData(150);
    northData.setMargins(new Margins(margins));
    northData.setCollapseHeaderVisible(true);

    BorderLayoutData westData = new BorderLayoutData(200);
    westData.setMargins(new Margins(0, margins, margins, margins));
    westData.setCollapsible(true);
    westData.setCollapseHeaderVisible(true);

    MarginData centerData = new MarginData(0, margins, margins, 0);

    view = new BorderLayoutContainer();
    view.setBorders(borders);
    view.setNorthWidget(northPanel, northData);
    view.setWestWidget(filterPanel, westData);
    view.setCenterWidget(centerPanel, centerData);

	}

}
