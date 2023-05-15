/*
 * Copyright 2018 - 2019
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
import java.util.Collection;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.editor.client.Editor.Path;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent;
import com.sencha.gxt.widget.core.client.event.CancelEditEvent.CancelEditHandler;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent;
import com.sencha.gxt.widget.core.client.event.CompleteEditEvent.CompleteEditHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;
import com.sencha.gxt.widget.core.client.grid.ColumnConfig;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;
import com.sencha.gxt.widget.core.client.grid.Grid;
import com.sencha.gxt.widget.core.client.grid.Grid.GridCell;
import com.sencha.gxt.widget.core.client.grid.editing.GridRowEditing;
import com.sencha.gxt.widget.core.client.grid.filters.GridFilters;
import com.sencha.gxt.widget.core.client.grid.filters.StringFilter;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public class UserManagerFrontEnd extends PopupPanel {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	interface UserProperties extends PropertyAccess<UserEntry> {
		@Path("userID")
		ModelKeyProvider<UserEntry> key();

		ValueProvider<UserEntry, String> firstname();
		ValueProvider<UserEntry, String> lastname();
		ValueProvider<UserEntry, String> email();
		ValueProvider<UserEntry, String> affiliation();
		ValueProvider<UserEntry, Integer> accessLevel(); 
		ValueProvider<UserEntry, Boolean> granted(); 
	}
	
//	private ContentPanel mainPanel = null;
	private UserProperties userProps = GWT.create(UserProperties.class);
	private Grid<UserEntry> grid = null;
//	private CheckBoxSelectionModel<UserEntry> selectionModel;
	private ListStore<UserEntry> sourceStore;
	private GridRowEditing<UserEntry> editing;

	/**
	 * 
	 */
	public UserManagerFrontEnd() {
		FramedPanel userManagerFP = new FramedPanel();
		userManagerFP.setHeading("User Manager of Frontend Website");
		ToolButton closeTB = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		closeTB.setToolTip(Util.createToolTip("Exit without saving"));
		ToolButton saveTB = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				saveClose();
			}
		});
		saveTB.setToolTip(Util.createToolTip("save & exit"));
		ToolButton resetPwTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetPwTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				for (UserEntry user :grid.getSelectionModel().getSelectedItems()) {
					dbService.resetPasswordFrontEnd(user, new AsyncCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean result) {
							
							Info.display("reset password for user: "+user.getUsername()+ " success?",Boolean.toString(result));
						}
						
						@Override
						public void onFailure(Throwable caught) {
						}
					});
				}
	
			}
		});
		resetPwTB.setToolTip(Util.createToolTip("reset password of selected user."));
		ToolButton addUserTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addUserTB.setToolTip(Util.createToolTip("Add a new user."));
		addUserTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				UserEntry newUser = new UserEntry(0, "", "", "", "", "", 1, "", "");
				sourceStore.add(newUser);
				editing.startEditing(new GridCell(sourceStore.size()-1, 0));
			}
		});
		createUI();
		userManagerFP.add(grid);
		userManagerFP.addTool(resetPwTB);
		userManagerFP.addTool(saveTB);
		userManagerFP.addTool(addUserTB);
		userManagerFP.addTool(closeTB);
		add(userManagerFP);
	}
	
	/**
	 * 
	 */
	private void loadUsers() {
		sourceStore.clear();
	    dbService.getUsersFrontEnd(new AsyncCallback<ArrayList<UserEntry>>() {
			
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
	 
	}
	private void createUI() {
//    IdentityValueProvider<UserEntry> identity = new IdentityValueProvider<UserEntry>();
//    selectionModel = new CheckBoxSelectionModel<UserEntry>(identity);
//    selectionModel.setSelectionMode(SelectionMode.SIMPLE);
		
//    RowExpander<UserEntry> rowExpander = new RowExpander<UserEntry>(new AbstractCell<UserEntry>() {
//			@Override
//			public void render(Context context, UserEntry value, SafeHtmlBuilder sb) {
//				sb.append(rowExpanderTemplates.extendedView(value));
//			}
//    });		
		
		ColumnConfig<UserEntry, String> firstnameCol = new ColumnConfig<UserEntry, String>(userProps.firstname(), 300, "Firstname");
		ColumnConfig<UserEntry, String> lastnameCol = new ColumnConfig<UserEntry, String>(userProps.lastname(), 300, "Lastname");
		ColumnConfig<UserEntry, String> emailCol = new ColumnConfig<UserEntry, String>(userProps.email(), 300, "Email");
		ColumnConfig<UserEntry, String> affCol = new ColumnConfig<UserEntry, String>(userProps.affiliation(), 300, "Affiliation");
		ColumnConfig<UserEntry, Integer> accessLevelCol = new ColumnConfig<UserEntry, Integer>(userProps.accessLevel(), 150, "Access Level");
		ColumnConfig<UserEntry, Boolean> grantedCol = new ColumnConfig<UserEntry, Boolean>(userProps.granted(), 150, "granted");
		
//		yearColumn.setHideable(false);
//		yearColumn.setHorizontalHeaderAlignment(HorizontalAlignmentConstant.startOf(Direction.DEFAULT));
		
    List<ColumnConfig<UserEntry, ?>> sourceColumns = new ArrayList<ColumnConfig<UserEntry, ?>>();
//    sourceColumns.add(selectionModel.getColumn());
    sourceColumns.add(firstnameCol);
    sourceColumns.add(lastnameCol);
    sourceColumns.add(emailCol);
    sourceColumns.add(affCol);
    sourceColumns.add(accessLevelCol);
    sourceColumns.add(grantedCol);

    ColumnModel<UserEntry> sourceColumnModel = new ColumnModel<UserEntry>(sourceColumns);
    
    sourceStore = new ListStore<UserEntry>(userProps.key());

    loadUsers();
    grid = new Grid<UserEntry>(sourceStore, sourceColumnModel);
//    grid.setSelectionModel(selectionModel);
//    grid.setColumnReordering(true);
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

	SimpleComboBox<Boolean> grantedCB = new SimpleComboBox<Boolean>(new LabelProvider<Boolean>() {

		@Override
		public String getLabel(Boolean item) {
			
			if (item) {
				return UserEntry.granted_LABEL.get(0);
			} else {
				return UserEntry.granted_LABEL.get(1);				
			}
		}
	});
	grantedCB.add(UserEntry.yes);
	grantedCB.add(UserEntry.no);
	grantedCB.setEditable(false);
	grantedCB.setTypeAhead(false);
	grantedCB.setTriggerAction(TriggerAction.ALL);

		// Setup the editors. Designate the input type per column.
    editing = new GridRowEditing<UserEntry>(grid);
    TextField firstnameTF = new TextField();
    firstnameTF.setAllowBlank(false);
    editing.addEditor(firstnameCol, firstnameTF);
    TextField lastnameTF = new TextField();
    lastnameTF.setAllowBlank(false);
    editing.addEditor(lastnameCol, lastnameTF);
    TextField emailTF = new TextField();
    emailTF.setAllowBlank(false);
    emailTF.addValidator(new RegExValidator(Util.REGEX_EMAIL_PATTERN, "please enter valid email"));
    editing.addEditor(emailCol, emailTF);
    editing.addEditor(accessLevelCol, accessRightsCB);
    editing.addEditor(grantedCol, grantedCB);
    
    editing.addCancelEditHandler(new CancelEditHandler<UserEntry>() {
			
			@Override
			public void onCancelEdit(CancelEditEvent<UserEntry> event) {
				UserEntry entry = event.getSource().getEditableGrid().getSelectionModel().getSelectedItem();
				if (entry!=null) {
					if (entry.getUserID() == 0 && ( !firstnameTF.isValid() || !lastnameTF.isValid() || !emailTF.isValid())) {
						sourceStore.remove(entry);
					}
				}
			}
		});
    editing.addCompleteEditHandler(new CompleteEditHandler<UserEntry>() {
			
			@Override
			public void onCompleteEdit(CompleteEditEvent<UserEntry> event) {
				UserEntry entry = event.getSource().getEditableGrid().getSelectionModel().getSelectedItem();
				if (entry!=null) {
					if (entry.getUserID() == 0 && ( !firstnameTF.isValid() || !lastnameTF.isValid() || !emailTF.isValid())) {
						sourceStore.remove(entry);
					}
				}
			}
		});
    
    StringFilter<UserEntry> usernameFilter = new StringFilter<UserEntry>(userProps.email());

    GridFilters<UserEntry> filters = new GridFilters<UserEntry>();
    filters.initPlugin(grid);
    filters.setLocal(true);
    filters.addFilter(usernameFilter);
    
	}
	
	private void saveClose() {
		Collection<Store<UserEntry>.Record> recordsModified = sourceStore.getModifiedRecords();

		for (Store<UserEntry>.Record record : recordsModified) {
			UserEntry entry = record.getModel();
			// record.getValue(...) will return null if it has not been changed
			entry.setUsername("");
			String firstname = record.getValue(userProps.firstname());
			if (firstname != null) {
				entry.setFirstname(firstname);
			}
			String lastname = record.getValue(userProps.lastname());
			if (lastname != null) {
				entry.setLastname(lastname);
			}
			String email = record.getValue(userProps.email());
			if (email != null) {
				entry.setEmail(email);
			}
			Integer accessLevel = record.getValue(userProps.accessLevel());
			if (email != null) {
				entry.setAccessLevel(accessLevel);
			}
			Boolean granted = record.getValue(userProps.granted());
			if (granted != null) {
				entry.setGranted(granted);
			}

			if (entry.getUserID() > 0) {
				dbService.updateUserEntryFrontEnd(entry, new AsyncCallback<Boolean>() {
					
					@Override
					public void onFailure(Throwable caught) {
						Util.showWarning("Server Error", "The changes for " + entry.getUsername() + " could not be saved! Error: "+caught.getMessage());
					}
					
					@Override
					public void onSuccess(Boolean result) {
						if (!result) {
							Util.showWarning("Server Error", "The changes for " + entry.getUsername() + " could not be saved!");
						} else {
							Info.display("User", "Updated");
							// Once the changes have been dealt with, commit them to the local store.
							// This will add the changed values to the model in the local store.
							loadUsers();
							hide();
						}
					}
				});
			} else {
				dbService.saveWebPageUser(entry, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						Util.showWarning("Server Error", entry.getUsername() + " could not be created!! Error: "+caught.getMessage());
					}

					@Override
					public void onSuccess(Integer result) {
						if (result > 0) {
							entry.setUserID(result);
							// Once the changes have been dealt with, commit them to the local store.
							// This will add the changed values to the model in the local store.
							loadUsers();
							hide();
						} else {
							Util.showWarning("Server Error", entry.getUsername() + " could not be created!");
						}
					}
					
				});
			}
		}
	}

}
