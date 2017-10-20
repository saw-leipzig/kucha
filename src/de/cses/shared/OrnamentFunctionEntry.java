package de.cses.shared;

public class OrnamentFunctionEntry extends AbstractEntry {
	
	private int ornamentFunctionID;
	private String name;
	
	public OrnamentFunctionEntry(){
		this(0, "");
	}
	
	public OrnamentFunctionEntry(int ornamentFunctionID, String name){
		this.ornamentFunctionID = ornamentFunctionID;
		this.name= name;
	}

	public int getOrnamentFunctionID() {
		return ornamentFunctionID;
	}

	public void setOrnamentFunctionID(int ornamentFunctionID) {
		this.ornamentFunctionID = ornamentFunctionID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "OrnamenFunction" + ornamentFunctionID;
	}
	
	

}
