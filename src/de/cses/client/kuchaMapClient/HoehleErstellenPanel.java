package de.cses.client.kuchaMapClient;



import java.util.ArrayList;


import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
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
import de.cses.shared.CaveEntry;









public class HoehleErstellenPanel extends DialogBox{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private CaveProperties caveProps;
	private ListStore<CaveEntry> caveEntryList;
	private ComboBox<CaveEntry> caveSelection;

	TextBox iDbox = new TextBox();
	 int left;
	 int top;
	 DetailansichtVerwaltung detailansichtVerwaltung;
	
	   public HoehleErstellenPanel() {

		      
		    }
	   
	   public void iniHoehleErstellenPanel(){
		   this.center();
		   detailansichtVerwaltung = Home.getKuchaMapPrototyp().getDetailansichtVerwaltung();
				caveProps = GWT.create(CaveProperties.class);
				caveEntryList = new ListStore<CaveEntry>(caveProps.caveID());
			int p = detailansichtVerwaltung.getaktiveRegion().getID();
			caveEntryList.clear();
			
			dbService.getCavesbyDistrictID(p,new AsyncCallback<ArrayList<CaveEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				public void onSuccess(ArrayList<CaveEntry> result) {
					
					for (int i = 0;i< result.size(); i++) {
						caveEntryList.add(result.get(i));
					}
					
				}
			});
			createHoehleErstellenPopupPanel();
		   
	   }
	   public void createHoehleErstellenPopupPanel(){
		 
		   
		   
		   
		   VBoxLayoutContainer vlcHoehleErstellen = new VBoxLayoutContainer();
		   
		   caveSelection = new ComboBox<CaveEntry>(caveEntryList, caveProps.officialName(),
						new AbstractSafeHtmlRenderer<CaveEntry>() {

							@Override
							public SafeHtml render(CaveEntry item) {
								final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
								return cvTemplates.caveLabel(item.getCaveID(), item.getOfficialName());
							}
						});
		   
				caveSelection.setTypeAhead(false);
				caveSelection.setEditable(false);
			
			vlcHoehleErstellen.add(new FieldLabel(caveSelection, "Select Cave"));
		   
		
		      setText("Create cave");

		      setAnimationEnabled(true);
		  
		      setGlassEnabled(true);
		     
		      Button speichern = new Button("Save&Exit");
		      vlcHoehleErstellen.add(speichern);
		      speichern.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						HoehleErstellenPanel.this.hide();
						Hoehle neueHoehle = new Hoehle();
						neueHoehle.setButtonpositiontop(top);
						neueHoehle.setButtonpositionleft(left);
						neueHoehle.setID(caveSelection.getValue().getCaveID());
						neueHoehle.setname(caveSelection.getValue().getOfficialName());
						Region aktiv = detailansichtVerwaltung.getaktiveRegion();
						aktiv.getHoehlenArrayList().add(neueHoehle);
						neueHoehle.setRegionID(aktiv.getID());
						detailansichtVerwaltung.NeueHoehleHinzufuegen();
						
					}
			      });
		      
		      
		      Button abbrechen = new Button("Cancel");
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
	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();

		LabelProvider<CaveEntry> officialName();
	}
	
	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>Cave {number}<br>{name}</div>")
		SafeHtml caveLabel(int number, String name);
	}
	   
	}
