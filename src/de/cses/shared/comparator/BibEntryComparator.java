package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.shared.AnnotatedBiblographyEntry;
import de.cses.shared.AuthorEntry;

public class BibEntryComparator implements Comparator<AnnotatedBiblographyEntry> {

	public BibEntryComparator() {
		// TODO Auto-generated constructor stub
	}

	String getComparisonString(AnnotatedBiblographyEntry entry) {
		String result = "";
		if (entry != null) {
			if (!entry.getAuthorList().isEmpty()) {
				for (AuthorEntry ae : entry.getAuthorList()) {
					result += ae.getLastname() + " ";
				}
			} else if (!entry.getEditorList().isEmpty()) {
				for (AuthorEntry ae : entry.getEditorList()) {
					result += ae.getLastname() + " ";
				}
			}
			result += entry.getYearORG() != null ? entry.getYearORG() : "";
		}
		return result;
	}

	@Override
	public int compare(AnnotatedBiblographyEntry entry1, AnnotatedBiblographyEntry entry2) {
		return getComparisonString(entry1).compareTo(getComparisonString(entry2));
	}

}
