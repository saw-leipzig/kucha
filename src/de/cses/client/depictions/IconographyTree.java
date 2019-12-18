package de.cses.client.depictions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.depictions.IconographySelector.IconographyKeyProvider;
import de.cses.client.depictions.IconographySelector.IconographyValueProvider;
import de.cses.shared.IconographyEntry;


public class IconographyTree {
	public TreeStore<IconographyEntry> iconographyTreeStore;
	public Tree<IconographyEntry, String> iconographyTree;
	public Map<String, IconographyEntry> selectedIconographyMap;
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
