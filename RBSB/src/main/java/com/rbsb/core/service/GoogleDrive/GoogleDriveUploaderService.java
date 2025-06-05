package com.rbsb.core.service.GoogleDrive;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.FileContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.rbsb.core.controller.GoogleDrive.CustomAuthorizationCodeInstalledApp;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@PropertySource("classpath:common.properties")
public class GoogleDriveUploaderService {

    @Value("${oauth_configuration_path}")
    private String serviceAccountFilePath;

    @Value("${oauth_token_directory_path}")
    private String clientSecretStoreTokenPath;

    private static final String APPLICATION_NAME = "FoodiesApp";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    private Credential getCredentials() throws IOException, GeneralSecurityException {
        String credentialsFilePath = serviceAccountFilePath + "credentials.json";
        InputStream in = new FileInputStream(credentialsFilePath);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(clientSecretStoreTokenPath)))
                .setAccessType("offline") 
                .setApprovalPrompt("force")// Important for refresh token
                .build();

        Credential credential = flow.loadCredential("user");

        if (credential == null) {
            // First-time authorization - only once needed
            log.info("No valid credentials found. Initiating first-time authorization.");

            LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                    .setHost("localhost")
                    .setPort(8889)
                    .build();

            //credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            credential = new CustomAuthorizationCodeInstalledApp(flow, receiver).authorize("user");
            log.info("Authorization successful. Access Token: {}", credential.getAccessToken());
        } else {
            // If expired, silently refresh
            if (credential.getAccessToken() == null || credential.getExpiresInSeconds() <= 60) {
                boolean refreshed = credential.refreshToken();
                if (refreshed) {
                    log.info("Access token refreshed successfully.");
                } else {
                    log.warn("Failed to refresh token. Manual authorization may be required.");
                }
            }
        }

        return credential;
    }

    public String uploadFile(MultipartFile file) {
        try {
            // 1. Convert MultipartFile to java.io.File (temporary file)
            java.io.File tempFile = java.io.File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            // 2. Initialize the Drive service
            Drive service = new Drive.Builder(GoogleNetHttpTransport.newTrustedTransport(), JSON_FACTORY, getCredentials())
                    .setApplicationName(APPLICATION_NAME)
                    .build();

            
            File fileMetadata = new File();
            fileMetadata.setName(file.getOriginalFilename());

            FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
            File uploadedFile = service.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            String fileId = uploadedFile.getId();

            // 4. Make public to the user
            Permission permission = new Permission()
                    .setType("anyone")
                    .setRole("reader");
            service.permissions().create(fileId, permission).execute();

            
         // 5. Generate view link
            String fileLink = "https://drive.google.com/file/d/" + fileId + "/view";
            log.info("File uploaded successfully. Link: {}", fileLink);

            // 6. Clean up temp file
            tempFile.delete();

            return fileLink;
        } catch (Exception e) {
            log.error("Error uploading file", e);
            return null;
        }
    }
}
