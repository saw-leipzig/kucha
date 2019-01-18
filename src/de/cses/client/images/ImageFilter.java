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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
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
	private TextField filenameSearch;
//	private Radio andSearch;
//	private Radio orSearch;
	private ImageTypeProperties imageTypeProps;
	private ListStore<ImageTypeEntry> imageTypeEntryList, selectedImagesTypesList;
	private NumberField<Integer> daysSinceUploadSearch;

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
		vlc.add(titleSearch, new VerticalLayoutData(1.0, .125));
		
		filenameSearch = new TextField();
		filenameSearch.setEmptyText("search filename");
		filenameSearch.setToolTip(Util.createToolTip("search in filename", "Search if the filename contains this sequence of characters."));
		vlc.add(filenameSearch, new VerticalLayoutData(1.0, .125));

		copyrightSearch = new TextField();
		copyrightSearch.setEmptyText("search copyright");
		copyrightSearch.setToolTip(Util.createToolTip("search in copyright", "Search if the copyright contains this sequence of characters."));
		vlc.add(copyrightSearch, new VerticalLayoutData(1.0, .125));
		
		daysSinceUploadSearch = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		daysSinceUploadSearch.setAllowNegative(false);
		daysSinceUploadSearch.setEmptyText("last X days");
		daysSinceUploadSearch.setToolTip(Util.createToolTip("Search in last X days.", "Searches for images uploaded in the last X days."));
		HorizontalLayoutContainer uploadedSinceHLC = new HorizontalLayoutContainer();
		uploadedSinceHLC.add(daysSinceUploadSearch, new HorizontalLayoutData(.7, 1.0, new Margins(0, 10, 0, 0)));
		uploadedSinceHLC.add(new HTML(lt.label("days")), new HorizontalLayoutData(.3, 1.0));
		vlc.add(uploadedSinceHLC, new VerticalLayoutData(1.0, .125));

		DualListField<ImageTypeEntry, String> dualListField = new DualListField<ImageTypeEntry, String>(imageTypeEntryList,
				selectedImagesTypesList, imageTypeProps.name(), new TextCell());
		dualListField.setEnableDnd(true);
		dualListField.getDownButton().removeFromParent();
		dualListField.getUpButton().removeFromParent();
		dualListField.setMode(DualListField.Mode.INSERT);

		vlc.add(dualListField, new VerticalLayoutData(1.0, .50));

		vlc.setHeight("300px");
		return vlc;
	}

	@Override
	public AbstractSearchEntry getSearchEntry() {
		ImageSearchEntry entry = new ImageSearchEntry(Cookies.getCookie(UserLogin.SESSION_ID), Cookies.getCookie(UserLogin.USERNAME));

		if (titleSearch.getValue() != null && !titleSearch.getValue().isEmpty()) {
			entry.setTitleSearch(titleSearch.getValue());
		}
		
		if (copyrightSearch.getValue() != null && !copyrightSearch.getValue().isEmpty()) {
			entry.setCopyrightSearch(copyrightSearch.getValue());
		}
		
		if (filenameSearch.getValue() != null && !filenameSearch.getValue().isEmpty()) {
			entry.setFilenameSearch(filenameSearch.getValue());
		}
		
		if (daysSinceUploadSearch.getValue() != null && daysSinceUploadSearch.getValue() > 0) {
			entry.setDaysSinceUploadSearch(daysSinceUploadSearch.getValue());
		}
		
		for (ImageTypeEntry ite : selectedImagesTypesList.getAll()) {
			entry.getImageTypeIdList().add(ite.getImageTypeID());
		}

		return entry;
	}

}
