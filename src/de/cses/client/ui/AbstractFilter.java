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
package de.cses.client.ui;


import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Portlet;

import de.cses.client.Util;
import de.cses.shared.AbstractSearchEntry;

/**
 * This class shall be extended to implement a new filter. To make sure all filters
 * follow a common structure, a <code>FramedPanel</code> is provided where the Widget from {@link #getFilterUI()} is 
 * placed. 
 * 
 * @author alingnau
 */
public abstract class AbstractFilter implements IsWidget{

	private Portlet panel = null;
	private String filterName;
	private AbstractSearchController saerchparent;
	private KeyPressHandler shortkey;
	/**
	 * 
	 */
	public AbstractFilter(String filterName) {
		this.filterName = filterName;
		this.shortkey = new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				Util.doLogging("Key Down registered");
	        	  if (event.getCharCode() == KeyCodes.KEY_ENTER) {
	        		  invokeSearch();
	        	  }
			}
		};
	}
	public void invokeSearch() {
		if (saerchparent!=null) {
		saerchparent.invokeSearch();
		}
	}
	/* (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IsWidget#asWidget()
	 */
	@Override
	public Widget asWidget() {
		if (panel == null) {
			panel = new Portlet();
			panel.setWidth("100%");
//			panel.setCollapsible(true);
			panel.setHeading(filterName);
			panel.add(getFilterUI());
		}
    panel.getHeader().setStylePrimaryName("frame-header");
		return panel;
	}
	public void setSerchParent(AbstractSearchController asc) {
		this.saerchparent=asc;
	}
	public AbstractSearchController getSearchParent() {
		return saerchparent;
	}

	public KeyPressHandler getShortkey() {
		return this.shortkey;
	}
	public void clear() {
		
	}
	/**
	 * 
	 * @return 
	 * A Widget representing the individual filter UI. This UI is placed inside the panel provided in {@link #asWidget()}. 
	 * Never try to call this method directly!
	 */
	protected abstract Widget getFilterUI();
	
	/**
	 * Every filter needs to implement a SearchEntry which will be used to request filtered searches from the server side.
	 * @return
	 */
	public abstract AbstractSearchEntry getSearchEntry();
	public abstract void setSearchEntry(AbstractSearchEntry searchEntry, boolean reset);
}
