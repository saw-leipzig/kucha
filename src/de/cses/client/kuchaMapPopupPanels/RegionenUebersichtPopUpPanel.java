package de.cses.client.kuchaMapPopupPanels;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class RegionenUebersichtPopUpPanel extends PopupPanel {

	transient VerticalPanel regionenUebersichtVerticalPanel = new VerticalPanel();
	String regionName="";
	String imageURL = "";
	String description="";
	int regionID ;
	
	public RegionenUebersichtPopUpPanel() {

		
   }
	
	public void iniRegionenUebersichtsPanel(){
		setAutoHideEnabled(true);
		createRegionenUebersichtPanel();
		setWidget(regionenUebersichtVerticalPanel);
	}
	public void createRegionenUebersichtPanel(){
		
		HTML nameHTML = new HTML(regionName);
		Image image = new Image(imageURL);
		HTML descriptionHTML = new HTML(description);
		image.setPixelSize(200, 200);
		
		regionenUebersichtVerticalPanel.add(nameHTML);
		regionenUebersichtVerticalPanel.add(descriptionHTML);
		regionenUebersichtVerticalPanel.add(image);
	}

	public String getName() {
		return regionName;
	}

	public void setName(String regionName) {
		this.regionName = regionName;
	}

	public String getUrl() {
		return imageURL;
	}

	public void setUrl(String imageURL) {
		this.imageURL = imageURL;
	}

	public int getiD() {
		return regionID;
	}

	public void setiD(int regionID) {
		this.regionID = regionID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
 }
