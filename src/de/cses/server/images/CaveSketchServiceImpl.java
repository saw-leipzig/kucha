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
package de.cses.server.images;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.cses.server.ServerProperties;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.CaveSketchEntry;

/**
 * This HttpServlet is used to upload images to the server's cave sketch directory. It also creates a new entry in the Images table of the database.
 * 
 * @author alingnau
 *
 */
@SuppressWarnings("serial")
@MultipartConfig
public class CaveSketchServiceImpl extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();

	public CaveSketchServiceImpl() {
		super();
	}

	/**
	 * This method is called when the submit button in the image paperUploader is pressed.
	 * 
	 * @see de.cses.client.images.ImageUploader
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadFileName;
		String fileType, filename = null;
		int caveID = 0;

		response.setContentType("text/plain");
		File imgHomeDir = new File(serverProperties.getProperty("home.cavesketches"));
		if (!imgHomeDir.exists()) {
			imgHomeDir.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		try {
			List<?> items = upload.parseRequest(request);
			Iterator<?> it = items.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				uploadFileName = item.getName();
				// we take the sub dir from the field name which corresponds with the
				// purpose of the upload (e.g. depictions, backgrounds, ...)
				fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toLowerCase();
				caveID = Integer.parseInt(request.getParameter("caveID"));
				if (item.isFormField()) {
					throw new ServletException("Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
				} else if (caveID>0) {
					CaveSketchEntry newEntry = new CaveSketchEntry(0, caveID, fileType);
					filename = caveID + "." + fileType;
					System.err.println("writing filename " + filename);
					target = new File(imgHomeDir, filename);
					item.write(target);
					item.delete();
					int newCaveSketchID = connector.insertCaveSketchEntry(newEntry);
					response.getWriter().write(Integer.toString(newCaveSketchID));
					response.getWriter().close();
				}
			}
		} catch (ServletException e) {
			System.err.println("ServletException");
		} catch (Exception e) {
			System.err.println("IllegalStateException");
			throw new IllegalStateException(e);
		}
	}

}
