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
package de.cses.client.walls;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.StaticTables;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.WallEntry;
import de.cses.shared.WallLocationEntry;

/**
 * @author alingnau
 *
 */
public class WallSelector implements IsWidget {

	private FlowLayoutContainer caveSketchContainer;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;
	private ContentPanel mainPanel = null;
	private ComboBox<WallEntry> wallSelectorCB;
	private WallProperties wallProps;
	private CaveEntry currentCave;
	private WallEntry selectedWallEntry;
	private WallViewTemplate wallVT;
	private ListStore<WallEntry> wallEntryLS;
	private StoreFilter<WallEntry> wallFilter;

	interface CaveLayoutViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"10\" src=\"{imageUri}\">")
		SafeHtml image(SafeUri imageUri);
	}

	interface WallProperties extends PropertyAccess<WallEntry> {
		ModelKeyProvider<WallEntry> wallLocationID();
		LabelProvider<WallEntry> locationLabel();
	}

	interface WallViewTemplate extends XTemplates {
		@XTemplate("<div>{label}</div>")
		SafeHtml wallLabel(String label);
	}

	/**
	 * 
	 */
	public WallSelector() {
		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		wallProps = GWT.create(WallProperties.class);
		wallVT = GWT.create(WallViewTemplate.class);
		wallEntryLS = new ListStore<WallEntry>(wallProps.wallLocationID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			init();
		}
		return mainPanel;
	}

	/**
	 * 
	 */
	private void init() {
		mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		caveSketchContainer = new FlowLayoutContainer();
		vlc.add(caveSketchContainer, new VerticalLayoutData(1.0, 0.8));

		wallSelectorCB = new ComboBox<WallEntry>(wallEntryLS, wallProps.locationLabel(), new AbstractSafeHtmlRenderer<WallEntry>() {

			@Override
			public SafeHtml render(WallEntry entry) {
				return wallVT.wallLabel(StaticTables.getInstance().getWallLocationEntries().get(entry.getWallLocationID()).getLabel());
			}
		});
		wallSelectorCB.setEditable(false);
		wallSelectorCB.setTypeAhead(false);
		wallSelectorCB.setTriggerAction(TriggerAction.ALL);
		wallSelectorCB.addSelectionHandler(new SelectionHandler<WallEntry>() {

			private WallEntry selectedWallEntry;

			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				selectedWallEntry = event.getSelectedItem();
			}
		});
		vlc.add(wallSelectorCB, new VerticalLayoutData(1.0, 0.2));

		mainPanel.add(vlc);
		mainPanel.setSize("1.0", "1.0");
	}
	
//	/**
//	 * 
//	 */
//	private void activateWallLocationFilter(int caveTypeID) {
//		wallEntryLS.setEnableFilters(false);
//		wallEntryLS.removeFilters();
//		wallFilter = new StoreFilter<WallEntry>() {
//
//			@Override
//			public boolean select(Store<WallEntry> store, WallEntry parent, WallEntry item) {
//				String caveAreaLabel = StaticTables.getInstance().getWallLocationEntries().get(item.getWallLocationID()).getCaveAreaLabel();
//				switch (caveTypeID) {
//					// 'antechamber','main chamber','main chamber corridor','rear area left corridor','rear area right corridor','rear area'
//					case 2: // square cave
//						return ((caveAreaLabel == WallLocationEntry.ANTECHAMBER_LABEL) || (caveAreaLabel == WallLocationEntry.MAIN_CHAMBER_LABEL));
//						
//					case 3: // resitential cave
//						return ((caveAreaLabel == WallLocationEntry.ANTECHAMBER_LABEL) || (caveAreaLabel == WallLocationEntry.MAIN_CHAMBER_LABEL)
//								|| (caveAreaLabel == WallLocationEntry.MAIN_CHAMBER_CORRIDOR_LABEL) || (caveAreaLabel == WallLocationEntry.REAR_AREA_LABEL));
//
//					case 4: // central-pillar cave
//						return ((caveAreaLabel == WallLocationEntry.ANTECHAMBER_LABEL) || (caveAreaLabel == WallLocationEntry.MAIN_CHAMBER_LABEL)
//								|| (caveAreaLabel == WallLocationEntry.REAR_AREA_LABEL) || (caveAreaLabel == WallLocationEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
//								|| (caveAreaLabel == WallLocationEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL));
//
//					case 6: // monumental image cave
//						return ((caveAreaLabel == WallLocationEntry.ANTECHAMBER_LABEL) || (caveAreaLabel == WallLocationEntry.MAIN_CHAMBER_LABEL)
//								|| (caveAreaLabel == WallLocationEntry.REAR_AREA_LABEL) || (caveAreaLabel == WallLocationEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
//								|| (caveAreaLabel == WallLocationEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL));
//						
//					default:
//						return false;
//				}
//			}
//		};
//		wallEntryLS.addFilter(wallFilter);
//		wallEntryLS.setEnableFilters(true);
//	}
//
	/**
	 * @param selectedCave
	 */
	public void setCave(CaveEntry selectedCave) {
		currentCave = selectedCave;
		CaveTypeEntry ctEntry = StaticTables.getInstance().getCaveTypeEntries().get(selectedCave.getCaveTypeID());
		caveSketchContainer.clear();
		caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(UriUtils
				.fromString("resource?background=" + ctEntry.getSketchName() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()))));
		wallEntryLS.clear();
		switch (ctEntry.getCaveTypeID()) {
			// 'antechamber','main chamber','main chamber corridor','rear area left corridor','rear area right corridor','rear area'
			case 2: // square cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL) || (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)) {
						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
					}
				}
				break;
				
			case 3: // resitential cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL) || (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_CORRIDOR_LABEL) || (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL)) {
						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
					}
				}
				break;

			case 4: // central-pillar cave
			case 6: // monumental image cave
				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL) || (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL) || (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL)) {
						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
					}
				}
				break;

			default:
				break;
		}
	}

	public WallEntry getSelectedWallEntry() {
		return selectedWallEntry;
	}

}
