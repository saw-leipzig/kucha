package de.cses.client.ornamentic;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ListField;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.shared.CaveEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentEntry;


public class Ornamentic implements IsWidget, ImageSelectorListener{
	FramedPanel header;
	 private VBoxLayoutContainer widget;
	 ContentPanel cavesContentPanel;
	 private OrnamentCaveRelationProperties ornamentCaveRelationProps;
	 private ListStore<OrnamentCaveRelation> caveOrnamentRelationList;
	 private Ornamentic ornamentic = this;
	 private ListView<OrnamentCaveRelation, String> cavesList;
	 private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	 protected PopupPanel imageSelectionDialog;
	 protected ImageSelector imageSelector;
		private ListView<ImageEntry, ImageEntry> imageListView;
		private ListStore<ImageEntry> imageEntryList;
		private ImageProperties imgProperties;


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

public Widget createForm(){
	imgProperties = GWT.create(ImageProperties.class);
	imageEntryList = new ListStore<ImageEntry>(imgProperties.imageID());
	ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);
	
	TabPanel tabpanel = new TabPanel();
	

	VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
	
	
  final TextField ornamentCode = new TextField();
  ornamentCode.setAllowBlank(false);
	header = new FramedPanel();
	header.setHeading("Ornament Code");
	header.add(ornamentCode);
	vlc.add(header);
  
  
  FramedPanel descriptionFramedPanel = new FramedPanel();
  descriptionFramedPanel.setHeading("Description");
  final TextField discription = new TextField();
  descriptionFramedPanel.add(discription);
  
  discription.setAllowBlank(true);
  vlc.add(descriptionFramedPanel);
  
  final TextField remarks = new TextField();
  remarks.setAllowBlank(true);
	header = new FramedPanel();
	header.setHeading("Remarks");
	header.add(remarks);
	vlc.add(header);
  
  final TextField annotations = new TextField();
  annotations.setAllowBlank(true);
	header = new FramedPanel();
	header.setHeading("Annotations");
	header.add(annotations);
	vlc.add(header);
  
  
  final TextField interpretation = new TextField();
  interpretation.setAllowBlank(true);
	header = new FramedPanel();
	header.setHeading("Interpretation");
	header.add(interpretation);
	vlc.add(header);
  
  final TextField references = new TextField();
  references.setAllowBlank(true);
	header = new FramedPanel();
	header.setHeading("References");
	header.add(references);
	vlc.add(header);
  

  
  Button addCaveButton = new Button();
  addCaveButton.setText("Add Cave");
 
  addCaveButton.setSize("60px", "40px");
	header = new FramedPanel();
	header.setHeading("Cave");
	header.add(addCaveButton);
	vlc.add(header);
  
  
//Place for Caves
  ClickHandler addCaveClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			OrnamentCaveAttributes attributespopup  = new OrnamentCaveAttributes();
			
      attributespopup.setOrnamentic(ornamentic);
			attributespopup.setGlassEnabled(true);
			//new Draggable(attributespopup);
			attributespopup.setWidth("900px");
			attributespopup.setHeight("630px");
			attributespopup.center();
			
		}
  	
  };
  
  addCaveButton.addClickHandler(addCaveClickHandler);
  
  cavesContentPanel = new ContentPanel();
  vlc.add(cavesContentPanel);
  caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(ornamentCaveRelationProps.caveID());
  
  

  
  
  cavesList = new ListView<OrnamentCaveRelation,String>(caveOrnamentRelationList, ornamentCaveRelationProps.name());
  cavesList.setAllowTextSelection(true);

  
  cavesContentPanel.setHeading("Added caves:");
  cavesContentPanel.add(cavesList);
  TextButton edit = new TextButton("edit");
  TextButton delete = new TextButton("delete");
  HorizontalPanel buttonCaveEditPanel = new HorizontalPanel();
  buttonCaveEditPanel.add(edit);
  buttonCaveEditPanel.add(delete);
  
  
  ClickHandler deleteClickHandler = new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
	caveOrnamentRelationList.remove(cavesList.getSelectionModel().getSelectedItem());
		
		}
  };
  delete.addHandler(deleteClickHandler, ClickEvent.getType());
  vlc.add(buttonCaveEditPanel);
  
  TextButton save = new TextButton("save"); 
  
  ClickHandler saveClickHandler = new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
		OrnamentEntry ornament = new OrnamentEntry();
		for(int i = 0; i < caveOrnamentRelationList.size();i++){
			ornament.getCavesRelations().add(caveOrnamentRelationList.get(i));
		}
		ornament.setCode(ornamentCode.getText());
		ornament.setDescription(discription.getText());
		ornament.setRemarks(remarks.getText());
		ornament.setAnnotations(annotations.getText());
		ornament.setInterpretation(interpretation.getText());
		ornament.setReferences(references.getText());
		//send ornament to server
		dbService.saveOrnamentEntry(ornament, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Window.alert("Saving failed");
				Window.alert(caught.getMessage());
			}

			@Override
			public void onSuccess(Boolean result) {
				Window.alert("saved");
				
			}
		});
		
		}
  };
  save.addHandler(saveClickHandler, ClickEvent.getType());
  
  FramedPanel framedpanelornamentic = new FramedPanel();
  TextButton cancel = new TextButton("cancel");
  

  framedpanelornamentic.addButton(save);
  framedpanelornamentic.addButton(cancel);
  
  VerticalPanel panel = new VerticalPanel();
 panel.add(vlc);

 
 
  framedpanelornamentic.setHeading("Create Ornamentic");
  framedpanelornamentic.add(panel);

	tabpanel.add(framedpanelornamentic, "General");
	
	FramedPanel imagesFramedPanel = new FramedPanel();
	imagesFramedPanel.setHeading("Images");
	
	imageListView = new ListView<ImageEntry, ImageEntry>(imageEntryList, new IdentityValueProvider<ImageEntry>() {
		@Override
		public void setValue(ImageEntry object, ImageEntry value) {
		}
	});

	imageListView.setCell(new SimpleSafeHtmlCell<ImageEntry>(new AbstractSafeHtmlRenderer<ImageEntry>() {
		final ImageViewTemplates imageViewTemplates = GWT.create(ImageViewTemplates.class);

		public SafeHtml render(ImageEntry item) {
			SafeUri imageUri = UriUtils.fromString("resource?imageID=" + item.getImageID() + "&thumb=150");
			return imageViewTemplates.image(imageUri, item.getTitle());
		}
	}));

	imageListView.setSize("340", "290");
	ListField<ImageEntry, ImageEntry> lf = new ListField<ImageEntry, ImageEntry>(imageListView);
	lf.setSize("350", "300");
	
	imageSelector = new ImageSelector(ImageSelector.PHOTO, this);
	TextButton addImageButton = new TextButton("Select Image");
	addImageButton.addSelectHandler(new SelectHandler() {

		@Override
		public void onSelect(SelectEvent event) {
			imageSelectionDialog = new PopupPanel();
			new Draggable(imageSelectionDialog);
			imageSelectionDialog.add(imageSelector);
			imageSelectionDialog.setModal(true);
			imageSelectionDialog.center();
			imageSelectionDialog.show();
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
	tabpanel.setWidth(300);
	
  
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

	// @XTemplate("<div qtip=\"{slogan}\" qtitle=\"State Slogan\">{name}</div>")
	// SafeHtml state(String slogan, String name);
}

interface OrnamentCaveRelationProperties extends PropertyAccess<CaveEntry> {
	ModelKeyProvider<OrnamentCaveRelation> caveID();

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

