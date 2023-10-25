/*
 * Copyright 2018 
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
package de.cses.client.bibliography;
import java.util.ArrayList;

import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.AnnotatedBibliographySearchEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamenticSearchEntry;
import de.cses.shared.PositionEntry;

/**
 * @author alingnau
 *
 */
public class AnnotatedBibliographyFilter extends AbstractFilter {
	
	
	private TextField IDSearchTF;
	public TextField authorNameTF;
	public TextField titleTF;
	private TextField publisherTF;
	private NumberField<Integer> yearSearch;
	private ArrayList<Integer> bibIDs = new ArrayList<Integer>();

	/**
	 * @param filterName
	 */
	public AnnotatedBibliographyFilter(String filterName) {
		super(filterName);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	

	@Override
	protected Widget getFilterUI() {
		IDSearchTF = new TextField();
		IDSearchTF.addValidator(new RegExValidator("[0-9]", "Only numbers allowed"));
		IDSearchTF.addInvalidHandler(new InvalidHandler() {
		      @Override
		      public void onInvalid(InvalidEvent event) {
		    	  Info.display("Search field for Ids deleted!", "Due to invalid Input.");
		    	  IDSearchTF.clear();
		      }
		    });
		IDSearchTF.setEmptyText("search ID");
		IDSearchTF.addDomHandler(getShortkey() , KeyPressEvent.getType());

		authorNameTF = new TextField();
		authorNameTF.setEmptyText("search author / editor / institute");
		authorNameTF.setToolTip(Util.createToolTip("searches all authors / editors / institutes"));
		authorNameTF.addKeyPressHandler(getShortkey());
		
		titleTF = new TextField();
		titleTF.setEmptyText("search title (orig./eng./trans.)");
		titleTF.setToolTip(Util.createToolTip("Searches in all sorts of titles.", "Includes Parent Title, Subtitle, Collection Title, Titleaddon, Book Title, etc."));
		titleTF.addKeyPressHandler(getShortkey());
		
		publisherTF = new TextField();
		publisherTF.setEmptyText("search publisher & address");
		publisherTF.setToolTip(Util.createToolTip("Search for publishers & addresses.", "Please note: publisher is a free text field. Due to diffent spelling or typos, searing can become difficult."));
		publisherTF.addKeyPressHandler(getShortkey());
		
		yearSearch = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		yearSearch.setAllowNegative(false);
		yearSearch.setEmptyText("year");
		yearSearch.setToolTip(Util.createToolTip("search year", "will be extended into searching for range of years in next version"));
		yearSearch.addKeyPressHandler( new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
	        	  if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        		  yearSearch.finishEditing();
	        		  invokeSearch();
	        	  }
			}
		});

		VerticalLayoutContainer bibFilterVLC = new VerticalLayoutContainer();
		bibFilterVLC.add(IDSearchTF , new VerticalLayoutData(1.0, .2));
		bibFilterVLC.add(authorNameTF, new VerticalLayoutData(1.0, .2));
		bibFilterVLC.add(titleTF, new VerticalLayoutData(1.0, .2));
		bibFilterVLC.add(publisherTF, new VerticalLayoutData(1.0, .2));
		bibFilterVLC.add(yearSearch, new VerticalLayoutData(1.0, .2));
		bibFilterVLC.setHeight("120px");
		return bibFilterVLC;
	}
	@Override
	public void clear() {
		IDSearchTF.reset();
		authorNameTF.clear();
		titleTF.clear();
		publisherTF.clear();
		yearSearch.clear();
		bibIDs.clear();
	}
	@Override
	public void setSearchEntry(AbstractSearchEntry searchEntry, boolean reset) {
		// Versenden der Eintraege an den Server nach erfolgter Suche
		if (reset) {
			clear();
		}
		if (((AnnotatedBibliographySearchEntry)searchEntry).getID() > 0) {
			IDSearchTF.setValue(Integer.toString(((DepictionSearchEntry)searchEntry).getID()));
		}

		if (((AnnotatedBibliographySearchEntry)searchEntry).getBibIdList()!=null) {
			if (!((AnnotatedBibliographySearchEntry)searchEntry).getBibIdList().isEmpty()) {
				for (Integer bibID : ((AnnotatedBibliographySearchEntry)searchEntry).getBibIdList()) {
					bibIDs.add(bibID);
				}
			}
		}

	}
		
	@Override
	public AbstractSearchEntry getSearchEntry() {
		AnnotatedBibliographySearchEntry searchEntry;
		if (UserLogin.isLoggedIn()) {
			searchEntry = new AnnotatedBibliographySearchEntry(UserLogin.getInstance().getSessionID());
		} else {
			searchEntry = new AnnotatedBibliographySearchEntry();
		}
		try {
			if (IDSearchTF.getValue() != null ) {			
				searchEntry.setID(Integer.parseInt(IDSearchTF.getValue()));
			}
		} catch (Exception e) {
			Info.display("Error", "Only numeric IDs are valid");
			return null;
		}		
		if (authorNameTF.getValue() != null && !authorNameTF.getValue().isEmpty()) {
			searchEntry.setAuthorSearch(authorNameTF.getValue());
		}
		
		if (titleTF.getValue() != null && !titleTF.getValue().isEmpty()) {
			searchEntry.setTitleSearch(titleTF.getValue());
		}
		
		if (publisherTF.getValue() != null && !publisherTF.getValue().isEmpty()) {
			searchEntry.setPublisherSearch(publisherTF.getValue());
		}
		
		if (yearSearch.getValue() != null && yearSearch.getValue() > 0) {
			searchEntry.setYearSearch(yearSearch.getValue());
		}
		for (Integer bibID :bibIDs) {
			searchEntry.getBibIdList().add(bibID);
		}
		
		return searchEntry;
	}

}
