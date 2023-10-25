package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.OrnamentComponentsEntry;

public interface OrnamentDataViewTemplate extends XTemplates {
		
	@XTemplate(source = "OrnamentDisplay_oben.html")
	SafeHtml displayoben(String code, SafeUri imageUri, SafeUri fullImageUri); 
	@XTemplate("<h4 class=\"data-display\">Detected in the following walls:</h4>")
	SafeHtml displaymitte();
	@XTemplate(source = "OrnamentDisplay_unten.html")
	SafeHtml displayunten(String typeDesc, String genRemarks,  ArrayList<AnnotatedBibliographyEntry> bib);
				}

