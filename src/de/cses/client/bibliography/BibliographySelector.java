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
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.Style.SelectionMode;
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
import de.cses.shared.AnnotatedBiblographyEntry;

/**
 * @author alingnau
 *
 */
public class BibliographySelector implements IsWidget {

	interface BibliographyProperties extends PropertyAccess<AnnotatedBiblographyEntry> {
		@Path("annotatedBiblographyID")
		ModelKeyProvider<AnnotatedBiblographyEntry> key();

		ValueProvider<AnnotatedBiblographyEntry, String> titleORG();
		
		ValueProvider<AnnotatedBiblographyEntry, String> titleEN();
		
		ValueProvider<AnnotatedBiblographyEntry, String> authors();
		
		ValueProvider<AnnotatedBiblographyEntry, String> editors();
		
		@Path("yearORG")
		ValueProvider<AnnotatedBiblographyEntry, String> year();
		
//		@Path("publicationType.name")
//		ValueProvider<AnnotatedBiblographyEntry, String> publicationType();
	}
	
//	private ContentPanel mainPanel = null;
	private BibliographyProperties bibProps = GWT.create(BibliographyProperties.class);
	private AnnotatedBiblographyViewTemplates rowExpanderTemplates = GWT.create(AnnotatedBiblographyViewTemplates.class);
	private Grid<AnnotatedBiblographyEntry> grid = null;
	private CheckBoxSelectionModel<AnnotatedBiblographyEntry> selectionModel;

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
    IdentityValueProvider<AnnotatedBiblographyEntry> identity = new IdentityValueProvider<AnnotatedBiblographyEntry>();
    selectionModel = new CheckBoxSelectionModel<AnnotatedBiblographyEntry>(identity);
    selectionModel.setSelectionMode(SelectionMode.SIMPLE);
		
    RowExpander<AnnotatedBiblographyEntry> rowExpander = new RowExpander<AnnotatedBiblographyEntry>(new AbstractCell<AnnotatedBiblographyEntry>() {
			@Override
			public void render(Context context, AnnotatedBiblographyEntry value, SafeHtmlBuilder sb) {
				sb.append(rowExpanderTemplates.view(value.getAuthors(), value.getYearORG(), value.getTitleORG(), "publisher"));
			}
    });		
		
		ColumnConfig<AnnotatedBiblographyEntry, String> titleOrgCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.titleORG(), 350, "Title");
		ColumnConfig<AnnotatedBiblographyEntry, String> titleEnCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.titleEN(), 350, "English Translation");
		ColumnConfig<AnnotatedBiblographyEntry, String> authorsCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.authors(), 300, "Authors");
		ColumnConfig<AnnotatedBiblographyEntry, String> editorsCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.authors(), 300, "Editors");
		ColumnConfig<AnnotatedBiblographyEntry, String> yearColumn = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.year(), 50, "Year");
		yearColumn.setHideable(false);
		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		
    // we don't want all columns visible at the beginning
		editorsCol.setHidden(true);
		titleEnCol.setHidden(true);
		
    List<ColumnConfig<AnnotatedBiblographyEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<AnnotatedBiblographyEntry, ?>>();
    sourceColumns.add(selectionModel.getColumn());
    sourceColumns.add(rowExpander);
    sourceColumns.add(titleOrgCol);
    sourceColumns.add(titleEnCol);
    sourceColumns.add(authorsCol);
    sourceColumns.add(editorsCol);
    sourceColumns.add(yearColumn);

    ColumnModel<AnnotatedBiblographyEntry> sourceColumnModel = new ColumnModel<AnnotatedBiblographyEntry>(sourceColumns);
    
    ListStore<AnnotatedBiblographyEntry> sourceStore = new ListStore<AnnotatedBiblographyEntry>(bibProps.key());
//    sourceStore.addSortInfo(new StoreSortInfo<AnnotatedBiblographyEntry>(bibProps.titleORG(), SortDir.ASC));
    for (AnnotatedBiblographyEntry abe : StaticTables.getInstance().getBibliographyEntries().values()) {
    	sourceStore.add(abe);
    }
    
    grid = new Grid<AnnotatedBiblographyEntry>(sourceStore, sourceColumnModel);
    grid.setSelectionModel(selectionModel);
    grid.setColumnReordering(true);
    grid.getView().setAutoExpandColumn(titleOrgCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    grid.getView().setForceFit(true);
    
    // State manager, make this grid stateful
//    grid.setStateful(true);
//    grid.setStateId("bibSelector");

    StringFilter<AnnotatedBiblographyEntry> titleFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.titleORG());
    StringFilter<AnnotatedBiblographyEntry> authorFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.authors());
    StringFilter<AnnotatedBiblographyEntry> editorFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.editors());

    GridFilters<AnnotatedBiblographyEntry> filters = new GridFilters<AnnotatedBiblographyEntry>();
    filters.initPlugin(grid);
    filters.setLocal(true);
    filters.addFilter(titleFilter);
    filters.addFilter(authorFilter);
    filters.addFilter(editorFilter);
    
    rowExpander.initPlugin(grid);
    
    // Stage manager, load the previous state
//    GridFilterStateHandler<AnnotatedBiblographyEntry> handler = new GridFilterStateHandler<AnnotatedBiblographyEntry>(grid, filters);
//    handler.loadState();
    
	}
	
	public List<AnnotatedBiblographyEntry> getSelectedEntries() {
		return selectionModel.getSelectedItems();
	}
	
	public void setSelectedEntries(List<AnnotatedBiblographyEntry> list) {
		selectionModel.setSelection(list);
	}
	
}
