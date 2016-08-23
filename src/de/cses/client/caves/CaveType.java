package de.cses.client.caves;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class CaveType implements IsWidget{
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
	
	private Widget createForm(){
		
		VerticalPanel MainVerticalPanel = new VerticalPanel();

		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.LEFT);
		
		
	  final TextField shortName = new TextField();
	  shortName.setAllowBlank(false);
	  vlc.add(new FieldLabel(shortName, "Short name"));
	  
		
	  final TextField description = new TextField();
	  description.setAllowBlank(false);
	  vlc.add(new FieldLabel(description, "Description"));
		
	  FramedPanel framedPanelCaves = new FramedPanel();
	  framedPanelCaves.setHeading("Create new cave type");
	  framedPanelCaves.add(MainVerticalPanel);
	  MainVerticalPanel.add(vlc);
		return framedPanelCaves;
	}
}
