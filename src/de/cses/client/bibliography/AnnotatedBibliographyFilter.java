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

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.ui.AbstractFilter;

/**
 * @author alingnau
 *
 */
public class AnnotatedBibliographyFilter extends AbstractFilter {
	
	public TextField authorNameTF;
	public TextField titleTF;

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
		authorNameTF = new TextField();
		authorNameTF.setEmptyText("search in author or institute");
		titleTF = new TextField();
		titleTF.setEmptyText("search in title (orig./eng./trans.)");
		VerticalLayoutContainer bibFilterVLC = new VerticalLayoutContainer();
		bibFilterVLC.add(authorNameTF, new VerticalLayoutData(1.0, .5));
		bibFilterVLC.add(titleTF, new VerticalLayoutData(1.0, .5));
		bibFilterVLC.setHeight("120px");
		return bibFilterVLC;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getSqlWhereClause()
	 */
	@Override
	public ArrayList<String> getSqlWhereClause() {
		ArrayList<String> result = new ArrayList<String>();
		if ((authorNameTF.getValue() != null) && !authorNameTF.getValue().isEmpty()) {
			String searchTerm = authorNameTF.getValue();
			String sqlTerm = "";
			for (String name : searchTerm.split("\\s+")) {
				sqlTerm += sqlTerm.length() > 0 ? " INTERSECT SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE ((FirstName LIKE '%" + name + "%') OR (LastName LIKE '%" + name + "%') OR (Institution LIKE '%" + name + "%'))))" 
						: "SELECT BibID FROM AuthorBibliographyRelation WHERE (AuthorID IN (SELECT DISTINCT AuthorID FROM Authors WHERE ((FirstName LIKE '%" + name + "%') OR (LastName LIKE '%" + name + "%') OR (Institution LIKE '%" + name + "%'))))";
			}
			sqlTerm = "BibID IN (" + sqlTerm + ")";
			result.add(sqlTerm);
		}
		if ((titleTF.getValue() != null) && !titleTF.getValue().isEmpty()) {
			String searchTerm = titleTF.getValue();
			result.add("("
					+ "(TitleORG LIKE '%" + searchTerm + "%')"
					+ "OR (TitleEN LIKE '%" + searchTerm + "%')"
					+ "OR (TitleTR LIKE '%" + searchTerm + "%')"
							+ ")");
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#showExtendedFilterView()
	 */
	@Override
	protected void showExtendedFilterView() {
		// TODO Auto-generated method stub
		
	}

}
