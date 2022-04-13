package de.cses.shared;

public class CoordinatesEntry extends AbstractEntry {

	private int register;
	private int number;

	public CoordinatesEntry(int register, int number) {
		this.register = register;
		this.number = number;
	}
	public String getName() {
		return "Register " + Integer.toString(register)+ ", Number "+Integer.toString(number);
	}
	public String getCoordinatesID() {
		return "Register " + Integer.toString(register)+ ", Number "+Integer.toString(number);
	}
	public int getRegister() {
		return this.register;
	}
	public void setRegister(int register) {
		this.register = register;
	}
	public int getNumber() {
		return this.number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public CoordinatesEntry() {
		this(0, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */

    @Override
    public boolean equals(Object anObject) {
    	boolean isEqual=false;
    	if (anObject instanceof CoordinatesEntry){
        	if ((this.getRegister()==((CoordinatesEntry)anObject).getRegister())&&(this.getNumber()==((CoordinatesEntry)anObject).getNumber()) ) {
        		isEqual=true;
        	}
    	}
        return isEqual;
    }

	@Override
	public String getUniqueID() {
		return "CoordinatesEntry-"+Integer.toString(this.getRegister())+"-"+Integer.toString(this.getNumber());
	}

}
