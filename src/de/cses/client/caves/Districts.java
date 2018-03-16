package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.shared.ImageEntry;

public class Districts implements IsWidget, ImageSelectorListener{
	ContentPanel panel;
	Districts districts =this;
	PopupPanel imagePanel = new PopupPanel();
	
	@Override
	public Widget asWidget() {
		if (panel == null) {
			createForm();
		}
		return panel;
	}
	public Widget createForm(){
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(400, 300, new Margins(15, 10, 10,10));
		
	  final TextField shortName = new TextField();
	  shortName.setAllowBlank(false);
	  vlc.add(new FieldLabel(shortName, "Name"));
	  
		
	  final TextField description = new TextField();
	  description.setAllowBlank(false);
	  vlc.add(new FieldLabel(description, "Description"));
		
	  TextButton map = new TextButton("Choose Map");
	  vlc.add(map);
	  
	  ClickHandler mapUpload = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				ImageSelector selector = new ImageSelector(districts);
				imagePanel = new PopupPanel();
				Draggable drag = new Draggable(imagePanel);
				imagePanel.add(selector);
				imagePanel.center();
				
				
			}
	  	
	  };
	  map.addHandler(mapUpload, ClickEvent.getType());
	  
		vlc.setLayoutData(vLayoutData);
		panel = new ContentPanel();
	  ButtonBar buttonbar = new ButtonBar();
	  vlc.add(buttonbar);
	  TextButton cancel = new TextButton("cancel");
	  TextButton save = new TextButton("save");
	  buttonbar.add(cancel);
	  buttonbar.add(save);
		panel = new ContentPanel();
		panel.add(vlc);
		vlc.setScrollMode(ScrollMode.AUTOY);
		panel.setHeading("Cella Editor");
    panel.setPixelSize(500, 500);
		return panel;
	}
	@Override
	public void imageSelected(ImageEntry entry) {
		imagePanel.hide();
		// imageID == 0  =>  Cancel Button pressed!
	}

}
