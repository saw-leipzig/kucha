package de.cses.shared.comparator;

import java.util.Comparator;

import de.cses.client.StaticTables;
import de.cses.shared.WallEntry;

public class WallEntryComparator implements Comparator<WallEntry> {

	public WallEntryComparator() {	}

	@Override
	public int compare(WallEntry we1, WallEntry we2) {
		return StaticTables.getInstance().getWallLocationEntries().get(we1.getWallLocationID()).getLabel()
				.compareTo(StaticTables.getInstance().getWallLocationEntries().get(we2.getWallLocationID()).getLabel());
	}
}
