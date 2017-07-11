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
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
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
public class UserLogin extends ContentPanel {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);

	private static UserLogin instance = null;
	private UserEntry user;
	private TextButton loginButton, logoutButton;
	private TextField usernameField;
	private PasswordField passwordField;
	private FramedPanel loginPanel, userPanel;

	private FieldLabel userDisplay;

	/**
	 * 
	 */
	private UserLogin() {
		this.setSize("250", "80");
		initLoginView();
		initUserView();
		this.add(loginPanel);
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
		userDisplay.setText(user.getFirstname() + " " + user.getLastname());
		loginPanel.removeFromParent();
		this.add(userPanel);
	}

	private void logout() {
		usernameField.setValue(user.getUsername());
		passwordField.reset();
		userPanel.removeFromParent();
		user = null;
		userDisplay.setText("");
		this.add(loginPanel);
	}

	private void initLoginView() {
		loginPanel = new FramedPanel();
		loginPanel.setHeading("Please enter ...");
		HorizontalLayoutContainer vlc = new HorizontalLayoutContainer();
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
		vlc.add(new FieldLabel(usernameField, "username: "), new HorizontalLayoutData(.4, .8));
		vlc.add(new FieldLabel(passwordField, "password: "), new HorizontalLayoutData(.4, .8));
		vlc.add(loginButton, new HorizontalLayoutData(.2, 1.0));
		loginPanel.add(vlc);
	}

	private void initUserView() {
		userPanel = new FramedPanel();
		userPanel.setHeading("Welcome");
		logoutButton = new TextButton("logout");
		logoutButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				logout();
			}
		});
		userDisplay = new FieldLabel(logoutButton, "");
		userPanel.add(userDisplay, new MarginData(5));
	}

	public int getAccessRights() {
		return (user == null ? 0 : user.getAccessrights());
	}

	public static String cryptWithMD5(String pass) {
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
