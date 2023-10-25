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
package de.cses.client.depictions;

import de.cses.shared.IconographyEntry;

/**
 * @author alingnau
 *
 */
public interface IconographySelectorListener {
	
	//public void iconographySelected(IconographyEntry entry);
	//public void cancel();
	public void icoHighlighter(int icoID);
	public void icoDeHighlighter(int icoID);
	public void reloadIconography(IconographyEntry iconographyEntry);
	public void reloadOSD();
	public void reduceTree();

}
