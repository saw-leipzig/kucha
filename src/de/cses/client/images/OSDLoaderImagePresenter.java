package de.cses.client.images;

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
import de.cses.shared.EmptySpotEntry;
import de.cses.shared.ExportEntry;
import de.cses.shared.IconographyEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.CoordinateEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallSketchEntry;


public class OSDLoaderImagePresenter extends AbstractOSDLoader {
	
	private ImageEntry ie;
	private String prefix;
	
	public OSDLoaderImagePresenter(ImageEntry ie, String prefix) {
		super(false, null, null, "rect", false, null);
		this.osdDic = createDic();
		this.editor = null;
		this.annos = null;
		ArrayList<ImageEntry> images = new ArrayList<ImageEntry>();
		images.add(ie);
		this.images=images;
		this.annotation =false;
		this.prefix = prefix;
	}
	public void setosd() {
		 destroyAllViewers();
		 dbService.getOSDContext(new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
			@Override
			public void onSuccess(String result) {
				loadTilesStart(result);
			}
		});
	}
	
	public void loadTilesStart(String context) {
		ArrayList<JavaScriptObject> results = loadTiles(null, null,null, images, context, annotation, readOnly);
		if (results != null) {
			JavaScriptObject tiles = results.remove(0);
			JavaScriptObject imgDic=results.remove(0);
			JavaScriptObject ifn=results.remove(0);
			viewers = createZoomImage(tiles,ifn,imgDic, osdDic,UserLogin.getInstance().getSessionID(), annotation, editor, this, annos, readOnly, annotationType, disableEditor, highlighter, prefix);	
			makeReadOnlyAllViewersJava(viewers);
		}
	}
	@Override
	public void annoCreated(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void annoChanged(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void createSelection(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void startSelection(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void selectAnnotation(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void changeSelectionTarget(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void annoDeleted(JavaScriptObject annotation) {
		// TODO Auto-generated method stub
		
	}
	
	
}
