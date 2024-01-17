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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.thirdparty.debugging.sourcemap.Base64;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.images.OSDLoaderImagePresenter;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.ui.OSDLoader;
import de.cses.client.ui.TextElement;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.PreservationAttributeEntry;
import de.cses.shared.WallLocationEntry;
import de.cses.shared.comparator.BibEntryComparator;

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
	protected Map<String, IconographyEntry> selectedIconographyMap;
	IconographyTree icoTree;
	private OSDLoaderImagePresenter  osdLoader;
	private String prefix = "";
	protected IconographySelector iconographySelector;
	private HashMap<Integer, IconographyEntry> allIconographyEntriesList = new HashMap<Integer, IconographyEntry>();
	public interface Images extends ClientBundle {


		public Images INSTANCE = GWT.create(Images.class);


		@Source("none.png")
		ImageResource foo();

		}
	public void resetSelection() {
		selectedIconographyMap.clear();
		if (icoTree.iconographyTree != null) {
			for (IconographyEntry entry : icoTree.iconographyTree.getCheckedSelection()) {
				icoTree.iconographyTree.setChecked(entry, CheckState.UNCHECKED);
			}
		}
	}
	public void setSelectedIconography(ArrayList<IconographyEntry> iconographyRelationList) {
		resetSelection();
		for (IconographyEntry entry : iconographyRelationList) {
			Util.doLogging("setSelectedIconography setting entry = " + entry.getIconographyID());
			icoTree.iconographyTree.setChecked(entry, CheckState.CHECKED);
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
		for (IconographyEntry child : icoTree.iconographyTreeStore.getChildren(item)) {
			icoTree.iconographyTree.setExpanded(child, true);
			if (icoTree.iconographyTreeStore.getChildren(item) != null) {
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
		icoTree.iconographyTreeStore.clear();
		for (IconographyEntry item : elements) {
			icoTree.iconographyTreeStore.add(item);
			if (item.getChildren() != null) {
				processParentIconographyEntry_select(icoTree.iconographyTreeStore, item,l);
			}

	  }
	}
	private void loadDepictionDataDisplay(){
		String cave = "";
		String wall = "";
		SafeUri realCaveSketchUri = null;
		DepictionViewTemplates view = GWT.create(DepictionViewTemplates.class);
		if (entry.getCave() != null) {
			if (entry.getCave().getSiteID() > 0) {
				cave += stab.getSiteEntries().get(entry.getCave().getSiteID()).getShortName() + ": ";
			}
			cave += entry.getCave().getOfficialNumber() + ((entry.getCave().getHistoricName() != null && entry.getCave().getHistoricName().length() > 0) ? " (" + entry.getCave().getHistoricName() + ")" : ""); 
			realCaveSketchUri = UriUtils.fromString("/resource?cavesketch=" + entry.getCave().getOptionalCaveSketch() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
			if ( entry.getWallID() > 0) {
				WallLocationEntry dummy = stab.getWallLocationEntries().get(entry.getCave().getWall(entry.getWallID()).getWallLocationID());
				if (dummy!=null) {
					
				wall =dummy.getLabel();
				}
						
			}
		}
		String shortname = entry.getShortName() != null ? entry.getShortName() : "";
		String expedition = entry.getExpedition() != null ? entry.getExpedition().getName() : "";
		String vendor = entry.getVendor() != null ? entry.getVendor().getVendorName() : "";
		String location = entry.getLocation() != null ? entry.getLocation().getName() : "";
		String date = entry.getPurchaseDate() != null ? entry.getPurchaseDate().toString() : "";
		String stateOfPreservation = "";
		for (PreservationAttributeEntry pae : entry.getPreservationAttributesList()) {
			stateOfPreservation += stateOfPreservation.length() > 0 ? ", " + pae.getName() : pae.getName();
		}
		GWT.debugger();
		ImageEntry masterImage = null;
		for (ImageEntry ie: entry.getRelatedImages()) {
			if (ie.getImageID() == entry.getMasterImageID()) {
				masterImage = ie;
				break;
			}
		}
		prefix = "dataDisplay-" + Integer.toString(entry.getDepictionID()) + "-";
		String imageUri = prefix + masterImage.getFilename();

		SafeUri fullImageUri = UriUtils.fromString("");
		String style = entry.getStyleID() > 0 ? stab.getStyleEntries().get(entry.getStyleID()).getStyleName() : "";
		String modesOfRepresentation = entry.getModeOfRepresentationID() > 0 ? stab.getModesOfRepresentationEntries().get(entry.getModeOfRepresentationID()).getName() : "";
		ArrayList<AnnotatedBibliographyEntry> bibList = entry.getRelatedBibliographyList();
		// TODO ugly and hard wired but it will do the trick for now

		selectedIconographyMap = new HashMap<String, IconographyEntry>();
		icoTree= new IconographyTree(StaticTables.getInstance().getIconographyEntries().values(),entry.getRelatedIconographyList(), true);

		TreeStyle treeStyle = new TreeStyle(); 
		treeStyle.setNodeCloseIcon(Images.INSTANCE.foo());
		treeStyle.setNodeOpenIcon(Images.INSTANCE.foo());
		icoTree.iconographyTree.setCheckable(false);
		icoTree.iconographyTree.setVisible(true);
		icoTree.iconographyTree.setStyle(treeStyle);

		
		setIconographyStore(StaticTables.getInstance().getIconographyEntries().values(),entry.getRelatedIconographyList());
		for (IconographyEntry ie : icoTree.iconographyTreeStore.getRootItems()) {
			expandchildren(ie);
			icoTree.iconographyTree.setExpanded(ie, false);
			Util.doLogging(ie.getText());
		}
		Util.doLogging("Treegröße: "+Integer.toString(icoTree.iconographyTree.getStore().getAllItemsCount()));
	
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
				entry.getInventoryNumber() != null ? entry.getInventoryNumber() : "",  
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
				entry.getWidth(), entry.getHeight(),
				style, 
				modesOfRepresentation, 
				entry.getDescription() != null ? entry.getDescription() : "",
				entry.getGeneralRemarks() != null ? entry.getGeneralRemarks() : "",
				entry.getOtherSuggestedIdentifications() != null ? entry.getOtherSuggestedIdentifications() : ""

			));
		HTML htmlWidget2 = new HTML(view.displayunten(
		entry.getRelatedBibliographyList(),
		entry.getLastChangedByUser() != null ? entry.getLastChangedByUser() : "",
		entry.getModifiedOn() != null ? entry.getModifiedOn() : "",
		entry.getDescription() != null ? entry.getDescription() : "",
		entry.getGeneralRemarks() != null ? entry.getGeneralRemarks() : "",
		entry.getOtherSuggestedIdentifications() != null ? entry.getOtherSuggestedIdentifications() : ""

	));		
		StyleInjector.inject(".myCustomStyle {font-family: verdana;background:#ffcc66; font-size: 16px; }");
		htmlWidget.addStyleName("html-data-view");
		htmlWidget2.addStyleName("html-data-view");
		icoTree.iconographyTreeStore.addSortInfo(new StoreSortInfo<IconographyEntry>(new Comparator<IconographyEntry>() {
		      @Override
		      public int compare(IconographyEntry o1, IconographyEntry o2) {
		    	  return Integer.compare(o1.getIconographyID(),o2.getIconographyID());
		        
		      }
		    }, SortDir.ASC));
		
		VerticalLayoutContainer decriptionVLC = new VerticalLayoutContainer();
		decriptionVLC.addStyleName("myCustomStyle");
		decriptionVLC.add(htmlWidget);
		decriptionVLC.add(icoTree.iconographyTree);
		decriptionVLC.add(htmlWidget2);
//		shortNameVLC.add(pictoralTree);
//		shortNameVLC.add(ornamentTree);
		add(decriptionVLC, new MarginData(0, 0, 0, 0));
		setHeading((shortname.length() > 0 ? shortname + " " : "") + (cave.length() > 0 ? " in " + cave : ""));
		
	}
	/**
	 * Visualization of the content of a DepictionEntry as a HTML page (right column of UI)
	 */
	public DepictionDataDisplay(DepictionEntry e) {
		super();
		super.addExpandHandler(new ExpandHandler() {

			@Override
			public void onExpand(ExpandEvent event) {
				Info.display("expanding", "started");
				osdLoader.setosd();
			}
			
		});
		entry = e;
		loadDepictionDataDisplay();
		ImageEntry masterImage = null;
		for (ImageEntry ie: entry.getRelatedImages()) {
			if (ie.getImageID() == entry.getMasterImageID()) {
				masterImage = ie;
				break;
			}
		}

		osdLoader = new OSDLoaderImagePresenter(masterImage, prefix);
		
		
	}
	public void setosd() {
		osdLoader.setosd();
	}
	private static native String b64decode(String a) /*-{
	  return window.btoa(String.fromCharCode.apply(null, new Uint8Array(arrayBuffer)));;
	}-*/;
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
