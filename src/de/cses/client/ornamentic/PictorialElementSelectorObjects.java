/*
 * Copyright 2016 
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
package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.OrnamentCaveRelation;
import de.cses.shared.PictorialElementEntry;

public class PictorialElementSelectorObjects implements IsWidget {

	class KeyProvider implements ModelKeyProvider<PictorialElementEntry> {
		@Override
		public String getKey(PictorialElementEntry item) {
			return Integer.toString(item.getPictorialElementID());
		}
	}

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private TreeStore<PictorialElementEntry> store;
	private Tree<PictorialElementEntry, String> tree;
	private VerticalLayoutContainer vlc;
	private OrnamentCaveRelation ornamentCaveRelationEntry;

	public void initPictorialElementSelectorObjects() {
		store = new TreeStore<PictorialElementEntry>(new KeyProvider());
		refreshPEStore();
	}
	public PictorialElementSelectorObjects(){
		initPictorialElementSelectorObjects();
	}
	public PictorialElementSelectorObjects(OrnamentCaveRelation ornamentCaveRelation){
		this.ornamentCaveRelationEntry = ornamentCaveRelation;
		initPictorialElementSelectorObjects();
	}

	private void processParent(TreeStore<PictorialElementEntry> store, PictorialElementEntry item) {
		for (PictorialElementEntry child : item.getChildren()) {
			store.add(item, child);
			if(ornamentCaveRelationEntry != null){
			
				for(PictorialElementEntry pe: ornamentCaveRelationEntry.getPictorialElements()){
					if(pe.getPictorialElementID() == item.getPictorialElementID()){
						tree.setChecked(item, CheckState.CHECKED);
					}
				}
			}
			if (child.getChildren() != null) {
				processParent(store, child);
			}
		}
	}

	private void refreshPEStore() {
		store.clear();
		dbService.getPictorialElementsObjects(new AsyncCallback<ArrayList<PictorialElementEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				Info.display("Loading Pictorial Elements", "failed");
				tree = null;
			}

			@Override
			public void onSuccess(ArrayList<PictorialElementEntry> result) {

				for (PictorialElementEntry item : result) {
					store.add(item);
					if (item.getChildren() != null) {
						processParent(store, item);
//						Info.display("Children added", item.getText());
					}
				}
				if(ornamentCaveRelationEntry != null){
					for(PictorialElementEntry pe: ornamentCaveRelationEntry.getPictorialElements()) {
							tree.setChecked(pe, CheckState.CHECKED);
					}
				}
			}
		});
	}

	@Override
	public Widget asWidget() {
		if (vlc == null) {
			initPanel();
		}
		return vlc;
	}
	
	private void initPanel() {
		vlc = new VerticalLayoutContainer();

		tree = new Tree<PictorialElementEntry, String>(store, new ValueProvider<PictorialElementEntry, String>() {

			@Override
			public String getValue(PictorialElementEntry object) {
				return object.getText();
			}

			@Override
			public void setValue(PictorialElementEntry object, String value) {
				// TODO Auto-generated method stub

			}

			@Override
			public String getPath() {
				return "name";
			}
		});
		tree.setWidth(300);
		tree.setCheckable(true);
    tree.setCheckStyle(CheckCascade.TRI);
    tree.setAutoLoad(true);

		vlc.add(tree, new VerticalLayoutData(1, 1));
		vlc.setScrollMode(ScrollMode.AUTOY);
		
	}
	
	public List<PictorialElementEntry> getSelectedPE() {
		return tree.getCheckedSelection();
	}

	public void expandAll() {
		tree.expandAll();
	}

	public void collapseAll() {
		tree.collapseAll();
	}

	/**
	 * @return the ornamentCaveRelationEntry
	 */
	public OrnamentCaveRelation getOrnamentCaveRelationEntry() {
		return ornamentCaveRelationEntry;
	}

	/**
	 * @param ornamentCaveRelationEntry the ornamentCaveRelationEntry to set
	 */
	public void setOrnamentCaveRelationEntry(OrnamentCaveRelation ornamentCaveRelationEntry) {
		this.ornamentCaveRelationEntry = ornamentCaveRelationEntry;
		refreshPEStore();
	}
	

}
