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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Status;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.info.Info;

public class ImageUploader implements IsWidget {

	protected static final int MAX_HEIGHT = 250;
	protected static final int MAX_WIDHT = 500;
	protected static final int MIN_HEIGHT = 150;
	protected static final int MIN_WIDTH = 350;

	private FramedPanel panel;
	private FormPanel form;
	private ArrayList<ImageUploadListener> uploadListener;
	protected Window uploadInfoWindow;

	public ImageUploader(ImageUploadListener listener) {
		uploadListener = new ArrayList<ImageUploadListener>();
		uploadListener.add(listener);
	}

	@Override
	public Widget asWidget() {
		if (panel == null) {
			initPanel();
		}
		return panel;
	}

	private void initPanel() {
		final FileUploadField file = new FileUploadField();
		file.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				Info.display("File Changed", "You selected " + file.getValue());
			}
		});
		file.setAllowBlank(false);

		form = new FormPanel();
		form.setAction("infosystem/imgUploader");
		form.setEncoding(Encoding.MULTIPART);
		form.setMethod(Method.POST);
		// file.setName(purpose);

		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
			public void onSubmitComplete(SubmitCompleteEvent event) {
				String resultHtml = event.getResults();
				uploadInfoWindow.hide();
				for (ImageUploadListener listener : uploadListener) {
					listener.uploadCompleted();
				}
			}
		});
		form.add(new FieldLabel(file, "File"));
		form.setSize("450px", "100px");
		// titleField = new TextField();
		// form.add(new FieldLabel(titleField, "Title"), new MarginData(64));

		TextButton submitButton = new TextButton("Submit");
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

		panel = new FramedPanel();
		panel.setHeading("Upload image");
		panel.setButtonAlign(BoxLayoutPack.CENTER);
		panel.add(form);
		panel.addButton(resetButton);
		panel.addButton(submitButton);

		// panel.setWidth(MIN_WIDTH);
		// panel.setHeight(MAX_HEIGHT);

	}

}
