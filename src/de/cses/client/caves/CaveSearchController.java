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
import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.images.ImageView;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public class CaveSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param caveResultView
	 */
	public CaveSearchController(String selectorTitle, CaveResultView caveResultView) {
		super(selectorTitle, caveResultView);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ArrayList<String> sqlWhereClauses = new ArrayList<String>();
		for (AbstractFilter filter : getRelatedFilter()) {
			if (filter != null) {
				sqlWhereClauses.addAll(filter.getSqlWhereClause());
			}
		}
		String sqlWhere = null;
		for (int i=0; i<sqlWhereClauses.size(); ++i) {
			if (i == 0) {
				sqlWhere = sqlWhereClauses.get(i);
			} else {
				sqlWhere = sqlWhere + " AND " + sqlWhereClauses.get(i);
			}
		}
		dbService.getCaves(sqlWhere, new AsyncCallback<ArrayList<CaveEntry>>() {
			
			private String getComparisonLabel(CaveEntry ce) {
				StaticTables stab = StaticTables.getInstance();
				String shortName = stab.getSiteEntries().get(ce.getSiteID()).getShortName();
				int len = 0;
				while ((len < ce.getOfficialNumber().length()) && isInteger(ce.getOfficialNumber().substring(0, len+1))) {
					++len;
				}
				switch (len) {
					case 1:
						return shortName + "  " + ce.getOfficialNumber();
					case 2:
						return shortName + " " + ce.getOfficialNumber();
					default:
						return shortName + ce.getOfficialNumber();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				result.sort(new Comparator<CaveEntry>() {

					@Override
					public int compare(CaveEntry ce1, CaveEntry ce2) {
						return getComparisonLabel(ce1).compareTo(getComparisonLabel(ce2));
					}
				});
				getResultView().reset();
				for (CaveEntry ce : result) {
					getResultView().addResult(new CaveView(ce));	
				}
			}
		});
	}

	private boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		PopupPanel caveEditorPanel = new PopupPanel(false);
		CaveEditor ced = new CaveEditor(null);
		ced.addEditorListener(new EditorListener() {

			@Override
			public void closeRequest(AbstractEntry entry) {
				caveEditorPanel.hide();
				getResultView().addResult(new CaveView((CaveEntry)entry));
			}

			@Override
			public void updateEntryRequest(AbstractEntry updatedEntry) { }
		});
		caveEditorPanel.add(ced);
		caveEditorPanel.setGlassEnabled(true);
		caveEditorPanel.center();
	}

}
