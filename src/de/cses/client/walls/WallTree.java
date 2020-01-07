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

import de.cses.shared.CaveEntry;
import de.cses.shared.WallTreeEntry;
import de.cses.client.Util;


public class WallTree {
	public TreeStore<WallTreeEntry> wallTreeStore;
	public Tree<WallTreeEntry, String> wallTree;
	public Map<String, WallTreeEntry> selectedwallMap;
	private CaveEntry cEntry;
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
	public WallTree(Collection<WallTreeEntry> elements, ArrayList<WallTreeEntry> l, boolean dropunselected, boolean editable, CaveEntry entry) {
		cEntry= entry;
		setWallTreeStore(elements,l,dropunselected);
		buildTree(editable);
	}
	private void processParentWallTreeEntry( WallTreeEntry item) {
		
			
			
			switch (cEntry.getCaveTypeID()) {

			case 2: // square cave
				for (WallTreeEntry child : item.getChildren()) {
					if ((child.getWallLocationID() == WallTreeEntry.ANTECHAMBER_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.MAIN_CHAMBER_LABEL)||(child.getWallLocationID()<100)) {
							Util.doLogging(child.getText());
							wallTreeStore.add(item, child);
							if (child.getChildren() != null) {
								processParentWallTreeEntry(child);
							}
						}
					}
				
			case 3: // residential cave
				for (WallTreeEntry child : item.getChildren()) {
					if ((child.getWallLocationID() == WallTreeEntry.ANTECHAMBER_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.MAIN_CHAMBER_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.MAIN_CHAMBER_CORRIDOR_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.REAR_AREA_LABEL)||(child.getWallLocationID()<100)) {
						wallTreeStore.add(item, child);
						if (child.getChildren() != null) {
							processParentWallTreeEntry(child);
						}
						}
				}

			case 4: // central-pillar cave
			case 6: // monumental image cave
				for (WallTreeEntry child : item.getChildren()) {
					if ((child.getWallLocationID() == WallTreeEntry.ANTECHAMBER_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.MAIN_CHAMBER_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.REAR_AREA_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
							|| (child.getWallLocationID() == WallTreeEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL)||(child.getWallLocationID()<100)) {
						wallTreeStore.add(item, child);
						if (child.getChildren() != null) {
							processParentWallTreeEntry(child);
						}
					}
				}
			default:
				break;
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
	}
	public void setWall(int wallKey) {
		Util.doLogging("Starte Suche nach WallTreeEntry");
		for (WallTreeEntry wall : wallTreeStore.getAll()) {
			Util.doLogging("Vergleiche: "+Integer.toString(wall.getWallLocationID())+" mit "+Integer.toString(wallKey));
			if (wall.getWallLocationID()==wallKey) {
				Util.doLogging("WallTreeEntry: "+wall.getText());
			}
		}
	}

	public void setWallTreeStore(Collection<WallTreeEntry> elements, ArrayList<WallTreeEntry> l, boolean dropunselected) {
		Util.doLogging("Länge von Elements:"+ Integer.toString(elements.size()));
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
					//if (item.getParentID()==null) {
					processParentWallTreeEntry(item);
				}
			}

	  }
	}
	public void buildTree( boolean editable){
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
		wallTree.setCheckable(editable);


	}

	
}
