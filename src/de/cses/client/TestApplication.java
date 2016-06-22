package de.cses.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint {

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	//private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private TabLayoutPanel main;
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		
		main = new TabLayoutPanel(3.0, Unit.EM);
		RootPanel.get().add(main);
		
		main.setHeight(Window.getClientHeight()+"px");
		main.add(new TestPanel("Maja"), "Tab 1");
		main.add(new TestPanel("Peter"), "Tab 2");
		
	}
	
	

}
