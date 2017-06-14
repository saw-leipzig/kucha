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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.shared.DepictionEntry;

/**
 * @author alingnau
 *
 */
public class DepictionSearchController extends AbstractSearchController {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PopupPanel depictionEditorPanel;

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public DepictionSearchController(String selectorTitle, AbstractResultView resultView) {
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
		dbService.getDepictions(sqlWhere, new AsyncCallback<ArrayList<DepictionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DepictionEntry> result) {
				getResultView().reset();
				for (DepictionEntry de : result) {
					getResultView().addResult(new DepictionView(de));
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		depictionEditorPanel = new PopupPanel(false);
		DepictionEditor de = new DepictionEditor(null);
		de.addEditorListener(this);
		depictionEditorPanel.add(de);
		depictionEditorPanel.setGlassEnabled(true);
		depictionEditorPanel.center();
		depictionEditorPanel.show();
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.EditorListener#closeRequest()
	 */
	@Override
	public void closeRequest() {
		depictionEditorPanel.hide();
	}

}
