/*
 * Copyright 2017 
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

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.form.TextField;

/**
 * @author Nina
 *
 */
public class AnnotatedBiblographyEditor implements IsWidget {
	private VBoxLayoutContainer widget;
	HorizontalLayoutContainer horizontBackground;

	
	VerticalLayoutContainer deskriptionVLayout;
	VerticalLayoutContainer originalVLayout;
	VerticalLayoutContainer transkripVLayout;
	VerticalLayoutContainer englishVLayout;
	
	@Override
	public Widget asWidget() {
		if (widget == null) {
			BoxLayoutData flex = new BoxLayoutData();
			flex.setFlex(1);
			widget = new VBoxLayoutContainer();
			widget.add(createForm(), flex);
		}

		return widget;
	}
	public Widget createForm(){
		
		FramedPanel frame = new FramedPanel();
		frame.setHeading("Biblography");
		VerticalLayoutContainer background = new VerticalLayoutContainer();
		frame.add(background);
		
		//Overview FramedPanel
		FramedPanel overview = new FramedPanel();
		overview.setHeading("Overview");
		background.add(overview);
		
		
		horizontBackground = new HorizontalLayoutContainer();
		overview.add(horizontBackground);
		
		deskriptionVLayout = new VerticalLayoutContainer();
		originalVLayout = new VerticalLayoutContainer();
		transkripVLayout = new VerticalLayoutContainer();
		englishVLayout = new VerticalLayoutContainer();
		
		horizontBackground.add(deskriptionVLayout);
		horizontBackground.add(englishVLayout);
		horizontBackground.add(transkripVLayout);
		horizontBackground.add(originalVLayout);
		
		TextField authorEN = new TextField();
		TextField authorORG = new TextField();
		TextField authorTR = new TextField();
		HTML authorDES = new HTML("Author: ");
		
		deskriptionVLayout.add(authorDES);
		englishVLayout.add(authorEN);
		transkripVLayout.add(authorTR);
		originalVLayout.add(authorORG);

		TextField yearEN = new TextField();
		TextField yearORG = new TextField();
		TextField yearTR = new TextField();
		HTML yearDES = new HTML("Year: ");
		
		deskriptionVLayout.add(yearDES);
		englishVLayout.add(yearEN);
		transkripVLayout.add(yearTR);
		originalVLayout.add(yearORG);
		
		TextField titelEN = new TextField();
		TextField titelORG = new TextField();
		TextField titelTR = new TextField();
		HTML titelDES = new HTML("Titel: ");
		
		deskriptionVLayout.add(titelDES);
		englishVLayout.add(titelEN);
		transkripVLayout.add(titelTR);
		originalVLayout.add(titelORG);
		
		
		// Publications FramedPanel
		FramedPanel publication = new FramedPanel();
		overview.setHeading("Publication");
		background.add(overview);

		horizontBackground = new HorizontalLayoutContainer();
		publication.add(horizontBackground);
		
		deskriptionVLayout = new VerticalLayoutContainer();
		originalVLayout = new VerticalLayoutContainer();
		transkripVLayout = new VerticalLayoutContainer();
		englishVLayout = new VerticalLayoutContainer();
		
		horizontBackground.add(deskriptionVLayout);
		horizontBackground.add(englishVLayout);
		horizontBackground.add(transkripVLayout);
		horizontBackground.add(originalVLayout);
		
		TextField publisherEN = new TextField();
		TextField publisherORG = new TextField();
		TextField publisherTR = new TextField();
		HTML publisherDES = new HTML("Publisher: ");
		
		deskriptionVLayout.add(publisherDES);
		englishVLayout.add(publisherEN);
		transkripVLayout.add(publisherTR);
		originalVLayout.add(publisherORG);
		
		
		
		
		return frame;
		
	}

}
