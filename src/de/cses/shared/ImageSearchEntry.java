package de.cses.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.Cookies;

import de.cses.client.user.UserLogin;

public class ImageSearchEntry extends AbstractSearchEntry {
	
	private String titleSearch = "";
	private String copyrightSearch = "";
	private String commentSearch = "";
	private int daysSinceUploadSearch = 0;
	ArrayList<Integer> imageTypeIdList = new ArrayList<Integer>();
	ArrayList<Integer> imageIdList = new ArrayList<Integer>();
	ArrayList<Integer> caveIdList = new ArrayList<Integer>();
	ArrayList<Integer> bibIdList = new ArrayList<Integer>();
	

	public ImageSearchEntry(boolean orSearch, String sessionID) {
		super(orSearch, sessionID);
	}

	public ImageSearchEntry(String sessionID) {
		super(sessionID);
	}

	public ImageSearchEntry() { }

	public String getTitleSearch() {
		return titleSearch;
	}

	public void setTitleSearch(String titleSearch) {
		this.titleSearch = titleSearch;
	}
	public ArrayList<Integer> getCaveIdList() {
		return caveIdList;
	}

	public void setCaveIdList(ArrayList<Integer> caveIdList) {
		this.caveIdList = caveIdList;
	}
	public ArrayList<Integer> getBibIdList() {
		return bibIdList;
	}

	public void setBibIdList(ArrayList<Integer> bibIdList) {
		this.bibIdList = bibIdList;
	}

	public String getCopyrightSearch() {
		return copyrightSearch;
	}

	public void setCopyrightSearch(String copyrightSearch) {
		this.copyrightSearch = copyrightSearch;
	}

	public ArrayList<Integer> getImageTypeIdList() {
		return imageTypeIdList;
	}

	public void setImageTypeIdList(ArrayList<Integer> imageTypeIdList) {
		this.imageTypeIdList = imageTypeIdList;
	}
	public ArrayList<Integer> getImageIdList() {
		return imageIdList;
	}

	public void setImageIdList(ArrayList<Integer> imageIdList) {
		this.imageIdList = imageIdList;
	}

	public String getCommentSearch() {
		return commentSearch;
	}

	public void setCommentSearch(String commentSearch) {
		this.commentSearch = commentSearch;
	}

	public int getDaysSinceUploadSearch() {
		return daysSinceUploadSearch;
	}

	public void setDaysSinceUploadSearch(int daysSinceUploadSearch) {
		this.daysSinceUploadSearch = daysSinceUploadSearch;
	}

}
