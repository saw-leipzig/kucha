package de.cses.client.KuchaMapProject;


import com.google.gwt.user.client.ui.IsWidget;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;

public class Home implements IsWidget {
	static KuchaMapPrototyp kuchaMapPrototyp;
	static VerticalPanel kuchaMapPanel = new VerticalPanel();
	private VBoxLayoutContainer widget;

	

	
	public Home() {
		kuchaMapPrototyp = new KuchaMapPrototyp();
		kuchaMapPrototyp.iniKuchaMapPrototyp();
		
		
		kuchaMapPrototyp.create();
		kuchaMapPrototyp.inititialize();
		kuchaMapPrototyp.Start();
		//TestCommit
	

	}

	
	public static KuchaMapPrototyp getKuchaMapPrototyp(){
		return kuchaMapPrototyp;
	}
	public static VerticalPanel getRootLayoutPanel(){
		return kuchaMapPanel;
	}
	
	
	@Override
	public Widget asWidget() {
		  if (widget == null) {
			    BoxLayoutData flex = new BoxLayoutData();
			    flex.setFlex(1);
			    widget = new VBoxLayoutContainer();
			    widget.add(kuchaMapPanel, flex);
			  }
		  return widget;
	}

}
