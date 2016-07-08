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

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.thirdparty.javascript.rhino.Node.FileLevelJsDocBuilder;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent;
import com.sencha.gxt.widget.core.client.event.SubmitCompleteEvent.SubmitCompleteHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FileUploadField;
import com.sencha.gxt.widget.core.client.form.FormPanel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.FormPanel.Encoding;
import com.sencha.gxt.widget.core.client.form.FormPanel.Method;
import com.sencha.gxt.widget.core.client.info.Info;

public class ImageUploader implements IsWidget {

	protected static final int MAX_HEIGHT = 250;
	protected static final int MAX_WIDHT = 500;
	protected static final int MIN_HEIGHT = 150;
	protected static final int MIN_WIDTH = 350;
	
	public static final String DEPICTIONS = "depictions";
	public static final String BACKGROUNDS = "backgrounds";
	public static final String SKETCHES = "sketches";

	private FramedPanel panel;
	private FormPanel form;
	private String purpose;
	private TextField titleField;
	protected AutoProgressMessageBox amb;

	public ImageUploader(String purpose) {
		this.purpose = purpose;
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
		file.setName(purpose);
		
		form.addSubmitCompleteHandler(new SubmitCompleteHandler() {
	    public void onSubmitComplete(SubmitCompleteEvent event) {
	      String resultHtml = event.getResults();
	      amb.hide();
	    }
	  });
		form.add(new FieldLabel(file, "File"));
		form.setSize("450px", "100px");
//		titleField = new TextField();
//		form.add(new FieldLabel(titleField, "Title"), new MarginData(64));
		

		TextButton submitButton = new TextButton("Submit");
		submitButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
//				form.setAction("infosystem/imgUploader");
//				form.setEncoding(Encoding.MULTIPART);
//				form.setMethod(Method.POST);
				if (form.isValid()) {
					form.submit();
					amb = new AutoProgressMessageBox("File upload", "please wait ...");
					amb.show();
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
		panel.setHeading("Upload "+purpose+" image");
		panel.setButtonAlign(BoxLayoutPack.CENTER);
		panel.add(form);
		panel.addButton(resetButton);
		panel.addButton(submitButton);
		
//		panel.setWidth(MIN_WIDTH);
//		panel.setHeight(MAX_HEIGHT);		
	}

}
