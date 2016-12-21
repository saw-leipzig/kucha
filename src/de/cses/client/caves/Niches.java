package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;

public class Niches implements IsWidget{
	 ContentPanel panel;
	 private CaveEditor caveEditor;

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
	  
	  final CheckBox nichesInCave = new CheckBox();
	  vlc.add(new FieldLabel(nichesInCave, "Niches in cave"));
	  
	  TextButton save = new TextButton("save");
	  TextButton cancel = new TextButton("cancel");
	  

	  ClickHandler saveClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
//				caveEditor.getCellaPanel().hide();	
			}
	  	
	  };
	  save.addHandler(saveClickHandler, ClickEvent.getType());
	  
	  
	  ClickHandler cancelClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
//				caveEditor.getNichesPanel().hide();	
			}
	  	
	  };
	  cancel.addHandler(cancelClickHandler, ClickEvent.getType());
	  
	  ButtonBar buttonbar = new ButtonBar();
	  vlc.add(buttonbar);
	  buttonbar.add(cancel);
	  buttonbar.add(save);
	  
	  panel = new ContentPanel();
	  panel.setHeading("Create new niche");
	  panel.add(vlc);
		vlc.setScrollMode(ScrollMode.AUTOY);
		return panel;
	}
	public CaveEditor getCaves() {
		return caveEditor;
	}
	public void setCaves(CaveEditor caveEditor) {
		this.caveEditor = caveEditor;
	}

	
}
