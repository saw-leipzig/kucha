package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.user.UserLogin;
import de.cses.shared.IconographyEntry;



public class IconographyTree {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public TreeStore<IconographyEntry> iconographyTreeStore;
	public Tree<IconographyEntry, String> iconographyTree;
	public Map<String, IconographyEntry> selectedIconographyMap;
	private Integer ImageID;
	private Map<Integer, String> imageMap =null;
	private void initpics() {
    	dbService.getMasterImageFromOrnament(300, UserLogin.getInstance().getUsernameSessionIDParameterForUri(),  new AsyncCallback<Map<Integer,String>>() {
    		@Override
    		public void onFailure(Throwable caught) {
    			
    		}

    		@Override
    		public void onSuccess(Map<Integer,String> imageID) {
    			imageMap=imageID;
    	}});
      }
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
	public IconographyTree(Collection<IconographyEntry> elements, ArrayList<IconographyEntry> l, boolean dropunselected) {
		initpics();
		setIconographyStore(elements,l,dropunselected);
		buildTree(false);
	}
	private void processParentIconographyEntry( IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			iconographyTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry(child);
			}
		}
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
	private void buildTreeStore(Collection<IconographyEntry> elements, boolean ornaments){
		iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
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
	public void setIconographyStore(Collection<IconographyEntry> elements, ArrayList<IconographyEntry> l, boolean dropunselected) {
		buildTreeStore(elements, false);
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
	}
	public void buildTree( boolean ornament){
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

	}

	
}
