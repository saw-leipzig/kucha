/*
 * Copyright 2016 - 2019
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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.UserEntry;

@SuppressWarnings("serial")
public class ResourceDownloadServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();

	public ResourceDownloadServlet() {
	}

	/*
	 * Using the parameter <code>thumb=xx</code> where <code>xx</code> is an integer >0 returns a scaled thumbnail
	 * with a max. side length of <code>xx</code>.
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String sessionID = request.getParameter("sessionID");
		if (request.getParameter("imageID") != null) {
			String imageID = request.getParameter("imageID");
			ImageEntry imgEntry = connector.getImageEntry(Integer.parseInt(imageID));
			String filename;
			File inputFile;
			int userAccessLevel = AbstractEntry.ACCESS_LEVEL_PUBLIC;
			ArrayList<Integer> authorizedAccessLevel = new ArrayList<Integer>();
			switch (connector.getAccessLevelForSessionID(sessionID)) {
				case UserEntry.GUEST:
					break;
				case UserEntry.ASSOCIATED:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					System.err.println("acess Level PUBLIC and COPYRIGHT");
					break; 
				case UserEntry.FULL:
				case UserEntry.ADMIN:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PRIVATE);
					System.err.println("acess Level PUBLIC, COPYRIGHT and PRIVATE");
					break;
				default:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					System.err.println("acess Level PUBLIC");
					break;
			}
			System.err.println("sessionID=" + sessionID + ", userAccessLevel=" + connector.getAccessLevelForSessionID(sessionID) + ", ImageEntry accessLevel=" + imgEntry.getAccessLevel());
			
			if (imgEntry!=null && authorizedAccessLevel.contains(imgEntry.getAccessLevel())) {
				filename = imgEntry.getFilename();
//				inputFile = new File(serverProperties.getProperty("home.images"), filename);
			} else if ((connector.getAccessLevelForSessionID(sessionID) == UserEntry.GUEST) && (imgEntry.getAccessLevel() == AbstractEntry.ACCESS_LEVEL_COPYRIGHT)) {
				// guests should be informed that there is an image
				filename = "accessNotPermitted.png";
			} else {
				response.setStatus(403);
				return;
			}
			ServletOutputStream out = response.getOutputStream();
			URL imageURL;
			if (request.getParameter("thumb") != null) {
				int tnSize = Integer.valueOf(request.getParameter("thumb")); // the requested size is given as a parameter
				imageURL = new URL(
						"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/!" + tnSize + "," + tnSize + "/0/default.png"
					);
			} else {
				imageURL = new URL(
						"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/max/0/default.png"
					);
			}
			InputStream in = imageURL.openStream();
			response.setContentType("image/png");
			byte buffer[] = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
			}
			in.close();
			out.close();
		} else if (request.getParameter("background") != null) {
			String filename = request.getParameter("background");
			if (filename.startsWith(".")) {
				response.setStatus(400);
				return;
			} else {
				File inputFile = new File(serverProperties.getProperty("home.backgrounds"), filename);
				if (inputFile.exists()) {
					FileInputStream fis = new FileInputStream(inputFile);
					response.setContentType(filename.toLowerCase().endsWith("png") ? "image/png" : "image/jpeg");
					ServletOutputStream out = response.getOutputStream();
					byte buffer[] = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					out.close();
					fis.close();
				} else {
					response.setStatus(404);
					return;
				}
			}
		} else if (request.getParameter("cavesketch") != null) {
			String filename = request.getParameter("cavesketch");
			if (filename.startsWith(".")) {
				response.setStatus(400);
				return;
			} else {
				File inputFile = new File(serverProperties.getProperty("home.cavesketches"), filename);
				if (inputFile.exists()) {
					FileInputStream fis = new FileInputStream(inputFile);
					response.setContentType(filename.toLowerCase().endsWith("png") ? "image/png" : "image/jpeg");
					ServletOutputStream out = response.getOutputStream();
					byte buffer[] = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					out.close();
					fis.close();
				} else {
					response.setStatus(404);
					return;
				}
			}
		} else if (request.getParameter("document") != null) {
			if (connector.getAccessLevelForSessionID(sessionID) == UserEntry.FULL) {
				String filename = request.getParameter("document");
				if (filename.startsWith(".")) {
					response.setStatus(400);
					return;
				} else {
					File inputFile = new File(serverProperties.getProperty("home.documents"), filename);
					if (inputFile.exists()) {
						FileInputStream fis = new FileInputStream(inputFile);
						response.setContentType(filename.toLowerCase().endsWith("pdf") ? "application/pdf" : "text/html");
						ServletOutputStream out = response.getOutputStream();
						byte buffer[] = new byte[4096];
						int bytesRead = 0;
						while ((bytesRead = fis.read(buffer)) > 0) {
							out.write(buffer, 0, bytesRead);
						}
						out.close();
						fis.close();
					} else {
						response.setStatus(404);
						return;
					}
				}
			} else {
				response.setStatus(403);
				return;
			}
		} else {
			response.setStatus(400);
		}
	}

}
