package de.cses.client.caves;


import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class CaveType implements IsWidget{
	ContentPanel panel;

		@Override
		public Widget asWidget() {
			if (panel == null) {
				createForm();
			}
			return panel;
		}
	
	private Widget createForm(){
		

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(400, 300, new Margins(15, 10, 10,10));
		vlc.setLayoutData(vLayoutData);
		
		
	  final TextField shortName = new TextField();
	  shortName.setAllowBlank(false);
	  vlc.add(new FieldLabel(shortName, "Short name"));
	  
		
	  final TextField description = new TextField();
	  description.setAllowBlank(false);
	  vlc.add(new FieldLabel(description, "Description"));
	  
	  TextButton cancel = new TextButton("cancel");
	  TextButton save = new TextButton("save");
	  ButtonBar buttonbar = new ButtonBar();
	  vlc.add(buttonbar);
	  buttonbar.add(cancel);
	  buttonbar.add(save);
	  
	  panel = new ContentPanel();
	  panel.setHeading("Create new cave type");
	  panel.add(vlc);
		vlc.setScrollMode(ScrollMode.AUTOY);
		return panel;
	}
}
