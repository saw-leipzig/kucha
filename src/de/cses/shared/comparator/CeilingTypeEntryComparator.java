package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.shared.CeilingTypeEntry;

public class CeilingTypeEntryComparator implements Comparator<CeilingTypeEntry> {

	public CeilingTypeEntryComparator() { }

	@Override
	public int compare(CeilingTypeEntry ct1, CeilingTypeEntry ct2) {
		return ct1.getName().toLowerCase().compareTo(ct2.getName().toLowerCase());
	}

}
