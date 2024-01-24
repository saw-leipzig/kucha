package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.BeforeCheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCheckChangeEvent.BeforeCheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.depictions.DepictionDataDisplay.Images;
import de.cses.client.depictions.IconographySelector.IconographyKeyProvider;
import de.cses.client.depictions.IconographySelector.IconographyValueProvider;
import de.cses.client.ui.EditorListener;
import de.cses.client.user.UserLogin;
import de.cses.client.walls.CaveWallsTree;
import de.cses.shared.AbstractEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.WallTreeEntry;

public class OrnamenticIconographyTree {
	protected boolean dialogboxnotcalled = false;
	private OrnamentEntry ornamentEntry = null;
	protected boolean closeThisEditor = false;
	private VerticalLayoutContainer iconographySelectorBLC;
	private CaveWallsTree wallTree;
	private StoreFilterField<IconographyEntry> filterField;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	protected int iconographyIDforOrnamentJump;
	private IconographyEntry prevCheckedEntry;
	protected IconographyEntry ie = null;
	protected IconographyEntry selectedie = null;
	private Tree<IconographyEntry, String> iconographyTree;
	private TreeStore<IconographyEntry> iconographyTreeStore;
	private Tree<IconographyEntry, String> selectedIconographyTree;
	private TreeStore<IconographyEntry> selectedIconographyTreeStore;
	protected Map<String, IconographyEntry> selectedIconographyMap;
	protected boolean iconographyIDUsed = false;
	private void addparent(IconographyEntry item) {
	
		//Util.doLogging(item.getText());
		if(item.getParentID() != 0) {
			addparent(iconographyTreeStore.getParent(item));
			selectedIconographyTreeStore.add(iconographyTreeStore.getParent(item), item);
			selectedIconographyTree.setExpanded(iconographyTreeStore.getParent(item),true);
		}
		else {
			selectedIconographyTreeStore.add(item);
		}
		
	}
	private void processParentIconographyEntry( IconographyEntry item) {
		for (IconographyEntry child : item.getChildren()) {
			iconographyTreeStore.add(item, child);
			if (child.getChildren() != null) {
				processParentIconographyEntry(child);
			}
		}
	}

	public OrnamenticIconographyTree(OrnamentEntry oe) {
		
		ornamentEntry = oe;;

		selectedIconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		selectedIconographyTree = new Tree<IconographyEntry, String>(selectedIconographyTreeStore, new IconographyValueProvider());
		selectedIconographyTreeStore.clear();
		TreeStyle treeStyle = new TreeStyle(); 
		treeStyle.setNodeCloseIcon(Images.INSTANCE.foo());
		treeStyle.setNodeOpenIcon(Images.INSTANCE.foo());
		selectedIconographyTree.setCheckable(false);
		selectedIconographyTree.setStyle(treeStyle);
		selectedIconographyTree.setAutoExpand(true);
		iconographyTreeStore = new TreeStore<IconographyEntry>(new IconographyKeyProvider());
		iconographyTreeStore.clear();
		iconographyTree = new Tree<IconographyEntry, String>(iconographyTreeStore, new IconographyValueProvider());
		for (IconographyEntry item : StaticTables.getInstance().getIconographyEntries().values()) {
			//if (item.getIconographyID()==3) {
				iconographyTreeStore.add(item);
				if (item.getChildren() != null) {
					processParentIconographyEntry(item);
				}
		
			//}
		}
		iconographyTree.setCheckable(true);
		//		iconographyTree = IconographySelector.buildTree(true);
		iconographyTree.setCheckStyle(CheckCascade.PARENTS);
		iconographyTree.addBeforeCheckChangeHandler(new BeforeCheckChangeHandler<IconographyEntry>(){
			public void onBeforeCheckChange(BeforeCheckChangeEvent<IconographyEntry> event) {
				iconographyIDUsed=false;

				//Util.doLogging("BeforeChecked was called. "+event.getItem().getText());
				if ((ie==null) && (!iconographyIDUsed)) {
					Util.doLogging(Integer.toString(event.getItem().getIconographyID()));

					//Util.doLogging("Checked are resetted.");					
					ie=event.getItem();
					iconographyTree.setCheckedSelection(null);
					selectedie=event.getItem();
				}
				if (iconographyIDUsed) {
//					event.setCancelled(true);				

				}

					if (iconographyTree.getStore().getRootItems().contains(event.getItem())) {
						ie=null;
					}
				
			}
		});

		iconographyTree.addCheckChangeHandler(new CheckChangeHandler<IconographyEntry>(){
			public void onCheckChange(CheckChangeEvent<IconographyEntry> event) {
				boolean found = false;
				if (prevCheckedEntry != null) {
					for (IconographyEntry ie: event.getItem().getChildren()) {
						if (ie.getIconographyID() == prevCheckedEntry.getIconographyID()) {
							found = true;
						}
					}
				
				}
				prevCheckedEntry = event.getItem();
				if (!found) {
					if (event.getChecked() == Tree.CheckState.CHECKED ) {
						Util.doLogging("OnCheckChange was called. "+event.getItem().getText());
						Util.doLogging("Checkstate was. "+event.getChecked() );
						dbService.iconographyIDisUsed(event.getItem().getIconographyID(), ornamentEntry!=null ? ornamentEntry.getTypicalID() : 0, new AsyncCallback<Boolean>(){

							@Override
							public void onFailure(Throwable caught) {
								// Util.doLogging(("Update failed"+ caught.getMessage()));
								Info.display("Update failed", caught.getMessage());
							}

							@Override
							public void onSuccess(Boolean result) {
//								Util.doLogging("EventIconographyID is: "+event.getItem().getIconographyID());
//								Util.doLogging("IconographyIDUsed: "+Boolean.toString(iconographyIDUsed));
								iconographyIDUsed=result;
								dialogboxnotcalled=result;
								//Util.doLogging("set dialogboxnotcalled="+Boolean.toString(dialogboxnotcalled));
								//Util.doLogging("IconographyIDUsed - after: "+Boolean.toString(iconographyIDUsed));					
								
								if ((iconographyIDUsed)&&(iconographyTree.isChecked(event.getItem()))) {
									iconographyIDforOrnamentJump=event.getItem().getIconographyID();
									//Util.doLogging("EventIconographyID is checked: "+Boolean.toString(iconographyTree.isChecked(event.getItem())));
									iconographyTree.setCheckedSelection(null);		
									if (dialogboxnotcalled) {
										dialogboxnotcalled=false;

										Util.showYesNo("Ornament already assigned", "Do you wish to open the ornament entry, to which it was assigned?", new SelectHandler() {
											
											@Override
											public void onSelect(SelectEvent event) {
												dialogboxnotcalled=false;
												closeThisEditor=true;
												dbService.getOrnamentsWHERE("IconographyID = "+Integer.toString(iconographyIDforOrnamentJump), new AsyncCallback<ArrayList<OrnamentEntry>>() {

													public void onFailure(Throwable caught) {
														caught.printStackTrace();
													}

													@Override
													public void onSuccess(ArrayList<OrnamentEntry> result) {
														Util.doLogging("Länge des Ergebnisses: "+Integer.toString(result.size()));
														if (result.size()==1) {

															if (closeThisEditor) {
																closeThisEditor=false;
																//closeEditor(ornamentEntry);	
																DialogBox ornamenticEditorPanel = new DialogBox(false);
																EditorListener el = new EditorListener() {
																	
																	@Override
																	public void closeRequest(AbstractEntry entry) {
																		ornamenticEditorPanel.hide();
																	}
																	public Integer getClickNumber() {
																		return 0;
																	}
																	public void addClickNumber() {
																	}
																	public void setClickNumber(int clicks) {
																	}

//																	@Override
//																	public void updateEntryRequest(AbstractEntry updatedEntry) { }
																};
																OrnamenticEditor oe = new OrnamenticEditor(result.remove(0), el);
																//oe.addEditorListener( );
																ornamenticEditorPanel.add(oe);
																ornamenticEditorPanel.center();
															}
															
															
														}
													}
												});
											}
										}, new SelectHandler() {
						
											@Override
											public void onSelect(SelectEvent event) {
												dialogboxnotcalled=false;
												closeThisEditor=false;
												
											}}, new KeyDownHandler() {
						
											@Override
											public void onKeyDown(KeyDownEvent e) {
												if (e.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
													dialogboxnotcalled=false;
													closeThisEditor=false;

											}}}
										);
										//Util.doLogging("reset dialogboxnotcalled="+Boolean.toString(dialogboxnotcalled));
									}
								} else {
									selectedie = event.getItem();
								}
							}
						
						});								
						
					}
				}
		


			}
		});
		iconographySelectorBLC = new VerticalLayoutContainer();
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
		iconographySelectorBLC.add(iconographyTree, new VerticalLayoutData(1.0, .95));
		iconographySelectorBLC.add(filterField, new VerticalLayoutData(1.0, .05));

		
		wallTree = new CaveWallsTree( null);


	}
	public void refreshTrees() {
		Util.doLogging("refreshTree started with selectedIE: "+selectedie.getText());
		selectedIconographyTreeStore.clear();
		wallTree.wallTreeStore.clear();
		if (iconographyTree.getCheckedSelection().size()>0) {
			addparent(selectedie);
			
			dbService.getWallTreeEntriesByIconographyID(selectedie.getIconographyID(),UserLogin.getInstance().getSessionID(), new AsyncCallback<ArrayList<WallTreeEntry>>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
				}

				public void onSuccess(ArrayList<WallTreeEntry> result) {
//					for (WallTreeEntry we : result){
//						Util.doLogging("Erhalten Wall:"+we.getText()+" Positions: "+Integer.toString(we.getPosition().size()));
//					}
					wallTree.setWallTreeStore(result);
					
				}
			});
			
		}

	}
	public void setTreeStore() {
		for (IconographyEntry ie: iconographyTreeStore.getAll()) {
			if (ie.getIconographyID()==ornamentEntry.getIconographyID()) {
				iconographyTree.setChecked(ie, CheckState.CHECKED);
				addparent(ie);
				selectedie=ie;
				dbService.getWallTreeEntriesByIconographyID(selectedie.getIconographyID(),UserLogin.getInstance().getSessionID(), new AsyncCallback<ArrayList<WallTreeEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						caught.printStackTrace();
					}

					public void onSuccess(ArrayList<WallTreeEntry> result) {
//						for (WallTreeEntry we : result){
//							if (we.getPosition()!=null){
//								Util.doLogging("Erhalten Wall:"+we.getText()+" Positions: "+Integer.toString(we.getPosition().size()));											
//							}
//							else {
//								Util.doLogging("Erhalten Wall:"+we.getText()+" Positions:  - ");
//								
//							}
//						}
						wallTree.setWallTreeStore(result);
					}
				});
				break;
			}
			
		}

	}
	public IconographyEntry getSelectedie() {
		return selectedie;
		
	}
	public VerticalLayoutContainer getIcoTree() {
		return iconographySelectorBLC;
		
	}
	public Tree<IconographyEntry, String> getSelectedIcoTree() {
		return selectedIconographyTree;
		
	}
	public CaveWallsTree getWalls() {
		return wallTree;
	}
	public void setDialogboxnotcalled(Boolean dialogboxnotcalled) {
		this.dialogboxnotcalled=dialogboxnotcalled;
	}
}
