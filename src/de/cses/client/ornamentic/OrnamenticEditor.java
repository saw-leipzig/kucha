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
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
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
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.client.ornamentic.OrnamentCaveAttributes.MainTypologicalClassProperties;
import de.cses.client.ornamentic.OrnamentCaveAttributes.MainTypologicalClassViewTemplates;
import de.cses.client.ornamentic.OrnamentCaveAttributes.StructureOrganizationProperties;
import de.cses.client.ornamentic.OrnamentCaveAttributes.StructureOrganizationViewTemplates;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveRelation;
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
	private ImageProperties imgProperties;
	private OrnamentEntry ornamentEntry = null;;
	private ListStore<StructureOrganization> structureOrganization;
	private ComboBox<StructureOrganization> structureorganizationComboBox;
	private StructureOrganizationProperties structureOrganizationProps;
	private ListStore<MainTypologicalClass> mainTypologicalClass;
	private ComboBox<MainTypologicalClass> mainTypologicalClassComboBox;
	private MainTypologicalClassProperties mainTypologicalClassProps;

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

		imgProperties = GWT.create(ImageProperties.class);
		imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
		ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);
		mainTypologicalClassProps = GWT.create(MainTypologicalClassProperties.class);
		mainTypologicalClass = new ListStore<MainTypologicalClass>(mainTypologicalClassProps.mainTypologicalClassID());

		structureOrganizationProps = GWT.create(StructureOrganizationProperties.class);
		structureOrganization = new ListStore<StructureOrganization>(structureOrganizationProps.structureOrganizationID());

		dbService.getStructureOrganizations(new AsyncCallback<ArrayList<StructureOrganization>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<StructureOrganization> result) {
				structureOrganization.clear();
				for (StructureOrganization pe : result) {
					structureOrganization.add(pe);
				}
			}
		});

		dbService.getMainTypologicalClasses(new AsyncCallback<ArrayList<MainTypologicalClass>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<MainTypologicalClass> result) {
				mainTypologicalClass.clear();
				for (MainTypologicalClass pe : result) {
					mainTypologicalClass.add(pe);
				}
			}
		});

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
		final TextArea discription = new TextArea ();
		panel.add(header, new VerticalLayoutData(1.0, .125));
		header.add(discription);
		discription.setAllowBlank(true);
		if (ornamentEntry != null) {
			discription.setText(ornamentEntry.getDescription());
		}

		final TextArea  remarks = new TextArea ();
		remarks.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Remarks");
		header.add(remarks);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			remarks.setText(ornamentEntry.getRemarks());
		}

		final TextArea  annotations = new TextArea ();
		annotations.setAllowBlank(true);
		header = new FramedPanel();

		header.setHeading("Annotations");
		header.add(annotations);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			annotations.setText(ornamentEntry.getAnnotations());
		}

		final TextArea  interpretation = new TextArea ();
		interpretation.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Interpretation");
		header.add(interpretation);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			interpretation.setText(ornamentEntry.getInterpretation());
		}

		mainTypologicalClassComboBox = new ComboBox<MainTypologicalClass>(mainTypologicalClass, mainTypologicalClassProps.name(),
				new AbstractSafeHtmlRenderer<MainTypologicalClass>() {

					@Override
					public SafeHtml render(MainTypologicalClass item) {
						final MainTypologicalClassViewTemplates pvTemplates = GWT.create(MainTypologicalClassViewTemplates.class);
						return pvTemplates.mainTypologicalClass(item.getName());
					}
				});

		header = new FramedPanel();
		header.setHeading("Main typologycal class");
		mainTypologicalClassComboBox.setTriggerAction(TriggerAction.ALL);
		header.add(mainTypologicalClassComboBox);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			mainTypologicalClassComboBox.select(mainTypologicalClass.findModelWithKey(Integer.toString(ornamentEntry.getMainTypologicalClassID())));
		}

		final TextArea  mainTypoTextField = new TextArea ();
		mainTypoTextField.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Interpretation");
		header.add(mainTypoTextField);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			mainTypoTextField.setText(ornamentEntry.getInterpretation());
		}
		//wird eventuell mal privat oder geloescht
	/*	structureorganizationComboBox = new ComboBox<StructureOrganization>(structureOrganization, structureOrganizationProps.name(),
				new AbstractSafeHtmlRenderer<StructureOrganization>() {

					@Override
					public SafeHtml render(StructureOrganization item) {
						final StructureOrganizationViewTemplates pvTemplates = GWT.create(StructureOrganizationViewTemplates.class);
						return pvTemplates.structureOrganization(item.getName());
					}
				});

		header = new FramedPanel();
		header.setHeading("Structure-Organization");
		header.add(structureorganizationComboBox);
		structureorganizationComboBox.setTriggerAction(TriggerAction.ALL);
		panel.add(header, new VerticalLayoutData(1.0, .125));
		if (ornamentEntry != null) {
			structureorganizationComboBox.select(structureOrganization.findModelWithKey(Integer.toString(ornamentEntry.getStructureOrganizationID())));
		}
		*/

		final TextArea  references = new TextArea ();
		references.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("References");
		header.add(references);
		verticalgeneral2Background.add(header, new VerticalLayoutData(1.0, .125));
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
		caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(ornamentCaveRelationProps.ornamentID());

		cavesList = new ListView<OrnamentCaveRelation, String>(caveOrnamentRelationList, ornamentCaveRelationProps.name());
		cavesList.setAllowTextSelection(true);

		if (ornamentEntry != null) {
			for (int i = 0; i < ornamentEntry.getCavesRelations().size(); i++) {
				caveOrnamentRelationList.add(ornamentEntry.getCavesRelations().get(i));
			}
		}

		cavesContentPanel.setHeading("Added caves:");

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
				OrnamentEntry oEntry = new OrnamentEntry();
				for (int i = 0; i < caveOrnamentRelationList.size(); i++) {
					oEntry.getCavesRelations().add(caveOrnamentRelationList.get(i));
				}
				for (int i = 0; i < imageEntryList.size(); i++) {
					oEntry.getImages().add(imageEntryList.get(i));
				}
				if(ornamentCode.getText() == "") {
					Window.alert("Please insert Ornamentation Code");
					return;
				}
				oEntry.setCode(ornamentCode.getText());
				oEntry.setDescription(discription.getText());
				oEntry.setRemarks(remarks.getText());
				oEntry.setAnnotations(annotations.getText());
				oEntry.setInterpretation(interpretation.getText());
				oEntry.setReferences(references.getText());
				if(mainTypologicalClassComboBox.getCurrentValue() == null) {
					oEntry.setMainTypologicalClassID(0); // unknown
				} else {
				oEntry.setMainTypologicalClassID(mainTypologicalClassComboBox.getCurrentValue().getMainTypologicalClassID());
				}
				//if(structureorganizationComboBox.getCurrentValue() == null) {
				//	oEntry.setStructureOrganizationID(0); // unknown
				//}
			//	else {
			//		oEntry.setStructureOrganizationID(structureorganizationComboBox.getCurrentValue().getStructureOrganizationID());
			//	}
				
				// send ornament to server
				dbService.saveOrnamentEntry(oEntry, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
						Window.alert("Saving failed");
						Window.alert(caught.getMessage());
					}

					@Override
					public void onSuccess(Boolean result) {
						Window.alert("saved");
						closeEditor();
					}
				});

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
		FramedPanel imagesFramedPanel = new FramedPanel();
		imagesFramedPanel.setHeading("Images");

		imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
			@Override
			public void setValue(ImageEntry object, ImageEntry value) {
			}
		});

		if(ornamentEntry != null){
			for(int i = 0; i< ornamentEntry.getImages().size(); i ++){
			imageEntryList.add(ornamentEntry.getImages().get(i));
			}	
		}
			
		imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
			final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

			public SafeHtml render(ImageEntry item) {
				SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=150" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
				return imageViewTemplates.image(imageUri, item.getTitle());
			}
		}));

		imageListView.setSize("500", "290");
		ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
		lf.setSize("500", "300");

		imageSelector = new ImageSelector(ImageSelector.PHOTO, this);
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

	public void imageSelected(int imageID) {
		if (imageID != 0) {
			dbService.getImage(imageID, new AsyncCallback<ImageEntry>() {

				@Override
				public void onSuccess(ImageEntry result) {
					imageEntryList.add(result);
				}

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
				}
			});
		}
		imageSelectionDialog.hide();
	}

	interface ImageProperties extends PropertyAccess<ImageEntry> {
		ModelKeyProvider<ImageEntry> imageID();

		LabelProvider<ImageEntry> title();
	}

	interface ImageViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);

	}

	interface OrnamentCaveRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<OrnamentCaveRelation> ornamentID(); // changed to ornamentID (was caveID) (Andreas)
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

}

