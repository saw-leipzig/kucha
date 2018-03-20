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

import java.sql.Date;
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
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.TabPanel;
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
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.bibliography.BibDocumentUploader.BibDocumentUploadListener;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	private AnnotatedBiblographyEntry bibEntry;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ListStore<PublisherEntry> publisherListStore;
	private ListStore<AnnotatedBiblographyEntry> firstEditionBiblographyEntryLS;

	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> editorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;

	private PublisherProperties publisherProps;
	private AnnotatedBiblographyEntryProperties annotatedBiblographyEntryProps;
	private AuthorProperties authorProps;

	private FramedPanel mainFP = null; // das oberste Framed Panel als Rahmen

	private TabPanel tabpanel;
	private StoreFilterField<AuthorEntry> authorListFilterField;
	private StoreFilterField<AuthorEntry> editorListFilterField;
	private DocumentLinkTemplate documentLinkTemplate;

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

		LabelProvider<PublisherEntry> label();
	}

	interface AnnotatedBiblographyEntryProperties extends PropertyAccess<AnnotatedBiblographyEntry> {
		ModelKeyProvider<AnnotatedBiblographyEntry> annotatedBiblographyID();

		ValueProvider<AnnotatedBiblographyEntry, String> titleEN();

		LabelProvider<AnnotatedBiblographyEntry> label();
	}

	interface AuthorProperties extends PropertyAccess<AuthorEntry> {
		ModelKeyProvider<AuthorEntry> authorID();

		ValueProvider<AuthorEntry, String> name();
	}

	interface DocumentLinkTemplate extends XTemplates {
		@XTemplate("<a target=\"_blank\" href=\"{documentUri}\" rel=\"noopener\">click here to open {documentDescription}</a>")
		SafeHtml documentLink(SafeUri documentUri, String documentDescription);
	}

	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry) {
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

	public void save(boolean close) {

		authorListFilterField.clear();
		authorListFilterField.validate();
		editorListFilterField.clear();
		editorListFilterField.validate();

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

		if (bibEntry.getAnnotatedBiblographyID() > 0) {
			dbService.updateAnnotatedBiblographyEntry(bibEntry, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					Window.alert("Error while saving!");
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result) {
						updateEntry(bibEntry);
						if (close) {
							closeEditor();
						}
					}
				}

			});
		} else {
			dbService.insertAnnotatedBiblographyEntry(bibEntry, new AsyncCallback<Integer>() {

				@Override
				public void onFailure(Throwable caught) {
					caught.printStackTrace();
					Window.alert("Error while saving!");
				}

				@Override
				public void onSuccess(Integer result) {
					bibEntry.setAnnotatedBiblographyID(result);
					updateEntry(bibEntry);
					if (close) {
						closeEditor();
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
		authorListStore.addSortInfo(new StoreSortInfo<AuthorEntry>(new ValueProvider<AuthorEntry, String>() {

			@Override
			public String getValue(AuthorEntry object) {
				return object.getName();
			}

			@Override
			public void setValue(AuthorEntry object, String value) {
			}

			@Override
			public String getPath() {
				return "name";
			}
		}, SortDir.ASC));

		editorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		editorListStore.addSortInfo(new StoreSortInfo<AuthorEntry>(new ValueProvider<AuthorEntry, String>() {

			@Override
			public String getValue(AuthorEntry object) {
				return object.getName();
			}

			@Override
			public void setValue(AuthorEntry object, String value) {
			}

			@Override
			public String getPath() {
				return "name";
			}
		}, SortDir.ASC));

		annotatedBiblographyEntryProps = GWT.create(AnnotatedBiblographyEntryProperties.class);
		firstEditionBiblographyEntryLS = new ListStore<AnnotatedBiblographyEntry>(annotatedBiblographyEntryProps.annotatedBiblographyID());
		firstEditionBiblographyEntryLS.addSortInfo(new StoreSortInfo<>(annotatedBiblographyEntryProps.titleEN(), SortDir.ASC));

		publisherProps = GWT.create(PublisherProperties.class);
		publisherListStore = new ListStore<PublisherEntry>(publisherProps.publisherID());
		publisherListStore.addSortInfo(new StoreSortInfo<PublisherEntry>(new ValueProvider<PublisherEntry, String>() {

			@Override
			public String getValue(PublisherEntry object) {
				return object.getLabel();
			}

			@Override
			public void setValue(PublisherEntry object, String value) {
			}

			@Override
			public String getPath() {
				return "label";
			}
		}, SortDir.ASC));

		dbService.getAuthors(new AsyncCallback<ArrayList<AuthorEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(ArrayList<AuthorEntry> result) {
				for (AuthorEntry ae : result) {
					authorListStore.add(ae);
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

		dbService.getPublishers(new AsyncCallback<ArrayList<PublisherEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
				Window.alert("Error while loading publisher list!");
			}

			@Override
			public void onSuccess(ArrayList<PublisherEntry> result) {
				for (PublisherEntry pe : result) {
					publisherListStore.add(pe);
				}
			}
		});

		/**
		 * We're assuming that only publications of the same type can be first editions to the current publication
		 */
		dbService.getAnnotatedBibliography(new AsyncCallback<ArrayList<AnnotatedBiblographyEntry>>() {

					@Override
					public void onFailure(Throwable caught) {
						Window.alert("There's been an error while loading some of the information!");
					}

					@Override
					public void onSuccess(ArrayList<AnnotatedBiblographyEntry> result) {
						for (AnnotatedBiblographyEntry ae : result) {
							firstEditionBiblographyEntryLS.add(ae);
						}
					}
				});
	}

	public void createForm() {

		rebuildMainInput();

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
						save(true);
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
				save(false);
			}

		});

		mainFP = new FramedPanel();
		mainFP.setHeading("Annotated Biblography");
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
		titleEN.setText(bibEntry.getTitleEN());
		titleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleEN(event.getValue());
			}
		});
		TextField titleORG = new TextField();
		titleORG.setText(bibEntry.getTitleORG());
		titleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleORG(event.getValue());
			}
		});
		TextField titleTR = new TextField();
		titleTR.setText(bibEntry.getTitleTR());
		titleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleTR(event.getValue());
			}
		});

		VerticalLayoutContainer titelVLC = new VerticalLayoutContainer();
		titelVLC.add(new FieldLabel(titleEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		titelVLC.add(new FieldLabel(titleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		titelVLC.add(new FieldLabel(titleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel titleFP = new FramedPanel();
		titleFP.setHeading("Titel");
		titleFP.add(titelVLC);
		firstTabInnerLeftVLC.add(titleFP, new VerticalLayoutData(1.0, 1.0 / 5));

		/**
		 * the title of the proceedings
		 */
		if (pubType.isProceedingsTitleEnabled()) {
			TextField procEN = new TextField();
			procEN.setText(bibEntry.getProcTitleEN());
			procEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setProcTitleEN(event.getValue());
				}
			});
			TextField procORG = new TextField();
			procORG.setText(bibEntry.getProcTitleORG());
			procORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setProcTitleORG(event.getValue());
				}
			});
			TextField procTR = new TextField();
			procTR.setText(bibEntry.getProcTitleTR());
			procTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setProcTitleTR(event.getValue());
				}
			});

			VerticalLayoutContainer proceedingsVLC = new VerticalLayoutContainer();
			proceedingsVLC.add(new FieldLabel(procEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			proceedingsVLC.add(new FieldLabel(procORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			proceedingsVLC.add(new FieldLabel(procTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel proceedingsFP = new FramedPanel();
			proceedingsFP.setHeading("Proceedings Title");
			proceedingsFP.add(proceedingsVLC);
			firstTabInnerLeftVLC.add(proceedingsFP, new VerticalLayoutData(1.0, 1.0 / 5));

		}

//		/**
//		 * the chapter tile
//		 */
//		if (pubType.isChapterTitleEnabled()) {
//			TextField chapterTitleEN = new TextField();
//			chapterTitleEN.setText(bibEntry.getChapTitleEN());
//			chapterTitleEN.addValueChangeHandler(new ValueChangeHandler<String>() {
//
//				@Override
//				public void onValueChange(ValueChangeEvent<String> event) {
//					bibEntry.setChapTitleEN(event.getValue());
//				}
//			});
//			TextField chapterTitleORG = new TextField();
//			chapterTitleORG.setText(bibEntry.getChapTitleORG());
//			chapterTitleORG.addValueChangeHandler(new ValueChangeHandler<String>() {
//
//				@Override
//				public void onValueChange(ValueChangeEvent<String> event) {
//					bibEntry.setChapTitleORG(event.getValue());
//				}
//			});
//			TextField chapterTitleTR = new TextField();
//			chapterTitleTR.setText(bibEntry.getChapTitleTR());
//			chapterTitleTR.addValueChangeHandler(new ValueChangeHandler<String>() {
//
//				@Override
//				public void onValueChange(ValueChangeEvent<String> event) {
//					bibEntry.setChapTitleTR(event.getValue());
//				}
//			});
//
//			VerticalLayoutContainer chapterTitleVLC = new VerticalLayoutContainer();
//			chapterTitleVLC.add(new FieldLabel(chapterTitleEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
//			chapterTitleVLC.add(new FieldLabel(chapterTitleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
//			chapterTitleVLC.add(new FieldLabel(chapterTitleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));
//
//			FramedPanel chapterFP = new FramedPanel();
//			chapterFP.setHeading("Chapter Title");
//			chapterFP.add(chapterTitleVLC);
//			firstTabInnerLeftVLC.add(chapterFP, new VerticalLayoutData(1.0, 1.0 / 5));
//		}

		/**
		 * the book title
		 */
		if (pubType.isBookTitleEnabled()) {
			TextField booktitelEN = new TextField();
			booktitelEN.setText(bibEntry.getBookTitleEN());
			booktitelEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setBookTitleEN(event.getValue());
				}
			});
			TextField booktitelORG = new TextField();
			booktitelORG.setText(bibEntry.getBookTitleORG());
			booktitelORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setBookTitleORG(event.getValue());
				}
			});
			TextField booktitelTR = new TextField();
			booktitelTR.setText(bibEntry.getBookTitleTR());
			booktitelTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setBookTitleTR(event.getValue());
				}
			});

			VerticalLayoutContainer bookTitleVLC = new VerticalLayoutContainer();
			bookTitleVLC.add(new FieldLabel(booktitelEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			bookTitleVLC.add(new FieldLabel(booktitelORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			bookTitleVLC.add(new FieldLabel(booktitelTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel bookTitleFP = new FramedPanel();
			bookTitleFP.setHeading("Booktitle");
			bookTitleFP.add(bookTitleVLC);
			firstTabInnerLeftVLC.add(bookTitleFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * university name
		 */
		if (pubType.isUniversityEnabled()) {
			TextField uniEN = new TextField();
			uniEN.setText(bibEntry.getUniversityEN());
			uniEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityEN(event.getValue());
				}
			});
			TextField uniORG = new TextField();
			uniORG.setText(bibEntry.getUniversityORG());
			uniORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityORG(event.getValue());
				}
			});
			TextField uniTR = new TextField();
			uniTR.setText(bibEntry.getUniversityTR());
			uniTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setUniversityTR(event.getValue());
				}
			});

			VerticalLayoutContainer universityVLC = new VerticalLayoutContainer();
			universityVLC.add(new FieldLabel(uniEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			universityVLC.add(new FieldLabel(uniTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel universityFP = new FramedPanel();
			universityFP.setHeading("University");
			universityFP.add(universityVLC);
			firstTabInnerLeftVLC.add(universityFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * number fields for issue 
		 */
		if (pubType.isNumberEnabled()) { // achtung hier muss sie bleiben
			TextField numberEN = new TextField();
			numberEN.setText(bibEntry.getNumberEN());
			numberEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberEN(event.getValue());
				}
			});
			TextField numberORG = new TextField();
			numberORG.setText(bibEntry.getNumberORG());
			numberORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberORG(event.getValue());
				}
			});
			TextField numberTR = new TextField();
			numberTR.setText(bibEntry.getNumberTR());
			numberTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setNumberTR(event.getValue());
				}
			});

			VerticalLayoutContainer numberVLC = new VerticalLayoutContainer();
			numberVLC.add(new FieldLabel(numberEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			numberVLC.add(new FieldLabel(numberORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
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
			accessDateEN.setText(bibEntry.getAccessdateEN());
			accessDateEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateEN(event.getValue());
				}
			});
			TextField accessDateORG = new TextField();
			accessDateORG.setText(bibEntry.getAccessdateORG());
			accessDateORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateORG(event.getValue());
				}
			});
			TextField accessDateTR = new TextField();
			accessDateTR.setText(bibEntry.getAccessdateTR());
			accessDateTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setAccessdateTR(event.getValue());
				}
			});

			VerticalLayoutContainer accessDateVLC = new VerticalLayoutContainer();
			accessDateVLC.add(new FieldLabel(accessDateEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessDateORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessDateTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel accessDateFP = new FramedPanel();
			accessDateFP.setHeading("Access Date");
			accessDateFP.add(accessDateVLC, new HorizontalLayoutData(1.0, 1.0));
			firstTabInnerRightVLC.add(accessDateFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * some publication types have a addon to the title
		 */
		TextField titleaddonEN = new TextField();
		titleaddonEN.setText(bibEntry.getTitleaddonEN());
		titleaddonEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleaddonEN(event.getValue());
			}
		});
		TextField titleaddonORG = new TextField();
		titleaddonORG.setText(bibEntry.getTitleaddonORG());
		titleaddonORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleaddonORG(event.getValue());
			}
		});
		TextField titleaddonTR = new TextField();
		titleaddonTR.setText(bibEntry.getTitleaddonTR());
		titleaddonTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setTitleaddonTR(event.getValue());
			}
		});

		VerticalLayoutContainer titleAddonVLC = new VerticalLayoutContainer();
		titleAddonVLC.add(new FieldLabel(titleaddonEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		titleAddonVLC.add(new FieldLabel(titleaddonORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		titleAddonVLC.add(new FieldLabel(titleaddonTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel titleAddonFP = new FramedPanel();
		titleAddonFP.setHeading("Titleaddon");
		titleAddonFP.add(titleAddonVLC);
		firstTabInnerLeftVLC.add(titleAddonFP, new VerticalLayoutData(1.0, 1.0 / 5));

		/**
		 * the publisher selection
		 */
		ComboBox<PublisherEntry> publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.label(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getLabel());
					}
				});
		publisherComboBox.setEditable(false);
		publisherComboBox.setTypeAhead(true);
		publisherComboBox.setTriggerAction(TriggerAction.ALL);
		PublisherEntry publisher = bibEntry.getPublisher();
		if ((publisher != null) && (publisher.getPublisherID() > 0)) {
			publisherComboBox.setValue(bibEntry.getPublisher());
		}
		publisherComboBox.addValidator(new Validator<PublisherEntry>() {

			@Override
			public List<EditorError> validate(Editor<PublisherEntry> editor, PublisherEntry value) {
				List<EditorError> l = new ArrayList<EditorError>();
				if (value == null) {
					l.add(new DefaultEditorError(editor, "please select publisher", value));
				}
				return l;
			}
		});
		publisherComboBox.addSelectionHandler(new SelectionHandler<PublisherEntry>() {

			@Override
			public void onSelection(SelectionEvent<PublisherEntry> event) {
				bibEntry.setPublisher(event.getSelectedItem());
			}
		});
		FramedPanel publisherFP = new FramedPanel();
		publisherFP.setHeading("Publisher");
		publisherFP.add(publisherComboBox);
		
		ToolButton resetPublisherSelectionTB = new ToolButton(ToolButton.REFRESH);
		publisherFP.addTool(resetPublisherSelectionTB);
		resetPublisherSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				publisherComboBox.setValue(null, true);
			}
		});

		ToolButton addPublisherTB = new ToolButton(ToolButton.PLUS);
		publisherFP.addTool(addPublisherTB);
		addPublisherTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addPublisherDialog = new PopupPanel();
				FramedPanel addPublisherFP = new FramedPanel();
				addPublisherFP.setHeading("Add New Publisher");
//				addPublisherFP.setWidth("400px");
				TextField publisherNameField = new TextField();
				publisherNameField.addValidator(new MinLengthValidator(2));
				publisherNameField.addValidator(new MaxLengthValidator(128));
				publisherNameField.setValue("");
				TextField publisherLocationField = new TextField();
				publisherLocationField.addValidator(new MinLengthValidator(2));
				publisherLocationField.addValidator(new MaxLengthValidator(128));
				publisherLocationField.setValue("");
				VerticalLayoutContainer newPublisherVLC = new VerticalLayoutContainer();
				newPublisherVLC.add(new FieldLabel(publisherNameField, "Name"), new VerticalLayoutData(1.0, .5));
				newPublisherVLC.add(new FieldLabel(publisherLocationField, "Location"), new VerticalLayoutData(1.0, .5));
				addPublisherFP.add(newPublisherVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (publisherNameField.isValid() && publisherLocationField.isValid()) {
							PublisherEntry publisherEntry = new PublisherEntry(0, publisherNameField.getCurrentValue(),
									publisherLocationField.getCurrentValue());
							dbService.insertPublisherEntry(publisherEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									addPublisherDialog.hide();
									Window.alert("Error while saving!");
								}

								@Override
								public void onSuccess(Integer result) {
									addPublisherDialog.hide();
									publisherEntry.setPublisherID(result);
									publisherListStore.add(publisherEntry);
								}
							});
						}
					}
				});
				addPublisherFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addPublisherDialog.hide();
					}
				});
				addPublisherFP.addButton(cancelButton);
				addPublisherDialog.add(addPublisherFP);
				addPublisherDialog.setModal(true);
				addPublisherDialog.center();
			}
		});
		secondTabVLC.add(publisherFP, new VerticalLayoutData(1.0, .1));

		/**
		 * add new author
		 */
		ToolButton addAuthorTB = new ToolButton(ToolButton.PLUS);
		addAuthorTB.setToolTip("Since authors can also be editors,\n newly added authors will\n appear in both author and editor selection.");
		addAuthorTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addAuthorDialog = new PopupPanel();
				FramedPanel addAuthorFP = new FramedPanel();
				addAuthorFP.setHeading("Add New Author");
//				addAuthorFP.setWidth("400px");
				TextField authorLastNameTF = new TextField();
				authorLastNameTF.addValidator(new MinLengthValidator(2));
				authorLastNameTF.addValidator(new MaxLengthValidator(128));
				authorLastNameTF.setValue("");
				TextField authorFirstNameTF = new TextField();
				authorFirstNameTF.addValidator(new MinLengthValidator(2));
				authorFirstNameTF.addValidator(new MaxLengthValidator(128));
				authorFirstNameTF.setValue("");
				DateField authorDateOfVisitDF = new DateField();
				TextField authorAffiliation = new TextField();
				TextField authorEmailTF = new TextField();
				authorEmailTF.addValidator(new RegExValidator(Util.REGEX_EMAIL_PATTERN, "please enter valid email address"));
				TextField authorHomepageTF = new TextField();
				authorHomepageTF.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "please enter valid URL"));
				VerticalLayoutContainer newAuthorVLC = new VerticalLayoutContainer();
				newAuthorVLC.add(new FieldLabel(authorLastNameTF, "Surname"), new VerticalLayoutData(1.0, 1.0 / 6));
				newAuthorVLC.add(new FieldLabel(authorFirstNameTF, "First Name"), new VerticalLayoutData(1.0, 1.0 / 6));
				newAuthorVLC.add(new FieldLabel(authorDateOfVisitDF, "Date of visit"), new VerticalLayoutData(1.0, 1.0 / 6));
				newAuthorVLC.add(new FieldLabel(authorAffiliation, "Affiliation"), new VerticalLayoutData(1.0, 1.0 / 6));
				newAuthorVLC.add(new FieldLabel(authorEmailTF, "E-mail"), new VerticalLayoutData(1.0, 1.0 / 6));
				newAuthorVLC.add(new FieldLabel(authorHomepageTF, "Homepage"), new VerticalLayoutData(1.0, 1.0 / 6));
				addAuthorFP.add(newAuthorVLC);
				TextButton saveButton = new TextButton("save");
				saveButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						if (authorLastNameTF.validate() && authorFirstNameTF.validate() && authorEmailTF.validate() && authorHomepageTF.validate()) {
							AuthorEntry authorEntry = new AuthorEntry(0, authorLastNameTF.getCurrentValue(), authorFirstNameTF.getCurrentValue(),
									new Date(authorDateOfVisitDF.getCurrentValue().getTime()), authorAffiliation.getCurrentValue(), authorEmailTF.getCurrentValue(),
									authorHomepageTF.getCurrentValue());
							dbService.insertAuthorEntry(authorEntry, new AsyncCallback<Integer>() {

								@Override
								public void onFailure(Throwable caught) {
									addAuthorDialog.hide();
									Window.alert("Error while saving!");
								}

								@Override
								public void onSuccess(Integer result) {
									addAuthorDialog.hide();
									authorEntry.setAuthorID(result);
									authorListStore.add(authorEntry);
									editorListStore.add(authorEntry);
								}
							});
						}
					}
				});
				addAuthorFP.addButton(saveButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {

					@Override
					public void onSelect(SelectEvent event) {
						addAuthorDialog.hide();
					}
				});
				addAuthorFP.addButton(cancelButton);
				addAuthorDialog.add(addAuthorFP);
				addAuthorDialog.setModal(true);
				addAuthorDialog.center();
			}
		});
		
		ToolButton infoTB = new ToolButton(ToolButton.QUESTION);
		infoTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				Window.alert(
						"Since authors can also be editors,\n newly added authors will\n appear in both author and editor selection.");
			}
		});

		/**
		 * the author selection
		 */
		if (pubType.isAuthorEnabled()) {
			DualListField<AuthorEntry, String> authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore,
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
			VerticalLayoutContainer authorVLC = new VerticalLayoutContainer();
			authorVLC.add(authorSelection, new VerticalLayoutData(1.0, .85));
			authorListFilterField = new StoreFilterField<AuthorEntry>() {

				@Override
				protected boolean doSelect(Store<AuthorEntry> store, AuthorEntry parent, AuthorEntry item, String filter) {
					if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
						return true;
					} else {
						return false;
					}
				}
			};
			authorListFilterField.bind(authorListStore);
			authorVLC.add(new FieldLabel(authorListFilterField, "Filter"), new VerticalLayoutData(.5, .15, new Margins(10, 0, 0, 0)));
			FramedPanel authorFP = new FramedPanel();
			authorFP.setHeading("Author");
			authorFP.add(authorVLC);
			authorFP.addTool(addAuthorTB);
			secondTabVLC.add(authorFP, new VerticalLayoutData(1.0, .45));
		}

		/**
		 * the editor selection
		 */
		if (pubType.isEditorEnabled()) {
			DualListField<AuthorEntry, String> editorSelection = new DualListField<AuthorEntry, String>(editorListStore, selectedEditorListStore,
					authorProps.name(), new TextCell());
			editorSelection.setMode(Mode.INSERT);
			editorSelection.setEnableDnd(true);
			VerticalLayoutContainer editorVLC = new VerticalLayoutContainer();
			editorVLC.add(editorSelection, new VerticalLayoutData(1.0, .85));
			editorListFilterField = new StoreFilterField<AuthorEntry>() {

				@Override
				protected boolean doSelect(Store<AuthorEntry> store, AuthorEntry parent, AuthorEntry item, String filter) {
					if (item.getName().toLowerCase().contains(filter.toLowerCase())) {
						return true;
					} else {
						return false;
					}
				}
			};
			editorListFilterField.bind(editorListStore);
			editorVLC.add(new FieldLabel(editorListFilterField, "Filter"), new VerticalLayoutData(.5, .15, new Margins(10, 0, 0, 0)));
			FramedPanel editorFP = new FramedPanel();
			editorFP.setHeading("Editor");
			editorFP.add(editorVLC);
			if (pubType.isAuthorEnabled()) {
				editorFP.addTool(infoTB);
			} else {
				editorFP.addTool(addAuthorTB);
			}
			secondTabVLC.add(editorFP, new VerticalLayoutData(1.0, .45));

		}

		/**
		 * series
		 */
		if (pubType.isSeriesEnabled()) { // hier muss sie bleiben
			TextField seriesEN = new TextField();
			seriesEN.setText(bibEntry.getSeriesEN());
			seriesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesEN(event.getValue());
				}
			});
			TextField seriesORG = new TextField();
			seriesORG.setText(bibEntry.getSeriesORG());
			seriesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesORG(event.getValue());
				}
			});
			TextField seriesTR = new TextField();
			seriesTR.setText(bibEntry.getSeriesTR());
			seriesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setSeriesTR(event.getValue());
				}
			});

			VerticalLayoutContainer seriesVLC = new VerticalLayoutContainer();
			seriesVLC.add(new FieldLabel(seriesEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			seriesVLC.add(new FieldLabel(seriesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel seriesFP = new FramedPanel();
			seriesFP.setHeading("Series");
			seriesFP.add(seriesVLC);
			firstTabInnerRightVLC.add(seriesFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}
		
		/**
		 * edition
		 */
		if (pubType.isEditionEnabled()) {
			TextField editionEN = new TextField();
			editionEN.setText(bibEntry.getEditionEN());
			editionEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionEN(event.getValue());
				}
			});
			TextField editionORG = new TextField();
			editionORG.setText(bibEntry.getEditionORG());
			editionORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionORG(event.getValue());
				}
			});
			TextField editionTR = new TextField();
			editionTR.setText(bibEntry.getEditionTR());
			editionTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setEditionTR(event.getValue());
				}
			});

			VerticalLayoutContainer editionVLC = new VerticalLayoutContainer();
			editionVLC.add(new FieldLabel(editionEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			editionVLC.add(new FieldLabel(editionORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
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
			volumeEN.setText(bibEntry.getVolumeEN());
			volumeEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeEN(event.getValue());
				}
			});
			TextField volumeORG = new TextField();
			volumeORG.setText(bibEntry.getVolumeORG());
			volumeORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeORG(event.getValue());
				}
			});
			TextField volumeTR = new TextField();
			volumeTR.setText(bibEntry.getVolumeTR());
			volumeTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setVolumeTR(event.getValue());
				}
			});

			VerticalLayoutContainer volumeVLC = new VerticalLayoutContainer();
			volumeVLC.add(new FieldLabel(volumeEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(volumeTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel volumeFP = new FramedPanel();
			volumeFP.setHeading("Volume");
			volumeFP.add(volumeVLC);
			firstTabInnerRightVLC.add(volumeFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * issue 
		 */
		if (pubType.isIssueEnabled()) {
			TextField issueEN = new TextField();
			issueEN.setText(bibEntry.getVolumeEN());
			issueEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueEN(event.getValue());
				}
			});
			TextField issueORG = new TextField();
			issueORG.setText(bibEntry.getVolumeORG());
			issueORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueORG(event.getValue());
				}
			});
			TextField issueTR = new TextField();
			issueTR.setText(bibEntry.getVolumeTR());
			issueTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setIssueTR(event.getValue());
				}
			});

			VerticalLayoutContainer volumeVLC = new VerticalLayoutContainer();
			volumeVLC.add(new FieldLabel(issueEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			volumeVLC.add(new FieldLabel(issueORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
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
		yearORG.setText(bibEntry.getYearORG());
		yearORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setYearORG(event.getValue());
			}
		});
		TextField yearTR = new TextField();
		yearTR.setText(bibEntry.getYearTR());
		yearTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				bibEntry.setYearTR(event.getValue());
			}
		});

		VerticalLayoutContainer yearVLC = new VerticalLayoutContainer();
		yearVLC.add(new FieldLabel(yearEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
		yearVLC.add(new FieldLabel(yearTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

		FramedPanel yearFP = new FramedPanel();
		yearFP.setHeading("Year");
		yearFP.add(yearVLC);
		firstTabInnerRightVLC.add(yearFP, new VerticalLayoutData(1.0, 1.0 / 5));

		if (pubType.isMonthEnabled()) { // bleiben
			TextField monthEN = new TextField();
			monthEN.setText(bibEntry.getMonthEN());
			monthEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthEN(event.getValue());
				}
			});
			TextField monthORG = new TextField();
			monthORG.setText(bibEntry.getMonthORG());
			monthORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthORG(event.getValue());
				}
			});
			TextField monthTR = new TextField();
			monthTR.setText(bibEntry.getMonthTR());
			monthTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setMonthTR(event.getValue());
				}
			});

			VerticalLayoutContainer monthVLC = new VerticalLayoutContainer();
			monthVLC.add(new FieldLabel(monthEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			monthVLC.add(new FieldLabel(monthORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
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
			pagesEN.setText(bibEntry.getPagesEN());
			pagesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesEN(event.getValue());
				}
			});
			TextField pagesORG = new TextField();
			pagesORG.setText(bibEntry.getPagesORG());
			pagesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesORG(event.getValue());
				}
			});
			TextField pagesTR = new TextField();
			pagesTR.setText(bibEntry.getPagesTR());
			pagesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					bibEntry.setPagesTR(event.getValue());
				}
			});

			VerticalLayoutContainer pagesVLC = new VerticalLayoutContainer();
			pagesVLC.add(new FieldLabel(pagesEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			pagesVLC.add(new FieldLabel(pagesORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			pagesVLC.add(new FieldLabel(pagesTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel pagesFP = new FramedPanel();
			pagesFP.setHeading("Pages");
			pagesFP.add(pagesVLC);
			firstTabInnerRightVLC.add(pagesFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		/**
		 * comments
		 */
		TextArea commentsTA = new TextArea();
		FramedPanel commentsFP = new FramedPanel();
		commentsFP.setHeading("Comments");
		commentsFP.add(commentsTA);
		commentsTA.setText(bibEntry.getComments());
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
		notesTA.setText(bibEntry.getNotes());
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
		abstractTextTA.setText(bibEntry.getAbstractText());
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
		urlTF.setText(bibEntry.getUrl());
		urlTF.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "Please enter valid URL"));
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
		uriTF.setText(bibEntry.getUri());
		uriTF.addValidator(new RegExValidator(Util.REGEX_URL_PATTERN, "Please enter valid URI"));
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
		CheckBox unpublishedCB = new CheckBox();
		FramedPanel unpublishedFP = new FramedPanel();
		unpublishedFP.setHeading("Unpublished");
		unpublishedFP.add(new FieldLabel(unpublishedCB, "is unpublished"));
		unpublishedCB.setValue(bibEntry.isUnpublished());
		unpublishedCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				bibEntry.setUnpublished(event.getValue());
			}
		});

		/**
		 * open access
		 */
		CheckBox openAccessCB = new CheckBox();
		FramedPanel openAccessFP = new FramedPanel();
		openAccessFP.setHeading("Open Access");
		openAccessFP.add(new FieldLabel(openAccessCB, "is open"));
		openAccessCB.setValue(bibEntry.isOpenAccess());
		openAccessCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				bibEntry.setOpenAccess(event.getValue());
			}
		});

		/**
		 * first edition
		 */
		ComboBox<AnnotatedBiblographyEntry> firstEditionComboBox = new ComboBox<AnnotatedBiblographyEntry>(firstEditionBiblographyEntryLS,
				annotatedBiblographyEntryProps.label(), new AbstractSafeHtmlRenderer<AnnotatedBiblographyEntry>() {

					@Override
					public SafeHtml render(AnnotatedBiblographyEntry item) {
						final AnnotatedBiblographyEntryViewTemplates pvTemplates = GWT.create(AnnotatedBiblographyEntryViewTemplates.class);
						return pvTemplates.AnnotatedBiblographyEntry(item.getLabel());
					}
				});
		firstEditionComboBox.setTypeAhead(false);
		firstEditionComboBox.setEditable(false);
		firstEditionComboBox.setTriggerAction(TriggerAction.ALL);
		firstEditionComboBox.addSelectionHandler(new SelectionHandler<AnnotatedBiblographyEntry>() {

			@Override
			public void onSelection(SelectionEvent<AnnotatedBiblographyEntry> event) {
				bibEntry.setFirstEditionBibID(event.getSelectedItem().getAnnotatedBiblographyID());
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
			firstEditionComboBox.setValue(firstEditionBiblographyEntryLS.findModelWithKey(Integer.toString(bibEntry.getFirstEditionBibID())));
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
		firstEditionComboBox.addSelectionHandler(new SelectionHandler<AnnotatedBiblographyEntry>() {

			@Override
			public void onSelection(SelectionEvent<AnnotatedBiblographyEntry> event) {
				bibEntry.setFirstEditionBibID(event.getSelectedItem().getAnnotatedBiblographyID());
			}
		});
		ToolButton resetFirstEditionSelectionTB = new ToolButton(ToolButton.REFRESH);
		resetPublisherSelectionTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				firstEditionComboBox.setValue(null, true);
			}
		});
		firstEditionFP.addTool(resetFirstEditionSelectionTB);
		
		FramedPanel bibDocPaperFP = new FramedPanel();
		bibDocPaperFP.setHeading("paper");
		bibDocPaperFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
				"resource?document=" + bibEntry.getUniqueID() + "-paper.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
				"download paper")));
		ToolButton paperUploadButton = new ToolButton(ToolButton.PLUS);
		paperUploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (bibEntry.getAnnotatedBiblographyID() == 0) {
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

		FramedPanel bibDocSummaryFP = new FramedPanel();
		bibDocSummaryFP.setHeading("summary");
		bibDocSummaryFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
				"resource?document=" + bibEntry.getUniqueID() + "-summary.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
				"download summary")));
		ToolButton summaryUploadButton = new ToolButton(ToolButton.PLUS);
		summaryUploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (bibEntry.getAnnotatedBiblographyID() == 0) {
					Window.alert("Documents cannot be uploaded\n before the new entry has been saved.");
					return;
				}
				PopupPanel bibDocUploadPanel = new PopupPanel();
				BibDocumentUploader summaryUploader = new BibDocumentUploader(bibEntry.getUniqueID() + "-summary", new BibDocumentUploadListener() {

					@Override
					public void uploadCompleted(String documentFilename) {
						bibDocUploadPanel.hide();
					}

					@Override
					public void uploadCanceled() {
						bibDocUploadPanel.hide();
					}
				});
				bibDocUploadPanel.add(summaryUploader);
				bibDocUploadPanel.setGlassEnabled(true);
				bibDocUploadPanel.center();
			}
		});
		bibDocSummaryFP.addTool(summaryUploadButton);

		FramedPanel bibDocAnnotationFP = new FramedPanel();
		bibDocAnnotationFP.setHeading("annotation");
		bibDocAnnotationFP.add(new HTMLPanel(documentLinkTemplate.documentLink(UriUtils.fromString(
				"resource?document=" + bibEntry.getUniqueID() + "-annotation.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri()),
				"download annotation")));
		ToolButton annotationUploadButton = new ToolButton(ToolButton.PLUS);
		annotationUploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				if (bibEntry.getAnnotatedBiblographyID() == 0) {
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
		
		HorizontalLayoutContainer documentsHLC = new HorizontalLayoutContainer();
		documentsHLC.add(bibDocPaperFP, new HorizontalLayoutData(1.0 / 3, 1.0));
		documentsHLC.add(bibDocAnnotationFP, new HorizontalLayoutData(1.0 / 3, 1.0));
		documentsHLC.add(bibDocSummaryFP, new HorizontalLayoutData(1.0 / 3, 1.0));
		
		VerticalLayoutContainer thirdTabVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer notesCommentsVLC = new VerticalLayoutContainer();
		notesCommentsVLC.add(commentsFP, new VerticalLayoutData(1.0, .5));
		notesCommentsVLC.add(notesFP, new VerticalLayoutData(1.0, .5));
		HorizontalLayoutContainer notesCommtentsAbstractHLC = new HorizontalLayoutContainer();
		notesCommtentsAbstractHLC.add(notesCommentsVLC, new HorizontalLayoutData(.5, 1.0));
		notesCommtentsAbstractHLC.add(abstractTextFP, new HorizontalLayoutData(.5, 1.0));
		thirdTabVLC.add(notesCommtentsAbstractHLC, new VerticalLayoutData(1.0, .5));
		thirdTabVLC.add(urlFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(uriFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(unpublishedFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(firstEditionFP, new VerticalLayoutData(1.0, .1));
		thirdTabVLC.add(documentsHLC, new VerticalLayoutData(1.0, .1));

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
