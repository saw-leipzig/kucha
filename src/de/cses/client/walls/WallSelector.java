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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.shared.AntechamberEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.MainChamberEntry;
import de.cses.shared.RearAreaEntry;
import de.cses.shared.WallEntry;

/**
 * @author alingnau
 *
 */
public class WallSelector implements IsWidget {
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private FlowLayoutContainer caveSketchContainer;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;
	private ContentPanel mainPanel = null;
	private SimpleComboBox<WallEntry> wallSelectorSCB;
	private WallProperties wallProps;
	private CaveEntry currentCave;
	private String selectedWallLabel;

//	class WallNames {
//		private int wallID;
//		private String label;
//		/**
//		 * @param fieldName
//		 * @param label
//		 */
//		public WallNames(int wallID, String label) {
//			super();
//			this.wallID = wallID;
//			this.label = label;
//		}
//		public int getWallID() {
//			return wallID;
//		}
//		public String getLabel() {
//			return label;
//		}
//	}
	
	interface CaveLayoutViewTemplates extends XTemplates {
		@XTemplate("<img align=\"center\" margin=\"10\" src=\"{imageUri}\">")
		SafeHtml image(SafeUri imageUri);
	}
	
	interface WallProperties extends PropertyAccess<WallEntry> {
		ModelKeyProvider<WallEntry> uniqueID();
		LabelProvider<WallEntry> locationLabel();
	}

	/**
	 * 
	 */
	public WallSelector() {
		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		wallProps = GWT.create(WallProperties.class);
	}

	/* (non-Javadoc)
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
		
		wallSelectorSCB = new SimpleComboBox<WallEntry>(wallProps.locationLabel());
		wallSelectorSCB.setEditable(false);
		wallSelectorSCB.setTypeAhead(false);
		wallSelectorSCB.setTriggerAction(TriggerAction.ALL);
		wallSelectorSCB.addSelectionHandler(new SelectionHandler<WallEntry>() {
			
			@Override
			public void onSelection(SelectionEvent<WallEntry> event) {
				Info.display("Wall Selection", "WallID = " + event.getSelectedItem().getLocationLabel());
				selectedWallLabel = event.getSelectedItem().getLocationLabel();
			}
		});
		vlc.add(wallSelectorSCB, new VerticalLayoutData(1.0, 0.2));
		
		mainPanel.add(vlc);
		mainPanel.setSize("1.0", "1.0");
	}

	/**
	 * 
	 */
	private void refreshWallSelector() {
		ListStore<WallEntry> store = new ListStore<>(wallProps.uniqueID());
		// Antechamber is not available at in cave types
		if ((currentCave.getCaveTypeID() == 2) || (currentCave.getCaveTypeID() == 4) || (currentCave.getCaveTypeID() == 6))  {
			store.add(currentCave.getWall(WallEntry.ANTECHAMBER_FRONT_WALL));
			store.add(currentCave.getWall(WallEntry.ANTECHAMBER_LEFT_WALL));
			store.add(currentCave.getWall(WallEntry.ANTECHAMBER_RIGHT_WALL));
			store.add(currentCave.getWall(WallEntry.ANTECHAMBER_REAR_WALL));
		}

		// main chamber is always available
		if ((currentCave.getCaveTypeID() == 2) || (currentCave.getCaveTypeID() == 3) || (currentCave.getCaveTypeID() == 4) || (currentCave.getCaveTypeID() == 6))  {
			store.add(currentCave.getWall(WallEntry.MAIN_CHAMBER_FRONT_WALL));
			store.add(currentCave.getWall(WallEntry.MAIN_CHAMBER_LEFT_WALL));
			store.add(currentCave.getWall(WallEntry.MAIN_CHAMBER_RIGHT_WALL));
			store.add(currentCave.getWall(WallEntry.MAIN_CHAMBER_REAR_WALL));
		}
		
		if ((currentCave.getCaveTypeID() == 4) || (currentCave.getCaveTypeID() == 6))  {
			store.add(currentCave.getWall(WallEntry.REAR_AREA_LEFT_CORRIDOR_OUTER_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_LEFT_CORRIDOR_INNER_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_RIGHT_CORRIDOR_INNER_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_RIGHT_CORRIDOR_OUTER_WALL));
		}
		
		if ((currentCave.getCaveTypeID() == 4) || (currentCave.getCaveTypeID() == 6))  {
			store.add(currentCave.getWall(WallEntry.REAR_AREA_INNER_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_OUTER_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_RIGHT_WALL));
			store.add(currentCave.getWall(WallEntry.REAR_AREA_LEFT_WALL));
		}
		
		wallSelectorSCB.setStore(store);
	}

	/**
	 * @param ctEntry
	 */
	private void setCaveType(CaveTypeEntry ctEntry) {
		caveSketchContainer.clear();
		caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(UriUtils.fromString("resource?background=" + ctEntry.getSketchName()))));
		refreshWallSelector();
	}

	/**
	 * @param selectedCave
	 */
	public void setCave(CaveEntry selectedCave) {
		currentCave = selectedCave;
		setCaveType(StaticTables.getInstance().getCaveTypeEntries().get(selectedCave.getCaveTypeID()));
		dbService.getCaveTypebyID(selectedCave.getCaveTypeID(), new AsyncCallback<CaveTypeEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(CaveTypeEntry ctEntry) {
				setCaveType(ctEntry);
			}
		});
	}

	public String getSelectedWallLocationLabel() {
		return selectedWallLabel;
	}
	
}
