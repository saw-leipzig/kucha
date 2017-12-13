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
package de.cses.client.images;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.images.ImageEditor.PhotographerProperties;
import de.cses.shared.PhotographerEntry;

public class PhotographerEditor implements IsWidget {

	/**
	 * Create a remote service proxy to talk to the server-side service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ComboBox<PhotographerEntry> photographerSelection;
	private PhotographerProperties photographerProps;
	private ListStore<PhotographerEntry> photographerEntryList;
	private ContentPanel panel;

	private TextField newNameField;

	interface PhotographerViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml photographer(String name);
	}

	public PhotographerEditor() {
		photographerProps = GWT.create(PhotographerProperties.class);
		photographerEntryList = new ListStore<PhotographerEntry>(photographerProps.photographerID());
		refreshPhotographerList();
	}
	
	private void refreshPhotographerList() {
		dbService.getPhotographer(new AsyncCallback<ArrayList<PhotographerEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PhotographerEntry> result) {
				photographerEntryList.clear();
				for (PhotographerEntry pe : result) {
					photographerEntryList.add(pe);
				}
			}
		});		
	}

	@Override
	public Widget asWidget() {
		if (panel == null) {
			initPanel();
		}
		return panel;
	}
	
	private void initPanel() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		VerticalLayoutData vLayoutData = new VerticalLayoutData(150, 300, new Margins(15, 5, 0, 0));
		vlc.setLayoutData(vLayoutData);
		
		photographerSelection = new ComboBox<PhotographerEntry>(photographerEntryList, photographerProps.name(),
				new AbstractSafeHtmlRenderer<PhotographerEntry>() {

					@Override
					public SafeHtml render(PhotographerEntry item) {
						final PhotographerViewTemplates pvTemplates = GWT.create(PhotographerViewTemplates.class);
						return pvTemplates.photographer(item.getName());
					}
				});
		photographerSelection.setEmptyText("Select a Photographer ...");
		photographerSelection.setTypeAhead(false);
		photographerSelection.setEditable(false);
		photographerSelection.setTriggerAction(TriggerAction.ALL);
		
		TextButton newButton = new TextButton("new entry");
		newButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				newPhotographerEntry();
			}
		});

		TextButton deleteButton = new TextButton("delete entry");
		deleteButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				deleteSelectedPhotographerEntry();
			}
		});
				
		vlc.add(new FieldLabel(photographerSelection, "Photographer"));
		
//		mainInputVLC.add(new FieldLabel(newNameField, "New Name"));
		
		BoxLayoutData bbLayout = new BoxLayoutData(new Margins(5, 15, 5, 0));
		ButtonBar bb = new ButtonBar();
		bb.add(newButton, bbLayout);
		bb.add(deleteButton, bbLayout);
		vlc.add(bb);

		panel = new ContentPanel();
		panel.setHeading("Photographer Editor");
    panel.setPixelSize(510, 410);
    panel.setBounds(0, 0, 510, 410);
    panel.setPosition(5, 5);
		panel.add(vlc);
	}

	protected void deleteSelectedPhotographerEntry() {
		Dialog deleteDialog = new Dialog();
		deleteDialog.setHeading("Delete photographer name");
		deleteDialog.setWidth(300);
		deleteDialog.setResizable(false);
		deleteDialog.setHideOnButtonClick(true);
		deleteDialog.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO);
		deleteDialog.setBodyStyleName("pad-text");
		deleteDialog.getBody().addClassName("pad-text");
		deleteDialog.add(new Label("Do you really want to delete " + photographerSelection.getCurrentValue().getName() + "?"));
		deleteDialog.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbService.deleteEntry(photographerSelection.getCurrentValue().getDeleteSql(), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
					}

					@Override
					public void onSuccess(Boolean result) {
						photographerSelection.clear();
						refreshPhotographerList();
					}
				});
			}
		});
		deleteDialog.setVisible(true);
	}

	protected void newPhotographerEntry() {
		Dialog newEntryDialog = new Dialog();
		newEntryDialog.setHeading("Add new photographer name");
		newEntryDialog.setWidth(300);
		newEntryDialog.setResizable(false);
		newEntryDialog.setHideOnButtonClick(true);
		newEntryDialog.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		newEntryDialog.setBodyStyleName("pad-text");
		newEntryDialog.getBody().addClassName("pad-text");
		
		newNameField = new TextField();
		newNameField.setWidth(300);
		newEntryDialog.add(newNameField);
		
		newEntryDialog.getButton(PredefinedButton.OK).addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				String newName;
				newName = newNameField.getText();
				PhotographerEntry newPE = new PhotographerEntry();
				newPE.setName(newName);
				dbService.insertPhotographerEntry(newPE, new AsyncCallback<Integer>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(Integer result) {
						refreshPhotographerList();
					}});
			}
		});
		
		newEntryDialog.setVisible(true);

	}

}
