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

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.i18n.client.HasDirection.Direction;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.dnd.core.client.GridDragSource;
import com.sencha.gxt.dnd.core.client.GridDropTarget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
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
	
	private ContentPanel mainPanel = null;
	private BibliographyProperties bibProps = GWT.create(BibliographyProperties.class);

	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			createUI();
		}
		return mainPanel;
	}

	/**
	 * 
	 */
	private void createUI() {
		ColumnConfig<AnnotatedBiblographyEntry, String> titleOrgCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.titleORG(), 300, "Original Title");
		ColumnConfig<AnnotatedBiblographyEntry, String> titleEnCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.titleEN(), 300, "English Translation");
		ColumnConfig<AnnotatedBiblographyEntry, String> authorsCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.authors(), 240, "Authors");
		ColumnConfig<AnnotatedBiblographyEntry, String> editorsCol = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.authors(), 240, "Editors");
		ColumnConfig<AnnotatedBiblographyEntry, String> yearColumn = new ColumnConfig<AnnotatedBiblographyEntry, String>(bibProps.year(), 60, "Year");
		yearColumn.setHideable(false);
		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		
    // we don't want all columns visible at the beginning
		editorsCol.setHidden(true);
		titleEnCol.setHidden(true);
		
    List<ColumnConfig<AnnotatedBiblographyEntry, ?>> columns = new ArrayList<ColumnConfig<AnnotatedBiblographyEntry, ?>>();
    columns.add(titleOrgCol);
    columns.add(titleEnCol);
    columns.add(authorsCol);
    columns.add(editorsCol);
    columns.add(yearColumn);

    ColumnModel<AnnotatedBiblographyEntry> cm = new ColumnModel<AnnotatedBiblographyEntry>(columns);
    
    ListStore<AnnotatedBiblographyEntry> sourceStore = new ListStore<AnnotatedBiblographyEntry>(bibProps.key());
    sourceStore.addSortInfo(new StoreSortInfo<AnnotatedBiblographyEntry>(bibProps.titleORG(), SortDir.ASC));
    for (AnnotatedBiblographyEntry abe : StaticTables.getInstance().getBibliographyEntries().values()) {
    	sourceStore.add(abe);
    }
    
    ListStore<AnnotatedBiblographyEntry> selectedStore = new ListStore<AnnotatedBiblographyEntry>(bibProps.key());
    selectedStore.addSortInfo(new StoreSortInfo<AnnotatedBiblographyEntry>(bibProps.titleORG(), SortDir.ASC));
    
    Grid<AnnotatedBiblographyEntry> sourceGrid = new Grid<AnnotatedBiblographyEntry>(sourceStore, cm);
    sourceGrid.setColumnReordering(true);
    sourceGrid.getView().setAutoExpandColumn(titleOrgCol);
    sourceGrid.setBorders(false);
    sourceGrid.getView().setStripeRows(true);
    sourceGrid.getView().setColumnLines(true);
    
    Grid<AnnotatedBiblographyEntry> selectedGrid = new Grid<AnnotatedBiblographyEntry>(selectedStore, cm);
    selectedGrid.setColumnReordering(true);
    selectedGrid.getView().setAutoExpandColumn(titleOrgCol);
    selectedGrid.setBorders(false);
    selectedGrid.getView().setStripeRows(true);
    selectedGrid.getView().setColumnLines(true);
    selectedGrid.getView().setForceFit(true);
    
    new GridDragSource<AnnotatedBiblographyEntry>(sourceGrid).setGroup("bib");
    new GridDragSource<AnnotatedBiblographyEntry>(selectedGrid).setGroup("bib");
    
    new GridDropTarget<AnnotatedBiblographyEntry>(sourceGrid).setGroup("bib");
    new GridDropTarget<AnnotatedBiblographyEntry>(selectedGrid).setGroup("bib");
    
    // State manager, make this sourceGrid stateful
//    sourceGrid.setStateful(true);
//    sourceGrid.setStateId("bibSelector");

    StringFilter<AnnotatedBiblographyEntry> titleFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.titleORG());
    StringFilter<AnnotatedBiblographyEntry> authorFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.authors());
    StringFilter<AnnotatedBiblographyEntry> editorFilter = new StringFilter<AnnotatedBiblographyEntry>(bibProps.editors());

    GridFilters<AnnotatedBiblographyEntry> filters = new GridFilters<AnnotatedBiblographyEntry>();
    filters.initPlugin(sourceGrid);
    filters.setLocal(true);
    filters.addFilter(titleFilter);
    filters.addFilter(authorFilter);
    filters.addFilter(editorFilter);
    
    // Stage manager, load the previous state
//    GridFilterStateHandler<AnnotatedBiblographyEntry> handler = new GridFilterStateHandler<AnnotatedBiblographyEntry>(sourceGrid, filters);
//    handler.loadState();
    
    FramedPanel sourceFP = new FramedPanel();
    sourceFP.setHeading("source");
    sourceFP.add(sourceGrid);
    
    FramedPanel selectedFP = new FramedPanel();
    selectedFP.setHeading("selected");
    selectedFP.add(selectedGrid);

    VerticalLayoutContainer bibSelectorVLC = new VerticalLayoutContainer();
    bibSelectorVLC.add(sourceFP, new VerticalLayoutData(1.0, .5));
    bibSelectorVLC.add(selectedFP, new VerticalLayoutData(1.0, .5));
//    bibSelectorBLC.setWidth("600px");

    mainPanel = new ContentPanel();
		mainPanel.setHeaderVisible(false);
		mainPanel.add(bibSelectorVLC);
	}

}
