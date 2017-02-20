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

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.CaveTypeEntry;

/**
 * @author alingnau
 *
 */
public class CaveFilter extends AbstractFilter {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryList;
	private ComboBox<CaveTypeEntry> caveTypeSelection;

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();
		LabelProvider<CaveTypeEntry> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}
	
	/**
	 * 
	 */
	public CaveFilter(String filterName) {
		super(filterName);
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		loadCaveTypes();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {
		VerticalPanel vp = new VerticalPanel();
		vp.add(caveTypeSelection);
		return vp;
	}
	
	/**
	 * 
	 */
	private void loadCaveTypes() {
		dbService.getCaveTypes(new AsyncCallback<ArrayList<CaveTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveTypeEntry> result) {
				caveTypeEntryList.clear();
				for (CaveTypeEntry pe : result) {
					caveTypeEntryList.add(pe);
				}
			}
		});
		
		caveTypeSelection = new ComboBox<CaveTypeEntry>(caveTypeEntryList, caveTypeProps.nameEN(),
				new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

					@Override
					public SafeHtml render(CaveTypeEntry item) {
						final CaveTypeViewTemplates ctvTemplates = GWT.create(CaveTypeViewTemplates.class);
						return ctvTemplates.caveTypeLabel(item.getNameEN());
					}
				});
		caveTypeSelection.setEmptyText("select cave type");
		caveTypeSelection.setTypeAhead(false);
		caveTypeSelection.setEditable(false);
		caveTypeSelection.setTriggerAction(TriggerAction.ALL);
		caveTypeSelection.addSelectionHandler(new SelectionHandler<CaveTypeEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<CaveTypeEntry> event) {
				// ToDo
			}
		});
		caveTypeSelection.setWidth(180);
	}	
	
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		ArrayList<String> result = new ArrayList<String>();
		if (caveTypeSelection.getValue() != null) {
			result.add("CaveTypeID=" + caveTypeSelection.getCurrentValue().getCaveTypeID());
		}
		return result;
	}

}
