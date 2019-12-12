/*
 * Copyright 2018 
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
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import de.cses.client.DatabaseService;
//import de.cses.client.depictions.GetInfo;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.ui.TextElement;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.comparator.BibEntryComparator;
import de.cses.client.depictions.IconographySelector.IconographyValueProvider;

/**
 * @author alingnau
 *
 */
public class DepictionDataDisplay extends AbstractDataDisplay {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private DepictionEntry entry;
	private ArrayList<TextElement> iconographyList;
	private ArrayList<TextElement> pictorialElementsList;
	private ArrayList<TextElement> decorationOrnamentsList;
	private StaticTables stab = StaticTables.getInstance();
	private Tree<IconographyEntry, String> iconographyTree;
	private TreeStore<IconographyEntry> iconographyTreeStore;
	protected Map<String, IconographyEntry> selectedIconographyMap;
	protected IconographySelector iconographySelector;
	private HashMap<Integer, IconographyEntry> allIconographyEntriesList = new HashMap<Integer, IconographyEntry>();
	public interface Images extends ClientBundle {


		public Images INSTANCE = GWT.create(Images.class);


		@Source("none.png")
		ImageResource foo();

		}
	public void resetSelection() {
		selectedIconographyMap.clear();
		if (iconographyTree != null) {
			for (IconographyEntry entry : iconographyTree.getCheckedSelection()) {
				iconographyTree.setChecked(entry, CheckState.UNCHECKED);
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
	private void processParentIconographyEntry(TreeStore<IconographyEntry> store, IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			store.add(item, child);
			if (child.getChildren() != null) {
				
				processParentIconographyEntry(store, child);
			}
		}
	}
	private void expandchildren(IconographyEntry item) {
		for (IconographyEntry child : iconographyTreeStore.getChildren(item)) {
			iconographyTree.setExpanded(child, true);
			if (iconographyTreeStore.getChildren(item) != null) {
				expandchildren(child);
			}
		}
	}
	private void processParentIconographyEntry_select(TreeStore<IconographyEntry> store, IconographyEntry item, ArrayList<IconographyEntry> l) {	
		for (IconographyEntry child : item.getChildren()) {
			Boolean found =new Boolean(false);
			for (IconographyEntry entry : l) {
				if (entry.getIconographyID()==child.getIconographyID()){
					found=true;
					break;}}
			if (found){
				//Info.display(child.getText(), child.getText());
				store.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry_select(store, child,l);
			}
		}}
	}
	private void setIconographyStore(Collection<IconographyEntry> elements, ArrayList<IconographyEntry> l) {
		iconographyTreeStore.clear();
		for (IconographyEntry item : elements) {
			iconographyTreeStore.add(item);
			if (item.getChildren() != null) {
				processParentIconographyEntry_select(iconographyTreeStore, item,l);
			}

	  }
	}

	/**
	 * Visualization of the content of a DepictionEntry as a HTML page (right column of UI)
	 */
	public DepictionDataDisplay(DepictionEntry e) {
		super();
		entry = e;
		
		
		String cave = "";
		String wall = "";
		SafeUri realCaveSketchUri = null;
		DepictionViewTemplates view = GWT.create(DepictionViewTemplates.class);
		if (e.getCave() != null) {
			if (e.getCave().getSiteID() > 0) {
				cave += stab.getSiteEntries().get(e.getCave().getSiteID()).getShortName() + ": ";
			}
			cave += e.getCave().getOfficialNumber() + ((e.getCave().getHistoricName() != null && e.getCave().getHistoricName().length() > 0) ? " (" + e.getCave().getHistoricName() + ")" : ""); 
			realCaveSketchUri = UriUtils.fromString("/resource?cavesketch=" + e.getCave().getOptionalCaveSketch() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
			wall = e.getWallID() > 0 ? stab.getWallLocationEntries().get(e.getCave().getWall(e.getWallID()).getWallLocationID()).getLabel() : "";
		}
		String shortname = e.getShortName() != null ? e.getShortName() : "";
		String expedition = e.getExpedition() != null ? e.getExpedition().getName() : "";
		String vendor = e.getVendor() != null ? e.getVendor().getVendorName() : "";
		String location = e.getLocation() != null ? e.getLocation().getName() : "";
		String date = e.getPurchaseDate() != null ? e.getPurchaseDate().toString() : "";
		String stateOfPreservation = "";
		for (PreservationAttributeEntry pae : e.getPreservationAttributesList()) {
			stateOfPreservation += stateOfPreservation.length() > 0 ? ", " + pae.getName() : pae.getName();
		}
		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + e.getMasterImageID() + "&thumb=700" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		SafeUri fullImageUri = UriUtils.fromString("resource?imageID=" + e.getMasterImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		String style = e.getStyleID() > 0 ? stab.getStyleEntries().get(e.getStyleID()).getStyleName() : "";
		String modesOfRepresentation = e.getModeOfRepresentationID() > 0 ? stab.getModesOfRepresentationEntries().get(e.getModeOfRepresentationID()).getName() : "";
		ArrayList<AnnotatedBibliographyEntry> bibList = e.getRelatedBibliographyList();
		// TODO ugly and hard wired but it will do the trick for now

		selectedIconographyMap = new HashMap<String, IconographyEntry>();
		iconographyTreeStore=iconographySelector.setIconographyStore(StaticTables.getInstance().getIconographyEntries().values(),e.getRelatedIconographyList(),true);
		iconographyTree = new Tree<IconographyEntry, String>(iconographyTreeStore, new IconographyValueProvider());
		TreeStyle treeStyle = new TreeStyle(); 
		treeStyle.setNodeCloseIcon(Images.INSTANCE.foo());
		treeStyle.setNodeOpenIcon(Images.INSTANCE.foo());
		iconographyTree.setCheckable(false);
		iconographyTree.setVisible(true);
		iconographyTree.setStyle(treeStyle);

		
		setIconographyStore(StaticTables.getInstance().getIconographyEntries().values(),e.getRelatedIconographyList());
		for (IconographyEntry ie : iconographyTreeStore.getRootItems()) {
			expandchildren(ie);
			iconographyTree.setExpanded(ie, false);
			Util.doLogging(ie.getText());
		}
		Util.doLogging("Treegröße: "+Integer.toString(iconographyTree.getStore().getAllItemsCount()));
	
		if (!bibList.isEmpty()) {
			bibList.sort(new BibEntryComparator());
		}
		
//		HTML htmlWidget = new HTML(view.display(
//				shortname, 
//				e.getInventoryNumber() != null ? e.getInventoryNumber() : "",  
//				cave,
//				wall,
//				expedition, 
//				vendor, 
//				date, 
//				location, 
//				stateOfPreservation, 
//				imageUri,
//				fullImageUri, 
//				realCaveSketchUri, 
//				e.getWidth(), e.getHeight(),
//				style, 
//				modesOfRepresentation, 
//				e.getDescription() != null ? e.getDescription() : "",
//				e.getGeneralRemarks() != null ? e.getGeneralRemarks() : "",
//				e.getOtherSuggestedIdentifications() != null ? e.getOtherSuggestedIdentifications() : "",
//				iconographyList,
//				pictorialElementsList,
//				decorationOrnamentsList,
//				e.getRelatedBibliographyList(),
//				e.getLastChangedByUser() != null ? e.getLastChangedByUser() : "",
//				e.getModifiedOn() != null ? e.getModifiedOn() : ""
//			));
		HTML htmlWidget = new HTML(view.displayoben(
				shortname, 
				e.getInventoryNumber() != null ? e.getInventoryNumber() : "",  
				cave,
				wall,
				expedition, 
				vendor, 
				date, 
				location, 
				stateOfPreservation, 
				imageUri,
				fullImageUri, 
				realCaveSketchUri, 
				e.getWidth(), e.getHeight(),
				style, 
				modesOfRepresentation, 
				e.getDescription() != null ? e.getDescription() : "",
				e.getGeneralRemarks() != null ? e.getGeneralRemarks() : "",
				e.getOtherSuggestedIdentifications() != null ? e.getOtherSuggestedIdentifications() : ""

			));
		HTML htmlWidget2 = new HTML(view.displayunten(
		e.getRelatedBibliographyList(),
		e.getLastChangedByUser() != null ? e.getLastChangedByUser() : "",
		e.getModifiedOn() != null ? e.getModifiedOn() : "",
		e.getDescription() != null ? e.getDescription() : "",
		e.getGeneralRemarks() != null ? e.getGeneralRemarks() : "",
		e.getOtherSuggestedIdentifications() != null ? e.getOtherSuggestedIdentifications() : ""

	));		
		StyleInjector.inject(".myCustomStyle {font-family: verdana;background:#ffcc66; font-size: 16px; }");
		htmlWidget.addStyleName("html-data-view");
		htmlWidget2.addStyleName("html-data-view");
		iconographyTreeStore.addSortInfo(new StoreSortInfo<IconographyEntry>(new Comparator<IconographyEntry>() {
		      @Override
		      public int compare(IconographyEntry o1, IconographyEntry o2) {
		    	  return Integer.compare(o1.getIconographyID(),o2.getIconographyID());
		        
		      }
		    }, SortDir.ASC));
		
		VerticalLayoutContainer decriptionVLC = new VerticalLayoutContainer();
		decriptionVLC.addStyleName("myCustomStyle");
		decriptionVLC.add(htmlWidget);
		decriptionVLC.add(iconographyTree);
		decriptionVLC.add(htmlWidget2);
//		shortNameVLC.add(pictoralTree);
//		shortNameVLC.add(ornamentTree);
		add(decriptionVLC, new MarginData(0, 0, 0, 0));
		setHeading((shortname.length() > 0 ? shortname + " " : "") + (cave.length() > 0 ? " in " + cave : ""));
	}
	//public buildIconographys
	private void addToIconographiesList(IconographyEntry iconographyEntry) {
		allIconographyEntriesList.put(iconographyEntry.getIconographyID(), iconographyEntry);
		for (IconographyEntry ie : iconographyEntry.getChildren()) {
			addToIconographiesList(ie);
		}
	}

	/**
	 * sorts the IconographyElements into the correct lists
	 * @param text
	 * @param ie
	 */
	private void processIconographyEntry(String text, IconographyEntry ie) {
		String s = text.isEmpty() ? ie.getText() : ie.getText() + " \u2192 " + text;
		if (ie.getParentID() > 1000) { // that means it is not one of the basic categories
			IconographyEntry nextEntry = allIconographyEntriesList.get(ie.getParentID());
			processIconographyEntry(s, nextEntry);
		} else {
			switch (ie.getParentID()) {
				case 1:
					iconographyList.add(new TextElement(s));
					break;
				case 2:
					pictorialElementsList.add(new TextElement(s));
					break;
				case 3:
					decorationOrnamentsList.add(new TextElement(s));
					break;
				default:
					break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractDataDisplay#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return entry.getUniqueID();
	}

	@Override
	public AbstractEntry getEntry() {
		return entry;
	}

}
