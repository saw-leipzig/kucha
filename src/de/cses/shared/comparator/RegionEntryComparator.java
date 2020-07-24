package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.client.StaticTables;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

public class RegionEntryComparator implements Comparator<RegionEntry> {

	private StaticTables st = StaticTables.getInstance();

	public RegionEntryComparator() { }

	@Override
	public int compare(RegionEntry re1, RegionEntry re2) {
		SiteEntry se1 = re1.getSiteID() > 0 ? st.getSiteEntries().get(re1.getSiteID()) : null;
		SiteEntry se2 = re2.getSiteID() > 0 ? st.getSiteEntries().get(re2.getSiteID()) : null;
		String comp1 = se1 != null ? se1.getName() + " " + re1.getEnglishName() : re1.getEnglishName();
		String comp2 = se2 != null ? se2.getName() + " " + re2.getEnglishName() : re2.getEnglishName();
		return comp1.toLowerCase().compareTo(comp2.toLowerCase());
	}
}