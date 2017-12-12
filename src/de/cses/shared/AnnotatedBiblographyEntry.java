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
package de.cses.shared;

import java.util.ArrayList;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEntry extends AbstractEntry {
	
	private PublicationTypeEntry publicationType;

	private int annotatedBiblographyID;
	private String titleEN;
	private String titleTR;
	private String titleORG;
	
	private String procTitleEN;
	private String procTitleTR;
	private String procTitleORG;
	
	private String bookTitleEN;
	private String bookTitleTR;
	private String bookTitleORG;
	
	private String chapTitleEN;
	private String chapTitleTR;
	private String chapTitleORG;
	
	private String universityEN;
	private String universityORG;
	private String universityTR;
	
	private String numberEN;
	private String numberTR;
	private String numberORG;
	
	private String accessdateEN;
	private String accessdateTR;
	private String accessdateORG;
	
	private String titleaddonEN;
	private String titleaddonORG;
	private String titleaddonTR;
	
	private PublisherEntry publisher;
	
	private String serieEN;
	private String serieTR;
	private String serieORG;
	
	private String editionEN;
	private String editionORG;
	private String editionTR;
	
	private String volumeEN;
	private String volumeTR;
	private String volumeORG;
	
	private int yearEN;
	private String yearORG;
	private String yearTR;
	
	private String monthEN;
	private String monthTR;
	private String monthORG;
	
	private String pagesEN;
	private String pagesORG;
	private String pagesTR;
	
	private String comments;
	
	private String notes;
	
	private String url;
	
	private String uri;
	
	private boolean unpublished;
	
	private boolean erstauflage;
	
	private AnnotatedBiblographyEntry erstauflageEntry;
	
	private ArrayList<AuthorAnnotatedRelation> authorAnnotatedList = new ArrayList<AuthorAnnotatedRelation>();

	private ArrayList<EditorAnnotatedRelation> editorAnnotatedList = new ArrayList<EditorAnnotatedRelation>();
	
	/**
	 * @return the titleEN
	 */
	
	
	public String getTitleEN() {
		return titleEN;
	}

	/**
	 * @return the publicationTypeID
	 */
	

	/**
	 * @param titleEN the titleEN to set
	 */
	public void setTitleEN(String titleEN) {
		this.titleEN = titleEN;
	}

	/**
	 * @return the publicationType
	 */
	public PublicationTypeEntry getPublicationType() {
		return publicationType;
	}

	/**
	 * @param publicationType the publicationType to set
	 */
	public void setPublicationType(PublicationTypeEntry publicationType) {
		this.publicationType = publicationType;
	}

	/**
	 * @return the titleTR
	 */
	public String getTitleTR() {
		return titleTR;
	}

	/**
	 * @param titleTR the titleTR to set
	 */
	public void setTitleTR(String titleTR) {
		this.titleTR = titleTR;
	}

	/**
	 * @return the titleORG
	 */
	public String getTitleORG() {
		return titleORG;
	}

	/**
	 * @param titleORG the titleORG to set
	 */
	public void setTitleORG(String titleORG) {
		this.titleORG = titleORG;
	}

	/**
	 * @return the procTitleEN
	 */
	public String getProcTitleEN() {
		return procTitleEN;
	}

	/**
	 * @param procTitleEN the procTitleEN to set
	 */
	public void setProcTitleEN(String procTitleEN) {
		this.procTitleEN = procTitleEN;
	}

	/**
	 * @return the procTitleTR
	 */
	public String getProcTitleTR() {
		return procTitleTR;
	}

	/**
	 * @param procTitleTR the procTitleTR to set
	 */
	public void setProcTitleTR(String procTitleTR) {
		this.procTitleTR = procTitleTR;
	}

	/**
	 * @return the procTitleORG
	 */
	public String getProcTitleORG() {
		return procTitleORG;
	}

	/**
	 * @param procTitleORG the procTitleORG to set
	 */
	public void setProcTitleORG(String procTitleORG) {
		this.procTitleORG = procTitleORG;
	}

	/**
	 * @return the bookTitleEN
	 */
	public String getBookTitleEN() {
		return bookTitleEN;
	}

	/**
	 * @param bookTitleEN the bookTitleEN to set
	 */
	public void setBookTitleEN(String bookTitleEN) {
		this.bookTitleEN = bookTitleEN;
	}

	/**
	 * @return the bookTitleTR
	 */
	public String getBookTitleTR() {
		return bookTitleTR;
	}

	/**
	 * @param bookTitleTR the bookTitleTR to set
	 */
	public void setBookTitleTR(String bookTitleTR) {
		this.bookTitleTR = bookTitleTR;
	}

	/**
	 * @return the bookTitleORG
	 */
	public String getBookTitleORG() {
		return bookTitleORG;
	}

	/**
	 * @param bookTitleORG the bookTitleORG to set
	 */
	public void setBookTitleORG(String bookTitleORG) {
		this.bookTitleORG = bookTitleORG;
	}

	/**
	 * @return the chapTitleEN
	 */
	public String getChapTitleEN() {
		return chapTitleEN;
	}

	/**
	 * @param chapTitleEN the chapTitleEN to set
	 */
	public void setChapTitleEN(String chapTitleEN) {
		this.chapTitleEN = chapTitleEN;
	}

	/**
	 * @return the chapTitleTR
	 */
	public String getChapTitleTR() {
		return chapTitleTR;
	}

	/**
	 * @param chapTitleTR the chapTitleTR to set
	 */
	public void setChapTitleTR(String chapTitleTR) {
		this.chapTitleTR = chapTitleTR;
	}

	/**
	 * @return the chapTitleORG
	 */
	public String getChapTitleORG() {
		return chapTitleORG;
	}

	/**
	 * @param chapTitleORG the chapTitleORG to set
	 */
	public void setChapTitleORG(String chapTitleORG) {
		this.chapTitleORG = chapTitleORG;
	}

	/**
	 * @return the universityEN
	 */
	public String getUniversityEN() {
		return universityEN;
	}

	/**
	 * @param universityEN the universityEN to set
	 */
	public void setUniversityEN(String universityEN) {
		this.universityEN = universityEN;
	}

	/**
	 * @return the universityORG
	 */
	public String getUniversityORG() {
		return universityORG;
	}

	/**
	 * @param universityORG the universityORG to set
	 */
	public void setUniversityORG(String universityORG) {
		this.universityORG = universityORG;
	}

	/**
	 * @return the universityTR
	 */
	public String getUniversityTR() {
		return universityTR;
	}

	/**
	 * @param universityTR the universityTR to set
	 */
	public void setUniversityTR(String universityTR) {
		this.universityTR = universityTR;
	}

	/**
	 * @return the numberEN
	 */
	public String getNumberEN() {
		return numberEN;
	}

	/**
	 * @param numberEN the numberEN to set
	 */
	public void setNumberEN(String numberEN) {
		this.numberEN = numberEN;
	}

	/**
	 * @return the numberTR
	 */
	public String getNumberTR() {
		return numberTR;
	}

	/**
	 * @param numberTR the numberTR to set
	 */
	public void setNumberTR(String numberTR) {
		this.numberTR = numberTR;
	}

	/**
	 * @return the numberORG
	 */
	public String getNumberORG() {
		return numberORG;
	}

	/**
	 * @param numberORG the numberORG to set
	 */
	public void setNumberORG(String numberORG) {
		this.numberORG = numberORG;
	}

	/**
	 * @return the accessdateEN
	 */
	public String getAccessdateEN() {
		return accessdateEN;
	}

	/**
	 * @param accessdateEN the accessdateEN to set
	 */
	public void setAccessdateEN(String accessdateEN) {
		this.accessdateEN = accessdateEN;
	}

	/**
	 * @return the accessdateTR
	 */
	public String getAccessdateTR() {
		return accessdateTR;
	}

	/**
	 * @param accessdateTR the accessdateTR to set
	 */
	public void setAccessdateTR(String accessdateTR) {
		this.accessdateTR = accessdateTR;
	}

	/**
	 * @return the accessdateORG
	 */
	public String getAccessdateORG() {
		return accessdateORG;
	}

	/**
	 * @param accessdateORG the accessdateORG to set
	 */
	public void setAccessdateORG(String accessdateORG) {
		this.accessdateORG = accessdateORG;
	}

	/**
	 * @return the titleaddonEN
	 */
	public String getTitleaddonEN() {
		return titleaddonEN;
	}

	/**
	 * @param titleaddonEN the titleaddonEN to set
	 */
	public void setTitleaddonEN(String titleaddonEN) {
		this.titleaddonEN = titleaddonEN;
	}

	/**
	 * @return the titleaddonORG
	 */
	public String getTitleaddonORG() {
		return titleaddonORG;
	}

	/**
	 * @param titleaddonORG the titleaddonORG to set
	 */
	public void setTitleaddonORG(String titleaddonORG) {
		this.titleaddonORG = titleaddonORG;
	}

	/**
	 * @return the titleaddonTR
	 */
	public String getTitleaddonTR() {
		return titleaddonTR;
	}

	/**
	 * @param titleaddonTR the titleaddonTR to set
	 */
	public void setTitleaddonTR(String titleaddonTR) {
		this.titleaddonTR = titleaddonTR;
	}

	/**
	 * @return the publisherID
	 */


	/**
	 * @return the serieEN
	 */
	public String getSerieEN() {
		return serieEN;
	}

	/**
	 * @return the publisher
	 */
	public PublisherEntry getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher the publisher to set
	 */
	public void setPublisher(PublisherEntry publisher) {
		this.publisher = publisher;
	}

	/**
	 * @param serieEN the serieEN to set
	 */
	public void setSerieEN(String serieEN) {
		this.serieEN = serieEN;
	}

	/**
	 * @return the serieTR
	 */
	public String getSerieTR() {
		return serieTR;
	}

	/**
	 * @param serieTR the serieTR to set
	 */
	public void setSerieTR(String serieTR) {
		this.serieTR = serieTR;
	}

	/**
	 * @return the serieORG
	 */
	public String getSerieORG() {
		return serieORG;
	}

	/**
	 * @param serieORG the serieORG to set
	 */
	public void setSerieORG(String serieORG) {
		this.serieORG = serieORG;
	}

	/**
	 * @return the editionEN
	 */
	public String getEditionEN() {
		return editionEN;
	}

	/**
	 * @param editionEN the editionEN to set
	 */
	public void setEditionEN(String editionEN) {
		this.editionEN = editionEN;
	}

	/**
	 * @return the editionORG
	 */
	public String getEditionORG() {
		return editionORG;
	}

	/**
	 * @param editionORG the editionORG to set
	 */
	public void setEditionORG(String editionORG) {
		this.editionORG = editionORG;
	}

	/**
	 * @return the editionTR
	 */
	public String getEditionTR() {
		return editionTR;
	}

	/**
	 * @param editionTR the editionTR to set
	 */
	public void setEditionTR(String editionTR) {
		this.editionTR = editionTR;
	}

	/**
	 * @return the volumeEN
	 */
	public String getVolumeEN() {
		return volumeEN;
	}

	/**
	 * @param volumeEN the volumeEN to set
	 */
	public void setVolumeEN(String volumeEN) {
		this.volumeEN = volumeEN;
	}

	/**
	 * @return the volumeTR
	 */
	public String getVolumeTR() {
		return volumeTR;
	}

	/**
	 * @param volumeTR the volumeTR to set
	 */
	public void setVolumeTR(String volumeTR) {
		this.volumeTR = volumeTR;
	}

	/**
	 * @return the volumeORG
	 */
	public String getVolumeORG() {
		return volumeORG;
	}

	/**
	 * @param volumeORG the volumeORG to set
	 */
	public void setVolumeORG(String volumeORG) {
		this.volumeORG = volumeORG;
	}

	/**
	 * @return the yearEN
	 */
	public int getYearEN() {
		return yearEN;
	}

	/**
	 * @param yearEN the yearEN to set
	 */
	public void setYearEN(int yearEN) {
		this.yearEN = yearEN;
	}

	/**
	 * @return the yearORG
	 */
	public String getYearORG() {
		return yearORG;
	}

	/**
	 * @param yearORG the yearORG to set
	 */
	public void setYearORG(String yearORG) {
		this.yearORG = yearORG;
	}

	/**
	 * @return the yearTR
	 */
	public String getYearTR() {
		return yearTR;
	}

	/**
	 * @param yearTR the yearTR to set
	 */
	public void setYearTR(String yearTR) {
		this.yearTR = yearTR;
	}

	/**
	 * @return the monthEN
	 */
	public String getMonthEN() {
		return monthEN;
	}

	/**
	 * @param monthEN the monthEN to set
	 */
	public void setMonthEN(String monthEN) {
		this.monthEN = monthEN;
	}

	/**
	 * @return the monthTR
	 */
	public String getMonthTR() {
		return monthTR;
	}

	/**
	 * @param monthTR the monthTR to set
	 */
	public void setMonthTR(String monthTR) {
		this.monthTR = monthTR;
	}

	/**
	 * @return the monthORG
	 */
	public String getMonthORG() {
		return monthORG;
	}

	/**
	 * @param monthORG the monthORG to set
	 */
	public void setMonthORG(String monthORG) {
		this.monthORG = monthORG;
	}

	/**
	 * @return the pagesEN
	 */
	public String getPagesEN() {
		return pagesEN;
	}

	/**
	 * @param pagesEN the pagesEN to set
	 */
	public void setPagesEN(String pagesEN) {
		this.pagesEN = pagesEN;
	}

	/**
	 * @return the pagesORG
	 */
	public String getPagesORG() {
		return pagesORG;
	}

	/**
	 * @param pagesORG the pagesORG to set
	 */
	public void setPagesORG(String pagesORG) {
		this.pagesORG = pagesORG;
	}

	/**
	 * @return the pagesTR
	 */
	public String getPagesTR() {
		return pagesTR;
	}

	/**
	 * @param pagesTR the pagesTR to set
	 */
	public void setPagesTR(String pagesTR) {
		this.pagesTR = pagesTR;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the notes
	 */
	public String getNotes() {
		return notes;
	}

	/**
	 * @param notes the notes to set
	 */
	public void setNotes(String notes) {
		this.notes = notes;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * @param uri the uri to set
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * @return the unpublished
	 */
	public boolean isUnpublished() {
		return unpublished;
	}

	/**
	 * @param unpublished the unpublished to set
	 */
	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}

	/**
	 * @return the erstauflage
	 */
	public boolean isErstauflage() {
		return erstauflage;
	}

	/**
	 * @param erstauflage the erstauflage to set
	 */
	public void setErstauflage(boolean erstauflage) {
		this.erstauflage = erstauflage;
	}

	/**
	 * @return the erstauflageID
	 */


	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @return the erstauflageEntry
	 */
	public AnnotatedBiblographyEntry getErstauflageEntry() {
		return erstauflageEntry;
	}

	/**
	 * @param erstauflageEntry the erstauflageEntry to set
	 */
	public void setErstauflageEntry(AnnotatedBiblographyEntry erstauflageEntry) {
		this.erstauflageEntry = erstauflageEntry;
	}

	/**
	 * @return the annotatedBiblographyID
	 */
	public int getAnnotatedBiblographyID() {
		return annotatedBiblographyID;
	}

	/**
	 * @param annotatedBiblographyID the annotatedBiblographyID to set
	 */
	public void setAnnotatedBiblographyID(int annotatedBiblographyID) {
		this.annotatedBiblographyID = annotatedBiblographyID;
	}

	/**
	 * @return the authorAnnotatedList
	 */
	public ArrayList<AuthorAnnotatedRelation> getAuthorAnnotatedList() {
		return authorAnnotatedList;
	}

	/**
	 * @param authorAnnotatedList the authorAnnotatedList to set
	 */
	public void setAuthorAnnotatedList(ArrayList<AuthorAnnotatedRelation> authorAnnotatedList) {
		this.authorAnnotatedList = authorAnnotatedList;
	}

	/**
	 * @return the editorAnnotatedList
	 */
	public ArrayList<EditorAnnotatedRelation> getEditorAnnotatedList() {
		return editorAnnotatedList;
	}

	/**
	 * @param editorAnnotatedList the editorAnnotatedList to set
	 */
	public void setEditorAnnotatedList(ArrayList<EditorAnnotatedRelation> editorAnnotatedList) {
		this.editorAnnotatedList = editorAnnotatedList;
	}
	
	

}
