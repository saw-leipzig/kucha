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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.caves.CaveEditor.CaveTypeProperties;
import de.cses.client.caves.CaveEditor.CaveTypeViewTemplates;
import de.cses.client.user.UserLogin;
import de.cses.shared.Cave3DModelEntry;
import de.cses.shared.CaveTypeEntry;

public class Cave3DModelUploader implements IsWidget {
	
	public interface Cave3DModelUploadListener {
		public void uploadCompleted(Cave3DModelEntry cave3DModelEntry);
		public void uploadCanceled();
	}

	private FormPanel form;
	private ArrayList<Cave3DModelUploadListener> uploadListener;
	protected Window uploadInfoWindow;
	private FileUploadField file;
	private FramedPanel mainPanel;
	private TextField caveGroup3DModelTitleField;
	private ArrayList<String> typeList;
	private int caveTypeID;
	private int cave3DModelID = -1;
	protected String selectedFile;
	protected String modelType;
	private String title;
	private String filename;
	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveType3DModelEntryListStore;
	private CaveTypeViewTemplates ctvTemplates;
	private ComboBox<CaveTypeEntry> caveType3DModelSelectionCB;


	public Cave3DModelUploader(String title, int caveTypeID, Cave3DModelUploadListener listener) {
		this.caveTypeID = caveTypeID;
		uploadListener = new ArrayList<Cave3DModelUploadListener>();
		uploadListener.add(listener);
		this.title = title;
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
		typeList.add("glb");

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Cave 3D Model Uploader");
		mainPanel.setWidth(300);
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveType3DModelEntryListStore = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		ctvTemplates = GWT.create(CaveTypeViewTemplates.class);
		for (CaveTypeEntry pe : StaticTables.getInstance().getCaveTypeEntries().values()) {
			caveType3DModelEntryListStore.add(pe);
		}

		FramedPanel new3DModelFP = new FramedPanel();
		new3DModelFP.setHeading("3D Model Title");
		caveGroup3DModelTitleField = new TextField();
		caveGroup3DModelTitleField.setValue("");
		caveGroup3DModelTitleField.setWidth(200);
		new3DModelFP.add(caveGroup3DModelTitleField);
		FramedPanel caveType3DModelFP = new FramedPanel();
		caveType3DModelFP.setHeading("Cave Type");
		caveType3DModelSelectionCB = new ComboBox<CaveTypeEntry>(caveType3DModelEntryListStore, caveTypeProps.nameEN(),
				new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

					@Override
					public SafeHtml render(CaveTypeEntry item) {
						return ctvTemplates.caveTypeLabel(item.getNameEN());
					}
				});
		caveType3DModelSelectionCB.setEmptyText("select cave type");
		caveType3DModelSelectionCB.setTypeAhead(false);
		caveType3DModelSelectionCB.setEditable(false);
		caveType3DModelSelectionCB.setTriggerAction(TriggerAction.ALL);
		caveType3DModelSelectionCB.addSelectionHandler(new SelectionHandler<CaveTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveTypeEntry> event) {
				caveTypeID = event.getSelectedItem().getCaveTypeID();
			}
		});
		caveType3DModelSelectionCB.select(0);
		caveType3DModelFP.add(caveType3DModelSelectionCB);
		FramedPanel new3DModelFileFP = new FramedPanel();
		new3DModelFileFP.setHeading("3D Model File");
		
		file = new FileUploadField();
		file.setName("uploaded3dModel");
		file.setAllowBlank(false);
		file.addChangeHandler(new ChangeHandler() {
			
			@Override
			public void onChange(ChangeEvent event) {
				filename = file.getValue().toLowerCase();
				selectedFile = file.getValue().toLowerCase();
				int startIdx = Math.max(selectedFile.lastIndexOf("\\"), selectedFile.lastIndexOf("/"));
				selectedFile = selectedFile.substring(startIdx>0 ? startIdx+1 : 0, selectedFile.length());
				modelType = filename.substring(filename.lastIndexOf(".")+1);
				if ((filename.lastIndexOf(".") < 0) || !typeList.contains(modelType)) {
					Util.showWarning("Unsopported 3d Model Type", "Please select glb!");
					file.reset();
				}
			}
		});
		new3DModelFileFP.add(file);
		VerticalLayoutContainer newCave3DModelCommentsVLC = new VerticalLayoutContainer();
		newCave3DModelCommentsVLC.add(new3DModelFP, new VerticalLayoutData(1.0, .34));
		newCave3DModelCommentsVLC.add(new3DModelFileFP, new VerticalLayoutData(1.0, .33));
		newCave3DModelCommentsVLC.add(caveType3DModelFP, new VerticalLayoutData(1.0, .33));

		form = new FormPanel();
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				uploadInfoWindow.hide();
				Document doc = XMLParser.parse(event.getResults());
				NodeList nodelist = doc.getElementsByTagName("pre");
				Node node = nodelist.item(0);
				for (Cave3DModelUploadListener listener : uploadListener) {
					int newCaveSketchID = Integer.parseInt(node.getFirstChild().toString());
					listener.uploadCompleted(new Cave3DModelEntry(newCaveSketchID, title, filename, caveTypeID, false));
				}
			}
		});
		form.add(newCave3DModelCommentsVLC);
		form.setLayoutData(new MarginData(10, 0, 0, 0));
		mainPanel.add(form);

		TextButton submitButton = new TextButton("Upload");
		submitButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (form.isValid()) {
					update();
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
				for (Cave3DModelUploadListener listener : uploadListener) {
					// ToDo: send information after image upload for database
					listener.uploadCanceled();
				}

			}
		});

		mainPanel.addButton(cancelButton);
		mainPanel.addButton(resetButton);
		mainPanel.addButton(submitButton);
	}
	public void update() {
		if (cave3DModelID<0) {
			Util.doLogging("Image submiting triggered. " + selectedFile);
			form.setAction("cave3DModelUpload?3DModelFilename="+selectedFile+"&title="+caveGroup3DModelTitleField.getValue()+"&caveTypeID="+Integer.toString(caveType3DModelSelectionCB.getValue().getCaveTypeID())+"&modifiedBy="+UserLogin.getInstance().getUsername()+"&sessionID="+UserLogin.getInstance().getSessionID());
		}
		else {
			form.setAction("cave3DModelUpload?3DModelFilename="+selectedFile+"&title="+caveGroup3DModelTitleField.getValue()+"&caveTypeID="+Integer.toString(caveType3DModelSelectionCB.getValue().getCaveTypeID())+"&hasID="+Integer.toString(cave3DModelID)+"&modifiedBy="+UserLogin.getInstance().getUsername()+"&sessionID="+UserLogin.getInstance().getSessionID());
		}
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setCaveTypeID(int caveTypeID) {
		this.caveTypeID = caveTypeID;
	}

}
