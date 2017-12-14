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
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
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
	private TreeStore<PictorialElementEntry> peStore;
	private Tree<PictorialElementEntry, String> tree;
	private int depictionID;
	private ContentPanel treePanel;
	private VerticalLayoutContainer mainVLC = null;
	private Map<String, PictorialElementEntry> selectedPictorialElementsMap;
	private StoreFilterField<PictorialElementEntry> filterField;

	class PictorialElementKeyProvider implements ModelKeyProvider<PictorialElementEntry> {
		@Override
		public String getKey(PictorialElementEntry item) {
			return item.getUniqueID();
		}
	}

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
		peStore = new TreeStore<PictorialElementEntry>(new PictorialElementKeyProvider());
		selectedPictorialElementsMap = new HashMap<String, PictorialElementEntry>();
		loadPEStore();
	}

	private void processParent(TreeStore<PictorialElementEntry> store, PictorialElementEntry item) {
		for (PictorialElementEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void loadPEStore() {
		for (PictorialElementEntry item : StaticTables.getInstance().getPictorialElementEntries().values()) {
			peStore.add(item);
			if (item.getChildren() != null) {
				processParent(peStore, item);
			}
		}
		dbService.getRelatedPE(depictionID, new AsyncCallback<ArrayList<PictorialElementEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PictorialElementEntry> peRelationList) {
				for (PictorialElementEntry peEntry : peRelationList) {
					tree.setChecked(peEntry, CheckState.CHECKED);
					selectedPictorialElementsMap.put(peEntry.getUniqueID(), peEntry);
				}
			}
		});
	}

	@Override
	public Widget asWidget() {
		if (mainVLC == null) {
			initPanel();
		}
		return mainVLC;
	}

	private void initPanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();

		tree = new Tree<PictorialElementEntry, String>(peStore, new PictorialElementValueProvider()) {

			@Override
			protected void onFilter(StoreFilterEvent<PictorialElementEntry> se) {
				super.onFilter(se);
				for (PictorialElementEntry peEntry : selectedPictorialElementsMap.values()) {
					// if (tree.getStore().findModel(peEntry) != null) {
					tree.setChecked(peEntry, CheckState.CHECKED);
					// }
				}
			}

		};
		tree.setWidth(350);
		tree.setCheckable(true);
		tree.setAutoLoad(true);
		tree.setCheckStyle(CheckCascade.NONE);

		tree.addCheckChangeHandler(new CheckChangeHandler<PictorialElementEntry>() {

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

		vlc.add(tree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.setPixelSize(700, 475);
		vlc.setBorders(true);

		treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);

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
		filterField.bind(peStore);

		mainVLC = new VerticalLayoutContainer();
		mainVLC.add(treePanel, new VerticalLayoutData(1.0, .85));
		mainVLC.add(filterField, new VerticalLayoutData(.5, .15, new Margins(10, 0, 0, 0)));

	}

	public List<PictorialElementEntry> getSelectedPE() {
		filterField.clear();
		filterField.validate();
		return tree.getCheckedSelection();
	}

	public void expandAll() {
		tree.expandAll();
	}

	public void collapseAll() {
		tree.collapseAll();
	}

}
