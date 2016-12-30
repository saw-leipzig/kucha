package de.cses.client.KuchaMapProject;


import com.google.gwt.user.client.Window;
//import com.KuchaMapPrototyp6.client.popuppanels.HilfePanels;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.cses.client.kuchaMapClient.Detailansicht;
import de.cses.client.kuchaMapClient.DetailansichtVerwaltung;
import de.cses.client.kuchaMapClient.EditierenVerwaltung;
import de.cses.client.kuchaMapClient.Environment;
import de.cses.client.kuchaMapClient.EnvironmentVerwaltung;
import de.cses.client.kuchaMapClient.HoehlenUebersicht;
import de.cses.client.kuchaMapClient.RegionenUebersicht;



public class KuchaMapPrototyp {
	

	private Detailansicht detailansicht;
	 private DetailansichtVerwaltung detailansichtVerwaltung;
	 private EditierenVerwaltung editierenVerwaltung;
	 private Environment environment;
	 private EnvironmentVerwaltung environmentVerwaltung;
	 //private HilfePanels hilfePanels;
	 private HoehlenUebersicht hoehlenUebersicht;
	 private VerticalPanel mainPanel;
	 private HorizontalPanel inhalt;
	 private KuchaDatabase kuchaDatabase;
	 private RegionenUebersicht regionenUebersicht;
	
	public KuchaMapPrototyp(){
		
	}
	public void  iniKuchaMapPrototyp(){
	
	mainPanel = new VerticalPanel();
	inhalt = new HorizontalPanel();

	}
	public void create(){
		
		detailansicht = new Detailansicht();
		hoehlenUebersicht = new HoehlenUebersicht();
		detailansichtVerwaltung = new DetailansichtVerwaltung();
		regionenUebersicht = new RegionenUebersicht();
		environment = new Environment();
		environmentVerwaltung = new EnvironmentVerwaltung();
		editierenVerwaltung = new EditierenVerwaltung();
		//hilfePanels = new HilfePanels();
		kuchaDatabase = new KuchaDatabase();

		
		
	}
	public void inititialize(){
		detailansicht.iniDetailansicht();
		detailansichtVerwaltung.iniDetailansichtVerwaltung();
		environment.iniEnvironment();
		environmentVerwaltung.iniEnvironmentVerwaltung();
		editierenVerwaltung.iniEditierenVerwaltung();
		
		
	

	}

	public Detailansicht getDetailansicht() {
		return detailansicht;
	}
	public DetailansichtVerwaltung getDetailansichtVerwaltung() {
		return detailansichtVerwaltung;
	}
	public EditierenVerwaltung getEditierenVerwaltung() {
		return editierenVerwaltung;
	}
	public Environment getEnvironment() {
		return environment;
	}
	public EnvironmentVerwaltung getEnvironmentVerwaltung() {
		return environmentVerwaltung;
	}
	/*public HilfePanels getHilfePanels() {
		return hilfePanels;
	}
	*/

	public VerticalPanel getMainPanel() {
		return mainPanel;
	}

	public HorizontalPanel getInhalt() {
		return inhalt;
	}
	public void Start(){
		Window.alert("in der start methode");
		detailansicht.CreateDetailansicht();
		kuchaDatabase.loadRegionen(true);

		
		
	
	}
	
	public void createPanels(){
		
		environment.CreateEnvironment();
		environment.CreateHeaderTop();
		Home.getRootLayoutPanel().add(mainPanel);
		Window.alert("pannel geaddet");
		mainPanel.add(inhalt);
		mainPanel.setSize("100%", "100%");
		inhalt.addStyleName("inhalt");
		inhalt.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Home.getRootLayoutPanel().setSize("100%", "100%");
		inhalt.add(detailansicht.getVerticalPanel());
		environment.createslider();
		environment.createHeaderBottom();
		
		mainPanel.addStyleName("Main");
	}
	
	public KuchaDatabase getSaveOnDatabase() {
		return kuchaDatabase;
	}
	public void setSaveOnDatabase(KuchaDatabase kuchaDatabase) {
		this.kuchaDatabase = kuchaDatabase;
	}
	public HoehlenUebersicht getHoehlenUebersicht() {
		return hoehlenUebersicht;
	}
	public void setHoehlenUebersicht(HoehlenUebersicht hoehlenUebersicht) {
		this.hoehlenUebersicht = hoehlenUebersicht;
	}
	public KuchaDatabase getKuchaDatabase() {
		return kuchaDatabase;
	}
	public void setKuchaDatabase(KuchaDatabase kuchaDatabase) {
		this.kuchaDatabase = kuchaDatabase;
	}
	public RegionenUebersicht getRegionenUebersicht() {
		return regionenUebersicht;
	}
	public void setRegionenUebersicht(RegionenUebersicht regionenUebersicht) {
		this.regionenUebersicht = regionenUebersicht;
	}
	
	

}
