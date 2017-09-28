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
package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
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

public class CaveSketchUploader implements IsWidget {
	
	public interface CaveSketchUploadListener {
		public void uploadCompleted(String imageFilename);
		public void uploadCanceled();
	}

	private FormPanel form;
	private ArrayList<CaveSketchUploadListener> uploadListener;
	protected Window uploadInfoWindow;
	private FileUploadField file;
	private FramedPanel mainPanel;
	private ArrayList<String> typeList;
	private int caveID;


	public CaveSketchUploader(int caveID, CaveSketchUploadListener listener) {
		this.caveID = caveID;
		uploadListener = new ArrayList<CaveSketchUploadListener>();
		uploadListener.add(listener);
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
		mainPanel.setHeading("Cave Sketch Uploader");

		file = new FileUploadField();
		file.setName("uploadedsketch");
		file.setAllowBlank(false);
		file.setPixelSize(300, 30);
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String selected = file.getValue().toLowerCase();
				if ((selected.lastIndexOf(".") < 0) || !typeList.contains(selected.substring(selected.lastIndexOf(".")+1))) {
					Util.showWarning("Unsopported Image Type", "Please select JPG, PNG or TIFF!");
					file.reset();
				}
			}
		});
		form = new FormPanel();
		form.setAction("caveSketchUpload?caveID=" + caveID);
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadInfoWindow.hide();
				Document doc = XMLParser.parse(event.getResults());
				NodeList nodelist = doc.getElementsByTagName("pre");
				Node node = nodelist.item(0);
				for (CaveSketchUploadListener listener : uploadListener) {
					listener.uploadCompleted(node.getFirstChild().toString());
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
					s.setBusy("uploading cave sketch ...");
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
				for (CaveSketchUploadListener listener : uploadListener) {
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
