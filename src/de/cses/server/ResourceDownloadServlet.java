/*
 * Copyright 2016 
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

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.cses.server.mysql.MysqlConnector;

@SuppressWarnings("serial")
public class ResourceDownloadServlet extends HttpServlet {

	public static final String SERVER_IMAGES_PATHNAME = System.getProperty("user.dir") + "/webapps/images";
	MysqlConnector connector = MysqlConnector.getInstance();

	public ResourceDownloadServlet() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest,
	 * javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// if (request.getSession().getAttribute("userID") == null ||
		// request.getParameter("resourceid") == null) {
		if (request.getParameter("imageID") == null) {
			response.setStatus(403);
			return;
		}
		String imageID = request.getParameter("imageID");
		String filename = connector.getImageEntry(Integer.parseInt(imageID)).getFilename();
		File inputFile = new File(SERVER_IMAGES_PATHNAME, (request.getParameter("thumb") != null ? "tn" : "") + filename);
		if (inputFile.exists()) {
			FileInputStream fis = new FileInputStream(inputFile);
			response.setContentType(filename.toLowerCase().endsWith("png") ? "image/png" : "image/jpeg");
			ServletOutputStream out = response.getOutputStream();
			byte buffer[] = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = fis.read(buffer)) > 0)
				out.write(buffer, 0, bytesRead);
			out.close();
			fis.close();
		}
	}

}
