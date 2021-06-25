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
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sound.sampled.Port.Info;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.BrowserEvents;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
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
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent.BeforeHideHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.DisableEvent;
import com.sencha.gxt.widget.core.client.event.DisableEvent.DisableHandler;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
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
import com.google.gwt.user.client.Timer;
import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.EditorListener;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentEntry;
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
	
	interface MasterImg extends XTemplates {
		@XTemplate("<p><img src='{img}'></img>")
		SafeHtml img(SafeUri img);
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
	private ArrayList<OrnamentEntry> ornaments;
	private PopupPanel imgPop = new PopupPanel();
//	private FramedPanel mainPanel = null;
	private StoreFilterField<IconographyEntry> filterField;
	protected static Map<String, IconographyEntry> selectedIconographyMap;
	private Map<Integer,String> imgdDic =StaticTables.getInstance().getOrnamentMasterPics();
	private ArrayList<OrnamentEntry> ornamentEntries;
	private Context currentContext;
	protected EditorListener el=null;
	private Boolean dropUnselected = false;
	private List<IconographyEntry> allEntries;
	private ArrayList<IconographyEntry> allAnnotationEntries = new ArrayList<IconographyEntry>();
	private ArrayList<AnnotationEntry> relatedAnnotationList;
	private IconographySelectorListener icoSelectorListener;
	private List<IconographyEntry> beforeSelection;
	private VerticalLayoutContainer iconographySelectorBLC;
	private ToolButton minTB;
	private ToolButton maxTB;
	public ToolButton closeTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
	private ToolButton addEntryTB;
	private ToolButton renameEntryTB;
	private StoreFilter<IconographyEntry> filterFieldDropUnselected;
	

	ArrayList<IconographyEntry> iconographyRelationList;
	public IconographySelector(Collection<IconographyEntry> elements,EditorListener el,boolean dropunselected, ArrayList<AnnotationEntry> relatedAnnotationList, IconographySelectorListener icoSelectorListener) {
		this.el=el;
		this.dropUnselected=dropunselected;
		this.relatedAnnotationList=relatedAnnotationList;
		this.icoSelectorListener=icoSelectorListener;
		initPanel(elements);
		fillAllAnnotationEntries();
	}
	public IconographySelector(Collection<IconographyEntry> elements,EditorListener el) {
		this.el=el;
		initPanel(elements);
	}

	public IconographySelector(Collection<IconographyEntry> elements) {
		initPanel(elements);
		this.el=null;
	}
	public IconographySelector(Collection<IconographyEntry> elements, IconographySelectorListener icoSelectorListener) {
		initPanel(elements);
		this.el=null;
		this.icoSelectorListener=icoSelectorListener;
	}
	private void fillAllAnnotationEntries() {
		allAnnotationEntries.clear();
		for (AnnotationEntry ae : relatedAnnotationList) {
			for (IconographyEntry ie : ae.getTags()) {
				addWithParents(ie);
			}
		}
	}
	public void setRelatedAnnotationList(ArrayList<AnnotationEntry> relatedAnnotationList) {
		this.relatedAnnotationList=relatedAnnotationList;
		fillAllAnnotationEntries();
		iconographyTreeStore.setEnableFilters(false);
		iconographyTreeStore.setEnableFilters(true);
	}
	private void addWithParents(IconographyEntry ie) {
		if (!allAnnotationEntries.contains(ie)) {
			allAnnotationEntries.add(ie);
			iconographyTree.refresh(ie);
			if (ie.getParentID()!=0) {
				IconographyEntry parent = iconographyTreeStore.findModelWithKey(Integer.toString(ie.getParentID()));
				addWithParents(parent);
			}
		}
	}

	
	public void buildTreeStore(Collection<IconographyEntry> elements, boolean ornaments, boolean dropUnselected){
		allEntries = new ArrayList<IconographyEntry>();
		iconographyTreeStore.clear();
			for (IconographyEntry item : elements) {
				if ((item.getIconographyID()==3)||(!ornaments)) {
					iconographyTreeStore.add(item);
					allEntries.add(item);
					if (item.getChildren() != null) {
						processParentIconographyEntry(item);
					}
			
				}
			}
	}
	public void IconographyTreeEnabled(boolean enable) {
		iconographyTree.setEnabled(enable);
	}
	private void refreshMasterpics() {
		for (OrnamentEntry oe :ornaments) {
			iconographyTree.refresh(iconographyTreeStore.findModelWithKey(Integer.toString(oe.getIconographyID())));
		}

	}
	public TreeStore<IconographyEntry> getIconographyStore() {
		return iconographyTreeStore;
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
			
			void onSelect(XElement node, boolean select) {
				Util.doLogging("Selected");
			}
		
		};
		MasterImg masterImg = GWT.create(MasterImg.class);
		class CustomImageCell extends AbstractCell<String> {
		    private ImageXTemplate imageTemplate = GWT.create(ImageXTemplate.class);
		    public CustomImageCell(String... consumedEvents) {
		        super(consumedEvents);
		      }
		    public CustomImageCell(Set<String> consumedEvents) {
		        super(consumedEvents);
		      }

		    @Override
		    public void render(Context context, String ie, SafeHtmlBuilder sb) {
		    	boolean found = false;
		    	for (IconographyEntry icoEntry : allAnnotationEntries) {
		    			if (Integer.toString(icoEntry.getIconographyID())==context.getKey()) {
		    				found=true;
		    			}
		    	}
		    	if (found) {
		    		sb.append(SafeHtmlUtils.fromTrustedString("<p style=\"color:green;\">"+ie+"</p>"));
		    	}else {
		    		sb.append(SafeHtmlUtils.fromTrustedString("<p style=\"color:red;\">"+ie+"</p>"));
		    	}
		    	
				
		    }
		    @Override
		    public void onBrowserEvent(Context context, Element parent, String value,
		    	      NativeEvent event, ValueUpdater<String> valueUpdater) {
		    	    String eventType = event.getType();
		    	    Util.doLogging("Mouse ober Ico triggered");
		    	    // Special case the ENTER key for a unified user experience.
		    	    if (BrowserEvents.MOUSEOVER.equals(eventType) ) {
		    	    	beforeSelection=iconographyTree.getSelectionModel().getSelectedItems();
		    	    	currentContext=context;
			    	    showPOPUP(context, event.getClientX()+10,event.getClientY()+10);
			    	    }
		    	    
		    	    if (BrowserEvents.MOUSEOUT.equals(eventType)) {
				    	if (currentContext==context) {
				    	    hidePOPUP();				    		
				    	}
				    	if (icoSelectorListener!=null) {
			    	    	icoSelectorListener.icoDeHighlighter(Integer.parseInt((String)context.getKey()));
				    	}
			    	}
		    	    if (BrowserEvents.MOUSEUP.equals(eventType)) {
		    	    	Util.doLogging("Clicked "+context.getKey());
		    	    	IconographyEntry ie = iconographyTree.getStore().findModelWithKey((String)context.getKey());
		    	    	for (IconographyEntry selectedIE : beforeSelection) {
		    	    		if (selectedIE.getIconographyID()==ie.getIconographyID()) {
		    	    			iconographyTree.getSelectionModel().deselect(ie);
		    	    			return;
		    	    		}
		    	    	}
			    	 }

		    	    if (BrowserEvents.KEYDOWN.equals(eventType) && event.getKeyCode() == KeyCodes.KEY_ENTER) {
			    	      onEnterKeyDown(context, parent, value, event, valueUpdater);
			    	}
		    }
		    private void showPOPUP(Context context,int x,int y) {
		    	imgPop.clear();
		    	if (icoSelectorListener!=null) {

		    		icoSelectorListener.icoHighlighter(Integer.parseInt((String)context.getKey()));
		    	}
		    	if (imgdDic.containsKey(Integer.parseInt((String)context.getKey()))) {
			    	imgPop.setPopupPosition(x,y);
			    	//imgPop.setSize(300, 300);
			    	//Info.display("imgdDic sice", Integer.toString(imgdDic.size()));
			    	HTMLPanel info = new HTMLPanel(masterImg.img(UriUtils.fromTrustedString(imgdDic.get(Integer.parseInt((String)context.getKey())))));
			    	info.setTitle((String)context.getKey());
			    	imgPop.add(info);
			    	imgPop.show();
			        Timer timer = new Timer()
			        {
			            @Override
			            public void run()
			            {
			            	imgPop.hide();
			            }
			        };

			        timer.schedule(5000);
		    	}
		    }
		    private void hidePOPUP() {
		    	imgPop.hide();
		    }


		}
		Set<String> events = new HashSet<String>();
	    events.add(BrowserEvents.MOUSEOVER);
	    events.add(BrowserEvents.MOUSEOUT);
	    events.add(BrowserEvents.MOUSEUP);
		Cell<String> cCell = new CustomImageCell(events);
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
		if (dropUnselected) {
			iconographyTree.setCheckable(false);			
		}
		else {
			iconographyTree.setCheckable(true);

		}
//		iconographyTree.setCheckable(true);
		//iconographyTree.setAutoLoad(true);
		//iconographyTree.setCheckStyle(CheckCascade.NONE);
		iconographyTree.setCheckNodes(CheckNodes.BOTH);

		iconographyTree.addCheckChangeHandler(new CheckChangeHandler<IconographyEntry>() {
			
			@Override
			public void onCheckChange(CheckChangeEvent<IconographyEntry> event) {
				if (el!=null) {
					el.addClickNumber();
				}
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
	public void loadOrnamentMasterPics(List<IconographyEntry> iconographies) {
		if (UserLogin.getInstance().getSessionID()!="") {
			String wherefirst = "IconographyID in (";
			String where= "";
			for (IconographyEntry ie : iconographies) {
				if (where.isEmpty()) {
					where=Integer.toString(ie.getIconographyID());
				}
				else {
					where=where+", "+Integer.toString(ie.getIconographyID());				
				}
			}
			
			where=wherefirst+where+")";
			Util.doLogging("where for getOrnamentsWhere: "+where);
			dbService.getOrnamentsWHERE(where, new AsyncCallback<ArrayList<OrnamentEntry>>() {
	
				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}
				@Override
				public void onSuccess(ArrayList<OrnamentEntry> result) {
					Util.doLogging("Größe von Ornaments:"+Integer.toString(result.size()));
					String where= "";
						ornamentEntries = result;
						for (OrnamentEntry oe :result) {
							if (where.isEmpty()) {
								where=Integer.toString(oe.getMasterImageID());				
							}
							else {
								where=where+", "+Integer.toString(oe.getMasterImageID());				
							}
	
						}
						dbService.getPicsByImageID(where, 400, UserLogin.getInstance().getSessionID(), new AsyncCallback<Map<Integer,String>>() {
							
							@Override
							public void onFailure(Throwable caught) {				
								caught.printStackTrace();
							}
							
							@Override
							public void onSuccess(Map<Integer,String> imgdic) {
								for (OrnamentEntry oe : ornamentEntries) {
									if (oe.getMasterImageID()>0) {
										if (imgdic.containsKey(oe.getMasterImageID())) {
											//Util.doLogging(imgdic.get(oe.getMasterImageID()));
											imgdDic.put(oe.getIconographyID(), imgdic.get(oe.getMasterImageID()));
											StaticTables.getInstance().setOrnamentMasterPics(imgdDic);
											}	
									}
								}
							}
						});
					}
			});

		}

	}
	public void imgPopHide() {
		imgPop.hide();
	}

	private void processParentIconographyEntry( IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			iconographyTreeStore.add(item, child);
			allEntries.add(item);
			if (child.getChildren() != null) {
				processParentIconographyEntry(child);
			}
		}
	}
	public void findParent(IconographyEntry child) {
		if (child.getParentID()==0) {
			if (iconographyTreeStore.findModel(child)==null) {
				Util.doLogging("Found: "+Integer.toString(child.getParentID())+" - "+Integer.toString(child.getIconographyID()));
		    	iconographyTreeStore.add(child);
			}
		}
		else {
		for (IconographyEntry ie : allEntries) {
			if (ie.getIconographyID()==child.getParentID()) {

					findParent(ie);
					Util.doLogging("Found: "+child.getText()+" - "+child.getText());
				    if (iconographyTreeStore.findModel(child)==null) {
				    	iconographyTreeStore.add(ie, child);
				    }
				}
				
			
			}
		};
	}

	public void dropunselected() {
		filterFieldDropUnselected = new StoreFilter<IconographyEntry>() {

			@Override
			public boolean select(Store<IconographyEntry> store, IconographyEntry parent, IconographyEntry item) {
				TreeStore<IconographyEntry> treeStore = (TreeStore<IconographyEntry>) store;
//				do {
//					boolean found = false;
					for (IconographyEntry ie : iconographyRelationList) {
						//Util.doLogging("found "+item.getText()+" at old selected items ");
						if (item.getIconographyID()==ie.getIconographyID()) {
							return true;
						}
					}
					for (IconographyEntry ie : allAnnotationEntries) {
						//Util.doLogging(item.getText()+" - "+ie.getText());
						//Util.doLogging("found "+item.getText()+" at Annotated items ");
						if (item.getIconographyID()==ie.getIconographyID()) {
							return true;
						}
					}
//					item = treeStore.getParent(item);
//				} while (item != null);
				return false;
			}
			};
		
		iconographyTreeStore.addFilter(filterFieldDropUnselected);
		iconographyTreeStore.setEnableFilters(true);
//		iconographyTreeStore.clear();
//		for (IconographyEntry ie : ies) {
//					findParent(ie);
//			
//		}
	}
	public void setSelectedIconography(ArrayList<IconographyEntry> iconographyRelationList) {
		Util.doLogging("*** setSelectedIconography called - iconographyTree no. of items = " + iconographyTree.getStore().getAllItemsCount());
		this.iconographyRelationList=iconographyRelationList;
		iconographyTree.expandAll();
		resetSelection();
		for (IconographyEntry entry : iconographyRelationList) {
			//Util.doLogging("setSelectedIconography setting entry = " + entry.getIconographyID());
			iconographyTree.setChecked(entry, CheckState.CHECKED);
			selectedIconographyMap.put(entry.getUniqueID(), entry);
		}			
		if (dropUnselected) {
				dropunselected();
		}
	}
	private void initPanel(Collection<IconographyEntry> elements) {
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
		this.addHideHandler(new HideHandler() {

			@Override
			public void onHide(HideEvent event) {
				// TODO Auto-generated method stub
				imgPop.hide();
			}
			
		});
		this.addBeforeHideHandler(new BeforeHideHandler() {

			@Override
			public void onBeforeHide(BeforeHideEvent event) {
				// TODO Auto-generated method stub
				imgPop.hide();
			}
			
		});
		
		this.addDisableHandler(new DisableHandler() {

			@Override
			public void onDisable(DisableEvent event) {
				// TODO Auto-generated method stub
				imgPop.hide();
			}
			
		});
		iconographyTree=buildTree(false);
		buildTreeStore(elements,false, dropUnselected);
		imgdDic =StaticTables.getInstance().getOrnamentMasterPics();
		if (imgdDic.size()==0) {
			loadOrnamentMasterPics(iconographyTree.getStore().getAll());
		}
		iconographyTree.setEnabled(false);
		iconographySelectorBLC = new VerticalLayoutContainer();

		iconographySelectorBLC.add(iconographyTree, new VerticalLayoutData(1.0, .95));
		iconographySelectorBLC.add(filterField, new VerticalLayoutData(1.0, .05));
		
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

		addEntryTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
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
										imgdDic = StaticTables.getInstance().getOrnamentMasterPics();
										addChildIconographyEntry(iconographyEntry);
										//icoSelectorListener.reloadIconography(iconographyEntry);
										icoSelectorListener.reloadOSD();
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

		renameEntryTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
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
									imgdDic = StaticTables.getInstance().getOrnamentMasterPics();
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
		minTB = new ToolButton(new IconConfig("minimizeButton", "minimizeButtonOver"));
		minTB.setToolTip(Util.createToolTip("Show only selected in tree"));
		maxTB= new ToolButton(new IconConfig("maximizeButton", "maximizeButtonOver"));
		maxTB.setToolTip(Util.createToolTip("Show whole tree"));
		minTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				iconographyRelationList = getSelectedIconography();
				dropUnselected = true;
				iconographyTree.setCheckable(false);			
				iconographyTreeStore.setEnableFilters(true);
				iconographyTreeStore.addFilter(filterFieldDropUnselected);
				minTB.setVisible(false);
				maxTB.setVisible(true);
			}
		});
		//getHeader().addTool(minTB);

		maxTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dropUnselected = false;
				iconographyTree.setCheckable(true);	
				iconographyTreeStore.removeFilter(filterFieldDropUnselected);
				iconographyTreeStore.setEnableFilters(false);
				setSelectedIconography(iconographyRelationList);
				maxTB.setVisible(false);
				minTB.setVisible(true);
			}
		});

//		mainPanel = new FramedPanel();
		setHeading("Iconography Selector");
		add(iconographySelectorBLC);
		addTool(iconographyExpandTB);
		addTool(iconographyCollapseTB);
		addTool(addEntryTB);				
		addTool(renameEntryTB);
		addTool(maxTB);
		addTool(minTB);
		addTool(closeTB);
		
		//addTool(resetTB);
		if (el!=null) {
			el.setClickNumber(0);
		}
		minTB.setVisible(false);
		closeTB.setVisible(false);
		testforUserRights();
	}
	public void testforUserRights() {
		if (UserLogin.getInstance().getAccessRights() >= UserEntry.FULL) {
			Util.doLogging("adding edit fields for iconography");
			addEntryTB.enable();				
			renameEntryTB.enable();
		}
		else {
			Util.doLogging("not adding edit fields for iconography due to rights");
			addEntryTB.disable();				
			renameEntryTB.disable();
		}

	}
	public void setVisibleMaxMinTB() {
		minTB.setVisible(true);
		maxTB.setVisible(true);
	}
	public void setVisibleMaxMinTBbyDropUnselected() {
		minTB.setVisible(!dropUnselected);
		maxTB.setVisible(dropUnselected);
	}
	public void addChildIconographyEntry(IconographyEntry child) {
		for (IconographyEntry entry : iconographyTreeStore.getAll()) {
			if (entry.getIconographyID() == child.getParentID()) {
				iconographyTreeStore.setEnableFilters(false);
				iconographyTreeStore.add(entry, child);
				iconographyTreeStore.setEnableFilters(true);
				
			}
		}
	}
	public List<IconographyEntry> getCLickedItems(){
		return iconographyTree.getSelectionModel().getSelectedItems();
	}
	
	public ArrayList<IconographyEntry> getSelectedIconography() {
		if (dropUnselected) {
			return iconographyRelationList;
		}
		else {
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
