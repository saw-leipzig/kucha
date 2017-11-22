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
 * @author Nina
 *
 */
public class AuthorAnnotatedRelation extends AbstractEntry{
	private int AuthorAnnotatedRelationID;
	private AuthorEntry author;
	private AnnotatedBiblographyEntry bib;

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		// TODO Auto-generated method stub
		return null;
	}
	public AuthorAnnotatedRelation(){
		
	}

	/**
	 * @return the author
	 */
	public AuthorEntry getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(AuthorEntry author) {
		this.author = author;
	}

	/**
	 * @return the bib
	 */
	public AnnotatedBiblographyEntry getBib() {
		return bib;
	}

	/**
	 * @param bib the bib to set
	 */
	public void setBib(AnnotatedBiblographyEntry bib) {
		this.bib = bib;
	}

	/**
	 * @return the authorAnnotatedRelationID
	 */
	public int getAuthorAnnotatedRelationID() {
		return AuthorAnnotatedRelationID;
	}

	/**
	 * @param authorAnnotatedRelationID the authorAnnotatedRelationID to set
	 */
	public void setAuthorAnnotatedRelationID(int authorAnnotatedRelationID) {
		AuthorAnnotatedRelationID = authorAnnotatedRelationID;
	}
	
	

}
