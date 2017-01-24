package de.cses.client.kuchaMapClient;



import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class Detailansicht  {
	private VerticalPanel verticalPanel;
	private AbsolutePanel fotoAbsolutePanel;
	Image hoehlenFotoIMG ;
	boolean startseiteVisibility = true;


	public Detailansicht(){
		
	}
	
	public void iniDetailansicht(){
		verticalPanel = new VerticalPanel();
		fotoAbsolutePanel = new AbsolutePanel();
		hoehlenFotoIMG = new Image("http://e03-elmundo.uecdn.es/assets/multimedia/imagenes/2015/11/13/14474300157302.jpg");
	}
	
	public void CreateDetailansicht(){
		
		
		ScrollPanel hoehlenFotoScrollPanel = new ScrollPanel();
		hoehlenFotoScrollPanel.add(fotoAbsolutePanel);
		hoehlenFotoScrollPanel.setHorizontalScrollPosition(1500);
		verticalPanel.add(hoehlenFotoScrollPanel);
		//Window.alert(hoehlenFotoScrollPanel.getMaximumHorizontalScrollPosition());
		hoehlenFotoScrollPanel.addStyleName("FotoView");
	}
	public void setVisibility(boolean visible){
		verticalPanel.setVisible(visible);
	}
	
	public boolean getVisibility(){
		return verticalPanel.isVisible();
	}
	public AbsolutePanel getFotoAbsolutePanel(){
		return fotoAbsolutePanel;
	}
	public Image getHoehlenFotoIMG(){
		return hoehlenFotoIMG;
	}

	public VerticalPanel getVerticalPanel() {
		return verticalPanel;
	}
	public boolean getStartseiteVisibility(){
		return startseiteVisibility;
	}
	public void setStartseiteVisibility(boolean startseiteVisibility){
		this.startseiteVisibility = startseiteVisibility;
	}

	public void setHoehlenFotoIMG(Image hoehlenFotoIMG) {
		this.hoehlenFotoIMG = hoehlenFotoIMG;
	}
	

	
	

}
