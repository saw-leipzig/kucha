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

import com.google.gwt.core.client.GWT;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.Util;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyView extends AbstractView {
	
	private AnnotatedBibliographyEntry annotatedBibliographyEntry;
	private AnnotatedBibliographyViewTemplates dvTemplates;
	private String bib="";
	private String translit="";
	private String bold="";
	private String translat="";
	private String tail="";

	/**
	 * @param text
	 */
	public AnnotatedBiblographyView(AnnotatedBibliographyEntry annotatedBibliographyEntry) {
		this.annotatedBibliographyEntry = annotatedBibliographyEntry;
		dvTemplates = GWT.create(AnnotatedBibliographyViewTemplates.class);
		//
	//setHTML(dvTemplates.view(annotatedBibliographyEntry));
//		setHTML(dvTemplates.extendedView(annotatedBibliographyEntry));
		processtoview(annotatedBibliographyEntry);
		setSize("95%", "5em");
		Integer.toString(getHTML().length());

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(annotatedBibliographyEntry);
				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry));
//				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry.getTitleEN()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	protected AbstractEditor getEditor(AbstractEntry entry) {
		return new AnnotatedBibliographyEditor(annotatedBibliographyEntry.clone());
	}
	
	public void setEditor(AnnotatedBibliographyEntry entry) {
		this.annotatedBibliographyEntry=entry;
	}

	public void processtoview(AnnotatedBibliographyEntry annotatedBibliographyEntry) {
		if ((annotatedBibliographyEntry.getPublicationTypeID()==1) || (annotatedBibliographyEntry.getPublicationTypeID()==3)) {
			if (annotatedBibliographyEntry.getAuthors()=="") {

			}
			else {
					bib=bib+annotatedBibliographyEntry.getAuthors();
			}
			if (annotatedBibliographyEntry.getYearORG()!="") {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (annotatedBibliographyEntry.getTitleTRFull()!="") {
				translit=" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (annotatedBibliographyEntry.getTitleORGFull()!="") {
				bold=" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (annotatedBibliographyEntry.getTitleENFull()!="") {
				translat=" "+annotatedBibliographyEntry.getTitleENFull();
			}
			if (annotatedBibliographyEntry.getVolumeORG()!="") {
				tail=tail+", Vol."+annotatedBibliographyEntry.getVolumeORG();
			}
			if (annotatedBibliographyEntry.getEditionORG()!="") {
				tail=tail+", Edition: "+annotatedBibliographyEntry.getEditionORG();
			}
			if (annotatedBibliographyEntry.getSeriesORG()!="") {
				tail=tail+", Series: "+annotatedBibliographyEntry.getSeriesORG();
			}
			tail=tail+". ";
			if (annotatedBibliographyEntry.getPublisher()!="") {
				tail=tail+annotatedBibliographyEntry.getPublisher();
					}
			if (annotatedBibliographyEntry.getEditors()!="") {
				if (annotatedBibliographyEntry.getPublisher()!="") {
					tail= tail+", "+annotatedBibliographyEntry.getEditors();
				}
				else{
					tail= tail+annotatedBibliographyEntry.getEditors();
				}
				if (annotatedBibliographyEntry.getEditorType()=="") {
					bib=bib+" ("+annotatedBibliographyEntry.getEditorType()+")";
					}
			}
			if (annotatedBibliographyEntry.getThesisType()!="") {
				if (annotatedBibliographyEntry.getPublisher()=="") {
					tail=tail+annotatedBibliographyEntry.getThesisType()+" thesis";
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getThesisType();
				}
			}
			tail=tail+". ";
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail));
			}
			
		}
		else if ((annotatedBibliographyEntry.getPublicationTypeID()==4)||(annotatedBibliographyEntry.getPublicationTypeID()==7)) {
			bib=bib+annotatedBibliographyEntry.getAuthors();
			if (annotatedBibliographyEntry.getYearORG()!="") {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (annotatedBibliographyEntry.getTitleTRFull()!="") {
				bib=bib+" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (annotatedBibliographyEntry.getTitleORGFull()!="") {
				bib=bib+" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (annotatedBibliographyEntry.getTitleENFull()!="") {
				bib=bib+" "+annotatedBibliographyEntry.getTitleENFull();
			}
			bib=bib+". In: ";
			if (annotatedBibliographyEntry.getEditors()!="") {
				bib= bib+annotatedBibliographyEntry.getEditors();
				if (annotatedBibliographyEntry.getEditorType()=="") {
					bib=bib+" ("+annotatedBibliographyEntry.getEditorType()+")";
					}
			}
			if (annotatedBibliographyEntry.getParentTitleTR()!="") {
				translit=translit+" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (annotatedBibliographyEntry.getParentTitleORG()!="") {
				bold=bold+" "+annotatedBibliographyEntry.getParentTitleORG();
			}
			if (annotatedBibliographyEntry.getParentTitleEN()!="") {
				translat=translat+" "+annotatedBibliographyEntry.getParentTitleEN();
			}
			tail=tail+". ";
			if (annotatedBibliographyEntry.getPublisher()!="") {
				tail=tail+annotatedBibliographyEntry.getPublisher();
					}
			if (annotatedBibliographyEntry.getPagesORG()!="") {
				if (annotatedBibliographyEntry.getPublisher()=="") {
					tail=tail+". "+annotatedBibliographyEntry.getPagesORG();
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getPagesORG();
				}
			}
			if (annotatedBibliographyEntry.getUrl()!="") {
				tail=tail+", "+annotatedBibliographyEntry.getUrl();
				if (annotatedBibliographyEntry.getAccessdateORG()!="") {
					tail=tail+" ["+annotatedBibliographyEntry.getAccessdateORG()+"]";
				}
			}
			tail=tail+". ";
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail));
			}
		}
		else if (annotatedBibliographyEntry.getPublicationTypeID()==8) {
			bib=bib+annotatedBibliographyEntry.getAuthors();
			if (annotatedBibliographyEntry.getYearORG()!="") {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (annotatedBibliographyEntry.getTitleTRFull()!="") {
				translit=" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (annotatedBibliographyEntry.getTitleORGFull()!="") {
				bold=" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (annotatedBibliographyEntry.getTitleENFull()!="") {
				translat=" "+annotatedBibliographyEntry.getTitleENFull();
			}
			if (annotatedBibliographyEntry.getParentTitleORG()!="") {
				tail=tail+", "+annotatedBibliographyEntry.getParentTitleORG();
			}
			if (annotatedBibliographyEntry.getVolumeORG()!="") {
				tail=tail+" "+annotatedBibliographyEntry.getVolumeORG();
			}
			if (annotatedBibliographyEntry.getIssueORG()!="") {
				tail=tail+" "+annotatedBibliographyEntry.getIssueORG();
			}
			if (annotatedBibliographyEntry.getPagesORG()!="") {
				if (annotatedBibliographyEntry.getPublisher()=="") {
					tail=tail+". "+annotatedBibliographyEntry.getPagesORG();
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getPagesORG();
				}
			}
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail));
			}

		}
		else {
			setHTML(dvTemplates.view(annotatedBibliographyEntry));
		}
	}
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEntry()
	 */
	protected AbstractEntry getEntry() {
		return annotatedBibliographyEntry;
	}

//	/* (non-Javadoc)
//	 * @see de.cses.client.ui.EditorListener#updateEntryRequest(de.cses.shared.AbstractEntry)
//	 */
//	@Override
//	public void updateEntryRequest(AbstractEntry updatedEntry) {
//	}

	@Override
	public void closeRequest(AbstractEntry entry) {
		super.closeRequest(entry);
		if (entry != null && entry instanceof AnnotatedBibliographyEntry) { // refresh view
			annotatedBibliographyEntry = (AnnotatedBibliographyEntry) entry;
//			setHTML(dvTemplates.view(annotatedBibliographyEntry));
//			setHTML(dvTemplates.extendedView(annotatedBibliographyEntry));
			processtoview(annotatedBibliographyEntry);
		}
	}
	public String getLabel() {
		String result = bib + translit + bold + translat + tail;
		return result;
	}
	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getPermalink()
	 */
	@Override
	protected String getPermalink() {
		// TODO Auto-generated method stub
		return null;
	}

}
