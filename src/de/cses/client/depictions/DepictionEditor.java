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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.theme.base.client.field.FieldLabelDefaultAppearance;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FieldLabel.FieldLabelOptions;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.DepictionEntry;
import de.cses.shared.IconographyEntry;

public class DepictionEditor implements IsWidget {

	private DepictionProperties depictionProps;
	private ListStore<DepictionEntry> depictionEntryList;
	private ContentPanel panel;
	private ComboBox<DepictionEntry> depictionSelection;
	
	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField styleField;
	private TextField inscriptionsField;
	private TextField datingField;
	private NumberField<Double> widthField;
	private NumberField<Double> heightField;
	private DateField purchaseDateField;
	private DateField dateOfAcquisitionField;
	private TextArea descriptionArea;
	private TextField backgroundColourField;
	private TextField materialField;
	private TextArea generalRemarksArea;
	private TextArea othersSuggestedIdentificationsArea;
	private Label iconographyField;
	protected IconographySelector iconographySelector;
	

	interface DepictionProperties extends PropertyAccess<DepictionEntry> {
		ModelKeyProvider<DepictionEntry> depictionID();
		LabelProvider<DepictionEntry> name();
	}

	interface DepictionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml depiction(String name);
	}

	public DepictionEditor() {
		depictionProps = GWT.create(DepictionProperties.class);
		depictionEntryList = new ListStore<DepictionEntry>(depictionProps.depictionID());
		refreshDepictionList();
		iconographySelector = new IconographySelector(); 
	}

	private void refreshDepictionList() {
		dbService.getDepictions(new AsyncCallback<ArrayList<DepictionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Refresh Depictions List", "failed");
			}

			@Override
			public void onSuccess(ArrayList<DepictionEntry> result) {
				depictionEntryList.clear();
				for (DepictionEntry de : result) {
					depictionEntryList.add(de);
				}
			}
			
		});
	}

	@Override
	public Widget asWidget() {
		if (panel == null) {
			initPanel();
		}
		return panel;
	}

	private void initPanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(250, 350, new Margins(20, 10, 5, 5));
		vlc.setLayoutData(vLayoutData);
		vlc.setScrollMode(ScrollMode.AUTOY);

		depictionSelection = new ComboBox<DepictionEntry>(depictionEntryList, depictionProps.name(),
				new AbstractSafeHtmlRenderer<DepictionEntry>() {

					@Override
					public SafeHtml render(DepictionEntry item) {
						final DepictionViewTemplates pvTemplates = GWT.create(DepictionViewTemplates.class);
						return pvTemplates.depiction("Cave: " + item.getCaveID() + " - Depiction: " + item.getDepictionID());
					}
				});
		depictionSelection.setEmptyText("Select a Depiction ...");
		depictionSelection.setTypeAhead(false);
		depictionSelection.setEditable(false);
		depictionSelection.setTriggerAction(TriggerAction.ALL);
		depictionSelection.addSelectionHandler(new SelectionHandler<DepictionEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<DepictionEntry> event) {
				DepictionEntry de = event.getSelectedItem();
				setFields(de);
			}
		});
		
		vlc.add(new FieldLabel(depictionSelection, "Depiction"));

		styleField = new TextField();
		styleField.setWidth(65);
		vlc.add(new FieldLabel(styleField, "Style"));
		
		inscriptionsField = new TextField();
		inscriptionsField.setWidth(130);
		vlc.add(new FieldLabel(inscriptionsField, "Inscriptions"));
		
		datingField = new TextField();
		datingField.setWidth(130);
		vlc.add(new FieldLabel(datingField, "Dating"));

		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthField.setWidth(100);
		vlc.add(new FieldLabel(widthField, "width"));
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightField.setWidth(100);
		vlc.add(new FieldLabel(heightField, "height"));
		
		purchaseDateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		vlc.add(new FieldLabel(purchaseDateField, "Date purchased"));
		
		vlc.add(new Label("Vendor"));

		vlc.add(new Label("Acquired by expedition"));
		
		dateOfAcquisitionField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		FieldLabel label = new FieldLabel(dateOfAcquisitionField, "Date of acquisition");
		label.setLabelWidth(150);
		vlc.add(label);
		
		vlc.add(new Label("Current location"));
		
		descriptionArea = new TextArea();
		descriptionArea.setSize("300", "100");
		vlc.add(new FieldLabel(descriptionArea, "Description"));
		
		backgroundColourField = new TextField();
		backgroundColourField.setWidth(35);
		vlc.add(new FieldLabel(backgroundColourField, "Background clolour"));
		
		materialField = new TextField();
		materialField.setWidth(130);
		vlc.add(new FieldLabel(materialField, "Material"));
		
		generalRemarksArea = new TextArea();
		generalRemarksArea.setSize("300", "100");
		vlc.add(new FieldLabel(generalRemarksArea, "General remarks"));
		
		othersSuggestedIdentificationsArea = new TextArea();
		othersSuggestedIdentificationsArea.setSize("300", "100");
		vlc.add(new FieldLabel(othersSuggestedIdentificationsArea, "Other suggested identifications"));
		
		iconographyField = new Label("select iconography...");
		iconographyField.setSize("150", "50");
		TextButton iconographyButton = new TextButton("select iconography");
		iconographyButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				Dialog id = new Dialog();
				id.setHeading("Select Iconography");
				id.setWidget(iconographySelector);
				id.setBodyStyle("fontWeight:bold;padding:13px;");
				id.setPixelSize(610, 510);
				id.setHideOnButtonClick(true);
				id.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
				id.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {
					
					private IconographyEntry selectedIconography;

					@Override
					public void onSelect(SelectEvent event) {
						selectedIconography = iconographySelector.getSelectedIconography();
						if (selectedIconography != null) {
							iconographyField.setText(selectedIconography.getText());
						}
					}
				});
				id.show();
			}
		});
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
		hlc.add(iconographyField, new HorizontalLayoutData(300, 60));
		hlc.add(iconographyButton, new HorizontalLayoutData(80, 40));
		hlc.setPixelSize(250, 80);
		vlc.add(new FieldLabel(hlc, "Iconography"));
		
		TextButton newButton = new TextButton("new entry");
		newButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				newDepictionEntry();
			}
		});

		TextButton deleteButton = new TextButton("delete entry");
		deleteButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				deleteSelectedDepictionEntry();
			}
		});
		
		hlc = new HorizontalLayoutContainer();
		hlc.add(newButton, new HorizontalLayoutData(80, 40));
		hlc.add(deleteButton, new HorizontalLayoutData(80, 40));
		hlc.setPixelSize(200, 50);
		
    MarginData center = new MarginData();

    BorderLayoutData south = new BorderLayoutData();
    south.setMargins(new Margins(10, 20, 10, 10));
    
    BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
//    BorderLayoutData bld = new BorderLayoutData();
//    borderLayoutContainer.setBounds(0, 0, 605, 505);
    borderLayoutContainer.setCenterWidget(vlc, center);
    borderLayoutContainer.setSouthWidget(hlc);

		panel = new ContentPanel();
		panel.setPixelSize(610, 510);
		panel.setBounds(0, 0, 610, 510);
		panel.setPosition(5, 5);
		panel.setHeading("Depiction Editor");
		panel.add(borderLayoutContainer);
	}

	protected void setFields(DepictionEntry de) {
		// TODO Auto-generated method stub
		
	}

	protected void newDepictionEntry() {
		refreshDepictionList();
	}

	protected void deleteSelectedDepictionEntry() {
		// TODO Auto-generated method stub

	}

}