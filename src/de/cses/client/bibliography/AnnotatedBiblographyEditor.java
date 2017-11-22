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
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
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
import de.cses.shared.AuthorAnnotatedRelation;
import de.cses.shared.AuthorEntry;
import de.cses.shared.EditorAnnotatedRelation;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	HorizontalLayoutContainer horizontBackground;
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
	
	FramedPanel original;
	FramedPanel eng;
	FramedPanel trans;

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
	
	TextField accessEN;
	TextField accessORG;
	TextField accessTR;
	
	TextField titeladdonEN;
	TextField titeladdonORG;
	TextField titeladdonTR;
	
	TextField seriesEN;
	TextField seriesORG;
	TextField seriesTR;
	
	
	TextField editionEN;
	TextField editionORG;
	TextField editionTR;
	
	TextField volumeEN;
	TextField volumeORG;
	TextField volumeTR;
	
	TextField yearEN;
	TextField yearORG;
	TextField yearTR;
	
	TextField monthEN;
	TextField monthORG;
	TextField monthTR;
	
	TextField pagesEN;
	TextField pagesORG;
	TextField pagesTR;
	
	TextArea comments ;
	TextArea notes;
	TextField url;
	TextField uri;
	CheckBox unpublished;
	CheckBox erstauflage;
	
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
	
	public void save(){
		AnnotatedBiblographyEntry bib= new AnnotatedBiblographyEntry();
		
	
		bib.setTitleaddonEN(titeladdonEN.getText());
		bib.setTitleaddonORG(titeladdonORG.getText());
		bib.setTitleaddonTR(titeladdonTR.getText());
		
		bib.setTitleEN(titelEN.getText());
		bib.setTitleORG(titelORG.getText());
		bib.setTitleTR(titelORG.getText());
		
		if (publicationtype == 7) {
		bib.setAccessdateEN(accessEN.getText());
		bib.setAccessdateORG(accessORG.getText());
		bib.setAccessdateTR(accessTR.getText());
		}
		
		if (publicationtype == 1) {
		bib.setBookTitleEN(booktitelEN.getText());
		bib.setBookTitleORG(booktitelORG.getText());
		bib.setBookTitleTR(booktitelTR.getText());
		}
		
		if (publicationtype == 5) {
		bib.setChapTitleEN(chaptitEN.getText());
		bib.setChapTitleORG(booktitelORG.getText());
		bib.setChapTitleTR(booktitelTR.getText());
		}
		
		bib.setComments(comments.getText());
		
		if (publicationtype == 1 || publicationtype == 5) {
		bib.setEditionEN(editionEN.getText());
		bib.setEditionORG(editionORG.getText());
		bib.setEditionTR(editionTR.getText());
		}
		
		bib.setErstauflage(erstauflage.getValue());
		
		if (publicationtype == 8) {
		bib.setMonthEN(monthEN.getText());
		bib.setMonthORG(monthORG.getText());
		bib.setMonthTR(monthTR.getText());
		}
		
		bib.setErstauflageID(erstauflageComboBox.getValue().getAnnotatedBiblographyID());
		
		bib.setNotes(notes.getText());
		
		if (publicationtype == 8) {
		bib.setNumberEN(numberEN.getText());
		bib.setNumberORG(numberORG.getText());
		bib.setNumberTR(numberTR.getText());
		}
		
		bib.setPagesEN(pagesEN.getText());
		bib.setPagesORG(pagesORG.getText());
		bib.setPagesTR(pagesTR.getText());
		
		if (publicationtype == 6) {
		bib.setProcTitleEN(procEN.getText());
		bib.setProcTitleORG(procORG.getText());
		bib.setProcTitleTR(procTR.getText());
		}
		
		bib.setPublisherID(publisherComboBox.getValue().getPublisherID());
		
		if (publicationtype == 8) {
		bib.setSerieEN(seriesEN.getText());
		bib.setSerieORG(seriesORG.getText());
		bib.setSerieTR(seriesTR.getText());
		}
		
		if (publicationtype == 3) {
		bib.setUniversityEN(uniEN.getText());
		bib.setUniversityORG(uniORG.getText());
		bib.setUniversityTR(uniTR.getText());
		}
		
		bib.setUnpublished(unpublished.getValue());
		
		bib.setUri(uri.getText());
		bib.setUrl(url.getText());
		
		if (publicationtype == 8) {
		bib.setVolumeEN(volumeEN.getText());
		bib.setVolumeORG(volumeORG.getText());
		bib.setVolumeTR(volumeTR.getText());
		}
		
		bib.setYearEN(yearEN.getText());
		bib.setYearORG(yearORG.getText());
		bib.setYearTR(yearTR.getText());
		
		if (publicationtype != 6) {
			for(int i = 0; i< selectedAuthorListStore.size(); i++){
				AuthorEntry author = selectedAuthorListStore.get(i);
				AuthorAnnotatedRelation relation = new AuthorAnnotatedRelation(author, bib);
				bib.getAuthorAnnotatedList().add(relation);
			}
			
		}
		for(int i = 0; i< selectedEditorListStore.size(); i++){
			AuthorEntry editor = selectedEditorListStore.get(i);
			EditorAnnotatedRelation relation = new EditorAnnotatedRelation(bib, editor);
			bib.getEditorAnnotatedList().add(relation);
		}
		
		//dbService.saveAnnotatedBiblography(
		
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
	
		 horizontBackground = new HorizontalLayoutContainer();
		 
			frame = new FramedPanel();
			frame.setHeading("Titel");
			frame.add(horizontBackground);
			firstTabVLC.add(frame, new VerticalLayoutData(1.0, .5));
			
		titelEN = new TextField();
		titelORG = new TextField();
		titelTR = new TextField();
		
		original = new FramedPanel();
		original.setHeading("Original");
		trans = new FramedPanel();
		trans.setHeading("Transkription");
		eng = new FramedPanel();
		eng.setHeading("English");
		
		original.add(titelORG);
		trans.add(titelTR);
		eng.add(titelEN);

		horizontBackground.add(eng, new HorizontalLayoutData(.3, .1));
		horizontBackground.add(original, new HorizontalLayoutData(.3, .1));
		horizontBackground.add(trans, new HorizontalLayoutData(.3, .1));
		


		if (publicationtype == 6) {
			horizontBackground = new HorizontalLayoutContainer();
			procEN = new TextField();
			procORG = new TextField();
			procTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(procORG);
			trans.add(procTR);
			eng.add(procEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Proceedings Title");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 5) {
			horizontBackground = new HorizontalLayoutContainer();
			chaptitEN = new TextField();
			chaptitORG = new TextField();
			chaptitTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(chaptitORG);
			trans.add(chaptitTR);
			eng.add(chaptitEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Chapter Title");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 1) {

			horizontBackground = new HorizontalLayoutContainer();
			booktitelEN = new TextField();
			booktitelORG = new TextField();
			booktitelTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(booktitelORG);
			trans.add(booktitelTR);
			eng.add(booktitelEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Booktitle");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 3) {
			horizontBackground = new HorizontalLayoutContainer();
			uniEN = new TextField();
			uniORG = new TextField();
			uniTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(uniORG);
			trans.add(uniTR);
			eng.add(uniEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("University");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 8) {

			horizontBackground = new HorizontalLayoutContainer();
			numberEN = new TextField();
			numberORG = new TextField();
			numberTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(numberORG);
			trans.add(numberTR);
			eng.add(numberEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Number");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 7) {
			horizontBackground = new HorizontalLayoutContainer();
			accessEN = new TextField();
			accessORG = new TextField();
			accessTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(accessORG);
			trans.add(accessTR);
			eng.add(accessEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Access Date");
			frame.add(horizontBackground);
			thirdTabVLC.add(frame);
		}

		horizontBackground = new HorizontalLayoutContainer();
		titeladdonEN = new TextField();
		titeladdonORG = new TextField();
		titeladdonTR = new TextField();

		original = new FramedPanel();
		original.setHeading("Original");
		trans = new FramedPanel();
		trans.setHeading("Transkription");
		eng = new FramedPanel();
		eng.setHeading("English");
		
		horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));

		horizontBackground.add(eng);
		horizontBackground.add(original);
		horizontBackground.add(trans);
		
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
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(publisherComboBox);
		frame = new FramedPanel();
		frame.setHeading("Publisher");
		frame.add(horizontBackground);
		secoundTabVLC.add(frame);
		authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore, authorProps.name(), new TextCell());

		editorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedEditorListStore, authorProps.name(), new TextCell());

		if (publicationtype != 6) {
			horizontBackground = new HorizontalLayoutContainer();
			horizontBackground.add(authorSelection, new HorizontalLayoutData(0.1, 0.1));
			frame = new FramedPanel();
			frame.setHeading("Author");
			frame.add(horizontBackground);
			secoundTabVLC.add(frame);
		}

		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(editorSelection, new HorizontalLayoutData(0.1, 0.1));
		frame = new FramedPanel();
		frame.setHeading("Editor");
		frame.add(horizontBackground);
		secoundTabVLC.add(frame);
		
		if (publicationtype == 8) {
			horizontBackground = new HorizontalLayoutContainer();
			seriesEN = new TextField();
			seriesORG = new TextField();
			seriesTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(seriesORG);
			trans.add(seriesTR);
			eng.add(seriesEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Serie");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		if (publicationtype == 1 || publicationtype == 5) {

			horizontBackground = new HorizontalLayoutContainer();
			editionEN = new TextField();
			editionORG = new TextField();
			editionTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));

			horizontBackground.add(eng);
			horizontBackground.add(original);
			horizontBackground.add(trans);
			
			frame = new FramedPanel();
			frame.setHeading("Edition");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
			
			}
		if (publicationtype == 8) {
			horizontBackground = new HorizontalLayoutContainer();
			volumeEN = new TextField();
			volumeORG = new TextField();
			volumeTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(volumeORG);
			trans.add(volumeTR);
			eng.add(volumeEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Volume");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
			}
		horizontBackground = new HorizontalLayoutContainer();
		yearEN = new TextField();
		yearORG = new TextField();
		yearTR = new TextField();

		original = new FramedPanel();
		original.setHeading("Original");
		trans = new FramedPanel();
		trans.setHeading("Transkription");
		eng = new FramedPanel();
		eng.setHeading("English");
		
		original.add(yearORG);
		trans.add(yearTR);
		eng.add(yearEN);

		horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
		
		frame = new FramedPanel();
		frame.setHeading("Year");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);

		if (publicationtype == 8) {
			horizontBackground = new HorizontalLayoutContainer();
			monthEN = new TextField();
			monthORG = new TextField();
			monthTR = new TextField();

			original = new FramedPanel();
			original.setHeading("Original");
			trans = new FramedPanel();
			trans.setHeading("Transkription");
			eng = new FramedPanel();
			eng.setHeading("English");
			
			original.add(monthORG);
			trans.add(monthTR);
			eng.add(monthEN);

			horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
			horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
			
			frame = new FramedPanel();
			frame.setHeading("Month");
			frame.add(horizontBackground);
			firstTabVLC.add(frame);
		}

		horizontBackground = new HorizontalLayoutContainer();
	  pagesEN = new TextField();
		pagesORG = new TextField();
		pagesTR = new TextField();

		original = new FramedPanel();
		original.setHeading("Original");
		trans = new FramedPanel();
		trans.setHeading("Transkription");
		eng = new FramedPanel();
		eng.setHeading("English");
		
		original.add(pagesORG);
		trans.add(pagesTR);
		eng.add(pagesEN);

		horizontBackground.add(eng, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(original, new HorizontalLayoutData(0.3, 0.1));
		horizontBackground.add(trans, new HorizontalLayoutData(0.3, 0.1));
		frame = new FramedPanel();
		frame.setHeading("Pages");
		frame.add(horizontBackground);
		firstTabVLC.add(frame);

		comments = new TextArea();
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(comments, new HorizontalLayoutData(1.0, 0.1) );
		frame = new FramedPanel();
		frame.setHeading("Comments");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		notes = new TextArea();
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(notes, new HorizontalLayoutData(1.0, 0.1));
		frame = new FramedPanel();
		frame.setHeading("Notes");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		 url = new TextField();
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(url, new HorizontalLayoutData(1.0, 0.1));
		frame = new FramedPanel();
		frame.setHeading("URL");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		uri = new TextField();
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(uri, new HorizontalLayoutData(1.0, 0.1));
		frame = new FramedPanel();
		frame.setHeading("URI");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		unpublished = new CheckBox();
		horizontBackground = new HorizontalLayoutContainer();
		horizontBackground.add(unpublished);
		frame = new FramedPanel();
		frame.setHeading("Unpublished");
		frame.add(horizontBackground);
		thirdTabVLC.add(frame);

		erstauflage = new CheckBox();
		horizontBackground = new HorizontalLayoutContainer();
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
				horizontBackground = new HorizontalLayoutContainer();
				horizontBackground.add(erstauflageComboBox, new HorizontalLayoutData(1.0, 0.1));
				frame = new FramedPanel();
				frame.setHeading("Erstauflage");
				frame.add(horizontBackground);
				thirdTabVLC.add(frame);
}
else{
	erstauflageComboBox.removeFromParent();
}
		
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
