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
package de.cses.client.bibliography;

import java.sql.Date;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.shared.AuthorEntry;

/**
 * @author alingnau
 *
 */
public class AuthorEditor implements IsWidget {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private AuthorEntry currentAuthorEntry = null;
	private FramedPanel mainPanel = null;
	private TextField lastnameField, firstnameField, emailField, homepageField;
	private DateField kuchaVisitDateField;
	private TextArea affiliationArea;
	private ArrayList<AuthorEditorListener> listenerList;

	/**
	 * 
	 */
	public AuthorEditor(int authorID, AuthorEditorListener listener) {
		listenerList = new ArrayList<AuthorEditorListener>();
		listenerList.add(listener);
		if (authorID > 0) {
			loadAuthor(authorID);
		} else {
			currentAuthorEntry = new AuthorEntry();
		}
	}

	/**
	 * @param authorID
	 */
	public void loadAuthor(int authorID) {
		dbService.getAuthorEntry(authorID, new AsyncCallback<AuthorEntry>() {

			@Override
			public void onSuccess(AuthorEntry result) {
				currentAuthorEntry = result;
			}

			@Override
			public void onFailure(Throwable caught) {
				currentAuthorEntry = null;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initUI();
		}
		setAuthorValues();
		return mainPanel;
	}

	/**
	 * 
	 */
	private void setAuthorValues() {
		if (currentAuthorEntry.getAuthorID() > 0) {
			lastnameField.setValue(currentAuthorEntry.getLastname());
			firstnameField.setValue(currentAuthorEntry.getFirstname());
			kuchaVisitDateField.setValue(currentAuthorEntry.getKuchaVisitDate());
			affiliationArea.setValue(currentAuthorEntry.getAffiliation());
			emailField.setValue(currentAuthorEntry.getEmail());
			homepageField.setValue(currentAuthorEntry.getHomepage());
		}
	}

	/**
	 * 
	 */
	private void initUI() {
		mainPanel = new FramedPanel();
		mainPanel.setHeading("Author Editor");

		VerticalPanel vPanel = new VerticalPanel();

		ContentPanel cp = new ContentPanel();
		cp.setHeading("Last name");
		lastnameField = new TextField();
		lastnameField.setEmptyText("enter last name");
		lastnameField.setAllowBlank(false);
		lastnameField.setWidth(200);
		lastnameField.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentAuthorEntry.setLastname(event.getValue());;
			}
		});
		cp.add(lastnameField);
		vPanel.add(cp);

		cp = new ContentPanel();
		cp.setHeading("First name");
		firstnameField = new TextField();
		firstnameField.setEmptyText("enter first name");
		firstnameField.setWidth(200);
		firstnameField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentAuthorEntry.setFirstname(event.getValue());
			}
		});
		cp.add(firstnameField);
		vPanel.add(cp);

		cp = new ContentPanel();
		cp.setHeading("Kucha Visit Date");
		kuchaVisitDateField = new DateField(new DateTimePropertyEditor("MMMM yyyy"));
		kuchaVisitDateField.addValueChangeHandler(new ValueChangeHandler<java.util.Date>() {

			@Override
			public void onValueChange(ValueChangeEvent<java.util.Date> event) {
				currentAuthorEntry.setKuchaVisitDate(new Date(event.getValue().getTime()));
			}
		});
		cp.add(kuchaVisitDateField);
		vPanel.add(cp);

		cp = new ContentPanel();
		cp.setHeading("Affiliation");
		affiliationArea = new TextArea();
		affiliationArea.setSize("200px", "3em");
		affiliationArea.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentAuthorEntry.setAffiliation(event.getValue());
			}
		});
		cp.add(affiliationArea);
		vPanel.add(cp);

		cp = new ContentPanel();
		cp.setHeading("Email");
		emailField = new TextField();
		emailField.setWidth(200);
		emailField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentAuthorEntry.setEmail(event.getValue());
			}
			
		});
		cp.add(emailField);
		vPanel.add(cp);
		
		cp = new ContentPanel();
		cp.setHeading("Homepage");
		homepageField = new TextField();
		homepageField.setWidth(200);
		homepageField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentAuthorEntry.setHomepage(event.getValue());
			}
		});
		cp.add(homepageField);
		vPanel.add(cp);
		
		mainPanel.add(vPanel);

		TextButton cancelButton = new TextButton("Cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				for (AuthorEditorListener l : listenerList) {
					l.authorSaved(null);
				}
			}
		});

		TextButton saveButton = new TextButton("Save & Exit");
		saveButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				DateTimeFormat dtf = DateTimeFormat.getFormat("yyyy-MM-dd");
				if (!lastnameField.isValid()) {
					return; // do nothing because we need at least a lastname of an author!
				}
				if (currentAuthorEntry.getAuthorID() == 0) {
					dbService.insertAuthorEntry(currentAuthorEntry, new AsyncCallback<Integer>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Integer result) {
							currentAuthorEntry.setAuthorID(result);
						}
					});

				} else {
					dbService.updateAuthorEntry(currentAuthorEntry, new AsyncCallback<Boolean>() {

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub
							
						}

						@Override
						public void onSuccess(Boolean result) {
							// TODO Auto-generated method stub
							
						}
					});
				}

				for (AuthorEditorListener l : listenerList) {
					l.authorSaved(currentAuthorEntry);
				}
			}
		});

		mainPanel.addButton(saveButton);
		mainPanel.addButton(cancelButton);
	}

}
