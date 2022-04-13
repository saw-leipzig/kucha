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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.testing.MockEditorError;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
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
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.fx.client.Draggable.DraggableAppearance;
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
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
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
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.ModifiedEntry;
import de.cses.shared.PhotographerEntry;

public class SingleImageEditor extends AbstractEditor {

	private TextField titleField;
	private TextField shortNameField;
	private TextField inventoryNumberField;
	private NumberField<Double> widthField;
	private NumberField<Double> heightField;
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
	private HTMLPanel imgHP;
	private FramedPanel imgFP;
	private ComboBox<LocationEntry> locationSelectionCB;
	private LocationProperties locationProps;
	private ListStore<LocationEntry> locationEntryLS;
	private JavaScriptObject osdDic;
	private int numSave;
	private OSDLoader osdLoader;
	private ToolButton saveToolButton;

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
	interface LocationViewTemplates extends XTemplates {
		@XTemplate("<div>{country}, {town}<br>{name}</div>")
		SafeHtml caveLabel(String name, String town, String country);

		@XTemplate("<div>{country}<br>{name}</div>")
		SafeHtml caveLabel(String name, String country);

		@XTemplate("<div>{name}</div>")
		SafeHtml caveLabel(String name);
	}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<figure style='text-align: center; margin: 0;'>"
			
				+ "<img src='{imgUri}' style='position: relative; padding: 5px; background: black;'>"
				+ "</figure>")
		SafeHtml view(SafeUri imgUri);
	}
	interface LocationProperties extends PropertyAccess<LocationEntry> {
		ModelKeyProvider<LocationEntry> locationID();
		LabelProvider<LocationEntry> name();
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
    public static void activatePanel(PopupPanel popup) {
    	popup.center();
    	
    }
	@Override
	protected void loadModifiedEntries() {
		sourceStore.clear();
	    dbService.getModifiedAbstractEntry((AbstractEntry)imgEntry, new AsyncCallback<ArrayList<ModifiedEntry>>() {
			
				@Override
				public void onSuccess(ArrayList<ModifiedEntry> result) {
					for (ModifiedEntry entry : result) {
						sourceStore.add(entry);
					}
				}
				
				@Override
				public void onFailure(Throwable caught) {
				}
			});
	 
	}

    public static native void exportStaticMethod() /*-{
		$wnd.activatepanel = $entry(@de.cses.client.images.SingleImageEditor::activatePanel(*));
	}-*/;
	 private void loadLocations() {
			for (LocationEntry locEntry : StaticTables.getInstance().getLocationEntries().values()) {
				locationEntryLS.add(locEntry);
			}
			locationEntryLS.addSortInfo(new StoreSortInfo<LocationEntry>(new ValueProvider<LocationEntry, String>(){

				@Override
				public String getValue(LocationEntry object) {
					return object.getCounty()+" "+object.getTown()+" "+object.getName();
				}

				@Override
				public void setValue(LocationEntry object, String value) {}

				@Override
				public String getPath() {
					return "name";
				}}, SortDir.ASC));
			ImageEntry ry2 = imgEntry;
			if (imgEntry.getLocation() != null) {
				locationSelectionCB.setValue(imgEntry.getLocation());
			}
//			else {
//				if (imgEntry.getTitle().toLowerCase().contains("britishmuseum")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("26"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_vanda_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("12"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("ashmoleanmuseum")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("11"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().matches(".*[t][a][0-9]{4}.*")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("4"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().matches(".*[i]{3}[0-9]{4}.*")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("4"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_hoppferencemuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("13"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_ikuohirayamasilkroadmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("7"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_tokyonationalmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("9"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_tokyouniversity_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("28"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_mwoods_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("37"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_eremitage_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("5"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_nmk_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("15"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_academiasinica_taipei_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("25"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_seattleasianartmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("22"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_bostonmfa_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("20"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_museumofart_cleveland_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("17"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_detroitinstituteofarts_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("35"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_harvardfoggartmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("34"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_nelson-atkinsmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("36"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_brooklynartmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("27"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_metropolitanmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("19"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_metropolitanmuseumny_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("19"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_metropolitanmuseumny_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("19"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_nyc_metropolitan_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("19"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_metny_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("19"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_pennmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("21"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_asianartmuseumsanfrancisco_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("16"));
//				}
//				else if (imgEntry.getTitle().toLowerCase().contains("_smithsonianamericanartmuseum_")) {
//					locationSelectionCB.setValue(locationEntryLS.findModelWithKey("23"));
//				}
//			}
	 }


    
    /**
	 * This widget allows to edit the information of an ImageEntry, i.e. an image in the database. It also allows for uploading new images to the database.
	 */
	public SingleImageEditor(ImageEntry imgEntry, EditorListener av) {
		this.addEditorListener(av);
		this.imgEntry = imgEntry;
		this.imgEntry.setLastChangedByUser(UserLogin.getInstance().getUsername());
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
		loadLocations();
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


	private void addOSDPanel(int clwidth, int clheight) {
		int width = (clwidth/100*59);
//		Info.display("ImageWidth",Integer.toString(width));
		if (width<640) {
			width=640;
		}
		HTMLPanel zoomPanel = new HTMLPanel(SafeHtmlUtils.fromTrustedString("<figure class='paintRepImgPreview' style='height: "+Integer.toString(clheight/100*90)+"px;width: "+Integer.toString(width)+"px;text-align: center;'><div id= '"+imgEntry.getFilename()+"' style='width: 100%; height: 100%;text-align: center;'></div></fugure>"));

		imgFP.clear();
		imgFP.add(zoomPanel);
		
	}
	private void initPanel() {
		numSave=0;
		osdLoader = new OSDLoader(imgEntry);
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
		shortNameField.addValidator(new MaxLengthValidator(32));
		shortNamePanel.setHeading("Short Name");
		shortNameField.setValue(imgEntry.getShortName());
		shortNamePanel.add(shortNameField);

		FramedPanel inventoryNumberPanel = new FramedPanel();
		inventoryNumberField = new TextField();
		inventoryNumberPanel.setHeading("Inventory Number");
		inventoryNumberField.setValue(imgEntry.getInventoryNumber());
		inventoryNumberPanel.add(inventoryNumberField);
		
		FramedPanel widthPanel = new FramedPanel();
		widthField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());

		widthPanel.setHeading("Width");
		if (imgEntry.getWidth()>0) {
			widthField.setValue(imgEntry.getWidth());			
		}
		else {
			widthField.setValue(0.0);
		}

		widthPanel.add(widthField);
		
		FramedPanel heightPanel = new FramedPanel();
		heightField = new NumberField<Double>(new NumberPropertyEditor.DoublePropertyEditor());

		heightPanel.setHeading("Height");
		if (imgEntry.getHeight()>0) {
			heightField.setValue(imgEntry.getHeight());			
		}
		else {
			heightField.setValue(0.0);
		}
		heightPanel.add(heightField);
		
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
		authorSelectionCB.addValueChangeHandler(new ValueChangeHandler<PhotographerEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PhotographerEntry> event) {
				imgEntry.setImageAuthor(event.getValue());
			}
		});

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
		locationProps = GWT.create(LocationProperties.class);
		locationEntryLS = new ListStore<LocationEntry>(locationProps.locationID());
		StoreFilter<LocationEntry> filter = new StoreFilter<LocationEntry>() {
		    @Override
		    public boolean select(Store<LocationEntry> store, LocationEntry parent, LocationEntry item) {
		    	
		      boolean canView = false; 
		      
		      if ((item.getCounty()!=null)) {
		    	  if (item.getCounty().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
		    	  canView = true;
		    	  }
		      };
		      if ((item.getTown()!=null)) {
		    	  if (item.getTown().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
		    	  canView = true;
		    	  }
		      };
		      if ((item.getName()!=null)) {
		    	  if (item.getName().toLowerCase().contains(locationSelectionCB.getText().toLowerCase())) {
		    	  canView = true;
		    	  }
		      };
		      //if (canView) {
	    	  //Util.doLogging("Found: "+item.getName()+", gesucht wurde: "+locationSelectionCB.getText());
		      //};
		      return canView;
		    }
		  };
		  locationEntryLS.addFilter(filter);

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
		
		FramedPanel currentLocationFP = new FramedPanel();
		currentLocationFP.setHeading("Current Location");
		locationSelectionCB = new ComboBox<LocationEntry>(locationEntryLS, locationProps.name(), new AbstractSafeHtmlRenderer<LocationEntry>() {

			@Override
			public SafeHtml render(LocationEntry item) {
				final LocationViewTemplates lvTemplates = GWT.create(LocationViewTemplates.class);
				if ((item.getCounty() != null) && (!item.getCounty().isEmpty())) {
					if ((item.getTown() != null) && (!item.getTown().isEmpty())) {
						return lvTemplates.caveLabel(item.getName(), item.getRegion()!=null && !item.getRegion().isEmpty() ? item.getTown()+" ("+item.getRegion()+")" : item.getTown(), item.getCounty());
					} else if ((item.getRegion() != null) && (!item.getRegion().isEmpty())) {
						return lvTemplates.caveLabel(item.getName(), item.getTown()!=null && !item.getTown().isEmpty() ? item.getTown()+" ("+item.getRegion()+")" : item.getRegion(), item.getCounty());
					} else {
						return lvTemplates.caveLabel(item.getName(), item.getCounty());
					}
				} else {
					return lvTemplates.caveLabel(item.getName());
				}
			}
		});
		locationSelectionCB.setEmptyText("select current location");
		locationSelectionCB.setTypeAhead(false);
		locationSelectionCB.setEditable(true);
	
		locationSelectionCB.setTriggerAction(TriggerAction.ALL);
		locationSelectionCB.addValueChangeHandler(new ValueChangeHandler<LocationEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<LocationEntry> event) {
				imgEntry.setLocation(event.getValue());
			}
		});
		ToolButton resetLocationSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetLocationSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetLocationSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				locationSelectionCB.setValue(null, true);
			}
		});
		// adding new locations
		ToolButton newLocationPlusTool = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		newLocationPlusTool.setToolTip(Util.createToolTip("add Location"));
		newLocationPlusTool.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addLocationDialog = new PopupPanel();
				FramedPanel newLocationFP = new FramedPanel();
				newLocationFP.setHeading("Add Location");
				VerticalLayoutContainer locationVLC = new VerticalLayoutContainer();
				TextField locationNameField = new TextField();
				locationNameField.addValidator(new MinLengthValidator(2));
				locationNameField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationNameField, "Name"));
				TextField locationTownField = new TextField();
				locationTownField.addValidator(new MinLengthValidator(2));
				locationTownField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationTownField, "Town"));
				TextField locationRegionField = new TextField();
				locationRegionField.addValidator(new MinLengthValidator(2));
				locationRegionField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationRegionField, "Region"));
				TextField locationCountryField = new TextField();
				locationCountryField.addValidator(new MinLengthValidator(2));
				locationCountryField.addValidator(new MaxLengthValidator(64));
				locationVLC.add(new FieldLabel(locationCountryField, "Country"));
				TextField locationUrlField = new TextField();
				locationUrlField.addValidator(new MinLengthValidator(2));
				locationUrlField.addValidator(new MaxLengthValidator(256));
				locationVLC.add(new FieldLabel(locationUrlField, "URL"));
				newLocationFP.add(locationVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (locationNameField.isValid()) {
							LocationEntry lEntry = new LocationEntry();
							lEntry.setName(locationNameField.getCurrentValue());
							lEntry.setTown(locationTownField.getCurrentValue());
							lEntry.setRegion(locationRegionField.getCurrentValue());
							lEntry.setCounty(locationCountryField.getCurrentValue());
							lEntry.setUrl(locationUrlField.getCurrentValue());
							dbService.insertLocationEntry(lEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									lEntry.setLocationID(result);
									locationEntryLS.add(lEntry);
								}
							});
							addLocationDialog.hide();
						}
					}
				});
				newLocationFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addLocationDialog.hide();
					}
				});
				newLocationFP.addButton(cancelButton);
				addLocationDialog.add(newLocationFP);
				addLocationDialog.setModal(true);
				addLocationDialog.center();
			}
		});				
		currentLocationFP.addTool(newLocationPlusTool);
		currentLocationFP.addTool(resetLocationSelectionTB);
		currentLocationFP.add(locationSelectionCB);

		
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
		HorizontalLayoutContainer imageDimensionslHLC = new HorizontalLayoutContainer();
		imageDimensionslHLC.add(widthPanel, new HorizontalLayoutData(.5, 1.0));
		imageDimensionslHLC.add(heightPanel, new HorizontalLayoutData(.5, 1.0));
		ToolButton deleteToolButton = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
		deleteToolButton.setToolTip(Util.createToolTip("delete"));
		deleteToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				de.cses.client.Util.showYesNo("Delete Warning!", "Proceeding will remove this Entry from the Database, are you sure?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						imgEntry.delete();
						if (imgEntry==null) {Util.doLogging("imgEntry is null!");}
						closeEditor(imgEntry);
						deleteEntry(imgEntry);
					}
				}, new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 
					}
				}, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						
					}}
			
					
			
			  );
			}
		});
		
		saveToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveToolButton.setToolTip(Util.createToolTip("save"));
		saveToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveToolButton.disable();
				Util.doLogging("Save-Button hit");
				save(false,0);
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
						 save(true,0);
//						if (verifyInputs()) {
//							closeEditor(null);
//						}
					}
				}, new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
						 closeEditor(null);
					}
				}, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						
						//save(true,0);
						//closeEditor(null);
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
		ToolButton resetTB = new ToolButton(ToolButton.REFRESH);
		resetTB.setToolTip(Util.createToolTip("Reload this image.", "This will delete the former picture!"));
		resetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				final DialogBox imageUploadPanel = new DialogBox(false);
				ImageUploader iu = new ImageUploader(new ImageUploadListener() {

					@Override
					public void uploadCompleted(int newImageID, final String filename) {
						imageUploadPanel.hide();
						for (EditorListener el :getListenerList()) {
							((ImageView)el).getImageResultView().addResult(new ImageView(imgEntry,UriUtils.fromTrustedString("icons/load_active.png"),((ImageView)el).getImageResultView()));
							String imageIDs = Integer.toString(imgEntry.getImageID());
							((ImageView)el).getImageResultView().getPics(imageIDs, 120, UserLogin.getInstance().getSessionID());
							
							}							}

					@Override
					public void uploadCanceled() {
						imageUploadPanel.hide();
					}
				}, imgEntry);
				imageUploadPanel.add(iu);
				imageUploadPanel.setGlassEnabled(true);
				imageUploadPanel.center();
				imageUploadPanel.show();
			}

			});

//	    Document doc = Document.get();
//	    ScriptElement script = doc.createScriptElement();
//	    script.setSrc("scripts/openseadragon-bin-2.4.2/openseadragon.min.js");
//	    script.setType("text/javascript");
//	    doc.getBody().appendChild(script);

		

		//imgFP.setId("openseadragon1");
		//imgHP = createZoomeImage(imageUri.asString());
		imgFP = new FramedPanel();
		try {
			imgFP.setHeading("Image format: " + imgEntry.getFilename().substring(imgEntry.getFilename().lastIndexOf(".")+1).toUpperCase());			
		} catch (Exception e) {
			imgFP.setHeading("Error accured, assembling titel: "+e.getMessage());			

		}
		//addOSDPanel();
		//Util.doLogging(Integer.toString(getListenerList().size()));
		//EditorListener el = getListenerList().get(0);
		//PopupPanel popP = ((ImageView)(el)).getEditorPanel();
		//exportStaticMethod();
		//createZoomeImage(imageUri.asString(),zoomPanel.getElement());
		
		imgFP.addTool(resetTB);
		imgFP.addTool(viewFullSizeTB);
		
		VerticalLayoutContainer leftEditVLC = new VerticalLayoutContainer();
		leftEditVLC.add(inventoryNumberPanel, new VerticalLayoutData(1.0, .20));
		leftEditVLC.add(shortNamePanel, new VerticalLayoutData(1.0, .20));
		leftEditVLC.add(copyrightPanel, new VerticalLayoutData(1.0, .4));
		leftEditVLC.add(datePanel, new VerticalLayoutData(1.0, .20));

		VerticalLayoutContainer rightEditVLC = new VerticalLayoutContainer();
		rightEditVLC.add(imageDimensionslHLC, new VerticalLayoutData(1.0, .20));
		rightEditVLC.add(imageAccessLevelHLC, new VerticalLayoutData(1.0, .20));
		rightEditVLC.add(commentPanel, new VerticalLayoutData(1.0, .6));

		HorizontalLayoutContainer editHLC = new HorizontalLayoutContainer();
		editHLC.add(leftEditVLC, new HorizontalLayoutData(.5, 1.0));
		editHLC.add(rightEditVLC, new HorizontalLayoutData(.5, 1.0));
				
		VerticalLayoutContainer editVLC = new VerticalLayoutContainer();
		editVLC.add(titlePanel, new VerticalLayoutData(1.0, .12));
		editVLC.add(authorFP, new VerticalLayoutData(1.0, .12));
		editVLC.add(currentLocationFP, new VerticalLayoutData(1.0, .12));
		editVLC.add(editHLC, new VerticalLayoutData(1.0, .64));
	
		HorizontalLayoutContainer mainHLC = new HorizontalLayoutContainer();
		ScrollSupport scrContainer = mainHLC.getScrollSupport();
		mainHLC.setScrollMode(ScrollMode.AUTO);
		HorizontalLayoutContainer imgScroll = new HorizontalLayoutContainer();
		imgScroll.setScrollMode(ScrollMode.AUTOX);

		imgScroll.add(imgFP);
		mainHLC.add(imgScroll, new HorizontalLayoutData(.6, 1.0));
		mainHLC.add(editVLC, new HorizontalLayoutData(.4, 1.0));

		panel.addDomHandler(new KeyDownHandler() {
		    @Override
		    public void onKeyDown(KeyDownEvent e) {

	        	  if ((e.isShiftKeyDown()) && (e.getNativeKeyCode() == KeyCodes.KEY_ENTER)) {
	        		  	Util.doLogging("Shift-Enter hit");
		  				de.cses.client.Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {
							
							@Override
							public void onSelect(SelectEvent event) {
				        		save(true,0);
								//closeEditor(null);
							}
						}, new SelectHandler() {
								
							@Override
							public void onSelect(SelectEvent event) {
								closeEditor(null);
							}
						}, new KeyDownHandler() {

							@Override
							public void onKeyDown(KeyDownEvent e) {
								//if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
								//closeEditor(null);
							}
					
							
						}
					  );

		        }
		    }			
		}, KeyDownEvent.getType());
		panel.setHeading("Image Editor (entry number: " + Integer.toString(imgEntry.getImageID()) + ")");
		panel.setSize( Integer.toString(Window.getClientWidth()/100*90),Integer.toString(Window.getClientHeight()/100*90));
		panel.add(mainHLC);
		createNextPrevButtons();
		panel.addTool(modifiedToolButton);
		panel.addTool(prevToolButton);
		panel.addTool(nextToolButton);
		panel.addTool(deleteToolButton);
		panel.addTool(saveToolButton);
		panel.addTool(closeToolButton);
		panel.setResize(true);
		panel.addResizeHandler(new ResizeHandler() {
			@Override
			public void onResize(ResizeEvent event) {
				
				//Util.doLogging("Broser-Dimensions: " +Integer.toString(Window.getClientWidth())+" x "+Integer.toString(Window.getClientHeight()));
				addOSDPanel(event.getWidth(),event.getHeight());
				osdLoader.destroyAllViewers();
				osdDic = OSDLoader.createDic();
				osdLoader.setosd();
				
			}
		});
		panel.setCollapsible(false);
		//Info.display("ERROR", test);
		//System.err.println(test);
		//Info.display("test","test");
		new Resizable(panel);
		new Draggable(panel, panel.getHeader(), GWT.<DraggableAppearance> create(DraggableAppearance.class));


		
	}

	@Override
	public void setfocus() {
		osdLoader.setosd();
		titleField.getFocusSupport().setIgnore(false);
		titleField.focus();
	}

	/**
	 * This method will save the currently selected ImageEntry from the left list of previews. In future versions, the missing fields will be added. Also, the
	 * Photographer ID us currently not mapped to the text entry in this box. (shows a yes/no dialog first)
	 */
	public void dohandle(boolean closeEditorRequested, int slide) {
		Util.doLogging("dohandle triggered");
		updateImageEntryInForm();
		// only of the yes button is selected, we will perform the command
		// to simplify we just ignore the no button event by doing nothing

		dbService.updateImageEntry(imgEntry, new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				saveToolButton.enable();
				Info.display("ERROR", "Image information has NOT been updated!");
				Util.doLogging(caught.getLocalizedMessage());
				
			}

			@Override
			public void onSuccess(Boolean result) {
				saveToolButton.enable();
				if (result) {
					Info.display("Successfully saved!", "Image information has been updated!");
					if (closeEditorRequested) {
						closeEditor(imgEntry);
					}
					if (slide!=0) {
						doslide(slide);
					}
					if (closeEditorRequested) {
						closeEditor(imgEntry);
					}
				} else {
					Info.display("ERROR", "Image information has NOT been updated!");
				}

				
			}
		}
		);
	}
	@Override
	protected void save(boolean closeEditorRequested, int slide) {
		Util.doLogging("number of saves:"+Integer.toString(numSave));
		numSave+=1;
		for (EditorListener el :getListenerList()) {
			if (el instanceof ImageView) {
				((ImageView)el).setEditor(imgEntry);
			}
		}
		if (!verifyInputs()) {
			saveToolButton.enable();
			Info.display("Warning!","Saving aborted, due to incorrectly filled form.");
			if (closeEditorRequested) {
				Util.showYesNo("Warning", "Saving cancelled due to incorrectly filled forms! Do you want to continue closing the form? All changed data will be lost!", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						closeEditor(imgEntry);
					}
				}, new SelectHandler() {
						
					@Override
					public void onSelect(SelectEvent event) {
					}
				}, new KeyDownHandler() {

					@Override
					public void onKeyDown(KeyDownEvent e) {
						}}
			
					
				
				);

				
			}
			return;
		}
		Util.showYesNo("Save", "Saving will overwrite the existing information in the Database. This cannot be reversed! Do you want to continue?", new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dohandle(closeEditorRequested,slide);
			}
		}, new SelectHandler() {
				
			@Override
			public void onSelect(SelectEvent event) {
				saveToolButton.enable();
				if (closeEditorRequested) {
					closeEditor(imgEntry);
				}
			}
		}, new KeyDownHandler() {

			@Override
			public void onKeyDown(KeyDownEvent e) {
				//dohandle(closeEditorRequested, slide);
			}}
	
			
		
		);


	}

	private void updateImageEntryInForm() {
		imgEntry.setTitle(titleField.getCurrentValue());
		imgEntry.setShortName(shortNameField.getCurrentValue());
		imgEntry.setCopyright(copyrightArea.getCurrentValue());
		imgEntry.setComment(commentArea.getCurrentValue());
		imgEntry.setDate(dateField.getCurrentValue());
		imgEntry.setImageAuthor(authorSelectionCB.getCurrentValue());
		imgEntry.setImageTypeID(imageTypeSelection.getCurrentValue().getImageTypeID());
		imgEntry.setWidth(widthField.getCurrentValue());
		imgEntry.setHeight(heightField.getCurrentValue());
		imgEntry.setInventoryNumber(inventoryNumberField.getCurrentValue());
	}

	/**
	 * @return
	 */
	private boolean verifyInputs() {
		try {
			return titleField.isValid() && shortNameField.isValid() && copyrightArea.validate() && dateField.validate();

		}catch (Exception e) {
			return false;
		}
	}

}
