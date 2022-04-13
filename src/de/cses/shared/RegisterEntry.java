/*
 * Copyright 2016 
 * Saxon Academy of Science in Leipzig, Germany
 * 
 * This is free software: you can redistribute it and/or modify it under the terms of the 
 * GNU General Public License version 3 (GPL v3) as published by the Free Software Foundation.
 * 
 * This software is distributed WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. Please read the GPL v3 for more details.
 * 
 * You should have received a copy of the GPL v3 along with the software. 
 * If not, you can access it from here: <https://www.gnu.org/licenses/gpl-3.0.txt>.
 */
package de.cses.shared;

public class RegisterEntry {
	private int caveID;
	private int depictionID;
	private int register;
	private int number;
	private boolean rightToLeftOrder;
	
	public RegisterEntry() {	}

	public RegisterEntry(int caveID, int depictionID, int register, int number, boolean rightToLeftOrder) {
		this.caveID = caveID;
		this.depictionID = depictionID;
		this.register = register;
		this.number = number;
		this.rightToLeftOrder = rightToLeftOrder;
	}

	public int getCaveID() {
		return caveID;
	}

	public void setCaveID(int caveID) {
		this.caveID = caveID;
	}

	public int getDepictionID() {
		return depictionID;
	}

	public void setDepictionID(int depictionID) {
		this.depictionID = depictionID;
	}

	public int getRegister() {
		return register;
	}

	public void setRegister(int register) {
		this.register = register;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public boolean getRightToLeftOrder() {
		return rightToLeftOrder;
	}

	public void setRightToLeftOrder(boolean rightToLeftOrder) {
		this.rightToLeftOrder = rightToLeftOrder;
	}
	
}
