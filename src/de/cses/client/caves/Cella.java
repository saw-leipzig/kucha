package de.cses.client.caves;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;


public class Cella implements IsWidget{
	 private VerticalLayoutContainer widget;
	
	@Override
	public Widget asWidget() {
		 if (widget == null) {
		    VerticalLayoutData flex = new VerticalLayoutData ();
		    widget = new VerticalLayoutContainer();
		    widget.add(createForm(), flex);
		  }

		  return widget;
	}
	private Widget createForm(){
		VerticalPanel MainVerticalPanel = new VerticalPanel();

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		MainVerticalPanel.add(vlc);
		
	  final TextField height = new TextField();
	  height.setAllowBlank(false);
	  vlc.add(new FieldLabel(height, "Height"));
	  

	  /*final TextField width = new TextField();
	  width.setAllowBlank(false);
	  vlc.add(new FieldLabel(width, "Width"));
	  

	  final TextField depth = new TextField();
	  depth.setAllowBlank(false);
	  vlc.add(new FieldLabel(depth, "Depth"));
	  
	  final CheckBox frontWall = new CheckBox();
	  vlc.add(new FieldLabel(frontWall, "Front wall"));
	  
	  
	  final CheckBox frontWallLeftSide = new CheckBox();
	  vlc.add(new FieldLabel(frontWallLeftSide, "Front wall left side"));
	  
	  final CheckBox frontWallRightSide = new CheckBox();
	  vlc.add(new FieldLabel(frontWallRightSide, "Front wall right side"));
	  
	  final CheckBox frontWallLunette = new CheckBox();
	  vlc.add(new FieldLabel(frontWallLunette, "Front lunette"));
	  
	  
	  final CheckBox leftSideWall = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWall, "Left side wall"));
	  
	  final CheckBox leftSideWallLowerBorder = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWallLowerBorder, "Left side wall lower border"));
	  
	  final CheckBox leftSideWallRegister = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWallRegister, "Left side wall register"));
	  
	  final CheckBox leftSideWallUpperBorder = new CheckBox();
	  vlc.add(new FieldLabel(leftSideWallUpperBorder, "Left side wall upper Border"));
	  
	  final CheckBox rightSideWall = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWall, "Right side wall"));
	  
	  final CheckBox rightSideWallLowerBorder = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWallLowerBorder, "Right side wall lower Border"));
	  
	  final CheckBox rightSideWallRegister = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWallRegister, "Right side wall register"));
	  
	  final CheckBox rightSideWallUpperBorder = new CheckBox();
	  vlc.add(new FieldLabel(rightSideWallUpperBorder, "Right side wall upper border"));
	  
	  final CheckBox mainWall = new CheckBox();
	  vlc.add(new FieldLabel(mainWall, "main wall"));
	  
	  final CheckBox niche = new CheckBox();
	  vlc.add(new FieldLabel(niche, "Niche"));
	  
	*/  
	  final CheckBox floor = new CheckBox();
	  vlc.add(new FieldLabel(floor, "Floor"));
	  
	  final CheckBox lunette = new CheckBox();
	  vlc.add(new FieldLabel(lunette, "Lunette"));

	  
	  final CheckBox lunetteNiches = new CheckBox();
	  vlc.add(new FieldLabel(lunetteNiches, "Lunette niches"));
	  
	  final CheckBox lunettePedestals = new CheckBox();
	  vlc.add(new FieldLabel(lunettePedestals, "Lunette pedestals"));
	  
	  final CheckBox lunetteCeiling = new CheckBox();
	  vlc.add(new FieldLabel(lunetteCeiling, "Lunette ceiling"));
	  
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
	  	  
	  
	  FramedPanel framedPanelCaves = new FramedPanel();
	  framedPanelCaves.setSize("1000000px", "100000px");
	  framedPanelCaves.setHeading("Create new cella");
	  framedPanelCaves.add(MainVerticalPanel);
		return framedPanelCaves;
		
	}

}
