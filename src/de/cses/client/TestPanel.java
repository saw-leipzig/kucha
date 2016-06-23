package de.cses.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.sencha.gxt.widget.core.client.box.ConfirmMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent;
import com.sencha.gxt.widget.core.client.event.DialogHideEvent.DialogHideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

public class TestPanel extends LayoutPanel {
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network " 
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	
	VerticalPanel mainPanel;

	/**
	 * This class extends a LayoutPanel and displays the input field and the send button for a database request.
	 * It also tests the GXT installation.
	 * @param name
	 */
	public TestPanel(String name) {
		final TextButton gxtSendButton = new TextButton("sendButton");
		final TextBox nameField = new TextBox();
		nameField.setText(name);
		final Label errorLabel = new Label();
		
		// We can add style names to widgets
		gxtSendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		mainPanel = new VerticalPanel();
		mainPanel.add(nameField);
		mainPanel.add(gxtSendButton);
		mainPanel.add(errorLabel);
		this.add(mainPanel);
		
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();

		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
//		dialogVPanel.add(closeButton);
//		dialogBox.setWidget(dialogVPanel);
		
    final ConfirmMessageBox gxtMessageBox = new ConfirmMessageBox("Remote Procedure Call","");   
    
    gxtMessageBox.addDialogHideHandler(new DialogHideHandler() {
      @Override
      public void onDialogHide(DialogHideEvent event) {
			  gxtSendButton.setEnabled(true);
  		  gxtSendButton.focus();
      }
    });

		
		// Create a handler for the sendButton and nameField
    /**
     * 
     * @author alingnau
     * An inner class that will handle the send button request.
     */
		class MyHandler implements SelectHandler {
			@Override
			public void onSelect(SelectEvent event) {
				requestAgeFromServer();
			}

			private void requestAgeFromServer() {
				String textToServer = nameField.getText();

				gxtSendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				dbService.dbServer(textToServer, new AsyncCallback<String>() {
					public void onFailure(Throwable caught) {
						// Show the RPC error message to the user
						gxtMessageBox.setMessage("Remote Procedure Call - Failure");
						serverResponseLabel.addStyleName("serverResponseLabelError");
						serverResponseLabel.setHTML(SERVER_ERROR);
						gxtMessageBox.center();
						gxtMessageBox.show();
					}

					public void onSuccess(String result) {
						gxtMessageBox.setMessage(result);
						gxtMessageBox.center();
						gxtMessageBox.show();
					}
				});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		gxtSendButton.addSelectHandler(handler);
		
	}

}
