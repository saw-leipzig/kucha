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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractFilter;
import de.cses.shared.AbstractSearchEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.InnerSecondaryPatternsEntry;
import de.cses.shared.OrnamentComponentsEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentOfOtherCulturesEntry;
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
	private ListView<InnerSecondaryPatternsEntry, OrnamentComponentsEntry> innerSecondaryPatternsSelectionLV;
	
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
		ValueProvider<OrnamentEntry, String> name();
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
		@XTemplate("<div>{name}</div>")
		SafeHtml relatedOrnamentsLabel(String name);
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
		//loadCaveEntryList();
		
		districtsProps = GWT.create(DistrictsProperties.class);
		districtsEntryList = new ListStore<DistrictEntry>(districtsProps.districtID());
		//loadDistrictEntryList();
		
		relatedOrnamentsProps = GWT.create(RelatedOrnamentsProperties.class);
		relatedOrnamentsEntryList = new ListStore<OrnamentEntry>(relatedOrnamentsProps.ornamentID());
		//loadInnerSecondaryPatternsEntryList();
		
		/*innerSecondaryPatternsProps = GWT.create(InnerSecondaryPatternsProperties.class);
		innerSecondaryPatternsEntryList = new ListStore<InnerSecondaryPatternsEntry>(innerSecondaryPatternsProps.innerSecondaryPatternsID());
		//loadInnerSecondaryPatternsEntryList();
		  */
		 
		
		functionProps = GWT.create(FunctionProperties.class);
		functionEntryList = new ListStore<OrnamentFunctionEntry>(functionProps.ornamentFunctionID());
		//loadInnerSecondaryPatternsEntryList();
		
		positionProps = GWT.create(PositionProperties.class);
		positionEntryList = new ListStore<OrnamentPositionEntry>(positionProps.ornamentPositionID());
		//loadInnerSecondaryPatternsEntryList();
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
	
	private void loadInnerSecondaryPatternsEntryList() {
		dbService.getInnerSecondaryPatterns(new AsyncCallback<ArrayList<InnerSecondaryPatternsEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<InnerSecondaryPatternsEntry> result) {
				ornamentComponentsEntryList.clear();
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
		
		ToolButton resetOrnamentComponentsPanelTB = new ToolButton(ToolButton.REFRESH);
		resetOrnamentComponentsPanelTB.setToolTip(Util.createToolTip("Reset selection"));
		resetOrnamentComponentsPanelTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				ornamentComponentsSelectionLV.getSelectionModel().deselectAll();
			}
		});
		ornamentComponentsPanel.addTool(resetOrnamentComponentsPanelTB);

		// add more here

		VerticalLayoutContainer ornamenticFilterVLC = new VerticalLayoutContainer();
		ornamenticFilterVLC.add(ornamentCodeSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentDeskriptionSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentInterpretationSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentOrnamentalGroupSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentReferencesSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentRemarksSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentSimilaritiesSearchTF, new VerticalLayoutData(1.0, .05));
		ornamenticFilterVLC.add(ornamentComponentsPanel, new VerticalLayoutData(1.0, .65));
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
		}
		if (ornamentDeskriptionSearchTF.getValue() != null && !ornamentDeskriptionSearchTF.getValue().isEmpty()) {
			searchEntry.setDescription(ornamentDeskriptionSearchTF.getValue());
		}
		if (ornamentInterpretationSearchTF.getValue() != null && !ornamentInterpretationSearchTF.getValue().isEmpty()) {
			searchEntry.setInterpretation(ornamentInterpretationSearchTF.getValue());
		}
		if (ornamentOrnamentalGroupSearchTF.getValue() != null && !ornamentOrnamentalGroupSearchTF.getValue().isEmpty()) {
			searchEntry.setGroup(ornamentOrnamentalGroupSearchTF.getValue());
		}
		if (ornamentReferencesSearchTF.getValue() != null && !ornamentReferencesSearchTF.getValue().isEmpty()) {
			searchEntry.setReferences(ornamentReferencesSearchTF.getValue());
		}
		if (ornamentRemarksSearchTF.getValue() != null && !ornamentRemarksSearchTF.getValue().isEmpty()) {
			searchEntry.setRemarks(ornamentRemarksSearchTF.getValue());
		}
		if (ornamentSimilaritiesSearchTF.getValue() != null && !ornamentSimilaritiesSearchTF.getValue().isEmpty()) {
			searchEntry.setSimilaritys(ornamentSimilaritiesSearchTF.getValue());
		}
		
		if (!ornamentComponentsSelectionLV.getSelectionModel().getSelectedItems().isEmpty()) {
			for (OrnamentComponentsEntry oce : ornamentComponentsSelectionLV.getSelectionModel().getSelectedItems()) {
				searchEntry.getComponents().add(oce);
			}
		}
		
		// @nina: siehe auch getSearchEntry() z.B. in DepictionFilter
		
		return searchEntry;
	}

}
