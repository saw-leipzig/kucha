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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.CaveEntry;

/**
 * @author alingnau
 *
 */
public class JsonServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();

	/**
	 * 
	 */
	public JsonServlet() { }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("request").equals("Caves") && (request.getParameter("caveID") != null)) {
			int caveID = Integer.parseInt(request.getParameter("caveID"));
			if (caveID > 0) {
				CaveEntry caveEntry = connector.getCave(caveID);
				response.setContentType("application/json");
				Gson gs = new Gson();
				PrintWriter out = response.getWriter();
				out.println(gs.toJson(caveEntry));
				out.close();
			}
		} else {
			response.setStatus(404);
		}
	}

}
