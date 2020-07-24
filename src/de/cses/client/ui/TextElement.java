package de.cses.client.ui;

public class TextElement {

	private String element;

	/**
	 * simple wrapper for use with XTemplates
	 * @param element
	 */
	public TextElement(String element) {
		this.element = element;
	}

	public String getElement() {
		return element;
	}

}
