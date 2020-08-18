package de.cses.client.ornamentic;

import java.util.ArrayList;
import java.util.Map;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.TreeStyle;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.depictions.DepictionDataDisplay.Images;
import de.cses.client.depictions.IconographySelector.IconographyKeyProvider;
import de.cses.client.depictions.IconographySelector.IconographyValueProvider;
import de.cses.client.ui.AbstractDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.OrnamentEntry;
import de.cses.shared.WallTreeEntry;

public class OrnamentDataDisplay extends AbstractDataDisplay {
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private OrnamenticIconographyTree ornamentTrees;
	private OrnamentEntry entry;
	private Tree<IconographyEntry, String> selectedIconographyTree;
	private TreeStore<IconographyEntry> selectedIconographyTreeStore;
	protected Map<String, IconographyEntry> selectedIconographyMap;

	public OrnamentDataDisplay(OrnamentEntry e) {
		super();
		entry = e;
		OrnamentDataViewTemplate view = GWT.create(OrnamentDataViewTemplate.class);
		SafeUri imageUri = UriUtils.fromString("resource?imageID=" + e.getMasterImageID() + "&thumb=700" + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		SafeUri fullImageUri = UriUtils.fromString("resource?imageID=" + e.getMasterImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		HTML htmlWidget = new HTML(view.displayoben(
				e.getCode(), 
				imageUri,
				fullImageUri 

			));
		HTML htmlWidget2 = new HTML(view.displayunten(
		e.getDescription(),
		e.getRemarks(),
		e.getRelatedBibliographyList(),
		e.getOrnamentComponents()
		));		
		HTML htmlWidgetmiddle = new HTML(view.displaymitte());
		ornamentTrees= new 	OrnamenticIconographyTree(entry);
		ornamentTrees.setDialogboxnotcalled(false);
		ornamentTrees.setTreeStore();

		StyleInjector.inject(".myCustomStyle {font-family: verdana;background:#ffcc66; font-size: 16px; }");
		htmlWidget.addStyleName("html-data-view");
		htmlWidgetmiddle.addStyleName("html-data-view");
		htmlWidget2.addStyleName("html-data-view");
		VerticalLayoutContainer decriptionVLC = new VerticalLayoutContainer();
		decriptionVLC.addStyleName("myCustomStyle");
		decriptionVLC.add(htmlWidget);
		decriptionVLC.add(ornamentTrees.getSelectedIcoTree());
		decriptionVLC.add(htmlWidgetmiddle);
		decriptionVLC.add(ornamentTrees.getWalls().wallTree);
		
		decriptionVLC.add(htmlWidget2);
//		shortNameVLC.add(pictoralTree);
//		shortNameVLC.add(ornamentTree);
		add(decriptionVLC, new MarginData(0, 0, 0, 0));
		setHeading(entry.getCode());
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
