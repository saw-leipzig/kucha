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
import de.cses.shared.CaveEntry;

public class C14DocumentUploader implements IsWidget {
	
	public interface C14DocumentUploadListener {
		public void uploadCompleted(String documentFilename);
		public void uploadCanceled();
	}

	private FormPanel form;
	private ArrayList<C14DocumentUploadListener> uploadListener;
	protected Window uploadInfoWindow;
	private FileUploadField file;
	private FramedPanel mainPanel;
	private CaveEntry entry;
	protected String uploadedFilename;

	/**
	 * 
	 * @param correspondingCaveEntry the filename without postfix as it should be used on server side (make sure it's unique!)
	 * @param docTypeList all allowed docTypes listed by postfix (e.g. pdf, txt)
	 * @param listener
	 */
	public C14DocumentUploader(CaveEntry entry, C14DocumentUploadListener listener) {
		this.entry = entry;
		uploadListener = new ArrayList<C14DocumentUploadListener>();
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
		
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Document Uploader");
		
		ArrayList<String> docTypeList = new ArrayList<String>();
		docTypeList.add("pdf");
		docTypeList.add("txt");

		file = new FileUploadField();
		file.setName("uploadedC14Document");
		file.setAllowBlank(false);
		file.setPixelSize(300, 30);
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				String selected = file.getValue().toLowerCase();
				if ((selected.lastIndexOf(".") < 0) || !docTypeList.contains(selected.substring(selected.lastIndexOf(".")+1))) {
					Util.showWarning("Unsopported Document Type", "Supported document types: PDF, TXT");
					file.reset();
				}
			}
		});
		form = new FormPanel();
		form.setAction("c14DocumentUpload?docFileName=" + entry.getUniqueID() + "-c14" + "&caveID=" + entry.getCaveID());
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadInfoWindow.hide();
				Document doc = XMLParser.parse(event.getResults());
				NodeList nodelist = doc.getElementsByTagName("pre");
				Node node = nodelist.item(0);
				for (C14DocumentUploadListener listener : uploadListener) {
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

					uploadedFilename = file.getValue();
					form.submit();
					uploadInfoWindow = new Window();
					uploadInfoWindow.setHeading("Please wait!");
					uploadInfoWindow.setModal(true);
					uploadInfoWindow.setPixelSize(150, 50);
					uploadInfoWindow.setMaximizable(false);
					uploadInfoWindow.setClosable(false);
					Status s = new Status();
					s.setBusy("uploading document ...");
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
				for (C14DocumentUploadListener listener : uploadListener) {
					listener.uploadCanceled();
				}

			}
		});

		mainPanel.addButton(cancelButton);
		mainPanel.addButton(resetButton);
		mainPanel.addButton(submitButton);
	}

	public String getUploadedFilename() {
		return uploadedFilename;
	}

}
