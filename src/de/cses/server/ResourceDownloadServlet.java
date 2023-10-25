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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.select.Elements;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.gargoylesoftware.htmlunit.javascript.host.dom.Comment;
import com.google.gson.Gson;
import com.google.gwt.thirdparty.json.JSONException;
import com.google.gwt.thirdparty.json.JSONObject;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.Util;
import de.cses.server.mysql.MysqlConnector;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.UserEntry;

@SuppressWarnings("serial")
public class ResourceDownloadServlet extends HttpServlet {

	private MysqlConnector connector = MysqlConnector.getInstance();
	private ServerProperties serverProperties = ServerProperties.getInstance();

	public ResourceDownloadServlet() {
	}
	  @Override
	  protected void doOptions(HttpServletRequest req, HttpServletResponse resp)
	          throws ServletException, IOException {
	      setAccessControlHeaders(resp);
	      resp.setStatus(HttpServletResponse.SC_OK);
	  }
	  @Override
	  public void doPost(HttpServletRequest request, HttpServletResponse response)
			  throws ServletException, IOException {
		  setAccessControlHeaders(response);
		  if (request.getParameter("registerrequest") != null) {
			    StringBuilder sb = new StringBuilder();
			    BufferedReader reader = request.getReader();
			    try {
			        String line;
			        while ((line = reader.readLine()) != null) {
			            sb.append(line).append('\n');
			        }
			    } finally {
			        reader.close();
			    }
			    Gson gson = new Gson();
			    UserEntry newUser = gson.fromJson(sb.toString(), UserEntry.class);
			    String answer = connector.saveWebPageUser(newUser);
			    if (answer != "") {
			    	response.setContentType("application/json");
			    	response.getWriter().write("{\"message\":\""+answer+"\"}");
			    	response.addHeader("message", answer);
			    	response.setStatus(405);
			    	response.getWriter().flush();
			    } else {
			    	connector.sendMail("kucha-admin@saw-leipzig.de","e.radisch@gmx.de", "Kucha-Admin","An account has been created at kuchatest.saw-leipzig.de","Dear Administartor,\n A new User needs to be granted rights for discussions. Details: \n First Name: "+newUser.getFirstname()+"\n Last Name: "+ newUser.getLastname() + "\n Email: "+newUser.getEmail()+"\n Affiliation: " + newUser.getAffiliation()+ "\n Sincerely, \n Kucha-Admin");
			    }
			    
			} else if (request.getParameter("validateUser") != null) {
			    StringBuilder sb = new StringBuilder();
			    BufferedReader reader = request.getReader();
			    try {
			        String line;
			        while ((line = reader.readLine()) != null) {
			            sb.append(line).append('\n');
			        }
			    } finally {
			        reader.close();
			    }
			    Gson gson = new Gson();
			    UserEntry newUser = gson.fromJson(sb.toString(), UserEntry.class);
			    UserEntry answer = connector.userLoginFrontEnd(newUser.getEmail(), newUser.getSessionID());
			    if (answer != null) {
			    	response.setContentType("application/json");
			    	response.getWriter().write(gson.toJson(answer));
			    	response.setStatus(200);
			    	response.getWriter().flush();
			    } else {
			    	response.setStatus(404);
			    }
			    
			} else if (request.getParameter("isLoggedIn") != null) {
		    StringBuilder sb = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    try {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            sb.append(line).append('\n');
		        }
		    } finally {
		        reader.close();
		    }
		    Gson gson = new Gson();
		    UserEntry newUser = gson.fromJson(sb.toString(), UserEntry.class);
		    UserEntry answer = connector.checkSessionIDFrontEnd(newUser.getSessionID());
		    if (answer != null) {
		    	response.setContentType("application/json");
		    	response.getWriter().write(gson.toJson(answer));
		    	response.setStatus(200);
		    	response.getWriter().flush();
		    } else {
		    	response.setStatus(404);
		    }		    
		} else if (request.getParameter("changeUser") != null) {
		    StringBuilder sb = new StringBuilder();
		    BufferedReader reader = request.getReader();
		    try {
		        String line;
		        while ((line = reader.readLine()) != null) {
		            sb.append(line).append('\n');
		        }
		    } finally {
		        reader.close();
		    }
		    Gson gson = new Gson();
		    String email = "";
		    String pw = "";
		    final String authorization = request.getHeader("Authorization");
		    if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
		        // Authorization: Basic base64credentials
		        String base64Credentials = authorization.substring("Basic".length()).trim();
		        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		        String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		        // credentials = username:password
		        email = credentials.split(":", 2)[0];
		        pw = credentials.split(":", 2)[1];
		    }
		    UserEntry updatedUser = gson.fromJson(sb.toString(), UserEntry.class);
		    try {
				JSONObject comment = new JSONObject(sb.toString());
				Boolean granted = comment.getBoolean("granted");
				updatedUser.setGranted(granted);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    boolean answer = connector.updateUserEntryFrontEnd(updatedUser, pw, updatedUser.getSessionID(), email);
		    if (answer) {
		    	response.setContentType("application/json");
		    	response.setStatus(200);
		    } else {
		    	response.setStatus(404);
		    }
		    
		}
	  }

	  private void setAccessControlHeaders(HttpServletResponse resp) {
	      resp.setHeader("Access-Control-Allow-Origin", "*");
	      resp.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT");
	      resp.setHeader("Access-Control-Allow-Headers", "*");
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
		setAccessControlHeaders(response);
		String sessionID = request.getParameter("sessionID");
		//System.out.println("doGetParameters: ");
		//for (String key : request.getParameterMap().keySet()) {
		//	System.out.println("   " +key+ " - "+request.getParameter(key));
		//}
		if (request.getParameter("getGames") != null) {
	    	response.setCharacterEncoding("UTF-8");
	    	response.setHeader("Content-Type", "application/json; charset=UTF-8");
	    	byte[] game = connector.getGame().getBytes("UTF-8");
	    	Integer bytes = game.length;
	    	response.addHeader("x-decompressed-content-length", bytes.toString());
	    	response.setContentLength(bytes);
	    	response.addIntHeader("content-length", bytes);
	    	response.getOutputStream().write(game);
	    	response.setStatus(200);
	    	response.getOutputStream().flush();		
		}
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
					//System.err.println("acess Level PUBLIC and COPYRIGHT");
					break; 
				case UserEntry.FULL:
				case UserEntry.ADMIN:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PRIVATE);
					//System.err.println("acess Level PUBLIC, COPYRIGHT and PRIVATE");
					break;
				default:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					//System.err.println("acess Level PUBLIC");
					break;
			}
			//System.err.println("sessionID=" + sessionID + ", userAccessLevel=" + connector.getAccessLevelForSessionID(sessionID) + ", ImageEntry accessLevel=" + imgEntry.getAccessLevel());
			
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
						"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/" + tnSize + ",/0/default.jpg"
					);
			} else {

				imageURL = new URL(
						"http://127.0.0.1:8182/iiif/2/" + serverProperties.getProperty("iiif.images") + filename + "/full/max/0/default.jpg"
					);
			}
//			System.out.println("Öffne Stream");
			InputStream in = imageURL.openStream();
			response.setContentType("image/jpg");
			byte buffer[] = new byte[4096];
			int bytesRead = 0;
			while ((bytesRead = in.read(buffer)) > 0) {
				out.write(buffer, 0, bytesRead);
//				System.out.println("ImageID: "+imageID+"Bytes Übertragen: "+bytesRead);
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
					response.setContentType(filename.toLowerCase().endsWith("png") ? "image/jpg" : "image/jpeg");
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
					response.setContentType(filename.toLowerCase().endsWith("png") ? "image/jpg" : "image/jpeg");
					ServletOutputStream out = response.getOutputStream();
					byte buffer[] = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					out.close();
					fis.close();
				} else {
					response.setStatus(404);http://localhost:8080
					return;
				}
			}
		} else if (request.getParameter("wallsketch") != null) {
			String filename = request.getParameter("wallsketch");
			if (filename.startsWith(".")) {
				response.setStatus(400);
				return;
			} else {
				File inputFile = new File(serverProperties.getProperty("home.wallketches"), filename);
				if (inputFile.exists()) {
					FileInputStream fis = new FileInputStream(inputFile);
					response.setContentType(filename.toLowerCase().endsWith("png") ? "image/jpg" : "image/jpeg");
					ServletOutputStream out = response.getOutputStream();
					byte buffer[] = new byte[4096];
					int bytesRead = 0;
					while ((bytesRead = fis.read(buffer)) > 0) {
						out.write(buffer, 0, bytesRead);
					}
					out.close();
					fis.close();
				} else {
					response.setStatus(404);http://localhost:8080
					return;
				}
			}
		} else if (request.getParameter("document") != null) {
			if (connector.getAccessLevelForSessionID(sessionID) >= UserEntry.FULL) {
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
			}
		} 
		else if (request.getParameter("annotation") != null) {
			String fileID = request.getParameter("annotation");
			Integer bibID = Integer.parseInt(fileID);
			Document annotation  = connector.generatePDF(bibID);
			AnnotatedBibliographyEntry bib = connector.getAnnotatedBibliographybyID(bibID, "");
			// System.out.println(annotation.html());
			Elements head = annotation.getElementsByTag("head");
			Element style2 = new Element("style");
			// we need to add styles because only selected fonts would work.
			style2.attr("type", "text/css");
			style2.append("\n    @font-face {\n"
					+ "    font-family: 'FreeSerif';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"FreeSerif.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    font-weight: normal;\n"
					+ "    font-style: normal;\n"
					+ "    unicode-range: U+0000-10FF;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'FreeSerif';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"FreeSerifBold.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    font-weight: bold;\n"
					+ "    font-style: normal;\n"
					+ "    unicode-range: U+0000-10FF;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'FreeSerif';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"FreeSerifBoldItalic.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    font-weight: bold;\n"
					+ "    font-style: italic;\n"
					+ "    unicode-range: U+0000-10FF;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'FreeSerif';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"FreeSerifItalic.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    font-weight: normal;\n"
					+ "    font-style: italic;\n"
					+ "    unicode-range: U+0000-10FF;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'Batang';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"BATANG.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    unicode-range: U+1100-11FF,U+1200-2BFF,U+3130-318F,U+AC00-D7AF;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'MS Mincho';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"MSMINCHO.TTF\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    }\n"
					+ "    @font-face {\n"
					+ "    font-family: 'SimSun';\n"
					+ "    src: url(\""+serverProperties.getProperty("home.fonts")+"SIMSUN.ttf\");\n"
					+ "    -fs-pdf-font-embed: embed;\n"
					+ "    -fs-pdf-font-encoding: Identity-H;\n"
					+ "    unicode-range: U+4E00-9FFF,U+2E80-312F,U+3190-319F,U+31A0-A4CF;\n"
					+ "    }\n"
					+ "\n    * { font-family: 'FreeSerif','Batang','SimSun','MS Mincho';}"
					+ "\n    *[lang|=\"ko\"] 	{"					
					+ "\n    font-family: 'Batang';"
					+ "\n    }"					
					+ "\n    *[lang|=\"zh\"] 	{"					
					+ "\n    font-family: 'SimSun';"
					+ "\n    }"					
					+ "\n    *[lang|=\"ja\"] 	{"					
					+ "\n    font-family: 'MS Mincho';"
					+ "\n    }"
					+ "\n    h1 	{"					
					+ "\n    font-size:1.2em;"
					+ "\n    margin-bottom: 1em;"
					+ "\n    }"
					+ "\n    b { font-weight: bold!important;}");
			head.first().appendChild(style2);
			response.setContentType("application/pdf; name=\"MyFile.pdf\"");
		    response.setHeader("Content-Disposition","attachment; name='fieldName';  filename='"+ bib.getBibtexKey() + ".pdf'");
			if (annotation != null) {
			    try (ServletOutputStream out = response.getOutputStream()){
				    ITextRenderer renderer = new ITextRenderer();
				    SharedContext sharedContext = renderer.getSharedContext();
				    sharedContext.setPrint(true);
				    sharedContext.setInteractive(false);
				    renderer.setDocumentFromString(annotation.html());
				    renderer.layout();
				    renderer.createPDF(out);    	
	
					out.close();
			    } catch (FileNotFoundException e) {
					e.getLocalizedMessage();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.getLocalizedMessage();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.getLocalizedMessage();
				}
			} else {
				response.setStatus(404);
				return;
			}
		} else if (request.getParameter("dataexport") != null)  {
	        Map<String, String> map = new HashMap<String, String>();

	        Enumeration headerNames = request.getHeaderNames();
	        while (headerNames.hasMoreElements()) {
	            String key = (String) headerNames.nextElement();
	            String value = request.getHeader(key);
	        }
	        String ip = "";
	        try(final DatagramSocket socket = new DatagramSocket()){
	        	  socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
	        	  ip = socket.getLocalAddress().getHostAddress();
	        	}
			if (request.getLocalAddr().equals("127.0.0.1")) {
				// connector.serializeAllDepictionEntries("");
			} else {
			}
		} else if (request.getParameter("imageFile") != null) {
			String filename = request.getParameter("imageFile");
			String parts[]=filename.split("\\.");
            String part1=parts[0];
			ImageEntry imgEntry = connector.getImageEntry(Integer.parseInt(part1));
			File inputFile;
			int userAccessLevel = AbstractEntry.ACCESS_LEVEL_PUBLIC;
			ArrayList<Integer> authorizedAccessLevel = new ArrayList<Integer>();
			switch (connector.getAccessLevelForSessionID(sessionID)) {
				case UserEntry.GUEST:
					break;
				case UserEntry.ASSOCIATED:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					//System.err.println("acess Level PUBLIC and COPYRIGHT");
					break; 
				case UserEntry.FULL:
				case UserEntry.ADMIN:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_COPYRIGHT);
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PRIVATE);
					//System.err.println("acess Level PUBLIC, COPYRIGHT and PRIVATE");
					break;
				default:
					authorizedAccessLevel.add(AbstractEntry.ACCESS_LEVEL_PUBLIC);
					//System.err.println("acess Level PUBLIC");
					break;
			}
			//System.err.println("sessionID=" + sessionID + ", userAccessLevel=" + connector.getAccessLevelForSessionID(sessionID) + ", ImageEntry accessLevel=" + imgEntry.getAccessLevel());
			
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

			
			File imgHomeDir = new File(serverProperties.getProperty("home.images"));
			File f = new File(imgHomeDir,filename);
	        String imgFilename = f.getName();
	        int length = 0;
	        try {
	            ServletOutputStream op = response.getOutputStream();
	            response.setContentType("application/octet-stream");
	            response.setContentLength((int) f.length());
	            response.setHeader("Content-Disposition", "attachment; filename*=\"utf-8''" + imgEntry.getTitle()+"."+parts[1] + "");
	            byte[] bbuf = new byte[1024];
	            DataInputStream in = new DataInputStream(new FileInputStream(f));
	            while ((in != null) && ((length = in.read(bbuf)) != -1)) {
	                op.write(bbuf, 0, length);
	            }
	            in.close();
	            op.flush();
	            op.close();
	        }
	        catch (Exception ex) {
	            ex.printStackTrace();
	        }
		} else {
			response.setStatus(400);
		}
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
  	    setAccessControlHeaders(response);
	    if (request.getCharacterEncoding() == null) {
	        request.setCharacterEncoding("UTF-8");
	    }
		String sessionID = request.getParameter("sessionID");
		//System.out.println("doGetParameters: ");
		//for (String key : request.getParameterMap().keySet()) {
		//	System.out.println("   " +key+ " - "+request.getParameter(key));
		//}
		
		if (request.getParameter("putComment") != null)  {
	        String discussion = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        String uuid = request.getParameter("uuid");
	        String sendMail = request.getParameter("sendMail");
	        String messageText = request.getParameter("message");
	        
	        if (connector.checkSessionIDFrontEnd(sessionID) != null) {
		        if (connector.putComment("", "", discussion, uuid, sendMail, messageText)) {
			        response.setStatus(200);	        	
		        } else {
			        response.setStatus(400);
		        };	        	
	        }
		}	else if (request.getParameter("putNews") != null)  {
	        String discussion = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        String uuid = request.getParameter("uuid");
	        String messageText = request.getParameter("message");
	        if (connector.checkSessionIDFrontEnd(sessionID) != null) {
		        if (connector.putNews("", "", discussion, uuid, messageText)) {
			        response.setStatus(200);	        	
		        } else {
			        response.setStatus(400);
		        };	        	
	        }
		} else {
			response.setStatus(400);
		}
	}

}
