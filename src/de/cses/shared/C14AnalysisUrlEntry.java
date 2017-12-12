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

/**
 * @author alingnau
 *
 */
public class C14AnalysisUrlEntry extends AbstractEntry {
	
	private int c14AnalysisUrlID;
	private String c14Url;
	private String c14ShortName;

	/**
	 * @param c14AnalysisUrlID
	 * @param c14Url
	 * @param c14ShortName
	 */
	public C14AnalysisUrlEntry(int c14AnalysisUrlID, String c14Url, String c14ShortName) {
		this.c14AnalysisUrlID = c14AnalysisUrlID;
		this.c14Url = c14Url;
		this.c14ShortName = c14ShortName;
	}

	/**
	 * 
	 */
	public C14AnalysisUrlEntry() { }

	/**
	 * 
	 */
	public C14AnalysisUrlEntry(String c14Url, String c14ShortName) {
		this(0, c14Url, c14ShortName);
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "C14AnalysisUrl" + c14AnalysisUrlID;
	}

	public int getC14AnalysisUrlID() {
		return c14AnalysisUrlID;
	}

	public void setC14AnalysisUrlID(int c14AnalysisUrlID) {
		this.c14AnalysisUrlID = c14AnalysisUrlID;
	}

	public String getC14Url() {
		return c14Url;
	}

	public void setC14Url(String c14Url) {
		this.c14Url = c14Url;
	}

	public String getC14ShortName() {
		return c14ShortName;
	}

	public void setC14ShortName(String c14ShortName) {
		this.c14ShortName = c14ShortName;
	}

}
