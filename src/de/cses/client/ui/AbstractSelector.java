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

import com.sencha.gxt.widget.core.client.button.ToggleButton;

/**
 * @author alingnau
 *
 */
public abstract class AbstractSelector extends ToggleButton {
	
	String selectorTitle;
	AbstractFilter filter;

	/**
	 * 
	 */
	public AbstractSelector(String selectorTitle, AbstractFilter filter) {
		this.selectorTitle = selectorTitle;
		this.filter = filter;
		setText(selectorTitle);
		setSize("50px", "50px");
	}
	
	
	public String getSelectorTitle() {
		return selectorTitle;
	}


	public AbstractFilter getFilter() {
		return filter;
	}

}
