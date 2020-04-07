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
package de.cses.client.images;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

import de.cses.client.Util;
import de.cses.shared.ImageEntry;

public class ImageUploader implements IsWidget {

	private FormPanel form;
	private ArrayList<ImageUploadListener> uploadListener;
	protected Window uploadInfoWindow;
	private FileUploadField file;
	private FramedPanel mainPanel;
	private ArrayList<String> typeList;
	protected String selectedFile;
	private String filename;
	int imageID = -1;
	ImageEntry imgEntry;


	public ImageUploader(ImageUploadListener listener) {
		uploadListener = new ArrayList<ImageUploadListener>();
		uploadListener.add(listener);
		imageID=-1;
	}
	public ImageUploader(ImageUploadListener listener, ImageEntry imgEntry) {
		uploadListener = new ArrayList<ImageUploadListener>();
		uploadListener.add(listener);
		this.imageID=imgEntry.getImageID();
		this.imgEntry=imgEntry;
	}
	private void proceed() {
		int startIdx = Math.max(selectedFile.lastIndexOf("\\"), selectedFile.lastIndexOf("/"));
		filename = selectedFile.substring(startIdx>0 ? startIdx+1 : 0, selectedFile.lastIndexOf("."));
		Util.doLogging( selectedFile+"- "+filename);
		if (imageID<0) {
			form.setAction("imgUpload?origImageFileName="+filename);
		}
		else {
			form.setAction("imgUpload?origImageFileName="+filename+"&hasID="+Integer.toString(imageID));
		}
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		form.reset();
		file.reset();
		return mainPanel;
	}

	private void initPanel() {
		
		typeList = new ArrayList<String>();
		typeList.add("jpg");
		typeList.add("jpeg");
		typeList.add("png");
		typeList.add("tif");
		typeList.add("tiff");

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Image Uploader");

		file = new FileUploadField();
		file.setName("uploadedfile");
		file.setAllowBlank(false);
		file.setPixelSize(300, 30);
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				selectedFile = file.getValue();
				if ((selectedFile.lastIndexOf(".") < 0) || !typeList.contains(selectedFile.toLowerCase().substring(selectedFile.lastIndexOf(".")+1))) {
					Util.showWarning("Unsopported Image Type", "Please select JPG, PNG or TIFF!");
					file.reset();
				} else {
					if (imageID>0) {
						int startIdx = Math.max(selectedFile.lastIndexOf("\\"), selectedFile.lastIndexOf("/"));
						if (imgEntry.getFilename()!=selectedFile.substring(startIdx>0 ? startIdx+1 : 0, selectedFile.lastIndexOf("."))) {
			  				de.cses.client.Util.showYesNo("File names not identical!", "Choosing a wrong image will overwrite the old filename. Changes will be irreversible. Do only proceed, if you are certain.", new SelectHandler() {
								
								@Override
								public void onSelect(SelectEvent event) {
					        		proceed();
								}
							}, new SelectHandler() {
									
								@Override
								public void onSelect(SelectEvent event) {
									for (ImageUploadListener listener : uploadListener) {
										// ToDo: send information after image upload for database
										listener.uploadCanceled();
									}
								}
							}, new KeyDownHandler() {

								@Override
								public void onKeyDown(KeyDownEvent e) {
									for (ImageUploadListener listener : uploadListener) {
										// ToDo: send information after image upload for database
										listener.uploadCanceled();
									}
								}
							}
						  );
						}
					}
					else {
						proceed();
					}
				}
			}
		});
		form = new FormPanel();
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadInfoWindow.hide();
				Document doc = XMLParser.parse(event.getResults());
				Util.doLogging(event.getResults());
				NodeList nodelist = doc.getElementsByTagName("pre");
				Node node = nodelist.item(0);
				imageID = Integer.parseInt(node.getFirstChild().toString());
				if (imageID == 0) {
					Util.showWarning("Duplicate Image Error", "This image has already been uploaded!");
				} else {
					for (ImageUploadListener listener : uploadListener) {
						listener.uploadCompleted(imageID, filename);
					}
				}
			}
		});
		form.add(file);
		form.setLayoutData(new MarginData(10, 0, 0, 0));
		mainPanel.add(form);

		TextButton submitButton = new TextButton("Upload");
		submitButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (form.isValid()) {
					form.submit();
					uploadInfoWindow = new Window();
					uploadInfoWindow.setHeading("Please wait!");
					uploadInfoWindow.setModal(true);
					uploadInfoWindow.setPixelSize(150, 50);
					uploadInfoWindow.setMaximizable(false);
					uploadInfoWindow.setClosable(false);
					Status s = new Status();
					s.setBusy("uploading image ...");
					uploadInfoWindow.setWidget(s);
					uploadInfoWindow.show();
				}
			}
		});

		TextButton resetButton = new TextButton("Reset");
		resetButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				form.reset();
				file.reset();
			}
		});

		TextButton cancelButton = new TextButton("Cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				for (ImageUploadListener listener : uploadListener) {
					// ToDo: send information after image upload for database
					listener.uploadCanceled();
				}

			}
		});

		mainPanel.addButton(cancelButton);
		mainPanel.addButton(resetButton);
		mainPanel.addButton(submitButton);
	}

}
