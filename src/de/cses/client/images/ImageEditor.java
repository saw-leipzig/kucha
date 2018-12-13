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
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.testing.MockEditorError;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.PhotographerEntry;

public class ImageEditor implements IsWidget, ImageUploadListener {

	private TextField titleField;
	private TextField shortNameField;
	private TextArea copyrightArea;
	private TextArea commentArea;
	private TextField dateField;
	private ComboBox<PhotographerEntry> photographerSelection;
	private FramedPanel panel;
	private ListStore<ImageEntry> imageEntryList;
	private ImageProperties properties;
	private PhotographerProperties photographerProps;
	private ListStore<PhotographerEntry> photographerEntryList;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ArrayList<ImageEditorListener> editorListenerList;
	private ComboBox<ImageTypeEntry> imageTypeSelection;
	private ImageTypeProperties imageTypeProps;
	private ListStore<ImageTypeEntry> imageTypeEntryList;

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField searchField;
	private StoreFilter<ImageEntry> searchFilter;
	private StoreFilter<ImageEntry> newImageFilter;

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();
		LabelProvider<ImageEntry> title();
	}

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();
		LabelProvider<PhotographerEntry> name();
	}

	interface ImageTypeProperties extends PropertyAccess<ImageTypeEntry> {
		ModelKeyProvider<ImageTypeEntry> imageTypeID();
		LabelProvider<ImageTypeEntry> name();
	}

	interface ImageTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml imageTypeLabel(String name);
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
		@XTemplate("<img align=\"center\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);
	}

	interface PhotographerViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml photographer(String name);
	}

	/**
	 * This widget allows to edit the information of an ImageEntry, i.e. an image
	 * in the database. It also allows for uploading new images to the database.
	 */
	public ImageEditor(ImageEditorListener listener) {
		editorListenerList = new ArrayList<ImageEditorListener>();
		editorListenerList.add(listener);
		properties = GWT.create(ImageProperties.class);
		imageEntryList = new ListStore<ImageEntry>(properties.imageID());
		newImageFilter = new StoreFilter<ImageEntry>() {
			@Override
			public boolean select(Store<ImageEntry> store, ImageEntry parent, ImageEntry item) {
				if ("New Image".equals(item.getTitle()))
					return true;
				return false;
			}
		};

		photographerProps = GWT.create(PhotographerProperties.class);
		photographerEntryList = new ListStore<PhotographerEntry>(photographerProps.photographerID());
		imageTypeProps = GWT.create(ImageTypeProperties.class);
		imageTypeEntryList = new ListStore<ImageTypeEntry>(imageTypeProps.imageTypeID());

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
		
//		dbService.getImageTypes(new AsyncCallback<ArrayList<ImageTypeEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(ArrayList<ImageTypeEntry> result) {
				for (ImageTypeEntry ite : StaticTables.getInstance().getImageTypeEntries().values()) {
					imageTypeEntryList.add(ite);
				}
//				imageTypeSelection.setValue(imageTypeEntryList.findModelWithKey(Integer.toString(imgEntry.getImageTypeID())));
//			}
//		});

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
		panel = new FramedPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		VerticalPanel imgPanel = new VerticalPanel();
		VerticalPanel editPanel = new VerticalPanel();

		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});
		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=150" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				return imageViewTemplates.image(imageUri, item.getTitle());
			}

		}));

		imageListView.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<ImageEntry>() {
			@Override
			public void onSelectionChanged(SelectionChangedEvent<ImageEntry> event) {
				if (!event.getSelection().isEmpty()) {
					ImageEntry selectedImageItem = event.getSelection().get(0);
					titleField.setValue(selectedImageItem.getTitle());
					shortNameField.setValue(selectedImageItem.getShortName());
					copyrightArea.setValue(selectedImageItem.getCopyright());
					commentArea.setValue(selectedImageItem.getComment());
					dateField.setValue(selectedImageItem.getDate());
					photographerSelection.setValue(selectedImageItem.getImageAuthor(), true);
					imageTypeSelection.setValue(imageTypeEntryList.findModelWithKey(Integer.toString(selectedImageItem.getImageTypeID())));
				}
			}
		});

		imageListView.setBorders(true);

		FramedPanel attributePanel = new FramedPanel();
		titleField = new TextField();
		titleField.addValidator(new MaxLengthValidator(128));
		titleField.addValidator(new Validator<String>() {

			@Override
			public List<EditorError> validate(Editor<String> editor, String value) {
				List<EditorError> errors = new ArrayList<EditorError>();
				if (value.contains("New Image")) {
					errors.add(new MockEditorError() {

						@Override
						public String getMessage() {
							return "Please don't use 'New Image' as part of the title!";
						}
					});
				}
				return errors;
			}
		});
		titleField = new TextField();
		titleField.setWidth(300);
		attributePanel.setHeading("Title");
		attributePanel.add(titleField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		shortNameField = new TextField();
		shortNameField.addValidator(new MinLengthValidator(3));
		shortNameField.addValidator(new MaxLengthValidator(12));
		shortNameField.setWidth(300);
		shortNameField.setValue("");
		attributePanel.setHeading("Short Name");
		attributePanel.add(shortNameField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		copyrightArea = new TextArea();
		copyrightArea.addValidator(new MaxLengthValidator(128));
		copyrightArea.setSize("300px", "50px");
		attributePanel.setHeading("Copyright");
		attributePanel.add(copyrightArea);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		commentArea = new TextArea();
		commentArea.setSize("300px", "100px");
		attributePanel.add(commentArea);
		attributePanel.setHeading("Comment");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		dateField = new TextField();
		dateField.addValidator(new MaxLengthValidator(32));
		attributePanel.add(dateField);
		attributePanel.setHeading("Date captured");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
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
		attributePanel.add(photographerSelection);
		attributePanel.setHeading("Photographer");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		imageTypeSelection = new ComboBox<ImageTypeEntry>(imageTypeEntryList, imageTypeProps.name(),
				new AbstractSafeHtmlRenderer<ImageTypeEntry>() {

					@Override
					public SafeHtml render(ImageTypeEntry item) {
						final ImageTypeViewTemplates itvTemplates = GWT.create(ImageTypeViewTemplates.class);
						return itvTemplates.imageTypeLabel(item.getName());
					}
				});
		imageTypeSelection.setEmptyText("select image type");
		imageTypeSelection.setTypeAhead(false);
		imageTypeSelection.setEditable(false);
		imageTypeSelection.setTriggerAction(TriggerAction.ALL);
//		imageTypeSelection.addSelectionHandler(new SelectionHandler<ImageTypeEntry>() {
//			
//			@Override
//			public void onSelection(SelectionEvent<ImageTypeEntry> event) {
//				imgEntry.setImageTypeID(event.getSelectedItem().getImageTypeID());
//			}
//		});
		attributePanel.add(imageTypeSelection);
		attributePanel.setHeading("Image Type");
		editPanel.add(attributePanel);
		
		TextButton closeButton = new TextButton("close");
		closeButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				for (ImageEditorListener listener : editorListenerList) {
					listener.closeImageEditor();
				}
			}
		});

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

		// C14DocumentUploader imgUploader = new C14DocumentUploader(this);
		// mainInputVLC.add(imgUploader);

		imageListView.setSize("250", "350");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("250", "350");
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Image Selection");
		attributePanel.add(lf);
		imgPanel.add(attributePanel);

		/**
		 * here we add the search for image titles
		 */
		searchField = new TextField();
		searchField.setSize("200", "30");
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

		FramedPanel searchPanel = new FramedPanel();
		searchPanel.setHeading("Search");
		searchPanel.add(searchField);
		searchPanel.addButton(searchButton);
		searchPanel.addButton(resetButton);
		imgPanel.add(searchPanel);

		hPanel.add(imgPanel);
		hPanel.add(editPanel);

		panel.setHeading("Image Editor");
		panel.add(hPanel);
		panel.addButton(closeButton);
		panel.addButton(saveButton);
		panel.addButton(deleteButton);

	}

	/**
	 * 
	 */
	protected void cancelEditing() {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Deletes the currently selected ImageEntry (shows a yes/no dialog first)
	 */
	private void deleteSelectedImageEntry() {
		if (imageListView.getSelectionModel().getSelectedItem() != null) {
			Util.showYesNo("Delete", "Do you really want to delete the selected entry?", new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					// only of the yes button is selected, we will perform the command
					// to simplify we just ignore the no button event by doing nothing
					int imageID = imageListView.getSelectionModel().getSelectedItem().getImageID();
					dbService.updateEntry("DELETE FROM Images WHERE ImageID=" + imageID, new AsyncCallback<Boolean>() {
						public void onFailure(Throwable caught) {
							Info.display("Error", "Problem with database connection!");
						}

						@Override
						public void onSuccess(Boolean result) {
							if (result) {
//								Info.display("Image information", "Image information has been updated!");
							} else {
//								Info.display("Image information", "Image information has been updated!");
							}
							refreshImages();
							imageListView.getSelectionModel().select(imageEntryList.get(0), true);
						}
					});
				}
			}, null);
//			Dialog simple = new Dialog();
//			simple.setHeading("Delete");
//			simple.setWidth(300);
//			simple.setResizable(false);
//			simple.setHideOnButtonClick(true);
//			simple.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
//			simple.setBodyStyleName("pad-text");
//			simple.getBody().addClassName("pad-text");
//			simple.add(new Label("Do you really want to delete the selected entry?"));
//			simple.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
//
//				@Override
//				public void onSelect(SelectEvent event) {
//					// only of the yes button is selected, we will perform the command
//					// to simplify we just ignore the no button event by doing nothing
//					int imageID = imageListView.getSelectionModel().getSelectedItem().getImageID();
//					dbService.updateEntry("DELETE FROM Images WHERE ImageID=" + imageID, new AsyncCallback<Boolean>() {
//						public void onFailure(Throwable caught) {
//							Info.display("Error", "Problem with database connection!");
//						}
//
//						@Override
//						public void onSuccess(Boolean result) {
//							if (result) {
////								Info.display("Image information", "Image information has been updated!");
//							} else {
////								Info.display("Image information", "Image information has been updated!");
//							}
//							refreshImages();
//							imageListView.getSelectionModel().select(imageEntryList.get(0), true);
//						}
//					});
//				}
//			});
//			simple.show();
			// constrain the dialog to the viewport (for small mobile screen sizes)
//			Rectangle bounds = simple.getElement().getBounds();
//			Rectangle adjusted = simple.getElement().adjustForConstraints(bounds);
//			if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
//				simple.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
//			}
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
		} else if (!verifyInputs()) {
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
				selectedItem.setTitle(titleField.getCurrentValue());
				selectedItem.setShortName(shortNameField.getCurrentValue());
				selectedItem.setCopyright(copyrightArea.getCurrentValue());
				selectedItem.setComment(commentArea.getCurrentValue());
				selectedItem.setDate(dateField.getCurrentValue());
				selectedItem.setImageAuthor(photographerSelection.getCurrentValue());
				selectedItem.setImageTypeID(imageTypeSelection.getCurrentValue().getImageTypeID());
				dbService.updateImageEntry(selectedItem, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						Info.display("Error", "Problem with database connection!");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
//							Info.display("Image information", "Image information has been updated!");
						} else {
//							Info.display("Image information", "Image information has been updated!");
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
	public void uploadCompleted(int newImageID, String filename) {
		imageEntryList.addFilter(newImageFilter);
		imageEntryList.setEnableFilters(true);
		refreshImages();
		imageListView.getSelectionModel().select(imageEntryList.get(0), true);
	}

	/* (non-Javadoc)
	 * @see de.cses.client.images.ImageUploadListener#uploadCanceled()
	 */
	@Override
	public void uploadCanceled() {
		// TODO Auto-generated method stub
		
	}
	
	/**
	 * @return
	 */
	private boolean verifyInputs() {
		return titleField.isValid() && shortNameField.isValid() && copyrightArea.validate() && dateField.validate();
	}

	
}
