package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;

import de.cses.shared.WallEntry;
import de.cses.shared.WallTreeEntry;



public class WallTree {
	public TreeStore<WallTreeEntry> wallTreeStore;
	public Tree<WallTreeEntry, String> wallTree;
	public Map<String, WallTreeEntry> selectedwallMap;
	class WallTreeEntryKeyProvider implements ModelKeyProvider<WallTreeEntry> {
		@Override
		public String getKey(WallTreeEntry item) {
			return Integer.toString(item.getWallLocationID());
		}
	}

	class WallTreeEntryValueProvider implements ValueProvider<WallTreeEntry, String> {

		@Override
		public String getValue(WallTreeEntry object) {
			return object.getText();
		}

		@Override
		public void setValue(WallTreeEntry object, String value) { 
			object.setText(value);
		}

		@Override
		public String getPath() {
			return "name";
		}
	}
	public WallTree(Collection<WallTreeEntry> elements, ArrayList<WallTreeEntry> l, boolean dropunselected) {
		setWallTreeStore(elements,l,dropunselected);
		buildTree(false);
	}
	private void processParentWallTreeEntry( WallTreeEntry item) {
		for (WallTreeEntry child : item.getChildren()) {
			wallTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentWallTreeEntry(child);
			}
		}
	}
	private void processParentWallTreeEntry_select(WallTreeEntry item, ArrayList<WallTreeEntry> l) {	
		for (WallTreeEntry child : item.getChildren()) {
			Boolean found =new Boolean(false);
			for (WallTreeEntry entry : l) {
				if (entry.getWallLocationID()==child.getWallLocationID()){
					found=true;
					break;}}
			if (found){
				//Info.display(child.getText(), child.getText());
				wallTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentWallTreeEntry_select(child,l);
			}
		}}
	}
	private void buildTreeStore(Collection<WallTreeEntry> elements, boolean ornaments){
		wallTreeStore = new TreeStore<WallTreeEntry>(new WallTreeEntryKeyProvider());
		wallTreeStore.clear();
			for (WallTreeEntry item : elements) {
				if ((item.getWallLocationID()==3)||(!ornaments)) {
					wallTreeStore.add(item);
					if (item.getChildren() != null) {
						processParentWallTreeEntry(item);
					}
			
				}
			}
	}
	public void setWallTreeStore(Collection<WallTreeEntry> elements, ArrayList<WallTreeEntry> l, boolean dropunselected) {
		buildTreeStore(elements, false);
		wallTreeStore.clear();
		for (WallTreeEntry item : elements) {
			wallTreeStore.add(item);
			if (item.getChildren() != null) {
				if (dropunselected) {
					processParentWallTreeEntry_select(item,l);
					
				}
				else
				{
					processParentWallTreeEntry(item);
				}
			}

	  }
	}
	public void buildTree( boolean ornament){
		selectedwallMap = new HashMap<String, WallTreeEntry>();

			
		wallTree = new Tree<WallTreeEntry, String>(wallTreeStore, new WallTreeEntryValueProvider()) {

			@Override
			protected void onFilter(StoreFilterEvent<WallTreeEntry> ie) {
				super.onFilter(ie);
				for (WallTreeEntry entry : selectedwallMap.values()) {
					wallTree.setChecked(entry, CheckState.CHECKED);
				}
			}

		};
	}

	
}
