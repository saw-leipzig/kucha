package de.cses.client.walls;



import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HtmlLayoutContainer;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

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

		
		FramedPanel framePanel = new FramedPanel();
		framePanel.setHeading("Wall editor");
	
		VerticalPanel main = new VerticalPanel();
		final AbsolutePanel background = new AbsolutePanel();
		main.add(background);
		background.setSize("800px", "400px");
		background.setStyleName("BackgroundStyle");
		background.addStyleDependentName("BackgroundStyle");
		framePanel.add(main);
	
		ButtonBar buttonbar = new ButtonBar();
		Button newOrnament = new Button("add new Ornament");
		Button newBorder = new Button("add new Border");
		Button newDoor = new Button("add new Door");
		buttonbar.add(newOrnament);
		buttonbar.add(newDoor);
		buttonbar.add(newBorder);
		
		ClickHandler borderHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
 Button border = new Button();
 border.addStyleName("ButtonStyle");
 border.removeStyleName("gwt-Button");
 background.add(border);
 border.setSize("100%", "40px");
 Draggable drag = new Draggable(border);
 
			}
		};
		
		ClickHandler ornamentHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				 Button ornament = new Button();
				 ornament.addStyleName("ButtonStyle");
				 ornament.removeStyleName("gwt-Button");
				 background.add(ornament);
				 ornament.setSize("40px", "40px");
				 Draggable drag = new Draggable(ornament);
			}
		};
		
		ClickHandler doorHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				 SimpleContainer door = new SimpleContainer();
				 background.add(door);
				 //door.addStyleName("ButtonStyle");
				 Image image = new Image("https://thumbs.dreamstime.com/x/weie-buddha-hhle-der-yungang-grotten-22895251.jpg");
				 door.add(image);
				 //door.removeStyleName("gwt-Button");
				 Draggable drag = new Draggable(door);
				// WidgetComponent component = new WidgetComponent(door);
				 Resizable resize = new Resizable(door, Resizable.Dir.NE,Resizable.Dir.NW, Resizable.Dir.SE, Resizable.Dir.SW);
				 resize.setPreserveRatio(true);
				 
				 
			}
		};
		
		newDoor.addClickHandler(doorHandler);
		newOrnament.addClickHandler(ornamentHandler);
		newBorder.addClickHandler(borderHandler);
		
		main.add(buttonbar);
		
		
		
		
		return framePanel;
		
	}
	
}
