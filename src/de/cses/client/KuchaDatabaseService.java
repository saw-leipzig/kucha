/*
 * Copyright 2016 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU Affero General Public License version 3 (GNU AGPLv3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GNU AGPLv3 for more details.
 * 
 * You should have received a copy of the GNU AGPLv3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/agpl-3.0.txt>.
 */
package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;

import de.cses.shared.District;

public class KuchaDatabaseService {
	ArrayList<District> Districts;
	ComboBox<District> combo;
	public KuchaDatabaseService(){
		
	}

	public void getDistricts(){
	  DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
		dbService.getDistricts(new AsyncCallback<ArrayList<District>>(){

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSuccess(ArrayList<District> Districts) {
				Window.alert(Integer.toString(Districts.size()));
			 
			  ListStore<District> store = TestApplication.getCreateOrnamentic().getStore();
			  store.addAll(Districts);
			  combo = TestApplication.getCreateOrnamentic().getCombo();
			  combo.setStore(store);
	
			}
			
		});
		
		
	}
}
