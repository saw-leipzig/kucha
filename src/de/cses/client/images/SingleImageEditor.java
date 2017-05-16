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

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.core.client.util.ToggleGroup;
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
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.ImageEntry;
import de.cses.shared.PhotographerEntry;

public class SingleImageEditor implements IsWidget {

	private TextField titleField;
	private TextField copyrightField;
	private TextArea commentArea;
	private DateField dateField;
	private ComboBox<PhotographerEntry> photographerSelection;
	private FramedPanel panel;
	private PhotographerProperties photographerProps;
	private ListStore<PhotographerEntry> photographerEntryList;
	private ArrayList<ImageEditorListener> editorListenerList;

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private Radio rPhoto;
	private Radio rSketch;
	private Radio rMap;
	private ImageEntry imgEntry;
	private FlowLayoutContainer imageContainer;

//	interface ImageProperties extends PropertyAccess<ImageEntry> {
//		ModelKeyProvider<ImageEntry> imageID();
//
//		LabelProvider<ImageEntry> title();
//	}

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();

		LabelProvider<PhotographerEntry> name();
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
		@XTemplate("<img align=\"center\" width=\"150\" height=\"150\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
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
	public SingleImageEditor(ImageEntry imgEntry, ImageEditorListener listener) {
		this.imgEntry = imgEntry;
		editorListenerList = new ArrayList<ImageEditorListener>();
		editorListenerList.add(listener);
//		GWT.create(ImageProperties.class);

		photographerProps = GWT.create(PhotographerProperties.class);
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
			initPanel();
		}
		return panel;
	}

	/**
	 * Initializes the editor's panel if it this has not already been done. Should
	 * usually only be called once a session is started!
	 */
	private void initPanel() {
		panel = new FramedPanel();
		HorizontalPanel hPanel = new HorizontalPanel();
		imageContainer = new FlowLayoutContainer();
		// VerticalPanel imgPanel = new VerticalPanel();
		VerticalPanel editPanel = new VerticalPanel();

		FramedPanel attributePanel = new FramedPanel();
		titleField = new TextField();
		titleField.setWidth(300);
		attributePanel.setHeading("Title");
		titleField.setValue(imgEntry.getTitle());
		titleField.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				imgEntry.setTitle(event.getValue());
			}
		});
		attributePanel.add(titleField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		copyrightField = new TextField();
		copyrightField.setWidth(300);
		copyrightField.setValue(imgEntry.getCopyright());
		copyrightField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				imgEntry.setCopyright(event.getValue());
			}
		});
		attributePanel.setHeading("Copyright");
		attributePanel.add(copyrightField);
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		commentArea = new TextArea();
		commentArea.setSize("300px", "100px");
		commentArea.setValue(imgEntry.getComment());
		commentArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				imgEntry.setComment(event.getValue());
			}
		});
		attributePanel.add(commentArea);
		attributePanel.setHeading("Comment");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		dateField = new DateField(new DateTimePropertyEditor("dd MMMM yyyy"));
		dateField.setValue(imgEntry.getCaptureDate());
		dateField.addValueChangeHandler(new ValueChangeHandler<java.util.Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<java.util.Date> event) {
				imgEntry.setCaptureDate(new Date(event.getValue().getTime()));
			}
		});		
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
		photographerSelection.setValue(photographerEntryList.findModelWithKey(Integer.toString(imgEntry.getPhotographerID())), true);
		photographerSelection.addValueChangeHandler(new ValueChangeHandler<PhotographerEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PhotographerEntry> event) {
				imgEntry.setPhotographerID(event.getValue().getPhotographerID());
			}
		});
		attributePanel.add(photographerSelection);
		attributePanel.setHeading("Photographer");
		editPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		rPhoto = new Radio();
		rPhoto.setBoxLabel("Photo");
		rSketch = new Radio();
		rSketch.setBoxLabel("Sketch");
		rMap = new Radio();
		rMap.setBoxLabel("Map");
		ToggleGroup tg = new ToggleGroup();
		tg.add(rPhoto);
		tg.add(rSketch);
		tg.add(rMap);
		HorizontalPanel rbPanel = new HorizontalPanel();
		rbPanel.add(rPhoto);
		rbPanel.add(rSketch);
		rbPanel.add(rMap);
		rbPanel.setWidth("100%");
		attributePanel.add(rbPanel);
		attributePanel.setHeading("Image Type");
		switch (imgEntry.getType()) {
		case "photo":
			rPhoto.setValue(true);
			break;
		case "sketch":
			rSketch.setValue(true);
			break;
		case "map":
			rMap.setValue(true);
			break;
		default:
			break;
		}
		rPhoto.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue())
					imgEntry.setType("photo");
			}
		});
		rSketch.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue())
					imgEntry.setType("sketch");
			}
		});
		rMap.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue())
					imgEntry.setType("map");
			}
		});
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

		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() +"&thumb=true");
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
		if ("New Image".equals(titleField.getCurrentValue())) {
			showTitleWarningDialog();
			return;
		} else {
			for (ImageEditorListener listener : editorListenerList) {
				listener.closeImageEditor();
			}
		}
	}

	/**
	 * 
	 */
	protected void cancelEditing() {
		for (ImageEditorListener listener : editorListenerList) {
			listener.cancelImageEditor();
		}
	}

	/**
	 * This method will save the currently selected ImageEntry from the left list
	 * of previews. In future versions, the missing fields will be added. Also,
	 * the Photographer ID us currently not mapped to the text entry in this box.
	 * (shows a yes/no dialog first)
	 */
	private void saveImageEntry() {
//		ImageEntry selectedItem = imageListView.getSelectionModel().getSelectedItem();
		if ("New Image".equals(titleField.getCurrentValue())) {
			showTitleWarningDialog();
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
				if (imgEntry.getCopyright() != null) {
					sqlUpdate = sqlUpdate.concat(",Copyright='" + imgEntry.getCopyright() + "'");
				}
				if (imgEntry.getPhotographerID() > 0) {
					sqlUpdate = sqlUpdate.concat(",PhotographerID=" + imgEntry.getPhotographerID());
				}
				if (imgEntry.getComment() != null) {
					sqlUpdate = sqlUpdate.concat(",Comment='" + imgEntry.getComment() + "'");
				}
				if (imgEntry.getCaptureDate() != null) {
					sqlUpdate = sqlUpdate.concat(",CaptureDate='" + dtf.format(imgEntry.getCaptureDate()) + "'");
				}
				sqlUpdate = sqlUpdate.concat(",ImageType='" + imgEntry.getType() + "'");

				sqlUpdate = sqlUpdate.concat(" WHERE ImageID=" + imgEntry.getImageID());

				dbService.updateEntry(sqlUpdate, new AsyncCallback<Boolean>() {
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

	/**
	 * 
	 */
	private void showTitleWarningDialog() {
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

}
