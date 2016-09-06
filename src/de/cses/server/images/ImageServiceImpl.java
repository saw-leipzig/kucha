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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.ImageEntry;

/**
 * This HttpServlet is used to upload images to the server's main image
 * directory. It also creates a new entry in the Images table of the database.
 * 
 * @author alingnau
 *
 */
@SuppressWarnings("serial")
@MultipartConfig
public class ImageServiceImpl extends HttpServlet {

	public static final String SERVER_IMAGES_PATHNAME = System.getProperty("user.dir") + "/webapps/images";
	MysqlConnector connector = MysqlConnector.getInstance();

	/**
	 * This method is called when the submit button in the image uploader is
	 * pressed. Images are stored in the SERVER_IMAGES_PATHNAME
	 * 
	 * @see de.cses.client.images.ImageUploader
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadFileName;
		String fileType, filename = "default.jpg";

		response.setContentType("text/plain");

		File imgHomeDir = new File(SERVER_IMAGES_PATHNAME);
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		try {
			try {
				List<?> items = upload.parseRequest(request);
				Iterator<?> it = items.iterator();
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					uploadFileName = item.getName();
					// we take the sub dir from the field name which corresponds with the
					// purpose of the upload (e.g. depictions, backgrounds, ...)
					fileType = uploadFileName.substring(uploadFileName.lastIndexOf("."));
					if (item.isFormField()) {
						throw new ServletException(
								"Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
					} else {
						int newImageID = connector.createNewImageEntry();
						if (newImageID >= 0) {
							filename = newImageID + fileType;
							ImageEntry ie = connector.getImageEntry(newImageID);
							ie.setFilename(filename);
							connector.updateEntry(ie.getSqlUpdate(ImageEntry.FILENAME));
						}
						target = new File(imgHomeDir, filename);
						item.write(target);
						item.delete();
					}
				}
			} catch (ServletException e) {
				throw e;
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
		} finally {
			if (target != null && target.exists()) {
				System.err.println("Uploaded file: " + target.getAbsolutePath());
				createThumbnail(imgHomeDir, filename);
				response.getWriter().write("File length: " + target.length());
				response.getWriter().close();
			}
		}
	}

	/**
	 * Create a thumbnail file of size 200x200
	 * 
	 * @param path
	 *          the directory where the image is located
	 * @param filename
	 *          of the image
	 */
	private void createThumbnail(File path, String filename) {
		File tnFile;
		String type;
		BufferedImage tnImg;

		tnImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		tnFile = new File(path, "tn" + filename);
		File readFile = new File(path, filename);
		type = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
		try {
			tnImg.createGraphics().drawImage(ImageIO.read(readFile).getScaledInstance(200, 200, Image.SCALE_SMOOTH), 0, 0,
					null);
			ImageIO.write(tnImg, type, tnFile);
		} catch (IOException e) {
			System.out.println("Thumbnail could not be created!");
		}
	}

}