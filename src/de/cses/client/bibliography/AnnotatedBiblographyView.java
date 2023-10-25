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
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.dnd.core.client.DndDragStartEvent;
import com.sencha.gxt.dnd.core.client.DragSource;

import de.cses.client.Util;
import de.cses.client.ui.AbstractEditor;
import de.cses.client.ui.AbstractView;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

/**
 * @author Nina; Erik Radisch
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
	private SafeHtml pdf;

	/**
	 * @param text
	 */
	public AnnotatedBiblographyView(AnnotatedBibliographyEntry annotatedBibliographyEntry) {
		this.annotatedBibliographyEntry = annotatedBibliographyEntry;
		dvTemplates = GWT.create(AnnotatedBibliographyViewTemplates.class);
		//
	//setHTML(dvTemplates.view(annotatedBibliographyEntry));
//		setHTML(dvTemplates.extendedView(annotatedBibliographyEntry));
		processToView(annotatedBibliographyEntry);
		setSize("95%", "90%");
		Integer.toString(getHTML().length());

		DragSource source = new DragSource(this) {

			@Override
			protected void onDragStart(DndDragStartEvent event) {
				super.onDragStart(event);
				event.setData(annotatedBibliographyEntry);
				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry, UserLogin.getInstance().getUsernameSessionIDParameterForUri(), annotatedBibliographyEntry.getArticle()));
//				event.getStatusProxy().update(dvTemplates.view(annotatedBibliographyEntry.getTitleEN()));
			}
			
		};
	}

	/* (non-Javadoc)
	 * @see de.cses.client.ui.AbstractView#getEditor()
	 */
	protected AbstractEditor getEditor(AbstractEntry entry, AbstractView av) {
		return new AnnotatedBibliographyEditor(annotatedBibliographyEntry.clone(), av);
	}
	
	public void setEditor(AnnotatedBibliographyEntry entry) {
		this.annotatedBibliographyEntry=entry;
	}

	public void processToView(AnnotatedBibliographyEntry annotatedBibliographyEntry) {
		bib = "";
		translit = "";
		bold = "";
		translat = "";
		tail = "";
		SafeHtmlBuilder shb = new SafeHtmlBuilder();
		if (!(annotatedBibliographyEntry.getAnnotationHTML() == null || annotatedBibliographyEntry.getAnnotationHTML().isEmpty() || annotatedBibliographyEntry.getAnnotationHTML().trim().isEmpty())) {
			String uri="resource?annotation=" + Integer.toString(annotatedBibliographyEntry.getAnnotatedBibliographyID());
			shb.append(SafeHtmlUtils.fromTrustedString("<a href="+uri+"><img width=\"24\" height=\"24\" src=\"icons/annotated pdf-button.ai.svg\" onmouseover=\"this.src='icons/annotated pdf-mouse over.ai.svg'\"\r\n"
					+ "onmouseout=\"this.src='icons/annotated pdf-button.ai.svg'\" alt=\"has annotation\"></img></a> "));
		}
		if (annotatedBibliographyEntry.getArticle()) {
			String uri="resource?document=" + annotatedBibliographyEntry.getUniqueID() + "-paper.pdf" + UserLogin.getInstance().getUsernameSessionIDParameterForUri();
			shb.append(SafeHtmlUtils.fromTrustedString("<a href="+uri+" target=\"_blank\"><img width=\"24\" height=\"24\" src=\"icons/artikel pdf-button.ai.svg\" onmouseover=\"this.src='icons/artikel pdf-mouse over.ai.svg'\"\r\n"
					+ "onmouseout=\"this.src='icons/artikel pdf-button.ai.svg'\" alt=\"has pdf\"></img></a> "));
		}
		pdf=shb.toSafeHtml();
		if ((annotatedBibliographyEntry.getPublicationTypeID()==1) || (annotatedBibliographyEntry.getPublicationTypeID()==3)) {
			if (annotatedBibliographyEntry.getEditors().isEmpty()) {
				if (!annotatedBibliographyEntry.getAuthors().isEmpty()) {
					bib=bib+annotatedBibliographyEntry.getAuthors();
				}
			}
			else {
				bib=bib+annotatedBibliographyEntry.getEditors()+" (ed.)";
			}
			if (!annotatedBibliographyEntry.getYearORG().isEmpty()) {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (!annotatedBibliographyEntry.getTitleTRFull().isEmpty()) {
				translit=" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (!annotatedBibliographyEntry.getTitleORGFull().isEmpty()) {
				bold=" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (!annotatedBibliographyEntry.getTitleENFull().isEmpty()) {
				translat=" "+annotatedBibliographyEntry.getTitleENFull();
			}
			if (!annotatedBibliographyEntry.getVolumeORG().isEmpty()) {
				tail=tail+", Vol."+annotatedBibliographyEntry.getVolumeORG();
			}
			if (!annotatedBibliographyEntry.getEditionORG().isEmpty()) {
				tail=tail+", Edition: "+annotatedBibliographyEntry.getEditionORG();
			}
			if (!annotatedBibliographyEntry.getSeriesORG().isEmpty()) {
				tail=tail+", Series: "+annotatedBibliographyEntry.getSeriesORG();
			}
			tail=tail+". ";
			if (!annotatedBibliographyEntry.getPublisher().isEmpty()) {
				tail=tail+annotatedBibliographyEntry.getPublisher();
					}
//			if (annotatedBibliographyEntry.getEditors()!="") {
//				if (annotatedBibliographyEntry.getPublisher()!="") {
//					tail= tail+", "+annotatedBibliographyEntry.getEditors();
//				}
//				else{
//					tail= tail+annotatedBibliographyEntry.getEditors();
//				}
//				if (annotatedBibliographyEntry.getEditorType()=="") {
//					bib=bib+" ("+annotatedBibliographyEntry.getEditorType()+")";
//					}
//			}
			if (!annotatedBibliographyEntry.getThesisType().isEmpty()) {
				if (annotatedBibliographyEntry.getPublisher().isEmpty()) {
					tail=tail+annotatedBibliographyEntry.getThesisType()+" thesis";
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getThesisType();
				}
			}
			tail=tail+". ";
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail,pdf));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail,pdf));
			}
			
		}
		else if ((annotatedBibliographyEntry.getPublicationTypeID()== 5)) {
			bib=bib+annotatedBibliographyEntry.getAuthors().trim();
			System.out.println("\""+bib+"\"");
			if (!annotatedBibliographyEntry.getYearORG().isEmpty()) {
				bib=bib.trim()+", "+annotatedBibliographyEntry.getYearORG().trim()+",";
			}
			if (!annotatedBibliographyEntry.getTitleTRFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleTRFull().trim();
			}
			if (!annotatedBibliographyEntry.getTitleORGFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleORGFull().trim();
			}
			if (!annotatedBibliographyEntry.getTitleENFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleENFull().trim();
			}
			bib=bib+". In: ";
			if (!annotatedBibliographyEntry.getEditors().isEmpty()) {
				bib= bib+annotatedBibliographyEntry.getEditors().trim();
				if (annotatedBibliographyEntry.getEditorType().isEmpty()) {
					bib=bib+" ("+annotatedBibliographyEntry.getEditorType().trim()+")";
					}
			}
			if (!annotatedBibliographyEntry.getParentTitleTR().isEmpty()) {
				translit=translit+" "+annotatedBibliographyEntry.getTitleTRFull().trim();
			}
			if (!annotatedBibliographyEntry.getParentTitleORG().isEmpty()) {
				bold=bold+" "+annotatedBibliographyEntry.getParentTitleORG().trim();
			}
			if (!annotatedBibliographyEntry.getParentTitleEN().isEmpty()) {
				translat=translat+" ["+annotatedBibliographyEntry.getParentTitleEN().trim()+"]";
			}
			tail=tail+". ";
			if (!annotatedBibliographyEntry.getPublisher().isEmpty()) {
				tail=tail+annotatedBibliographyEntry.getPublisher().trim();
					}
			if (!annotatedBibliographyEntry.getPagesORG().isEmpty()) {
				if (annotatedBibliographyEntry.getPublisher().isEmpty()) {
					tail=tail+". "+annotatedBibliographyEntry.getPagesORG().trim();
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getPagesORG().trim();
				}
			}
			if (!annotatedBibliographyEntry.getUrl().isEmpty()) {
				tail=tail+", "+annotatedBibliographyEntry.getUrl().trim();
				if (!annotatedBibliographyEntry.getAccessdateORG().isEmpty()) {
					tail=tail+" ["+annotatedBibliographyEntry.getAccessdateORG().trim()+"]";
				}
			}
			if (tail != ". ") {
				tail=tail+". ";				
			}
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail,pdf));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail,pdf));
			}
		}
		else if ((annotatedBibliographyEntry.getPublicationTypeID()==4)||(annotatedBibliographyEntry.getPublicationTypeID()==7)) {
			bib=bib+annotatedBibliographyEntry.getAuthors();
			if (!annotatedBibliographyEntry.getYearORG().isEmpty()) {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (!annotatedBibliographyEntry.getTitleTRFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (!annotatedBibliographyEntry.getTitleORGFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (!annotatedBibliographyEntry.getTitleENFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleENFull();
			}
			bib=bib+". In: ";
			if (!annotatedBibliographyEntry.getEditors().isEmpty()) {
				bib= bib+annotatedBibliographyEntry.getEditors();
				if (annotatedBibliographyEntry.getEditorType().isEmpty()) {
					bib=bib+" ("+annotatedBibliographyEntry.getEditorType()+")";
					}
			}
			if (!annotatedBibliographyEntry.getParentTitleTR().isEmpty()) {
				translit=translit+" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (!annotatedBibliographyEntry.getParentTitleORG().isEmpty()) {
				bold=bold+" "+annotatedBibliographyEntry.getParentTitleORG();
			}
			if (!annotatedBibliographyEntry.getParentTitleEN().isEmpty()) {
				translat=translat+" ["+annotatedBibliographyEntry.getParentTitleEN()+"]";
			}
			tail=tail+". ";
			if (!annotatedBibliographyEntry.getPublisher().isEmpty()) {
				tail=tail+annotatedBibliographyEntry.getPublisher();
					}
			if (!annotatedBibliographyEntry.getPagesORG().isEmpty()) {
				if (annotatedBibliographyEntry.getPublisher().isEmpty()) {
					tail=tail+". "+annotatedBibliographyEntry.getPagesORG();
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getPagesORG();
				}
			}
			if (!annotatedBibliographyEntry.getUrl().isEmpty()) {
				tail=tail+", "+annotatedBibliographyEntry.getUrl();
				if (!annotatedBibliographyEntry.getAccessdateORG().isEmpty()) {
					tail=tail+" ["+annotatedBibliographyEntry.getAccessdateORG()+"]";
				}
			}
			if (tail != ". ") {
				tail=tail+". ";				
			}
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail,pdf));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail,pdf));
			}
		}
		else if (annotatedBibliographyEntry.getPublicationTypeID()==8) {
			bib=bib+annotatedBibliographyEntry.getAuthors();
			if (!annotatedBibliographyEntry.getYearORG().isEmpty()) {
				bib=bib+", "+annotatedBibliographyEntry.getYearORG()+",";
			}
			if (!annotatedBibliographyEntry.getTitleTRFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleTRFull();
			}
			if (!annotatedBibliographyEntry.getTitleORGFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleORGFull();
			}
			if (!annotatedBibliographyEntry.getTitleENFull().isEmpty()) {
				bib=bib+" "+annotatedBibliographyEntry.getTitleENFull();
			}
			if (!annotatedBibliographyEntry.getParentTitleORG().isEmpty()) {
				bold=bold+", "+annotatedBibliographyEntry.getParentTitleORG();
			}
			if (!annotatedBibliographyEntry.getVolumeORG().isEmpty()) {
				tail=tail+" "+annotatedBibliographyEntry.getVolumeORG();
			}
			if (!annotatedBibliographyEntry.getIssueORG().isEmpty()) {
				tail=tail+" "+annotatedBibliographyEntry.getIssueORG();
			}
			if (!annotatedBibliographyEntry.getPagesORG().isEmpty()) {
				if (annotatedBibliographyEntry.getPublisher().isEmpty()) {
					tail=tail+". "+annotatedBibliographyEntry.getPagesORG();
					}
				else {
					tail=tail+", "+annotatedBibliographyEntry.getPagesORG();
				}
			}
			if (annotatedBibliographyEntry.getHasHan()) {

				setHTML(dvTemplates.bibViewHasHan(bib, translit, bold, translat, tail,pdf));
			}
			else {
				setHTML(dvTemplates.bibView(bib, translit, bold, translat, tail,pdf));
			}

		}
		else {
			Util.doLogging(Boolean.toString(annotatedBibliographyEntry.getArticle()));
			setHTML(dvTemplates.view(annotatedBibliographyEntry, UserLogin.getInstance().getUsernameSessionIDParameterForUri(), annotatedBibliographyEntry.getArticle()));
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
			processToView(annotatedBibliographyEntry);
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
