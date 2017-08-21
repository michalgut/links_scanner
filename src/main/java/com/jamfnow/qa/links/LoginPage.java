package com.jamfnow.qa.links;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginPage {

    @JsonProperty("user-selector")
    private String userSelector;
    @JsonProperty("user")
    private String user;
    @JsonProperty("password-selector")
    private String passwordSelector;
    @JsonProperty("password")
    private String password;

    public String getUserSelector() {
        return userSelector;
    }

    public void setUserSelector(final String userSelector) {
        this.userSelector = userSelector;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPasswordSelector() {
        return passwordSelector;
    }

    public void setPasswordSelector(final String passwordSelector) {
        this.passwordSelector = passwordSelector;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
