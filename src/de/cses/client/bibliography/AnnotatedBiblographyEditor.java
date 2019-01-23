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
import java.util.List;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.bibliography.BibDocumentUploader.BibDocumentUploadListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.BibKeywordEntry;
import de.cses.shared.PublicationTypeEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	private AnnotatedBibliographyEntry bibEntry;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ListStore<AnnotatedBibliographyEntry> firstEditionBiblographyEntryLS;

	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> editorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;

	private ListStore<BibKeywordEntry> bibKeywordsStore;
	private ListStore<BibKeywordEntry> selectedBibKeywordsStore;
	private BibKeywordProperties bibKeywordProps;

	private AnnotatedBiblographyEntryProperties annotatedBiblographyEntryProps;
	private AuthorProperties authorProps;

	private FramedPanel mainFP = null; // das oberste Framed Panel als Rahmen

	private TabPanel tabpanel;
	private StoreFilterField<AuthorEntry> authorListFilterField;
	private StoreFilterField<AuthorEntry> editorListFilterField;
	private StoreFilterField<BibKeywordEntry> bibKeywordFilterField;
	private DocumentLinkTemplate documentLinkTemplate;
	private DualListField<AuthorEntry, String> authorSelection;
	private DualListField<BibKeywordEntry, String> bibKeywordSelectionDLF;
	private ComboBox<AnnotatedBibliographyEntry> firstEditionComboBox;
	private DualListField<AuthorEntry, String> editorSelection;
	private TextField bibtexKeyTF;

//	interface PublisherViewTemplates extends XTemplates {
//		@XTemplate("<div>{name}</div>")
//		SafeHtml publisher(String name);
//	}
	
	interface AnnotatedBiblographyEntryViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml label(String name);
	}

	interface PublicationTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml publicationType(String name);
	}

//	interface PublisherProperties extends PropertyAccess<PublisherEntry> {
//		ModelKeyProvider<PublisherEntry> publisherID();
//
//		LabelProvider<PublisherEntry> label();
//	}

	interface AnnotatedBiblographyEntryProperties extends PropertyAccess<AnnotatedBibliographyEntry> {
		ModelKeyProvider<AnnotatedBibliographyEntry> annotatedBiblographyID();
		ValueProvider<AnnotatedBibliographyEntry, String> titleEN();
		LabelProvider<AnnotatedBibliographyEntry> label();
	}

	interface AuthorProperties extends PropertyAccess<AuthorEntry> {
		ModelKeyProvider<AuthorEntry> authorID();
		ValueProvider<AuthorEntry, String> name();
	}
	
	interface BibKeywordProperties extends PropertyAccess<BibKeywordEntry> {
		ModelKeyProvider<BibKeywordEntry> bibKeywordID();
		ValueProvider<BibKeywordEntry, String> bibKeyword();
	}

	interface DocumentLinkTemplate extends XTemplates {
		@XTemplate("<a target=\"_blank\" href=\"{documentUri}\" rel=\"noopener\">click here to open {documentDescription}</a>")
		SafeHtml documentLink(SafeUri documentUri, String documentDescription);
	}

	public AnnotatedBiblographyEditor(AnnotatedBibliographyEntry entry) {
		this.bibEntry = entry;
		documentLinkTemplate = GWT.create(DocumentLinkTemplate.class);
	}

	@Override
	public Widget asWidget() {
		if (mainFP == null) {
			init();
			createForm();
			// TODO we need to wait until all database access is finished
		}
		return mainFP;
	}

	public synchronized void save(boolean close) {

		if (authorListFilterField != null) {
			authorListFilterField.clear();
			authorListFilterField.validate();
		}
		if (editorListFilterField != null) {
			editorListFilterField.clear();
			editorListFilterField.validate();
		}
		if (bibKeywordFilterField != null) {
			bibKeywordFilterField.clear();
			bibKeywordFilterField.validate();
		}
		
		if (selectedAuthorListStore.size() < 1 && selectedEditorListStore.size() < 1) {
			Util.showWarning("No author of editor selected", "Please select either an author or an editor before saving!");
			return;
		}

		ArrayList<AuthorEntry> selectedAuthorsList = new ArrayList<AuthorEntry>();
		for (AuthorEntry ae : selectedAuthorListStore.getAll()) {
			selectedAuthorsList.add(ae);
		}
		bibEntry.setAuthorList(selectedAuthorsList);

		ArrayList<AuthorEntry> selectedEditorsList = new ArrayList<AuthorEntry>();
		for (AuthorEntry ae : selectedEditorListStore.getAll()) {
			selectedEditorsList.add(ae);
		}
		bibEntry.setEditorList(selectedEditorsList);
		
		ArrayList<BibKeywordEntry> selectedBibKeywordsList = new ArrayList<BibKeywordEntry>();
		for (BibKeywordEntry bke : selectedBibKeywordsStore.getAll()) {
			selectedBibKeywordsList.add(bke);
		}
		bibEntry.setKeywordList(selectedBibKeywordsList);

		if (bibEntry.getAnnotatedBibliographyID() > 0) {
			dbService.updateAnnotatedBiblographyEntry(bibEntry, new AsyncCallback<AnnotatedBibliographyEntry>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					Util.showWarning("Error", "A problem occured while saving!");
				}

				@Override
				public void onSuccess(AnnotatedBibliographyEntry result) {
					if (result!=null) {
						bibtexKeyTF.setValue(result.getBibtexKey(), true);
						if (close) {
							closeEditor(bibEntry);
						}
					}
				}

			});
		} else {
			dbService.insertAnnotatedBiblographyEntry(bibEntry, new AsyncCallback<AnnotatedBibliographyEntry>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					Util.showWarning("Error", "A problem occured while saving!");
				}

				@Override
				public void onSuccess(AnnotatedBibliographyEntry result) {
					bibEntry.setAnnotatedBiblographyID(result.getAnnotatedBibliographyID());
					bibtexKeyTF.setValue(result.getBibtexKey(), true);
//					updateEntry(bibEntry);
					if (close) {
						closeEditor(bibEntry);
					}
				}
			});
		}

	}

	public void init() {

		authorProps = GWT.create(AuthorProperties.class);
		selectedAuthorListStore = new ListStore<AuthorEntry>(authorProps.authorID()); // the order of authors should be accorings to the order on the paper
		selectedEditorListStore = new ListStore<AuthorEntry>(authorProps.authorID()); // the order of editors should be according to the order of the publication
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		authorListStore.addSortInfo(new StoreSortInfo<AuthorEntry>(authorProps.name(), SortDir.ASC));

		editorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		editorListStore.addSortInfo(new StoreSortInfo<AuthorEntry>(authorProps.name(), SortDir.ASC));
		
		bibKeywordProps = GWT.create(BibKeywordProperties.class);
		selectedBibKeywordsStore = new ListStore<BibKeywordEntry>(bibKeywordProps.bibKeywordID());
		selectedBibKeywordsStore.addSortInfo(new StoreSortInfo<>(bibKeywordProps.bibKeyword(), SortDir.ASC));
		for (BibKeywordEntry bkw : bibEntry.getKeywordList()) {
			selectedBibKeywordsStore.add(bkw);
		}
		
		bibKeywordsStore = new ListStore<BibKeywordEntry>(bibKeywordProps.bibKeywordID());
		bibKeywordsStore.addSortInfo(new StoreSortInfo<>(bibKeywordProps.bibKeyword(), SortDir.ASC));

		annotatedBiblographyEntryProps = GWT.create(AnnotatedBiblographyEntryProperties.class);
		firstEditionBiblographyEntryLS = new ListStore<AnnotatedBibliographyEntry>(annotatedBiblographyEntryProps.annotatedBiblographyID());
		firstEditionBiblographyEntryLS.addSortInfo(new StoreSortInfo<>(annotatedBiblographyEntryProps.titleEN(), SortDir.ASC));

		dbService.getAuthors(new AsyncCallback<ArrayList<AuthorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<AuthorEntry> result) {
				for (AuthorEntry ae : result) {
					if (!ae.isInstitutionEnabled()) {
						authorListStore.add(ae);
					}
					editorListStore.add(ae);
				}
				// now we shuffle the authors to the left in the correct order
				for (AuthorEntry ae : bibEntry.getAuthorList()) {
					AuthorEntry moveEntry = authorListStore.findModel(ae);
					if (moveEntry != null) {
						authorListStore.remove(moveEntry);
						selectedAuthorListStore.add(moveEntry);
					}
				}
				for (AuthorEntry ae : bibEntry.getEditorList()) {
					AuthorEntry moveEntry = editorListStore.findModel(ae);
					if (moveEntry != null) {
						editorListStore.remove(moveEntry);
						selectedEditorListStore.add(moveEntry);
					}
				}
			}
		});

		dbService.getBibKeywords(new AsyncCallback<ArrayList<BibKeywordEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<BibKeywordEntry> result) {
				for (BibKeywordEntry bke : result) {
					if (selectedBibKeywordsStore.findModelWithKey(Integer.toString(bke.getBibKeywordID())) == null) {
						bibKeywordsStore.add(bke);
					}
				}
			}
		});

	}
	
	private void loadFirstEditionCandidates() {
		/**
		 * We're assuming that only publications from same authors can be first editions to the current publication
		 */
		if (bibEntry.getAuthorList() == null || bibEntry.getAuthorList().isEmpty()) {
			return;
		}
		dbService.getAnnotatedBibliographyFromAuthors(bibEntry.getAuthorList(), new AsyncCallback<ArrayList<AnnotatedBibliographyEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("There's been an error while loading some of the information!");
					}

					@Override
					public void onSuccess(ArrayList<AnnotatedBibliographyEntry> result) {
						firstEditionBiblographyEntryLS.clear();
						for (AnnotatedBibliographyEntry ae : result) {
							firstEditionBiblographyEntryLS.add(ae);
						}
						if (bibEntry.getFirstEditionBibID() > 0) {
							firstEditionComboBox.setValue(firstEditionBiblographyEntryLS.findModelWithKey(Integer.toString(bibEntry.getFirstEditionBibID())));
						}
					}
				});
	}

	public void createForm() {

		rebuildMainInput();

		ToolButton closeToolButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeToolButton.setToolTip(Util.createToolTip("close"));
		closeToolButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				Util.showYesNo("Exit Warning!", "Do you wish to save before exiting?", new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						save(true);
					}
				}, new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						closeEditor(null);
					}
				});
				
			}
		});

		ToolButton saveToolButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveToolButton.setToolTip(Util.createToolTip("save"));
		saveToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				save(false);
			}

		});

		mainFP = new FramedPanel();
		mainFP.setHeading("Annotated Biblography (entry last modified on " + bibEntry.getModifiedOn() + ")");
		mainFP.setSize("900px", "650px"); // here we set the size of the panel
		mainFP.add(tabpanel, new VerticalLayoutData(1.0, 1.0));
		mainFP.addTool(saveToolButton);
		mainFP.addTool(closeToolButton);

	}

	public void rebuildMainInput() {
		
		PublicationTypeEntry pubType = bibEntry.getPublicationType();

		VerticalLayoutContainer firstTabInnerLeftVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer firstTabInnerRightVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer secondTabVLC = new VerticalLayoutContainer();

		/**
		 * The publicaton title
		 */
		TextField titleEN = new TextField();
		titleEN.setValue(bibEntry.getTitleEN());
		titleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleEN(event.getValue());
			}
		});
		titleEN.addValidator(new MaxLengthValidator(256));
		
		TextField titleORG = new TextField();
		titleORG.setValue(bibEntry.getTitleORG());
		titleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleORG(event.getValue());
			}
		});
		titleORG.setAllowBlank(false); // at least the original title should be put in
		titleORG.addValidator(new MaxLengthValidator(256));
		
		TextField titleTR = new TextField();
		titleTR.setValue(bibEntry.getTitleTR());
		titleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleTR(event.getValue());
			}
		});
		titleTR.addValidator(new MaxLengthValidator(256));
		
		CheckBox originalTranslationCB = new CheckBox();
		originalTranslationCB.setBoxLabel("official");
		originalTranslationCB.setValue(bibEntry.isOfficialTitleTranslation());
		originalTranslationCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				bibEntry.setOfficialTitleTranslation(event.getValue());
			}
		});
		
		HorizontalLayoutContainer titleEnHLC = new HorizontalLayoutContainer();
		titleEnHLC.add(titleEN, new HorizontalLayoutData(.85, 1.0, new Margins(0, 10, 0, 0)));
		titleEnHLC.add(originalTranslationCB, new HorizontalLayoutData(.15, 1.0));

		VerticalLayoutContainer titleVLC = new VerticalLayoutContainer();
		titleVLC.add(new FieldLabel(titleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		titleVLC.add(new FieldLabel(titleEnHLC, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
		titleVLC.add(new FieldLabel(titleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel titleFP = new FramedPanel();
		titleFP.setHeading("Title");
		titleFP.add(titleVLC);
		firstTabInnerLeftVLC.add(titleFP, new VerticalLayoutData(1.0, 1.0 / 5));
		
		/**
		 * Subtitle
		 */
		TextField subtitleEN = new TextField();
		subtitleEN.setValue(bibEntry.getSubtitleEN());
		subtitleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setSubtitleEN(event.getValue());
			}
		});
		subtitleEN.addValidator(new MaxLengthValidator(256));
		
		TextField subtitleORG = new TextField();
		subtitleORG.setValue(bibEntry.getSubtitleORG());
		subtitleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setSubtitleORG(event.getValue());
			}
		});
		subtitleORG.addValidator(new MaxLengthValidator(256));
		
		TextField subtitleTR = new TextField();
		subtitleTR.setValue(bibEntry.getSubtitleTR());
		subtitleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setSubtitleTR(event.getValue());
			}
		});
		subtitleTR.addValidator(new MaxLengthValidator(256));

		VerticalLayoutContainer subtitleVLC = new VerticalLayoutContainer();
		subtitleVLC.add(new FieldLabel(subtitleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		subtitleVLC.add(new FieldLabel(subtitleEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
		subtitleVLC.add(new FieldLabel(subtitleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel subtitleFP = new FramedPanel();
		subtitleFP.setHeading("Subtitle");
		subtitleFP.add(subtitleVLC);
		firstTabInnerLeftVLC.add(subtitleFP, new VerticalLayoutData(1.0, 1.0 / 5));

		/**
		 * the parent title (i.e. Book Title, Journal Name, Conference Name, Proceedings Title, etc
		 */
		if (pubType.isParentTitleEnabled()) {
			TextField parentTitleEN = new TextField();
			parentTitleEN.setValue(bibEntry.getParentTitleEN());
			parentTitleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setParentTitleEN(event.getValue());
				}
			});
			parentTitleEN.addValidator(new MaxLengthValidator(256));
			
			TextField parentTitleORG = new TextField();
			parentTitleORG.setValue(bibEntry.getParentTitleORG());
			parentTitleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setParentTitleORG(event.getValue());
				}
			});
			parentTitleORG.addValidator(new MaxLengthValidator(256));
			parentTitleORG.setAllowBlank(false);
			
			TextField parentTitleTR = new TextField();
			parentTitleTR.setValue(bibEntry.getParentTitleTR());
			parentTitleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setParentTitleTR(event.getValue());
				}
			});
			parentTitleTR.addValidator(new MaxLengthValidator(256));

			VerticalLayoutContainer parentTitleVLC = new VerticalLayoutContainer();
			parentTitleVLC.add(new FieldLabel(parentTitleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			parentTitleVLC.add(new FieldLabel(parentTitleEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			parentTitleVLC.add(new FieldLabel(parentTitleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel parentTitleFP = new FramedPanel();
			parentTitleFP.setHeading(pubType.getParentTitleLabel());
			parentTitleFP.add(parentTitleVLC);
			firstTabInnerLeftVLC.add(parentTitleFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * some publication types have a addon to the title
		 */
		if (pubType.isTitleAddonEnabled()) {
			TextField titleaddonEN = new TextField();
			titleaddonEN.setValue(bibEntry.getTitleaddonEN());
			titleaddonEN.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setTitleaddonEN(event.getValue());
				}
			});
			titleaddonEN.addValidator(new MaxLengthValidator(256));
			
			TextField titleaddonORG = new TextField();
			titleaddonORG.setValue(bibEntry.getTitleaddonORG());
			titleaddonORG.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setTitleaddonORG(event.getValue());
				}
			});
			titleaddonORG.addValidator(new MaxLengthValidator(256));
			
			TextField titleaddonTR = new TextField();
			titleaddonTR.setValue(bibEntry.getTitleaddonTR());
			titleaddonTR.addValueChangeHandler(new ValueChangeHandler<String>() {
				
				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setTitleaddonTR(event.getValue());
				}
			});
			titleaddonTR.addValidator(new MaxLengthValidator(256));
			
			VerticalLayoutContainer titleAddonVLC = new VerticalLayoutContainer();
			titleAddonVLC.add(new FieldLabel(titleaddonORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			titleAddonVLC.add(new FieldLabel(titleaddonEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			titleAddonVLC.add(new FieldLabel(titleaddonTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));
			
			FramedPanel titleAddonFP = new FramedPanel();
			titleAddonFP.setHeading(pubType.getTitleAddonLabel());
			titleAddonFP.add(titleAddonVLC);
			firstTabInnerLeftVLC.add(titleAddonFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}
		
		/**
		 * university name
		 */
		if (pubType.isUniversityEnabled()) {
			TextField uniEN = new TextField();
			uniEN.setValue(bibEntry.getUniversityEN());
			uniEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityEN(event.getValue());
				}
			});
			uniEN.addValidator(new MaxLengthValidator(128));
			
			TextField uniORG = new TextField();
			uniORG.setValue(bibEntry.getUniversityORG());
			uniORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityORG(event.getValue());
				}
			});
			uniORG.addValidator(new MaxLengthValidator(128));
			
			TextField uniTR = new TextField();
			uniTR.setValue(bibEntry.getUniversityTR());
			uniTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityTR(event.getValue());
				}
			});
			uniTR.addValidator(new MaxLengthValidator(128));

			VerticalLayoutContainer universityVLC = new VerticalLayoutContainer();
			universityVLC.add(new FieldLabel(uniORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel universityFP = new FramedPanel();
			universityFP.setHeading("University");
			universityFP.add(universityVLC);
			firstTabInnerLeftVLC.add(universityFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * fields for number
		 */
		if (pubType.isNumberEnabled()) { // achtung hier muss sie bleiben
			TextField numberEN = new TextField();
			numberEN.setValue(bibEntry.getNumberEN());
			numberEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberEN(event.getValue());
				}
			});
			numberEN.addValidator(new MaxLengthValidator(32));
			
			TextField numberORG = new TextField();
			numberORG.setValue(bibEntry.getNumberORG());
			numberORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberORG(event.getValue());
				}
			});
			numberORG.addValidator(new MaxLengthValidator(32));
			
			TextField numberTR = new TextField();
			numberTR.setValue(bibEntry.getNumberTR());
			numberTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberTR(event.getValue());
				}
			});
			numberTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer numberVLC = new VerticalLayoutContainer();
			numberVLC.add(new FieldLabel(numberORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			numberVLC.add(new FieldLabel(numberEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			numberVLC.add(new FieldLabel(numberTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel numberFP = new FramedPanel();
			numberFP.setHeading("Number");
			numberFP.add(numberVLC);
			firstTabInnerRightVLC.add(numberFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * access
		 */
		if (pubType.isAccessDateEnabled()) {
			TextField accessDateEN = new TextField();
			accessDateEN.setValue(bibEntry.getAccessdateEN());
			accessDateEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateEN(event.getValue());
				}
			});
			accessDateEN.addValidator(new MaxLengthValidator(32));
			
			TextField accessDateORG = new TextField();
			accessDateORG.setValue(bibEntry.getAccessdateORG());
			accessDateORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateORG(event.getValue());
				}
			});
			accessDateORG.addValidator(new MaxLengthValidator(32));
			
			TextField accessDateTR = new TextField();
			accessDateTR.setValue(bibEntry.getAccessdateTR());
			accessDateTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateTR(event.getValue());
				}
			});
			accessDateTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer accessDateVLC = new VerticalLayoutContainer();
			accessDateVLC.add(new FieldLabel(accessDateORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessDateEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessDateTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel accessDateFP = new FramedPanel();
			accessDateFP.setHeading("Access Date");
			accessDateFP.add(accessDateVLC, new HorizontalLayoutData(1.0, 1.0));
			firstTabInnerRightVLC.add(accessDateFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * the publisher selection
		 */
		TextField publisherTextField = new TextField();
		publisherTextField.setValue(bibEntry.getPublisher());
		publisherTextField.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setPublisher(event.getValue());
				
			}
		});
		publisherTextField.addValidator(new MaxLengthValidator(256));
		FramedPanel publisherFP = new FramedPanel();
		publisherFP.setHeading("Publisher");
		publisherFP.add(publisherTextField);
		
		secondTabVLC.add(publisherFP, new VerticalLayoutData(1.0, .1));

		/**
		 * add new author
		 */
		ToolButton addAuthorTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addAuthorTB.setToolTip(Util.createToolTip("Add author", "New authors will appear in both author and editor selection."));
		addAuthorTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addAuthorDialog = new PopupPanel();
				AuthorEditor aEditor = new AuthorEditor(new AuthorEditorListener() {
					
					@Override
					public void editorCanceled() {
						addAuthorDialog.hide();
					}
					
					@Override
					public void authorSaved(AuthorEntry authorEntry) {
						if (!authorEntry.isInstitutionEnabled()) {
							authorListStore.add(authorEntry);
						}
						editorListStore.add(authorEntry);
						addAuthorDialog.hide();
					}
				});
				addAuthorDialog.add(aEditor);
				addAuthorDialog.setModal(true);
				addAuthorDialog.center();
			}
		});
		
		ToolButton infoTB = new ToolButton(ToolButton.QUESTION);
		infoTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Util.showWarning("Information", "Since authors can also be editors,\n newly added authors will\n appear in both author and editor selection.");
			}
		});
		
		ToolButton editAuthorTB = new ToolButton(new IconConfig("editButton", "editButtonOver"));
		editAuthorTB.setToolTip(Util.createToolTip("Edit author information", "Select author in left column, then click here."));
		editAuthorTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addAuthorDialog = new PopupPanel();
				AuthorEntry selectedEntry;
				if (authorSelection.getToView().getSelectionModel().getSelectedItem() != null) {
					selectedEntry = authorSelection.getToView().getSelectionModel().getSelectedItem();
				} else if (authorSelection.getFromView().getSelectionModel().getSelectedItem() != null) {
					selectedEntry = authorSelection.getFromView().getSelectionModel().getSelectedItem();
				} else {
					return;
				}
				AuthorEditor aEditor = new AuthorEditor(selectedEntry, new AuthorEditorListener() {
					
					@Override
					public void editorCanceled() {
						addAuthorDialog.hide();
					}
					
					@Override
					public void authorSaved(AuthorEntry authorEntry) {
						addAuthorDialog.hide();
						authorSelection.getFromView().refresh();
						authorSelection.getToView().refresh();
						editorSelection.getFromView().refresh();
						editorSelection.getToView().refresh();
					}
				});
				addAuthorDialog.add(aEditor);
				addAuthorDialog.setModal(true);
				addAuthorDialog.center();
			}
		});
		
		ToolButton deleteAuthorTB = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
		deleteAuthorTB.setToolTip(Util.createToolTip("Delete author", "Select author in left column, then click here. Please note that only names not already linked to publications can be deleted!"));
		deleteAuthorTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				AuthorEntry selectedEntry;
				selectedEntry = authorSelection.getFromView().getSelectionModel().getSelectedItem();
				Util.showYesNo("Delete Author", "Do you really want to delete \n" + selectedEntry.getName() + "?", new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						dbService.deleteAuthorEntry(selectedEntry, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Util.showWarning("Delete Author", "Unknown error while trying to delete " + selectedEntry.getName() + ".");
							}

							@Override
							public void onSuccess(Boolean result) {
								if (result) {
									authorSelection.getFromStore().remove(selectedEntry);
									if (editorSelection != null) {
										editorSelection.getFromStore().remove(selectedEntry);
									}
								} else {
									Util.showWarning("Delete Author", selectedEntry.getName() + " couln't be deleted.\n It's probably used already.");
								}
							}
						});
					}
				}, new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
					}
				});
			}
		});

		/**
		 * the author selection
		 */
		if (pubType.isAuthorEnabled()) {
			authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore,
					authorProps.name(), new TextCell());
			authorSelection.setMode(Mode.INSERT);
			authorSelection.setEnableDnd(true);
			authorSelection.addValidator(new Validator<List<AuthorEntry>>() {

				@Override
				public List<EditorError> validate(Editor<List<AuthorEntry>> editor, List<AuthorEntry> value) {
					List<EditorError> l = new ArrayList<EditorError>();
					if (selectedAuthorListStore.size() == 0) {
						l.add(new DefaultEditorError(editor, "please select editor(s)", value));
					}
					return l;
				}
			});

			authorListFilterField = new StoreFilterField<AuthorEntry>() {

				@Override
				protected boolean doSelect(Store<AuthorEntry> store, AuthorEntry parent, AuthorEntry item, String filter) {
					return item.getName().toLowerCase().contains(filter.toLowerCase()); 
				}
			};
			authorListFilterField.bind(authorListStore);

			bibtexKeyTF = new TextField();
			bibtexKeyTF.setEmptyText("generated automatically when saved");
			bibtexKeyTF.setAllowBlank(false);
			bibtexKeyTF.setValue(bibEntry.getBibtexKey());
			bibtexKeyTF.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setBibtexKey(event.getValue());
				}
			});
			
			HorizontalLayoutContainer filterBibTexKeyHLC = new HorizontalLayoutContainer();
			filterBibTexKeyHLC.add(new HTML("<span style='font: 12px tahoma,arial,verdana,sans-serif;'>Filter:</span>", true), new HorizontalLayoutData(.15, 1.0, new Margins(0, 0, 0, 0)));
			filterBibTexKeyHLC.add(authorListFilterField, new HorizontalLayoutData(.35, 1.0, new Margins(0, 20, 0, 0)));
			filterBibTexKeyHLC.add(new HTML("<span style='font: 12px tahoma,arial,verdana,sans-serif;'>BibTex key:</span>", true), new HorizontalLayoutData(.15, 1.0, new Margins(0, 0, 0, 20)));
			filterBibTexKeyHLC.add(bibtexKeyTF, new HorizontalLayoutData(.35, 1.0, new Margins(0, 0, 0, 0)));

			VerticalLayoutContainer authorVLC = new VerticalLayoutContainer();
			authorVLC.add(authorSelection, new VerticalLayoutData(1.0, .85));
			authorVLC.add(filterBibTexKeyHLC, new VerticalLayoutData(1.0, .15, new Margins(10, 0, 0, 0)));

			FramedPanel authorFP = new FramedPanel();
			authorFP.setHeading("Author");
			authorFP.add(authorVLC);
			authorFP.addTool(addAuthorTB);
			authorFP.addTool(deleteAuthorTB);
			authorFP.addTool(editAuthorTB);
			secondTabVLC.add(authorFP, new VerticalLayoutData(1.0, .45));
		}

		/**
		 * the editor selection
		 */
		if (pubType.isEditorEnabled()) {
			editorSelection = new DualListField<AuthorEntry, String>(editorListStore, selectedEditorListStore,
					authorProps.name(), new TextCell());
			editorSelection.setMode(Mode.INSERT);
			editorSelection.setEnableDnd(true);
			VerticalLayoutContainer editorVLC = new VerticalLayoutContainer();
			editorVLC.add(editorSelection, new VerticalLayoutData(1.0, .85));
			editorListFilterField = new StoreFilterField<AuthorEntry>() {

				@Override
				protected boolean doSelect(Store<AuthorEntry> store, AuthorEntry parent, AuthorEntry item, String filter) {
					return item.getName().toLowerCase().contains(filter.toLowerCase()) ? true : false; 
				}
			};
			editorListFilterField.bind(editorListStore);
			
			TextField editorTypeTF = new TextField();
			editorTypeTF.setEmptyText("e.g. edd. / ed. / transl.");
			editorTypeTF.setValue(bibEntry.getEditorType());
			editorTypeTF.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditorType(event.getValue());
				}
			});
			
			HorizontalLayoutContainer filterEditorTypeHLC = new HorizontalLayoutContainer();
			filterEditorTypeHLC.add(new HTML("<span style='font: 12px tahoma,arial,verdana,sans-serif;'>Filter:</span>", true), new HorizontalLayoutData(.15, 1.0, new Margins(0, 0, 0, 0)));
			filterEditorTypeHLC.add(editorListFilterField, new HorizontalLayoutData(.35, 1.0, new Margins(0, 20, 0, 0)));
			filterEditorTypeHLC.add(new HTML("<span style='font: 12px tahoma,arial,verdana,sans-serif;'>Editor type:</span>", true), new HorizontalLayoutData(.15, 1.0, new Margins(0, 0, 0, 20)));
			filterEditorTypeHLC.add(editorTypeTF, new HorizontalLayoutData(.35, 1.0, new Margins(0, 0, 0, 0)));
			
			editorVLC.add(filterEditorTypeHLC, new VerticalLayoutData(1.0, .15, new Margins(10, 0, 0, 0)));

			ToolButton editEditorTB = new ToolButton(new IconConfig("addButton", "addButtonOver"));
			editEditorTB.setToolTip(Util.createToolTip("Edit author", "Select editor in left column, then click here to edit information."));
			editEditorTB.addSelectHandler(new SelectHandler() {
				
				@Override
				public void onSelect(SelectEvent event) {
					PopupPanel addAuthorDialog = new PopupPanel();
					AuthorEntry selectedEntry;
					if (editorSelection.getToView().getSelectionModel().getSelectedItem() != null) {
						selectedEntry = editorSelection.getToView().getSelectionModel().getSelectedItem();
					} else if (editorSelection.getFromView().getSelectionModel().getSelectedItem() != null) {
						selectedEntry = editorSelection.getFromView().getSelectionModel().getSelectedItem();
					} else {
						return;
					}
					AuthorEditor aEditor = new AuthorEditor(selectedEntry, new AuthorEditorListener() {
						
						@Override
						public void editorCanceled() {
							addAuthorDialog.hide();
						}
						
						@Override
						public void authorSaved(AuthorEntry authorEntry) {
							addAuthorDialog.hide();
							authorSelection.getFromView().refresh();
							authorSelection.getToView().refresh();
							editorSelection.getFromView().refresh();
							editorSelection.getToView().refresh();
						}
					});
					addAuthorDialog.add(aEditor);
					addAuthorDialog.setModal(true);
					addAuthorDialog.setGlassEnabled(true);
					addAuthorDialog.center();
				}
			});

			ToolButton deleteEditorTB = new ToolButton(new IconConfig("removeButton", "removeButtonOver"));
			deleteEditorTB.setToolTip(Util.createToolTip("Delete editor", "Select editor in left column, then click here to delete. Please note that only names not already linked to publications can be deleted!"));
			deleteEditorTB.addSelectHandler(new SelectHandler() {
				
				@Override
				public void onSelect(SelectEvent event) {
					AuthorEntry selectedEntry;
					selectedEntry = editorSelection.getFromView().getSelectionModel().getSelectedItem();
					Util.showYesNo("Delete Editor", "Do you really want to delete \n" + selectedEntry.getName() + "?", new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event) {
							dbService.deleteAuthorEntry(selectedEntry, new AsyncCallback<Boolean>() {
								
								@Override
								public void onFailure(Throwable caught) {
									Util.showWarning("Delete Editor", "Unknown error while trying to delete " + selectedEntry.getName() + ".");
								}
								
								@Override
								public void onSuccess(Boolean result) {
									if (result) {
										editorSelection.getFromStore().remove(selectedEntry);
										authorSelection.getFromStore().remove(selectedEntry);
									} else {
										Util.showWarning("Delete Editor", selectedEntry.getName() + " couln't be deleted.\n It's probably used already.");
									}
								}
							});
						}
					}, new SelectHandler() {
						
						@Override
						public void onSelect(SelectEvent event) {
						}
					});
				}
			});		
			
			FramedPanel editorFP = new FramedPanel();
			editorFP.setHeading("Editor");
			editorFP.add(editorVLC);
			if (pubType.isAuthorEnabled()) {
				editorFP.addTool(infoTB);
			} else {
				editorFP.addTool(addAuthorTB);
			}
			editorFP.addTool(deleteEditorTB);
			editorFP.addTool(editEditorTB);
			secondTabVLC.add(editorFP, new VerticalLayoutData(1.0, .45));
		}
		

		/**
		 * Keywords
		 */
		bibKeywordSelectionDLF = new DualListField<>(bibKeywordsStore, selectedBibKeywordsStore, bibKeywordProps.bibKeyword(), new TextCell());
		bibKeywordSelectionDLF.getUpButton().removeFromParent();
		bibKeywordSelectionDLF.getDownButton().removeFromParent();
		bibKeywordSelectionDLF.setMode(Mode.INSERT);
		bibKeywordSelectionDLF.setEnableDnd(true);
		VerticalLayoutContainer bibKeywordVLC = new VerticalLayoutContainer();
		bibKeywordVLC.add(bibKeywordSelectionDLF, new VerticalLayoutData(1.0, .85));
		bibKeywordFilterField = new StoreFilterField<BibKeywordEntry>() {
			
			@Override
			protected boolean doSelect(Store<BibKeywordEntry> store, BibKeywordEntry parent, BibKeywordEntry item, String filter) {
				return item.getBibKeyword().toLowerCase().contains(filter.toLowerCase()) ? true : false;
			}
			
		};
		bibKeywordFilterField.bind(bibKeywordsStore);
		bibKeywordVLC.add(bibKeywordFilterField, new VerticalLayoutData(1.0, .15, new Margins(5, 10, 0, 10)));
		
		ToolButton addKeywordToolButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addKeywordToolButton.setToolTip(Util.createToolTip("add keyword"));
		addKeywordToolButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addKeywordDialog = new PopupPanel();
				FramedPanel addKeywordFP = new FramedPanel();
				addKeywordFP.setHeading("Add Keyword");
				TextField keywordField = new TextField();
				keywordField.setAllowBlank(false);
				keywordField.addValidator(new MaxLengthValidator(32));
				keywordField.setValue("");
				keywordField.setWidth(200);
				addKeywordFP.add(keywordField);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (keywordField.validate()) {
							BibKeywordEntry bkEntry = new BibKeywordEntry();
							bkEntry.setBibKeyword(keywordField.getValue());
							dbService.insertBibKeyword(bkEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									caught.printStackTrace();
									Util.showWarning("Error", "The new keyword could not be saved!");
								}

								@Override
								public void onSuccess(Integer result) {
									if (result > 0) {
										bkEntry.setBibKeywordID(result);
										selectedBibKeywordsStore.add(bkEntry);
									}
								}
							});
							addKeywordDialog.hide();
						}
					}
				});
				addKeywordFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addKeywordDialog.hide();
					}
				});
				addKeywordFP.addButton(cancelButton);
				addKeywordDialog.add(addKeywordFP);
				addKeywordDialog.setModal(true);
				addKeywordDialog.center();
			}
		});
		
		FramedPanel bibKeywordFP = new FramedPanel();
		bibKeywordFP.setHeading("Keyword selection");
		bibKeywordFP.add(bibKeywordVLC);
		bibKeywordFP.addTool(addKeywordToolButton);

		/**
		 * series
		 */
		if (pubType.isSeriesEnabled()) { // hier muss sie bleiben
			TextField seriesEN = new TextField();
			seriesEN.setValue(bibEntry.getSeriesEN());
			seriesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesEN(event.getValue());
				}
			});
			seriesEN.addValidator(new MaxLengthValidator(256));
			
			TextField seriesORG = new TextField();
			seriesORG.setValue(bibEntry.getSeriesORG());
			seriesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesORG(event.getValue());
				}
			});
			seriesORG.addValidator(new MaxLengthValidator(256));
			
			TextField seriesTR = new TextField();
			seriesTR.setValue(bibEntry.getSeriesTR());
			seriesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesTR(event.getValue());
				}
			});
			seriesTR.addValidator(new MaxLengthValidator(256));

			VerticalLayoutContainer seriesVLC = new VerticalLayoutContainer();
			seriesVLC.add(new FieldLabel(seriesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel seriesFP = new FramedPanel();
			seriesFP.setHeading("Series");
			seriesFP.add(seriesVLC);
			firstTabInnerLeftVLC.add(seriesFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}
		
		/**
		 * edition
		 */
		if (pubType.isEditionEnabled()) {
			TextField editionEN = new TextField();
			editionEN.setValue(bibEntry.getEditionEN());
			editionEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionEN(event.getValue());
				}
			});
			editionEN.addValidator(new MaxLengthValidator(32));
			
			TextField editionORG = new TextField();
			editionORG.setValue(bibEntry.getEditionORG());
			editionORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionORG(event.getValue());
				}
			});
			editionORG.addValidator(new MaxLengthValidator(32));
			
			TextField editionTR = new TextField();
			editionTR.setValue(bibEntry.getEditionTR());
			editionTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionTR(event.getValue());
				}
			});
			editionTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer editionVLC = new VerticalLayoutContainer();
			editionVLC.add(new FieldLabel(editionORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			editionVLC.add(new FieldLabel(editionEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			editionVLC.add(new FieldLabel(editionTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel editionFP = new FramedPanel();
			editionFP.setHeading("Edition");
			editionFP.add(editionVLC);
			firstTabInnerRightVLC.add(editionFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * volume 
		 */
		if (pubType.isVolumeEnabled()) {
			TextField volumeEN = new TextField();
			volumeEN.setValue(bibEntry.getVolumeEN());
			volumeEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeEN(event.getValue());
				}
			});
			volumeEN.addValidator(new MaxLengthValidator(32));
			
			TextField volumeORG = new TextField();
			volumeORG.setValue(bibEntry.getVolumeORG());
			volumeORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeORG(event.getValue());
				}
			});
			volumeORG.addValidator(new MaxLengthValidator(32));
			
			TextField volumeTR = new TextField();
			volumeTR.setValue(bibEntry.getVolumeTR());
			volumeTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeTR(event.getValue());
				}
			});
			volumeTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer volumeVLC = new VerticalLayoutContainer();
			volumeVLC.add(new FieldLabel(volumeORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel volumeFP = new FramedPanel();
			volumeFP.setHeading("Volume");
			volumeFP.add(volumeVLC);
			firstTabInnerRightVLC.add(volumeFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 *  
		 */
		if (pubType.isIssueEnabled()) {
			TextField issueEN = new TextField();
			issueEN.setValue(bibEntry.getIssueEN());
			issueEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueEN(event.getValue());
				}
			});
			issueEN.addValidator(new MaxLengthValidator(32));
			
			TextField issueORG = new TextField();
			issueORG.setValue(bibEntry.getIssueORG());
			issueORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueORG(event.getValue());
				}
			});
			issueORG.addValidator(new MaxLengthValidator(32));
			
			TextField issueTR = new TextField();
			issueTR.setValue(bibEntry.getIssueTR());
			issueTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueTR(event.getValue());
				}
			});
			issueTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer volumeVLC = new VerticalLayoutContainer();
			volumeVLC.add(new FieldLabel(issueORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(issueEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(issueTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel volumeFP = new FramedPanel();
			volumeFP.setHeading("Issue");
			volumeFP.add(volumeVLC);
			firstTabInnerRightVLC.add(volumeFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * year of publication
		 */
		NumberField<Integer> yearEN = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		DateWrapper dw = new DateWrapper(); // we always want to use the current year as max year
		if (bibEntry.getYearEN() > 0) {
			yearEN.setValue(bibEntry.getYearEN());
		}
		yearEN.addValidator(new MaxNumberValidator<Integer>(dw.getFullYear()));
		yearEN.setAllowNegative(false);
		yearEN.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				bibEntry.setYearEN(event.getValue());
			}
		});
		
		TextField yearORG = new TextField();
		yearORG.setValue(bibEntry.getYearORG());
		yearORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setYearORG(event.getValue());
			}
		});
		yearORG.addValidator(new MaxLengthValidator(32));
		
		TextField yearTR = new TextField();
		yearTR.setValue(bibEntry.getYearTR());
		yearTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setYearTR(event.getValue());
			}
		});
		yearTR.addValidator(new MaxLengthValidator(32));

		VerticalLayoutContainer yearVLC = new VerticalLayoutContainer();
		yearVLC.add(new FieldLabel(yearORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel yearFP = new FramedPanel();
		yearFP.setHeading("Year");
		yearFP.add(yearVLC);
		firstTabInnerRightVLC.add(yearFP, new VerticalLayoutData(1.0, 1.0 / 5));

		if (pubType.isMonthEnabled()) { // bleiben
			TextField monthEN = new TextField();
			monthEN.setValue(bibEntry.getMonthEN());
			monthEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthEN(event.getValue());
				}
			});
			monthEN.addValidator(new MaxLengthValidator(32));
			
			TextField monthORG = new TextField();
			monthORG.setValue(bibEntry.getMonthORG());
			monthORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthORG(event.getValue());
				}
			});
			monthORG.addValidator(new MaxLengthValidator(32));
			
			TextField monthTR = new TextField();
			monthTR.setValue(bibEntry.getMonthTR());
			monthTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthTR(event.getValue());
				}
			});
			monthTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer monthVLC = new VerticalLayoutContainer();
			monthVLC.add(new FieldLabel(monthORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			monthVLC.add(new FieldLabel(monthEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			monthVLC.add(new FieldLabel(monthTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel monthFP = new FramedPanel();
			monthFP.setHeading("Month");
			monthFP.add(monthVLC);
			firstTabInnerRightVLC.add(monthFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * pages
		 */
		if (pubType.isPagesEnabled()) {
			TextField pagesEN = new TextField();
			pagesEN.setValue(bibEntry.getPagesEN());
			pagesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesEN(event.getValue());
				}
			});
			pagesEN.addValidator(new MaxLengthValidator(32));
			
			TextField pagesORG = new TextField();
			pagesORG.setEmptyText("e.g. pp. xx - yy / p. xx");
			pagesORG.setValue(bibEntry.getPagesORG());
			pagesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesORG(event.getValue());
				}
			});
			pagesORG.addValidator(new MaxLengthValidator(32));
			
			TextField pagesTR = new TextField();
			pagesTR.setValue(bibEntry.getPagesTR());
			pagesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesTR(event.getValue());
				}
			});
			pagesTR.addValidator(new MaxLengthValidator(32));

			VerticalLayoutContainer pagesVLC = new VerticalLayoutContainer();
			pagesVLC.add(new FieldLabel(pagesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			pagesVLC.add(new FieldLabel(pagesEN, "English Transl."), new VerticalLayoutData(1.0, 1.0 / 3));
			pagesVLC.add(new FieldLabel(pagesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel pagesFP = new FramedPanel();
			pagesFP.setHeading("Pages");
			pagesFP.add(pagesVLC);
			firstTabInnerRightVLC.add(pagesFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}
		
		/**
		 * thesis type
		 */
		if (pubType.isThesisTypeEnabled()) {
			SimpleComboBox<String> thesisTypeCB = new SimpleComboBox<String>(new LabelProvider<String>() {

				@Override
				public String getLabel(String item) {
					return item;
				}
			});
			thesisTypeCB.add("Master");
			thesisTypeCB.add("PhD");
			thesisTypeCB.add("other");
			thesisTypeCB.setEditable(false);
			thesisTypeCB.setTypeAhead(false);
			thesisTypeCB.setTriggerAction(TriggerAction.ALL);
			thesisTypeCB.setValue(bibEntry.getThesisType());
			thesisTypeCB.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setThesisType(event.getValue());
				}
			});
			FramedPanel thesisTypeFP = new FramedPanel();
			thesisTypeFP.setHeading("Degree");
			thesisTypeFP.add(thesisTypeCB);
			firstTabInnerRightVLC.add(thesisTypeFP, new VerticalLayoutData(1.0, 1.0 / 10));
		}

		/**
		 * comments
		 */
		TextArea commentsTA = new TextArea();
		FramedPanel commentsFP = new FramedPanel();
		commentsFP.setHeading("Comments");
		commentsFP.add(commentsTA);
		commentsTA.setValue(bibEntry.getComments());
		commentsTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setComments(event.getValue());
			}
		});

		/**
		 * notes
		 */
		TextArea notesTA = new TextArea();
		FramedPanel notesFP = new FramedPanel();
		notesFP.setHeading("Notes");
		notesFP.add(notesTA);
		notesTA.setValue(bibEntry.getNotes());
		notesTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setNotes(event.getValue());
			}
		});

		/**
		 * notes
		 */
		TextArea abstractTextTA = new TextArea();
		FramedPanel abstractTextFP = new FramedPanel();
		abstractTextFP.setHeading("Abstract");
		abstractTextFP.add(abstractTextTA);
		abstractTextTA.setValue(bibEntry.getAbstractText());
		abstractTextTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setAbstractText(event.getValue());
			}
		});

		/**
		 * URL
		 */
		TextField urlTF = new TextField();
		HorizontalLayoutContainer urlHLC = new HorizontalLayoutContainer();
		urlHLC.add(urlTF, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel urlFP = new FramedPanel();
		urlFP.setHeading("URL");
		urlFP.add(urlHLC, new HorizontalLayoutData(1.0, 1.0));
		urlTF.setValue(bibEntry.getUrl());
		urlTF.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "Please enter valid URL"));
		urlTF.addValidator(new MaxLengthValidator(256));
		urlTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (urlTF.validate()) {
					bibEntry.setUrl(event.getValue());
				}
			}
		});

		/**
		 * URI
		 */
		TextField uriTF = new TextField();
		FramedPanel uriFP = new FramedPanel();
		uriFP.setHeading("URI");
		uriFP.add(uriTF);
		uriTF.setValue(bibEntry.getUri());
		uriTF.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "Please enter valid URI"));
		uriTF.addValidator(new MaxLengthValidator(256));
		uriTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (uriTF.validate()) {
					bibEntry.setUri(event.getValue());
				}
			}
		});

		/**
		 * unpublished
		 */
		FramedPanel unpublishedFP = new FramedPanel();
		unpublishedFP.setHeading("Unpublished");
		CheckBox unpublishedCB = new CheckBox();
		unpublishedCB.setBoxLabel("yes");
		unpublishedCB.setValue(bibEntry.isUnpublished());
		unpublishedFP.add(unpublishedCB);
		unpublishedCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				bibEntry.setUnpublished(event.getValue());
			}
		});


		/**
		 * Acess Level
		 */
		SimpleComboBox<String> accessLevelCB = new SimpleComboBox<String>(new LabelProvider<String>() {

			@Override
			public String getLabel(String item) {
				return item;
			}
		});
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(0));
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(1));
		accessLevelCB.add(AbstractEntry.ACCESS_LEVEL_LABEL.get(2));
		accessLevelCB.setEditable(false);
		accessLevelCB.setTypeAhead(false);
		accessLevelCB.setTriggerAction(TriggerAction.ALL);
		accessLevelCB.setValue(AbstractEntry.ACCESS_LEVEL_LABEL.get(bibEntry.getAccessLevel()));
		accessLevelCB.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setAccessLevel(accessLevelCB.getSelectedIndex());
			}
		});
		FramedPanel accessLevelFP = new FramedPanel();
		accessLevelFP.setHeading("Access Level");
		accessLevelFP.add(accessLevelCB);

		/**
		 * first edition
		 */
		firstEditionComboBox = new ComboBox<AnnotatedBibliographyEntry>(firstEditionBiblographyEntryLS,
				annotatedBiblographyEntryProps.label(), new AbstractSafeHtmlRenderer<AnnotatedBibliographyEntry>() {

					@Override
					public SafeHtml render(AnnotatedBibliographyEntry item) {
						final AnnotatedBiblographyEntryViewTemplates pvTemplates = GWT.create(AnnotatedBiblographyEntryViewTemplates.class);
						return pvTemplates.label(item.getLabel());
					}
				});
		firstEditionComboBox.setEmptyText("n/a");
		firstEditionComboBox.setTypeAhead(true);
		firstEditionComboBox.setEditable(false);
		firstEditionComboBox.setTriggerAction(TriggerAction.ALL);
		firstEditionComboBox.addSelectionHandler(new SelectionHandler<AnnotatedBibliographyEntry>() {

			@Override
			public void onSelection(SelectionEvent<AnnotatedBibliographyEntry> event) {
				bibEntry.setFirstEditionBibID(event.getSelectedItem().getAnnotatedBibliographyID());
			}
		});

		CheckBox firstEditionCB = new CheckBox();
		FramedPanel firstEditionFP = new FramedPanel();
		firstEditionFP.setHeading("First Edition Link");
		HorizontalLayoutContainer firstEditionHLC = new HorizontalLayoutContainer();
		firstEditionHLC.add(firstEditionCB, new HorizontalLayoutData(.1, 1.0));
		firstEditionHLC.add(firstEditionComboBox, new HorizontalLayoutData(.9, 1.0));
		firstEditionFP.add(firstEditionHLC);
		firstEditionFP.setToolTip("Choose First Edition");
		if (bibEntry.getFirstEditionBibID() > 0) {
			firstEditionCB.setValue(true);
			firstEditionComboBox.setEnabled(true);
//			firstEditionComboBox.setValue(firstEditionBiblographyEntryLS.findModelWithKey(Integer.toString(bibEntry.getFirstEditionBibID())));
		} else {
			firstEditionCB.setValue(false);
			firstEditionComboBox.setEnabled(false);
		}
		firstEditionCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				firstEditionComboBox.setEnabled(event.getValue());
			}
		});
		firstEditionComboBox.addSelectionHandler(new SelectionHandler<AnnotatedBibliographyEntry>() {

			@Override
			public void onSelection(SelectionEvent<AnnotatedBibliographyEntry> event) {
				bibEntry.setFirstEditionBibID(event.getSelectedItem().getAnnotatedBibliographyID());
			}
		});
		ToolButton resetFirstEditionSelectionTB = new ToolButton(new IconConfig("resetButton", "resetButtonOver"));
		resetFirstEditionSelectionTB.setToolTip(Util.createToolTip("reset selection"));
		resetFirstEditionSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				firstEditionComboBox.setValue(null, true);
			}
		});
		firstEditionFP.addTool(resetFirstEditionSelectionTB);
		loadFirstEditionCandidates();
		
		/**
		 * paper upload
		 */
		FramedPanel bibDocPaperFP = new FramedPanel();
		bibDocPaperFP.setHeading("paper");
		bibDocPaperFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
				"resource?document=" + bibEntry.getUniqueID() + "-paper.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
				"paper")));
		ToolButton paperUploadButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		paperUploadButton.setToolTip(Util.createToolTip("upload paper as PDF"));
		paperUploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (bibEntry.getAnnotatedBibliographyID() == 0) {
					Window.alert("Documents cannot be uploaded\n before the new entry has been saved.");
					return;
				}
				PopupPanel bibDocUploadPanel = new PopupPanel();
				BibDocumentUploader paperUploader = new BibDocumentUploader(bibEntry.getUniqueID() + "-paper", new BibDocumentUploadListener() {

					@Override
					public void uploadCompleted(String documentFilename) {
						bibDocUploadPanel.hide();
					}

					@Override
					public void uploadCanceled() {
						bibDocUploadPanel.hide();
					}
				});
				bibDocUploadPanel.add(paperUploader);
				bibDocUploadPanel.setGlassEnabled(true);
				bibDocUploadPanel.center();
			}
		});
		bibDocPaperFP.addTool(paperUploadButton);

//		/**
//		 * summary panel
//		 */
//		FramedPanel bibDocSummaryFP = new FramedPanel();
//		bibDocSummaryFP.setHeading("summary");
//		bibDocSummaryFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
//				"resource?document=" + bibEntry.getUniqueID() + "-summary.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
//				"download summary")));
//		ToolButton summaryUploadButton = new ToolButton(ToolButton.PLUS);
//		summaryUploadButton.addSelectHandler(new SelectHandler() {
//
//			@Override
//			public void onSelect(SelectEvent event) {
//				if (bibEntry.getAnnotatedBiblographyID() == 0) {
//					Window.alert("Documents cannot be uploaded\n before the new entry has been saved.");
//					return;
//				}
//				PopupPanel bibDocUploadPanel = new PopupPanel();
//				BibDocumentUploader summaryUploader = new BibDocumentUploader(bibEntry.getUniqueID() + "-summary", new BibDocumentUploadListener() {
//
//					@Override
//					public void uploadCompleted(String documentFilename) {
//						bibDocUploadPanel.hide();
//					}
//
//					@Override
//					public void uploadCanceled() {
//						bibDocUploadPanel.hide();
//					}
//				});
//				bibDocUploadPanel.add(summaryUploader);
//				bibDocUploadPanel.setGlassEnabled(true);
//				bibDocUploadPanel.center();
//			}
//		});
//		bibDocSummaryFP.addTool(summaryUploadButton);

		/**
		 * annotations upload
		 */
		FramedPanel bibDocAnnotationFP = new FramedPanel();
		bibDocAnnotationFP.setHeading("annotation");
		bibDocAnnotationFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
				"resource?document=" + bibEntry.getUniqueID() + "-annotation.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
				"annotation")));
		ToolButton annotationUploadButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		annotationUploadButton.setToolTip(Util.createToolTip("upload annotation as PDF"));
		annotationUploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (bibEntry.getAnnotatedBibliographyID() == 0) {
					Window.alert("Documents cannot be uploaded\n before the new entry has been saved.");
					return;
				}
				PopupPanel bibDocUploadPanel = new PopupPanel();
				BibDocumentUploader annotationUploader = new BibDocumentUploader(bibEntry.getUniqueID() + "-annotation", new BibDocumentUploadListener() {

					@Override
					public void uploadCompleted(String documentFilename) {
						bibDocUploadPanel.hide();
					}

					@Override
					public void uploadCanceled() {
						bibDocUploadPanel.hide();
					}
				});
				bibDocUploadPanel.add(annotationUploader);
				bibDocUploadPanel.setGlassEnabled(true);
				bibDocUploadPanel.center();
			}
		});
		bibDocAnnotationFP.addTool(annotationUploadButton);
		
		HorizontalLayoutContainer bottonHLC = new HorizontalLayoutContainer();
		bottonHLC.add(unpublishedFP, new HorizontalLayoutData(.25, 1.0));
		bottonHLC.add(accessLevelFP, new HorizontalLayoutData(.25, 1.0));
		bottonHLC.add(bibDocPaperFP, new HorizontalLayoutData(.25, 1.0));
		bottonHLC.add(bibDocAnnotationFP, new HorizontalLayoutData(.25, 1.0));
//		documentsHLC.add(bibDocSummaryFP, new HorizontalLayoutData(1.0 / 3, 1.0));
		
		VerticalLayoutContainer notesCommentsRightVLC = new VerticalLayoutContainer();
		notesCommentsRightVLC.add(bibKeywordFP, new VerticalLayoutData(1.0, .65));
		notesCommentsRightVLC.add(notesFP, new VerticalLayoutData(1.0, .35));

		VerticalLayoutContainer notesCommentsLeftVLC = new VerticalLayoutContainer();
		notesCommentsLeftVLC.add(abstractTextFP, new VerticalLayoutData(1.0, .65));
		notesCommentsLeftVLC.add(commentsFP, new VerticalLayoutData(1.0, .35));
		
		HorizontalLayoutContainer notesCommtentsAbstractHLC = new HorizontalLayoutContainer();
		notesCommtentsAbstractHLC.add(notesCommentsLeftVLC, new HorizontalLayoutData(.5, 1.0));
		notesCommtentsAbstractHLC.add(notesCommentsRightVLC, new HorizontalLayoutData(.5, 1.0));
		
		VerticalLayoutContainer thirdTabVLC = new VerticalLayoutContainer();
		thirdTabVLC.add(notesCommtentsAbstractHLC, new VerticalLayoutData(1.0, .6));
		thirdTabVLC.add(urlFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(uriFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(firstEditionFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(bottonHLC, new VerticalLayoutData(1.0, .1));

		/**
		 * now we assemble the TabPanel
		 */
		tabpanel = new TabPanel();

		HorizontalLayoutContainer firstTabHLC = new HorizontalLayoutContainer();
		firstTabHLC.add(firstTabInnerLeftVLC, new HorizontalLayoutData(.65, 1.0));
		firstTabHLC.add(firstTabInnerRightVLC, new HorizontalLayoutData(.35, 1.0));
		
		tabpanel.add(firstTabHLC, "Basics (" + bibEntry.getPublicationType().getName() + ")");
		tabpanel.add(secondTabVLC, "Authors and Editors");
		tabpanel.add(thirdTabVLC, "Others");
		tabpanel.setTabScroll(false);
	}

}
