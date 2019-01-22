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
package de.cses.client.user;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.editing.GridEditing;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public class UserManager implements IsWidget {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface UserProperties extends PropertyAccess<UserEntry> {
		@Path("userID")
		ModelKeyProvider<UserEntry> key();

		ValueProvider<UserEntry, String> username();
		ValueProvider<UserEntry, String> firstname();
		ValueProvider<UserEntry, String> lastname();
		ValueProvider<UserEntry, String> email();
		ValueProvider<UserEntry, Integer> accessrights(); 
	}
	
//	private ContentPanel mainPanel = null;
	private UserProperties userProps = GWT.create(UserProperties.class);
	private Grid<UserEntry> grid = null;
//	private CheckBoxSelectionModel<UserEntry> selectionModel;

	/**
	 * 
	 */
	public UserManager() {
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
    IdentityValueProvider<UserEntry> identity = new IdentityValueProvider<UserEntry>();
//    selectionModel = new CheckBoxSelectionModel<UserEntry>(identity);
//    selectionModel.setSelectionMode(SelectionMode.SIMPLE);
		
//    RowExpander<UserEntry> rowExpander = new RowExpander<UserEntry>(new AbstractCell<UserEntry>() {
//			@Override
//			public void render(Context context, UserEntry value, SafeHtmlBuilder sb) {
//				sb.append(rowExpanderTemplates.extendedView(value));
//			}
//    });		
		
		ColumnConfig<UserEntry, String> usernameCol = new ColumnConfig<UserEntry, String>(userProps.username(), 300, "Username");
		ColumnConfig<UserEntry, String> firstnameCol = new ColumnConfig<UserEntry, String>(userProps.firstname(), 300, "Firstname");
		ColumnConfig<UserEntry, String> lastnameCol = new ColumnConfig<UserEntry, String>(userProps.lastname(), 300, "Lastname");
		ColumnConfig<UserEntry, String> emailCol = new ColumnConfig<UserEntry, String>(userProps.email(), 300, "Email");
		ColumnConfig<UserEntry, Integer> accessLevelCol = new ColumnConfig<UserEntry, Integer>(userProps.accessrights(), 150, "Access Level");
		
//		yearColumn.setHideable(false);
//		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		
    List<ColumnConfig<UserEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<UserEntry, ?>>();
//    sourceColumns.add(selectionModel.getColumn());
    sourceColumns.add(usernameCol);
    sourceColumns.add(firstnameCol);
    sourceColumns.add(lastnameCol);
    sourceColumns.add(emailCol);
    sourceColumns.add(accessLevelCol);

    ColumnModel<UserEntry> sourceColumnModel = new ColumnModel<UserEntry>(sourceColumns);
    
    ListStore<UserEntry> sourceStore = new ListStore<UserEntry>(userProps.key());

    dbService.getUsers(new AsyncCallback<ArrayList<UserEntry>>() {
			
			@Override
			public void onSuccess(ArrayList<UserEntry> result) {
				for (UserEntry entry : result) {
					sourceStore.add(entry);
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
			}
		});
    
    grid = new Grid<UserEntry>(sourceStore, sourceColumnModel);
//    grid.setSelectionModel(selectionModel);
//    grid.setColumnReordering(true);
    grid.getView().setAutoExpandColumn(usernameCol);
    grid.setBorders(false);
    grid.getView().setStripeRows(true);
    grid.getView().setColumnLines(true);
    grid.getView().setForceFit(true);
    
		SimpleComboBox<Integer> accessRightsCB = new SimpleComboBox<Integer>(new LabelProvider<Integer>() {

			@Override
			public String getLabel(Integer item) {
				return UserEntry.ACCESS_RIGHTS_LABEL.get(item-1);
			}
		});
		accessRightsCB.add(UserEntry.GUEST);
		accessRightsCB.add(UserEntry.ASSOCIATED);
		accessRightsCB.add(UserEntry.FULL);
		accessRightsCB.add(UserEntry.ADMIN);
		accessRightsCB.setEditable(false);
		accessRightsCB.setTypeAhead(false);
		accessRightsCB.setTriggerAction(TriggerAction.ALL);

		// Setup the editors. Designate the input type per column.
    GridEditing<UserEntry> editing = new GridRowEditing<UserEntry>(grid);
    editing.addEditor(usernameCol, new TextField());
    editing.addEditor(firstnameCol, new TextField());
    editing.addEditor(lastnameCol, new TextField());
    editing.addEditor(emailCol, new TextField());
    editing.addEditor(accessLevelCol, accessRightsCB);
    editing.addCompleteEditHandler(new CompleteEditHandler<UserEntry>() {
			
			@Override
			public void onCompleteEdit(CompleteEditEvent<UserEntry> event) {
				dbService.updateUserEntry(grid.getSelectionModel().getSelectedItem(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Util.showWarning("Server Error", "The changes could not be saved!");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (!result) {
							Util.showWarning("Server Error", "The changes could not be saved!");
						}
					}
				});
				Util.doLogging("editing completed for userID=" + grid.getSelectionModel().getSelectedItem().getUserID());
			}
		});

    StringFilter<UserEntry> usernameFilter = new StringFilter<UserEntry>(userProps.username());

    GridFilters<UserEntry> filters = new GridFilters<UserEntry>();
    filters.initPlugin(grid);
    filters.setLocal(true);
    filters.addFilter(usernameFilter);
    
	}
	
}
