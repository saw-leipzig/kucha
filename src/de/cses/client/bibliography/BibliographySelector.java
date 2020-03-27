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
import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
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
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.VendorEntry;

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
    VerticalPanel gridVP; 
    HorizontalPanel gridHP;
    ListStore<AnnotatedBibliographyEntry> sourceStore;
    boolean showdialog = false;
	/**
	 * 
	 */
	public BibliographySelector(List<AnnotatedBibliographyEntry> selectedEntries) {
		this.selectedEntries = selectedEntries;
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
				for (AnnotatedBibliographyEntry abe : selectedEntries) {
					sourceStore.findModel(abe).setQuotedPages(abe.getQuotedPages());
				}
			}

		return gridVP;
	}
	private void loadentries() {
		showdialog=false;
	    for (AnnotatedBibliographyEntry abe : StaticTables.getInstance().getBibliographyEntries().values()) {
	    		abe.setQuotedPages(null);
	    		sourceStore.add(abe);
	    }
	    for (AnnotatedBibliographyEntry selectedabe: selectedEntries) {
	    	sourceStore.findModel(selectedabe).setQuotedPages(selectedabe.getQuotedPages());
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
    selectionModel.addSelectionChangedHandler(new SelectionChangedHandler<AnnotatedBibliographyEntry>() {
				@Override
				public void onSelectionChanged(SelectionChangedEvent<AnnotatedBibliographyEntry> event) {
					List<AnnotatedBibliographyEntry> selected = event.getSelection();
					if ((grid.getSelectionModel().getSelectedItem()!=null)&(showdialog)) {
						PopupPanel addPageDialog = new PopupPanel();
						FramedPanel pageFP = new FramedPanel();
						pageFP.setHeading("Set Pages");
						TextField pageField = new TextField();
						pageField.setValue("");
						pageField.setWidth(200);
						pageFP.add(pageField);
						TextButton saveButton = new TextButton("save");
						saveButton.addSelectHandler(new SelectHandler() {

							@Override
							public void onSelect(SelectEvent event) {
									grid.getSelectionModel().getSelectedItem().setQuotedPages(pageField.getValue());
//									dbService.insertVendorEntry(vEntry, new AsyncCallback<Integer>() {
	//
//										@Override
//										public void onFailure(Throwable caught) {
//											caught.printStackTrace();
//										}
	//
//										@Override
//										public void onSuccess(Integer result) {
//											vEntry.setVendorID(result);
//											vendorEntryLS.add(vEntry);
//										}
//									});
									addPageDialog.hide();
									Info.display("gehwähltes Item",grid.getSelectionModel().getSelectedItem().getQuotedPages());
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
						
					}
						
				}
			});
		
    RowExpander<AnnotatedBibliographyEntry> rowExpander = new RowExpander<AnnotatedBibliographyEntry>(new AbstractCell<AnnotatedBibliographyEntry>() {
			@Override
			public void render(Context context, AnnotatedBibliographyEntry value, SafeHtmlBuilder sb) {
				sb.append(rowExpanderTemplates.extendedView(value));
			}
    });		
		
		ColumnConfig<AnnotatedBibliographyEntry, String> titleOrgCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.title(), 350, "Title");
		ColumnConfig<AnnotatedBibliographyEntry, String> authorsCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.authors(), 300, "Authors");
		ColumnConfig<AnnotatedBibliographyEntry, String> yearColumn = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.year(), 50, "Year");
		ColumnConfig<AnnotatedBibliographyEntry, String> pageColumn = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.pages(), 50, "Pages");
		pageColumn.setFixed(false);
//		yearColumn.setHideable(false);
//		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		yearColumn.setMenuDisabled(false);
		authorsCol.setMenuDisabled(false);
		titleOrgCol.setMenuDisabled(false);
    List<ColumnConfig<AnnotatedBibliographyEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<AnnotatedBibliographyEntry, ?>>();
   
    sourceColumns.add(selectionModel.getColumn());
    sourceColumns.add(rowExpander);
    sourceColumns.add(titleOrgCol);
    sourceColumns.add(authorsCol);
    sourceColumns.add(yearColumn);
    sourceColumns.add(pageColumn);
     ColumnModel<AnnotatedBibliographyEntry> sourceColumnModel = new ColumnModel<AnnotatedBibliographyEntry>(sourceColumns);
    sourceStore = new ListStore<AnnotatedBibliographyEntry>(bibProps.key());

//    sourceStore.addSortInfo(new StoreSortInfo<AnnotatedBibliographyEntry>(bibProps.titleORG(), SortDir.ASC));
    loadentries();
    FramedPanel fpGrid = new FramedPanel();
    showdialog=false;
    grid = new Grid<AnnotatedBibliographyEntry>(sourceStore, sourceColumnModel);
    grid.setHideMode(HideMode.OFFSETS);
    grid.setSelectionModel(selectionModel);
//    grid.setColumnReordering(true);
    //grid.getView().setAutoExpandColumn(titleOrgCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    grid.getView().setForceFit(true);
    gridVP = new VerticalPanel();
    gridHP = new HorizontalPanel();
    titleField = new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
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
	fpTitleField.add(titleField);
	fpTitleField.setHeading("Title");
	fpTitleField.setWidth(400);
    AuthorsField= new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
					return true;
				}
				else {
		        	  for (AuthorEntry ae : item.getAuthorList()) {
		        		  try {
		        		  if ((!ae.getName().isEmpty())&(!filter.isEmpty())) {
			        		  if (ae.getName().toLowerCase().contains(filter.toLowerCase())) {
			        			  return true;
			        		  }		        			  
		        		  }
		        		  if ((!ae.getAlias().isEmpty())&(!filter.isEmpty())) {
		        			  if (ae.getAlias().toLowerCase().contains(filter.toLowerCase())) {
		        				  return true;
		        			  }
		        		  }}
			        	  catch(Exception e) {
			        		  Util.doLogging(e.getMessage());
			        		  Util.doLogging(ae.getName());

			        	  }
		        	  }
		        	  for (AuthorEntry ae : item.getEditorList()) {
						try {
		        		  if ((!ae.getName().isEmpty())&(!filter.isEmpty())) {
			        		  if (ae.getName().toLowerCase().contains(filter.toLowerCase())) {
			        			  return true;
			        		  }		        			  
		        		  }
		        		  if ((!ae.getAlias().isEmpty())&(!filter.isEmpty())) {
		        			  if (ae.getAlias().toLowerCase().contains(filter.toLowerCase())) {
		        				  return true;
		        			  }
		        		  }
		        	  }		        	  catch(Exception e) {
		        		  Util.doLogging(e.getMessage());
		        		  Util.doLogging(ae.getName());

		        	  }
					}

				}
			return false;
		}
	};
	FramedPanel fpAuthorsField = new FramedPanel();
	fpAuthorsField.setHeading("Authors");
	fpAuthorsField.add(AuthorsField);
	fpAuthorsField.setWidth(350);
	yearField= new StoreFilterField<AnnotatedBibliographyEntry>(){

		@Override
		protected boolean doSelect(Store<AnnotatedBibliographyEntry> store, AnnotatedBibliographyEntry parent, AnnotatedBibliographyEntry item, String filter) {
				if (filter.isEmpty()){
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
	fpYearField.add(yearField);
	fpYearField.setWidth(50);
 	titleField.bind(sourceStore);
	AuthorsField.bind(sourceStore);
	yearField.bind(sourceStore);
    gridHP.add(fpTitleField);
    gridHP.add(fpAuthorsField);
    gridHP.add(fpYearField);
    gridVP.add(gridHP);
    gridVP.add(grid);
    // State manager, make this grid stateful
//    grid.setStateful(true);
//    grid.setStateId("bibSelector");

    StringFilter<AnnotatedBibliographyEntry> titleFilter = new StringFilter<AnnotatedBibliographyEntry>(bibProps.title());
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
    }
    showdialog=true;
    
    // Stage manager, load the previous state
//    GridFilterStateHandler<AnnotatedBibliographyEntry> handler = new GridFilterStateHandler<AnnotatedBibliographyEntry>(grid, filters);
//    handler.loadState();
    
	}
	public void clearPages() {
		for (AnnotatedBibliographyEntry abe:selectionModel.getSelectedItems()) {
			grid.getStore().findModel(abe).setQuotedPages("");
		}
	}
	public ArrayList<AnnotatedBibliographyEntry> getSelectedEntries() {
		return new ArrayList<AnnotatedBibliographyEntry>(selectionModel.getSelectedItems());
	}
	
//	public void setSelectedEntries(List<AnnotatedBibliographyEntry> list) {
//		selectionModel.setSelection(list);
//	}

}
