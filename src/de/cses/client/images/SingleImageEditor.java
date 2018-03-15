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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
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
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.PhotographerEntry;

public class SingleImageEditor extends AbstractEditor {

	private TextField titleField;
	private TextField shortNameField;
	private TextArea copyrightArea;
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

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();

		LabelProvider<PhotographerEntry> name();
	}

	/**
	 * Creates the view how a thumbnail of an image entry will be shown currently we are relying on the url of the image until we have user management implemented
	 * and protect images from being viewed from the outside without permission
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
	 * This widget allows to edit the information of an ImageEntry, i.e. an image in the database. It also allows for uploading new images to the database.
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

		for (ImageTypeEntry ite : StaticTables.getInstance().getImageTypeEntries().values()) {
			imageTypeEntryList.add(ite);
		}
		imageTypeSelection.setValue(imageTypeEntryList.findModelWithKey(Integer.toString(imgEntry.getImageTypeID())));

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
		HorizontalLayoutContainer mainHLC = new HorizontalLayoutContainer();
		imageContainer = new FlowLayoutContainer();
		HorizontalLayoutContainer editHLC = new HorizontalLayoutContainer();
		VerticalLayoutContainer editVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer leftEditVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer rightEditVLC = new VerticalLayoutContainer();

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
		// titleField.setWidth(300);
		FramedPanel titlePanel = new FramedPanel();
		titlePanel.setHeading("Title");
		titleField.setValue(imgEntry.getTitle());
		titlePanel.add(titleField);
		editVLC.add(titlePanel, new VerticalLayoutData(1.0, .2));

		FramedPanel shortNamePanel = new FramedPanel();
		shortNameField = new TextField();
		shortNameField.addValidator(new MaxLengthValidator(12));
		// shortNameField.setWidth(300);
		shortNamePanel.setHeading("Short Name");
		shortNameField.setValue(imgEntry.getShortName());
		shortNamePanel.add(shortNameField);
		
		FramedPanel imageFormatPanel = new FramedPanel();
		imageFormatPanel.add(new Label(imgEntry.getFilename().substring(imgEntry.getFilename().lastIndexOf("."))));
		
		HorizontalLayoutContainer helperHLC = new HorizontalLayoutContainer();
		helperHLC.add(shortNamePanel, new HorizontalLayoutData(1.0, .5));
		helperHLC.add(imageFormatPanel, new HorizontalLayoutData(1.0, .5));

		leftEditVLC.add(helperHLC, new VerticalLayoutData(1.0, .2));

		FramedPanel copyrightPanel = new FramedPanel();
		copyrightArea = new TextArea();
		copyrightArea.addValidator(new MaxLengthValidator(128));
		copyrightArea.setValue(imgEntry.getCopyright());
		copyrightPanel.setHeading("Copyright");
		copyrightPanel.add(copyrightArea);
		rightEditVLC.add(copyrightPanel, new VerticalLayoutData(1.0, .35));

		FramedPanel commentPanel = new FramedPanel();
		commentArea = new TextArea();
		commentArea.setEmptyText("optional comments");
		if (imgEntry.getComment() != null) {
			commentArea.setValue(imgEntry.getComment());
		}
		commentPanel.add(commentArea);
		commentPanel.setHeading("Comment");
		rightEditVLC.add(commentPanel, new VerticalLayoutData(1.0, .65));

		FramedPanel datePanel = new FramedPanel();
		dateField = new TextField();
		dateField.addValidator(new MaxLengthValidator(32));
		dateField.setValue(imgEntry.getDate());
		datePanel.add(dateField);
		datePanel.setHeading("Date");
		leftEditVLC.add(datePanel, new VerticalLayoutData(1.0, .2));

		FramedPanel authorPanel = new FramedPanel();
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
		authorPanel.add(authorSelection);
		authorPanel.setHeading("Author");
		leftEditVLC.add(authorPanel, new VerticalLayoutData(1.0, .2));

		FramedPanel imageTypeSelectionPanel = new FramedPanel();
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

		imageTypeSelectionPanel.add(imageTypeSelection);
		imageTypeSelectionPanel.setHeading("Image Type");

		FramedPanel publicImagePanel = new FramedPanel();
		CheckBox publicImageCB = new CheckBox();
		publicImageCB.setBoxLabel("is public");
		publicImageCB.setValue(imgEntry.isPublicImage());
		publicImageCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				imgEntry.setPublicImage(event.getValue());
			}
		});
		publicImagePanel.add(publicImageCB);
		publicImagePanel.setHeading("Image Mode");

		HorizontalLayoutContainer imageOptionsHLC = new HorizontalLayoutContainer();
		imageOptionsHLC.add(imageTypeSelectionPanel, new HorizontalLayoutData(.5, 1.0));
		imageOptionsHLC.add(publicImagePanel, new HorizontalLayoutData(.5, 1.0));
		leftEditVLC.add(imageOptionsHLC, new VerticalLayoutData(1.0, .2));
		
		ToolButton saveToolButton = new ToolButton(ToolButton.SAVE);
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveImageEntry(false);
			}
		});
		
		ToolButton closeToolButton = new ToolButton(ToolButton.CLOSE);
		closeToolButton.setToolTip("close");
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				 Dialog d = new Dialog();
				 d.setHeading("Exit Warning!");
				 d.setWidget(new HTML("Do you wish to save before exiting?"));
				 d.setBodyStyle("fontWeight:bold;padding:13px;");
				 d.setPixelSize(300, 100);
				 d.setHideOnButtonClick(true);
				 d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
				 d.setModal(true);
				 d.center();
				 d.show();
				 d.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						saveImageEntry(true);
					}
				});
				 d.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 closeEditor();
					}
				});
			}
		});		

		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=300" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		Image img = new Image(imageUri);
		imageContainer.add(img);
		imageContainer.setPixelSize(310, 310);

		editHLC.add(leftEditVLC, new HorizontalLayoutData(.5, 1.0));
		editHLC.add(rightEditVLC, new HorizontalLayoutData(.5, 1.0));
		editVLC.add(editHLC, new VerticalLayoutData(1.0, .8));
		
		mainHLC.add(imageContainer, new HorizontalLayoutData(.4, 1.0));
		mainHLC.add(editVLC, new HorizontalLayoutData(.6, 1.0));

		panel.setHeading("Image Editor");
		panel.add(mainHLC);
		panel.addTool(saveToolButton);
		panel.addTool(closeToolButton);
		panel.setSize("900px", "400px");
	}

	/**
	 * This method will save the currently selected ImageEntry from the left list of previews. In future versions, the missing fields will be added. Also, the
	 * Photographer ID us currently not mapped to the text entry in this box. (shows a yes/no dialog first)
	 */
	private void saveImageEntry(boolean closeEditorRequested) {
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
							if (closeEditorRequested) {
								closeEditor();
							}
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
		imgEntry.setCopyright(copyrightArea.getCurrentValue());
		imgEntry.setComment(commentArea.getCurrentValue());
		imgEntry.setDate(dateField.getCurrentValue());
		imgEntry.setPhotographerID(authorSelection.getCurrentValue() != null ? authorSelection.getCurrentValue().getPhotographerID() : 0);
		imgEntry.setImageTypeID(imageTypeSelection.getCurrentValue().getImageTypeID());
	}

	/**
	 * @return
	 */
	private boolean verifyInputs() {
		return titleField.isValid() && shortNameField.isValid() && copyrightArea.validate() && dateField.validate();
	}

}
