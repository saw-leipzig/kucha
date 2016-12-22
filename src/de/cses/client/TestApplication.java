/*
 * Copyright 2016 
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
package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.client.bibliography.AuthorEditor;
import de.cses.client.bibliography.AuthorEditorListener;
import de.cses.client.caves.CaveEditor;
import de.cses.client.caves.CaveType;
import de.cses.client.caves.Cella;
import de.cses.client.depictions.DepictionEditor;
import de.cses.client.depictions.DepictionEditorListener;
import de.cses.client.images.ImageEditor;
import de.cses.client.images.ImageEditorListener;
import de.cses.client.images.ImageUploadListener;
import de.cses.client.images.ImageUploader;
import de.cses.client.images.PhotographerEditor;
import de.cses.client.ornamentic.Ornamentic;
import de.cses.client.walls.Walls;
import de.cses.shared.AuthorEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.DepictionEntry;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint, ImageEditorListener {

	static Ornamentic ornamentic = new Ornamentic();
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private TabLayoutPanel main;
	private PopupPanel depictionEditorPanel;
	private PopupPanel imageEditorPanel;
	private PopupPanel imageUploadPanel;
	private PopupPanel authorEditorPanel;
	private PopupPanel caveEditorPanel;
	private CaveProperties caveProps;
	private ListStore<CaveEntry> caveEntryList;
	private ComboBox<CaveEntry> caveSelection;

	protected int selectedCaveID = 0;

	interface CaveProperties extends PropertyAccess<CaveEntry> {
		ModelKeyProvider<CaveEntry> caveID();
		LabelProvider<CaveEntry> officialNumber();
	}

	interface CaveViewTemplates extends XTemplates {
		@XTemplate("<div>{officialNumber}: {officialName}</div>")
		SafeHtml caveLabel(String officialNumber, String officialName);
		
		@XTemplate("<div>{officialNumber}</div>")
		SafeHtml caveLabel(String officialNumber);	
	}

	/**
	 * This is the entry point method.
	 */

	public void onModuleLoad() {

		/*
		 * apparently, the viewport is important since it guarantees that the
		 * content of all tabs will be updated in the background and look nice and
		 * clean all the time
		 */
		main = new TabLayoutPanel(3.0, Unit.EM);
		Viewport v = new Viewport();
		v.add(main);
		RootPanel.get().add(v); // use RootPanel, not RootLayoutPanel here!

		Ornamentic co = new Ornamentic();
		Cella cella = new Cella();
		CaveType caveType = new CaveType();

		// ImageUploader imageUploader = new ImageUploader(imgEditor);
//		PhotographerEditor pEditor = new PhotographerEditor();

		main.add(co.asWidget(), "Ornamentic Editor");
		main.add(caveType.asWidget(), "Cave Type Editor");

		// we are using FlowLayoutContainer
		FlowLayoutContainer flowLCcella = new FlowLayoutContainer();
		flowLCcella.setScrollMode(ScrollMode.AUTOY);
		MarginData layoutDatacella = new MarginData(new Margins(0, 5, 0, 0));
		flowLCcella.add(cella, layoutDatacella);
		main.add(flowLCcella, "Cella Editor");

		main.add(cella, "Cella Editor");
		// main.add(pEditor, "Photographer Editor");
		Walls wall = new Walls();
		main.add(wall, "Wall Editor");
		
		VerticalPanel vp = new VerticalPanel();

		TextButton imgEditorButton = new TextButton("Edit Image");
		imageEditorPanel = new PopupPanel(false);
		imageEditorPanel.add(new ImageEditor(this));
		new Draggable(imageEditorPanel);
		imgEditorButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageEditorPanel.setGlassEnabled(true);
				imageEditorPanel.center();
				imageEditorPanel.show();
			}
		});
		vp.add(imgEditorButton);

		TextButton depictionButton = new TextButton("Edit Depiction");
		depictionEditorPanel = new PopupPanel(false);
		depictionEditorPanel.add(new DepictionEditor(0, new DepictionEditorListener() {

			@Override
			public void depictionSaved(DepictionEntry depictionEntry) {
				depictionEditorPanel.hide();
			}
		}));
		new Draggable(depictionEditorPanel);
		depictionButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				depictionEditorPanel.setGlassEnabled(true);
				// depictionEditorPanel.setModal(true);
				depictionEditorPanel.center();
				depictionEditorPanel.show();
			}
		});
		vp.add(depictionButton);

		TextButton uploadButton = new TextButton("Image Uploader");
		imageUploadPanel = new PopupPanel(false);
		imageUploadPanel.add(new ImageUploader(new ImageUploadListener() {

			@Override
			public void uploadCompleted() {
				imageUploadPanel.hide();
			}
		}));
		new Draggable(imageUploadPanel);
		uploadButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				imageUploadPanel.setGlassEnabled(true);
				imageUploadPanel.center();
				imageUploadPanel.show();
			}
		});
		vp.add(uploadButton);

		TextButton authorEditorButton = new TextButton("Author Editor");
		authorEditorPanel = new PopupPanel(false);
		authorEditorPanel.add(new AuthorEditor(0, new AuthorEditorListener() {

			@Override
			public void authorSaved(AuthorEntry entry) {
				authorEditorPanel.hide();
			}
		}));
		new Draggable(authorEditorPanel);
		authorEditorButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				authorEditorPanel.setGlassEnabled(true);
				authorEditorPanel.center();
				authorEditorPanel.show();
			}
		});
		vp.add(authorEditorButton);

		caveProps = GWT.create(CaveProperties.class);
		caveEntryList = new ListStore<CaveEntry>(caveProps.caveID());
		dbService.getCaves(new AsyncCallback<ArrayList<CaveEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<CaveEntry> result) {
				for (CaveEntry ce : result) {
					caveEntryList.add(ce);
				}
			}
		});

		caveSelection = new ComboBox<CaveEntry>(caveEntryList, caveProps.officialNumber(),
				new AbstractSafeHtmlRenderer<CaveEntry>() {

					@Override
					public SafeHtml render(CaveEntry item) {
						final CaveViewTemplates cvTemplates = GWT.create(CaveViewTemplates.class);
						if ((item.getOfficialName() != null) && (item.getOfficialName().length() == 0)) {
							return cvTemplates.caveLabel(item.getOfficialNumber());
						} else {
							return cvTemplates.caveLabel(item.getOfficialNumber(), item.getOfficialName());
						}
					}
				});
		caveSelection.setEmptyText("Select a Cave ...");
		caveSelection.setTypeAhead(false);
		caveSelection.setEditable(false);
		caveSelection.setTriggerAction(TriggerAction.ALL);
		caveSelection.addSelectionHandler(new SelectionHandler<CaveEntry>() {

			@Override
			public void onSelection(SelectionEvent<CaveEntry> event) {
				selectedCaveID = event.getSelectedItem().getCaveID();
			}
		});

		TextButton caveEditorButton = new TextButton("Cave Editor");
		caveEditorButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				caveEditorPanel = new PopupPanel(false);
				caveEditorPanel.add(new CaveEditor(selectedCaveID));
				new Draggable(caveEditorPanel);
				caveEditorPanel.setGlassEnabled(true);
				caveEditorPanel.center();
				caveEditorPanel.show();
			}
		});
		
		HorizontalPanel cavePanel = new HorizontalPanel();
		cavePanel.add(caveSelection);
		cavePanel.add(caveEditorButton);
		vp.add(cavePanel);

		main.add(vp, "Editor testing");

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.client.images.ImageEditorListener#closeImageEditor()
	 */
	@Override
	public void closeImageEditor() {
		imageEditorPanel.hide();
	}

}