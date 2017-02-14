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
package de.cses.client.caves;

import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSelector;

/**
 * @author alingnau
 *
 */
public class CaveSelector extends AbstractSelector {

	/**
	 * @param selectorTitle
	 */
	public CaveSelector(String selectorTitle, CaveFilter filter) {
		super(selectorTitle, filter);
	}

}
