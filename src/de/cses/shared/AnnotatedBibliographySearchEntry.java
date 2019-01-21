package de.cses.shared;

import com.google.gwt.user.client.Cookies;

import de.cses.client.Util;
import de.cses.client.user.UserLogin;

public class AnnotatedBibliographySearchEntry extends AbstractSearchEntry {
	
	private String titleSearch = "";
	private String authorSearch = "";
	private String publisherSearch = "";
	private int yearSearch = 0;
	
	public AnnotatedBibliographySearchEntry(boolean orSearch, String sessionID) {
		super(orSearch, sessionID);
	}

	public AnnotatedBibliographySearchEntry(String sessionID) {
		super(sessionID);
	}

	public AnnotatedBibliographySearchEntry() {	}

	public String getTitleSearch() {
		return titleSearch;
	}

	public void setTitleSearch(String titleSearch) {
		this.titleSearch = titleSearch;
	}

	public String getAuthorSearch() {
		return authorSearch;
	}

	public void setAuthorSearch(String authorSearch) {
		this.authorSearch = authorSearch;
	}

	public String getPublisherSearch() {
		return publisherSearch;
	}

	public void setPublisherSearch(String publisherSearch) {
		this.publisherSearch = publisherSearch;
	}

	public int getYearSearch() {
		return yearSearch;
	}

	public void setYearSearch(int yearSearch) {
		this.yearSearch = yearSearch;
	}

}
