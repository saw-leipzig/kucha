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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.Messages.Select;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.dnd.core.client.DndDropEvent;
import com.sencha.gxt.dnd.core.client.DropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
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
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.DataViewPortalLayoutContainer;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.UserEntry;
import javafx.scene.image.Image;

/**
 * @author alingnau
 *
 */
public class MainView implements IsWidget {
	
	// this footer will be shown at the bottom of the WebApp
	private static final String FOOTER_TEXT = "\u00A9 2019 Sächsische Akademie der Wissenschaften zu Leipzig (Version 0.9.3)";
	
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
	private DataViewPortalLayoutContainer dataViewPLC;
//	private DataDisplayController dataDisplayController;
	private ToolButton saveWorkspaceToolButton;
	private ToolButton loadWorkspaceToolButton;
	private Resources res;

	interface Resources extends ClientBundle {
		@Source("caves/cave.png")
		ImageResource cave();
		
		@Source("caves/cave_active.png")
		ImageResource cave_active();
	}
	
	
	/**
	 * 
	 */
	public MainView() {
		res = GWT.create(Resources.class);
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

    ToolButton caveInactiveTB = new ToolButton(new IconConfig("caveButton", "caveButtonOver"));
    caveInactiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				filterView.add(caveSearchController.getFilter(), 0);
				resultView.add(caveSearchController.getResultView(), 0);
			}
		});
    ToolButton caveActiveTB = new ToolButton(new IconConfig("caveButtonActive", "caveButtonOver"));
    caveActiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				caveSearchController.getResultView().removeFromParent();
				caveSearchController.getFilter().asWidget().removeFromParent();
			}
		});
		caveSearchController = new CaveSearchController("Caves", new CaveFilter("Caves"), new CaveResultView("Caves"), caveInactiveTB, caveActiveTB);
//		caveSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					filterView.add(caveSearchController.getFilter(), 0);
//					resultView.add(caveSearchController.getResultView(), 0);
//					caveSearchController.setIcon(res.cave_active());
//				} else {
//					caveSearchController.getResultView().removeFromParent();
//					caveSearchController.getFilter().asWidget().removeFromParent();
//					caveSearchController.setIcon(res.cave());
//				}
//			}
//		});
		
    ToolButton depictionInactiveTB = new ToolButton(new IconConfig("depictionButton", "depictionButtonOver"));
    depictionInactiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				filterView.add(depictionSearchController.getFilter(), 0);
				resultView.add(depictionSearchController.getResultView(), 0);
			}
		});
    ToolButton depictionActiveTB = new ToolButton(new IconConfig("depictionButtonActive", "depictionButtonOver"));
    depictionActiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				depictionSearchController.getFilter().asWidget().removeFromParent();
				depictionSearchController.getResultView().removeFromParent();
			}
		});
		depictionSearchController = new DepictionSearchController("Painted Representation", new DepictionFilter("Painted Representations"), new DepictionResultView("Painted Representation"), depictionInactiveTB, depictionActiveTB);
//		depictionSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					filterView.add(depictionSearchController.getFilter(), 0);
//					resultView.add(depictionSearchController.getResultView(), 0);
//				} else {
//					depictionSearchController.getFilter().asWidget().removeFromParent();
//					depictionSearchController.getResultView().removeFromParent();
//				}
//			}
//		});
		
    ToolButton imageInactiveTB = new ToolButton(new IconConfig("imagePoolButton", "imagePoolButtonOver"));
    imageInactiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				filterView.add(imageSearchController.getFilter(), 0);
				resultView.add(imageSearchController.getResultView(), 0);
			}
		});
    ToolButton imageActiveTB = new ToolButton(new IconConfig("imagePoolButtonActive", "imagePoolButtonOver"));
    imageActiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				imageSearchController.getFilter().asWidget().removeFromParent();
				imageSearchController.getResultView().removeFromParent();
			}
		});
		imageSearchController = new ImageSearchController("Image Pool", new ImageFilter("Image Filter"), new ImageResultView("Image Pool"), imageInactiveTB, imageActiveTB);
//		imageSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					filterView.add(imageSearchController.getFilter(), 0);
//					resultView.add(imageSearchController.getResultView(), 0);
//				} else {
//					imageSearchController.getFilter().asWidget().removeFromParent();
//					imageSearchController.getResultView().removeFromParent();
//				}
//			}
//			
//		});
		
    ToolButton ornamenticInactiveTB = new ToolButton(new IconConfig("ornamentationButton", "ornamentationButtonOver"));
    ornamenticInactiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				filterView.add(ornamenticSearchController.getFilter(), 0);
				resultView.add(ornamenticSearchController.getResultView(), 0);
			}
		});
    ToolButton ornamenticActiveTB = new ToolButton(new IconConfig("ornamentationButtonActive", "ornamentationButtonOver"));
    ornamenticActiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				ornamenticSearchController.getFilter().asWidget().removeFromParent();
				ornamenticSearchController.getResultView().removeFromParent();
			}
		});
		ornamenticSearchController = new OrnamenticSearchController("Ornamentation", new OrnamenticFilter("Ornamentation"), new OrnamenticResultView("Ornamentation"), ornamenticInactiveTB, ornamenticActiveTB);
//		ornamenticSearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					filterView.add(ornamenticSearchController.getFilter(), 0);
//					resultView.add(ornamenticSearchController.getResultView(), 0);
//				} else {
//					ornamenticSearchController.getFilter().asWidget().removeFromParent();
//					ornamenticSearchController.getResultView().removeFromParent();
//				}
//			}
//		});
		
		// annotated bibliography
		
    ToolButton bibInactiveTB = new ToolButton(new IconConfig("bibButton", "bibButtonOver"));
    bibInactiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				filterView.add(annotatedBiblographySearchController.getFilter(), 0);
				resultView.add(annotatedBiblographySearchController.getResultView(), 0);
			}
		});
    ToolButton bibActiveTB = new ToolButton(new IconConfig("bibButtonActive", "bibButtonOver"));
    bibActiveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				annotatedBiblographySearchController.getFilter().asWidget().removeFromParent();
				annotatedBiblographySearchController.getResultView().removeFromParent();
			}
		});
		annotatedBiblographySearchController = new AnnotatedBiblographySearchController("Annotated Biblography", new AnnotatedBibliographyFilter("Bibliography"), new AnnotatedBiblographyResultView("Annotated Biblography"), bibInactiveTB, bibActiveTB);
//		annotatedBiblographySearchController.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//
//			@Override
//			public void onValueChange(ValueChangeEvent<Boolean> event) {
//				if (event.getValue()) {
//					filterView.add(annotatedBiblographySearchController.getFilter(), 0);
//					resultView.add(annotatedBiblographySearchController.getResultView(), 0);
//				} else {
//					annotatedBiblographySearchController.getFilter().asWidget().removeFromParent();
//					annotatedBiblographySearchController.getResultView().removeFromParent();
//				}
//			}
//		});

		
		// ----------------------------------- assembling the menu bar ---------------------------------------------
		
		selectorLayoutContainer = new HorizontalLayoutContainer();
		selectorLayoutContainer.add(caveSearchController, new HorizontalLayoutData(75, 75, new Margins(5, 0, 5, 5)));
		selectorLayoutContainer.add(depictionSearchController, new HorizontalLayoutData(75, 75, new Margins(5, 0, 5, 5)));
		selectorLayoutContainer.add(ornamenticSearchController, new HorizontalLayoutData(75, 75, new Margins(5, 0, 5, 5)));
		selectorLayoutContainer.add(annotatedBiblographySearchController, new HorizontalLayoutData(75, 75, new Margins(5, 0, 5, 5)));
		selectorLayoutContainer.add(imageSearchController, new HorizontalLayoutData(75, 75, new Margins(5, 0, 5, 5)));
		
    ContentPanel centerPanel = new ContentPanel();
    centerPanel.setHeading("Search Results");
    centerPanel.getHeader().setStylePrimaryName("frame-header");
    centerPanel.addStyleName("transparent");
    centerPanel.setResize(true);

    resultView = new PortalLayoutContainer(1);
    resultView.setSpacing(10);
    resultView.setColumnWidth(0, 1.0);
    centerPanel.add(resultView);

    ContentPanel north = new ContentPanel();
    north.addStyleName("transparent");
    north.getHeader().setStylePrimaryName("frame-header");
    north.add(selectorLayoutContainer);
    
    // updating heading when user is logged in / out
    UserLogin.getInstance().addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
		    if (UserLogin.isLoggedIn()) {
		    	north.setHeading("Welcome back, " + UserLogin.getInstance().getUsername());
//		    	imageSearchController.setVisible(true);
		    	imageSearchController.setEnabled(true);
		    	if (UserLogin.getInstance().getAccessRights() >= UserEntry.ASSOCIATED) {
		    		loadWorkspaceToolButton.setEnabled(true);
		    		saveWorkspaceToolButton.setEnabled(true);
		    	}
		    } else {
		    	north.setHeading("Welcome! Your are currently here as a guest!");
		    	imageSearchController.setEnabled(false);
//		    	imageSearchController.setVisible(false);
		    	loadWorkspaceToolButton.setEnabled(false);
		    	saveWorkspaceToolButton.setEnabled(false);
		    }
			}
		});
    
    ToolButton loginTB = new ToolButton(new IconConfig("loginButton", "loginButtonOver"));
    loginTB.setToolTip(Util.createToolTip("User Login"));
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
    filterPanel.addStyleName("transparent");
    filterPanel.getHeader().setStylePrimaryName("frame-header");
    filterPanel.add(filterView);
    
    
    ContentPanel dataViewPanel = new ContentPanel();
    dataViewPanel.setResize(true);
    dataViewPanel.setHeading("View");
    dataViewPanel.addStyleName("transparent");
    dataViewPanel.getHeader().setStylePrimaryName("frame-header");
    dataViewPLC = new DataViewPortalLayoutContainer(1, dataViewPanel);
    dataViewPanel.add(dataViewPLC);
    saveWorkspaceToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
    saveWorkspaceToolButton.setToolTip(Util.createToolTip("save", "Save this view as a colletion either for private purpose or tp share with other users."));
    saveWorkspaceToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dataViewPLC.save();
			}
		});
    dataViewPanel.addTool(saveWorkspaceToolButton);
    loadWorkspaceToolButton = new ToolButton(new IconConfig("loadButton", "loadButtonOver"));
    loadWorkspaceToolButton.setToolTip(Util.createToolTip("load collection"));
    loadWorkspaceToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dataViewPLC.load();
			}
		});
    dataViewPanel.addTool(loadWorkspaceToolButton);
    ToolButton resetWorkspaceToolButton = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
    resetWorkspaceToolButton.setToolTip(Util.createToolTip("Reset to create new colection.", "Click here to delete content and start fresh collection."));
    resetWorkspaceToolButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dataViewPLC.resetView();
			}
		});
    dataViewPanel.addTool(resetWorkspaceToolButton);

    new DropTarget(dataViewPanel) {

			@Override
			protected void onDragDrop(DndDropEvent event) {
				super.onDragDrop(event);
				if (event.getData() instanceof AbstractEntry) {
					dataViewPLC.drop((AbstractEntry)event.getData());
				}
			}
		};
		
		Label footerLabel = new Label(FOOTER_TEXT);
		footerLabel.setStyleName("footer");
    
    BorderLayoutData northData = new BorderLayoutData(100);
    northData.setMinSize(100);
    northData.setMargins(new Margins(5, 5, 0, 5));
    
    BorderLayoutData southData = new BorderLayoutData(20);
    southData.setMargins(new Margins(0, 5, 5, 5));

    BorderLayoutData westData = new BorderLayoutData(220);
    westData.setMargins(new Margins(5));
    westData.setCollapsible(true);
    westData.setCollapseHeaderVisible(true);
    westData.setSplit(true);
    westData.setMinSize(220);
    
    BorderLayoutData eastData = new BorderLayoutData(500);
    eastData.setMaxSize(800);
    eastData.setMinSize(300);
    eastData.setMargins(new Margins(5));
    eastData.setCollapsible(true);
    eastData.setCollapseHeaderVisible(true);
    eastData.setSplit(true);

    MarginData centerData = new MarginData(5, 0, 5, 0);

    view = new BorderLayoutContainer();
    view.setBorders(borders);
    view.setNorthWidget(north, northData);
    view.setWestWidget(filterPanel, westData);
    view.setEastWidget(dataViewPanel, eastData);
    view.setSouthWidget(footerLabel, southData);
    view.setCenterWidget(centerPanel, centerData);
    view.setStyleName("");
    
    if (UserLogin.isLoggedIn()) {
    	north.setHeading("Welcome back, " + UserLogin.getInstance().getUsername());
    } else {
    	north.setHeading("Welcome! Your are currently here as a guest!");
//    	imageSearchController.setVisible(false);
    	imageSearchController.setEnabled(false);
    	saveWorkspaceToolButton.setEnabled(false);
    	loadWorkspaceToolButton.setEnabled(false);
    }
	}
	
	/**
	 * 
	 */
//	protected ArrayList<AbstractSearchController> getActiveFilters() {
//		ArrayList<AbstractSearchController> activeSelectors = new ArrayList<AbstractSearchController>();
//		Widget w;
//		Iterator<Widget> it;
//		AbstractSearchController selector;
//		it = selectorLayoutContainer.iterator();
//		while (it.hasNext()) {
//			w = it.next();
//			if (w instanceof AbstractSearchController) {
//				selector = ((AbstractSearchController) w);
//				if (selector.getValue()) {
//					activeSelectors.add(selector);
//				}
//			}
//		}
//		return activeSelectors;
//	}

}
