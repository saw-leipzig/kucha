package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;

import de.cses.client.Util;
import de.cses.client.depictions.DepictionDataDisplay.Images;
import de.cses.shared.CaveEntry;
import de.cses.shared.WallTreeEntry;


public class WallTree {
	public TreeStore<WallTreeEntry> wallTreeStore;
	public Tree<WallTreeEntry, String> wallTree;
	public Map<String, WallTreeEntry> selectedwallMap;
	private CaveEntry cEntry;
	private ArrayList<WallTreeEntry> allEntries;
	private boolean dropunselected;
	private boolean editable;
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
	public WallTree(Collection<WallTreeEntry> elements, List<WallTreeEntry> wallIDs, boolean dropunselected, boolean editable, CaveEntry entry) {
		this.cEntry= entry;
		this.dropunselected = dropunselected;
		this.editable=editable;
		buildTree(editable);
		setWallTreeStore(elements, dropunselected, wallIDs);
	}
	public WallTree(ArrayList<WallTreeEntry> elements, List<WallTreeEntry> wallIDs, boolean dropunselected, boolean editable, CaveEntry entry) {
		this.cEntry= entry;
		this.dropunselected = dropunselected;
		this.editable=editable;
		buildTree(editable);
		setWallTreeStore(elements, dropunselected, wallIDs);
	}
	private void processParentWallTreeEntry( WallTreeEntry item) {
		
			if (cEntry==null) {
				for (WallTreeEntry child : item.getChildren()) {
					child.setPosition(null);
					wallTreeStore.add(item, child);	
					allEntries.add(child);
					if (child.getChildren() != null) {
						processParentWallTreeEntry(child);
					}
				}
			}
			else {
				switch (cEntry.getCaveTypeID()) {
				
				case 2: // square cave
					//Util.doLogging("cEntry.getCaveTypeID: "+Integer.toString(cEntry.getCaveTypeID()));
					for (WallTreeEntry child : item.getChildren()) {
						child.setPosition(null);
						if (( WallTreeEntry.ANTECHAMBER_LABEL.contains(child.getWallLocationID()))
							|| (WallTreeEntry.MAIN_CHAMBER_LABEL.contains(child.getWallLocationID()))||(child.getWallLocationID()<100)) {
							//Util.doLogging(item.getText()+" - "+child.getText()+" /1");
							wallTreeStore.add(item, child);
							allEntries.add(child);
							if (child.getChildren() != null) {
								processParentWallTreeEntry(child);
								}
							}
						}
					break;
					
				case 3: // residential cave
					//Util.doLogging("cEntry.getCaveTypeID: "+Integer.toString(cEntry.getCaveTypeID()));
					for (WallTreeEntry child : item.getChildren()) {
						child.setPosition(null);
						if ((WallTreeEntry.ANTECHAMBER_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.MAIN_CHAMBER_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.MAIN_CHAMBER_CORRIDOR_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.REAR_AREA_LABEL.contains(child.getWallLocationID()))||(child.getWallLocationID()<100)) {
							//Util.doLogging(item.getText()+" - "+child.getText()+" /2");
							wallTreeStore.add(item, child);
							allEntries.add(child);
							if (child.getChildren() != null) {
								processParentWallTreeEntry(child);
							}
							}
					}
					break;
	
				case 4: // central-pillar cave
				case 6: // monumental image cave
					//Util.doLogging("cEntry.getCaveTypeID: "+Integer.toString(cEntry.getCaveTypeID()));
					for (WallTreeEntry child : item.getChildren()) {
						child.setPosition(null);
						if ((WallTreeEntry.ANTECHAMBER_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.MAIN_CHAMBER_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.REAR_AREA_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.REAR_AREA_LEFT_CORRIDOR_LABEL.contains(child.getWallLocationID()))
								|| (WallTreeEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL.contains(child.getWallLocationID()))||(child.getWallLocationID()<100)) {
							//Util.doLogging(item.getText()+" - "+child.getText()+" /3");
							wallTreeStore.add(item, child);
							allEntries.add(child);
							if (child.getChildren() != null) {
								processParentWallTreeEntry(child);
							}
						}
					}
					break;
				default:
					break;
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

	private void buildTreeStore( boolean ornaments){
		wallTreeStore = new TreeStore<WallTreeEntry>(new WallTreeEntryKeyProvider());
		wallTreeStore.clear();
		allEntries = new ArrayList<WallTreeEntry>();
	}
	public void setWall(List<WallTreeEntry> wallKeys) {
		dropunselected(wallKeys);
	}
	public void findParent(WallTreeEntry child) {
		if (child.getParentID()==0) {
		    Util.doLogging("Found: "+Integer.toString(child.getParentID())+" - "+Integer.toString(child.getWallLocationID()));
		    if (wallTreeStore.findModel(child)==null) {
		    	wallTreeStore.add(child);
		    }
		}
		else {
		for (WallTreeEntry wall : allEntries) {
			if (wall.getWallLocationID()==child.getParentID()) {

					findParent(wall);
					Util.doLogging("Found: "+Integer.toString(child.getParentID())+" - "+Integer.toString(child.getWallLocationID()));
				    if (wallTreeStore.findModel(child)==null) {
				    	wallTreeStore.add(wall, child);
				    }
				}
				
			
			}
		};
	}


	public void setWallTreeStore(Collection<WallTreeEntry> elements, boolean dropunselected, List<WallTreeEntry> wallIDs) {
		Util.doLogging("Länge von Elements:"+ Integer.toString(elements.size()));
		for (WallTreeEntry item : elements) {
			item.setPosition(null);
			wallTreeStore.add(item);
			allEntries.add(item);
			if (item.getChildren() != null) {

					//if (item.getParentID()==null) {
					processParentWallTreeEntry(item);
			}
		}
		if (dropunselected) {
			dropunselected(wallIDs);
		}
		else {
			selectitems(wallIDs);
		}

	}
	public void dropunselected(List<WallTreeEntry> wallIDs) {
		wallTreeStore.clear();
		for (WallTreeEntry wall : wallIDs) {
					findParent(wall);
			
		}
	}
		
	public void selectitems(List<WallTreeEntry> wallIDs) {
		wallTree.setCheckStyle(CheckCascade.PARENTS);
		for (WallTreeEntry wall : wallIDs) {
					Util.doLogging(Integer.toString(wall.getWallLocationID()));
					if (wall.getWallLocationID()!=0) {
						wallTreeStore.findModelWithKey(Integer.toString(wall.getWallLocationID())).setPosition(wall.getPosition());
						wallTree.setChecked(wall, CheckState.CHECKED);
					}
				}

	}
		

	public void buildTree( boolean editable){
		selectedwallMap = new HashMap<String, WallTreeEntry>();
		buildTreeStore(false);
		wallTreeStore.clear();
		allEntries.clear();
		
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
		TreeStyle treeStyle = new TreeStyle(); 
		treeStyle.setNodeCloseIcon(Images.INSTANCE.foo());
		treeStyle.setNodeOpenIcon(Images.INSTANCE.foo());	
		wallTree.setStyle(treeStyle);
		wallTree.setAutoExpand(true);



	}

	
}
