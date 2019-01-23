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

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * @author Nina
 *
 */
public class AnnotatedBibliographyEntry extends AbstractEntry implements Comparable<AnnotatedBibliographyEntry> {

	private int annotatedBiblographyID = 0;
	private PublicationTypeEntry publicationType = null;
	private String titleEN="", titleTR="", titleORG="";
	private String subtitleEN="", subtitleTR="", subtitleORG="";
	private String parentTitleEN="", parentTitleTR="", parentTitleORG="";
	private String universityEN="", universityORG="", universityTR="";
	private String numberEN="", numberTR="", numberORG="";
	private String accessdateEN="", accessdateTR="", accessdateORG="";
	private String titleaddonEN="", titleaddonORG="", titleaddonTR="";
	private String publisher="";
	private String seriesEN="", seriesTR="", seriesORG="";
	private String editionEN="", editionORG="", editionTR="";
	private String volumeEN="", volumeTR="", volumeORG="";
	private String issueEN="", issueTR="", issueORG="";
	private int yearEN;
	private String yearORG="", yearTR="";
	private String monthEN="", monthTR="", monthORG="";
	private String pagesEN="", pagesORG="", pagesTR="";
	private String comments="";
	private String notes="";
	private String url="";
	private String uri="";
	private boolean unpublished = false;
	private int firstEditionBibID = 0;
	private String abstractText="";
	private String thesisType="";
	private String editorType="";
	private boolean officialTitleTranslation = false;
	private String bibtexKey="";
	private ArrayList<AuthorEntry> authorList = new ArrayList<AuthorEntry>();
	private ArrayList<AuthorEntry> editorList = new ArrayList<AuthorEntry>();
	private ArrayList<BibKeywordEntry> keywordList = new ArrayList<BibKeywordEntry>();

	public AnnotatedBibliographyEntry(int annotatedBiblographyID, PublicationTypeEntry publicationType, 
			String titleEN, String titleORG, String titleTR,
			String parentTitleEN, String parentTitleORG, String parentTitleTR,
			String subtitleEN, String subtitleORG, String subtitleTR,
			String universityEN, String universityORG, String universityTR,
			String numberEN, String numberORG, String numberTR, 
			String accessdateEN, String accessdateORG, String accessdateTR,
			String titleaddonEN, String titleaddonORG, String titleaddonTR, 
			String publisher, 
			String seriesEN, String seriesORG, String seriesTR,
			String editionEN, String editionORG, String editionTR, 
			String volumeEN, String volumeORG, String volumeTR, 
			String issueEN, String issueORG, String issueTR,
			int yearEN, String yearORG, String yearTR, 
			String monthEN, String monthORG, String monthTR, 
			String pagesEN, String pagesORG, String pagesTR, 
			String comments, String notes, String url, String uri, boolean unpublished, int firstEditionBibID, 
			int accessLevel, String abstractText, String thesisType, String editorType, boolean officialTitleTranslation,
			String bibtexKey, String lastChangedOn) {
		super();
		this.annotatedBiblographyID = annotatedBiblographyID;
		this.publicationType = publicationType;
		this.titleEN = titleEN;
		this.titleTR = titleTR;
		this.titleORG = titleORG;
		this.parentTitleEN = parentTitleEN;
		this.parentTitleTR = parentTitleTR;
		this.parentTitleORG = parentTitleORG;
		this.subtitleEN = subtitleEN;
		this.subtitleORG = subtitleORG;
		this.subtitleTR = subtitleTR;
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
		this.issueEN = issueEN;
		this.issueTR = issueTR;
		this.issueORG = issueORG;
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
		this.accessLevel = accessLevel;
		this.abstractText = abstractText;
		this.thesisType = thesisType;
		this.editorType = editorType;
		this.officialTitleTranslation = officialTitleTranslation;
		this.bibtexKey = bibtexKey;
		this.setModifiedOn(lastChangedOn);
	}

	public AnnotatedBibliographyEntry() { }

	public AnnotatedBibliographyEntry clone() {
		AnnotatedBibliographyEntry clonedEntry = new AnnotatedBibliographyEntry(annotatedBiblographyID, publicationType, 
				titleEN, titleORG, titleTR,
				parentTitleEN, parentTitleORG, parentTitleTR,
				subtitleEN, subtitleORG, subtitleTR,
				universityEN, universityORG, universityTR, 
				numberEN, numberORG, numberTR, 
				accessdateEN, accessdateORG, accessdateTR, 
				titleaddonEN, titleaddonORG, titleaddonTR, 
				publisher, 
				seriesEN, seriesORG, seriesTR,
				editionEN, editionORG, editionTR, 
				volumeEN, volumeORG, volumeTR,
				issueEN, issueORG, issueTR,  
				yearEN, yearORG, yearTR, 
				monthEN, monthORG, monthTR,  
				pagesEN, pagesORG, pagesTR, 
				comments, notes, url, uri, unpublished, firstEditionBibID, accessLevel, 
				abstractText, thesisType, editorType, officialTitleTranslation, bibtexKey, this.modifiedOn);
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
		ArrayList<BibKeywordEntry> clonedKeywordList = new ArrayList<BibKeywordEntry>();
		for (BibKeywordEntry bke : this.getKeywordList()) {
			clonedKeywordList.add(bke);
		}
		clonedEntry.setKeywordList(clonedKeywordList);
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
	public PublicationTypeEntry getPublicationType() {
		return publicationType;
	}

	/**
	 * @param publicationType
	 *          the publicationType to set
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
	 * @return the serieEN
	 */
	public String getSeriesEN() {
		return seriesEN;
	}

	/**
	 * @return the publisher
	 */
	public String getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher
	 *          the publisher to set
	 */
	public void setPublisher(String publisher) {
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
		return "AnnotatedBibliography." + annotatedBiblographyID;
	}
	
	/**
	 * @return the annotatedBiblographyID
	 */
	public int getAnnotatedBibliographyID() {
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
		if (publicationType.isAuthorEnabled()) {
			result = getAuthors() + ". ";
		} else {
			result = getEditors();
			if (!editorType.isEmpty()) {
				result += " (" + editorType + "). ";
			}
		}
		if (!titleORG.isEmpty()) {
			result += titleORG + ". ";
		}
		if (!publisher.isEmpty()) {
			result += publisher + ". ";
		}
		if (yearEN > 0) {
			result += yearEN + ". ";
		}
		return result;
	}

	public String getIssueEN() {
		return issueEN;
	}

	public void setIssueEN(String issueEN) {
		this.issueEN = issueEN;
	}

	public String getIssueTR() {
		return issueTR;
	}

	public void setIssueTR(String issueTR) {
		this.issueTR = issueTR;
	}

	public String getIssueORG() {
		return issueORG;
	}

	public void setIssueORG(String issueORG) {
		this.issueORG = issueORG;
	}

	public String getAbstractText() {
		return abstractText;
	}

	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}

	public String getParentTitleEN() {
		return parentTitleEN;
	}

	public void setParentTitleEN(String parentTitleEN) {
		this.parentTitleEN = parentTitleEN;
	}

	public String getParentTitleTR() {
		return parentTitleTR;
	}

	public void setParentTitleTR(String parentTitleTR) {
		this.parentTitleTR = parentTitleTR;
	}

	public String getParentTitleORG() {
		return parentTitleORG;
	}

	public void setParentTitleORG(String parentTitleORG) {
		this.parentTitleORG = parentTitleORG;
	}
	
	/**
	 * 
	 * @return the authors as a string of names using et al. if more than 3 authors
	 */
	public String getAuthors() {
		String result = "";
		if (authorList.size() > 3) {
			AuthorEntry ae = authorList.get(0);
			result = ae.getName() + ", et al.";
		} else {
			for (AuthorEntry ae : authorList) {
				result = result.concat(!result.isEmpty() ? "; " + ae.getName() : ae.getName());
			}
		}
		return result;
	}

	/**
	 * 
	 * @return the editors as a string of names using et al. if more than 3 editors
	 */
	public String getEditors() {
		String result = "";
		if (editorList.size() > 3) {
			AuthorEntry ae = editorList.get(0);
			result = ae.getName() + ", et al.";
		} else {
			for (AuthorEntry ae : editorList) {
				result = result.concat(!result.isEmpty() ? "; " + ae.getName() : ae.getName());
			}
		}
		return result;
	}

	public String getThesisType() {
		return thesisType;
	}

	public void setThesisType(String thesisType) {
		this.thesisType = thesisType;
	}

	public ArrayList<BibKeywordEntry> getKeywordList() {
		return keywordList;
	}

	public void setKeywordList(ArrayList<BibKeywordEntry> keywordList) {
		this.keywordList = keywordList;
	}

	public String getEditorType() {
		return editorType;
	}

	public void setEditorType(String editorType) {
		this.editorType = editorType;
	}

	public String getSubtitleORG() {
		return subtitleORG;
	}

	public void setSubtitleORG(String subtitleORG) {
		this.subtitleORG = subtitleORG;
	}

	public String getSubtitleEN() {
		return subtitleEN;
	}

	public void setSubtitleEN(String subtitleEN) {
		this.subtitleEN = subtitleEN;
	}

	public String getSubtitleTR() {
		return subtitleTR;
	}

	public void setSubtitleTR(String subtitleTR) {
		this.subtitleTR = subtitleTR;
	}

	public boolean isOfficialTitleTranslation() {
		return officialTitleTranslation;
	}

	public void setOfficialTitleTranslation(boolean officialTitleTranslation) {
		this.officialTitleTranslation = officialTitleTranslation;
	}
	
	public String getTitleFull() {
		String result = subtitleORG.isEmpty() ? titleORG : titleORG + ": " + subtitleORG;
		if (!titleEN.isEmpty()) {
			String translation = subtitleEN.isEmpty() ? titleEN : titleEN + ": " + subtitleEN;
			result += officialTitleTranslation ? " (" + translation + ")" : " [" + translation + "]";
		}
		return result;
	}

	public String getBibtexKey() {
		return bibtexKey;
	}

	public void setBibtexKey(String bibtexKey) {
		this.bibtexKey = bibtexKey;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(AnnotatedBibliographyEntry bibEntry) {
		String toString = !bibEntry.authorList.isEmpty() ? bibEntry.getAuthors() : (!bibEntry.getEditorList().isEmpty() ? bibEntry.getEditors(): "");
		String fromString = !authorList.isEmpty() ? getAuthors() : (!getEditorList().isEmpty() ? getEditors(): "");
		return fromString.compareTo(toString);
	}

}
