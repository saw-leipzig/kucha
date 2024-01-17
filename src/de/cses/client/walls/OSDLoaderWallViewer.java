package de.cses.client.walls;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.info.Info;

import de.cses.client.StaticTables;
import de.cses.client.Util;
import de.cses.client.ui.AbstractOSDLoader;
import de.cses.client.ui.OSDListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.user.UserLogin;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ExportEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.CoordinateEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallSketchEntry;



public class OSDLoaderWallViewer extends OSDLoaderWallDimension {

	
	public OSDLoaderWallViewer(WallDimensionEntry wde, boolean annotation, OSDListener osdListener) {
		super(wde, annotation, null, "viewer");
		editorReadOnly=true;
		

	}
	public OSDLoaderWallViewer(WallDimensionEntry wde, DepictionEntry de, boolean annotation, OSDListener osdListener) {
		super(wde, de, annotation, null, "viewer");
		editorReadOnly=true;
	}

	
	public void setosd() {
		 destroyAllViewers();
		 dbService.getOSDContextWallSketches(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(String result) {
				loadTilesStart(result);
				setViewerConstrained();
			}
		});
	}
}
