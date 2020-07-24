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

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * @author alingnau
 *
 */
@SuppressWarnings("serial")
public class BibDocumentServiceImpl extends HttpServlet {
	
	private ServerProperties serverProperties = ServerProperties.getInstance();

	/**
	 * 
	 */
	public BibDocumentServiceImpl() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String uploadFileName;
		String fileType, filename = null;

		resp.setContentType("text/plain");
		File imgHomeDir = new File(serverProperties.getProperty("home.documents"));
		if (!imgHomeDir.exists()) {
			imgHomeDir.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		try {
			List<?> items = upload.parseRequest(req);
			Iterator<?> it = items.iterator();
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				uploadFileName = item.getName();
				fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".")).toLowerCase();
				if (item.isFormField()) {
					throw new ServletException("Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
				} else {
					filename = req.getParameter("docFileName") + fileType;
					target = new File(imgHomeDir, filename);
					if (target.exists()) {
						target.delete(); // replacement of the old file!
					}
					item.write(target);
					item.delete();
					resp.getWriter().write(String.valueOf(filename));
					resp.getWriter().close();
				}
			}
		} catch (ServletException e) {
			System.err.println("ServletException");
		} catch (Exception e) {
			System.err.println("IllegalStateException");
			throw new IllegalStateException(e);
		}	}

	

}
