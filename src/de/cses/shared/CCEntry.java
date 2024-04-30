
package de.cses.shared;

/**
 * @author alingnau
 *
 */
public class CCEntry extends AbstractEntry {
	
	private int cCID = 0;
	private String name, html;

	
	/**
	 * @param locationID
	 * @param name
	 * @param town
	 * @param region
	 * @param county
	 * @param url
	 */
	public CCEntry(int cCID, String name, String html) {
		super();
		this.cCID = cCID;
		this.name = name;
		this.html = html;
	}

	/**
	 * 
	 */
	public CCEntry() {	}

	/* (non-Javadoc)
	 * @see de.cses.shared.AbstractEntry#getUniqueID()
	 */
	@Override
	public String getUniqueID() {
		return "LocationEntry-" + cCID;
	}

	public int getCCID() {
		return cCID;
	}

	public void setCCID(int cCID) {
		this.cCID = cCID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHtml() {
		return html;
	}

	public void setHtml(String html) {
		this.html = html;
	}
}
