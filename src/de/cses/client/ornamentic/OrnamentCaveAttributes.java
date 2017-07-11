package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
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
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.StructureOrganization;
import de.cses.shared.WallOrnamentCaveRelation;

public class OrnamentCaveAttributes extends PopupPanel{
	
	private FramedPanel header;
	private ComboBox<CaveEntry> caveEntryComboBox;
	private ListStore<CaveEntry> caveEntryList;
	private CaveEntryProperties caveEntryProps;
	private OrnamentOfOtherCulturesEntryProperties ornamentOfOtherCulturesEntryProps;
	private ListStore<DistrictEntry> districtEntryList;
	private DistrictEntryProperties districtEntryProps;
	private ListStore<OrnamentEntry> ornamentEntryList;
	private ListStore<OrnamentEntry> ornamentEntryList2;
	private OrnamentEntryProperties ornamentEntryProps;
	private ComboBox<DistrictEntry> districtComboBox;
	 private ListStore<WallOrnamentCaveRelation> wallsListStore;
	 private ListView<WallOrnamentCaveRelation, String> wallList;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PopupPanel popup = this;
	private OrnamentCaveAttributes ornamentCaveAttributes =this;
	private ListStore<OrnamentEntry>selectedSimilarOrnaments;
	private ListStore<OrnamentEntry>selectedRedlatedOrnaments;
	 private WallRelationProperties wallRelationProps;
	private PictorialElementSelectorObjects selector;
	private Ornamentic ornamentic;
	private ListStore<OrientationEntry> orientation;
	private ListStore<OrientationEntry> selectedorientation;
	private TextField style = new TextField();
	private TextField caveType = new TextField();
	private OrnamentCaveRelation ornamentCaveRelationEntry;
	


	
	private OrientationProperties orientationProps;
	

	
	
	public OrnamentCaveAttributes(OrnamentCaveRelation ornamentCaveRelationEntry){
	this.ornamentCaveRelationEntry = ornamentCaveRelationEntry;
		init();
	}
	public OrnamentCaveAttributes() {
		init();
	}
	


	public void init() {
		caveEntryProps = GWT.create(CaveEntryProperties.class);
		ornamentEntryProps = GWT.create(OrnamentEntryProperties.class);
		districtEntryProps = GWT.create(DistrictEntryProperties.class);
		wallRelationProps = GWT.create(WallRelationProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveEntryProps.caveID());
		districtEntryList = new ListStore<DistrictEntry>(districtEntryProps.districtID());
		ornamentEntryList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamentEntryList2 = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedSimilarOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedRedlatedOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamentOfOtherCulturesEntryProps = GWT.create(OrnamentOfOtherCulturesEntryProperties.class);
		
		orientationProps = GWT.create(OrientationProperties.class);
	
	

		selectedorientation = new ListStore<OrientationEntry>(orientationProps.orientationID());
		orientation = new ListStore<OrientationEntry>(orientationProps.orientationID());

		

		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				ornamentEntryList.clear();
				ornamentEntryList2.clear();
				for (OrnamentEntry pe : result) {
					ornamentEntryList.add(pe);
					ornamentEntryList2.add(pe);
				}
			}
		});
		
		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				districtEntryList.clear();
				for (DistrictEntry pe : result) {
					districtEntryList.add(pe);
				}
				if(ornamentCaveRelationEntry != null){
					Window.alert("selecting district ");
					Window.alert("gewaehlter district:"+ ornamentCaveRelationEntry.getDistrict().getName());
					districtComboBox.setValue(ornamentCaveRelationEntry.getDistrict());
					ValueChangeEvent.fire(districtComboBox, ornamentCaveRelationEntry.getDistrict());
					
				}
			}
		});
		
		
		dbService.getOrientations(new AsyncCallback<ArrayList<OrientationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrientationEntry> result) {
				orientation.clear();
				for (OrientationEntry pe : result) {
					if(ornamentCaveRelationEntry != null ){
						int z = 0;
						Window.alert("groesse von caves orientations" + Integer.toString(ornamentCaveRelationEntry.getOrientations().size()));
						for(OrientationEntry oe:  ornamentCaveRelationEntry.getOrientations()){
							if(oe.getOrientationID() != pe.getOrientationID()){
								z++;
							}
							else{
								break;
							}
							if(z == ornamentCaveRelationEntry.getOrientations().size() ){
								orientation.add(pe);
							}
						}
						
					}
					else{
						orientation.add(pe);
					}
					}
			
				}
		});
		
		
		setWidget(createForm());
		

	}
	
	
	
	
	public Widget createForm(){
		TabPanel tabPanel = new TabPanel();
		tabPanel.setWidth(650);
		tabPanel.setHeight(570);
		tabPanel.setTabScroll(true);
		
		VerticalPanel caveAttributesVerticalPanel = new VerticalPanel();
		
		caveAttributesVerticalPanel.add(tabPanel);
		
		
		VBoxLayoutContainer vlcCave = new VBoxLayoutContainer();

		FramedPanel cavesFrame = new FramedPanel();
		tabPanel.add(cavesFrame, "Cave");
		cavesFrame.add(vlcCave);
		cavesFrame.setHeading("Cave");
		
		districtComboBox = new ComboBox<DistrictEntry>(districtEntryList, districtEntryProps.name(),
				new AbstractSafeHtmlRenderer<DistrictEntry>() {

					@Override
					public SafeHtml render(DistrictEntry item) {
						final DistrictViewTemplates pvTemplates = GWT.create(DistrictViewTemplates.class);
						return pvTemplates.district(item.getName());
					}
				});
		districtComboBox.setTypeAhead(true);
		districtComboBox.setEditable(false);
		districtComboBox.setTriggerAction(TriggerAction.ALL);
	
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Select District");
		header.add(districtComboBox);
		vlcCave.add(header);
		
		ValueChangeHandler<DistrictEntry> districtSelectionHandler = new ValueChangeHandler<DistrictEntry>(){

			@Override
			public void onValueChange(ValueChangeEvent<DistrictEntry> event) {
				int p = event.getValue().getDistrictID();
				caveEntryList.clear();
				caveType.clear();
				caveEntryComboBox.reset();
				wallsListStore.clear();
				style.clear();
				
				dbService.getCavesbyDistrictID(p,new AsyncCallback<ArrayList<CaveEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<CaveEntry> result) {
						for (CaveEntry pe : result) {
							caveEntryList.add(pe);
						}
						if(ornamentCaveRelationEntry != null){
							caveEntryComboBox.setValue(ornamentCaveRelationEntry.getCave());
						}
					}
				});
				
			}
			
		};
		
		districtComboBox.addValueChangeHandler(districtSelectionHandler);
		
		
		caveEntryComboBox = new ComboBox<CaveEntry>(caveEntryList, caveEntryProps.officialNumber(),
				new AbstractSafeHtmlRenderer<CaveEntry>() {

					@Override
					public SafeHtml render(CaveEntry item) {
						final CaveViewTemplates pvTemplates = GWT.create(CaveViewTemplates.class);
						if ((item.getHistoricName() != null) && (item.getHistoricName().length() == 0)) {
							return pvTemplates.caveLabel(item.getOfficialNumber());
						} else {
							return pvTemplates.caveLabel(item.getOfficialNumber(), item.getHistoricName());
						}
					}
				});
		
		caveEntryComboBox.setTypeAhead(true);
		caveEntryComboBox.setEditable(false);
		caveEntryComboBox.setTriggerAction(TriggerAction.ALL);
	
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Select Cave");
		header.add(caveEntryComboBox);
		vlcCave.add(header);
		
		
	  
	  caveType.setAllowBlank(false);
	  caveType.setEnabled(false);
	 
	  
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Cave Type");
		header.add(caveType);
		vlcCave.add(header);
	  
	  
		SelectionHandler<CaveEntry> caveSelectionHandler = new SelectionHandler<CaveEntry>(){

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				int p = event.getSelectedItem().getCaveTypeID();
				wallsListStore.clear();
				dbService.getCaveTypebyID(p, new AsyncCallback<CaveTypeEntry>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(CaveTypeEntry result) {
					caveType.setText(result.getNameEN());
					}
				});
				// style ueber hoehle raussuchen
			}
			
		};
	  
	  style.setEnabled(false);
		header = new FramedPanel();
	  header.setWidth(300);
		header.setHeading("Style in which it is used");
		header.add(style);
		vlcCave.add(header);
		
		Button addWalls = new Button("Select Walls");

		
	  ClickHandler addWallsClickHandler = new ClickHandler(){
	  	

			@Override
			public void onClick(ClickEvent event) {
				if(!caveEntryComboBox.getValue().equals(null)){
				OrnamentWallAttributes attributespopup  = new OrnamentWallAttributes(caveEntryComboBox.getValue());
				attributespopup.setOrnamentCaveRelation(ornamentCaveAttributes);
	      attributespopup.setGlassEnabled(true);
				attributespopup.center();
				}
				else{
					Window.alert("Select Cave before adding walls");
				}
				
			}
	  	
	  };
		addWalls.addClickHandler(addWallsClickHandler);
		
		caveEntryComboBox.addSelectionHandler(caveSelectionHandler);
		

		header = new FramedPanel();
		header.setWidth(300);
		HorizontalPanel selectedWallsHorizontalPanel = new HorizontalPanel();
		header.setHeading("Walls");
		header.add(selectedWallsHorizontalPanel);
		vlcCave.add(header);
		
		
	  wallsListStore = new ListStore<WallOrnamentCaveRelation>(wallRelationProps.wallID());
	  
	  
	  wallList = new ListView<WallOrnamentCaveRelation,String>(wallsListStore,wallRelationProps.name());
	  wallList.setAllowTextSelection(true);
	  wallList.setWidth(300);

	  
	  
	  selectedWallsHorizontalPanel.add(wallList);
	  TextButton edit = new TextButton("edit");
	  TextButton delete = new TextButton("delete");
	  
	  header.addButton(addWalls);
	  header.addButton(edit);
	  header.addButton(delete);
	  
	  
	  ClickHandler deleteClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
		wallsListStore.remove(wallList.getSelectionModel().getSelectedItem());
			
			}
	  };
	  delete.addHandler(deleteClickHandler, ClickEvent.getType());
		
		VBoxLayoutContainer vlcAttributes = new VBoxLayoutContainer();
		

	  

	  
	  HorizontalPanel orientationHorizontalPanel = new HorizontalPanel();
	  
	  ListView<OrientationEntry, String> orientationView = new ListView<OrientationEntry, String>(orientation, orientationProps.nameEN());
	  orientationView.setPixelSize(150, 150);
	  ListView<OrientationEntry, String> selectedOrientationView = new ListView<OrientationEntry, String>(selectedorientation,orientationProps.nameEN());
	  selectedOrientationView.setPixelSize(150, 150);
	  orientationHorizontalPanel.add(orientationView);
	  orientationHorizontalPanel.add(selectedOrientationView);
	  
    new ListViewDragSource<OrientationEntry>(orientationView).setGroup("Orientation");
    new ListViewDragSource<OrientationEntry>(selectedOrientationView).setGroup("Orientation");

    new ListViewDropTarget<OrientationEntry>(selectedOrientationView).setGroup("Orientation");
    new ListViewDropTarget<OrientationEntry>(orientationView).setGroup("Orientation");
    
    
		header = new FramedPanel();
		header.setHeading("Select Orientation");
		if(ornamentCaveRelationEntry != null){
			for(int i = 0; i< ornamentCaveRelationEntry.getOrientations().size(); i ++){
			selectedorientation.add(ornamentCaveRelationEntry.getOrientations().get(i));
			
			}
		}
		header.add(orientationHorizontalPanel);
		
		vlcAttributes.add(header);
	  

	  
	  
	  final TextField colours = new TextField();
	  colours.setAllowBlank(true);
		header = new FramedPanel();
		 header.setWidth(300);
		header.setHeading("Colours");
		header.add(colours);
		vlcAttributes.add(header);
		
	  final TextField notes = new TextField();
	  colours.setAllowBlank(true);
		header = new FramedPanel();
		header.setWidth(300);
		header.setHeading("Notes");
		header.add(notes);
		vlcAttributes.add(header);
		
		
	  
	  FramedPanel attributes = new FramedPanel();
	  attributes.setHeading("Attributes");
	  attributes.add(vlcAttributes);
	  tabPanel.add(attributes, "Attributes");

	  
	  
	 
	  FramedPanel relationToOtherOrnaments = new FramedPanel();
	  relationToOtherOrnaments.setHeading("Similar of related elements or ornaments");
	  VBoxLayoutContainer vlcRelationToTherornaments1= new VBoxLayoutContainer();
	  VBoxLayoutContainer vlcRelationToTherornaments2 = new VBoxLayoutContainer();
	  HorizontalPanel backgroundHorizontalPanel = new HorizontalPanel();
	  
	  relationToOtherOrnaments.add(backgroundHorizontalPanel);
	  backgroundHorizontalPanel.add(vlcRelationToTherornaments1);
	  backgroundHorizontalPanel.add(vlcRelationToTherornaments2);
	  
	  
	  
	  
	  HorizontalPanel relatedOrnamentsHorizontalPanel = new HorizontalPanel();
	  
	  ListView<OrnamentEntry, String> ornamentListViewRelated = new ListView<OrnamentEntry, String>(ornamentEntryList, ornamentEntryProps.code());
	  ornamentListViewRelated.setPixelSize(150, 150);
	  ListView<OrnamentEntry, String> selectedRelatedOrnamentsListView = new ListView<OrnamentEntry, String>(selectedRedlatedOrnaments, ornamentEntryProps.code());
	  selectedRelatedOrnamentsListView.setPixelSize(150, 150);
	  relatedOrnamentsHorizontalPanel.add(ornamentListViewRelated);
	  relatedOrnamentsHorizontalPanel.add(selectedRelatedOrnamentsListView);
	  
    new ListViewDragSource<OrnamentEntry>(ornamentListViewRelated).setGroup("relatedOrnament");
    new ListViewDragSource<OrnamentEntry>(selectedRelatedOrnamentsListView).setGroup("relatedOrnament");

    new ListViewDropTarget<OrnamentEntry>(selectedRelatedOrnamentsListView).setGroup("relatedOrnament");
    new ListViewDropTarget<OrnamentEntry>(ornamentListViewRelated).setGroup("relatedOrnament");
    
    
		header = new FramedPanel();
		header.setHeading("Select related ornaments");
		header.add(relatedOrnamentsHorizontalPanel);
		
		 vlcRelationToTherornaments1.add(header);
		
		
	  
	  
	  
	  
	  HorizontalPanel similarOrnamentsHorizontalPanel = new HorizontalPanel();
	  ListView<OrnamentEntry, String> ornamentListViewSimilar = new ListView<OrnamentEntry, String>(ornamentEntryList2, ornamentEntryProps.code());
	  ornamentListViewSimilar.setPixelSize(150, 150);
	  ListView<OrnamentEntry, String> selectedSimilarOrnamentsListView = new ListView<OrnamentEntry, String>(selectedSimilarOrnaments, ornamentEntryProps.code());
	  selectedSimilarOrnamentsListView.setPixelSize(150, 150);
	  similarOrnamentsHorizontalPanel.add(ornamentListViewSimilar);
	  similarOrnamentsHorizontalPanel.add(selectedSimilarOrnamentsListView);
    
    new ListViewDragSource<OrnamentEntry>(ornamentListViewSimilar).setGroup("similarOrnament");
    new ListViewDragSource<OrnamentEntry>(selectedSimilarOrnamentsListView).setGroup("similarOrnament");

    new ListViewDropTarget<OrnamentEntry>(selectedSimilarOrnamentsListView).setGroup("similarOrnament");
    new ListViewDropTarget<OrnamentEntry>(ornamentListViewSimilar).setGroup("similarOrnament");
    
	 
		header = new FramedPanel();
		header.setHeading("Select similar ornaments");
		header.add(similarOrnamentsHorizontalPanel);
		vlcRelationToTherornaments2.add(header);
		
	  
	  

    
    selector = new PictorialElementSelectorObjects();
		header = new FramedPanel();
		selector.asWidget().setHeight("300px");
		selector.asWidget().setWidth("320px");
		header.setHeading("Select similar elements");
		header.add(selector.asWidget());
		header.setHeight(300);
		header.setWidth(320);
		vlcRelationToTherornaments2.add(header);
    
	 
    final TextField groupOfOrnaments = new TextField();
	  groupOfOrnaments.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Group of Ornaments");
		header.add(groupOfOrnaments);
		vlcRelationToTherornaments1.add(header);
		groupOfOrnaments.setWidth(300);
	 
	  final TextField relatedElementsofOtherCultures = new TextField();
	  relatedElementsofOtherCultures.setWidth(300);
	  
		header = new FramedPanel();
		header.setHeading("Describe related elements of other cultures");
		header.add(relatedElementsofOtherCultures);
		vlcRelationToTherornaments1.add(header);
		
	  final TextField similarElementsofOtherCultures = new TextField();
	  similarElementsofOtherCultures.setWidth(300);
	  
		header = new FramedPanel();
		header.setHeading("Describe similar elements of other cultures");
		header.add(similarElementsofOtherCultures);
		vlcRelationToTherornaments1.add(header);
	  
	  
	  
	  tabPanel.add(relationToOtherOrnaments, "Relations");
	  
	  
	  
	  HorizontalPanel buttonsPanel = new HorizontalPanel();
	  
	  TextButton save = new TextButton("save");
	  FramedPanel panel = new FramedPanel();
	  TextButton cancel = new TextButton("cancel");
	  panel.addButton(save);
	  panel.addButton(cancel);
	  caveAttributesVerticalPanel.add(buttonsPanel);
	  
	  ClickHandler cancelClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				popup.hide();
				
			}
	  };
	  cancel.addHandler(cancelClickHandler, ClickEvent.getType());
	  
	  ClickHandler saveClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
			OrnamentCaveRelation ornamentCaveRelation = new OrnamentCaveRelation();
			ornamentCaveRelation.setName("Cave: "+ caveEntryComboBox.getValue().getCaveID() );
			ornamentCaveRelation.setCave(caveEntryComboBox.getValue());
			ornamentCaveRelation.setDistrict(districtComboBox.getValue());
			ornamentCaveRelation.setColours(colours.getText());
			ornamentCaveRelation.setGroup(groupOfOrnaments.getText());
			for(int i = 0; i< selectedorientation.size(); i ++){
				ornamentCaveRelation.getOrientations().add(selectedorientation.get(i));
			}
			ornamentCaveRelation.setNotes(notes.getText());
			List<OrnamentEntry> relatedOrnaments = selectedRedlatedOrnaments.getAll();
			for( OrnamentEntry ornament : relatedOrnaments){
				ornamentCaveRelation.getRelatedOrnamentsRelations().add(ornament);
			}
			List<OrnamentEntry> similarOrnaments = selectedSimilarOrnaments.getAll();
			for( OrnamentEntry ornament : similarOrnaments){
				ornamentCaveRelation.getSimilarOrnamentsRelations().add(ornament);
			}
			Window.alert("kurz vor pictorial hinzugefuegt anzahl ist: "+ selector.getSelectedPE().size());
			for(int i = 0; i <selector.getSelectedPE().size(); i++){
				ornamentCaveRelation.getPictorialElements().add(selector.getSelectedPE().get(i));
				Window.alert("pictorial hinzugefuegt");
			}
			for(int i = 0; i<  wallsListStore.size(); i++){
				ornamentCaveRelation.getWalls().add(wallsListStore.get(i));
			}
			ornamentCaveRelation.setRelatedelementeofOtherCultures(relatedElementsofOtherCultures.getText());
			ornamentCaveRelation.setSimilarelementsOfOtherCultures(similarElementsofOtherCultures.getText());
			// set walls
			ornamentic.getCaveOrnamentRelationList().add(ornamentCaveRelation);
			
			popup.hide();
			
			}
	  };
	  save.addHandler(saveClickHandler, ClickEvent.getType());
	  
	  
		
		
	  
	  panel.setHeading("New Cave Relation");
	  panel.add(caveAttributesVerticalPanel);
		return panel;
		
	}
	interface CaveEntryProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();

		LabelProvider<CaveEntry> officialNumber();
	}
	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{officialNumber}: {historicName}</div>")
		SafeHtml caveLabel(String officialNumber, String historicName);

		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);
	}
	interface OrnamentEntryProperties extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> OrnamentID();
		 @Path("code")
	   ValueProvider<OrnamentEntry, String> code();
	}
	interface OrnamentOfOtherCulturesEntryProperties extends PropertyAccess<OrnamentOfOtherCulturesEntry> {
		ModelKeyProvider<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesID();

		ValueProvider<OrnamentOfOtherCulturesEntry, String> name();
	}
	interface DistrictEntryProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();

		LabelProvider<DistrictEntry> name();
	}
	interface OrnamentViewTemplates extends XTemplates {
		@XTemplate("<div>{code}</div>")
		SafeHtml ornament(String code);
	}
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml district(String name);
	}
	interface OrnamentOfOtherCulturesEntryViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentOfOtherCultures(String name);
	}
	
	
	interface OrientationViewTemplates extends XTemplates {
		@XTemplate("<div>{nameEN}</div>")
		SafeHtml orientation(String nameEN);
	}
	
	interface StructureOrganizationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml structureOrganization(String name);
	}
	
	interface MainTypologicalClassViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml mainTypologicalClass(String name);
	}
	
	interface OrnamentCaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentCaveType(String name);
	}
	


	interface OrientationProperties extends PropertyAccess<OrientationEntry> {
		ModelKeyProvider<OrientationEntry> orientationID();

		ValueProvider<OrientationEntry, String> nameEN();
	}
	
	interface MainTypologicalClassProperties extends PropertyAccess<MainTypologicalClass> {
		ModelKeyProvider<MainTypologicalClass> mainTypologicalClassID();

		LabelProvider<MainTypologicalClass> name();
	}

	interface StructureOrganizationProperties extends PropertyAccess<StructureOrganization> {
		ModelKeyProvider<StructureOrganization> structureOrganizationID();

		LabelProvider<StructureOrganization> name();
	}
	
	interface OrnamentCaveTypeProperties extends PropertyAccess<OrnamentCaveType> {
		ModelKeyProvider<OrnamentCaveType> ornamentCaveTypeID();

		LabelProvider<OrnamentCaveType> name();
	}
	



	public Ornamentic getOrnamentic() {
		return ornamentic;
	}




	public void setOrnamentic(Ornamentic ornamentic) {
		this.ornamentic = ornamentic;
	}




	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		
		return createForm();
	}


	interface WallRelationProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<WallOrnamentCaveRelation> wallID();

		ValueProvider<WallOrnamentCaveRelation, String> name();
	}




	/**
	 * @return the wallsListStore
	 */
	public ListStore<WallOrnamentCaveRelation> getWallsListStore() {
		return wallsListStore;
	}




	/**
	 * @param wallsListStore the wallsListStore to set
	 */
	public void setWallsListStore(ListStore<WallOrnamentCaveRelation> wallsListStore) {
		this.wallsListStore = wallsListStore;
	}




	
}
