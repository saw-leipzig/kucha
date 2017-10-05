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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.cses.server.mysql.MysqlConnector;

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
		// if (request.getSession().getAttribute("userID") == null ||
		// request.getParameter("resourceid") == null) {
		if (request.getParameter("imageID") != null) {
			String imageID = request.getParameter("imageID");
			String filename = connector.getImageEntry(Integer.parseInt(imageID)).getFilename();
			File inputFile = new File(
					serverProperties.getProperty("home.images"), 
					(request.getParameter("thumb") != null ? "tn" + filename.substring(0, filename.lastIndexOf(".")) + ".png" : filename) 
				);
//			File inputFile = new File(serverProperties.getProperty("home.images"), filename);
			ServletOutputStream out = response.getOutputStream();
			if (inputFile.exists()) {
				if (request.getParameter("thumb") != null) {
					int tnSize = Integer.valueOf(request.getParameter("thumb")); // the requested size is given as a parameter
					out.write(getScaledThumbnailInstance(inputFile, "png", tnSize));
				} else { // load the original file
					if (filename.toLowerCase().endsWith("png")) {
						response.setContentType("image/png");
					} else if (filename.toLowerCase().endsWith("jpg")) {
						response.setContentType("image/jpeg");
					} else if (filename.toLowerCase().endsWith("tiff")) {
						response.setContentType("image/tiff");
					}
					FileInputStream fis = new FileInputStream(inputFile);
					byte buffer[] = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					fis.close();
				}
				out.close();
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
		}
	}

	/**
	 * Create a thumbnail image file with a max side length of THUMBNAIL_SIZE
	 * 
	 * @param path
	 *          the directory where the image is located
	 * @param filename
	 *          of the image
	 * @return
	 */
	private byte[] getScaledThumbnailInstance(File readFile, String imgType, int thumbnailSize) {
		// File tnFile;
		// String type;
		BufferedImage tnImg = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		// tnFile = new File(path, "tn" + filename);
		// File readFile = new File(inputFile);
		// type = filename.substring(filename.lastIndexOf(".") + 1).toUpperCase();
		try {
			BufferedImage buf = ImageIO.read(readFile);
			float w = buf.getWidth();
			float h = buf.getHeight();
			if (w == h) {
				tnImg = new BufferedImage(thumbnailSize, thumbnailSize, BufferedImage.TYPE_INT_RGB);
				tnImg.createGraphics().drawImage(buf.getScaledInstance(thumbnailSize, thumbnailSize, Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(tnImg, imgType, baos);
				// ImageIO.write(tnImg, type, tnFile);
			} else if (w > h) {
				float factor = thumbnailSize / w;
				float tnHeight = h * factor;
				tnImg = new BufferedImage(thumbnailSize, Math.round(tnHeight), BufferedImage.TYPE_INT_RGB);
				tnImg.createGraphics().drawImage(buf.getScaledInstance(thumbnailSize, Math.round(tnHeight), Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(tnImg, imgType, baos);
				// ImageIO.write(tnImg, type, tnFile);
			} else {
				float factor = thumbnailSize / h;
				float tnWidth = w * factor;
				tnImg = new BufferedImage(Math.round(tnWidth), thumbnailSize, BufferedImage.TYPE_INT_RGB);
				tnImg.createGraphics().drawImage(buf.getScaledInstance(Math.round(tnWidth), thumbnailSize, Image.SCALE_SMOOTH), 0, 0, null);
				ImageIO.write(tnImg, imgType, baos);
				// ImageIO.write(tnImg, type, tnFile);
			}
		} catch (IOException e) {
			System.out.println("Scaled instance of thumbnail could not be created!");
		}
		return baos.toByteArray();
	}

}
