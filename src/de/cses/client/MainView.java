/*
 * Copyright 2017, 2018 
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

import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.bibliography.AnnotatedBibliographyFilter;
import de.cses.client.bibliography.AnnotatedBiblographyResultView;
import de.cses.client.bibliography.AnnotatedBiblographySearchController;
import de.cses.client.caves.CaveDataDisplay;
import de.cses.client.caves.CaveFilter;
import de.cses.client.caves.CaveResultView;
import de.cses.client.caves.CaveSearchController;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.client.depictions.DepictionFilter;
import de.cses.client.depictions.DepictionResultView;
import de.cses.client.depictions.DepictionSearchController;
import de.cses.client.images.ImageFilter;
import de.cses.client.images.ImageResultView;
import de.cses.client.images.ImageSearchController;
import de.cses.client.ornamentic.OrnamenticFilter;
import de.cses.client.ornamentic.OrnamenticResultView;
import de.cses.client.ornamentic.OrnamenticSearchController;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;

/**
 * @author alingnau
 *
 */
public class MainView implements IsWidget {
	
	private static ArrayList<String> dataDisplayUniqueIDList = null;

	private BorderLayoutContainer view = null;
	private CaveSearchController caveSearchController;
	private ContentPanel filterPanel;
	private PortalLayoutContainer filterView;
	private PortalLayoutContainer resultView;
//	private TextButton searchButton;
	private TextField searchText;
	private FramedPanel searchTextPanel;
	private HorizontalLayoutContainer selectorLayoutContainer;
	private DepictionSearchController depictionSearchController;
	private ImageSearchController imageSearchController;
	private OrnamenticSearchController ornamenticSearchController;
//	private ResultCollectorController resultCollectorController;
	private AnnotatedBiblographySearchController annotatedBiblographySearchController;
	private PortalLayoutContainer dataViewPLC;
//	private DataDisplayController dataDisplayController;
	private ToolButton saveWorkspaceToolButton;
	private ToolButton loadWorkspaceToolButton;

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
    boolean borders = true;
    
//    northPanel = new VerticalLayoutContainer();
//
//    northPanel.add(UserLogin.getInstance(), new VerticalLayoutData(1.0, .4));
    
//    LocationFilter lFilter = new LocationFilter("Location Filter");

		caveSearchController = new CaveSearchController("Caves", new CaveResultView("Caves"));
		caveSearchController.addRelatedFilter(new CaveFilter("Caves"));
//		caveSearchController.addRelatedFilter(lFilter);
		caveSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : caveSearchController.getRelatedFilter()) {
						filterView.add(filter, 0);
					}
					resultView.add(caveSearchController.getResultView(), 0);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : caveSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter, 0);
						}
					}
					caveSearchController.getResultView().removeFromParent();
				}
			}
		});
		
		depictionSearchController = new DepictionSearchController("Painted Representations", new DepictionResultView("Painted Representations"));
		depictionSearchController.addRelatedFilter(new DepictionFilter("Painted Representations"));
		depictionSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : depictionSearchController.getRelatedFilter()) {
						filterView.add(filter, 0);
					}
					resultView.add(depictionSearchController.getResultView(), 0);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : depictionSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter, 0);
						}
					}
					depictionSearchController.getResultView().removeFromParent();
				}
			}
		});
		
		imageSearchController = new ImageSearchController("Image Pool", new ImageResultView("Image Pool"));
		imageSearchController.addRelatedFilter(new ImageFilter("Image Filter"));
		imageSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : imageSearchController.getRelatedFilter()) {
						filterView.add(filter, 0);
					}
					resultView.add(imageSearchController.getResultView(), 0);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : imageSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)) {
							filterView.remove(filter, 0);
						}
					}
					imageSearchController.getResultView().removeFromParent();
				}
			}
			
		});
		
		ornamenticSearchController = new OrnamenticSearchController("Ornamentation", new OrnamenticResultView("Ornamentation"));
		ornamenticSearchController.addRelatedFilter(new OrnamenticFilter("Ornamentations"));
		ornamenticSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : ornamenticSearchController.getRelatedFilter()) {
						filterView.add(filter, 0);
					}
					resultView.add(ornamenticSearchController.getResultView(), 0);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : ornamenticSearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter, 0);
						}
					}
					ornamenticSearchController.getResultView().removeFromParent();
				}
			}
		});
		
		// annotated bibliography
		
		annotatedBiblographySearchController = new AnnotatedBiblographySearchController("Annotated Biblography", new AnnotatedBiblographyResultView("Annotated Biblography"));
		annotatedBiblographySearchController.addRelatedFilter(new AnnotatedBibliographyFilter("Bibliography"));
		annotatedBiblographySearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue()) {
					for (AbstractFilter filter : annotatedBiblographySearchController.getRelatedFilter()) {
						filterView.add(filter, 0);
					}
					resultView.add(annotatedBiblographySearchController.getResultView(), 0);
				} else {
					ArrayList<AbstractFilter> usedFilter = getUsedFilter();
					for (AbstractFilter filter : annotatedBiblographySearchController.getRelatedFilter()) {
						if (!usedFilter.contains(filter)){
							filterView.remove(filter, 0);
						}
					}
					annotatedBiblographySearchController.getResultView().removeFromParent();
				}
			}
		});
		
		// result collector
		
//		resultCollectorController = new ResultCollectorController("Personal Desktop", new ResultCollectorView("Personal Desktop"));
//		resultCollectorController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					dataViewPLC.add(resultCollectorController.getResultView(), 0);
//				} else {
//					resultCollectorController.getResultView().removeFromParent();
//				}
//			}
//		});
		
		// Data Display
		
//		dataDisplayController = new DataDisplayController("Data", new DataDisplayView("Data"));
//		dataDisplayController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					dataViewPLC.add(dataDisplayController.getResultView(), 0);
//				} else {
//					dataDisplayController.getResultView().removeFromParent();
//				}
//			}
//		});

		
		// ----------------------------------- assembling the menu bar ---------------------------------------------
		
    HorizontalLayoutData hLayoutData = new HorizontalLayoutData(140, 1.0, new Margins(5, 0, 5, 5));
    
		selectorLayoutContainer = new HorizontalLayoutContainer();
		selectorLayoutContainer.add(caveSearchController, hLayoutData);
		selectorLayoutContainer.add(depictionSearchController, hLayoutData);
		selectorLayoutContainer.add(ornamenticSearchController, hLayoutData);
		selectorLayoutContainer.add(annotatedBiblographySearchController, hLayoutData);
		selectorLayoutContainer.add(imageSearchController, hLayoutData);
//		selectorLayoutContainer.add(resultCollectorController, hLayoutData);
//		selectorLayoutContainer.add(dataDisplayController, hLayoutData);
		
    ContentPanel centerPanel = new ContentPanel();
    centerPanel.setHeading("Search Results");
    centerPanel.setResize(true);
    /*
     * Currently we implement a 2-column layout with a spacing of 10. 
     */
    resultView = new PortalLayoutContainer(1);
    resultView.setSpacing(10);
    resultView.setColumnWidth(0, 1.0);
//    resultView.setColumnWidth(1, .40);
    centerPanel.add(resultView);

    ContentPanel north = new ContentPanel();
    north.add(selectorLayoutContainer);
    
    // updating heading when user is logged in / out
    UserLogin.getInstance().addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
		    if (UserLogin.isLoggedIn()) {
		    	north.setHeading("Welcome back, " + UserLogin.getInstance().getUsername());
//		    	imageSearchController.setVisible(true);
		    	imageSearchController.setEnabled(true);
		    	loadWorkspaceToolButton.setVisible(true);
		    	saveWorkspaceToolButton.setVisible(true);
		    } else {
		    	north.setHeading("Welcome! Your are currently here as a guest!");
		    	imageSearchController.setEnabled(false);
//		    	imageSearchController.setVisible(false);
		    	loadWorkspaceToolButton.setVisible(false);
		    	saveWorkspaceToolButton.setVisible(false);
		    }
			}
		});

    ToolButton loginTB = new ToolButton(ToolButton.GEAR);
    loginTB.setToolTip(Util.toolTip.create("User Login"));
    loginTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				UserLogin.getInstance().center();
			}
		});
    loginTB.setToolTip("login");
    north.addTool(loginTB);
//    northPanel.add(north, new VerticalLayoutData(1.0, .6));
    
    filterView = new PortalLayoutContainer(1);
    filterView.setSpacing(10);
    filterView.setColumnWidth(0, 1.00);
    
		searchText = new TextField();
		searchTextPanel = new Portlet();
		searchTextPanel.add(searchText);
		searchTextPanel.setHeading("search for");
		
    filterPanel = new ContentPanel();
    filterPanel.setResize(true);
    filterPanel.setHeading("Filter");
    filterPanel.add(filterView);
    
    dataViewPLC = new PortalLayoutContainer(1);
    dataViewPLC.setSpacing(10);
    dataViewPLC.setColumnWidth(0, 1.00);
    
    ContentPanel dataViewPanel = new ContentPanel();
    dataViewPanel.setResize(true);
    dataViewPanel.setHeading("View");
    dataViewPanel.add(dataViewPLC);
    saveWorkspaceToolButton = new ToolButton(ToolButton.SAVE);
    saveWorkspaceToolButton.setToolTip(Util.toolTip.create("save", "not yet implemented"));
    saveWorkspaceToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				// TODO save view
			}
		});
    dataViewPanel.addTool(saveWorkspaceToolButton);
    loadWorkspaceToolButton = new ToolButton(ToolButton.RESTORE);
    loadWorkspaceToolButton.setToolTip(Util.toolTip.create("load", "not yet implemented"));
    loadWorkspaceToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				// TODO load workspace
			}
		});
    dataViewPanel.addTool(loadWorkspaceToolButton);

    DropTarget target = new DropTarget(dataViewPanel) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof CaveEntry) {
					CaveDataDisplay cdd = new CaveDataDisplay((CaveEntry) event.getData());
					if (!MainView.getDataDisplayUniqueIDList().contains(cdd.getUniqueID())) {
						dataViewPLC.add(cdd, 0);
						MainView.getDataDisplayUniqueIDList().add(cdd.getUniqueID());
					}
				} else if (event.getData() instanceof DepictionEntry) {
					DepictionDataDisplay ddd = new DepictionDataDisplay((DepictionEntry) event.getData());
					if (!MainView.getDataDisplayUniqueIDList().contains(ddd.getUniqueID())) {
						dataViewPLC.add(ddd, 0);
						MainView.getDataDisplayUniqueIDList().add(ddd.getUniqueID());
					}
				} else if (event.getData() instanceof ImageEntry) {
//					addResult(new ImageView((ImageEntry) event.getData()));
				} else if (event.getData() instanceof OrnamentEntry) {
//					addResult(new OrnamenticView((OrnamentEntry) event.getData()));
				} else if (event.getData() instanceof AnnotatedBiblographyEntry) {
//					addResult(new AnnotatedBiblographyView((AnnotatedBiblographyEntry) event.getData()));
				}
			}
		};
    
    BorderLayoutData northData = new BorderLayoutData(70);
    northData.setMargins(new Margins(5));

    BorderLayoutData westData = new BorderLayoutData(220);
    westData.setMargins(new Margins(5));
    westData.setCollapsible(true);
    westData.setCollapseHeaderVisible(true);
    westData.setSplit(true);
    
    BorderLayoutData eastData = new BorderLayoutData(500);
    eastData.setMaxSize(800);
    eastData.setMinSize(300);
    eastData.setMargins(new Margins(5));
    eastData.setCollapsible(true);
    eastData.setCollapseHeaderVisible(true);
    eastData.setSplit(true);

    MarginData centerData = new MarginData(5);

    view = new BorderLayoutContainer();
    view.setBorders(borders);
    view.setNorthWidget(north, northData);
    view.setWestWidget(filterPanel, westData);
    view.setEastWidget(dataViewPanel, eastData);
    view.setCenterWidget(centerPanel, centerData);
    view.setStyleName("");

    if (UserLogin.isLoggedIn()) {
    	north.setHeading("Welcome back, " + UserLogin.getInstance().getUsername());
    } else {
    	north.setHeading("Welcome! Your are currently here as a guest!");
//    	imageSearchController.setVisible(false);
    	imageSearchController.setEnabled(false);
    	saveWorkspaceToolButton.setVisible(false);
    	loadWorkspaceToolButton.setVisible(false);
    }
	}
	
//	private void addDroppedDataDisplay(AbstractDataDisplay dd) {
//		Iterator<Widget> widgetIterator = dataViewPLC.getContainer().iterator();
//		while (widgetIterator.hasNext()) {
//			if (dd instanceof Widget) {
//				Widget w = widgetIterator.next();
//				if (w.toString().equals(((Widget)dd).toString())) {
//					Util.doLogging("AbstractDataDisplay already added");
//					return;
//				}
//			}
//		}
//		Util.doLogging("adding newly dropped element");
//		dataViewPLC.add(dd, 0);
//	}
	
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

	public static ArrayList<String> getDataDisplayUniqueIDList() {
		if (dataDisplayUniqueIDList == null) {
			dataDisplayUniqueIDList = new ArrayList<String>();
		}
		return dataDisplayUniqueIDList;
	}
	
}
