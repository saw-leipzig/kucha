package de.cses.shared;

import java.util.ArrayList;

public class OrnamenticSearchEntry extends AbstractSearchEntry {
	
	private String code;
	private boolean empty = true;
	private String description;
	private String remarks;
	private String interpretation;
	private String references;
	private String similaritys;
	private String group; // heisst mittlerweile unit
	private OrnamentClassEntry ornamentClass; // heisst mittlerweile motif
	private StyleEntry style; // fehlt noch, wurde bisher nicht gewuenscht.
	private ArrayList <CaveEntry> caves = new ArrayList<CaveEntry>();
	private ArrayList <OrnamentComponentsEntry> components = new ArrayList<OrnamentComponentsEntry>();
	private ArrayList <InnerSecondaryPatternsEntry> secondarypatterns = new ArrayList<InnerSecondaryPatternsEntry>();
	private ArrayList <DistrictEntry> districts = new ArrayList<DistrictEntry>();
	private ArrayList <OrnamentEntry> relatedOrnaments = new ArrayList<OrnamentEntry>();
	private ArrayList <IconographyEntry> iconographys = new ArrayList<IconographyEntry>();
	private ArrayList <OrnamentPositionEntry> position = new ArrayList <OrnamentPositionEntry>();
	private ArrayList <OrnamentFunctionEntry> function = new ArrayList <OrnamentFunctionEntry>();
	
	
	/**
	 * @return the empty
	 */
	public boolean isEmpty() {
		return empty;
	}
	/**
	 * @param empty the empty to set
	 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}
	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	/**
	 * @return the interpretation
	 */
	public String getInterpretation() {
		return interpretation;
	}
	/**
	 * @param interpretation the interpretation to set
	 */
	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}
	/**
	 * @return the references
	 */
	public String getReferences() {
		return references;
	}
	/**
	 * @param references the references to set
	 */
	public void setReferences(String references) {
		this.references = references;
	}
	/**
	 * @return the similaritys
	 */
	public String getSimilaritys() {
		return similaritys;
	}
	/**
	 * @param similaritys the similaritys to set
	 */
	public void setSimilaritys(String similaritys) {
		this.similaritys = similaritys;
	}
	/**
	 * @return the ornamentClass
	 */
	public OrnamentClassEntry getOrnamentClass() {
		return ornamentClass;
	}
	/**
	 * @param ornamentClass the ornamentClass to set
	 */
	public void setOrnamentClass(OrnamentClassEntry ornamentClass) {
		this.ornamentClass = ornamentClass;
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
	/**
	 * @return the caves
	 */
	public ArrayList<CaveEntry> getCaves() {
		return caves;
	}
	/**
	 * @param caves the caves to set
	 */
	public void setCaves(ArrayList<CaveEntry> caves) {
		this.caves = caves;
	}
	/**
	 * @return the components
	 */
	public ArrayList<OrnamentComponentsEntry> getComponents() {
		return components;
	}
	/**
	 * @param components the components to set
	 */
	public void setComponents(ArrayList<OrnamentComponentsEntry> components) {
		this.components = components;
	}
	/**
	 * @return the secondarypatterns
	 */
	public ArrayList<InnerSecondaryPatternsEntry> getSecondarypatterns() {
		return secondarypatterns;
	}
	/**
	 * @param secondarypatterns the secondarypatterns to set
	 */
	public void setSecondarypatterns(ArrayList<InnerSecondaryPatternsEntry> secondarypatterns) {
		this.secondarypatterns = secondarypatterns;
	}
	/**
	 * @return the districts
	 */
	public ArrayList<DistrictEntry> getDistricts() {
		return districts;
	}
	/**
	 * @param districts the districts to set
	 */
	public void setDistricts(ArrayList<DistrictEntry> districts) {
		this.districts = districts;
	}
	/**
	 * @return the relatedOrnaments
	 */
	public ArrayList<OrnamentEntry> getRelatedOrnaments() {
		return relatedOrnaments;
	}
	/**
	 * @param relatedOrnaments the relatedOrnaments to set
	 */
	public void setRelatedOrnaments(ArrayList<OrnamentEntry> relatedOrnaments) {
		this.relatedOrnaments = relatedOrnaments;
	}
	/**
	 * @return the iconographys
	 */
	public ArrayList<IconographyEntry> getIconographys() {
		return iconographys;
	}
	/**
	 * @param iconographys the iconographys to set
	 */
	public void setIconographys(ArrayList<IconographyEntry> iconographys) {
		this.iconographys = iconographys;
	}
	/**
	 * @return the position
	 */
	public ArrayList<OrnamentPositionEntry> getPosition() {
		return position;
	}
	/**
	 * @param position the position to set
	 */
	public void setPosition(ArrayList<OrnamentPositionEntry> position) {
		this.position = position;
	}
	/**
	 * @return the function
	 */
	public ArrayList<OrnamentFunctionEntry> getFunction() {
		return function;
	}
	/**
	 * @param function the function to set
	 */
	public void setFunction(ArrayList<OrnamentFunctionEntry> function) {
		this.function = function;
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

	
	
	

}
