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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.cses.server.mysql.MysqlConnector;

@SuppressWarnings("serial")
public class ResourceDownloadServlet extends HttpServlet {

//	public static final String SERVER_IMAGES_PATHNAME = System.getProperty("user.dir") + "/webapps/images";
	public static final String SERVER_BACKGROUNDS_PATHNAME = System.getProperty("user.dir") + "/webapps/backgrounds";
	private Properties serverProperties = new Properties();
	MysqlConnector connector = MysqlConnector.getInstance();

	public ResourceDownloadServlet() {
		FileReader fReader;
		try {
			fReader = new FileReader(System.getProperty("user.dir") + "/kucha.properties");
			serverProperties.load(fReader);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		if (request.getParameter("imageID") != null) {
			String imageID = request.getParameter("imageID");
			String filename = connector.getImageEntry(Integer.parseInt(imageID)).getFilename();
			File inputFile = new File(serverProperties.getProperty("imageHomeDir"), (request.getParameter("thumb") != null ? "tn" : "") + filename);
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
			} else {
				response.setStatus(404);
				return;
			}
		} else if (request.getParameter("background") != null) {
			String filename = request.getParameter("background");
			if (filename.startsWith(".")) {
				response.setStatus(400);
				return;
			} else {
				File inputFile = new File(SERVER_BACKGROUNDS_PATHNAME, filename);
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
				} else {
					response.setStatus(404);
					return;
				}
			}
		} else {
			response.setStatus(403);
		}
	}

}
