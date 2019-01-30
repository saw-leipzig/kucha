package de.cses.client.bibliography;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.cses.client.ui.AbstractDataDisplay;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;

public class AnnotatedBibliographyDataDisplay extends AbstractDataDisplay {
	
	AnnotatedBibliographyEntry bibEntry;
	
	
	interface AnnotatedBibliographyViewTemplates extends XTemplates {
		
		@XTemplate(source = "AnnotatedBibliographyDisplay.html")
		SafeHtml display(AnnotatedBibliographyEntry bibEntry);
	}

	public AnnotatedBibliographyDataDisplay(AnnotatedBibliographyEntry bibEntry) {
		this.bibEntry = bibEntry;
		
		AnnotatedBibliographyViewTemplates view = GWT.create(AnnotatedBibliographyViewTemplates.class);
		HTML htmlWidget = new HTML(view.display(bibEntry));
		htmlWidget.addStyleName("html-data-display");
		add(htmlWidget, new MarginData(0));
		setHeading(bibEntry.getTitleORG());
	}

	@Override
	public String getUniqueID() {
		return null;
	}

	@Override
	public AbstractEntry getEntry() {
		return bibEntry;
	}

}