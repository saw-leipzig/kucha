/*
 * Copyright 2016 - 2018
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
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.shared.IconographyEntry;

public class IconographySelector implements IsWidget {

	class IconographyKeyProvider implements ModelKeyProvider<IconographyEntry> {
		@Override
		public String getKey(IconographyEntry item) {
			return Integer.toString(item.getIconographyID());
		}
	}

	class IconographyValueProvider implements ValueProvider<IconographyEntry, String> {

		@Override
		public String getValue(IconographyEntry object) {
			return object.getText();
		}

		@Override
		public void setValue(IconographyEntry object, String value) {
			// TODO Auto-generated method stub

		}

		@Override
		public String getPath() {
			return "name";
		}
	}

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TreeStore<IconographyEntry> iconographyTreeStore;
	private Tree<IconographyEntry, String> iconographyTree;
	private FramedPanel mainPanel;
	private VerticalLayoutContainer vlc;
	private StoreFilterField<IconographyEntry> filterField;
	private int depictionID;
	protected Map<String, IconographyEntry> selectedIconographyMap;

	public IconographySelector(int depictionID) {
		this.depictionID = depictionID;
		iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		selectedIconographyMap = new HashMap<String, IconographyEntry>();
		loadIconographyStore();
	}

	private void processParentIconographyEntry(TreeStore<IconographyEntry> store, IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry(store, child);
			}
		}
	}

	private void loadIconographyStore() {
		for (IconographyEntry item : StaticTables.getInstance().getIconographyEntries().values()) {
			iconographyTreeStore.add(item);
			if (item.getChildren() != null) {
				processParentIconographyEntry(iconographyTreeStore, item);
			}
		}
		dbService.getRelatedIconography(depictionID, new AsyncCallback<ArrayList<IconographyEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<IconographyEntry> iconographyRelationList) {
				for (IconographyEntry entry : iconographyRelationList) {
					iconographyTree.setChecked(entry, CheckState.CHECKED);
					selectedIconographyMap.put(entry.getUniqueID(), entry);
				}
			}
		});
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		vlc = new VerticalLayoutContainer();

		iconographyTree = new Tree<IconographyEntry, String>(iconographyTreeStore, new IconographyValueProvider()) {

			@Override
			protected void onFilter(StoreFilterEvent<IconographyEntry> ie) {
				super.onFilter(ie);
				for (IconographyEntry entry : selectedIconographyMap.values()) {
					iconographyTree.setChecked(entry, CheckState.CHECKED);
				}
			}

		};
		
		iconographyTree.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		iconographyTree.setCheckable(true);
		iconographyTree.setAutoLoad(true);
		iconographyTree.setCheckStyle(CheckCascade.NONE);
		iconographyTree.setCheckNodes(CheckNodes.LEAF);
		
		iconographyTree.addCheckChangeHandler(new CheckChangeHandler<IconographyEntry>() {
			
			@Override
			public void onCheckChange(CheckChangeEvent<IconographyEntry> event) {
				IconographyEntry ie = event.getItem();
				if (event.getChecked() == CheckState.CHECKED) {
					if (!selectedIconographyMap.containsKey(ie.getUniqueID())) {
						selectedIconographyMap.put(ie.getUniqueID(), ie);
					}
				} else {
					selectedIconographyMap.remove(ie.getUniqueID());
				}
			}
		});
				
//		iconographyTree.setWidth(350);
		vlc.add(iconographyTree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);

		ContentPanel treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);

		filterField = new StoreFilterField<IconographyEntry>() {

			@Override
			protected boolean doSelect(Store<IconographyEntry> store, IconographyEntry parent, IconographyEntry item, String filter) {
				TreeStore<IconographyEntry> treeStore = (TreeStore<IconographyEntry>) store;
				do {
					String name = item.getText().toLowerCase();
					if (name.contains(filter.toLowerCase())) {
						return true;
					}
					item = treeStore.getParent(item);
				} while (item != null);
				return false;
			}
		};
		filterField.bind(iconographyTreeStore);

		BorderLayoutContainer iconographySelectorBLC = new BorderLayoutContainer();
		iconographySelectorBLC.setCenterWidget(treePanel, new MarginData(0, 10, 5, 10));
		iconographySelectorBLC.setSouthWidget(filterField, new BorderLayoutData(25.0));

		ToolButton iconographyExpandTB = new ToolButton(ToolButton.EXPAND);
		iconographyExpandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.expandAll();
			}
		});

		ToolButton iconographyCollapseTB = new ToolButton(ToolButton.COLLAPSE);
		iconographyCollapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.collapseAll();
			}
		});

		mainPanel = new FramedPanel();
		mainPanel.setHeading("Iconography Selector");
		mainPanel.add(iconographySelectorBLC);
		mainPanel.addTool(iconographyExpandTB);
		mainPanel.addTool(iconographyCollapseTB);
	}

	public ArrayList<IconographyEntry> getSelectedIconography() {
		filterField.clear();
		filterField.validate();
		ArrayList<IconographyEntry> result = new ArrayList<IconographyEntry>();
		for (IconographyEntry entry : iconographyTree.getCheckedSelection()) {
			result.add(entry);
		}
		return result;	
	}


}
