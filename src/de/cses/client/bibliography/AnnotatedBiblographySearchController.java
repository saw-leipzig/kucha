/*
 * Copyright 2017 
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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractFilter;
import de.cses.client.ui.AbstractResultView;
import de.cses.client.ui.AbstractSearchController;
import de.cses.client.ui.EditorListener;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.PublicationTypeEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographySearchController extends AbstractSearchController{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private PublicationTypeProperties publicationTypeProps;
	private ListStore<PublicationTypeEntry> publicationTypeListStore;
	protected PublicationTypeEntry publicationType = null;

	interface PublicationTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml publicationType(String name);
	}

	interface PublicationTypeProperties extends PropertyAccess<PublicationTypeEntry> {
		ModelKeyProvider<PublicationTypeEntry> publicationTypeID();
		LabelProvider<PublicationTypeEntry> name();
	}

	/**
	 * @param searchControllerTitle
	 * @param resultView
	 */
	public AnnotatedBiblographySearchController(String selectorTitle, AbstractResultView resultView) {
		super(selectorTitle, resultView);
		
		publicationTypeProps = GWT.create(PublicationTypeProperties.class);
		publicationTypeListStore = new ListStore<PublicationTypeEntry>(publicationTypeProps.publicationTypeID());
		for (PublicationTypeEntry pe : StaticTables.getInstance().getPublicationTypes().values()) {
			publicationTypeListStore.add(pe);
		}
		
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#invokeSearch()
	 */
	@Override
	public void invokeSearch() {
		ArrayList<String> sqlWhereClauses = new ArrayList<String>();
		for (AbstractFilter filter : getRelatedFilter()) {
			if ((filter != null) && (filter.getSqlWhereClause() != null)) {
				sqlWhereClauses.addAll(filter.getSqlWhereClause());
			}
		}
		String sqlWhere = null;
		for (String sql : sqlWhereClauses) {
			if (sqlWhere == null) {
				sqlWhere = sql;
			} else {
				sqlWhere = sqlWhere + " AND " + sql;
			}
		}
		dbService.getAnnotatedBibliography(new AsyncCallback<ArrayList<AnnotatedBiblographyEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<AnnotatedBiblographyEntry> result) {
				getResultView().reset();
				for (AnnotatedBiblographyEntry abe : result) {
					getResultView().addResult(new AnnotatedBiblographyView(abe));
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractSearchController#addNewElement()
	 */
	@Override
	public void addNewElement() {
		ComboBox<PublicationTypeEntry> publicationTypeComboBox = new ComboBox<PublicationTypeEntry>(publicationTypeListStore, publicationTypeProps.name(),
				new AbstractSafeHtmlRenderer<PublicationTypeEntry>() {

					@Override
					public SafeHtml render(PublicationTypeEntry item) {
						final PublicationTypeViewTemplates pvTemplates = GWT.create(PublicationTypeViewTemplates.class);
						return pvTemplates.publicationType(item.getName());
					}
				});
		publicationTypeComboBox.setTriggerAction(TriggerAction.ALL);
		publicationTypeComboBox.setEditable(false);
		publicationTypeComboBox.setTypeAhead(false);
		publicationTypeComboBox.addSelectionHandler(new SelectionHandler<PublicationTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<PublicationTypeEntry> event) {
				publicationType = event.getSelectedItem();
			}
		});
		
		PopupPanel publicationTypeDialog = new PopupPanel(false);

		TextButton createButton = new TextButton("create");
		createButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (publicationType != null) {
					publicationTypeDialog.hide();
					PopupPanel depictionEditorPanel = new PopupPanel(false);
					AnnotatedBiblographyEntry newBibEntry = new AnnotatedBiblographyEntry();
					newBibEntry.setPublicationType(publicationType);
					AnnotatedBiblographyEditor abe = new AnnotatedBiblographyEditor(newBibEntry);
					abe.addEditorListener(new EditorListener() {
						
						@Override
						public void closeRequest() {
							depictionEditorPanel.hide();
						}

						@Override
						public void updateEntryRequest(AbstractEntry updatedEntry) {
							// nothing is happening here
						}
					});
					depictionEditorPanel.add(abe);
					depictionEditorPanel.setGlassEnabled(true);
					depictionEditorPanel.center();
				}
			}
		});

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				publicationTypeDialog.hide();
			}
		});
		
		FramedPanel publicationTypeSelectorFP = new FramedPanel();
		publicationTypeSelectorFP.setHeading("Select type of publication for new entry!");
		publicationTypeSelectorFP.add(publicationTypeComboBox);
		publicationTypeSelectorFP.addButton(createButton);
		publicationTypeSelectorFP.addButton(cancelButton);
		
		
		publicationTypeDialog.add(publicationTypeSelectorFP);
		publicationTypeDialog.setGlassEnabled(true);
		publicationTypeDialog.center();
	}

}
