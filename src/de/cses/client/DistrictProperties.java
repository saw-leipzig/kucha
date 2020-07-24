package de.cses.client;

import com.google.gwt.editor.client.Editor.Path;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.PropertyAccess;

import de.cses.shared.DistrictEntry;

public interface DistrictProperties extends PropertyAccess<DistrictEntry> {
	
  @Path("districtID")
  ModelKeyProvider<DistrictEntry> key();
  
  @Path("name")
  LabelProvider<DistrictEntry> NameLabel();

  ValueProvider<DistrictEntry, String> Name();
  
  ValueProvider<DistrictEntry, String> Description();

}
