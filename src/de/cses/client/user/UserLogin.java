/*
 * Copyright 2017 
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
package de.cses.client.user;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;

/**
 * @author alingnau
 *
 */
public class UserLogin extends SimpleContainer {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	private static final String SESSION_ID = "sessionID";

	private static UserLogin instance = null;
	private TextButton loginButton, logoutButton;
	private TextField usernameField;
	private PasswordField passwordField;
	private HorizontalLayoutContainer loginView, userView;
	private Header loginHeadline, headline;
	private String username;

	/**
	 * 
	 */
	private UserLogin() {
		initLoginView();
		initUserView();
		add(loginView);
		String localSessionID = Cookies.getCookie(SESSION_ID);
		if (localSessionID != null) {
			checkIfLoggedIn(localSessionID);
		}
	}

	public static synchronized UserLogin getInstance() {
		if (instance == null) {
			instance = new UserLogin();
		}
		return instance;
	}
	
	private void login() {
		username = usernameField.getValue().toLowerCase();
		dbService.userLogin(username, cryptWithMD5(passwordField.getValue()), new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Util.showWarning("Login Message", "A problem occurred during login.\n Please check username / password!");
				passwordField.reset();
			}

			@Override
			public void onSuccess(String result) { // we get the sessionID
				if (result != null) {
					loginView.removeFromParent();
					Cookies.setCookie(SESSION_ID, result);
					logoutButton.setText("logout " + username);
			    headline.setHTML("<h1>Welcome to the Kucha Information System! You are logged in!</h1>");
					add(userView);
				} else {
					Util.showWarning("Login Message", "Login error! Please check username / password!");
					usernameField.reset();
					passwordField.reset();
				}
			}
		});
	}
	
	/**
	 * 
	 */
	private void checkIfLoggedIn(String sessionID) {
		dbService.checkSessionID(sessionID, new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(String result) { // we get the username
				if (username != null) {
					username = result;
					loginView.removeFromParent();
					logoutButton.setText("logout " + username);
			    headline.setHTML("<h1>Welcome to the Kucha Information System! You are logged in!</h1>");
					add(userView);
				}
			}
		});
	}

	private void logout() {
		Cookies.removeCookie(SESSION_ID);
		usernameField.setValue(username);
		passwordField.reset();
		userView.removeFromParent();
		add(loginView);
	}

	private void initLoginView() {
		loginHeadline = new Header();
		loginHeadline.setHTML("<h1>Welcome to the Kucha Information System</h1>");
		loginView = new HorizontalLayoutContainer();
		usernameField = new TextField();
		usernameField.setEmptyText("username");
		passwordField = new PasswordField();
		passwordField.setEmptyText("password");
		loginButton = new TextButton("login");
		loginButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				login();
			}
		});
		loginView.add(loginHeadline, new HorizontalLayoutData(1.0, 1.0, new Margins(5)));
		loginView.add(usernameField, new HorizontalLayoutData(120.0, 1.0, new Margins(5)));
		loginView.add(passwordField, new HorizontalLayoutData(120.0, 1.0, new Margins(5)));
		loginView.add(loginButton, new HorizontalLayoutData(50.0, 30.0, new Margins(5)));
	}

	private void initUserView() {
		headline = new Header();
		userView = new HorizontalLayoutContainer();

		logoutButton = new TextButton("logout");
		logoutButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				logout();
			}
		});
		
    headline.setHTML("<h1>Welcome to the Kucha Information System</h1>");
		userView.add(headline, new HorizontalLayoutData(.9, 1.0, new Margins(5)));
		userView.add(logoutButton, new HorizontalLayoutData(.1, 1.0, new Margins(5)));
	}
	
	private static String cryptWithMD5(String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] passBytes = pass.getBytes();
			md.reset();
			byte[] digested = md.digest(passBytes);
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < digested.length; i++) {
				sb.append(Integer.toHexString(0xff & digested[i]));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		}
		return null;

	}

	public String getUsernameSessionIDParameterForUri() {
		return "&sessionID=" + Cookies.getCookie(SESSION_ID);
	}

	/**
	 * @return
	 */
	public static boolean isLoggedIn() {
		return (Cookies.getCookie(SESSION_ID) != null);
	}

}
