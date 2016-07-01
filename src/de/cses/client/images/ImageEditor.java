package de.cses.client.images;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutPack;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer.VBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.DateField;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.StringComboBox;
import com.sencha.gxt.widget.core.client.form.TextArea;

public class ImageEditor implements IsWidget {

	private int currentImageID;
	private TextField copyrightField;
	private TextArea commentArea;
	private DateField dateField;
	private StringComboBox photographerSelection;
	private FramedPanel panel;
//	private VBoxLayoutContainer vlc;

	public ImageEditor() {
	}

	@Override
	public Widget asWidget() {
		if (panel== null) {
			initPanel();
		}
		return  panel;
	}
	
	private void initPanel() {
		VBoxLayoutContainer vlc = new VBoxLayoutContainer(VBoxLayoutAlign.STRETCH);
		
//		vlc = new VBoxLayoutContainer();
//    vlc.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
    BoxLayoutData layoutData = new BoxLayoutData(new Margins(5, 0, 0, 5));
//    vlc.add(new TextButton("Button 1"), layoutData);
//    vlc.add(new TextButton("Button 2"), layoutData);
//    vlc.add(new TextButton("Button 3"), layoutData);

		
	  copyrightField = new TextField();
	  vlc.add(new FieldLabel(copyrightField, "Copyright"));
	  
	  commentArea = new TextArea();
	  commentArea.setSize("300px", "50px");
	  vlc.add(new FieldLabel(commentArea, "Comment"));

	  dateField = new DateField(new DateTimePropertyEditor("dd.MM.yyyy"));
	  vlc.add(new FieldLabel(dateField, "Date captured"));
	  
	  ArrayList<String> photographerList = new ArrayList<String>();
	  photographerList.add("Roy Shneider");
	  photographerList.add("Getty Images");
	  photographerList.add("Frank o'Grapher");
	  photographerSelection = new StringComboBox(photographerList);
	  vlc.add(new FieldLabel(photographerSelection, "Photographer"));
	  
		TextButton saveButton = new TextButton("save");
		saveButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				saveImage(currentImageID);
			}
		});

		TextButton cancelButton = new TextButton("cancel");
		cancelButton.addSelectHandler(new SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {

			}
		});

		
		panel = new FramedPanel();
		panel.setHeading("Image Editor");
		panel.setButtonAlign(BoxLayoutPack.CENTER);
		panel.add(vlc, layoutData);
		panel.addButton(saveButton);
		panel.addButton(cancelButton);
	}
	
	/**
	 * This method is loading the image form the database.
	 * @param imageID
	 */
	private void loadImage(int imageID) {
		
	}

	/**
	 * This method will save the image information into the database.
	 * @param imageID
	 */
	private void saveImage(int imageID) {
		
	}
}
