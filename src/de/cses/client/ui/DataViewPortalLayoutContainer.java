package de.cses.client.ui;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

import de.cses.client.MainView;
import de.cses.client.Util;
import de.cses.client.caves.CaveDataDisplay;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;

public class DataViewPortalLayoutContainer extends PortalLayoutContainer {

	public DataViewPortalLayoutContainer(int numColumns) {
		super(numColumns);
    setSpacing(10);
    setColumnWidth(0, 1.00);
	}
	
	public void drop(AbstractEntry entry) {
		if (containsEntry(entry)) {
			return;
		}
		if (entry instanceof CaveEntry) {
			CaveDataDisplay cdd = new CaveDataDisplay((CaveEntry) entry);
			add(cdd, 0);
		} else if (entry instanceof DepictionEntry) {
			DepictionDataDisplay ddd = new DepictionDataDisplay((DepictionEntry) entry);
			add(ddd, 0);
		} else if (entry instanceof ImageEntry) {
//			addResult(new ImageView((ImageEntry) event.getData()));
		} else if (entry instanceof OrnamentEntry) {
//			addResult(new OrnamenticView((OrnamentEntry) event.getData()));
		} else if (entry instanceof AnnotatedBibliographyEntry) {
//			addResult(new AnnotatedBiblographyView((AnnotatedBibliographyEntry) event.getData()));
		}
	}
	
	private boolean containsEntry(AbstractEntry entry) {
		for (int i=0; i<getColumnCount(); ++i) {
			Iterator<Widget> wi = getWidget(i).iterator();
			while (wi.hasNext()) {
				Widget w = wi.next();
				if (w instanceof AbstractDataDisplay) {
					AbstractEntry e = ((AbstractDataDisplay)w).getEntry();
					if (e.getUniqueID().equals(entry.getUniqueID())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void load() {
		
	}
	
	public void save() {
		for (int i=0; i<getColumnCount(); ++i) {
			Iterator<Widget> wi = getWidget(i).iterator();
			while (wi.hasNext()) {
				Widget w = wi.next();
				if (w instanceof AbstractDataDisplay) {
					AbstractEntry e = ((AbstractDataDisplay)w).getEntry();
					Util.doLogging(e.getUniqueID());
				}
			}
		}
	}

}
