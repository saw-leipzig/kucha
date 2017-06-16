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
package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.shared.OrnamentEntry;

/**
 * @author nina
 *
 */
public class OrnamenticSearchController extends AbstractSearchController {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public OrnamenticSearchController(String selectorTitle, AbstractResultView resultView) {
		super(selectorTitle, resultView);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ArrayList<String> sqlWhereClauses = new ArrayList<String>();
		for (AbstractFilter filter : getRelatedFilter()) {
			if ((filter != null) && (filter.getSqlWhereClause() != null)) {
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
		dbService.getOrnamentsWHERE(sqlWhere, new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				getResultView().reset();
				for (OrnamentEntry de : result) {
					getResultView().addResult(new OrnamenticView(de));
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		PopupPanel ornamentEditorPanel = new PopupPanel(false);
		Ornamentic ornamentic = new Ornamentic();
		ornamentic.setPopup(ornamentEditorPanel);
		ornamentEditorPanel.add(ornamentic);
		ornamentEditorPanel.setGlassEnabled(true);
		ornamentEditorPanel.center();
	}

}
