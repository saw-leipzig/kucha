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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.Util;
import de.cses.shared.IconographyEntry;

public class IconographySelector extends FramedPanel {

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
			object.setText(value);
		}

		@Override
		public String getPath() {
			return "name";
		}
	}

	private TreeStore<IconographyEntry> iconographyTreeStore;
	private Tree<IconographyEntry, String> iconographyTree;
//	private FramedPanel mainPanel = null;
	private StoreFilterField<IconographyEntry> filterField;
	protected Map<String, IconographyEntry> selectedIconographyMap;

	public IconographySelector(Collection<IconographyEntry> elements) {
		iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
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
		selectedIconographyMap = new HashMap<String, IconographyEntry>();
		setIconographyStore(elements);
		initPanel();
	}

	private void processParentIconographyEntry(TreeStore<IconographyEntry> store, IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry(store, child);
			}
		}
	}
	
	private void setIconographyStore(Collection<IconographyEntry> elements) {
		for (IconographyEntry item : elements) {
			iconographyTreeStore.add(item);
			if (item.getChildren() != null) {
				processParentIconographyEntry(iconographyTreeStore, item);
			}
		}
	}

	public void setSelectedIconography(ArrayList<IconographyEntry> iconographyRelationList) {
		Util.doLogging("*** setSelectedIconography called - iconographyTree no. of items = " + iconographyTree.getStore().getAllItemsCount());
		resetSelection();
		for (IconographyEntry entry : iconographyRelationList) {
			Util.doLogging("setSelectedIconography setting entry = " + entry.getIconographyID());
			iconographyTree.setChecked(entry, CheckState.CHECKED);
			selectedIconographyMap.put(entry.getUniqueID(), entry);
		}
	}

	private void initPanel() {
		Util.doLogging("IconographySelector.init() has been called! " + this.getClass().toString());
		
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
		iconographyTree.setCheckNodes(CheckNodes.BOTH);

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
		
		BorderLayoutContainer iconographySelectorBLC = new BorderLayoutContainer();
		iconographySelectorBLC.setCenterWidget(iconographyTree, new MarginData(0, 2, 5, 2));
		iconographySelectorBLC.setSouthWidget(filterField, new BorderLayoutData(25.0));
		
		ToolButton resetTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetTB.setToolTip(Util.createToolTip("Reset selection.", "All selected items will be deselected."));
		resetTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				for (IconographyEntry ie : getSelectedIconography()) {
					iconographyTree.setChecked(ie, CheckState.UNCHECKED);
				}
			}
		});

		ToolButton iconographyExpandTB = new ToolButton(ToolButton.EXPAND);
		iconographyExpandTB.setToolTip(Util.createToolTip("Expand full tree."));
		iconographyExpandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.expandAll();
			}
		});

		ToolButton iconographyCollapseTB = new ToolButton(ToolButton.COLLAPSE);
		iconographyCollapseTB.setToolTip(Util.createToolTip("Collapse tree."));
		iconographyCollapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.collapseAll();
			}
		});

//		mainPanel = new FramedPanel();
		setHeading("Iconography Selector");
		add(iconographySelectorBLC);
		addTool(resetTB);
		addTool(iconographyExpandTB);
		addTool(iconographyCollapseTB);
	}

	public ArrayList<IconographyEntry> getSelectedIconography() {
		filterField.clear();
		filterField.validate();
		ArrayList<IconographyEntry> result = new ArrayList<IconographyEntry>();
		if (iconographyTree != null) {
			for (IconographyEntry entry : iconographyTree.getCheckedSelection()) {
				result.add(entry);
			}
		}
		return result;	
	}
	
	public void resetSelection() {
		selectedIconographyMap.clear();
		if (iconographyTree != null) {
			for (IconographyEntry entry : iconographyTree.getCheckedSelection()) {
				iconographyTree.setChecked(entry, CheckState.UNCHECKED);
			}
		}
	}


}
