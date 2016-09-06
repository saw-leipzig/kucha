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
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.ImageEntry;
import de.cses.shared.PhotographerEntry;

public class ImageEditor implements IsWidget, ImageUploadListener {

	private TextField titleField;
	private TextField copyrightField;
	private TextArea commentArea;
	private DateField dateField;
	private ComboBox<PhotographerEntry> photographerSelection;
	private ContentPanel panel;
	private ListStore<ImageEntry> imageEntryList;
	private ImageProperties properties;
	private PhotographerProperties photographerProps;
	private ListStore<PhotographerEntry> photographerEntryList;
	private ListView<ImageEntry, ImageEntry> imageListView;

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField searchField;
	private StoreFilter<ImageEntry> searchFilter;
	private StoreFilter<ImageEntry> newImageFilter;
	private RadioButton rbPhoto;
	private RadioButton rbSketch;
	private RadioButton rbMap;

//	protected ImageEntry selectedImageItem;
	// protected PhotographerEntry selectedPhotographerItem;

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();
	}

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();

		LabelProvider<PhotographerEntry> name();
	}

	/**
	 * creates the view how a thumbnail of an image entry will be shown currently
	 * we are relying on the url of the image until we have user management
	 * implemented and protect images from being viewed from the outside without
	 * permission
	 * 
	 * @author alingnau
	 *
	 */
	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" width=\"150\" height=\"150\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);

		// @XTemplate("<div qtip=\"{slogan}\" qtitle=\"State Slogan\">{name}</div>")
		// SafeHtml state(String slogan, String name);
	}

	interface PhotographerViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml photographer(String name);
	}

	/**
	 * This widget allows to edit the information of an ImageEntry, i.e. an image
	 * in the database.
	 */
	public ImageEditor() {
		properties = GWT.create(ImageProperties.class);
		photographerProps = GWT.create(PhotographerProperties.class);
		imageEntryList = new ListStore<ImageEntry>(properties.imageID());
		newImageFilter = new StoreFilter<ImageEntry>() {
			@Override
			public boolean select(Store<ImageEntry> store, ImageEntry parent, ImageEntry item) {
				if ("New Image".equals(item.getTitle())) 
					return true;
				return false;
			}
		};
		
		photographerEntryList = new ListStore<PhotographerEntry>(photographerProps.photographerID());

		dbService.getPhotographer(new AsyncCallback<ArrayList<PhotographerEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PhotographerEntry> result) {
				photographerEntryList.clear();
				for (PhotographerEntry pe : result) {
					photographerEntryList.add(pe);
				}
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

	/**
	 * Initialises the editor's panel if it this has not already been done. Should
	 * usually only be called once a session is started!
	 */
	private void initPanel() {

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(150, 300, new Margins(15, 5, 0, 0));
		vlc.setLayoutData(vLayoutData);
		
		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList,
				new IdentityValueProvider<ImageEntry>() {
					@Override
					public void setValue(ImageEntry object, ImageEntry value) {
					}
				});
		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils
						.fromString("http://kucha.informatik.hu-berlin.de/tomcat/images/tn" + item.getFilename());
				return imageViewTemplates.image(imageUri, item.getTitle());
			}

		}));
		
		imageListView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ImageEntry>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<ImageEntry> event) {
				if (!event.getSelection().isEmpty()) {
					ImageEntry selectedImageItem = event.getSelection().get(0);
					titleField.setValue(selectedImageItem.getTitle());
					copyrightField.setValue(selectedImageItem.getCopyright());
					commentArea.setValue(selectedImageItem.getComment());
					dateField.setValue(selectedImageItem.getCaptureDate());
					photographerSelection.setValue(
							photographerEntryList.findModelWithKey(Integer.toString(selectedImageItem.getPhotographerID())), true);
					switch (selectedImageItem.getType()) {
					case "photo":
						rbPhoto.setValue(true);
						break;
					case "sketch":
						rbSketch.setValue(true);
						break;
					case "map":
						rbMap.setValue(true);
						break;
					default:
						break;
					}
				}
			}
		});

		imageListView.setBorders(true);

		titleField = new TextField();
		titleField.setWidth(300);
		vlc.add(new FieldLabel(titleField, "Title"));

		copyrightField = new TextField();
		copyrightField.setWidth(300);
		vlc.add(new FieldLabel(copyrightField, "Copyright"));

		commentArea = new TextArea();
		commentArea.setSize("300px", "100px");
		vlc.add(new FieldLabel(commentArea, "Comment"));

		dateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		vlc.add(new FieldLabel(dateField, "Date captured"));

		photographerSelection = new ComboBox<PhotographerEntry>(photographerEntryList, photographerProps.name(),
				new AbstractSafeHtmlRenderer<PhotographerEntry>() {

					@Override
					public SafeHtml render(PhotographerEntry item) {
						final PhotographerViewTemplates pvTemplates = GWT.create(PhotographerViewTemplates.class);
						return pvTemplates.photographer(item.getName());
					}
				});
		photographerSelection.setEmptyText("Select a Photographer ...");
		photographerSelection.setTypeAhead(false);
		photographerSelection.setEditable(false);
		photographerSelection.setTriggerAction(TriggerAction.ALL);

		vlc.add(new FieldLabel(photographerSelection, "Photographer"));

		final String IMAGE_TYPE_GROUP = "imageTypeSelection";
		rbPhoto = new RadioButton(IMAGE_TYPE_GROUP, "Photo");
		rbSketch = new RadioButton(IMAGE_TYPE_GROUP, "Sketch");
		rbMap = new RadioButton(IMAGE_TYPE_GROUP, "Map");
		FlowLayoutContainer radioButtonContainer = new FlowLayoutContainer();
		MarginData radioButtonLayoutData = new MarginData(10, 5, 10, 5);
		radioButtonContainer.add(rbPhoto, radioButtonLayoutData);
		radioButtonContainer.add(rbSketch, radioButtonLayoutData);
		radioButtonContainer.add(rbMap, radioButtonLayoutData);
		vlc.add(new FieldLabel(radioButtonContainer, "Image Type"));

		TextButton saveButton = new TextButton("save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveSelectedImageEntry();
			}
		});

		TextButton deleteButton = new TextButton("delete");
		deleteButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				deleteSelectedImageEntry();
			}
		});
		
		ButtonBar bb = new ButtonBar();
		bb.add(saveButton);
		bb.add(deleteButton);		
		vlc.add(bb);
		
		ImageUploader imgUploader = new ImageUploader(this);
		vlc.add(imgUploader);

		imageListView.setSize("240", "340");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("250", "350");
		
    BorderLayoutData west = new BorderLayoutData(150);
    west.setMargins(new Margins(5));

    BorderLayoutData south = new BorderLayoutData();
    south.setMargins(new Margins(5));
    
    /**
     * here we add the search for image titles
     */
    searchField = new TextField();
    searchField.setSize("150", "50");
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
    FlowLayoutContainer searchLayoutContainer = new FlowLayoutContainer();
    MarginData searchLayoutData = new MarginData(new Margins(5, 0, 5, 0));
    searchLayoutContainer.add(searchField, searchLayoutData);
    searchLayoutContainer.add(searchButton, searchLayoutData);
    searchLayoutContainer.add(resetButton, searchLayoutData);
    
    MarginData center = new MarginData();

    BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
//    BorderLayoutData bld = new BorderLayoutData();
    borderLayoutContainer.setBounds(0, 0, 600, 500);
    borderLayoutContainer.setWestWidget(lf, west);
    borderLayoutContainer.setCenterWidget(vlc, center);
    borderLayoutContainer.setSouthWidget(searchLayoutContainer, south);

    panel = new ContentPanel();
//    panel.setHeading("Horizontal Box Layout");
    /** here we set the size and position, but be careful
     * to make it larger than the widget that is inserted
     */
    panel.setPixelSize(610, 510);
    panel.setBounds(0, 0, 610, 510);
    panel.setPosition(5, 5);
		panel.setHeading("Image Editor");
    panel.add(borderLayoutContainer);		
		
	}

	/**
	 * Deletes the currently selected ImageEntry (shows a yes/no dialog first)
	 */
	private void deleteSelectedImageEntry() {
		if (imageListView.getSelectionModel().getSelectedItem() != null) {
			Dialog simple = new Dialog();
			simple.setHeading("Delete");
			simple.setWidth(300);
			simple.setResizable(false);
			simple.setHideOnButtonClick(true);
			simple.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
			simple.setBodyStyleName("pad-text");
			simple.getBody().addClassName("pad-text");
			simple.add(new Label("Do you really want to delete the selected entry?"));
			simple.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					// only of the yes button is selected, we will perform the command
					// to simplify we just ignore the no button event by doing nothing
					int imageID = imageListView.getSelectionModel().getSelectedItem().getImageID();
					dbService.updateEntry("DELETE FROM Images WHERE ImageID=" + imageID,
							new AsyncCallback<Boolean>() {
								public void onFailure(Throwable caught) {
									Info.display("Error", "Problem with database connection!");
								}

								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										Info.display("Image information", "Image information has been updated!");
									} else {
										Info.display("Image information", "Image information has been updated!");
									}
									refreshImages();
									imageListView.getSelectionModel().select(imageEntryList.get(0), true);
								}
							});
				}
			});
			simple.show();
			// constrain the dialog to the viewport (for small mobile screen sizes)
			Rectangle bounds = simple.getElement().getBounds();
			Rectangle adjusted = simple.getElement().adjustForConstraints(bounds);
			if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
				simple.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
			}
		}
	}

	/**
	 * refreshes @imageEntryList which will automatically update the view of the
	 * thumbnails
	 */
	private void refreshImages() {
		// Info.display("refreshImages()", "starting....");
		dbService.getImages(new AsyncCallback<ArrayList<ImageEntry>>() {

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

	/**
	 * This method will save the currently selected ImageEntry from the left list
	 * of previews. In future versions, the missing fields will be added. Also,
	 * the Photographer ID us currently not mapped to the text entry in this box.
	 * (shows a yes/no dialog first)
	 */
	private void saveSelectedImageEntry() {
		ImageEntry selectedItem = imageListView.getSelectionModel().getSelectedItem();
		if (selectedItem == null) {
			Info.display("Save Image", "There is no image selected for editing!");
			return;
		} else if ("New Image".equals(titleField.getCurrentValue())) {
			Dialog warning = new Dialog();
			warning.setHeading("A problem occurred!");
			warning.setWidth(300);
			warning.setResizable(false);
			warning.setHideOnButtonClick(true);
			warning.setPredefinedButtons(PredefinedButton.OK);
			warning.setBodyStyleName("pad-text");
			warning.getBody().addClassName("pad-text");
			warning.add(new Label(
					"Please change at least the title of the uploaded image! If necessary, all other information can be changed at a later time."));
			warning.show();
			// constrain the dialog to the viewport (for small mobile screen sizes)
			Rectangle bounds = warning.getElement().getBounds();
			Rectangle adjusted = warning.getElement().adjustForConstraints(bounds);
			if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
				warning.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
			}
			return;
		}

		Dialog simple = new Dialog();
		simple.setHeading("Save");
		simple.setWidth(300);
		simple.setResizable(false);
		simple.setHideOnButtonClick(true);
		simple.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
		simple.setBodyStyleName("pad-text");
		simple.getBody().addClassName("pad-text");
		simple.add(new Label(
				"Saving will overwrite the existing information in the Database. This cannot be reversed! Do you want to continue?"));
		simple.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				// only of the yes button is selected, we will perform the command
				// to simplify we just ignore the no button event by doing nothing
				DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
				String sqlUpdate = "UPDATE Images SET Title='" + titleField.getText() + "'";
				if (copyrightField.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",Copyright='" + copyrightField.getText() + "'");
				}
				if (photographerSelection.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",PhotographerID=" + photographerSelection.getValue().getPhotographerID());
				}
				if (commentArea.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",Comment='" + commentArea.getText() + "'");
				}
				if (dateField.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",CaptureDate='" + dtf.format(dateField.getValue()) + "'");
				}
				if (rbPhoto.getValue()) {
					sqlUpdate = sqlUpdate.concat(",ImageType='photo'");
				} else if (rbSketch.getValue()) {
					sqlUpdate = sqlUpdate.concat(",ImageType='sketch'");
				} else if (rbMap.getValue()) {
					sqlUpdate = sqlUpdate.concat(",ImageType='map'");
				}
				
				sqlUpdate = sqlUpdate.concat(" WHERE ImageID=" + imageListView.getSelectionModel().getSelectedItem().getImageID());

				dbService.updateEntry(sqlUpdate, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						Info.display("Error", "Problem with database connection!");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							Info.display("Image information", "Image information has been updated!");
						} else {
							Info.display("Image information", "Image information has been updated!");
						}
						imageEntryList.setEnableFilters(false);
						imageEntryList.removeFilter(newImageFilter);
						refreshImages();
					}
				});
			}
		});
		simple.show();
		// constrain the dialog to the viewport (for small mobile screen sizes)
		Rectangle bounds = simple.getElement().getBounds();
		Rectangle adjusted = simple.getElement().adjustForConstraints(bounds);
		if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
			simple.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
		}

	}

	@Override
	public void uploadCompleted() {
		imageEntryList.addFilter(newImageFilter);
		imageEntryList.setEnableFilters(true);
		refreshImages();
//		Iterator<ImageEntry> list = imageEntryList.getAll().iterator();
		imageListView.getSelectionModel().select(imageEntryList.get(0), true);
//		ImageEntry ie;
//		while (list.hasNext()) {
//			ie = list.next();
//			if ("New Image".equals(ie.getTitle())) {
////				Info.display("New Image", "imageID = " + ie.getImageID());
////				imageListView.getSelectionModel().select(ie, false);
////				imageListView.getSelectionModel().select(imageEntryList.findModelWithKey(Integer.toString(ie.getImageID())), false);
//				imageListView.getSelectionModel().select(imageEntryList.indexOf(ie), false);
//				return;
//			}
//		}
	}

}
