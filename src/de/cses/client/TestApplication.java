package de.cses.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;

import de.cses.client.images.ImageManager;
import de.cses.client.ornamentic.CreateOrnamentic;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class TestApplication implements EntryPoint {

	static CreateOrnamentic CreateOrnamentic = new CreateOrnamentic();

	private TabLayoutPanel main;
	
	/**
	 * This is the entry point method.
	 */
	
	public void onModuleLoad() {
		
		main = new TabLayoutPanel(3.0, Unit.EM);
		RootPanel.get().add(main);
		
		CreateOrnamentic co = new CreateOrnamentic();
		ImageManager imgManager = new ImageManager();
		
		main.setHeight(Window.getClientHeight()+"px");
		main.add(new TestPanel("Maja"), "Tab 1");
		main.add(co.asWidget(), "Ornamentic Editor");
		main.add(imgManager.asWidget(), "Image Manager");
	}

	public static CreateOrnamentic getCreateOrnamentic() {
		return CreateOrnamentic;
	}

	public static void setCreateOrnamentic(CreateOrnamentic createOrnamentic) {
		CreateOrnamentic = createOrnamentic;
	}
}
