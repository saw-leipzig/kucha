package de.cses.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.caves.CaveDataDisplay;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;

public class DataViewPortalLayoutContainer extends PortalLayoutContainer {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

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
			add(new CaveDataDisplay((CaveEntry) entry), 0);
		} else if (entry instanceof DepictionEntry) {
			add(new DepictionDataDisplay((DepictionEntry) entry), 0);
		} else if (entry instanceof ImageEntry) {
			// TODO
		} else if (entry instanceof OrnamentEntry) {
			// TODO
		} else if (entry instanceof AnnotatedBibliographyEntry) {
			// TODO
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
		ArrayList<AbstractEntry> entryList = new ArrayList<AbstractEntry>();
		for (int i=0; i<getColumnCount(); ++i) {
			Iterator<Widget> wi = getWidget(i).iterator();
			while (wi.hasNext()) {
				Widget w = wi.next();
				if (w instanceof AbstractDataDisplay) {
					AbstractEntry e = ((AbstractDataDisplay)w).getEntry();
					Util.doLogging(e.getClass().getSimpleName() + ": " + e.getUniqueID());
					entryList.add(e);
				}
			}
		}
		if (!entryList.isEmpty()) {
			String collectionLabel = "test";
			dbService.saveCollectedEntries(UserLogin.getInstance().getSessionID() , collectionLabel, entryList, new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Boolean result) {
					// TODO Auto-generated method stub
					
				}
			});
		}
	}

}
