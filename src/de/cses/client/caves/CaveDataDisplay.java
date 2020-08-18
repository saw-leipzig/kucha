package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.CaveAreaEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveSketchEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.DistrictEntry;
import de.cses.shared.RegionEntry;
import de.cses.shared.SiteEntry;

public class CaveDataDisplay extends AbstractDataDisplay {
	private HTML htmlWidget;
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	class CaveSketchUri {
		
		SafeUri uri;

		/**
		 * @param caveSketch
		 */
		public CaveSketchUri(SafeUri uri) {
			this.uri = uri;
		}

		public SafeUri getUri() {
			return uri;
		}

		public void setUri(SafeUri uri) {
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
		dbService.getCaveAreas(ce.getCaveID(),new AsyncCallback<ArrayList<CaveAreaEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				System.err.println("Problem loading CaveGroupEntry");
			}

			@Override
			public void onSuccess(ArrayList<CaveAreaEntry> result) {
				entry.setCaveAreaList(result);
				htmlWidget = new HTML(view.display(
				entry.getOfficialNumber(), 
				entry.getHistoricName(), 
				entry.getOptionalHistoricName(), 
				se != null ? se.getName() + (!se.getAlternativeName().isEmpty() ? " ("+se.getAlternativeName()+")" : "") : "site unknown", 
				de != null ? de.getLabel() : "", 
				re != null ? re.getLabel(): "", 
				entry.getLastChangedByUser(),
				entry.getModifiedOn(), 
				getCaveSketchURIs(),
				entry.getFirstDocumentedBy(), 
				entry.getFirstDocumentedInYear() > 0 ? String.valueOf(entry.getFirstDocumentedInYear()) : "",
				entry.getCaveTypeID() > 0 ? stab.getCaveTypeEntries().get(entry.getCaveTypeID()).getNameEN() : "",
				entry.getOrientationID() > 0 ? stab.getOrientationEntries().get(entry.getOrientationID()).getName() : "",
				entry.getCaveAreaList(),
				entry.getFindings() != null ? entry.getFindings() : "",
				entry.getNotes() != null ? entry.getNotes() : "",
				entry.getCaveLayoutComments(),
				entry.isHasVolutedHorseShoeArch(),
				entry.isHasSculptures(),
				entry.isHasClayFigures(),
				entry.isHasImmitationOfMountains(),
				entry.isHasHolesForFixationOfPlasticalItems(),
				entry.isHasWoodenConstruction()
			));
			}});
		htmlWidget.addStyleName("html-data-display");
		add(htmlWidget, new MarginData(0, 0, 0, 0));
		setHeading((se != null ? se.getShortName() : "Site Unknown") + " " + entry.getOfficialNumber() + (!entry.getHistoricName().isEmpty() ? " (" + entry.getHistoricName() + ")" : ""));
	}
	
	private ArrayList<CaveSketchUri> getCaveSketchURIs() {
		ArrayList<CaveSketchUri> result = new ArrayList<CaveSketchUri>();
		for (CaveSketchEntry cse : entry.getCaveSketchList()) {
			result.add(new CaveSketchUri(UriUtils.fromString("resource?cavesketch=" + cse.getCaveSketchFilename() + UserLogin.getInstance().getUsernameSessionIDParameterForUri())));
		}
		CaveTypeEntry cte = StaticTables.getInstance().getCaveTypeEntries().get(entry.getCaveTypeID());
		if ((cte != null) && !cte.getSketchName().isEmpty()) {
			result.add(new CaveSketchUri(UriUtils.fromString("resource?background=" + cte.getSketchName() + UserLogin.getInstance().getUsernameSessionIDParameterForUri())));
		}
		return result;
	}

	@Override
	public String getUniqueID() {
		return entry.getUniqueID();
	}

	@Override
	public AbstractEntry getEntry() {
		return entry;
	}

}
