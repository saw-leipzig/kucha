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
package de.cses.client.images;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.form.Radio;

import de.cses.client.ui.AbstractFilter;

/**
 * @author alingnau
 *
 */
public class ImageFilter extends AbstractFilter {
	
	private Radio rPhoto;
	private Radio rSketch;
	private Radio rMap;

	public static final String PHOTO = "photo";
	public static final String SKETCH = "sketch";
	public static final String MAP = "map";

	/**
	 * @param filterName
	 */
	public ImageFilter(String filterName) {
		super(filterName);
	}		

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {
		VerticalPanel vp = new VerticalPanel();
		rPhoto = new Radio();
		rPhoto.setBoxLabel("Photo");
		rSketch = new Radio();
		rSketch.setBoxLabel("Sketch");
		rMap = new Radio();
		rMap.setBoxLabel("Map");
		ToggleGroup tg = new ToggleGroup();
		tg.add(rPhoto);
		tg.add(rSketch);
		tg.add(rMap);
		rPhoto.setValue(true);
		vp.add(rPhoto);
		vp.add(rSketch);
		vp.add(rMap);
//		vp.setWidth("250px");		
		return vp;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		ArrayList<String> result = new ArrayList<String>();
		if (rPhoto.getValue()) {
			result.add("ImageType='photo'");
		} else if (rSketch.getValue()) {
			result.add("ImageType='sketch'");
		} else if (rMap.getValue()) {
			result.add("ImageType='map'");
		}
		return result;
	}

}
