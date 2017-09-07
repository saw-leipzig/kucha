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
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
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

	public IconographySelector(int selectedIconographyID, IconographySelectorListener listener) {
		this.selectedIconographyID = selectedIconographyID;
		store = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		listenerList = new ArrayList<IconographySelectorListener>();
		listenerList.add(listener);
		initPanel();
		refreshIconographyStore();
	}

	private void processParent(TreeStore<IconographyEntry> store, IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void refreshIconographyStore() {
		store.clear();
		dbService.getIconography(new AsyncCallback<ArrayList<IconographyEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				tree = null;
			}

			@Override
			public void onSuccess(ArrayList<IconographyEntry> result) {

				IconographyEntry selectedEntry=null;
				for (IconographyEntry item : result) {
					store.add(item);
					if (item.getIconographyID() == selectedIconographyID) {
						selectedEntry = item;
					}
					if (item.getChildren() != null) {
						processParent(store, item);
					}
				}
				if (selectedEntry!=null) {
					tree.getSelectionModel().select(selectedEntry, false);
					tree.expandAll();
					tree.scrollIntoView(selectedEntry);
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
		tree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tree.setWidth(350);
		
		vlc.add(tree, new VerticalLayoutData(1, 1));
		vlc.setScrollMode(ScrollMode.AUTOY);
		vlc.setPixelSize(700, 450);
		ContentPanel treePanel = new ContentPanel();
		treePanel.setHeaderVisible(false);
		treePanel.add(vlc);
		
		mainPanel.add(treePanel);
		
		TextButton iconographyExpandButton = new TextButton("expand tree");
		iconographyExpandButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				expandAll();
			}
		});
		mainPanel.addButton(iconographyExpandButton);
		
		TextButton iconographyCollapseButton = new TextButton("collapse tree");
		iconographyCollapseButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				collapseAll();
			}
		});
		mainPanel.addButton(iconographyCollapseButton);

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				for (IconographySelectorListener l : listenerList) {
					l.iconographySelected(getSelectedIconography());
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

	public void expandAll() {
		tree.expandAll();
	}

	public void collapseAll() {
		tree.collapseAll();
	}

}
