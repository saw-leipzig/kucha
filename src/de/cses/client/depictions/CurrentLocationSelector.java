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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.cses.client.StaticTables;
import de.cses.shared.CurrentLocationEntry;

public class CurrentLocationSelector implements IsWidget {
	
	interface CurrentLocationSelectorListener {
		
		public void currentLocationSelected(CurrentLocationEntry entry);
		public void cancel();

	}

	class CurrentLocationKeyProvider implements ModelKeyProvider<CurrentLocationEntry> {
		@Override
		public String getKey(CurrentLocationEntry item) {
			return Integer.toString(item.getCurrentLocationID());
		}
	}

	class CurrentLocationValueProvider implements ValueProvider<CurrentLocationEntry, String> {

		@Override
		public String getValue(CurrentLocationEntry object) {
			return object.getLocationName();
		}

		@Override
		public void setValue(CurrentLocationEntry object, String value) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getPath() {
			return "name";
		}
	}

//	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TreeStore<CurrentLocationEntry> store;
	private Tree<CurrentLocationEntry, String> tree;
	private FramedPanel mainPanel;
	private VerticalLayoutContainer vlc;
	private ArrayList<CurrentLocationSelectorListener> listenerList;
	private int selectedCurrentLocationID;
//	private StoreFilter<CurrentLocationEntry> currentLocationFilter;
//	private TextField filterTextField;

	public CurrentLocationSelector(int selectedCurrentLocationID, CurrentLocationSelectorListener listener) {
		this.selectedCurrentLocationID = selectedCurrentLocationID;
		store = new TreeStore<CurrentLocationEntry>(new CurrentLocationKeyProvider());
		listenerList = new ArrayList<CurrentLocationSelectorListener>();
		listenerList.add(listener);
//		currentLocationFilter = new StoreFilter<CurrentLocationEntry>() {
//
//			@Override
//			public boolean select(Store<CurrentLocationEntry> store, CurrentLocationEntry parent, CurrentLocationEntry item) {
//				return (item.getLocationName().contains(filterTextField.getCurrentValue()));
//			}
//		};
		initPanel();
		loadCurrentLocationStore();
	}

	private void processParent(TreeStore<CurrentLocationEntry> store, CurrentLocationEntry item) {
		for (CurrentLocationEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void loadCurrentLocationStore() {
		CurrentLocationEntry selectedEntry = null;
		for (CurrentLocationEntry item : StaticTables.getInstance().getCurrentLocationEntries().values()) {
			store.add(item);
			if (item.getCurrentLocationID() == selectedCurrentLocationID) {
				selectedEntry = item;
			}
			if (item.getChildren() != null) {
				processParent(store, item);
			}
		}
		if (selectedEntry != null) {
			tree.getSelectionModel().select(selectedEntry, false);
			tree.expandAll();
			tree.scrollIntoView(selectedEntry);
		}
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Current Location");

		vlc = new VerticalLayoutContainer();

		tree = new Tree<CurrentLocationEntry, String>(store, new CurrentLocationValueProvider());
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.setWidth(350);
		vlc.add(tree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.setPixelSize(700, 475);
		ContentPanel treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);

//		StoreFilterField<CurrentLocationEntry> filterField = new StoreFilterField<CurrentLocationEntry>() {
//
//			@Override
//			protected boolean doSelect(Store<CurrentLocationEntry> store, CurrentLocationEntry parent, CurrentLocationEntry item, String filter) {
//				TreeStore<CurrentLocationEntry> treeStore = (TreeStore<CurrentLocationEntry>) store;
//				do {
//					String name = item.getLocationName().toLowerCase();
//					if (name.contains(filter.toLowerCase())) {
//						return true;
//					}
//					item = treeStore.getParent(item);
//				} while (item != null);
//				return false;
//			}
//		};
//		filterField.bind(store);

		VerticalLayoutContainer mainVLC = new VerticalLayoutContainer();
		mainVLC.add(treePanel, new VerticalLayoutData(1.0, 1.0));
//		mainVLC.add(filterField, new VerticalLayoutData(.5, .15, new Margins(10, 0, 0, 0)));

		mainPanel.add(mainVLC);

		TextButton expandButton = new TextButton("expand tree");
		expandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.expandAll();
			}
		});
		mainPanel.addButton(expandButton);

		TextButton collapseButton = new TextButton("collapse tree");
		collapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.collapseAll();
			}
		});

		mainPanel.addButton(collapseButton);

//		TextButton cancelButton = new TextButton("cancel");
//		cancelButton.addSelectHandler(new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				for (CurrentLocationSelectorListener l : listenerList) {
//					l.cancel();
//				}
//			}
//		});
//		mainPanel.addButton(cancelButton);
		
//		TextButton selectButton = new TextButton("select");
//		selectButton.addSelectHandler(new SelectHandler() {
//			@Override
//			public void onSelect(SelectEvent event) {
//				for (CurrentLocationSelectorListener l : listenerList) {
//					l.currentLocationSelected(getSelectedIconography());
//				}
//			}
//		});
//		mainPanel.addButton(selectButton);
	}
	
	public void addSelectionChangedHandler(SelectionChangedHandler<CurrentLocationEntry> handler) {
		tree.getSelectionModel().addSelectionChangedHandler(handler);
	}

//	private CurrentLocationEntry getSelectedIconography() {
//		return tree.getSelectionModel().getSelectedItem();
//	}

}
