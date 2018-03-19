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
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StyleEntry;

/**
 * @author alingnau
 *
 */
public class JsonServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
//	private ServerProperties serverProperties = ServerProperties.getInstance();
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
					
				case "iconographyID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getIconography();
					}
					
				case "paintedRepID":
					if (connector.checkSessionID(request.getParameter("sessionID")) != null) {
						getDepiction();
					}
					
				case "caveTypeID":
					getCaveTypes();
					break;
					
				case "styleID":
					getStyles();
					break;
					
				case "expeditionID":
					getExpeditions();
					break;
					
				case "paintedRepFromIconographyID":
					getRelatedDepictionsFromIconography();
					break;
					
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
		String loginStr = request.getParameter("login");
		String passwordStr = request.getParameter("pw");
		String newSessionIDStr = connector.userLogin(loginStr, passwordStr);
		if (newSessionIDStr != null) {
			response.getWriter().write(newSessionIDStr);
		}
		response.setContentType("application/json");
	}
	
	private void getCaves() throws IOException {
		String caveIDStr = request.getParameter("caveID");
		String sqlWhere=null;
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(caveIDStr)) {
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
			if (sqlWhere != null) {
				sqlWhere = sqlWhere.concat(")");
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
			CaveEntry entry = connector.getCave(Integer.parseInt(caveIDStr));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

	private void getSites() throws IOException {
		String siteIDStr = request.getParameter("siteID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(siteIDStr)) {
			ArrayList<SiteEntry> siteEntries = connector.getSites();
			out.println(gs.toJson(siteEntries));
		} else {
			SiteEntry entry = connector.getSite(Integer.parseInt(siteIDStr));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

	private void getRegions() throws IOException {
		String siteIDStr = request.getParameter("regionID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(siteIDStr)) {
			ArrayList<RegionEntry> regionEntries = connector.getRegions();
			out.println(gs.toJson(regionEntries));
		}
		out.close();
	}

	private void getDistricts() throws IOException {
		String districtIDStr = request.getParameter("districtID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(districtIDStr)) {
			ArrayList<DistrictEntry> districtEntries = connector.getDistricts();
			out.println(gs.toJson(districtEntries));
		} else {
			DistrictEntry entry = connector.getDistrict(Integer.parseInt(districtIDStr));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}

	private void getIconography() throws IOException {
		String iconographyIDStr = request.getParameter("iconographyID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(iconographyIDStr)) {
			ArrayList<IconographyEntry> districtEntries = connector.getIconography();
			out.println(gs.toJson(districtEntries));
		} else {
			IconographyEntry entry = connector.getIconographyEntry(Integer.parseInt(iconographyIDStr));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}
	
	private void getDepiction() throws IOException {
		String depictionIDStr = request.getParameter("paintedRepID");
		Gson gs = new Gson();
		String sqlWhere = null;
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(depictionIDStr)) {
			if (request.getParameter("caveID") != null) {
				sqlWhere = "(CaveID=" + Integer.parseInt(request.getParameter("caveID"));
			}
			ArrayList<DepictionEntry> districtEntries = connector.getDepictions(sqlWhere);
			out.println(gs.toJson(districtEntries));
		} else {
			DepictionEntry entry = connector.getDepictionEntry(Integer.parseInt(depictionIDStr));			
			out.println(gs.toJson(entry));
		}
		out.close();
	}
	
	private void getRelatedDepictionsFromIconography() throws IOException {
		int iconographyID = Integer.parseInt(request.getParameter("paintedRepID"));
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if (iconographyID > 0) {
		 ArrayList<DepictionEntry> depictionEntries = connector.getRelatedDepictions(iconographyID);
		 out.println(gs.toJson(depictionEntries));
		}
	}
	
	private void getCaveTypes() throws IOException {
		String caveTypeIDStr = request.getParameter("caveTypeID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(caveTypeIDStr)) {
			ArrayList<CaveTypeEntry> caveTypeEntries = connector.getCaveTypes();
			out.println(gs.toJson(caveTypeEntries));
		} else {
			CaveTypeEntry entry = connector.getCaveTypebyID(Integer.parseInt(caveTypeIDStr));
			out.println(gs.toJson(entry));
		}
		out.close();
	}
	
	private void getStyles() throws IOException {
		String styleIDStr = request.getParameter("styleID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(styleIDStr)) {
			ArrayList<StyleEntry> caveTypeEntries = connector.getStyles();
			out.println(gs.toJson(caveTypeEntries));
		}
		out.close();
	}
	
	private void getExpeditions() throws IOException {
		String expeditionIDStr = request.getParameter("expeditionID");
		Gson gs = new Gson();
		PrintWriter out = response.getWriter();
		response.setContentType("application/json");

		if ("all".equals(expeditionIDStr)) {
			ArrayList<ExpeditionEntry> caveTypeEntries = connector.getExpeditions();
			out.println(gs.toJson(caveTypeEntries));
		}
		out.close();
	}
	
}
