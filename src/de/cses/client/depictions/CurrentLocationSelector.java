/*
 * Copyright 2016-2018 
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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.shared.CurrentLocationEntry;

public class CurrentLocationSelector implements IsWidget {
	
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

	private TreeStore<CurrentLocationEntry> store;
	private Tree<CurrentLocationEntry, String> tree;
	private FramedPanel mainPanel;
	private VerticalLayoutContainer vlc;

	public CurrentLocationSelector() {
		store = new TreeStore<CurrentLocationEntry>(new CurrentLocationKeyProvider());
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
		for (CurrentLocationEntry item : StaticTables.getInstance().getCurrentLocationEntries().values()) {
			store.add(item);
			if (item.getChildren() != null) {
				processParent(store, item);
			}
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
		tree.setCheckable(false);
		tree.setAutoLoad(true);
		tree.setCheckStyle(CheckCascade.NONE);
		tree.setCheckNodes(CheckNodes.LEAF);
		tree.setCheckable(true);
		vlc.add(tree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);

		ContentPanel treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);

		VerticalLayoutContainer mainVLC = new VerticalLayoutContainer();
		mainVLC.add(treePanel, new VerticalLayoutData(1.0, 1.0));

		
		ToolButton expandTB = new ToolButton(ToolButton.EXPAND);
		expandTB.setToolTip(Util.createToolTip("expand window"));
		expandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.expandAll();
			}
		});

		ToolButton collapseTB = new ToolButton(ToolButton.COLLAPSE);
		collapseTB.setToolTip(Util.createToolTip("collapse window"));
		collapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.collapseAll();
			}
		});

		mainPanel.add(mainVLC);
		mainPanel.addTool(expandTB);
		mainPanel.addTool(collapseTB);

	}
	
	public CurrentLocationEntry getSelectedLocation() {
		return tree.getSelectionModel().getSelectedItem();
	}
	
	public void setSelectedLocation(int selectedLocationID) {
		tree.setChecked(store.findModelWithKey(Integer.toString(selectedLocationID)), CheckState.CHECKED);
	}

}
