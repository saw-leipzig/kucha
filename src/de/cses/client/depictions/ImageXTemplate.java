package de.cses.client.depictions;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.sencha.gxt.core.client.XTemplates;

public interface ImageXTemplate extends XTemplates{
	@XTemplate("<img style=\"border: 2px solid red; witdth: 25px;height: 250px;\" src=\"{url}\">")
	SafeHtml createImage(String url);
}


