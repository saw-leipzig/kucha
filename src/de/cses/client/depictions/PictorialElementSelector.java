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
package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.shared.PictorialElementEntry;

public class PictorialElementSelector implements IsWidget {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TreeStore<PictorialElementEntry> peTreeStore;
	private Tree<PictorialElementEntry, String> pictorialElementTree;
	private int depictionID;
	private ContentPanel mainPanel = null;
	private BorderLayoutContainer mainBLC = null;
	private Map<String, PictorialElementEntry> selectedPictorialElementsMap;
	private StoreFilterField<PictorialElementEntry> filterField;

	class PictorialElementKeyProvider implements ModelKeyProvider<PictorialElementEntry> {
		@Override
		public String getKey(PictorialElementEntry item) {
			return item.getUniqueID();
		}
	}

	@Deprecated
	class PictorialElementValueProvider implements ValueProvider<PictorialElementEntry, String> {

		@Override
		public String getValue(PictorialElementEntry object) {
			return object.getText();
		}

		@Override
		public void setValue(PictorialElementEntry object, String value) {
			object.setText(value);
		}

		@Override
		public String getPath() {
			return "name";
		}
	}

	public PictorialElementSelector(int depictionID) {
		this.depictionID = depictionID;
		peTreeStore = new TreeStore<PictorialElementEntry>(new PictorialElementKeyProvider());
		selectedPictorialElementsMap = new HashMap<String, PictorialElementEntry>();
		loadPictorialElementsStore();
	}

	private void processParent(TreeStore<PictorialElementEntry> store, PictorialElementEntry item) {
		for (PictorialElementEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void loadPictorialElementsStore() {
//		for (PictorialElementEntry item : StaticTables.getInstance().getPictorialElementEntries().values()) {
//			peTreeStore.add(item);
//			if (item.getChildren() != null) {
//				processParent(peTreeStore, item);
//			}
//		}
//		dbService.getRelatedPE(depictionID, new AsyncCallback<ArrayList<PictorialElementEntry>>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ArrayList<PictorialElementEntry> peRelationList) {
//				for (PictorialElementEntry peEntry : peRelationList) {
//					pictorialElementTree.setChecked(peEntry, CheckState.CHECKED);
//					selectedPictorialElementsMap.put(peEntry.getUniqueID(), peEntry);
//				}
//			}
//		});
	}

	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}

	private void initPanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();

		pictorialElementTree = new Tree<PictorialElementEntry, String>(peTreeStore, new PictorialElementValueProvider()) {

			@Override
			protected void onFilter(StoreFilterEvent<PictorialElementEntry> se) {
				super.onFilter(se);
				for (PictorialElementEntry peEntry : selectedPictorialElementsMap.values()) {
					pictorialElementTree.setChecked(peEntry, CheckState.CHECKED);
				}
			}

		};
		pictorialElementTree.setWidth(350);
		pictorialElementTree.setCheckable(true);
		pictorialElementTree.setAutoLoad(true);
		pictorialElementTree.setCheckStyle(CheckCascade.NONE);

		pictorialElementTree.addCheckChangeHandler(new CheckChangeHandler<PictorialElementEntry>() {

			@Override
			public void onCheckChange(CheckChangeEvent<PictorialElementEntry> event) {
				PictorialElementEntry pe = event.getItem();
				if (event.getChecked() == CheckState.CHECKED) {
					if (!selectedPictorialElementsMap.containsKey(pe.getUniqueID())) {
						selectedPictorialElementsMap.put(pe.getUniqueID(), pe);
					}
				} else {
					selectedPictorialElementsMap.remove(pe.getUniqueID());
				}
			}
		});

		vlc.add(pictorialElementTree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.setPixelSize(700, 475);
		vlc.setBorders(true);

		ContentPanel peTreePanel = new ContentPanel();
		peTreePanel.setHeaderVisible(false);
		peTreePanel.add(vlc);

		filterField = new StoreFilterField<PictorialElementEntry>() {

			@Override
			protected boolean doSelect(Store<PictorialElementEntry> store, PictorialElementEntry parent, PictorialElementEntry item,
					String filter) {
				TreeStore<PictorialElementEntry> treeStore = (TreeStore<PictorialElementEntry>) store;
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
		filterField.bind(peTreeStore);
		
		mainBLC = new BorderLayoutContainer();
		mainBLC.setCenterWidget(peTreePanel, new MarginData(0, 0, 5, 0));
		mainBLC.setSouthWidget(filterField, new BorderLayoutData(25.0));

		ToolButton pictorialElementExpandTB = new ToolButton(ToolButton.EXPAND);
		pictorialElementExpandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				pictorialElementTree.expandAll();
			}
		});

		ToolButton pictorialElementCollapseTB = new ToolButton(ToolButton.COLLAPSE);
		pictorialElementCollapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				pictorialElementTree.collapseAll();
			}
		});

		mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(true);
		mainPanel.setHeading("Pictorial Element Selector");
		mainPanel.add(mainBLC);
		mainPanel.addTool(pictorialElementExpandTB);
		mainPanel.addTool(pictorialElementCollapseTB);
	}

	public List<PictorialElementEntry> getSelectedPE() {
		filterField.clear();
		filterField.validate();
		return pictorialElementTree.getCheckedSelection();
	}

//	public void expandAll() {
//		pictorialElementTree.expandAll();
//	}
//
//	public void collapseAll() {
//		pictorialElementTree.collapseAll();
//	}
//
}
