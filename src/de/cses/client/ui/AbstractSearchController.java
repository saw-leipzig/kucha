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

import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.user.UserLogin;

/**
 * This abstract class is extended every time a new type (e.g. Caves, Ornaments, Images, Depictions, ...) shall be 
 * added to the UI. Is is representing the C in the MVC approach, visually represented by a ToggleButton
 * in the upper border of the MainView class. It controls the AbstractResultView as the V in MVC. 
 * It also controls the list of related filters which are shown on the left in the UI.
 *  
 * @author alingnau
 */
public abstract class AbstractSearchController extends ContentPanel {
	
	private String searchControllerTitle;
	private AbstractResultView resultView;
	private AbstractFilter filter;
	private ToolButton activeTB;
	private ToolButton inactiveTB;
		
	/**
	 * 
	 * @param searchControllerTitle
	 * @param filter the related filter
	 * @param resultView the related result view
	 * @param inactiveTB ToolButton shown when result view is inactive
	 * @param activeTB ToolButton shown when result view is active
	 */
	public AbstractSearchController(String searchControllerTitle, AbstractFilter filter, AbstractResultView resultView, ToolButton inactiveTB, ToolButton activeTB) {
		setHeaderVisible(false);
		setBorders(false);
		setBodyBorder(false);
		add(inactiveTB, new MarginData(2));
		setToolTip(Util.createToolTip(searchControllerTitle));
		// switching between buttons
		inactiveTB.addSelectHandler(new SelectHandler() { 
			
			@Override
			public void onSelect(SelectEvent event) {
				setActive(true);
			}
		});
		activeTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				setActive(false);
			}
		});
		
		this.activeTB = activeTB;
		this.inactiveTB = inactiveTB;
		this.searchControllerTitle = searchControllerTitle;
		this.resultView = resultView;
		this.filter = filter;
		this.resultView.addSearchSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				getResultView().setSearchEnabled(false);
				invokeSearch();
			}
		});
		this.resultView.addPlusSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				if (UserLogin.isLoggedIn()) {
					addNewElement();
				} else {
					Util.showWarning("You are not logged in", "You need to log in first\n to add new elements!");
				}
			}
		});
//		setText(searchControllerTitle.substring(0, 1)); // this is temporary until the icons are ready
//		setSize("70px", "70px");
	}
	
	private void setActive(boolean active) {
		if (active) {
			inactiveTB.removeFromParent();
			add(activeTB, new MarginData(2));
			activeTB.show();
		} else {
			activeTB.removeFromParent();
			add(inactiveTB, new MarginData(2));
			inactiveTB.show();
		}
	}
	
	public String getSelectorTitle() {
		return searchControllerTitle;
	}

	public AbstractResultView getResultView() {
		return resultView;
	}

	public void setResultView(AbstractResultView resultView) {
		this.resultView = resultView;
	}

	/**
	 * This abstract method need to be implemented for each controller. It should use the SearchEntry
	 * and then search for the data and display it in the resultView. For an example how it works, take a look
	 * into some implementations in classes extending AbstractSearchController.
	 * @param sourceToolButton 
	 */
	public abstract void invokeSearch();

	/**
	 * This abstract method needs to be implemented for each controller. It should open the corresponding editor in a mode
	 * where a new element is created and added to the database.
	 */
	public abstract void addNewElement();

	public AbstractFilter getFilter() {
		return filter;
	}

}
