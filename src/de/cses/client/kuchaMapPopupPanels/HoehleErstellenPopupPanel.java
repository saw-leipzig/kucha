package de.cses.client.kuchaMapPopupPanels;



import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PopupPanel;

import de.cses.client.KuchaMapProject.Home;
import de.cses.client.kuchaMapClient.DetailansichtVerwaltung;




public class HoehleErstellenPopupPanel extends PopupPanel{


	private int left;
	private int top;
	private DetailansichtVerwaltung detailansichtVerwaltung;
	
	
	public HoehleErstellenPopupPanel(){

}
	public void iniHoehlenErstellenPopUpPanel(){
		setAutoHideEnabled(true);
		detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		hoehleErstellenPopupOeffnen();
	}
	
	public void hoehleErstellenPopupOeffnen(){
		//Opens Popup with "Create new Cave" possibility
		
		  Command cmd = new Command() {
		        @Override
				public void execute() {
		        HoehleErstellenPopupPanel.this.hide();
		         detailansichtVerwaltung.ShowHoehleErstellenPopupPanel(left, top);
		         
		        }
		      };
		    MenuBar hoehleErstellenMenu = new MenuBar(true);
		    hoehleErstellenMenu.addItem("neue Hoehle erstellen", cmd);
		    setWidget(hoehleErstellenMenu);
		
	}
	public int getLeft() {
		return left;
	}
	public void setLeft(int left) {
		this.left = left;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	
}
