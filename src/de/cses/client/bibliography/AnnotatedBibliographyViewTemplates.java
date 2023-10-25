/*
 * Copyright 2018 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.client.bibliography;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.shared.AnnotatedBibliographyEntry;

/**
 * @author alingnau; Erik Radisch
 *
 */
public interface AnnotatedBibliographyViewTemplates extends XTemplates {

	@XTemplate(source = "BibView.html")
	SafeHtml view(AnnotatedBibliographyEntry bibEntry, String uri, Boolean hasArticle);
	
	@XTemplate(source = "ExtendedBibView.html")
	SafeHtml extendedView(AnnotatedBibliographyEntry bibEntry);
	
	@XTemplate("<div style=\"font-size:12px; overflow: hidden; text-align: left; text-overflow: ellipsis;\"><p>{pdf}{bibliography}<i>{translit}{bold}</i>{translat}{tail}<p></div>")
	SafeHtml bibView(String bibliography, String translit, String bold, String translat, String tail,SafeHtml pdf);
	
	@XTemplate("<div style=\"font-size:12px; overflow: hidden; text-align: left; text-overflow: ellipsis;\"><p>{pdf}{bibliography}<i>{translit}</i>{bold} {translat}{tail}<p></div>")
	SafeHtml bibViewHasHan(String bibliography, String translit, String bold, String translat, String tail,SafeHtml pdf);
	
}
