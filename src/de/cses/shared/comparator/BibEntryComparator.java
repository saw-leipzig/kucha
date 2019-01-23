package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.AuthorEntry;

public class BibEntryComparator implements Comparator<AnnotatedBibliographyEntry> {

	public BibEntryComparator() {
		// TODO Auto-generated constructor stub
	}

	String getComparisonString(AnnotatedBibliographyEntry entry) {
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
			result += entry.getYearORG() != null ? entry.getYearORG() + " " : "";
			result += entry.getTitleFull();
		}
		result = result.replace("ä", "ae").replace("ö", "oe").replace("ü", "ue");
		result = result.replace("Ä", "Ae").replace("Ö", "Oe").replace("Ü", "Ue");
		return result;
	}

	@Override
	public int compare(AnnotatedBibliographyEntry entry1, AnnotatedBibliographyEntry entry2) {
		return getComparisonString(entry1).compareTo(getComparisonString(entry2));
	}

}
