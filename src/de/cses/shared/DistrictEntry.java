package de.cses.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class District implements IsSerializable {
int DistrictID;
String Name;
String Description;

public District(){
	
}
public District(String Name, String Description) {
  this();
  this.Name = Name;
  this.Description = Description;

}
public String getName() {
	return Name;
}
public void setName(String name) {
	Name = name;
}
public String getDescription() {
	return Description;
}
public void setDescription(String description) {
	Description = description;
}
public int getDistrictID() {
	return DistrictID;
}
public void setDistrictID(int districtID) {
	DistrictID = districtID;
}


}