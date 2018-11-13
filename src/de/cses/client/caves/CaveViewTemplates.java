package de.cses.client.caves;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.client.caves.CaveDataDisplay.CaveSketchUri;

public interface CaveViewTemplates extends XTemplates {
	
	@XTemplate(source = "CaveDisplay.html")
	SafeHtml display(String officialNumber, String historicName, String optionalHistoricName, String site, String district, String region, String user, String timestamp, ArrayList<CaveSketchUri> caveSketchList, String firstDocumentedBy, String firstDocumentedInYear,
			String caveType, String orientation);

}
