package de.cses.client.ornamentic;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DistrictProperties;
import de.cses.client.KuchaDatabaseService;
import de.cses.shared.District;

public class CreateOrnamentic implements IsWidget{
	 private VBoxLayoutContainer widget;
	 private ListStore<District> store;
	 VBoxLayoutContainer vlc;
	 ComboBox<District> combo;
	
	public CreateOrnamentic(){
		
	}

@Override
public Widget asWidget() {
  if (widget == null) {
    BoxLayoutData flex = new BoxLayoutData(new Margins(0, 0, 20, 0));
    flex.setFlex(1);
    widget = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
    widget.add(createForm(), flex);
  }

  return widget;
}

public Widget createForm(){
	
	vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
	
  TextField OrnamentID = new TextField();
  OrnamentID.setAllowBlank(false);
  OrnamentID.setEmptyText("Enter Ornaments ID...");
  vlc.add(new FieldLabel(OrnamentID, "Ornament ID"));
  
  KuchaDatabaseService KuchaDatabaseService = new KuchaDatabaseService();
  
  KuchaDatabaseService.getDistricts();
  DistrictProperties properties = GWT.create(DistrictProperties.class);
  store = new ListStore<District>(properties.key());
  combo = new ComboBox<District>(store, properties.NameLabel());
  vlc.add(new FieldLabel(combo, "Site"));
//  Window.alert("filled combobox");
 

  
  //Place for File-Upload
  
  TextField Discription = new TextField();
  Discription.setAllowBlank(true);
  vlc.add(new FieldLabel(Discription, "Discription"));
  
  TextField Remarks = new TextField();
  Remarks.setAllowBlank(true);
  vlc.add(new FieldLabel(Remarks, "Remarks"));
  
  TextField Annotations = new TextField();
  Annotations.setAllowBlank(true);
  vlc.add(new FieldLabel(Annotations, "Annotations"));
  
  //Place for Caves
  
  //Place for Occupied Position in Cave
  
  TextField RelationToOtherOrnamentsOrElementsText = new TextField();
  RelationToOtherOrnamentsOrElementsText.setAllowBlank(true);
  vlc.add(new FieldLabel(RelationToOtherOrnamentsOrElementsText, "Relation to other Ornaments or Elements"));
  //Here is a possibility to make a real relation to other Ornaments by ID
  
  TextField GroupOfOrnaments = new TextField();
  GroupOfOrnaments.setAllowBlank(true);
  vlc.add(new FieldLabel(GroupOfOrnaments, "Group of Ornaments"));
  
  TextField Interpretation = new TextField();
  Interpretation.setAllowBlank(true);
  vlc.add(new FieldLabel(Interpretation, "Interpretation"));
  
  TextField References = new TextField();
  References.setAllowBlank(true);
  vlc.add(new FieldLabel(References, "Group of Ornaments"));
  //Maybe change to a real relation to References
  
  FramedPanel panel = new FramedPanel();
  panel.setHeading("New Ornament");
  panel.add(vlc, new MarginData(15, 15, 0, 15));
  panel.addButton(new TextButton("Save"));
  panel.addButton(new TextButton("Cancel"));
  
  return panel;
	
}

public VBoxLayoutContainer getVlc() {
	return vlc;
}

public void setVlc(VBoxLayoutContainer vlc) {
	this.vlc = vlc;
}

public ListStore<District> getStore() {
	return store;
}

public void setStore(ListStore<District> store) {
	this.store = store;
}

public ComboBox<District> getCombo() {
	return combo;
}

public void setCombo(ComboBox<District> combo) {
	this.combo = combo;
}


}
