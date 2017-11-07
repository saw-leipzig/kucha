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

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractEditor;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	HorizontalPanel horizontBackground;
	AnnotatedBiblographyEntry entry;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	int publicationtype = 0;
	
	private FramedPanel frame;

	private DualListField<AuthorEntry, String> authorSelection;
	private DualListField<AuthorEntry, String> editorSelection;

	private ComboBox<PublisherEntry> publisherComboBox;
	private ComboBox<PublicationTypeEntry> publicationTypeComboBox;
	private ComboBox<AnnotatedBiblographyEntry> 	erstauflageComboBox;

	private ListStore<PublicationTypeEntry> publicationTypeListStore;
	private ListStore<PublisherEntry> publisherListStore;
	private ListStore<AnnotatedBiblographyEntry>	AnnotatedBiblographyEntryListStore;

	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;

	private PublisherProperties publisherProps;
	private AnnotatedBiblographyEntryProperties annotatedBiblographyEntryProps;
	private PublicationTypeProperties publicationTypeProps;
	private AuthorProperties authorProps;

	private TabPanel tabpanel;
	VerticalLayoutContainer firstTabVLC = new VerticalLayoutContainer();
	VerticalLayoutContainer secoundTabVLC = new VerticalLayoutContainer();
	VerticalLayoutContainer thirdTabVLC = new VerticalLayoutContainer();
	
	VerticalLayoutContainer mainInputVLC = new VerticalLayoutContainer();

	FramedPanel mainFP = null; // das oberste Framed Panel als Rahmen
	VerticalLayoutContainer backgroundoverview = new VerticalLayoutContainer(); // verticaler background fuer die Lioteratur


	TextField titelEN;
	TextField titelORG;
	TextField titelTR;
	
	TextField procEN;
	TextField procORG;
	TextField procTR; 
	
	TextField chaptitEN;
	TextField chaptitORG;
	TextField chaptitTR;
	
	TextField booktitelEN ;
	TextField booktitelORG;
	TextField booktitelTR;
	
	TextField uniEN;
	TextField uniORG;
	TextField uniTR;
	
	TextField numberEN;
	TextField numberORG;
	TextField numberTR;
	
	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry) {
		this.entry = entry;
	}

	@Override
	public Widget asWidget() {
		if (mainFP == null) {
			init();
			createForm();
		}

		return mainFP;
	}

	public AnnotatedBiblographyEditor() {

	}

	public void init() {

		authorProps = GWT.create(AuthorProperties.class);
		annotatedBiblographyEntryProps = GWT.create(AnnotatedBiblographyEntryProperties.class);
		publisherProps = GWT.create(PublisherProperties.class);
		publicationTypeProps = GWT.create(PublicationTypeProperties.class);
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		AnnotatedBiblographyEntryListStore = new ListStore<AnnotatedBiblographyEntry>(annotatedBiblographyEntryProps.annotatedBiblographyID());
		selectedAuthorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		publicationTypeListStore = new ListStore<PublicationTypeEntry>(publicationTypeProps.publicationTypeID());
		selectedEditorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		dbService.getPublicationTypes(new AsyncCallback<ArrayList<PublicationTypeEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PublicationTypeEntry> result) {
				publicationTypeListStore.clear();
				for (PublicationTypeEntry pe : result) {
					publicationTypeListStore.add(pe);
				}
			}
		});
	}

	public void createForm() {

		// Overview FramedPanel

		publicationTypeComboBox = new ComboBox<PublicationTypeEntry>(publicationTypeListStore, publicationTypeProps.name(),
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
		SelectionHandler<PublicationTypeEntry> publicationTypeSelectionHandler = new SelectionHandler<PublicationTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<PublicationTypeEntry> event) {

				publicationtype = event.getSelectedItem().getPublicationTypeID();
				rebuildMainInput(publicationtype);
			}
		};
		publicationTypeComboBox.addSelectionHandler(publicationTypeSelectionHandler);

		FramedPanel puplicationTypeFP = new FramedPanel();
		puplicationTypeFP.setHeading("Publication Type");
		puplicationTypeFP.add(publicationTypeComboBox);
		
		FramedPanel mainInputFP = new FramedPanel();
		mainInputFP.setHeading("Literatur");
		mainInputFP.add(mainInputVLC);
		

		
		backgroundoverview.add(puplicationTypeFP, new VerticalLayoutData(1.0, .1));
		backgroundoverview.add(mainInputFP, new VerticalLayoutData(1.0, .9));
		

		

		ToolButton closeToolButton = new ToolButton(ToolButton.CLOSE);
		closeToolButton.setToolTip("close");
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Dialog d = new Dialog();
				d.setHeading("Exit Warning!");
				d.setWidget(new HTML("Do you wish to save before exiting?"));
				d.setBodyStyle("fontWeight:bold;padding:13px;");
				d.setPixelSize(300, 100);
				d.setHideOnButtonClick(true);
				d.setPredefinedButtons(PredefinedButton.YES, PredefinedButton.NO, PredefinedButton.CANCEL);
				d.setModal(true);
				d.center();
				d.show();
				d.getButton(PredefinedButton.YES).addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						// saveEntries(true);
					}
				});
				d.getButton(PredefinedButton.NO).addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						closeEditor();
					}
				});
			}
		});
	
		mainFP = new FramedPanel();
		mainFP.setHeading("Annotated Biblography");
		mainFP.setSize("900px", "700px"); // here we set the size of the panel
		mainFP.add(backgroundoverview);
		mainFP.addTool(closeToolButton);

	}

	public void rebuildMainInput(int publicationtype) {
		mainInputVLC.clear();
		tabpanel = new TabPanel();
		firstTabVLC = new VerticalLayoutContainer();
		secoundTabVLC = new VerticalLayoutContainer();
		thirdTabVLC = new VerticalLayoutContainer();
		mainInputVLC.add(tabpanel);
		
		tabpanel.add(firstTabVLC, "Basics");
		tabpanel.add(secoundTabVLC, "Authors and Editors");
		tabpanel.add(thirdTabVLC, "Others");
	
		 horizontBackground = new HorizontalPanel();
		titelEN = new TextField();
		titelORG = new TextField();
		titelTR = new TextField();

		horizontBackground.add(titelEN);
		horizontBackground.add(titelTR);
		horizontBackground.add(titelORG);
		frame = new FramedPanel();
		frame.setHeading("Titel");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);

		if (publicationtype == 6) {
			horizontBackground = new HorizontalPanel();
			procEN = new TextField();
			procORG = new TextField();
			procTR = new TextField();

			horizontBackground.add(procEN);
			horizontBackground.add(procTR);
			horizontBackground.add(procORG);
			frame = new FramedPanel();
			frame.setHeading("Proceedings Title");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 5) {
			horizontBackground = new HorizontalPanel();
			chaptitEN = new TextField();
			chaptitORG = new TextField();
			chaptitTR = new TextField();

			horizontBackground.add(chaptitEN);
			horizontBackground.add(chaptitTR);
			horizontBackground.add(chaptitORG);
			frame = new FramedPanel();
			frame.setHeading("Chapter Title");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 1) {

			horizontBackground = new HorizontalPanel();
			booktitelEN = new TextField();
			booktitelORG = new TextField();
			booktitelTR = new TextField();

			horizontBackground.add(booktitelEN);
			horizontBackground.add(booktitelTR);
			horizontBackground.add(booktitelORG);
			frame = new FramedPanel();
			frame.setHeading("Booktitle");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 3) {
			horizontBackground = new HorizontalPanel();
			uniEN = new TextField();
			uniORG = new TextField();
			uniTR = new TextField();

			horizontBackground.add(uniEN);
			horizontBackground.add(uniTR);
			horizontBackground.add(uniORG);
			frame = new FramedPanel();
			frame.setHeading("University");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 8) {

			horizontBackground = new HorizontalPanel();
			TextField numberEN = new TextField();
			TextField numberORG = new TextField();
			TextField numberTR = new TextField();

			horizontBackground.add(numberEN);
			horizontBackground.add(numberTR);
			horizontBackground.add(numberORG);
			frame = new FramedPanel();
			frame.setHeading("Number");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 7) {
			horizontBackground = new HorizontalPanel();
			TextField accessEN = new TextField();
			TextField accessORG = new TextField();
			TextField accessTR = new TextField();

			horizontBackground.add(accessEN);
			horizontBackground.add(accessTR);
			horizontBackground.add(accessORG);
			frame = new FramedPanel();
			frame.setHeading("Access Date");
			frame.add(horizontBackground);
			thirdTabVLC.add(frame);
		}

		horizontBackground = new HorizontalPanel();
		TextField titeladdonEN = new TextField();
		TextField titeladdonORG = new TextField();
		TextField titeladdonTR = new TextField();

		horizontBackground.add(titeladdonEN);
		horizontBackground.add(titeladdonTR);
		horizontBackground.add(titeladdonORG);
		frame = new FramedPanel();
		frame.setHeading("Titleaddon");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);
		
		publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.name(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getName());
					}
				});
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(publisherComboBox);
		frame = new FramedPanel();
		frame.setHeading("Publisher");
		frame.add(horizontBackground);
		secoundTabVLC.add(frame);
		authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore, authorProps.name(), new TextCell());

		editorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedEditorListStore, authorProps.name(), new TextCell());

		if (publicationtype != 6) {
			horizontBackground = new HorizontalPanel();
			horizontBackground.add(authorSelection);
			frame = new FramedPanel();
			frame.setHeading("Author");
			frame.add(horizontBackground);
			secoundTabVLC.add(frame);
		}

		horizontBackground = new HorizontalPanel();
		horizontBackground.add(editorSelection);
		frame = new FramedPanel();
		frame.setHeading("Editor");
		frame.add(horizontBackground);
		secoundTabVLC.add(frame);
		
		if (publicationtype == 8) {
			horizontBackground = new HorizontalPanel();
			TextField seriesEN = new TextField();
			TextField seriesORG = new TextField();
			TextField seriesTR = new TextField();

			horizontBackground.add(seriesEN);
			horizontBackground.add(seriesTR);
			horizontBackground.add(seriesORG);
			frame = new FramedPanel();
			frame.setHeading("Serie");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 1 || publicationtype == 5) {

			horizontBackground = new HorizontalPanel();
			TextField editionEN = new TextField();
			TextField editionORG = new TextField();
			TextField editionTR = new TextField();

			horizontBackground.add(editionEN);
			horizontBackground.add(editionTR);
			horizontBackground.add(editionORG);
			frame = new FramedPanel();
			frame.setHeading("Edition");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
			
			}
		if (publicationtype == 8) {
			horizontBackground = new HorizontalPanel();
			TextField volumeEN = new TextField();
			TextField volumeORG = new TextField();
			TextField volumeTR = new TextField();

			horizontBackground.add(volumeEN);
			horizontBackground.add(volumeTR);
			horizontBackground.add(volumeORG);
			frame = new FramedPanel();
			frame.setHeading("Volume");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
			}
		horizontBackground = new HorizontalPanel();
		TextField yearEN = new TextField();
		TextField yearORG = new TextField();
		TextField yearTR = new TextField();

		horizontBackground.add(yearEN);
		horizontBackground.add(yearTR);
		horizontBackground.add(yearORG);
		frame = new FramedPanel();
		frame.setHeading("Year");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);

		if (publicationtype == 8) {
			horizontBackground = new HorizontalPanel();
			TextField monthEN = new TextField();
			TextField monthORG = new TextField();
			TextField monthTR = new TextField();

			horizontBackground.add(monthEN);
			horizontBackground.add(monthTR);
			horizontBackground.add(monthORG);
			frame = new FramedPanel();
			frame.setHeading("Month");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		horizontBackground = new HorizontalPanel();
		TextField pagesEN = new TextField();
		TextField pagesORG = new TextField();
		TextField pagesTR = new TextField();

		horizontBackground.add(pagesEN);
		horizontBackground.add(pagesTR);
		horizontBackground.add(pagesORG);
		frame = new FramedPanel();
		frame.setHeading("Pages");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);

		TextArea comments = new TextArea();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(comments);
		frame = new FramedPanel();
		frame.setHeading("Comments");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		TextArea notes = new TextArea();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(notes);
		frame = new FramedPanel();
		frame.setHeading("Notes");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		TextField url = new TextField();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(url);
		frame = new FramedPanel();
		frame.setHeading("URL");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		TextField uri = new TextField();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(uri);
		frame = new FramedPanel();
		frame.setHeading("URI");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		CheckBox unpublished = new CheckBox();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(unpublished);
		frame = new FramedPanel();
		frame.setHeading("Unpublished");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		CheckBox erstauflage = new CheckBox();
		horizontBackground = new HorizontalPanel();
		horizontBackground.add(erstauflage);
		erstauflage.setValue(true);
		frame = new FramedPanel();
		frame.setHeading("FirstEdition");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);
		
		ValueChangeHandler<Boolean> checkBoxHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
if(event.getValue()  == false){
				erstauflageComboBox = new ComboBox<AnnotatedBiblographyEntry>(AnnotatedBiblographyEntryListStore, annotatedBiblographyEntryProps.titleEN(),
						new AbstractSafeHtmlRenderer<AnnotatedBiblographyEntry>() {

							@Override
							public SafeHtml render(AnnotatedBiblographyEntry item) {
								final AnnotatedBiblographyEntryViewTemplates pvTemplates = GWT.create(AnnotatedBiblographyEntryViewTemplates.class);
								return pvTemplates.AnnotatedBiblographyEntry(item.getTitleEN());
							}
						});
}
else{
	erstauflageComboBox.removeFromParent();
}
				horizontBackground = new HorizontalPanel();
				horizontBackground.add(erstauflageComboBox);
				frame = new FramedPanel();
				frame.setHeading("Erstauflage");
				frame.add(horizontBackground);
				thirdTabVLC.add(frame);
				
			}
		};
		
		erstauflage.addValueChangeHandler(checkBoxHandler);
	}

}

interface PublisherViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml publisher(String name);
}

interface AnnotatedBiblographyEntryViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml AnnotatedBiblographyEntry(String name);
}

interface PublicationTypeViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml publicationType(String name);
}

interface PublisherProperties extends PropertyAccess<PublisherEntry> {
	ModelKeyProvider<PublisherEntry> publisherID();

	LabelProvider<PublisherEntry> name();
}

interface AnnotatedBiblographyEntryProperties extends PropertyAccess<AnnotatedBiblographyEntry> {
	ModelKeyProvider<AnnotatedBiblographyEntry> annotatedBiblographyID();

	LabelProvider<AnnotatedBiblographyEntry> titleEN();
}

interface PublicationTypeProperties extends PropertyAccess<PublicationTypeEntry> {
	ModelKeyProvider<PublicationTypeEntry> publicationTypeID();

	LabelProvider<PublicationTypeEntry> name();
}

interface AuthorProperties extends PropertyAccess<AuthorEntry> {
	ModelKeyProvider<AuthorEntry> authorID();

	ValueProvider<AuthorEntry, String> name();
}
