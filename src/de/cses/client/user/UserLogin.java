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

import org.cyberneko.html.HTMLScanner.CurrentEntity;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
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
public class UserLogin extends PopupPanel {

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public static final String SESSION_ID = "sessionID";
	public static final String USERNAME = "username";

	private static UserLogin instance = null;
	private TextField usernameField;
	private PasswordField passwordField;
//	private String username;
	private UserEntry currentUser = null;

	/**
	 * 
	 */
	private UserLogin() {
		String localSessionID = Cookies.getCookie(SESSION_ID);
		String username = Cookies.getCookie(USERNAME);
		usernameField = new TextField();
		usernameField.setEmptyText("username");
		passwordField = new PasswordField();
		passwordField.setEmptyText("password");
		if (localSessionID != null) {
			checkIfLoggedIn(localSessionID, username);
		}
		setModal(true);
		setGlassEnabled(true);
	}

	public static synchronized UserLogin getInstance() {
		if (instance == null) {
			instance = new UserLogin();
		}
		return instance;
	}
	
	private void login() {
		String username = usernameField.getValue().toLowerCase();
		dbService.userLogin(username, cryptWithMD5(passwordField.getValue()), new AsyncCallback<UserEntry>() {

			@Override
			public void onFailure(Throwable caught) {
				Util.showWarning("Login Message", "A problem occurred during login.\n Please check username / password!");
				passwordField.reset();
			}

			@Override
			public void onSuccess(UserEntry result) { // we get the sessionID
				if (result != null) {
					Cookies.setCookie(SESSION_ID, result.getSessionID());
					Cookies.setCookie(USERNAME, result.getUsername());
					currentUser = result;
					hide();
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
	private void checkIfLoggedIn(String sessionID, String username) {
		dbService.checkSessionID(sessionID, username, new AsyncCallback<UserEntry>() {

			@Override
			public void onFailure(Throwable caught) {
			}

			@Override
			public void onSuccess(UserEntry result) { // we get the current user
				if (result != null) {
					currentUser = result;
				}
			}
		});
	}

	private void logout() {
		Cookies.removeCookie(SESSION_ID);
		Cookies.removeCookie(USERNAME);
		usernameField.setValue(currentUser.getUsername());
		passwordField.reset();
		currentUser = null;
		hide();
	}

	private void showLoginView() {
		VerticalLayoutContainer loginView = new VerticalLayoutContainer();
		TextButton loginButton = new TextButton("login");
		loginButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				login();
			}
		});
		loginView.add(usernameField, new VerticalLayoutData(1.0, .4, new Margins(5)));
		loginView.add(passwordField, new VerticalLayoutData(1.0, .4, new Margins(5)));
		FramedPanel loginFP = new FramedPanel();
		loginFP.setHeading("Login");
		loginFP.add(loginView);
		loginFP.add(loginView);
		loginFP.addButton(loginButton);
		clear();
		add(loginFP);
		super.setSize("300px", "150px");
		super.center();
	}

	private void showUserView() { // all Information about the user and the possibility to change it
		VerticalLayoutContainer userView = new VerticalLayoutContainer();
		PasswordField passwordField = new PasswordField();
		
		Label usernameLabel = new Label(currentUser.getUsername());
		Label firstnameLabel = new Label(currentUser.getFirstname());
		Label lastnameLabel = new Label(currentUser.getLastname());
		TextField emailTF = new TextField();
		emailTF.setText(currentUser.getEmail());
		emailTF.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentUser.setEmail(event.getValue()!=null ? event.getValue() : "");
			}
		});
		
		TextButton updateButton = new TextButton("update");
		updateButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbService.updateUserEntry(currentUser, new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							hide();
						}
					}
				});
			}
		});
		
		TextButton logoutButton = new TextButton("logout");
		logoutButton.addSelectHandler(new SelectHandler() {

			@Override
			public void onSelect(SelectEvent event) {
				logout();
			}
		});
		
		userView.add(new FieldLabel(usernameLabel, "You are logged in as"), new VerticalLayoutData(1.0, .2, new Margins(5)));
		userView.add(new FieldLabel(firstnameLabel, "First name"), new VerticalLayoutData(1.0, .2, new Margins(5)));
		userView.add(new FieldLabel(lastnameLabel, "Last name"), new VerticalLayoutData(1.0, .2, new Margins(5)));
		userView.add(new FieldLabel(emailTF, "E-Mail"), new VerticalLayoutData(1.0, .2, new Margins(5)));
		userView.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1.0, .2, new Margins(5)));
		FramedPanel userFP = new FramedPanel();
		userFP.setHeading("User Information");
		userFP.add(userView);
		userFP.addButton(logoutButton);
		clear();
		add(userFP);
		super.setSize("300px", "450px");
		super.center();
	}
	
	@Override
	public void center() {
		if (Cookies.getCookie(SESSION_ID) != null) {
			showUserView();
		} else {
			showLoginView();
		}
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

	public String getUsername() {
		return currentUser.getUsername();
	}

}
