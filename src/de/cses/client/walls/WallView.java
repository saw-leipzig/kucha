package de.cses.client.walls;

import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.PlainTabPanel;
import com.sencha.gxt.widget.core.client.TabItemConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

import de.cses.client.Util;
import de.cses.client.bibliography.BibDocumentUploader;
import de.cses.client.bibliography.BibDocumentUploader.BibDocumentUploadListener;
import de.cses.client.ui.EditorListener;
import de.cses.client.ui.OSDLoader;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.PositionEntry;
import de.cses.shared.WallTreeEntry;

public class WallView {
	
	private PopupPanel mainPanel = new PopupPanel(); ;
	private WallTreeEntry wte;
	private FramedPanel mainFP;
	private PlainTabPanel tabPanel;	
	private OSDLoader osdLoader;
	
	public WallView() {
		initPanel();
	}
	public void show() {
		mainPanel.show();
	}
	public void setWall(WallTreeEntry wte) {
		this.wte = wte;
		mainFP.setHeading("Layouts of Wall: "+ wte.getWallName());
		mainFP.setSize("100%", "100%");
		if (wte.getPosition() != null) {
			osdLoader = new OSDLoader(wte.getPosition());
			for (PositionEntry pe: wte.getPosition()) {
				FramedPanel positionTabPanel = new FramedPanel();
				HTMLPanel zoomPanel = new HTMLPanel(SafeHtmlUtils.fromTrustedString("<figure class='paintRepImgPreview' style='height: 98%;width: 98%;text-align: center;'><div id= '"+pe.getName()+"' style='width: 100%; height: 100%;text-align: center;'></div></fugure>"));
				positionTabPanel.setHeading("Layout");
				positionTabPanel.add(zoomPanel);
				tabPanel.add(positionTabPanel, new TabItemConfig(pe.getName(), false));
			}
			osdLoader.setosd();
			mainPanel.center();			
		}
	}
	private void initPanel() {
		mainFP = new FramedPanel();
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
