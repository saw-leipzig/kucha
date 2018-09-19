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
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.FormPanel.LabelAlign;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextArea;
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
		usernameField.setWidth(200);
		passwordField = new PasswordField();
		passwordField.setEmptyText("password");
		passwordField.setWidth(200);
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
					clear();
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
				Util.doLogging("checkSessionID failed!");
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
		clear();
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
		ToolButton closeTB = new ToolButton(ToolButton.CLOSE);
		closeTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		loginView.add(new FieldLabel(usernameField, "Login name"), new VerticalLayoutData(1.0, .5));
		loginView.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1.0, .5));
		FramedPanel loginFP = new FramedPanel();
		loginFP.setHeading("Login");
		loginFP.add(loginView);
		loginFP.addButton(loginButton);
		loginFP.addTool(closeTB);
		add(loginFP);
//		super.setSize("300px", "200px");
		super.center();
	}

	private void showUserView() { // all Information about the user and the possibility to change it
		VerticalLayoutContainer userView = new VerticalLayoutContainer();
		PasswordField passwordField = new PasswordField();
		passwordField.setWidth(200);
		
		TextField emailTF = new TextField();
		emailTF.setWidth(300);
		emailTF.setValue(currentUser.getEmail());
		emailTF.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentUser.setEmail(event.getValue()!=null ? event.getValue() : "");
			}
		});
		
		TextArea affiliationTA = new TextArea();
		affiliationTA.setWidth(300);
		affiliationTA.setValue(currentUser.getAffiliation());
		affiliationTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentUser.setAffiliation(event.getValue()!=null ? event.getValue() : "");
			}
		});
		
		TextButton updateButton = new TextButton("update");
		updateButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbService.updateUserEntry(currentUser, cryptWithMD5(passwordField.getValue()), new AsyncCallback<Boolean>() {

					@Override
					public void onFailure(Throwable caught) {
						Util.doLogging("Updating user information failed!");
					}

					@Override
					public void onSuccess(Boolean result) {
						if (result) {
							hide();
							clear();
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
		ToolButton closeTB = new ToolButton(ToolButton.CLOSE);
		closeTB.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				hide();
			}
		});
		
		userView.add(new Label("You are logged in as" + currentUser.getFirstname() + " " + currentUser.getLastname() + " (" + currentUser.getUsername() + ")"), new VerticalLayoutData(1.0, .15));
		userView.add(new FieldLabel(emailTF, "E-Mail"), new VerticalLayoutData(1.0, .15));
		userView.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1.0, .15));
		FieldLabel affiliationFL = new FieldLabel(affiliationTA, "Affiliation");
		affiliationFL.setLabelAlign(LabelAlign.TOP);
		userView.add(affiliationFL, new VerticalLayoutData(1.0, .55));
		FramedPanel userFP = new FramedPanel();
		userFP.setHeading("User Information");
		userFP.add(userView);
		userFP.addButton(updateButton);
		userFP.addButton(logoutButton);
		userFP.addTool(closeTB);
		add(userFP);
//		super.setSize("300px", "250px");
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
		return currentUser != null ? currentUser.getUsername() : "";
	}

}
