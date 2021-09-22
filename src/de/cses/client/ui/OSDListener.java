package de.cses.client.ui;

import java.util.ArrayList;

import de.cses.shared.AnnotationEntry;

public interface OSDListener {

	public int getDepictionID();
	public boolean isOrnament();
	public void setAnnotationsInParent(ArrayList<AnnotationEntry> entryList);
	public ArrayList<AnnotationEntry> getAnnotations();
	public void addAnnotation(AnnotationEntry ae);
}
