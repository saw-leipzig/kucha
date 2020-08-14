package de.cses.client.images;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.widget.core.client.container.MarginData;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.StaticTables;
import de.cses.client.depictions.DepictionViewTemplates;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;

public class ImageDataDisplay  extends AbstractDataDisplay{
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private ImageEntry entry;
	public ImageDataDisplay(ImageEntry e) {
		super();
		entry = e;
		ImageDataViewTemplates view = GWT.create(ImageDataViewTemplates.class);
		String title = e.getTitle() != null ? e.getTitle() : "";
		String author = e.getImageAuthor() != null ? e.getImageAuthor().getLabel() : "";
		String shortname = e.getShortName() != null ? e.getShortName() : "";
		String imgType = e.getImageTypeID() > 0 ? StaticTables.getInstance().getImageTypeEntries().get(e.getImageTypeID()).getName() : "n/a";
		String  accessLvl = Integer.toString(e.getAccessLevel());
		String  copyright = e.getCopyright();
		String comment = e.getComment();
		String date = e.getDate();
		String location = "";
		if (entry.getLocation()!= null) {
			location = entry.getLocation().getName()+", "+entry.getLocation().getTown()+", "+entry.getLocation().getCounty();
		}
		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + e.getImageID() + "&thumb=700" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		SafeUri fullImageUri = UriUtils.fromString("resource?imageID=" + e.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		HTML htmlWidget = new HTML(view.display(
				imageUri, 
				fullImageUri, 
				title,
				author,
				shortname,	
				imgType,	
				accessLvl,
				copyright,	
				comment,
				date,
				location));
		StyleInjector.inject(".myCustomStyle {font-family: verdana;background:#ffcc66; font-size: 16px; }");
		htmlWidget.addStyleName("html-data-view");
		add(htmlWidget, new MarginData(0, 0, 0, 0));
		setHeading((title.length() > 0 ? title  : ""));

		
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
