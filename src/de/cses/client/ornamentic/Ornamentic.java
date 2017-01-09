package de.cses.client.ornamentic;


import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.CaveEntry;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.OrnamentEntry;


public class Ornamentic implements IsWidget{
	 private VBoxLayoutContainer widget;
	 ContentPanel cavesContentPanel;
	 private OrnamentCaveRelationProperties ornamentCaveRelationProps;
	 private ListStore<OrnamentCaveRelation> caveOrnamentRelationList;
	 private Ornamentic ornamentic = this;
	 private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);


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

	ornamentCaveRelationProps = GWT.create(OrnamentCaveRelationProperties.class);
	caveOrnamentRelationList = new ListStore<OrnamentCaveRelation>(ornamentCaveRelationProps.caveID());
	VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
	
  final TextField ornamentCode = new TextField();
  ornamentCode.setAllowBlank(false);
  vlc.add(new FieldLabel(ornamentCode, "Ornament Code"));
  
  
  //Place for File-Upload Sketch + Photo
  
  final TextField discription = new TextField();
  discription.setAllowBlank(true);
  vlc.add(new FieldLabel(discription, "Discription"));
  
  final TextField mainTopologycalclass = new TextField();
  mainTopologycalclass.setAllowBlank(true);
  vlc.add(new FieldLabel(mainTopologycalclass, "Main typologycal class"));
  
  final TextField structureOrganization = new TextField();
  structureOrganization.setAllowBlank(true);
  vlc.add(new FieldLabel(structureOrganization, "Structure-Organization"));
  
  final TextField remarks = new TextField();
  remarks.setAllowBlank(true);
  vlc.add(new FieldLabel(remarks, "Remarks"));
  
  final TextField annotations = new TextField();
  annotations.setAllowBlank(true);
  vlc.add(new FieldLabel(annotations, "Annotations"));
  
  final TextField sketch = new TextField();
  sketch.setAllowBlank(true);
  vlc.add(new FieldLabel(sketch, "Sketch"));
  
  final TextField interpretation = new TextField();
  interpretation.setAllowBlank(true);
  vlc.add(new FieldLabel(interpretation, "Interpretation"));
  
  final TextField references = new TextField();
  references.setAllowBlank(true);
  vlc.add(new FieldLabel(references, "References"));
  //Maybe change to a real relation to References
  

  
  Button addCaveButton = new Button();
  addCaveButton.setText("Add related CaveEditor");
  vlc.add(new FieldLabel(addCaveButton, "Add cave relations"));
  addCaveButton.setSize("60px", "40px");
  
  
//Place for CaveEditor
  ClickHandler addCaveClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			OrnamentCaveAttributes attributespopup  = new OrnamentCaveAttributes();
      attributespopup.setOrnamentic(ornamentic);
			attributespopup.setGlassEnabled(true);
			attributespopup.center();
			Draggable b = new Draggable(attributespopup);
			
		}
  	
  };
  
  addCaveButton.addClickHandler(addCaveClickHandler);
  
  
  
  ColumnConfig<OrnamentCaveRelation, String> nameColOrnamentrelation = new ColumnConfig<OrnamentCaveRelation, String>(ornamentCaveRelationProps.name(), 200, "Ornament Name");
  List<ColumnConfig<OrnamentCaveRelation, ?>> columnsrelation = new ArrayList<ColumnConfig<OrnamentCaveRelation, ?>>();
  columnsrelation.add(nameColOrnamentrelation);
  ColumnModel<OrnamentCaveRelation> cmrelation = new ColumnModel<OrnamentCaveRelation>(columnsrelation);
  final Grid<OrnamentCaveRelation> ornamentGridrelation= new Grid<OrnamentCaveRelation>(caveOrnamentRelationList,cmrelation);
  FramedPanel ornamentsrelation = new FramedPanel();
  VerticalPanel ornamentsrelationVertical = new VerticalPanel();
  ornamentsrelation.add(ornamentsrelationVertical);
  ornamentsrelation.setHeading("Added Cave relations");
  ornamentsrelationVertical.add(ornamentGridrelation);


  
 


  
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
		ornament.setSketch(sketch.getText());
		ornament.setInterpretation(interpretation.getText());
		ornament.setReferences(references.getText());
		ornament.setMaintypologycalClass(Integer.parseInt(mainTopologycalclass.getText()));
		ornament.setStructureOrganization(structureOrganization.getText());
		
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
  
  TextButton edit = new TextButton("edit");
  TextButton delete = new TextButton("delete");
  HorizontalPanel buttonCaveEditPanel = new HorizontalPanel();
  buttonCaveEditPanel.add(edit);
  buttonCaveEditPanel.add(delete);
  
  
  ClickHandler deleteClickHandler = new ClickHandler(){

		@Override
		public void onClick(ClickEvent event) {
	caveOrnamentRelationList.remove(ornamentGridrelation.getSelectionModel().getSelectedItem());
		
		}
  };
  delete.addHandler(deleteClickHandler, ClickEvent.getType());
 
  
  TextButton cancel = new TextButton("cancel");
  
  HorizontalPanel buttonPanel= new HorizontalPanel();
  buttonPanel.add(save);
  buttonPanel.add(cancel);
  
  VerticalPanel panel = new VerticalPanel();
 panel.add(vlc);
 panel.add(ornamentsrelation);
 ornamentsrelationVertical.add(buttonCaveEditPanel);
panel.add(buttonPanel);

 
  FramedPanel framedpanelornamentic = new FramedPanel();
  framedpanelornamentic.setHeading("Create Ornamentic");
  framedpanelornamentic.add(panel);


  return framedpanelornamentic;
	
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





}

