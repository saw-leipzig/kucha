/*
 * Copyright 2017 - 2018
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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.DepictionSearchEntry;
import de.cses.shared.ImageSearchEntry;
import de.cses.shared.ImageTypeEntry;

/**
 * @author alingnau
 *
 */
public class ImageFilter extends AbstractFilter {
	
	protected interface LabelTemplate extends XTemplates {
		@XTemplate("<div class='label'>{text}</div>")
		SafeHtml label(String text);
	}

//	private TextField shortnameSearch;
	private TextField titleSearch;
	private TextField copyrightSearch;
	private TextField commentSearch;
//	private Radio andSearch;
//	private Radio orSearch;
	private ImageTypeProperties imageTypeProps;
	private ListStore<ImageTypeEntry> imageTypeEntryList, selectedImagesTypesList;
	private NumberField<Integer> daysSinceUploadSearch;
	private ArrayList<Integer> ImgIDs = new ArrayList<Integer>();
	private ArrayList<Integer> caveIDs = new ArrayList<Integer>();
	private ArrayList<Integer> BibIDs = new ArrayList<Integer>();

	interface ImageTypeProperties extends PropertyAccess<ImageTypeEntry> {
		ModelKeyProvider<ImageTypeEntry> imageTypeID();
		LabelProvider<ImageTypeEntry> uniqueID();
		ValueProvider<ImageTypeEntry, String> name();
	}

	interface ImageTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml imageTypeLabel(String name);
	}

	/**
	 * @param filterName
	 */
	public ImageFilter(String filterName) {
		super(filterName);
		imageTypeProps = GWT.create(ImageTypeProperties.class);
		imageTypeEntryList = new ListStore<ImageTypeEntry>(imageTypeProps.imageTypeID());
		selectedImagesTypesList = new ListStore<ImageTypeEntry>(imageTypeProps.imageTypeID());
		for (ImageTypeEntry ite : StaticTables.getInstance().getImageTypeEntries().values()) {
			imageTypeEntryList.add(ite);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		LabelTemplate lt = GWT.create(LabelTemplate.class); 

		titleSearch = new TextField();
		titleSearch.setEmptyText("search title / shortname");
		titleSearch.setToolTip(Util.createToolTip("search in title or shortname", "Search if the title or shortname contains this sequence of characters."));
		titleSearch.addDomHandler(getShortkey(), KeyPressEvent.getType());
		vlc.add(titleSearch, new VerticalLayoutData(1.0, .125));
		
		commentSearch = new TextField();
		commentSearch.setEmptyText("search comment");
		commentSearch.setToolTip(Util.createToolTip("search in comments", "Search if the comments contain this sequence of characters."));
		commentSearch.addDomHandler(getShortkey(), KeyPressEvent.getType());
		vlc.add(commentSearch, new VerticalLayoutData(1.0, .125));

		copyrightSearch = new TextField();
		copyrightSearch.setEmptyText("search copyright");
		copyrightSearch.setToolTip(Util.createToolTip("search in copyright", "Search if the copyright contains this sequence of characters."));
		copyrightSearch.addDomHandler(getShortkey(), KeyPressEvent.getType());
		vlc.add(copyrightSearch, new VerticalLayoutData(1.0, .125));
		
		daysSinceUploadSearch = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		daysSinceUploadSearch.setAllowNegative(false);
		daysSinceUploadSearch.setEmptyText("last X days");
		daysSinceUploadSearch.setToolTip(Util.createToolTip("Search in last X days.", "Searches for images uploaded in the last X days."));
		daysSinceUploadSearch.addDomHandler(getShortkey(), KeyPressEvent.getType());
		daysSinceUploadSearch.addDomHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
	        	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	        		  invokeSearch();
	        	  }
			}
		}, KeyDownEvent.getType());
		HorizontalLayoutContainer uploadedSinceHLC = new HorizontalLayoutContainer();
		uploadedSinceHLC.add(daysSinceUploadSearch, new HorizontalLayoutData(.7, 1.0, new Margins(0, 10, 0, 0)));
		uploadedSinceHLC.add(new HTML(lt.label("days")), new HorizontalLayoutData(.3, 1.0));
		vlc.add(uploadedSinceHLC, new VerticalLayoutData(1.0, .125));

		DualListField<ImageTypeEntry, String> dualListField = new DualListField<ImageTypeEntry, String>(imageTypeEntryList,
				selectedImagesTypesList, imageTypeProps.name(), new TextCell());
		dualListField.setEnableDnd(true);
		dualListField.addDomHandler(new KeyDownHandler() {
			public void onKeyDown(KeyDownEvent event) {
	        	  if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
	        		  invokeSearch();
	        	  }
			}
		}, KeyDownEvent.getType());
		dualListField.getDownButton().removeFromParent();
		dualListField.getUpButton().removeFromParent();
		dualListField.setMode(DualListField.Mode.INSERT);

		vlc.add(dualListField, new VerticalLayoutData(1.0, .50));
		vlc.addDomHandler(getShortkey(), KeyPressEvent.getType());

		vlc.setHeight("300px");
		return vlc;
	}
	@Override
	public void clear() {
		titleSearch.clear();;
		copyrightSearch.clear();;
		commentSearch.clear();
		daysSinceUploadSearch.clear();
		ImgIDs.clear();
		caveIDs.clear();
		BibIDs.clear();

	}
	@Override
	public void setSearchEntry(AbstractSearchEntry searchEntry, boolean reset) {
		if (reset) {
			clear();			
		}
		if (((ImageSearchEntry)searchEntry).getImageIdList()!= null && !((ImageSearchEntry)searchEntry).getImageIdList().isEmpty()) {
			for (int img :((ImageSearchEntry)searchEntry).getImageIdList()) {
				ImgIDs.add(img);
			}
		}
		if (((ImageSearchEntry)searchEntry).getTitleSearch()!= null && !((ImageSearchEntry)searchEntry).getTitleSearch().isEmpty()) {
			titleSearch.setValue(((ImageSearchEntry)searchEntry).getTitleSearch());
		}
		if (((ImageSearchEntry)searchEntry).getCaveIdList()!= null && !((ImageSearchEntry)searchEntry).getCaveIdList().isEmpty()) {
			for (int img :((ImageSearchEntry)searchEntry).getCaveIdList()) {
				caveIDs.add(img);
			}
		}
		if (((ImageSearchEntry)searchEntry).getCaveIdList()!= null && !((ImageSearchEntry)searchEntry).getCaveIdList().isEmpty()) {
			for (int img :((ImageSearchEntry)searchEntry).getCaveIdList()) {
				caveIDs.add(img);
			}
		}
		if (((ImageSearchEntry)searchEntry).getBibIdList()!= null && !((ImageSearchEntry)searchEntry).getBibIdList().isEmpty()) {
			for (int img :((ImageSearchEntry)searchEntry).getBibIdList()) {
				BibIDs.add(img);
			}
		}
				

	}

	@Override
	public AbstractSearchEntry getSearchEntry() {
		ImageSearchEntry searchEntry;
		if (UserLogin.isLoggedIn()) {
			searchEntry = new ImageSearchEntry(UserLogin.getInstance().getSessionID());
		} else {
			searchEntry = new ImageSearchEntry();
		}

		if (titleSearch.getValue() != null && !titleSearch.getValue().isEmpty()) {
			searchEntry.setTitleSearch(titleSearch.getValue());
		}
		
		if (copyrightSearch.getValue() != null && !copyrightSearch.getValue().isEmpty()) {
			searchEntry.setCopyrightSearch(copyrightSearch.getValue());
		}
		if ( !ImgIDs.isEmpty()) {
			searchEntry.setImageIdList(ImgIDs);
		}
		
		if (commentSearch.getValue() != null && !commentSearch.getValue().isEmpty()) {
			searchEntry.setCommentSearch(commentSearch.getValue());
		}
		
		if (daysSinceUploadSearch.getValue() != null && daysSinceUploadSearch.getValue() > 0) {
			searchEntry.setDaysSinceUploadSearch(daysSinceUploadSearch.getValue());
		}
		
		for (ImageTypeEntry ite : selectedImagesTypesList.getAll()) {
			searchEntry.getImageTypeIdList().add(ite.getImageTypeID());
		}

		return searchEntry;
	}

}
