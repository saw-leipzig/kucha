/*
 * Copyright 2017, 2018
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.client.ornamentic;

import java.util.ArrayList;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ornamentic.OrnamentCaveRelationEditor.OrnamentViewTemplates;
import de.cses.client.ornamentic.WallOrnamentCaveRelationEditor.OrnamentFunctionViewTemplates;
import de.cses.client.ornamentic.WallOrnamentCaveRelationEditor.OrnamentPositionViewTemplates;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.OrnamenticSearchEntry;


/**
 * @author nina
 *
 */
public class OrnamenticFilter  extends AbstractFilter{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TextField ornamentCodeSearchTF;
	private TextField ornamentDeskriptionSearchTF;
	private TextField ornamentRemarksSearchTF;
	private TextField ornamentInterpretationSearchTF;
	private TextField ornamentReferencesSearchTF;
	private TextField ornamentOrnamentalGroupSearchTF;
	private TextField ornamentSimilaritiesSearchTF;
	
	private ListStore<OrnamentComponentsEntry> ornamentComponentsEntryList;
	private OrnamentComponentsProperties ornamentComponentsProps;
	private ListView<OrnamentComponentsEntry, OrnamentComponentsEntry> ornamentComponentsSelectionLV;
	
	private ListStore<CaveEntry> cavesEntryList;
	private CavesProperties cavesProps;
	private ListView<CaveEntry, CaveEntry> cavesSelectionLV;
	
	private ListStore<DistrictEntry> districtsEntryList;
	private DistrictsProperties districtsProps;
	private ListView<DistrictEntry, DistrictEntry> districtsSelectionLV;
	
	private ListStore<InnerSecondaryPatternsEntry> innerSecondaryPatternsEntryList;
	private InnerSecondaryPatternsProperties innerSecondaryPatternsProps;
	private ListView<InnerSecondaryPatternsEntry, InnerSecondaryPatternsEntry> innerSecondaryPatternsSelectionLV;
	
	private ListStore<OrnamentEntry> relatedOrnamentsEntryList;
	private RelatedOrnamentsProperties relatedOrnamentsProps;
	private ListView<OrnamentEntry, OrnamentEntry> relatedOrnamentsSelectionLV;
	
	/*private ListStore<InnerSecondaryPatternsEntry> iconographyEntryList;
	private InnerSecondaryPatternsProperties iconographyProps;
	private ListView<InnerSecondaryPatternsEntry, OrnamentComponentsEntry> iconographySelectionLV;
	*/
	
	private ListStore<OrnamentPositionEntry> positionEntryList;
	private PositionProperties positionProps;
	private ListView<OrnamentPositionEntry, OrnamentPositionEntry> positionSelectionLV;
	
	private ListStore<OrnamentFunctionEntry> functionEntryList;
	private FunctionProperties functionProps;
	private ListView<OrnamentFunctionEntry, OrnamentFunctionEntry> functionSelectionLV;
	
	interface OrnamentComponentsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<OrnamentComponentsEntry> ornamentComponentsID();
		LabelProvider<OrnamentComponentsEntry> uniqueID();
		ValueProvider<OrnamentComponentsEntry, String> name();
	}
	
	interface InnerSecondaryPatternsProperties extends PropertyAccess<OrnamentComponentsEntry> {
		ModelKeyProvider<InnerSecondaryPatternsEntry> innerSecondaryPatternsID();
		LabelProvider<InnerSecondaryPatternsEntry> uniqueID();
		ValueProvider<InnerSecondaryPatternsEntry, String> name();
	}
	interface CavesProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> uniqueID();
		ValueProvider<CaveEntry, String> name();
	}
	interface DistrictsProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> uniqueID();
		ValueProvider<DistrictEntry, String> name();
	}
	interface RelatedOrnamentsProperties extends PropertyAccess<OrnamentEntry> {
		ModelKeyProvider<OrnamentEntry> ornamentID();
		LabelProvider<OrnamentEntry> uniqueID();
		ValueProvider<OrnamentEntry, String> code();
	}
	interface PositionProperties extends PropertyAccess<OrnamentPositionEntry> {
		ModelKeyProvider<OrnamentPositionEntry> ornamentPositionID();
		LabelProvider<OrnamentPositionEntry> uniqueID();
		ValueProvider<OrnamentPositionEntry, String> name();
	}
	interface FunctionProperties extends PropertyAccess<OrnamentFunctionEntry> {
		ModelKeyProvider<OrnamentFunctionEntry> ornamentFunctionID();
		LabelProvider<OrnamentFunctionEntry> uniqueID();
		ValueProvider<OrnamentFunctionEntry, String> name();
	}
	interface IconographyProperties extends PropertyAccess<IconographyEntry> {
		ModelKeyProvider<IconographyEntry> iconographyID();
		LabelProvider<IconographyEntry> uniqueID();
		ValueProvider<IconographyEntry, String> name();
	}

	interface OrnamentComponentsViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ornamentComponentsLabel(String name);
	}
	interface InnerSecondaryPatternsViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml innerSecondaryPatternsLabel(String name);
	}
	interface CavesViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml cavesLabel(String name);
	}
	interface DistrictsViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml districtsLabel(String name);
	}
	interface RelatedOrnamentsViewTemplates extends XTemplates {
		@XTemplate("<div>{code}</div>")
		SafeHtml relatedOrnamentsLabel(String code);
	}
	interface IconographyViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml iconographyLabel(String name);
	}
	interface PositionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml positionLabel(String name);
	}
	interface FunctionViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml functionLabel(String name);
	}
	
	public OrnamenticFilter(String filterName) {
		super(filterName);
		
		ornamentComponentsProps = GWT.create(OrnamentComponentsProperties.class);
		ornamentComponentsEntryList = new ListStore<OrnamentComponentsEntry>(ornamentComponentsProps.ornamentComponentsID());
		loadOrnamentComponentsEntryList();
		
		innerSecondaryPatternsProps = GWT.create(InnerSecondaryPatternsProperties.class);
		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(innerSecondaryPatternsProps.innerSecondaryPatternsID());
		loadInnerSecondaryPatternsEntryList();
		
		cavesProps = GWT.create(CavesProperties.class);
		cavesEntryList = new ListStore<CaveEntry>(cavesProps.caveID());
		loadCaveEntryList();
		
		districtsProps = GWT.create(DistrictsProperties.class);
		districtsEntryList = new ListStore<DistrictEntry>(districtsProps.districtID());
		loadDistrictEntryList();
		
		relatedOrnamentsProps = GWT.create(RelatedOrnamentsProperties.class);
		relatedOrnamentsEntryList = new ListStore<OrnamentEntry>(relatedOrnamentsProps.ornamentID());
		loadOrnamentEntryList();
		 
		
		functionProps = GWT.create(FunctionProperties.class);
		functionEntryList = new ListStore<OrnamentFunctionEntry>(functionProps.ornamentFunctionID());
		loadFunctionEntryList();
		
		positionProps = GWT.create(PositionProperties.class);
		positionEntryList = new ListStore<OrnamentPositionEntry>(positionProps.ornamentPositionID());
		loadPositionEntryList();
	}
	
	

	private void loadOrnamentComponentsEntryList() {
		dbService.getOrnamentComponents(new AsyncCallback<ArrayList<OrnamentComponentsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentComponentsEntry> result) {
				ornamentComponentsEntryList.clear();
				for (OrnamentComponentsEntry pe : result) {
					ornamentComponentsEntryList.add(pe);
				}
			}
		});
	}
	
	private void loadCaveEntryList() {
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				cavesEntryList.clear();
				for (CaveEntry pe : result) {
					cavesEntryList.add(pe);
				}
			}
		});
	}
	
	private void loadDistrictEntryList() {
		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				districtsEntryList.clear();
				for (DistrictEntry pe : result) {
					districtsEntryList.add(pe);
				}
			}
		});
	}
	
	private void loadOrnamentEntryList() {
		dbService.getOrnaments(new AsyncCallback<ArrayList<OrnamentEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentEntry> result) {
				relatedOrnamentsEntryList.clear();
				for (OrnamentEntry pe : result) {
					relatedOrnamentsEntryList.add(pe);
				}
			}
		});
	}
	
	private void loadFunctionEntryList() {
		dbService.getOrnamentFunctions(new AsyncCallback<ArrayList<OrnamentFunctionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentFunctionEntry> result) {
				functionEntryList.clear();
				for (OrnamentFunctionEntry pe : result) {
					functionEntryList.add(pe);
				}
			}
		});
	}
	
	private void loadPositionEntryList() {
		dbService.getOrnamentPositions(new AsyncCallback<ArrayList<OrnamentPositionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentPositionEntry> result) {
				positionEntryList.clear();
				for (OrnamentPositionEntry pe : result) {
					positionEntryList.add(pe);
				}
			}
		});
	}
	
	
	private void loadInnerSecondaryPatternsEntryList() {
		dbService.getInnerSecondaryPatterns(new AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<InnerSecondaryPatternsEntry> result) {
				innerSecondaryPatternsEntryList.clear();
				for (InnerSecondaryPatternsEntry pe : result) {
					innerSecondaryPatternsEntryList.add(pe);
				}
			}
		});
	}
	

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractFilter#getFilterUI()
	 */
	@Override
	protected Widget getFilterUI() {

		ornamentCodeSearchTF = new TextField();
		ornamentCodeSearchTF.setEmptyText("search ornament code");
		ornamentCodeSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentInterpretationSearchTF = new TextField();
		ornamentInterpretationSearchTF.setEmptyText("search ornament interpretation");
		ornamentInterpretationSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentDeskriptionSearchTF = new TextField();
		ornamentDeskriptionSearchTF.setEmptyText("search ornament deskription");
		ornamentDeskriptionSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentOrnamentalGroupSearchTF = new TextField();
		ornamentOrnamentalGroupSearchTF.setEmptyText("search ornamental group");
		ornamentOrnamentalGroupSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentReferencesSearchTF = new TextField();
		ornamentReferencesSearchTF.setEmptyText("search ornament references");
		ornamentReferencesSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentRemarksSearchTF = new TextField();
		ornamentRemarksSearchTF.setEmptyText("search ornament remarks");
		ornamentRemarksSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));
		
		ornamentSimilaritiesSearchTF = new TextField();
		ornamentSimilaritiesSearchTF.setEmptyText("search similar ornaments");
		ornamentSimilaritiesSearchTF.setToolTip(Util.createToolTip("search ornament code", "Search for this character sequence in the ornament code field."));

		//Components
		ornamentComponentsSelectionLV = new ListView<OrnamentComponentsEntry, OrnamentComponentsEntry>(ornamentComponentsEntryList, new IdentityValueProvider<OrnamentComponentsEntry>(), 
				new SimpleSafeHtmlCell<OrnamentComponentsEntry>(new AbstractSafeHtmlRenderer<OrnamentComponentsEntry>() {
			final OrnamentComponentsViewTemplates ocvTemplates = GWT.create(OrnamentComponentsViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentComponentsEntry entry) {
				return ocvTemplates.ornamentComponentsLabel(entry.getName());
			}
			
		}));
		ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentComponentsPanel = new ContentPanel();
		ornamentComponentsPanel.setHeaderVisible(true);
		ornamentComponentsPanel.setToolTip(Util.createToolTip("Search for ornament components.", "Select one or more elements to search for Ornamentations."));
		ornamentComponentsPanel.setHeading("Ornament Components");
		ornamentComponentsPanel.add(ornamentComponentsSelectionLV);
		
		ToolButton resetOrnamentComponentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentComponentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentComponentsPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				ornamentComponentsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentComponentsPanel.addTool(resetOrnamentComponentsPanelTB);

		// caves
		cavesSelectionLV = new ListView<CaveEntry, CaveEntry>(cavesEntryList, new IdentityValueProvider<CaveEntry>(), 
				new SimpleSafeHtmlCell<CaveEntry>(new AbstractSafeHtmlRenderer<CaveEntry>() {
			final CavesViewTemplates ocvTemplates = GWT.create(CavesViewTemplates.class);

			@Override
			public SafeHtml render(CaveEntry entry) {
				return ocvTemplates.cavesLabel(Integer.toString(entry.getCaveID()));
			}
			
		}));
		ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		
		ContentPanel ornamentCavesPanel = new ContentPanel();
		ornamentCavesPanel.setHeaderVisible(true);
		ornamentCavesPanel.setToolTip(Util.createToolTip("Search for ornaments in specific caves.", "Select one or more elements to search for Ornamentations."));
		ornamentCavesPanel.setHeading("Ornament Caves");
		ornamentCavesPanel.add(cavesSelectionLV);
		
		ToolButton resetOrnamentCavesPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentCavesPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentCavesPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				cavesSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentCavesPanel.addTool(resetOrnamentCavesPanelTB);
		
		//Districts
		
		districtsSelectionLV = new ListView<DistrictEntry, DistrictEntry>(districtsEntryList, new IdentityValueProvider<DistrictEntry>(), 
				new SimpleSafeHtmlCell<DistrictEntry>(new AbstractSafeHtmlRenderer<DistrictEntry>() {
			final DistrictsViewTemplates ocvTemplates = GWT.create(DistrictsViewTemplates.class);

			@Override
			public SafeHtml render(DistrictEntry entry) {
				return ocvTemplates.districtsLabel(entry.getName());
			}
			
		}));
		cavesSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentdistrictsPanel = new ContentPanel();
		ornamentdistrictsPanel.setHeaderVisible(true);
		ornamentdistrictsPanel.setToolTip(Util.createToolTip("Search for districts.", "Select one or more elements to search for Ornamentations."));
		ornamentdistrictsPanel.setHeading("Districts");
		ornamentdistrictsPanel.add(districtsSelectionLV);
		
		ToolButton resetDistrictsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetDistrictsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetDistrictsPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				districtsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentdistrictsPanel.addTool(resetDistrictsPanelTB);
		
		//seconpary patterns
		
		innerSecondaryPatternsSelectionLV = new ListView<InnerSecondaryPatternsEntry, InnerSecondaryPatternsEntry>(innerSecondaryPatternsEntryList, new IdentityValueProvider<InnerSecondaryPatternsEntry>(), 
				new SimpleSafeHtmlCell<InnerSecondaryPatternsEntry>(new AbstractSafeHtmlRenderer<InnerSecondaryPatternsEntry>() {
			final InnerSecondaryPatternsViewTemplates ocvTemplates = GWT.create(InnerSecondaryPatternsViewTemplates.class);

			@Override
			public SafeHtml render(InnerSecondaryPatternsEntry entry) {
				return ocvTemplates.innerSecondaryPatternsLabel(entry.getName());
			}
			
		}));
		innerSecondaryPatternsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel innerSecPanel = new ContentPanel();
		innerSecPanel.setHeaderVisible(true);
		innerSecPanel.setToolTip(Util.createToolTip("Search for inner secondary patterns.", "Select one or more elements to search for Ornamentations."));
		innerSecPanel.setHeading("InnerSecondary Pattern");
		innerSecPanel.add(innerSecondaryPatternsSelectionLV);
		
		ToolButton resetInnerSecPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetInnerSecPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetInnerSecPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				innerSecondaryPatternsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		innerSecPanel.addTool(resetInnerSecPanelTB);

		
		//related ornaments
		relatedOrnamentsSelectionLV = new ListView<OrnamentEntry, OrnamentEntry>(relatedOrnamentsEntryList, new IdentityValueProvider<OrnamentEntry>(), 
				new SimpleSafeHtmlCell<OrnamentEntry>(new AbstractSafeHtmlRenderer<OrnamentEntry>() {
			final OrnamentViewTemplates ocvTemplates = GWT.create(OrnamentViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentEntry entry) {
				return ocvTemplates.ornament(entry.getCode());
			}
			
		}));
		relatedOrnamentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel relatedornamentPanel = new ContentPanel();
		relatedornamentPanel.setHeaderVisible(true);
		relatedornamentPanel.setToolTip(Util.createToolTip("Search for related ornaments.", "Select one or more elements to search for Ornamentations."));
		relatedornamentPanel.setHeading("Related Ornaments");
		relatedornamentPanel.add(relatedOrnamentsSelectionLV);
		
		ToolButton resetrelatedOrnamentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetrelatedOrnamentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetrelatedOrnamentsPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				relatedOrnamentsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		relatedornamentPanel.addTool(resetrelatedOrnamentsPanelTB);
		
		
		//position
		
		positionSelectionLV = new ListView<OrnamentPositionEntry, OrnamentPositionEntry>(positionEntryList, new IdentityValueProvider<OrnamentPositionEntry>(), 
				new SimpleSafeHtmlCell<OrnamentPositionEntry>(new AbstractSafeHtmlRenderer<OrnamentPositionEntry>() {
			final OrnamentPositionViewTemplates ocvTemplates = GWT.create(OrnamentPositionViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentPositionEntry entry) {
				return ocvTemplates.ornamentPosition(entry.getName());
			}
			
		}));
		positionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentpositionPanel = new ContentPanel();
		ornamentpositionPanel.setHeaderVisible(true);
		ornamentpositionPanel.setToolTip(Util.createToolTip("Search for ornament positions.", "Select one or more elements to search for Ornamentations."));
		ornamentpositionPanel.setHeading("Ornament Position");
		ornamentpositionPanel.add(positionSelectionLV);
		
		ToolButton resetOrnamentPositionPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentPositionPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentPositionPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				positionSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentpositionPanel.addTool(resetOrnamentPositionPanelTB);

		
		//function
		
		functionSelectionLV = new ListView<OrnamentFunctionEntry, OrnamentFunctionEntry>(functionEntryList, new IdentityValueProvider<OrnamentFunctionEntry>(), 
			new SimpleSafeHtmlCell<OrnamentFunctionEntry>(new AbstractSafeHtmlRenderer<OrnamentFunctionEntry>() {
			final OrnamentFunctionViewTemplates ocvTemplates = GWT.create(OrnamentFunctionViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentFunctionEntry entry) {
				return ocvTemplates.ornamentFunction(entry.getName());
			}
			
		}));
		functionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentFunctionPanel = new ContentPanel();
		ornamentFunctionPanel.setHeaderVisible(true);
		ornamentFunctionPanel.setToolTip(Util.createToolTip("Search for ornament functions.", "Select one or more elements to search for Ornamentations."));
		ornamentFunctionPanel.setHeading("Ornament Function");
		ornamentFunctionPanel.add(functionSelectionLV);
		
		ToolButton resetOrnamentFunctionPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentFunctionPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentFunctionPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				functionSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentFunctionPanel.addTool(resetOrnamentFunctionPanelTB);
		
		//iconographys?
		/*
		ornamentComponentsSelectionLV = new ListView<OrnamentComponentsEntry, OrnamentComponentsEntry>(ornamentComponentsEntryList, new IdentityValueProvider<OrnamentComponentsEntry>(), 
				new SimpleSafeHtmlCell<OrnamentComponentsEntry>(new AbstractSafeHtmlRenderer<OrnamentComponentsEntry>() {
			final OrnamentComponentsViewTemplates ocvTemplates = GWT.create(OrnamentComponentsViewTemplates.class);

			@Override
			public SafeHtml render(OrnamentComponentsEntry entry) {
				return ocvTemplates.ornamentComponentsLabel(entry.getName());
			}
			
		}));
		ornamentComponentsSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
		
		ContentPanel ornamentComponentsPanel = new ContentPanel();
		ornamentComponentsPanel.setHeaderVisible(true);
		ornamentComponentsPanel.setToolTip(Util.createToolTip("Search for ornament components.", "Select one or more elements to search for Ornamentations."));
		ornamentComponentsPanel.setHeading("Ornament Components");
		ornamentComponentsPanel.add(ornamentComponentsSelectionLV);
		
		ToolButton resetOrnamentComponentsPanelTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetOrnamentComponentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentComponentsPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				ornamentComponentsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentComponentsPanel.addTool(resetOrnamentComponentsPanelTB);

*/
		VerticalLayoutContainer ornamenticFilterVLC = new VerticalLayoutContainer();
		
		ContentPanel ornamentCodePanel = new ContentPanel();
		ornamentCodePanel.setHeaderVisible(true);
		ornamentCodePanel.setToolTip(Util.createToolTip("Search for ornament codes."));
		ornamentCodePanel.setHeading("Ornament Code");
		ornamentCodePanel.add(ornamentCodeSearchTF);
		ornamenticFilterVLC.add(ornamentCodePanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel ornamentDeskriptionPanel = new ContentPanel();
		ornamentDeskriptionPanel.setHeaderVisible(true);
		ornamentDeskriptionPanel.setToolTip(Util.createToolTip("Search for ornament deskription."));
		ornamentDeskriptionPanel.setHeading("Ornament Deskription");
		ornamentDeskriptionPanel.add(ornamentDeskriptionSearchTF);
		ornamenticFilterVLC.add(ornamentDeskriptionPanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel ornamentInterpretationPanel = new ContentPanel();
		ornamentInterpretationPanel.setHeaderVisible(true);
		ornamentInterpretationPanel.setToolTip(Util.createToolTip("Search for ornament interpretation."));
		ornamentInterpretationPanel.setHeading("Ornament Interpretation");
		ornamentInterpretationPanel.add(ornamentInterpretationSearchTF);
		ornamenticFilterVLC.add(ornamentInterpretationPanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel ornamentGroupPanel = new ContentPanel();
		ornamentGroupPanel.setHeaderVisible(true);
		ornamentGroupPanel.setToolTip(Util.createToolTip("Search for ornament groups."));
		ornamentGroupPanel.setHeading("Ornament Group");
		ornamentGroupPanel.add(ornamentOrnamentalGroupSearchTF);
		ornamenticFilterVLC.add(ornamentGroupPanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel referencesPanel = new ContentPanel();
		referencesPanel.setHeaderVisible(true);
		referencesPanel.setToolTip(Util.createToolTip("Search for ornament references."));
		referencesPanel.setHeading("Ornament References");
		referencesPanel.add(ornamentReferencesSearchTF);
		ornamenticFilterVLC.add(referencesPanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel remarksPanel = new ContentPanel();
		remarksPanel.setHeaderVisible(true);
		remarksPanel.setToolTip(Util.createToolTip("Search for ornament remarks."));
		remarksPanel.setHeading("Ornament Refmarks");
		remarksPanel.add(ornamentRemarksSearchTF);
		ornamenticFilterVLC.add(remarksPanel, new VerticalLayoutData(1.0, .02));
		
		ContentPanel similatitiesPanel = new ContentPanel();
		similatitiesPanel.setHeaderVisible(true);
		similatitiesPanel.setToolTip(Util.createToolTip("Search for similar ornaments or elements of other cultures."));
		similatitiesPanel.setHeading("Similarities");
		similatitiesPanel.add(ornamentSimilaritiesSearchTF);
		ornamenticFilterVLC.add(similatitiesPanel, new VerticalLayoutData(1.0, .02));
		
		ornamenticFilterVLC.add(ornamentCavesPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(ornamentFunctionPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(ornamentpositionPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(ornamentdistrictsPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(innerSecPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(relatedornamentPanel, new VerticalLayoutData(1.0, .1));
		ornamenticFilterVLC.add(ornamentComponentsPanel, new VerticalLayoutData(1.0, .1));
		//iconography? ornamenticFilterVLC.add(ornamentComponentsPanel, new VerticalLayoutData(1.0, .65));
		ornamenticFilterVLC.setHeight("1000px");
		
		return ornamenticFilterVLC;
	}

	/**
	 * In this method we assemble the search entry from the fields in the filter. Here we create the search entry 
	 * that will be send to the server.
	 */
	@Override
	public AbstractSearchEntry getSearchEntry() {
		OrnamenticSearchEntry searchEntry = new OrnamenticSearchEntry();
		
		if (ornamentCodeSearchTF.getValue() != null && !ornamentCodeSearchTF.getValue().isEmpty()) {
			searchEntry.setCode(ornamentCodeSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentDeskriptionSearchTF.getValue() != null && !ornamentDeskriptionSearchTF.getValue().isEmpty()) {
			searchEntry.setDescription(ornamentDeskriptionSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentInterpretationSearchTF.getValue() != null && !ornamentInterpretationSearchTF.getValue().isEmpty()) {
			searchEntry.setInterpretation(ornamentInterpretationSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentOrnamentalGroupSearchTF.getValue() != null && !ornamentOrnamentalGroupSearchTF.getValue().isEmpty()) {
			searchEntry.setGroup(ornamentOrnamentalGroupSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentReferencesSearchTF.getValue() != null && !ornamentReferencesSearchTF.getValue().isEmpty()) {
			searchEntry.setReferences(ornamentReferencesSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentRemarksSearchTF.getValue() != null && !ornamentRemarksSearchTF.getValue().isEmpty()) {
			searchEntry.setRemarks(ornamentRemarksSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		if (ornamentSimilaritiesSearchTF.getValue() != null && !ornamentSimilaritiesSearchTF.getValue().isEmpty()) {
			searchEntry.setSimilaritys(ornamentSimilaritiesSearchTF.getValue());
			searchEntry.setEmpty(false);
		}
		
		if (!ornamentComponentsSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentComponentsEntry oce : ornamentComponentsSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getComponents().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		if (!innerSecondaryPatternsSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (InnerSecondaryPatternsEntry oce : innerSecondaryPatternsSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getSecondarypatterns().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		if (!districtsSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (DistrictEntry oce : districtsSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getDistricts().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		if (!relatedOrnamentsSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentEntry oce : relatedOrnamentsSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getRelatedOrnaments().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		
		if (!positionSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentPositionEntry oce : positionSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getPosition().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		
		
		if (!functionSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentFunctionEntry oce : functionSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getFunction().add(oce);
			}
			searchEntry.setEmpty(false);
		}
		
		
		return searchEntry;
	}

}
