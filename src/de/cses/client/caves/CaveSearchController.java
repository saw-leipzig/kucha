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
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

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

	/* (non-Javadoc)
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
				RegionEntry re = stab.getRegionEntries().get(ce.getRegionID());
				SiteEntry se = stab.getSiteEntries().get(re.getSiteID());
				switch (ce.getOfficialNumber().length()) {
					case 1:
						return se.getShortName() + "  " + ce.getOfficialNumber();
					case 2:
						return se.getShortName() + " " + ce.getOfficialNumber();
					default:
						return se.getShortName() + ce.getOfficialNumber();
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

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		PopupPanel caveEditorPanel = new PopupPanel(false);
		CaveEditor ced = new CaveEditor(null);
		ced.addEditorListener(new EditorListener() {
			
			@Override
			public void closeRequest() {
				caveEditorPanel.hide();
			}

			@Override
			public void updateEntryRequest(AbstractEntry updatedEntry) {
			}
		});
		caveEditorPanel.add(ced);
		caveEditorPanel.setGlassEnabled(true);
		caveEditorPanel.center();
	}

}
