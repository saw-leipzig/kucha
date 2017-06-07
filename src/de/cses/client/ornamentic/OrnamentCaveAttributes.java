package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
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
import de.cses.shared.DistrictEntry;
import de.cses.shared.MainTypologicalClass;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentCaveType;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
import de.cses.shared.OrnamentOrientation;
import de.cses.shared.StructureOrganization;
import de.cses.shared.WallOrnamentCaveRelation;

public class OrnamentCaveAttributes extends PopupPanel{
	
	private FramedPanel header;
	private ComboBox<CaveEntry> caveEntryComboBox;
	private ListStore<CaveEntry> caveEntryList;
	private CaveEntryProperties caveEntryProps;
	private ListStore<OrnamentOfOtherCulturesEntry> ornamentOfOtherCulturesEntryList;
	private ListStore<OrnamentOfOtherCulturesEntry> similarOrnamentsofOtherCulturesListStore;
	private OrnamentOfOtherCulturesEntryProperties ornamentOfOtherCulturesEntryProps;
	private ListStore<DistrictEntry> districtEntryList;
	private DistrictEntryProperties districtEntryProps;
	private ListStore<OrnamentEntry> ornamentEntryList;
	private OrnamentEntryProperties ornamentEntryProps;
	private ComboBox<DistrictEntry> districtComboBox;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PopupPanel popup = this;
	private OrnamentCaveAttributes ornamentCaveAttributes =this;
	private ListStore<OrnamentEntry>selectedSimilarOrnaments;
	private ListStore<OrnamentEntry>selectedRedlatedOrnaments;
	private PictorialElementSelectorObjects selector;
	private Ornamentic ornamentic;
	// neue comboboxen
	private ListStore<OrnamentOrientation> orientation;
	private ComboBox<OrnamentOrientation> orientationComboBox;
	private ArrayList<WallOrnamentCaveRelation> walls = new ArrayList<WallOrnamentCaveRelation>();

	


	
	private OrientationProperties orientationProps;
	

	
	



	public OrnamentCaveAttributes() {
		//super(false);
		caveEntryProps = GWT.create(CaveEntryProperties.class);
		ornamentEntryProps = GWT.create(OrnamentEntryProperties.class);
		districtEntryProps = GWT.create(DistrictEntryProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveEntryProps.caveID());
		districtEntryList = new ListStore<DistrictEntry>(districtEntryProps.districtID());
		ornamentEntryList = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedSimilarOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		selectedRedlatedOrnaments = new ListStore<OrnamentEntry>(ornamentEntryProps.OrnamentID());
		ornamentOfOtherCulturesEntryProps = GWT.create(OrnamentOfOtherCulturesEntryProperties.class);
		ornamentOfOtherCulturesEntryList = new ListStore<OrnamentOfOtherCulturesEntry>(ornamentOfOtherCulturesEntryProps.ornamentOfOtherCulturesID());
		similarOrnamentsofOtherCulturesListStore = new ListStore<OrnamentOfOtherCulturesEntry>(ornamentOfOtherCulturesEntryProps.ornamentOfOtherCulturesID());
		
		orientationProps = GWT.create(OrientationProperties.class);
	
	

		
		orientation = new ListStore<OrnamentOrientation>(orientationProps.ornamentOrientationID());




		
		
		
		
		

		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				ornamentEntryList.clear();
				for (OrnamentEntry pe : result) {
					ornamentEntryList.add(pe);
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
			}
		});
		
		dbService.getOrnamentsOfOtherCultures(new AsyncCallback<ArrayList<OrnamentOfOtherCulturesEntry>>() {


			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentOfOtherCulturesEntry> result) {
				ornamentOfOtherCulturesEntryList.clear();
				for (OrnamentOfOtherCulturesEntry pe : result) {
					ornamentOfOtherCulturesEntryList.add(pe);
				}
			}
		});
		

		

		
		dbService.getOrientations(new AsyncCallback<ArrayList<OrnamentOrientation>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentOrientation> result) {
				orientation.clear();
				for (OrnamentOrientation pe : result) {
					orientation.add(pe);
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
		
		header = new FramedPanel();
		header.setHeading("Select District");
		header.add(districtComboBox);
		vlcCave.add(header);
		
		SelectionHandler<DistrictEntry> districtSelectionHandler = new SelectionHandler<DistrictEntry>(){

			@Override
			public void onSelection(SelectionEvent<DistrictEntry> event) {
				int p = event.getSelectedItem().getDistrictID();
				caveEntryList.clear();
				
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
					}
				});
				
			}
			
		};
		
		districtComboBox.addSelectionHandler(districtSelectionHandler);
		
		
		caveEntryComboBox = new ComboBox<CaveEntry>(caveEntryList, caveEntryProps.OfficialName(),
				new AbstractSafeHtmlRenderer<CaveEntry>() {

					@Override
					public SafeHtml render(CaveEntry item) {
						final CaveViewTemplates pvTemplates = GWT.create(CaveViewTemplates.class);
						return pvTemplates.caver(item.getOfficialName());
					}
				});
		
		
		header = new FramedPanel();
		header.setHeading("Select Cave");
		header.add(caveEntryComboBox);
		vlcCave.add(header);
		
		
	  final TextField caveType = new TextField();
	  caveType.setAllowBlank(false);
	  caveType.setEnabled(false);
	 
	  
		header = new FramedPanel();
		header.setHeading("Cave Type");
		header.add(caveType);
		vlcCave.add(header);
	  
	  
		SelectionHandler<CaveEntry> caveSelectionHandler = new SelectionHandler<CaveEntry>(){

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				String p = event.getSelectedItem().getOfficialName();
				caveType.setText(p);
			}
			
		};
		
		
		Button addWalls = new Button("Select Walls");
		header = new FramedPanel();
		header.setHeading("Select Walls");
		header.add(addWalls);
		vlcCave.add(header);
		
	  ClickHandler addWallsClickHandler = new ClickHandler(){
	  	

			@Override
			public void onClick(ClickEvent event) {
				OrnamentWallAttributes attributespopup  = new OrnamentWallAttributes();
				attributespopup.setOrnamentCaveRelation(ornamentCaveAttributes);
	      attributespopup.setCave(caveEntryComboBox.getValue());
	      attributespopup.setGlassEnabled(true);
				attributespopup.center();
				
			}
	  	
	  };
		addWalls.addClickHandler(addWallsClickHandler);
		
		caveEntryComboBox.addSelectionHandler(caveSelectionHandler);
		
		VBoxLayoutContainer vlcAttributes = new VBoxLayoutContainer();
		
	  final TextField style = new TextField();
	  style.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Style in which it is used");
		header.add(style);
		vlcAttributes.add(header);
	  

	  
		orientationComboBox = new ComboBox<OrnamentOrientation>(orientation, orientationProps.name(),
				new AbstractSafeHtmlRenderer<OrnamentOrientation>() {

					@Override
					public SafeHtml render(OrnamentOrientation item) {
						final OrientationViewTemplates pvTemplates = GWT.create(OrientationViewTemplates.class);
						return pvTemplates.orientation(item.getName());
					}
				});
		

	 
		header = new FramedPanel();
		header.setHeading("Orientation");
		header.add(orientationComboBox);
		vlcAttributes.add(header);
	  

	  
	  
	  final TextField colours = new TextField();
	  colours.setAllowBlank(true);
		header = new FramedPanel();
		header.setHeading("Colours");
		header.add(colours);
		vlcAttributes.add(header);
		
	  final TextField notes = new TextField();
	  colours.setAllowBlank(true);
		header = new FramedPanel();
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
	  ListView<OrnamentEntry, String> ornamentListViewSimilar = new ListView<OrnamentEntry, String>(ornamentEntryList, ornamentEntryProps.code());
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
		
	  
	  
	  HorizontalPanel similarElementsHorizontalPanel = new HorizontalPanel();
	  ListView<OrnamentOfOtherCulturesEntry, String> elementsListViewSimilar = new ListView<OrnamentOfOtherCulturesEntry, String>(ornamentOfOtherCulturesEntryList, ornamentOfOtherCulturesEntryProps.name());
	  elementsListViewSimilar.setPixelSize(150, 150);
	  ListView<OrnamentOfOtherCulturesEntry, String> selectedSimilarElementsListView = new ListView<OrnamentOfOtherCulturesEntry, String>(similarOrnamentsofOtherCulturesListStore, ornamentOfOtherCulturesEntryProps.name());
	  selectedSimilarElementsListView.setPixelSize(150, 150);
	  similarElementsHorizontalPanel.add(elementsListViewSimilar);
	  similarElementsHorizontalPanel.add(selectedSimilarElementsListView);
	  

    new ListViewDragSource<OrnamentOfOtherCulturesEntry>(elementsListViewSimilar).setGroup("similarElement");
    new ListViewDragSource<OrnamentOfOtherCulturesEntry>(selectedSimilarElementsListView).setGroup("similarElement");

    new ListViewDropTarget<OrnamentOfOtherCulturesEntry>(elementsListViewSimilar).setGroup("similarElement");
    new ListViewDropTarget<OrnamentOfOtherCulturesEntry>(selectedSimilarElementsListView).setGroup("similarElement");
    
    selector = new PictorialElementSelectorObjects();
		header = new FramedPanel();
		selector.asWidget().setHeight("300px");
		selector.asWidget().setWidth("320px");
		header.setHeading("Select similar objects");
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
			ornamentCaveRelation.setCaveID(caveEntryComboBox.getValue().getCaveID());
			ornamentCaveRelation.setColours(colours.getText());
			ornamentCaveRelation.setGroup(Integer.parseInt(groupOfOrnaments.getText()));
			ornamentCaveRelation.setOrientation(orientationComboBox.getValue().getOrnamentOrientationID());
			ornamentCaveRelation.setNotes(notes.getText());
			ornamentCaveRelation.setStyle(Integer.parseInt(style.getText()));
			List<OrnamentEntry> relatedOrnaments = selectedRedlatedOrnaments.getAll();
			for( OrnamentEntry ornament : relatedOrnaments){
				ornamentCaveRelation.getRelatedOrnamentsRelationID().add(ornament.getOrnamentID());
			}
			List<OrnamentEntry> similarOrnaments = selectedSimilarOrnaments.getAll();
			for( OrnamentEntry ornament : similarOrnaments){
				ornamentCaveRelation.getSimilarOrnamentsRelationID().add(ornament.getOrnamentID());
			}
			Window.alert("kurz vor pictorial hinzugefuegt anzahl ist: "+ selector.getSelectedPE().size());
			for(int i = 0; i <selector.getSelectedPE().size(); i++){
				ornamentCaveRelation.getPictorialElementIDs().add(selector.getSelectedPE().get(i).getPictorialElementID());
				Window.alert("pictorial hinzugefuegt");
			}
			ornamentCaveRelation.setWalls(walls);
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

		LabelProvider<CaveEntry> OfficialName();
	}
	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caver(String name);
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
		@XTemplate("<div>{name}</div>")
		SafeHtml orientation(String name);
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
	


	interface OrientationProperties extends PropertyAccess<OrnamentOrientation> {
		ModelKeyProvider<OrnamentOrientation> ornamentOrientationID();

		LabelProvider<OrnamentOrientation> name();
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




	public ArrayList<WallOrnamentCaveRelation> getWalls() {
		return walls;
	}




	public void setWalls(ArrayList<WallOrnamentCaveRelation> walls) {
		this.walls = walls;
	}

	




	
}
