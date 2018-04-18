package de.cses.shared;

import java.util.ArrayList;
import java.util.List;

public class OrnamentCaveRelation extends AbstractEntry {
	private int ornamentID;
	private String name;
	private CaveEntry cave;
	private String colours;
	private String notes;
	private DistrictEntry district;
	private String group;
	private String similarelementsOfOtherCultures;
	private ArrayList<OrientationEntry> orientations = new ArrayList<OrientationEntry>();
	private ArrayList<PictorialElementEntry> PictorialElements = new ArrayList<PictorialElementEntry>();
	private ArrayList<OrnamentEntry> relatedOrnamentsRelations = new ArrayList<OrnamentEntry>();
	private ArrayList<WallOrnamentCaveRelation> walls = new ArrayList<WallOrnamentCaveRelation>();
	private StyleEntry style;

	
	
	public OrnamentCaveRelation(){
		
	}
	public OrnamentCaveRelation(int ornamentID, CaveEntry caveEntry, String colours, String notes, String group, String similarElementsOfOtherCultures, ArrayList<PictorialElementEntry> PictorialElements,ArrayList<OrnamentEntry> relatedOrnamentsRelations ,  ArrayList<WallOrnamentCaveRelation> walls, ArrayList<OrientationEntry> orientations){
		this.ornamentID = ornamentID;
		this.name = caveEntry.getHistoricName() != null ? "Cave: " + caveEntry.getOfficialNumber() + " (" + caveEntry.getHistoricName() + ")" : "Cave: " + caveEntry.getOfficialNumber();
		this.colours = colours;
		this.notes = notes;
		this.similarelementsOfOtherCultures = similarElementsOfOtherCultures;
		this.orientations = orientations;
		this.PictorialElements = PictorialElements;
		this.group = group;
		this.relatedOrnamentsRelations = relatedOrnamentsRelations;
		this.walls = walls;

	}


	public int getOrnamentID() {
		return ornamentID;
	}


	public void setOrnamentID(int ornamentID) {
		this.ornamentID = ornamentID;
	}



	/**
	 * @return the district
	 */
	public DistrictEntry getDistrict() {
		return district;
	}


	/**
	 * @param district the district to set
	 */
	public void setDistrict(DistrictEntry district) {
		this.district = district;
	}


	/**
	 * @return the cave
	 */
	public CaveEntry getCave() {
		return cave;
	}


	/**
	 * @param cave the cave to set
	 */
	public void setCave(CaveEntry cave) {
		this.cave = cave;
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





	public void setWalls(ArrayList<WallOrnamentCaveRelation> walls) {
		this.walls = walls;
	}


	


	public String getSimilarelementsOfOtherCultures() {
		return similarelementsOfOtherCultures;
	}


	public void setSimilarelementsOfOtherCultures(String similarelementsOfOtherCultures) {
		this.similarelementsOfOtherCultures = similarelementsOfOtherCultures;
	}



	/**
	 * @return the orientations
	 */
	public ArrayList<OrientationEntry> getOrientations() {
		return orientations;
	}


	/**
	 * @param orientations the orientations to set
	 */
	public void setOrientations(ArrayList<OrientationEntry> orientations) {
		this.orientations = orientations;
	}


	/**
	 * @return the pictorialElements
	 */
	public ArrayList<PictorialElementEntry> getPictorialElements() {
		return PictorialElements;
	}


	/**
	 * @param pictorialElements the pictorialElements to set
	 */
	public void setPictorialElements(ArrayList<PictorialElementEntry> pictorialElements) {
		PictorialElements = pictorialElements;
	}


	/**
	 * @return the relatedOrnamentsRelations
	 */
	public ArrayList<OrnamentEntry> getRelatedOrnamentsRelations() {
		return relatedOrnamentsRelations;
	}


	/**
	 * @param relatedOrnamentsRelations the relatedOrnamentsRelations to set
	 */
	public void setRelatedOrnamentsRelations(ArrayList<OrnamentEntry> relatedOrnamentsRelations) {
		this.relatedOrnamentsRelations = relatedOrnamentsRelations;
	}


	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamentCaveRelation" + ornamentID;
	}


	/**
	 * @return the style
	 */
	public StyleEntry getStyle() {
		return style;
	}


	/**
	 * @param style the style to set
	 */
	public void setStyle(StyleEntry style) {
		this.style = style;
	}



	


	
	

}
