package com.palash.sampleapp.entiry;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ELLogin {
    public String UserId;
    public String LoginName;
    public String Password;
    public String FullName;
    public String RememberMe;
    public String LoginStatus;

    @JsonProperty("UserId")
    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    @JsonProperty("LoginName")
    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    @JsonProperty("Password")
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @JsonProperty("FullName")
    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    @JsonProperty("RememberMe")
    public String getRememberMe() {
        return RememberMe;
    }

    public void setRememberMe(String rememberMe) {
        RememberMe = rememberMe;
    }

    @JsonProperty("LoginStatus")
    public String getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        LoginStatus = loginStatus;
    }
}
