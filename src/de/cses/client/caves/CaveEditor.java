/*
 * Copyright 2016-2017
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
import com.google.gwt.user.client.ui.PopupPanel;
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
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.ui.AbstractEditor;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveGroupEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.OrientationEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

public class CaveEditor extends AbstractEditor {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private FramedPanel mainPanel;
	private CaveEntry correspondingCaveEntry;
	private ComboBox<CaveTypeEntry> caveTypeSelection;
	private CaveTypeProperties caveTypeProps;
	private ListStore<CaveTypeEntry> caveTypeEntryList;
	private DistrictProperties districtProps;
	private ListStore<DistrictEntry> districtEntryList;
	private RegionProperties regionProps;
	private ListStore<RegionEntry> regionEntryList;
	private SiteProperties siteProps;
	private OrientationProperties orientationProps;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;

	private TextField officialNumberField;
	private TextField historicNameField;
	private TextField optionalHistoricNameField;
	private ComboBox<DistrictEntry> districtSelection;
	private ComboBox<RegionEntry> regionSelection;
	private TextArea stateOfPreservationTextArea;
	private ListStore<SiteEntry> siteEntryList;
	private StoreFilter<RegionEntry> regionFilter;
	private ListStore<OrientationEntry> orientationEntryList;
	private ComboBox<SiteEntry> siteSelection;
	private StoreFilter<DistrictEntry> districtFilter;
	protected Object siteEntryAccess;
//	private ArrayList<CaveEditorListener> listenerList;
//	private Label siteDisplay;
	private TextField alterationDateField;
	private TextArea findingsTextArea;
	private FlowLayoutContainer imageContainer;
	private ComboBox<OrientationEntry> orientationSelection;
	private CeilingTypeProperties ceilingTypeProps;
	private ListStore<CeilingTypeEntry> ceilingTypeEntryList;
	private ComboBox<CeilingTypeEntry> rearAreaCeilingTypeSelector;
	private ComboBox<CeilingTypeEntry> mainChamberCeilingTypeSelector;
	private ComboBox<CeilingTypeEntry> antechamberCeilingTypeSelector;
	private PreservationClassificationProperties preservationClassificationProps;
	private ListStore<PreservationClassificationEntry> preservationClassificationEntryList;
	private ComboBox<PreservationClassificationEntry> rearAreaPreservationSelector;
	private ComboBox<PreservationClassificationEntry> rearAreaCeilingPreservationSelector;
	private ComboBox<PreservationClassificationEntry> mainChamberPreservationSelector;
	private ComboBox<PreservationClassificationEntry> mainChamberCeilingPreservationSelector;
	private ComboBox<PreservationClassificationEntry> antechamberPreservationSelector;
	private ComboBox<PreservationClassificationEntry> antechamberCeilingPreservationSelector;
//	private ComboBox<PreservationClassificationEntry> overallPreservationSelector;
	private ComboBox<CaveGroupEntry> caveGroupSelector;
	private CaveGroupProperties caveGroupProps;
	private ListStore<CaveGroupEntry> caveGroupEntryList;

	interface CaveTypeProperties extends PropertyAccess<CaveTypeEntry> {
		ModelKeyProvider<CaveTypeEntry> caveTypeID();

		LabelProvider<CaveTypeEntry> nameEN();
	}

	interface CaveTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveTypeLabel(String name);
	}

	interface CeilingTypeProperties extends PropertyAccess<CeilingTypeEntry> {
		ModelKeyProvider<CeilingTypeEntry> ceilingTypeID();

		LabelProvider<CeilingTypeEntry> name();
	}

	interface CeilingTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml ceilingTypeLabel(String name);
	}

	interface PreservationClassificationProperties extends PropertyAccess<PreservationClassificationEntry> {
		ModelKeyProvider<PreservationClassificationEntry> preservationClassificationID();
		LabelProvider<PreservationClassificationEntry> name();
	}

	interface PreservationClassificationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml preservationClassificationLabel(String name);
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

	interface OrientationProperties extends PropertyAccess<OrientationEntry> {
		ModelKeyProvider<OrientationEntry> orientationID();

		LabelProvider<OrientationEntry> nameEN();
	}

	interface OrientationViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml orientationLabel(String name);
	}

	interface CaveGroupProperties extends PropertyAccess<CaveGroupEntry> {
		ModelKeyProvider<CaveGroupEntry> caveGroupID();

		LabelProvider<CaveGroupEntry> name();
	}

	interface CaveGroupViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml caveGroupLabel(String name);
	}

	interface CaveLayoutViewTemplates extends XTemplates {
		// @XTemplate("<img align=\"center\" width=\"242\" height=\"440\" margin=\"20\" src=\"{imageUri}\">")
		@XTemplate("<img align=\"center\" margin=\"10\" src=\"{imageUri}\">")
		SafeHtml image(SafeUri imageUri);
	}

	public CaveEditor(CaveEntry caveEntry) {
		if (caveEntry == null) {
			createNewCaveEntry();
		} else {
			correspondingCaveEntry = caveEntry;
		}
		caveTypeProps = GWT.create(CaveTypeProperties.class);
		caveTypeEntryList = new ListStore<CaveTypeEntry>(caveTypeProps.caveTypeID());
		ceilingTypeProps = GWT.create(CeilingTypeProperties.class);
		ceilingTypeEntryList = new ListStore<CeilingTypeEntry>(ceilingTypeProps.ceilingTypeID());
		preservationClassificationProps = GWT.create(PreservationClassificationProperties.class);
		preservationClassificationEntryList = new ListStore<PreservationClassificationEntry>(
				preservationClassificationProps.preservationClassificationID());
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		orientationProps = GWT.create(OrientationProperties.class);
		orientationEntryList = new ListStore<OrientationEntry>(orientationProps.orientationID());
		caveGroupProps = GWT.create(CaveGroupProperties.class);
		caveGroupEntryList = new ListStore<CaveGroupEntry>(caveGroupProps.caveGroupID());
		regionProps = GWT.create(RegionProperties.class);
		regionEntryList = new ListStore<RegionEntry>(regionProps.regionID());
		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		initPanel();
		loadCaveAndCeilingTypes();
		loadDistricts();
		loadRegions();
		loadSites();
		loadOrientation();
		loadCaveGroups();
	}

	/**
	 * 
	 */
	private void loadCaveAndCeilingTypes() {
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
					CaveTypeEntry correspondingCaveTypeEntry = caveTypeEntryList
							.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID()));
					caveTypeSelection.setValue(correspondingCaveTypeEntry);
					imageContainer.clear();
					imageContainer.add(new HTMLPanel(
							caveLayoutViewTemplates.image(UriUtils.fromString("resource?background=" + correspondingCaveTypeEntry.getSketchName()))));
				}
				dbService.getCeilingTypes(new AsyncCallback<ArrayList<CeilingTypeEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<CeilingTypeEntry> result) {
						ceilingTypeEntryList.clear();
						for (CeilingTypeEntry cte : result) {
							ceilingTypeEntryList.add(cte);
						}
						if (correspondingCaveEntry.getCaveTypeID() > 0) {
							CeilingTypeEntry ctEntry = ceilingTypeEntryList
									.findModelWithKey(Integer.toString(correspondingCaveEntry.getRearAreaEntry().getCeilingTypeID()));
							rearAreaCeilingTypeSelector.setValue(ctEntry);
							ctEntry = ceilingTypeEntryList
									.findModelWithKey(Integer.toString(correspondingCaveEntry.getMainChamberEntry().getCeilingTypeID()));
							mainChamberCeilingTypeSelector.setValue(ctEntry);
//							Info.display("MainChamber.CeilingTypeID", "id = " + correspondingCaveEntry.getMainChamberEntry().getCeilingTypeID());
							ctEntry = ceilingTypeEntryList
									.findModelWithKey(Integer.toString(correspondingCaveEntry.getAntechamberEntry().getCeilingTypeID()));
							antechamberCeilingTypeSelector.setValue(ctEntry);
						}
					}
				});
				dbService.getPreservationClassifications(new AsyncCallback<ArrayList<PreservationClassificationEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					@Override
					public void onSuccess(ArrayList<PreservationClassificationEntry> result) {
						preservationClassificationEntryList.clear();
						for (PreservationClassificationEntry pce : result) {
							preservationClassificationEntryList.add(pce);
						}
						rearAreaPreservationSelector.setValue(preservationClassificationEntryList
								.findModelWithKey(Integer.toString(correspondingCaveEntry.getRearAreaEntry().getPreservationClassificationID())));
						mainChamberPreservationSelector.setValue(preservationClassificationEntryList
								.findModelWithKey(Integer.toString(correspondingCaveEntry.getMainChamberEntry().getPreservationClassificationID())));
						antechamberPreservationSelector.setValue(preservationClassificationEntryList
								.findModelWithKey(Integer.toString(correspondingCaveEntry.getAntechamberEntry().getPreservationClassificationID())));
					}

				});
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

	/**
	 * 
	 */
	private void loadOrientation() {
		dbService.getOrientationInformation(new AsyncCallback<ArrayList<OrientationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Problem loading OrientationEntry");
			}

			@Override
			public void onSuccess(ArrayList<OrientationEntry> result) {
				orientationEntryList.clear();
				for (OrientationEntry entry : result) {
					orientationEntryList.add(entry);
					if (correspondingCaveEntry.getOrientationID() > 0) {
						orientationSelection
								.setValue(orientationEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getOrientationID())));
					}

				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void loadCaveGroups() {
		dbService.getCaveGroups(new AsyncCallback<ArrayList<CaveGroupEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Problem loading CaveGroupEntry");
			}

			@Override
			public void onSuccess(ArrayList<CaveGroupEntry> result) {
				caveGroupEntryList.clear();
				for (CaveGroupEntry entry : result) {
					caveGroupEntryList.add(entry);
					if (correspondingCaveEntry.getCaveGroupID() > 0) {
						caveGroupSelector.setValue(caveGroupEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveGroupID())));
					}
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
					RegionEntry re = regionEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getRegionID()));
					regionSelection.setValue(re);
					if (siteSelection.getCurrentValue() == null || siteSelection.getCurrentValue().getSiteID() != re.getSiteID()) {
						dbService.getSite(re.getSiteID(), new AsyncCallback<SiteEntry>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace(System.err);
							}

							@Override
							public void onSuccess(SiteEntry result) {
								siteSelection.setValue(result);
								activateRegionFilter();
								activateDistrictFilter();
							}
						});
					}
					
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
				for (final DistrictEntry de : result) {
					districtEntryList.add(de);
				}
				if (correspondingCaveEntry.getDistrictID() > 0) {
				final	DistrictEntry de = districtEntryList.findModelWithKey(Integer.toString(correspondingCaveEntry.getDistrictID()));
					districtSelection.setValue(de);
					if (siteSelection.getCurrentValue() == null || siteSelection.getCurrentValue().getSiteID() != de.getSiteID()) {
						dbService.getSite(de.getSiteID(), new AsyncCallback<SiteEntry>() {

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace(System.err);
							}

							@Override
							public void onSuccess(SiteEntry result) {
								siteSelection.setValue(result);
								activateRegionFilter();
								activateDistrictFilter();
							}
						});
					}
				}
			}
		});
	}

	/**
	 * 
	 */
	private void createNewCaveEntry() {
		correspondingCaveEntry = new CaveEntry();
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		// all fields added are encapsulated by a FramedPanel
		FramedPanel attributePanel;

		final CaveTypeViewTemplates ctvt = GWT.create(CaveTypeViewTemplates.class);
		final PreservationClassificationViewTemplates pcvt = GWT.create(PreservationClassificationViewTemplates.class);

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Cave Editor");
		mainPanel.setSize("800px", "600px"); // here we set the size of the panel

		TabPanel tabPanel = new TabPanel();
		tabPanel.setTabScroll(false);
		// the tab only gets 70% of the width and the added images get the 25% on the right to be shown all the time
		// tabPanel.setSize("70%", "100%");

		// the tab will use the right 70% of the main HorizontalLayoutPanel
		HorizontalLayoutContainer mainHlContainer = new HorizontalLayoutContainer();

		// each column is represented by a VerticalLayoutPanel
		VerticalLayoutContainer vlContainer = new VerticalLayoutContainer();

		/**
		 * ------------------------- this is the first column on the left side -------------------------
		 */
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Official Number");
		officialNumberField = new TextField();
		officialNumberField.addValidator(new MinLengthValidator(1));
		officialNumberField.setEmptyText("mandatory cave number");
		officialNumberField.setAllowBlank(false);
		officialNumberField.setValue(correspondingCaveEntry.getOfficialNumber());
		officialNumberField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setOfficialNumber(event.getValue());
			}
		});
		attributePanel.add(officialNumberField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Historic Name");
		historicNameField = new TextField();
		historicNameField.addValidator(new MaxLengthValidator(64));
		historicNameField.setEmptyText("historic cave name");
		historicNameField.setValue(correspondingCaveEntry.getHistoricName());
		historicNameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setHistoricName(event.getValue());
			}
		});
		attributePanel.add(historicNameField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Optional Historic Name");
		optionalHistoricNameField = new TextField();
		optionalHistoricNameField.addValidator(new MaxLengthValidator(64));
		optionalHistoricNameField.setEmptyText("optional historic name");
		optionalHistoricNameField.setValue(correspondingCaveEntry.getOptionalHistoricName());
		optionalHistoricNameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setOptionalHistoricName(event.getValue());
			}
		});
		attributePanel.add(optionalHistoricNameField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Cave Group");
		caveGroupSelector = new ComboBox<CaveGroupEntry>(caveGroupEntryList, caveGroupProps.name(),
				new AbstractSafeHtmlRenderer<CaveGroupEntry>() {

					@Override
					public SafeHtml render(CaveGroupEntry item) {
						final CaveGroupViewTemplates cgvTemplates = GWT.create(CaveGroupViewTemplates.class);
						return cgvTemplates.caveGroupLabel(item.getName());
					}
				});
		caveGroupSelector.setTriggerAction(TriggerAction.ALL);
		caveGroupSelector.setEditable(false);
		caveGroupSelector.setTypeAhead(false);
		caveGroupSelector.addSelectionHandler(new SelectionHandler<CaveGroupEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveGroupEntry> event) {
				correspondingCaveEntry.setCaveGroupID(event.getSelectedItem().getCaveGroupID());
			}
		});
		attributePanel.add(caveGroupSelector);
		TextButton addCaveGroupButton = new TextButton("New Cave Group");
		addCaveGroupButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addNewCaveGroupDialog = new PopupPanel();
				FramedPanel newCaveGroupFP = new FramedPanel();
				newCaveGroupFP.setHeading("Add Cave Group");
				VerticalLayoutContainer newCaveGroupVLC = new VerticalLayoutContainer();
				FramedPanel fp = new FramedPanel();
				fp.setHeading("Cave Group Name");
				TextField caveGroupNameField = new TextField();
				caveGroupNameField.addValidator(new MinLengthValidator(2));
				caveGroupNameField.setValue("");
				fp.add(caveGroupNameField);
				newCaveGroupVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 2));
				newCaveGroupFP.add(newCaveGroupVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						if (caveGroupNameField.isValid()) {
							CaveGroupEntry cgEntry = new CaveGroupEntry();
							cgEntry.setName(caveGroupNameField.getCurrentValue());
							dbService.insertEntry(cgEntry.getInsertSql(), new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									loadCaveGroups();
								}
							});
							addNewCaveGroupDialog.hide();
						}
					}
				});
				newCaveGroupFP.addButton(saveButton);
				TextButton closeButton = new TextButton("close");
				closeButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						addNewCaveGroupDialog.hide();
					}
				});
				newCaveGroupFP.addButton(closeButton);
				newCaveGroupFP.setSize("250", "250");
				addNewCaveGroupDialog.add(newCaveGroupFP);
				addNewCaveGroupDialog.setModal(true);
				addNewCaveGroupDialog.center();
			}
		});
		attributePanel.addButton(addCaveGroupButton);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Orientation");
		orientationSelection = new ComboBox<OrientationEntry>(orientationEntryList, orientationProps.nameEN(),
				new AbstractSafeHtmlRenderer<OrientationEntry>() {

					@Override
					public SafeHtml render(OrientationEntry item) {
						final OrientationViewTemplates ovTemplates = GWT.create(OrientationViewTemplates.class);
						return ovTemplates.orientationLabel(item.getNameEN());
					}
				});
		orientationSelection.setTriggerAction(TriggerAction.ALL);
		orientationSelection.setEditable(false);
		orientationSelection.setTypeAhead(false);
		orientationSelection.addSelectionHandler(new SelectionHandler<OrientationEntry>() {

			@Override
			public void onSelection(SelectionEvent<OrientationEntry> event) {
				correspondingCaveEntry.setOrientationID(event.getSelectedItem().getOrientationID());
			}
		});
		attributePanel.add(orientationSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Site");
//		if (correspondingCaveEntry.getCaveID() > 0) {
//			siteDisplay = new Label();
//			siteDisplay.setWidth("100%");
//			attributePanel.add(siteDisplay);
//		} else {
			siteSelection = new ComboBox<SiteEntry>(siteEntryList, siteProps.name(), new AbstractSafeHtmlRenderer<SiteEntry>() {

				@Override
				public SafeHtml render(SiteEntry item) {
					final SiteViewTemplates svTemplates = GWT.create(SiteViewTemplates.class);
					return svTemplates.siteLabel(item.getName());
				}
			});
			siteSelection.setEmptyText("select site");
			siteSelection.setTypeAhead(false);
			siteSelection.setEditable(false);
			siteSelection.setWidth("100%");
			siteSelection.setTriggerAction(TriggerAction.ALL);
			siteSelection.addSelectionHandler(new SelectionHandler<SiteEntry>() {

				@Override
				public void onSelection(SelectionEvent<SiteEntry> event) {
					activateRegionFilter();
					activateDistrictFilter();
				}
			});
			siteSelection.setWidth(250);
			attributePanel.add(siteSelection);
//		}
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .11));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("District");
		districtSelection = new ComboBox<DistrictEntry>(districtEntryList, districtProps.name(), new AbstractSafeHtmlRenderer<DistrictEntry>() {

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
		
		TextButton addDistrictButton = new TextButton("New District");
		addDistrictButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				if (siteSelection.getCurrentValue() == null) {
					Util.showWarning("A problem occurred", "Please select Site first!");
					return;
				}
				PopupPanel addNewDistrictDialog = new PopupPanel();
				FramedPanel newDistrictFP = new FramedPanel();
				newDistrictFP.setHeading("Add District in " + siteSelection.getCurrentValue().getName());
				DistrictEntry de = new DistrictEntry();
				de.setSiteID(siteSelection.getCurrentValue().getSiteID());
				VerticalLayoutContainer newDistrictVLC = new VerticalLayoutContainer();
				FramedPanel fp = new FramedPanel();
				fp.setHeading("District Name");
				TextField districtNameField = new TextField();
				districtNameField.addValidator(new MinLengthValidator(2));
				districtNameField.setValue("");
				fp.add(districtNameField);
				newDistrictVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("Descritpion");
				TextArea descriptionField = new TextArea();
				descriptionField.setValue("");
				fp.add(descriptionField);
				newDistrictVLC.add(fp, new VerticalLayoutData(1.0, 2.0 / 3));
				newDistrictFP.add(newDistrictVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						if (districtNameField.isValid()) {
							de.setName(districtNameField.getValue());
							de.setDescription(descriptionField.getValue().isEmpty() ? "" : descriptionField.getValue());
							dbService.insertEntry(de.getInsertSql(), new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									loadDistricts();
								}
							});
							addNewDistrictDialog.hide();
						}
					}
				});
				newDistrictFP.addButton(saveButton);
				TextButton closeButton = new TextButton("close");
				closeButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						addNewDistrictDialog.hide();
					}
				});
				newDistrictFP.addButton(closeButton);
				newDistrictFP.setSize("250", "250");
				addNewDistrictDialog.add(newDistrictFP);
				addNewDistrictDialog.setModal(true);
				addNewDistrictDialog.center();
			}
		});
		attributePanel.addButton(addDistrictButton);
		
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .17));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Region");
		regionSelection = new ComboBox<RegionEntry>(regionEntryList, regionProps.englishName(), new AbstractSafeHtmlRenderer<RegionEntry>() {

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
		
		TextButton addRegionButton = new TextButton("New Region");
		addRegionButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				if (siteSelection.getCurrentValue() == null) {
					Util.showWarning("A problem occurred", "Please select Site first!");
					return;
				}
				PopupPanel addNewRegionDialog = new PopupPanel();
				FramedPanel newRegionFP = new FramedPanel();
				newRegionFP.setHeading("Add Region in " + siteSelection.getCurrentValue().getName());
				RegionEntry re = new RegionEntry();
				re.setSiteID(siteSelection.getCurrentValue().getSiteID());
				VerticalLayoutContainer newRegionVLC = new VerticalLayoutContainer();
				FramedPanel fp = new FramedPanel();
				fp.setHeading("Phonetic Name");
				TextField phoneticNameField = new TextField();
				phoneticNameField.setValue("");
				fp.add(phoneticNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("Original Name");
				TextField originalNameField = new TextField();
				originalNameField.setValue("");
				fp.add(originalNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				fp = new FramedPanel();
				fp.setHeading("English Name");
				TextField englishNameField = new TextField();
				englishNameField.setValue("");
				fp.add(englishNameField);
				newRegionVLC.add(fp, new VerticalLayoutData(1.0, 1.0 / 3));
				newRegionFP.add(newRegionVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						if (englishNameField.getValue().isEmpty()) {
							Util.showWarning("A problem occurred", "Please add at least an english name!");
						} else {
							re.setPhoneticName(phoneticNameField.getValue().isEmpty() ? "" : phoneticNameField.getValue());
							re.setOriginalName(originalNameField.getValue().isEmpty() ? "" : originalNameField.getValue());
							re.setEnglishName(englishNameField.getValue());
							dbService.insertEntry(re.getInsertSql(), new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									loadRegions();
								}
							});
							addNewRegionDialog.hide();
						}
					}
				});
				newRegionFP.addButton(saveButton);
				TextButton closeButton = new TextButton("close");
				closeButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						addNewRegionDialog.hide();
					}
				});
				newRegionFP.addButton(closeButton);
				newRegionFP.setSize("250", "250");
				addNewRegionDialog.add(newRegionFP);
				addNewRegionDialog.setModal(true);
				addNewRegionDialog.center();
			}
		});
		attributePanel.addButton(addRegionButton);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .17));

		mainHlContainer.add(vlContainer, new HorizontalLayoutData(.3, 1.0));

		/**
		 * ------------------------------ the column with the text fields (state of preservation tab) ----------------------------
		 */

		// we will use this HLC for the tabs
		HorizontalLayoutContainer stateOfPreservationHLC = new HorizontalLayoutContainer();
		vlContainer = new VerticalLayoutContainer();

//		attributePanel = new FramedPanel();
//		attributePanel.setHeading("Overall Preservation");
//		overallPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
//				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {
//
//					@Override
//					public SafeHtml render(PreservationClassificationEntry item) {
//						return pcvt.preservationClassificationLabel(item.getName());
//					}
//				});
//		overallPreservationSelector.setEmptyText("select cave type");
//		overallPreservationSelector.setTypeAhead(false);
//		overallPreservationSelector.setEditable(false);
//		overallPreservationSelector.setTriggerAction(TriggerAction.ALL);
//		overallPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {
//
//			@Override
//			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
//				correspondingCaveEntry.setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
//			}
//		});
//		
//		attributePanel.add(overallPreservationSelector);
//		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("ar Area");
		rearAreaPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		rearAreaPreservationSelector.setEmptyText("state of preservation");
		rearAreaPreservationSelector.setTypeAhead(false);
		rearAreaPreservationSelector.setEditable(false);
		rearAreaPreservationSelector.setTriggerAction(TriggerAction.ALL);
		rearAreaPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getRearAreaEntry().setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		attributePanel.add(rearAreaPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Main Chamber");
		mainChamberPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		mainChamberPreservationSelector.setEmptyText("state of preservation");
		mainChamberPreservationSelector.setTypeAhead(false);
		mainChamberPreservationSelector.setEditable(false);
		mainChamberPreservationSelector.setTriggerAction(TriggerAction.ALL);
		mainChamberPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getMainChamberEntry().setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		attributePanel.add(mainChamberPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Antechamber");
		antechamberPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		antechamberPreservationSelector.setEmptyText("state of preservation");
		antechamberPreservationSelector.setTypeAhead(false);
		antechamberPreservationSelector.setEditable(false);
		antechamberPreservationSelector.setTriggerAction(TriggerAction.ALL);
		antechamberPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.getAntechamberEntry().setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		attributePanel.add(antechamberPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));
		
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Rear Area Ceiling");
		rearAreaCeilingPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		rearAreaCeilingPreservationSelector.setEmptyText("state of preservation");
		rearAreaCeilingPreservationSelector.setTypeAhead(false);
		rearAreaCeilingPreservationSelector.setEditable(false);
		rearAreaCeilingPreservationSelector.setTriggerAction(TriggerAction.ALL);
		rearAreaCeilingPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		
		attributePanel.add(rearAreaCeilingPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Main Chamber Ceiling");
		mainChamberCeilingPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		mainChamberCeilingPreservationSelector.setEmptyText("state of preservation");
		mainChamberCeilingPreservationSelector.setTypeAhead(false);
		mainChamberCeilingPreservationSelector.setEditable(false);
		mainChamberCeilingPreservationSelector.setTriggerAction(TriggerAction.ALL);
		mainChamberCeilingPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		
		attributePanel.add(mainChamberCeilingPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));
	
		attributePanel = new FramedPanel();
		attributePanel.setHeading("Antechamber Ceiling");
		antechamberCeilingPreservationSelector = new ComboBox<PreservationClassificationEntry>(preservationClassificationEntryList,
				preservationClassificationProps.name(), new AbstractSafeHtmlRenderer<PreservationClassificationEntry>() {

					@Override
					public SafeHtml render(PreservationClassificationEntry item) {
						return pcvt.preservationClassificationLabel(item.getName());
					}
				});
		antechamberCeilingPreservationSelector.setEmptyText("state of preservation");
		antechamberCeilingPreservationSelector.setTypeAhead(false);
		antechamberCeilingPreservationSelector.setEditable(false);
		antechamberCeilingPreservationSelector.setTriggerAction(TriggerAction.ALL);
		antechamberCeilingPreservationSelector.addSelectionHandler(new SelectionHandler<PreservationClassificationEntry>() {

			@Override
			public void onSelection(SelectionEvent<PreservationClassificationEntry> event) {
				correspondingCaveEntry.setPreservationClassificationID(event.getSelectedItem().getPreservationClassificationID());
			}
		});
		
		attributePanel.add(antechamberCeilingPreservationSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0/6));

		stateOfPreservationHLC.add(vlContainer, new HorizontalLayoutData(.4, 1.0));

		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Further Comments");
		stateOfPreservationTextArea = new TextArea();
		stateOfPreservationTextArea.setEmptyText("This field is for remarks on the state of the preservation");
		stateOfPreservationTextArea.setValue(correspondingCaveEntry.getStateOfPerservation());
		stateOfPreservationTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setStateOfPerservation(event.getValue());
			}
		});
		// stateOfPreservationTextArea.setSize("250px", "200px");
		attributePanel.add(stateOfPreservationTextArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0));

		stateOfPreservationHLC.add(vlContainer, new HorizontalLayoutData(.6, 1.0));

		/**
		 * ------------------------------ the column with the text fields (description tab) ----------------------------
		 */

		HorizontalLayoutContainer descriptionHLC = new HorizontalLayoutContainer();
		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Findings");
		findingsTextArea = new TextArea();
		findingsTextArea.setEmptyText("research findings");
		findingsTextArea.setValue(correspondingCaveEntry.getFindings());
		findingsTextArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				correspondingCaveEntry.setFindings(event.getValue());
			}
		});
		attributePanel.add(findingsTextArea);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .7));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("C14 Analysis (link)");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("C14 additional documents");
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		descriptionHLC.add(vlContainer, new HorizontalLayoutData(1.0, 1.0));

		/**
		 * ------------------------- the cave type and layout description (cave type tab) -----------------------------
		 */

		HorizontalLayoutContainer caveTypeHLC = new HorizontalLayoutContainer();
		vlContainer = new VerticalLayoutContainer();

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
				CaveTypeEntry correspondingCaveTypeEntry = caveTypeEntryList
						.findModelWithKey(Integer.toString(correspondingCaveEntry.getCaveTypeID()));
				imageContainer.clear();
				imageContainer.add(new HTMLPanel(
						caveLayoutViewTemplates.image(UriUtils.fromString("resource?background=" + correspondingCaveTypeEntry.getSketchName()))));
			}
		});
		attributePanel.add(caveTypeSelection);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Rear Area Ceiling Type");
		rearAreaCeilingTypeSelector = new ComboBox<CeilingTypeEntry>(ceilingTypeEntryList, ceilingTypeProps.name(),
				new AbstractSafeHtmlRenderer<CeilingTypeEntry>() {

					@Override
					public SafeHtml render(CeilingTypeEntry item) {
						return ctvt.caveTypeLabel(item.getName());
					}
				});
		rearAreaCeilingTypeSelector.setEmptyText("select ceiling type");
		rearAreaCeilingTypeSelector.setTypeAhead(false);
		rearAreaCeilingTypeSelector.setEditable(false);
		rearAreaCeilingTypeSelector.setTriggerAction(TriggerAction.ALL);
		rearAreaCeilingTypeSelector.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getRearAreaEntry().setCeilingTypeID(event.getSelectedItem().getCeilingTypeID());
			}
		});
		attributePanel.add(rearAreaCeilingTypeSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Main Chamber Ceiling Type");
		mainChamberCeilingTypeSelector = new ComboBox<>(ceilingTypeEntryList, ceilingTypeProps.name(),
				new AbstractSafeHtmlRenderer<CeilingTypeEntry>() {

					@Override
					public SafeHtml render(CeilingTypeEntry item) {
						return ctvt.caveTypeLabel(item.getName());
					}
				});
		mainChamberCeilingTypeSelector.setEmptyText("select ceiling type");
		mainChamberCeilingTypeSelector.setTypeAhead(false);
		mainChamberCeilingTypeSelector.setEditable(false);
		mainChamberCeilingTypeSelector.setTriggerAction(TriggerAction.ALL);
		mainChamberCeilingTypeSelector.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getMainChamberEntry().setCeilingTypeID(event.getSelectedItem().getCeilingTypeID());
			}
		});
		attributePanel.add(mainChamberCeilingTypeSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Antechamber Ceiling Type");
		antechamberCeilingTypeSelector = new ComboBox<>(ceilingTypeEntryList, ceilingTypeProps.name(),
				new AbstractSafeHtmlRenderer<CeilingTypeEntry>() {

					@Override
					public SafeHtml render(CeilingTypeEntry item) {
						return ctvt.caveTypeLabel(item.getName());
					}
				});
		antechamberCeilingTypeSelector.setEmptyText("select ceiling type");
		antechamberCeilingTypeSelector.setTypeAhead(false);
		antechamberCeilingTypeSelector.setEditable(false);
		antechamberCeilingTypeSelector.setTriggerAction(TriggerAction.ALL);
		antechamberCeilingTypeSelector.addSelectionHandler(new SelectionHandler<CeilingTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<CeilingTypeEntry> event) {
				correspondingCaveEntry.getAntechamberEntry().setCeilingTypeID(event.getSelectedItem().getCeilingTypeID());
			}
		});
		attributePanel.add(antechamberCeilingTypeSelector);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));
		
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
		attributePanel.add(alterationDateField);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, .15));

		caveTypeHLC.add(vlContainer, new HorizontalLayoutData(.4, 1.0));

		vlContainer = new VerticalLayoutContainer();

		attributePanel = new FramedPanel();
		attributePanel.setHeading("Cave Layout");
		imageContainer = new FlowLayoutContainer();
		attributePanel.add(imageContainer);
		vlContainer.add(attributePanel, new VerticalLayoutData(1.0, 1.0));

		caveTypeHLC.add(vlContainer, new HorizontalLayoutData(.6, 1.0));

		/**
		 * now we are assembling the tabs and add them to the main hlc
		 */

		tabPanel.add(caveTypeHLC, "Cave Layout");
		tabPanel.add(stateOfPreservationHLC, "State of Preservation");
		tabPanel.add(descriptionHLC, "Descriptions");
		mainHlContainer.add(tabPanel, new HorizontalLayoutData(.7, 1.0));

		/**
		 * adding the mainHlContainer to the FramedPanel and adding the buttons
		 */
		mainPanel.add(mainHlContainer);

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
				closeEditor();
			}
		});
		mainPanel.addButton(saveButton);
		mainPanel.addButton(cancelButton);

	}

//	/**
//	 * Will be called when the cancel button is selected. Calls <code>CaveEditorListener.closeRequest()</code>
//	 */
//	protected void closeCaveEditor() {
//		for (CaveEditorListener l : listenerList) {
//			l.closeRequest();
//		}
//	}
//
//	/**
//	 * Adds a <code>CaveEditorListener</code>
//	 * 
//	 * @param l
//	 */
//	public void addCaveEditorListener(CaveEditorListener l) {
//		listenerList.add(l);
//	}

	/**
	 * Will be called when the save button is selected. After saving <code>CaveEditorListener.closeRequest()</code> is called to inform all
	 * listener.
	 */
	protected void saveEntries() {
		if (!validateFields()) {
			return;
		}
		if (correspondingCaveEntry.getOfficialNumber().isEmpty()) {
			officialNumberField.getElement().getStyle().setBackgroundColor("#FFFF00");
			Util.showWarning("Missing information", "Please fill in mandatory cave number!");
			return;
		}
		if (correspondingCaveEntry.getCaveID() > 0) {
			dbService.updateCaveEntry(correspondingCaveEntry, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					Util.showWarning("A problem occurred", "The changes could not be saved!");
				}

				@Override
				public void onSuccess(Boolean result) {
					closeEditor();
				}
			});
			
		} else { // its 0 and we need to create a new entry
			dbService.insertCaveEntry(correspondingCaveEntry, new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					Util.showWarning("A problem occurred", "The new Cave could not be saved!");
				}

				@Override
				public void onSuccess(Integer result) {
					correspondingCaveEntry.setCaveID(result.intValue());
					closeEditor();
				}
			});
		}
	}

	/**
	 * @return
	 */
	private boolean validateFields() {
		return officialNumberField.validate() && historicNameField.validate() && optionalHistoricNameField.validate();
	}

	/**
	 * 
	 */
	private void activateRegionFilter() {
		regionFilter = new StoreFilter<RegionEntry>() {

			@Override
			public boolean select(Store<RegionEntry> store, RegionEntry parent, RegionEntry item) {
				return (item.getSiteID() == siteSelection.getCurrentValue().getSiteID());
			}
		};
		regionEntryList.addFilter(regionFilter);
		regionEntryList.setEnableFilters(true);
		regionSelection.setEnabled(true);
	}

	/**
	 * 
	 */
	private void activateDistrictFilter() {
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
	
}
