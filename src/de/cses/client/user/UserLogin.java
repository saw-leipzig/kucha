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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.Header;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextField;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public class UserLogin extends SimpleContainer {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private static UserLogin instance = null;
	private UserEntry user;
	private TextButton loginButton, logoutButton;
	private TextField usernameField;
	private PasswordField passwordField;
	private HorizontalLayoutContainer loginView, userView;
	private Header loginHeadline, headline;

	/**
	 * 
	 */
	private UserLogin() {
		initLoginView();
		initUserView();
		add(loginView);
	}

	public static synchronized UserLogin getInstance() {
		if (instance == null) {
			instance = new UserLogin();
		}
		return instance;
	}

	private void checkLogin() {
		dbService.userLogin(usernameField.getValue(), cryptWithMD5(passwordField.getValue()), new AsyncCallback<UserEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				Util.showWarning("Login Message", "A problem occurred during login.\n Please check username / password!");
				passwordField.reset();
			}

			@Override
			public void onSuccess(UserEntry result) {
				setUser(result);
			}
		});
	}

	private void setUser(UserEntry entry) {
		user = entry;
		loginView.removeFromParent();
    headline.setHTML("<h1>Hello " + user.getFirstname() + " " + user.getLastname() + "! Welcome to the Kucha Information System!</h1>");
		add(userView);
	}

	private void logout() {
		usernameField.setValue(user.getUsername());
		passwordField.reset();
		userView.removeFromParent();
		user = null;
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
				checkLogin();
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
		userView.add(headline, new HorizontalLayoutData(1.0, 1.0, new Margins(5)));
		userView.add(logoutButton, new HorizontalLayoutData(50.0, 30.0, new Margins(5)));
	}

	public int getAccessRights() {
		return (user == null ? 0 : user.getAccessrights());
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

}
