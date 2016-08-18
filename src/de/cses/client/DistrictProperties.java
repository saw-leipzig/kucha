package de.cses.client;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.shared.District;

public interface DistrictProperties extends PropertyAccess<District> {
	
  @Path("districtID")
  ModelKeyProvider<District> key();
  
  @Path("name")
  LabelProvider<District> NameLabel();

  ValueProvider<District, String> Name();
  
  ValueProvider<District, String> Description();

}
