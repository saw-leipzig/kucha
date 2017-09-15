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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.tree.Tree;

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
	private TreeStore<IconographyEntry> store;
	private Tree<IconographyEntry, String> tree;
	private FramedPanel mainPanel;
	private VerticalLayoutContainer vlc;
	private ArrayList<IconographySelectorListener> listenerList;
	private int selectedIconographyID;
	private StoreFilter<IconographyEntry> iconographyFilter;
	private TextField filterTextField;

	public IconographySelector(int selectedIconographyID, IconographySelectorListener listener) {
		this.selectedIconographyID = selectedIconographyID;
		store = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		listenerList = new ArrayList<IconographySelectorListener>();
		listenerList.add(listener);
		iconographyFilter = new StoreFilter<IconographyEntry>() {

			@Override
			public boolean select(Store<IconographyEntry> store, IconographyEntry parent, IconographyEntry item) {
				return (item.getText().contains(filterTextField.getCurrentValue()));
			}
		};
		initPanel();
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
			if (item.getIconographyID() == selectedIconographyID) {
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
		mainPanel.setHeading("Iconography Selector");

		vlc = new VerticalLayoutContainer();

		tree = new Tree<IconographyEntry, String>(store, new IconographyValueProvider());
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.setWidth(350);
		vlc.add(tree, new VerticalLayoutData(1.0, 1.0));
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.setPixelSize(700, 475);
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

		TextButton iconographyExpandButton = new TextButton("expand tree");
		iconographyExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.expandAll();
			}
		});
		mainPanel.addButton(iconographyExpandButton);

		TextButton iconographyCollapseButton = new TextButton("collapse tree");
		iconographyCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				tree.collapseAll();
			}
		});
		mainPanel.addButton(iconographyCollapseButton);

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				for (IconographySelectorListener l : listenerList) {
					l.cancel();
				}
			}
		});
		mainPanel.addButton(cancelButton);

		TextButton selectButton = new TextButton("select");
		selectButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				for (IconographySelectorListener l : listenerList) {
					l.iconographySelected(getSelectedIconography());
				}
			}
		});
		mainPanel.addButton(selectButton);
	}

	private IconographyEntry getSelectedIconography() {
		return tree.getSelectionModel().getSelectedItem();
	}

}
