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
package de.cses.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXFormatter;
import org.jbibtex.Key;
import org.jbibtex.KeyValue;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AnnotatedBiblographyEntry;

/**
 * @author alingnau
 *
 */
@SuppressWarnings("serial")
public class BibTexServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
//	private ServerProperties serverProperties = ServerProperties.getInstance();
	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * 
	 */
	public BibTexServlet() { }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.request = request;
		this.response = response;
		
		Enumeration<String> reqList = request.getParameterNames();
		if (reqList.hasMoreElements()) {
			switch (reqList.nextElement()) {
				case "bibID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getBibTex();
					} else {
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
					}
					break;
										
				default:
					response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					break;
			}
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
		response.getWriter().close();
	}
	
	private void getBibTex() throws IOException {
		String bibIDStr = request.getParameter("bibID");

		response.setContentType("application/txt");
		response.setCharacterEncoding("UTF8");
		PrintWriter out = response.getWriter();

		ArrayList<AnnotatedBiblographyEntry> bibEntries;

		if ("all".equals(bibIDStr)) {
			bibEntries = connector.getAnnotatedBiblography();
		} else {
			bibEntries = connector.getAnnotatedBibliography("BibID IN (" + bibIDStr + ")");
		}
		System.err.println("No. of elements found in AnnotatedBib: " + bibEntries.size());
		
		BibTeXDatabase database = new BibTeXDatabase();
		for (AnnotatedBiblographyEntry be : bibEntries) {
			database.addObject(bibEntryConverter(be));
		}
		System.err.println("No. of references in BibTexDatabase: " + database.getObjects().size());
		BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
		bibtexFormatter.format(database, out);
		out.flush();
		out.close();
	}
	
	private BibTeXEntry bibEntryConverter(AnnotatedBiblographyEntry abe) {
		BibTeXEntry bte;
		switch (abe.getPublicationType().getPublicationTypeID()) {
			case 1: // Book
				bte = new BibTeXEntry(BibTeXEntry.TYPE_BOOK, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_BOOKTITLE, abe.getTitleORG());
				break;
			
			case 3: // PhD 
				bte = new BibTeXEntry(BibTeXEntry.TYPE_PHDTHESIS, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_TITLE, abe.getTitleORG());
				break;
			
			case 4: // Incollection
				bte = new BibTeXEntry(BibTeXEntry.TYPE_INCOLLECTION, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_TITLE, abe.getTitleORG());
				break;
			
			case 5: // Book section
				bte = new BibTeXEntry(BibTeXEntry.TYPE_INBOOK, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_BOOKTITLE, abe.getParentTitleORG());
				checkAndAdd(bte, BibTeXEntry.KEY_CHAPTER, abe.getTitleORG());
				break;
			
			case 7: // Electronic
				bte = new BibTeXEntry(BibTeXEntry.TYPE_MISC, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_TITLE, abe.getTitleORG());
				break;
			
			case 8: // Journal Article
				bte = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_JOURNAL, abe.getParentTitleORG());
				checkAndAdd(bte, BibTeXEntry.KEY_TITLE, abe.getTitleORG());
				break;
			
			default: 
				bte = new BibTeXEntry(BibTeXEntry.TYPE_MISC, new Key(abe.getUniqueID()));
				checkAndAdd(bte, BibTeXEntry.KEY_TITLE, abe.getTitleORG());
				break;
		}
		checkAndAdd(bte, BibTeXEntry.KEY_AUTHOR, abe.getAuthors());
		checkAndAdd(bte, BibTeXEntry.KEY_EDITOR, abe.getEditors());
		checkAndAdd(bte, BibTeXEntry.KEY_MONTH, abe.getMonthORG());
		checkAndAdd(bte, BibTeXEntry.KEY_NUMBER, abe.getNumberORG());
		checkAndAdd(bte, BibTeXEntry.KEY_PAGES, abe.getPagesORG());
		checkAndAdd(bte, BibTeXEntry.KEY_PUBLISHER, abe.getPublisher());
		checkAndAdd(bte, BibTeXEntry.KEY_SERIES, abe.getSeriesORG());
		checkAndAdd(bte, BibTeXEntry.KEY_URL, abe.getUrl());
		checkAndAdd(bte, BibTeXEntry.KEY_VOLUME, abe.getVolumeORG());
		checkAndAdd(bte, BibTeXEntry.KEY_YEAR, abe.getYearORG());
		return bte;
	}
	
	private void checkAndAdd(BibTeXEntry entry, Key bibTexKey, String value) {
		if (value != null && !value.isEmpty()) {
			entry.addField(bibTexKey, new KeyValue(value));
		}
	}
}
