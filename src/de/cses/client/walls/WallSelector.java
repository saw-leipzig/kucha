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
package de.cses.client.walls;

import java.util.Comparator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.StaticTables;
import de.cses.client.user.UserLogin;
import de.cses.shared.CaveEntry;
import de.cses.shared.CaveTypeEntry;
import de.cses.shared.WallEntry;

/**
 * @author alingnau
 *
 */
public class WallSelector implements IsWidget {

	private FlowLayoutContainer caveSketchContainer;
	private CaveLayoutViewTemplates caveLayoutViewTemplates;
	// private ContentPanel mainPanel = null;
	private ComboBox<WallEntry> wallSelectorCB;
	private WallProperties wallProps;
	private CaveEntry currentCave;
	private WallViewTemplate wallVT;
	private ListStore<WallEntry> wallEntryLS;
//	private VerticalLayoutContainer mainVLC = null;
	private BorderLayoutContainer mainBLC;

	interface CaveLayoutViewTemplates extends XTemplates {
		@XTemplate("<img src=\"{imageUri}\" style=\"{defaultSketchWidth}; height: auto; align-content: center; margin: 5px;\">")
		SafeHtml image(SafeUri imageUri, SafeStyles defaultSketchWidth);
		
		@XTemplate("<img src=\"{imageUri}\" style=\"width: 100%; height: auto; align-content: center; margin: 5px;\">")
		SafeHtml image(SafeUri imageUri);
	}

	interface WallProperties extends PropertyAccess<WallEntry> {
		ModelKeyProvider<WallEntry> wallLocationID();
	}

	interface WallViewTemplate extends XTemplates {
		@XTemplate("<div>{label}</div>")
		SafeHtml wallLabel(String label);
	}

	/**
	 * 
	 * @param defaultCaveSketchWidth
	 *          string representing the default sketch width in pixel (px)
	 */
	public WallSelector(SelectionHandler<WallEntry> wallSelectionHandler) {
		caveLayoutViewTemplates = GWT.create(CaveLayoutViewTemplates.class);
		wallProps = GWT.create(WallProperties.class);
		wallVT = GWT.create(WallViewTemplate.class);
		wallEntryLS = new ListStore<WallEntry>(wallProps.wallLocationID());

		Comparator<WallEntry> comparator = new Comparator<WallEntry>() {
			@Override
			public int compare(WallEntry we1, WallEntry we2) {
				return StaticTables.getInstance().getWallLocationEntries().get(we1.getWallLocationID()).getLabel()
						.compareTo(StaticTables.getInstance().getWallLocationEntries().get(we2.getWallLocationID()).getLabel());
			}
		};
		wallEntryLS.addSortInfo(new StoreSortInfo<WallEntry>(comparator, SortDir.ASC));
		createUI();
		wallSelectorCB.addSelectionHandler(wallSelectionHandler);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (mainBLC == null) {
			createUI();
		}
		return mainBLC;
	}

	/**
	 * 
	 */
	private void createUI() {
		caveSketchContainer = new FlowLayoutContainer();
		caveSketchContainer.setScrollMode(ScrollMode.AUTOY);

		LabelProvider<WallEntry> wallSelectorLP = new LabelProvider<WallEntry>() {
			@Override
			public String getLabel(WallEntry entry) {
				return StaticTables.getInstance().getWallLocationEntries().get(entry.getWallLocationID()).getLabel();
			}
		};
		
		AbstractSafeHtmlRenderer<WallEntry> wallSelectorRenderer = new AbstractSafeHtmlRenderer<WallEntry>() {
			@Override
			public SafeHtml render(WallEntry entry) {
				return wallVT.wallLabel(StaticTables.getInstance().getWallLocationEntries().get(entry.getWallLocationID()).getLabel());
			}
		};
		
		wallSelectorCB = new ComboBox<WallEntry>(wallEntryLS, wallSelectorLP, wallSelectorRenderer);
		wallSelectorCB.setEditable(false);
		wallSelectorCB.setTypeAhead(false);
		wallSelectorCB.setTriggerAction(TriggerAction.ALL);
		wallSelectorCB.setEmptyText("select wall");
//		wallSelectorCB.addSelectionHandler(new SelectionHandler<WallEntry>() {
//
//			@Override
//			public void onSelection(SelectionEvent<WallEntry> event) {
//				selectedWallEntry = event.getSelectedItem();
//			}
//		});

		BorderLayoutData southBLD = new BorderLayoutData(25);
		southBLD.setMargins(new Margins(10, 0, 0, 0));
		
		mainBLC = new BorderLayoutContainer();
		mainBLC.setCenterWidget(caveSketchContainer, new BorderLayoutData());
		mainBLC.setSouthWidget(wallSelectorCB, southBLD);

	}

	/**
	 * @param selectedCave
	 */
	public void setCave(CaveEntry selectedCave) {
		currentCave = selectedCave;
		CaveTypeEntry ctEntry = StaticTables.getInstance().getCaveTypeEntries().get(selectedCave.getCaveTypeID());
		caveSketchContainer.clear();
		if ((selectedCave.getOptionalCaveSketch() != null) && !selectedCave.getOptionalCaveSketch().isEmpty()) {
			caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(
					UriUtils.fromString("resource?cavesketch=" + selectedCave.getOptionalCaveSketch()
							+ UserLogin.getInstance().getUsernameSessionIDParameterForUri()))));
		}
		if ((ctEntry.getSketchName() != null) && !ctEntry.getSketchName().isEmpty()) {
			caveSketchContainer.add(new HTMLPanel(caveLayoutViewTemplates.image(
					UriUtils
							.fromString("resource?background=" + ctEntry.getSketchName() + UserLogin.getInstance().getUsernameSessionIDParameterForUri()))));
//					,SafeStylesUtils.forWidth(defaultCaveSketchWidth, Unit.PX))));
		}
		wallEntryLS.clear();
		wallEntryLS.addAll(currentCave.getWallList());
//		switch (ctEntry.getCaveTypeID()) {
//
//			case 2: // square cave
//				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
//					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)) {
//						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
//					}
//				}
//				break;
//
//			case 3: // residential cave
//				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
//					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_CORRIDOR_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL)) {
//						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
//					}
//				}
//				break;
//
//			case 4: // central-pillar cave
//			case 6: // monumental image cave
//				for (WallLocationEntry wle : StaticTables.getInstance().getWallLocationEntries().values()) {
//					if ((wle.getCaveAreaLabel() == WallLocationEntry.ANTECHAMBER_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.MAIN_CHAMBER_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_LEFT_CORRIDOR_LABEL)
//							|| (wle.getCaveAreaLabel() == WallLocationEntry.REAR_AREA_RIGHT_CORRIDOR_LABEL)) {
//						wallEntryLS.add(currentCave.getWall(wle.getWallLocationID()));
//					}
//				}
//				break;
//
//			default:
//				break;
//		}
	}
	
	public void selectWall(int wallLocationID) {
		wallSelectorCB.setValue(wallEntryLS.findModelWithKey(Integer.toString(wallLocationID)), true);
	}

	public WallEntry getSelectedWallEntry() {
		return wallSelectorCB.getCurrentValue();
	}
//
//	public void setWallEntry(WallEntry wallEntry) {
//		wallSelectorCB.setValue(wallEntry, true);
//	}

//	/**
//	 * @return the wallSelectorCB
//	 */
//	public ComboBox<WallEntry> getWallSelectorCB() {
//		return wallSelectorCB;
//	}
//
//	/**
//	 * @param wallSelectorCB the wallSelectorCB to set
//	 */
//	public void setWallSelectorCB(ComboBox<WallEntry> wallSelectorCB) {
//		this.wallSelectorCB = wallSelectorCB;
//	}
	

}
