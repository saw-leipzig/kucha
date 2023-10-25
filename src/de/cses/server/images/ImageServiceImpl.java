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
import de.cses.shared.ImageEntry;

/**
 * This HttpServlet is used to upload images to the server's main image directory. It also creates a new entry in the Images table of the
 * database.
 * 
 * @author alingnau
 *
 */
@SuppressWarnings("serial")
@MultipartConfig
public class ImageServiceImpl extends HttpServlet {

//	private static final int THUMBNAIL_SIZE = 300;
	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();
	private int newImageID = 0;
	private ImageEntry ie;

	public ImageServiceImpl() {
		super();
	}

	/**
	 * This method is called when the submit button in the image paperUploader is pressed. Images are stored in the SERVER_IMAGES_PATHNAME
	 * 
	 * @see de.cses.client.images.ImageUploader
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadFileName;
		String fileType, filename=null;

		response.setContentType("text/plain");
		String origUploadFileName = request.getParameter("origImageFileName");
		String hasID = request.getParameter("hasID");
		String sessionID = request.getParameter("sessionID");
		String modifiedBy = request.getParameter("modifiedBy");
		File imgHomeDir = new File(serverProperties.getProperty("home.images"));
		if (!imgHomeDir.exists()) {
			imgHomeDir.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		if (hasID==null){
			if (!connector.getImageEntries("Title=\"" + origUploadFileName + "\"").isEmpty()) { // filename already exists
				System.err.println(origUploadFileName + " already exists in database!");
				response.getWriter().write(String.valueOf(0));
				System.err.println("Response: "+response.toString());
				System.err.println("Response Status: "+response.getStatus());
//				System.err.println("response written!");
				response.getWriter().close();
//				System.err.println("response closed!");
				return;
			}
		}
		try {
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
					System.err.println("uploadFileName = " + uploadFileName);
					fileType = uploadFileName.substring(uploadFileName.lastIndexOf(".")).toLowerCase();
					System.err.println("imageType = " + fileType);
					if (item.isFormField()) {
						throw new ServletException("Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
					} else {
						if (hasID==null){
							System.err.println("requesting new imageID");
							newImageID = connector.createNewImageEntry().getImageID();
							System.err.println("newImageID = " + newImageID);
							filename = newImageID + fileType;
						}
						else {
							newImageID= Integer.parseInt(hasID);
							filename = hasID + fileType;
						}
						if (newImageID > 0) {
							System.err.println("filename = " + filename);
							ie = connector.getImageEntry(newImageID);
							ie.setFilename(filename);
							ie.setLastChangedByUser(modifiedBy);
							ie.setTitle(uploadFileName);
							File oldImageFile = new File(imgHomeDir,filename);
							oldImageFile.delete();
							System.out.println("deleting file success: "+Boolean.toString(oldImageFile.exists()));
							connector.updateImageEntry(ie, sessionID);
							target = new File(imgHomeDir, filename);
							try {	
								URL imageURL = new URL(
										"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/max/0/default.jpg"
									);
								InputStream in = imageURL.openStream();
								in.close();	
								ArrayList<String> sizes = new ArrayList<String>();
								sizes.add("700");
								sizes.add("300");
								sizes.add("180");
								for (String tnSize:sizes) {
									imageURL = new URL(
											"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/!" + tnSize + "," + tnSize + "/0/default.jpg"
										);
									System.out.println(imageURL);
									in = imageURL.openStream();
									in.close();																	
								}
							}
							catch (Exception e) {
								System.err.println("IllegalStateException"+e.getLocalizedMessage());
							}
							target.createNewFile();
							item.write(target);
							item.delete();
						}
					}
				}
			} catch (ServletException e) {
				System.err.println("ServletException");
			} catch (Exception e) {
				System.err.println("IllegalStateException"+e.getLocalizedMessage());
				throw new IllegalStateException(e);
			}
		} finally {
			if (target != null && target.exists()) {
				System.err.println("Uploaded file stored as: " + target.getAbsolutePath());
//				if (!target.getAbsolutePath().endsWith("tif")) {
//				  createThumbnail(target, new File(imgHomeDir, "tn" + newImageID + ".png"));
//				}
				response.getWriter().write(String.valueOf(newImageID));
				response.getWriter().close();
			}
		}	
	}

	/**
	 * Create a thumbnail image file with a max side length of THUMBNAIL_SIZE
	 * 
	 * @param path
	 *          the file of the image
	 * @param tnFile
	 *          the new thumbnail file
	 */
//	private void createThumbnail(File readFile, File tnFile) {
//		String type = "png";
//		BufferedImage tnImg;
//
//		try {
//			System.err.println("trying to create thumbnail ...");
//			// we need to call the scanner in order to detect the additional libraries
//			// the libraries used are from https://haraldk.github.io/TwelveMonkeys/
//			ImageIO.scanForPlugins();
//			
//			BufferedImage buf = ImageIO.read(readFile);
//			float w = buf.getWidth();
//			float h = buf.getHeight();
//			System.err.println("w=" + w + " h=" + h);
//			if (w == h) {
//				tnImg = new BufferedImage(THUMBNAIL_SIZE, THUMBNAIL_SIZE, BufferedImage.TYPE_INT_RGB);
//				tnImg.createGraphics().drawImage(buf.getScaledInstance(THUMBNAIL_SIZE, THUMBNAIL_SIZE, Image.SCALE_SMOOTH), 0, 0, null);
//			} else if (w > h) {
//				float factor = THUMBNAIL_SIZE / w;
//				float tnHeight = h * factor;
//				tnImg = new BufferedImage(THUMBNAIL_SIZE, Math.round(tnHeight), BufferedImage.TYPE_INT_RGB);
//				tnImg.createGraphics().drawImage(buf.getScaledInstance(THUMBNAIL_SIZE, Math.round(tnHeight), Image.SCALE_SMOOTH), 0, 0, null);
//			} else {
//				float factor = THUMBNAIL_SIZE / h;
//				float tnWidth = w * factor;
//				tnImg = new BufferedImage(Math.round(tnWidth), THUMBNAIL_SIZE, BufferedImage.TYPE_INT_RGB);
//				tnImg.createGraphics().drawImage(buf.getScaledInstance(Math.round(tnWidth), THUMBNAIL_SIZE, Image.SCALE_SMOOTH), 0, 0, null);
//			}
//			System.err.println("creating thumbnail = " + tnFile.getAbsolutePath());
//			ImageIO.write(tnImg, type, tnFile);
//		} catch (IOException e) {
//			System.err.println("I/O Exception - thumbnail could not be created!");
//		} catch (OutOfMemoryError e) {
//			System.err.println("An OutOfMemoryError has occurred while scaling the image!");
//		} catch (Exception e) {
//			System.err.println("An unknown exception occurred during thumbnail creation!");
//		}
//	}
	
}
