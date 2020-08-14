package de.cses.client.images;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;

import de.cses.client.ui.TextElement;
import de.cses.shared.AnnotatedBibliographyEntry;

public interface ImageDataViewTemplates  extends XTemplates {
	
	@XTemplate(source = "ImageDisplay.html")
	SafeHtml display(SafeUri imageUri, SafeUri fullImageUri, String title, String author, String shortname, String imgType, String accessLvl, String copyright, String comment, String date, String location);

}
