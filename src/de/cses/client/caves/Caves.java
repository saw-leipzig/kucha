package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;


public class Caves implements IsWidget{
	 private VBoxLayoutContainer widget;
	 private Caves cave = this;
	 PopupPanel cellaPanel = new PopupPanel();
		PopupPanel antechamberPanel = new PopupPanel();
		PopupPanel nichesPanel = new PopupPanel();

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
public Widget createForm(){
	VerticalPanel MainVerticalPanel = new VerticalPanel();

	VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.LEFT);
	
  final TextField officialName = new TextField();
  officialName.setAllowBlank(false);
  vlc.add(new FieldLabel(officialName, "Official name"));
  
  final TextField officialNumber = new TextField();
  officialNumber.setAllowBlank(false);
  vlc.add(new FieldLabel(officialNumber, "Official number"));
  
  final TextField historicalName = new TextField();
  historicalName.setAllowBlank(false);
  vlc.add(new FieldLabel(historicalName, "Historical name"));
  
 //TODO caveType
  
  final TextField state = new TextField();
  state.setAllowBlank(false);
  vlc.add(new FieldLabel(state, "State of Preservation"));
  
  final TextField orientation = new TextField();
  orientation.setAllowBlank(false);
  vlc.add(new FieldLabel(orientation, "Orientation"));
  
  final TextField pedestals = new TextField();
  pedestals.setAllowBlank(false);
  vlc.add(new FieldLabel(pedestals, "Pedestals"));
  
  final TextField findings = new TextField();
  findings.setAllowBlank(false);
  vlc.add(new FieldLabel(findings, "Findings"));
  
  
  
  TextButton addNiches = new TextButton("Add Niche");
  vlc.add(addNiches);
  ClickHandler addNicheClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Niches newniche = new Niches();
		  newniche.setCaves(cave);
      nichesPanel.add(newniche.asWidget());
			nichesPanel.setGlassEnabled(true);
			nichesPanel.center();
			nichesPanel.show();
		}
  };
  addNiches.addHandler(addNicheClickHandler, ClickEvent.getType());
  
  
  TextButton addAntechamber = new TextButton("Add Antechamber");
  vlc.add(addAntechamber);
  
  ClickHandler addAntechamberClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Antechamber newantechamber = new Antechamber();
			newantechamber.setCaves(cave);
      antechamberPanel.add(newantechamber.asWidget());
			antechamberPanel.setGlassEnabled(true);
			antechamberPanel.center();
			antechamberPanel.show();
		}
  };
  addAntechamber.addHandler(addAntechamberClickHandler, ClickEvent.getType());
  
  
  
  
  TextButton addCella = new TextButton("Add Cella");
  vlc.add(addCella);
  
  ClickHandler addCellaClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Cella newcella = new Cella();
			newcella.setCaves(cave);
      cellaPanel.add(newcella.asWidget());
			cellaPanel.setGlassEnabled(true);
			cellaPanel.center();
			cellaPanel.show();
		}
  };
  addCella.addHandler(addCellaClickHandler, ClickEvent.getType());
  
  
  MainVerticalPanel.add(vlc);
  
  
  
  
 
  
	
	
  FramedPanel framedPanelCaves = new FramedPanel();
  framedPanelCaves.setHeading("Create new cave");
  framedPanelCaves.add(MainVerticalPanel);
	return framedPanelCaves;
}
public PopupPanel getCellaPanel() {
	return cellaPanel;
}
public void setCellaPanel(PopupPanel cellaPanel) {
	this.cellaPanel = cellaPanel;
}
public PopupPanel getAntechamberPanel() {
	return antechamberPanel;
}
public void setAntechamberPanel(PopupPanel antechamberPanel) {
	this.antechamberPanel = antechamberPanel;
}
public PopupPanel getNichesPanel() {
	return nichesPanel;
}
public void setNichesPanel(PopupPanel nichesPanel) {
	this.nichesPanel = nichesPanel;
}


}
