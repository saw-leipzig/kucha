package de.cses.client.kuchaMapPopupPanels;


import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.KuchaMapProject.Home;
import de.cses.client.kuchaMapClient.DetailansichtVerwaltung;

public class HoehleLoeschenPopupPanel extends PopupPanel{
	

	DetailansichtVerwaltung detailansichtVerwaltung;
	int iD;
	
	public HoehleLoeschenPopupPanel() {	  
}
	
	public void iniHoehleLoeschenPopupPanel(){
		setAutoHideEnabled(true);
		 detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		 HoehleLoeschenPopupOeffnen();
	}
	public void HoehleLoeschenPopupOeffnen(){
		  Command cmd = new Command() {
		        @Override
				public void execute() {
		          detailansichtVerwaltung.loescheHoehle(iD);
		          HoehleLoeschenPopupPanel.this.hide();
		        }
		      }; 
		    MenuBar hoehleLoeschenMenuBar = new MenuBar(true);
		    hoehleLoeschenMenuBar.addItem("loeschen", cmd);
		    setWidget(hoehleLoeschenMenuBar);
	}
	
	public int getID() {
		return iD;
	}
	public void setID(int iD) {
		this.iD = iD;
	}
	
	

}
