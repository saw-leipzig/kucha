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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;

public class ImageUploader implements IsWidget {

	private FormPanel form;
	private ArrayList<ImageUploadListener> uploadListener;
	protected Window uploadInfoWindow;
	private FileUploadField file;
	private VerticalLayoutContainer vlc;

	public ImageUploader(ImageUploadListener listener) {
		uploadListener = new ArrayList<ImageUploadListener>();
		uploadListener.add(listener);
	}

	@Override
	public Widget asWidget() {
		if (vlc == null) {
			initPanel();
		}
		form.reset();
		file.reset();
		return vlc;
	}

	private void initPanel() {
		
		vlc = new VerticalLayoutContainer();
//		VerticalLayoutData vLayoutData = new VerticalLayoutData(400, 150);
//		vlc.setLayoutData(vLayoutData);
		
		file = new FileUploadField();
//		file.addChangeHandler(new ChangeHandler() {
//			@Override
//			public void onChange(ChangeEvent event) {
//				Info.display("File Changed", "You selected " + file);
//			}
//		});
		file.setAllowBlank(false);

		form = new FormPanel();
		form.setAction("infosystem/imgUploader");
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
//		form.setPixelSize(500, 120);
		// file.setName(purpose);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
//				String resultHtml = event.getResults();
//				Info.display("Upload result", resultHtml);
				uploadInfoWindow.hide();
				for (ImageUploadListener listener : uploadListener) {
					//ToDo: send information after image upload for database
					listener.uploadCompleted();
				}
			}
		});
		form.add(new FieldLabel(file, "File"));
//		form.setSize("450px", "100px");
		// titleField = new TextField();
		// form.add(new FieldLabel(titleField, "Title"), new MarginData(64));
		form.setLayoutData(new MarginData(10, 0, 0, 0));
		vlc.add(form);
		
//		titleField = new TextField();
//		titleField.setWidth(300);
//		vlc.add(new FieldLabel(titleField, "Title"));

		TextButton submitButton = new TextButton("Upload");
		submitButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				// form.setAction("infosystem/imgUploader");
				// form.setEncoding(Encoding.MULTIPART);
				// form.setMethod(Method.POST);
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
		
		ButtonBar bb = new ButtonBar();
		bb.add(resetButton);
		bb.add(submitButton);
		
		vlc.add(bb);
		
		
//		FlowLayoutContainer fl = new FlowLayoutContainer();
//		fl.setScrollMode(ScrollMode.NONE);
//		fl.add(form);
//		fl.add(bb);	
//		fl.setBounds(0, 0, 600, 200);

//    BorderLayoutData west = new BorderLayoutData(150);
//    west.setMargins(new Margins(5));
//    MarginData center = new MarginData();
//    BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
//    borderLayoutContainer.setBounds(0, 0, 600, 200);
//    borderLayoutContainer.setWestWidget(imageContentPanel, west);
//    borderLayoutContainer.setCenterWidget(vlc, center);

//		panel = new ContentPanel();
//		panel.setHeading("Upload image");
//    panel.setPixelSize(610, 210);
//    panel.setPosition(5, 5);
//		panel.add(vlc);

	}

}
