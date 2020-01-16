/*
 * Copyright 2016 -2018
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
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.testing.MockEditorError;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.PhotographerEntry;

public class SingleImageEditor extends AbstractEditor {

	private TextField titleField;
	private TextField shortNameField;
	private TextArea copyrightArea;
	private TextArea commentArea;
	private TextField dateField;
	private ComboBox<PhotographerEntry> authorSelectionCB;
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
	private ImageViewTemplates imgViewTemplates;

	interface PhotographerProperties extends PropertyAccess<PhotographerEntry> {
		ModelKeyProvider<PhotographerEntry> photographerID();
		LabelProvider<PhotographerEntry> label();
	}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<figure style='text-align: center; margin: 0;'>"
				+ "<img src='{imgUri}' style='position: relative; padding: 5px; background: black;'>"
				+ "</figure>")
		SafeHtml view(SafeUri imgUri);
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

		imgViewTemplates = GWT.create(ImageViewTemplates.class);
		
		photographerProps = GWT.create(PhotographerProperties.class);
		photographerEntryList = new ListStore<PhotographerEntry>(photographerProps.photographerID());
		photographerEntryList.addSortInfo(new StoreSortInfo<PhotographerEntry>(new ValueProvider<PhotographerEntry, String>() {

			@Override
			public String getValue(PhotographerEntry entry) {
				return entry.getLabel();
			}

			@Override
			public void setValue(PhotographerEntry object, String value) { }

			@Override
			public String getPath() {
				return "label";
			}
		}, SortDir.ASC));

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
				for (PhotographerEntry pe : result) {
					photographerEntryList.add(pe);
				}
				if (imgEntry.getImageAuthor() != null) {
//					authorSelectionCB.setValue(photographerEntryList.findModelWithKey(Integer.toString(imgEntry.getImageAuthor().getPhotographerID())));
					authorSelectionCB.setValue(imgEntry.getImageAuthor());
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
		FramedPanel titlePanel = new FramedPanel();
		titlePanel.setHeading("Title");
		titleField.setValue(imgEntry.getTitle());
		titlePanel.add(titleField);

		FramedPanel shortNamePanel = new FramedPanel();
		shortNameField = new TextField();

		shortNameField.addValidator(new MinLengthValidator(2));
		shortNameField.addValidator(new MaxLengthValidator(12));
		shortNamePanel.setHeading("Short Name");
		shortNameField.setValue(imgEntry.getShortName());
		shortNamePanel.add(shortNameField);
		
		FramedPanel copyrightPanel = new FramedPanel();
		copyrightArea = new TextArea();
		copyrightArea.addValidator(new MaxLengthValidator(128));
		copyrightArea.setValue(imgEntry.getCopyright());
		copyrightPanel.setHeading("Copyright");
		copyrightPanel.add(copyrightArea);

		FramedPanel commentPanel = new FramedPanel();
		commentArea = new TextArea();
		commentArea.setEmptyText("optional comments");
		if (imgEntry.getComment() != null) {
			commentArea.setValue(imgEntry.getComment());
		}
		commentPanel.add(commentArea);
		commentPanel.setHeading("Comment");

		FramedPanel datePanel = new FramedPanel();
		dateField = new TextField();
		dateField.setEmptyText("optional date");
		dateField.addValidator(new MaxLengthValidator(32));
		dateField.setValue(imgEntry.getDate());
		datePanel.add(dateField);
		datePanel.setHeading("Date");

		/**
		 * The Author selection
		 */
		FramedPanel authorFP = new FramedPanel();
		authorSelectionCB = new ComboBox<PhotographerEntry>(photographerEntryList, photographerProps.label(),
				new AbstractSafeHtmlRenderer<PhotographerEntry>() {

					@Override
					public SafeHtml render(PhotographerEntry item) {
						final PhotographerViewTemplates pvTemplates = GWT.create(PhotographerViewTemplates.class);
						String name = item.getName();
						String institution = item.getInstitution();
						return pvTemplates.photographer(name!=null ? (institution!=null ? name + " ( " + institution + ")" : name) : institution);
					}
				});
		authorSelectionCB.setEmptyText("select an author ...");
		authorSelectionCB.setTypeAhead(false);
		authorSelectionCB.setEditable(false);
		authorSelectionCB.setTriggerAction(TriggerAction.ALL);
		authorSelectionCB.addSelectionHandler(new SelectionHandler<PhotographerEntry>() {

			@Override
			public void onSelection(SelectionEvent<PhotographerEntry> event) {
				imgEntry.setImageAuthor(event.getSelectedItem());
			}
		});
//		authorSelectionCB.setValue(photographerEntryList.findModelWithKey(Integer.toString(imgEntry.getPhotographerID())));
		
		ToolButton addPhotoAuthorTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addPhotoAuthorTB.setToolTip(Util.createToolTip("Add new Photo Author"));
		addPhotoAuthorTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addPhotoAuthorDialog = new PopupPanel();
				FramedPanel addPhotoAuthorFP = new FramedPanel();
				addPhotoAuthorFP.setHeading("Add Photo Author");
				TextField authorNameField = new TextField();
				authorNameField.addValidator(new MinLengthValidator(2));
				authorNameField.addValidator(new MaxLengthValidator(64));
				authorNameField.setEmptyText("photo author name");
				authorNameField.setWidth(300);
				TextField institutionField = new TextField();
				institutionField.addValidator(new MinLengthValidator(2));
				institutionField.addValidator(new MaxLengthValidator(64));
				institutionField.setEmptyText("institution");
				institutionField.setWidth(300);
				VerticalPanel authorVP = new VerticalPanel();
				authorVP.add(authorNameField);
				authorVP.add(institutionField);
				addPhotoAuthorFP.add(authorVP);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (authorNameField.isValid() || institutionField.isValid()) {
							PhotographerEntry pEntry = new PhotographerEntry(authorNameField.getCurrentValue(), institutionField.getCurrentValue());
							dbService.insertPhotographerEntry(pEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									Window.alert("A problem occured while saving!");
								}

								@Override
								public void onSuccess(Integer result) {
									pEntry.setPhotographerID(result);
									photographerEntryList.add(pEntry);
								}
							});
							addPhotoAuthorDialog.hide();
						}
					}
				});
				addPhotoAuthorFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addPhotoAuthorDialog.hide();
					}
				});
				addPhotoAuthorFP.addButton(cancelButton);
				addPhotoAuthorDialog.add(addPhotoAuthorFP);
				addPhotoAuthorDialog.setModal(true);
				addPhotoAuthorDialog.center();
			}
		});
		
		ToolButton resetSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				authorSelectionCB.setValue(null);
			}
		});
		authorFP.addTool(resetSelectionTB);
		authorFP.addTool(addPhotoAuthorTB);
		authorFP.add(authorSelectionCB);
		authorFP.setHeading("Author");
		
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

		SimpleComboBox<String> accessLevelCB = new SimpleComboBox<String>(new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		});
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(0));
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(1));
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(2));
		accessLevelCB.setEditable(false);
		accessLevelCB.setTypeAhead(false);
		accessLevelCB.setTriggerAction(TriggerAction.ALL);
		accessLevelCB.setToolTip(Util.createToolTip(
				"The acccess level for the image will influence who can see the image.",
				"PRIVATE means that users need at least FULL access rights, COPYRIGHT means user need ASSICIATED access rights."
			));
		accessLevelCB.setValue(AbstractEntry.ACCESS_LEVEL_LABEL.get(imgEntry.getAccessLevel()));
		accessLevelCB.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				imgEntry.setAccessLevel(accessLevelCB.getSelectedIndex());
			}
		});
		FramedPanel accessLevelFP = new FramedPanel();
		accessLevelFP.setHeading("Access Level");
		accessLevelFP.add(accessLevelCB);

		HorizontalLayoutContainer imageAccessLevelHLC = new HorizontalLayoutContainer();
		imageAccessLevelHLC.add(imageTypeSelectionPanel, new HorizontalLayoutData(.5, 1.0));
		imageAccessLevelHLC.add(accessLevelFP, new HorizontalLayoutData(.5, 1.0));
		
		ToolButton saveToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveToolButton.setToolTip(Util.createToolTip("save"));
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveImageEntry(false);
			}
		});
		
		ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeToolButton.setToolTip(Util.createToolTip("close"));
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						saveImageEntry(true);
					}
				}, new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 closeEditor(null);
					}
				}, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						
						saveImageEntry(true);
						closeEditor(null);
					}
			
					
				}
				);
			}
		});		

		ToolButton viewFullSizeTB = new ToolButton(ToolButton.MAXIMIZE);
		viewFullSizeTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				com.google.gwt.user.client.Window.open("/resource?imageID=" + imgEntry.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri(),"_blank",null);
			}
		});
		viewFullSizeTB.setToolTip(Util.createToolTip("View image full size.", "A new tab will be opened in the browser."));
		
		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + imgEntry.getImageID() + "&thumb=300" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		HTMLPanel imgHP = new HTMLPanel(imgViewTemplates.view(imageUri));
		FramedPanel imgFP = new FramedPanel();
		imgFP.setHeading("Image format: " + imgEntry.getFilename().substring(imgEntry.getFilename().lastIndexOf(".")+1).toUpperCase());
		imgFP.add(imgHP);
		imgFP.addTool(viewFullSizeTB);
		
		VerticalLayoutContainer leftEditVLC = new VerticalLayoutContainer();
		leftEditVLC.add(shortNamePanel, new VerticalLayoutData(1.0, .25));
		leftEditVLC.add(copyrightPanel, new VerticalLayoutData(1.0, .5));
		leftEditVLC.add(datePanel, new VerticalLayoutData(1.0, .25));

		VerticalLayoutContainer rightEditVLC = new VerticalLayoutContainer();
		rightEditVLC.add(imageAccessLevelHLC, new VerticalLayoutData(1.0, .25));
		rightEditVLC.add(commentPanel, new VerticalLayoutData(1.0, .75));

		HorizontalLayoutContainer editHLC = new HorizontalLayoutContainer();
		editHLC.add(leftEditVLC, new HorizontalLayoutData(.5, 1.0));
		editHLC.add(rightEditVLC, new HorizontalLayoutData(.5, 1.0));
				
		VerticalLayoutContainer editVLC = new VerticalLayoutContainer();
		editVLC.add(titlePanel, new VerticalLayoutData(1.0, .18));
		editVLC.add(authorFP, new VerticalLayoutData(1.0, .18));
		editVLC.add(editHLC, new VerticalLayoutData(1.0, .64));
	
		HorizontalLayoutContainer mainHLC = new HorizontalLayoutContainer();
		ScrollSupport scrContainer = mainHLC.getScrollSupport();
		mainHLC.setScrollMode(ScrollMode.AUTO);
		mainHLC.add(imgFP, new HorizontalLayoutData(.4, 1.0));
		mainHLC.add(editVLC, new HorizontalLayoutData(.6, 1.0));

		panel.addDomHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent e) {
	        	  if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
	        		  saveImageEntry(true);
		        }
		    }			
		}, KeyDownEvent.getType());
		panel.setHeading("Image Editor (entry last modified on " + imgEntry.getModifiedOn() + ")");
		panel.setSize( Integer.toString(Window.getClientWidth()/100*80),Integer.toString(Window.getClientHeight()/100*80));
		panel.add(mainHLC);
		panel.addTool(saveToolButton);
		panel.addTool(closeToolButton);
		panel.setResize(true);
		panel.setCollapsible(false);
		//Info.display("ERROR", test);
		//System.err.println(test);
		//Info.display("test","test");
		new Resizable(panel);
		new Draggable(panel);
		

		
	}

	@Override
	public void setfocus() {
		titleField.getFocusSupport().setIgnore(false);
		titleField.focus();
	}

	/**
	 * This method will save the currently selected ImageEntry from the left list of previews. In future versions, the missing fields will be added. Also, the
	 * Photographer ID us currently not mapped to the text entry in this box. (shows a yes/no dialog first)
	 */
	public void dohandle(boolean closeEditorRequested) {
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
						closeEditor(imgEntry);
					}
				} else {
					Info.display("ERROR", "Image information has NOT been updated!");
				}
				if (closeEditorRequested) {
					closeEditor(imgEntry);
				}
				
			}
		}
		);
	}
	private void saveImageEntry(boolean closeEditorRequested) {
		if (!verifyInputs()) {
			if (closeEditorRequested) {
				closeEditor(imgEntry);
			}
			return;
		}
		Util.showYesNo("Save", "Saving will overwrite the existing information in the Database. This cannot be reversed! Do you want to continue?", new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dohandle(closeEditorRequested);
			}
		}, new SelectHandler() {
				
			@Override
			public void onSelect(SelectEvent event) {
				if (closeEditorRequested) {
					closeEditor(imgEntry);
				}
			}
		}, new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent e) {
				dohandle(closeEditorRequested);
			}}
	
			
		
		);
//		Dialog simple = new Dialog();
//		simple.setHeading("Save");
//		simple.setWidth(300);
//		simple.setResizable(false);
//		simple.setHideOnButtonClick(true);
//		simple.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
//		simple.setBodyStyleName("pad-text");
//		simple.getBody().addClassName("pad-text");
//		simple.add(
//				new Label("Saving will overwrite the existing information in the Database. This cannot be reversed! Do you want to continue?"));
//		simple.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				updateImageEntry();
//				// only of the yes button is selected, we will perform the command
//				// to simplify we just ignore the no button event by doing nothing
//
//				dbService.updateImageEntry(imgEntry, new AsyncCallback<Boolean>() {
//					public void onFailure(Throwable caught) {
//						Info.display("ERROR", "Image information has NOT been updated!");
//						simple.hide();
//					}
//
//					@Override
//					public void onSuccess(Boolean result) {
//						if (result) {
//							if (closeEditorRequested) {
//								closeEditor(imgEntry);
//							}
//						} else {
//							Info.display("ERROR", "Image information has NOT been updated!");
//						}
//						simple.hide();
//					}
//				}
//				);
//			}
//		});
//		simple.show();
//		// constrain the dialog to the viewport (for small mobile screen sizes)
//		Rectangle bounds = simple.getElement().getBounds();
//		Rectangle adjusted = simple.getElement().adjustForConstraints(bounds);
//		if (adjusted.getWidth() != bounds.getWidth() || adjusted.getHeight() != bounds.getHeight()) {
//			simple.setPixelSize(adjusted.getWidth(), adjusted.getHeight());
//		}

	}

	private void updateImageEntry() {
		imgEntry.setTitle(titleField.getCurrentValue());
		imgEntry.setShortName(shortNameField.getCurrentValue());
		imgEntry.setCopyright(copyrightArea.getCurrentValue());
		imgEntry.setComment(commentArea.getCurrentValue());
		imgEntry.setDate(dateField.getCurrentValue());
		imgEntry.setImageAuthor(authorSelectionCB.getCurrentValue());
		imgEntry.setImageTypeID(imageTypeSelection.getCurrentValue().getImageTypeID());
	}

	/**
	 * @return
	 */
	private boolean verifyInputs() {
		return titleField.isValid() && shortNameField.isValid() && copyrightArea.validate() && dateField.validate();
	}

}
