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
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.grid.CheckBoxSelectionModel;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.RowExpander;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

import de.cses.client.StaticTables;
import de.cses.shared.AnnotatedBibliographyEntry;

/**
 * @author alingnau
 *
 */
public class BibliographySelector implements IsWidget {

	interface BibliographyProperties extends PropertyAccess<AnnotatedBibliographyEntry> {
		@Path("annotatedBiblographyID")
		ModelKeyProvider<AnnotatedBibliographyEntry> key();

		@Path("titleORG")
		ValueProvider<AnnotatedBibliographyEntry, String> title();
		
		ValueProvider<AnnotatedBibliographyEntry, String> authors();
		@Path("yearORG")
		ValueProvider<AnnotatedBibliographyEntry, String> year();
	}
	
//	private ContentPanel mainPanel = null;
	private BibliographyProperties bibProps = GWT.create(BibliographyProperties.class);
	private AnnotatedBiblographyViewTemplates rowExpanderTemplates = GWT.create(AnnotatedBiblographyViewTemplates.class);
	private Grid<AnnotatedBibliographyEntry> grid = null;
	private CheckBoxSelectionModel<AnnotatedBibliographyEntry> selectionModel;
	private List<AnnotatedBibliographyEntry> selectedEntries;

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
		return grid;
	}

	/**
	 * 
	 */
	private void createUI() {
    IdentityValueProvider<AnnotatedBibliographyEntry> identity = new IdentityValueProvider<AnnotatedBibliographyEntry>();
    selectionModel = new CheckBoxSelectionModel<AnnotatedBibliographyEntry>(identity);
    selectionModel.setSelectionMode(SelectionMode.SIMPLE);
		
    RowExpander<AnnotatedBibliographyEntry> rowExpander = new RowExpander<AnnotatedBibliographyEntry>(new AbstractCell<AnnotatedBibliographyEntry>() {
			@Override
			public void render(Context context, AnnotatedBibliographyEntry value, SafeHtmlBuilder sb) {
				sb.append(rowExpanderTemplates.extendedView(value));
			}
    });		
		
		ColumnConfig<AnnotatedBibliographyEntry, String> titleOrgCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.title(), 350, "Title");
		ColumnConfig<AnnotatedBibliographyEntry, String> authorsCol = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.authors(), 300, "Authors");
		ColumnConfig<AnnotatedBibliographyEntry, String> yearColumn = new ColumnConfig<AnnotatedBibliographyEntry, String>(bibProps.year(), 50, "Year");
//		yearColumn.setHideable(false);
//		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		
    List<ColumnConfig<AnnotatedBibliographyEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<AnnotatedBibliographyEntry, ?>>();
    sourceColumns.add(selectionModel.getColumn());
    sourceColumns.add(rowExpander);
    sourceColumns.add(titleOrgCol);
    sourceColumns.add(authorsCol);
    sourceColumns.add(yearColumn);

    ColumnModel<AnnotatedBibliographyEntry> sourceColumnModel = new ColumnModel<AnnotatedBibliographyEntry>(sourceColumns);
    
    ListStore<AnnotatedBibliographyEntry> sourceStore = new ListStore<AnnotatedBibliographyEntry>(bibProps.key());
//    sourceStore.addSortInfo(new StoreSortInfo<AnnotatedBibliographyEntry>(bibProps.titleORG(), SortDir.ASC));
    for (AnnotatedBibliographyEntry abe : StaticTables.getInstance().getBibliographyEntries().values()) {
    	sourceStore.add(abe);
    }
    
    grid = new Grid<AnnotatedBibliographyEntry>(sourceStore, sourceColumnModel);
    grid.setSelectionModel(selectionModel);
//    grid.setColumnReordering(true);
    grid.getView().setAutoExpandColumn(titleOrgCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    grid.getView().setForceFit(true);
    
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
    
    rowExpander.initPlugin(grid);
    
    if (selectedEntries != null && selectedEntries.size() > 0) {
    	selectionModel.setSelection(selectedEntries);
    }
    
    // Stage manager, load the previous state
//    GridFilterStateHandler<AnnotatedBibliographyEntry> handler = new GridFilterStateHandler<AnnotatedBibliographyEntry>(grid, filters);
//    handler.loadState();
    
	}
	
	public ArrayList<AnnotatedBibliographyEntry> getSelectedEntries() {
		return new ArrayList<AnnotatedBibliographyEntry>(selectionModel.getSelectedItems());
	}
	
//	public void setSelectedEntries(List<AnnotatedBibliographyEntry> list) {
//		selectionModel.setSelection(list);
//	}

}
