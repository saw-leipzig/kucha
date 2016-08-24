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
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
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

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	protected ImageEntry selectedImageItem;
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
		@XTemplate("<img align=\"center\" width=\"150\" height=\"150\" src=\"{imageUri}\"><br> {title}")
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
	 * Initialises the editor's panle if it this has not already been done. Should
	 * usually only be called once a session is started!
	 */
	private void initPanel() {
    BoxLayoutData vBoxData = new BoxLayoutData();
    vBoxData.setMargins(new Margins(5, 5, 5, 5));
    vBoxData.setMinSize(100);
    vBoxData.setMaxSize(500);
    vBoxData.setFlex(0);
		
//		VBoxLayoutContainer vlc = new VBoxLayoutContainer();
//		vlc.setPadding(new Padding(5));
//		vlc.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCHMAX);	
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(150, 300);
		vlc.setLayoutData(vLayoutData);
//		vlc.setScrollMode(ScrollMode.AUTOY);
		
//		HBoxLayoutContainer hlc = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
//		BoxLayoutData bld = new BoxLayoutData(new Margins(10));

		ListView<ImageEntry, ImageEntry> imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList,
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
					selectedImageItem = event.getSelection().get(0);
					titleField.setValue(selectedImageItem.getTitle());
					copyrightField.setValue(selectedImageItem.getCopyright());
					commentArea.setValue(selectedImageItem.getComment());
					dateField.setValue(selectedImageItem.getCaptureDate());
					photographerSelection.setValue(
							photographerEntryList.findModelWithKey(Integer.toString(selectedImageItem.getPhotographerID())), true);
				}
			}

		});
		imageListView.setBorders(true);

		// ListView<PhotographerEntry, PhotographerEntry>photographerListView = new
		// ListView<PhotographerEntry, PhotographerEntry>(photographerEntryList,
		// new IdentityValueProvider<PhotographerEntry>() {
		// @Override
		// public void setValue(PhotographerEntry object, PhotographerEntry value) {
		//
		// }
		// });
		// photographerListView.getSelectionModel().addSelectionChangedHandler(new
		// SelectionChangedHandler<PhotographerEntry>() {
		//
		// @Override
		// public void onSelectionChanged(SelectionChangedEvent<PhotographerEntry>
		// event) {
		// if (!event.getSelection().isEmpty()) {
		// selectedPhotographerItem = event.getSelection().get(0);
		// }
		// }
		//
		// });

		titleField = new TextField();
		titleField.setWidth(300);
		vlc.add(new FieldLabel(titleField, "Title"));

		copyrightField = new TextField();
		copyrightField.setWidth(300);
		vlc.add(new FieldLabel(copyrightField, "Copyright"));

		commentArea = new TextArea();
		commentArea.setSize("300px", "50px");
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

		imageListView.setSize("250", "350");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("240", "340");

    BorderLayoutData west = new BorderLayoutData(150);
    west.setMargins(new Margins(5));

    MarginData center = new MarginData();

    BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();
//    BorderLayoutData bld = new BorderLayoutData();
    borderLayoutContainer.setBounds(0, 0, 600, 500);
    borderLayoutContainer.setWestWidget(lf, west);
    borderLayoutContainer.setCenterWidget(vlc, center);

    panel = new ContentPanel();
//    panel.setHeading("Horizontal Box Layout");
    /** here we set the size and position, but be careful
     * to make it larger than the widget that is inserted
     */
    panel.setPixelSize(610, 510);
    panel.setPosition(5, 5);
		panel.setHeading("Image Editor");
    panel.add(borderLayoutContainer);		
		
	}

	/**
	 * Deletes the currently selected ImageEntry (shows a yes/no dialog first)
	 */
	private void deleteSelectedImageEntry() {
		if (selectedImageItem != null) {
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
					dbService.updateEntry("DELETE FROM Images WHERE ImageID=" + selectedImageItem.getImageID(),
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
				// Info.display("Refresh Image List", "sucess");
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
		if (selectedImageItem == null) {
			Info.display("Save Image", "There is no image selected for edititing!");
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
				sqlUpdate = sqlUpdate.concat(",PhotographerID=" + photographerSelection.getValue().getPhotographerID());
				if (commentArea.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",Comment='" + commentArea.getText() + "'");
				}
				if (dateField.getValue() != null) {
					sqlUpdate = sqlUpdate.concat(",CaptureDate='" + dtf.format(dateField.getValue()) + "'");
				}
				sqlUpdate = sqlUpdate.concat(" WHERE ImageID=" + selectedImageItem.getImageID());

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
		refreshImages();
	}

}
