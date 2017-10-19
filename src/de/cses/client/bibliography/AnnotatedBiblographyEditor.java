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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;

import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

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
	private VBoxLayoutContainer widget;
	HorizontalPanel horizontBackground;
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
	
	VerticalLayoutContainer vlc= new VerticalLayoutContainer();;
	
	FramedPanel overviewFramedPanel = new FramedPanel(); // Framed Panel welches alles vom overview beinhaltet
	
	FramedPanel frame = new FramedPanel(); // das oberste Framed Panel als Rahmen
	VerticalLayoutContainer backgroundoverview = new VerticalLayoutContainer(); // verticaler background fuer die Lioteratur
	VerticalLayoutContainer overviewvertical = new VerticalLayoutContainer(); // hintergrund welcher alle frames panels beinhaltet

	
	
	public AnnotatedBiblographyEditor(AnnotatedBiblographyEntry entry){
		this.entry = entry;
	}
	
	@Override
	public Widget asWidget() {
		if (widget == null) {
			widget = new VBoxLayoutContainer();
			init();
			widget.setSize("1000px", "1000px");
			widget.add(createForm());
		}

		return widget;
	}
	public AnnotatedBiblographyEditor() {

	}
	
	public void init(){

		authorProps = GWT.create(AuthorProperties.class);
		publisherProps = GWT.create(PublisherProperties.class);
		publicationTypeProps = GWT.create(PublicationTypeProperties.class);
		authorListStore = new ListStore<AuthorEntry>(authorProps.authorID());
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
	public Widget createForm(){
	
		frame.setHeading("Annotated Biblography");
		frame.add(overviewvertical);
		overviewvertical.add(overviewFramedPanel);
		
		//Overview FramedPanel

		overviewFramedPanel.setHeading("Literature");
		overviewFramedPanel.add(backgroundoverview);
		
		
		publicationTypeComboBox = new ComboBox<PublicationTypeEntry>(publicationTypeListStore, publicationTypeProps.name(),
				new AbstractSafeHtmlRenderer<PublicationTypeEntry>() {

					@Override
					public SafeHtml render(PublicationTypeEntry item) {
						final PublicationTypeViewTemplates pvTemplates = GWT.create(PublicationTypeViewTemplates.class);
						return pvTemplates.publicationType(item.getName());
					}
				});
		backgroundoverview.add(publicationTypeComboBox);
		
		
		SelectionHandler<PublicationTypeEntry> publicationTypeSelectionHandler = new SelectionHandler<PublicationTypeEntry>() {

			@Override
			public void onSelection(SelectionEvent<PublicationTypeEntry> event) {
			
				backgroundoverview.remove(vlc);
				vlc.removeFromParent();
				vlc.clear();
				vlc.setEnabled(false);
				publicationtype = event.getSelectedItem().getPublicationTypeID();
				backgroundoverview.add(buildEingabe(publicationtype));
			}
		};
		publicationTypeComboBox.addSelectionHandler(publicationTypeSelectionHandler);
		
		return frame;
	}
	
	public Widget buildEingabe(int publicationtype){
		vlc = new VerticalLayoutContainer();
		
		horizontBackground = new HorizontalPanel();
		TextField titelEN = new TextField();
		TextField titelORG = new TextField();
		TextField titelTR = new TextField();
		HTML titelDES = new HTML("Titel: ");
		
		horizontBackground.add(titelDES);
		horizontBackground.add(titelEN);
		horizontBackground.add(titelTR);
		horizontBackground.add(titelORG);
		vlc.add(horizontBackground);
		
		if(publicationtype == 6){
		horizontBackground = new HorizontalPanel();
		TextField procEN = new TextField();
		TextField procORG = new TextField();
		TextField procTR = new TextField();
		HTML procDES = new HTML("Proceedings Title: ");
		
		horizontBackground.add(procDES);
		horizontBackground.add(procEN);
		horizontBackground.add(procTR);
		horizontBackground.add(procORG);
		vlc.add(horizontBackground);
		}
		
		if(publicationtype == 5){
		horizontBackground = new HorizontalPanel();
		TextField chaptitEN = new TextField();
		TextField chaptitORG = new TextField();
		TextField chaptitTR = new TextField();
		HTML chaptitDES = new HTML("Chapter Title: ");
		
		horizontBackground.add(chaptitDES);
		horizontBackground.add(chaptitEN);
		horizontBackground.add(chaptitTR);
		horizontBackground.add(chaptitORG);
		vlc.add(horizontBackground);
		}
		
		if(publicationtype == 1){
		
		horizontBackground = new HorizontalPanel();
		TextField booktitelEN = new TextField();
		TextField booktitelORG = new TextField();
		TextField booktitelTR = new TextField();
		HTML booktitelDES = new HTML("Booktitel: ");
		
		horizontBackground.add(booktitelDES);
		horizontBackground.add(booktitelEN);
		horizontBackground.add(booktitelTR);
		horizontBackground.add(booktitelORG);
		vlc.add(horizontBackground);
		}
		
		if(publicationtype == 3){
		horizontBackground = new HorizontalPanel();
		TextField uniEN = new TextField();
		TextField uniORG = new TextField();
		TextField uniTR = new TextField();
		HTML uniDES = new HTML("University: ");
		
		horizontBackground.add(uniDES);
		horizontBackground.add(uniEN);
		horizontBackground.add(uniTR);
		horizontBackground.add(uniORG);
		vlc.add(horizontBackground);
		}
		
		if(publicationtype == 8){
		
		horizontBackground = new HorizontalPanel();
		TextField numberEN = new TextField();
		TextField numberORG = new TextField();
		TextField numberTR = new TextField();
		HTML numberDES = new HTML("Number: ");
		
		horizontBackground.add(numberDES);
		horizontBackground.add(numberEN);
		horizontBackground.add(numberTR);
		horizontBackground.add(numberORG);
		vlc.add(horizontBackground);
		}
		
		
		if(publicationtype == 7){
		horizontBackground = new HorizontalPanel();
		TextField accessEN = new TextField();
		TextField accessORG = new TextField();
		TextField accessTR = new TextField();
		HTML accessDES = new HTML("Access Date: ");
		
		horizontBackground.add(accessDES);
		horizontBackground.add(accessEN);
		horizontBackground.add(accessTR);
		horizontBackground.add(accessORG);
		vlc.add(horizontBackground);
		}
		
		horizontBackground = new HorizontalPanel();
		TextField titeladdonEN = new TextField();
		TextField titeladdonORG = new TextField();
		TextField titeladdonTR = new TextField();
		HTML titeladdonDES = new HTML("Titeladdon: ");
		
		horizontBackground.add(titeladdonDES);
		horizontBackground.add(titeladdonEN);
		horizontBackground.add(titeladdonTR);
		horizontBackground.add(titeladdonORG);
		vlc.add(horizontBackground);
		

		publisherComboBox = new ComboBox<PublisherEntry>(publisherListStore, publisherProps.name(),
				new AbstractSafeHtmlRenderer<PublisherEntry>() {

					@Override
					public SafeHtml render(PublisherEntry item) {
						final PublisherViewTemplates pvTemplates = GWT.create(PublisherViewTemplates.class);
						return pvTemplates.publisher(item.getName());
					}
				});
		horizontBackground = new HorizontalPanel();
		HTML publisher = new HTML("Publisher: ");
		horizontBackground.add(publisher);
		horizontBackground.add(publisherComboBox);
		vlc.add(horizontBackground);
		
		authorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedAuthorListStore, authorProps.name(), new TextCell());
		
		editorSelection = new DualListField<AuthorEntry, String>(authorListStore, selectedEditorListStore, authorProps.name(), new TextCell());
		
		if(publicationtype != 6){
		horizontBackground = new HorizontalPanel();
		HTML author = new HTML("Author: ");
		horizontBackground.add(author);
		horizontBackground.add(authorSelection);
		vlc.add(horizontBackground);
		}
		
		horizontBackground = new HorizontalPanel();
		HTML editor = new HTML("Editor: ");
		horizontBackground.add(editor);
		horizontBackground.add(editorSelection);
		vlc.add(horizontBackground);
		
		
		if(publicationtype == 8){
		horizontBackground = new HorizontalPanel();
		TextField seriesEN = new TextField();
		TextField seriesORG = new TextField();
		TextField seriesTR = new TextField();
		HTML seriesDES = new HTML("Serie: ");
		
		horizontBackground.add(seriesDES);
		horizontBackground.add(seriesEN);
		horizontBackground.add(seriesTR);
		horizontBackground.add(seriesORG);
		vlc.add(horizontBackground);
		}
		
		if(publicationtype == 1 ||publicationtype ==  5){
		
		horizontBackground = new HorizontalPanel();
		TextField editionEN = new TextField();
		TextField editionORG = new TextField();
		TextField editionTR = new TextField();
		HTML editionDES = new HTML("Edition: ");
		
		horizontBackground.add(editionDES);
		horizontBackground.add(editionEN);
		horizontBackground.add(editionTR);
		horizontBackground.add(editionORG);
		vlc.add(horizontBackground);
		}
		if(publicationtype == 8){
		horizontBackground = new HorizontalPanel();
		TextField volumeEN = new TextField();
		TextField volumeORG = new TextField();
		TextField volumeTR = new TextField();
		HTML volumeDES = new HTML("Volume: ");
		
		horizontBackground.add(volumeDES);
		horizontBackground.add(volumeEN);
		horizontBackground.add(volumeTR);
		horizontBackground.add(volumeORG);
		vlc.add(horizontBackground);
		}
		horizontBackground = new HorizontalPanel();
		TextField yearEN = new TextField();
		TextField yearORG = new TextField();
		TextField yearTR = new TextField();
		HTML yearDES = new HTML("Year: ");
		
		horizontBackground.add(yearDES);
		horizontBackground.add(yearEN);
		horizontBackground.add(yearTR);
		horizontBackground.add(yearORG);
		vlc.add(horizontBackground);
		
		if(publicationtype == 8){
		horizontBackground = new HorizontalPanel();
		TextField monthEN = new TextField();
		TextField monthORG = new TextField();
		TextField monthTR = new TextField();
		HTML monthDES = new HTML("Month: ");
		
		horizontBackground.add(monthDES);
		horizontBackground.add(monthEN);
		horizontBackground.add(monthTR);
		horizontBackground.add(monthORG);
		vlc.add(horizontBackground);
		}

		horizontBackground = new HorizontalPanel();
		TextField pagesEN = new TextField();
		TextField pagesORG = new TextField();
		TextField pagesTR = new TextField();
		HTML pagesDES = new HTML("Pages: ");
		
		horizontBackground.add(pagesDES);
		horizontBackground.add(pagesEN);
		horizontBackground.add(pagesTR);
		horizontBackground.add(pagesORG);
		vlc.add(horizontBackground);
		
	TextArea comments = new TextArea();
	horizontBackground = new HorizontalPanel();
	HTML commentsdes = new HTML("Comments: ");
	horizontBackground.add(commentsdes);
	horizontBackground.add(comments);
	vlc.add(horizontBackground);
	
	TextArea notes = new TextArea();
	horizontBackground = new HorizontalPanel();
	HTML notesdes = new HTML("Notes: ");
	horizontBackground.add(notesdes);
	horizontBackground.add(notes);
	vlc.add(horizontBackground);
	
	
	TextField url = new TextField();
	horizontBackground = new HorizontalPanel();
	HTML urldes = new HTML("URL: ");
	horizontBackground.add(urldes);
	horizontBackground.add(url);
	vlc.add(horizontBackground);
	
	TextField uri = new TextField();
	horizontBackground = new HorizontalPanel();
	HTML urides = new HTML("URI: ");
	horizontBackground.add(urides);
	horizontBackground.add(uri);
	vlc.add(horizontBackground);
	
	CheckBox unpublished = new CheckBox();
	horizontBackground = new HorizontalPanel();
	HTML unpublisheddes = new HTML("Unpublished: ");
	horizontBackground.add(unpublisheddes);
	horizontBackground.add(unpublished);
	vlc.add(horizontBackground);
	
	CheckBox erstauflage = new CheckBox();
	horizontBackground = new HorizontalPanel();
	HTML erstauflagedes = new HTML("First Edition: ");
	horizontBackground.add(erstauflagedes);
	horizontBackground.add(erstauflage);
	erstauflage.setValue(true);
	vlc.add(horizontBackground);
		 
	return vlc;
		
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


