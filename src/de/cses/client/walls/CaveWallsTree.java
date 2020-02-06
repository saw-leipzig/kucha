package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.dom.client.Style.FontWeight;
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


public class CaveWallsTree {
	public TreeStore<WallTreeEntry> wallTreeStore;
	public Tree<WallTreeEntry, String> wallTree;
	public Map<String, WallTreeEntry> selectedwallMap;
	private CaveEntry cEntry;
	private ArrayList<WallTreeEntry> allEntries;

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
	public CaveWallsTree(List<WallTreeEntry> wallIDs) {
		buildTree(wallIDs);
		setWallTreeStore( wallIDs);
	}

	private void processParentWallTreeEntry( WallTreeEntry item) {
		
			
			
			switch (cEntry.getCaveTypeID()) {

			case 2: // square cave
				//Util.doLogging("cEntry.getCaveTypeID: "+Integer.toString(cEntry.getCaveTypeID()));
				for (WallTreeEntry child : item.getChildren()) {
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

	private void buildTreeStore(){
		wallTreeStore = new TreeStore<WallTreeEntry>(new WallTreeEntryKeyProvider());
		wallTreeStore.clear();
		allEntries = new ArrayList<WallTreeEntry>();
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
	void addnext(WallTreeEntry wte, List<WallTreeEntry> wallIDs) {
		for (WallTreeEntry item : wallIDs) {
			if (item.getParentID()==wte.getWallLocationID()) {
				boolean notInTree=false;
				if (wallTreeStore.findModel(item)!=null) {
					if (wallTreeStore.findModel(item).getPosition()==null)
						wallTreeStore.remove(item);
						wallTreeStore.add(wte, item);
				}
				else {
					wallTreeStore.add(wte, item);
				}
					
										
				addnext(item, wallIDs);
			}
		}
	}

	public void setWallTreeStore( List<WallTreeEntry> wallIDs) {
		if (wallIDs != null){
			Util.doLogging("Länge von Elements:"+ Integer.toString(wallIDs.size()));
			for (WallTreeEntry item : wallIDs) {
				if ((item.getParentID()==0)) {
					wallTreeStore.add(item);
					addnext(item,wallIDs);
				}
				}
			}

		}


			


	public void buildTree(List<WallTreeEntry> wallIDs){
		buildTreeStore();
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
            @Override
            protected void update() {
                super.update();
                for (WallTreeEntry item : this.getStore().getAll()) {
                    // Put your condition here
                    if (item.getParentID()==0) {
                        // Severe nullchecking is important. Soon after adding items to store, there might not be corresponding nodes. If there are nodes, there might not be Elements or they have no Style yet.
                        if (this.findNode(item) != null) {
                            if (this.getView().getIconElement(this.findNode(item)) != null && this.getView().getIconElement(this.findNode(item)).getStyle() != null) {
                                this.getView().getIconElement(this.findNode(item)).getStyle().setFontWeight(FontWeight.BOLDER);
                            }
                            if (this.getView().getTextElement(this.findNode(item)) != null && this.getView().getTextElement(this.findNode(item)).getStyle() != null) {
                                this.getView().getTextElement(this.findNode(item)).getStyle().setFontWeight(FontWeight.BOLDER);
                            }
                        }
                    }
                }
            }

		};
		wallTree.setCheckable(false);
		TreeStyle treeStyle = new TreeStyle(); 
		treeStyle.setNodeCloseIcon(Images.INSTANCE.foo());
		treeStyle.setNodeOpenIcon(Images.INSTANCE.foo());	
		wallTree.setStyle(treeStyle);
		wallTree.setAutoExpand(true);



	}

	
}
