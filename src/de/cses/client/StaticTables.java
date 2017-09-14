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

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.shared.DistrictEntry;
import de.cses.shared.SiteEntry;

/**
 * @author alingnau
 *
 */
public class StaticTables {
	
	private static StaticTables instance = null;

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	
	private ListStore<SiteEntry> siteEntryList;
	private ListStore<DistrictEntry> districtEntryList;
	private SiteProperties siteProps;
	private DistrictProperties districtProps;
	
	interface SiteProperties extends PropertyAccess<SiteEntry> {
		ModelKeyProvider<SiteEntry> siteID();

		LabelProvider<SiteEntry> name();
	}

	interface DistrictProperties extends PropertyAccess<DistrictEntry> {
		ModelKeyProvider<DistrictEntry> districtID();
		LabelProvider<DistrictEntry> name();
	}

	public static synchronized StaticTables getInstance() {
		if (instance == null) {
			instance = new StaticTables();
		}
		return instance;
	}

	/**
	 * 
	 */
	private StaticTables() {
		siteProps = GWT.create(SiteProperties.class);
		siteEntryList = new ListStore<SiteEntry>(siteProps.siteID());
		districtProps = GWT.create(DistrictProperties.class);
		districtEntryList = new ListStore<DistrictEntry>(districtProps.districtID());
		
		loadDistricts();
		loadSites();
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
			}
		});
	}

	public ListStore<DistrictEntry> getDistrictEntryList() {
		return districtEntryList;
	}

	public ListStore<SiteEntry> getSiteEntryList() {
		return siteEntryList;
	}

	
}
