package de.cses.client.caves;

import com.google.gwt.user.client.ui.IsWidget;
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
  
  TextButton addAntechamber = new TextButton("Add Antechamber");
  vlc.add(addAntechamber);
  
  TextButton addCella = new TextButton("Add Cella");
  vlc.add(addCella);
  
  MainVerticalPanel.add(vlc);
  
  
  
  
 
  
	
	
  FramedPanel framedPanelCaves = new FramedPanel();
  framedPanelCaves.setHeading("Create new cave");
  framedPanelCaves.add(MainVerticalPanel);
	return framedPanelCaves;
}
}
