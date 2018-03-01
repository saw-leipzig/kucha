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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
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
import com.sencha.gxt.widget.core.client.form.DualListField;
import com.sencha.gxt.widget.core.client.form.DualListField.Mode;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.StoreFilterField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.MaxNumberValidator;
import com.sencha.gxt.widget.core.client.form.validator.MinLengthValidator;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.ui.AbstractEditor;
import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.AuthorEntry;
import de.cses.shared.PublisherEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	private AnnotatedBiblographyEntry entry;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ListStore<PublisherEntry> publisherListStore;
	private ListStore<AnnotatedBiblographyEntry> annotatedBiblographyEntryLS;

	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> editorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;

	private PublisherProperties publisherProps;
	private AnnotatedBiblographyEntryProperties annotatedBiblographyEntryProps;
	private AuthorProperties authorProps;

	private FramedPanel framefirstedition;
	private FramedPanel mainFP = null; // das oberste Framed Panel als Rahmen

	private TabPanel tabpanel;
	private StoreFilterField<AuthorEntry> authorListFilterField;
	private StoreFilterField<AuthorEntry> editorListFilterField;

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

		LabelProvider<AnnotatedBiblographyEntry> titleEN();
	}

	interface AuthorProperties extends PropertyAccess<AuthorEntry> {
		ModelKeyProvider<AuthorEntry> authorID();

		ValueProvider<AuthorEntry, String> name();
	}

	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry) {
		this.entry = entry;
	}

	public AnnotatedBiblographyEditor() {
	}

	@Override
	public Widget asWidget() {
		if (mainFP == null) {
			init();
			createForm();
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
		entry.setAuthorList(selectedAuthorsList);

		ArrayList<AuthorEntry> selectedEditorsList = new ArrayList<AuthorEntry>();
		for (AuthorEntry ae : selectedEditorListStore.getAll()) {
			selectedEditorsList.add(ae);
		}
		entry.setEditorList(selectedEditorsList);

		dbService.insertAnnotatedBiblographyEntry(entry, new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}

			@Override
			public void onSuccess(Integer result) {
				entry.setAnnotatedBiblographyID(result);
				updateEntry(entry);
				if (close) {
					closeEditor();
				}
			}
		});

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
		annotatedBiblographyEntryLS = new ListStore<AnnotatedBiblographyEntry>(annotatedBiblographyEntryProps.annotatedBiblographyID());

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
				for (AuthorEntry pe : result) {
					authorListStore.add(pe);
					editorListStore.add(pe);
				}
			}
		});

		dbService.getPublishers(new AsyncCallback<ArrayList<PublisherEntry>>() {

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

		// backgroundoverview.add(tabpanel, new VerticalLayoutData(1.0, 1.0));

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

	public void rebuildMainInput(int publicationtype) {
		tabpanel = new TabPanel();

		HorizontalLayoutContainer firstTabHLC = new HorizontalLayoutContainer();
		VerticalLayoutContainer firstTabInnerLeftVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer firstTabInnerRightVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer secondTabVLC = new VerticalLayoutContainer();
		VerticalLayoutContainer thirdTabVLC = new VerticalLayoutContainer();

		firstTabHLC.add(firstTabInnerLeftVLC, new HorizontalLayoutData(.65, 1.0));
		firstTabHLC.add(firstTabInnerRightVLC, new HorizontalLayoutData(.35, 1.0));

		tabpanel.add(firstTabHLC, "Basics");
		tabpanel.add(secondTabVLC, "Authors and Editors");
		tabpanel.add(thirdTabVLC, "Others");
		tabpanel.setTabScroll(false);

		TextField titleEN = new TextField();
		titleEN.setText(entry.getTitleEN());
		titleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleEN(event.getValue());
			}
		});
		TextField titleORG = new TextField();
		titleORG.setText(entry.getTitleORG());
		titleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleORG(event.getValue());
			}
		});
		TextField titleTR = new TextField();
		titleTR.setText(entry.getTitleTR());
		titleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleTR(event.getValue());
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

		if (publicationtype == 6) {
			TextField procEN = new TextField();
			procEN.setText(entry.getProcTitleEN());
			procEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setProcTitleEN(event.getValue());
				}
			});
			TextField procORG = new TextField();
			procORG.setText(entry.getProcTitleORG());
			procORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setProcTitleORG(event.getValue());
				}
			});
			TextField procTR = new TextField();
			procTR.setText(entry.getProcTitleTR());
			procTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setProcTitleTR(event.getValue());
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

		if (publicationtype == 5) {
			TextField chapterTitleEN = new TextField();
			chapterTitleEN.setText(entry.getChapTitleEN());
			chapterTitleEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setChapTitleEN(event.getValue());
				}
			});
			TextField chapterTitleORG = new TextField();
			chapterTitleORG.setText(entry.getChapTitleORG());
			chapterTitleORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setChapTitleORG(event.getValue());
				}
			});
			TextField chapterTitleTR = new TextField();
			chapterTitleTR.setText(entry.getChapTitleTR());
			chapterTitleTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setChapTitleTR(event.getValue());
				}
			});

			VerticalLayoutContainer chapterTitleVLC = new VerticalLayoutContainer();
			chapterTitleVLC.add(new FieldLabel(chapterTitleEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			chapterTitleVLC.add(new FieldLabel(chapterTitleORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			chapterTitleVLC.add(new FieldLabel(chapterTitleTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel chapterFP = new FramedPanel();
			chapterFP.setHeading("Chapter Title");
			chapterFP.add(chapterTitleVLC);
			firstTabInnerLeftVLC.add(chapterFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		if (publicationtype == 1) {
			TextField booktitelEN = new TextField();
			booktitelEN.setText(entry.getBookTitleEN());
			booktitelEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setBookTitleEN(event.getValue());
				}
			});
			TextField booktitelORG = new TextField();
			booktitelORG.setText(entry.getBookTitleORG());
			booktitelORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setBookTitleORG(event.getValue());
				}
			});
			TextField booktitelTR = new TextField();
			booktitelTR.setText(entry.getBookTitleTR());
			booktitelTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setBookTitleTR(event.getValue());
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

		if (publicationtype == 3) {
			TextField uniEN = new TextField();
			uniEN.setText(entry.getUniversityEN());
			uniEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setUniversityEN(event.getValue());
				}
			});
			TextField uniORG = new TextField();
			uniORG.setText(entry.getUniversityORG());
			uniORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setUniversityORG(event.getValue());
				}
			});
			TextField uniTR = new TextField();
			uniTR.setText(entry.getUniversityTR());
			uniTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setUniversityTR(event.getValue());
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

		if (publicationtype == 8) { // achtung hier muss sie bleiben
			TextField numberEN = new TextField();
			numberEN.setText(entry.getNumberEN());
			numberEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setNumberEN(event.getValue());
				}
			});
			TextField numberORG = new TextField();
			numberORG.setText(entry.getNumberORG());
			numberORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setNumberORG(event.getValue());
				}
			});
			TextField numberTR = new TextField();
			numberTR.setText(entry.getNumberTR());
			numberTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setNumberTR(event.getValue());
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

		if (publicationtype == 7) {
			TextField accessEN = new TextField();
			accessEN.setText(entry.getAccessdateEN());
			accessEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setAccessdateEN(event.getValue());
				}
			});
			TextField accessORG = new TextField();
			accessORG.setText(entry.getAccessdateORG());
			accessORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setAccessdateORG(event.getValue());
				}
			});
			TextField accessTR = new TextField();
			accessTR.setText(entry.getAccessdateTR());
			accessTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setAccessdateTR(event.getValue());
				}
			});

			VerticalLayoutContainer accessDateVLC = new VerticalLayoutContainer();
			accessDateVLC.add(new FieldLabel(accessEN, "English"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessORG, "Original"), new VerticalLayoutData(1.0, 1.0 / 3));
			accessDateVLC.add(new FieldLabel(accessTR, "Transcription"), new VerticalLayoutData(1.0, 1.0 / 3));

			FramedPanel accessDateFP = new FramedPanel();
			accessDateFP.setHeading("Access Date");
			accessDateFP.add(accessDateVLC, new HorizontalLayoutData(1.0, 1.0));
			firstTabInnerRightVLC.add(accessDateFP, new VerticalLayoutData(1.0, 1.0 / 5));
		}

		TextField titleaddonEN = new TextField();
		titleaddonEN.setText(entry.getTitleaddonEN());
		titleaddonEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleaddonEN(event.getValue());
			}
		});
		TextField titleaddonORG = new TextField();
		titleaddonORG.setText(entry.getTitleaddonORG());
		titleaddonORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleaddonORG(event.getValue());
			}
		});
		TextField titleaddonTR = new TextField();
		titleaddonTR.setText(entry.getTitleaddonTR());
		titleaddonTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setTitleaddonTR(event.getValue());
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

		ComboBox<PublisherEntry> publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.label(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getLabel());
					}
				});
		publisherComboBox.setEditable(false);
		publisherComboBox.setTypeAhead(false);
		FramedPanel publisherFP = new FramedPanel();
		publisherFP.setHeading("Publisher");
		publisherFP.add(publisherComboBox);

		ToolButton addPublisherTB = new ToolButton(ToolButton.PLUS);
		publisherFP.addTool(addPublisherTB);
		addPublisherTB.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				PopupPanel addPublisherDialog = new PopupPanel();
				FramedPanel addPublisherFP = new FramedPanel();
				addPublisherFP.setHeading("Add New Publisher");
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
									Window.alert("An error occurred during saving!");
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

		if (entry != null) {
			// publisherComboBox.setValue(entry.getPublisher());
		}

		if (publicationtype != 6) {
			DualListField<AuthorEntry, String> authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore,
					authorProps.name(), new TextCell());
			authorSelection.setMode(Mode.INSERT);
			authorSelection.setEnableDnd(true);
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
			secondTabVLC.add(authorFP, new VerticalLayoutData(1.0, .45));
		}

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
		secondTabVLC.add(editorFP, new VerticalLayoutData(1.0, .45));

		if (publicationtype == 8) { // hier muss sie bleiben
			TextField seriesEN = new TextField();
			seriesEN.setText(entry.getSeriesEN());
			seriesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setSeriesEN(event.getValue());
				}
			});
			TextField seriesORG = new TextField();
			seriesORG.setText(entry.getSeriesORG());
			seriesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setSeriesORG(event.getValue());
				}
			});
			TextField seriesTR = new TextField();
			seriesTR.setText(entry.getSeriesTR());
			seriesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setSeriesTR(event.getValue());
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

		if (publicationtype == 1 || publicationtype == 5) {
			TextField editionEN = new TextField();
			editionEN.setText(entry.getEditionEN());
			editionEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setEditionEN(event.getValue());
				}
			});
			TextField editionORG = new TextField();
			editionORG.setText(entry.getEditionORG());
			editionORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setEditionORG(event.getValue());
				}
			});
			TextField editionTR = new TextField();
			editionTR.setText(entry.getEditionTR());
			editionTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setEditionTR(event.getValue());
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

		if (publicationtype == 8) {
			TextField volumeEN = new TextField();
			volumeEN.setText(entry.getVolumeEN());
			volumeEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setVolumeEN(event.getValue());
				}
			});
			TextField volumeORG = new TextField();
			volumeORG.setText(entry.getVolumeORG());
			volumeORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setVolumeORG(event.getValue());
				}
			});
			TextField volumeTR = new TextField();
			volumeTR.setText(entry.getVolumeTR());
			volumeTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setVolumeTR(event.getValue());
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

		NumberField<Integer> yearEN = new NumberField<Integer>(new NumberPropertyEditor.IntegerPropertyEditor());
		DateWrapper dw = new DateWrapper(); // we always want to use the current year as max year
		yearEN.setValue(entry.getYearEN());
		yearEN.addValidator(new MaxNumberValidator<Integer>(dw.getFullYear()));
		yearEN.setAllowNegative(false);
		yearEN.addValueChangeHandler(new ValueChangeHandler<Integer>() {

			@Override
			public void onValueChange(ValueChangeEvent<Integer> event) {
				entry.setYearEN(event.getValue());
			}
		});
		TextField yearORG = new TextField();
		yearORG.setText(entry.getYearORG());
		yearORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setYearORG(event.getValue());
			}
		});
		TextField yearTR = new TextField();
		yearTR.setText(entry.getYearTR());
		yearTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setYearTR(event.getValue());
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

		if (publicationtype == 8) { // bleiben
			TextField monthEN = new TextField();
			monthEN.setText(entry.getMonthEN());
			monthEN.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setMonthEN(event.getValue());
				}
			});
			TextField monthORG = new TextField();
			monthORG.setText(entry.getMonthORG());
			monthORG.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setMonthORG(event.getValue());
				}
			});
			TextField monthTR = new TextField();
			monthTR.setText(entry.getMonthTR());
			monthTR.addValueChangeHandler(new ValueChangeHandler<String>() {

				@Override
				public void onValueChange(ValueChangeEvent<String> event) {
					entry.setMonthTR(event.getValue());
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

		TextField pagesEN = new TextField();
		pagesEN.setText(entry.getPagesEN());
		pagesEN.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setPagesEN(event.getValue());
			}
		});
		TextField pagesORG = new TextField();
		pagesORG.setText(entry.getPagesORG());
		pagesORG.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setPagesORG(event.getValue());
			}
		});
		TextField pagesTR = new TextField();
		pagesTR.setText(entry.getPagesTR());
		pagesTR.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setPagesTR(event.getValue());
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

		TextArea commentsTA = new TextArea();
		FramedPanel commentsFP = new FramedPanel();
		commentsFP.setHeading("Comments");
		commentsFP.add(commentsTA);
		thirdTabVLC.add(commentsFP, new VerticalLayoutData(1.0, .25));
		commentsTA.setText(entry.getComments());
		commentsTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setComments(event.getValue());
			}
		});

		TextArea notesTA = new TextArea();
		FramedPanel notesFP = new FramedPanel();
		notesFP.setHeading("Notes");
		notesFP.add(notesTA);
		notesTA.setText(entry.getNotes());
		notesTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				entry.setNotes(event.getValue());
			}
		});
		thirdTabVLC.add(notesFP, new VerticalLayoutData(1.0, .25));

		TextField urlTF = new TextField();
		HorizontalLayoutContainer urlHLC = new HorizontalLayoutContainer();
		urlHLC.add(urlTF, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel urlFP = new FramedPanel();
		urlFP.setHeading("URL");
		urlFP.add(urlHLC, new HorizontalLayoutData(1.0, 1.0));
		urlTF.setText(entry.getUrl());
		urlTF.addValidator(new RegExValidator(
				"^(((https?|ftps?)://)(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$", "Please enter valid URL"));
		urlTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (urlTF.validate()) {
					entry.setUrl(event.getValue());
				}
			}
		});
		thirdTabVLC.add(urlFP, new VerticalLayoutData(1.0, .1));

		TextField uriTF = new TextField();
		FramedPanel uriFP = new FramedPanel();
		uriFP.setHeading("URI");
		uriFP.add(uriTF);
		uriTF.setText(entry.getUri());
		uriTF.addValidator(new RegExValidator(
				"^(((https?|ftps?)://)(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)([).!';/?:,][[:blank:]])?$", "Please enter valid URI"));
		uriTF.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (uriTF.validate()) {
					entry.setUri(event.getValue());
				}
			}
		});
		thirdTabVLC.add(uriFP, new VerticalLayoutData(1.0, .1));

		CheckBox unpublishedCB = new CheckBox();
		FramedPanel unpublishedFP = new FramedPanel();
		unpublishedFP.setHeading("Unpublished");
		unpublishedFP.add(unpublishedCB);
		unpublishedCB.setValue(entry.isUnpublished());
		unpublishedCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				entry.setUnpublished(event.getValue());
			}
		});
		thirdTabVLC.add(unpublishedFP, new VerticalLayoutData(1.0, .1));

		ComboBox<AnnotatedBiblographyEntry> firstEditionComboBox = new ComboBox<AnnotatedBiblographyEntry>(annotatedBiblographyEntryLS,
				annotatedBiblographyEntryProps.titleEN(), new AbstractSafeHtmlRenderer<AnnotatedBiblographyEntry>() {

					@Override
					public SafeHtml render(AnnotatedBiblographyEntry item) {
						final AnnotatedBiblographyEntryViewTemplates pvTemplates = GWT.create(AnnotatedBiblographyEntryViewTemplates.class);
						return pvTemplates.AnnotatedBiblographyEntry(item.getTitleEN());
					}
				});
		firstEditionComboBox.setValue(annotatedBiblographyEntryLS.findModelWithKey(Integer.toString(entry.getFirstEditionBibID())));
		firstEditionComboBox.addValueChangeHandler(new ValueChangeHandler<AnnotatedBiblographyEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<AnnotatedBiblographyEntry> event) {
				entry.setFirstEditionBibID(event.getValue().getAnnotatedBiblographyID());
			}
		});

		CheckBox firstEditionCB = new CheckBox();
		firstEditionCB.setValue(true);
		FramedPanel firstEditionFP = new FramedPanel();
		firstEditionFP.setHeading("FirstEdition");
		firstEditionFP.add(firstEditionCB);
		firstEditionCB.setValue(entry.isFirstEdition());
		firstEditionCB.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

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
		});
		thirdTabVLC.add(firstEditionFP, new VerticalLayoutData(1.0, .1));
	}

}
