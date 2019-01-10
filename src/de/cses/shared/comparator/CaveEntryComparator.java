package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.client.StaticTables;
import de.cses.shared.CaveEntry;

public class CaveEntryComparator implements Comparator<CaveEntry> {

	public CaveEntryComparator() {
	}

	private String getComparisonLabel(CaveEntry ce) {
		StaticTables stab = StaticTables.getInstance();
		String shortName = stab.getSiteEntries().get(ce.getSiteID()).getShortName();
		int len = 0;
		while ((len < ce.getOfficialNumber().length()) && isInteger(ce.getOfficialNumber().substring(0, len + 1))) {
			++len;
		}
		switch (len) {
			case 1:
				return shortName + "  " + ce.getOfficialNumber();
			case 2:
				return shortName + " " + ce.getOfficialNumber();
			default:
				return shortName + ce.getOfficialNumber();
		}
	}

	private boolean isInteger(String str) {
		if (str == null) {
			return false;
		}
		int length = str.length();
		if (length == 0) {
			return false;
		}
		int i = 0;
		if (str.charAt(0) == '-') {
			if (length == 1) {
				return false;
			}
			i = 1;
		}
		for (; i < length; i++) {
			char c = str.charAt(i);
			if (c < '0' || c > '9') {
				return false;
			}
		}
		return true;
	}

	@Override
	public int compare(CaveEntry ce1, CaveEntry ce2) {
		return getComparisonLabel(ce1).compareTo(getComparisonLabel(ce2));
	}
}
