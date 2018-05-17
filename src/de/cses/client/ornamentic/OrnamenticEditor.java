package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.dnd.core.client.ListViewDropTarget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ornamentic.OrnamentCaveAttributes.StructureOrganizationProperties;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentClassEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.StructureOrganization;

public class OrnamenticEditor extends AbstractEditor implements ImageSelectorListener {
	FramedPanel header;
	private VBoxLayoutContainer widget;
	FramedPanel cavesContentPanel;
	private OrnamentCaveRelationProperties ornamentCaveRelationProps;
	private ListStore<OrnamentCaveRelation> caveOrnamentRelationList;
	private OrnamenticEditor ornamenticEditor = this;
	private ListView<OrnamentCaveRelation, String> cavesList;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	protected PopupPanel imageSelectionDialog;
	protected ImageSelector imageSelector;
	private ListView<ImageEntry, ImageEntry> imageListView;
	private ListStore<ImageEntry> imageEntryList;
	private ListStore<OrnamentClassEntry> ornamentClassEntryList;
	private ImageProperties imgProperties;
	private OrnamentEntry ornamentEntry = null;;
	private ListStore<StructureOrganization> structureOrganization;
	private ComboBox<StructureOrganization> structureorganizationComboBox;
	private ComboBox<OrnamentClassEntry> ornamentClassComboBox;
	private StructureOrganizationProperties structureOrganizationProps;
	private ListStore<MainTypologicalClass> mainTypologicalClass;
	private OrnamentClassProperties ornamentClassProps;
	private InnerSecondaryPatternsProperties innerSecondaryPatternsProps;
	private ListStore<InnerSecondaryPatternsEntry> innerSecondaryPatternsEntryList;
	private ListStore<InnerSecondaryPatternsEntry> selectedinnerSecondaryPatternsEntryList;
	private ListStore<OrnamentComponentsEntry> ornamentComponents;
	private ListStore<OrnamentComponentsEntry> selectedOrnamentComponents;
	private OrnamentComponentsProperties ornamentComponentsProps;

	@Override
	public Widget asWidget() {
		if (widget == null) {
			BoxLayoutData flex = new BoxLayoutData();
			flex.setFlex(1);
			widget = new VBoxLayoutContainer();
			widget.add(createForm(), flex);
		}
		return widget;
	}

	public OrnamenticEditor(OrnamentEntry ornamentEntry) {
		this.ornamentEntry = ornamentEntry;
	}

	public Widget createForm() {
		HorizontalLayoutContainer horizontBackground = new HorizontalLayoutContainer();
		VerticalLayoutContainer verticalgeneral2Background = new VerticalLayoutContainer();

		VerticalLayoutContainer verticalgeneral3Background = new VerticalLayoutContainer();

		imgProperties = GWT.create(ImageProperties.class);
		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		ornamentClassProps = GWT.create(OrnamentClassProperties.class);
		innerSecondaryPatternsProps = GWT.create(InnerSecondaryPatternsProperties.class);
		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
		ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);

		structureOrganizationProps = GWT.create(StructureOrganizationProperties.class);
		structureOrganization = new ListStore<StructureOrganization>(structureOrganizationProps.structureOrganizationID());
		ornamentClassEntryList = new ListStore<OrnamentClassEntry>(ornamentClassProps.ornamentClassID());
		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(innerSecondaryPatternsProps.innerSecondaryPatternsID());
		selectedinnerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(
				innerSecondaryPatternsProps.innerSecondaryPatternsID());

		selectedOrnamentComponents = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());
		ornamentComponents = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());

		dbService.getOrnamentComponents(new AsyncCallback<ArrayList<OrnamentComponentsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			public void onSuccess(ArrayList<OrnamentComponentsEntry> result) {

				ornamentComponents.clear();
				selectedOrnamentComponents.clear();
				if (ornamentEntry != null) {
					for (OrnamentComponentsEntry pe : result) {
						int count = 0;
						for (OrnamentComponentsEntry oe : ornamentEntry.getOrnamentComponents()) {
							if (pe.getOrnamentComponentsID() != oe.getOrnamentComponentsID()) {
								count++;
							}
							if (count == ornamentEntry.getOrnamentComponents().size()) {
								ornamentComponents.add(pe);
							}
						}
					}
					for (OrnamentComponentsEntry oe : ornamentEntry.getOrnamentComponents()) {
						selectedOrnamentComponents.add(oe);
					}
					if (ornamentEntry.getOrnamentComponents().size() == 0) {
						for (OrnamentComponentsEntry nu : result) {
							ornamentComponents.add(nu);
						}
					}
				} else {
					for (OrnamentComponentsEntry pe : result) {
						ornamentComponents.add(pe);
					}
				}

			}
		});

		dbService.getInnerSecondaryPatterns(new AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<InnerSecondaryPatternsEntry> result) {

				innerSecondaryPatternsEntryList.clear();
				selectedinnerSecondaryPatternsEntryList.clear();
				if (ornamentEntry != null) {
					for (InnerSecondaryPatternsEntry pe : result) {
						int count = 0;
						for (InnerSecondaryPatternsEntry oe : ornamentEntry.getInnerSecondaryPatterns()) {
							if (pe.getInnerSecondaryPatternsID() != oe.getInnerSecondaryPatternsID()) {
								count++;
							}
							if (count == ornamentEntry.getInnerSecondaryPatterns().size()) {
								innerSecondaryPatternsEntryList.add(pe);
							}
						}
					}
					for (InnerSecondaryPatternsEntry oe : ornamentEntry.getInnerSecondaryPatterns()) {
						selectedinnerSecondaryPatternsEntryList.add(oe);
					}
					if (ornamentEntry.getInnerSecondaryPatterns().size() == 0) {
						for (InnerSecondaryPatternsEntry nu : result) {
							innerSecondaryPatternsEntryList.add(nu);
						}
					}
				} else {
					for (InnerSecondaryPatternsEntry pe : result) {
						innerSecondaryPatternsEntryList.add(pe);
					}
				}

			}
		});

		dbService.getOrnamentClass(new AsyncCallback<ArrayList<OrnamentClassEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentClassEntry> result) {
				ornamentClassEntryList.clear();
				for (OrnamentClassEntry pe : result) {
					ornamentClassEntryList.add(pe);
				}
				if (ornamentEntry != null) {
					ornamentClassComboBox.setValue(ornamentClassEntryList.findModelWithKey(Integer.toString(ornamentEntry.getOrnamentClass())));
				}
			}
		});

		/*
		 * dbService.getStructureOrganizations(new AsyncCallback<ArrayList<StructureOrganization>>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { caught.printStackTrace(); }
		 * 
		 * @Override public void onSuccess(ArrayList<StructureOrganization> result) { structureOrganization.clear(); for (StructureOrganization pe : result) {
		 * structureOrganization.add(pe); } } });
		 */

		/*
		 * dbService.getMainTypologicalClasses(new AsyncCallback<ArrayList<MainTypologicalClass>>() {
		 * 
		 * @Override public void onFailure(Throwable caught) { caught.printStackTrace(); }
		 * 
		 * @Override public void onSuccess(ArrayList<MainTypologicalClass> result) { mainTypologicalClass.clear(); for (MainTypologicalClass pe : result) {
		 * mainTypologicalClass.add(pe); } } });
		 */

		TabPanel tabpanel = new TabPanel();
		tabpanel.setWidth(620);
		tabpanel.setHeight(600);

		VerticalLayoutContainer panel = new VerticalLayoutContainer();
		VerticalLayoutContainer panel2 = new VerticalLayoutContainer();

		final TextField ornamentCode = new TextField();
		ornamentCode.setAllowBlank(false);
		header = new FramedPanel();
		header.setHeading("Ornament Code");
		header.add(ornamentCode);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			ornamentCode.setText(ornamentEntry.getCode());
		}

		header = new FramedPanel();
		header.setHeading("Description");
		final TextArea discription = new TextArea();
		panel.add(header, new VerticalLayoutData(1.0, .3));
		header.add(discription);
		discription.setAllowBlank(true);
		if (ornamentEntry != null) {
			discription.setText(ornamentEntry.getDescription());
		}

		final TextArea remarks = new TextArea();
		remarks.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Remarks");
		header.add(remarks);
		panel.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			remarks.setText(ornamentEntry.getRemarks());
		}

		final TextArea annotations = new TextArea();
		annotations.setAllowBlank(true);
		header = new FramedPanel();

		header.setHeading("Annotations");
		header.add(annotations);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			annotations.setText(ornamentEntry.getAnnotations());
		}

		final TextArea interpretation = new TextArea();
		interpretation.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Interpretation");
		header.add(interpretation);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			interpretation.setText(ornamentEntry.getInterpretation());
		}

		ornamentClassComboBox = new ComboBox<OrnamentClassEntry>(ornamentClassEntryList, ornamentClassProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentClassEntry>() {

					@Override
					public SafeHtml render(OrnamentClassEntry item) {
						final OrnamentClassViewTemplates pvTemplates = GWT.create(OrnamentClassViewTemplates.class);
						return pvTemplates.ornamentClass(item.getName());
					}
				});

		header = new FramedPanel();
		header.setHeading("Ornament Class");
		ornamentClassComboBox.setTriggerAction(TriggerAction.ALL);
		header.add(ornamentClassComboBox);
		panel.add(header, new VerticalLayoutData(1.0, .125));

		ToolButton addOrnamentClassButton = new ToolButton(ToolButton.PLUS);

		FramedPanel ornamentClassFramedPanel = new FramedPanel();
		ornamentClassFramedPanel.setHeading("New Ornament Class");

		ToolButton saveOrnamentClass = new ToolButton(ToolButton.SAVE);
		ornamentClassFramedPanel.add(saveOrnamentClass);

		ToolButton cancelOrnamentClass = new ToolButton(ToolButton.CLOSE);

		ornamentClassFramedPanel.addTool(cancelOrnamentClass);
		ornamentClassFramedPanel.addTool(saveOrnamentClass);

		HorizontalLayoutContainer newOrnamentClassLayoutPanel = new HorizontalLayoutContainer();
		TextField newOrnamentClassTextField = new TextField();
		newOrnamentClassLayoutPanel.add(newOrnamentClassTextField);
		ornamentClassFramedPanel.add(newOrnamentClassLayoutPanel);
		addOrnamentClassButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newOrnamentClassPopup = new PopupPanel();
				newOrnamentClassPopup.add(ornamentClassFramedPanel);
				ornamentClassFramedPanel.setSize("150", "80");
				newOrnamentClassPopup.center();
				cancelOrnamentClass.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newOrnamentClassPopup.hide();
					}
				});
				saveOrnamentClass.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						OrnamentClassEntry entry = new OrnamentClassEntry();
						entry.setName(newOrnamentClassTextField.getText());
						dbService.addOrnamentClass(entry, new AsyncCallback<OrnamentClassEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(OrnamentClassEntry result) {
								Util.doLogging(this.getClass().getName() + " added " + result.getName());
								ornamentClassEntryList.add(result);
								newOrnamentClassPopup.hide();
							}
						});
						newOrnamentClassPopup.hide();
					}
				});

			}
		});
		header.addTool(addOrnamentClassButton);

		// wird eventuell mal privat oder geloescht
		/*
		 * structureorganizationComboBox = new ComboBox<StructureOrganization>(structureOrganization, structureOrganizationProps.name(), new
		 * AbstractSafeHtmlRenderer<StructureOrganization>() {
		 * 
		 * @Override public SafeHtml render(StructureOrganization item) { final StructureOrganizationViewTemplates pvTemplates =
		 * GWT.create(StructureOrganizationViewTemplates.class); return pvTemplates.structureOrganization(item.getName()); } });
		 * 
		 * header = new FramedPanel(); header.setHeading("Structure-Organization"); header.add(structureorganizationComboBox);
		 * structureorganizationComboBox.setTriggerAction(TriggerAction.ALL); panel.add(header, new VerticalLayoutData(1.0, .125)); if (ornamentEntry != null) {
		 * structureorganizationComboBox.select(structureOrganization.findModelWithKey(Integer.toString(ornamentEntry.getStructureOrganizationID()))); }
		 */

		final TextArea references = new TextArea();
		references.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("References");
		header.add(references);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .3));
		if (ornamentEntry != null) {
			references.setText(ornamentEntry.getReferences());
		}

		TextButton addCaveButton = new TextButton();
		addCaveButton.setText("Add Cave");

		VerticalPanel cavesPanel = new VerticalPanel();
		header = new FramedPanel();
		header.setHeading("Cave");

		header.add(cavesPanel);
		panel2.add(header, new VerticalLayoutData(1.0, 1.0));

		// Place for Caves
		ClickHandler addCaveClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				OrnamentCaveAttributes attributespopup = new OrnamentCaveAttributes();

				attributespopup.setOrnamentic(ornamenticEditor);
				attributespopup.setGlassEnabled(true);
				attributespopup.center();

			}

		};

		addCaveButton.addHandler(addCaveClickHandler, ClickEvent.getType());

		cavesContentPanel = new FramedPanel();
		cavesPanel.add(cavesContentPanel);
		caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(ornamentCaveRelationProps.ornamentCaveRelationID());

		cavesList = new ListView<OrnamentCaveRelation, String>(caveOrnamentRelationList, ornamentCaveRelationProps.name());
		cavesList.setAllowTextSelection(true);

		if (ornamentEntry != null) {
			Util.doLogging("Listengroesse: " + ornamentEntry.getCavesRelations().size());
			for (int i = 0; i < ornamentEntry.getCavesRelations().size(); i++) {
				caveOrnamentRelationList.add(ornamentEntry.getCavesRelations().get(i));
				Util.doLogging("Daten: notes: " + ornamentEntry.getCavesRelations().get(i).getNotes());

				Util.doLogging("Daten: ID: " + ornamentEntry.getCavesRelations().get(i).getCaveEntry().getCaveID() + "DistrictID: "
						+ ornamentEntry.getCavesRelations().get(i).getCaveEntry().getDistrictID());
			}
			Util.doLogging("nachher liste: " + Integer.toString(caveOrnamentRelationList.size()));
		}

		cavesContentPanel.setHeading("Added caves");
		cavesContentPanel.add(cavesList);

		TextButton edit = new TextButton("edit");
		TextButton delete = new TextButton("delete");

		header.addButton(addCaveButton);
		header.addButton(edit);
		header.addButton(delete);

		ClickHandler deleteClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				caveOrnamentRelationList.remove(cavesList.getSelectionModel().getSelectedItem());

			}
		};
		delete.addHandler(deleteClickHandler, ClickEvent.getType());

		ClickHandler editClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				Util.doLogging("Vorher caveid: " + cavesList.getSelectionModel().getSelectedItem());
				Util.doLogging("Vorher caveid: " + cavesList.getSelectionModel().getSelectedItem().getCaveEntry().getOfficialNumber());
				OrnamentCaveAttributes attributespopup = new OrnamentCaveAttributes(cavesList.getSelectionModel().getSelectedItem());
				attributespopup.setOrnamentic(ornamenticEditor);
				attributespopup.setGlassEnabled(true);
				attributespopup.center();

			}
		};
		edit.addHandler(editClickHandler, ClickEvent.getType());

		TextButton save = new TextButton("save");

		ClickHandler saveClickHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (ornamentCode.validate()) {
					Window.alert("Please insert Ornamentation Code");
					return;
				}

				ArrayList<OrnamentCaveRelation> corList = new ArrayList<OrnamentCaveRelation>();
				for (int i = 0; i < caveOrnamentRelationList.size(); i++) {
					corList.add(caveOrnamentRelationList.get(i));
				}
				ornamentEntry.setCavesRelations(corList);
				
				ArrayList<ImageEntry> ieList = new ArrayList<ImageEntry>();
				for (int i = 0; i < imageEntryList.size(); i++) {
					ieList.add(imageEntryList.get(i));
				}
				ornamentEntry.setImages(ieList);
				
				ornamentEntry.setCode(ornamentCode.getText());
				ornamentEntry.setDescription(discription.getText());
				ornamentEntry.setRemarks(remarks.getText());
				ornamentEntry.setAnnotations(annotations.getText());
				ornamentEntry.setInterpretation(interpretation.getText());
				ornamentEntry.setReferences(references.getText());
				if (ornamentClassComboBox.getValue() == null) {
					// what should happen here?
				} else {
					Util.doLogging("ID gesetzt");
					ornamentEntry.setOrnamentClass(ornamentClassComboBox.getValue().getOrnamentClassID());
				}
				
				ArrayList<InnerSecondaryPatternsEntry> ispeList = new ArrayList<InnerSecondaryPatternsEntry>();
				for (int i = 0; i < selectedinnerSecondaryPatternsEntryList.size(); i++) {
					ispeList.add(selectedinnerSecondaryPatternsEntryList.get(i));
				}
				ornamentEntry.setInnerSecondaryPatterns(ispeList);

				ArrayList<OrnamentComponentsEntry> oceList = new ArrayList<OrnamentComponentsEntry>();
				for (int i = 0; i < selectedOrnamentComponents.size(); i++) {
					oceList.add(selectedOrnamentComponents.get(i));
				}
				ornamentEntry.setOrnamentComponents(oceList);

				// if(structureorganizationComboBox.getCurrentValue() == null) {
				// oEntry.setStructureOrganizationID(0); // unknown
				// }
				// else {
				// oEntry.setStructureOrganizationID(structureorganizationComboBox.getCurrentValue().getStructureOrganizationID());
				// }

				// send ornament to server
				if (ornamentEntry.getOrnamentID() == 0) {
					dbService.saveOrnamentEntry(ornamentEntry, new AsyncCallback<Integer>() {

						@Override
						public void onFailure(Throwable caught) {
							Util.showWarning("Saving failed", caught.getMessage());
						}

						@Override
						public void onSuccess(Integer result) {
							Util.doLogging(this.getClass().getName() + " saving sucessful");
							ornamentEntry.setOrnamentID(result);
							closeEditor();
						}
					});
				} else {
					dbService.updateOrnamentEntry(ornamentEntry, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							Util.showWarning("Update failed", caught.getMessage());
						}

						@Override
						public void onSuccess(Boolean result) {
							closeEditor();
						}
					});
				}

			}
		};
		save.addHandler(saveClickHandler, ClickEvent.getType());

		FramedPanel framedpanelornamentic = new FramedPanel();
		TextButton cancel = new TextButton("cancel");

		ClickHandler cancelHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				closeEditor();
			}

		};
		cancel.addHandler(cancelHandler, ClickEvent.getType());

		framedpanelornamentic.addButton(save);
		framedpanelornamentic.addButton(cancel);

		horizontBackground.add(panel, new HorizontalLayoutData(.5, 1.0));
		horizontBackground.add(panel2, new HorizontalLayoutData(.5, 1.0));
		framedpanelornamentic.setHeading("Ornamentation Editor");
		framedpanelornamentic.add(horizontBackground);

		tabpanel.add(framedpanelornamentic, "1. General");

		FramedPanel general2FramedPanel = new FramedPanel();
		general2FramedPanel.setHeading("2. General");
		tabpanel.add(general2FramedPanel, "2. General");
		general2FramedPanel.add(verticalgeneral2Background);

		FramedPanel general3FramedPanel = new FramedPanel();
		general3FramedPanel.setHeading("3. General");
		tabpanel.add(general3FramedPanel, "3. General");
		general3FramedPanel.add(verticalgeneral3Background);

		HorizontalLayoutContainer ornamentComponentsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<OrnamentComponentsEntry, String> ornamentComponentView = new ListView<OrnamentComponentsEntry, String>(ornamentComponents,
				ornamentComponentsProps.name());
		ListView<OrnamentComponentsEntry, String> selectedOrnamentComponentView = new ListView<OrnamentComponentsEntry, String>(
				selectedOrnamentComponents, ornamentComponentsProps.name());
		ornamentComponentsHorizontalPanel.add(ornamentComponentView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		ornamentComponentsHorizontalPanel.add(selectedOrnamentComponentView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");
		new ListViewDragSource<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");

		new ListViewDropTarget<OrnamentComponentsEntry>(selectedOrnamentComponentView).setGroup("Components");
		new ListViewDropTarget<OrnamentComponentsEntry>(ornamentComponentView).setGroup("Components");

		header = new FramedPanel();
		header.setHeading("Select Ornament Components");
		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getOrnamentComponents().size(); i++) {
				selectedOrnamentComponents.add(ornamentEntry.getOrnamentComponents().get(i));

			}
		}
		header.add(ornamentComponentsHorizontalPanel);

		verticalgeneral3Background.add(header, new VerticalLayoutData(1.0, .3));

		ToolButton addComponentButton = new ToolButton(ToolButton.PLUS);
		header.addTool(addComponentButton);

		FramedPanel componentFramedPanel = new FramedPanel();
		componentFramedPanel.setHeading("New Component");

		ToolButton saveComponent = new ToolButton(ToolButton.SAVE);
		componentFramedPanel.addTool(saveComponent);

		ToolButton cancelComponent = new ToolButton(ToolButton.CLOSE);

		componentFramedPanel.addTool(cancelComponent);

		HorizontalLayoutContainer newComponentLayoutPanel = new HorizontalLayoutContainer();
		TextField newComponentTextField = new TextField();
		newComponentLayoutPanel.add(newComponentTextField);
		componentFramedPanel.add(newComponentLayoutPanel);
		addComponentButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newComponentPopup = new PopupPanel();
				newComponentPopup.add(componentFramedPanel);
				newComponentPopup.setSize("150px", "80px");
				newComponentPopup.center();

				cancelComponent.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newComponentPopup.hide();
					}
				});
				saveComponent.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						OrnamentComponentsEntry entry = new OrnamentComponentsEntry();
						entry.setName(newComponentTextField.getText());

						dbService.addOrnamentComponent(entry, new AsyncCallback<OrnamentComponentsEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(OrnamentComponentsEntry result) {
								ornamentComponents.add(entry);
								Util.doLogging(this.getClass().getName() + " saving sucessful");
								newComponentPopup.hide();
							}
						});
					}
				});
			}
		});

		HorizontalLayoutContainer innerSecondaryPatternsHorizontalPanel = new HorizontalLayoutContainer();

		ListView<InnerSecondaryPatternsEntry, String> innerSecondaryPatternsView = new ListView<InnerSecondaryPatternsEntry, String>(
				innerSecondaryPatternsEntryList, innerSecondaryPatternsProps.name());
		ListView<InnerSecondaryPatternsEntry, String> selectedinnerSecondaryPatternsView = new ListView<InnerSecondaryPatternsEntry, String>(
				selectedinnerSecondaryPatternsEntryList, innerSecondaryPatternsProps.name());
		innerSecondaryPatternsHorizontalPanel.add(innerSecondaryPatternsView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));
		innerSecondaryPatternsHorizontalPanel.add(selectedinnerSecondaryPatternsView, new HorizontalLayoutData(.5, 1.0, new Margins(1)));

		new ListViewDragSource<InnerSecondaryPatternsEntry>(innerSecondaryPatternsView).setGroup("innersec");
		new ListViewDragSource<InnerSecondaryPatternsEntry>(selectedinnerSecondaryPatternsView).setGroup("innersec");

		new ListViewDropTarget<InnerSecondaryPatternsEntry>(selectedinnerSecondaryPatternsView).setGroup("innersec");
		new ListViewDropTarget<InnerSecondaryPatternsEntry>(innerSecondaryPatternsView).setGroup("innersec");

		header = new FramedPanel();
		header.setHeading("Select inner Secondary Patterns");
		ToolButton addInnerSecondaryPatternsButton = new ToolButton(ToolButton.PLUS);

		FramedPanel innersecFramedPanel = new FramedPanel();
		innersecFramedPanel.setHeading("New Inner Secondary Pattern");

		ToolButton saveInnerSec = new ToolButton(ToolButton.SAVE);
		innersecFramedPanel.add(saveInnerSec);

		ToolButton cancelInnerSec = new ToolButton(ToolButton.CLOSE);

		innersecFramedPanel.addTool(cancelInnerSec);
		innersecFramedPanel.addTool(saveInnerSec);

		HorizontalLayoutContainer newinnersecLayoutPanel = new HorizontalLayoutContainer();
		TextField newinnersecTextField = new TextField();
		newinnersecLayoutPanel.add(newinnersecTextField);
		innersecFramedPanel.add(newinnersecLayoutPanel);
		addInnerSecondaryPatternsButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel newInnerSecondaryPatternPopup = new PopupPanel();
				newInnerSecondaryPatternPopup.add(innersecFramedPanel);
				innersecFramedPanel.setSize("150", "80");
				newInnerSecondaryPatternPopup.center();
				cancelInnerSec.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						newInnerSecondaryPatternPopup.hide();
					}
				});
				saveInnerSec.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						InnerSecondaryPatternsEntry entry = new InnerSecondaryPatternsEntry();
						entry.setName(newinnersecTextField.getText());
						dbService.addInnerSecondaryPatterns(entry, new AsyncCallback<InnerSecondaryPatternsEntry>() {

							public void onFailure(Throwable caught) {
								caught.printStackTrace();
							}

							@Override
							public void onSuccess(InnerSecondaryPatternsEntry result) {
								Util.doLogging(this.getClass().getName() + "saving sucessful");
								innerSecondaryPatternsEntryList.add(result);
								newInnerSecondaryPatternPopup.hide();
							}
						});
						newInnerSecondaryPatternPopup.hide();
					}
				});

			}
		});

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getInnerSecondaryPatterns().size(); i++) {
				selectedinnerSecondaryPatternsEntryList.add(ornamentEntry.getInnerSecondaryPatterns().get(i));

			}
		}
		header.add(innerSecondaryPatternsHorizontalPanel);
		header.addTool(addInnerSecondaryPatternsButton);

		verticalgeneral3Background.add(header, new VerticalLayoutData(1.0, .3));

		FramedPanel imagesFramedPanel = new FramedPanel();
		imagesFramedPanel.setHeading("Images");

		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getImages().size(); i++) {
				imageEntryList.add(ornamentEntry.getImages().get(i));
			}
		}

		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString(
						"resource?imageID=" + item.getImageID() + "&thumb=150" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				return imageViewTemplates.image(imageUri, item.getTitle());
			}
		}));

		imageListView.setSize("500", "290");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("500", "300");

		imageSelector = new ImageSelector(this);
		TextButton addImageButton = new TextButton("Select Image");
		addImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageSelectionDialog = new PopupPanel();
				imageSelectionDialog.add(imageSelector);
				imageSelectionDialog.setModal(true);
				imageSelectionDialog.center();
			}
		});
		TextButton removeImageButton = new TextButton("Remove Image");
		removeImageButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageEntryList.remove(imageListView.getSelectionModel().getSelectedItem());
			}
		});

		VerticalPanel imagesVerticalPanel = new VerticalPanel();
		imagesVerticalPanel.add(lf);
		HorizontalPanel hbp = new HorizontalPanel();
		hbp.add(addImageButton);
		hbp.add(removeImageButton);
		imagesVerticalPanel.add(hbp);
		tabpanel.add(imagesFramedPanel, "Images");
		imagesFramedPanel.add(imagesVerticalPanel);
		tabpanel.setTabScroll(true);

		return tabpanel;

	}

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();
	}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);

	}

	interface OrnamentClassViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentClass(String name);
	}

	interface OrnamentClassProperties extends PropertyAccess<OrnamentClassEntry> {
		ModelKeyProvider<OrnamentClassEntry> ornamentClassID();

		LabelProvider<OrnamentClassEntry> name();
	}

	interface OrnamentComponentsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<OrnamentComponentsEntry> ornamentComponentsID();

		ValueProvider<OrnamentComponentsEntry, String> name();
	}

	interface InnerSecondaryPatternsProperties extends PropertyAccess<InnerSecondaryPatternsEntry> {
		ModelKeyProvider<InnerSecondaryPatternsEntry> innerSecondaryPatternsID();

		ValueProvider<InnerSecondaryPatternsEntry, String> name();
	}

	interface OrnamentCaveRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<OrnamentCaveRelation> ornamentCaveRelationID();
		ValueProvider<OrnamentCaveRelation, String> name();
	}

	public ListStore<OrnamentCaveRelation> getCaveOrnamentRelationList() {
		return caveOrnamentRelationList;
	}

	public void setCaveOrnamentRelationList(ListStore<OrnamentCaveRelation> caveOrnamentRelationList) {
		this.caveOrnamentRelationList = caveOrnamentRelationList;
	}

	public ListView<OrnamentCaveRelation, String> getCavesList() {
		return cavesList;
	}

	public void setCavesList(ListView<OrnamentCaveRelation, String> cavesList) {
		this.cavesList = cavesList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.images.ImageSelectorListener#imageSelected(de.cses.shared.ImageEntry)
	 */
	@Override
	public void imageSelected(ImageEntry entry) {
		if (entry.getImageID() != 0) {
			imageEntryList.add(entry);
		}
		imageSelectionDialog.hide();
	}

}
