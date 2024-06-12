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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
import de.cses.shared.Cave3DModelEntry;
import de.cses.shared.CaveSketchEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.WallSketchEntry;

/**
 * This HttpServlet is used to upload images to the server's cave sketch directory. It also creates a new entry in the Images table of the database.
 * 
 * @author erikradisch
 *
 */
@SuppressWarnings("serial")
@MultipartConfig
public class Cave3DServiceImpl extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();
	private int newCave3DModelID = 0;
	private Cave3DModelEntry c3DMe;
	
	public Cave3DServiceImpl() {
		super();
	}

	/**
	 * This method is called when the submit button in the image paperUploader is pressed.
	 * 
	 * @see de.cses.client.cave.Cave3DModelUploader
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("got new 3D Object");
		String uploadFileName;
		String fileType = null;
		String title, cave3DFilename = "";
		Integer caveTypeID = -1;
		cave3DFilename = request.getParameter("3DModelFilename");
		System.out.println("origImageFileName " + cave3DFilename);
		title = request.getParameter("title");
		System.out.println("title " + title);
		caveTypeID = Integer.parseInt(request.getParameter("caveTypeID"));
		System.out.println("caveID " + caveTypeID);
		String sessionID = request.getParameter("sessionID");
		System.out.println("sessionID " + sessionID);
		String modifiedBy = request.getParameter("modifiedBy");
		System.out.println("modifiedBy " + modifiedBy);
		String hasID = request.getParameter("hasID");
		System.out.println("hasID " + hasID);

		response.setContentType("text/plain");
		File imgHomeDir = new File(serverProperties.getProperty("home.3DModels"));
		System.out.println("imgHomeDir " + imgHomeDir);
		if (!imgHomeDir.exists()) {
			imgHomeDir.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		if (hasID==null){
			if (!connector.get3DModelEntry("Filename=\"" + cave3DFilename + "\"").isEmpty()) { // filename already exists
				System.err.println(cave3DFilename + " already exists in database!");
				response.getWriter().write(String.valueOf(0));
				System.err.println("Response: "+response.toString());
				System.err.println("Response Status: "+response.getStatus());
				System.err.println("response written!");
				response.getWriter().close();
				System.err.println("response closed!");
				return;
			}
		}
		try {
			System.err.println(request);
			List<?> items = upload.parseRequest(request);
			System.err.println("items" + items);
			Iterator<?> it = items.iterator();
			System.out.println("Iterator " + Boolean.toString(it.hasNext()));
			while (it.hasNext()) {
				FileItem item = (FileItem) it.next();
				uploadFileName = item.getName();
				// we take the sub dir from the field name which corresponds with the
				// purpose of the upload (e.g. depictions, backgrounds, ...)
				fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".") + 1).toLowerCase();
				if (item.isFormField()) {
					throw new ServletException("Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
				} else {
					if (hasID==null){
						System.err.println("requesting new imageID for " + item.getName());
						c3DMe = connector.createNew3DModelEntry(item.getName(), title, caveTypeID);
						System.err.println("newCave3DModelID = " + c3DMe.getCave3DModelID());
						newCave3DModelID = c3DMe.getCave3DModelID();
					} else {
						newCave3DModelID= Integer.parseInt(hasID);
					}
					if (newCave3DModelID > 0) {
						System.err.println("filename = " + item.getName());
						c3DMe = connector.getCave3DModelEntry(newCave3DModelID);
						c3DMe.setFilename(item.getName());
						c3DMe.setLastChangedByUser(modifiedBy);
						c3DMe.setTitle(title);
						File oldImageFile = new File(imgHomeDir,item.getName());
						oldImageFile.delete();
						System.out.println("deleting file success: "+Boolean.toString(oldImageFile.exists()));
						connector.updateCave3DModelEntry(c3DMe, sessionID);
						target = new File(imgHomeDir, item.getName());
						target.createNewFile();
						item.write(target);
						item.delete();
					}
				}
				System.out.println("Upload done.");				
			}
		} catch (ServletException e) {
			System.err.println("ServletException" + e.getLocalizedMessage());
		} catch (Exception e) {
			System.err.println("IllegalStateException" + e.getLocalizedMessage());
			throw new IllegalStateException(e);
		} finally {
		if (target != null && target.exists()) {
			System.err.println("Uploaded file stored as: " + target.getAbsolutePath());
//			if (!target.getAbsolutePath().endsWith("tif")) {
//			  createThumbnail(target, new File(imgHomeDir, "tn" + newCave3DModelID + ".png"));
//			}
			response.getWriter().write(String.valueOf(true));
			response.getWriter().close();
		}
	}	
	}

}
