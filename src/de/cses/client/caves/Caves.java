package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.CaveTypeEntry;



public class Caves implements IsWidget{
	 ContentPanel panel;
	 private Caves cave = this;
	 PopupPanel cellaPanel = new PopupPanel();
		PopupPanel antechamberPanel = new PopupPanel();
		PopupPanel nichesPanel = new PopupPanel();
		private ComboBox<CaveTypeEntry> caveTypeEntryComboBox;
		private CaveTypeEntryProperties caveTypeEntryProps  = GWT.create(CaveTypeEntryProperties.class);
		private ListStore<CaveTypeEntry> caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeEntryProps.caveTypeID()); 
		
		private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

		@Override
		public Widget asWidget() {
			if (panel == null) {
				createForm();
			}
			return panel;
		}
		
public Widget createForm(){

	VerticalLayoutContainer vlc = new VerticalLayoutContainer();
	VerticalLayoutData vLayoutData = new VerticalLayoutData(200, 300, new Margins(15, 10, 10,10));
	vlc.setLayoutData(vLayoutData);
	
  final TextField officialName = new TextField();
  officialName.setAllowBlank(false);
  vlc.add(new FieldLabel(officialName, "Official name"));
  
  final TextField officialNumber = new TextField();
  officialNumber.setAllowBlank(false);
  vlc.add(new FieldLabel(officialNumber, "Official number"));
  
  final TextField historicalName = new TextField();
  historicalName.setAllowBlank(false);
  vlc.add(new FieldLabel(historicalName, "Historical name"));
  
	dbService.getCaveTypes(new AsyncCallback<ArrayList<CaveTypeEntry>>() {


		@Override
		public void onFailure(Throwable caught) {
			caught.printStackTrace();
		}

		@Override
		public void onSuccess(ArrayList<CaveTypeEntry> result) {
			caveTypeEntryList.clear();
			
			for (CaveTypeEntry pe : result) {
				caveTypeEntryList.add(pe);
	
			}
		}
	});
	caveTypeEntryComboBox = new ComboBox<CaveTypeEntry>(caveTypeEntryList, caveTypeEntryProps.enShortname(),
			new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

				@Override
				public SafeHtml render(CaveTypeEntry item) {
					final CaveTypeEntryViewTemplates pvTemplates = GWT.create(CaveTypeEntryViewTemplates.class);
					return pvTemplates.district(item.getEnShortname());
				}
			});
	
	vlc.add(new FieldLabel(caveTypeEntryComboBox, "Select Cave Type"));
  
  final TextField state = new TextField();
  state.setAllowBlank(false);
  vlc.add(new FieldLabel(state, "State of Preservation"));
  
  final TextField orientation = new TextField();
  orientation.setAllowBlank(false);
  vlc.add(new FieldLabel(orientation, "Orientation"));
  
  final TextField pedestals = new TextField();
  pedestals.setAllowBlank(false);
  vlc.add(new FieldLabel(pedestals, "Pedestals"));
  
  final TextField findings = new TextField();
  findings.setAllowBlank(false);
  vlc.add(new FieldLabel(findings, "Findings"));
  
  
  ButtonBar addButtons = new ButtonBar();
  vlc.add(addButtons);
  TextButton addNiches = new TextButton("Add Niche");
  ClickHandler addNicheClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Niches newniche = new Niches();
			nichesPanel = new PopupPanel();
			Draggable drag = new Draggable(nichesPanel);
		  newniche.setCaves(cave);
      nichesPanel.add(newniche.asWidget());
			nichesPanel.setGlassEnabled(true);
			nichesPanel.center();
			nichesPanel.show();
		}
  };
  addNiches.addHandler(addNicheClickHandler, ClickEvent.getType());
  
  
  TextButton addAntechamber = new TextButton("Add Antechamber");
  
  ClickHandler addAntechamberClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Antechamber newantechamber = new Antechamber();
			newantechamber.setCaves(cave);
			antechamberPanel = new PopupPanel();
			Draggable drag = new Draggable(antechamberPanel);
      antechamberPanel.add(newantechamber.asWidget());
			antechamberPanel.setGlassEnabled(true);
			antechamberPanel.center();
			antechamberPanel.show();
		}
  };
  addAntechamber.addHandler(addAntechamberClickHandler, ClickEvent.getType());
  
  
  
  
  TextButton addCella = new TextButton("Add Cella"); 
  
  
  ClickHandler addCellaClickHandler = new ClickHandler(){
  	

		@Override
		public void onClick(ClickEvent event) {
			Cella newcella = new Cella();
			newcella.setCaves(cave);
			cellaPanel = new PopupPanel();
			Draggable drag = new Draggable(cellaPanel);
			//ScrollPanel scroll = new ScrollPanel();
			//scroll.setAlwaysShowScrollBars(true);
			//scroll.setHorizontalScrollPosition(100);
		 //Resizable resize = new Resizable(cellaPanel,Dir.E, Dir.N, Dir.NE);
			//scroll.setSize("450px", "500px");
			//cellaPanel.setSize("300px", "200px");
			//scroll.add(newcella);
      cellaPanel.add(newcella);
			cellaPanel.setGlassEnabled(true);
			cellaPanel.center();
			cellaPanel.show();
		}
  };
  addCella.addHandler(addCellaClickHandler, ClickEvent.getType());
  
  TextButton cancel = new TextButton("cancel");
  TextButton save = new TextButton("save");
  ButtonBar buttonbar = new ButtonBar();
  vlc.add(buttonbar);
  buttonbar.add(cancel);
  buttonbar.add(save);
  
  panel = new ContentPanel();
  panel.setHeading("Create new cave");
  panel.add(vlc);
  vlc.setScrollMode(ScrollMode.AUTOY);
  addButtons.add(addCella);
  addButtons.add(addAntechamber);
  addButtons.add(addNiches);

	return panel;
}
public PopupPanel getCellaPanel() {
	return cellaPanel;
}
public void setCellaPanel(PopupPanel cellaPanel) {
	this.cellaPanel = cellaPanel;
}
public PopupPanel getAntechamberPanel() {
	return antechamberPanel;
}
public void setAntechamberPanel(PopupPanel antechamberPanel) {
	this.antechamberPanel = antechamberPanel;
}
public PopupPanel getNichesPanel() {
	return nichesPanel;
}
public void setNichesPanel(PopupPanel nichesPanel) {
	this.nichesPanel = nichesPanel;
}
interface CaveTypeEntryProperties extends PropertyAccess<CaveTypeEntry> {
	ModelKeyProvider<CaveTypeEntry> caveTypeID();

	LabelProvider<CaveTypeEntry> enShortname();
}
interface CaveTypeEntryViewTemplates extends XTemplates {
	@XTemplate("<div>{enShortname}</div>")
	SafeHtml district(String enShortname);
}


}
