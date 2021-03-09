/*
 * Copyright 2017-2018 
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
package de.cses.client.ui;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.TouchEndEvent;
import com.google.gwt.event.dom.client.TouchEndHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.dom.client.TouchStartEvent;
import com.google.gwt.event.dom.client.TouchStartHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

import de.cses.client.user.UserLogin;
import de.cses.shared.AbstractEntry;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public abstract class AbstractView extends Button implements EditorListener {
	private static Integer clicks =0;
	private static PopupPanel editorPanel = new PopupPanel(false);
	private Boolean hasMoved=false;
	/**
	 * This is the general constructor that amongst other tasks initializes the PopupPanel for the editor
	 */
	public AbstractView() {
		addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (UserLogin.getInstance().getAccessRights() >= UserEntry.FULL) { // guests are not allowed to edit
					showEditor(getEntry());
				}
			}
		});
		this.addTouchEndHandler(new TouchEndHandler() {

			@Override
			public void onTouchEnd(TouchEndEvent event) {
				if (!hasMoved){
					if (UserLogin.getInstance().getAccessRights() >= UserEntry.FULL) { // guests are not allowed to edit
						showEditor(getEntry());
					}					
				}
			}
		});
		this.addTouchMoveHandler(new TouchMoveHandler() {

			@Override
			public void onTouchMove(TouchMoveEvent event) {
				hasMoved=true;
				
			}
			
		});
		this.addTouchStartHandler(new TouchStartHandler() {


			@Override
			public void onTouchStart(TouchStartEvent event) {
				hasMoved=false;
				
			}
			
		});
	}

	public void showEditor(AbstractEntry entry) {
		AbstractEditor editor = getEditor(entry, this);
		loadEditor(editor);
	}
	public Widget getNextChild(Widget child) {
		return ((FlowLayoutContainer)this.getParent()).getWidget(((FlowLayoutContainer)this.getParent()).getWidgetIndex(child)+1);
	}
	public Widget getPrevChild(Widget child) {
		return ((FlowLayoutContainer)this.getParent()).getWidget(((FlowLayoutContainer)this.getParent()).getWidgetIndex(child)-1);
	}

	public static void loadEditor(AbstractEditor editor) {
		clicks=0;
		editorPanel.clear();
		editorPanel.addDomHandler(new ChangeHandler() {
		    @Override
		    public void onChange(ChangeEvent event) {
		    	 clicks+=1;		  		
		    }
		  }, ChangeEvent.getType());
		editorPanel.addDomHandler(new ClickHandler() {
		    @Override
		    public void onClick(ClickEvent event) {
		    	 clicks+=1;
		  		
		    }
		  }, ClickEvent.getType());
		editorPanel.addDomHandler(new KeyPressHandler() {
		    @Override
		    public void onKeyPress(KeyPressEvent event) {
		    	 clicks+=1;
		  		
		    }
		  }, KeyPressEvent.getType());

		editorPanel.add(editor);
		editorPanel.setModal(true);
		editorPanel.setGlassEnabled(true);
		editorPanel.center();
		editor.setfocus();
		}
	private void viewDataSet(String url) { // 
		Window.open(url,"_blank",null);
	}
	
	abstract protected AbstractEditor getEditor(AbstractEntry entry,AbstractView av);
	
	abstract protected AbstractEntry getEntry();
	
	public PopupPanel getEditorPanel() {
		return editorPanel;
	}
	public Integer getClickNumber() {
		return clicks;
	}
	public void addClickNumber() {
		clicks+=1;
	}
	public void setClickNumber(int clicks) {
		this.clicks=clicks;
	}
	
	abstract protected String getPermalink(); // this will be the URI for the server request...
	 
	@Override
	public void closeRequest(AbstractEntry entry) {
		editorPanel.hide();
		getElement().getStyle().setBorderColor("#FFA500");
		getElement().getStyle().setBorderWidth(3.0, Unit.PX);
	}

	
}
