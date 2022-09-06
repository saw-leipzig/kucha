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
package de.cses.client.bibliography;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreFilter;
import com.sencha.gxt.data.shared.event.StoreFilterEvent;
import com.sencha.gxt.data.shared.event.StoreFilterEvent.StoreFilterHandler;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent;
import com.sencha.gxt.widget.core.client.event.CellDoubleClickEvent.CellDoubleClickHandler;
import com.sencha.gxt.widget.core.client.event.GridEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

/**
 * @author alingnau
 *
 */
public class BibliographySelector implements IsWidget {

	interface BibliographyProperties extends PropertyAccess<AnnotatedBibliographyEntry> {
		@Path("annotatedBibliographyID")
		ModelKeyProvider<AnnotatedBibliographyEntry> key();

		@Path("titleORG")
		ValueProvider<AnnotatedBibliographyEntry, String> title();

		ValueProvider<AnnotatedBibliographyEntry, String> getTitleFull();

		
		ValueProvider<AnnotatedBibliographyEntry, String> authors();
		@Path("yearORG")
		ValueProvider<AnnotatedBibliographyEntry, String> year();
		@Path("quotedPages")
		ValueProvider<AnnotatedBibliographyEntry, String> pages();
	}
	
//	private ContentPanel mainPanel = null;
	private BibliographyProperties bibProps = GWT.create(BibliographyProperties.class);
	private AnnotatedBibliographyViewTemplates rowExpanderTemplates = GWT.create(AnnotatedBibliographyViewTemplates.class);
	private Grid<AnnotatedBibliographyEntry> grid = null;
	private CheckBoxSelectionModel<AnnotatedBibliographyEntry> selectionModel;
	private List<AnnotatedBibliographyEntry> selectedEntries;
	private StoreFilterField<AnnotatedBibliographyEntry> titleField;
	private StoreFilterField<AnnotatedBibliographyEntry> AuthorsField;
	private StoreFilterField<AnnotatedBibliographyEntry> yearField;
	protected Map<Integer, AnnotatedBibliographyEntry> selectedBibMap= new HashMap<Integer, AnnotatedBibliographyEntry>();
	VerticalLayoutContainer gridVP; 
	ScrollPanel gridVLC;
    HorizontalLayoutContainer gridHP;
    //FramedPanel mainGrid = new FramedPanel();
    ListStore<AnnotatedBibliographyEntry> sourceStore;
    boolean showdialog = false;
    boolean itemSelected = false;
    AnnotatedBibliographyEntry selectedEntry = null;
    EditorListener el = null;
	/**
	 * 
	 */
	public BibliographySelector(List<AnnotatedBibliographyEntry> selectedEntries) {
		this.selectedEntries = selectedEntries;
	}
	public BibliographySelector(List<AnnotatedBibliographyEntry> selectedEntries,EditorListener el) {
		this.selectedEntries = selectedEntries;
		this.el=el;
	}
	
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (grid == null) {
			createUI();
		}
		else{
				//sourceStore.clear();
		    if (selectedEntries != null && selectedEntries.size() > 0) {
		    	selectionModel.setSelection(selectedEntries);
		    	selectedBibMap.clear();
		    	for (AnnotatedBibliographyEntry abe : selectedEntries) {
		    		if (grid.getStore().findModel(abe)!=null) {
			    		grid.getStore().findModel(abe).setQuotedPages(abe.getQuotedPages());
			    		selectedBibMap.put(abe.getAnnotatedBibliographyID(), abe);		    			
		    		}
		    	}
		    	if (el!=null) {
		    		el.setClickNumber(0);
		    	}
			}
		}
		return gridVP;
	}
	public void setwidth(int width, int height) {
		gridVP.setWidth(width);
		gridVP.setHeight((height/100)*95);
	 	//titleField.setWidth(((int)width/100*50));;
		//AuthorsField.setWidth(((int)width/100*40));
		//yearField.setWidth(((int)width/100*10));
		//Util.doLogging("Width: "+Integer.toString(width)+" - "+Integer.toString(((int)width/100*50))+" - "+Integer.toString(((int)width/100*40))+" - "+Integer.toString(((int)width/100*10)));

		
	}
	private void setPage(AnnotatedBibliographyEntry bibEntry) {
		PopupPanel addPageDialog = new PopupPanel();
		FramedPanel pageFP = new FramedPanel();
		pageFP.setHeading("Set Pages");
		TextField pageField = new TextField();
		pageField.setValue("");
		pageField.setWidth(200);
		pageFP.add(pageField);
		TextButton saveButton = new TextButton("save");
		selectedEntry=bibEntry;
		if (bibEntry.getQuotedPages() != "") {
			pageField.setText(bibEntry.getQuotedPages());
		}
		saveButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Util.doLogging("jier??");
					selectedEntry.setQuotedPages(pageField.getValue());
//					dbService.insertVendorEntry(vEntry, new AsyncCallback<Integer>() {
//
//						@Override
//						public void onFailure(Throwable caught) {
//							caught.printStackTrace();
//						}
//
//						@Override
//						public void onSuccess(Integer result) {
//							vEntry.setVendorID(result);
//							vendorEntryLS.add(vEntry);
//						}
//					});
					addPageDialog.hide();
					//Info.display("gehwähltes Item",event.getSelectedItem().getQuotedPages());
					//grid.sync(true);
					grid.getView().refresh(true);
			}
		});
		pageFP.addButton(saveButton);
		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				addPageDialog.hide();
			}
		});
		pageFP.addButton(cancelButton);
		addPageDialog.add(pageFP);
		addPageDialog.setModal(true);
		addPageDialog.center();
		saveButton.getFocusSupport();
		saveButton.focus();
		saveButton.setTabIndex(0);
		cancelButton.setTabIndex(1);

	}
	private void loadentries() {
		showdialog=false;
		Collection<AnnotatedBibliographyEntry> anBibs = StaticTables.getInstance().getBibliographyEntries().values();
	    for (AnnotatedBibliographyEntry abe : anBibs) {
	    		abe.setQuotedPages(null);
	    		sourceStore.add(abe);
	    }
	    for (AnnotatedBibliographyEntry selectedabe: selectedEntries) {
	    	if (sourceStore.findModel(selectedabe)!=null) {
		    	sourceStore.findModel(selectedabe).setQuotedPages(selectedabe.getQuotedPages());	    		
	    	}
	    }
    	showdialog=true;

	}
	/**
	 * 
	 */
	private void createUI() {
    IdentityValueProvider<AnnotatedBibliographyEntry> identity = new IdentityValueProvider<AnnotatedBibliographyEntry>();
    selectionModel = new CheckBoxSelectionModel<AnnotatedBibliographyEntry>(identity);
    selectionModel.setSelectionMode(SelectionMode.SIMPLE);
    SelectionHandler<AnnotatedBibliographyEntry> sh = new SelectionHandler<AnnotatedBibliographyEntry>() {
		
		@Override
		public void onSelection(SelectionEvent<AnnotatedBibliographyEntry> event) {
			if (el!=null) {
				el.addClickNumber();
				//Info.display("Click",Integer.toString(el.getClickNumber()));
			}
			if (showdialog) {
				
				//Util.doLogging("selectedItem: "+event.getSelectedItem().getTitleORG());;
				selectedBibMap.put(event.getSelectedItem().getAnnotatedBibliographyID(), event.getSelectedItem());
				//Util.doLogging("selectedBibMap: ");
				//for (AnnotatedBibliographyEntry abe2 : selectedBibMap.values()) {
	    			//Util.doLogging(abe2.getTitleORG());
	    		//}
	 
				setPage(event.getSelectedItem());
				itemSelected=true;
				
			}
				

			//AnnotatedBibliographyEntry ie = event.getSelectedItem();
//			Info.display("Called", "onSelecetion");
//			for (AnnotatedBibliographyEntry abe : selectedIconographyMap.values()) {
//				Util.doLogging(abe.getTitleORG()+ " - "+Boolean.toString(event.getSelection().contains(abe)));
//			}
			
			
//			if (event.getChecked() == CheckState.CHECKED) {
//				if (!selectedIconographyMap.containsKey(ie.getUniqueID())) {
//					selectedIconographyMap.put(ie.getUniqueID(), ie);
//				}
//			} else {
//				selectedIconographyMap.remove(ie.getUniqueID());
//			}
		}};
    selectionModel.addSelectionHandler(sh);
    selectionModel.addSelectionChangedHandler(new SelectionChangedHandler<AnnotatedBibliographyEntry>() {
				@Override
				public void onSelectionChanged(SelectionChangedEvent<AnnotatedBibliographyEntry> event) {
					Util.doLogging("");
					if ((!itemSelected)&(showdialog)){
						for (Map.Entry<Integer, AnnotatedBibliographyEntry> entry : selectedBibMap.entrySet()) {
							//Util.doLogging(Integer.toString(entry.getKey()));
							boolean found=false;
							for (AnnotatedBibliographyEntry abe:grid.getSelectionModel().getSelectedItems()) {
								if (abe.getAnnotatedBibliographyID()==entry.getKey()) {
									found=true;
									break;
								}
							}
							if (!found) {
								//Util.doLogging(abe.getTitleORG()+" noch vorhanden.");
	//							}
	//						else {
								if (grid.getStore().findModelWithKey(Integer.toString(entry.getKey()))!=null) {
									//Util.doLogging(abe.getTitleORG()+" wird gelöscht.");
									selectedBibMap.remove(entry.getKey());
									grid.getStore().findModelWithKey(Integer.toString(entry.getKey())).setQuotedPages("");
									grid.getView().refresh(true);
									}
	//							else {
	//								
	//								//Util.doLogging(abe.getTitleORG()+" noch vorhanden.");
	//								}
								}
							}
					
					}
				itemSelected=false;
				}
			});
		
    RowExpander<AnnotatedBibliographyEntry> rowExpander = new RowExpander<AnnotatedBibliographyEntry>(new AbstractCell<AnnotatedBibliographyEntry>() {
			@Override
			public void render(Context context, AnnotatedBibliographyEntry value, SafeHtmlBuilder sb) {
				sb.append(rowExpanderTemplates.extendedView(value));
			}
    });		
		
		ColumnConfig<AnnotatedBibliographyEntry, String> titleOrgCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.getTitleFull(), 350, "Title");
		ColumnConfig<AnnotatedBibliographyEntry, String> authorsCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.authors(), 300, "Authors");
		ColumnConfig<AnnotatedBibliographyEntry, String> yearColumn = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.year(), 50, "Year");
		ColumnConfig<AnnotatedBibliographyEntry, String> pageColumn = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.pages(), 50, "Pages");
		ColumnConfig<AnnotatedBibliographyEntry, AnnotatedBibliographyEntry> selected = selectionModel.getColumn();
		selected.setSortable(true);
		pageColumn.setFixed(false);
//		yearColumn.setHideable(false);
//		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		yearColumn.setMenuDisabled(false);
		authorsCol.setMenuDisabled(false);
		titleOrgCol.setMenuDisabled(false);
    List<ColumnConfig<AnnotatedBibliographyEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<AnnotatedBibliographyEntry, ?>>();
   
    sourceColumns.add(selected);
    sourceColumns.add(rowExpander);
    sourceColumns.add(titleOrgCol);
    sourceColumns.add(authorsCol);
    sourceColumns.add(yearColumn);
    sourceColumns.add(pageColumn);
     ColumnModel<AnnotatedBibliographyEntry> sourceColumnModel = new ColumnModel<AnnotatedBibliographyEntry>(sourceColumns);
    sourceStore = new ListStore<AnnotatedBibliographyEntry>(bibProps.key());
    StoreFilterHandler stf = new StoreFilterHandler<AnnotatedBibliographyEntry>() {
        @Override
        public void onFilter(StoreFilterEvent<AnnotatedBibliographyEntry> event) {
        	if (selectedBibMap.values().size()>0) {
            	showdialog=false;
//    			for (AnnotatedBibliographyEntry abe : selectedBibMap.values()) {
//    				selectionModel.select(abe, true);
//    			}
    			showdialog=true;
        		
        	}
          }
    };
    sourceStore.addStoreFilterHandler(stf);
    //    sourceStore.addSortInfo(new StoreSortInfo<AnnotatedBibliographyEntry>(bibProps.titleORG(), SortDir.ASC));
    //sourceStore.sor
    loadentries();
    FramedPanel fpGrid = new FramedPanel();
    showdialog=false;
    grid = new Grid<AnnotatedBibliographyEntry>(sourceStore, sourceColumnModel);
    grid.setHideMode(HideMode.OFFSETS);
    CellDoubleClickHandler doubleClick = new CellDoubleClickHandler() {

		@Override
		public void onCellClick(CellDoubleClickEvent event) {
			setPage(grid.getStore().get(event.getRowIndex()));
			Util.doLogging(Integer.toString(event.getRowIndex()));
			Util.doLogging(grid.getStore().get(event.getRowIndex()).getTitleORGFull());
		}
    };
    grid.addCellDoubleClickHandler(doubleClick);
    grid.setSelectionModel(selectionModel);
    //grid.getStore().clearSortInfo();
    //grid.getStore().addSortInfo(new Store.StoreSortInfo<AnnotatedBibliographyEntry>(bibProps..titleORG(), SortDir.ASC));
//    grid.setColumnReordering(true);
    //grid.getView().setAutoExpandColumn(titleOrgCol);
    grid.setBorders(false);
    //grid.getStore().addSortInfo(new Store.StoreSortInfo<AnnotatedBibliographyEntry>(bibProps., SortDir.ASC));
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    grid.getView().setForceFit(true);
    gridVP = new VerticalLayoutContainer();
    gridVLC = new ScrollPanel();
    gridHP = new HorizontalLayoutContainer();
    titleField = new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
					return true;
				}
				else if (selectionModel.getSelectedItems().contains(item)){
					return true;
				}
				else {
		        	  if (item.getTitleEN().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
		        	  else if (item.getTitleORG().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
		        	  else if (item.getTitleTR().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
		        	  else if (item.getSubtitleEN().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
		        	  else if (item.getSubtitleORG().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
		        	  else if (item.getSubtitleTR().toLowerCase().contains(filter.toLowerCase())) {
		        		  return true;
		        	  }
				}
			return false;
		}
	};
	FramedPanel fpTitleField = new FramedPanel();
	//fpTitleField.setWidth(350);
	fpTitleField.setHeading("Title");
	fpTitleField.add(titleField);
    AuthorsField= new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
					return true;
				}
				else if (selectionModel.getSelectedItems().contains(item)){
					return true;
				}
				else {
					if (item.getAuthorList()!=null) {
						if (!item.getAuthorList().isEmpty()) {
			        	  for (AuthorEntry ae : item.getAuthorList()) {
			        		  try {	
			        			  if (ae.getName()!=null) {
					        		  if ((!ae.getName().isEmpty())&(!filter.isEmpty())) {
						        		  if (ae.getName().toLowerCase().contains(filter.toLowerCase())) {
						        			  return true;
						        		  }		        			  
					        		  }			        				  
			        			  }
			        			  if (ae.getAlias()!=null) {
					        		  if ((!ae.getAlias().isEmpty())&(!filter.isEmpty())) {
					        			  if (ae.getAlias().toLowerCase().contains(filter.toLowerCase())) {
					        				  return true;
					        			  }
					        		  }
			        				  
			        			  }
			        		  }
				        	  catch(Exception e) {
				        		  Util.doLogging(e.getMessage());
				        		  Util.doLogging(ae.getName());
	
				        	  }
			        	  }
							
						}
					}
					if (item.getAuthorList()!=null) {
						if (!item.getAuthorList().isEmpty()) {
			        	  for (AuthorEntry ae : item.getEditorList()) {
							try {
			        			  if (ae.getName()!=null) {
					        		  if ((!ae.getName().isEmpty())&(!filter.isEmpty())) {
						        		  if (ae.getName().toLowerCase().contains(filter.toLowerCase())) {
						        			  return true;
						        		  }		        			  
					        		  }			        				  
			        			  }
			        			  if (ae.getAlias()!=null) {
					        		  if ((!ae.getAlias().isEmpty())&(!filter.isEmpty())) {
					        			  if (ae.getAlias().toLowerCase().contains(filter.toLowerCase())) {
					        				  return true;
					        			  }
					        		  }
			        				  
			        			  }
			        	  }		        	  
							catch(Exception e) {
			        		  Util.doLogging(e.getMessage());
			        		  Util.doLogging(ae.getName());
	
			        	    }
						  }
							
						}
					}


				}
			return false;
		}
	};
	FramedPanel fpAuthorsField = new FramedPanel();
	fpAuthorsField.setHeading("Authors");
	fpAuthorsField.add(AuthorsField, new VerticalLayoutData(1,1));
	//fpAuthorsField.addResizeHandler(new ResizeHandler() {
	//		public void onResize(ResizeEvent event) {
	//			AuthorsField.setWidth(fpAuthorsField.getOffsetWidth());
	//			//bibliographySelector.setwidth(Integer.toString(mainHLC.getWidget(0).getOffsetWidth()));
	//		}
	//	});
	yearField= new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
					return true;
				}
				else if (selectionModel.getSelectedItems().contains(item)){
					return true;
				}
				else {
		        	  if (Integer.toString(item.getYearEN()).contains(filter)) {
		        		  return true;
		        	  }
		        	  else if (item.getYearORG().contains(filter)) {
		        		  return true;
		        	  }
				}

			return false;
		}
	};
	FramedPanel fpYearField = new FramedPanel();

	fpYearField.setHeading("Year");
	//fpYearField.setWidth(50);
	fpYearField.add(yearField);
 	titleField.bind(sourceStore);
	AuthorsField.bind(sourceStore);
	yearField.bind(sourceStore);
    gridHP.add(fpTitleField, new HorizontalLayoutData(0.4,1));
    gridHP.add(fpAuthorsField, new HorizontalLayoutData(0.4,1));
    gridHP.add(fpYearField, new HorizontalLayoutData(0.2,1));
    //gridVLC.add(grid);
    //gridHP.add(gridHLC);
    gridVP.add(gridHP, new VerticalLayoutData(1,0.1));
    gridVP.add(grid, new VerticalLayoutData(1,0.9));

	//mainGrid.add(gridVP);
	Resizable rs = new Resizable(gridVP);
	rs.setDynamic(true);

    // State manager, make this grid stateful
//    grid.setStateful(true);
//    grid.setStateId("bibSelector");

    StringFilter<AnnotatedBibliographyEntry> titleFilter = new StringFilter<AnnotatedBibliographyEntry>(bibProps.getTitleFull());
    StringFilter<AnnotatedBibliographyEntry> authorFilter = new StringFilter<AnnotatedBibliographyEntry>(bibProps.authors());
    StringFilter<AnnotatedBibliographyEntry> yearFilter = new StringFilter<AnnotatedBibliographyEntry>(bibProps.year());
    GridFilters<AnnotatedBibliographyEntry> filters = new GridFilters<AnnotatedBibliographyEntry>();
    filters.initPlugin(grid);
    filters.setLocal(true);
    filters.addFilter(titleFilter);
    filters.addFilter(authorFilter);
    filters.addFilter(yearFilter);
    StoreFilter<AnnotatedBibliographyEntry> filter = new StoreFilter<AnnotatedBibliographyEntry>() {
        @Override
        public boolean select(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item) {
          boolean filter = false;
          if (!titleField.getText().isEmpty()) {
        	  if (item.getTitleEN().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
        	  if (item.getTitleORG().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
        	  if (item.getTitleTR().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
        	  if (item.getSubtitleEN().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
        	  if (item.getSubtitleORG().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
        	  if (item.getSubtitleTR().toLowerCase().contains(titleField.getText().toLowerCase())) {
        		  filter=true;
        	  }
          }
          if (!AuthorsField.getText().isEmpty()) {
        	  for (AuthorEntry ae : item.getAuthorList()) {
        		  if (ae.getName().toLowerCase().contains(AuthorsField.getText().toLowerCase())) {
        			  filter=true;
        		  }
        		  if (ae.getAlias().toLowerCase().contains(AuthorsField.getText().toLowerCase())) {
        			  filter=true;
        		  }
        	  }
          }
          if (!yearField.getText().isEmpty()) {
        	  if (Integer.toString(item.getYearEN()).toLowerCase().contains(yearField.getText().toLowerCase())) {
        		  filter=true;
        	  }
          }
          
          return filter;
        }
      };
    
    rowExpander.initPlugin(grid);
    
    if (selectedEntries != null && selectedEntries.size() > 0) {
    	selectionModel.setSelection(selectedEntries);
    	selectedBibMap.clear();
    	for (AnnotatedBibliographyEntry abe : selectedEntries) {
    		if (grid.getStore().findModel(abe)!=null) {
	    		grid.getStore().findModel(abe).setQuotedPages(abe.getQuotedPages());
	    		selectedBibMap.put(abe.getAnnotatedBibliographyID(), abe);
    		}
    	}
 //   	Util.doLogging("selectedBibMap: ");
//		for (AnnotatedBibliographyEntry abe2 : selectedBibMap.values()) {
//			Util.doLogging(abe2.getTitleORG());
//		}
    		
    }
    showdialog=true;
    if (el!=null) {
    	el.setClickNumber(0);
    }
    
    // Stage manager, load the previous state
//    GridFilterStateHandler<AnnotatedBibliographyEntry> handler = new GridFilterStateHandler<AnnotatedBibliographyEntry>(grid, filters);
//    handler.loadState();
    
	}
	public void clearPages() {
		for (AnnotatedBibliographyEntry abe:selectionModel.getSelectedItems()) {
			if (grid.getStore().findModel(abe)!=null) {
				grid.getStore().findModel(abe).setQuotedPages("");
			}
		}
	}
	public ArrayList<AnnotatedBibliographyEntry> getSelectedEntries() {
		//for (AnnotatedBibliographyEntry ab : selectionModel.getSelection()) {
		//	Util.doLogging(ab.getTitleORG());
		//}
		ArrayList<AnnotatedBibliographyEntry> results = new ArrayList<AnnotatedBibliographyEntry>();
		for (AnnotatedBibliographyEntry abe : selectionModel.getSelectedItems()) {
			results.add(abe.clone());
		}
		return results;
	}
	
//	public void setSelectedEntries(List<AnnotatedBibliographyEntry> list) {
//		selectionModel.setSelection(list);
//	}

}
