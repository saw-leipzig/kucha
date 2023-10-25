package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.ImageViewTemplates;
import de.cses.client.images.ImageEditorListener;
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.TextElement;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.ExternalRessourceEntry;
import de.cses.shared.ExternalRessourceTypeEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.PhotographerEntry;
import de.cses.shared.UserEntry;



interface ExternalRessourceTypeProperties extends PropertyAccess<ExternalRessourceTypeEntry> {
	ModelKeyProvider<ExternalRessourceTypeEntry> externalRessourceTypeID();

	LabelProvider<ExternalRessourceTypeEntry> description();
}

interface ExternalRessourceProperties extends PropertyAccess<ExternalRessourceEntry> {
	ModelKeyProvider<ExternalRessourceEntry> imageID();

	LabelProvider<ExternalRessourceEntry> entity();
}

interface ExternalRessourceTypeEntryViewTemplates extends XTemplates {
	@XTemplate("<div style=\"border: 1px solid grey;\">{shortName}</div>")
	SafeHtml externalRessourceTypeLabel(String shortName);
}


public class ExternalRessourceSelector implements IsWidget {
	
	private FramedPanel mainPanel;
	private ToolButton addEntryTB;
	private ToolButton renameEntryTB;
	private ComboBox<ExternalRessourceTypeEntry> externalRessourceTypeSelectionCB;
	private TextArea ertURLTextArea;
	private TextArea ertDescriptionTextArea;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ListStore<ExternalRessourceTypeEntry> externalRessourceTypeEntryLS;
	private ListStore<ExternalRessourceEntry> extResEntryLS;
	private ListView<ExternalRessourceEntry, ExternalRessourceEntry> extResListView;
	private TextArea erEntitiyArea;
	
	private List<ExternalRessourceEntry> selectedEntries;
	private ExternalRessourceSelectorListener el;
	
	public ExternalRessourceSelector(List<ExternalRessourceEntry> selectedEntries,ExternalRessourceSelectorListener el) {
		this.selectedEntries = selectedEntries;
		this.el=el;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}
	public void update() {
		this.selectedEntries = el.getExternalRessourceList();
		this.extResEntryLS.clear();
		for (ExternalRessourceEntry el: this.selectedEntries) {
			this.extResEntryLS.add(el);
		}
		this.extResListView.refresh();
	}
	private Widget getResTypeToolbutton(Boolean isEdit, SelectHandler save) {
		ToolButton addResTypeTB;
		if (!isEdit) {
			addResTypeTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
			addResTypeTB.setToolTip(Util.createToolTip("Add External Ressource Type."));
			
		} else {
			addResTypeTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
			addResTypeTB.setToolTip(Util.createToolTip("Edit External Ressource Type."));
		}
		addResTypeTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (isEdit && externalRessourceTypeSelectionCB == null) {
					return;
				}
				PopupPanel addExtResType = new PopupPanel();
				FramedPanel addExtResTypeFP = new FramedPanel();
				HTML htmlResType = new HTML();
				htmlResType.setWidth("100%");
				htmlResType.setWordWrap(true);
				htmlResType.setStylePrimaryName("html-display");
				htmlResType.setWidth("280px");
				ertDescriptionTextArea = new TextArea();
				ertDescriptionTextArea.addValidator(new MinLengthValidator(2));
				ertDescriptionTextArea.addValidator(new MaxLengthValidator(256));
				if (isEdit) {
					ertDescriptionTextArea.setValue(externalRessourceTypeSelectionCB.getValue().getDescription());
				}
				FramedPanel fpanelDescription = new FramedPanel();
				fpanelDescription.setHeading("Description");
				fpanelDescription.add(ertDescriptionTextArea);
				FramedPanel fpanelResTypeSource = new FramedPanel();
				fpanelResTypeSource.setHeading("Ressource Type Source");
				ertURLTextArea = new TextArea();
				ertURLTextArea.addValidator(new MinLengthValidator(2));
				ertURLTextArea.addValidator(new MaxLengthValidator(256));
				if (isEdit) {
					ertURLTextArea.setValue(externalRessourceTypeSelectionCB.getValue().getSource());
				}
				fpanelResTypeSource.add(ertURLTextArea);
				VerticalLayoutContainer addExtResTypeVLC = new VerticalLayoutContainer();
				addExtResTypeVLC.add(htmlResType, new VerticalLayoutData(1.0, .2));
				addExtResTypeVLC.add(fpanelDescription, new VerticalLayoutData(1.0, .4));
				addExtResTypeVLC.add(fpanelResTypeSource, new VerticalLayoutData(1.0, .4));
				addExtResTypeFP.add(addExtResTypeVLC);
				addExtResTypeFP.setSize("300px", "280px");
				if (isEdit) {
					addExtResTypeFP.setHeading("edit an External Ressource Type");
				} else {
					addExtResTypeFP.setHeading("add a new External Ressource Type");
				}
				ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveTB.addSelectHandler(save);
				addExtResTypeFP.addTool(saveTB);
				ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addExtResType.hide();
					}
				});
				addExtResTypeFP.addTool(cancelTB);
				addExtResType.add(addExtResTypeFP);				
				addExtResType.setModal(true);
				addExtResType.center();					
			}
		});
		return addResTypeTB;

	}

	
	private void initPanel() {
		ExternalRessourceTypeProperties extResTypeProperties  = GWT.create(ExternalRessourceTypeProperties.class);
		externalRessourceTypeEntryLS = new ListStore<ExternalRessourceTypeEntry>(extResTypeProperties.externalRessourceTypeID());
		loadTypeEntries();
		mainPanel = new FramedPanel();
		mainPanel.setHeading("External Entities");
		ExternalRessourceProperties extResProperties = GWT.create(ExternalRessourceProperties.class);
		extResEntryLS = new ListStore<ExternalRessourceEntry>(extResProperties.imageID());

		extResListView = new ListView<ExternalRessourceEntry, ExternalRessourceEntry>(extResEntryLS, new IdentityValueProvider<ExternalRessourceEntry>() {
			@Override
			public void setValue(ExternalRessourceEntry object, ExternalRessourceEntry value) {
			}
		});
		SimpleSafeHtmlCell<ExternalRessourceEntry> imageCell = new SimpleSafeHtmlCell<ExternalRessourceEntry>(new AbstractSafeHtmlRenderer<ExternalRessourceEntry>() {
			final ExternalRessourceViewTemplates extResViewTemplates = GWT.create(ExternalRessourceViewTemplates.class);
			public SafeHtml render(ExternalRessourceEntry item) {
				SafeHtml sb;
				sb = extResViewTemplates.extRes(item.getSource().getDescription(), item.getSource().getSource(), item.getEntity(), item.getSource().getSource(), item.getEntity());

				return sb;
			}
		});
		loadEntries();
		extResListView.setCell(imageCell);
		mainPanel.add(extResListView);
		addEntryTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addEntryTB.setToolTip(Util.createToolTip("Add new External Ressource."));
		addEntryTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addExternalRessourceEntryDialog = new PopupPanel();
				FramedPanel newExternalRessourceFP = new FramedPanel();
				erEntitiyArea = new TextArea();
				erEntitiyArea.addValidator(new MinLengthValidator(2));
				erEntitiyArea.addValidator(new MaxLengthValidator(256));
				FramedPanel fpanelEntity = new FramedPanel();
				fpanelEntity.setHeading("Entity");
				fpanelEntity.add(erEntitiyArea);
				externalRessourceTypeSelectionCB = new ComboBox<ExternalRessourceTypeEntry>(externalRessourceTypeEntryLS, new LabelProvider<ExternalRessourceTypeEntry>() {

					@Override
					public String getLabel(ExternalRessourceTypeEntry item) {
						
						return item.getDescription();

					}
				}, new AbstractSafeHtmlRenderer<ExternalRessourceTypeEntry>() {

					@Override
					public SafeHtml render(ExternalRessourceTypeEntry item) {
						final ExternalRessourceTypeEntryViewTemplates cvTemplates = GWT.create(ExternalRessourceTypeEntryViewTemplates.class);
						return cvTemplates.externalRessourceTypeLabel(item.getDescription());
					}
				});
				FramedPanel fpanelResType = new FramedPanel();
				fpanelResType.setHeading("Ressource Type");
				fpanelResType.add(externalRessourceTypeSelectionCB);
				fpanelResType.addTool(getResTypeToolbutton(false, new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						ExternalRessourceTypeEntry newErt = new ExternalRessourceTypeEntry(-1, ertDescriptionTextArea.getValue(), ertURLTextArea.getValue());
						dbService.insertExternalRessourceType(newErt, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							public void onSuccess(Boolean result) {
								if (result) {
									Info.display("Success", "External Ressource Type saved.");									
								} else {
									Info.display("Error!", "External Ressource Type sving finished with errors.");																		
								}
							}
						});
					}
					
				}));
				fpanelResType.addTool(getResTypeToolbutton(true, new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						ExternalRessourceTypeEntry updErt = externalRessourceTypeSelectionCB.getCurrentValue();
						updErt.setDescription(ertDescriptionTextArea.getValue());
						updErt.setSource(ertURLTextArea.getValue());
						dbService.updateExternalRessourceType(updErt, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							public void onSuccess(Boolean result) {
								if (result) {
									Info.display("Success", "External Ressource Type saved.");									
								} else {
									Info.display("Error!", "External Ressource Type saving finished with errors.");																		
								}
							}
						});

					}
					
				}));
				VerticalLayoutContainer newExternalRessourceVLC = new VerticalLayoutContainer();
				newExternalRessourceVLC.add(fpanelResType, new VerticalLayoutData(1.0, .5));
				newExternalRessourceVLC.add(fpanelEntity, new VerticalLayoutData(1.0, .5));
				newExternalRessourceFP.add(newExternalRessourceVLC);
				newExternalRessourceFP.setSize("300px", "280px");
				newExternalRessourceFP.setHeading("add a new External Ressource");
				ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (erEntitiyArea.isValid()) {
							ExternalRessourceEntry resEntry = new ExternalRessourceEntry(-1, erEntitiyArea.getCurrentValue(), externalRessourceTypeSelectionCB.getValue());
							el.addExternalRessource(resEntry);
							update();
							addExternalRessourceEntryDialog.hide();
						}
					}
				});
				newExternalRessourceFP.addTool(saveTB);
				ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addExternalRessourceEntryDialog.hide();
					}
				});
				newExternalRessourceFP.addTool(cancelTB);
				addExternalRessourceEntryDialog.add(newExternalRessourceFP);				
				addExternalRessourceEntryDialog.setModal(true);
				addExternalRessourceEntryDialog.center();					
			}
		});

		renameEntryTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		renameEntryTB.setToolTip(Util.createToolTip("Edit selected External Ressource."));
		renameEntryTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (extResListView.getSelectionModel().getSelectedItem() == null) { // we can only add a new entry if there is a parent selected
					return;
				}
				ExternalRessourceEntry extResToEdit = extResListView.getSelectionModel().getSelectedItem();
				PopupPanel editExtRes = new PopupPanel();
				FramedPanel editExtResFP = new FramedPanel();
				erEntitiyArea = new TextArea();
				erEntitiyArea.addValidator(new MinLengthValidator(2));
				erEntitiyArea.addValidator(new MaxLengthValidator(256));
				String test = extResListView.getSelectionModel().getSelectedItem().getEntity();
				erEntitiyArea.setText(test);
				FramedPanel fpanelEntity = new FramedPanel();
				fpanelEntity.setHeading("Entity");
				fpanelEntity.add(erEntitiyArea);
				ExternalRessourceTypeProperties extResProperties  = GWT.create(ExternalRessourceTypeProperties.class);

				externalRessourceTypeSelectionCB = new ComboBox<ExternalRessourceTypeEntry>(externalRessourceTypeEntryLS, new LabelProvider<ExternalRessourceTypeEntry>() {

					@Override
					public String getLabel(ExternalRessourceTypeEntry item) {
						
						return item.getDescription();

					}
				}, new AbstractSafeHtmlRenderer<ExternalRessourceTypeEntry>() {

					@Override
					public SafeHtml render(ExternalRessourceTypeEntry item) {
						final ExternalRessourceTypeEntryViewTemplates cvTemplates = GWT.create(ExternalRessourceTypeEntryViewTemplates.class);
						return cvTemplates.externalRessourceTypeLabel(item.getDescription());
					}
				});
				ExternalRessourceTypeEntry selectedErte = null;
				for (ExternalRessourceTypeEntry erte: externalRessourceTypeEntryLS.getAll()){
					if (erte.getExternalRessourceTypeID() == extResListView.getSelectionModel().getSelectedItem().getSource().getExternalRessourceTypeID()) {
						selectedErte = erte;
						break;
					}
				}
				externalRessourceTypeSelectionCB.setValue(selectedErte);
				FramedPanel fpanelResType = new FramedPanel();
				fpanelResType.setHeading("Ressource Type");
				fpanelResType.add(externalRessourceTypeSelectionCB);
				VerticalLayoutContainer editExtResVLC = new VerticalLayoutContainer();
				editExtResVLC.add(fpanelResType, new VerticalLayoutData(1.0, .5));
				editExtResVLC.add(fpanelEntity, new VerticalLayoutData(1.0, .6));
				editExtResFP.add(editExtResVLC);
				editExtResFP.setSize("300px", "280px");
				editExtResFP.setHeading("Edit External Ressource");
				ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (erEntitiyArea.isValid()) {
							ExternalRessourceEntry resEntry = extResListView.getSelectionModel().getSelectedItem();
							resEntry.setEntity(erEntitiyArea.getCurrentValue());
							resEntry.setSource(externalRessourceTypeSelectionCB.getValue());
							el.changeExternalRessource(resEntry);
							loadEntries();
							editExtRes.hide();
						}
					}
				});
				editExtResFP.addTool(saveTB);
				ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						editExtRes.hide();
					}
				});
				editExtResFP.addTool(cancelTB);
				editExtRes.add(editExtResFP);				
				editExtRes.setModal(true);
				editExtRes.center();					
			}
		});
		mainPanel.addTool(addEntryTB);
		mainPanel.addTool(renameEntryTB);
		

	}
	private void loadTypeEntries() {
		dbService.getOrnamentRessourceTypeList(new AsyncCallback<List<ExternalRessourceTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(List<ExternalRessourceTypeEntry> result) {
				externalRessourceTypeEntryLS.addAll(result);
			}
		});
	}
	private void loadEntries() {
		extResEntryLS.clear();
		for (ExternalRessourceEntry ere: el.getExternalRessourceList()) {
			extResEntryLS.add(ere);
		}
	}
	/**
	 * 
	 */
	private void createUI() {
    
	}

}
