package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveSketchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

public class CaveDataDisplay extends AbstractDataDisplay {
	
	class CaveSketchUri {
		
		String uri;

		/**
		 * @param caveSketch
		 */
		public CaveSketchUri(String uri) {
			this.uri = uri;
		}

		public String getUri() {
			return uri;
		}

		public void setUri(String uri) {
			this.uri = uri;
		}
	}
	
	private CaveEntry entry;
	private StaticTables stab = StaticTables.getInstance();

	public CaveDataDisplay(CaveEntry ce) {
		entry = ce;
		CaveViewTemplates view = GWT.create(CaveViewTemplates.class);
		SiteEntry se = stab.getSiteEntries().get(entry.getSiteID());
		DistrictEntry de = stab.getDistrictEntries().get(entry.getDistrictID());
		RegionEntry re = stab.getRegionEntries().get(entry.getRegionID());
		HTML htmlWidget = new HTML(view.display(
				entry.getOfficialNumber(), 
				entry.getHistoricName(), 
				entry.getOptionalHistoricName(), 
				se != null ? se.getShortName() : "", 
				de != null ? de.getLabel() : "", 
				re != null ? re.getLabel(): "", 
				entry.getLastChangedByUser(),
				entry.getLastChangedOnDate(), 
				getCaveSketchURIs()
			));
		htmlWidget.addStyleName("html-data-display");
		add(htmlWidget, new MarginData(0, 0, 0, 0));
		setHeading((se != null ? se.getShortName() : "Site Unknown") + entry.getOfficialNumber() + (!entry.getHistoricName().isEmpty() ? "(" + entry.getHistoricName() + ")" : ""));
	}
	
	private ArrayList<CaveSketchUri> getCaveSketchURIs() {
		ArrayList<CaveSketchUri> result = new ArrayList<CaveSketchUri>();
		for (CaveSketchEntry cse : entry.getCaveSketchList()) {
			result.add(new CaveSketchUri("resource?cavesketch=" + cse.getCaveSketchFilename() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
		}
		CaveTypeEntry cte = StaticTables.getInstance().getCaveTypeEntries().get(entry.getCaveTypeID());
		if ((cte != null) && !cte.getSketchName().isEmpty()) {
			result.add(new CaveSketchUri("resource?background=" + cte.getSketchName() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()));
		}
		return result;
	}

	@Override
	public String getUniqueID() {
		return entry.getUniqueID();
	}

}