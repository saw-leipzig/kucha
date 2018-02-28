/*
 * Copyright 2017 - 2018
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
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.ibm.icu.impl.CalendarAstronomer.Horizon;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
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
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
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
//	private HorizontalLayoutContainer horizontBackground;
	private AnnotatedBiblographyEntry entry;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

//	private FramedPanel frame;

	private DualListField<AuthorEntry, String> authorSelection;
	private DualListField<AuthorEntry, String> editorSelection;

	private ComboBox<PublisherEntry> publisherComboBox;
	private ComboBox<AnnotatedBiblographyEntry> firstEditionComboBox;

	private ListStore<PublisherEntry> publisherListStore;
	private ListStore<AnnotatedBiblographyEntry> annotatedBiblographyEntryLS;

	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;

	private PublisherProperties publisherProps;
	private AnnotatedBiblographyEntryProperties annotatedBiblographyEntryProps;
	private AuthorProperties authorProps;

	private TabPanel tabpanel;
//	private VerticalLayoutContainer firstTabVLC = new VerticalLayoutContainer();
//	private VerticalLayoutContainer secoundTabVLC = new VerticalLayoutContainer();
//	private VerticalLayoutContainer thirdTabVLC = new VerticalLayoutContainer();
//	private VerticalLayoutContainer firstsecoundTabVLC = new VerticalLayoutContainer();

	// VerticalLayoutContainer mainInputVLC = new VerticalLayoutContainer();
	private FramedPanel framefirstedition;

//	private FramedPanel original;
//	private FramedPanel eng;
//	private FramedPanel trans;

	private FramedPanel mainFP = null; // das oberste Framed Panel als Rahmen
	private VerticalLayoutContainer backgroundoverview = new VerticalLayoutContainer(); // verticaler background fuer die Lioteratur

	private TextField titelEN;
	private TextField titelORG;
	private TextField titelTR;

	private TextField procEN;
	private TextField procORG;
	private TextField procTR;

	private TextField chapterTitleEN;
	private TextField chapterTitleORG;
	private TextField chapterTitleTR;

	private TextField booktitelEN;
	private TextField booktitelORG;
	private TextField booktitelTR;

	private TextField uniEN;
	private TextField uniORG;
	private TextField uniTR;

	private TextField numberEN;
	private TextField numberORG;
	private TextField numberTR;

	private TextField accessEN;
	private TextField accessORG;
	private TextField accessTR;

	private TextField titeladdonEN;
	private TextField titeladdonORG;
	private TextField titeladdonTR;

	private TextField seriesEN;
	private TextField seriesORG;
	private TextField seriesTR;

	private TextField editionEN;
	private TextField editionORG;
	private TextField editionTR;

	private TextField volumeEN;
	private TextField volumeORG;
	private TextField volumeTR;

	NumberField<Integer> yearEN;
	private TextField yearORG;
	private TextField yearTR;

	private TextField monthEN;
	private TextField monthORG;
	private TextField monthTR;

	private TextField pagesEN;
	private TextField pagesORG;
	private TextField pagesTR;

	private TextArea comments;
	private TextArea notes;
	private TextField url;
	private TextField uri;
	private CheckBox unpublished;
	private CheckBox firstEditionCB;
	private HorizontalLayoutContainer firstTabHLC = null;
	private VerticalLayoutContainer secondTabVLC = null;
	private VerticalLayoutContainer thirdTabVLC = null;

	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry) {
		this.entry = entry;
	}

	public AnnotatedBiblographyEditor() {	}

	@Override
	public Widget asWidget() {
		if (mainFP == null) {
			init();
			createForm();
		}
		return mainFP;
	}

	public void save() {
//		bib.setTitleaddonEN(titeladdonEN.getText());
//		bib.setTitleaddonORG(titeladdonORG.getText());
//		bib.setTitleaddonTR(titeladdonTR.getText());
//
//		bib.setTitleEN(titelEN.getText());
//		bib.setTitleORG(titelORG.getText());
//		bib.setTitleTR(titelORG.getText());
//
//		if (publicationtype == 7) {
//			bib.setAccessdateEN(accessEN.getText());
//			bib.setAccessdateORG(accessORG.getText());
//			bib.setAccessdateTR(accessTR.getText());
//		}
//
//		if (publicationtype == 1) {
//			bib.setBookTitleEN(booktitelEN.getText());
//			bib.setBookTitleORG(booktitelORG.getText());
//			bib.setBookTitleTR(booktitelTR.getText());
//		}
//
//		if (publicationtype == 5) {
//			bib.setChapTitleEN(chapterTitleEN.getText());
//			bib.setChapTitleORG(chapterTitleORG.getText());
//			bib.setChapTitleTR(chapterTitleTR.getText());
//		}
//
//		bib.setComments(comments.getText());
//
//		if (publicationtype == 1 || publicationtype == 5) {
//			bib.setEditionEN(editionEN.getText());
//			bib.setEditionORG(editionORG.getText());
//			bib.setEditionTR(editionTR.getText());
//		}
//
//		bib.setFirstEdition(firstEditionCB.getValue());
//
//		if (publicationtype == 8) { // bleiben
//			bib.setMonthEN(monthEN.getText());
//			bib.setMonthORG(monthORG.getText());
//			bib.setMonthTR(monthTR.getText());
//		}
//
//		if (firstEditionComboBox.getValue() != null) {
//			bib.setFirstEditionEntry(firstEditionComboBox.getValue());
//		}
//
//		bib.setNotes(notes.getText());
//
//		if (publicationtype == 8) {
//			bib.setNumberEN(numberEN.getText());
//			bib.setNumberORG(numberORG.getText());
//			bib.setNumberTR(numberTR.getText());
//		}
//
//		bib.setPagesEN(pagesEN.getText());
//		bib.setPagesORG(pagesORG.getText());
//		bib.setPagesTR(pagesTR.getText());
//
//		if (publicationtype == 6) {
//			bib.setProcTitleEN(procEN.getText());
//			bib.setProcTitleORG(procORG.getText());
//			bib.setProcTitleTR(procTR.getText());
//		}
//
//		if (publisherComboBox.getValue() != null) {
//			bib.setPublisher(publisherComboBox.getValue());
//		}
//
//		if (publicationtype == 8) {
//			bib.setSerieEN(seriesEN.getText());
//			bib.setSerieORG(seriesORG.getText());
//			bib.setSerieTR(seriesTR.getText());
//		}
//
//		if (publicationtype == 3) {
//			bib.setUniversityEN(uniEN.getText());
//			bib.setUniversityORG(uniORG.getText());
//			bib.setUniversityTR(uniTR.getText());
//		}
//
//		bib.setUnpublished(unpublished.getValue());
//
//		bib.setUri(uri.getText());
//		bib.setUrl(url.getText());
//
//		if (publicationtype == 8) {
//			bib.setVolumeEN(volumeEN.getText());
//			bib.setVolumeORG(volumeORG.getText());
//			bib.setVolumeTR(volumeTR.getText());
//		}
//
//		bib.setYearEN(yearEN.getValue());
//		bib.setYearORG(yearORG.getText());
//		bib.setYearTR(yearTR.getText());
//
//		if (publicationtype != 6) {
//			for (int i = 0; i < selectedAuthorListStore.size(); i++) {
//				AuthorEntry author = selectedAuthorListStore.get(i);
//				AuthorAnnotatedRelation relation = new AuthorAnnotatedRelation(author, bib);
//				bib.getAuthorAnnotatedList().add(relation);
//			}
//
//		}
//		for (int i = 0; i < selectedEditorListStore.size(); i++) {
//			AuthorEntry editor = selectedEditorListStore.get(i);
//			EditorAnnotatedRelation relation = new EditorAnnotatedRelation(bib, editor);
//			bib.getEditorAnnotatedList().add(relation);
//		}

		dbService.insertAnnotatedBiblographyEntry(entry, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer result) {
				entry.setAnnotatedBiblographyID(result);
			}
		});

	}

	public void init() {

		authorProps = GWT.create(AuthorProperties.class);
		selectedAuthorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		selectedEditorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());

		annotatedBiblographyEntryProps = GWT.create(AnnotatedBiblographyEntryProperties.class);
		annotatedBiblographyEntryLS = new ListStore<AnnotatedBiblographyEntry>(annotatedBiblographyEntryProps.annotatedBiblographyID());

		publisherProps = GWT.create(PublisherProperties.class);
		publisherListStore = new ListStore<PublisherEntry>(publisherProps.publisherID());

		dbService.getAuthors(new AsyncCallback<ArrayList<AuthorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<AuthorEntry> result) {
				authorListStore.clear();
				for (AuthorEntry pe : result) {
					authorListStore.add(pe);
				}
			}
		});

		dbService.getPublisher(new AsyncCallback<ArrayList<PublisherEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<PublisherEntry> result) {
				publisherListStore.clear();
				for (PublisherEntry pe : result) {
					publisherListStore.add(pe);
				}
			}
		});
	}

	public void createForm() {

//		publicationTypeComboBox = new ComboBox<PublicationTypeEntry>(publicationTypeListStore, publicationTypeProps.name(),
//				new AbstractSafeHtmlRenderer<PublicationTypeEntry>() {
//
//					@Override
//					public SafeHtml render(PublicationTypeEntry item) {
//						final PublicationTypeViewTemplates pvTemplates = GWT.create(PublicationTypeViewTemplates.class);
//						return pvTemplates.publicationType(item.getName());
//					}
//				});
//		publicationTypeComboBox.setTriggerAction(TriggerAction.ALL);
//		publicationTypeComboBox.setEditable(false);
//		publicationTypeComboBox.setTypeAhead(false);
//		SelectionHandler<PublicationTypeEntry> publicationTypeSelectionHandler = new SelectionHandler<PublicationTypeEntry>() {
//
//			@Override
//			public void onSelection(SelectionEvent<PublicationTypeEntry> event) {
//				rebuildMainInput(event.getSelectedItem().getPublicationTypeID());
//				entry.setPublicationTypeID(event.getSelectedItem().getPublicationTypeID());
//			}
//		};
//		publicationTypeComboBox.addSelectionHandler(publicationTypeSelectionHandler);
//		if (entry != null) {
//			publicationTypeComboBox.setValue(StaticTables.getInstance().getPublicationTypes().get(entry.getPublicationTypeID()));
//		}
//		
//		FramedPanel puplicationTypeFP = new FramedPanel();
//		puplicationTypeFP.setHeading("Publication Type");
//		puplicationTypeFP.add(publicationTypeComboBox);
		
		tabpanel = new TabPanel();

		//		backgroundoverview.add(puplicationTypeFP, new VerticalLayoutData(1.0, .1));
		backgroundoverview.add(tabpanel, new VerticalLayoutData(1.0, 1.0));
		
		rebuildMainInput(entry.getPublicationTypeID());

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
						save();
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

		ToolButton saveToolButton = new ToolButton(ToolButton.SAVE);
		saveToolButton.setToolTip("save");
		saveToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				save();

			}

		});

		mainFP = new FramedPanel();
		mainFP.setHeading("Annotated Biblography");
		mainFP.setSize("900px", "650px"); // here we set the size of the panel
		mainFP.add(backgroundoverview, new VerticalLayoutData(1.0, 1.0));
		mainFP.addTool(saveToolButton);
		mainFP.addTool(closeToolButton);

	}

	public void rebuildMainInput(int publicationtype) {
		// mainInputVLC.clear();
//		backgroundoverview.remove(tabpanel);
//		tabpanel = new TabPanel();
//		backgroundoverview.add(tabpanel, new VerticalLayoutData(1.0, 0.9));

//		if (firstTabHLC != null) tabpanel.remove(firstTabHLC);
//		if (secondTabVLC != null) tabpanel.remove(secondTabVLC);
//		if (thirdTabVLC != null) tabpanel.remove(thirdTabVLC);

//		VerticalLayoutContainer firstTabVLC = new VerticalLayoutContainer();
		firstTabHLC = new HorizontalLayoutContainer();
		VerticalLayoutContainer firstTabInnerLeftVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer firstTabInnerRightVLC = new VerticalLayoutContainer();
//		VerticalLayoutContainer firstsecoundTabVLC = new VerticalLayoutContainer();
		secondTabVLC = new VerticalLayoutContainer();
		thirdTabVLC = new VerticalLayoutContainer();
		// mainInputVLC.add(tabpanel, new VerticalLayoutData(1.0, 1.0));
		
		tabpanel.add(firstTabHLC, "Basics");
//		tabpanel.add(firstsecoundTabVLC, "2. Basics");
		tabpanel.add(secondTabVLC, "Authors and Editors");
		tabpanel.add(thirdTabVLC, "Others");
		tabpanel.setTabScroll(false);

//		firstTabVLC.setWidth("890px");
//		firstTabVLC.setHeight("595px");
//		firstsecoundTabVLC.setWidth("890px");
//		firstsecoundTabVLC.setHeight("595px");
//		secoundTabVLC.setWidth("890px");
//		secoundTabVLC.setHeight("595px");
//		thirdTabVLC.setWidth("890px");
//		thirdTabVLC.setHeight("595px");

//		horizontBackground = new HorizontalLayoutContainer();

		titelEN = new TextField();
		titelORG = new TextField();
		titelTR = new TextField();
		
		VerticalLayoutContainer titelVLC = new VerticalLayoutContainer();
		titelVLC.add(new FieldLabel(titelEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		titelVLC.add(new FieldLabel(titelORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		titelVLC.add(new FieldLabel(titelTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel titleFP = new FramedPanel();
		titleFP.setHeading("Titel");
		titleFP.add(titelVLC);

//		firstTabVLC.add(titleFP, new VerticalLayoutData(1.0, .2));
		firstTabHLC.add(firstTabInnerLeftVLC, new HorizontalLayoutData(.65, 1.0));
		firstTabHLC.add(firstTabInnerRightVLC, new HorizontalLayoutData(.35, 1.0));
		
		
		firstTabInnerLeftVLC.add(titleFP, new VerticalLayoutData(1.0, 1.0 / 5));
		

//		original = new FramedPanel();
//		original.setHeading("Original");
//		trans = new FramedPanel();
//		trans.setHeading("Transkription");
//		eng = new FramedPanel();
//		eng.setHeading("English");
//
//		original.add(titelORG);
//		trans.add(titelTR);
//		eng.add(titelEN);

//		horizontBackground.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//		horizontBackground.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//		horizontBackground.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

		if (entry != null) {
			titelEN.setText(entry.getTitleEN());
			titelORG.setText(entry.getTitleORG());
			titelTR.setText(entry.getTitleTR());
		}

		if (publicationtype == 6) {
//			HorizontalLayoutContainer proceedingsHLC = new HorizontalLayoutContainer();
			procEN = new TextField();
			procORG = new TextField();
			procTR = new TextField();

			VerticalLayoutContainer proceedingsVLC = new VerticalLayoutContainer();
			proceedingsVLC.add(new FieldLabel(procEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			proceedingsVLC.add(new FieldLabel(procORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			proceedingsVLC.add(new FieldLabel(procTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(procORG);
//			trans.add(procTR);
//			eng.add(procEN);

//			proceedingsHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			proceedingsHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			proceedingsHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel proceedingsFP = new FramedPanel();
			proceedingsFP.setHeading("Proceedings Title");
			proceedingsFP.add(proceedingsVLC);
			firstTabInnerLeftVLC.add(proceedingsFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				procEN.setText(entry.getProcTitleEN());
				procORG.setText(entry.getProcTitleORG());
				procTR.setText(entry.getProcTitleTR());
			}
		}

		if (publicationtype == 5) {
//			HorizontalLayoutContainer chapterHLC = new HorizontalLayoutContainer();
			chapterTitleEN = new TextField();
			chapterTitleORG = new TextField();
			chapterTitleTR = new TextField();
			
			VerticalLayoutContainer chapterTitleVLC = new VerticalLayoutContainer();
			chapterTitleVLC.add(new FieldLabel(chapterTitleEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			chapterTitleVLC.add(new FieldLabel(chapterTitleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			chapterTitleVLC.add(new FieldLabel(chapterTitleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(chapterTitleORG);
//			trans.add(chapterTitleTR);
//			eng.add(chapterTitleEN);
//
//			chapterHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			chapterHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			chapterHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel chapterFP = new FramedPanel();
			chapterFP.setHeading("Chapter Title");
			chapterFP.add(chapterTitleVLC);
			firstTabInnerLeftVLC.add(chapterFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				chapterTitleEN.setText(entry.getChapTitleEN());
				chapterTitleORG.setText(entry.getChapTitleORG());
				chapterTitleTR.setText(entry.getChapTitleTR());
			}
		}

		if (publicationtype == 1) {

//			HorizontalLayoutContainer bookTitleHLC = new HorizontalLayoutContainer();
			booktitelEN = new TextField();
			booktitelORG = new TextField();
			booktitelTR = new TextField();

			VerticalLayoutContainer bookTitleVLC = new VerticalLayoutContainer();
			bookTitleVLC.add(new FieldLabel(booktitelEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			bookTitleVLC.add(new FieldLabel(booktitelORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			bookTitleVLC.add(new FieldLabel(booktitelTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(booktitelORG);
//			trans.add(booktitelTR);
//			eng.add(booktitelEN);
//
//			bookTitleHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			bookTitleHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			bookTitleHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel bookTitleFP = new FramedPanel();
			bookTitleFP.setHeading("Booktitle");
			bookTitleFP.add(bookTitleVLC);
			firstTabInnerLeftVLC.add(bookTitleFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				booktitelEN.setText(entry.getBookTitleEN());
				booktitelORG.setText(entry.getBookTitleORG());
				booktitelTR.setText(entry.getBookTitleTR());
			}
		}

		if (publicationtype == 3) {
//			HorizontalLayoutContainer universityHLC = new HorizontalLayoutContainer();
			uniEN = new TextField();
			uniORG = new TextField();
			uniTR = new TextField();

			VerticalLayoutContainer universityVLC = new VerticalLayoutContainer();
			universityVLC.add(new FieldLabel(uniEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(uniORG);
//			trans.add(uniTR);
//			eng.add(uniEN);
//
//			universityHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			universityHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			universityHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel universityFP = new FramedPanel();
			universityFP.setHeading("University");
			universityFP.add(universityVLC);
			firstTabInnerLeftVLC.add(universityFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				uniEN.setText(entry.getUniversityEN());
				uniORG.setText(entry.getUniversityORG());
				uniTR.setText(entry.getUniversityTR());
			}

		}

		if (publicationtype == 8) { // achtung hier muss sie bleiben

//			HorizontalLayoutContainer numberHLC = new HorizontalLayoutContainer();
			numberEN = new TextField();
			numberORG = new TextField();
			numberTR = new TextField();

			VerticalLayoutContainer numberVLC = new VerticalLayoutContainer();
			numberVLC.add(new FieldLabel(numberEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			numberVLC.add(new FieldLabel(numberORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			numberVLC.add(new FieldLabel(numberTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(numberORG);
//			trans.add(numberTR);
//			eng.add(numberEN);
//
//			numberHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			numberHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			numberHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel numberFP = new FramedPanel();
			numberFP.setHeading("Number");
			numberFP.add(numberVLC);
			firstTabInnerRightVLC.add(numberFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				numberEN.setText(entry.getNumberEN());
				numberORG.setText(entry.getNumberORG());
				numberTR.setText(entry.getNumberTR());
			}
		}

		if (publicationtype == 7) {
//			HorizontalLayoutContainer accessDateHLC = new HorizontalLayoutContainer();
			accessEN = new TextField();
			accessORG = new TextField();
			accessTR = new TextField();

			VerticalLayoutContainer accessDateVLC = new VerticalLayoutContainer();
			accessDateVLC.add(new FieldLabel(accessEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(accessORG);
//			trans.add(accessTR);
//			eng.add(accessEN);
//
//			accessDateHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			accessDateHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			accessDateHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel accessDateFP = new FramedPanel();
			accessDateFP.setHeading("Access Date");
			accessDateFP.add(accessDateVLC, new HorizontalLayoutData(1.0, 1.0));
			firstTabInnerRightVLC.add(accessDateFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				accessEN.setText(entry.getAccessdateEN());
				accessORG.setText(entry.getAccessdateORG());
				accessTR.setText(entry.getAccessdateTR());
			}
		}

//		HorizontalLayoutContainer titleAddonHLC = new HorizontalLayoutContainer();
		titeladdonEN = new TextField();
		titeladdonORG = new TextField();
		titeladdonTR = new TextField();

		VerticalLayoutContainer titleAddonVLC = new VerticalLayoutContainer();
		titleAddonVLC.add(new FieldLabel(titeladdonEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		titleAddonVLC.add(new FieldLabel(titeladdonORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		titleAddonVLC.add(new FieldLabel(titeladdonTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//		original = new FramedPanel();
//		original.setHeading("Original");
//		trans = new FramedPanel();
//		trans.setHeading("Transkription");
//		eng = new FramedPanel();
//		eng.setHeading("English");
//
//		original.add(titeladdonORG);
//		trans.add(titeladdonTR);
//		eng.add(titeladdonEN);
//
//		titleAddonHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//		titleAddonHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//		titleAddonHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

		FramedPanel titleAddonFP = new FramedPanel();
		titleAddonFP.setHeading("Titleaddon");
		titleAddonFP.add(titleAddonVLC);
		firstTabInnerLeftVLC.add(titleAddonFP, new VerticalLayoutData(1.0, 1.0 / 5));

		if (entry != null) {
			titeladdonEN.setText(entry.getTitleaddonEN());
			titeladdonORG.setText(entry.getTitleaddonORG());
			titeladdonTR.setText(entry.getTitleaddonTR());
		}

		publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.name(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getName());
					}
				});
		HorizontalLayoutContainer publisherHLC = new HorizontalLayoutContainer();
		publisherHLC.add(publisherComboBox, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel publisherFP = new FramedPanel();
		publisherFP.setHeading("Publisher");
		publisherFP.add(publisherHLC, new VerticalLayoutData(1.0, 1.0));
		secondTabVLC.add(publisherFP, new VerticalLayoutData(1.0, 1.0 / 6));

		if (entry != null) {
//			publisherComboBox.setValue(entry.getPublisher());
		}

		authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore, authorProps.name(), new TextCell());

		editorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedEditorListStore, authorProps.name(), new TextCell());

		if (publicationtype != 6) {
			HorizontalLayoutContainer authorHLC = new HorizontalLayoutContainer();
			authorHLC.add(authorSelection, new HorizontalLayoutData(1.0, 1.0));
			FramedPanel authorFP = new FramedPanel();
			authorFP.setHeading("Author");
			authorFP.add(authorHLC, new VerticalLayoutData(1.0, 1.0));
			secondTabVLC.add(authorFP, new VerticalLayoutData(1.0, 1.0 / 3));
		}

		HorizontalLayoutContainer editorHLC = new HorizontalLayoutContainer();
		editorHLC.add(editorSelection, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel editorFP = new FramedPanel();
		editorFP.setHeading("Editor");
		editorFP.add(editorHLC, new VerticalLayoutData(1.0, 1.0));
		secondTabVLC.add(editorFP, new VerticalLayoutData(1.0, 1.0 / 3));

		if (publicationtype == 8) { // hier muss sie bleiben
//			HorizontalLayoutContainer seriesHLC = new HorizontalLayoutContainer();
			seriesEN = new TextField();
			seriesORG = new TextField();
			seriesTR = new TextField();

			VerticalLayoutContainer seriesVLC = new VerticalLayoutContainer();
			seriesVLC.add(new FieldLabel(seriesEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(seriesORG);
//			trans.add(seriesTR);
//			eng.add(seriesEN);
//
//			seriesHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			seriesHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			seriesHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel seriesFP = new FramedPanel();
			seriesFP.setHeading("Series");
			seriesFP.add(seriesVLC);
			firstTabInnerRightVLC.add(seriesFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				seriesEN.setText(entry.getSeriesEN());
				seriesORG.setText(entry.getSeriesORG());
				seriesTR.setText(entry.getSeriesTR());
			}
		}

		if (publicationtype == 1 || publicationtype == 5) {

//			HorizontalLayoutContainer editionHLC = new HorizontalLayoutContainer();
			editionEN = new TextField();
			editionORG = new TextField();
			editionTR = new TextField();

			VerticalLayoutContainer editionVLC = new VerticalLayoutContainer();
			editionVLC.add(new FieldLabel(editionEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			editionVLC.add(new FieldLabel(editionORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			editionVLC.add(new FieldLabel(editionTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(editionORG);
//			trans.add(editionTR);
//			eng.add(editionEN);
//
//			editionHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			editionHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			editionHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel editionFP = new FramedPanel();
			editionFP.setHeading("Edition");
			editionFP.add(editionVLC);
			firstTabInnerRightVLC.add(editionFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				editionEN.setText(entry.getEditionEN());
				editionORG.setText(entry.getEditionORG());
				editionTR.setText(entry.getEditionTR());
			}

		}
		if (publicationtype == 8) {
//			HorizontalLayoutContainer volumeHLC = new HorizontalLayoutContainer();
			volumeEN = new TextField();
			volumeORG = new TextField();
			volumeTR = new TextField();

			VerticalLayoutContainer volumeVLC = new VerticalLayoutContainer();
			volumeVLC.add(new FieldLabel(volumeEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(volumeORG);
//			trans.add(volumeTR);
//			eng.add(volumeEN);
//
//			volumeHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			volumeHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			volumeHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel volumeFP = new FramedPanel();
			volumeFP.setHeading("Volume");
			volumeFP.add(volumeVLC);
			firstTabInnerRightVLC.add(volumeFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				volumeEN.setText(entry.getVolumeEN());
				volumeORG.setText(entry.getVolumeORG());
				volumeTR.setText(entry.getVolumeTR());
			}

		}
//		HorizontalLayoutContainer yearHLC = new HorizontalLayoutContainer();
		yearEN = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		DateWrapper dw = new DateWrapper(); // we always want to use the current year as max year
		yearEN.addValidator(new MaxNumberValidator<Integer>(dw.getFullYear()));
		yearEN.setAllowNegative(false);

		yearORG = new TextField();
		yearTR = new TextField();

		VerticalLayoutContainer yearVLC = new VerticalLayoutContainer();
		yearVLC.add(new FieldLabel(yearEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//		original = new FramedPanel();
//		original.setHeading("Original");
//		trans = new FramedPanel();
//		trans.setHeading("Transkription");
//		eng = new FramedPanel();
//		eng.setHeading("English");
//
//		original.add(yearORG);
//		trans.add(yearTR);
//		eng.add(yearEN);
//
//		yearHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//		yearHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//		yearHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

		FramedPanel yearFP = new FramedPanel();
		yearFP.setHeading("Year");
		yearFP.add(yearVLC);
		firstTabInnerRightVLC.add(yearFP, new VerticalLayoutData(1.0, 1.0 / 5));

		if (entry != null) {
			yearEN.setValue(entry.getYearEN());
			yearORG.setText(entry.getYearORG());
			yearTR.setText(entry.getYearTR());
		}

		if (publicationtype == 8) { // bleiben
//			HorizontalLayoutContainer monthHLC = new HorizontalLayoutContainer();
			monthEN = new TextField();
			monthORG = new TextField();
			monthTR = new TextField();

			VerticalLayoutContainer monthVLC = new VerticalLayoutContainer();
			monthVLC.add(new FieldLabel(monthEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			monthVLC.add(new FieldLabel(monthORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			monthVLC.add(new FieldLabel(monthTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//			original = new FramedPanel();
//			original.setHeading("Original");
//			trans = new FramedPanel();
//			trans.setHeading("Transkription");
//			eng = new FramedPanel();
//			eng.setHeading("English");
//
//			original.add(monthORG);
//			trans.add(monthTR);
//			eng.add(monthEN);
//
//			monthHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//			monthHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//			monthHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

			FramedPanel monthFP = new FramedPanel();
			monthFP.setHeading("Month");
			monthFP.add(monthVLC);
			firstTabInnerRightVLC.add(monthFP, new VerticalLayoutData(1.0, 1.0 / 5));

			if (entry != null) {
				monthEN.setText(entry.getMonthEN());
				monthORG.setText(entry.getMonthORG());
				monthTR.setText(entry.getMonthTR());
			}

		}

//		HorizontalLayoutContainer pagesHLC = new HorizontalLayoutContainer();
		pagesEN = new TextField();
		pagesORG = new TextField();
		pagesTR = new TextField();

		VerticalLayoutContainer pagesVLC = new VerticalLayoutContainer();
		pagesVLC.add(new FieldLabel(pagesEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		pagesVLC.add(new FieldLabel(pagesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		pagesVLC.add(new FieldLabel(pagesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

//		original = new FramedPanel();
//		original.setHeading("Original");
//		trans = new FramedPanel();
//		trans.setHeading("Transkription");
//		eng = new FramedPanel();
//		eng.setHeading("English");
//
//		original.add(pagesORG);
//		trans.add(pagesTR);
//		eng.add(pagesEN);
//
//		pagesHLC.add(eng, new HorizontalLayoutData(1.0 / 3, 1.0));
//		pagesHLC.add(original, new HorizontalLayoutData(1.0 / 3, 1.0));
//		pagesHLC.add(trans, new HorizontalLayoutData(1.0 / 3, 1.0));

		FramedPanel pagesFP = new FramedPanel();
		pagesFP.setHeading("Pages");
		pagesFP.add(pagesVLC);
		firstTabInnerRightVLC.add(pagesFP, new VerticalLayoutData(1.0, 1.0 / 5));

		if (entry != null) {
			pagesEN.setText(entry.getPagesEN());
			pagesORG.setText(entry.getPagesORG());
			pagesTR.setText(entry.getPagesTR());
		}

		comments = new TextArea();
		HorizontalLayoutContainer commentsHLC = new HorizontalLayoutContainer();
		commentsHLC.add(comments, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel commentsFP = new FramedPanel();
		commentsFP.setHeading("Comments");
		commentsFP.add(commentsHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(commentsFP, new VerticalLayoutData(1.0, 1.0 / 7));

		if (entry != null) {
			comments.setText(entry.getComments());
		}

		notes = new TextArea();
		HorizontalLayoutContainer notesHLC = new HorizontalLayoutContainer();
		notesHLC.add(notes, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel notesFP = new FramedPanel();
		notesFP.setHeading("Notes");
		notesFP.add(notesHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(notesFP, new VerticalLayoutData(1.0, 1.0 / 7));
		if (entry != null) {
			notes.setText(entry.getNotes());
		}

		url = new TextField();
		HorizontalLayoutContainer urlHLC = new HorizontalLayoutContainer();
		urlHLC.add(url, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel urlFP = new FramedPanel();
		urlFP.setHeading("URL");
		urlFP.add(urlHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(urlFP, new VerticalLayoutData(1.0, 1.0 / 16));
		if (entry != null) {
			url.setText(entry.getUrl());
		}

		uri = new TextField();
		HorizontalLayoutContainer uriHLC = new HorizontalLayoutContainer();
		uriHLC.add(uri, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel uriFP = new FramedPanel();
		uriFP.setHeading("URI");
		uriFP.add(uriHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(uriFP, new VerticalLayoutData(1.0, 1.0 / 16));

		if (entry != null) {
			uri.setText(entry.getUri());
		}

		unpublished = new CheckBox();
		HorizontalLayoutContainer unpublishedHLC = new HorizontalLayoutContainer();
		unpublishedHLC.add(unpublished, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel unpublishedFP = new FramedPanel();
		unpublishedFP.setHeading("Unpublished");
		unpublishedFP.add(unpublishedHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(unpublishedFP, new VerticalLayoutData(1.0, 1.0 / 16));
		if (entry != null) {
			unpublished.setValue(entry.isUnpublished());
		}

		firstEditionCB = new CheckBox();
		HorizontalLayoutContainer firstEditionHLC = new HorizontalLayoutContainer();
		firstEditionHLC.add(firstEditionCB, new HorizontalLayoutData(1.0, 1.0));
		firstEditionCB.setValue(true);
		FramedPanel firstEditionFP = new FramedPanel();
		firstEditionFP.setHeading("FirstEdition");
		firstEditionFP.add(firstEditionHLC, new HorizontalLayoutData(1.0, 1.0));
		thirdTabVLC.add(firstEditionFP, new VerticalLayoutData(1.0, 1.0 / 16));

		if (entry != null) {
			firstEditionCB.setValue(entry.isFirstEdition());
		}

		firstEditionComboBox = new ComboBox<AnnotatedBiblographyEntry>(annotatedBiblographyEntryLS,
				annotatedBiblographyEntryProps.titleEN(), new AbstractSafeHtmlRenderer<AnnotatedBiblographyEntry>() {

					@Override
					public SafeHtml render(AnnotatedBiblographyEntry item) {
						final AnnotatedBiblographyEntryViewTemplates pvTemplates = GWT.create(AnnotatedBiblographyEntryViewTemplates.class);
						return pvTemplates.AnnotatedBiblographyEntry(item.getTitleEN());
					}
				});

		if (entry != null) {
//			firstEditionComboBox.setValue(entry.getFirstEditionEntry());
		}

		ValueChangeHandler<Boolean> checkBoxHandler = new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (event.getValue() == false) {
					framefirstedition = new FramedPanel();
					framefirstedition.setHeading("Choose First Edition");
					framefirstedition.add(firstEditionComboBox);
					thirdTabVLC.add(framefirstedition);
				} else {
					thirdTabVLC.remove(framefirstedition);
				}

			}
		};

		firstEditionCB.addValueChangeHandler(checkBoxHandler);
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

interface AuthorProperties extends PropertyAccess<AuthorEntry> {
	ModelKeyProvider<AuthorEntry> authorID();
	ValueProvider<AuthorEntry, String> name();
}
