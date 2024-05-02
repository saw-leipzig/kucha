package de.cses.client.walls;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
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
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.Style.SelectionMode;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.CloseEvent;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.NumberField;
import com.sencha.gxt.widget.core.client.form.NumberPropertyEditor;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

import de.cses.client.DatabaseService;
import de.cses.client.StaticTables;
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
import de.cses.shared.DepictionEntry;
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
interface PositionProperties extends PropertyAccess<PositionEntry> {
	ModelKeyProvider<PositionEntry> positionID();
									

	LabelProvider<PositionEntry> name();
}

interface PositionViewTemplates extends XTemplates {
	@XTemplate("<div>{name}</div>")
	SafeHtml positionView(String name);
}

public class WallView implements IsWidget{
	
	private PopupPanel mainPanel = new PopupPanel(); ;
	private WallTreeEntry wte;
	private FramedPanel mainFP;
	private PlainTabPanel tabPanel;	
	private WallViewListener wvl;
	private DepictionEntry correspondingDepictionEntry = null;
	private PositionProperties positionProps;
	private ListStore<PositionEntry> positionEntryLS;
	private ListView<PositionEntry, PositionEntry> PositionSelectionLV;
	private SimpleComboBox<String> positionEntriesCB;
	
	public WallView(WallViewListener wvl, DepictionEntry de) {
		this.correspondingDepictionEntry = de;
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
	private void addDimensionToWall(WallDimensionEntry wde){
		wte.addDimension(wde);
	}
	public void setWall(WallTreeEntry wte) {
		Util.doLogging("setting wall");
		this.wte = wte;
		Integer count = tabPanel.getWidgetCount();
		while (tabPanel.iterator().hasNext()) {
			tabPanel.remove(tabPanel.iterator().next());
		}
		if (correspondingDepictionEntry != null) {
			if (wte.getPositions()==null) {
				PositionSelectionLV.getSelectionModel().deselectAll();
			}
			else {
				PositionSelectionLV.getSelectionModel().deselectAll();
//				PositionSelectionLV.getSelectionModel().setSelection(wallTree.wallTree.getSelectionModel().getSelectedItem().getPosition());
				for (PositionEntry pe : wte.getPositions()) {
					Util.doLogging("set position");
					PositionSelectionLV.getSelectionModel().select(true, pe);
				}	
			}			
		}

		mainFP.setHeading("Information on Wall: "+ wte.getWallName());
		mainFP.setSize("100%", "100%");
		Util.doLogging("dimension length" + Integer.toString(wte.getDimensions().size()));
		if (wte.getDimensions() != null) {
			for (WallDimensionEntry wde: this.wte.getDimensions()) {
				if (!wde.isdeleted()) {
					Util.doLogging("found wall register");
					DimensionEditorListener del = new DimensionEditorListener() {

						@Override
						public void saveDimension(WallDimensionEntry wde) {
							Util.doLogging("saveWallDimension" + wde.getName());
							addDimensionToWall(wde);
							
						}
						@Override
						public void deleteDimension(WallDimensionEntry wde) {
							Util.doLogging("deleteWallDimension" + wde.getName());
							wte.replaceDimensions(wde);
							setWall(wte);
						}
						
					};
					DimensionEditor deEntry = new DimensionEditor(wde,del, correspondingDepictionEntry);
					
					tabPanel.add(deEntry, new TabItemConfig(wde.getName(), false));					
				}
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
				wde.setWallId(wte.getWallLocationID());
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
		// mainFP.addTool(saveButton);
		ToolButton closeButton = new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
		closeButton.setToolTip(Util.createToolTip("Close View"));
		closeButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				mainPanel.hide();
				wvl.saveWall(wte);
			}
		});
		tabPanel = new PlainTabPanel();
		tabPanel.setSize("750px", "500px");
		mainFP.add(tabPanel);
		mainFP.addTool(closeButton);
		if (correspondingDepictionEntry != null){
			FramedPanel positionFP = new FramedPanel();
			positionFP.setHeading("Choose areal Position");
			positionProps = GWT.create(PositionProperties.class);
			positionEntryLS = new ListStore<PositionEntry>(positionProps.positionID());

			for (PositionEntry ope : StaticTables.getInstance().getPositionEntries().values()) {
				positionEntryLS.add(ope);
			}
			final PositionViewTemplates pvTemplates = GWT.create(PositionViewTemplates.class);
			PositionSelectionLV = new ListView<PositionEntry, PositionEntry>(positionEntryLS,
					new IdentityValueProvider<PositionEntry>(),
					new SimpleSafeHtmlCell<PositionEntry>(new AbstractSafeHtmlRenderer<PositionEntry>() {

						@Override
						public SafeHtml render(PositionEntry entry) {
							return pvTemplates.positionView(entry.getName());
						}

					}));
			PositionSelectionLV.getSelectionModel().addSelectionHandler(new SelectionHandler<PositionEntry>() {

				@Override
				public void onSelection(SelectionEvent<PositionEntry> arg0) {

				}
				
			});
			PositionSelectionLV.getSelectionModel().addSelectionChangedHandler(new SelectionChangedHandler<PositionEntry>() {

				@Override
				public void onSelectionChanged(SelectionChangedEvent<PositionEntry> event) {
					ArrayList<PositionEntry> positions = new ArrayList<PositionEntry>();
					for (PositionEntry pe : PositionSelectionLV.getSelectionModel().getSelectedItems()) {
						Util.doLogging("setting new Position");
						positions.add(pe);
					};
					wte.setPositions(positions);
				}
				
			});
			PositionSelectionLV.getSelectionModel().setSelectionMode(SelectionMode.SIMPLE);
			positionFP.add(PositionSelectionLV);
			VerticalLayoutContainer vlcDepictionPosition = new VerticalLayoutContainer();
			vlcDepictionPosition.add(mainFP, new VerticalLayoutData(1,0.8));
			vlcDepictionPosition.add(positionFP, new VerticalLayoutData(1,0.2));
			mainPanel.add(vlcDepictionPosition);
		} else {
			mainPanel.add(mainFP);
		}
		
	}

}
