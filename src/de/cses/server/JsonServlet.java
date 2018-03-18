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

import com.google.gson.Gson;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class JsonServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();
	private HttpServletRequest request;
	private HttpServletResponse response;

	/**
	 * 
	 */
	public JsonServlet() { }
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		this.request = request;
		this.response = response;
		
		Enumeration<String> reqList = request.getParameterNames();
		if (reqList.hasMoreElements()) {
			switch (reqList.nextElement()) {
				case "login":
					login();
					break;
					
				case "caveID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getCaves();
					}
					break;
					
				case "siteID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getSites();
					}
					
				case "regionID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getRegions();
					}
					
				case "districtID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getDistricts();
					}
					
				default:
					break;
			}
		} else {
			response.setStatus(404);
			response.getWriter().close();
		}
		response.getWriter().close();
	}
	
	private void login() throws IOException {
		String login = request.getParameter("login");
		String password = request.getParameter("pw");
		String newSessionID = connector.userLogin(login, password);
		if (newSessionID != null) {
			response.getWriter().write(newSessionID);
		}
		response.setContentType("application/json");
	}
	
	private void getCaves() throws IOException {
		String caveID = request.getParameter("caveID");
		String sqlWhere=null;
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(caveID)) {
			if (request.getParameter("siteID") != null) {
				sqlWhere = "(SiteID=" + Integer.parseInt(request.getParameter("siteID"));
			}
			if (request.getParameter("districtID") != null) {
				if (sqlWhere != null) {
					sqlWhere = sqlWhere.concat("OR DistrictID=" + Integer.parseInt(request.getParameter("districtID")));
				} else {
					sqlWhere = "(DistrictID=" + Integer.parseInt(request.getParameter("districtID"));
				}
			}
			if (request.getParameter("regionID") != null) {
				if (sqlWhere != null) {
					sqlWhere = sqlWhere.concat("OR RegionID=" + Integer.parseInt(request.getParameter("regionID")));
				} else {
					sqlWhere = "(RegionID=" + Integer.parseInt(request.getParameter("regionID"));
				}
			}
			if (request.getParameter("caveTypeID") != null) {
				if (sqlWhere != null) {
					sqlWhere = sqlWhere.concat(") AND CaveTypeID=" + Integer.parseInt(request.getParameter("caveTypeID")));
				} else {
					sqlWhere = "CaveTypeID=" + Integer.parseInt(request.getParameter("caveTypeID"));
				}
			}
			ArrayList<CaveEntry> caveEntries = connector.getCaves(sqlWhere);
			out.println(gs.toJson(caveEntries));
		} else {
			CaveEntry entry = connector.getCave(Integer.parseInt(caveID));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

	private void getSites() throws IOException {
		String siteID = request.getParameter("siteID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(siteID)) {
			ArrayList<SiteEntry> siteEntries = connector.getSites();
			out.println(gs.toJson(siteEntries));
		} else {
			SiteEntry entry = connector.getSite(Integer.parseInt(siteID));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

	private void getRegions() throws IOException {
		String siteID = request.getParameter("regionID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(siteID)) {
			ArrayList<RegionEntry> regionEntries = connector.getRegions();
			out.println(gs.toJson(regionEntries));
		}
		out.close();
	}

	private void getDistricts() throws IOException {
		String districtID = request.getParameter("districtID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(districtID)) {
			ArrayList<DistrictEntry> districtEntries = connector.getDistricts();
			out.println(gs.toJson(districtEntries));
		} else {
			DistrictEntry entry = connector.getDistrict(Integer.parseInt(districtID));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

}
