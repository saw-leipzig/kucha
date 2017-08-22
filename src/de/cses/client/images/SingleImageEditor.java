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
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractEditor;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.PhotographerEntry;

public class SingleImageEditor extends AbstractEditor {

	private TextField titleField;
	private TextField shortNameField;
	private TextField copyrightField;
	private TextArea commentArea;
	private TextField dateField;
	private ComboBox<PhotographerEntry> authorSelection;
	private FramedPanel panel;
	private PhotographerProperties photographerProps;
	private ListStore<PhotographerEntry> photographerEntryList;
	private ComboBox<ImageTypeEntry> imageTypeSelection;
	private ImageTypeProperties imageTypeProps;
	private ListStore<ImageTypeEntry> imageTypeEntryList;

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ImageEntry imgEntry;
	private FlowLayoutContainer imageContainer;

	// interface ImageProperties extends PropertyAccess<ImageEntry> {
	// ModelKeyProvider<ImageEntry> imageID();
	//
	// LabelProvider<ImageEntry> title();
	// }

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();

		LabelProvider<PhotographerEntry> name();
	}

	/**
	 * Creates the view how a thumbnail of an image entry will be shown currently we are relying on the url of the image until we have user
	 * management implemented and protect images from being viewed from the outside without permission
	 * 
	 * @author alingnau
	 *
	 */
	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" width=\"150\" height=\"150\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);
	}

	interface PhotographerViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml photographer(String name);
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
	 * This widget allows to edit the information of an ImageEntry, i.e. an image in the database. It also allows for uploading new images to
	 * the database.
	 */
	public SingleImageEditor(ImageEntry imgEntry) {
		this.imgEntry = imgEntry;

		photographerProps = GWT.create(PhotographerProperties.class);
		photographerEntryList = new ListStore<PhotographerEntry>(photographerProps.photographerID());
		
		imageTypeProps = GWT.create(ImageTypeProperties.class);
		imageTypeEntryList = new ListStore<ImageTypeEntry>(imageTypeProps.imageTypeID());
		
		initPanel();
		
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
		
		dbService.getImageTypes(new AsyncCallback<ArrayList<ImageTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<ImageTypeEntry> result) {
				for (ImageTypeEntry ite : result) {
					imageTypeEntryList.add(ite);
				}
				imageTypeSelection.setValue(imageTypeEntryList.findModelWithKey(Integer.toString(imgEntry.getImageTypeID())));
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

	/**
	 * Initializes the editor's panel if it this has not already been done. Should usually only be called once a session is started!
	 */
	private void initPanel() {
		panel = new FramedPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		imageContainer = new FlowLayoutContainer();
		// VerticalPanel imgPanel = new VerticalPanel();
		VerticalPanel editPanel = new VerticalPanel();
		
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
		titleField.setWidth(300);
		attributePanel.setHeading("Title");
		titleField.setValue(imgEntry.getTitle());
		attributePanel.add(titleField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		shortNameField = new TextField();
		shortNameField.addValidator(new MaxLengthValidator(12));
//		shortNameField.addValidator(noQuoteValidator);
		shortNameField.setWidth(300);
		attributePanel.setHeading("Short Name");
		shortNameField.setValue(imgEntry.getShortName());
		attributePanel.add(shortNameField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		copyrightField = new TextField();
		copyrightField.setWidth(300);
		copyrightField.addValidator(new MaxLengthValidator(64));
//		copyrightField.addValidator(noQuoteValidator);
		copyrightField.setValue(imgEntry.getCopyright());
		attributePanel.setHeading("Copyright");
		attributePanel.add(copyrightField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		commentArea = new TextArea();
		commentArea.setSize("300px", "100px");
		commentArea.setValue(imgEntry.getComment());
//		commentArea.addValidator(noQuoteValidator);
		attributePanel.add(commentArea);
		attributePanel.setHeading("Comment");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		dateField = new TextField();
		dateField.addValidator(new MaxLengthValidator(32));
		dateField.setValue(imgEntry.getDate());
		attributePanel.add(dateField);
		attributePanel.setHeading("Date");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		authorSelection = new ComboBox<PhotographerEntry>(photographerEntryList, photographerProps.name(),
				new AbstractSafeHtmlRenderer<PhotographerEntry>() {

					@Override
					public SafeHtml render(PhotographerEntry item) {
						final PhotographerViewTemplates pvTemplates = GWT.create(PhotographerViewTemplates.class);
						return pvTemplates.photographer(item.getName());
					}
				});
		authorSelection.setEmptyText("Select an author ...");
		authorSelection.setTypeAhead(false);
		authorSelection.setEditable(false);
		authorSelection.setTriggerAction(TriggerAction.ALL);
		authorSelection.setValue(photographerEntryList.findModelWithKey(Integer.toString(imgEntry.getPhotographerID())), true);
		attributePanel.add(authorSelection);
		attributePanel.setHeading("Author");
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
		imageTypeSelection.addSelectionHandler(new SelectionHandler<ImageTypeEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<ImageTypeEntry> event) {
				imgEntry.setImageTypeID(event.getSelectedItem().getImageTypeID());
			}
		});

		attributePanel.add(imageTypeSelection);
		attributePanel.setHeading("Image Type");
		editPanel.add(attributePanel);

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				cancelDialog();
			}
		});

		TextButton saveButton = new TextButton("save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveImageEntry();
			}
		});

		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=200");
		Image img = new Image(imageUri);
		imageContainer.add(img);
		imageContainer.setPixelSize(210, 210);
		hPanel.add(imageContainer);
		hPanel.add(editPanel);

		panel.setHeading("Image Editor");
		panel.add(hPanel);
		panel.addButton(cancelButton);
		panel.addButton(saveButton);
		// panel.addButton(deleteButton);

	}

	/**
	 * 
	 */
	protected void cancelDialog() {
		closeEditor();
	}

	// /**
	// *
	// */
	// protected void cancelEditing() {
	// closeEditor();
	// }

	/**
	 * This method will save the currently selected ImageEntry from the left list of previews. In future versions, the missing fields will be
	 * added. Also, the Photographer ID us currently not mapped to the text entry in this box. (shows a yes/no dialog first)
	 */
	private void saveImageEntry() {
		// ImageEntry selectedItem = imageListView.getSelectionModel().getSelectedItem();
		if (!verifyInputs()) {
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
		simple.add(
				new Label("Saving will overwrite the existing information in the Database. This cannot be reversed! Do you want to continue?"));
		simple.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				updateImageEntry();
				// only of the yes button is selected, we will perform the command
				// to simplify we just ignore the no button event by doing nothing

				dbService.updateImageEntry(imgEntry, new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						Info.display("ERROR", "Image information has NOT been updated!");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							cancelDialog();
						} else {
							Info.display("ERROR", "Image information has NOT been updated!");
						}
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

	private void updateImageEntry() {
		imgEntry.setTitle(titleField.getCurrentValue());
		imgEntry.setShortName(shortNameField.getCurrentValue());
		imgEntry.setCopyright(copyrightField.getCurrentValue());
		imgEntry.setComment(commentArea.getCurrentValue());
		imgEntry.setDate(dateField.getCurrentValue());
		imgEntry.setPhotographerID(authorSelection.getCurrentValue() != null ? authorSelection.getCurrentValue().getPhotographerID() : 0);
		imgEntry.setImageTypeID(imageTypeSelection.getCurrentValue().getImageTypeID());
	}

	/**
	 * @return
	 */
	private boolean verifyInputs() {
		return titleField.isValid() && shortNameField.isValid() && copyrightField.validate() && dateField.validate();
	}

}
