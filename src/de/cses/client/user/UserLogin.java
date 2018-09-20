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
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.widget.core.client.FramedPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.FieldLabel;
import com.sencha.gxt.widget.core.client.form.PasswordField;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.form.TextField;
import com.sencha.gxt.widget.core.client.form.Validator;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.RegExValidator;

import de.cses.client.DatabaseService;
import de.cses.client.DatabaseServiceAsync;
import de.cses.client.Util;
import de.cses.shared.UserEntry;

/**
 * @author alingnau
 *
 */
public class UserLogin extends PopupPanel {
	
	interface UserInformationTemplate extends XTemplates {
		@XTemplate("<div style='font: 12px tahoma,arial,verdana,sans-serif;'>You are logged in as {fullname}<br>Username: {username}<br>Password is only needed when updating information!</div>")
		SafeHtml userLabel(String fullname, String username);
	}

	private final DatabaseServiceAsync dbService = GWT.create(DatabaseService.class);
	public static final String SESSION_ID = "sessionID";
	public static final String USERNAME = "username";

	private static UserLogin instance = null;
	private TextField usernameField;
	private PasswordField passwordField;
	private UserEntry currentUser = null;
	private UserInformationTemplate uiTemplate;

	/**
	 * 
	 */
	private UserLogin() {
		String localSessionID = Cookies.getCookie(SESSION_ID);
		String username = Cookies.getCookie(USERNAME);
		usernameField = new TextField();
		usernameField.setEmptyText("username or email");
		usernameField.setWidth(200);
		passwordField = new PasswordField();
		passwordField.setEmptyText("password");
		passwordField.setWidth(200);
		if (localSessionID != null) {
			checkIfLoggedIn(localSessionID, username);
		}
		uiTemplate = GWT.create(UserInformationTemplate.class);
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
				} else {
					Cookies.removeCookie(SESSION_ID);
					Cookies.removeCookie(USERNAME);
					currentUser = null;
					usernameField.reset();
					passwordField.reset();
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
				clear();
			}
		});
		loginView.add(new HTML("<div style='font: 11px tahoma,arial,verdana,sans-serif;'>You can now also use your email to log in!</div>"), new VerticalLayoutData(1.0, .3));
		loginView.add(new FieldLabel(usernameField, "Login name"), new VerticalLayoutData(1.0, .35));
		loginView.add(new FieldLabel(passwordField, "Password"), new VerticalLayoutData(1.0, .35));
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
		
		PasswordField passwordField = new PasswordField();
		passwordField.setWidth(200);
		FramedPanel passwordFP = new FramedPanel();
		passwordFP.setHeading("Password (for update/change password only)");
		passwordFP.add(passwordField);
		
		TextField emailTF = new TextField();
		emailTF.setWidth(300);
		emailTF.setValue(currentUser.getEmail());
		emailTF.addValueChangeHandler(new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentUser.setEmail(event.getValue()!=null ? event.getValue() : "");
			}
		});
		FramedPanel emailFP = new FramedPanel();
		emailFP.setHeading("E-Mail");
		emailFP.add(emailTF);
		
		TextArea affiliationTA = new TextArea();
		affiliationTA.setWidth(300);
		affiliationTA.setValue(currentUser.getAffiliation());
		affiliationTA.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				currentUser.setAffiliation(event.getValue()!=null ? event.getValue() : "");
			}
		});
		FramedPanel affiliationFP = new FramedPanel();
		affiliationFP.setHeading("Affiliation");
		affiliationFP.add(affiliationTA);
		
		PasswordField newPassword, retypeNewPassword;
		newPassword = new PasswordField();
		newPassword.addValidator(new Validator<String>() {
			
			@Override
			public List<EditorError> validate(Editor<String> editor, String value) {
				List<EditorError> l = new ArrayList<EditorError>();
				if (!newPassword.getCurrentValue().isEmpty() && (newPassword.getValue().length() < 8)) {
					l.add(new DefaultEditorError(editor, "the new password needs at least 8 characters", value));
				}
				return l;
			}
		});
		newPassword.addValidator(new RegExValidator("([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*", "please use at least one number and one character"));
		newPassword.setEmptyText("type in new password");
		retypeNewPassword = new PasswordField();
		retypeNewPassword.setEmptyText("retype new password");
		retypeNewPassword.addValidator(new Validator<String>() {
			
			@Override
			public List<EditorError> validate(Editor<String> editor, String value) {
				List<EditorError> l = new ArrayList<EditorError>();
				if (!value.equals(newPassword.getCurrentValue())) {
					l.add(new DefaultEditorError(editor, "not matching", value));
				}
				return l;
			}
		});
		retypeNewPassword.setAutoValidate(true);
		VerticalLayoutContainer changePasswordVLC = new VerticalLayoutContainer();
		changePasswordVLC.add(newPassword, new VerticalLayoutData(1.0, .5));
		changePasswordVLC.add(retypeNewPassword, new VerticalLayoutData(1.0, .5));
		FramedPanel changePasswordFP = new FramedPanel();
		changePasswordFP.setHeading("Change password (optional)");
		changePasswordFP.add(changePasswordVLC);
		
		TextButton updateButton = new TextButton("update");
		updateButton.addSelectHandler(new SelectHandler() {
			
			@Override
			public void onSelect(SelectEvent event) {
				dbService.updateUserEntry(currentUser, cryptWithMD5(passwordField.getValue()), cryptWithMD5(newPassword.getValue()), new AsyncCallback<Boolean>() {

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
				clear();
			}
		});
		
		VerticalLayoutContainer userVL = new VerticalLayoutContainer();
		userVL.add(new HTML(uiTemplate.userLabel(currentUser.getFirstname() + " " + currentUser.getLastname(), currentUser.getUsername())), new VerticalLayoutData(1.0, .2));
		userVL.add(emailFP, new VerticalLayoutData(1.0, .14));
		userVL.add(affiliationFP, new VerticalLayoutData(1.0, .3));
		userVL.add(changePasswordFP, new VerticalLayoutData(1.0, .22));
		userVL.add(passwordFP, new VerticalLayoutData(1.0, .14));
		HorizontalLayoutContainer userHL = new HorizontalLayoutContainer();
		userHL.add(userVL, new HorizontalLayoutData(1.0, 1.0));
		FramedPanel userFP = new FramedPanel();
		userFP.setHeading("User Information");
		userFP.add(userHL);
		userFP.addButton(updateButton);
		userFP.addButton(logoutButton);
		userFP.addTool(closeTB);
		userFP.setHeight(500);
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
