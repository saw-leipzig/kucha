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
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.theme.base.client.listview.ListViewCustomAppearance;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.ImageEntry;
import de.cses.shared.PhotographerEntry;

public class ImageEditor implements IsWidget {

	private int currentImageID;
	private TextField titleField;
	private TextField copyrightField;
	private TextArea commentArea;
	private DateField dateField;
	private StringComboBox photographerSelection;
	private FramedPanel panel;
	private ComboBox<ImageEntry> imageCombo;
	private ListStore<ImageEntry> imageEntryList;
	private ImageProperties properties;
	private ArrayList<PhotographerEntry> photographerList;

	// private VBoxLayoutContainer vlc;
	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();
		LabelProvider<ImageEntry> title();
	}

	interface ComboBoxTemplates extends XTemplates {
		@XTemplate("<img width=\"150\" height=\"150\" src=\"{imageUri}\"> {title}")
		SafeHtml image(SafeUri imageUri, String title);

		// @XTemplate("<div qtip=\"{slogan}\" qtitle=\"State Slogan\">{name}</div>")
		// SafeHtml state(String slogan, String name);
	}

	public ImageEditor() {
		properties = GWT.create(ImageProperties.class);
		imageEntryList = new ListStore<ImageEntry>(properties.imageID());
		dbService.getPhotographer(new AsyncCallback<ArrayList<PhotographerEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<PhotographerEntry> result) {
				photographerList = result;
			}
		});
	}

	@Override
	public Widget asWidget() {
		if (panel == null) {
			refreshImages();
			initPanel();
		}
		return panel;
	}

	private void initPanel() {
		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		HBoxLayoutContainer hlc = new HBoxLayoutContainer(HBoxLayoutAlign.STRETCH);
		BoxLayoutData bld = new BoxLayoutData(new Margins(10));
		
    ListView<ImageEntry, ImageEntry> listView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
      @Override
      public void setValue(ImageEntry object, ImageEntry value) {
      }
    });
    listView.setCell(new SimpleSafeHtmlCell<ImageEntry>(
				new AbstractSafeHtmlRenderer<ImageEntry>() {
					final ComboBoxTemplates comboBoxTemplates = GWT.create(ComboBoxTemplates.class);

					public SafeHtml render(ImageEntry item) {
						SafeUri imageUri = UriUtils.fromString("http://kucha.informatik.hu-berlin.de/tomcat/images/depictions/" + item.getFilename());
//  					SafeUri imageUri = UriUtils.fromString(GWT.getModuleBaseURL() + "/images/depictions/");
						Info.display("loading images", "Image path:- " + GWT.getModuleBaseURL());
						return comboBoxTemplates.image(imageUri, item.getTitle());
					}

				}
    ));
    listView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ImageEntry>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<ImageEntry> event) {
				ImageEntry ie;
			  if (!event.getSelection().isEmpty()) {
			  	ie = event.getSelection().get(0);
			  	titleField.setText(ie.getTitle());
					copyrightField.setText(ie.getCopyright());
					commentArea.setText(ie.getComment());
					dateField.setValue(ie.getCaptureDate());
					photographerSelection.setText(photographerList.get(ie.getPhotographerID()).getName());
			  }
			}
    });
    listView.setBorders(false);
    
    titleField = new TextField();
    vlc.add(new FieldLabel(titleField, "Title"));
    
    copyrightField = new TextField();
		vlc.add(new FieldLabel(copyrightField, "Copyright"));

		commentArea = new TextArea();
		commentArea.setSize("300px", "50px");
		vlc.add(new FieldLabel(commentArea, "Comment"));

		dateField = new DateField(new DateTimePropertyEditor("dd.MM.yyyy"));
		vlc.add(new FieldLabel(dateField, "Date captured"));

		ArrayList<String> photographerList = new ArrayList<String>();
		photographerList.add("Roy Shneider");
		photographerList.add("Getty Images");
		photographerList.add("Frank o'Grapher");
		photographerSelection = new StringComboBox(photographerList);
		vlc.add(new FieldLabel(photographerSelection, "Photographer"));

		TextButton saveButton = new TextButton("save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveImage(currentImageID);
			}
		});

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

			}
		});
		
		HorizontalLayoutData layoutData = new HorizontalLayoutData(100, 100, new Margins(5));

    listView.setSize("200px", "350px");
    ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(listView);
    lf.setSize("200px", "350px");
//		hlc.setScrollMode(ScrollMode.AUTOY);
		hlc.add(lf);
		hlc.add(vlc);

		panel = new FramedPanel();
		panel.setHeading("Image Editor");
		panel.setButtonAlign(BoxLayoutPack.CENTER);
		panel.add(hlc, bld);
		panel.addButton(saveButton);
		panel.addButton(cancelButton);

	}

	private void refreshImages() {
//		Info.display("refreshImages()", "starting....");
		dbService.getImages(new AsyncCallback<ArrayList<ImageEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Refresh Image List", "failed");
			}

			@Override
			public void onSuccess(ArrayList<ImageEntry> result) {
				imageEntryList.clear();
				Info.display("Refresh Image List", "sucess");
				for (ImageEntry ie : result) {
					imageEntryList.add(ie);
				}
			}
		});
	}

	/**
	 * This method is loading the image form the database.
	 * 
	 * @param imageID
	 */
	private void loadImage(int imageID) {

	}

	/**
	 * This method will save the image information into the database.
	 * 
	 * @param imageID
	 */
	private void saveImage(int imageID) {

	}
}
