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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.safehtml.shared.SafeUri;
import com.google.gwt.safehtml.shared.UriUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.sencha.gxt.fx.client.Draggable;
import com.sencha.gxt.widget.core.client.Resizable;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.user.UserLogin;
import de.cses.shared.DepictionEntry;
import de.cses.shared.ImageEntry;

/**
 * @author nina
 *
 */
public class DepictionOnWallView extends SimpleContainer {

	/**
	 * 
	 */

	DepictionEntry depiction;
	DepictionOnWallView depictionview = this;
	private DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	public DepictionOnWallView(DepictionEntry depiction, final boolean editable) {

		super();

		this.depiction = depiction;
		SafeUri uri = UriUtils.fromString("resource?imageID=" + depiction.getMasterImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
		final Image image = new Image(uri);
		depictionview.add(image);
		if (editable) {
			Draggable drag = new Draggable(depictionview);
		}
		depictionview.setPixelSize(50, 50);
		Resizable resize = new Resizable(depictionview, Resizable.Dir.NE, Resizable.Dir.NW, Resizable.Dir.SE, Resizable.Dir.SW);
		resize.setPreserveRatio(true);

		ResizeHandler resizeHandler = new ResizeHandler() {

			@Override
			public void onResize(ResizeEvent event) {

				image.setPixelSize(depictionview.getOffsetWidth(), depictionview.getOffsetHeight());
			}

		};
		depictionview.addHandler(resizeHandler, ResizeEvent.getType());

//		dbService.getMasterImageEntryForDepiction(depiction, new AsyncCallback<ImageEntry>() {
//
//			@Override
//			public void onFailure(Throwable caught) {
//				caught.printStackTrace();
//			}
//
//			@Override
//			public void onSuccess(ImageEntry imageresult) {
//
//				SafeUri uri = UriUtils.fromString("resource?imageID=" + imageresult.getImageID() + UserLogin.getInstance().getUsernameSessionIDParameterForUri());
//				final Image image = new Image(uri);
//				depictionview.add(image);
//				if (editable) {
//					Draggable drag = new Draggable(depictionview);
//				}
//				depictionview.setPixelSize(50, 50);
//				Resizable resize = new Resizable(depictionview, Resizable.Dir.NE, Resizable.Dir.NW, Resizable.Dir.SE, Resizable.Dir.SW);
//				resize.setPreserveRatio(true);
//
//				ResizeHandler resizeHandler = new ResizeHandler() {
//
//					@Override
//					public void onResize(ResizeEvent event) {
//
//						image.setPixelSize(depictionview.getOffsetWidth(), depictionview.getOffsetHeight());
//					}
//
//				};
//				depictionview.addHandler(resizeHandler, ResizeEvent.getType());
//			}
//		});
	}

	/**
	 * @param deferElement
	 */
	public DepictionOnWallView(boolean deferElement) {
		super(deferElement);
		// TODO Auto-generated constructor stub
	}

	public int getDepictionID() {
		return depiction.getDepictionID();
	}

//	public void setDepictionID(int depictionID) {
//		this.depictionID = depictionID;
//	}

}
