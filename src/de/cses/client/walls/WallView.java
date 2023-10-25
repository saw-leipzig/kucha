package de.cses.client.walls;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.bibliography.BibDocumentUploader;
import de.cses.client.bibliography.BibDocumentUploader.BibDocumentUploadListener;
import de.cses.client.caves.CaveSketchUploader;
import de.cses.client.caves.CaveSketchUploader.CaveSketchUploadListener;
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.OSDListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.user.UserLogin;
import de.cses.client.walls.WallSketchUploader.WallSketchUploadListener;
import de.cses.shared.AnnotationEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveSketchEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.ImageTypeEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.VendorEntry;
import de.cses.shared.WallDimensionEntry;
import de.cses.shared.WallSketchEntry;
import de.cses.shared.WallTreeEntry;


interface ImageViewTemplates extends XTemplates {
	@XTemplate("<div>{filename}</div>")
	SafeHtml imageLabel(String filename);
}

public class WallView implements IsWidget{
	
	private PopupPanel mainPanel = new PopupPanel(); ;
	private WallTreeEntry wte;
	private FramedPanel mainFP;
	private PlainTabPanel tabPanel;	
	private WallViewListener wvl;

	public WallView(WallViewListener wvl) {
		this.wvl = wvl;
		initPanel();
	}
	
	@Override
	public Widget asWidget() {
		if (mainPanel == null) {
			initPanel();
		}
		return mainPanel;
	}
	
	public void show() {
		mainPanel.show();
	}
	public void setWall(WallTreeEntry wte) {
		this.wte = wte;
		for (int i = 0; i < tabPanel.getWidgetCount(); i++) {
			tabPanel.remove(i);
			}
		mainFP.setHeading("Layouts of Wall: "+ wte.getWallName());
		mainFP.setSize("100%", "100%");
		tabPanel.clear();
		if (wte.getDimensions() != null) {
			for (WallDimensionEntry wde: this.wte.getDimensions()) {
				DimensionEditorListener del = new DimensionEditorListener() {

					@Override
					public void saveDimension(WallDimensionEntry wde) {
						wte.addDimension(wde);
						
					}
					
				};
				DimensionEditor de = new DimensionEditor(wde,del);
				tabPanel.add(de, new TabItemConfig(wde.getName(), false));
			}
			mainPanel.center();			
		}
	}
	private void initPanel() {
		mainFP = new FramedPanel();
		ToolButton addButton = new ToolButton(new IconConfig("addButton", "addButtonOver"));
		addButton.setToolTip(Util.createToolTip("Add New Wall Register"));
		addButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				WallDimensionEntry wde = new WallDimensionEntry();
				wde.setName("New Entry");
				wte.addDimension(wde);
				setWall(wte);
			}
		});
		mainFP.addTool(addButton);
		ToolButton saveButton = new ToolButton(new IconConfig("saveButton", "saveButtonOver"));
		saveButton.setToolTip(Util.createToolTip("Save Wall"));
		saveButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				wvl.saveWall(wte);
			}
		});
		mainFP.addTool(saveButton);
		ToolButton closeButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeButton.setToolTip(Util.createToolTip("Close View"));
		closeButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				mainPanel.hide();
			}
		});
		tabPanel = new PlainTabPanel();
		tabPanel.setSize("550px", "550px");
		mainFP.add(tabPanel);
		mainFP.addTool(closeButton);
		mainPanel.add(mainFP);
	}

}
