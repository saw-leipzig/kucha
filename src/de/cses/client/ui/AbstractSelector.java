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

import de.cses.client.caves.CaveResultView;

/**
 * @author alingnau
 *
 */
public abstract class AbstractSelector extends ToggleButton {
	
	String selectorTitle;
	ArrayList<AbstractFilter> relatedFilter;
	AbstractResultView resultView;
		
	/**
	 * 
	 * @param selectorTitle
	 * @param resultView 
	 */
	public AbstractSelector(String selectorTitle, AbstractResultView resultView) {
		super();
		this.selectorTitle = selectorTitle;
		this.resultView = resultView;
		relatedFilter = new ArrayList<AbstractFilter>();
		setText(selectorTitle);
		setSize("50px", "50px");
	}

	public void addRelatedFilter(AbstractFilter filter) {
		relatedFilter.add(filter);
	}
	
	public String getSelectorTitle() {
		return selectorTitle;
	}

	public ArrayList <AbstractFilter> getRelatedFilter() {
		return relatedFilter;
	}

	public AbstractResultView getResultView() {
		return resultView;
	}

	public void setResultView(AbstractResultView resultView) {
		this.resultView = resultView;
	}

	public abstract void invokeSearch();

}
