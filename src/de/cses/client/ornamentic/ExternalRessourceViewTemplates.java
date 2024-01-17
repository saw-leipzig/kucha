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
package de.cses.client.ornamentic;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import de.cses.client.ui.TextElement;

/**
 * @author alingnau
 * 
 * This interface is for displaying images in the DepictionEditor. In future versions the style shall be moved to a CSS file 
 */
public interface ExternalRessourceViewTemplates extends XTemplates {

	@XTemplate(
			
			"<div style='font-size:12px; padding: 10px;'>"
			+ "<Table style=\"width:100%\">"
					+"<tr>"
						+"<td style=\"width:80%\">"
							+ "{description}"
							+ "<p style='font-size:10px;'> {source}{entity} </p>"
						+"</td>"
						+"<td style=\"width:20%\">"
					     + "<form action=\"{source}{entity}\" target=\"_blank\">"
				         + "<button type=\"submit\">Visit Ressource Now</button>"
				         + "</form>"
						+"</td>"
					+"</tr>"
				+"</Table>"
			+ "</div>")
	SafeHtml extRes(String description, String source, String entity, String source2, String entity2);


}
