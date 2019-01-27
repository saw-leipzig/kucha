package de.cses.client.ui;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.CheckBox;
import com.sencha.gxt.widget.core.client.form.SimpleComboBox;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.validator.MaxLengthValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.client.caves.CaveDataDisplay;
import de.cses.client.depictions.DepictionDataDisplay;
import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.AnnotatedBibliographyEntry;
import de.cses.shared.CaveEntry;
import de.cses.shared.CollectionEntry;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;
import de.cses.shared.OrnamentEntry;

public class DataViewPortalLayoutContainer extends PortalLayoutContainer {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private ContentPanel dataViewPanel;

	public DataViewPortalLayoutContainer(int numColumns, ContentPanel dataViewPanel) {
		super(numColumns);
    setSpacing(10);
    setColumnWidth(0, 1.00);
    this.dataViewPanel = dataViewPanel;
	}
	
	public void drop(AbstractEntry entry) {
		if (containsEntry(entry)) {
			return;
		}
		if (entry instanceof CaveEntry) {
			add(new CaveDataDisplay((CaveEntry) entry), 0);
		} else if (entry instanceof DepictionEntry) {
			add(new DepictionDataDisplay((DepictionEntry) entry), 0);
		} else if (entry instanceof ImageEntry) {
			// TODO
		} else if (entry instanceof OrnamentEntry) {
			// TODO
		} else if (entry instanceof AnnotatedBibliographyEntry) {
			// TODO
		}
	}
	
	private boolean containsEntry(AbstractEntry entry) {
		for (int i=0; i<getColumnCount(); ++i) {
			Iterator<Widget> wi = getWidget(i).iterator();
			while (wi.hasNext()) {
				Widget w = wi.next();
				if (w instanceof AbstractDataDisplay) {
					AbstractEntry e = ((AbstractDataDisplay)w).getEntry();
					if (e.getUniqueID().equals(entry.getUniqueID())) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public void load() {
		dbService.getRelatedCollectionNames(UserLogin.getInstance().getSessionID(), new AsyncCallback<ArrayList<CollectionEntry>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<CollectionEntry> result) {
				PopupPanel loadCollectionDialog = new PopupPanel();
				FramedPanel loadCollectionFP = new FramedPanel();
				loadCollectionFP.setHeading("Select collection to load");
				SimpleComboBox<CollectionEntry> collectionNameCB = new SimpleComboBox<CollectionEntry>(new LabelProvider<CollectionEntry>() {

					@Override
					public String getLabel(CollectionEntry item) {
						return item.getCollectionName() + 
								(UserLogin.getInstance().getUsername().equals(item.getUser().getUsername()) ? "(personal collection)" : "(" + item.getUser().getUsername() + ")");
					}
				});
				collectionNameCB.add(result);
				collectionNameCB.setEditable(false);
				collectionNameCB.setTypeAhead(false);
				collectionNameCB.setTriggerAction(TriggerAction.ALL);
				loadCollectionFP.add(collectionNameCB);
				TextButton loadButton = new TextButton("load");
				loadButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						dbService.loadCollectedEntries(collectionNameCB.getValue(), new AsyncCallback<ArrayList<AbstractEntry>>() {

							@Override
							public void onFailure(Throwable caught) {
							}

							@Override
							public void onSuccess(ArrayList<AbstractEntry> resultList) {
								for (AbstractEntry entry : resultList) {
									drop(entry);
								}
								dataViewPanel.setHeading("View collection: " + collectionNameCB.getValue().getCollectionName());
								loadCollectionDialog.hide();
							}
						});
					}
				});
				loadCollectionFP.addButton(loadButton);
				TextButton cancelButton = new TextButton("cancel");
				cancelButton.addSelectHandler(new SelectHandler() {
					
					@Override
					public void onSelect(SelectEvent event) {
						loadCollectionDialog.hide();
					}
				});
				loadCollectionFP.addButton(cancelButton);
				loadCollectionDialog.add(loadCollectionFP);
				loadCollectionDialog.setModal(true);
				loadCollectionDialog.setWidth("450px");
				loadCollectionDialog.center();
			}
		});
		
	}
	
	public void save() {
		ArrayList<AbstractEntry> entryList = new ArrayList<AbstractEntry>();
		for (int i=0; i<getColumnCount(); ++i) {
			Iterator<Widget> wi = getWidget(i).iterator();
			while (wi.hasNext()) {
				Widget w = wi.next();
				if (w instanceof AbstractDataDisplay) {
					AbstractEntry e = ((AbstractDataDisplay)w).getEntry();
					Util.doLogging(e.getClass().getSimpleName() + ": " + e.getUniqueID());
					entryList.add(e);
				}
			}
		}
		if (!entryList.isEmpty()) {
			PopupPanel newCollectionNameDialog = new PopupPanel();
			FramedPanel newCollectionNameFP = new FramedPanel();
			newCollectionNameFP.setHeading("Enter collection name");
			TextField collectionNameField = new TextField();
			collectionNameField.setAllowBlank(false);
			collectionNameField.addValidator(new MaxLengthValidator(256));
			newCollectionNameFP.add(collectionNameField);
			CheckBox groupCollectionCB = new CheckBox();
			groupCollectionCB.setBoxLabel("group collection");
			groupCollectionCB.setToolTip(Util.createToolTip("Check box if you want to share with other users.", "Users need at least ASSOCIATE rights to access collections."));
			groupCollectionCB.setValue(false);
			VerticalLayoutContainer groupCollectionDialogVLC = new VerticalLayoutContainer();
			groupCollectionDialogVLC.add(collectionNameField, new VerticalLayoutData(1.0, .5, new Margins(5)));
			groupCollectionDialogVLC.add(groupCollectionCB,  new VerticalLayoutData(1.0, .5, new Margins(5)));
			ToolButton saveTB = new ToolButton(new IconConfig("saveButton","saveButtonOver"));
			saveTB.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					if (collectionNameField.validate()) {
						newCollectionNameDialog.hide();
						dbService.saveCollectedEntries(UserLogin.getInstance().getSessionID() , collectionNameField.getValue(), groupCollectionCB.getValue(), entryList, new AsyncCallback<Boolean>() {

							@Override
							public void onFailure(Throwable caught) {
								Util.showWarning("Server error", "The collection could not be saved.");
							}

							@Override
							public void onSuccess(Boolean result) {
							}
						});
					}
				}
			});
			newCollectionNameFP.addTool(saveTB);
			ToolButton cancelTB= new ToolButton(new IconConfig("closeButton", "closeButtonOver"));
			cancelTB.addSelectHandler(new SelectHandler() {

				@Override
				public void onSelect(SelectEvent event) {
					newCollectionNameDialog.hide();
				}
			});
			newCollectionNameFP.addTool(cancelTB);
			newCollectionNameDialog.add(newCollectionNameFP);
			newCollectionNameDialog.setModal(true);
			newCollectionNameDialog.setWidth("300px");
			newCollectionNameDialog.center();
		}
	}
	
}
