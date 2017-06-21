package de.cses.shared;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class OrnamentCaveRelation implements IsSerializable{
	private int ornamentID;
	private String name;
	private int caveID;
	private int style;
	private int orientation;
	private String colours;
	private String notes;
	private String group;
	private String relatedelementeofOtherCultures;
	private String similarelementsOfOtherCultures;
	private ArrayList<Integer> similarOrnamentsRelationID= new ArrayList<Integer>();
	private ArrayList<Integer> PictorialElementIDs = new ArrayList<Integer>();
	private ArrayList<Integer> relatedOrnamentsRelationID = new ArrayList<Integer>();
	private ArrayList<WallOrnamentCaveRelation> walls = new ArrayList<WallOrnamentCaveRelation>();

	
	
	public OrnamentCaveRelation(){
		
	}


	public int getOrnamentID() {
		return ornamentID;
	}


	public void setOrnamentID(int ornamentID) {
		this.ornamentID = ornamentID;
	}


	public int getCaveID() {
		return caveID;
	}


	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}


	public int getStyle() {
		return style;
	}


	public void setStyle(int style) {
		this.style = style;
	}


	public int getOrientation() {
		return orientation;
	}


	public void setOrientation(int orientation) {
		this.orientation = orientation;
	}



	public String getColours() {
		return colours;
	}


	public void setColours(String colours) {
		this.colours = colours;
	}




	public String getNotes() {
		return notes;
	}


	public void setNotes(String notes) {
		this.notes = notes;
	}






	/**
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}


	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<WallOrnamentCaveRelation> getWalls() {
		return walls;
	}



	public ArrayList<Integer> getSimilarOrnamentsRelationID() {
		return similarOrnamentsRelationID;
	}


	public void setSimilarOrnamentsRelationID(ArrayList<Integer> similarOrnamentsRelationID) {
		this.similarOrnamentsRelationID = similarOrnamentsRelationID;
	}


	public ArrayList<Integer> getRelatedOrnamentsRelationID() {
		return relatedOrnamentsRelationID;
	}


	public void setRelatedOrnamentsRelationID(ArrayList<Integer> relatedOrnamentsRelationID) {
		this.relatedOrnamentsRelationID = relatedOrnamentsRelationID;
	}


	public void setWalls(ArrayList<WallOrnamentCaveRelation> walls) {
		this.walls = walls;
	}


	public String getRelatedelementeofOtherCultures() {
		return relatedelementeofOtherCultures;
	}


	public void setRelatedelementeofOtherCultures(String relatedelementeofOtherCultures) {
		this.relatedelementeofOtherCultures = relatedelementeofOtherCultures;
	}


	public String getSimilarelementsOfOtherCultures() {
		return similarelementsOfOtherCultures;
	}


	public void setSimilarelementsOfOtherCultures(String similarelementsOfOtherCultures) {
		this.similarelementsOfOtherCultures = similarelementsOfOtherCultures;
	}


	public ArrayList<Integer> getPictorialElementIDs() {
		return PictorialElementIDs;
	}


	public void setPictorialElementIDs(ArrayList<Integer> pictorialElementIDs) {
		PictorialElementIDs = pictorialElementIDs;
	}

	


	
	

}
