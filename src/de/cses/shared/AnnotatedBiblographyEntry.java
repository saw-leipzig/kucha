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

	private int annotatedBiblographyID = 0;
	private int publicationTypeID = 0;
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
	private PublisherEntry publisher = new PublisherEntry();
	private String seriesEN;
	private String seriesTR;
	private String seriesORG;
	private String editionEN;
	private String editionORG;
	private String editionTR;
	private String volumeEN;
	private String volumeTR;
	private String volumeORG;
	private int yearEN = 0;
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
	private boolean unpublished = false;
	private int firstEditionBibID = 0;
	private ArrayList<AuthorEntry> authorList = new ArrayList<AuthorEntry>();
	private ArrayList<AuthorEntry> editorList = new ArrayList<AuthorEntry>();

	public AnnotatedBiblographyEntry(int annotatedBiblographyID, int publicationTypeID, String titleEN, String titleTR, String titleORG,
			String procTitleEN, String procTitleTR, String procTitleORG, String bookTitleEN, String bookTitleTR, String bookTitleORG,
			String chapTitleEN, String chapTitleTR, String chapTitleORG, String universityEN, String universityORG, String universityTR,
			String numberEN, String numberTR, String numberORG, String accessdateEN, String accessdateTR, String accessdateORG,
			String titleaddonEN, String titleaddonORG, String titleaddonTR, PublisherEntry publisher, String seriesEN, String seriesTR, String seriesORG,
			String editionEN, String editionORG, String editionTR, String volumeEN, String volumeTR, String volumeORG, int yearEN, String yearORG,
			String yearTR, String monthEN, String monthTR, String monthORG, String pagesEN, String pagesORG, String pagesTR, String comments,
			String notes, String url, String uri, boolean unpublished, int firstEditionBibID) {
		super();
		this.annotatedBiblographyID = annotatedBiblographyID;
		this.publicationTypeID = publicationTypeID;
		this.titleEN = titleEN;
		this.titleTR = titleTR;
		this.titleORG = titleORG;
		this.procTitleEN = procTitleEN;
		this.procTitleTR = procTitleTR;
		this.procTitleORG = procTitleORG;
		this.bookTitleEN = bookTitleEN;
		this.bookTitleTR = bookTitleTR;
		this.bookTitleORG = bookTitleORG;
		this.chapTitleEN = chapTitleEN;
		this.chapTitleTR = chapTitleTR;
		this.chapTitleORG = chapTitleORG;
		this.universityEN = universityEN;
		this.universityORG = universityORG;
		this.universityTR = universityTR;
		this.numberEN = numberEN;
		this.numberTR = numberTR;
		this.numberORG = numberORG;
		this.accessdateEN = accessdateEN;
		this.accessdateTR = accessdateTR;
		this.accessdateORG = accessdateORG;
		this.titleaddonEN = titleaddonEN;
		this.titleaddonORG = titleaddonORG;
		this.titleaddonTR = titleaddonTR;
		this.publisher = publisher;
		this.seriesEN = seriesEN;
		this.seriesTR = seriesTR;
		this.seriesORG = seriesORG;
		this.editionEN = editionEN;
		this.editionORG = editionORG;
		this.editionTR = editionTR;
		this.volumeEN = volumeEN;
		this.volumeTR = volumeTR;
		this.volumeORG = volumeORG;
		this.yearEN = yearEN;
		this.yearORG = yearORG;
		this.yearTR = yearTR;
		this.monthEN = monthEN;
		this.monthTR = monthTR;
		this.monthORG = monthORG;
		this.pagesEN = pagesEN;
		this.pagesORG = pagesORG;
		this.pagesTR = pagesTR;
		this.comments = comments;
		this.notes = notes;
		this.url = url;
		this.uri = uri;
		this.unpublished = unpublished;
		this.firstEditionBibID = firstEditionBibID;
	}

	public AnnotatedBiblographyEntry() { }

	public AnnotatedBiblographyEntry clone() {
		AnnotatedBiblographyEntry clonedEntry = new AnnotatedBiblographyEntry(annotatedBiblographyID, publicationTypeID, titleEN, titleTR,
				titleORG, procTitleEN, procTitleTR, procTitleORG, bookTitleEN, bookTitleTR, bookTitleORG, chapTitleEN, chapTitleTR, chapTitleORG,
				universityEN, universityORG, universityTR, numberEN, numberTR, numberORG, accessdateEN, accessdateTR, accessdateORG, titleaddonEN,
				titleaddonORG, titleaddonTR, publisher, seriesEN, seriesTR, seriesORG, editionEN, editionORG, editionTR, volumeEN, volumeTR, volumeORG,
				yearEN, yearORG, yearTR, monthEN, monthTR, monthORG, pagesEN, pagesORG, pagesTR, comments, notes, url, uri, unpublished,
				firstEditionBibID);
		ArrayList<AuthorEntry> clonedAuthorList = new ArrayList<AuthorEntry>();
		for (AuthorEntry ae : this.authorList) {
			clonedAuthorList.add(ae);
		}
		clonedEntry.setAuthorList(clonedAuthorList);
		ArrayList<AuthorEntry> clonedEditorList = new ArrayList<AuthorEntry>();
		for (AuthorEntry ae : this.getEditorList()) {
			clonedEditorList.add(ae);
		}
		clonedEntry.setEditorList(clonedEditorList);
		return clonedEntry;
	}

	public String getTitleEN() {
		return titleEN;
	}

	/**
	 * @param titleEN
	 *          the titleEN to set
	 */
	public void setTitleEN(String titleEN) {
		this.titleEN = titleEN;
	}

	/**
	 * @return the publicationType
	 */
	public int getPublicationTypeID() {
		return publicationTypeID;
	}

	/**
	 * @param publicationType
	 *          the publicationType to set
	 */
	public void setPublicationTypeID(int publicationTypeID) {
		this.publicationTypeID = publicationTypeID;
	}

	/**
	 * @return the titleTR
	 */
	public String getTitleTR() {
		return titleTR;
	}

	/**
	 * @param titleTR
	 *          the titleTR to set
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
	 * @param titleORG
	 *          the titleORG to set
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
	 * @param procTitleEN
	 *          the procTitleEN to set
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
	 * @param procTitleTR
	 *          the procTitleTR to set
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
	 * @param procTitleORG
	 *          the procTitleORG to set
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
	 * @param bookTitleEN
	 *          the bookTitleEN to set
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
	 * @param bookTitleTR
	 *          the bookTitleTR to set
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
	 * @param bookTitleORG
	 *          the bookTitleORG to set
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
	 * @param chapTitleEN
	 *          the chapTitleEN to set
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
	 * @param chapTitleTR
	 *          the chapTitleTR to set
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
	 * @param chapTitleORG
	 *          the chapTitleORG to set
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
	 * @param universityEN
	 *          the universityEN to set
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
	 * @param universityORG
	 *          the universityORG to set
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
	 * @param universityTR
	 *          the universityTR to set
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
	 * @param numberEN
	 *          the numberEN to set
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
	 * @param numberTR
	 *          the numberTR to set
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
	 * @param numberORG
	 *          the numberORG to set
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
	 * @param accessdateEN
	 *          the accessdateEN to set
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
	 * @param accessdateTR
	 *          the accessdateTR to set
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
	 * @param accessdateORG
	 *          the accessdateORG to set
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
	 * @param titleaddonEN
	 *          the titleaddonEN to set
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
	 * @param titleaddonORG
	 *          the titleaddonORG to set
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
	 * @param titleaddonTR
	 *          the titleaddonTR to set
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
	public String getSeriesEN() {
		return seriesEN;
	}

	/**
	 * @return the publisher
	 */
	public PublisherEntry getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *          the publisher to set
	 */
	public void setPublisher(PublisherEntry publisher) {
		this.publisher = publisher;
	}

	/**
	 * @param serieEN
	 *          the serieEN to set
	 */
	public void setSeriesEN(String seriesEN) {
		this.seriesEN = seriesEN;
	}

	/**
	 * @return the serieTR
	 */
	public String getSeriesTR() {
		return seriesTR;
	}

	/**
	 * @param serieTR
	 *          the serieTR to set
	 */
	public void setSeriesTR(String seriesTR) {
		this.seriesTR = seriesTR;
	}

	/**
	 * @return the serieORG
	 */
	public String getSeriesORG() {
		return seriesORG;
	}

	/**
	 * @param serieORG
	 *          the serieORG to set
	 */
	public void setSeriesORG(String seriesORG) {
		this.seriesORG = seriesORG;
	}

	/**
	 * @return the editionEN
	 */
	public String getEditionEN() {
		return editionEN;
	}

	/**
	 * @param editionEN
	 *          the editionEN to set
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
	 * @param editionORG
	 *          the editionORG to set
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
	 * @param editionTR
	 *          the editionTR to set
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
	 * @param volumeEN
	 *          the volumeEN to set
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
	 * @param volumeTR
	 *          the volumeTR to set
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
	 * @param volumeORG
	 *          the volumeORG to set
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
	 * @param yearEN
	 *          the yearEN to set
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
	 * @param yearORG
	 *          the yearORG to set
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
	 * @param yearTR
	 *          the yearTR to set
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
	 * @param monthEN
	 *          the monthEN to set
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
	 * @param monthTR
	 *          the monthTR to set
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
	 * @param monthORG
	 *          the monthORG to set
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
	 * @param pagesEN
	 *          the pagesEN to set
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
	 * @param pagesORG
	 *          the pagesORG to set
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
	 * @param pagesTR
	 *          the pagesTR to set
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
	 * @param comments
	 *          the comments to set
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
	 * @param notes
	 *          the notes to set
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
	 * @param url
	 *          the url to set
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
	 * @param uri
	 *          the uri to set
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
	 * @param unpublished
	 *          the unpublished to set
	 */
	public void setUnpublished(boolean unpublished) {
		this.unpublished = unpublished;
	}

	@Override
	public String getUniqueID() {
		return "AnnotatedBibliography-" + annotatedBiblographyID;
	}

	/**
	 * @return the annotatedBiblographyID
	 */
	public int getAnnotatedBiblographyID() {
		return annotatedBiblographyID;
	}

	/**
	 * @param annotatedBiblographyID
	 *          the annotatedBiblographyID to set
	 */
	public void setAnnotatedBiblographyID(int annotatedBiblographyID) {
		this.annotatedBiblographyID = annotatedBiblographyID;
	}

	/**
	 * @return the authorAnnotatedList
	 */
	public ArrayList<AuthorEntry> getAuthorList() {
		return authorList;
	}

	/**
	 * @param authorAnnotatedList
	 *          the authorAnnotatedList to set
	 */
	public void setAuthorList(ArrayList<AuthorEntry> authorList) {
		this.authorList = authorList;
	}

	/**
	 * @return the editorAnnotatedList
	 */
	public ArrayList<AuthorEntry> getEditorList() {
		return editorList;
	}

	/**
	 * @param editorAnnotatedList
	 *          the editorAnnotatedList to set
	 */
	public void setEditorList(ArrayList<AuthorEntry> editorList) {
		this.editorList = editorList;
	}

	public int getFirstEditionBibID() {
		return firstEditionBibID;
	}

	public void setFirstEditionBibID(int firstEditionBibID) {
		this.firstEditionBibID = firstEditionBibID;
	}

	public String getLabel() {
		String result = null;
		for (AuthorEntry author : authorList) {
			if (result == null) {
				result = author.getName();
			} else {
				result = result.concat("; " + author.getName());
			}
		}
		result = result.concat(" (" + yearEN + "). " +  titleEN + ". " + publisher.getLabel());
		return result;
	}
}
