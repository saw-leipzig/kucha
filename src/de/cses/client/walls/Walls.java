package de.cses.client.walls;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;

public class Walls implements IsWidget{
	private VBoxLayoutContainer widget;

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
		
		return null;
		
	}

}
