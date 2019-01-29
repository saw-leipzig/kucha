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

import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
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
public abstract class AbstractSearchController extends ToggleButton {
	
	private String searchControllerTitle;
	private AbstractResultView resultView;
	private AbstractFilter filter;
		
	/**
	 * 
	 * @param searchControllerTitle
	 * @param resultView 
	 */
	public AbstractSearchController(String searchControllerTitle, AbstractFilter filter, AbstractResultView resultView) {
		super();
		setToolTip(Util.createToolTip(searchControllerTitle));

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
		setText(searchControllerTitle);
		setSize("70px", "70px");
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
