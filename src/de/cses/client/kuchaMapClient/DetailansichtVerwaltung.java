package de.cses.client.kuchaMapClient;


import java.util.ArrayList;


import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.cses.client.KuchaMapProject.Home;
import de.cses.client.KuchaMapProject.KuchaDatabase;
import de.cses.client.kuchaMapPopupPanels.HoehleErstellenPopupPanel;


public class DetailansichtVerwaltung{
 
	 private Region aktiveRegion;
	 private Detailansicht detailansicht;
	 private AbsolutePanel fotoAbsolutePanel;
	 private EditierenVerwaltung editierenVerwaltung;
	 private  ArrayList<HandlerRegistration> handlerArrayList;
	 private HoehlenUebersicht hoehlenUebersicht;
	 private ArrayList<Region> regionen = new ArrayList<Region>();
	 private KuchaDatabase kuchaDatabase;
	 boolean loaded;
	 Image image;
	 private Environment environment;
	 RegionenUebersicht regionenUebersicht;
	 ArrayList<Integer> allCaveIDs;
	 
	 public DetailansichtVerwaltung(){
		 
	 }
	 public void  iniDetailansichtVerwaltung(){
		 handlerArrayList = new ArrayList<HandlerRegistration>();
		 detailansicht = Home.getKuchaMapPrototyp().getDetailansicht();
		 editierenVerwaltung = Home.getKuchaMapPrototyp().getEditierenVerwaltung();
		 fotoAbsolutePanel = detailansicht.getFotoAbsolutePanel();
		 hoehlenUebersicht = Home.getKuchaMapPrototyp().getHoehlenUebersicht();
		 kuchaDatabase = Home.getKuchaMapPrototyp().getKuchaDatabase();
		 regionenUebersicht = Home.getKuchaMapPrototyp().getRegionenUebersicht();
		 environment = Home.getKuchaMapPrototyp().getEnvironment();
	 }

	 public ArrayList<HandlerRegistration> getHandlerArrayList(){
		 return handlerArrayList;
	 }
	 
	 public void loescheHoehle(int ID){
			
			Hoehle aktuell = aktiveRegion.getHoehlebyID(ID);
			aktiveRegion.getHoehlenArrayList().remove(aktuell);
			aktuell.getButton().setVisible(false);
			environment.getLoeschenListe().add(ID);
			
			
		}
	 
	 
		public void ShowHoehleErstellenPopupPanel(int x, int y){
	    HoehleErstellenPopupPanel popup = new HoehleErstellenPopupPanel();
	    popup.setLeft(x);
	    popup.setTop(y);
	    popup.iniHoehlenErstellenPopUpPanel();
	    popup.setPopupPosition(x, y);
	    popup.show();
		}
		
		public void ShowHoehleErstellenPanel(int x, int y){
			HoehleErstellenPanel HoehleErstellenPanel = new HoehleErstellenPanel();
			HoehleErstellenPanel.setLeft(x);
			HoehleErstellenPanel.setTop(y);
			HoehleErstellenPanel.iniHoehleErstellenPanel();
			HoehleErstellenPanel.show();
			
		}
		public void addHoehleLoeschenClickHandler(SimpleContainer button, int HoehlenID){
			CustomClickHandler costum = new CustomClickHandler();
			costum.setButton(button);
			costum.setHoehlenID(HoehlenID);
			
				
		}
		
		public synchronized void createHoehlenButton(int ArrayListPosition){
			
			SimpleContainer createdHoehlenButton = aktiveRegion.getHoehlenArrayList().get(ArrayListPosition).getButton();
			Hoehle Hoehle= aktiveRegion.getHoehlenArrayList().get(ArrayListPosition);
			aktiveRegion.getHoehlenArrayList().get(ArrayListPosition).getDrag().setContainer(fotoAbsolutePanel);
			if(aktiveRegion.getID()!=-1){
			hoehlenUebersicht = new HoehlenUebersicht();
		
			hoehlenUebersicht.setButton(createdHoehlenButton);
			hoehlenUebersicht.setHoehle(Hoehle);
			hoehlenUebersicht.setID(Hoehle.getID());
		
			hoehlenUebersicht.getHoehlenInfos();
		
			}
			else{
				regionenUebersicht = new RegionenUebersicht();
				regionenUebersicht.setB(createdHoehlenButton);
				regionenUebersicht.setRegion(Hoehle);
				regionenUebersicht.setRegionname(Hoehle.getname());
				regionenUebersicht.setID(Hoehle.getID());
				regionenUebersicht.getRegionenInfos();
				
			}
			
		
			
			image = new Image("http://kucha.informatik.hu-berlin.de/tomcat/images/BUTTONHOEHLEpngblau.png");
			createdHoehlenButton.add(image);
			createdHoehlenButton.setTitle(Hoehle.getname());
			image.setPixelSize(aktiveRegion.getHoehlenButtonsize(), aktiveRegion.getHoehlenButtonsize());
			fotoAbsolutePanel.add(createdHoehlenButton);
			fotoAbsolutePanel.setWidgetPosition(createdHoehlenButton,Hoehle.getButtonPositionLeft(), Hoehle.getButtonPositionTop());
		
			
		}
		
		public void NeueHoehleHinzufuegen(){
			createHoehlenButton(aktiveRegion.getHoehlenArrayList().size()-1);
			makeDetailansichtDraggable();
		}
		public Region getaktiveRegion(){
			return aktiveRegion;
		}
		public void setaktiveRegion(Region region){
			aktiveRegion = region;
		}
		public void loadRegion(){
			kuchaDatabase.loadRegionen(false);
			/*fotoAbsolutePanel.clear();
			Window.alert(aktiveRegion.getFoto());
			detailansicht.getHoehlenFotoIMG().setUrl(aktiveRegion.getFoto());
			fotoAbsolutePanel.add(detailansicht.getHoehlenFotoIMG());
			editierenVerwaltung.switchEDundAN();
			*/
		}
		
		public Detailansicht getDetailansicht(){
			return detailansicht;
		}
		public void makeDetailansichtDraggable(){
			for(int i = 0; i < aktiveRegion.getHoehlenArrayList().size(); i++){
				
				aktiveRegion.getHoehlenArrayList().get(i).getDrag().setEnabled(true);
				
				
			}
		}
		public  void makeDetailansichtNoDraggable(){
			
				
					for(int i = 0; i< aktiveRegion.getHoehlenArrayList().size(); i ++){
						aktiveRegion.getHoehlenArrayList().get(i).getDrag().setEnabled(false);
					}
					
				
	}
		public Region getRegionbyID( int ID){
			for(int i = 0; i < regionen.size(); i++){
				if (regionen.get(i).getID() == ID){
					return regionen.get(i);
				}
			}
			return null;
		}
		public ArrayList<Region> getRegionen() {
			return regionen;
		}
		public void setRegionen(ArrayList<Region> regionen) {
			this.regionen = regionen;
		}
		public boolean isLoaded() {
			return loaded;
		}
		public void setLoaded(boolean loaded) {
			this.loaded = loaded;
		}
		public AbsolutePanel getFotoAbsolutePanel() {
			return fotoAbsolutePanel;
		}
		public void setFotoAbsolutePanel(AbsolutePanel fotoAbsolutePanel) {
			this.fotoAbsolutePanel = fotoAbsolutePanel;
		}
		public EditierenVerwaltung getEditierenVerwaltung() {
			return editierenVerwaltung;
		}
		public void setEditierenVerwaltung(EditierenVerwaltung editierenVerwaltung) {
			this.editierenVerwaltung = editierenVerwaltung;
		}
		public Image getImage() {
			return image;
		}
		public void setImage(Image image) {
			this.image = image;
		}
		public ArrayList<Integer> getAllCaveIDs() {
			return allCaveIDs;
		}
		public void setAllCaveIDs(ArrayList<Integer> allCaveIDs) {
			this.allCaveIDs = allCaveIDs;
		}
		
		
		
		
}
