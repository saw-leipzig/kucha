package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
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


public class Cella implements IsWidget{
	 ContentPanel panel;
	 CaveEditor caveEditor;
	
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
	  vlc.add(new FieldLabel(frontWall, "Front wall"));
	  
	  final DisclosurePanel disclosurepanelFrontWall = new DisclosurePanel();
	  vlc.add(disclosurepanelFrontWall);
	  
	  VerticalLayoutContainer vlcFrontWall = new VerticalLayoutContainer();
	  disclosurepanelFrontWall.setContent(vlcFrontWall);
	  
	  ClickHandler frontWallHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				boolean value = frontWall.getValue();
				disclosurepanelFrontWall.setOpen(!value);
			}
	  };
	  frontWall.addHandler(frontWallHandler, ClickEvent.getType());
	  
	  
	  final CheckBox frontWallLeftSide = new CheckBox();
	  vlcFrontWall.add(new FieldLabel(frontWallLeftSide, "Front wall left side"));
	  
	  final CheckBox frontWallRightSide = new CheckBox();
	  vlcFrontWall.add(new FieldLabel(frontWallRightSide, "Front wall right side"));
	  
	  final CheckBox frontWallLunette = new CheckBox();
	  vlcFrontWall.add(new FieldLabel(frontWallLunette, "Front lunette"));
	  
	  
	  final CheckBox leftSideWall = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWall, "Left side wall"));
	  
	  final DisclosurePanel disclosurepanelLeftSideWall = new DisclosurePanel();
	  vlc.add(disclosurepanelLeftSideWall);
	  
	  VerticalLayoutContainer vlcLeftSideWall = new VerticalLayoutContainer();
	  disclosurepanelLeftSideWall.setContent(vlcLeftSideWall);
	  
	  ClickHandler leftSideWallHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				boolean value = leftSideWall.getValue();
				disclosurepanelLeftSideWall .setOpen(!value);
			}
	  };
	  leftSideWall.addHandler(leftSideWallHandler, ClickEvent.getType());
	  
	  final CheckBox leftSideWallLowerBorder = new CheckBox();
	  vlcLeftSideWall.add(new FieldLabel(leftSideWallLowerBorder, "Left side wall lower border"));
	  
	  final CheckBox leftSideWallRegister = new CheckBox();
	  vlcLeftSideWall.add(new FieldLabel(leftSideWallRegister, "Left side wall register"));
	  
	  final CheckBox leftSideWallUpperBorder = new CheckBox();
	  vlcLeftSideWall.add(new FieldLabel(leftSideWallUpperBorder, "Left side wall upper Border"));
	  
	  final CheckBox rightSideWall = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWall, "Right side wall"));
	  
	  
	  final DisclosurePanel disclosurepanelRightSideWall = new DisclosurePanel();
	  vlc.add(disclosurepanelRightSideWall);
	  
	  VerticalLayoutContainer vlcRightSideWall = new VerticalLayoutContainer();
	  disclosurepanelRightSideWall.setContent(vlcRightSideWall);
	  
	  ClickHandler rightSideWallHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				boolean value = rightSideWall.getValue();
				disclosurepanelRightSideWall .setOpen(!value);
			}
	  };
	  rightSideWall.addHandler(rightSideWallHandler, ClickEvent.getType());
	  
	  
	  final CheckBox rightSideWallLowerBorder = new CheckBox();
	  vlcRightSideWall.add(new FieldLabel(rightSideWallLowerBorder, "Right side wall lower Border"));
	  
	  final CheckBox rightSideWallRegister = new CheckBox();
	  vlcRightSideWall.add(new FieldLabel(rightSideWallRegister, "Right side wall register"));
	  
	  final CheckBox rightSideWallUpperBorder = new CheckBox();
	  vlcRightSideWall.add(new FieldLabel(rightSideWallUpperBorder, "Right side wall upper border"));
	  
	  final CheckBox mainWall = new CheckBox();
	  vlc.add(new FieldLabel(mainWall, "Main wall"));
	  
	  final DisclosurePanel disclosurepanelMainWall = new DisclosurePanel();
	  vlc.add(disclosurepanelMainWall);
	  
	  VerticalLayoutContainer vlcMainWall = new VerticalLayoutContainer();
	  disclosurepanelMainWall.setContent(vlcMainWall);
	  
	  ClickHandler mainWallHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				boolean value = mainWall.getValue();
				disclosurepanelMainWall .setOpen(!value);
			}
	  };
	  mainWall.addHandler(mainWallHandler, ClickEvent.getType());
	  
	  final CheckBox niche = new CheckBox();
	  vlcMainWall.add(new FieldLabel(niche, "Niche"));
	   
	  final CheckBox floor = new CheckBox();
	  vlc.add(new FieldLabel(floor, "Floor"));
	  
	  final CheckBox lunette = new CheckBox();
	  vlcMainWall.add(new FieldLabel(lunette, "Lunette"));

	  
	  final CheckBox lunetteNiches = new CheckBox();
	  vlc.add(new FieldLabel(lunetteNiches, "Niches"));
	  
	  final CheckBox lunettePedestals = new CheckBox();
	  vlc.add(new FieldLabel(lunettePedestals, "Pedestals"));
	  
	  final CheckBox lunetteCeiling = new CheckBox();
	  vlc.add(new FieldLabel(lunetteCeiling, "Ceiling"));
	  
	  final DisclosurePanel disclosurepanelCeiling = new DisclosurePanel();
	  vlc.add(disclosurepanelCeiling);
	  
	  VerticalLayoutContainer vlcLunetteCeiling = new VerticalLayoutContainer();
	  
	  ClickHandler luentteCeilingHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				boolean value =  lunetteCeiling.getValue();
				disclosurepanelCeiling.setOpen(!value);
			}
	  };
	  lunetteCeiling.addHandler(luentteCeilingHandler, ClickEvent.getType());
	  
	  disclosurepanelCeiling.setContent(vlcLunetteCeiling);
	  
	  final CheckBox laternCeiling = new CheckBox();
	  vlcLunetteCeiling.add(new FieldLabel(laternCeiling, "Latern ceiling"));
	  
	  final CheckBox copulaDome = new CheckBox();
	  vlcLunetteCeiling.add(new FieldLabel(copulaDome, "Copula/Dome"));
	  
	  final CheckBox vaultedCeiling = new CheckBox();
	  vlcLunetteCeiling.add(new FieldLabel(vaultedCeiling, "vaulted ceiling"));
	  
	  final DisclosurePanel disclosurepanelvaultedCeiling = new DisclosurePanel();
	  
	  VerticalLayoutContainer vlcLunettevaultedCeiling = new VerticalLayoutContainer();
	  disclosurepanelvaultedCeiling.setContent(vlcLunettevaultedCeiling);
	  vlcLunetteCeiling.add(disclosurepanelvaultedCeiling);
	  
	  
	  ClickHandler luenttevaultedCeilingHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				boolean value = vaultedCeiling.getValue();
				disclosurepanelvaultedCeiling.setOpen(!value);
			
				
			}
	  };
	  vaultedCeiling.addHandler(luenttevaultedCeilingHandler, ClickEvent.getType());
	  
	  
	  final CheckBox lozengeShapedMountainPattern	 = new CheckBox();
	  vlcLunettevaultedCeiling.add(new FieldLabel(lozengeShapedMountainPattern, "Lozenge-shaped mountain pattern"));
	  

	  
	  final DisclosurePanel disclosurepanelvaultedlozengeCeiling = new DisclosurePanel();
	  vlcLunettevaultedCeiling.add(disclosurepanelvaultedlozengeCeiling);
	  
	  VerticalLayoutContainer vlcLunettevaultedlozengeCeiling = new VerticalLayoutContainer();
	  disclosurepanelvaultedlozengeCeiling.setContent(vlcLunettevaultedlozengeCeiling);
	  
	  ClickHandler luenttevaultedlozengeCeilingHandler = new ClickHandler(){
			@Override
			public void onClick(ClickEvent event) {
				
				boolean value = lozengeShapedMountainPattern.getValue();
				disclosurepanelvaultedlozengeCeiling.setOpen(!value);
			}
	  };
	  lozengeShapedMountainPattern.addHandler(luenttevaultedlozengeCeilingHandler, ClickEvent.getType());
	  
	  
	  final TextField numberOfLozenges	 = new TextField();
	  vlcLunettevaultedlozengeCeiling.add(new FieldLabel(numberOfLozenges, "Number of lozenges"));
	  
	  
	  final TextField numberOfJatakasAvadanas	 = new TextField();
	  vlcLunettevaultedlozengeCeiling.add(new FieldLabel(numberOfJatakasAvadanas, "Number of Jatakas Avadanas"));
	  
	  final CheckBox buddhaInCentre	 = new CheckBox();
	  vlcLunettevaultedlozengeCeiling.add(new FieldLabel(buddhaInCentre, "Buddha in centre"));
	  
	  
	  final CheckBox colouring	 = new CheckBox();
	  vlcLunettevaultedlozengeCeiling.add(new FieldLabel(colouring, "colouring"));
	  
	  final CheckBox zenith	 = new CheckBox();
	  vlcLunettevaultedCeiling.add(new FieldLabel(zenith, "Zenith"));
	  	  
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
//				caveEditor.getCellaPanel().hide();	
			}
	  	
	  };
	  cancel.addHandler(cancelClickHandler, ClickEvent.getType());
	  
	  ButtonBar buttonbar = new ButtonBar();
	  vlc.add(buttonbar);
	  buttonbar.add(cancel);
	  buttonbar.add(save);
	  
		panel = new ContentPanel();
		panel.add(vlc);
		vlc.setScrollMode(ScrollMode.AUTOY);
		panel.setHeading("Cella Editor");
    panel.setPixelSize(500, 500);
   // panel.setBounds(0, 0, 510, 500);
   // panel.setPosition(5, 5);
		return panel;
		
	}
	public CaveEditor getCaves() {
		return caveEditor;
	}
	public void setCaves(CaveEditor caveEditor) {
		this.caveEditor = caveEditor;
	}
	
	

}
