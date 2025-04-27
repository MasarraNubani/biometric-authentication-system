package com.example.authenticationproject;


public class AuthenticationResult {
    public void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    boolean isAuthenticated;
    String matchedUserID;

    public AuthenticationResult(boolean isAuthenticated, String matchedUserID) {
        this.isAuthenticated = isAuthenticated;
        this.matchedUserID = matchedUserID;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public String getMatchedUserID() {
        return matchedUserID;
    }
}
