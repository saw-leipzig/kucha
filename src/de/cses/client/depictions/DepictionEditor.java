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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;

public class DepictionEditor implements IsWidget, ImageSelectorListener {

	private DepictionProperties depictionProps;
	private ListStore<DepictionEntry> depictionEntryList;
//	private ComboBox<DepictionEntry> depictionSelection;
	
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
//	private Label iconographyField;
//	private Label imageField;
	protected IconographySelector iconographySelector;
	protected PictorialElementSelector peSelector;
	protected ImageSelector imageSelector;
	private FramedPanel mainPanel;
	private CenterLayoutContainer imageContainer;
	private BorderLayoutContainer borderLayoutContainer;
	protected PopupPanel imageSelectionDialog; 
	

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
		peSelector = new PictorialElementSelector();
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
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		FramedPanel attributePanel;
		
		mainPanel  = new FramedPanel();
		mainPanel.setHeading("Depiction Editor");
		
		HorizontalPanel hPanel = new HorizontalPanel();
		
		VerticalPanel vPanel = new VerticalPanel();
		
//		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
//		vlc.setScrollMode(ScrollMode.AUTOY);

//		depictionSelection = new ComboBox<DepictionEntry>(depictionEntryList, depictionProps.name(),
//				new AbstractSafeHtmlRenderer<DepictionEntry>() {
//
//					@Override
//					public SafeHtml render(DepictionEntry item) {
//						final DepictionViewTemplates pvTemplates = GWT.create(DepictionViewTemplates.class);
//						return pvTemplates.depiction("Cave: " + item.getCaveID() + " - Depiction: " + item.getDepictionID());
//					}
//				});
//		depictionSelection.setEmptyText("Select a Depiction ...");
//		depictionSelection.setTypeAhead(false);
//		depictionSelection.setEditable(false);
//		depictionSelection.setTriggerAction(TriggerAction.ALL);
//		depictionSelection.addSelectionHandler(new SelectionHandler<DepictionEntry>() {
//			
//			@Override
//			public void onSelection(SelectionEvent<DepictionEntry> event) {
//				DepictionEntry de = event.getSelectedItem();
//				setFields(de);
//			}
//		});
//		
//		vlc.add(new FieldLabel(depictionSelection, "Depiction"));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Style");
		styleField = new TextField();
		styleField.setWidth(65);
		attributePanel.add(styleField);
		attributePanel.setHeight(100);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Inscriptions");
		inscriptionsField = new TextField();
		inscriptionsField.setWidth(130);
		attributePanel.add(inscriptionsField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Dating");
		datingField = new TextField();
		datingField.setWidth(130);
		attributePanel.add(datingField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Dimension");
		VerticalPanel dimPanel = new VerticalPanel();
		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		widthField.setWidth(100);
		dimPanel.add(new FieldLabel(widthField, "width"));
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());
		heightField.setWidth(100);
		dimPanel.add(new FieldLabel(heightField, "height"));
		attributePanel.add(dimPanel);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date purchased");
		purchaseDateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		attributePanel.add(purchaseDateField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Vendor");
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Acquired by expedition");
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Date of acquisition");
		dateOfAcquisitionField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		attributePanel.add(dateOfAcquisitionField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Current location");
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Background colour");
		backgroundColourField = new TextField();
		backgroundColourField.setWidth(35);
		attributePanel.add(backgroundColourField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Material");
		materialField = new TextField();
		materialField.setWidth(130);
		attributePanel.add(materialField);
		vPanel.add(attributePanel);	
		
		TextButton saveButton = new TextButton("save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveDepictionEntry();
			}
		});
		saveButton.setSize("100", "100");
		vPanel.add(saveButton);

		hPanel.add(vPanel);
		vPanel = new VerticalPanel();

		imageSelector = new ImageSelector(ImageSelector.PHOTO, this);
		imageContainer = new CenterLayoutContainer();
		imageContainer.setPixelSize(250, 250);
		TextButton imageButton = new TextButton("Select Image");
//		imageButton.setPixelSize(100, 30);
		imageButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				imageSelectionDialog = new PopupPanel();
				new Draggable(imageSelectionDialog);
				imageSelectionDialog.add(imageSelector);
				imageSelectionDialog.setModal(true);
				imageSelectionDialog.center();
				imageSelectionDialog.show();
			}
		});
		VerticalPanel vp = new VerticalPanel();
		vp.add(imageContainer);
		vp.add(imageButton);
		vp.setSize("350", "300");
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Image");
		attributePanel.add(vp);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Description");
		descriptionArea = new TextArea();
		descriptionArea.setSize("350", "100");
		attributePanel.add(descriptionArea);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("General remarks");
		generalRemarksArea = new TextArea();
		generalRemarksArea.setSize("350", "100");
		attributePanel.add(generalRemarksArea);
		vPanel.add(attributePanel);	
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Other suggested identifications");
		othersSuggestedIdentificationsArea = new TextArea();
		othersSuggestedIdentificationsArea.setSize("350", "100");
		attributePanel.add(othersSuggestedIdentificationsArea);
		vPanel.add(attributePanel);	

		hPanel.add(vPanel);
		vPanel = new VerticalPanel();
		
		TextButton iconographyExpandButton = new TextButton("expand tree");
		iconographyExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographySelector.expandAll();
			}
		});
		TextButton iconographyCollapseButton = new TextButton("collapse tree");
		iconographyCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographySelector.collapseAll();
			}
		});
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Select Iconography");
		HorizontalPanel hp = new HorizontalPanel();
		hp.add(iconographyExpandButton);
		hp.add(iconographyCollapseButton);
		hp.setPixelSize(300, 30);
		vp = new VerticalPanel(); 
		vp.add(iconographySelector);
		vp.add(hp);
		attributePanel.add(vp);		
		vPanel.add(attributePanel);	
		
		TextButton peExpandButton = new TextButton("expand tree");
		peExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				peSelector.expandAll();
			}
		});
		TextButton peCollapseButton = new TextButton("collapse tree");
		peCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				peSelector.collapseAll();
			}
		});
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Pictorial Elements");
		hp = new HorizontalPanel();
		hp.add(peExpandButton);
		hp.add(peCollapseButton);
//		hp.setPixelSize(300, 30);
		vp = new VerticalPanel(); 
		vp.add(peSelector);
		vp.add(hp);
		attributePanel.add(vp);		
		vPanel.add(attributePanel);
		
		hPanel.add(vPanel);
		
//		hlc = new HorizontalLayoutContainer();
//		hlc.add(newButton, new HorizontalLayoutData(80, 40));
//		hlc.add(deleteButton, new HorizontalLayoutData(80, 40));
//		hlc.setPixelSize(200, 50);

//    borderLayoutContainer = new BorderLayoutContainer();
////    borderLayoutContainer.setPixelSize(800, 600);
//    borderLayoutContainer.setCenterWidget(hPanel);
//    borderLayoutContainer.setSouthWidget(saveButton);
//
		mainPanel.add(hPanel);
    mainPanel.setPixelSize(1000, 800);
	}

	protected void setFields(DepictionEntry de) {
		// TODO Auto-generated method stub
		
	}

	protected void newDepictionEntry() {
		refreshDepictionList();
	}

	protected void saveDepictionEntry() {
		// TODO Auto-generated method stub

	}

	@Override
	public void imageSelected(int imageID) {
		if (imageID != 0) {
			dbService.getImage(imageID, new AsyncCallback<ImageEntry>() {
				
				@Override
				public void onSuccess(ImageEntry result) {
					SafeUri imageUri = UriUtils
							.fromString("http://kucha.informatik.hu-berlin.de/tomcat/images/tn" + result.getFilename());
					Image img = new Image(imageUri);
					imageContainer.clear();
					imageContainer.add(img);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
		imageSelectionDialog.hide();
	}

}