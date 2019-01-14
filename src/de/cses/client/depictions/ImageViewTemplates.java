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
package de.cses.client.depictions;

import java.util.ArrayList;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.sencha.gxt.core.client.XTemplates;

import de.cses.client.depictions.DepictionEditor.TitleElement;

/**
 * @author alingnau
 * 
 * This interface is for displaying images in the DepictionEditor. In future versions the style shall be moved to a CSS file 
 */
public interface ImageViewTemplates extends XTemplates {

	@XTemplate("<figure style='border-style: solid; border-color: #99ff66; border-width: 3px; border-radius: 10px; margin: 0; padding: 2px; width: 95%'>"
			+ "<img src='{imageUri}' style='border-radius: 10px; position: relative; margin-left: 2px; width: 230px; background: white;'>"
			+ "<figcaption style='font-size:12px; padding: 10px; text-align: center;'>{shortName} ({imageFormat}) <div class='freeIcon' style='text-align: left;'/>"
			+ "<p style='font-size:10px;'> <tpl for='titleList'> {element}<wbr> </tpl> </p>"
			+ "<p style='font-size: 8px; text-align: right;'>{imageAuthor}<br>{copyright}</p></figcaption></figure>")
	SafeHtml openAccessImage(SafeUri imageUri, String shortName, ArrayList<TitleElement> titleList, String imageFormat, String imageAuthor, String copyright);

	@XTemplate("<figure style='border-style: solid; border-color: #ff1a1a; border-width: 3px; border-radius: 10px; margin: 0; padding: 2px; width: 95%'>"
			+ "<img src='{imageUri}' style='border-radius: 10px; position: relative; margin-left: 2px; width: 230px; background: white;'>"
			+ "<figcaption style='font-size:12px; padding: 10px; text-align: center;'>{shortName} ({imageFormat})"
			+ "<p style='font-size:10px;'> <tpl for='titleList'> {element}<wbr> </tpl> </p>"
			+ "<p style='font-size: 8px; text-align: right;'>{imageAuthor}<br>{copyright}</p></figcaption></figure>")
	SafeHtml nonOpenAccessImage(SafeUri imageUri, String shortName, ArrayList<TitleElement> titleList, String imageFormat, String imageAuthor, String copyright);

	@XTemplate("<figure style='border-style: solid; border-color: #0073e6; border-width: 3px; border-radius: 10px; margin: 0; padding: 2px; width: 95%'>"
			+ "<img src='{imageUri}' style='border-radius: 10px; position: relative; margin-left: 2px; width: 230px; background: white;'>"
			+ "<figcaption style='font-size:12px; padding: 10px; text-align: center;'>{shortName} ({imageFormat})"
			+ "<p style='font-size:10px;'> <tpl for='titleList'> {element}<wbr> </tpl> </p>"
			+ "<p style='font-size: 8px; text-align: right;'>{imageAuthor}<br>{copyright}</p></figcaption></figure>")
	SafeHtml masterImage(SafeUri imageUri, String shortName, ArrayList<TitleElement> titleList, String imageFormat, String imageAuthor, String copyright);

}
