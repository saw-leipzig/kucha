package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;

import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.DepictionEntry;

/**
 * 
 * @author alingnau
 *
 */
public class Walls implements IsWidget{
	private DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private VBoxLayoutContainer widget;
	private int wallID;
	private AbsolutePanel background;
	private boolean editable = false;
	HorizontalPanel buttonbar = new HorizontalPanel();
	private PopupPanel panel;
	private Button save = new Button("save & exit");
	private Button uploadBackgorund = new Button("upload Background");

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
	
	public Walls(int wallID, boolean editable){
		this.editable = editable;
		this.wallID= wallID; 
		
	}
	
	public Widget createForm(){
		
		FramedPanel framePanel = new FramedPanel();
		framePanel.setHeading("Wall editor");
	
		VerticalPanel main = new VerticalPanel();
		background = new AbsolutePanel();
		main.add(background);
		background.setSize("800px", "400px");
		framePanel.add(main);
	

		
		Button cancel = new Button("cancel & close");
		buttonbar.add(cancel);
		buttonbar.add(save);
		buttonbar.add(uploadBackgorund);
		save.setVisible(false);
		uploadBackgorund.setVisible(false);
		
		cancel.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				panel.hide();
			}
		});
		
	 
		main.add(buttonbar);
		return framePanel;
		
	}
	
	public void createNewDepictionOnWall(DepictionEntry depiction, boolean editable, boolean firstTime){
		
				DepictionOnWallView depictionview = new DepictionOnWallView(depiction, editable );
			
				 background.add(depictionview);
				 if(firstTime){
					 background.setWidgetPosition(depictionview, 30, 30);
				 }
				 else{
				 background.setWidgetPosition(depictionview, depiction.getAbsoluteLeft(), depiction.getAbsoluteTop());
				 }
		
	}
	public void getAllDepictionIDsbyWall(){
		dbService.getDepictionsbyWallID(wallID, new AsyncCallback<ArrayList<DepictionEntry>>() {


			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DepictionEntry> result) {
				
				
				for (final DepictionEntry depiction : result) {
					if(depiction.getAbsoluteLeft() != -1 && depiction.getAbsoluteTop() != -1 ){
						
					createNewDepictionOnWall(depiction, editable, false);
					
					}
		
				}
			}
		});
	}
	
	public void add(DepictionEntry depiction){
	displayButtons(true);
	createNewDepictionOnWall(depiction, true, true);
	}
	public void show(){
		getAllDepictionIDsbyWall();
		
	}

	public PopupPanel getPanel() {
		return panel;
	}

	public void setPanel(PopupPanel panel) {
		this.panel = panel;
	}
	
	public void displayButtons(boolean display){
		if(display){	
			save.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					
					Iterator<Widget> iterator = background.iterator();
					
					while(iterator.hasNext() ){
						Widget w = iterator.next();
						if(w instanceof DepictionOnWallView){
					DepictionOnWallView depictionOnWallView =(DepictionOnWallView) w;
						
					int absoluteLeft = 	depictionOnWallView.getAbsoluteLeft();
					int absoluteTop = 	depictionOnWallView.getAbsoluteTop();
					dbService.saveDepiction(depictionOnWallView.getDepictionID(), absoluteLeft, absoluteTop, new AsyncCallback<String>() {


						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace();
						}

						@Override
						public void onSuccess(String result) {
						
						}
					});
						
					}
					}
					panel.hide();
					}
					
				//}
				});
				save.setVisible(true);
				uploadBackgorund.setVisible(true);
			
		}
		else{
				save.setVisible(false);
				uploadBackgorund.setVisible(false);
			
		}
					
	
					}
}
