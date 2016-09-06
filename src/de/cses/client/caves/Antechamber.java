package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class Antechamber implements IsWidget {
	private VBoxLayoutContainer widget;
	Caves caves;
	
	@Override
	public Widget asWidget() {
		 if (widget == null) {
		    BoxLayoutData flex = new BoxLayoutData();
		    //flex.setFlex(1);
		    widget = new VBoxLayoutContainer();
		    widget.add(createForm(), flex);
		  }

		  return widget;
	}

	private Widget createForm(){
		VerticalPanel MainVerticalPanel = new VerticalPanel();

		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.LEFT);
		
		
	  final TextField height = new TextField();
	  height.setAllowBlank(false);
	  vlc.add(new FieldLabel(height, "Height"));
	  

	  final TextField width = new TextField();
	  width.setAllowBlank(false);
	  vlc.add(new FieldLabel(width, "Width"));
	  

	  final TextField depth = new TextField();
	  depth.setAllowBlank(false);
	  vlc.add(new FieldLabel(depth, "Depth"));
	  
	  final CheckBox frontWall = new CheckBox();
	  vlc.add(new FieldLabel(frontWall, "Front Wall"));
	  
	  final CheckBox leftSideWall = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWall, "Left-Side wall"));
	  
	  final CheckBox rightSideWall = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWall, "Right-Side wall"));
	  
	  final CheckBox mainWall = new CheckBox();
	  vlc.add(new FieldLabel(mainWall, "Main wall"));
	  
	  final CheckBox ceiling = new CheckBox();
	  vlc.add(new FieldLabel(ceiling, "Ceiling"));
	  
	  final CheckBox floor = new CheckBox();
	  vlc.add(new FieldLabel(floor, "Floor"));
		
		
	  
	  TextButton save = new TextButton("save");
	  TextButton cancel = new TextButton("cancel");
	  

	  ClickHandler saveClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				caves.getCellaPanel().hide();	
			}
	  	
	  };
	  save.addHandler(saveClickHandler, ClickEvent.getType());
	  
	  
	  ClickHandler cancelClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				caves.getAntechamberPanel().hide();	
			}
	  	
	  };
	  cancel.addHandler(cancelClickHandler, ClickEvent.getType());
	  
	  vlc.add(cancel);
	  vlc.add(save);
	  
	  FramedPanel framedPanelCaves = new FramedPanel();
	  framedPanelCaves.setHeading("Create new Antechamber");
	  framedPanelCaves.add(MainVerticalPanel);
	  MainVerticalPanel.add(vlc);
		return framedPanelCaves;
	}

	public Caves getCaves() {
		return caves;
	}

	public void setCaves(Caves caves) {
		this.caves = caves;
	}
	
}
