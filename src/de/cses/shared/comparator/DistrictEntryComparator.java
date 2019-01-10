package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.client.StaticTables;
import de.cses.shared.DistrictEntry;
import de.cses.shared.SiteEntry;

public class DistrictEntryComparator implements Comparator<DistrictEntry> {

	StaticTables st = StaticTables.getInstance();

	public DistrictEntryComparator() {
	}

	@Override
	public int compare(DistrictEntry de1, DistrictEntry de2) {
		SiteEntry se1 = de1.getSiteID() > 0 ? st.getSiteEntries().get(de1.getSiteID()) : null;
		SiteEntry se2 = de2.getSiteID() > 0 ? st.getSiteEntries().get(de2.getSiteID()) : null;
		String comp1 = se1 != null ? se1.getName() + " " + de1.getName() : de1.getName();
		String comp2 = se2 != null ? se2.getName() + " " + de2.getName() : de2.getName();
		return comp1.toLowerCase().compareTo(comp2.toLowerCase());
	}

}
