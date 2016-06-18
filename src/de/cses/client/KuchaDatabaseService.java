package de.cses.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.form.ComboBox;
import com.sencha.gxt.widget.core.client.form.FieldLabel;

import de.cses.client.ornamentic.CreateOrnamentic;
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
			  ComboBox<District> combo = TestApplication.getCreateOrnamentic().getCombo();
			  combo.setStore(store);
	

			  
				
			}
			
		});
		
		
	}
}
