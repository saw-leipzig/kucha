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

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.ImageEntry;

public class ImageSelector implements IsWidget {

	private ListStore<ImageEntry> imageEntryList;
	private ImageProperties properties;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ArrayList<ImageSelectorListener> selectorListener;

	public static final int PHOTO = 1;
	public static final int SKETCH = 2;
	public static final int MAP = 3;

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private FlowLayoutContainer imageContainer;
	private int imageTypeID;
	private TextField searchField;
	private StoreFilter<ImageEntry> searchFilter;
	private FramedPanel mainPanel = null;

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();
		LabelProvider<ImageEntry> title();
	}

	/**
	 * Creates the view how a thumbnail of an image entry will be shown currently
	 * we are relying on the url of the image until we have user management
	 * implemented and protect images from being viewed from the outside without
	 * permission
	 * 
	 * @author alingnau
	 *
	 */
	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"20\" src=\"{imageUri}\"><br> {title}<br> {shortName}")
		SafeHtml image(SafeUri imageUri, String title, String shortName);
	}

	/**
	 * 
	 * @param type
	 *          The type of image the selector should display for selection.
	 * @see ImageSelecor.PHOTO
	 * @see ImageSelector.SKETCH
	 * @see ImageSelector.MAP
	 * @param listener
	 */
	public ImageSelector(int imageTypeID, ImageSelectorListener listener) {
		this.imageTypeID = imageTypeID;
		selectorListener = new ArrayList<ImageSelectorListener>();
		selectorListener.add(listener);
		properties = GWT.create(ImageProperties.class);
		imageEntryList = new ListStore<ImageEntry>(properties.imageID());
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			refreshImages();
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Image Selector");
		mainPanel.setSize("750", "500");
		
		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});
		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=250");
				return imageViewTemplates.image(imageUri, item.getTitle(), item.getShortName());
			}

		}));

		imageListView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ImageEntry>() {

			@Override
			public void onSelectionChanged(SelectionChangedEvent<ImageEntry> event) {
				ImageEntry item = event.getSelection().get(0);
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID());
				Image img = new Image(imageUri);
				imageContainer.clear();
				imageContainer.add(img);
			}
		});

		imageListView.setSize("1.0", "1.0");

		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		
		lf.setSize("1.0", "1.0");

		TextButton previewButton = new TextButton("Preview");

		previewButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel previewPanel = new PopupPanel(true);
				previewPanel.add(imageContainer);
				previewPanel.center();
				previewPanel.show();
			}
		});

		TextButton selectButton = new TextButton("Select");

		selectButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				ImageEntry ie = imageListView.getSelectionModel().getSelectedItem();
				if (ie == null) {
					return;
				}
				for (ImageSelectorListener listener : selectorListener) {
					listener.imageSelected(imageListView.getSelectionModel().getSelectedItem().getImageID());
				}
			}
		});

		TextButton cancelButton = new TextButton("Cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				for (ImageSelectorListener listener : selectorListener) {
					listener.imageSelected(0);
				}
			}
		});

		/**
		 * here we add the search for image titles
		 */
		searchField = new TextField();
		searchField.setSize("1.0", ".5");
		searchFilter = new StoreFilter<ImageEntry>() {
			@Override
			public boolean select(Store<ImageEntry> store, ImageEntry parent, ImageEntry item) {
				if (item.getTitle().toLowerCase().contains(searchField.getCurrentValue().toLowerCase())) {
					return true;
				}
				return false;
			}
		};
		imageEntryList.addFilter(searchFilter);
		TextButton searchButton = new TextButton("search");
		searchButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				if (searchField.getCurrentValue() != null) {
					imageEntryList.addFilter(searchFilter);
					imageEntryList.setEnableFilters(true);
				}
			}
		});
		TextButton resetButton = new TextButton("reset");
		resetButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				imageEntryList.setEnableFilters(false);
				imageEntryList.removeFilter(searchFilter);
			}
		});

		imageContainer = new FlowLayoutContainer();
		imageContainer.setScrollMode(ScrollMode.AUTO);

//		HorizontalPanel hPanel = new HorizontalPanel();
		HorizontalLayoutContainer hlc = new HorizontalLayoutContainer();
//		VerticalPanel vPanel = new VerticalPanel();

		FramedPanel fp = new FramedPanel();
		fp.setHeading("Filter");
		fp.add(searchField);
		fp.addButton(searchButton);
		fp.addButton(resetButton);
		hlc.add(fp, new HorizontalLayoutData(.4, 1.0));

		fp = new FramedPanel();
		fp.setHeading("Images");
		fp.add(lf);
		hlc.add(lf, new HorizontalLayoutData(.6, 1.0));
//		vPanel.add(fp);


//		hlc.add(vPanel, new HorizontalLayoutData(.35, 1.0));
//		vPanel = new VerticalPanel();

//		fp = new FramedPanel();
//		fp.setHeading("Preview");
//		imageContainer.setPixelSize(400, 400);
//		fp.add(imageContainer);
//		hlc.add(fp, new HorizontalLayoutData(.65, 1.0));

		mainPanel.add(hlc);
		mainPanel.addButton(previewButton);
		mainPanel.addButton(selectButton);
		mainPanel.addButton(cancelButton);
	}

	/**
	 * refreshes the list of images which will automatically update the view of
	 * the thumbnails
	 * 
	 * @see imageEntryList
	 */
	private void refreshImages() {
		dbService.getImages("ImageTypeID=" + imageTypeID, new AsyncCallback<ArrayList<ImageEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Refresh Image List", "failed");
			}

			@Override
			public void onSuccess(ArrayList<ImageEntry> result) {
				imageEntryList.clear();
				// Info.display("Refresh Image List", "success");
				for (ImageEntry ie : result) {
					imageEntryList.add(ie);
				}
			}
		});
	}

}
