package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable.Dir;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;


public class Caves implements IsWidget{
	 ContentPanel panel;
	 private Caves cave = this;
	 PopupPanel cellaPanel = new PopupPanel();
		PopupPanel antechamberPanel = new PopupPanel();
		PopupPanel nichesPanel = new PopupPanel();

		@Override
		public Widget asWidget() {
			if (panel == null) {
				createForm();
			}
			return panel;
		}
		
public Widget createForm(){

	VerticalLayoutContainer vlc = new VerticalLayoutContainer();
	VerticalLayoutData vLayoutData = new VerticalLayoutData(200, 300, new Margins(15, 10, 10,10));
	vlc.setLayoutData(vLayoutData);
	
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
  
  
  ButtonBar addButtons = new ButtonBar();
  vlc.add(addButtons);
  TextButton addNiches = new TextButton("Add Niche");
  ClickHandler addNicheClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Niches newniche = new Niches();
			nichesPanel = new PopupPanel();
			Draggable drag = new Draggable(nichesPanel);
		  newniche.setCaves(cave);
      nichesPanel.add(newniche.asWidget());
			nichesPanel.setGlassEnabled(true);
			nichesPanel.center();
			nichesPanel.show();
		}
  };
  addNiches.addHandler(addNicheClickHandler, ClickEvent.getType());
  
  
  TextButton addAntechamber = new TextButton("Add Antechamber");
  
  ClickHandler addAntechamberClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Antechamber newantechamber = new Antechamber();
			newantechamber.setCaves(cave);
			antechamberPanel = new PopupPanel();
			Draggable drag = new Draggable(antechamberPanel);
      antechamberPanel.add(newantechamber.asWidget());
			antechamberPanel.setGlassEnabled(true);
			antechamberPanel.center();
			antechamberPanel.show();
		}
  };
  addAntechamber.addHandler(addAntechamberClickHandler, ClickEvent.getType());
  
  
  
  
  TextButton addCella = new TextButton("Add Cella"); 
  
  
  ClickHandler addCellaClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Cella newcella = new Cella();
			newcella.setCaves(cave);
			cellaPanel = new PopupPanel();
			Draggable drag = new Draggable(cellaPanel);
			//ScrollPanel scroll = new ScrollPanel();
			//scroll.setAlwaysShowScrollBars(true);
			//scroll.setHorizontalScrollPosition(100);
		 //Resizable resize = new Resizable(cellaPanel,Dir.E, Dir.N, Dir.NE);
			//scroll.setSize("450px", "500px");
			//cellaPanel.setSize("300px", "200px");
			//scroll.add(newcella);
      cellaPanel.add(newcella);
			cellaPanel.setGlassEnabled(true);
			cellaPanel.center();
			cellaPanel.show();
		}
  };
  addCella.addHandler(addCellaClickHandler, ClickEvent.getType());
  
  TextButton cancel = new TextButton("cancel");
  TextButton save = new TextButton("save");
  ButtonBar buttonbar = new ButtonBar();
  vlc.add(buttonbar);
  buttonbar.add(cancel);
  buttonbar.add(save);
  
  panel = new ContentPanel();
  panel.setHeading("Create new cave");
  panel.add(vlc);
  vlc.setScrollMode(ScrollMode.AUTOY);
  addButtons.add(addCella);
  addButtons.add(addAntechamber);
  addButtons.add(addNiches);

	return panel;
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
