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
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
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
import de.cses.shared.CaveEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.PublicationTypeEntry;
import de.cses.shared.PublisherEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor extends AbstractEditor {
	private VBoxLayoutContainer widget;
	HorizontalLayoutContainer horizontBackground;
	AnnotatedBiblographyEntry entry;
	
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	
	int publicationtype= 0;
	
	private DualListField<AuthorEntry, String> authorSelection;
	private DualListField<AuthorEntry, String> editorSelection;

	
	
	private ComboBox<PublisherEntry> publisherComboBox;
	private ComboBox<PublicationTypeEntry> publicationTypeComboBox;
	
	private ListStore<PublicationTypeEntry> publicationTypeListStore;
	private ListStore<PublisherEntry> publisherListStore;
	
	private ListStore<AuthorEntry> authorListStore;
	private ListStore<AuthorEntry> selectedAuthorListStore;
	private ListStore<AuthorEntry> selectedEditorListStore;
	
	private PublisherProperties publisherProps;
	private PublicationTypeProperties publicationTypeProps;
	private AuthorProperties authorProps;
	
	FramedPanel overview = new FramedPanel();
	FramedPanel frame = new FramedPanel();
	VerticalLayoutContainer background = new VerticalLayoutContainer();
	VerticalLayoutContainer overviewVerticalLayout = new VerticalLayoutContainer();
	
	
	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry){
		this.entry = entry;
	}
	
	@Override
	public Widget asWidget() {
		if (widget == null) {
			BoxLayoutData flex = new BoxLayoutData();
			flex.setFlex(1);
			widget = new VBoxLayoutContainer();
			init();
			widget.add(createForm(), flex);
		}

		return widget;
	}
	
	public void init(){
		authorProps = GWT.create(AuthorProperties.class);
		publisherProps = GWT.create(PublisherProperties.class);
		publicationTypeProps = GWT.create(PublicationTypeProperties.class);
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
	}
	public Widget createForm(){
		
		
		frame.setHeading("Annotated Biblography");
		frame.add(background);
		
		//Overview FramedPanel

		overview.setHeading("Literature");
		background.add(overview);
		
		publicationTypeComboBox = new ComboBox<PublicationTypeEntry>(publicationTypeListStore, publicationTypeProps.name(),
				new AbstractSafeHtmlRenderer<PublicationTypeEntry>() {

					@Override
					public SafeHtml render(PublicationTypeEntry item) {
						final PublicationTypeViewTemplates pvTemplates = GWT.create(PublicationTypeViewTemplates.class);
						return pvTemplates.publicationType(item.getName());
					}
				});
		overviewVerticalLayout.add(publicationTypeComboBox);
		
		
		ValueChangeHandler<PublicationTypeEntry> publicationTypeSelectionHandler = new ValueChangeHandler<PublicationTypeEntry>() {

			@Override
			public void onValueChange(ValueChangeEvent<PublicationTypeEntry> event) {
				publicationtype = event.getValue().getId();
			}
		};
		publicationTypeComboBox.addValueChangeHandler(publicationTypeSelectionHandler);
		return build(publicationtype);
	}
	
	public Widget build(int publicationtype){
		
		overview.add(overviewVerticalLayout);
		overviewVerticalLayout.add(horizontBackground);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField titelEN = new TextField();
		TextField titelORG = new TextField();
		TextField titelTR = new TextField();
		HTML titelDES = new HTML("Titel: ");
		
		horizontBackground.add(titelDES);
		horizontBackground.add(titelEN);
		horizontBackground.add(titelTR);
		horizontBackground.add(titelORG);
		overviewVerticalLayout.add(horizontBackground);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField booktitelEN = new TextField();
		TextField booktitelORG = new TextField();
		TextField booktitelTR = new TextField();
		HTML booktitelDES = new HTML("Booktitel: ");
		
		horizontBackground.add(booktitelDES);
		horizontBackground.add(booktitelEN);
		horizontBackground.add(booktitelTR);
		horizontBackground.add(booktitelORG);
		overviewVerticalLayout.add(horizontBackground);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField titeladdonEN = new TextField();
		TextField titeladdonORG = new TextField();
		TextField titeladdonTR = new TextField();
		HTML titeladdonDES = new HTML("Titeladdon: ");
		
		horizontBackground.add(titeladdonDES);
		horizontBackground.add(titeladdonEN);
		horizontBackground.add(titeladdonTR);
		horizontBackground.add(titeladdonORG);
		overviewVerticalLayout.add(horizontBackground);
		
		publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.name(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getName());
					}
				});
		overviewVerticalLayout.add(publisherComboBox);
		
		authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore, authorProps.name(), new TextCell());
		
		editorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedEditorListStore, authorProps.name(), new TextCell());
		
		overviewVerticalLayout.add(authorSelection);
		overviewVerticalLayout.add(editorSelection);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField seriesEN = new TextField();
		TextField seriesORG = new TextField();
		TextField seriesTR = new TextField();
		HTML seriesDES = new HTML("Serie: ");
		
		horizontBackground.add(seriesDES);
		horizontBackground.add(seriesEN);
		horizontBackground.add(seriesTR);
		horizontBackground.add(seriesORG);
		overviewVerticalLayout.add(horizontBackground);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField volumeEN = new TextField();
		TextField volumeORG = new TextField();
		TextField volumeTR = new TextField();
		HTML volumeDES = new HTML("Volume: ");
		
		horizontBackground.add(volumeDES);
		horizontBackground.add(volumeEN);
		horizontBackground.add(volumeTR);
		horizontBackground.add(volumeORG);
		overviewVerticalLayout.add(horizontBackground);

		horizontBackground = new HorizontalLayoutContainer();
		TextField yearEN = new TextField();
		TextField yearORG = new TextField();
		TextField yearTR = new TextField();
		HTML yearDES = new HTML("Year: ");
		
		horizontBackground.add(yearDES);
		horizontBackground.add(yearEN);
		horizontBackground.add(yearTR);
		horizontBackground.add(yearORG);
		overviewVerticalLayout.add(horizontBackground);
		
		horizontBackground = new HorizontalLayoutContainer();
		TextField monthEN = new TextField();
		TextField monthORG = new TextField();
		TextField monthTR = new TextField();
		HTML monthDES = new HTML("Month: ");
		
		horizontBackground.add(monthDES);
		horizontBackground.add(monthEN);
		horizontBackground.add(monthTR);
		horizontBackground.add(monthORG);
		overviewVerticalLayout.add(horizontBackground);

		horizontBackground = new HorizontalLayoutContainer();
		TextField pagesEN = new TextField();
		TextField pagesORG = new TextField();
		TextField pagesTR = new TextField();
		HTML pagesDES = new HTML("Pages: ");
		
		horizontBackground.add(pagesDES);
		horizontBackground.add(pagesEN);
		horizontBackground.add(pagesTR);
		horizontBackground.add(pagesORG);
		overviewVerticalLayout.add(horizontBackground);
		
	TextArea comments = new TextArea();
	overviewVerticalLayout.add(comments);
	
	TextArea notes = new TextArea();
	overviewVerticalLayout.add(notes);
	
	TextField url = new TextField();
	overviewVerticalLayout.add(url);
	
	TextField uri = new TextField();
	overviewVerticalLayout.add(uri);
	
	CheckBox unpublished = new CheckBox();
	overviewVerticalLayout.add(unpublished);
	
	CheckBox erstauflage = new CheckBox();
	overviewVerticalLayout.add(erstauflage);
		
		
		// Annotations FramedPanel
		FramedPanel publication = new FramedPanel();
		overview.setHeading("Annotations");
		background.add(overview);

		horizontBackground = new HorizontalLayoutContainer();
		publication.add(horizontBackground);
		
		
		
		
			return frame;
		
}
	
		
	}
	interface PublisherViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml publisher(String name);
	}
	
	interface PublicationTypeViewTemplates extends XTemplates {
		@XTemplate("<div>{name}</div>")
		SafeHtml publicationType(String name);
	}
	
	
	interface PublisherProperties extends PropertyAccess<PublisherEntry> {
		ModelKeyProvider<PublisherEntry> publisherID();

		LabelProvider<PublisherEntry> name();
	}
	
	interface PublicationTypeProperties extends PropertyAccess<PublicationTypeEntry> {
		ModelKeyProvider<PublicationTypeEntry> publicationTypeID();

		LabelProvider<PublicationTypeEntry> name();
	}
	
	interface AuthorProperties extends PropertyAccess<AuthorEntry> {
		ModelKeyProvider<AuthorEntry> authorID();

		ValueProvider<AuthorEntry,String> name();
	}


