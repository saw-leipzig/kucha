/*
 * Copyright 2016 - 2018
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
import java.util.Comparator;

import de.cses.client.Util;

/**
 * @author alingnau
 *
 */
public class CommentEntry extends AbstractEntry implements Comparable<CommentEntry> {

	private String commentID = "";
	private String comment, author;
	private ArrayList<CommentEntry> comments;
	/**
	 * The default constructor is used to create a new AuthorEntry. The authorID
	 * is set to 0 to indicate that this entry is not taken from a database and
	 * therefore has to be inserted instead of updated.
	 */
	public CommentEntry() {	}

	public CommentEntry(String commentID, String comment, String author, ArrayList<CommentEntry> comments) {
		this.commentID = commentID;
		this.comment = comment;
		this.author = author;
		this.comments = comments;
	}
	
	public String getCommentID() {
		return commentID;
	}

	public void setCommentID(String commentID) {
		this.commentID = commentID;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public ArrayList<CommentEntry> getComments() {
		return comments;
	}

	public void setComments(ArrayList<CommentEntry> comments) {
		this.comments = comments;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "Comment-" + commentID;
	}

	 
    @Override
    public int compareTo(CommentEntry ae)
    {
    	return getCommentID().compareTo(ae.getCommentID());
    }

}
