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
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.ImageSearchEntry;
import de.cses.shared.ImageTypeEntry;

/**
 * @author alingnau
 *
 */
public class ImageFilter extends AbstractFilter {

//	private TextField shortnameSearch;
	private TextField titleSearch;
	private TextField copyrightSearch;
//	private Radio andSearch;
//	private Radio orSearch;
	private ImageTypeProperties imageTypeProps;
	private ListStore<ImageTypeEntry> imageTypeEntryList, selectedImagesTypesList;

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

		// TODO 
		titleSearch = new TextField();
//		titleSearch.addValidator(new RegExValidator("^[a-zA-Z0-9 _\\-]*$", "We are working on a new search interface. Currently only a-z, A-Z, 0-9, _, - and [SPACE] are allowed."));
//		titleSearch.setAutoValidate(true);
//		titleSearch.setValue("");
		titleSearch.setEmptyText("search title / shortname");
		vlc.add(titleSearch, new VerticalLayoutData(1.0, .15));

//		shortnameSearch = new TextField();
//		shortnameSearch.addValidator(new RegExValidator("^[a-zA-Z0-9 _\\-]*$", "We are working on a new search interface. Currently only a-z, A-Z, 0-9, _, - and [SPACE] are allowed."));
//		shortnameSearch.setAutoValidate(true);
//		shortnameSearch.setValue("");
//		shortnameSearch.setEmptyText("search image short name");
//		vlc.add(shortnameSearch, new VerticalLayoutData(1.0, .125));

		copyrightSearch = new TextField();
//		copyrightSearch.addValidator(new RegExValidator("^[a-zA-Z0-9 _\\-]*$", "We are working on a new search interface. Currently only a-z, A-Z, 0-9, _, - and [SPACE] are allowed."));
//		copyrightSearch.setAutoValidate(true);
//		copyrightSearch.setValue("");
		copyrightSearch.setEmptyText("search copyright");
		vlc.add(copyrightSearch, new VerticalLayoutData(1.0, .15));

//		HorizontalPanel searchTypeHP = new HorizontalPanel();
//		andSearch = new Radio();
//		andSearch.setBoxLabel("AND");
//		orSearch = new Radio();
//		orSearch.setBoxLabel("OR");
//		ToggleGroup tg = new ToggleGroup();
//		tg.add(andSearch);
//		tg.add(orSearch);
//		andSearch.setValue(true);
//		searchTypeHP.add(andSearch);
//		searchTypeHP.add(orSearch);
//		vlc.add(searchTypeHP, new VerticalLayoutData(1.0, .125));

		DualListField<ImageTypeEntry, String> dualListField = new DualListField<ImageTypeEntry, String>(imageTypeEntryList,
				selectedImagesTypesList, imageTypeProps.name(), new TextCell());
		dualListField.setEnableDnd(true);
		dualListField.getDownButton().removeFromParent();
		dualListField.getUpButton().removeFromParent();
		dualListField.setMode(DualListField.Mode.INSERT);

		vlc.add(dualListField, new VerticalLayoutData(1.0, .70));

		vlc.setHeight("300px");
		return vlc;
	}

	@Override
	public AbstractSearchEntry getSearchEntry() {
		ImageSearchEntry entry = new ImageSearchEntry();
		
		if (titleSearch.getValue() != null && !titleSearch.getValue().isEmpty()) {
			entry.setTitleSearch(titleSearch.getValue());
		}
		
		if (copyrightSearch.getValue() != null && !copyrightSearch.getValue().isEmpty()) {
			entry.setCopyrightSearch(copyrightSearch.getValue());
		}
		
		for (ImageTypeEntry ite : selectedImagesTypesList.getAll()) {
			entry.getImageTypeIdList().add(ite.getImageTypeID());
		}

		return entry;
	}

}
