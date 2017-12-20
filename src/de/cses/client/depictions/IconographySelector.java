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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
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
import de.cses.shared.PictorialElementEntry;

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
	private TreeStore<IconographyEntry> store;
	private Tree<IconographyEntry, String> tree;
	private FramedPanel mainPanel;
	private VerticalLayoutContainer vlc;
	private StoreFilterField<PictorialElementEntry> filterField;
	private int depictionID;
	protected Map<String, IconographyEntry> selectedIconographyMap;

	public IconographySelector(int depictionID) {
		this.depictionID = depictionID;
		store = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		selectedIconographyMap = new HashMap<String, IconographyEntry>();
//		initPanel();
		loadIconographyStore();
	}

	private void processParent(TreeStore<IconographyEntry> store, IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void loadIconographyStore() {
		IconographyEntry selectedEntry = null;
		for (IconographyEntry item : StaticTables.getInstance().getIconographyEntries().values()) {
			store.add(item);
			if (item.getChildren() != null) {
				processParent(store, item);
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
					tree.setChecked(entry, CheckState.CHECKED);
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
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Iconography Selector");

		vlc = new VerticalLayoutContainer();

		tree = new Tree<IconographyEntry, String>(store, new IconographyValueProvider());
		tree.getSelectionModel().setSelectionMode(SelectionMode.MULTI);
		tree.setCheckable(true);
		tree.setCheckStyle(CheckCascade.NONE);
		tree.setCheckNodes(CheckNodes.LEAF);
//		tree.setWidth(350);
		vlc.add(tree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);
//		vlc.setPixelSize(700, 475);
		ContentPanel treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);

		StoreFilterField<IconographyEntry> filterField = new StoreFilterField<IconographyEntry>() {

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
		filterField.bind(store);

		VerticalLayoutContainer mainVLC = new VerticalLayoutContainer();
		mainVLC.add(treePanel, new VerticalLayoutData(1.0, .85));
		mainVLC.add(filterField, new VerticalLayoutData(.5, .15, new Margins(10, 0, 0, 0)));

		mainPanel.add(mainVLC);

		ToolButton iconographyExpandTB = new ToolButton(ToolButton.EXPAND);
		iconographyExpandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.expandAll();
			}
		});
		mainPanel.addTool(iconographyExpandTB);

		ToolButton iconographyCollapseTB = new ToolButton(ToolButton.COLLAPSE);
		iconographyCollapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.collapseAll();
			}
		});
		mainPanel.addTool(iconographyCollapseTB);

	}

	public ArrayList<IconographyEntry> getSelectedIconography() {
		filterField.clear();
		filterField.validate();
		return new ArrayList<IconographyEntry>(tree.getCheckedSelection());	
	}


}
