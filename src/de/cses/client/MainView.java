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

import java.util.ArrayList;
import java.util.Iterator;

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
import com.sencha.gxt.widget.core.client.button.TextButton;
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

import de.cses.client.caves.CaveFilter;
import de.cses.client.caves.CaveResultView;
import de.cses.client.caves.CaveSearchController;
import de.cses.client.depictions.DepictionFilter;
import de.cses.client.depictions.DepictionResultView;
import de.cses.client.depictions.DepictionSearchController;
import de.cses.client.images.ImageFilter;
import de.cses.client.images.ImageResultView;
import de.cses.client.images.ImageSearchController;
import de.cses.client.ornamentic.OrnamenticFilter;
import de.cses.client.ornamentic.OrnamenticResultView;
import de.cses.client.ornamentic.OrnamenticSearchController;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.LocationFilter;

/**
 * @author alingnau
 *
 */
public class MainView implements IsWidget {

	private BorderLayoutContainer view = null;
	private CaveSearchController caveSearchController;
	private ContentPanel filterPanel;
	private VerticalLayoutContainer filterView;
	private VerticalLayoutContainer resultView;
	private VerticalLayoutData filterLayoutData;
	private TextButton searchButton;
	private TextField searchText;
	private VerticalLayoutData resultLayoutData;
	private VerticalPanel northPanel;
	private FramedPanel searchTextPanel;
	private HorizontalLayoutContainer selectorLayoutContainer;
	private DepictionSearchController depictionSearchController;
	private ImageSearchController imageSearchController;
	private OrnamenticSearchController ornamenticSearchController;

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
    
    northPanel = new VerticalPanel();

    Header headline = new Header();
    headline.setHTML("<h1>Welcome to the Kucha Information System</h1>");
    headline.setHeight("2em");
    northPanel.add(headline);
    
    selectorLayoutContainer = new HorizontalLayoutContainer();
    HorizontalLayoutData hLayoutData = new HorizontalLayoutData(80, 60, new Margins(margins, 0, margins, margins));
    
    LocationFilter lFilter = new LocationFilter("Location Filter");
    CaveFilter cFilter = new CaveFilter("Cave Filter");
    filterLayoutData = new VerticalLayoutData(-1, -1, new Margins(5, 0, 5, 0));

		caveSearchController = new CaveSearchController("Caves", new CaveResultView("Caves"));
		caveSearchController.addRelatedFilter(cFilter);
		caveSearchController.addRelatedFilter(lFilter);
		caveSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : caveSearchController.getRelatedFilter()) {
						filterView.add(filter, filterLayoutData);
					}
					resultView.add(caveSearchController.getResultView(), resultLayoutData);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : caveSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter);
						}
					}
					caveSearchController.getResultView().removeFromParent();
				}
			}
		});
		selectorLayoutContainer.add(caveSearchController, hLayoutData);
		
		depictionSearchController = new DepictionSearchController("Depictions", new DepictionResultView("Depictions"));
		depictionSearchController.addRelatedFilter(new DepictionFilter("Depiction Filter"));
		depictionSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : depictionSearchController.getRelatedFilter()) {
						filterView.add(filter, filterLayoutData);
					}
					resultView.add(depictionSearchController.getResultView(), resultLayoutData);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : depictionSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter);
						}
					}
					depictionSearchController.getResultView().removeFromParent();
				}
			}
		});
		selectorLayoutContainer.add(depictionSearchController, hLayoutData);
		
		imageSearchController = new ImageSearchController("Images", new ImageResultView("Images"));
		imageSearchController.addRelatedFilter(new ImageFilter("Image Filter"));
		imageSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : imageSearchController.getRelatedFilter()) {
						filterView.add(filter, filterLayoutData);
					}
					resultView.add(imageSearchController.getResultView(), resultLayoutData);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : imageSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)) {
							filterView.remove(filter);
						}
					}
					imageSearchController.getResultView().removeFromParent();
				}
			}
			
		});
		selectorLayoutContainer.add(imageSearchController, hLayoutData);
		
		
		ornamenticSearchController = new OrnamenticSearchController("Ornamentic", new OrnamenticResultView("Ornamentic"));
		ornamenticSearchController.addRelatedFilter(new OrnamenticFilter("Ornamentic Filter"));
		ornamenticSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : ornamenticSearchController.getRelatedFilter()) {
						filterView.add(filter, filterLayoutData);
					}
					resultView.add(ornamenticSearchController.getResultView(), resultLayoutData);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : ornamenticSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter);
						}
					}
					ornamenticSearchController.getResultView().removeFromParent();
				}
			}
		});
		selectorLayoutContainer.add(ornamenticSearchController, hLayoutData);
		
		
    ContentPanel centerPanel = new ContentPanel();
    centerPanel.setHeading("Results");
    centerPanel.setResize(false);
    resultView = new VerticalLayoutContainer();
    resultLayoutData = new VerticalLayoutData(-1, -1, new Margins(margins, 0, margins, 0));
    resultView.setScrollMode(ScrollMode.AUTOY);
    centerPanel.add(resultView);

    ContentPanel north = new ContentPanel();
    north.setHeading("What are you looking for?");
    north.add(selectorLayoutContainer);
    north.setHeight("80px");
    northPanel.add(north);
    
    filterView = new VerticalLayoutContainer();
    filterView.setScrollMode(ScrollMode.AUTOY);
		searchText = new TextField();
		searchText.setEmptyText("search for ...");
		searchText.setWidth(185);
		searchTextPanel = new FramedPanel();
		searchTextPanel.add(searchText);
		searchTextPanel.setHeaderVisible(false);
		filterView.add(searchTextPanel, filterLayoutData);		

		searchButton = new TextButton("search");
		searchButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				for (AbstractSearchController s : getActiveFilters()) {
					s.invokeSearch();
				}
			}
		});
		
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

	/**
	 * 
	 */
	protected ArrayList<AbstractFilter> getUsedFilter() {
		ArrayList<AbstractFilter> usedFilter = new ArrayList<AbstractFilter>();
		Widget w;
		Iterator<Widget> it;
		AbstractSearchController selector;
		it = selectorLayoutContainer.iterator();
		while (it.hasNext()) {
			w = it.next();
			if (w instanceof AbstractSearchController) {
				selector = ((AbstractSearchController) w);
				if (selector.getValue()) {
					usedFilter.addAll(selector.getRelatedFilter());
				}
			}
		}
		return usedFilter;
	}
	
	/**
	 * 
	 */
	protected ArrayList<AbstractSearchController> getActiveFilters() {
		ArrayList<AbstractSearchController> activeSelectors = new ArrayList<AbstractSearchController>();
		Widget w;
		Iterator<Widget> it;
		AbstractSearchController selector;
		it = selectorLayoutContainer.iterator();
		while (it.hasNext()) {
			w = it.next();
			if (w instanceof AbstractSearchController) {
				selector = ((AbstractSearchController) w);
				if (selector.getValue()) {
					activeSelectors.add(selector);
				}
			}
		}
		return activeSelectors;
	}
	
}
