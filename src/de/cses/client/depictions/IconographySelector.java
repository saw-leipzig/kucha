/*
 * Copyright 2016 - 2019
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
import java.util.Comparator;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
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
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckNodes;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.user.UserLogin;
import de.cses.shared.AuthorEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.UserEntry;

public class IconographySelector extends FramedPanel {

	public static class IconographyKeyProvider implements ModelKeyProvider<IconographyEntry> {
		@Override
		public String getKey(IconographyEntry item) {
			return Integer.toString(item.getIconographyID());
		}
	}
	interface IcoProperties extends PropertyAccess<IconographyEntry> {
		ModelKeyProvider<AuthorEntry> IconographyID();
		ValueProvider<AuthorEntry, String> text();
	}

	public static class IconographyValueProvider implements ValueProvider<IconographyEntry, String> {

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

	private TreeStore<IconographyEntry> iconographyTreeStore = iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());;
	private Tree<IconographyEntry, String> iconographyTree;
//	private FramedPanel mainPanel = null;
	private StoreFilterField<IconographyEntry> filterField;
	protected static Map<String, IconographyEntry> selectedIconographyMap;
	
	public void buildTreeStore(Collection<IconographyEntry> elements, boolean ornaments){
		iconographyTreeStore.clear();
			for (IconographyEntry item : elements) {
				if ((item.getIconographyID()==3)||(!ornaments)) {
					iconographyTreeStore.add(item);
					if (item.getChildren() != null) {
						processParentIconographyEntry(item);
					}
			
				}
			}
	}
	public void IconographyTreeEnabled(boolean enable) {
		iconographyTree.setEnabled(enable);
	}
	public Tree<IconographyEntry, String> buildTree( boolean ornament){
		selectedIconographyMap = new HashMap<String, IconographyEntry>();
			
		iconographyTree = new Tree<IconographyEntry, String>(iconographyTreeStore, new IconographyValueProvider()) {

			@Override
			protected void onFilter(StoreFilterEvent<IconographyEntry> ie) {
				super.onFilter(ie);
				for (IconographyEntry entry : selectedIconographyMap.values()) {
					iconographyTree.setChecked(entry, CheckState.CHECKED);
				}
			}

		};
		class CustomImageCell extends AbstractCell<String> {
		    private ImageXTemplate imageTemplate = GWT.create(ImageXTemplate.class);
		    
		    @Override
		    public void render(Context context, String ie, SafeHtmlBuilder sb) {
		    	sb.appendHtmlConstant("<span  qtitle='Change' qtip='To see a describing picture, click on the element.'>" +ie+"<br></span>");
		    	//sb.append(imageTemplate.createImage(""));
		    }
		}
	    Cell<String> cCell = new CustomImageCell();
	    iconographyTree.setCell(cCell);
	    QuickTip qt = new QuickTip(iconographyTree);
		if (ornament) {
			iconographyTree.setCheckStyle(CheckCascade.NONE);;
			iconographyTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
			
		}
		else {
			iconographyTree.setCheckStyle(CheckCascade.PARENTS);;
			iconographyTree.getSelectionModel().setSelectionMode(SelectionMode.MULTI);

		}
		iconographyTree.setCheckable(true);
		iconographyTree.setAutoLoad(true);
		//iconographyTree.setCheckStyle(CheckCascade.NONE);
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
		return iconographyTree;
	}
	private void processParentIconographyEntry_select(IconographyEntry item, ArrayList<IconographyEntry> l) {	
		for (IconographyEntry child : item.getChildren()) {
			Boolean found =new Boolean(false);
			for (IconographyEntry entry : l) {
				if (entry.getIconographyID()==child.getIconographyID()){
					found=true;
					break;}}
			if (found){
				//Info.display(child.getText(), child.getText());
				iconographyTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry_select(child,l);
			}
		}}
	}
	public TreeStore<IconographyEntry> setIconographyStore(Collection<IconographyEntry> elements, ArrayList<IconographyEntry> l, boolean dropunselected) {
		TreeStore<IconographyEntry> iconographyTreeStore = iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());;
		iconographyTreeStore.clear();
		for (IconographyEntry item : elements) {
				iconographyTreeStore.add(item);
				if (item.getChildren() != null) {
					processParentIconographyEntry(item);
				}		
		}
		iconographyTreeStore.clear();
		for (IconographyEntry item : elements) {
			iconographyTreeStore.add(item);
			if (item.getChildren() != null) {
				if (dropunselected) {
					processParentIconographyEntry_select(item,l);
					
				}
				else
				{
					processParentIconographyEntry(item);
				}
			}
			
		}
		Comparator<? super IconographyEntry> itemComparator = new Comparator<IconographyEntry>() {
			@Override
			public int compare(IconographyEntry o1, IconographyEntry o2) {
				return ((Integer)o1.getIconographyID()).compareTo(o2.getIconographyID());
			}
		};
		StoreSortInfo<IconographyEntry> sortInfo = new StoreSortInfo<IconographyEntry>(itemComparator, SortDir.DESC);
		iconographyTreeStore.addSortInfo(sortInfo);
		iconographyTreeStore.applySort(true);
	return iconographyTreeStore;
	}
	public ArrayList<IconographyEntry> setSelectionByIDList(ArrayList<Integer> ids) {
		ArrayList<IconographyEntry> ies = new ArrayList<IconographyEntry>();
		for (int id : ids) {
			if (id > 0) {
				IconographyEntry ie = iconographyTree.getStore().findModelWithKey(Integer.toString(id));
				ies.add(ie);
			}
		}
		//iconographyTree.getSelectionModel().setSelection(ies);
		return ies;
	}
	public IconographySelector(Collection<IconographyEntry> elements) {

		filterField = new StoreFilterField<IconographyEntry>() {

			@Override
			protected boolean doSelect(Store<IconographyEntry> store, IconographyEntry parent, IconographyEntry item, String filter) {
				TreeStore<IconographyEntry> treeStore = (TreeStore<IconographyEntry>) store;
				do {
					String treename = "";
					String treesearch = "";
					if (item.getText()!=null) {
						treename = item.getText().toLowerCase().replaceAll("\\p{M}", "");						
					};
					if(item.getSearch()!=null){
						treesearch = item.getSearch().toLowerCase().replaceAll("\\p{M}", "");						
					};
					filter = filter.toLowerCase().replaceAll("\\p{M}", "");

					if ((treename.contains(filter))||(treesearch.contains(filter))) {
						return true;
					}
					item = treeStore.getParent(item);
				} while (item != null);
				return false;
			}
		};
		filterField.setEmptyText("enter a search term");
		filterField.bind(iconographyTreeStore);
		initPanel(iconographyTreeStore, elements);
	}

	private void processParentIconographyEntry( IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			iconographyTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry(child);
			}
		}
	}

	public void setSelectedIconography(ArrayList<IconographyEntry> iconographyRelationList) {
		Util.doLogging("*** setSelectedIconography called - iconographyTree no. of items = " + iconographyTree.getStore().getAllItemsCount());
		resetSelection();
		for (IconographyEntry entry : iconographyRelationList) {
			//Util.doLogging("setSelectedIconography setting entry = " + entry.getIconographyID());
			iconographyTree.setChecked(entry, CheckState.CHECKED);
			selectedIconographyMap.put(entry.getUniqueID(), entry);
		}
	}

	private void initPanel(TreeStore<IconographyEntry> iconographyTreeStore, Collection<IconographyEntry> elements) {
		iconographyTree=buildTree(false);
		buildTreeStore(elements,false);
		iconographyTree.setEnabled(false);
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
				FramedPanel fpanelText = new FramedPanel();
				fpanelText.setHeading("Name");
				fpanelText.add(ieTextArea);
				TextArea ieTextAreaSearch = new TextArea();
				ieTextAreaSearch.addValidator(new MinLengthValidator(2));
				ieTextAreaSearch.addValidator(new MaxLengthValidator(256));
				FramedPanel fpanelSearch = new FramedPanel();
				fpanelSearch.setHeading("Alternative Names");
				fpanelSearch.add(ieTextAreaSearch);
				VerticalLayoutContainer newIconogryphyVLC = new VerticalLayoutContainer();
				newIconogryphyVLC.add(html, new VerticalLayoutData(1.0, .2));
				newIconogryphyVLC.add(fpanelText, new VerticalLayoutData(1.0, .4));
				newIconogryphyVLC.add(fpanelSearch, new VerticalLayoutData(1.0, .4));
				newIconographyEntryFP.add(newIconogryphyVLC);
				newIconographyEntryFP.setSize("300px", "280px");
				newIconographyEntryFP.setHeading("add child element to");
				ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (ieTextArea.isValid()) {
							IconographyEntry iconographyEntry = new IconographyEntry(0, iconographyTree.getSelectionModel().getSelectedItem().getIconographyID(), ieTextArea.getValue(), ieTextAreaSearch.getValue());
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
				newIconographyEntryFP.addTool(saveTB);
				ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addIconographyEntryDialog.hide();
					}
				});
				newIconographyEntryFP.addTool(cancelTB);
				addIconographyEntryDialog.add(newIconographyEntryFP);				
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
				FramedPanel fpanelText = new FramedPanel();
				fpanelText.setHeading("Name");
				fpanelText.add(ieTextArea);
				TextArea ieTextAreaSearch = new TextArea();
				ieTextAreaSearch.addValidator(new MinLengthValidator(2));
				ieTextAreaSearch.addValidator(new MaxLengthValidator(256));
				ieTextAreaSearch.setValue(iconographyEntryToEdit.getSearch());
				FramedPanel fpanelSearch = new FramedPanel();
				fpanelSearch.setHeading("Alternative Names");
				fpanelSearch.add(ieTextAreaSearch);
				VerticalLayoutContainer editIconogryphyVLC = new VerticalLayoutContainer();
				editIconogryphyVLC.add(fpanelText, new VerticalLayoutData(1.0, .5));
				editIconogryphyVLC.add(fpanelSearch, new VerticalLayoutData(1.0, .5));

				newIconographyEntryFP.add(editIconogryphyVLC);
				newIconographyEntryFP.setHeading("edit text");
				newIconographyEntryFP.setSize("300px", "250px");
				ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
				saveTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (ieTextArea.isValid()) {
							iconographyEntryToEdit.setText(ieTextArea.getValue());
							iconographyEntryToEdit.setSearch(ieTextAreaSearch.getValue());
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
				newIconographyEntryFP.addTool(saveTB);
				ToolButton cancelTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
				cancelTB.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addIconographyEntryDialog.hide();
					}
				});
				newIconographyEntryFP.addTool(cancelTB);
				addIconographyEntryDialog.add(newIconographyEntryFP);
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
			int i =0;
			for (IconographyEntry entry : selectedIconographyMap.values()) {
				result.add(entry);
				Util.doLogging("Added "+Integer.toString(i++)+" items to iconographyresult");
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
	public void selectNoParents(boolean noParents) {
		if (noParents) {
			
				iconographyTree.setCheckStyle(CheckCascade.NONE);
		
		}
		else {
			iconographyTree.setCheckStyle(CheckCascade.PARENTS);
		}
	}
	public void selectChildren(boolean Children) {
		if (Children) {
			
				iconographyTree.setCheckStyle(CheckCascade.CHILDREN);
		
		}
		else {
			iconographyTree.setCheckStyle(CheckCascade.NONE);
		}
	}


}
