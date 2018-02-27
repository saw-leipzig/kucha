/*
 * Copyright 2017 
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
package de.cses.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.shared.CaveTypeEntry;
import de.cses.shared.CeilingTypeEntry;
import de.cses.shared.ChamberTypeEntry;
import de.cses.shared.CurrentLocationEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.ExpeditionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.LocationEntry;
import de.cses.shared.ModeOfRepresentationEntry;
import de.cses.shared.OrnamentFunctionEntry;
import de.cses.shared.OrnamentPositionEntry;
import de.cses.shared.PictorialElementEntry;
import de.cses.shared.PreservationClassificationEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;
import de.cses.shared.StyleEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallLocationEntry;
import de.cses.shared.WallOrnamentCaveRelation;

/**
 * @author alingnau
 *
 */
public class StaticTables {
	
	private static StaticTables instance = null;

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	protected HashMap<Integer, SiteEntry> siteEntryMap;
	protected HashMap<Integer, DistrictEntry> districtEntryMap;
	protected HashMap<Integer, RegionEntry> regionEntryMap;
	protected HashMap<Integer, CaveTypeEntry> caveTypeEntryMap;
	protected HashMap<Integer, CeilingTypeEntry> ceilingTypeEntryMap;
	protected HashMap<Integer, PreservationClassificationEntry> preservationClassificationEntryMap;
	protected HashMap<Integer, ImageTypeEntry> imageTypeEntryMap;
	protected HashMap<Integer, ExpeditionEntry> expeditionEntryMap;
	protected HashMap<Integer, StyleEntry> styleEntryMap;
	protected HashMap<Integer, IconographyEntry> iconographyEntryMap;
	protected HashMap<Integer, PictorialElementEntry> pictorialElementEntryMap;
	protected HashMap<Integer, ModeOfRepresentationEntry> modesOfRepresentationEntryMap;
	protected HashMap<Integer, WallLocationEntry> wallLocationEntryMap;
	protected HashMap<Integer, OrnamentPositionEntry> ornamentPositionMap;
	protected HashMap<Integer, OrnamentFunctionEntry> ornamentFunctionMap;
	protected HashMap<Integer, CurrentLocationEntry> currentLocationMap;
	protected HashMap<Integer, LocationEntry> locationMap;
	protected HashMap<Integer, VendorEntry> vendorMap;
	protected HashMap<Integer, PublicationTypeEntry> publicationTypeMap;

	private int loadCounter;

	private ListsLoadedListener listener;


	public interface ListsLoadedListener {
		void listsLoaded(double progressCounter);
	}

	public static synchronized StaticTables getInstance() {
		return instance;
	}

	public static synchronized void createInstance(ListsLoadedListener l) {
		if (instance == null) {
			instance = new StaticTables(l);
		}
	}

	/**
	 * 
	 */
	public StaticTables(ListsLoadedListener l) {
		listener = l;
		loadCounter = 18;
		loadDistricts();
		loadSites();
		loadRegions();
		loadCaveTypes();
		loadCeilingTypes();
		loadPreservationClassification();
		loadImageTypes();
		loadExpeditions();
		loadStyles();
		loadIconography();
		loadPictorialElements();
		loadModesOfRepresentation();
		loadWallLocations();
		loadOrnamentPositionTable();
		loadOrnamentFunctionTable();
		loadLocationMap();
		loadVendor();
		loadPublicationTypes();
	}

	private void listLoaded() {
		--loadCounter;
		listener.listsLoaded((17.0 - loadCounter) / 17.0);
	}

	/**
	 * 
	 */
	private void loadSites() {
		siteEntryMap = new HashMap<Integer, SiteEntry>();
		dbService.getSites(new AsyncCallback<ArrayList<SiteEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<SiteEntry> result) {
				for (SiteEntry se : result) {
					siteEntryMap.put(se.getSiteID(), se);
				}
				listLoaded();
			}
		});
	}

	/**
	 * 
	 */
	private void loadDistricts() {
		districtEntryMap = new HashMap<Integer, DistrictEntry>();
		dbService.getDistricts(new AsyncCallback<ArrayList<DistrictEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<DistrictEntry> result) {
				for (DistrictEntry de : result) {
					districtEntryMap.put(de.getDistrictID(), de);
				}
				listLoaded();
			}
		});
	}

	/* 
	 * 
	 */
	private void loadRegions() {
		regionEntryMap = new HashMap<Integer, RegionEntry>();
		dbService.getRegions(new AsyncCallback<ArrayList<RegionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<RegionEntry> result) {
				for (RegionEntry re : result) {
					regionEntryMap.put(re.getRegionID(), re);
				}
				listLoaded();
			}
		});
	}

	/**
	 * 
	 */
	private void loadCaveTypes() {
		caveTypeEntryMap = new HashMap<Integer, CaveTypeEntry>();
		dbService.getCaveTypes(new AsyncCallback<ArrayList<CaveTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CaveTypeEntry> result) {
				for (CaveTypeEntry cte : result) {
					caveTypeEntryMap.put(cte.getCaveTypeID(), cte);
				}
				listLoaded();
			}
		});
	}

	private void loadCeilingTypes() {
		ceilingTypeEntryMap = new HashMap<Integer, CeilingTypeEntry>();
		dbService.getCeilingTypes(new AsyncCallback<ArrayList<CeilingTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<CeilingTypeEntry> result) {
				for (CeilingTypeEntry cte : result) {
					ceilingTypeEntryMap.put(cte.getCeilingTypeID(), cte);
				}
				listLoaded();
			}
		});
	}

	private void loadPreservationClassification() {
		preservationClassificationEntryMap = new HashMap<Integer, PreservationClassificationEntry>();
		dbService.getPreservationClassifications(new AsyncCallback<ArrayList<PreservationClassificationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PreservationClassificationEntry> result) {
				for (PreservationClassificationEntry pce : result) {
					preservationClassificationEntryMap.put(pce.getPreservationClassificationID(), pce);
				}
				listLoaded();
			}
		});
	}

	private void loadImageTypes() {
		imageTypeEntryMap = new HashMap<Integer, ImageTypeEntry>();
		dbService.getImageTypes(new AsyncCallback<ArrayList<ImageTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<ImageTypeEntry> result) {
				for (ImageTypeEntry ite : result) {
					imageTypeEntryMap.put(ite.getImageTypeID(), ite);
				}
				listLoaded();
			}
		});
	}

	/**
	 * 
	 */
	private void loadExpeditions() {
		expeditionEntryMap = new HashMap<Integer, ExpeditionEntry>();
		dbService.getExpeditions(new AsyncCallback<ArrayList<ExpeditionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<ExpeditionEntry> expedResults) {
				for (ExpeditionEntry exped : expedResults) {
					expeditionEntryMap.put(exped.getExpeditionID(), exped);
				}
				listLoaded();
			}
		});
	}

	/**
	 * 
	 */
	private void loadStyles() {
		styleEntryMap = new HashMap<Integer, StyleEntry>();
		dbService.getStyles(new AsyncCallback<ArrayList<StyleEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<StyleEntry> styleResults) {
				for (StyleEntry se : styleResults) {
					styleEntryMap.put(se.getStyleID(), se);
				}
				listLoaded();
			}
		});
	}

	private void loadIconography() {
		iconographyEntryMap = new HashMap<Integer, IconographyEntry>();
		dbService.getIconography(new AsyncCallback<ArrayList<IconographyEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<IconographyEntry> result) {
				for (IconographyEntry ie : result) {
					iconographyEntryMap.put(ie.getIconographyID(), ie);
				}
				listLoaded();
			}
		});
	}

	private void loadPictorialElements() {
		pictorialElementEntryMap = new HashMap<Integer, PictorialElementEntry>();
		dbService.getPictorialElements(new AsyncCallback<ArrayList<PictorialElementEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<PictorialElementEntry> peList) {
				for (PictorialElementEntry item : peList) {
					pictorialElementEntryMap.put(item.getPictorialElementID(), item);
				}
				listLoaded();
			}
		});
	}
	
	private void loadModesOfRepresentation() {
		modesOfRepresentationEntryMap = new HashMap<Integer, ModeOfRepresentationEntry>();
		dbService.getModesOfRepresentation(new AsyncCallback<ArrayList<ModeOfRepresentationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<ModeOfRepresentationEntry> result) {
				for (ModeOfRepresentationEntry mor : result) {
					modesOfRepresentationEntryMap.put(mor.getModeOfRepresentationID(), mor);
				}
				listLoaded();
			}
		});
	}
	
	private void loadWallLocations() {
		wallLocationEntryMap = new HashMap<Integer, WallLocationEntry>();
		dbService.getWallLocations(new AsyncCallback<ArrayList<WallLocationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<WallLocationEntry> result) {
				for (WallLocationEntry wle : result) {
					wallLocationEntryMap.put(wle.getWallLocationID(), wle);
				}
				listLoaded();
			}
		});
	}
	
	private void loadOrnamentPositionTable() {
		ornamentPositionMap = new HashMap<Integer, OrnamentPositionEntry>();
		dbService.getOrnamentPositions(new AsyncCallback<ArrayList<OrnamentPositionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				listLoaded();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentPositionEntry> result) {
				for (OrnamentPositionEntry ope : result) {
					ornamentPositionMap.put(ope.getOrnamentPositionID(), ope);
				}
				listLoaded();
			}
		});
	}
	
	private void loadOrnamentFunctionTable() {
		ornamentFunctionMap = new HashMap<Integer, OrnamentFunctionEntry>();
		dbService.getOrnamentFunctions(new AsyncCallback<ArrayList<OrnamentFunctionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<OrnamentFunctionEntry> result) {
				for (OrnamentFunctionEntry ofe : result) {
					ornamentFunctionMap.put(ofe.getOrnamentFunctionID(), ofe);
				}
				listLoaded();
			}
		});
	}
	
	private void loadLocationMap() {
		locationMap = new HashMap<Integer, LocationEntry>();
		dbService.getLocations(new AsyncCallback<ArrayList<LocationEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<LocationEntry> result) {
				for (LocationEntry entry : result) {
					locationMap.put(entry.getLocationID(), entry);
				}
				listLoaded();
			}
		});
	}
	
	private void loadVendor() {
		vendorMap = new HashMap<Integer, VendorEntry>();
		dbService.getVendors(new AsyncCallback<ArrayList<VendorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<VendorEntry> results) {
				for (VendorEntry entry : results) {
					vendorMap.put(entry.getVendorID(), entry);
				}
				listLoaded();
			}
		});
	}
	
	private void loadPublicationTypes() {
		publicationTypeMap = new HashMap<Integer, PublicationTypeEntry>();
		dbService.getPublicationTypes(new AsyncCallback<ArrayList<PublicationTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PublicationTypeEntry> result) {
				for (PublicationTypeEntry pte : result) {
					publicationTypeMap.put(pte.getPublicationTypeID(), pte);
				}
				listLoaded();
			}
		});
		
	}

	public Map<Integer, DistrictEntry> getDistrictEntries() {
		return districtEntryMap;
	}

	public Map<Integer, SiteEntry> getSiteEntries() {
		return siteEntryMap;
	}

	public Map<Integer, RegionEntry> getRegionEntries() {
		return regionEntryMap;
	}

	public Map<Integer, CaveTypeEntry> getCaveTypeEntries() {
		return caveTypeEntryMap;
	}

	public Map<Integer, CeilingTypeEntry> getCeilingTypeEntries() {
		return ceilingTypeEntryMap;
	}

	public Map<Integer, PreservationClassificationEntry> getPreservationClassificationEntries() {
		return preservationClassificationEntryMap;
	}

	public Map<Integer, ImageTypeEntry> getImageTypeEntries() {
		return imageTypeEntryMap;
	}

	public Map<Integer, ExpeditionEntry> getExpeditionEntries() {
		return expeditionEntryMap;
	}

	public Map<Integer, StyleEntry> getStyleEntries() {
		return styleEntryMap;
	}

	public Map<Integer, IconographyEntry> getIconographyEntries() {
		return iconographyEntryMap;
	}

	public Map<Integer, PictorialElementEntry> getPictorialElementEntries() {
		return pictorialElementEntryMap;
	}
	
	public Map<Integer, ModeOfRepresentationEntry> getModesOfRepresentationEntries() {
		return modesOfRepresentationEntryMap;
	}
	
	public Map<Integer, WallLocationEntry> getWallLocationEntries() {
		return wallLocationEntryMap;
	}
	
	public Map<Integer, OrnamentPositionEntry> getOrnamentPositionEntries() {
		return ornamentPositionMap;
	}
	
	public Map<Integer, OrnamentFunctionEntry> getOrmanemtFunctionEntries() {
		return ornamentFunctionMap;
	}

	public Map<Integer, CurrentLocationEntry> getCurrentLocationEntries() {
		return currentLocationMap;
	}

	public Map<Integer, LocationEntry> getLocationEntries() {
		return locationMap;
	}
	
	public Map<Integer, VendorEntry> getVendorEntries() {
		return vendorMap;
	}
	
	public Map<Integer, PublicationTypeEntry> getPublicationTypes() {
		return publicationTypeMap;
	}
	
}
