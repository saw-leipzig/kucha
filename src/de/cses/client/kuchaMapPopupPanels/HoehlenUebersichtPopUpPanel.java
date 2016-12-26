package de.cses.client.kuchaMapPopupPanels;
import java.util.ArrayList;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class HoehlenUebersichtPopUpPanel extends PopupPanel{

	transient VerticalPanel hoehlenUebersichtVerticalPanel;
	String officialName="";
	ArrayList<String> hoehlenFotoURLs = new ArrayList<String>();
	int hoehlenID;
	String historicName;
	String caveTypeDescription;
	Image baseImg;
	
	
	public HoehlenUebersichtPopUpPanel( ) {
		
    }
	public void iniHoehlenUebersichtPanel(){
		setAutoHideEnabled(true);
		hoehlenUebersichtVerticalPanel = new VerticalPanel();
	    createHoehlenUebersichtPanel();
		setWidget(hoehlenUebersichtVerticalPanel);
		
	}
	
	public void createHoehlenUebersichtPanel(){
		HTML nameHTML = new HTML(officialName);
		HTML historicNameHTML = new HTML(historicName);
		HTML caveTypeHTML = new HTML(caveTypeDescription);
		
		
		
		hoehlenUebersichtVerticalPanel.add(nameHTML);
		hoehlenUebersichtVerticalPanel.add(historicNameHTML);
		hoehlenUebersichtVerticalPanel.add(caveTypeHTML);
		
		if( hoehlenFotoURLs.size()>0){
		baseImg = new Image(hoehlenFotoURLs.get(0));
		Timer timer = new Timer() {

			int index;
			
			@Override
			public void run() {
				index = (index+1<hoehlenFotoURLs.size()) ? index+1 : 0;
				baseImg.setUrl(hoehlenFotoURLs.get(index) );
			}
		};
		timer.scheduleRepeating(2000);
		}
		else{
			baseImg = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/300px-No_image_available.svg.png");
			baseImg.setSize("100px", "100px");
		}
		hoehlenUebersichtVerticalPanel.add(baseImg);
		
 
	}

	public void setName(String officialName) {
		this.officialName = officialName;
	}
	public int getHoehlenID() {
		return hoehlenID;
	}
	public String getOfficialName() {
		return officialName;
	}
	public void setOfficialName(String officialName) {
		this.officialName = officialName;
	}
	public String getHistoricName() {
		return historicName;
	}
	public void setHistoricName(String historicName) {
		this.historicName = historicName;
	}
	public String getCaveTypeDescription() {
		return caveTypeDescription;
	}
	public void setCaveTypeDescription(String caveTypeDescription) {
		this.caveTypeDescription = caveTypeDescription;
	}
	public ArrayList<String> getHoehlenFotoURLs() {
		return hoehlenFotoURLs;
	}
	public void setHoehlenFotoURLs(ArrayList<String> hoehlenFotoURLs) {
		this.hoehlenFotoURLs = hoehlenFotoURLs;
	}
	public void setHoehlenID(int hoehlenID) {
		this.hoehlenID = hoehlenID;
	}
	

  }