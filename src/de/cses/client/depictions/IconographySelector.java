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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
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
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.IconographyEntry;
import de.cses.shared.UserEntry;
import de.cses.shared.VendorEntry;

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

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

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
		iconographyTreeStore.clear();
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

		ToolButton iconographyExpandTB = new ToolButton(new IconConfig("unfoldButton", "unfoldButtonOver"));
		iconographyExpandTB.setToolTip(Util.createToolTip("Expand full tree."));
		iconographyExpandTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.expandAll();
			}
		});

		ToolButton iconographyCollapseTB = new ToolButton(new IconConfig("foldButton", "foldButtonOver"));
		iconographyCollapseTB.setToolTip(Util.createToolTip("Collapse tree."));
		iconographyCollapseTB.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				iconographyTree.collapseAll();
			}
		});
		
		ToolButton addEntryTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addEntryTB.setToolTip(Util.createToolTip("Add new entry to tree.", "Select parent entry first (selection indicated by shade) and click here."));
		addEntryTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (iconographyTree.getSelectionModel().getSelectedItem() == null) { // we can only add a new entry if there is a parent selected
					return;
				}
				PopupPanel addIconographyEntryDialog = new PopupPanel();
				FramedPanel newIconographyEntryFP = new FramedPanel();
				HTML html = new HTML(iconographyTree.getSelectionModel().getSelectedItem().getText());
				html.setWidth("100%");
				html.setWordWrap(true);
				html.setStylePrimaryName("html-display");
				html.setWidth("280px");
				TextArea ieTextArea = new TextArea();
				ieTextArea.addValidator(new MinLengthValidator(2));
				ieTextArea.addValidator(new MaxLengthValidator(256));
				VerticalLayoutContainer newIconogryphyVLC = new VerticalLayoutContainer();
				newIconogryphyVLC.add(html, new VerticalLayoutData(1.0, .5));
				newIconogryphyVLC.add(ieTextArea, new VerticalLayoutData(1.0, .5));
				newIconographyEntryFP.add(newIconogryphyVLC);
				newIconographyEntryFP.setHeading("add child element to");
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (ieTextArea.isValid()) {
							IconographyEntry iconographyEntry = new IconographyEntry(0, iconographyTree.getSelectionModel().getSelectedItem().getIconographyID(), ieTextArea.getValue());
							dbService.insertIconographyEntry(iconographyEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Integer result) {
									if (result > 0) { // otherwise there has been a problem adding the entry
										iconographyEntry.setIconographyID(result);
										StaticTables.getInstance().reloadIconography(); // we need to reload the whole tree otherwise this won't work
										addChildIconographyEntry(iconographyTreeStore, iconographyEntry);
									}
								}
							});
							addIconographyEntryDialog.hide();
						}
					}
				});
				newIconographyEntryFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addIconographyEntryDialog.hide();
					}
				});
				newIconographyEntryFP.addButton(cancelButton);
				addIconographyEntryDialog.add(newIconographyEntryFP);
				addIconographyEntryDialog.setSize("300px", "250px");
				addIconographyEntryDialog.setModal(true);
				addIconographyEntryDialog.center();
			}
		});

		ToolButton renameEntryTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		renameEntryTB.setToolTip(Util.createToolTip("Edit entry text.", "Select entry first (selection indicated by shade) and click here."));
		renameEntryTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (iconographyTree.getSelectionModel().getSelectedItem() == null) { // we can only add a new entry if there is a parent selected
					return;
				}
				IconographyEntry iconographyEntryToEdit = iconographyTree.getSelectionModel().getSelectedItem();
				PopupPanel addIconographyEntryDialog = new PopupPanel();
				FramedPanel newIconographyEntryFP = new FramedPanel();
				TextArea ieTextArea = new TextArea();
				ieTextArea.addValidator(new MinLengthValidator(2));
				ieTextArea.addValidator(new MaxLengthValidator(256));
				ieTextArea.setValue(iconographyEntryToEdit.getText());
//				ieTextArea.setSize("300px", "100px");
				newIconographyEntryFP.add(ieTextArea);
				newIconographyEntryFP.setHeading("edit text");
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (ieTextArea.isValid()) {
							iconographyEntryToEdit.setText(ieTextArea.getValue());
							iconographyTreeStore.update(iconographyEntryToEdit);
							dbService.updateIconographyEntry(iconographyEntryToEdit, new AsyncCallback<Boolean>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
								}

								@Override
								public void onSuccess(Boolean result) {
									StaticTables.getInstance().reloadIconography(); // we need to reload the whole tree otherwise this won't work
								}
							});
							addIconographyEntryDialog.hide();
						}
					}
				});
				newIconographyEntryFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addIconographyEntryDialog.hide();
					}
				});
				newIconographyEntryFP.addButton(cancelButton);
				addIconographyEntryDialog.add(newIconographyEntryFP);
				addIconographyEntryDialog.setSize("300px", "200px");
				addIconographyEntryDialog.setModal(true);
				addIconographyEntryDialog.center();
			}
		});

//		mainPanel = new FramedPanel();
		setHeading("Iconography Selector");
		add(iconographySelectorBLC);
		addTool(iconographyExpandTB);
		addTool(iconographyCollapseTB);
		if (UserLogin.getInstance().getAccessRights() >= UserEntry.FULL) {
			addTool(addEntryTB);
			addTool(renameEntryTB);
		}
		addTool(resetTB);
	}
	
	private void addChildIconographyEntry(TreeStore<IconographyEntry> store, IconographyEntry child) {
		for (IconographyEntry entry : store.getAll()) {
			if (entry.getIconographyID() == child.getParentID()) {
				store.add(entry, child);
			}
		}
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
