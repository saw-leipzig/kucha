package de.cses.server.images;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

import de.cses.server.mysql.MysqlConnector;

@MultipartConfig
public class ImageServiceImpl extends HttpServlet {
	
	MysqlConnector connector = MysqlConnector.getInstance();

	public static final void copyInputStreamAndClose(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len;

		while ((len = in.read(buffer)) >= 0) {
			out.write(buffer, 0, len);
		}
		in.close();
		out.close();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String uploadFileName;
		String uploadSubdirName;
		String fileType;

		response.setContentType("text/plain");

		File imgHomeDir = new File(System.getProperty("user.dir")+"/webapps/images");
//		File uploadDir = new File("/tmp/kucha");
		FileItemFactory factory = new DiskFileItemFactory(1000000, imgHomeDir);
		ServletFileUpload upload = new ServletFileUpload(factory);
		File target = null;
		try {
			try {
//				System.err.println("Step 2: ");
				List<?> items = upload.parseRequest(request);
//				System.err.println("Step 3: ");
				Iterator<?> it = items.iterator();
//				System.err.println("Step 4: ");
				while (it.hasNext()) {
					FileItem item = (FileItem) it.next();
					uploadFileName = item.getName(); 
					uploadSubdirName = item.getFieldName();
					fileType = uploadFileName.substring(uploadFileName.lastIndexOf("."));
					if (item.isFormField()) {
						throw new ServletException("Unsupported non-file property [" + item.getFieldName() + "] with value: " + item.getString());
					} else {
//						target = File.createTempFile("temp_", fileType, uploadDir);
						
						int newImageID = connector.createNewImageEntry();
						if (newImageID >= 0) {
							target = new File(imgHomeDir, uploadSubdirName+"/"+ newImageID + fileType);
//						System.err.println("Try to create file: " + target.getAbsolutePath());
						} else {
							target = new File(imgHomeDir, uploadSubdirName+"/unknown" + fileType);
						}
						item.write(target);
						item.delete();
					}
				}
			} catch (ServletException e) {
				throw e;
			} catch (Exception e) {
				throw new IllegalStateException(e);
			}
			response.getWriter().write("File length: " + target.length());
			response.getWriter().close();
		} finally {
			if (target != null && target.exists()) {
				System.err.println("Uploaded file: " + target.getAbsolutePath());
			}
		}
	}

}
