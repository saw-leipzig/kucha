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

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.ui.AbstractFilter;

/**
 * @author alingnau
 *
 */
public class ImageFilter extends AbstractFilter {
	
	private Radio rPhoto;
	private Radio rSketch;
	private Radio rMap;
	private TextField shortnameSearch;
	private TextField titleSearch;
	private TextField copyrightSearch;
	private Radio andSearch;
	private Radio orSearch;

	public static final int PHOTO = 1;
	public static final int SKETCH = 2;
	public static final int MAP = 3;

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
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		
		titleSearch = new TextField();
		titleSearch.setValue("");
		titleSearch.setEmptyText("search image title");
		vlc.add(titleSearch, new VerticalLayoutData(1.0, 0.15));
		
		shortnameSearch = new TextField();
		shortnameSearch.setValue("");
		shortnameSearch.setEmptyText("search image short name");
		vlc.add(shortnameSearch, new VerticalLayoutData(1.0, .15));
		
		copyrightSearch = new TextField();
		copyrightSearch.setValue("");
		copyrightSearch.setEmptyText("search image copyright");
		vlc.add(copyrightSearch, new VerticalLayoutData(1.0, 0.15));
		
		HorizontalPanel searchTypeHP = new HorizontalPanel();
		andSearch = new Radio();
		andSearch.setBoxLabel("AND");
		orSearch = new Radio();
		orSearch.setBoxLabel("OR");
 		ToggleGroup tg = new ToggleGroup();
		tg.add(andSearch);
		tg.add(orSearch);
		andSearch.setValue(true);
		searchTypeHP.add(andSearch);
		searchTypeHP.add(orSearch);
		vlc.add(searchTypeHP, new VerticalLayoutData(1.0, .15));
		
		VerticalPanel imageTypeVP = new VerticalPanel();
		rPhoto = new Radio();
		rPhoto.setBoxLabel("Photo");
		rSketch = new Radio();
		rSketch.setBoxLabel("Sketch");
		rMap = new Radio();
		rMap.setBoxLabel("Map");
		tg = new ToggleGroup();
		tg.add(rPhoto);
		tg.add(rSketch);
		tg.add(rMap);
		rPhoto.setValue(true);
		imageTypeVP.add(rPhoto);
		imageTypeVP.add(rSketch);
		imageTypeVP.add(rMap);
		vlc.add(imageTypeVP, new VerticalLayoutData(1.0, .40));
		
		vlc.setHeight("200px");
		return vlc;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		String textFieldQuery = "";
		if (!titleSearch.getValue().isEmpty()) {
			textFieldQuery = "Title LIKE '%" + titleSearch.getValue() + "%'";
		}
		if (!shortnameSearch.getValue().isEmpty()) {
			textFieldQuery = textFieldQuery.concat((!textFieldQuery.isEmpty() ? (andSearch.getValue() ? " AND " : " OR ") : "") + "ShortName LIKE '%" + shortnameSearch.getValue() + "%'");
		}
		if (!copyrightSearch.getValue().isEmpty()) {
			textFieldQuery = textFieldQuery.concat((!textFieldQuery.isEmpty() ? (andSearch.getValue() ? " AND " : " OR ") : "") + "Copyright LIKE '%" + copyrightSearch.getValue() + "%'");
		}
		ArrayList<String> result = new ArrayList<String>();
		if (!textFieldQuery.isEmpty()) {
			result.add("(" + textFieldQuery + ")");
		}
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
