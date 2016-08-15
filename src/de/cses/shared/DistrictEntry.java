package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class DistrictEntry implements IsSerializable {
private int districtID;
private String name;
private String description;

public DistrictEntry(){
	
}
public DistrictEntry(String name, String description) {
  this();
  this.name = name;
  this.description = description;

}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
public int getDistrictID() {
	return districtID;
}
public void setDistrictID(int districtID) {
	this.districtID = districtID;
}


}