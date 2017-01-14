package de.cses.client.kuchaMapClient;



import java.util.ArrayList;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextBox;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.KuchaMapProject.Home;
import de.cses.shared.HoehlenContainer;









public class HoehleErstellenPanel extends DialogBox{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ComboBox<HoehlenContainer> HoehlenContainerComboBox;
	private ListStore<HoehlenContainer> HoehlenContainerList;
	private HoehlenContainerProperties HoehlenContainerProps;

	TextBox iDbox = new TextBox();
	 int left;
	 int top;
	 DetailansichtVerwaltung detailansichtVerwaltung;
	
	   public HoehleErstellenPanel() {

		      
		    }
	   
	   public void iniHoehleErstellenPanel(){
		   this.center();
		   detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
		   HoehlenContainerProps = GWT.create(HoehlenContainerProperties.class);
		   HoehlenContainerList = new ListStore<HoehlenContainer>(HoehlenContainerProps.getID());
			int p = detailansichtVerwaltung.getaktiveRegion().getID();
			HoehlenContainerList.clear();
			
			dbService.getCavesbyDistrictIDKucha(p,new AsyncCallback<ArrayList<HoehlenContainer>>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				@Override
				public void onSuccess(ArrayList<HoehlenContainer> result) {
					Window.alert("die groeﬂe des ergebnisses ist : " + Integer.toString(result.size()));
					for (int i = 0;i< result.size(); i++) {
						Window.alert("gefundene Hoehle mit ID" + Integer.toString(result.get(i).getID()) +"und regionsID: "+ Integer.toString( result.get(i).getRegionID()));
						HoehlenContainerList.add(result.get(i));
						Window.alert("added eine Hoehle zum Store");
					}
					
				}
			});
			createHoehleErstellenPopupPanel();
		   
	   }
	   public void createHoehleErstellenPopupPanel(){
		 
		   
		   
		   
		   VBoxLayoutContainer vlcHoehleErstellen = new VBoxLayoutContainer();
		   
			HoehlenContainerComboBox = new ComboBox<HoehlenContainer>(HoehlenContainerList, HoehlenContainerProps.name(),
					new AbstractSafeHtmlRenderer<HoehlenContainer>() {

						@Override
						public SafeHtml render(HoehlenContainer item) {
							final CaveViewTemplates pvTemplates = GWT.create(CaveViewTemplates.class);
							return pvTemplates.caver(item.getName());
						}
					});
			
			vlcHoehleErstellen.add(new FieldLabel(HoehlenContainerComboBox, "Select Cave"));
		   
		
		      setText("Hoehle erstellen");

		      setAnimationEnabled(true);
		  
		      setGlassEnabled(true);
		     
		      Button speichern = new Button("speichern");
		      vlcHoehleErstellen.add(speichern);
		      speichern.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						HoehleErstellenPanel.this.hide();
						Hoehle neueHoehle = new Hoehle();
						neueHoehle.setButtonpositiontop(top);
						neueHoehle.setButtonpositionleft(left);
						neueHoehle.setID(HoehlenContainerComboBox.getValue().getID());
						neueHoehle.setname(HoehlenContainerComboBox.getValue().getName());
						Region aktiv = detailansichtVerwaltung.getaktiveRegion();
						aktiv.getHoehlenArrayList().add(neueHoehle);
						neueHoehle.setRegionID(aktiv.getID());
						detailansichtVerwaltung.NeueHoehleHinzufuegen();
						
					}
			      });
		      
		      
		      Button abbrechen = new Button("Abbrechen");
		      vlcHoehleErstellen.add(abbrechen);
		      abbrechen.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						HoehleErstellenPanel.this.hide();
					}
			      });
		      
		      setWidget(vlcHoehleErstellen);
		   
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
	interface HoehlenContainerProperties extends PropertyAccess<HoehlenContainer> {
		ModelKeyProvider<HoehlenContainer> getID();

		LabelProvider<HoehlenContainer> name();
	}
	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caver(String name);
	}
	   
	}
