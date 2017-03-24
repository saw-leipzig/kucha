package de.cses.client.kuchaMapClient;


import java.util.ArrayList;



import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.sencha.gxt.widget.core.client.Slider;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuBar;
import com.sencha.gxt.widget.core.client.menu.MenuBarItem;
import com.sencha.gxt.widget.core.client.menu.MenuItem;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.KuchaMapProject.Home;
import de.cses.client.KuchaMapProject.KuchaDatabase;
import de.cses.client.images.ImageSelector;
import de.cses.client.images.ImageSelectorListener;
import de.cses.shared.HoehlenContainer;
import de.cses.shared.ImageEntry;

public class Environment implements ImageSelectorListener {
	private Slider hoehlenButtonSlider;
	private HorizontalPanel bottomHeaderHorizontalPanel = new HorizontalPanel();
	private HorizontalPanel topHeaderHorizontalPanel = new HorizontalPanel();
	private Button suchenButton;
	private Button resetSuche;
	private TextBox suchenBox;
	private Button fotoUpload;
	private EditierenVerwaltung editierenVerwaltung;
	private DetailansichtVerwaltung detailansichtVerwaltung;
	MenuBarItem edit;
	PopupPanel imageSelectorPopupPanel;
	Button exit = new Button("Exit");
	private KuchaDatabase kuchaDatabase;
	Button save = new Button("Save&Exit");
	private ArrayList<Integer>loeschenListe = new ArrayList<Integer>();
	Environment environment = this;
	Suche suche;


	
	public Environment(){
		
	}
	
	public void iniEnvironment(){
		suchenButton = new Button("Search");
		resetSuche = new Button("Reset");
		suche = Home.getKuchaMapPrototyp().getSuche();
		
		final ClickHandler sucheClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				suche.search();
				SucheSminulation.sucheSimulation(suchenBox.getText());
			
			}
			
		};
		suchenButton.addClickHandler(sucheClickHandler);
		suchenBox = new TextBox();
		fotoUpload = new Button("FotoUpload");
		fotoUpload.removeStyleName("gwt-Button");
		fotoUpload.addStyleName("environmentButton");
		hoehlenButtonSlider = new Slider(true);
		editierenVerwaltung = Home.getKuchaMapPrototyp().getEditierenVerwaltung();
		detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		kuchaDatabase = Home.getKuchaMapPrototyp().getSaveOnDatabase();
	
	}
	public void CreateEnvironment(){
		
		Menu editMenu = new Menu();
		MenuBar menue = new MenuBar();
		Menu goTo = new Menu();	
		//Menu helpMenu = new Menu();
		Menu regionen = new Menu();
		MenuItem helpItem = new MenuItem("Enable Tipps");
		helpItem.setId("enable");
		MenuItem helpItemDisable = new MenuItem("Disable Tipps");
		helpItemDisable.setId("disable");
		MenuItem regionenMenu = new MenuItem("Sites");
		editMenu.setToolTip("Enables to move caves");
	
		
		
		//helpMenu.add(helpItemDisable);
		//helpMenu.add(helpItem);
		regionenMenu.setSubMenu(regionen);
		
		 for(int i = 1 ; i< detailansichtVerwaltung.getRegionen().size(); i++){
			 MenuItem regionenItem = new MenuItem(detailansichtVerwaltung.getRegionen().get(i).getName());
			 regionenItem.setId(Integer.toString(detailansichtVerwaltung.getRegionen().get(i).getID()));
			 regionen.add(regionenItem);
		 }
		MenuItem startseiteitem = new MenuItem("Main");
		startseiteitem.setId("-1");
		
		goTo.add(startseiteitem);
		goTo.add(regionenMenu);
		MenuBarItem menugoto = new MenuBarItem("Go to..", goTo);
		edit = new MenuBarItem("Edit Site", editMenu);
		//MenuBarItem help = new MenuBarItem("Help", helpMenu);
		menue.add(menugoto);
		//menue.add(help);
		menue.add(edit);
		menue.getElement().getStyle().setMarginRight(25, Style.Unit.PCT);
		
		
	    SelectionHandler<Item> handler = new SelectionHandler<Item>() {
			@Override
			public void onSelection(SelectionEvent<Item> event) {
				 if (event.getSelectedItem() instanceof MenuItem) {
			            MenuItem item = (MenuItem) event.getSelectedItem();
			            int id= Integer.parseInt(item.getId());
			            detailansichtVerwaltung.setaktiveRegion(detailansichtVerwaltung.getRegionbyID(id));
			            editierenVerwaltung.seteditieren(false);
			            detailansichtVerwaltung.loadRegion();
			            
			            
			          }
			}
	      };
	      
	      SelectionHandler<Item> handleredit = new SelectionHandler<Item>() {
				@Override
				public void onSelection(SelectionEvent<Item> event) {
					 if (event.getSelectedItem() instanceof MenuItem) {
						 editierenVerwaltung.seteditieren(true);
						 editierenVerwaltung.switchEDundAN();
				          }
				}
		      };
		      
		     /* SelectionHandler<Item> handlerhelp = new SelectionHandler<Item>() {
					@Override
					public void onSelection(SelectionEvent<Item> event) {
						 if (event.getSelectedItem() instanceof MenuItem) {
							 if(event.getSelectedItem().getId().equals("disable")){
								 Window.alert("selected disable");
								
								 
							 }
							 if(event.getSelectedItem().getId().equals("enable")){
								 Window.alert("selected enable");
								
							 }
					          }
					}
			      };
			      */
		      
			  	BeforeShowHandler handlershow = new BeforeShowHandler() {

					@Override
					public void onBeforeShow(BeforeShowEvent event) {
						event.setCancelled(true);
						 editierenVerwaltung.seteditieren(true);
						 editierenVerwaltung.switchEDundAN();	
					}	
				};
		      
		  editMenu.addBeforeShowHandler(handlershow);    
		  //helpMenu.addHandler(handlerhelp,SelectionEvent.getType());
		  editMenu.addHandler(handleredit, SelectionEvent.getType());
	      goTo.addHandler(handler, SelectionEvent.getType());
	      regionen.addHandler(handler, SelectionEvent.getType());
	      
		
		Home.getKuchaMapPrototyp().getMainPanel().add(menue);
		menue.addStyleName("menueStyle");
		
		
		
	}
	public  void CreateHeaderTop(){
		Image lupe = new Image("http://www.fotos-hochladen.net/uploads/magnifyingglasgiucxa86lj.png");
		lupe.setSize("30px", "30px");
		lupe.addStyleName("lupenStyle");
		topHeaderHorizontalPanel.add(lupe);
		topHeaderHorizontalPanel.add(suchenBox);
		topHeaderHorizontalPanel.add(suchenButton);
		topHeaderHorizontalPanel.add(resetSuche);
		topHeaderHorizontalPanel.addStyleName("FlowPanel");
		topHeaderHorizontalPanel.add(fotoUpload);
		
		ClickHandler sucheResetHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				suche.setSearching(false);
				detailansichtVerwaltung.loadRegion();
				
			}
			
		};
		
		resetSuche.addClickHandler(sucheResetHandler);
		
		ClickHandler fotoUploadClickHandler = new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				imageSelectorPopupPanel = new PopupPanel();
				ImageSelector selector = new ImageSelector(ImageSelector.MAP, environment);
				imageSelectorPopupPanel.add(selector);
				imageSelectorPopupPanel.center();
				
			}
			
		};
		fotoUpload.addClickHandler(fotoUploadClickHandler);
		Home.getKuchaMapPrototyp().getMainPanel().add(topHeaderHorizontalPanel);
		
		
		
	}
	public void createHeaderBottom(){
		
		exit.removeStyleName("button");
		exit.removeStyleName("gwt-Button");
		exit.addStyleName("environmentButton");
		exit.setStyleName("environmentButton");
		bottomHeaderHorizontalPanel.add(exit);
		exit.addClickHandler(new ClickHandler() {
		    @Override
			public void onClick(ClickEvent event) {
		    	editierenVerwaltung.seteditieren(false);
		    	editierenVerwaltung.switchEDundAN();
		    	detailansichtVerwaltung.loadRegion();
		    	
		    }
		});
		
		save.setStyleName("environmentButton");
		bottomHeaderHorizontalPanel.add(save);
		save.addClickHandler(new ClickHandler() {
		    @Override
			public void onClick(ClickEvent event) {
		    	ArrayList<HoehlenContainer> hoehlenContainerList = new ArrayList<HoehlenContainer>();
		    	editierenVerwaltung.seteditieren(false);
		    	editierenVerwaltung.switchEDundAN();
		    	for(int i =0; i <detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().size();i++ ){
		    	
		    		if(detailansichtVerwaltung.getaktiveRegion().getID()==-1 && i ==0){
		    			i++;
		    		}
		    		Hoehle hoehle = detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i);
		    		HoehlenContainer hoehlenContainer = new HoehlenContainer();
		    		hoehlenContainer.setID(hoehle.getID());
		    		hoehlenContainer.setRegionID(hoehle.getRegionID());
		    		
		    		hoehlenContainer.setButtonPositionLeft(detailansichtVerwaltung.getDetailansicht().getFotoAbsolutePanel().getWidgetLeft(hoehle.getButton()));
		    		hoehlenContainer.setButtonPositionTop(detailansichtVerwaltung.getDetailansicht().getFotoAbsolutePanel().getWidgetTop(hoehle.getButton()));
		    		hoehlenContainerList.add(hoehlenContainer);
		    		
		    	}
		    	for(int i = 0; i< loeschenListe.size(); i++){
		    	kuchaDatabase.deleteHoehlebyID(loeschenListe.get(i));
		    	}
		    	
		    	kuchaDatabase.saveRegion(hoehlenContainerList, detailansichtVerwaltung.getaktiveRegion().getID(), detailansichtVerwaltung.getaktiveRegion().getHoehlenButtonsize(), detailansichtVerwaltung.getaktiveRegion().getImageID());
		    
		    }
		});
		
		Home.getKuchaMapPrototyp().getMainPanel().add(bottomHeaderHorizontalPanel);
		bottomHeaderHorizontalPanel.addStyleName("BottomStyle");
		
	}
	public void createslider(){
		
		Home.getKuchaMapPrototyp().getInhalt().add(hoehlenButtonSlider);
		
		hoehlenButtonSlider.setIncrement(1);
		hoehlenButtonSlider.setMinValue(0);
		hoehlenButtonSlider.setMaxValue(50);
		hoehlenButtonSlider.setVisible(false);
		
		hoehlenButtonSlider.addStyleName("HoehlenZoomSlider");
		
		ValueChangeHandler<Integer> handlerhoehlenzoom = new ValueChangeHandler<Integer>(){

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
			
				
				detailansichtVerwaltung.getaktiveRegion().setHoehlenButtonsize(event.getValue()); 
				for(int i = 0 ; i < detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().size(); i++){
					//detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i).getButton().setPixelSize(event.getValue(),event.getValue());
					detailansichtVerwaltung.getaktiveRegion().getHoehlenArrayList().get(i).getButton().getWidget().setPixelSize(event.getValue(),event.getValue());
				}
				
		
				
			}
			
		};
		hoehlenButtonSlider.addValueChangeHandler(handlerhoehlenzoom);
		
	}

	public Slider getHoehlenButtonSlider() {
		return hoehlenButtonSlider;
	}
	public HorizontalPanel getENBottomHeaderHorizontalPanel() {
		return bottomHeaderHorizontalPanel;
	}
	public HorizontalPanel getENTopHeaderHorizontalPanel() {
		return topHeaderHorizontalPanel;
	}
	public Button getSuchenButton() {
		return suchenButton;
	}
	public TextBox getSuchenBox() {
		return suchenBox;
	}
	public Button getFotoUpload() {
		return fotoUpload;
	}

	public ArrayList<Integer> getLoeschenListe() {
		return loeschenListe;
	}

	public void setLoeschenListe(ArrayList<Integer> loeschenListe) {
		this.loeschenListe = loeschenListe;
	}

	public MenuBarItem getEdit() {
		return edit;
	}

	public void setEdit(MenuBarItem edit) {
		this.edit = edit;
	}

	public Button getSave() {
		return save;
	}

	public void setSave(Button save) {
		this.save = save;
	}

	public Button getExit() {
		return exit;
	}

	public void setExit(Button exit) {
		this.exit = exit;
	}

	@Override
	public void imageSelected(int imageID) {
	final Region aktiveRegion = detailansichtVerwaltung.getaktiveRegion();
	imageSelectorPopupPanel.hide();
	aktiveRegion.setImageID(imageID);
	DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	dbService.getImage(imageID, new AsyncCallback<ImageEntry>() {

		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			
		}

		@Override
		public void onSuccess(ImageEntry result) {
			detailansichtVerwaltung.getDetailansicht().getHoehlenFotoIMG().setUrl("http://kucha.informatik.hu-berlin.de/tomcat/images/"+result.getFilename());
			
			
		}
	});

	}

	public Button getResetSuche() {
		return resetSuche;
	}

	public void setResetSuche(Button resetSuche) {
		this.resetSuche = resetSuche;
	}
	
	
	


}
