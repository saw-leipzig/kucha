/*
 * Copyright 2016 
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
package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.BackAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.MainChamberEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

public class CaveEditor implements IsWidget {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private FramedPanel mainPanel;
	private CaveEntry correspondingCaveEntry;
//	private AntechamberEntry correspondingAntechamberEntry;
//	private MainChamberEntry correspondingMainChamberEntry;
//	private BackAreaEntry correspondingBackAreaEntry;
	private ComboBox<CaveTypeEntry> caveTypeSelection;
	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private SiteProperties siteProps;

	private TextField officialNumberField;
	private TextField officialNameField;
	private TextField historicNameField;
	private ComboBox<DistrictEntry> districtSelection;
	private ComboBox<RegionEntry> regionSelection;
	private TextArea stateOfPreservationTextArea;
	private ListStore<SiteEntry> siteEntryList;
	private StoreFilter<RegionEntry> regionFilter;
	private ComboBox<SiteEntry> siteSelection;
	private StoreFilter<DistrictEntry> districtFilter;
	protected Object siteEntryAccess;
	private ArrayList<CaveEditorListener> listenerList;
	private Label siteDisplay;
	private TextField alterationDateField;
	private TextArea findingsTextArea;

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();
		LabelProvider<CaveTypeEntry> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}
	
	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> name();
	}
	
	interface DistrictViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml districtLabel(String name);
	}
	
	interface RegionProperties extends PropertyAccess<RegionEntry> {
		ModelKeyProvider<RegionEntry> regionID();
		LabelProvider<RegionEntry> englishName();
	}
	
	interface RegionViewTemplates extends XTemplates {
		@XTemplate("<div>{phoneticName} ({originalName} / {englishName})</div>")
		SafeHtml regionLabel(String phoneticName, String originalName, String englishName);
		
		@XTemplate("<div>{englishName}</div>")
		SafeHtml regionLabel(String englishName);
	}
	
	interface SiteProperties extends PropertyAccess<SiteEntry> {
		ModelKeyProvider<SiteEntry> siteID();
		LabelProvider<SiteEntry> name();
	}
	
	interface SiteViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml siteLabel(String name);
	}
	
	interface CaveLayoutViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" width=\"242\" height=\"440\" margin=\"20\" src=\"{imageUri}\"><br> {title}")
		SafeHtml image(SafeUri imageUri, String title);
	}

	public CaveEditor(CaveEntry caveEntry, CaveEditorListener listener) {
		if (caveEntry == null) {
			createNewCaveEntry();
		} else {
			correspondingCaveEntry = caveEntry;
		}
		listenerList = new ArrayList<CaveEditorListener>();
		listenerList.add(listener);
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		regionProps = GWT.create(RegionProperties.class);
		regionEntryList = new ListStore<RegionEntry>(regionProps.regionID());
		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		initPanel();
		loadCaveTypes();
		loadDistricts();
		loadRegions();
		loadSites();
	}

	/**
	 * 
	 */
	private void loadCaveTypes() {
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
				if (correspondingCaveEntry.getCaveTypeID() > 0) {
					caveTypeSelection.setValue(caveTypeEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID())));
				}
			}
		});
	}

	/**
	 * 
	 */
	private void loadSites() {
		dbService.getSites(new AsyncCallback<ArrayList<SiteEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<SiteEntry> result) {
				siteEntryList.clear();
				for (SiteEntry se : result) {
					siteEntryList.add(se);
				}
			}
		});
	}

	/* 
	 * 
	 */
	private void loadRegions() {
		dbService.getRegions(new AsyncCallback<ArrayList<RegionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<RegionEntry> result) {
				regionEntryList.clear();
				for (RegionEntry re : result) {
					regionEntryList.add(re);
				}
				if (correspondingCaveEntry.getRegionID() > 0) {
					regionSelection.setValue(regionEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getRegionID())));
				}
			}
		});
	}

	/**
	 * 
	 */
	private void loadDistricts() {
		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				districtEntryList.clear();
				for (DistrictEntry de : result) {
					districtEntryList.add(de);
				}
				if (correspondingCaveEntry.getDistrictID() > 0) {
					DistrictEntry de = districtEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getDistrictID()));
					districtSelection.setValue(de);
					dbService.getSite(de.getSiteID(), new AsyncCallback<SiteEntry>() {

						@Override
						public void onFailure(Throwable caught) {
							caught.printStackTrace(System.err);
						}

						@Override
						public void onSuccess(SiteEntry result) {
							siteDisplay.setText(result.getName());
						}
					});
					
				}
			}
		});
	}

	/**
	 * 
	 */
	private void createNewCaveEntry() {
		correspondingCaveEntry = new CaveEntry(0, "enter official cave number", "enter official cave name", "optional historic name", 0, 0,
				0, null, null, null, "enter findings here", null);
	}

	@Override
	public Widget asWidget() {
		Info.display("asWidget", "called");
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	public void initPanel() {
		FramedPanel attributePanel;
		
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Cave Editor");

		HorizontalPanel hPanel = new HorizontalPanel();

		VerticalPanel vPanel = new VerticalPanel();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Official Number");
		officialNumberField = new TextField();
		officialNumberField.setAllowBlank(false);
		officialNumberField.setValue(correspondingCaveEntry.getOfficialNumber());
		officialNumberField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setOfficialNumber(event.getValue());
			}
		});
		attributePanel.add(officialNumberField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Official Name");
		officialNameField = new TextField();
		officialNameField.setValue(correspondingCaveEntry.getOfficialName());
		officialNameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setOfficialName(event.getValue());
			}
		});
		attributePanel.add(officialNameField);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Historic Name");
		historicNameField = new TextField();
		historicNameField.setValue(correspondingCaveEntry.getHistoricName());
		historicNameField.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setHistoricName(event.getValue());
			}
		});
		attributePanel.add(historicNameField);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Site");
		if (correspondingCaveEntry.getCaveID() > 0) {
			siteDisplay = new Label();
			siteDisplay.setWidth("20em");
			attributePanel.add(siteDisplay);
		} else {
			siteSelection = new ComboBox<SiteEntry>(siteEntryList, siteProps.name(), 
					new AbstractSafeHtmlRenderer<SiteEntry>() {

						@Override
						public SafeHtml render(SiteEntry item) {
							final SiteViewTemplates svTemplates = GWT.create(SiteViewTemplates.class);
							return svTemplates.siteLabel(item.getName());
						}
					});
			siteSelection.setEmptyText("select site");
			siteSelection.setTypeAhead(false);
			siteSelection.setEditable(false);
			siteSelection.setTriggerAction(TriggerAction.ALL);
			siteSelection.addSelectionHandler(new SelectionHandler<SiteEntry>() {

				@Override
				public void onSelection(SelectionEvent<SiteEntry> event) {
					regionFilter = new StoreFilter<RegionEntry>() {

						@Override
						public boolean select(Store<RegionEntry> store, RegionEntry parent, RegionEntry item) {
							return (item.getSiteID() == siteSelection.getCurrentValue().getSiteID());
						}
					};
					regionEntryList.addFilter(regionFilter);
					regionEntryList.setEnableFilters(true);
					regionSelection.setEnabled(true);
					districtFilter = new StoreFilter<DistrictEntry>() {

						@Override
						public boolean select(Store<DistrictEntry> store, DistrictEntry parent, DistrictEntry item) {
							return (item.getSiteID() == siteSelection.getCurrentValue().getSiteID());
						}

					};
					districtEntryList.addFilter(districtFilter);
					districtEntryList.setEnableFilters(true);
					districtEntryList.setEnableFilters(true);
					districtSelection.setEnabled(true);
				}
			});
			siteSelection.setWidth(250);
			attributePanel.add(siteSelection);			
		}
		vPanel.add(attributePanel);		

		attributePanel = new FramedPanel();
		attributePanel.setHeading("District");
		districtSelection = new ComboBox<DistrictEntry>(districtEntryList, districtProps.name(), 
				new AbstractSafeHtmlRenderer<DistrictEntry>() {

					@Override
					public SafeHtml render(DistrictEntry item) {
						final DistrictViewTemplates dvTemplates = GWT.create(DistrictViewTemplates.class);
						return dvTemplates.districtLabel(item.getName());
					}
				});
		districtSelection.setEmptyText("select district");
		districtSelection.setTypeAhead(false);
		districtSelection.setEditable(false);
		districtSelection.setTriggerAction(TriggerAction.ALL);
		districtSelection.addSelectionHandler(new SelectionHandler<DistrictEntry>() {

			@Override
			public void onSelection(SelectionEvent<DistrictEntry> event) {
				correspondingCaveEntry.setDistrictID(event.getSelectedItem().getDistrictID());
			}
		});
		districtSelection.setWidth(250);
		if (correspondingCaveEntry.getCaveID() == 0) {
			districtSelection.setEnabled(false);
		}
		attributePanel.add(districtSelection);
		vPanel.add(attributePanel);

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Region");
		regionSelection = new ComboBox<RegionEntry>(regionEntryList, regionProps.englishName(), 
				new AbstractSafeHtmlRenderer<RegionEntry>() {

					@Override
					public SafeHtml render(RegionEntry item) {
						final RegionViewTemplates rvTemplates = GWT.create(RegionViewTemplates.class);
						if (!item.getPhoneticName().isEmpty() && !item.getOriginalName().isEmpty()) {
							return rvTemplates.regionLabel(item.getPhoneticName(), item.getOriginalName(), item.getEnglishName());
						} else {
							return rvTemplates.regionLabel(item.getEnglishName());
						}
					}
				});
		regionSelection.setEmptyText("select region");
		regionSelection.setTypeAhead(false);
		regionSelection.setEditable(false);
		regionSelection.setTriggerAction(TriggerAction.ALL);
		regionSelection.addSelectionHandler(new SelectionHandler<RegionEntry>() {

			@Override
			public void onSelection(SelectionEvent<RegionEntry> event) {
				correspondingCaveEntry.setRegionID(event.getSelectedItem().getRegionID());
			}
		});
		regionSelection.setWidth(250);
		if (correspondingCaveEntry.getCaveID() == 0) {
			regionSelection.setEnabled(false);
		}
		attributePanel.add(regionSelection);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Alteration date");
		alterationDateField = new TextField();
		alterationDateField.setValue(correspondingCaveEntry.getAlterationDate());
		alterationDateField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setAlterationDate(event.getValue());
			}
		});
		alterationDateField.setWidth("250px");
		attributePanel.add(alterationDateField);
		vPanel.add(attributePanel);

		hPanel.add(vPanel);
		vPanel = new VerticalPanel();
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("State of Preservation");
		stateOfPreservationTextArea = new TextArea();
		stateOfPreservationTextArea.setValue(correspondingCaveEntry.getStateOfPerservation());
		stateOfPreservationTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setStateOfPerservation(event.getValue());
			}
		});
		stateOfPreservationTextArea.setSize("250px", "200px");
		attributePanel.add(stateOfPreservationTextArea);
		vPanel.add(attributePanel);
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Findings");
		findingsTextArea = new TextArea();
		findingsTextArea.setValue(correspondingCaveEntry.getFindings());
		findingsTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setFindings(event.getValue());
			}
		});
		findingsTextArea.setSize("250px", "350px");
		attributePanel.add(findingsTextArea);
		vPanel.add(attributePanel);
		
		hPanel.add(vPanel);
		vPanel = new VerticalPanel();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Cave Type");
		caveTypeSelection = new ComboBox<CaveTypeEntry>(caveTypeEntryList, caveTypeProps.nameEN(),
				new AbstractSafeHtmlRenderer<CaveTypeEntry>() {

					@Override
					public SafeHtml render(CaveTypeEntry item) {
						final CaveTypeViewTemplates ctvTemplates = GWT.create(CaveTypeViewTemplates.class);
						return ctvTemplates.caveTypeLabel(item.getNameEN());
					}
				});
		caveTypeSelection.setEmptyText("select cave type");
		caveTypeSelection.setTypeAhead(false);
		caveTypeSelection.setEditable(false);
		caveTypeSelection.setTriggerAction(TriggerAction.ALL);
		caveTypeSelection.addSelectionHandler(new SelectionHandler<CaveTypeEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<CaveTypeEntry> event) {
				correspondingCaveEntry.setCaveTypeID(event.getSelectedItem().getCaveTypeID());
			}
		});
		caveTypeSelection.setWidth(250);
		attributePanel.add(caveTypeSelection);
		vPanel.add(attributePanel);
		
		final CaveLayoutViewTemplates caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);	
		SafeUri imageUri = UriUtils.fromString("resource?background=centralPillarCave.jpeg");
		FlowLayoutContainer imageContainer = new FlowLayoutContainer();
		imageContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(imageUri, "Central Pillar Cave")));
		vPanel.add(imageContainer);
		
		hPanel.add(vPanel);

		mainPanel.add(hPanel);
		
		TextButton saveButton = new TextButton("Save & Exit");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveEntries();
			}
		});
		TextButton cancelButton = new TextButton("Cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				cancelCaveEditor();
			}
		});
		mainPanel.addButton(saveButton);
		mainPanel.addButton(cancelButton);
		
	}

	/**
	 * 
	 */
	protected void cancelCaveEditor() {
		for (CaveEditorListener l : listenerList) {
			l.closeRequest();
		}
	}
	
	public void addCaveEditorListener(CaveEditorListener l) {
		listenerList.add(l);
	}

	/**
	 * 
	 */
	protected void saveEntries() {
		if (correspondingCaveEntry.getCaveID() > 0) {
			dbService.updateEntry(correspondingCaveEntry.getUpdateSql(), new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Boolean result) {
					// TODO Auto-generated method stub
					
				}
			});
		} else { // then its 0 and we need to create a new entry
			dbService.insertEntry(correspondingCaveEntry.getInsertSql(), new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Integer result) {
					correspondingCaveEntry.setCaveID(result);
				}
			});
		}
		cancelCaveEditor();
	}

}
