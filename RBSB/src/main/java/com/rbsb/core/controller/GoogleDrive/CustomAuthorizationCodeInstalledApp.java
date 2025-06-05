package com.rbsb.core.controller.GoogleDrive;

import java.io.IOException;


import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;

/**
 * Custom AuthorizationCodeInstalledApp to automatically open and close the OAuth URL in the browser.
 */
public class CustomAuthorizationCodeInstalledApp extends AuthorizationCodeInstalledApp {

    public CustomAuthorizationCodeInstalledApp(GoogleAuthorizationCodeFlow flow, VerificationCodeReceiver receiver) {
        super(flow, receiver);
    }
    
    @Override
    public Credential authorize(String userId) throws IOException {
        try {
          Credential credential = getFlow().loadCredential(userId);
          if (credential != null && credential.getAccessToken()!=null
              && (credential.getRefreshToken() != null
                  || credential.getExpiresInSeconds() == null
                  || credential.getExpiresInSeconds() > 60)) {
            return credential;
          }
          String redirectUri = getReceiver().getRedirectUri();
          AuthorizationCodeRequestUrl authorizationUrl =
              getFlow().newAuthorizationUrl().setRedirectUri(redirectUri);
          onAuthorization(authorizationUrl);
         
          String code = getReceiver().waitForCode();
          TokenResponse response = getFlow().newTokenRequest(code).setRedirectUri(redirectUri).execute();
       
          return getFlow().createAndStoreCredential(response, userId);
        } finally {
          getReceiver().stop();
        }
      }
  @Override
    protected void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        // Override this method to automatically open the URL in the browser.
    	
        String url = authorizationUrl.build();
        System.out.println("Attempting to open the following URL in the browser: " + url);

    
      try {
        
          if (System.getProperty("os.name").toLowerCase().contains("win")) {
              Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
             
          } else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
              Runtime.getRuntime().exec("open " + url);
          } else if (System.getProperty("os.name").toLowerCase().contains("nix") ||
                     System.getProperty("os.name").toLowerCase().contains("nux")) {
              Runtime.getRuntime().exec("xdg-open " + url);
          } else {
              throw new UnsupportedOperationException("Platform not supported.");
          }
       } catch (Exception e) {
          System.err.println("Failed to open the browser automatically. Please open the URL manually: " + url);
      }
      
    }
}


