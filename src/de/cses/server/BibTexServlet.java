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
		BibTeXEntry bte;
		for (AnnotatedBiblographyEntry be : bibEntries) {
			bte = new BibTeXEntry(BibTeXEntry.TYPE_ARTICLE, new Key(be.getUniqueID()));
			checkAndAdd(bte, BibTeXEntry.KEY_AUTHOR, be.getAuthors());
			checkAndAdd(bte, BibTeXEntry.KEY_TITLE, be.getTitleORG());
			checkAndAdd(bte, BibTeXEntry.KEY_YEAR, be.getYearORG());
			checkAndAdd(bte, BibTeXEntry.KEY_PAGES, be.getPagesORG());
			checkAndAdd(bte, BibTeXEntry.KEY_EDITOR, be.getEditors());
			database.addObject(bte);
		}
		System.err.println("No. of references in BibTexDatabase: " + database.getObjects().size());
		BibTeXFormatter bibtexFormatter = new BibTeXFormatter();
		bibtexFormatter.format(database, out);
		out.flush();
		out.close();
	}
	
	private void checkAndAdd(BibTeXEntry entry, Key bibTexKey, String value) {
		if (value != null && !value.isEmpty()) {
			entry.addField(bibTexKey, new KeyValue(value));
		}
	}
}
